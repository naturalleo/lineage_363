package com.lineage.server.model.Instance;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.NPCTalkDataTable;
import com.lineage.server.datatables.lock.TownReading;
import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1NpcTalkData;
import com.lineage.server.model.L1PcQuest;
import com.lineage.server.model.L1TownLocation;
import com.lineage.server.model.gametime.L1GameTimeClock;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_NPCPack_M;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.world.WorldClan;

/**
 * 对象:对话NPC 控制项
 * 
 * @author daien
 * 
 */
public class L1MerchantInstance extends L1NpcInstance {
    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    private static final Log _log = LogFactory.getLog(L1MerchantInstance.class);

    /**
     * 对象:对话NPC
     * 
     * @param template
     */
    public L1MerchantInstance(final L1Npc template) {
        super(template);
    }

    /**
     * TODO 接触资讯
     */
    @Override
    public void onPerceive(final L1PcInstance perceivedFrom) {
        try {
            perceivedFrom.addKnownObject(this);
            perceivedFrom.sendPackets(new S_NPCPack_M(this));
            // this.onNpcAI();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void onAction(final L1PcInstance pc) {
        try {
            final L1AttackMode attack = new L1AttackPc(pc, this);
            //attack.calcHit();
            attack.action();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void onNpcAI() {
        if (this.isAiRunning()) {
            return;
        }
        this.setActived(false);
        this.startAI();
    }

    @Override
    public void onTalkAction(final L1PcInstance player) {
        final int objid = this.getId();
        final L1NpcTalkData talking = NPCTalkDataTable.get().getTemplate(
                this.getNpcTemplate().get_npcId());
        final int npcid = this.getNpcTemplate().get_npcId();
        final L1PcQuest quest = player.getQuest();
        String htmlid = null;
        String[] htmldata = null;

        final int pcX = player.getX();
        final int pcY = player.getY();
        final int npcX = this.getX();
        final int npcY = this.getY();

        // 不具备工作的NPC
        if (this.WORK == null) {
            if (this.getNpcTemplate().getChangeHead()) {
                if ((pcX == npcX) && (pcY < npcY)) {
                    this.setHeading(0);

                } else if ((pcX > npcX) && (pcY < npcY)) {
                    this.setHeading(1);

                } else if ((pcX > npcX) && (pcY == npcY)) {
                    this.setHeading(2);

                } else if ((pcX > npcX) && (pcY > npcY)) {
                    this.setHeading(3);

                } else if ((pcX == npcX) && (pcY > npcY)) {
                    this.setHeading(4);

                } else if ((pcX < npcX) && (pcY > npcY)) {
                    this.setHeading(5);

                } else if ((pcX < npcX) && (pcY == npcY)) {
                    this.setHeading(6);

                } else if ((pcX < npcX) && (pcY < npcY)) {
                    this.setHeading(7);
                }
                this.broadcastPacketAll(new S_ChangeHeading(this));
            }
        }

        if (talking != null) {
            if (npcid == 70841) { // ルーディエル
                if (player.isElf()) { // エルフ
                    htmlid = "luudielE1";
                } else if (player.isDarkelf()) { // ダークエルフ
                    htmlid = "luudielCE1";
                } else {
                    htmlid = "luudiel1";
                }

                /*
                 * } else if (npcid == 70724) { // ヘイト if (player.isElf()) { //
                 * エルフ final int lv45_step =
                 * quest.get_step(L1PcQuest.QUEST_LEVEL45); if (lv45_step >= 4)
                 * { // ヘイト终了济み htmlid = "heit5"; } else if (lv45_step >= 3) {
                 * // フルート交换济み htmlid = "heit3"; } else if (lv45_step >= 2) { //
                 * ヘイト同意济み htmlid = "heit2"; } else if (lv45_step >= 1) { //
                 * マシャー同意济み htmlid = "heit1"; } }
                 */

                /*
                 * } else if (npcid == 70904) { // クプ if (player.isDarkelf()) {
                 * if (quest.get_step(L1PcQuest.QUEST_LEVEL45) == 1) { //
                 * ブルディカ同意济み htmlid = "koup12"; } }
                 */

            } else if (npcid == 70087) { // セディア
                if (player.isDarkelf()) {
                    htmlid = "sedia";
                }
            } else if (npcid == 70099) { // クーパー
                if (!quest.isEnd(L1PcQuest.QUEST_OILSKINMANT)) {
                    if (player.getLevel() > 13) {
                        htmlid = "kuper1";
                    }
                }
            } else if (npcid == 70796) { // ダンハム
                if (!quest.isEnd(L1PcQuest.QUEST_OILSKINMANT)) {
                    if (player.getLevel() > 13) {
                        htmlid = "dunham1";
                    }
                }
            } else if (npcid == 70011) { // 话せる岛の船着き管理人
                final int time = L1GameTimeClock.getInstance().currentTime()
                        .getSeconds() % 86400;
                if ((time < 60 * 60 * 6) || (time > 60 * 60 * 20)) { // 20:00～6:00
                    htmlid = "shipEvI6";
                }
            } else if (npcid == 70553) { // ケント城 侍从长 イスマエル
                final boolean hascastle = this.checkHasCastle(player,
                        L1CastleLocation.KENT_CASTLE_ID);
                if (hascastle) { // 城盟成员
                    if (this.checkClanLeader(player)) { // 血盟主
                        htmlid = "ishmael1";
                    } else {
                        htmlid = "ishmael6";
                        htmldata = new String[] { player.getName() };
                    }
                } else {
                    htmlid = "ishmael7";
                }
            } else if (npcid == 70822) { // オークの森 セゲム アトゥバ
                final boolean hascastle = this.checkHasCastle(player,
                        L1CastleLocation.OT_CASTLE_ID);
                if (hascastle) { // 城盟成员
                    if (this.checkClanLeader(player)) { // 血盟主
                        htmlid = "seghem1";
                    } else {
                        htmlid = "seghem6";
                        htmldata = new String[] { player.getName() };
                    }
                } else {
                    htmlid = "seghem7";
                }
            } else if (npcid == 70784) { // ウィンダウッド城 侍从长 オスモンド
                final boolean hascastle = this.checkHasCastle(player,
                        L1CastleLocation.WW_CASTLE_ID);
                if (hascastle) { // 城盟成员
                    if (this.checkClanLeader(player)) { // 血盟主
                        htmlid = "othmond1";
                    } else {
                        htmlid = "othmond6";
                        htmldata = new String[] { player.getName() };
                    }
                } else {
                    htmlid = "othmond7";
                }
            } else if (npcid == 70623) { // ギラン城 侍从长 オービル
                final boolean hascastle = this.checkHasCastle(player,
                        L1CastleLocation.GIRAN_CASTLE_ID);
                if (hascastle) { // 城盟成员
                    if (this.checkClanLeader(player)) { // 血盟主
                        htmlid = "orville1";
                    } else {
                        htmlid = "orville6";
                        htmldata = new String[] { player.getName() };
                    }
                } else {
                    htmlid = "orville7";
                }
            } else if (npcid == 70880) { // ハイネ城 侍从长 フィッシャー
                final boolean hascastle = this.checkHasCastle(player,
                        L1CastleLocation.HEINE_CASTLE_ID);
                if (hascastle) { // 城盟成员
                    if (this.checkClanLeader(player)) { // 血盟主
                        htmlid = "fisher1";
                    } else {
                        htmlid = "fisher6";
                        htmldata = new String[] { player.getName() };
                    }
                } else {
                    htmlid = "fisher7";
                }
            } else if (npcid == 70665) { // ドワーフ城 侍从长 ポテンピン
                final boolean hascastle = this.checkHasCastle(player,
                        L1CastleLocation.DOWA_CASTLE_ID);
                if (hascastle) { // 城盟成员
                    if (this.checkClanLeader(player)) { // 血盟主
                        htmlid = "potempin1";
                    } else {
                        htmlid = "potempin6";
                        htmldata = new String[] { player.getName() };
                    }
                } else {
                    htmlid = "potempin7";
                }
            } else if (npcid == 70721) { // アデン城 侍从长 ティモン
                final boolean hascastle = this.checkHasCastle(player,
                        L1CastleLocation.ADEN_CASTLE_ID);
                if (hascastle) { // 城盟成员
                    if (this.checkClanLeader(player)) { // 血盟主
                        htmlid = "timon1";
                    } else {
                        htmlid = "timon6";
                        htmldata = new String[] { player.getName() };
                    }
                } else {
                    htmlid = "timon7";
                }
            } else if (npcid == 81155) { // ディアド要塞 オーレ
                final boolean hascastle = this.checkHasCastle(player,
                        L1CastleLocation.DIAD_CASTLE_ID);
                if (hascastle) { // 城盟成员
                    if (this.checkClanLeader(player)) { // 血盟主
                        htmlid = "olle1";
                    } else {
                        htmlid = "olle6";
                        htmldata = new String[] { player.getName() };
                    }
                } else {
                    htmlid = "olle7";
                }
            } else if (npcid == 80057) { // アルフォンス
                switch (player.getKarmaLevel()) {
                    case 0:
                        htmlid = "alfons1";
                        break;
                    case -1:
                        htmlid = "cyk1";
                        break;
                    case -2:
                        htmlid = "cyk2";
                        break;
                    case -3:
                        htmlid = "cyk3";
                        break;
                    case -4:
                        htmlid = "cyk4";
                        break;
                    case -5:
                        htmlid = "cyk5";
                        break;
                    case -6:
                        htmlid = "cyk6";
                        break;
                    case -7:
                        htmlid = "cyk7";
                        break;
                    case -8:
                        htmlid = "cyk8";
                        break;
                    case 1:
                        htmlid = "cbk1";
                        break;
                    case 2:
                        htmlid = "cbk2";
                        break;
                    case 3:
                        htmlid = "cbk3";
                        break;
                    case 4:
                        htmlid = "cbk4";
                        break;
                    case 5:
                        htmlid = "cbk5";
                        break;
                    case 6:
                        htmlid = "cbk6";
                        break;
                    case 7:
                        htmlid = "cbk7";
                        break;
                    case 8:
                        htmlid = "cbk8";
                        break;
                    default:
                        htmlid = "alfons1";
                        break;
                }
            } else if (npcid == 80058) { // 次元の扉(砂漠)
                final int level = player.getLevel();
                if (level <= 44) {
                    htmlid = "cpass03";
                } else if ((level <= 51) && (45 <= level)) {
                    htmlid = "cpass02";
                } else {
                    htmlid = "cpass01";
                }
            } else if (npcid == 80059) { // 次元の扉(土)
                if (player.getKarmaLevel() > 0) {
                    htmlid = "cpass03";
                } else if (player.getInventory().checkItem(40921)) { // 元素の支配者
                    htmlid = "wpass02";
                } else if (player.getInventory().checkItem(40917)) { // 地の支配者
                    htmlid = "wpass14";
                } else if (player.getInventory().checkItem(40912) // 风の通行证
                        || player.getInventory().checkItem(40910) // 水の通行证
                        || player.getInventory().checkItem(40911)) { // 火の通行证
                    htmlid = "wpass04";
                } else if (player.getInventory().checkItem(40909)) { // 地の通行证
                    final int count = this.getNecessarySealCount(player);
                    if (player.getInventory().checkItem(40913, count)) { // 地の印章
                        this.createRuler(player, 1, count);
                        htmlid = "wpass06";
                    } else {
                        htmlid = "wpass03";
                    }
                } else if (player.getInventory().checkItem(40913)) { // 地の印章
                    htmlid = "wpass08";
                } else {
                    htmlid = "wpass05";
                }
            } else if (npcid == 80060) { // 次元の扉(风)
                if (player.getKarmaLevel() > 0) {
                    htmlid = "cpass03";
                } else if (player.getInventory().checkItem(40921)) { // 元素の支配者
                    htmlid = "wpass02";
                } else if (player.getInventory().checkItem(40920)) { // 风の支配者
                    htmlid = "wpass13";
                } else if (player.getInventory().checkItem(40909) // 地の通行证
                        || player.getInventory().checkItem(40910) // 水の通行证
                        || player.getInventory().checkItem(40911)) { // 火の通行证
                    htmlid = "wpass04";
                } else if (player.getInventory().checkItem(40912)) { // 风の通行证
                    final int count = this.getNecessarySealCount(player);
                    if (player.getInventory().checkItem(40916, count)) { // 风の印章
                        this.createRuler(player, 8, count);
                        htmlid = "wpass06";
                    } else {
                        htmlid = "wpass03";
                    }
                } else if (player.getInventory().checkItem(40916)) { // 风の印章
                    htmlid = "wpass08";
                } else {
                    htmlid = "wpass05";
                }
            } else if (npcid == 80061) { // 次元の扉(水)
                if (player.getKarmaLevel() > 0) {
                    htmlid = "cpass03";
                } else if (player.getInventory().checkItem(40921)) { // 元素の支配者
                    htmlid = "wpass02";
                } else if (player.getInventory().checkItem(40918)) { // 水の支配者
                    htmlid = "wpass11";
                } else if (player.getInventory().checkItem(40909) // 地の通行证
                        || player.getInventory().checkItem(40912) // 风の通行证
                        || player.getInventory().checkItem(40911)) { // 火の通行证
                    htmlid = "wpass04";
                } else if (player.getInventory().checkItem(40910)) { // 水の通行证
                    final int count = this.getNecessarySealCount(player);
                    if (player.getInventory().checkItem(40914, count)) { // 水の印章
                        this.createRuler(player, 4, count);
                        htmlid = "wpass06";
                    } else {
                        htmlid = "wpass03";
                    }
                } else if (player.getInventory().checkItem(40914)) { // 水の印章
                    htmlid = "wpass08";
                } else {
                    htmlid = "wpass05";
                }
            } else if (npcid == 80062) { // 次元の扉(火)
                if (player.getKarmaLevel() > 0) {
                    htmlid = "cpass03";
                } else if (player.getInventory().checkItem(40921)) { // 元素の支配者
                    htmlid = "wpass02";
                } else if (player.getInventory().checkItem(40919)) { // 火の支配者
                    htmlid = "wpass12";
                } else if (player.getInventory().checkItem(40909) // 地の通行证
                        || player.getInventory().checkItem(40912) // 风の通行证
                        || player.getInventory().checkItem(40910)) { // 水の通行证
                    htmlid = "wpass04";
                } else if (player.getInventory().checkItem(40911)) { // 火の通行证
                    final int count = this.getNecessarySealCount(player);
                    if (player.getInventory().checkItem(40915, count)) { // 火の印章
                        this.createRuler(player, 2, count);
                        htmlid = "wpass06";
                    } else {
                        htmlid = "wpass03";
                    }
                } else if (player.getInventory().checkItem(40915)) { // 火の印章
                    htmlid = "wpass08";
                } else {
                    htmlid = "wpass05";
                }
            } else if (npcid == 80065) { // バルログの密侦
                if (player.getKarmaLevel() < 3) {
                    htmlid = "uturn0";
                } else {
                    htmlid = "uturn1";
                }
            } else if (npcid == 80047) { // ヤヒの召使
                if (player.getKarmaLevel() > -3) {
                    htmlid = "uhelp1";
                } else {
                    htmlid = "uhelp2";
                }
            } else if (npcid == 80049) { // 摇らぐ者
                if (player.getKarma() <= -10000000) {
                    htmlid = "betray11";
                } else {
                    htmlid = "betray12";
                }
            } else if (npcid == 80050) { // ヤヒの执政官
                if (player.getKarmaLevel() > -1) {
                    htmlid = "meet103";
                } else {
                    htmlid = "meet101";
                }
            } else if (npcid == 80053) { // ヤヒの锻冶屋
                final int karmaLevel = player.getKarmaLevel();
                if (karmaLevel == 0) {
                    htmlid = "aliceyet";
                } else if (karmaLevel >= 1) {
                    if (player.getInventory().checkItem(196)
                            || player.getInventory().checkItem(197)
                            || player.getInventory().checkItem(198)
                            || player.getInventory().checkItem(199)
                            || player.getInventory().checkItem(200)
                            || player.getInventory().checkItem(201)
                            || player.getInventory().checkItem(202)
                            || player.getInventory().checkItem(203)) {
                        htmlid = "alice_gd";
                    } else {
                        htmlid = "gd";
                    }
                } else if (karmaLevel <= -1) {
                    if (player.getInventory().checkItem(40991)) {
                        if (karmaLevel <= -1) {
                            htmlid = "Mate_1";
                        }
                    } else if (player.getInventory().checkItem(196)) {
                        if (karmaLevel <= -2) {
                            htmlid = "Mate_2";
                        } else {
                            htmlid = "alice_1";
                        }
                    } else if (player.getInventory().checkItem(197)) {
                        if (karmaLevel <= -3) {
                            htmlid = "Mate_3";
                        } else {
                            htmlid = "alice_2";
                        }
                    } else if (player.getInventory().checkItem(198)) {
                        if (karmaLevel <= -4) {
                            htmlid = "Mate_4";
                        } else {
                            htmlid = "alice_3";
                        }
                    } else if (player.getInventory().checkItem(199)) {
                        if (karmaLevel <= -5) {
                            htmlid = "Mate_5";
                        } else {
                            htmlid = "alice_4";
                        }
                    } else if (player.getInventory().checkItem(200)) {
                        if (karmaLevel <= -6) {
                            htmlid = "Mate_6";
                        } else {
                            htmlid = "alice_5";
                        }
                    } else if (player.getInventory().checkItem(201)) {
                        if (karmaLevel <= -7) {
                            htmlid = "Mate_7";
                        } else {
                            htmlid = "alice_6";
                        }
                    } else if (player.getInventory().checkItem(202)) {
                        if (karmaLevel <= -8) {
                            htmlid = "Mate_8";
                        } else {
                            htmlid = "alice_7";
                        }
                    } else if (player.getInventory().checkItem(203)) {
                        htmlid = "alice_8";
                    } else {
                        htmlid = "alice_no";
                    }
                }
            } else if (npcid == 80055) { // ヤヒの补佐官
                int amuletLevel = 0;
                if (player.getInventory().checkItem(20358)) { // 奴隶のアミュレット
                    amuletLevel = 1;
                } else if (player.getInventory().checkItem(20359)) { // 约束のアミュレット
                    amuletLevel = 2;
                } else if (player.getInventory().checkItem(20360)) { // 解放のアミュレット
                    amuletLevel = 3;
                } else if (player.getInventory().checkItem(20361)) { // 猎犬のアミュレット
                    amuletLevel = 4;
                } else if (player.getInventory().checkItem(20362)) { // 魔族のアミュレット
                    amuletLevel = 5;
                } else if (player.getInventory().checkItem(20363)) { // 勇士のアミュレット
                    amuletLevel = 6;
                } else if (player.getInventory().checkItem(20364)) { // 将军のアミュレット
                    amuletLevel = 7;
                } else if (player.getInventory().checkItem(20365)) { // 大将军のアミュレット
                    amuletLevel = 8;
                }
                if (player.getKarmaLevel() == -1) {
                    if (amuletLevel >= 1) {
                        htmlid = "uamuletd";
                    } else {
                        htmlid = "uamulet1";
                    }
                } else if (player.getKarmaLevel() == -2) {
                    if (amuletLevel >= 2) {
                        htmlid = "uamuletd";
                    } else {
                        htmlid = "uamulet2";
                    }
                } else if (player.getKarmaLevel() == -3) {
                    if (amuletLevel >= 3) {
                        htmlid = "uamuletd";
                    } else {
                        htmlid = "uamulet3";
                    }
                } else if (player.getKarmaLevel() == -4) {
                    if (amuletLevel >= 4) {
                        htmlid = "uamuletd";
                    } else {
                        htmlid = "uamulet4";
                    }
                } else if (player.getKarmaLevel() == -5) {
                    if (amuletLevel >= 5) {
                        htmlid = "uamuletd";
                    } else {
                        htmlid = "uamulet5";
                    }
                } else if (player.getKarmaLevel() == -6) {
                    if (amuletLevel >= 6) {
                        htmlid = "uamuletd";
                    } else {
                        htmlid = "uamulet6";
                    }
                } else if (player.getKarmaLevel() == -7) {
                    if (amuletLevel >= 7) {
                        htmlid = "uamuletd";
                    } else {
                        htmlid = "uamulet7";
                    }
                } else if (player.getKarmaLevel() == -8) {
                    if (amuletLevel >= 8) {
                        htmlid = "uamuletd";
                    } else {
                        htmlid = "uamulet8";
                    }
                } else {
                    htmlid = "uamulet0";
                }
            } else if (npcid == 80056) { // 业の管理者
                if (player.getKarma() <= -10000000) {
                    htmlid = "infamous11";
                } else {
                    htmlid = "infamous12";
                }
            } else if (npcid == 80064) { // バルログの执政官
                if (player.getKarmaLevel() < 1) {
                    htmlid = "meet003";
                } else {
                    htmlid = "meet001";
                }
            } else if (npcid == 80066) { // 摇らめく者
                if (player.getKarma() >= 10000000) {
                    htmlid = "betray01";
                } else {
                    htmlid = "betray02";
                }
            } else if (npcid == 80071) { // バルログの补佐官
                int earringLevel = 0;
                if (player.getInventory().checkItem(21020)) { // 踊跃のイアリング
                    earringLevel = 1;
                } else if (player.getInventory().checkItem(21021)) { // 双子のイアリング
                    earringLevel = 2;
                } else if (player.getInventory().checkItem(21022)) { // 友好のイアリング
                    earringLevel = 3;
                } else if (player.getInventory().checkItem(21023)) { // 极知のイアリング
                    earringLevel = 4;
                } else if (player.getInventory().checkItem(21024)) { // 暴走のイアリング
                    earringLevel = 5;
                } else if (player.getInventory().checkItem(21025)) { // 从魔のイアリング
                    earringLevel = 6;
                } else if (player.getInventory().checkItem(21026)) { // 血族のイアリング
                    earringLevel = 7;
                } else if (player.getInventory().checkItem(21027)) { // 奴隶のイアリング
                    earringLevel = 8;
                }
                if (player.getKarmaLevel() == 1) {
                    if (earringLevel >= 1) {
                        htmlid = "lringd";
                    } else {
                        htmlid = "lring1";
                    }
                } else if (player.getKarmaLevel() == 2) {
                    if (earringLevel >= 2) {
                        htmlid = "lringd";
                    } else {
                        htmlid = "lring2";
                    }
                } else if (player.getKarmaLevel() == 3) {
                    if (earringLevel >= 3) {
                        htmlid = "lringd";
                    } else {
                        htmlid = "lring3";
                    }
                } else if (player.getKarmaLevel() == 4) {
                    if (earringLevel >= 4) {
                        htmlid = "lringd";
                    } else {
                        htmlid = "lring4";
                    }
                } else if (player.getKarmaLevel() == 5) {
                    if (earringLevel >= 5) {
                        htmlid = "lringd";
                    } else {
                        htmlid = "lring5";
                    }
                } else if (player.getKarmaLevel() == 6) {
                    if (earringLevel >= 6) {
                        htmlid = "lringd";
                    } else {
                        htmlid = "lring6";
                    }
                } else if (player.getKarmaLevel() == 7) {
                    if (earringLevel >= 7) {
                        htmlid = "lringd";
                    } else {
                        htmlid = "lring7";
                    }
                } else if (player.getKarmaLevel() == 8) {
                    if (earringLevel >= 8) {
                        htmlid = "lringd";
                    } else {
                        htmlid = "lring8";
                    }
                } else {
                    htmlid = "lring0";
                }
            } else if (npcid == 80072) { // バルログの锻冶屋
                final int karmaLevel = player.getKarmaLevel();
                if (karmaLevel == 1) {
                    htmlid = "lsmith0";
                } else if (karmaLevel == 2) {
                    htmlid = "lsmith1";
                } else if (karmaLevel == 3) {
                    htmlid = "lsmith2";
                } else if (karmaLevel == 4) {
                    htmlid = "lsmith3";
                } else if (karmaLevel == 5) {
                    htmlid = "lsmith4";
                } else if (karmaLevel == 6) {
                    htmlid = "lsmith5";
                } else if (karmaLevel == 7) {
                    htmlid = "lsmith7";
                } else if (karmaLevel == 8) {
                    htmlid = "lsmith8";
                } else {
                    htmlid = "";
                }
            } else if (npcid == 80074) { // 业の管理者
                if (player.getKarma() >= 10000000) {
                    htmlid = "infamous01";
                } else {
                    htmlid = "infamous02";
                }
            } else if (npcid == 80104) { // アデン骑马团员
                if (!player.isCrown()) { // 君主
                    htmlid = "horseseller4";
                }
            } else if (npcid == 70528) { // 话せる岛の村 タウンマスター
                htmlid = talkToTownmaster(player,
                        L1TownLocation.TOWNID_TALKING_ISLAND);
            } else if (npcid == 70546) { // ケント村 タウンマスター
                htmlid = talkToTownmaster(player, L1TownLocation.TOWNID_KENT);
            } else if (npcid == 70567) { // グルーディン村 タウンマスター
                htmlid = talkToTownmaster(player, L1TownLocation.TOWNID_GLUDIO);
            } else if (npcid == 70815) { // 火田村 タウンマスター
                htmlid = talkToTownmaster(player,
                        L1TownLocation.TOWNID_ORCISH_FOREST);
            } else if (npcid == 70774) { // ウッドベック村 タウンマスター
                htmlid = talkToTownmaster(player,
                        L1TownLocation.TOWNID_WINDAWOOD);
            } else if (npcid == 70799) { // シルバーナイトタウン タウンマスター
                htmlid = talkToTownmaster(player,
                        L1TownLocation.TOWNID_SILVER_KNIGHT_TOWN);
            } else if (npcid == 70594) { // ギラン都市 タウンマスター
                htmlid = talkToTownmaster(player, L1TownLocation.TOWNID_GIRAN);
            } else if (npcid == 70860) { // ハイネ都市 タウンマスター
                htmlid = talkToTownmaster(player, L1TownLocation.TOWNID_HEINE);
            } else if (npcid == 70654) { // ウェルダン村 タウンマスター
                htmlid = talkToTownmaster(player, L1TownLocation.TOWNID_WERLDAN);
            } else if (npcid == 70748) { // 象牙の塔の村 タウンマスター
                htmlid = talkToTownmaster(player, L1TownLocation.TOWNID_OREN);
            } else if (npcid == 70534) { // 话せる岛の村 タウンアドバイザー
                htmlid = talkToTownadviser(player,
                        L1TownLocation.TOWNID_TALKING_ISLAND);
            } else if (npcid == 70556) { // ケント村 タウンアドバイザー
                htmlid = talkToTownadviser(player, L1TownLocation.TOWNID_KENT);
            } else if (npcid == 70572) { // グルーディン村 タウンアドバイザー
                htmlid = talkToTownadviser(player, L1TownLocation.TOWNID_GLUDIO);
            } else if (npcid == 70830) { // 火田村 タウンアドバイザー
                htmlid = talkToTownadviser(player,
                        L1TownLocation.TOWNID_ORCISH_FOREST);
            } else if (npcid == 70788) { // ウッドベック村 タウンアドバイザー
                htmlid = talkToTownadviser(player,
                        L1TownLocation.TOWNID_WINDAWOOD);
            } else if (npcid == 70806) { // シルバーナイトタウン タウンアドバイザー
                htmlid = talkToTownadviser(player,
                        L1TownLocation.TOWNID_SILVER_KNIGHT_TOWN);
            } else if (npcid == 70631) { // ギラン都市 タウンアドバイザー
                htmlid = talkToTownadviser(player, L1TownLocation.TOWNID_GIRAN);
            } else if (npcid == 70876) { // ハイネ都市 タウンアドバイザー
                htmlid = talkToTownadviser(player, L1TownLocation.TOWNID_HEINE);
            } else if (npcid == 70663) { // ウェルダン村 タウンアドバイザー
                htmlid = talkToTownadviser(player,
                        L1TownLocation.TOWNID_WERLDAN);
            } else if (npcid == 70761) { // 象牙の塔の村 タウンアドバイザー
                htmlid = talkToTownadviser(player, L1TownLocation.TOWNID_OREN);

            } else if (npcid == 70506) { // ルバー
                htmlid = this.talkToRuba(player);

            } else if (npcid == 71026) { // ココ
                if (player.getLevel() < 10) {
                    htmlid = "en0113";
                } else if ((player.getLevel() >= 10)
                        && (player.getLevel() < 25)) {
                    htmlid = "en0111";
                } else if (player.getLevel() > 25) {
                    htmlid = "en0112";
                }
            } else if (npcid == 71027) { // クン
                if (player.getLevel() < 10) {
                    htmlid = "en0283";
                } else if ((player.getLevel() >= 10)
                        && (player.getLevel() < 25)) {
                    htmlid = "en0281";
                } else if (player.getLevel() > 25) {
                    htmlid = "en0282";
                }

            } else if (npcid == 70512) { // 治疗师（歌う岛 村の中）
                if (player.getLevel() >= 25) {
                    htmlid = "jpe0102";
                }
            } else if (npcid == 70514) { // ヘイスト师
                if (player.getLevel() >= 25) {
                    htmlid = "jpe0092";
                }
            } else if (npcid == 71038) { // 长老 ノナメ
                if (player.getInventory().checkItem(41060)) { // ノナメの推荐书
                    if (player.getInventory().checkItem(41090) // ネルガのトーテム
                            || player.getInventory().checkItem(41091) // ドゥダ-マラのトーテム
                            || player.getInventory().checkItem(41092)) { // アトゥバのトーテム
                        htmlid = "orcfnoname7";
                    } else {
                        htmlid = "orcfnoname8";
                    }
                } else {
                    htmlid = "orcfnoname1";
                }
            } else if (npcid == 71040) { // 调查团长 アトゥバ ノア
                if (player.getInventory().checkItem(41060)) { // ノナメの推荐书
                    if (player.getInventory().checkItem(41065)) { // 调查团の证书
                        if (player.getInventory().checkItem(41086) // スピリッドの根
                                || player.getInventory().checkItem(41087) // スピリッドの表皮
                                || player.getInventory().checkItem(41088) // スピリッドの叶
                                || player.getInventory().checkItem(41089)) { // スピリッドの木の枝
                            htmlid = "orcfnoa6";
                        } else {
                            htmlid = "orcfnoa5";
                        }
                    } else {
                        htmlid = "orcfnoa2";
                    }
                } else {
                    htmlid = "orcfnoa1";
                }
            } else if (npcid == 71041) { // ネルガ フウモ
                if (player.getInventory().checkItem(41060)) { // ノナメの推荐书
                    if (player.getInventory().checkItem(41064)) { // 调查团の证书
                        if (player.getInventory().checkItem(41081) // オークのバッジ
                                || player.getInventory().checkItem(41082) // オークのアミュレット
                                || player.getInventory().checkItem(41083) // シャーマンパウダー
                                || player.getInventory().checkItem(41084) // イリュージョンパウダー
                                || player.getInventory().checkItem(41085)) { // 予言者のパール
                            htmlid = "orcfhuwoomo2";
                        } else {
                            htmlid = "orcfhuwoomo8";
                        }
                    } else {
                        htmlid = "orcfhuwoomo1";
                    }
                } else {
                    htmlid = "orcfhuwoomo5";
                }
            } else if (npcid == 71042) { // ネルガ バクモ
                if (player.getInventory().checkItem(41060)) { // ノナメの推荐书
                    if (player.getInventory().checkItem(41062)) { // 调查团の证书
                        if (player.getInventory().checkItem(41071) // 银のお盆
                                || player.getInventory().checkItem(41072) // 银の烛台
                                || player.getInventory().checkItem(41073) // バンディッドの键
                                || player.getInventory().checkItem(41074) // バンディッドの袋
                                || player.getInventory().checkItem(41075)) { // 污れた发の毛
                            htmlid = "orcfbakumo2";
                        } else {
                            htmlid = "orcfbakumo8";
                        }
                    } else {
                        htmlid = "orcfbakumo1";
                    }
                } else {
                    htmlid = "orcfbakumo5";
                }
            } else if (npcid == 71043) { // ドゥダ-マラ ブカ
                if (player.getInventory().checkItem(41060)) { // ノナメの推荐书
                    if (player.getInventory().checkItem(41063)) { // 调查团の证书
                        if (player.getInventory().checkItem(41076) // 污れた地のコア
                                || player.getInventory().checkItem(41077) // 污れた水のコア
                                || player.getInventory().checkItem(41078) // 污れた火のコア
                                || player.getInventory().checkItem(41079) // 污れた风のコア
                                || player.getInventory().checkItem(41080)) { // 污れた精灵のコア
                            htmlid = "orcfbuka2";
                        } else {
                            htmlid = "orcfbuka8";
                        }
                    } else {
                        htmlid = "orcfbuka1";
                    }
                } else {
                    htmlid = "orcfbuka5";
                }
            } else if (npcid == 71044) { // ドゥダ-マラ カメ
                if (player.getInventory().checkItem(41060)) { // ノナメの推荐书
                    if (player.getInventory().checkItem(41061)) { // 调查团の证书
                        if (player.getInventory().checkItem(41066) // 污れた根
                                || player.getInventory().checkItem(41067) // 污れた枝
                                || player.getInventory().checkItem(41068) // 污れた拔け壳
                                || player.getInventory().checkItem(41069) // 污れたタテガミ
                                || player.getInventory().checkItem(41070)) { // 污れた妖精の羽
                            htmlid = "orcfkame2";
                        } else {
                            htmlid = "orcfkame8";
                        }
                    } else {
                        htmlid = "orcfkame1";
                    }
                } else {
                    htmlid = "orcfkame5";
                }
            } else if (npcid == 71055) { // ルケイン（海贼岛の秘密）
                if (player.getQuest().get_step(L1PcQuest.QUEST_RESTA) == 3) {
                    htmlid = "lukein13";
                } else if ((player.getQuest().get_step(L1PcQuest.QUEST_LUKEIN1) == L1PcQuest.QUEST_END)
                        && (player.getQuest().get_step(L1PcQuest.QUEST_RESTA) == 2)
                        && player.getInventory().checkItem(40631)) {
                    htmlid = "lukein10";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_LUKEIN1) == L1PcQuest.QUEST_END) {
                    htmlid = "lukein0";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_LUKEIN1) == 11) {
                    if (player.getInventory().checkItem(40716)) {
                        htmlid = "lukein9";
                    }
                } else if ((player.getQuest().get_step(L1PcQuest.QUEST_LUKEIN1) >= 1)
                        && (player.getQuest().get_step(L1PcQuest.QUEST_LUKEIN1) <= 10)) {
                    htmlid = "lukein8";
                }
            } else if (npcid == 71063) { // 小さな箱-１番目（海贼岛の秘密）
                if (player.getQuest().get_step(L1PcQuest.QUEST_TBOX1) == L1PcQuest.QUEST_END) {
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_LUKEIN1) == 1) {
                    htmlid = "maptbox";
                }
            } else if (npcid == 71064) { // 小さな箱-2番目-ｂ地点（海贼岛の秘密）
                if (player.getQuest().get_step(L1PcQuest.QUEST_LUKEIN1) == 2) {
                    htmlid = this.talkToSecondtbox(player);
                }
            } else if (npcid == 71065) { // 小さな箱-2番目-c地点（海贼岛の秘密）
                if (player.getQuest().get_step(L1PcQuest.QUEST_LUKEIN1) == 3) {
                    htmlid = this.talkToSecondtbox(player);
                }
            } else if (npcid == 71066) { // 小さな箱-2番目-d地点（海贼岛の秘密）
                if (player.getQuest().get_step(L1PcQuest.QUEST_LUKEIN1) == 4) {
                    htmlid = this.talkToSecondtbox(player);
                }
            } else if (npcid == 71067) { // 小さな箱-3番目-e地点（海贼岛の秘密）
                if (player.getQuest().get_step(L1PcQuest.QUEST_LUKEIN1) == 5) {
                    htmlid = this.talkToThirdtbox(player);
                }
            } else if (npcid == 71068) { // 小さな箱-3番目-f地点（海贼岛の秘密）
                if (player.getQuest().get_step(L1PcQuest.QUEST_LUKEIN1) == 6) {
                    htmlid = this.talkToThirdtbox(player);
                }
            } else if (npcid == 71069) { // 小さな箱-3番目-g地点（海贼岛の秘密）
                if (player.getQuest().get_step(L1PcQuest.QUEST_LUKEIN1) == 7) {
                    htmlid = this.talkToThirdtbox(player);
                }
            } else if (npcid == 71070) { // 小さな箱-3番目-h地点（海贼岛の秘密）
                if (player.getQuest().get_step(L1PcQuest.QUEST_LUKEIN1) == 8) {
                    htmlid = this.talkToThirdtbox(player);
                }
            } else if (npcid == 71071) { // 小さな箱-3番目-i地点（海贼岛の秘密）
                if (player.getQuest().get_step(L1PcQuest.QUEST_LUKEIN1) == 9) {
                    htmlid = this.talkToThirdtbox(player);
                }
            } else if (npcid == 71072) { // 小さな箱-3番目-j地点（海贼岛の秘密）
                if (player.getQuest().get_step(L1PcQuest.QUEST_LUKEIN1) == 10) {
                    htmlid = this.talkToThirdtbox(player);
                }
            } else if (npcid == 71056) { // シミズ（消えた息子）
                if (player.getQuest().get_step(L1PcQuest.QUEST_RESTA) == 4) {
                    if (player.getInventory().checkItem(40631)) {
                        htmlid = "SIMIZZ11";
                    } else {
                        htmlid = "SIMIZZ0";
                    }
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_SIMIZZ) == 2) {
                    htmlid = "SIMIZZ0";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_SIMIZZ) == L1PcQuest.QUEST_END) {
                    htmlid = "SIMIZZ15";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_SIMIZZ) == 1) {
                    htmlid = "SIMIZZ6";
                }
            } else if (npcid == 71057) { // ドイル（宝の地图1）
                if (player.getQuest().get_step(L1PcQuest.QUEST_DOIL) == L1PcQuest.QUEST_END) {
                    htmlid = "doil4b";
                }
            } else if (npcid == 71059) { // ルディアン（宝の地图2）
                if (player.getQuest().get_step(L1PcQuest.QUEST_RUDIAN) == L1PcQuest.QUEST_END) {
                    htmlid = "rudian1c";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_RUDIAN) == 1) {
                    htmlid = "rudian7";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_DOIL) == L1PcQuest.QUEST_END) {
                    htmlid = "rudian1b";
                } else {
                    htmlid = "rudian1a";
                }
            } else if (npcid == 71060) { // レスタ（宝の地图3）
                if (player.getQuest().get_step(L1PcQuest.QUEST_RESTA) == L1PcQuest.QUEST_END) {
                    htmlid = "resta1e";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_SIMIZZ) == L1PcQuest.QUEST_END) {
                    htmlid = "resta14";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_RESTA) == 4) {
                    htmlid = "resta13";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_RESTA) == 3) {
                    htmlid = "resta11";
                    player.getQuest().set_step(L1PcQuest.QUEST_RESTA, 4);
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_RESTA) == 2) {
                    htmlid = "resta16";
                } else if (((player.getQuest().get_step(L1PcQuest.QUEST_SIMIZZ) == 2) && (player
                        .getQuest().get_step(L1PcQuest.QUEST_CADMUS) == 1))
                        || player.getInventory().checkItem(40647)) {
                    htmlid = "resta1a";
                } else if ((player.getQuest().get_step(L1PcQuest.QUEST_CADMUS) == 1)
                        || player.getInventory().checkItem(40647)) {
                    htmlid = "resta1c";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_SIMIZZ) == 2) {
                    htmlid = "resta1b";
                }
            } else if (npcid == 71061) { // カドムス（宝の地图4）
                if (player.getQuest().get_step(L1PcQuest.QUEST_CADMUS) == L1PcQuest.QUEST_END) {
                    htmlid = "cadmus1c";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_CADMUS) == 3) {
                    htmlid = "cadmus8";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_CADMUS) == 2) {
                    htmlid = "cadmus1a";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_DOIL) == L1PcQuest.QUEST_END) {
                    htmlid = "cadmus1b";
                }
            } else if (npcid == 71036) { // カミーラ（ドレイクの真实）
                if (player.getQuest().get_step(L1PcQuest.QUEST_KAMYLA) == L1PcQuest.QUEST_END) {
                    htmlid = "kamyla26";
                } else if ((player.getQuest().get_step(L1PcQuest.QUEST_KAMYLA) == 4)
                        && player.getInventory().checkItem(40717)) {
                    htmlid = "kamyla15";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_KAMYLA) == 4) {
                    htmlid = "kamyla14";
                } else if ((player.getQuest().get_step(L1PcQuest.QUEST_KAMYLA) == 3)
                        && player.getInventory().checkItem(40630)) {
                    htmlid = "kamyla12";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_KAMYLA) == 3) {
                    htmlid = "kamyla11";
                } else if ((player.getQuest().get_step(L1PcQuest.QUEST_KAMYLA) == 2)
                        && player.getInventory().checkItem(40644)) {
                    htmlid = "kamyla9";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_KAMYLA) == 1) {
                    htmlid = "kamyla8";
                } else if ((player.getQuest().get_step(L1PcQuest.QUEST_CADMUS) == L1PcQuest.QUEST_END)
                        && player.getInventory().checkItem(40621)) {
                    htmlid = "kamyla1";
                }
            } else if (npcid == 71089) { // フランコ（ドレイクの真实）
                if (player.getQuest().get_step(L1PcQuest.QUEST_KAMYLA) == 2) {
                    htmlid = "francu12";
                }
            } else if (npcid == 71090) { // 试练のクリスタル2（ドレイクの真实）
                if ((player.getQuest().get_step(L1PcQuest.QUEST_CRYSTAL) == 1)
                        && player.getInventory().checkItem(40620)) {
                    htmlid = "jcrystal2";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_CRYSTAL) == 1) {
                    htmlid = "jcrystal3";
                }
            } else if (npcid == 71091) { // 试练のクリスタル3（ドレイクの真实）
                if ((player.getQuest().get_step(L1PcQuest.QUEST_CRYSTAL) == 2)
                        && player.getInventory().checkItem(40654)) {
                    htmlid = "jcrystall2";
                }
            } else if (npcid == 71074) { // リザードマンの长老
                if (player.getQuest().get_step(L1PcQuest.QUEST_LIZARD) == L1PcQuest.QUEST_END) {
                    htmlid = "lelder0";
                } else if ((player.getQuest().get_step(L1PcQuest.QUEST_LIZARD) == 3)
                        && player.getInventory().checkItem(40634)) {
                    htmlid = "lelder12";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_LIZARD) == 3) {
                    htmlid = "lelder11";
                } else if ((player.getQuest().get_step(L1PcQuest.QUEST_LIZARD) == 2)
                        && player.getInventory().checkItem(40633)) {
                    htmlid = "lelder7";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_LIZARD) == 2) {
                    htmlid = "lelder7b";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_LIZARD) == 1) {
                    htmlid = "lelder7b";
                } else if (player.getLevel() >= 40) {
                    htmlid = "lelder1";
                }
            } else if (npcid == 71076) { // ヤングリザードマンファイター
                if (player.getQuest().get_step(L1PcQuest.QUEST_LIZARD) == L1PcQuest.QUEST_END) {
                    htmlid = "ylizardb";
                } else {
                }
            } else if (npcid == 80079) { // ケプリシャ
                if ((player.getQuest().get_step(L1PcQuest.QUEST_KEPLISHA) == L1PcQuest.QUEST_END)
                        && !player.getInventory().checkItem(41312)) {
                    htmlid = "keplisha6";
                } else {
                    if (player.getInventory().checkItem(41314)) { // 占星术师のお守り
                        htmlid = "keplisha3";
                    } else if (player.getInventory().checkItem(41313)) { // 占星术师の玉
                        htmlid = "keplisha2";
                    } else if (player.getInventory().checkItem(41312)) { // 占星术师の壶
                        htmlid = "keplisha4";
                    }
                }
            } else if (npcid == 80102) { // フィリス
                if (player.getInventory().checkItem(41329)) { // 剥制の制作依赖书
                    htmlid = "fillis3";
                }
            } else if (npcid == 71167) { // フリム
                if (player.getTempCharGfx() == 3887) {// キャリングダークエルフ变身
                    htmlid = "frim1";
                }
            } else if (npcid == 71141) { // 坑夫オーム1
                if (player.getTempCharGfx() == 3887) {// キャリングダークエルフ变身
                    htmlid = "moumthree1";
                }
            } else if (npcid == 71142) { // 坑夫オーム2
                if (player.getTempCharGfx() == 3887) {// キャリングダークエルフ变身
                    htmlid = "moumtwo1";
                }
            } else if (npcid == 71145) { // 坑夫オーム3
                if (player.getTempCharGfx() == 3887) {// キャリングダークエルフ变身
                    htmlid = "moumone1";
                }

            } else if (npcid == 81200) { // 特典アイテム管理人
                if (player.getInventory().checkItem(21069) // 新生のベルト
                        || player.getInventory().checkItem(21074)) { // 亲睦のイアリング
                    htmlid = "c_belt";
                }
            } else if (npcid == 80076) { // 倒れた航海士
                if (player.getInventory().checkItem(41058)) { // 完成した航海日志
                    htmlid = "voyager8";
                } else if (player.getInventory().checkItem(49082) // 未完成の航海日志
                        || player.getInventory().checkItem(49083)) {
                    // ページを追加していない状态
                    if (player.getInventory().checkItem(41038) // 航海日志 1ページ
                            || player.getInventory().checkItem(41039) // 航海日志
                            // 2ページ
                            || player.getInventory().checkItem(41039) // 航海日志
                            // 3ページ
                            || player.getInventory().checkItem(41039) // 航海日志
                            // 4ページ
                            || player.getInventory().checkItem(41039) // 航海日志
                            // 5ページ
                            || player.getInventory().checkItem(41039) // 航海日志
                            // 6ページ
                            || player.getInventory().checkItem(41039) // 航海日志
                            // 7ページ
                            || player.getInventory().checkItem(41039) // 航海日志
                            // 8ページ
                            || player.getInventory().checkItem(41039) // 航海日志
                            // 9ページ
                            || player.getInventory().checkItem(41039)) { // 航海日志
                        // 10ページ
                        htmlid = "voyager9";
                    } else {
                        htmlid = "voyager7";
                    }
                } else if (player.getInventory().checkItem(49082) // 未完成の航海日志
                        || player.getInventory().checkItem(49083)
                        || player.getInventory().checkItem(49084)
                        || player.getInventory().checkItem(49085)
                        || player.getInventory().checkItem(49086)
                        || player.getInventory().checkItem(49087)
                        || player.getInventory().checkItem(49088)
                        || player.getInventory().checkItem(49089)
                        || player.getInventory().checkItem(49090)
                        || player.getInventory().checkItem(49091)) {
                    // ページを追加した状态
                    htmlid = "voyager7";
                }
            } else if (npcid == 80048) { // 空间の歪み
                final int level = player.getLevel();
                if (level <= 44) {
                    htmlid = "entgate3";
                } else if ((level >= 45) && (level <= 51)) {
                    htmlid = "entgate2";
                } else {
                    htmlid = "entgate";
                }
            } else if (npcid == 71168) { // 真冥王 ダンテス
                if (player.getInventory().checkItem(41028)) { // デスナイトの书
                    htmlid = "dantes1";
                }
            } else if (npcid == 80067) { // 谍报员(欲望の洞窟)
                if (player.getQuest().get_step(L1PcQuest.QUEST_DESIRE) == L1PcQuest.QUEST_END) {
                    htmlid = "minicod10";
                } else if (player.getKarmaLevel() >= 1) {
                    htmlid = "minicod07";
                } else if ((player.getQuest().get_step(L1PcQuest.QUEST_DESIRE) == 1)
                        && (player.getTempCharGfx() == 6034)) { // コラププリースト变身
                    htmlid = "minicod03";
                } else if ((player.getQuest().get_step(L1PcQuest.QUEST_DESIRE) == 1)
                        && (player.getTempCharGfx() != 6034)) {
                    htmlid = "minicod05";
                } else if ((player.getQuest().get_step(L1PcQuest.QUEST_SHADOWS) == L1PcQuest.QUEST_END // 影の神殿侧クエスト终了
                )
                        || player.getInventory().checkItem(41121) // カヘルの指令书
                        || player.getInventory().checkItem(41122)) { // カヘルの命令书
                    htmlid = "minicod01";
                } else if (player.getInventory().checkItem(41130) // 血痕の指令书
                        && player.getInventory().checkItem(41131)) { // 血痕の命令书
                    htmlid = "minicod06";
                } else if (player.getInventory().checkItem(41130)) { // 血痕の命令书
                    htmlid = "minicod02";
                }
            } else if (npcid == 81202) { // 谍报员(影の神殿)
                if (player.getQuest().get_step(L1PcQuest.QUEST_SHADOWS) == L1PcQuest.QUEST_END) {
                    htmlid = "minitos10";
                } else if (player.getKarmaLevel() <= -1) {
                    htmlid = "minitos07";
                } else if ((player.getQuest().get_step(L1PcQuest.QUEST_SHADOWS) == 1)
                        && (player.getTempCharGfx() == 6035)) { // レッサーデーモン变身
                    htmlid = "minitos03";
                } else if ((player.getQuest().get_step(L1PcQuest.QUEST_SHADOWS) == 1)
                        && (player.getTempCharGfx() != 6035)) {
                    htmlid = "minitos05";
                } else if ((player.getQuest().get_step(L1PcQuest.QUEST_DESIRE) == L1PcQuest.QUEST_END // 欲望の洞窟侧クエスト终了
                )
                        || player.getInventory().checkItem(41130) // 血痕の指令书
                        || player.getInventory().checkItem(41131)) { // 血痕の命令书
                    htmlid = "minitos01";
                } else if (player.getInventory().checkItem(41121) // カヘルの指令书
                        && player.getInventory().checkItem(41122)) { // カヘルの命令书
                    htmlid = "minitos06";
                } else if (player.getInventory().checkItem(41121)) { // カヘルの命令书
                    htmlid = "minitos02";
                }
            } else if (npcid == 81208) { // 污れたブロッブ
                if (player.getInventory().checkItem(41129) // 血痕の精髓
                        || player.getInventory().checkItem(41138)) { // カヘルの精髓
                    htmlid = "minibrob04";
                } else if ((player.getInventory().checkItem(41126) // 血痕の堕落した精髓
                        && player.getInventory().checkItem(41127) // 血痕の无力な精髓
                && player.getInventory().checkItem(41128) // 血痕の我执な精髓
                        )
                        || (player.getInventory().checkItem(41135) // カヘルの堕落した精髓
                                && player.getInventory().checkItem(41136) // カヘルの我执な精髓
                        && player.getInventory().checkItem(41137))) { // カヘルの我执な精髓
                    htmlid = "minibrob02";
                }
            } else if (npcid == 50113) { // 溪谷の村 レックマン
                if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == L1PcQuest.QUEST_END) {
                    htmlid = "orena14";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 1) {
                    htmlid = "orena0";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 2) {
                    htmlid = "orena2";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 3) {
                    htmlid = "orena3";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 4) {
                    htmlid = "orena4";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 5) {
                    htmlid = "orena5";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 6) {
                    htmlid = "orena6";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 7) {
                    htmlid = "orena7";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 8) {
                    htmlid = "orena8";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 9) {
                    htmlid = "orena9";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 10) {
                    htmlid = "orena10";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 11) {
                    htmlid = "orena11";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 12) {
                    htmlid = "orena12";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 13) {
                    htmlid = "orena13";
                }
            } else if (npcid == 50112) { // 旧?歌う岛 セリアン
                if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == L1PcQuest.QUEST_END) {
                    htmlid = "orenb14";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 1) {
                    htmlid = "orenb0";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 2) {
                    htmlid = "orenb2";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 3) {
                    htmlid = "orenb3";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 4) {
                    htmlid = "orenb4";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 5) {
                    htmlid = "orenb5";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 6) {
                    htmlid = "orenb6";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 7) {
                    htmlid = "orenb7";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 8) {
                    htmlid = "orenb8";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 9) {
                    htmlid = "orenb9";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 10) {
                    htmlid = "orenb10";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 11) {
                    htmlid = "orenb11";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 12) {
                    htmlid = "orenb12";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 13) {
                    htmlid = "orenb13";
                }
            } else if (npcid == 50111) { // 话せる岛 リリー
                if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == L1PcQuest.QUEST_END) {
                    htmlid = "orenc14";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 1) {
                    htmlid = "orenc1";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 2) {
                    htmlid = "orenc0";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 3) {
                    htmlid = "orenc3";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 4) {
                    htmlid = "orenc4";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 5) {
                    htmlid = "orenc5";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 6) {
                    htmlid = "orenc6";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 7) {
                    htmlid = "orenc7";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 8) {
                    htmlid = "orenc8";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 9) {
                    htmlid = "orenc9";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 10) {
                    htmlid = "orenc10";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 11) {
                    htmlid = "orenc11";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 12) {
                    htmlid = "orenc12";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 13) {
                    htmlid = "orenc13";
                }
            } else if (npcid == 50116) { // グルディオ ギオン
                if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == L1PcQuest.QUEST_END) {
                    htmlid = "orend14";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 1) {
                    htmlid = "orend3";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 2) {
                    htmlid = "orend1";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 3) {
                    htmlid = "orend0";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 4) {
                    htmlid = "orend4";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 5) {
                    htmlid = "orend5";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 6) {
                    htmlid = "orend6";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 7) {
                    htmlid = "orend7";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 8) {
                    htmlid = "orend8";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 9) {
                    htmlid = "orend9";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 10) {
                    htmlid = "orend10";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 11) {
                    htmlid = "orend11";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 12) {
                    htmlid = "orend12";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 13) {
                    htmlid = "orend13";
                }
            } else if (npcid == 50117) { // ケント シリア
                if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == L1PcQuest.QUEST_END) {
                    htmlid = "orene14";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 1) {
                    htmlid = "orene3";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 2) {
                    htmlid = "orene4";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 3) {
                    htmlid = "orene1";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 4) {
                    htmlid = "orene0";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 5) {
                    htmlid = "orene5";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 6) {
                    htmlid = "orene6";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 7) {
                    htmlid = "orene7";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 8) {
                    htmlid = "orene8";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 9) {
                    htmlid = "orene9";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 10) {
                    htmlid = "orene10";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 11) {
                    htmlid = "orene11";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 12) {
                    htmlid = "orene12";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 13) {
                    htmlid = "orene13";
                }
            } else if (npcid == 50119) { // ウッドベック オシーリア
                if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == L1PcQuest.QUEST_END) {
                    htmlid = "orenf14";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 1) {
                    htmlid = "orenf3";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 2) {
                    htmlid = "orenf4";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 3) {
                    htmlid = "orenf5";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 4) {
                    htmlid = "orenf1";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 5) {
                    htmlid = "orenf0";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 6) {
                    htmlid = "orenf6";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 7) {
                    htmlid = "orenf7";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 8) {
                    htmlid = "orenf8";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 9) {
                    htmlid = "orenf9";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 10) {
                    htmlid = "orenf10";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 11) {
                    htmlid = "orenf11";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 12) {
                    htmlid = "orenf12";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 13) {
                    htmlid = "orenf13";
                }
            } else if (npcid == 50121) { // 火田村 ホーニン
                if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == L1PcQuest.QUEST_END) {
                    htmlid = "oreng14";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 1) {
                    htmlid = "oreng3";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 2) {
                    htmlid = "oreng4";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 3) {
                    htmlid = "oreng5";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 4) {
                    htmlid = "oreng6";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 5) {
                    htmlid = "oreng1";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 6) {
                    htmlid = "oreng0";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 7) {
                    htmlid = "oreng7";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 8) {
                    htmlid = "oreng8";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 9) {
                    htmlid = "oreng9";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 10) {
                    htmlid = "oreng10";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 11) {
                    htmlid = "oreng11";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 12) {
                    htmlid = "oreng12";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 13) {
                    htmlid = "oreng13";
                }
            } else if (npcid == 50114) { // エルフの森 チコ
                if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == L1PcQuest.QUEST_END) {
                    htmlid = "orenh14";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 1) {
                    htmlid = "orenh3";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 2) {
                    htmlid = "orenh4";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 3) {
                    htmlid = "orenh5";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 4) {
                    htmlid = "orenh6";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 5) {
                    htmlid = "orenh7";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 6) {
                    htmlid = "orenh1";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 7) {
                    htmlid = "orenh0";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 8) {
                    htmlid = "orenh8";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 9) {
                    htmlid = "orenh9";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 10) {
                    htmlid = "orenh10";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 11) {
                    htmlid = "orenh11";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 12) {
                    htmlid = "orenh12";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 13) {
                    htmlid = "orenh13";
                }
            } else if (npcid == 50120) { // シルバーナイトタウン ホップ
                if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == L1PcQuest.QUEST_END) {
                    htmlid = "oreni14";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 1) {
                    htmlid = "oreni3";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 2) {
                    htmlid = "oreni4";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 3) {
                    htmlid = "oreni5";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 4) {
                    htmlid = "oreni6";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 5) {
                    htmlid = "oreni7";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 6) {
                    htmlid = "oreni8";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 7) {
                    htmlid = "oreni1";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 8) {
                    htmlid = "oreni0";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 9) {
                    htmlid = "oreni9";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 10) {
                    htmlid = "oreni10";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 11) {
                    htmlid = "oreni11";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 12) {
                    htmlid = "oreni12";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 13) {
                    htmlid = "oreni13";
                }
            } else if (npcid == 50122) { // ギラン ターク
                if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == L1PcQuest.QUEST_END) {
                    htmlid = "orenj14";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 1) {
                    htmlid = "orenj3";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 2) {
                    htmlid = "orenj4";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 3) {
                    htmlid = "orenj5";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 4) {
                    htmlid = "orenj6";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 5) {
                    htmlid = "orenj7";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 6) {
                    htmlid = "orenj8";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 7) {
                    htmlid = "orenj9";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 8) {
                    htmlid = "orenj1";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 9) {
                    htmlid = "orenj0";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 10) {
                    htmlid = "orenj10";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 11) {
                    htmlid = "orenj11";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 12) {
                    htmlid = "orenj12";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 13) {
                    htmlid = "orenj13";
                }
            } else if (npcid == 50123) { // ハイネ ガリオン
                if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == L1PcQuest.QUEST_END) {
                    htmlid = "orenk14";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 1) {
                    htmlid = "orenk3";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 2) {
                    htmlid = "orenk4";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 3) {
                    htmlid = "orenk5";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 4) {
                    htmlid = "orenk6";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 5) {
                    htmlid = "orenk7";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 6) {
                    htmlid = "orenk8";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 7) {
                    htmlid = "orenk9";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 8) {
                    htmlid = "orenk10";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 9) {
                    htmlid = "orenk1";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 10) {
                    htmlid = "orenk0";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 11) {
                    htmlid = "orenk11";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 12) {
                    htmlid = "orenk12";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 13) {
                    htmlid = "orenk13";
                }
            } else if (npcid == 50125) { // 象牙の塔 ギルバート
                if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == L1PcQuest.QUEST_END) {
                    htmlid = "orenl14";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 1) {
                    htmlid = "orenl3";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 2) {
                    htmlid = "orenl4";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 3) {
                    htmlid = "orenl5";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 4) {
                    htmlid = "orenl6";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 5) {
                    htmlid = "orenl7";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 6) {
                    htmlid = "orenl8";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 7) {
                    htmlid = "orenl9";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 8) {
                    htmlid = "orenl10";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 9) {
                    htmlid = "orenl11";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 10) {
                    htmlid = "orenl1";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 11) {
                    htmlid = "orenl0";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 12) {
                    htmlid = "orenl12";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 13) {
                    htmlid = "orenl13";
                }
            } else if (npcid == 50124) { // ウェルダン フォリカン
                if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == L1PcQuest.QUEST_END) {
                    htmlid = "orenm14";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 1) {
                    htmlid = "orenm3";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 2) {
                    htmlid = "orenm4";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 3) {
                    htmlid = "orenm5";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 4) {
                    htmlid = "orenm6";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 5) {
                    htmlid = "orenm7";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 6) {
                    htmlid = "orenm8";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 7) {
                    htmlid = "orenm9";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 8) {
                    htmlid = "orenm10";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 9) {
                    htmlid = "orenm11";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 10) {
                    htmlid = "orenm12";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 11) {
                    htmlid = "orenm1";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 12) {
                    htmlid = "orenm0";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 13) {
                    htmlid = "orenm13";
                }
            } else if (npcid == 50126) { // アデン ジェリック
                if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == L1PcQuest.QUEST_END) {
                    htmlid = "orenn14";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 1) {
                    htmlid = "orenn3";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 2) {
                    htmlid = "orenn4";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 3) {
                    htmlid = "orenn5";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 4) {
                    htmlid = "orenn6";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 5) {
                    htmlid = "orenn7";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 6) {
                    htmlid = "orenn8";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 7) {
                    htmlid = "orenn9";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 8) {
                    htmlid = "orenn10";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 9) {
                    htmlid = "orenn11";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 10) {
                    htmlid = "orenn12";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 11) {
                    htmlid = "orenn13";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 12) {
                    htmlid = "orenn1";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 13) {
                    htmlid = "orenn0";
                }
            } else if (npcid == 50115) { // 沈默の洞窟 ザルマン
                if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == L1PcQuest.QUEST_END) {
                    htmlid = "oreno0";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 1) {
                    htmlid = "oreno3";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 2) {
                    htmlid = "oreno4";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 3) {
                    htmlid = "oreno5";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 4) {
                    htmlid = "oreno6";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 5) {
                    htmlid = "oreno7";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 6) {
                    htmlid = "oreno8";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 7) {
                    htmlid = "oreno9";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 8) {
                    htmlid = "oreno10";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 9) {
                    htmlid = "oreno11";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 10) {
                    htmlid = "oreno12";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 11) {
                    htmlid = "oreno13";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 12) {
                    htmlid = "oreno14";
                } else if (player.getQuest().get_step(L1PcQuest.QUEST_TOSCROLL) == 13) {
                    htmlid = "oreno1";
                }

            } else if (npcid == 70838) { // ネルファ
                if (player.isCrown() || player.isKnight() || player.isWizard()
                        || player.isDragonKnight() || player.isIllusionist()) {
                    htmlid = "nerupam1";
                } else if (player.isDarkelf() && (player.getLawful() <= -1)) {
                    htmlid = "nerupaM2";
                } else if (player.isDarkelf()) {
                    htmlid = "nerupace1";
                } else if (player.isElf()) {
                    htmlid = "nerupae1";
                }
                /*
                 * } else if (npcid == 80094) { // 祭坛 if
                 * (player.isIllusionist()) { htmlid = "altar1"; } else if
                 * (!player.isIllusionist()) { htmlid = "altar2"; }
                 */
    			//添加宝石加工师大卫 add hjx1000
            } else if (npcid == 81261) { // 寶石加工師^大衛
                if (player.getInventory().checkItem(49031, 1)) { // 冰之結晶
                    if (player.getInventory().checkItem(21081, 1)) {
                        htmlid = "gemout1";
                    } else if (player.getInventory().checkItem(21082, 1)) {
                        htmlid = "gemout2";
                    } else if (player.getInventory().checkItem(21083, 1)) {
                        htmlid = "gemout3";
                    } else if (player.getInventory().checkItem(21084, 1)) {
                        htmlid = "gemout4";
                    } else if (player.getInventory().checkItem(21085, 1)) {
                        htmlid = "gemout5";
                    } else if (player.getInventory().checkItem(21086, 1)) {
                        htmlid = "gemout6";
                    } else if (player.getInventory().checkItem(21087, 1)) {
                        htmlid = "gemout7";
                    } else if (player.getInventory().checkItem(21088, 1)) {
                        htmlid = "gemout8";
                    } else {
                        htmlid = "gemout17";
                    }
                }
                //添加宝石加工师大卫 end
            } else if (npcid == 80099) { // 治安团长ラルソン
                if (player.getQuest().get_step(
                        L1PcQuest.QUEST_GENERALHAMELOFRESENTMENT) == 1) {
                    if (player.getInventory().checkItem(41325, 1)) {
                        htmlid = "rarson8";
                    } else {
                        htmlid = "rarson10";
                    }
                } else if (player.getQuest().get_step(
                        L1PcQuest.QUEST_GENERALHAMELOFRESENTMENT) == 2) {
                    if (player.getInventory().checkItem(41317, 1)
                            && player.getInventory().checkItem(41315, 1)) {
                        htmlid = "rarson13";
                    } else {
                        htmlid = "rarson19";
                    }
                } else if (player.getQuest().get_step(
                        L1PcQuest.QUEST_GENERALHAMELOFRESENTMENT) == 3) {
                    htmlid = "rarson14";
                } else if (player.getQuest().get_step(
                        L1PcQuest.QUEST_GENERALHAMELOFRESENTMENT) == 4) {
                    if (!(player.getInventory().checkItem(41326, 1))) {
                        htmlid = "rarson18";
                    } else if (player.getInventory().checkItem(41326, 1)) {
                        htmlid = "rarson11";
                    } else {
                        htmlid = "rarson17";
                    }
                } else if (player.getQuest().get_step(
                        L1PcQuest.QUEST_GENERALHAMELOFRESENTMENT) >= 5) {
                    htmlid = "rarson1";
                }
            } else if (npcid == 80101) { // クエン
                if (player.getQuest().get_step(
                        L1PcQuest.QUEST_GENERALHAMELOFRESENTMENT) == 4) {
                    if ((player.getInventory().checkItem(41315, 1))
                            && player.getInventory().checkItem(40494, 30)
                            && player.getInventory().checkItem(41317, 1)) {
                        htmlid = "kuen4";
                    } else if (player.getInventory().checkItem(41316, 1)) {
                        htmlid = "kuen1";
                    } else if (!player.getInventory().checkItem(41316)) {
                        player.getQuest().set_step(
                                L1PcQuest.QUEST_GENERALHAMELOFRESENTMENT, 1);
                    }
                } else if ((player.getQuest().get_step(
                        L1PcQuest.QUEST_GENERALHAMELOFRESENTMENT) == 2)
                        && (player.getInventory().checkItem(41317, 1))) {
                    htmlid = "kuen3";
                } else {
                    htmlid = "kuen1";
                }
            }

            // html表示パケット送信
            if (htmlid != null) { // htmlidが指定されている场合
                if (htmldata != null) { // html指定がある场合は表示
                    player.sendPackets(new S_NPCTalkReturn(objid, htmlid,
                            htmldata));
                } else {
                    player.sendPackets(new S_NPCTalkReturn(objid, htmlid));
                }
            } else {
                if (player.getLawful() < -1000) { // プレイヤーがカオティック
                    player.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
                } else {
                    player.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
                }
            }

            // 动作暂停
            set_stop_time(REST_MILLISEC);
            this.setRest(true);
        }
    }

    private static String talkToTownadviser(final L1PcInstance pc,
            final int town_id) {
        String htmlid;
        if ((pc.getHomeTownId() == town_id)
                && TownReading.get().isLeader(pc, town_id)) {
            htmlid = "secretary1";
        } else {
            htmlid = "secretary2";
        }

        return htmlid;
    }

    private static String talkToTownmaster(final L1PcInstance pc,
            final int town_id) {
        String htmlid;
        if (pc.getHomeTownId() == town_id) {
            htmlid = "hometown";
        } else {
            htmlid = "othertown";
        }
        return htmlid;
    }

    @Override
    public void onFinalAction(final L1PcInstance player, final String action) {
    }

    public void doFinalAction(final L1PcInstance player) {
    }

    private boolean checkHasCastle(final L1PcInstance player,
            final int castle_id) {
        if (player.getClanid() != 0) { // クラン所属中
            final L1Clan clan = WorldClan.get().getClan(player.getClanname());
            if (clan != null) {
                if (clan.getCastleId() == castle_id) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkClanLeader(final L1PcInstance player) {
        if (player.isCrown()) { // 君主
            final L1Clan clan = WorldClan.get().getClan(player.getClanname());
            if (clan != null) {
                if (player.getId() == clan.getLeaderId()) {
                    return true;
                }
            }
        }
        return false;
    }

    private int getNecessarySealCount(final L1PcInstance pc) {
        int rulerCount = 0;
        int necessarySealCount = 10;
        if (pc.getInventory().checkItem(40917)) { // 地の支配者
            rulerCount++;
        }
        if (pc.getInventory().checkItem(40920)) { // 风の支配者
            rulerCount++;
        }
        if (pc.getInventory().checkItem(40918)) { // 水の支配者
            rulerCount++;
        }
        if (pc.getInventory().checkItem(40919)) { // 火の支配者
            rulerCount++;
        }
        if (rulerCount == 0) {
            necessarySealCount = 10;
        } else if (rulerCount == 1) {
            necessarySealCount = 100;
        } else if (rulerCount == 2) {
            necessarySealCount = 200;
        } else if (rulerCount == 3) {
            necessarySealCount = 500;
        }
        return necessarySealCount;
    }

    private void createRuler(final L1PcInstance pc, final int attr,
            final int sealCount) {
        // 1.地属性,2.火属性,4.水属性,8.风属性
        int rulerId = 0;
        int protectionId = 0;
        int sealId = 0;
        if (attr == 1) {
            rulerId = 40917;
            protectionId = 40909;
            sealId = 40913;
        } else if (attr == 2) {
            rulerId = 40919;
            protectionId = 40911;
            sealId = 40915;
        } else if (attr == 4) {
            rulerId = 40918;
            protectionId = 40910;
            sealId = 40914;
        } else if (attr == 8) {
            rulerId = 40920;
            protectionId = 40912;
            sealId = 40916;
        }
        pc.getInventory().consumeItem(protectionId, 1);
        pc.getInventory().consumeItem(sealId, sealCount);
        final L1ItemInstance item = pc.getInventory().storeItem(rulerId, 1);
        if (item != null) {
            pc.sendPackets(new S_ServerMessage(143, this.getNpcTemplate()
                    .get_name(), item.getLogName())); // \f1%0が%1をくれました。
        }
    }

    private String talkToRuba(final L1PcInstance pc) {
        String htmlid = "";

        if (pc.isCrown() || pc.isWizard()) {
            htmlid = "en0101";
        } else if (pc.isKnight() || pc.isElf() || pc.isDarkelf()) {
            htmlid = "en0102";
        }

        return htmlid;
    }

    private String talkToSecondtbox(final L1PcInstance pc) {
        String htmlid = "";
        if (pc.getQuest().get_step(L1PcQuest.QUEST_TBOX1) == L1PcQuest.QUEST_END) {
            if (pc.getInventory().checkItem(40701)) {
                htmlid = "maptboxa";
            } else {
                htmlid = "maptbox0";
            }
        } else {
            htmlid = "maptbox0";
        }
        return htmlid;
    }

    private String talkToThirdtbox(final L1PcInstance pc) {
        String htmlid = "";
        if (pc.getQuest().get_step(L1PcQuest.QUEST_TBOX2) == L1PcQuest.QUEST_END) {
            if (pc.getInventory().checkItem(40701)) {
                htmlid = "maptboxd";
            } else {
                htmlid = "maptbox0";
            }
        } else {
            htmlid = "maptbox0";
        }
        return htmlid;
    }
}
