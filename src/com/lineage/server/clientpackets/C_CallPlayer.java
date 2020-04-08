package com.lineage.server.clientpackets;

import static com.lineage.server.model.skill.L1SkillId.*;

import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.concurrent.RejectedExecutionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.command.executor.L1AllBuff;
import com.lineage.server.datatables.lock.CharBuffReading;
import com.lineage.server.datatables.lock.IpReading;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Party;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillMode;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.World;

/**
 * 要求线上人物列表(GM管理选单)
 * 
 * @author daien
 * 
 */
public class C_CallPlayer extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_CallPlayer.class);

    /*
     * public C_CallPlayer() { }
     * 
     * public C_CallPlayer(final byte[] abyte0, final ClientExecutor client) {
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

            if (!pc.isGm()) {
                return;
            }

            final String arg = this.readS();

            final StringTokenizer stringtokenizer = new StringTokenizer(arg,
                    ":");
            // int mode = 0;
            int mode = pc.getTempID();
            try {
                // mode = Integer.parseInt(stringtokenizer.nextToken());
                int mapid = Integer.parseInt(stringtokenizer.nextToken());
                final String name = stringtokenizer.nextToken();

                // 长度不为0
                if (name.isEmpty()) {
                    return;
                }
                // 取回人物资料
                final L1PcInstance target = World.get().getPlayer(name);

                if (target == null) {
                    // 73 \f1%0%d 不在线上。
                    pc.sendPackets(new S_ServerMessage(73, name));
                    return;
                }

                switch (mode) {
                    case 0: // 删除已存人物保留技能
                        target.clearAllSkill();
                        CharBuffReading.get().deleteBuff(target);
                        stopSkill(target);
                        pc.sendPackets(new S_ServerMessage(166, target
                                .getName() + " Buff清除!"));
                        break;

                    case 1:// 移动座标
                           // 取回座标资料
                        final L1Location loc = L1Location.randomLocation(
                                target.getLocation(), 1, 2, false);

                        // 设置副本编号
                        pc.set_showId(target.get_showId());

                        L1Teleport.teleport(pc, loc.getX(), loc.getY(),
                                target.getMapId(), pc.getHeading(), false);
                        pc.sendPackets(new S_ServerMessage(166, "移动座标至指定人物身边: "
                                + name));
                        break;

                    case 2:// 召回指定人物
                           // 取回座标资料
                        final L1Location gmloc = L1Location.randomLocation(
                                pc.getLocation(), 1, 2, false);
                        L1Teleport.teleport(target, gmloc.getX(), gmloc.getY(),
                                pc.getMapId(), target.getHeading(), false);
                        pc.sendPackets(new S_ServerMessage(166, "召回指定人物: "
                                + name));
                        break;

                    case 3:// 召回指定队伍
                        final L1Party party = target.getParty();
                        if (party != null) {
                            final int x = pc.getX();
                            final int y = pc.getY() + 2;
                            final short map = pc.getMapId();

                            final HashMap<Integer, L1PcInstance> pcs = new HashMap<Integer, L1PcInstance>();
                            pcs.putAll(party.partyUsers());
                            // final ConcurrentHashMap<Integer, L1PcInstance>
                            // pcs = party.partyUsers();

                            if (pcs.isEmpty()) {
                                return;
                            }
                            if (pcs.size() <= 0) {
                                return;
                            }

                            for (final L1PcInstance pc2 : pcs.values()) {
                                try {
                                    L1Teleport
                                            .teleport(pc2, x, y, map, 5, true);
                                    pc2.sendPackets(new S_SystemMessage(
                                            "管理员召唤!"));

                                } catch (final Exception e) {
                                }
                            }
                            pcs.clear();
                        }
                        break;

                    case 4:// 全技能
                        L1AllBuff.startPc(target);
                        break;

                    case 5:// 踢除下线
                        pc.sendPackets(new S_SystemMessage(target.getName()
                                + " 踢除下线。"));
                        target.getNetConnection().kick();
                        break;

                    case 6:// 帐号封锁
                        final StringBuilder ipaddr = target.getNetConnection()
                                .getIp();
                        final StringBuilder macaddr = target.getNetConnection()
                                .getMac();
                        if (ipaddr != null) {
                            // 加入IP封锁
                            IpReading.get().add(ipaddr.toString(),
                                    "GM命令:L1PowerKick 封锁");
                        }
                        if (macaddr != null) {
                            // 加入MAC封锁
                            IpReading.get().add(macaddr.toString(),
                                    "GM命令:L1PowerKick 封锁");
                        }
                        pc.sendPackets(new S_SystemMessage(target.getName()
                                + " 封锁IP/MAC。"));
                        target.getNetConnection().kick();
                        break;

                    case 7:// 帐号封锁
                        IpReading.get().add(target.getAccountName(),
                                "GM命令:L1AccountBanKick 封锁帐号");
                        pc.sendPackets(new S_SystemMessage(target.getName()
                                + " 帐号封锁。"));
                        target.getNetConnection().kick();
                        break;

                    case 8:// 杀死指定人物
                        target.setCurrentHp(0);
                        target.death(null);
                        pc.sendPackets(new S_SystemMessage(target.getName()
                                + " 人物死亡。"));
                        break;
                }

            } catch (final RejectedExecutionException e) {
                final String name = arg;

                // 长度不为0
                if (name.isEmpty()) {
                    return;
                }
                // 取回人物资料
                final L1PcInstance target = World.get().getPlayer(name);

                if (target == null) {
                    // 73 \f1%0%d 不在线上。
                    pc.sendPackets(new S_ServerMessage(73, name));
                    return;
                }
                // 取回座标资料
                final L1Location loc = L1Location.randomLocation(
                        target.getLocation(), 1, 2, false);
                L1Teleport.teleport(pc, loc.getX(), loc.getY(),
                        target.getMapId(), pc.getHeading(), false);
                pc.sendPackets(new S_ServerMessage(166, "移动座标至指定人物身边: " + name));
            }

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    /**
     * 技能解除
     * 
     * @param pc
     */
    private void stopSkill(final L1PcInstance pc) {
        // 技能解除
        for (int skillNum = SKILLS_BEGIN; skillNum <= SKILLS_END; skillNum++) {
            if (L1SkillMode.get().isNotCancelable(skillNum) && !pc.isDead()) {
                continue;
            }
            pc.removeSkillEffect(skillNum);
        }

        pc.curePoison();
        pc.cureParalaysis();
        for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_END; skillNum++) {
            pc.removeSkillEffect(skillNum);
        }

        // 料理解除
        for (int skillNum = COOKING_BEGIN; skillNum <= COOKING_END; skillNum++) {
            if (L1SkillMode.get().isNotCancelable(skillNum)) {
                continue;
            }
            pc.removeSkillEffect(skillNum);
        }

        if (pc.getHasteItemEquipped() > 0) {
            pc.setMoveSpeed(0);
            pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));
        }

        pc.removeSkillEffect(STATUS_FREEZE);
        pc.sendPacketsAll(new S_CharVisualUpdate(pc));

    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
