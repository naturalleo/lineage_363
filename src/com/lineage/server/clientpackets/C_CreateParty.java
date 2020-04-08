package com.lineage.server.clientpackets;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Party;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

/**
 * 要求邀请加入队伍(要求创立队伍)
 * 
 * @author daien
 * 
 */
public class C_CreateParty extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_CreateParty.class);

    /*
     * public C_CreateParty() { }
     * 
     * public C_CreateParty(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
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

            final int type = this.readC();

            switch (type) {
                case 0:
                case 1: // パーティー(パーティー自动分配ON/OFFで异なる)
                    final int targetId = this.readD();
                    final L1Object temp = World.get().findObject(targetId);
                    if (temp instanceof L1PcInstance) {
                        final L1PcInstance targetPc = (L1PcInstance) temp;
                        if (pc.getId() == targetPc.getId()) {
                            return;
                        }
                        
                        // 邀请组队时，对象不在荧幕内或是7步内 add hjx1000
                        if ((!pc.getLocation().isInScreen(targetPc.getLocation()) || (pc
                                .getLocation().getTileLineDistance(
                                        targetPc.getLocation()) > 7))) {
                            pc.sendPackets(new S_ServerMessage(952)); // 距离太远以致于无法邀请至队伍。
                            return;
                        }
                        if (targetPc.getMapId() != pc.getMapId()) {//add hjx1000
                            pc.sendPackets(new S_ServerMessage(952)); // 距离太远以致于无法邀请至队伍。
                            return;
                        }

                        if (targetPc.isInParty()) {
                            // 您无法邀请已经参加其他队伍的人。
                            pc.sendPackets(new S_ServerMessage(415));
                            return;
                        }

                        if (pc.isInParty()) {
                            if (pc.getParty().isLeader(pc)) {
                                targetPc.setPartyID(pc.getId());
                                // 玩家 %0%s 邀请您加入队伍？(Y/N)
                                targetPc.sendPackets(new S_Message_YN(953, pc
                                        .getName()));

                            } else {
                                // 只有领导者才能邀请其他的成员。
                                pc.sendPackets(new S_ServerMessage(416));
                            }

                        } else {
                            targetPc.setPartyID(pc.getId());
                            // 玩家 %0%s 邀请您加入队伍？(Y/N)
                            targetPc.sendPackets(new S_Message_YN(953, pc
                                    .getName()));
                        }
                    }
                    break;

                case 2: // チャットパーティー
                    final String name = this.readS();
                    final L1PcInstance targetPc = World.get().getPlayer(name);
                    if (targetPc == null) {
                        // 没有叫%0的人。
                        pc.sendPackets(new S_ServerMessage(109));
                        return;
                    }

                    if (pc.getId() == targetPc.getId()) {
                        return;
                    }
                    
                    // 邀请组队时，对象不在荧幕内或是7步内 add hjx1000
                    if ((!pc.getLocation().isInScreen(targetPc.getLocation()) || (pc
                            .getLocation().getTileLineDistance(
                                    targetPc.getLocation()) > 7))) {
                        pc.sendPackets(new S_ServerMessage(952)); // 距离太远以致于无法邀请至队伍。
                        return;
                    }
                    if (targetPc.getMapId() != pc.getMapId()) {//add hjx1000
                        pc.sendPackets(new S_ServerMessage(952)); // 距离太远以致于无法邀请至队伍。
                        return;
                    }

                    if (targetPc.isInChatParty()) {
                        // 您无法邀请已经参加其他队伍的人。
                        pc.sendPackets(new S_ServerMessage(415));
                        return;
                    }

                    if (pc.isInChatParty()) {
                        if (pc.getChatParty().isLeader(pc)) {
                            targetPc.setPartyID(pc.getId());
                            // 您要接受玩家 %0%s 提出的队伍对话邀请吗？(Y/N)
                            targetPc.sendPackets(new S_Message_YN(951, pc
                                    .getName()));

                        } else {
                            // 只有领导者才能邀请其他的成员。
                            pc.sendPackets(new S_ServerMessage(416));
                        }

                    } else {
                        targetPc.setPartyID(pc.getId());
                        // 您要接受玩家 %0%s 提出的队伍对话邀请吗？(Y/N)
                        targetPc.sendPackets(new S_Message_YN(951, pc.getName()));
                    }
                    break;

                case 3:// 队长转移
                    if (pc.isInParty()) {
                        if (pc.getParty().isLeader(pc)) {
                            final int objid = this.readD();
                            final L1Object object = World.get().findObject(
                                    objid);
                            if (object instanceof L1PcInstance) {
                                final L1PcInstance tgpc = (L1PcInstance) object;
                                // 不同地图
                                if (tgpc.getMapId() != pc.getMapId()) {
                                    // 1,695：要委任队长的队员没在附近。
                                    pc.sendPackets(new S_Message_YN(1695));
                                }
                                // 指定座标19格范围内
                                if (pc.getLocation().isInScreen(
                                        tgpc.getLocation())) {
                                    final HashMap<Integer, L1PcInstance> map = new HashMap<Integer, L1PcInstance>();
                                    map.putAll(pc.getParty().partyUsers());

                                    // 建立新的成员名单
                                    final ArrayList<L1PcInstance> newList = new ArrayList<L1PcInstance>();

                                    for (L1PcInstance newpc : map.values()) {
                                        // 不是新队长 加入新成员名单
                                        if (!newpc.equals(tgpc)) {
                                            newList.add(newpc);
                                        }
                                    }
                                    map.clear();

                                    // 解散原队伍
                                    pc.getParty().breakup();

                                    // 建立新队伍资讯
                                    final L1Party party = new L1Party();
                                    party.addMember(tgpc);// 第一个加入队伍者将为队长
                                    for (L1PcInstance newpc : newList) {
                                        party.addMember(newpc);
                                        // 424：%0%s 加入了您的队伍。
                                        tgpc.sendPackets(new S_ServerMessage(
                                                424, newpc.getName()));
                                    }

                                    // 传递新队长讯息
                                    party.msgToAll();
                                    newList.clear();

                                } else {
                                    // 1,695：要委任队长的队员没在附近。
                                    pc.sendPackets(new S_ServerMessage(1695));
                                }

                            }
                        } else {
                            // 1,697：不是队长，无法行使许可权。
                            pc.sendPackets(new S_ServerMessage(1697));
                        }
                    }
                    break;
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
