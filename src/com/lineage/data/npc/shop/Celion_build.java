package com.lineage.data.npc.shop;

import java.util.Random;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 席琳武器打造<BR>
 * XXXX
 * 
 * @author hjx1000
 * 
 */
public class Celion_build extends NpcExecutor {

    /**
	 *
	 */
    private Celion_build() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Celion_build();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "xilin"));
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
    	final Random random = new Random();
        if (cmd.equalsIgnoreCase("xilin1")) { //席琳双刀
        	//final int rnd = random.nextInt(100) + 1;
            if (!pc.getInventory().checkEnchantItem(84, 0, 1)
                    || !pc.getInventory().checkItem(40308, 10000000)
                    || !pc.getInventory().checkItem(55107, 1)) {
                pc.sendPackets(new S_ServerMessage(337, "打造武器所需的物品"));
                // 升级物品不足
            } else if (/*rnd > 70*/true) {// 升级成功
                pc.getInventory().consumeEnchantItem(84, 0, 1);
                pc.getInventory().consumeItem(40308, 10000000);
                pc.getInventory().consumeItem(55107, 1);
                final L1ItemInstance item = pc.getInventory().storeItem(
                        316, 1);
                pc.sendPackets(new S_ServerMessage(143,
                		npc.getNpcTemplate().get_name(),item.getItem().getNameId()));
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "xilincg"));
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4541));
            } else {// 升级失败
                pc.getInventory().consumeEnchantItem(84, 0, 1);
                pc.getInventory().consumeItem(40308, 10000000);
                pc.getInventory().consumeItem(55107, 1);
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "xilinsb"));
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4581));
            }
        } else if (cmd.equalsIgnoreCase("xilin2")) {//席琳双手剑
        	//final int rnd = random.nextInt(100) + 1;
            if (!pc.getInventory().checkEnchantItem(59, 0, 1)
                    || !pc.getInventory().checkItem(40308, 10000000)
                    || !pc.getInventory().checkItem(55107, 1)) {
                pc.sendPackets(new S_ServerMessage(337, "打造武器所需的物品"));
                // 升级物品不足
            } else if (/*rnd > 70*/true) {// 升级成功
                pc.getInventory().consumeEnchantItem(59, 0, 1);
                pc.getInventory().consumeItem(40308, 10000000);
                pc.getInventory().consumeItem(55107, 1);
                final L1ItemInstance item = pc.getInventory().storeItem(
                        313, 1);
                pc.sendPackets(new S_ServerMessage(143,
                		npc.getNpcTemplate().get_name(),item.getItem().getNameId()));
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "xilincg"));
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4541));
            } else {// 升级失败
                pc.getInventory().consumeEnchantItem(59, 0, 1);
                pc.getInventory().consumeItem(40308, 10000000);
                pc.getInventory().consumeItem(55107, 1);
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "xilinsb"));
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4581));
            }
        } else if (cmd.equalsIgnoreCase("xilin3")) {//席琳弓
        	//final int rnd = random.nextInt(100) + 1;
            if (!pc.getInventory().checkEnchantItem(189, 0, 1)
                    || !pc.getInventory().checkItem(40308, 10000000)
                    || !pc.getInventory().checkItem(55107, 1)) {
                pc.sendPackets(new S_ServerMessage(337, "打造武器所需的物品"));
                // 升级物品不足
            } else if (/*rnd > 70*/true) {// 升级成功
                pc.getInventory().consumeEnchantItem(189, 0, 1);
                pc.getInventory().consumeItem(40308, 10000000);
                pc.getInventory().consumeItem(55107, 1);
                final L1ItemInstance item = pc.getInventory().storeItem(
                        314, 1);
                pc.sendPackets(new S_ServerMessage(143,
                		npc.getNpcTemplate().get_name(),item.getItem().getNameId()));
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "xilincg"));
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4541));
            } else {// 升级失败
                pc.getInventory().consumeEnchantItem(189, 0, 1);
                pc.getInventory().consumeItem(40308, 10000000);
                pc.getInventory().consumeItem(55107, 1);
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "xilinsb"));
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4581));
            }
        } else if (cmd.equalsIgnoreCase("xilin4")) {//席琳锁链剑
        	//final int rnd = random.nextInt(100) + 1;
            if (!pc.getInventory().checkEnchantItem(310, 0, 1)
                    || !pc.getInventory().checkItem(40308, 10000000)
                    || !pc.getInventory().checkItem(55107, 1)) {
                pc.sendPackets(new S_ServerMessage(337, "打造武器所需的物品"));
                // 升级物品不足
            } else if (/*rnd > 70*/true) {// 升级成功
                pc.getInventory().consumeEnchantItem(310, 0, 1);
                pc.getInventory().consumeItem(40308, 10000000);
                pc.getInventory().consumeItem(55107, 1);
                final L1ItemInstance item = pc.getInventory().storeItem(
                        317, 1);
                pc.sendPackets(new S_ServerMessage(143,
                		npc.getNpcTemplate().get_name(),item.getItem().getNameId()));
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "xilincg"));
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4541));
            } else {// 升级失败
                pc.getInventory().consumeEnchantItem(310, 0, 1);
                pc.getInventory().consumeItem(40308, 10000000);
                pc.getInventory().consumeItem(55107, 1);
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "xilinsb"));
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4581));
            }
        } else if (cmd.equalsIgnoreCase("xilin5")) {//席琳短剑
        	//final int rnd = random.nextInt(100) + 1;
            if (!pc.getInventory().checkEnchantItem(9, 0, 1)
                    || !pc.getInventory().checkItem(40308, 10000000)
                    || !pc.getInventory().checkItem(55107, 1)) {
                pc.sendPackets(new S_ServerMessage(337, "打造武器所需的物品"));
                // 升级物品不足
            } else if (/*rnd > 70*/true) {// 升级成功
                pc.getInventory().consumeEnchantItem(9, 0, 1);
                pc.getInventory().consumeItem(40308, 10000000);
                pc.getInventory().consumeItem(55107, 1);
                final L1ItemInstance item = pc.getInventory().storeItem(
                        312, 1);
                pc.sendPackets(new S_ServerMessage(143,
                		npc.getNpcTemplate().get_name(),item.getItem().getNameId()));
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "xilincg"));
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4541));
            } else {// 升级失败
                pc.getInventory().consumeEnchantItem(9, 0, 1);
                pc.getInventory().consumeItem(40308, 10000000);
                pc.getInventory().consumeItem(55107, 1);
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "xilinsb"));
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4581));
            }
        } else if (cmd.equalsIgnoreCase("xilin6")) {//席琳法杖
        	//final int rnd = random.nextInt(100) + 1;
            if (!pc.getInventory().checkEnchantItem(121, 0, 1)
                    || !pc.getInventory().checkItem(40308, 10000000)
                    || !pc.getInventory().checkItem(55107, 1)) {
                pc.sendPackets(new S_ServerMessage(337, "打造武器所需的物品"));
                // 升级物品不足
            } else if (/*rnd > 70*/true) {// 升级成功
                pc.getInventory().consumeEnchantItem(121, 0, 1);
                pc.getInventory().consumeItem(40308, 10000000);
                pc.getInventory().consumeItem(55107, 1);
                final L1ItemInstance item = pc.getInventory().storeItem(
                        315, 1);
                pc.sendPackets(new S_ServerMessage(143,
                		npc.getNpcTemplate().get_name(),item.getItem().getNameId()));
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "xilincg"));
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4541));
            } else {// 升级失败
                pc.getInventory().consumeEnchantItem(121, 0, 1);
                pc.getInventory().consumeItem(40308, 10000000);
                pc.getInventory().consumeItem(55107, 1);
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "xilinsb"));
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4581));
            }
        } else if (cmd.equalsIgnoreCase("xilin7")) {//席琳奇古兽
        	//final int rnd = random.nextInt(100) + 1;
            if (!pc.getInventory().checkEnchantItem(311, 0, 1)
                    || !pc.getInventory().checkItem(40308, 10000000)
                    || !pc.getInventory().checkItem(55107, 1)) {
                pc.sendPackets(new S_ServerMessage(337, "打造武器所需的物品"));
                // 升级物品不足
            } else if (/*rnd > 70*/true) {// 升级成功
                pc.getInventory().consumeEnchantItem(311, 0, 1);
                pc.getInventory().consumeItem(40308, 10000000);
                pc.getInventory().consumeItem(55107, 1);
                final L1ItemInstance item = pc.getInventory().storeItem(
                        318, 1);
                pc.sendPackets(new S_ServerMessage(143,
                		npc.getNpcTemplate().get_name(),item.getItem().getNameId()));
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "xilincg"));
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4541));
            } else {// 升级失败
                pc.getInventory().consumeEnchantItem(311, 0, 1);
                pc.getInventory().consumeItem(40308, 10000000);
                pc.getInventory().consumeItem(55107, 1);
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "xilinsb"));
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4581));
            }
        }
    }
}
