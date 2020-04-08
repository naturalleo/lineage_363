package com.lineage.server.utils;

import static com.lineage.server.model.skill.L1SkillId.MEDITATION;
import static com.lineage.server.model.skill.L1SkillId.WIND_SHACKLE;

import java.util.HashSet;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1DragonSlayer;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_NPCPack_Doll;
import com.lineage.server.serverpackets.S_MapID;
import com.lineage.server.serverpackets.S_OtherCharPacks;
import com.lineage.server.serverpackets.S_OwnCharPack;
import com.lineage.server.serverpackets.S_NPCPack_Pet;
import com.lineage.server.serverpackets.S_PacketBoxWindShackle;
import com.lineage.server.serverpackets.S_NPCPack_Summon;
import com.lineage.server.timecontroller.server.ServerUseMapTimer;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;

/**
 * 移动后座标更新
 * 
 * @author dexc
 * 
 */
public class Teleportation {

    private static final Log _log = LogFactory.getLog(Teleportation.class);

    private static Random _random = new Random();

    private Teleportation() {
    }

    /**
     * 移动后座标更新
     * 
     * @param pc
     */
    public static void teleportation(final L1PcInstance pc) {
        try {
            if (pc == null) {
                return;
            }

            if (pc.getOnlineStatus() == 0) {
                return;
            }

            if (pc.getNetConnection() == null) {
                return;
            }

            if (pc.isDead()) {
                return;
            }

            if (pc.isTeleport()) {
                return;
            }

            // 解除旧座标障碍宣告
            pc.getMap().setPassable(pc.getLocation(), true);

            int x = pc.getTeleportX();
            int y = pc.getTeleportY();
            short mapId = pc.getTeleportMapId();
            final int head = pc.getTeleportHeading();

            // 防止座标异常
            final L1Map map = L1WorldMap.get().getMap(mapId);

            if (!map.isInMap(x, y) && !pc.isGm()) {
                x = pc.getX();
                y = pc.getY();
                mapId = pc.getMapId();
            }

            
            if (mapId >= 53 && mapId <= 56){//修正奇岩地监到时间时顺飞可以呆在里面 hjx1000
            	pc.startRocksPrison();
            }
        	//限制某些地图开启挂机
//            if (pc.isActived()) { 
//            	if (pc.getMapId() == 70 ||
//                	pc.getMapId() == 303 ||
//                	pc.getMapId() == 400 ||
//                	pc.getMapId() == 410 ||
//                	pc.getMapId() == 5167 ||
//                	pc.getMapId() == 5168) {
//                	pc.setActived(false);
//                	pc.sendPackets(new S_ServerMessage("\\aD自动挂机已经结束。"));
//                }
//            }

            final L1Clan clan = WorldClan.get().getClan(pc.getClanname());
            if (clan != null) {
                if (clan.getWarehouseUsingChar() == pc.getId()) { // 使用血盟仓库中
                    clan.setWarehouseUsingChar(0); // 解除使用状态
                }
            }

            World.get().moveVisibleObject(pc, mapId);
            // 设置座标资讯
            pc.setLocation(x, y, mapId);
            pc.setHeading(head);

            // 记录移动前座标
            pc.setOleLocX(x);
            pc.setOleLocY(y);

            // 地图水中状态
            boolean isUnderwater = pc.getMap().isUnderwater();

            // 更新所在地图封包
            pc.sendPackets(new S_MapID(pc, pc.getMap().getBaseMapId(), isUnderwater));

            // 地图经验加倍
            /*
             * if (MapExpTable.get().get_level(pc.getMapId(), pc.getLevel())) {
             * // 1,226：稍微增加狩猎的经验值。 pc.sendPackets(new S_ServerMessage(1226)); }
             */

            // 发送人物资料给周边物件
            if (!pc.isGhost() && !pc.isGmInvis() && !pc.isInvisble()) {
                pc.broadcastPacketAll(new S_OtherCharPacks(pc));
            }

            if (pc.isReserveGhost()) { // 鬼魂状态解除
                pc.endGhost();
            }

            pc.sendPackets(new S_OwnCharPack(pc));

            pc.removeAllKnownObjects();
            pc.sendVisualEffectAtTeleport(); // クラウン、毒、水中等の视觉效果を表示
            pc.updateObject();
            // spr番号6310, 5641の变身中にテレポートするとテレポート后に移动できなくなる
            // 武器を着脱すると移动できるようになるため、S_CharVisualUpdateを送信する
            pc.sendPackets(new S_CharVisualUpdate(pc));

            pc.killSkillEffectTimer(MEDITATION);
            pc.setCallClanId(0); // コールクランを唱えた后に移动すると召唤无效

            /*
             * subjects ペットとサモンのテレポート先画面内へ居たプレイヤー。
             * 各ペット每にUpdateObjectを行う方がコード上ではスマートだが、
             * ネットワーク负荷が大きくなる为、一旦Setへ格纳して最后にまとめてUpdateObjectする。
             */
            final HashSet<L1PcInstance> subjects = new HashSet<L1PcInstance>();
            subjects.add(pc);

            if (!pc.isGhost()) {
                // 可以携带宠物
                if (pc.getMap().isTakePets()) {
                    // 宠物的跟随移动
                    for (final L1NpcInstance petNpc : pc.getPetList().values()) {
                        // 主人身边随机座标取回
                        final L1Location loc = pc.getLocation().randomLocation(
                                3, false);
                        int nx = loc.getX();
                        int ny = loc.getY();
                        if ((pc.getMapId() == 5125) || (pc.getMapId() == 5131)
                                || (pc.getMapId() == 5132)
                                || (pc.getMapId() == 5133)
                                || (pc.getMapId() == 5134)) { // 宠物战战场
                            nx = 32799 + _random.nextInt(5) - 3;
                            ny = 32864 + _random.nextInt(5) - 3;
                        }

                        // 设置副本编号
                        petNpc.set_showId(pc.get_showId());

                        teleport(petNpc, nx, ny, mapId, head);

                        if (petNpc instanceof L1SummonInstance) { // 召唤兽的跟随移动
                            final L1SummonInstance summon = (L1SummonInstance) petNpc;
                            pc.sendPackets(new S_NPCPack_Summon(summon, pc));

                        } else if (petNpc instanceof L1PetInstance) { // 宠物的跟随移动
                            final L1PetInstance pet = (L1PetInstance) petNpc;
                            pc.sendPackets(new S_NPCPack_Pet(pet, pc));
                        }

                        for (final L1PcInstance visiblePc : World.get()
                                .getVisiblePlayer(petNpc)) {
                            // 画面内可见人物 认识更新
                            visiblePc.removeKnownObject(petNpc);
                            if (visiblePc.get_showId() == petNpc.get_showId()) {
                                subjects.add(visiblePc);
                            }
                        }
                    }

                } else {
                    //
                }

                // 娃娃的跟随移动
                if (!pc.getDolls().isEmpty()) {
                    // 主人身边随机座标取回
                    final L1Location loc = pc.getLocation().randomLocation(3,
                            false);
                    final int nx = loc.getX();
                    final int ny = loc.getY();

                    final Object[] dolls = pc.getDolls().values().toArray();
                    for (final Object obj : dolls) {
                        final L1DollInstance doll = (L1DollInstance) obj;
                        teleport(doll, nx, ny, mapId, head);
                        pc.sendPackets(new S_NPCPack_Doll(doll, pc));
                        // 设置副本编号
                        doll.set_showId(pc.get_showId());

                        for (final L1PcInstance visiblePc : World.get()
                                .getVisiblePlayer(doll)) {
                            // 画面内可见人物 认识更新
                            visiblePc.removeKnownObject(doll);
                            if (visiblePc.get_showId() == doll.get_showId()) {
                                subjects.add(visiblePc);
                            }
                        }
                    }
                }
            }

            for (final L1PcInstance updatePc : subjects) {
                updatePc.updateObject();
            }

            Integer time = ServerUseMapTimer.MAP.get(pc);
            if (time != null) {
                ServerUseMapTimer.put(pc, time);
            }

            pc.setTeleport(false);

            if (pc.hasSkillEffect(WIND_SHACKLE)) {
                pc.sendPackets(new S_PacketBoxWindShackle(pc.getId(), pc
                        .getSkillEffectTimeSec(WIND_SHACKLE)));
            }
            // 副本编号与副本地图不符
            if ((pc.getPortalNumber() != -1)
                    && (pc.getMapId() != (1005 + pc.getPortalNumber()))) {
                L1DragonSlayer.getInstance().removePlayer(pc, pc.getPortalNumber());
                pc.setPortalNumber(-1);
            }

            // 新增座标障碍宣告
            pc.getMap().setPassable(pc.getLocation(), false);
            pc.getPetModel();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 宠物的传送
     * 
     * @param npc
     * @param x
     * @param y
     * @param map
     * @param head
     */
    private static void teleport(final L1NpcInstance npc, final int x,
            final int y, final short map, final int head) {
        try {
            World.get().moveVisibleObject(npc, map);

            L1WorldMap.get().getMap(npc.getMapId())
                    .setPassable(npc.getX(), npc.getY(), true, 2);
            npc.setX(x);
            npc.setY(y);
            npc.setMap(map);
            npc.setHeading(head);
            L1WorldMap.get().getMap(npc.getMapId())
                    .setPassable(npc.getX(), npc.getY(), false, 2);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
    /**
     * 魔法娃娃的传送
     * 
     * @param doll
     * @param x
     * @param y
     * @param map
     * @param head
     */
    private static void teleport(final L1DollInstance doll, final int x,
            final int y, final short map, final int head) {
        try {
            World.get().moveVisibleObject(doll, map);

            doll.setX(x);
            doll.setY(y);
            doll.setMap(map);
            doll.setHeading(head);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

}
