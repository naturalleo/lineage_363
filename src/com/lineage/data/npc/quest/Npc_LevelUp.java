package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.clientpackets.C_CreateChar;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.model.skill.L1SkillMode;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_OwnCharStatus2;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.utils.CalcInitHpMp;
import com.lineage.server.utils.CalcStat;

/**
 * 英雄之魂 81004
 * 
 * @author dexc
 * 
 */
public class Npc_LevelUp extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_LevelUp.class);

    /**
     * 转生尊者
     */
    private Npc_LevelUp() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_LevelUp();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "yuleX1_1", null));
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        String[] info = null;
        if (cmd.equalsIgnoreCase("c")) {// 我想调整我的属性
            final L1ItemInstance item = pc.getInventory().checkItemX(49142, 1);
            if (item != null) {
                pc.get_otherList().clear_uplevelList();
                info = showInfo(pc, 2);

            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ySrc_E", info));
            }

        } else if (cmd.equalsIgnoreCase("s1")) {// 力量 +
            info = showInfoX(pc, 1);

        } else if (cmd.equalsIgnoreCase("s2")) {// 力量 -
            info = showInfoX(pc, 2);

        } else if (cmd.equalsIgnoreCase("d1")) {// 敏捷 +
            info = showInfoX(pc, 3);

        } else if (cmd.equalsIgnoreCase("d2")) {// 敏捷 -
            info = showInfoX(pc, 4);

        } else if (cmd.equalsIgnoreCase("c1")) {// 体质 +
            info = showInfoX(pc, 5);

        } else if (cmd.equalsIgnoreCase("c2")) {// 体质 -
            info = showInfoX(pc, 6);

        } else if (cmd.equalsIgnoreCase("w1")) {// 精神 +
            info = showInfoX(pc, 7);

        } else if (cmd.equalsIgnoreCase("w2")) {// 精神 -
            info = showInfoX(pc, 8);

        } else if (cmd.equalsIgnoreCase("i1")) {// 智力 +
            info = showInfoX(pc, 9);

        } else if (cmd.equalsIgnoreCase("i2")) {// 智力 -
            info = showInfoX(pc, 10);

        } else if (cmd.equalsIgnoreCase("h1")) {// 魅力 +
            info = showInfoX(pc, 11);

        } else if (cmd.equalsIgnoreCase("h2")) {// 魅力 -
            info = showInfoX(pc, 12);

        } else if (cmd.equalsIgnoreCase("x")) {// 我决定好了(出生点数)
            // 49142 回忆蜡烛
            final L1ItemInstance item = pc.getInventory().checkItemX(49142, 1);
            if (item != null) {
                int elixirStats = pc.get_otherList().get_uplevelList(0);
                if (elixirStats > 0) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "yuleX4",
                            new String[] { String.valueOf(elixirStats) }));
                    return;
                }
                // 技能解除/装备解除
                Npc_LevelUp.stopSkill(pc);

                info = showInfo(pc, 0);

            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ySrc_E", info));
            }

        } else if (cmd.equalsIgnoreCase("b")) {// 我决定好了(升级点数)
            // 49142 回忆蜡烛
            final L1ItemInstance item = pc.getInventory().checkItemX(49142, 1);
            if (item != null) {
                int elixirStats = pc.get_otherList().get_uplevelList(0);
                if (elixirStats > 0) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "yuleX4",
                            new String[] { String.valueOf(elixirStats) }));
                    return;
                }
                // 技能解除/装备解除
                Npc_LevelUp.stopSkill(pc);

                info = showInfo(pc, 1);

            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ySrc_E", info));
            }

        } else if (cmd.equalsIgnoreCase("d")) {// 我决定好了(万能药点数)
            // 49142 回忆蜡烛
            final L1ItemInstance item = pc.getInventory().checkItemX(49142, 1);
            if (item != null) {
                // 取回升级点数可分配剩余数量
                int elixirStats = pc.get_otherList().get_uplevelList(0);
                if (elixirStats > 0) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "yuleX4",
                            new String[] { String.valueOf(elixirStats) }));
                    return;
                }
                // 技能解除/装备解除
                Npc_LevelUp.stopSkill(pc);

                pc.getInventory().removeItem(item, 1);// 删除道具

                final int hp = CalcInitHpMp.calcInitHp(pc);
                final int mp = CalcInitHpMp.calcInitMp(pc);

                int baseStr = pc.getBaseStr();
                int baseDex = pc.getBaseDex();
                int baseCon = pc.getBaseCon();
                int baseWis = pc.getBaseWis();
                int baseInt = pc.getBaseInt();
                int baseCha = pc.getBaseCha();

                pc.addBaseStr(-baseStr);
                pc.addBaseDex(-baseDex);
                pc.addBaseCon(-baseCon);
                pc.addBaseWis(-baseWis);
                pc.addBaseInt(-baseInt);
                pc.addBaseCha(-baseCha);

                int originalStr = pc.get_otherList().get_uplevelList(1);
                int originalDex = pc.get_otherList().get_uplevelList(2);
                int originalCon = pc.get_otherList().get_uplevelList(3);
                int originalWis = pc.get_otherList().get_uplevelList(4);
                int originalInt = pc.get_otherList().get_uplevelList(5);
                int originalCha = pc.get_otherList().get_uplevelList(6);

                int addStr = pc.get_otherList().get_uplevelList(7);
                int addDex = pc.get_otherList().get_uplevelList(8);
                int addCon = pc.get_otherList().get_uplevelList(9);
                int addWis = pc.get_otherList().get_uplevelList(10);
                int addInt = pc.get_otherList().get_uplevelList(11);
                int addCha = pc.get_otherList().get_uplevelList(12);

                pc.addBaseStr((byte) ((addStr + originalStr) - 1));
                pc.addBaseDex((byte) ((addDex + originalDex) - 1));
                pc.addBaseCon((byte) ((addCon + originalCon) - 1));
                pc.addBaseWis((byte) ((addWis + originalWis) - 1));
                pc.addBaseInt((byte) ((addInt + originalInt) - 1));
                pc.addBaseCha((byte) ((addCha + originalCha) - 1));

                // 变更原始素质设定
                pc.setOriginalStr(pc.get_otherList().get_newPcOriginal()[0]);
                pc.setOriginalDex(pc.get_otherList().get_newPcOriginal()[1]);
                pc.setOriginalCon(pc.get_otherList().get_newPcOriginal()[2]);
                pc.setOriginalWis(pc.get_otherList().get_newPcOriginal()[3]);
                pc.setOriginalInt(pc.get_otherList().get_newPcOriginal()[4]);
                pc.setOriginalCha(pc.get_otherList().get_newPcOriginal()[5]);

                pc.addMr(0 - pc.getMr());
                pc.addDmgup(0 - pc.getDmgup());
                pc.addHitup(0 - pc.getHitup());
                pc.addBaseMaxHp((short) (hp - pc.getBaseMaxHp()));
                pc.addBaseMaxMp((short) (mp - pc.getBaseMaxMp()));

                if (pc.getOriginalAc() > 0) {
                    pc.addAc(pc.getOriginalAc());
                }

                if (pc.getOriginalMr() > 0) {
                    pc.addMr(0 - pc.getOriginalMr());
                }

                // 全属性重置
                pc.refresh();

                setHpMp(pc);

                pc.setCurrentHp(pc.getMaxHp());
                pc.setCurrentMp(pc.getMaxMp());

                // //////////////////////
                try {
                    // 纪录人物初始化资料
                    CharacterTable.saveCharStatus(pc);

                    pc.sendPackets(new S_OwnCharStatus2(pc));
                    // 更新角色防御属性
                    pc.sendPackets(new S_OwnCharAttrDef(pc));
                    // 更改人物状态
                    pc.sendPackets(new S_OwnCharStatus(pc));
                    // 更改人物魔法攻击与魔法防御
                    pc.sendPackets(new S_SPMR(pc));
                    pc.save();

                } catch (final Exception e) {
                    _log.error(e.getLocalizedMessage(), e);
                }
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7625));

            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ySrc_E", info));
            }
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
            // 清空属性重置处理清单
            pc.get_otherList().clear_uplevelList();
        }

        if (info != null) {
            int xmode = pc.get_otherList().get_uplevelList(13);
            switch (xmode) {
                case 0:// 0:升级点数
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "yuleX2",
                            info));
                    break;

                case 1:// 1:万能药点数
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "yuleX2_1",
                            info));
                    break;

                case 2:// 2:人物出生数值
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ySrc_1",
                            info));
                    break;
            }
        }
    }

    /**
     * 重新计算HP MP
     * 
     * @param pc
     *            执行人物
     */
    public static void setHpMp(final L1PcInstance pc) {
        for (int i = 0; i < pc.getLevel(); i++) {
            final short randomHp = CalcStat.calcStatHp(pc.getType(),
                    pc.getBaseMaxHp(), pc.getBaseCon(), pc.getOriginalHpup());

            final short randomMp = CalcStat.calcStatMp(pc.getType(),
                    pc.getBaseMaxMp(), pc.getBaseWis(), pc.getOriginalMpup());

            pc.addBaseMaxHp(randomHp);
            pc.addBaseMaxMp(randomMp);
        }
    }

    /**
     * 开始选取点数分配
     * 
     * @param pc
     * @param mode
     *            1 力量 + 2 力量 - 3 敏捷 + 4 敏捷 - 5 体质 + 6 体质 - 7 精神 + 8 精神 - 9 智力 +
     *            10 智力 - 11 魅力 + 12 魅力 -
     * @return
     */
    public static String[] showInfoX(final L1PcInstance pc, final int mode) {
        int xmode = pc.get_otherList().get_uplevelList(13);
        int max[] = new int[] { 0, 0, 0, 0, 0, 0 };
        switch (xmode) {
            case 0:// 0:升级点
                max = new int[] { ConfigAlt.POWER, ConfigAlt.POWER,
                        ConfigAlt.POWER, ConfigAlt.POWER, ConfigAlt.POWER,
                        ConfigAlt.POWER };// ConfigAlt.POWER;
                break;

            case 1:// 1:万能药
                max = new int[] { ConfigAlt.POWERMEDICINE,
                        ConfigAlt.POWERMEDICINE, ConfigAlt.POWERMEDICINE,
                        ConfigAlt.POWERMEDICINE, ConfigAlt.POWERMEDICINE,
                        ConfigAlt.POWERMEDICINE };// ConfigAlt.POWERMEDICINE;
                break;

            case 2:// 2:人物出生点数
                int type = pc.getType();
                // 各职业初始化属性可分配最大值(力量,敏捷,体质,精神,魅力,智力)
                switch (type) {
                    case 0:// 0:王族
                        max = new int[] { 20, 18, 18, 18, 18, 18 };
                        break;

                    case 1:// 1:骑士
                        max = new int[] { 20, 16, 18, 13, 12, 16 };
                        break;

                    case 2:// 2:精灵
                        max = new int[] { 18, 18, 18, 18, 18, 16 };
                        break;

                    case 3:// 3:法师
                        max = new int[] { 20, 14, 18, 18, 18, 18 };
                        break;

                    case 4:// 4:黑妖
                        max = new int[] { 18, 18, 18, 18, 18, 18 };
                        break;

                    case 5:// 5:龙骑士
                        max = new int[] { 19, 16, 18, 17, 17, 14 };
                        break;

                    case 6:// 6:幻术师
                        max = new int[] { 19, 16, 18, 18, 18, 18 };
                        break;
                }

                break;
        }

        int elixirStats = pc.get_otherList().get_uplevelList(0);

        int originalStr = pc.get_otherList().get_uplevelList(1);
        int originalDex = pc.get_otherList().get_uplevelList(2);
        int originalCon = pc.get_otherList().get_uplevelList(3);
        int originalWis = pc.get_otherList().get_uplevelList(4);
        int originalInt = pc.get_otherList().get_uplevelList(5);
        int originalCha = pc.get_otherList().get_uplevelList(6);

        int addStr = pc.get_otherList().get_uplevelList(7);
        int addDex = pc.get_otherList().get_uplevelList(8);
        int addCon = pc.get_otherList().get_uplevelList(9);
        int addWis = pc.get_otherList().get_uplevelList(10);
        int addInt = pc.get_otherList().get_uplevelList(11);
        int addCha = pc.get_otherList().get_uplevelList(12);

        switch (mode) {
            case 1:// 力量 +
                elixirStats -= 1;
                if (elixirStats < 0) {
                    return null;
                }
                addStr += 1;
                if (addStr > (max[0] - originalStr) || addStr < 0) {
                    return null;
                }
                pc.get_otherList().add_levelList(7, addStr);
                break;

            case 2:// 力量 -
                elixirStats += 1;
                if (elixirStats < 0) {
                    return null;
                }
                addStr -= 1;
                if (addStr > (max[0] - originalStr) || addStr < 0) {
                    return null;
                }
                pc.get_otherList().add_levelList(7, addStr);
                break;

            case 3:// 敏捷 +
                elixirStats -= 1;
                if (elixirStats < 0) {
                    return null;
                }
                addDex += 1;
                if (addDex > (max[1] - originalDex) || addDex < 0) {
                    return null;
                }
                pc.get_otherList().add_levelList(8, addDex);
                break;

            case 4:// 敏捷 -
                elixirStats += 1;
                if (elixirStats < 0) {
                    return null;
                }
                addDex -= 1;
                if (addDex > (max[1] - originalDex) || addDex < 0) {
                    return null;
                }
                pc.get_otherList().add_levelList(8, addDex);
                break;

            case 5:// 体质 +
                elixirStats -= 1;
                if (elixirStats < 0) {
                    return null;
                }
                addCon += 1;
                if (addCon > (max[2] - originalCon) || addCon < 0) {
                    return null;
                }
                pc.get_otherList().add_levelList(9, addCon);
                break;

            case 6:// 体质 -
                elixirStats += 1;
                if (elixirStats < 0) {
                    return null;
                }
                addCon -= 1;
                if (addCon > (max[2] - originalCon) || addCon < 0) {
                    return null;
                }
                pc.get_otherList().add_levelList(9, addCon);
                break;

            case 7:// 精神 +
                elixirStats -= 1;
                if (elixirStats < 0) {
                    return null;
                }
                addWis += 1;
                if (addWis > (max[3] - originalWis) || addWis < 0) {
                    return null;
                }
                pc.get_otherList().add_levelList(10, addWis);
                break;

            case 8:// 精神 -
                elixirStats += 1;
                if (elixirStats < 0) {
                    return null;
                }
                addWis -= 1;
                if (addWis > (max[3] - originalWis) || addWis < 0) {
                    return null;
                }
                pc.get_otherList().add_levelList(10, addWis);
                break;

            case 9:// 智力 +
                elixirStats -= 1;
                if (elixirStats < 0) {
                    return null;
                }
                addInt += 1;
                if (addInt > (max[4] - originalInt) || addInt < 0) {
                    return null;
                }
                pc.get_otherList().add_levelList(11, addInt);
                break;

            case 10:// 智力 -
                elixirStats += 1;
                if (elixirStats < 0) {
                    return null;
                }
                addInt -= 1;
                if (addInt > (max[4] - originalInt) || addInt < 0) {
                    return null;
                }
                pc.get_otherList().add_levelList(11, addInt);
                break;

            case 11:// 魅力 +
                elixirStats -= 1;
                if (elixirStats < 0) {
                    return null;
                }
                addCha += 1;
                if (addCha > (max[5] - originalCha) || addCha < 0) {
                    return null;
                }
                pc.get_otherList().add_levelList(12, addCha);
                break;

            case 12:// 魅力 -
                elixirStats += 1;
                if (elixirStats < 0) {
                    return null;
                }
                addCha -= 1;
                if (addCha > (max[5] - originalCha) || addCha < 0) {
                    return null;
                }
                pc.get_otherList().add_levelList(12, addCha);
                break;
        }

        // 更新剩余点数
        pc.get_otherList().add_levelList(0, elixirStats);

        return info(pc);
    }

    /**
     * 初始化点选
     * 
     * @param pc
     *            执行人物
     * @param isBonus
     *            0:升级点 1:万能药 2:人物出生点数
     * @return
     */
    public static String[] showInfo(final L1PcInstance pc, final int mode) {
        int elixirStats = 0;

        // 取回原始数值
        int originalStr = 0;
        int originalDex = 0;
        int originalCon = 0;
        int originalWis = 0;
        int originalInt = 0;
        int originalCha = 0;

        int type = pc.getType();
        // 各职业初始化属性(王族, 骑士, 精灵, 法师, 黑妖, 龙骑士, 幻术师)
        originalStr = C_CreateChar.ORIGINAL_STR[type];
        originalDex = C_CreateChar.ORIGINAL_DEX[type];
        originalCon = C_CreateChar.ORIGINAL_CON[type];
        originalWis = C_CreateChar.ORIGINAL_WIS[type];
        originalInt = C_CreateChar.ORIGINAL_INT[type];
        originalCha = C_CreateChar.ORIGINAL_CHA[type];

        switch (mode) {
            case 0:// 0:升级点
                   // 加上升级属性提升值
                if (pc.getBonusStats() > 0) {
                    elixirStats += pc.getBonusStats();
                }

                // 升级属性提升值重选(13-0)
                pc.get_otherList().add_levelList(13, 0);
                break;

            case 1:// 1:万能药
                   // 加上万能药使用次数
                if (pc.getElixirStats() > 0) {
                    elixirStats += pc.getElixirStats();
                }
                originalStr = pc.get_otherList().get_uplevelList(1);
                originalDex = pc.get_otherList().get_uplevelList(2);
                originalCon = pc.get_otherList().get_uplevelList(3);
                originalWis = pc.get_otherList().get_uplevelList(4);
                originalInt = pc.get_otherList().get_uplevelList(5);
                originalCha = pc.get_otherList().get_uplevelList(6);

                // 万能药使用次数重选(13-1)
                pc.get_otherList().add_levelList(13, 1);
                break;

            case 2:// 2:人物出生点数

                // 各职业初始化可分配点数(王族, 骑士, 精灵, 法师, 黑妖, 龙骑士, 幻术师)
                elixirStats = C_CreateChar.ORIGINAL_AMOUNT[type];

                // 人物出生点数重选(13-2)
                pc.get_otherList().add_levelList(13, 2);
                break;
        }

        // 更新剩余点数
        pc.get_otherList().add_levelList(0, elixirStats);

        switch (mode) {
            case 0:// 0:升级点
                   // 取回上一阶段(人物出生点数)所决定的点数
                int addStrS = pc.get_otherList().get_uplevelList(7);
                int addDexS = pc.get_otherList().get_uplevelList(8);
                int addConS = pc.get_otherList().get_uplevelList(9);
                int addWisS = pc.get_otherList().get_uplevelList(10);
                int addIntS = pc.get_otherList().get_uplevelList(11);
                int addChaS = pc.get_otherList().get_uplevelList(12);

                pc.get_otherList().add_levelList(1, originalStr + addStrS);// 力量
                                                                           // (原始)
                                                                           // +
                                                                           // 上一阶段(人物出生点数)所决定的点数
                pc.get_otherList().add_levelList(2, originalDex + addDexS);// 敏捷
                                                                           // (原始)
                                                                           // +
                                                                           // 上一阶段(人物出生点数)所决定的点数
                pc.get_otherList().add_levelList(3, originalCon + addConS);// 体质
                                                                           // (原始)
                                                                           // +
                                                                           // 上一阶段(人物出生点数)所决定的点数
                pc.get_otherList().add_levelList(4, originalWis + addWisS);// 精神
                                                                           // (原始)
                                                                           // +
                                                                           // 上一阶段(人物出生点数)所决定的点数
                pc.get_otherList().add_levelList(5, originalInt + addIntS);// 智力
                                                                           // (原始)
                                                                           // +
                                                                           // 上一阶段(人物出生点数)所决定的点数
                pc.get_otherList().add_levelList(6, originalCha + addChaS);// 魅力
                                                                           // (原始)
                                                                           // +
                                                                           // 上一阶段(人物出生点数)所决定的点数

                // 暂存人物原始素质改变
                pc.get_otherList().set_newPcOriginal(
                        new int[] { originalStr + addStrS,// 力量 (原始) +
                                                          // 上一阶段(人物出生点数)所决定的点数
                                originalDex + addDexS,// 敏捷 (原始) +
                                                      // 上一阶段(人物出生点数)所决定的点数
                                originalCon + addConS,// 体质 (原始) +
                                                      // 上一阶段(人物出生点数)所决定的点数
                                originalWis + addWisS,// 精神 (原始) +
                                                      // 上一阶段(人物出生点数)所决定的点数
                                originalInt + addIntS,// 智力 (原始) +
                                                      // 上一阶段(人物出生点数)所决定的点数
                                originalCha + addChaS,// 魅力 (原始)
                        });
                break;

            case 1:// 1:万能药
                   // 取回上一阶段(升级属性提升值)所决定的点数
                int addStrR = pc.get_otherList().get_uplevelList(7);
                int addDexR = pc.get_otherList().get_uplevelList(8);
                int addConR = pc.get_otherList().get_uplevelList(9);
                int addWisR = pc.get_otherList().get_uplevelList(10);
                int addIntR = pc.get_otherList().get_uplevelList(11);
                int addChaR = pc.get_otherList().get_uplevelList(12);

                pc.get_otherList().add_levelList(1, originalStr + addStrR);// 力量
                                                                           // (原始)
                                                                           // +
                                                                           // 上一阶段(升级属性提升值)所决定的点数
                pc.get_otherList().add_levelList(2, originalDex + addDexR);// 敏捷
                                                                           // (原始)
                                                                           // +
                                                                           // 上一阶段(升级属性提升值)所决定的点数
                pc.get_otherList().add_levelList(3, originalCon + addConR);// 体质
                                                                           // (原始)
                                                                           // +
                                                                           // 上一阶段(升级属性提升值)所决定的点数
                pc.get_otherList().add_levelList(4, originalWis + addWisR);// 精神
                                                                           // (原始)
                                                                           // +
                                                                           // 上一阶段(升级属性提升值)所决定的点数
                pc.get_otherList().add_levelList(5, originalInt + addIntR);// 智力
                                                                           // (原始)
                                                                           // +
                                                                           // 上一阶段(升级属性提升值)所决定的点数
                pc.get_otherList().add_levelList(6, originalCha + addChaR);// 魅力
                                                                           // (原始)
                                                                           // +
                                                                           // 上一阶段(升级属性提升值)所决定的点数
                break;

            case 2:// 2:人物出生点数
                pc.get_otherList().add_levelList(1, originalStr);// 力量 (原始)
                pc.get_otherList().add_levelList(2, originalDex);// 敏捷 (原始)
                pc.get_otherList().add_levelList(3, originalCon);// 体质 (原始)
                pc.get_otherList().add_levelList(4, originalWis);// 精神 (原始)
                pc.get_otherList().add_levelList(5, originalInt);// 智力 (原始)
                pc.get_otherList().add_levelList(6, originalCha);// 魅力 (原始)
                break;
        }

        // 归零 上一阶段(升级属性提升值)所决定的点数
        int addStr = 0;
        pc.get_otherList().add_levelList(7, addStr);
        int addDex = 0;
        pc.get_otherList().add_levelList(8, addDex);
        int addCon = 0;
        pc.get_otherList().add_levelList(9, addCon);
        int addWis = 0;
        pc.get_otherList().add_levelList(10, addWis);
        int addInt = 0;
        pc.get_otherList().add_levelList(11, addInt);
        int addCha = 0;
        pc.get_otherList().add_levelList(12, addCha);

        return info(pc);
    }

    /**
     * 传回显示于HTML的文字
     * 
     * @param pc
     * @return
     */
    private static String[] info(final L1PcInstance pc) {
        String[] info = null;
        int p1 = pc.get_otherList().get_uplevelList(0);// 剩余可用点数

        int p2 = pc.get_otherList().get_uplevelList(1);// 力量 (原始)
        int p3 = pc.get_otherList().get_uplevelList(7);// 力量 +-

        int p4 = pc.get_otherList().get_uplevelList(2);// 敏捷 (原始)
        int p5 = pc.get_otherList().get_uplevelList(8);// 敏捷 +-

        int p6 = pc.get_otherList().get_uplevelList(3);// 体质 (原始)
        int p7 = pc.get_otherList().get_uplevelList(9);// 体质 +-

        int p8 = pc.get_otherList().get_uplevelList(4);// 精神 (原始)
        int p9 = pc.get_otherList().get_uplevelList(10);// 精神 +-

        int p10 = pc.get_otherList().get_uplevelList(5);// 智力 (原始)
        int p11 = pc.get_otherList().get_uplevelList(11);// 智力 +-

        int p12 = pc.get_otherList().get_uplevelList(6);// 魅力 (原始)
        int p13 = pc.get_otherList().get_uplevelList(12);// 魅力 +-

        int xmode = pc.get_otherList().get_uplevelList(13);
        if (xmode == 2) {
            int type = pc.getType();

            // 各职业初始化可分配点数(王族, 骑士, 精灵, 法师, 黑妖, 龙骑士, 幻术师)
            int elixirStats = C_CreateChar.ORIGINAL_AMOUNT[type];

            String nameid = "";
            // 各职业初始化属性(王族, 骑士, 精灵, 法师, 黑妖, 龙骑士, 幻术师)
            switch (type) {
                case 0:// 0:王族
                       // 1127：[王族]
                    nameid = "$1127";
                    break;

                case 1:// 1:骑士
                       // 1128：[骑士]
                    nameid = "$1128";
                    break;

                case 2:// 2:精灵
                       // 1129：[妖精]
                    nameid = "$1129";
                    break;

                case 3:// 3:法师
                       // 1130：[法师]
                    nameid = "$1130";
                    break;

                case 4:// 4:黑妖
                       // 2503：[黑暗妖精]
                    nameid = "$2503";
                    break;

                case 5:// 5:龙骑士
                       // 5889：[龙骑士]
                    nameid = "$5889";
                    break;

                case 6:// 6:幻术师
                       // 5890：[幻术士]
                    nameid = "$5890";
                    break;
            }
            info = new String[] { nameid, String.valueOf(elixirStats),

            String.valueOf(p1),

            String.valueOf(p2 < 10 ? "0" + p2 : p2),
                    String.valueOf(p3 < 10 ? "0" + p3 : p3),

                    String.valueOf(p4 < 10 ? "0" + p4 : p4),
                    String.valueOf(p5 < 10 ? "0" + p5 : p5),

                    String.valueOf(p6 < 10 ? "0" + p6 : p6),
                    String.valueOf(p7 < 10 ? "0" + p7 : p7),

                    String.valueOf(p8 < 10 ? "0" + p8 : p8),
                    String.valueOf(p9 < 10 ? "0" + p9 : p9),

                    String.valueOf(p10 < 10 ? "0" + p10 : p10),
                    String.valueOf(p11 < 10 ? "0" + p11 : p11),

                    String.valueOf(p12 < 10 ? "0" + p12 : p12),
                    String.valueOf(p13 < 10 ? "0" + p13 : p13), };

        } else {
            info = new String[] { String.valueOf(p1),

            String.valueOf(p2 < 10 ? "0" + p2 : p2),
                    String.valueOf(p3 < 10 ? "0" + p3 : p3),

                    String.valueOf(p4 < 10 ? "0" + p4 : p4),
                    String.valueOf(p5 < 10 ? "0" + p5 : p5),

                    String.valueOf(p6 < 10 ? "0" + p6 : p6),
                    String.valueOf(p7 < 10 ? "0" + p7 : p7),

                    String.valueOf(p8 < 10 ? "0" + p8 : p8),
                    String.valueOf(p9 < 10 ? "0" + p9 : p9),

                    String.valueOf(p10 < 10 ? "0" + p10 : p10),
                    String.valueOf(p11 < 10 ? "0" + p11 : p11),

                    String.valueOf(p12 < 10 ? "0" + p12 : p12),
                    String.valueOf(p13 < 10 ? "0" + p13 : p13), };
        }
        return info;
    }

    /**
     * 技能解除/装备解除
     * 
     * @param pc
     */
    public static void stopSkill(final L1PcInstance pc) {
        // 使用牛的代号脱除全部装备
        pc.getInventory().takeoffEquip(945);

        // 技能解除
        for (int skillNum = L1SkillId.SKILLS_BEGIN; skillNum <= L1SkillId.SKILLS_END; skillNum++) {
            if (L1SkillMode.get().isNotCancelable(skillNum) && !pc.isDead()) {
                continue;
            }
            pc.removeSkillEffect(skillNum);
        }

        pc.curePoison();
        pc.cureParalaysis();
        for (int skillNum = L1SkillId.STATUS_BEGIN; skillNum <= L1SkillId.STATUS_END; skillNum++) {
            pc.removeSkillEffect(skillNum);
        }

        // 料理解除
        for (int skillNum = L1SkillId.COOKING_BEGIN; skillNum <= L1SkillId.COOKING_END; skillNum++) {
            if (L1SkillMode.get().isNotCancelable(skillNum)) {
                continue;
            }
            pc.removeSkillEffect(skillNum);
        }

        if (pc.getHasteItemEquipped() > 0) {
            pc.setMoveSpeed(0);
            pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));
        }

        pc.sendPacketsAll(new S_CharVisualUpdate(pc));
    }
}
