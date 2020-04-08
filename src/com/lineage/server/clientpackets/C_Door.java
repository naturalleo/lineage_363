package com.lineage.server.clientpackets;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.quest.DarkElfLv50_1;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.ActionCodes;
import com.lineage.server.datatables.lock.HouseReading;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.templates.L1House;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;

/**
 * 要求门的控制
 * 
 * @author daien
 * 
 */
public class C_Door extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_Door.class);

    /*
     * public C_Door() { }
     * 
     * public C_Door(final byte[] abyte0, final ClientExecutor client) {
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

            final int locX = this.readH();
            final int locY = this.readH();
            final int objectId = this.readD();

            final L1Object obj = World.get().findObject(objectId);

            if (obj instanceof L1DoorInstance) {
                final L1DoorInstance door = (L1DoorInstance) World.get()
                        .findObject(objectId);
                if (door == null) {
                    return;
                }

                if (((door.getDoorId() >= 5001) && (door.getDoorId() <= 5010))) { // 水晶の洞窟
                    return;
                }

                switch (door.getDoorId()) {
                    case 6006:
                        if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
                            return;
                        }
                        if (pc.getInventory().consumeItem(40163, 1)) { // 黄金钥匙
                            door.open();
                            final CloseTimer closetimer = new CloseTimer(door);
                            closetimer.begin();
                        }
                        break;

                    case 6007:
                        if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
                            return;
                        }
                        if (pc.getInventory().consumeItem(40313, 1)) { // 银钥匙
                            door.open();
                            final CloseTimer closetimer = new CloseTimer(door);
                            closetimer.begin();
                        }
                        break;

                    case 10000:// 不死族的钥匙
                        if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
                            return;
                        }
                        if (pc.getInventory().consumeItem(40581, 1)) { // 不死族的钥匙
                            door.open();
                        }
                        break;

                    case 10001:// 僵尸钥匙
                        if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
                            return;
                        }
                        if (pc.getInventory().consumeItem(40594, 1)) { // 僵尸钥匙
                            door.open();
                        }
                        break;

                    case 10002:// 骷髅钥匙
                        if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
                            return;
                        }
                        if (pc.getInventory().consumeItem(40604, 1)) { // 骷髅钥匙
                            door.open();
                        }
                        break;

                    case 10003:// 机关门(说明:不死族的叛徒 (法师30级以上官方任务))
                        break;

                    case 10004:// 蛇女房间钥匙
                        if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
                            return;
                        }
                        if (pc.getInventory().consumeItem(40543, 1)) { // 蛇女房间钥匙
                            door.open();
                        }
                        break;

                    case 10005:// 安塔瑞斯洞穴
                    case 10006:// 安塔瑞斯洞穴
                    case 10007:// 安塔瑞斯洞穴
                        break;

                    case 10008:// 法利昂洞穴
                    case 10009:// 法利昂洞穴
                    case 10010:// 法利昂洞穴
                        break;

                    case 10011:// 法利昂洞穴
                    case 10012:// 法利昂洞穴
                    case 10013:// 法利昂洞穴
                        break;

                    case 10019:// 魔法师．哈汀(故事) 禁开
                    case 10036:// 魔法师．哈汀(故事) 禁开
                        break;

                    case 10015:// 魔法师．哈汀(故事)// NO 1
                        if (pc.get_hardinR().DOOR_1) {
                            if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
                                door.close();
                            } else if (door.getOpenStatus() == ActionCodes.ACTION_Close) {
                                door.open();
                            }
                        }
                        break;

                    case 10016:// 魔法师．哈汀(故事)// NO 2
                        if (pc.get_hardinR().DOOR_2) {
                            if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
                                door.close();
                            } else if (door.getOpenStatus() == ActionCodes.ACTION_Close) {
                                door.open();
                            }
                        }
                        break;

                    case 10017:// 魔法师．哈汀(故事)// NO 2
                        if (pc.get_hardinR().DOOR_2) {
                            if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
                                door.close();
                            } else if (door.getOpenStatus() == ActionCodes.ACTION_Close) {
                                door.open();
                            }
                        }
                        break;

                    case 10020:// 魔法师．哈汀(故事)// NO 4
                        if (pc.get_hardinR().DOOR_4) {
                            if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
                                door.close();
                            } else if (door.getOpenStatus() == ActionCodes.ACTION_Close) {
                                door.open();
                                pc.get_hardinR().DOOR_4OPEN = true;
                            }
                        }
                        break;

                    default:
                        if (!this.isExistKeeper(pc, door.getKeeperId())) {
                            if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
                                door.close();

                            } else if (door.getOpenStatus() == ActionCodes.ACTION_Close) {
                                door.open();
                            }
                        }
                        break;
                }

            } else if (obj instanceof L1NpcInstance) {
                L1NpcInstance npc = (L1NpcInstance) obj;

                switch (npc.getNpcId()) {
                    case 70918:// 黑暗妖精试炼用宝箱
                        openDeLv50(pc, npc);
                        break;
                }
            }
            /*
             * if (door.getDoorId() == 6006) { // TIC2F if (door.getOpenStatus()
             * == ActionCodes.ACTION_Open) { return; }
             * 
             * if (pc.getInventory().consumeItem(40163, 1)) { // 黄金钥匙
             * door.open(); final CloseTimer closetimer = new CloseTimer(door);
             * closetimer.begin(); }
             * 
             * } else if (door.getDoorId() == 6007) { // TIC2F if
             * (door.getOpenStatus() == ActionCodes.ACTION_Open) { return; }
             * 
             * if (pc.getInventory().consumeItem(40313, 1)) { // 银钥匙
             * door.open(); final CloseTimer closetimer = new CloseTimer(door);
             * closetimer.begin(); }
             * 
             * } else if (!this.isExistKeeper(pc, door.getKeeperId())) { if
             * (door.getOpenStatus() == ActionCodes.ACTION_Open) { door.close();
             * 
             * } else if (door.getOpenStatus() == ActionCodes.ACTION_Close) {
             * door.open(); } }
             */

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    /**
     * 打开黑妖LV50试炼宝箱
     * 
     * @param npc
     * @param pc
     * 
     */
    private void openDeLv50(L1PcInstance pc, L1NpcInstance npc) {

        final L1ItemInstance item = pc.getInventory().checkItemX(40600, 1);// 堕落钥匙40600
        if (item != null) {
            pc.getInventory().removeItem(item, 1);
            final HashMap<Integer, L1Object> mapList = new HashMap<Integer, L1Object>();
            mapList.putAll(World.get().getVisibleObjects(DarkElfLv50_1.MAPID));
            // 任务地图内物件
            /*
             * final ConcurrentHashMap<Integer, L1Object> mapList =
             * World.get().getVisibleObjects(DarkElfLv50_1.MAPID);
             */

            npc.setStatus(ActionCodes.ACTION_On);
            npc.broadcastPacketAll(new S_DoActionGFX(npc.getId(),
                    ActionCodes.ACTION_On));

            for (L1Object tgobj : mapList.values()) {
                if (tgobj instanceof L1NpcInstance) {
                    L1NpcInstance tgnpc = (L1NpcInstance) tgobj;
                    if (tgnpc.getNpcId() == 70905) {// 黑暗妖精试炼用障碍
                        if (tgnpc.get_showId() == npc.get_showId()) {
                            // 移除
                            tgnpc.deleteMe();
                        }
                    }
                }
            }
            mapList.clear();
        }
    }

    /**
     * 盟屋大门管理
     * 
     * @param pc
     * @param keeperId
     * @return
     */
    private boolean isExistKeeper(final L1PcInstance pc, final int keeperId) {
        if (keeperId == 0) {
            return false;
        }
        if (pc.isGm()) {
            return false;
        }
        final L1Clan clan = WorldClan.get().getClan(pc.getClanname());
        if (clan != null) {
            final int houseId = clan.getHouseId();
            if (houseId != 0) {
                final L1House house = HouseReading.get().getHouseTable(houseId);
                if (keeperId == house.getKeeperId()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 自动关闭时间
     * 
     * @author daien
     */
    public class CloseTimer extends TimerTask {

        private L1DoorInstance _door;

        public CloseTimer(final L1DoorInstance door) {
            this._door = door;
        }

        @Override
        public void run() {
            if (this._door.getOpenStatus() == ActionCodes.ACTION_Open) {
                this._door.close();
            }
        }

        public void begin() {
            final Timer timer = new Timer();
            timer.schedule(this, 5 * 1000);
        }
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
