package com.lineage.server.clientpackets;

import static com.lineage.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static com.lineage.server.model.skill.L1SkillId.CKEW_LV50;
import static com.lineage.server.model.skill.L1SkillId.SOLID_CARRIAGE;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.ItemClass;
import com.lineage.data.quest.CKEWLv50_1;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.ItemBoxTable;
import com.lineage.server.model.L1ItemDelay;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.L1PcQuest;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_PacketBoxItemLv;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Box;
import com.lineage.server.templates.L1EtcItem;
import com.lineage.server.utils.CheckUtil;

/**
 * 要求使用物品
 * 
 * @author daien
 * 
 */
public class C_ItemUSe extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_ItemUSe.class);

    /*
     * public C_ItemUSe() { }
     * 
     * public C_ItemUSe(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            // 使用者
            final L1PcInstance pc = client.getActiveChar();

            // 鬼魂模式
            if (pc.isGhost()) {
                return;
            }

            // 例外状况:人物死亡
            if (pc.isDead()) {
                return;
            }

            // 例外状况:传送锁定状态
            if (pc.isTeleport()) {
                pc.setTeleport(false);
                pc.sendPackets(new S_Paralysis(
                        S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
                return;
            }

            // 使用物件的OBJID
            final int itemObjid = this.readD();

            // 取回使用物件
            final L1ItemInstance useItem = pc.getInventory().getItem(itemObjid);

            // 例外状况:物件为空
            if (useItem == null) {
                return;
            }

            // 设置使用者OBJID
            useItem.set_char_objid(pc.getId());

            boolean isStop = false;

            // 再生圣殿 1楼/2楼/3楼
            if (pc.getMapId() == CKEWLv50_1.MAPID) {

            }

            // 例外状况:该地图不允许使用道具
            if (!pc.getMap().isUsableItem()) {
                // System.out.println("例外状况:该地图不允许使用道具");
                // 563 \f1你无法在这个地方使用。
                pc.sendPackets(new S_ServerMessage(563));
                isStop = true;
            }

            // 无法攻击/使用道具/技能/回城的状态
            if (pc.isParalyzedX() && !isStop) {
                isStop = true;
            }

            if (!isStop) {
                switch (pc.getMapId()) {
                    case 22:// 杰瑞德的试炼地监
                        switch (useItem.getItemId()) {
                            case 30:// 红骑士之剑
                            case 40017:// 解毒药水
                                break;

                            default:
                                // 563 \f1你无法在这个地方使用。
                                pc.sendPackets(new S_ServerMessage(563));
                                isStop = true;
                                break;
                        }
                }
            }

            if (!CheckUtil.getUseItemAll(pc) && !isStop) {
                isStop = true;
            }

            if (pc.isPrivateShop() && !isStop) {// 商店村模式
                isStop = true;
            }

            // 取得物件触发事件
            final int use_type = useItem.getItem().getUseType();

            if (isStop) {
                pc.setTeleport(false);
                pc.sendPackets(new S_Paralysis(
                        S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
                return;
            }

            // 例外状况:数量小于等于0
            /*
             * if (useItem.getCount() <= 0) { return; }
             */
            boolean isClass = false;// 是否具有CLASS
            final String className = useItem.getItem().getclassname();// 独立执行项位置
            if (!className.equals("0")) {
                isClass = true;
            }

            if (pc.getCurrentHp() > 0) {
                int delay_id = 0;
                if (useItem.getItem().getType2() == 0) { // 种别：一般使用物品：normal
                    delay_id = ((L1EtcItem) useItem.getItem()).get_delayid();
                    // 502:道具禁止使用
                    if (pc.hasItemDelay(L1ItemDelay.ITEM) == true) {
                        return;
                    }
                }

                if (delay_id != 0) {
                    // 延迟作用中
                    if (pc.hasItemDelay(delay_id) == true) {
                        // System.out.println("延迟作用中");
                        pc.sendPackets(new S_Paralysis(
                                S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
                        return;
                    }
                }

                // 例外状况:数量异常
                if (useItem.getCount() <= 0) {
                    // \f1没有具有 %0%o。
                    pc.sendPackets(new S_ServerMessage(329, useItem
                            .getLogName()));
                    return;
                }

                // 取回可用等级限制
                final int min = useItem.getItem().getMinLevel();
                final int max = useItem.getItem().getMaxLevel();

                // 例外状况:等级不足
                if ((min != 0) && (min > pc.getLevel())) {// 等级不足
                    if (min < 50) {
                        // 672 等级%d以上才可使用此道具。
                        final S_PacketBoxItemLv toUser = new S_PacketBoxItemLv(
                                min, 0);
                        pc.sendPackets(toUser);

                    } else {
                        // 318 等级 %0以上才可使用此道具。
                        final S_ServerMessage toUser = new S_ServerMessage(318,
                                String.valueOf(min));
                        pc.sendPackets(toUser);
                    }
                    return;
                }
                // 例外状况:等级过高
                if ((max != 0) && (max < pc.getLevel())) {// 等级过高
                    final S_PacketBoxItemLv toUser = new S_PacketBoxItemLv(0,
                            max);
                    pc.sendPackets(toUser);
                    return;
                }

                // 例外状况:具有时间设置
                boolean isDelayEffect = false;
                if (useItem.getItem().getType2() == 0) {
                    final int delayEffect = ((L1EtcItem) useItem.getItem())
                            .get_delayEffect();
                    // int delayEffect =
                    // l1iteminstance.getItem().get_delayEffect();
                    if (delayEffect > 0) {
                        isDelayEffect = true;
                        final Timestamp lastUsed = useItem.getLastUsed();
                        if (lastUsed != null) {
                            final Calendar cal = Calendar.getInstance();
                            long useTime = (cal.getTimeInMillis() - lastUsed
                                    .getTime()) / 1000;
                            if (useTime <= delayEffect) {
                                // 转换为需等待时间
                                useTime = (delayEffect - useTime) / 60;
                                // 时间数字 转换为字串
                                final String useTimeS = useItem.getLogName()
                                        + " " + String.valueOf(useTime);
                                // 1139 %0 分钟之内无法使用。
                                pc.sendPackets(new S_ServerMessage(1139,
                                        useTimeS));
                                return;
                            }
                        }
                    }
                }

                // 取得物件触发事件判断
                switch (use_type) {
                    case -11:// 对读取方法调用无法分类的物品
                        if (isClass) {
                            ItemClass.get().item(null, pc, useItem);
                        }
                        break;

                    case -10:// 加速药水
                        if (!CheckUtil.getUseItem(pc)) {
                            return;
                        }
                        if (isClass) {
                            ItemClass.get().item(null, pc, useItem);
                        }
                        break;

                    case -9:// 技术书
                        if (isClass) {
                            ItemClass.get().item(null, pc, useItem);
                        }
                        break;

                    case -8:// 料理书
                        if (isClass) {
                            try {
                                final int[] newData = new int[2];
                                newData[0] = this.readC();
                                newData[1] = this.readC();
                                ItemClass.get().item(newData, pc, useItem);

                            } catch (final Exception e) {
                                return;
                            }
                        }
                        break;

                    case -7:// 增HP道具
                        if (!CheckUtil.getUseItem(pc)) {
                            return;
                        }
                        if (isClass) {
                            ItemClass.get().item(null, pc, useItem);
                        }
                        break;

                    case -6:// 增MP道具
                        if (!CheckUtil.getUseItem(pc)) {
                            return;
                        }
                        if (isClass) {
                            ItemClass.get().item(null, pc, useItem);
                        }
                        break;

                    case -4:// 项圈
                        if (isClass) {
                            ItemClass.get().item(null, pc, useItem);
                        }
                        break;

                    case -3:// 飞刀
                        pc.getInventory().setSting(useItem.getItemId());
                        // 452 %0%s 被选择了。
                        pc.sendPackets(new S_ServerMessage(452, useItem
                                .getLogName()));
                        break;

                    case -2:// 箭
                        pc.getInventory().setArrow(useItem.getItemId());
                        // 452 %0%s 被选择了。
                        pc.sendPackets(new S_ServerMessage(452, useItem
                                .getLogName()));
                        break;

                    case -12:// 宠物用具
                    case -5:// 食人妖精竞赛票 / 死亡竞赛票
                    case -1:// 无法使用
                        // 无法使用讯息
                        pc.sendPackets(new S_ServerMessage(74, useItem
                                .getLogName()));
                        break;

                    case 0:// 一般物品(直接施放)
                        if (isClass) {
                            ItemClass.get().item(null, pc, useItem);
                        }
                        break;

                    case 1:// 武器
                           // 武器禁止使用
                        if (pc.hasItemDelay(L1ItemDelay.WEAPON) == true) {
                            return;
                        }
                        if (this.useItem(pc, useItem)) {
                            this.useWeapon(pc, useItem);
                        }
                        break;

                    case 2:// 盔甲
                    case 18:// T恤
                    case 19:// 斗篷
                    case 20:// 手套
                    case 21:// 靴
                    case 22:// 头盔
                    case 23:// 戒指
                    case 24:// 项链
                    case 25:// 盾牌
                    case 37:// 腰带
                    case 40:// 耳环
                    case 43:// 副助道具
                    case 44:// 副助道具
                    case 45:// 副助道具
                    case 48:// 副助道具
                    case 47:// 副助道具
                        // 防具禁止使用
                        if (pc.hasItemDelay(L1ItemDelay.ARMOR) == true) {
                            return;
                        }
                        if (this.useItem(pc, useItem)) {
                            this.useArmor(pc, useItem);
                        }
                        break;

                    case 3:// 创造怪物魔杖(无须选取目标) (无数量:没有任何事情发生)
                        if (isClass) {
                            ItemClass.get().item(null, pc, useItem);
                        }
                        break;

                    case 4:// 希望魔杖(无须选取目标)(有数量:你想要什么 / 无数量:没有任何事情发生)
                        break;

                    case 5:// 魔杖类型(须选取目标)
                        if (isClass) {
                            try {
                                final int[] newData = new int[3];
                                newData[0] = this.readD();// 选取目标的OBJID
                                newData[1] = this.readH();// X座标
                                newData[2] = this.readH();// Y座标
                                ItemClass.get().item(newData, pc, useItem);

                            } catch (final Exception e) {
                                return;
                            }
                        }
                        break;

                    case 6:// 瞬间移动卷轴
                        /*if (!CheckUtil.getUseItem(pc)) {
                            return;
                        }*/ //停掉这个if 修正药水霜化后无法瞬移 hjx1000
                        if (isClass) {
                            try {
                                final int[] newData = new int[3];
                                newData[0] = readH(); // 地图编号
                                newData[1] = readH(); // X坐标
                                newData[2] = readH(); // Y坐标
                                ItemClass.get().item(newData, pc, useItem);
                                pc.sendPackets(new S_Paralysis(
                                        S_Paralysis.TYPE_TELEPORT_UNLOCK, false));

                            } catch (final Exception e) {
                                return;
                            }
                        }
                        break;

                    case 7:// 鉴定卷轴
                        if (isClass) {
                            try {
                                final int[] newData = new int[1];
                                newData[0] = this.readD();// 选取物件的OBJID
                                ItemClass.get().item(newData, pc, useItem);

                            } catch (final Exception e) {
                                return;
                            }
                        }
                        break;

                    case 8:// 复活卷轴
                        if (isClass) {
                            try {
                                final int[] newData = new int[1];
                                newData[0] = this.readD();// 选取目标的OBJID
                                ItemClass.get().item(newData, pc, useItem);

                            } catch (final Exception e) {
                                return;
                            }
                        }
                        break;

                    case 9:// 传送回家的卷轴 / 血盟传送卷轴
                        /*if (!CheckUtil.getUseItem(pc)) {
                            return;
                        }*/ //停掉这个if 修正药水霜化后无法回家 hjx1000
                        if (isClass) {
                            ItemClass.get().item(null, pc, useItem);
                        }
                        break;

                    case 10:// 照明道具
                        // 取得道具编号
                        if ((useItem.getRemainingTime() <= 0)
                                && (useItem.getItemId() != 40004)) {
                            return;
                        }
                        if (isClass) {
                            ItemClass.get().item(null, pc, useItem);
                        }
                        break;

                    case 14:// 请选择一个物品(道具栏位) 灯油/磨刀石/胶水
                        if (isClass) {
                            try {
                                final int[] newData = new int[1];
                                newData[0] = this.readD();// 选取物件的OBJID
                                ItemClass.get().item(newData, pc, useItem);

                            } catch (final Exception e) {
                                return;
                            }
                        }
                        break;

                    case 15:// 哨子
                        if (isClass) {
                            ItemClass.get().item(null, pc, useItem);
                        }
                        break;

                    case 16:// 变形卷轴
                        if (isClass) {
                            final String cmd = this.readS();
                            pc.setText(cmd);// 选取的变身命令
                            ItemClass.get().item(null, pc, useItem);
                        }
                        break;

                    case 17:// 选取目标 (近距离)
                        if (isClass) {
                            try {
                                final int[] newData = new int[3];
                                newData[0] = this.readD();// 选取目标的OBJID
                                newData[1] = this.readH();// X座标
                                newData[2] = this.readH();// Y座标
                                ItemClass.get().item(newData, pc, useItem);

                            } catch (final Exception e) {
                                return;
                            }
                        }
                        break;

                    case 27:// 对盔甲施法的卷轴
                    case 26:// 对武器施法的卷轴
                    case 46:// 饰品强化卷轴
                        if (isClass) {
                            try {
                                final int[] newData = new int[1];
                                // 选取目标的OBJID
                                newData[0] = this.readD();
                                ItemClass.get().item(newData, pc, useItem);

                            } catch (final Exception e) {
                                return;
                            }
                        }
                        break;

                    case 28:// 空的魔法卷轴
                        if (isClass) {
                            try {
                                final int[] newData = new int[1];
                                newData[0] = this.readC();
                                ItemClass.get().item(newData, pc, useItem);

                            } catch (final Exception e) {
                                return;
                            }
                        }
                        break;

                    case 29:// 瞬间移动卷轴(祝福)
                        /*if (!CheckUtil.getUseItem(pc)) {
                            return;
                        }*/ //停掉这个if 修正药水霜化后无法瞬移 hjx1000
                        if (isClass) {
                            try {
                                final int[] newData = new int[3];
                                newData[0] = readH(); // 记忆坐标的地图编号
                                newData[1] = readH(); // 记忆坐标的X
                                newData[2] = readH(); // 记忆坐标的Y
                                ItemClass.get().item(newData, pc, useItem);
                                pc.sendPackets(new S_Paralysis(
                                        S_Paralysis.TYPE_TELEPORT_UNLOCK, false));

                            } catch (final Exception e) {
                                return;
                            }
                        }
                        break;

                    case 30:// 选取目标 (Ctrl 远距离)
                        if (isClass) {
                            try {
                                final int obj = this.readD();// 选取目标的OBJID
                                final int[] newData = new int[] { obj };
                                ItemClass.get().item(newData, pc, useItem);

                            } catch (final Exception e) {
                                return;
                            }
                        }
                        break;

                    case 12:// 信纸
                    case 31:// 圣诞卡片
                    case 33:// 情人节卡片
                    case 35:// 白色情人节卡片
                        if (isClass) {
                            try {
                                final int[] newData = new int[1];
                                newData[0] = this.readH();
                                pc.setText(this.readS());
                                pc.setTextByte(this.readByte());
                                ItemClass.get().item(newData, pc, useItem);

                            } catch (final Exception e) {
                                return;
                            }
                        }
                        break;

                    case 13:// 信纸(打开)
                    case 32:// 圣诞卡片(打开)
                    case 34:// 情人节卡片(打开)
                    case 36:// 白色情人节卡片(打开)
                        if (isClass) {
                            ItemClass.get().item(null, pc, useItem);
                        }
                        break;

                    case 38:// 食物
                        if (!CheckUtil.getUseItem(pc)) {
                            return;
                        }
                        if (isClass) {
                            ItemClass.get().item(null, pc, useItem);
                        }
                        break;

                    case 39:// 选取目标 (远距离)
                        if (isClass) {
                            try {
                                final int[] newData = new int[3];
                                newData[0] = this.readD();// 选取目标的OBJID
                                newData[1] = this.readH();// X座标
                                newData[2] = this.readH();// Y座标
                                ItemClass.get().item(newData, pc, useItem);

                            } catch (final Exception e) {
                                return;
                            }
                        }
                        break;

                    case 42:// 钓鱼杆
                        if (isClass) {
                            try {
                                final int[] newData = new int[3];
                                newData[0] = this.readH();// X座标
                                newData[1] = this.readH();// Y座标
                                ItemClass.get().item(newData, pc, useItem);

                            } catch (final Exception e) {
                                return;
                            }
                        }
                        break;

                    case 55:// 魔法娃娃成长药剂
                        if (isClass) {
                            try {
                                final int[] newData = new int[1];
                                newData[0] = this.readD();// 选取物件的OBJID
                                ItemClass.get().item(newData, pc, useItem);

                            } catch (final Exception e) {
                                return;
                            }
                        }
                        break;

                    default:// 测试
                        _log.info("未处理的物品分类: " + use_type);
                        break;
                }

                if ((useItem.getItem().getType2() == 0) && (use_type == 0)) { // 种别：一般道具
                    // 取得道具编号
                    final int itemId = useItem.getItem().getItemId();

                    switch (itemId) {
                        case 40630:// 迪哥的旧日记
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                    "diegodiary"));
                            break;

                        case 40663:// 儿子的信
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                    "sonsletter"));
                            break;

                        case 40701:// 小藏宝图
                            if (pc.getQuest().get_step(L1PcQuest.QUEST_LUKEIN1) == 1) {
                                pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                        "firsttmap"));
                            } else if (pc.getQuest().get_step(
                                    L1PcQuest.QUEST_LUKEIN1) == 2) {
                                pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                        "secondtmapa"));
                            } else if (pc.getQuest().get_step(
                                    L1PcQuest.QUEST_LUKEIN1) == 3) {
                                pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                        "secondtmapb"));
                            } else if (pc.getQuest().get_step(
                                    L1PcQuest.QUEST_LUKEIN1) == 4) {
                                pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                        "secondtmapc"));
                            } else if (pc.getQuest().get_step(
                                    L1PcQuest.QUEST_LUKEIN1) == 5) {
                                pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                        "thirdtmapd"));
                            } else if (pc.getQuest().get_step(
                                    L1PcQuest.QUEST_LUKEIN1) == 6) {
                                pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                        "thirdtmape"));
                            } else if (pc.getQuest().get_step(
                                    L1PcQuest.QUEST_LUKEIN1) == 7) {
                                pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                        "thirdtmapf"));
                            } else if (pc.getQuest().get_step(
                                    L1PcQuest.QUEST_LUKEIN1) == 8) {
                                pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                        "thirdtmapg"));
                            } else if (pc.getQuest().get_step(
                                    L1PcQuest.QUEST_LUKEIN1) == 9) {
                                pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                        "thirdtmaph"));
                            } else if (pc.getQuest().get_step(
                                    L1PcQuest.QUEST_LUKEIN1) == 10) {
                                pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                        "thirdtmapi"));
                            }
                            break;

                        case 41007:// 伊莉丝的命令书：灵魂之安息
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                    "erisscroll"));
                            break;

                        case 41009:// 伊莉丝的命令书：同盟之意志
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                    "erisscroll2"));
                            break;

                        case 41060:// 诺曼阿吐巴的信
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                    "nonames"));
                            break;

                        case 41061:// 妖精调查书：卡麦都达玛拉
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                    "kames"));
                            break;

                        case 41062:// 人类调查书：巴库摩那鲁加
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                    "bakumos"));
                            break;

                        case 41063:// 精灵调查书：可普都达玛拉
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                    "bukas"));
                            break;

                        case 41064:// 妖魔调查书：弧邬牟那鲁加
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                    "huwoomos"));
                            break;

                        case 41065:// 死亡之树调查书：诺亚阿吐巴
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                    "noas"));
                            break;

                        case 41317:// 拉罗森的推荐书
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                    "rarson"));
                            break;

                        case 41318:// 可恩的便条纸
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                    "kuen"));
                            break;

                        case 41329:// 标本制作委托书
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                    "anirequest"));
                            break;

                        case 41340:// 佣兵团长多文的推荐书
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                    "tion"));
                            break;

                        case 41356:// 波伦的资源清单
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
                                    "rparum3"));
                            break;

                    }
                }

                if (useItem.getItem().getType2() == 0
                        && useItem.getItem().getType() == 16) { // treasure_box
                    // 容量确认
                    if (pc.getInventory().getSize() >= 160) {
                        // 263 \f1一个角色最多可携带180个道具。
                        pc.sendPackets(new S_ServerMessage(263));
                        return;
                    }

                    if (pc.getInventory().getWeight240() >= 180) {
                        // 82 此物品太重了，所以你无法携带。
                        pc.sendPackets(new S_ServerMessage(82));
                        return;
                    }

                    final ArrayList<L1Box> list = ItemBoxTable.get().get(pc,
                            useItem);
                    if (list == null) {
                        ItemBoxTable.get().get_all(pc, useItem);
                    }
                }

                // 物件使用延迟设置
                if (isDelayEffect) {
                    final Timestamp ts = new Timestamp(
                            System.currentTimeMillis());
                    // 设置使用时间
                    useItem.setLastUsed(ts);
                    pc.getInventory().updateItem(useItem,
                            L1PcInventory.COL_DELAY_EFFECT);
                    pc.getInventory().saveItem(useItem,
                            L1PcInventory.COL_DELAY_EFFECT);
                }
                try {
                    // 分类道具使用延迟
                    L1ItemDelay.onItemUse(client, useItem);

                } catch (final Exception e) {
                    _log.error("分类道具使用延迟异常:" + useItem.getItemId(), e);
                }
            }

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    /**
     * 武器防具的使用<BR>
     * 
     * @param pc
     * @param useItem
     * @return 该职业可用传回:true
     */
    private boolean useItem(final L1PcInstance pc, final L1ItemInstance useItem) {
        boolean isEquipped = false;
        // 职业与物件的使用限制
        if (pc.isCrown()) {// 王族
            if (useItem.getItem().isUseRoyal()) {
                isEquipped = true;
            }
        } else if (pc.isKnight()) {// 骑士
            if (useItem.getItem().isUseKnight()) {
                isEquipped = true;
            }
        } else if (pc.isElf()) {// 精灵
            if (useItem.getItem().isUseElf()) {
                isEquipped = true;
            }
        } else if (pc.isWizard()) {// 法师
            if (useItem.getItem().isUseMage()) {
                isEquipped = true;
            }
        } else if (pc.isDarkelf()) {// 黑暗精灵;
            if (useItem.getItem().isUseDarkelf()) {
                isEquipped = true;
            }
        } else if (pc.isDragonKnight()) {// 龙骑士
            if (useItem.getItem().isUseDragonknight()) {
                isEquipped = true;
            }
        } else if (pc.isIllusionist()) {// 幻术师
            if (useItem.getItem().isUseIllusionist()) {
                isEquipped = true;
            }
        }

        if (!isEquipped) {
            // 264 \f1你的职业无法使用此装备。
            pc.sendPackets(new S_ServerMessage(264));
        }
        return isEquipped;
    }

    /**
     * 防具的使用<BR>
     * 特别地图定义
     * 
     * @param pc
     * @param useItem
     */
    /*
     * private void armor(final L1PcInstance pc, final L1ItemInstance useItem) {
     * // 种别：防具 if ((pc.isCrown() && useItem.getItem().isUseRoyal()// 皇族 ) ||
     * (pc.isKnight() && useItem.getItem().isUseKnight()// 骑士 ) || (pc.isElf()
     * && useItem.getItem().isUseElf() // 精灵 ) || (pc.isWizard() &&
     * useItem.getItem().isUseMage()// 魔法师 ) || (pc.isDarkelf() &&
     * useItem.getItem().isUseDarkelf()// 黑暗妖精 ) || (pc.isDragonKnight() &&
     * useItem.getItem().isUseDragonknight()// 龙骑士 ) || (pc.isIllusionist() &&
     * useItem.getItem().isUseIllusionist()))// 幻术师 { this.useArmor(pc,
     * useItem); } else { // 264 \f1你的职业无法使用此装备。 pc.sendPackets(new
     * S_ServerMessage(264)); } }
     */

    /**
     * 设置防具的装备
     * 
     * @param pc
     * @param armor
     */
    private void useArmor(final L1PcInstance pc, final L1ItemInstance armor) {
        final int type = armor.getItem().getType();
        final L1PcInventory pcInventory = pc.getInventory();
        boolean equipeSpace; // 装备栏是否有空位
        if (type == 9) { // type类型为9是戒指可戴4个,其他都只能戴1个
            equipeSpace = pcInventory.getTypeEquipped(2, 9) <= 3;

        } else {
            equipeSpace = pcInventory.getTypeEquipped(2, type) <= 0;
        }
        // 防具穿戴
        if (equipeSpace && !armor.isEquipped()) { // 要安装的装备栏尚未安装物品
            final int polyid = pc.getTempCharGfx();

            if (!L1PolyMorph.isEquipableArmor(polyid, type)) { // 不可此穿戴防具的变身形态下
                return;
            }

            if (((type == 13) && (pcInventory.getTypeEquipped(2, 7) >= 1 // 已经装备其他东西。
                    ))
                    || ((type == 7) && (pcInventory.getTypeEquipped(2, 13) >= 1))) {
                pc.sendPackets(new S_ServerMessage(124));
                return;
            }
            
            if (type == 9) {//第三和第四个戒指等级限制 hjx1000
            	if (pcInventory.getTypeEquipped(2, 9) > 1 && pc.getLevel() < 76) {
                    pc.sendPackets(new S_SystemMessage("等级不足LV.76"));
            		return;
            	}else if (pcInventory.getTypeEquipped(2, 9) > 2 && pc.getLevel() < 81) {
            		pc.sendPackets(new S_SystemMessage("等级不足LV.81"));
            		return;
            	}
            	if(pcInventory.getTypeAndItemIdEquipped(2, 9, armor.getItem().getNameId()) >= 2){ 
     			   pc.sendPackets(new S_SystemMessage("同类戒指不可佩戴3個.")); 
     				return;	
     			}
            }

            if ((type == 7) && (pc.getWeapon() != null)) { // 使用双手武器时无法使用盾
                if (pc.getWeapon().getItem().isTwohandedWeapon()) { // 双手武器
                    // 129 \f1当你使用双手武器时，无法装备盾牌。
                    pc.sendPackets(new S_ServerMessage(129));
                    return;
                }
            }

            if ((type == 3) && (pcInventory.getTypeEquipped(2, 4) >= 1)) { // 穿着斗篷时不可穿内衣
                // 126 \f1穿着%1 无法装备 %0%o 。
                pc.sendPackets(new S_ServerMessage(126, "$224", "$225"));
                return;

            } else if ((type == 3) && (pcInventory.getTypeEquipped(2, 2) >= 1)) { // 穿着盔甲时不可穿内衣
                // 126 \f1穿着%1 无法装备 %0%o 。
                pc.sendPackets(new S_ServerMessage(126, "$224", "$226"));
                return;

            } else if ((type == 2) && (pcInventory.getTypeEquipped(2, 4) >= 1)) { // 穿着斗篷时不可穿盔甲
                // 126 \f1穿着%1 无法装备 %0%o 。
                pc.sendPackets(new S_ServerMessage(126, "$226", "$225"));
                return;
            }

            // 物品穿戴成功解除技能：魔法屏障
            if (pc.hasSkillEffect(ABSOLUTE_BARRIER)) {
                pc.killSkillEffectTimer(ABSOLUTE_BARRIER);
                pc.startHpRegeneration();
                pc.startMpRegeneration();
            }

            pcInventory.setEquipped(armor, true);

            // 防具脱除
        } else if (armor.isEquipped()) { // 所选防具穿戴在身上
            if (armor.getItem().getBless() == 2) { // 咒われていた场合
                // 150 \f1你无法这样做。这个物品已经被诅咒了。
                pc.sendPackets(new S_ServerMessage(150));
                return;

            } else {
                if ((type == 3) && (pcInventory.getTypeEquipped(2, 2) >= 1)) { // 穿着盔甲时不能脱下内衣
                    // 127 \f1你不能够脱掉那个。
                    pc.sendPackets(new S_ServerMessage(127));
                    return;

                } else if (((type == 2) || (type == 3))
                        && (pcInventory.getTypeEquipped(2, 4) >= 1)) { // 穿着斗篷时不能脱下内衣
                    // 127 \f1你不能够脱掉那个。
                    pc.sendPackets(new S_ServerMessage(127));
                    return;
                }
                if (type == 7) { // 解除魔法技能坚固防御
                    if (pc.hasSkillEffect(SOLID_CARRIAGE)) {
                        pc.removeSkillEffect(SOLID_CARRIAGE);
                    }
                }

                pcInventory.setEquipped(armor, false);
            }
            /*
             * 1:helm, 头盔 2:armor, 盔甲 3:T,内衣 4:cloak,斗篷 5:glove,手套 6:boots, 靴子
             * 7:shield, 盾 8:amulet, 项链 9:ring, 戒指 10:belt, 腰带 11:ring2,
             * 12:earring耳环 13:
             */
        } else {
            if (armor.getItem().getUseType() == 23) {
                // 144 \f1你已经戴着二个戒指。
            	pc.sendPackets(new S_SystemMessage("你已经戴着四个戒指"));//hjx1000
                //pc.sendPackets(new S_ServerMessage(144));
                return;

            } else {
                // 124 \f1已经装备其他东西。
                pc.sendPackets(new S_ServerMessage(124));
                return;
            }
        }

        // 更新HP,MP
        pc.setCurrentHp(pc.getCurrentHp());
        pc.setCurrentMp(pc.getCurrentMp());

        // 更新角色防御属性
        pc.sendPackets(new S_OwnCharAttrDef(pc));
        // 更改人物状态
        pc.sendPackets(new S_OwnCharStatus(pc));
        // 更改人物魔法攻击与魔法防御
        pc.sendPackets(new S_SPMR(pc));

    }

    /**
     * 设置武器的装备
     * 
     * @param pc
     * @param weapon
     */
    private void useWeapon(final L1PcInstance pc, final L1ItemInstance weapon) {
        switch (weapon.getItemId()) {
            case 65:// 天空之剑
            case 133:// 古代人的智慧
            case 191:// 水之竖琴
            case 192:// 水精灵之弓
                if (pc.getMapId() != CKEWLv50_1.MAPID
                        && !pc.getWeapon().equals(weapon)) {
                    // 563 \f1你无法在这个地方使用。
                    pc.sendPackets(new S_ServerMessage(563));
                    return;
                }
                break;

            default:
                if (pc.hasSkillEffect(CKEW_LV50)) {
                    // 563 \f1你无法在这个地方使用。
                    pc.sendPackets(new S_ServerMessage(563));
                    return;
                }
                break;
        }
        final L1PcInventory pcInventory = pc.getInventory();
        if ((pc.getWeapon() == null) || !pc.getWeapon().equals(weapon)) { // 没有使用武器或使用武器与所选武器不同
            final int weapon_type = weapon.getItem().getType();
            final int polyid = pc.getTempCharGfx();

            if (!L1PolyMorph.isEquipableWeapon(polyid, weapon_type)) { // 不可使用此武器的变身形态下使用武器
                return;
            }
            if (weapon.getItem().isTwohandedWeapon()
                    && (pcInventory.getTypeEquipped(2, 7) >= 1)) { // 使用了盾时不可再使用双手武器
                pc.sendPackets(new S_ServerMessage(128));
                return;
            }
        }
        // 解除魔法技能：绝对屏障
        if (pc.hasSkillEffect(ABSOLUTE_BARRIER)) {
            pc.killSkillEffectTimer(ABSOLUTE_BARRIER);
            pc.startHpRegeneration();
            pc.startMpRegeneration();
        }

        if (pc.getWeapon() != null) { // 已有装备的状态
            if (pc.getWeapon().getItem().getBless() == 2) {
                // 150 \f1你无法这样做。这个物品已经被诅咒了。
                pc.sendPackets(new S_ServerMessage(150));
                return;
            }
            if (pc.getWeapon().equals(weapon)) {
                // 解除装备
                pcInventory.setEquipped(pc.getWeapon(), false, false, false);
                return;

                // 武器交换
            } else {
                pcInventory.setEquipped(pc.getWeapon(), false, false, true);
            }

        }

        if (weapon.getItem().getBless() == 2) {
            // \f1%0%s 主动固定在你的手上！
            pc.sendPackets(new S_ServerMessage(149, weapon.getLogName()));
        }
        pcInventory.setEquipped(weapon, true, false, false);
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
