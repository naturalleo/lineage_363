package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.GetbackTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_MapID;
import com.lineage.server.serverpackets.S_OtherCharPacks;
import com.lineage.server.serverpackets.S_OwnCharPack;
import com.lineage.server.serverpackets.S_PacketBoxIcon1;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_Weather;
import com.lineage.server.world.World;

/**
 * 要求死亡后重新开始
 * 
 * @author daien
 * 
 */
public class C_Restart extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_Restart.class);

    /*
     * public C_Restart() { }
     * 
     * public C_Restart(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            // this.read(decrypt);

            final L1PcInstance pc = client.getActiveChar();

            if (pc != null) {

                int[] loc;

                if (pc.getHellTime() > 0) {
                    // 地狱坐标
                    loc = new int[3];
                    loc[0] = 32701;
                    loc[1] = 32777;
                    loc[2] = 666;

                } else {
                    // 返回村庄
                    loc = GetbackTable.GetBack_Location(pc, true);
                }

                pc.stopPcDeleteTimer();

                pc.removeAllKnownObjects();
                pc.broadcastPacketAll(new S_RemoveObject(pc));

                pc.setCurrentHp(pc.getLevel());
                pc.set_food(40);
                pc.setStatus(0);
                World.get().moveVisibleObject(pc, loc[2]);

                pc.setX(loc[0]);
                pc.setY(loc[1]);
                pc.setMap((short) loc[2]);

                // 设置副本编号
                //pc.set_showId(-1);//注掉这一句 修正黑暗妖精50经任务BUG hjx1000
                pc.sendPackets(new S_MapID(pc, pc.getMap().getBaseMapId(), pc.getMap()
                        .isUnderwater()));

                pc.broadcastPacketAll(new S_OtherCharPacks(pc));

                pc.sendPackets(new S_OwnCharPack(pc));
                pc.sendPackets(new S_CharVisualUpdate(pc));

                pc.startHpRegeneration();
                pc.startMpRegeneration();

                pc.sendPackets(new S_Weather(World.get().getWeather()));

                // 闪避率更新 修正 thatmystyle (UID: 3602)
                pc.sendPackets(new S_PacketBoxIcon1(true, pc.get_dodge()));

                if (pc.getHellTime() > 0) {
                    pc.beginHell(false);
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
