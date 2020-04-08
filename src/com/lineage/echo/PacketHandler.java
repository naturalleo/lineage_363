package com.lineage.echo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.Config;
import com.lineage.echo.encryptions.PacketPrint;
import com.lineage.server.clientpackets.*;

/**
 * 客户端封包处理
 * 
 * @author dexc
 * 
 */
public class PacketHandler extends PacketHandlerExecutor {

    private static final Log _log = LogFactory.getLog(PacketHandler.class);

    // Map<K,V>
    private static final Map<Integer, ClientBasePacket> _opListClient = new HashMap<Integer, ClientBasePacket>();

    private ClientExecutor _client;

    /**
     * 客户端封包处理
     * 
     * @param decrypt
     * @param object
     * @throws Exception
     */
    @Override
    public void handlePacket(final byte[] decrypt) {
        ClientBasePacket basePacket = null;
        if (decrypt == null) {
            return;
        }
        if (decrypt.length <= 0) {
            return;
        }
        try {
            final int key = decrypt[0] & 0xff;

            basePacket = _opListClient.get(key);

            if (Config.DEBUG) {
                if (basePacket != null) {
                    _log.info("客户端: "
                            + basePacket.getType()
                            + "\nOP ID: "
                            + key
                            + " length:"
                            + decrypt.length
                            + "\nInfo:\n"
                            + PacketPrint.get().printData(decrypt,
                                    decrypt.length));
                }
            }

            if (basePacket == null) {
                _log.info("\nClient: " + key + "\n"
                        + PacketPrint.get().printData(decrypt, decrypt.length)
                        + getNow_YMDHMS());

            } else {
                basePacket.start(decrypt, _client);
            }

        } catch (final Exception e) {
            if (Config.DEBUG) {
                String name = "Not Login Pc";
                if (_client.getActiveChar() != null) {
                    name = _client.getActiveChar().getName();
                }
                _log.error("OP ID: " + (decrypt[0] & 0xFF) + " Pc Name: "
                        + name + "\n" + basePacket.getType() + "\n"
                        + PacketPrint.get().printData(decrypt, decrypt.length),
                        e);
            }

        } finally {
            basePacket = null;
        }

    }

    /**
     * <font color=#00800>取得系统时间</font>
     * 
     * @return 传出标准时间格式 yyyy/MM/dd HH:mm:ss
     */
    private final String getNow_YMDHMS() {
        final String nowDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .format(new Date());
        return nowDate;
    }

    public PacketHandler(final ClientExecutor client) {
        _client = client;
    }

    /**
     * 设置执行类
     */
    public static void load() {
        PacketHandler.put(C_OPCODE_CHARRESET, new C_CharReset());
        PacketHandler.put(C_OPCODE_QUITGAME, new C_Disconnect());
        PacketHandler.put(C_OPCODE_EXCLUDE, new C_Exclude());
        PacketHandler.put(C_OPCODE_CHARACTERCONFIG, new C_CharcterConfig());
        PacketHandler.put(C_OPCODE_DOOR, new C_Door());
        PacketHandler.put(C_OPCODE_TITLE, new C_Title());
        PacketHandler.put(C_OPCODE_BOARDDELETE, new C_BoardDelete());
        PacketHandler.put(C_OPCODE_PLEDGE, new C_Pledge());
        PacketHandler.put(C_OPCODE_CHANGEHEADING, new C_ChangeHeading());
        PacketHandler.put(C_OPCODE_NPCACTION, new C_NPCAction());
        PacketHandler.put(C_OPCODE_USESKILL, new C_UseSkill());
        PacketHandler.put(C_OPCODE_EMBLEM, new C_Emblem());
        PacketHandler.put(C_OPCODE_TRADEADDCANCEL, new C_TradeCancel());
        PacketHandler.put(C_OPCODE_CHANGEWARTIME, new C_ChangeWarTime());
        PacketHandler.put(C_OPCODE_BOOKMARK, new C_AddBookmark());
        PacketHandler.put(C_OPCODE_CREATECLAN, new C_CreateClan());
        PacketHandler.put(C_OPCODE_CLIENTVERSION, new C_ServerVersion());
        PacketHandler.put(C_OPCODE_PROPOSE, new C_Propose());
        PacketHandler.put(C_OPCODE_BOARDBACK, new C_BoardBack());
        PacketHandler.put(C_OPCODE_SHOP, new C_Shop());
        PacketHandler.put(C_OPCODE_BOARDREAD, new C_BoardRead());
        PacketHandler.put(C_OPCODE_TRADE, new C_Trade());
        PacketHandler.put(C_OPCODE_DELETECHAR, new C_DeleteChar());
        PacketHandler.put(C_OPCODE_ATTR, new C_Attr());
        PacketHandler.put(C_OPCODE_LOGINPACKET, new C_AuthLogin());
        PacketHandler.put(C_OPCODE_RESULT, new C_Result());
        PacketHandler.put(C_OPCODE_DEPOSIT, new C_Deposit());
        PacketHandler.put(C_OPCODE_DRAWAL, new C_Drawal());
        PacketHandler.put(C_OPCODE_LOGINTOSERVEROK, new C_LoginToServerOK());

        PacketHandler.put(C_OPCODE_SKILLBUY, new C_SkillBuy());
        PacketHandler.put(C_OPCODE_SKILLBUYOK, new C_SkillBuyOK());

        PacketHandler.put(C_OPCODE_SKILLBUYITEM, new C_SkillBuyItem());
        PacketHandler.put(C_OPCODE_SKILLBUYOKITEM, new C_SkillBuyItemOK());

        PacketHandler.put(C_OPCODE_TRADEADDITEM, new C_TradeAddItem());
        PacketHandler.put(C_OPCODE_ADDBUDDY, new C_AddBuddy());
        PacketHandler.put(C_OPCODE_RETURNTOLOGIN, new C_ReturnToLogin());
        PacketHandler.put(C_OPCODE_CHAT, new C_Chat());
        PacketHandler.put(C_OPCODE_TRADEADDOK, new C_TradeOK());
        PacketHandler.put(C_OPCODE_CHECKPK, new C_CheckPK());
        PacketHandler.put(C_OPCODE_TAXRATE, new C_TaxRate());
        PacketHandler.put(C_OPCODE_CHANGECHAR, new C_NewCharSelect());
        PacketHandler.put(C_OPCODE_BUDDYLIST, new C_Buddy());
        PacketHandler.put(C_OPCODE_DROPITEM, new C_DropItem());
        PacketHandler.put(C_OPCODE_LEAVEPARTY, new C_LeaveParty());
        PacketHandler.put(C_OPCODE_ATTACK, new C_Attack());
        PacketHandler.put(C_OPCODE_ARROWATTACK, new C_AttackBow());
        PacketHandler.put(C_OPCODE_BANCLAN, new C_BanClan());
        PacketHandler.put(C_OPCODE_BOARD, new C_Board());
        PacketHandler.put(C_OPCODE_DELETEINVENTORYITEM,
                new C_DeleteInventoryItem());
        PacketHandler.put(C_OPCODE_CHATWHISPER, new C_ChatWhisper());
        PacketHandler.put(C_OPCODE_PARTY, new C_Party());
        PacketHandler.put(C_OPCODE_PICKUPITEM, new C_PickUpItem());
        PacketHandler.put(C_OPCODE_WHO, new C_Who());
        PacketHandler.put(C_OPCODE_GIVEITEM, new C_GiveItem());
        PacketHandler.put(C_OPCODE_MOVECHAR, new C_MoveChar());
        PacketHandler.put(C_OPCODE_BOOKMARKDELETE, new C_DeleteBookmark());
        PacketHandler.put(C_OPCODE_RESTART, new C_Restart());
        PacketHandler.put(C_OPCODE_LEAVECLANE, new C_LeaveClan());
        PacketHandler.put(C_OPCODE_NPCTALK, new C_NPCTalk());
        PacketHandler.put(C_OPCODE_BANPARTY, new C_BanParty());
        PacketHandler.put(C_OPCODE_DELBUDDY, new C_DelBuddy());
        PacketHandler.put(C_OPCODE_WAR, new C_War());
        PacketHandler.put(C_OPCODE_LOGINTOSERVER, new C_LoginToServer());
        PacketHandler.put(C_OPCODE_PRIVATESHOPLIST, new C_ShopList());
        PacketHandler.put(C_OPCODE_CHATGLOBAL, new C_ChatGlobal());
        PacketHandler.put(C_OPCODE_JOINCLAN, new C_JoinClan());

        PacketHandler.put(C_OPCODE_COMMONCLICK, new C_CommonClick());

        PacketHandler.put(C_OPCODE_NEWCHAR, new C_CreateChar());
        PacketHandler.put(C_OPCODE_EXTCOMMAND, new C_ExtraCommand());
        PacketHandler.put(C_OPCODE_BOARDWRITE, new C_BoardWrite());
        PacketHandler.put(C_OPCODE_USEITEM, new C_ItemUSe());
        PacketHandler.put(C_OPCODE_CREATEPARTY, new C_CreateParty());
        PacketHandler.put(C_OPCODE_ENTERPORTAL, new C_EnterPortal());
        PacketHandler.put(C_OPCODE_AMOUNT, new C_Amount());
        PacketHandler.put(C_OPCODE_FIX_WEAPON_LIST, new C_FixWeaponList());
        PacketHandler.put(C_OPCODE_SELECTLIST, new C_SelectList());
        PacketHandler.put(C_OPCODE_EXIT_GHOST, new C_ExitGhost());
        PacketHandler.put(C_OPCODE_CALL, new C_CallPlayer());
        PacketHandler.put(C_OPCODE_SELECTTARGET, new C_SelectTarget());
        PacketHandler.put(C_OPCODE_PETMENU, new C_PetMenu());
        PacketHandler.put(C_OPCODE_USEPETITEM, new C_UsePetItem());
        PacketHandler.put(C_OPCODE_RANK, new C_Rank());
        PacketHandler.put(C_OPCODE_CAHTPARTY, new C_ChatParty());
        PacketHandler.put(C_OPCODE_FIGHT, new C_Fight());
        PacketHandler.put(C_OPCODE_MAIL, new C_Mail());
        PacketHandler.put(C_OPCODE_SHIP, new C_Ship());
        PacketHandler.put(C_OPCODE_CLAN, new C_Clan());

        PacketHandler.put(C_OPCODE_NEWACC, new C_NewAccess());

        PacketHandler.put(C_OPCODE_TELEPORT, new C_Teleport());
        PacketHandler.put(C_OPCODE_TELEPORT2, new C_Teleport());

        PacketHandler.put(C_OPCODE_MOVELOCK, new C_UnLock());
        PacketHandler.put(C_OPCODE_CNITEM, new C_CnItem());
        PacketHandler.put(C_OPCODE_PWD, new C_Password());

        PacketHandler.put(C_OPCODE_KEEPALIVE, new C_KeepALIVE());

        // 33_TW
        PacketHandler.put(C_OPCODE_WINDOWS, new C_Windows());
        PacketHandler.put(C_OPCODE_AUTO, new C_AutoLogin());
        PacketHandler.put(C_OPCODE_FISHCLICK, new C_FishClick());

        PacketHandler.put(C_OPCODE_SHOPX2, new C_Unkonwn());
        PacketHandler.put(C_OPCODE_MSG, new C_Unkonwn());
        PacketHandler.put(C_OPCODE_HIRESOLDIER, new C_Unkonwn());
        PacketHandler.put(C_OPCODE_CLAN_RECOMMEND, new C_ClanMatching()); //血盟UI hjx1000
    }

    /**
     * 设置执行类
     */
    public static void put(final Integer key, final ClientBasePacket value) {
        if (_opListClient.get(key) == null) {
            _opListClient.put(key, value);

        } else {
            if (!key.equals(-1)) {
                _log.error("重复标记的OPID: " + key + " " + value.getType());
            }
        }
    }
}
