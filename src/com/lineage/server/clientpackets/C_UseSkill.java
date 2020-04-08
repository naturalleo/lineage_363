package com.lineage.server.clientpackets;

import static com.lineage.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static com.lineage.server.model.skill.L1SkillId.ADDITIONAL_FIRE;
import static com.lineage.server.model.skill.L1SkillId.AREA_OF_SILENCE;
import static com.lineage.server.model.skill.L1SkillId.AWAKEN_ANTHARAS;
import static com.lineage.server.model.skill.L1SkillId.AWAKEN_FAFURION;
import static com.lineage.server.model.skill.L1SkillId.AWAKEN_VALAKAS;
import static com.lineage.server.model.skill.L1SkillId.BOUNCE_ATTACK;
import static com.lineage.server.model.skill.L1SkillId.COUNTER_BARRIER;
import static com.lineage.server.model.skill.L1SkillId.CURE_POISON;
import static com.lineage.server.model.skill.L1SkillId.DETECTION;
import static com.lineage.server.model.skill.L1SkillId.DRAGON_SKIN;
import static com.lineage.server.model.skill.L1SkillId.ENCHANT_WEAPON;
import static com.lineage.server.model.skill.L1SkillId.HEAL;
import static com.lineage.server.model.skill.L1SkillId.HOLY_WEAPON;
import static com.lineage.server.model.skill.L1SkillId.ILLUSION_AVATAR;
import static com.lineage.server.model.skill.L1SkillId.ILLUSION_DIA_GOLEM;
import static com.lineage.server.model.skill.L1SkillId.ILLUSION_LICH;
import static com.lineage.server.model.skill.L1SkillId.ILLUSION_OGRE;
import static com.lineage.server.model.skill.L1SkillId.INSIGHT;
import static com.lineage.server.model.skill.L1SkillId.LIGHT;
import static com.lineage.server.model.skill.L1SkillId.MEDITATION;
import static com.lineage.server.model.skill.L1SkillId.MIRROR_IMAGE;
import static com.lineage.server.model.skill.L1SkillId.PATIENCE;
import static com.lineage.server.model.skill.L1SkillId.REDUCTION_ARMOR;
import static com.lineage.server.model.skill.L1SkillId.SHIELD;
import static com.lineage.server.model.skill.L1SkillId.SILENCE;
import static com.lineage.server.model.skill.L1SkillId.SOLID_CARRIAGE;
import static com.lineage.server.model.skill.L1SkillId.SOUL_OF_FLAME;
import static com.lineage.server.model.skill.L1SkillId.STATUS_POISON_SILENCE;
import static com.lineage.server.model.skill.L1SkillId.STATUS_RIBRAVE;
import static com.lineage.server.model.skill.L1SkillId.TELEPORT;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.PolyTable;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.utils.CheckUtil;
import com.lineage.server.world.World;

/**
 * 要求使用技能
 * 
 * @author daien
 * 
 */
public class C_UseSkill extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_UseSkill.class);

    /*
     * public C_UseSkill() { }
     * 
     * public C_UseSkill(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    // 隐身状态下可用的魔法
    private static final int[] _cast_with_invis = { HEAL, LIGHT, SHIELD,
            TELEPORT, HOLY_WEAPON, CURE_POISON, ENCHANT_WEAPON, DETECTION, 14,
            19, 21, 26, 31, 32, 35, 37, 42, 43, 44, 48, 49, 52, 54, 55, 57, 60,
            61, 63, 67, 68, 69, 72, 73, 75, 78, 79, REDUCTION_ARMOR,
            BOUNCE_ATTACK, SOLID_CARRIAGE, COUNTER_BARRIER, 97, 98, 99, 100,
            101, 102, 104, 105, 106, 107, 109, 110, 111, 113, 114, 115, 116,
            117, 118, 129, 130, 131, 133, 134, 137, 138, 146, 147, 148, 149,
            150, 151, 155, 156, 158, 159, 163, 164, 165, 166, 168, 169, 170,
            171, SOUL_OF_FLAME, ADDITIONAL_FIRE, DRAGON_SKIN, AWAKEN_ANTHARAS,
            AWAKEN_FAFURION, AWAKEN_VALAKAS, MIRROR_IMAGE, ILLUSION_OGRE,
            ILLUSION_LICH, PATIENCE, ILLUSION_DIA_GOLEM, INSIGHT,
            ILLUSION_AVATAR };

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final L1PcInstance pc = client.getActiveChar();
            /*
             * final long noetime = System.currentTimeMillis(); if (noetime -
             * pc.get_spr_skill_time() <=
             * SprTable.get().spr_skill_speed(pc.getTempCharGfx())) { if
             * (!pc.isGm()) { pc.getNetConnection().kick(); return; } }
             * pc.set_spr_skill_time(noetime);
             */

            if (pc.isDead()) { // 死亡
                return;
            }

            if (pc.isTeleport()) { // 传送中
                return;
            }

            if (pc.isPrivateShop()) {// 商店村模式
                return;
            }

            if (pc.getInventory().getWeight240() >= 197) { // 重量过重
                // 316 \f1你携带太多物品，因此无法使用法术。
                pc.sendPackets(new S_ServerMessage(316));
                pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK,
                        false));//hjx1000
                return;
            }
            

            if (!pc.getMap().isUsableSkill()) {
                // 563 \f1你无法在这个地方使用。
                pc.sendPackets(new S_ServerMessage(563));
                pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK,
                        false));//hjx1000
                return;
            }
            // 技能延迟状态
            if (pc.isSkillDelay()) {
            	return;
            }
//            if (!pc.hasSkillEffect(Card_Fee)) { //收费限制 hjx1000
//            	pc.sendPackets(new S_SystemMessage("点卡不足无法使用魔法，请您及时冲值点卡。"));
//            	return;
//            }


            boolean isError = false;

            // 变身限制
            final int polyId = pc.getTempCharGfx();
            final L1PolyMorph poly = PolyTable.get().getTemplate(polyId);
            // 该变身无法使用魔法
            if ((poly != null) && !poly.canUseSkill()) {
                isError = true;
            }
            // 麻痹?冻结状态
            if (pc.isParalyzed() && !isError) {
                isError = true;
            }

            // 下列状态无法使用魔法(魔法封印)
            if (pc.hasSkillEffect(SILENCE) && !isError) {
                isError = true;
            }

            // 下列状态无法使用魔法(封印禁地)
            if (pc.hasSkillEffect(AREA_OF_SILENCE) && !isError) {
                isError = true;
            }

            // 下列状态无法使用魔法(沈默毒素效果)
            if (pc.hasSkillEffect(STATUS_POISON_SILENCE) && !isError) {
                isError = true;
            }

            // 无法攻击/使用道具/技能/回城的状态
            if (pc.isParalyzedX() && !isError) {
                isError = true;
            }

            if (isError) {
                // 285 \f1在此状态下无法使用魔法。
                pc.sendPackets(new S_ServerMessage(285));
                pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK,
                        false));//hjx1000
                return;
            }

            // 加载封包内容
            final int row = this.readC();
            final int column = this.readC();

            // 计算使用的技能编号(>> 1: 除) (<< 1: 乘) 3=*8
            final int skillId = (row << 3) + column + 1;
            // _log.info("技能ID:" + skillId);
            /*
             * Speed_Ch.ACT_TYPE type = null; switch (skillId) { case 4:// 光箭
             * 1100 case 6:// 冰箭 1100 case 7:// 风刃 1100 case 10:// 寒冷战栗 1100
             * case 15:// 火箭 1300 case 16:// 地狱之牙 1300 case 17:// 极光雷电 1500 case
             * 18:// 起死回生术 1500 case 22:// 寒冰气息 1300 case 25:// 燃烧的火球 1600 case
             * 28:// 吸血鬼之吻 1250 case 30:// 岩牢 1600 case 34:// 极道落雷 1250 case
             * 38:// 冰锥 1100 case 45:// 地裂术 1100 case 46:// 烈炎术 1650 case 50://
             * 冰矛围篱 1600 case 58:// 火牢 1100 case 65:// 雷霆风暴 3000 case 74:// 流星雨
             * 4000 case 77:// 究极光裂术 6000 case 108:// 会心一击 1800 case 132:// 三重矢
             * 1250 case 184:// 岩浆喷吐 3300 case 187:// 屠宰者 3250 case 192:// 夺命之雷
             * 5150 case 194:// 寒冰喷吐 case 202:// 混乱 4000 case 203:// 暴击 3100
             * case 207:// 心灵破坏 4050 case 208:// 骷髅毁坏 3100 case 213:// 武器破坏者
             * 3100 //type = Speed_Ch.ACT_TYPE.SPELL_DIR; break;
             * 
             * case 1:// 初级治愈术 1300 case 8:// 神圣武器 1500 case 9:// 解毒术 1250 case
             * 11:// 毒咒 1650 case 19:// 中级治愈术 1250 case 20:// 闇盲咒术 1650 case
             * 23:// 能量感测 1100 case 26:// 通畅气脉术 1250 case 27:// 坏物术 4200 case
             * 29:// 缓速术 1600 case 33:// 木乃伊的诅咒 6200 case 35:// 高级治愈术 1500 case
             * 36:// 迷魅术 1250 case 37:// 圣洁之光 1250 case 39:// 魔力夺取 1650 case
             * 40:// 黑闇之影 1650 case 41:// 造尸术 case 42:// 体魄强健术 1650 case 43://
             * 加速术 900 case 44:// 魔法相消术 1450 case 47:// 弱化术 1650 case 48://
             * 祝福魔法武器 1250 case 55:// 狂暴术 1250 case 56:// 疾病术 1650 case 57://
             * 全部治愈术 1650 case 61:// 返生术 case 63:// 治愈能量风暴 1100 case 64:// 魔法封印
             * 1650 case 66:// 沉睡之雾 2750 case 67:// 变形术 case 68:// 圣结界 2750 case
             * 71:// 药水霜化术 1650 case 75:// 终极返生术 case 76:// 集体缓速术 4200 case
             * 79:// 灵魂升华 1250 case 87:// 冲击之晕 9500 case 103:// 暗黑盲咒 2700 case
             * 113:// 精准目标 1600 case 116:// 呼唤盟友 case 118:// 援护盟友 case 133://
             * 弱化属性 1250 case 145:// 释放元素 1250 case 148:// 火焰武器 1450 case 149://
             * 风之神射 1450 case 151:// 大地防护 1450 case 152:// 地面障碍 1250 case 153://
             * 魔法消除 1250 case 157:// 大地屏障 1250 case 158:// 生命之泉 1450 case 160://
             * 水之防护 1450 case 165:// 生命呼唤 1450 case 167:// 风之枷锁 1250 case 170://
             * 水之元气 1450 case 173:// 污浊之水 2200 case 174:// 精准射击 4250 case 188://
             * 恐惧无助 4250 case 193:// 惊悚死神 5150 case 204:// 幻觉：欧吉 3150 case
             * 206:// 专注 2050 case 209:// 幻觉：巫妖 3100 case 211:// 耐力 2050 case
             * 212:// 幻想 4050 case 214:// 幻觉：钻石高仑 3150 case 216:// 洞察 2200 case
             * 217:// 恐慌 5150 case 218:// 疼痛的欢愉 4050 case 219:// 幻觉：化身 6100
             * //type = Speed_Ch.ACT_TYPE.SPELL_DIR; break;
             * 
             * case 2:// 日光术 1300 case 3:// 保护罩 1300 case 5:// 指定传送 1100 case
             * 12:// 拟似魔法武器 1500 case 13:// 无所遁形术 1650 case 14:// 负重强化 1250 case
             * 21:// 铠甲护持 1500 case 31:// 魔法屏障 1250 case 32:// 冥想术 1250 case
             * 49:// 体力回复术 1650 case 51:// 召唤术 2200 case 52:// 神圣疾走 1250 case
             * 53:// 龙卷风 2500 case 54:// 强力加速术 case 59:// 冰雪暴 2600 case 60://
             * 隐身术 1250 case 62:// 震裂术 2600 case 69:// 集体传送术 1200 case 70:// 火风暴
             * 3000 case 72:// 强力无所遁形术 1650 case 73:// 创造魔法武器 1500 case 78://
             * 绝对屏障 13000 case 80:// 冰雪飓风 4000 case 88:// 增幅防御 4500 case 89://
             * 尖刺盔甲 6450 case 90:// 坚固防护 4450 case 91:// 反击屏障 4450 case 97://
             * 暗隐术 11700 case 98:// 附加剧毒 2750 case 99:// 影之防护 2750 case 100://
             * 提炼魔石 2000 case 101:// 行走加速 1750 case 102:// 燃烧斗志 4700 case 104://
             * 毒性抵抗 1750 case 105:// 双重破坏 1750 case 106:// 暗影闪避 1750 case 107://
             * 暗影之牙 1700 case 109:// 力量提升 1750 case 110:// 敏捷提升 1750 case 111://
             * 闪避提升 1750 case 114:// 激励士气 1600 case 115:// 钢铁士气 1500 case 117://
             * 冲击士气 1500 case 129:// 魔法防御 1300 case 130:// 心灵转换 1450 case 131://
             * 世界树的呼唤 case 134:// 镜反射 350 case 137:// 净化精神 1450 case 138:// 属性防御
             * 1450 case 146:// 魂体转换 2500 case 147:// 单属性防御 1450 case 150://
             * 风之疾走 1450 case 154:// 召唤属性精灵 1400 case 155:// 烈炎气息 1400 case
             * 156:// 暴风之眼 1400 case 159:// 大地的祝福 1450 case 161:// 封印禁地 10300
             * case 162:// 召唤强力属性精灵 1450 case 163:// 烈炎武器 1450 case 164:// 生命的祝福
             * 1650 case 166:// 暴风神射 1450 case 168:// 钢铁防护 1450 case 169:// 体能激发
             * 1450 case 171:// 属性之火 1450 case 172:// 暴风疾走 1450 case 175:// 烈焰之魂
             * case 176:// 能量激发 case 181:// 龙之护铠 1300 case 182:// 燃烧击砍 3300 case
             * 183:// 护卫毁灭 2200 case 185:// 觉醒：安塔瑞斯 1250 case 186:// 血之渴望 case
             * 189:// 冲击之肤 case 190:// 觉醒：法利昂 1250 case 191:// 致命身躯 2350 case
             * 195:// 觉醒：巴拉卡斯 1250 case 201:// 镜像 1250 case 205:// 立方：燃烧 6200
             * case 210:// 立方：地裂 6250 case 215:// 立方：冲击 6200 case 220:// 立方：和谐
             * 6100 //type = Speed_Ch.ACT_TYPE.SPELL_NODIR; break; } type =
             * Speed_Ch.ACT_TYPE.SPELL_NODIR;
             * pc.speed_Skill().checkIntervalS(type, skillId);//
             */

            if (skillId > 220) {
                return;
            }
            if (skillId < 0) {
                return;
            }
            if (skillId != 132) { //技能动作延时三重矢除外 hjx1000
            	pc.setskillHardDelay(true);
            }
            
            if (skillId == 60 || skillId == 97) { //隐身延时3秒 hjx1000
            	if (pc.hasSkillEffect(20005)) {
            		pc.sendPackets(new S_SystemMessage("5秒内只能够隐身一次"));
            		return;
            	}
            }

            // 隐身状态可用魔法限制
            if (pc.isInvisble() || pc.isInvisDelay()) {
                if (!this.isInvisUsableSkill(skillId)) {
                    // 1003：透明状态无法使用的魔法。
                    pc.sendPackets(new S_ServerMessage(1003));
                    return;
                }
            }

            // 技能合法判断
            if (!pc.isSkillMastery(skillId)) {
                // _log.info(pc.getAccountName() + ":" + pc.getName() + "(" +
                // pc.getType() + ") 非法技能:" + skillId);
                return;
            }

            // 检查地图使用权
            CheckUtil.isUserMap(pc);

            String charName = null;
            // String message = null;

            int targetId = 0;
            int targetX = 0;
            int targetY = 0;
            boolean checkLoc = false;
            if (decrypt.length > 4) {
                boolean isAttack = false;// 为攻击型态技能
                switch (skillId) {
                    case 43:// 加速术43
                        try {
                            targetId = readD();
                            checkLoc = true;

                        } catch (final Exception e) {
                        }
                        break;

                    case 52:// 神圣疾走52
                    case 54:// 强力加速术54
                    case 101:// 行走加速101
                    case 150:// 风之疾走150
                        try {
                            targetId = readD();

                        } catch (final Exception e) {
                        }
                        break;

                    case 186:// 血之渴望186
                        try {
                            // 具有生命之树果实效果
                            if (pc.hasSkillEffect(STATUS_RIBRAVE)) {
                                // 1,413：目前情况是无法使用。
                                pc.sendPackets(new S_ServerMessage(1413));
                                return;
                            }
                            targetId = readD();

                        } catch (final Exception e) {
                        }
                        break;

                    case 116:// 呼唤盟友
                    case 118:// 援护盟友
                        try {
                            charName = readS();

                        } catch (final Exception e) {
                        }
                        break;

                    case 113:// 精准目标 1600
                        try {
                            targetId = readD();
                            targetX = readH();
                            targetY = readH();

                            pc.setText(readS());
                            checkLoc = true;

                        } catch (final Exception e) {
                        }
                        break;

                    case 5:// 指定传送 1100
                    case 69:// 集体传送术 1200
                        try {
                            // readH(); // MapID
                            targetId = readH(); // BookmarkMapID
                            targetX = readH();
                            targetY = readH();
                            pc.setTempBookmarkMapID((short) targetId);
                            pc.setTempBookmarkLocX(targetX);
                            pc.setTempBookmarkLocY(targetY);
                        } catch (final Exception e) {
                        }
                        break;

                    case 58:// 火牢 1100
                    case 63:// 治愈能量风暴 1100
                        try {
                            targetX = readH();
                            targetY = readH();

                        } catch (final Exception e) {
                        }
                        break;

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
                        try {
                            targetId = readD();
                            targetX = readH();
                            targetY = readH();
                            isAttack = true;
                            checkLoc = true;
                            // _log.info("技能ID:" + skillId +
                            // "/"+targetId+"/"+targetX+"/"+targetY);

                        } catch (final Exception e) {
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
                    case 29:// 缓速术 1600
                    case 33:// 木乃伊的诅咒 6200
                    case 35:// 高级治愈术 1500
                    case 36:// 迷魅术 1250
                    case 37:// 圣洁之光 1250
                    case 39:// 魔力夺取 1650
                    case 40:// 黑闇之影 1650
                    case 41:// 造尸术
                    case 42:// 体魄强健术 1650
                        // case 43:// 加速术 900
                    case 44:// 魔法相消术 1450
                    case 47:// 弱化术 1650
                    case 48:// 祝福魔法武器 1250
                    case 55:// 狂暴术 1250
                    case 56:// 疾病术 1650
                    case 57:// 全部治愈术 1650
                    case 61:// 返生术
                    case 64:// 魔法封印 1650
                    case 66:// 沉睡之雾 2750
                    case 67:// 变形术
                    case 68:// 圣结界 2750
                    case 71:// 药水霜化术 1650
                    case 75:// 终极返生术
                    case 76:// 集体缓速术 4200
                    case 79:// 灵魂升华 1250
//                    case 87:// 冲击之晕 9500
                    case 103:// 暗黑盲咒 2700
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
                        try {
                            targetId = readD();
                            checkLoc = true;
                            // _log.info("技能ID:" + skillId +
                            // "/"+targetId+"/"+targetX+"/"+targetY);

                        } catch (final Exception e) {
                        }
                        break;

                    case 2:// 日光术 1300
                    case 3:// 保护罩 1300
                    case 12:// 拟似魔法武器 1500
                    case 13:// 无所遁形术 1650
                    case 14:// 负重强化 1250
                    case 21:// 铠甲护持 1500
                    case 31:// 魔法屏障 1250
                    case 32:// 冥想术 1250
                    case 49:// 体力回复术 1650
                    case 51:// 召唤术 2200
                        // case 52:// 神圣疾走 1250
                    case 53:// 龙卷风 2500
                        // case 54:// 强力加速术
                    case 59:// 冰雪暴 2600
                    case 60:// 隐身术 1250
                    case 62:// 震裂术 2600
                    case 70:// 火风暴 3000
                    case 72:// 强力无所遁形术 1650
                    case 73:// 创造魔法武器 1500
                    case 78:// 绝对屏障 13000
                    case 80:// 冰雪飓风 4000
                        // case 88:// 增幅防御 4500
                    case 89:// 尖刺盔甲 6450
                    case 90:// 坚固防护 4450
                        // case 91:// 反击屏障 4450
                    case 97:// 暗隐术 11700
                    case 98:// 附加剧毒 2750
                    case 99:// 影之防护 2750
                    case 100:// 提炼魔石 2000
                        // case 101:// 行走加速 1750
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
                        // case 150:// 风之疾走 1450
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
                        // case 186:// 血之渴望
                    case 189:// 冲击之肤
                    case 190:// 觉醒：法利昂 1250
                    case 191:// 致命身躯 2350
                    case 195:// 觉醒：巴拉卡斯 1250
                    case 201:// 镜像 1250
                    case 205:// 立方：燃烧 6200
                    case 210:// 立方：地裂 6250
                    case 215:// 立方：冲击 6200
                    case 220:// 立方：和谐 6100
                        try {
                            targetId = readD();
                            // _log.info("技能ID:" + skillId +
                            // "/"+targetId+"/"+targetX+"/"+targetY);

                        } catch (final Exception e) {
                        }
                        break;
                        
                    case 87: //冲击之晕
                        try {
                        	if (pc.getInventory().getTypeEquipped(1, 3) >= 1) {
                                targetId = readD();
                                checkLoc = true;
                        	} else {
                        		return;
                        	}
                            // _log.info("技能ID:" + skillId +
                            // "/"+targetId+"/"+targetX+"/"+targetY);

                        } catch (final Exception e) {
                        }
                        break;

                    case 88:// 增幅防御4500
                        try {
                            if (pc.getInventory().getTypeEquipped(2, 7) >= 1) {
                                targetId = readD();
                            } else {
                                return;
                            }

                        } catch (final Exception e) {
                        }
                        break;

                    case 91:// 反击屏障4450
                        try {
                            if (pc.getInventory().getTypeEquipped(1, 3) >= 1) {
                                targetId = readD();
                            } else {
                                return;
                            }

                        } catch (final Exception e) {
                        }
                        break;

                    default:
                        try {
                            targetId = readD();
                            // _log.info("技能ID:" + skillId +
                            // "/"+targetId+"/"+targetX+"/"+targetY);

                        } catch (final Exception e) {
                            /*
                             * OutErrorMsg.put(this.getClass().getSimpleName(),
                             * "检查 C_UseSkill 技能码定位(核心管理者参考) SkillId: " +
                             * skillId, e);
                             */
                        }
                        break;
                }

                if (targetId != 0) {
                    final L1Object target = World.get().findObject(targetId);
                    if (checkLoc) {
                        // 不在画面内
                        if (!World.get().getVisibleObjects(pc, target)) {
                            /*
                             * OutErrorMsg.put(pc.getId(), pc.getName() +
                             * " 对象人物不在同画面内 SkillId: " + skillId + " 对象:" +
                             * target.getId());
                             */
                            return;
                        }
                    }
                    if (isAttack) {
                        // 目标对象是PC
                        if (target instanceof L1PcInstance) {
                            L1PcInstance tg = (L1PcInstance) target;
                            pc.setNowTarget(tg);
                            pc.setPetTarget(tg);
                        }
                        // 目标对象是宠物
                        if (target instanceof L1PetInstance) {

                        }
                        // 目标对象是招唤兽
                        if (target instanceof L1PetInstance) {

                        }
                    }
                }
            }

            // 绝对屏障解除
            if (pc.hasSkillEffect(ABSOLUTE_BARRIER)) {
                pc.killSkillEffectTimer(ABSOLUTE_BARRIER);
                pc.startHpRegeneration();
                pc.startMpRegeneration();
            }

            // 冥想术解除
            pc.killSkillEffectTimer(MEDITATION);

            try {
                // 呼唤盟友/援护盟友
                if ((skillId == 116) || (skillId == 118)) {
                    if (charName.isEmpty()) {
                        // 输入名称为空
                        return;
                    }

                    final L1PcInstance target = World.get().getPlayer(charName);

                    if (target == null) {
                        // 73 \f1%0%d 不在线上。
                        pc.sendPackets(new S_ServerMessage(73, charName));
                        return;
                    }

                    // 无法攻击/使用道具/技能/回城的状态 XXX
                    /*
                     * if (target.isParalyzedX()) { return; }
                     */

                    if (pc.getClanid() != target.getClanid()) {
                        // 您只能邀请您血盟中的成员。
                        pc.sendPackets(new S_ServerMessage(414));
                        return;
                    }

                    targetId = target.getId();
                    if (skillId == 116) {// 呼唤盟友
                        // 移动せずに连续して同じクラン员にコールクランした场合、向きは前回の向きになる
                        final int callClanId = pc.getCallClanId();
                        if ((callClanId == 0) || (callClanId != targetId)) {
                            pc.setCallClanId(targetId);
                            pc.setCallClanHeading(pc.getHeading());
                        }
                    }
                }                
                final L1SkillUse skilluse = new L1SkillUse();
                skilluse.handleCommands(pc, skillId, targetId, targetX,
                        targetY,
                        // message,
                        0, L1SkillUse.TYPE_NORMAL);
            } catch (final Exception e) {
                /*
                 * OutErrorMsg.put(this.getClass().getSimpleName(),
                 * "检查 C_UseSkill 程式执行位置(核心管理者参考) SkillId: " + skillId, e);
                 */
            }

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    /**
     * 该技能可否在隐身状态使用
     * 
     * @param useSkillid
     *            技能编号
     * @return true:可 false:不可
     */
    private boolean isInvisUsableSkill(int useSkillid) {
        for (final int skillId : _cast_with_invis) {
            if (skillId == useSkillid) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
