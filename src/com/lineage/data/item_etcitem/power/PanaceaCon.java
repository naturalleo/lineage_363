package com.lineage.data.item_etcitem.power;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_OwnCharStatus2;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 万能药(体质)40034<br>
 * 
 * @author dexc
 * 
 */
public class PanaceaCon extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(PanaceaCon.class);

    /**
	 *
	 */
    private PanaceaCon() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new PanaceaCon();
    }

    /**
     * 道具物件执行
     * 
     * @param data
     *            参数
     * @param pc
     *            执行者
     * @param item
     *            物件
     */
    @Override
    public void execute(final int[] data, final L1PcInstance pc,
            final L1ItemInstance item) {
        final int itemId = item.getItemId();

        boolean save = false;
        boolean me = false;
        boolean po = false;

        switch (itemId) {
            case (40033):
                if (pc.getBaseStr() < ConfigAlt.POWERMEDICINE) {
                    if (pc.getElixirStats() < ConfigAlt.MEDICINE) {
                        pc.addBaseStr((byte) 1); // STR+1
                        pc.setElixirStats(pc.getElixirStats() + 1);
                        pc.getInventory().removeItem(item, 1);
                        pc.sendPackets(new S_OwnCharStatus2(pc));
                        save = true;
                    } else {
                        me = true;
                    }

                } else {
                    po = true;
                }
                break;

            case (40034):
                if (pc.getBaseCon() < ConfigAlt.POWERMEDICINE) {
                    if (pc.getElixirStats() < ConfigAlt.MEDICINE) {
                        pc.addBaseCon((byte) 1); // CON+1
                        pc.setElixirStats(pc.getElixirStats() + 1);
                        pc.getInventory().removeItem(item, 1);
                        pc.sendPackets(new S_OwnCharStatus2(pc));
                        save = true;
                    } else {
                        me = true;
                    }

                } else {
                    po = true;
                }
                break;

            case (40035):
                if (pc.getBaseDex() < ConfigAlt.POWERMEDICINE) {
                    if (pc.getElixirStats() < ConfigAlt.MEDICINE) {
                        pc.addBaseDex((byte) 1); // DEX+1
                        pc.resetBaseAc();
                        pc.setElixirStats(pc.getElixirStats() + 1);
                        pc.getInventory().removeItem(item, 1);
                        pc.sendPackets(new S_OwnCharStatus2(pc));
                        save = true;
                    } else {
                        me = true;
                    }

                } else {
                    po = true;
                }
                break;

            case (40036):
                if (pc.getBaseInt() < ConfigAlt.POWERMEDICINE) {
                    if (pc.getElixirStats() < ConfigAlt.MEDICINE) {
                        pc.addBaseInt((byte) 1); // INT+1
                        pc.setElixirStats(pc.getElixirStats() + 1);
                        pc.getInventory().removeItem(item, 1);
                        pc.sendPackets(new S_OwnCharStatus2(pc));
                        save = true;
                    } else {
                        me = true;
                    }

                } else {
                    po = true;
                }
                break;

            case (40037):
                if (pc.getBaseWis() < ConfigAlt.POWERMEDICINE) {
                    if (pc.getElixirStats() < ConfigAlt.MEDICINE) {
                        pc.addBaseWis((byte) 1); // WIS+1
                        pc.resetBaseMr();
                        pc.setElixirStats(pc.getElixirStats() + 1);
                        pc.getInventory().removeItem(item, 1);
                        pc.sendPackets(new S_OwnCharStatus2(pc));
                        save = true;
                    } else {
                        me = true;
                    }

                } else {
                    po = true;
                }
                break;

            case (40038):
                if (pc.getBaseCha() < ConfigAlt.POWERMEDICINE) {
                    if (pc.getElixirStats() < ConfigAlt.MEDICINE) {
                        pc.addBaseCha((byte) 1); // CHA+1
                        pc.setElixirStats(pc.getElixirStats() + 1);
                        pc.getInventory().removeItem(item, 1);
                        pc.sendPackets(new S_OwnCharStatus2(pc));
                        save = true;
                    } else {
                        me = true;
                    }

                } else {
                    po = true;
                }
                break;
        }

        if (po) {
            // \f1属性最大值只能到35。
            pc.sendPackets(new S_ServerMessage(481));
        }

        if (me) {
            // 79：\f1没有任何事情发生。
            pc.sendPackets(new S_ServerMessage(79));
        }

        if (save) {
            try {
                pc.save();
            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
