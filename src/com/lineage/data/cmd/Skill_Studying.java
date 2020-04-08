package com.lineage.data.cmd;

import com.lineage.server.ActionCodes;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_AddSkill;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_HPUpdate;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.templates.L1Skills;

/**
 * 技能学习成功与否的判断
 * 
 * @author dexc
 * 
 */
public class Skill_Studying implements Skill_StudyingExecutor {

    /**
     * 学习技能等级限制 与地图位置判断
     * 
     * @param pc
     *            人物
     * @param skillId
     *            技能编号
     * @param magicAtt
     *            技能等级分组<BR>
     *            1~10共同魔法<BR>
     *            11~20精灵魔法<BR>
     *            21~30王族魔法<BR>
     *            31~40骑士魔法<BR>
     *            41~50黑暗精灵魔法<BR>
     *            51~60龙骑士魔法<BR>
     *            61~70幻术师魔法<BR>
     * 
     * @param attribute
     *            技能属性<BR>
     *            0:中立属性魔法<BR>
     *            1:正义属性魔法<BR>
     *            2:邪恶属性魔法<BR>
     *            3:精灵专属魔法<BR>
     *            4:王族专属魔法<BR>
     *            5:骑士专属技能<BR>
     *            6:黑暗精灵专属魔法<BR>
     *            7:龙骑士专属魔法<BR>
     *            8:幻术师专属魔法<BR>
     * 
     * @param itemObj
     *            道具objid(点选的物品)
     */
    @Override
    public void magic(final L1PcInstance pc, final int skillId,
            final int magicAtt, final int attribute, final int itemObj) {
        // 人物等级
        final int pclvl = pc.getLevel();
        // 是否足够等级学习
        boolean isUse = true;

        // TODO 王族
        if (pc.isCrown()) {
            switch (magicAtt) {
                case 1:
                    if (pclvl >= 10) {// magic lv1
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 2:
                    if (pclvl >= 20) {// magic lv2
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 21:
                    if (pclvl >= 15) {// Crown magic lv1
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 22:
                    if (pclvl >= 30) {// Crown magic lv2
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 23:
                    if (pclvl >= 40) {// Crown magic lv3
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 24:
                    if (pclvl >= 45) {// Crown magic lv4
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 25:
                    if (pclvl >= 50) {// Crown magic lv5
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 26:
                    if (pclvl >= 55) {// Crown magic lv6
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                default:
                    // 79 没有任何事情发生
                    final S_ServerMessage msg = new S_ServerMessage(79);
                    pc.sendPackets(msg);
                    break;
            }

            // TODO 骑士
        } else if (pc.isKnight()) {
            switch (magicAtt) {
                case 1:
                    if (pclvl >= 50) {// magic lv1
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 31:
                    if (pclvl >= 50) {// Knight magic lv1
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 32:
                    if (pclvl >= 60) {// Knight magic lv2
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                default:
                    // 79 没有任何事情发生
                    final S_ServerMessage msg = new S_ServerMessage(79);
                    pc.sendPackets(msg);
                    break;
            }

            // TODO 法师
        } else if (pc.isWizard()) {
            switch (magicAtt) {
                case 1:// magic lv1
                    if (pclvl >= 4) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 2:// magic lv2
                    if (pclvl >= 8) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 3:// magic lv3
                    if (pclvl >= 12) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 4:// magic lv4
                    if (pclvl >= 16) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 5:// magic lv5
                    if (pclvl >= 20) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 6:// magic lv6
                    if (pclvl >= 24) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 7:// magic lv7
                    if (pclvl >= 28) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 8:// magic lv8
                    if (pclvl >= 32) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 9:// magic lv9
                    if (pclvl >= 36) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 10:// magic lv10
                    if (pclvl >= 40) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                default:
                    // 79 没有任何事情发生
                    final S_ServerMessage msg = new S_ServerMessage(79);
                    pc.sendPackets(msg);
                    break;
            }

            // TODO 精灵
        } else if (pc.isElf()) {
            switch (magicAtt) {
                case 1:// magic lv1
                    if (pclvl >= 8) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 2:// magic lv2
                    if (pclvl >= 16) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 3:// magic lv3
                    if (pclvl >= 24) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 4:// magic lv4
                    if (pclvl >= 32) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 5:// magic lv5
                    if (pclvl >= 40) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 6:// magic lv6
                    if (pclvl >= 48) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 11:// elf magic lv1
                    if (pclvl >= 10) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 12:// elf magic lv2
                    if (pclvl >= 20) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 13:// elf magic lv3
                    if (pclvl >= 30) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 14:// elf magic lv4
                    if (pclvl >= 40) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 15:// elf magic lv5
                    if (pclvl >= 50) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                default:
                    // 79 没有任何事情发生
                    final S_ServerMessage msg = new S_ServerMessage(79);
                    pc.sendPackets(msg);
                    break;
            }

            // TODO 黑暗精灵
        } else if (pc.isDarkelf()) {
            switch (magicAtt) {
                case 1:// magic lv1
                    if (pclvl >= 12) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 2:// magic lv2
                    if (pclvl >= 24) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 41:// Darkelf magic lv1
                    if (pclvl >= 15) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 42:// Darkelf magic lv2
                    if (pclvl >= 30) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 43:// Darkelf magic lv3
                    if (pclvl >= 45) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                default:
                    // 79 没有任何事情发生
                    final S_ServerMessage msg = new S_ServerMessage(79);
                    pc.sendPackets(msg);
                    break;
            }

            // TODO 龙骑士
        } else if (pc.isDragonKnight()) {
            switch (magicAtt) {
                case 51:
                    if (pclvl >= 15) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 52:
                    if (pclvl >= 30) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 53:
                    if (pclvl >= 45) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                default:
                    // 79 没有任何事情发生
                    final S_ServerMessage msg = new S_ServerMessage(79);
                    pc.sendPackets(msg);
                    break;
            }

            // TODO 幻术师
        } else if (pc.isIllusionist()) {
            switch (magicAtt) {
                case 61:
                    if (pclvl >= 10) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 62:
                    if (pclvl >= 20) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 63:
                    if (pclvl >= 30) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                case 64:
                    if (pclvl >= 40) {
                        this.mapPosition(pc, skillId, attribute, itemObj);

                    } else {
                        isUse = false;
                    }
                    break;

                default:
                    // 79 没有任何事情发生
                    final S_ServerMessage msg = new S_ServerMessage(79);
                    pc.sendPackets(msg);
                    break;
            }

        }

        if (!isUse) {
            // 312 你还不能学习法术。
            final S_ServerMessage msg = new S_ServerMessage(312);
            pc.sendPackets(msg);
        }
    }

    /**
     * 学习法术 人物位置判断
     * 
     * @param pc
     *            人物
     * @param skillId
     *            技能编号
     * @param attribute
     *            技能属性 0:中立属性魔法<BR>
     *            1:正义属性魔法<BR>
     *            2:邪恶属性魔法<BR>
     *            3:精灵专属魔法<BR>
     *            4:王族专属魔法<BR>
     *            5:骑士专属技能<BR>
     *            6:黑暗精灵专属魔法<BR>
     *            7:龙骑士专属魔法<BR>
     *            8:幻术师专属魔法<BR>
     * 
     * @param itemObj
     *            道具objid(点选的物品)
     */
    private void mapPosition(final L1PcInstance pc, final int skillId,
            final int attribute, final int itemObj) {
        // 要求使用正确学习点
        final boolean isOk = true;

        if (isOk) {
            final int x = pc.getX();
            final int y = pc.getY();
            final int m = pc.getMapId();
            switch (attribute) {
                case 0:// 中立属性魔法
                    if (((x > 32880) && (x < 32892) && (y > 32646)
                            && (y < 32658) && (m == 4 // 邪恶神殿(然柳)
                    ))
                            || ((x > 32662) && (x < 32674) && (y > 32297)
                                    && (y < 32309) && (m == 4 // 邪恶神殿(然柳)
                            ))
                            || ((x > 33135) && (x < 33146) && (y > 32232)
                                    && (y < 32249) && (m == 4 // 正义神殿(妖森)
                            ))
                            || ((x > 33116) && (x < 33128) && (y > 32930)
                                    && (y < 32942) && (m == 4 // 正义神殿(肯特)
                            ))
                            || ((x > 32791) && (x < 32796) && (y > 32842)
                                    && (y < 32848) && (m == 76))) {// 正义神殿(象牙塔)

                        this.spellBook(pc, skillId, itemObj);

                    } else {
                        // 79 没有任何事情发生
                        final S_ServerMessage msg = new S_ServerMessage(79);
                        pc.sendPackets(msg);
                    }
                    break;

                case 1:// 正义属性魔法
                    if (((x > 33116) && (x < 33128) && (y > 32930)
                            && (y < 32942) && (m == 4 // 正义神殿(肯特)
                    ))
                            || ((x > 33135) && (x < 33146) && (y > 32232)
                                    && (y < 32249) && (m == 4 // 正义神殿(妖森)
                            ))
                            || ((x > 32791) && (x < 32796) && (y > 32842)
                                    && (y < 32848) && (m == 76))) {// 正义神殿(象牙塔)

                        this.spellBook(pc, skillId, itemObj);

                    } else if (((x > 32880) && (x < 32892) && (y > 32646)
                            && (y < 32658) && (m == 4 // 邪恶神殿(然柳)
                    ))
                            || ((x > 32662) && (x < 32674) && (y > 32297)
                                    && (y < 32309) && (m == 4))) {// 邪恶神殿(然柳)

                        // 删除道具(错误地点学习)
                        pc.getInventory().removeItem(
                                pc.getInventory().getItem(itemObj), 1);

                        // 随机数字范围(伤害力)
                        final short dmg = (short) ((Math.random() * 50) + 30);

                        // HP减少计算
                        pc.receiveDamage(pc, dmg, false, true);

                        if (pc.isInvisble()) {// 隐身状态
                            pc.delInvis(); // 解除隐身状态
                        }

                        // 产生动画封包(电击)
                        final S_SkillSound sound = new S_SkillSound(pc.getId(),
                                10);
                        pc.sendPacketsX8(sound);

                        // 产生动作封包(招受攻击)
                        final S_DoActionGFX pack = new S_DoActionGFX(
                                pc.getId(), ActionCodes.ACTION_Damage);
                        pc.sendPacketsX8(pack);

                        // HP 更新
                        final S_HPUpdate newHp = new S_HPUpdate(
                                pc.getCurrentHp(), pc.getMaxHp());
                        pc.sendPackets(newHp);
                        if (pc.isInParty()) { // 队伍中
                            pc.getParty().updateMiniHP(pc);// 更新队伍画面显示
                        }

                    } else {
                        // 79 没有任何事情发生
                        final S_ServerMessage msg = new S_ServerMessage(79);
                        pc.sendPackets(msg);
                    }
                    break;

                case 2:// 邪恶属性魔法
                    if (((x > 32880) && (x < 32892) && (y > 32646)
                            && (y < 32658) && (m == 4 // 邪恶神殿(然柳)
                    ))
                            || ((x > 32662) && (x < 32674) && (y > 32297)
                                    && (y < 32309) && (m == 4))) {// 邪恶神殿(然柳)

                        this.spellBook(pc, skillId, itemObj);

                    } else if (((x > 33116) && (x < 33128) && (y > 32930)
                            && (y < 32942) && (m == 4 // 正义神殿(肯特)
                    ))
                            || ((x > 33135) && (x < 33146) && (y > 32232)
                                    && (y < 32249) && (m == 4 // 正义神殿(妖森)
                            ))
                            || ((x > 32791) && (x < 32796) && (y > 32842)
                                    && (y < 32848) && (m == 76))) {// 正义神殿(象牙塔2F)

                        // 删除道具(错误地点学习)
                        pc.getInventory().removeItem(
                                pc.getInventory().getItem(itemObj), 1);

                        // 随机数字范围(伤害力)
                        final short dmg = (short) ((Math.random() * 50) + 30);

                        // HP减少计算
                        pc.receiveDamage(pc, dmg, false, true);

                        if (pc.isInvisble()) {// 隐身状态
                            pc.delInvis(); // 解除隐身状态
                        }

                        // 产生动画封包(电击)
                        final S_SkillSound sound = new S_SkillSound(pc.getId(),
                                10);
                        pc.sendPacketsX8(sound);

                        // 产生动作封包(招受攻击)
                        final S_DoActionGFX pack = new S_DoActionGFX(
                                pc.getId(), ActionCodes.ACTION_Damage);
                        pc.sendPacketsX8(pack);

                        // HP 更新
                        final S_HPUpdate newHp = new S_HPUpdate(
                                pc.getCurrentHp(), pc.getMaxHp());
                        pc.sendPackets(newHp);
                        if (pc.isInParty()) { // 队伍中
                            pc.getParty().updateMiniHP(pc);// 更新队伍画面显示
                        }

                    } else {
                        // 79 没有任何事情发生
                        final S_ServerMessage msg = new S_ServerMessage(79);
                        pc.sendPackets(msg);
                    }
                    break;

                case 3:// 精灵专属魔法
                    if (((x > 33049) && (x < 33061) && (y > 32330)
                            && (y < 32343) && (m == 4 // 世界树
                    ))
                            || ((x > 32788) && (x < 32794) && (y > 32847)
                                    && (y < 32853) && (m == 75))) {// 象牙塔1F

                        this.spellBook(pc, skillId, itemObj);

                    } else {
                        // 79 没有任何事情发生
                        final S_ServerMessage msg = new S_ServerMessage(79);
                        pc.sendPackets(msg);
                    }
                    break;

                case 4:// 王族专属魔法
                case 5:// 骑士专属技能
                case 6:// 黑暗精灵专属魔法
                case 7:// 龙骑士魔法
                case 8:// 幻术师魔法
                    this.spellBook(pc, skillId, itemObj);
                    break;
            }

        } else {
            this.spellBook(pc, skillId, itemObj);
        }
    }

    /**
     * 人物技能写入
     * 
     * @param pc
     *            使用物品的人物
     * @param skillId
     *            技能编号
     * @param itemObj
     *            点选的物品 objectId
     * 
     * @return
     */
    private void spellBook(final L1PcInstance pc, final int skillId,
            final int itemObj) {
        // 删除道具
        pc.getInventory().removeItem(pc.getInventory().getItem(itemObj), 1);

        // 更新技能画面显示
        pc.sendPackets(new S_AddSkill(pc, skillId));

        // 动画效果
        final char c = '\343';
        final S_SkillSound sound = new S_SkillSound(pc.getId(), c);

        pc.sendPacketsX8(sound);

        // 取得魔法资料
        final L1Skills skill = SkillsTable.get().getTemplate(skillId);
        // 取得技能名称
        final String skillName = skill.getName();

        // 写入人物技能资料库
        CharSkillReading.get().spellMastery(pc.getId(), skillId, skillName, 0,
                0);
    }
}
