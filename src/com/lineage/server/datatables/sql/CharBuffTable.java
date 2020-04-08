package com.lineage.server.datatables.sql;

import static com.lineage.server.model.skill.L1SkillId.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.storage.CharBuffStorage;
import com.lineage.server.model.L1Cooking;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillMode;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.model.skill.skillmode.SkillMode;
import com.lineage.server.serverpackets.S_Liquor;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_PacketBoxCooking;
import com.lineage.server.serverpackets.S_PacketBoxThirdSpeed;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillBrave;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.templates.L1BuffTmp;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 保留技能纪录
 * 
 * @author dexc
 * 
 */
public class CharBuffTable implements CharBuffStorage {

    private static final Log _log = LogFactory.getLog(CharBuffTable.class);

    private static final Map<Integer, ArrayList<L1BuffTmp>> _buffMap = new HashMap<Integer, ArrayList<L1BuffTmp>>();

    // 执行保留的技能
    private static final int[] _buffSkill = {
            LIGHT, // 日光术

            SHAPE_CHANGE, // 变形术

            HASTE, // 加速术
            GREATER_HASTE, // 强力加速术

            SHIELD, // 保护罩
            SHADOW_ARMOR, // 影之防护
            EARTH_SKIN, // 大地防护
            EARTH_BLESS, // 大地的祝福
            IRON_SKIN, // 钢铁防护

            STATUS_BRAVE, // 勇敢药水效果
            STATUS_HASTE, // 加速药水效果
            STATUS_ELFBRAVE, // 精灵饼干效果
            HOLY_WALK, // 神圣疾走
            MOVING_ACCELERATION, // 行走加速
            WIND_WALK, // 风之疾走

            PHYSICAL_ENCHANT_DEX, // 通畅气脉术
            PHYSICAL_ENCHANT_STR, // 体魄强健术
            DRESS_MIGHTY, // 力量提升
            DRESS_DEXTERITY, // 敏捷提升

            GLOWING_AURA, // 激励士气
            SHINING_AURA, // 钢铁士气
            BRAVE_AURA, // 冲击士气

            FIRE_WEAPON, // 火焰武器
            FIRE_BLESS, // 烈炎气息
            BURNING_WEAPON, // 烈炎武器

            WIND_SHOT, // 风之神射
            STORM_EYE, // 暴风之眼
            STORM_SHOT, // 暴风神射

            STATUS_BLUE_POTION,// 魔力回复药水效果
            STATUS_CHAT_PROHIBITED, // 禁言效果

            COOKING_1_0_N,
            COOKING_1_0_S,
            COOKING_1_1_N,
            COOKING_1_1_S, // 料理(デザートは除く)
            COOKING_1_2_N, COOKING_1_2_S, COOKING_1_3_N, COOKING_1_3_S,
            COOKING_1_4_N, COOKING_1_4_S, COOKING_1_5_N, COOKING_1_5_S,
            COOKING_1_6_N, COOKING_1_6_S, COOKING_2_0_N, COOKING_2_0_S,
            COOKING_2_1_N, COOKING_2_1_S, COOKING_2_2_N, COOKING_2_2_S,
            COOKING_2_3_N, COOKING_2_3_S, COOKING_2_4_N, COOKING_2_4_S,
            COOKING_2_5_N, COOKING_2_5_S, COOKING_2_6_N, COOKING_2_6_S,
            COOKING_3_0_N, COOKING_3_0_S, COOKING_3_1_N, COOKING_3_1_S,
            COOKING_3_2_N, COOKING_3_2_S, COOKING_3_3_N, COOKING_3_3_S,
            COOKING_3_4_N, COOKING_3_4_S, COOKING_3_5_N, COOKING_3_5_S,
            COOKING_3_6_N, COOKING_3_6_S,

            EXP13, EXP15, EXP17, EXP20, // 第一段经验加倍

            SEXP13, SEXP15, SEXP17, SEXP20, // 第二段经验加倍

            REEXP20, // 第三段经验加倍

            STATUS_BRAVE3, // 巧克力蛋糕
            
            Card_Fee, //收费计时状态
            // 龙之血痕
            EFFECT_BLOODSTAIN_OF_ANTHARAS,
            EFFECT_BLOODSTAIN_OF_FAFURION,
    };

    /**
     * 初始化载入
     */
    @Override
    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `character_buff`");
            rs = ps.executeQuery();
            while (rs.next()) {
                final int char_obj_id = rs.getInt("char_obj_id");

                // 检查该资料所属是否遗失
                if (CharObjidTable.get().isChar(char_obj_id) != null) {
                    final int skill_id = rs.getInt("skill_id");
                    final int remaining_time = rs.getInt("remaining_time");
                    final int poly_id = rs.getInt("poly_id");

                    final L1BuffTmp buffTmp = new L1BuffTmp();
                    buffTmp.set_char_obj_id(char_obj_id);
                    buffTmp.set_skill_id(skill_id);
                    buffTmp.set_remaining_time(remaining_time);
                    buffTmp.set_poly_id(poly_id);

                    addMap(char_obj_id, buffTmp);

                } else {
                    delete(char_obj_id);
                }
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("载入保留技能纪录资料数量: " + _buffMap.size() + "(" + timer.get()
                + "ms)");
    }

    /**
     * 删除遗失资料
     * 
     * @param objid
     */
    private static void delete(final int objid) {
        final ArrayList<L1BuffTmp> list = _buffMap.get(objid);
        if (list != null) {
            list.clear();// 移除此列表中的所有元素
        }
        _buffMap.remove(objid);

        // 清空资料库纪录
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `character_buff` WHERE `char_obj_id`=?");
            ps.setInt(1, objid);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);

        }
    }

    /**
     * 加入清单
     * 
     * @param objId
     * @param buffTmp
     */
    private static void addMap(final int objId, final L1BuffTmp buffTmp) {
        final ArrayList<L1BuffTmp> list = _buffMap.get(objId);
        if (list == null) {
            final ArrayList<L1BuffTmp> newlist = new ArrayList<L1BuffTmp>();
            newlist.add(buffTmp);
            _buffMap.put(objId, newlist);

        } else {
            list.add(buffTmp);
        }
    }

    /**
     * 增加保留技能纪录
     * 
     * @param pc
     */
    @Override
    public void saveBuff(final L1PcInstance pc) {
        for (final int skillId : _buffSkill) {
            final int timeSec = pc.getSkillEffectTimeSec(skillId);
            if (0 < timeSec) {
                int polyId = -1;
                if (skillId == SHAPE_CHANGE) {
                    polyId = pc.getTempCharGfx();
                }
                this.storeBuff(pc.getId(), skillId, timeSec, polyId);
            }
        }
        // 删除全部技能效果
        pc.clearSkillEffectTimer();
    }

    /**
     * 写入保留技能纪录
     * 
     * @param objId
     * @param skillId
     * @param time
     * @param polyId
     */
    private void storeBuff(final int objId, final int skillId, final int time,
            final int polyId) {
        final L1BuffTmp buffTmp = new L1BuffTmp();
        buffTmp.set_char_obj_id(objId);
        buffTmp.set_skill_id(skillId);
        buffTmp.set_remaining_time(time);
        buffTmp.set_poly_id(polyId);
        // 加入MAP
        addMap(objId, buffTmp);
        // 写入资料库
        storeBuffR(buffTmp);
    }

    /**
     * 取回保留技能纪录
     * 
     * @param pc
     */
    @Override
    public void buff(final L1PcInstance pc) {
        final int objid = pc.getId();
        final ArrayList<L1BuffTmp> list = _buffMap.get(objid);
        if (list != null) {
            for (final L1BuffTmp buffTmp : list) {
                // 取回资料
                final int skill_id = buffTmp.get_skill_id();
                final int remaining_time = buffTmp.get_remaining_time();// 秒
                final int poly_id = buffTmp.get_poly_id();

                if (remaining_time > 0) {
                    if (poly_id != -1) {// 变身
                        L1PolyMorph.doPoly(pc, poly_id, remaining_time,
                                L1PolyMorph.MORPH_BY_LOGIN);

                    } else {
                        switch (skill_id) {
                            case STATUS_BRAVE3: // 巧克力蛋糕
                                pc.sendPackets(new S_PacketBoxThirdSpeed(
                                        remaining_time));
                                pc.sendPacketsAll(new S_Liquor(pc.getId(), 0x08));
                                pc.setSkillEffect(skill_id,
                                        remaining_time * 1000);
                                break;

                            case STATUS_BRAVE: // 勇敢药水效果
                                pc.sendPackets(new S_SkillBrave(pc.getId(), 1,
                                        remaining_time));
                                pc.broadcastPacketAll(new S_SkillBrave(pc
                                        .getId(), 1, 0));
                                pc.setBraveSpeed(1);
                                pc.setSkillEffect(skill_id,
                                        remaining_time * 1000);
                                break;

                            case STATUS_ELFBRAVE: // 精灵饼干效果
                                pc.sendPackets(new S_SkillBrave(pc.getId(), 3,
                                        remaining_time));
                                pc.broadcastPacketAll(new S_SkillBrave(pc
                                        .getId(), 3, 0));
                                pc.setBraveSpeed(1);
                                pc.setSkillEffect(skill_id,
                                        remaining_time * 1000);
                                break;

                            case STATUS_HASTE: // 加速药水效果
                                pc.sendPackets(new S_SkillHaste(pc.getId(), 1,
                                        remaining_time));
                                pc.broadcastPacketAll(new S_SkillHaste(pc
                                        .getId(), 1, 0));
                                pc.setMoveSpeed(1);
                                pc.setSkillEffect(skill_id,
                                        remaining_time * 1000);
                                break;

                            case STATUS_BLUE_POTION: // 蓝水
                                pc.sendPackets(new S_PacketBox(
                                        S_PacketBox.ICON_BLUEPOTION,
                                        remaining_time));
                                pc.setSkillEffect(skill_id,
                                        remaining_time * 1000);
                                break;

                            case STATUS_CHAT_PROHIBITED: // 禁言
                                pc.sendPackets(new S_PacketBox(
                                        S_PacketBox.ICON_CHATBAN,
                                        remaining_time));
                                pc.setSkillEffect(skill_id,
                                        remaining_time * 1000);
                                break;

                            case EXP13: // 第一段1.3倍经验
                                // 3021 目前正在享受 %0 倍经验.【剩余时间: %1 秒】
                                pc.sendPackets(new S_ServerMessage(
                                        "第一段1.3倍经验 剩余时间(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id,
                                        remaining_time * 1000);
                                // 狩猎的经验职将会增加
                                pc.sendPackets(new S_PacketBoxCooking(pc, 32,
                                        remaining_time));
                                break;

                            case EXP15: // 第一段1.5倍经验
                                // 3021 目前正在享受 %0 倍经验.【剩余时间: %1 秒】
                                pc.sendPackets(new S_ServerMessage(
                                        "第一段1.5倍经验 剩余时间(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id,
                                        remaining_time * 1000);
                                // 狩猎的经验职将会增加
                                pc.sendPackets(new S_PacketBoxCooking(pc, 32,
                                        remaining_time));
                                break;

                            case EXP17: // 第一段1.7倍经验
                                // 3021 目前正在享受 %0 倍经验.【剩余时间: %1 秒】
                                pc.sendPackets(new S_ServerMessage(
                                        "第一段1.7倍经验 剩余时间(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id,
                                        remaining_time * 1000);
                                // 狩猎的经验职将会增加
                                pc.sendPackets(new S_PacketBoxCooking(pc, 32,
                                        remaining_time));
                                break;

                            case EXP20: // 第一段双倍经验
                                // 3021 目前正在享受 %0 倍经验.【剩余时间: %1 秒】
                                pc.sendPackets(new S_ServerMessage(
                                        "第一段2.0倍经验 剩余时间(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id,
                                        remaining_time * 1000);
                                // 狩猎的经验职将会增加
                                pc.sendPackets(new S_PacketBoxCooking(pc, 32,
                                        remaining_time));
                                break;

                            case SEXP13: // 第二段1.3倍经验
                                // 3083 第二段经验1.3倍效果时间尚有 %0 秒。
                                pc.sendPackets(new S_ServerMessage(
                                        "第二段1.3倍经验 剩余时间(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id,
                                        remaining_time * 1000);
                                break;

                            case SEXP15: // 第二段1.5倍经验
                                // 3084 第二段经验1.5倍效果时间尚有 %0 秒。
                                pc.sendPackets(new S_ServerMessage(
                                        "第二段1.5倍经验 剩余时间(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id,
                                        remaining_time * 1000);
                                break;

                            case SEXP17: // 第二段1.7倍经验
                                // 3085 第二段经验1.7倍效果时间尚有 %0 秒。
                                pc.sendPackets(new S_ServerMessage(
                                        "第二段1.7倍经验 剩余时间(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id,
                                        remaining_time * 1000);
                                break;

                            case SEXP20: // 第二段2.0倍经验
                                // 3082 第二段经验2.0倍效果时间尚有 %0 秒。
                                pc.sendPackets(new S_ServerMessage(
                                        "第二段2.0倍经验 剩余时间(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id,
                                        remaining_time * 1000);
                                break;

                            case REEXP20: // 第三段双倍经验
                                // 3086 特殊经验双倍效果时间尚有 %0 秒。
                                pc.sendPackets(new S_ServerMessage(
                                        "第三段2.0倍经验 剩余时间(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id,
                                        remaining_time * 1000);
                                break;
                            case Card_Fee:// 点卡计费状态 hjx1000
//                                pc.sendPackets(new S_ServerMessage(
//                                        "您的点卡剩余时间:" + pc.getNetConnection().getAccount().get_card_fee()
//                                        + "分钟" + remaining_time + "秒"));
                                pc.setSkillEffect(skill_id,
                                        remaining_time * 1000);
                            	break;
                            case EFFECT_BLOODSTAIN_OF_ANTHARAS: // 安塔瑞斯的血痕
                                L1BuffUtil.bloodstain(pc, (byte) 0, remaining_time,
                                            false);

                                break;
                            case EFFECT_BLOODSTAIN_OF_FAFURION: // 法利昂的血痕
                                L1BuffUtil.bloodstain(pc, (byte) 1, remaining_time,
                                            false);
                                break;

                            case COOKING_1_0_N:
                            case COOKING_1_1_N:
                            case COOKING_1_2_N:
                            case COOKING_1_3_N:
                            case COOKING_1_4_N:
                            case COOKING_1_5_N:
                            case COOKING_1_6_N:
                            case COOKING_1_7_N:
                            case COOKING_1_0_S:
                            case COOKING_1_1_S:
                            case COOKING_1_2_S:
                            case COOKING_1_3_S:
                            case COOKING_1_4_S:
                            case COOKING_1_5_S:
                            case COOKING_1_6_S:
                            case COOKING_1_7_S:
                            case COOKING_2_0_N:
                            case COOKING_2_1_N:
                            case COOKING_2_2_N:
                            case COOKING_2_3_N:
                            case COOKING_2_4_N:
                            case COOKING_2_5_N:
                            case COOKING_2_6_N:
                            case COOKING_2_7_N:
                            case COOKING_2_0_S:
                            case COOKING_2_1_S:
                            case COOKING_2_2_S:
                            case COOKING_2_3_S:
                            case COOKING_2_4_S:
                            case COOKING_2_5_S:
                            case COOKING_2_6_S:
                            case COOKING_2_7_S:
                            case COOKING_3_0_N:
                            case COOKING_3_1_N:
                            case COOKING_3_2_N:
                            case COOKING_3_3_N:
                            case COOKING_3_4_N:
                            case COOKING_3_5_N:
                            case COOKING_3_6_N:
                            case COOKING_3_7_N:
                            case COOKING_3_0_S:
                            case COOKING_3_1_S:
                            case COOKING_3_2_S:
                            case COOKING_3_3_S:
                            case COOKING_3_4_S:
                            case COOKING_3_5_S:
                            case COOKING_3_6_S:
                            case COOKING_3_7_S:
                                L1Cooking.eatCooking(pc, skill_id,
                                        remaining_time);
                                break;

                            default:
                                // SKILL移转
                                final SkillMode mode = L1SkillMode.get()
                                        .getSkill(skill_id);
                                if (mode != null) {
                                    try {
                                        mode.start(pc, pc, null, remaining_time);

                                    } catch (Exception e) {
                                        _log.error(e.getLocalizedMessage(), e);
                                    }

                                } else {
                                    final L1SkillUse l1skilluse = new L1SkillUse();
                                    l1skilluse.handleCommands(pc, skill_id,
                                            pc.getId(), pc.getX(), pc.getY(),
                                            remaining_time,
                                            L1SkillUse.TYPE_LOGIN);
                                    //System.out.println("skillid=:"+skill_id);
                                }
                                break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 删除全部保留技能纪录
     * 
     * @param pc
     */
    @Override
    public void deleteBuff(final L1PcInstance pc) {
        delete(pc.getId());
    }

    /**
     * 删除全部保留技能纪录
     * 
     * @param objid
     */
    @Override
    public void deleteBuff(final int objid) {
        delete(objid);
    }

    /**
     * 写入保留技能纪录
     * 
     * @param buffTmp
     */
    private static void storeBuffR(L1BuffTmp buffTmp) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("INSERT INTO `character_buff` SET `char_obj_id`=?,`skill_id`=?,`remaining_time`=?,`poly_id`=?");
            ps.setInt(1, buffTmp.get_char_obj_id());
            ps.setInt(2, buffTmp.get_skill_id());
            ps.setInt(3, buffTmp.get_remaining_time());
            ps.setInt(4, buffTmp.get_poly_id());

            ps.execute();

        } catch (final SQLException e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }
}
