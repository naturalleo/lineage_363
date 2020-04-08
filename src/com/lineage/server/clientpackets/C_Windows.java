package com.lineage.server.clientpackets;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.L1DragonSlayer;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBoxLoc;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.World;

/**
 * 视窗失焦
 * 
 * @author dexc
 * 
 */
public class C_Windows extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_Windows.class);

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            read(decrypt);

            L1PcInstance pc = client.getActiveChar();
            int type = readC();

            switch (type) {
                case 13: // 窗口失焦(每次切换都会发)
                    break;
                case 32: // 打开仓库
                    break;
                case 34: // 保存记忆坐标(按下保存按钮或直接关闭窗口)
                    break;
                case 39: // 修改记忆坐标(调整顺序、更改颜色等等)
                    break;
                case 0x00:
                    int objid = readD();
                    L1Object obj = World.get().findObject(objid);
                    if (obj instanceof L1PcInstance) {
                        L1PcInstance tgpc = (L1PcInstance) obj;
                        _log.warn("玩家:" + pc.getName() + " 申诉:(" + objid + ")"
                                + tgpc.getName());
                    } else {
                        _log.warn("玩家:" + pc.getName() + " 申诉:NPC(" + objid
                                + ")");
                    }
                    break;

                case 0x0b:
                    String name = readS();
                    int mapid = readH();
                    int x = readH();
                    int y = readH();
                    int zone = readD();
                    L1PcInstance target = World.get().getPlayer(name);
                    if (target != null) {
                        target.sendPackets(new S_PacketBoxLoc(pc.getName(),
                                mapid, x, y, zone));
                        pc.sendPackets(new S_ServerMessage(1783, name));// 已发送座标位置给%0。

                    } else {
                        pc.sendPackets(new S_ServerMessage(1782));// 无法找到该角色或角色不在线上。
                    }
                    break;
                case 0x2c: // 清空杀怪数量
                    pc.setKillMonstersNumber(0);
                    break;
                case 0x06: //新龙副本
                    @SuppressWarnings("unused")
                    final int objectId = this.readD();
                    final int gate = this.readD();
                    final int dragonGate[] = { 81273, // 龙之门扉 (安塔瑞斯副本)
                            81274, // 龙之门扉 (法利昂副本)
                            81275, // 龙之门扉 (林德拜尔副本)
                            81276
                    // 龙之门扉 (巴拉卡斯副本)
                    };
                    if ((gate >= 0) && (gate <= 3)) {
                        final Calendar nowTime = Calendar.getInstance();
                        if ((nowTime.get(Calendar.HOUR_OF_DAY) >= 8)
                                && (nowTime.get(Calendar.HOUR_OF_DAY) < 12)) {
                            pc.sendPackets(new S_ServerMessage(1643)); // 每日上午 8 点到 12
                                                                       // 点为止，暂时无法使用龙之钥匙。
                        } else {
                            boolean limit = true;
                            switch (gate) {
                                case 0:
                                    for (int i = 0; i < 6; i++) {
                                        if (!L1DragonSlayer.getInstance()
                                                .getPortalNumber()[i]) {
                                            limit = false;
                                        }
                                    }
                                    break;
                                case 1:
                                    for (int i = 6; i < 12; i++) {
                                        if (!L1DragonSlayer.getInstance()
                                                .getPortalNumber()[i]) {
                                            limit = false;
                                        }
                                    }
                                    break;
                            }
                            if (!limit) { // 未达上限可开设龙门
                                if (!pc.getInventory().consumeItem(47010, 1)) {
                                    pc.sendPackets(new S_ServerMessage(1567)); // 需要龙之钥匙。
                                    return;
                                }
                                L1SpawnUtil.spawn(pc, dragonGate[gate], 0,
                                        120 * 60); // 开启 2 小时
                            }
                        }
                    }
                	break;
                default:
                    System.out.println("C_Windows - 其他: " + type);
                    break;
            }

            if (pc != null) {
                // 额外
                if (pc.get_mazu_time() != 0) {
                    if (pc.is_mazu()) {
                        final Calendar cal = Calendar.getInstance();
                        long h_time = cal.getTimeInMillis() / 1000;// 换算为秒
                        if (h_time - pc.get_mazu_time() >= 2400) {// 2400秒 =
                                                                  // 40分钟
                            pc.set_mazu_time(0);
                            pc.set_mazu(false);
                        }
                    }
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
