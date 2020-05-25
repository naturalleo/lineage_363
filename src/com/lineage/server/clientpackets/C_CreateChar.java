package com.lineage.server.clientpackets;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.Config;
import com.lineage.config.ConfigAlt;
import com.lineage.echo.ClientExecutor;
import com.lineage.list.BadNamesList;
import com.lineage.server.IdFactory;
import com.lineage.server.datatables.BeginnerTable;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CharCreateStatus;
import com.lineage.server.serverpackets.S_NewCharPacket;
import com.lineage.server.templates.L1Account;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.utils.CalcInitHpMp;

/**
 * 要求创造角色
 * 
 * @author daien
 * 
 */
public class C_CreateChar extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_CreateChar.class);

    /*
     * public C_CreateChar() { }
     * 
     * public C_CreateChar(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    // 各职业初始化属性(王族, 骑士, 精灵, 法师, 黑妖, 龙骑士, 幻术师)
    public static final int[] ORIGINAL_STR = new int[] { 13, 16, 11, 8, 12, 13,
            11 };
    public static final int[] ORIGINAL_DEX = new int[] { 10, 12, 12, 7, 15, 11,
            10 };
    public static final int[] ORIGINAL_CON = new int[] { 10, 14, 12, 12, 8, 14,
            12 };
    public static final int[] ORIGINAL_WIS = new int[] { 11, 9, 12, 12, 10, 12,
            12 };
    public static final int[] ORIGINAL_CHA = new int[] { 13, 12, 9, 8, 9, 8, 8 };
    public static final int[] ORIGINAL_INT = new int[] { 10, 8, 12, 12, 11, 11,
            12 };
    // 各职业初始化可分配点数(王族, 骑士, 精灵, 法师, 黑妖, 龙骑士, 幻术师)
    public static final int[] ORIGINAL_AMOUNT = new int[] { 8, 4, 7, 16, 10, 6,
            10 };

    // 人物外型决定
    private static final int[][] CLASS_LIST = new int[][] {
            new int[] { 0, 61, 138, 734, 2786, 6658, 6671 },// 男性
            new int[] { 1, 48, 37, 1186, 2796, 6661, 6650 } // 女性
    };

    // 出生地点座标
    private static final int[][] LOC_LIST = new int[][] {
            new int[] { 32684, 32870, 2005 }, new int[] { 32686, 32867, 2005 },
            new int[] { 32691, 32864, 2005 }, new int[] { 32684, 32870, 2005 },
            new int[] { 32686, 32867, 2005 }, new int[] { 32691, 32864, 2005 },
            new int[] { 32691, 32864, 2005 }
    /*
     * new int[]{32780, 32781, 68}, new int[]{32714, 32877, 69}, new
     * int[]{32714, 32877, 69}, new int[]{32780, 32781, 68}, new int[]{32714,
     * 32877, 69}, new int[]{32714, 32877, 69}, new int[]{32714, 32877, 69}
     */
    };

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);
            final L1PcInstance pc = new L1PcInstance();
            String name = Matcher.quoteReplacement(this.readS());

            final L1Account account = client.getAccount();
            final int characterSlot = account.get_character_slot();
            final int maxAmount = ConfigAlt.DEFAULT_CHARACTER_SLOT
                    + characterSlot;

            name = name.replaceAll("\\s", "");
            name = name.replaceAll("　", "");

            if (name.length() == 0) {
                client.out().encrypt(
                        new S_CharCreateStatus(
                                S_CharCreateStatus.REASON_INVALID_NAME));
                return;
            }
            
            //hjx1000 限制输入只有中文
            if(!name.matches("[\\u4E00-\\u9FA5]+")) {
                client.out().encrypt(
                        new S_CharCreateStatus(
                                S_CharCreateStatus.REASON_INVALID_NAME));
                return;
            }

            // 名称是否包含禁止字元
            if (!isInvalidName(name)) {
                client.out().encrypt(
                        new S_CharCreateStatus(
                                S_CharCreateStatus.REASON_INVALID_NAME));
                return;
            }

            // 检查名称是否以被使用
            if (CharObjidTable.get().charObjid(name) != 0) {
                client.out().encrypt(
                        new S_CharCreateStatus(
                                S_CharCreateStatus.REASON_ALREADY_EXSISTS));
                return;
            }

            // 已创人物数量
            int countCharacters = client.getAccount().get_countCharacters();
            if (countCharacters >= maxAmount) {
                client.out().encrypt(
                        new S_CharCreateStatus(
                                S_CharCreateStatus.REASON_WRONG_AMOUNT));
                return;
            }

            pc.setName(name);
            pc.setType(this.readC());
            pc.set_sex(this.readC());
            pc.addBaseStr((byte) this.readC());
            pc.addBaseDex((byte) this.readC());
            pc.addBaseCon((byte) this.readC());
            pc.addBaseWis((byte) this.readC());
            pc.addBaseCha((byte) this.readC());
            pc.addBaseInt((byte) this.readC());
            if(pc.getType() >= 6){ // 去除龙骑士和幻术师
                client.out().encrypt(
                        new S_CharCreateStatus(
                                S_CharCreateStatus.REASON_INVALID_NAME));
                return;                
            }

            boolean isStatusError = false;
            final int originalStr = ORIGINAL_STR[pc.getType()];
            final int originalDex = ORIGINAL_DEX[pc.getType()];
            final int originalCon = ORIGINAL_CON[pc.getType()];
            final int originalWis = ORIGINAL_WIS[pc.getType()];
            final int originalCha = ORIGINAL_CHA[pc.getType()];
            final int originalInt = ORIGINAL_INT[pc.getType()];
            final int originalAmount = ORIGINAL_AMOUNT[pc.getType()];

            if (((pc.getBaseStr() < originalStr)
                    || (pc.getBaseDex() < originalDex)
                    || (pc.getBaseCon() < originalCon)
                    || (pc.getBaseWis() < originalWis)
                    || (pc.getBaseCha() < originalCha) || (pc.getBaseInt() < originalInt))
                    || ((pc.getBaseStr() > 20)
                            || (pc.getBaseDex() > 18)
                            || (pc.getBaseCon() > 18)
                            || (pc.getBaseWis() > 18)
                            || (pc.getBaseCha() > 18) || (pc
                            .getBaseInt() > 18))) {
                isStatusError = true;
            }

            final int statusAmount = pc.getDex() + pc.getCha() + pc.getCon()
                    + pc.getInt() + pc.getStr() + pc.getWis();

            if ((statusAmount != 75) || isStatusError) {
                client.out().encrypt(
                        new S_CharCreateStatus(
                                S_CharCreateStatus.REASON_WRONG_AMOUNT));
                return;
            }

            client.getAccount().set_countCharacters(countCharacters + 1);
            client.out().encrypt(
                    new S_CharCreateStatus(S_CharCreateStatus.REASON_OK));
            initNewChar(client, pc);

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    /**
     * 创造角色
     * 
     * @param client
     * @param pc
     */
    private static void initNewChar(final ClientExecutor client,
            final L1PcInstance pc) {
        try {
            L1Account account = client.getAccount();
            pc.setId(IdFactory.get().nextId());
            int classid = CLASS_LIST[pc.get_sex()][pc.getType()];

            pc.setClassId(classid);
            pc.setTempCharGfx(classid);
            pc.setGfxId(classid);

            int[] loc = LOC_LIST[pc.getType()];
            pc.setX(loc[0]);
            pc.setY(loc[1]);
            pc.setMap((short) loc[2]);

            pc.setHeading(0);
            pc.setLawful(0);

            final int initHp = CalcInitHpMp.calcInitHp(pc);
            final int initMp = CalcInitHpMp.calcInitMp(pc);
            pc.addBaseMaxHp((short) initHp);
            pc.setCurrentHp((short) initHp);
            pc.addBaseMaxMp((short) initMp);
            pc.setCurrentMp((short) initMp);
            pc.resetBaseAc();
            pc.setTitle("");
            pc.setClanid(0);
            pc.setClanRank(0);
            pc.set_food(40);

            if (account.get_access_level() >= 200) {
                pc.setAccessLevel((short) account.get_access_level());
                pc.setGm(true);
                pc.setMonitor(false);

            } else {
                pc.setAccessLevel((short) 0);
                pc.setGm(false);
                pc.setMonitor(false);
            }

            pc.setGmInvis(false);
            pc.setExp(0);
            pc.setHighLevel(0);
            pc.setStatus(0);
            pc.setClanname("");
            pc.setBonusStats(0);
            pc.setElixirStats(0);
            pc.resetBaseMr();
            pc.setElfAttr(0);
            pc.set_PKcount(0);
            pc.setPkCountForElf(0);
            pc.setExpRes(0);
            pc.setPartnerId(0);
            pc.setOnlineStatus(0);
            pc.setHomeTownId(0);
            pc.setContribution(0);
            pc.setBanned(false);
            pc.setKarma(0);
            if (pc.isWizard()) {// 法师技能
                final int object_id = pc.getId();
                final L1Skills l1skills = SkillsTable.get().getTemplate(4); // EB
                final String skill_name = l1skills.getName();
                final int skill_id = l1skills.getSkillId();

                CharSkillReading.get().spellMastery(object_id, skill_id,
                        skill_name, 0, 0); // 资料库纪录
            }

            // 纪录人物帐号
            pc.setAccountName(client.getAccountName());
            // 初始化数值
            pc.refresh();

            client.out().encrypt(new S_NewCharPacket(pc));

            // 建立人物资料
            CharacterTable.get().storeNewCharacter(pc);
            // 纪录人物初始化资料
            CharacterTable.saveCharStatus(pc);
            // 给予新手道具
            BeginnerTable.get().giveItem(pc);
            // 加入建立PC OBJID资料
            CharObjidTable.get().addChar(pc.getId(), pc.getName());

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public static final String[] BANLIST = new String[] { "あ", "ア", "い", "イ",
            "う", "ウ", "え", "エ", "お", "オ", "か", "カ", "き", "キ", "く", "ク", "け",
            "ケ", "こ", "コ", "さ", "サ", "し", "シ", "す", "ス", "せ", "セ", "そ", "ソ",
            "た", "タ", "ち", "チ", "つ", "ツ", "て", "テ", "と", "ト", "な", "ナ", "に",
            "ニ", "ぬ", "ヌ", "ね", "ネ", "の", "ノ", "は", "ハ", "ひ", "ヒ", "ふ", "フ",
            "へ", "ヘ", "ほ", "ホ", "ま", "マ", "み", "ミ", "む", "ム", "め", "メ", "も",
            "モ", "や", "ヤ", "ゆ", "ユ", "よ", "ヨ", "ら", "ラ", "り", "リ", "る", "ル",
            "れ", "レ", "ろ", "ロ", "わ", "ワ", "を", "ヲ", "ん", "ン", "丶", "",
            "\ue6c1", "-", "/", "+", "*", "?", "!", "@", "#", "$", "%", "^",
            "&", "(", ")", "[", "]", "<", ">", "{", "}", ";", ":", "'", "\"",
            ",", ".", "~", "`", };

    public static boolean isInvalidName(final String name) {
        // int numOfNameBytes = 0;

        try {
            for (String ban : BANLIST) {
                if (name.indexOf(ban) != -1) {
                    return false;
                }
            }
            /*
             * if (name.indexOf("丶") != -1) { return false; }
             * 
             * if (name.indexOf("") != -1) { return false; }
             * 
             * if (name.indexOf("＞") != -1) { return false; }
             * 
             * if (name.indexOf("＜") != -1) { return false; }
             * 
             * if (name.indexOf("＼") != -1) { return false; }
             * 
             * if (name.indexOf("／") != -1) { return false; }
             * 
             * if (name.indexOf("\ue6c1") != -1) { return false; }
             */
//            //限制只允许使用中文字 hjx1000
//            Pattern p_str = Pattern.compile("[\\u4e00-\\u9fa5]+");
//            Matcher m = p_str.matcher(name);
//            if(!m.find() && !m.group(0).equals(name)){
//            	return false;
//            }

            if (BadNamesList.get().isBadName(name)) {
                return false;
            }

            // 将字串转为BYTE组 并取回BYTE长度
            final int numOfNameBytes = name
                    .getBytes(Config.CLIENT_LANGUAGE_CODE).length;
            // 全形字服 5字 半形12字
            if ((5 < (numOfNameBytes - name.length())) || (12 < numOfNameBytes)) {
                return false;
            }
            return true;

        } catch (final UnsupportedEncodingException e) {
            // _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
