package com.lineage.server.model.Instance;

import static com.lineage.server.model.skill.L1SkillId.GREATER_HASTE;
import static com.lineage.server.model.skill.L1SkillId.HASTE;
import static com.lineage.server.model.skill.L1SkillId.STATUS_HASTE;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigOther;
import com.lineage.server.ActionCodes;
import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.doll.Doll_DmgDownR;
import com.lineage.server.model.doll.Doll_DmgR;
import com.lineage.server.model.doll.Doll_Skill;
import com.lineage.server.model.doll.L1DollExecutor;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_NPCPack_Doll;
import com.lineage.server.serverpackets.S_MoveCharPacket;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_SkillBrave;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.templates.L1Doll;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.World;

/**
 * 对象:魔法娃娃 控制项
 * 
 * @author DaiEn
 * 
 */
public class L1DollInstance extends L1NpcInstance {

    private static final long serialVersionUID = 1L;

    private static final Log _log = LogFactory.getLog(L1DollInstance.class);

    private static Random _random = new Random();

    private int _itemObjId;

    private boolean _power_doll = false;// 该娃娃具备辅助技能效果

    private L1Doll _type;

    /**
     * 对象:魔法娃娃
     * 
     * @param template
     * @param master
     * @param dollType
     * @param itemObjId
     */
    public L1DollInstance(final L1Npc template, final L1PcInstance master,
            final int itemObjId, final L1Doll type) {
        super(template);
        try {
            setId(IdFactoryNpc.get().nextId());
            // 设置副本编号
            set_showId(master.get_showId());

            setItemObjId(itemObjId);

            _type = type;
            setGfxId(type.get_gfxid());
            setTempCharGfx(type.get_gfxid());
            setNameId(type.get_nameid());
            set_time(type.get_time());

            setMaster(master);
            setX(master.getX() + _random.nextInt(5) - 2);
            setY(master.getY() + _random.nextInt(5) - 2);
            setMap(master.getMapId());
            setHeading(5);
            setLightSize(template.getLightSize());

            World.get().storeObject(this);
            World.get().addVisibleObject(this);
            for (final L1PcInstance pc : World.get().getRecognizePlayer(this)) {
                onPerceive(pc);
            }
            master.addDoll(this);

            L1PcInstance masterpc = null;
            if (_master instanceof L1PcInstance) {
                masterpc = (L1PcInstance) _master;
                // 设置能力
                if (!_type.getPowerList().isEmpty()) {
                    for (L1DollExecutor p : _type.getPowerList()) {
                        if (p instanceof Doll_Skill) {
                            final Doll_Skill vv = (Doll_Skill) p;
                            set_skill(vv.get_int()[0], vv.get_int()[1]);

                        } else {
                            p.setDoll(masterpc);
                        }
                        if (p.is_reset()) {
                            _power_doll = true;
                        }
                    }
                }
                master.sendPackets(new S_PacketBox(S_PacketBox.DOLL, type
                        .get_time()));
            }

            // 娃娃登场
            broadcastPacketX10(new S_SkillSound(getId(), 5935));
            set_olX(getX());
            set_olY(getY());

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 娃娃收回
     */
    public void deleteDoll() {
        try {
            // 娃娃收回
            broadcastPacketX10(new S_SkillSound(getId(), 5936));

            L1PcInstance masterpc = null;
            if (_master instanceof L1PcInstance) {
                masterpc = (L1PcInstance) _master;
                // 移除能力
                if (!_type.getPowerList().isEmpty()) {
                    for (L1DollExecutor p : _type.getPowerList()) {
                        p.removeDoll(masterpc);
                    }
                }
                masterpc.sendPackets(new S_PacketBox(S_PacketBox.DOLL, 0));
            }

            _master.removeDoll(this);
            deleteMe();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * TODO 接触资讯
     */
    @Override
    public void onPerceive(final L1PcInstance perceivedFrom) {
        try {
            // 副本ID不相等 不相护显示
            if (perceivedFrom.get_showId() != get_showId()) {
                return;
            }
            perceivedFrom.addKnownObject(this);
            perceivedFrom.sendPackets(new S_NPCPack_Doll(this, perceivedFrom));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 跟随主人变更移动/速度状态
     */
    public void setNpcMoveSpeed() {
        try {
            if (!ConfigOther.WAR_DOLL) {
                for (int castle_id = 1; castle_id < 8; castle_id++) {
                    if (ServerWarExecutor.get().isNowWar(castle_id)) { // 攻城战期间内
                        if (L1CastleLocation.checkInWarArea(castle_id, this)) {
                            deleteDoll();
                            return;
                        }
                    }
                }
            }

            // 主人隐身或解除则魔法娃娃消失
            if (_master != null && _master.isInvisble()) {
                deleteDoll();
                return;
            }
            // 主人隐身或解除则魔法娃娃消失
            if (_master.isDead()) {
                deleteDoll();
                return;
            }
            if (_master.getMoveSpeed() != _srcMoveSpeed) {
                set_srcMoveSpeed(_master.getMoveSpeed());
                setMoveSpeed(_srcMoveSpeed);
            }

            if (_master.getBraveSpeed() != _srcBraveSpeed) {
                set_srcBraveSpeed(_master.getBraveSpeed());
                setBraveSpeed(_srcBraveSpeed);
            }

            if ((_master != null) && (_master.getMapId() == getMapId())) {
                if (getLocation().getTileLineDistance(_master.getLocation()) > 2) {
                    final int dir = this.targetDirection(_master.getX(),
                            _master.getY());
                    this.setDirectionMoveSrc(dir);
                    // 发送移动封包
                    broadcastPacketAll(new S_MoveCharPacket(this));
                }

            } else {
                deleteDoll();
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private int _srcMoveSpeed; // 移动加速状态(绿水)
    private int _srcBraveSpeed; // 攻击加速状态(勇水)

    /**
     * 移动加速状态(绿水)
     * 
     * @param srcMoveSpeed
     */
    private void set_srcMoveSpeed(int srcMoveSpeed) {
        try {
            _srcMoveSpeed = srcMoveSpeed;
            broadcastPacketAll(new S_SkillHaste(getId(), _srcMoveSpeed, 0));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 攻击加速状态(勇水)
     * 
     * @param srcBraveSpeed
     */
    private void set_srcBraveSpeed(int srcBraveSpeed) {
        try {
            _srcBraveSpeed = srcBraveSpeed;
            broadcastPacketAll(new S_SkillBrave(getId(), _srcBraveSpeed, 0));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public int getItemObjId() {
        return _itemObjId;
    }

    public void setItemObjId(final int i) {
        _itemObjId = i;
    }

    private int _time = 0;

    /**
     * 设置剩余使用时间
     * 
     * @return
     */
    public int get_time() {
        return _time;
    }

    /**
     * 剩余使用时间
     * 
     * @param time
     */
    public void set_time(final int time) {
        _time = time;
    }

    /**
     * 魔法娃娃特殊技能
     * 
     * @param target
     * @param dmg
     */
    public void startDollSkill(final L1Character target, final double dmg) {
        try {
            if (_skillid != -1) {
                if (_random.nextInt(1000) <= _r && _master.getLevel() > target.getLevel()) {
                    final L1Magic magic = new L1Magic(_master, target);
                    final int magic_dmg = magic.calcMagicDamage(_skillid);
                    magic.commit(magic_dmg, 0);
                    final L1Skills skill = SkillsTable.get().getTemplate(
                            _skillid);
                    final int castgfx = skill.getCastGfx();

                    target.broadcastPacketX10(new S_SkillSound(target.getId(),
                            castgfx));
                    if (target instanceof L1PcInstance) {
                        final L1PcInstance pc = (L1PcInstance) target;
                        pc.sendPackets(new S_SkillSound(pc.getId(), castgfx));
                    }
                    switch (_skillid) {
                        case 4:// 光箭 1100
                        case 6:// 冰箭 1100
                        case 7:// 风刃 1100
                        case 10:// 寒冷战栗 1100
                        case 15:// 火箭 1300
                        case 16:// 地狱之牙 1300
                        case 17:// 极光雷电 1500
                        case 18:// 起死回生术 1500
                        case 22:// 寒冰气息 1300
                        case 25:// 燃烧的火球 1600
                        case 28:// 吸血鬼之吻 1250
                        case 30:// 岩牢 1600
                        case 34:// 极道落雷 1250
                        case 38:// 冰锥 1100
                        case 45:// 地裂术 1100
                        case 46:// 烈炎术 1650
                        case 50:// 冰矛围篱 1600
                        case 58:// 火牢 1100
                        case 65:// 雷霆风暴 3000
                        case 74:// 流星雨 4000
                        case 77:// 究极光裂术 6000
                        case 108:// 会心一击 1800
                        case 132:// 三重矢 1250
                        case 184:// 岩浆喷吐 3300
                        case 187:// 屠宰者 3250
                        case 192:// 夺命之雷 5150
                        case 194:// 寒冰喷吐
                        case 202:// 混乱 4000
                        case 203:// 暴击 3100
                        case 207:// 心灵破坏 4050
                        case 208:// 骷髅毁坏 3100
                        case 213:// 武器破坏者 3100
                            broadcastPacketAll(new S_DoActionGFX(getId(),
                                    ActionCodes.ACTION_Aggress));
                            break;

                        case 29:// 缓速术 1600
                            switch (target.getMoveSpeed()) {
                                case 0:
                                    if (target instanceof L1PcInstance) {
                                        final L1PcInstance pc = (L1PcInstance) target;
                                        pc.sendPackets(new S_SkillHaste(pc
                                                .getId(), 2, skill
                                                .getBuffDuration()));
                                    }
                                    target.broadcastPacketAll(new S_SkillHaste(
                                            target.getId(), 2, skill
                                                    .getBuffDuration()));
                                    target.setMoveSpeed(2);
                                    break;

                                case 1:
                                    int skillNum = 0;
                                    if (target.hasSkillEffect(HASTE)) {
                                        skillNum = HASTE;

                                    } else if (target
                                            .hasSkillEffect(GREATER_HASTE)) {
                                        skillNum = GREATER_HASTE;

                                    } else if (target
                                            .hasSkillEffect(STATUS_HASTE)) {
                                        skillNum = STATUS_HASTE;
                                    }

                                    if (skillNum != 0) {
                                        target.removeSkillEffect(skillNum);
                                        target.setMoveSpeed(0);
                                    }
                                    break;
                            }
                            break;

                        case 1:// 初级治愈术 1300
                        case 8:// 神圣武器 1500
                        case 9:// 解毒术 1250
                        case 11:// 毒咒 1650
                        case 19:// 中级治愈术 1250
                        case 20:// 闇盲咒术 1650
                        case 23:// 能量感测 1100
                        case 26:// 通畅气脉术 1250
                        case 27:// 坏物术 4200
                        case 33:// 木乃伊的诅咒 6200
                        case 35:// 高级治愈术 1500
                        case 36:// 迷魅术 1250
                        case 37:// 圣洁之光 1250
                        case 39:// 魔力夺取 1650
                        case 40:// 黑闇之影 1650
                        case 41:// 造尸术
                        case 42:// 体魄强健术 1650
                        case 43:// 加速术 900
                        case 44:// 魔法相消术 1450
                        case 47:// 弱化术 1650
                        case 48:// 祝福魔法武器 1250
                        case 55:// 狂暴术 1250
                        case 56:// 疾病术 1650
                        case 57:// 全部治愈术 1650
                        case 61:// 返生术
                        case 63:// 治愈能量风暴 1100
                        case 64:// 魔法封印 1650
                        case 66:// 沉睡之雾 2750
                        case 67:// 变形术
                        case 68:// 圣结界 2750
                        case 71:// 药水霜化术 1650
                        case 75:// 终极返生术
                        case 76:// 集体缓速术 4200
                        case 79:// 灵魂升华 1250
                        case 87:// 冲击之晕 9500
                        case 103:// 暗黑盲咒 2700
                        case 113:// 精准目标 1600
                        case 116:// 呼唤盟友
                        case 118:// 援护盟友
                        case 133:// 弱化属性 1250
                        case 145:// 释放元素 1250
                        case 148:// 火焰武器 1450
                        case 149:// 风之神射 1450
                        case 151:// 大地防护 1450
                        case 152:// 地面障碍 1250
                        case 153:// 魔法消除 1250
                        case 157:// 大地屏障 1250
                        case 158:// 生命之泉 1450
                        case 160:// 水之防护 1450
                        case 165:// 生命呼唤 1450
                        case 167:// 风之枷锁 1250
                        case 170:// 水之元气 1450
                        case 173:// 污浊之水 2200
                        case 174:// 精准射击 4250
                        case 188:// 恐惧无助 4250
                        case 193:// 惊悚死神 5150
                        case 204:// 幻觉：欧吉 3150
                        case 206:// 专注 2050
                        case 209:// 幻觉：巫妖 3100
                        case 211:// 耐力 2050
                        case 212:// 幻想 4050
                        case 214:// 幻觉：钻石高仑 3150
                        case 216:// 洞察 2200
                        case 217:// 恐慌 5150
                        case 218:// 疼痛的欢愉 4050
                        case 219:// 幻觉：化身 6100
                            // type = Speed_Ch.ACT_TYPE.SPELL_DIR;
                            break;

                        case 2:// 日光术 1300
                        case 3:// 保护罩 1300
                        case 5:// 指定传送 1100
                        case 12:// 拟似魔法武器 1500
                        case 13:// 无所遁形术 1650
                        case 14:// 负重强化 1250
                        case 21:// 铠甲护持 1500
                        case 31:// 魔法屏障 1250
                        case 32:// 冥想术 1250
                        case 49:// 体力回复术 1650
                        case 51:// 召唤术 2200
                        case 52:// 神圣疾走 1250
                        case 53:// 龙卷风 2500
                        case 54:// 强力加速术
                        case 59:// 冰雪暴 2600
                        case 60:// 隐身术 1250
                        case 62:// 震裂术 2600
                        case 69:// 集体传送术 1200
                        case 70:// 火风暴 3000
                        case 72:// 强力无所遁形术 1650
                        case 73:// 创造魔法武器 1500
                        case 78:// 绝对屏障 13000
                        case 80:// 冰雪飓风 4000
                        case 88:// 增幅防御 4500
                        case 89:// 尖刺盔甲 6450
                        case 90:// 坚固防护 4450
                        case 91:// 反击屏障 4450
                        case 97:// 暗隐术 11700
                        case 98:// 附加剧毒 2750
                        case 99:// 影之防护 2750
                        case 100:// 提炼魔石 2000
                        case 101:// 行走加速 1750
                        case 102:// 燃烧斗志 4700
                        case 104:// 毒性抵抗 1750
                        case 105:// 双重破坏 1750
                        case 106:// 暗影闪避 1750
                        case 107:// 暗影之牙 1700
                        case 109:// 力量提升 1750
                        case 110:// 敏捷提升 1750
                        case 111:// 闪避提升 1750
                        case 114:// 激励士气 1600
                        case 115:// 钢铁士气 1500
                        case 117:// 冲击士气 1500
                        case 129:// 魔法防御 1300
                        case 130:// 心灵转换 1450
                        case 131:// 世界树的呼唤
                        case 134:// 镜反射 350
                        case 137:// 净化精神 1450
                        case 138:// 属性防御 1450
                        case 146:// 魂体转换 2500
                        case 147:// 单属性防御 1450
                        case 150:// 风之疾走 1450
                        case 154:// 召唤属性精灵 1400
                        case 155:// 烈炎气息 1400
                        case 156:// 暴风之眼 1400
                        case 159:// 大地的祝福 1450
                        case 161:// 封印禁地 10300
                        case 162:// 召唤强力属性精灵 1450
                        case 163:// 烈炎武器 1450
                        case 164:// 生命的祝福 1650
                        case 166:// 暴风神射 1450
                        case 168:// 钢铁防护 1450
                        case 169:// 体能激发 1450
                        case 171:// 属性之火 1450
                        case 172:// 暴风疾走 1450
                        case 175:// 烈焰之魂
                        case 176:// 能量激发
                        case 181:// 龙之护铠 1300
                        case 182:// 燃烧击砍 3300
                        case 183:// 护卫毁灭 2200
                        case 185:// 觉醒：安塔瑞斯 1250
                        case 186:// 血之渴望
                        case 189:// 冲击之肤
                        case 190:// 觉醒：法利昂 1250
                        case 191:// 致命身躯 2350
                        case 195:// 觉醒：巴拉卡斯 1250
                        case 201:// 镜像 1250
                        case 205:// 立方：燃烧 6200
                        case 210:// 立方：地裂 6250
                        case 215:// 立方：冲击 6200
                        case 220:// 立方：和谐 6100
                            // type = Speed_Ch.ACT_TYPE.SPELL_NODIR;
                            break;
                    }
                    /*
                     * final L1SkillUse skillUse = new L1SkillUse();
                     * skillUse.handleCommands( null, _skillid, target.getId(),
                     * target.getX(), target.getX(), 0, L1SkillUse.TYPE_GMBUFF,
                     * this );
                     */
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private int _skillid = -1;// 值1

    private int _r = -1;// 值2

    public void set_skill(int int1, int int2) {
        _skillid = int1;// 值1
        if (_skillid != -1) {
            this.setInt(_master.getInt());
        }
        _r = int2;// 值2
    }

    private int _olX = 0;// 上次站的位置X

    public void set_olX(int x) {
        _olX = x;
    }

    public int get_olX() {
        return _olX;
    }

    private int _olY = 0;// 上次站的位置Y

    public void set_olY(int y) {
        _olY = y;
    }

    public int get_olY() {
        return _olY;
    }

    // 辅助类型娃娃
    public void startDollSkill() {
        if (!_type.getPowerList().isEmpty()) {
            // int i = 0;
            if (_master instanceof L1PcInstance) {
                final L1PcInstance masterpc = (L1PcInstance) _master;
                for (L1DollExecutor p : _type.getPowerList()) {
                    if (p.is_reset()) {
                        p.setDoll(masterpc);
                    }
                }
            }
        }
    }

    /**
     * 动作展示
     * 
     * @param i
     *            1:机率增加攻击力 2:机率增加伤害减免
     */
    public void show_action(int i) {
        if (!_type.getPowerList().isEmpty()) {
            for (L1DollExecutor p : _type.getPowerList()) {
                if ((p instanceof Doll_DmgR) && (i == 1)) {
                    broadcastPacketAll(new S_DoActionGFX(getId(),
                            ActionCodes.ACTION_Aggress));
                }
                if ((p instanceof Doll_DmgDownR) && (i == 2)) {
                    broadcastPacketAll(new S_DoActionGFX(getId(),
                            ActionCodes.ACTION_Aggress));
                }
            }
        }
    }

    public boolean is_power_doll() {
        return _power_doll;
    }
}
