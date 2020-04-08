package com.lineage.server.clientpackets;

import java.util.Calendar;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.Config;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.NpcActionTable;
import com.lineage.server.datatables.lock.AuctionBoardReading;
import com.lineage.server.datatables.lock.CharItemsReading;
import com.lineage.server.datatables.lock.HouseReading;
import com.lineage.server.datatables.sql.AuctionBoardTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.npc.L1NpcHtml;
import com.lineage.server.model.npc.action.L1NpcAction;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1AuctionBoardTmp;
import com.lineage.server.templates.L1House;
import com.lineage.server.world.World;

/**
 * 要求物件对话视窗数量选取结果
 * 
 * @author dexc
 * 
 */
public class C_Amount extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_Amount.class);

    /*
     * public C_Amount() { }
     * 
     * public C_Amount(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client)
            throws Exception {
        try {
            // 资料载入
            this.read(decrypt);

            final L1PcInstance pc = client.getActiveChar();

            if (pc.isGhost()) { // 鬼魂模式
                return;
            }
            if (pc.isDead()) { // 死亡
                return;
            }
            if (pc.isTeleport()) { // 传送中
                return;
            }

            final int objectId = this.readD();// 对话物件OBJID

            int amount = Math.max(0, this.readD());// 数量
            if (amount <= 0) {
                return;
            }

            final int c = this.readC();// BYTE

            final String s = this.readS();// 命令文字

            if (amount > Integer.MAX_VALUE) {
                amount = Integer.MAX_VALUE;
            }

            final L1NpcInstance npc = (L1NpcInstance) World.get().findObject(
                    objectId);
            if (npc == null) {
                return;
            }

            String s1 = "";
            String s2 = "";
            try {
                final StringTokenizer stringtokenizer = new StringTokenizer(s);
                s1 = stringtokenizer.nextToken();
                s2 = stringtokenizer.nextToken();

            } catch (final NoSuchElementException e) {
                s1 = "";
                s2 = "";
            }

            if (s1.equalsIgnoreCase("agapply")) { // 盟屋拍卖布告出价
                final String pcName = pc.getName();
                final Collection<L1AuctionBoardTmp> boardList = AuctionBoardReading
                        .get().getAuctionBoardTableList().values();
                for (final L1AuctionBoardTmp board : boardList) {
                    if (pcName.equalsIgnoreCase(board.getBidder())) {
                        // 523 已经参与其他血盟小屋拍卖。
                        pc.sendPackets(new S_ServerMessage(523));
                        return;
                    }
                }

                final int houseId = Integer.valueOf(s2);
                final L1AuctionBoardTmp board = AuctionBoardReading.get()
                        .getAuctionBoardTable(houseId);
                if (board != null) {
                    // 传回目前售价
                    final long nowPrice = board.getPrice();
                    // 传回目前竞标者OBJID
                    final int nowBidderId = board.getBidderId();

                    // 检查金币
                    final L1ItemInstance item = pc.getInventory().checkItemX(
                            L1ItemId.ADENA, amount);
                    if (item != null) {
                        // 移除金币
                        if (pc.getInventory().consumeItem(L1ItemId.ADENA,
                                amount)) {
                            // 盟屋拍卖公告栏资料更新
                            board.setPrice(amount);
                            board.setBidder(pcName);
                            board.setBidderId(pc.getId());
                            AuctionBoardReading.get().updateAuctionBoard(board);

                            if (nowBidderId != 0) {
                                // 前竞标者金额退回
                                final L1PcInstance bidPc = (L1PcInstance) World
                                        .get().findObject(nowBidderId);
                                if (bidPc != null) { // 人物在线上
                                    bidPc.getInventory().storeItem(
                                            L1ItemId.ADENA, nowPrice);
                                    bidPc.sendPackets(new S_ServerMessage(525,
                                            String.valueOf(nowPrice)));

                                } else { // 人物不在线上
                                    CharItemsReading.get().getAdenaCount(
                                            nowBidderId, nowPrice);
                                }
                            }

                        } else {
                            // \f1金币不足。
                            pc.sendPackets(new S_ServerMessage(189));
                        }

                    } else {
                        // \f1金币不足。
                        pc.sendPackets(new S_ServerMessage(189));
                    }
                }

            } else if (s1.equalsIgnoreCase("agsell")) { // 出售
                if (npc.getNpcId() == 70535) {// 拍卖管理者
                    if (npc.ACTION != null) {
                        if (amount <= 0) {
                            return;
                        }
                        npc.ACTION.action(pc, npc, s, amount);
                        return;
                    }
                }
                final int houseId = Integer.valueOf(s2);
                final AuctionBoardTable boardTable = new AuctionBoardTable();
                final L1AuctionBoardTmp board = new L1AuctionBoardTmp();
                if (board != null) {
                    // 竞卖揭示板に新规书き迂み
                    board.setHouseId(houseId);
                    final L1House house = HouseReading.get().getHouseTable(
                            houseId);
                    board.setHouseName(house.getHouseName());
                    board.setHouseArea(house.getHouseArea());
                    final TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
                    final Calendar cal = Calendar.getInstance(tz);
                    cal.add(Calendar.DATE, 5); // 5日后
                    cal.set(Calendar.MINUTE, 0); // 分、秒は切り舍て
                    cal.set(Calendar.SECOND, 0);
                    board.setDeadline(cal);
                    board.setPrice(amount);
                    board.setLocation(house.getLocation());
                    board.setOldOwner(pc.getName());
                    board.setOldOwnerId(pc.getId());
                    board.setBidder("");
                    board.setBidderId(0);
                    boardTable.insertAuctionBoard(board);

                    house.setOnSale(true); // 竞卖中に设定
                    house.setPurchaseBasement(true); // 地下盟屋设置为未购入
                    HouseReading.get().updateHouse(house); // DBに书き迂み
                }

            } else {
                if (npc.ACTION != null) {
                    if (amount <= 0) {
                        return;
                    }
                    npc.ACTION.action(pc, npc, s, amount);
                    return;
                }

                final L1NpcAction action = NpcActionTable.getInstance().get(s,
                        pc, npc);
                if (action != null) {
                    final L1NpcHtml result = action.executeWithAmount(s, pc,
                            npc, amount);
                    if (result != null) {
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), result));
                    }
                    return;
                }
            }

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
