package com.lineage.server.command;

import java.util.TreeMap;

import com.lineage.commons.system.LanSecurityManager;
import com.lineage.server.datatables.lock.IpReading;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

/**
 * GM管理选单<BR>
 * 
 * @author dexc
 * 
 */
public class GmHtml {

    // 执行GM
    private final L1PcInstance _pc;

    // 执行模式 0:PC清单 1:DE清单 2:封锁清单
    private final int _mode;

    // 线上玩家数量
    private final int _users;

    // 暂存清单(PC)
    private final TreeMap<Integer, L1PcInstance> _allPcList;

    // 暂存清单(封锁)
    private final TreeMap<Integer, String> _banList;

    // 暂存清单(封锁)
    private TreeMap<Integer, String> _banTmpList;

    /**
     * GM管理选单
     * 
     * @param pc
     * @param mode
     *            0:PC清单 1:DE清单 2:封锁清单
     * 
     */
    public GmHtml(final L1PcInstance pc, int mode) {
        _pc = pc;
        _pc.get_other().set_page(0);
        // GM管理状态
        _pc.get_other().set_gmHtml(this);
        _users = World.get().getAllPlayers().size();

        _allPcList = new TreeMap<Integer, L1PcInstance>();
        _banList = new TreeMap<Integer, String>();

        _mode = mode;

        // System.out.println("_mode: " + _mode);

        // 加载MAP(PC)
        int keyPc = 0;
        for (L1PcInstance tgpc : World.get().getAllPlayers()) {
            _allPcList.put(new Integer(keyPc), tgpc);
            keyPc++;
        }

        // 加载M封锁
        int keyBan = 0;
        for (String ban : LanSecurityManager.BANIPMAP.keySet()) {
            _banList.put(new Integer(keyBan), ban);
            keyBan++;
        }
        for (String ban : LanSecurityManager.BANNAMEMAP.keySet()) {
            _banList.put(new Integer(keyBan), ban);
            keyBan++;
        }
        // 加载暂时封锁
        int keyTmpBan = 0;
        for (final String ban : LanSecurityManager.BANIPPACK.keySet()) {
            _banTmpList.put(new Integer(keyTmpBan), ban);
            keyTmpBan++;
        }
    }

    /**
     * 展示页面
     */
    public void show() {
        showPage(0);
    }

    /**
     * 执行指令判断
     * 
     * @param cmd
     */
    public void action(final String cmd) {
        // System.out.println("cmd: " + cmd);
        if (cmd.equals("up")) {// 上一页
            final int page = _pc.get_other().get_page() - 1;
            this.showPage(page);

        } else if (cmd.equals("dn")) {// 下一页
            final int page = _pc.get_other().get_page() + 1;
            this.showPage(page);

        } else if (cmd.startsWith("K")) {// 踢下线
            final int xcmd = Integer.parseInt(cmd.substring(1));
            this.startCmd(1, xcmd);

        } else if (cmd.startsWith("D")) {// 封号
            final int xcmd = Integer.parseInt(cmd.substring(1));
            this.startCmd(2, xcmd);

        } else if (cmd.startsWith("M")) {// 封MAC
            final int xcmd = Integer.parseInt(cmd.substring(1));
            this.startCmd(3, xcmd);

        } else if (cmd.startsWith("T")) {// 移动
            final int xcmd = Integer.parseInt(cmd.substring(1));
            this.startCmd(4, xcmd);
        }
    }

    /**
     * 执行命令
     * 
     * @param mode
     *            模式
     * @param cmd
     *            命令
     */
    private void startCmd(final int mode, final int cmd) {
        int page = _pc.get_other().get_page();
        if (page > 0) {
            page *= 10;
        }

        int xcmd = page + cmd;

        if (_mode == 0) {// 展示PC
            switch (mode) {
                case 1:// 踢下线
                    L1PcInstance target_pc1 = _allPcList.get(xcmd);
                    _pc.sendPackets(new S_ServerMessage(target_pc1.getName()
                            + " 踢除下线。"));

                    L1PcInstance target_pcX1 = World.get().getPlayer(
                            target_pc1.getName());
                    if (target_pcX1 != null) {// 线上
                        target_pcX1.getNetConnection().kick();
                    }
                    break;

                case 2:// 封号
                    L1PcInstance target_pc2 = _allPcList.get(xcmd);
                    IpReading.get().add(target_pc2.getAccountName(),
                            "GM命令:L1AccountBanKick 封锁帐号");
                    _pc.sendPackets(new S_ServerMessage(target_pc2.getName()
                            + " 帐号封锁。"));

                    L1PcInstance target_pcX2 = World.get().getPlayer(
                            target_pc2.getName());
                    if (target_pcX2 != null) {// 线上
                        target_pcX2.getNetConnection().kick();
                    }
                    break;

                case 3:// 封MAC
                    L1PcInstance target_pc3 = _allPcList.get(xcmd);
                    L1PcInstance target_pcX3 = World.get().getPlayer(
                            target_pc3.getName());

                    if (target_pcX3 != null) {// 线上
                        final StringBuilder macaddr = target_pcX3
                                .getNetConnection().getMac();
                        if (macaddr != null) {
                            // 加入MAC封锁
                            IpReading.get().add(macaddr.toString(),
                                    "GM命令:L1PowerKick 封锁");
                            _pc.sendPackets(new S_ServerMessage(target_pcX3
                                    .getName() + " 封锁MAC。"));
                        }
                        target_pcX3.getNetConnection().kick();

                    } else {// 离线
                        IpReading.get().add(target_pc3.getAccountName(),
                                "GM命令:L1AccountBanKick 封锁帐号");
                        _pc.sendPackets(new S_ServerMessage(target_pc3
                                .getName() + " (离线)帐号封锁。"));
                    }
                    break;

                case 4:// 移动
                    L1PcInstance target_pc4 = _allPcList.get(xcmd);
                    L1PcInstance target_pcX4 = World.get().getPlayer(
                            target_pc4.getName());

                    if (target_pcX4 != null) {// 线上
                        // 取回座标资料
                        final L1Location loc = L1Location.randomLocation(
                                target_pcX4.getLocation(), 1, 2, false);
                        L1Teleport
                                .teleport(_pc, loc.getX(), loc.getY(),
                                        target_pcX4.getMapId(),
                                        _pc.getHeading(), false);
                        _pc.sendPackets(new S_ServerMessage("移动座标至指定人物身边: "
                                + target_pcX4.getName()));

                    } else {
                        // 73 \f1%0%d 不在线上。
                        _pc.sendPackets(new S_ServerMessage(73, target_pc4
                                .getName()));
                    }
                    break;
            }

        } else if (_mode == 2) {// 展示封锁
            switch (mode) {
                case 1:// 解除封锁
                    String banInfo = _banList.get(xcmd);
                    // System.out.println("banInfo: " + banInfo);
                    IpReading.get().remove(banInfo);
                    _pc.sendPackets(new S_ServerMessage("解除封锁: " + banInfo));
                    break;
            }
        } else if (_mode == 3) {// 展示暂时封锁
            switch (mode) {
                case 1:// 解除封锁
                    String banInfo = _banTmpList.get(xcmd);
                    LanSecurityManager.BANIPPACK.remove(banInfo);
                    _pc.sendPackets(new S_ServerMessage("解除暂时封锁: " + banInfo));
                    break;
            }
        }
    }

    /**
     * 显示活动页面
     * 
     * @param page
     *            页面
     */
    private void showPage(int page) {
        // 全部页面数量
        int allpage = 0;

        final StringBuilder stringBuilder = new StringBuilder();

        if (_mode == 0) {// 展示PC
            // 全部页面数量
            allpage = _allPcList.size() / 10;
            if ((page > allpage) || (page < 0)) {
                page = 0;
            }

            if (_allPcList.size() % 10 != 0) {
                allpage += 1;
            }

            _pc.get_other().set_page(page);// 设置页面

            final int or = page * 10;
            // 写入线上人数
            stringBuilder.append(String.valueOf(_users) + ",");

            int i = 0;// 检查号
            for (final Integer key : _allPcList.keySet()) {
                if ((i >= or) && (i < (or + 10))) {
                    final L1PcInstance tgpc = _allPcList.get(key);
                    if (tgpc != null) {
                        stringBuilder.append(tgpc.getName() + "("
                                + tgpc.getAccountName() + ") PcLv:"
                                + tgpc.getLevel() + ",");
                    }
                }
                i++;
            }

        } else if (_mode == 2) {// 展示封锁
            // 全部页面数量
            allpage = _banList.size() / 10;
            if ((page > allpage) || (page < 0)) {
                page = 0;
            }

            if (_banList.size() % 10 != 0) {
                allpage += 1;
            }

            _pc.get_other().set_page(page);// 设置页面

            final int or = page * 10;

            int i = 0;// 检查号
            for (final Integer key : _banList.keySet()) {
                if ((i >= or) && (i < (or + 10))) {
                    final String banIp = _banList.get(key);
                    if (banIp != null) {
                        stringBuilder.append(banIp + ",");
                    }
                }
                i++;
            }

        } else if (_mode == 3) {// 展示暂时封锁
            // 全部页面数量
            allpage = _banTmpList.size() / 10;
            if ((page > allpage) || (page < 0)) {
                page = 0;
            }

            if (_banTmpList.size() % 10 != 0) {
                allpage += 1;
            }

            _pc.get_other().set_page(page);// 设置页面

            final int or = page * 10;

            int i = 0;// 检查号
            for (final Integer key : _banTmpList.keySet()) {
                if ((i >= or) && (i < (or + 10))) {
                    final String banIp = _banTmpList.get(key);
                    if (banIp != null) {
                        stringBuilder.append(banIp + ",");
                    }
                }
                i++;
            }
        }

        if (allpage >= (page + 1)) {
            final String[] clientStrAry = stringBuilder.toString().split(",");
            int length = clientStrAry.length - 1;
            if (_mode == 2) {
                _pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "y_GmE",
                        clientStrAry));

            } else if (_mode == 3) {
                _pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "y_GmE",
                        clientStrAry));

            } else {
                if (length > 0) {
                    _pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "y_Gm"
                            + length, clientStrAry));
                }
            }

        } else {
            // $6157 没有可以显示的项目
            _pc.sendPackets(new S_ServerMessage("没有可以显示的项目"));
        }
    }
}
