package com.lineage.data.npc.mob;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Party;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBoxGree;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.CheckUtil;

/**
 * NPC 死亡传送玩家<BR>
 * 设置范例:<BR>
 * LOCX LOCY MAPID 是否传送队员<BR>
 * classname: mob.MobTeleport 设置范例: mob.MobTeleport 33425 32827 4 flase<BR>
 * 
 * @author dexc
 * 
 */
public class MobTeleport extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(MobTeleport.class);

    private MobTeleport() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new MobTeleport();
    }

    @Override
    public int type() {
        return 8;
    }

    @Override
    public L1PcInstance death(final L1Character lastAttacker,
            final L1NpcInstance npc) {
        try {
            // 判断主要攻击者
            final L1PcInstance pc = CheckUtil.checkAtkPc(lastAttacker);

            if (pc != null) {
                if (_locx != 0 && _locy != 0) {
                    final M_teleport teleport = new M_teleport(pc);
                    teleport.stsrt_cmd();
                }
            }
            return pc;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    private int _locx = 0;

    private int _locy = 0;

    private int _mapid = 0;

    private boolean _party = false;

    @Override
    public void set_set(String[] set) {
        try {
            _locx = Integer.parseInt(set[1]);

        } catch (final Exception e) {
        }
        try {
            _locy = Integer.parseInt(set[2]);

        } catch (final Exception e) {
        }
        try {
            _mapid = Integer.parseInt(set[3]);

        } catch (final Exception e) {
        }
        try {
            _party = Boolean.parseBoolean(set[4]);

        } catch (final Exception e) {
        }
    }

    private class M_teleport implements Runnable {

        private final L1PcInstance _pc;

        private M_teleport(final L1PcInstance pc) {
            _pc = pc;
        }

        private void stsrt_cmd() throws IOException {
            GeneralThreadPool.get().execute(this);
        }

        @Override
        public void run() {
            try {
                _pc.sendPackets(new S_PacketBoxGree(0x01));
                _pc.sendPackets(new S_PacketBoxGree("$ 5秒后将会被传送!"));
                Thread.sleep(1000);
                _pc.sendPackets(new S_PacketBoxGree("$ 4秒后将会被传送!"));
                Thread.sleep(1000);
                _pc.sendPackets(new S_PacketBoxGree("$ 3秒后将会被传送!"));
                Thread.sleep(1000);
                _pc.sendPackets(new S_PacketBoxGree("$ 2秒后将会被传送!"));
                Thread.sleep(1000);
                _pc.sendPackets(new S_PacketBoxGree("$ 1秒后将会被传送!"));
                Thread.sleep(1000);

                _pc.sendPackets(new S_PacketBoxGree("$ "));
                // 自身的传送
                L1Teleport.teleport(_pc, _locx, _locy, (short) _mapid, 5, true);
                if (_party) {
                    final L1Party party = _pc.getParty();
                    if (party != null) {
                        // 队伍成员
                        for (L1PcInstance otherPc : party.partyUsers().values()) {
                            // 不是队长
                            if (otherPc.getId() != party.getLeaderID()) {
                                // 相同地图
                                if (_pc.getMapId() == otherPc.getMapId()) {
                                    // 传送成员
                                    L1Teleport.teleport(otherPc, _locx, _locy,
                                            (short) _mapid, 5, true);
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
