package com.lineage.server.clientpackets;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.ItemRestrictionsTable;
import com.lineage.server.datatables.PetTypeTable;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.serverpackets.S_ItemName;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1PetType;
import com.lineage.server.world.World;

/**
 * 要求给予物品
 * 
 * @author daien
 * 
 */
public class C_GiveItem extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_GiveItem.class);

    /*
     * public C_GiveItem() { }
     * 
     * public C_GiveItem(final byte[] abyte0, final ClientExecutor client) {
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

            if (pc.isPrivateShop()) { // 商店村模式
                return;
            }

            final int targetId = this.readD();
            final int x = this.readH();
            final int y = this.readH();

            final int itemObjId = this.readD();
            long count = this.readD();
            if (count > Integer.MAX_VALUE) {
                count = Integer.MAX_VALUE;
            }
            count = Math.max(0, count);
            final L1Object object = World.get().findObject(targetId);
            if ((object == null) || !(object instanceof L1NpcInstance)) {
                return;
            }

            final L1NpcInstance target = (L1NpcInstance) object;
            if (!this.isNpcItemReceivable(target.getNpcTemplate())) {
                return;
            }
            // 目标的背包
            final L1Inventory targetInv = target.getInventory();

            final L1Inventory inv = pc.getInventory();
            final L1ItemInstance item = inv.getItem(itemObjId);

            if (item == null) {
                return;
            }
            if (item.getCount() <= 0) {
                return;
            }

            if (item.isEquipped()) {
                // \f1你不能够将转移已经装备的物品。
                pc.sendPackets(new S_ServerMessage(141));
                return;
            }

            if (!item.getItem().isTradable()) {
                // \f1%0%d是不可转移的…
                pc.sendPackets(new S_ServerMessage(210, item.getItem()
                        .getNameId()));
                return;
            }
            if (item.getBless() >= 128) { // 封印装备
                // \f1%0%d是不可转移的…
                pc.sendPackets(new S_ServerMessage(210, item.getItem()
                        .getNameId()));
                return;
            }

            if (item.get_time() != null) {
                // \f1%0%d是不可转移的…
                pc.sendPackets(new S_ServerMessage(210, item.getItem()
                        .getNameId()));
                return;
            }
            if (ItemRestrictionsTable.RESTRICTIONS.contains(item.getItemId())) {
                // \f1%0%d是不可转移的…
                pc.sendPackets(new S_ServerMessage(210, item.getItem()
                        .getNameId()));
                return;
            }

            final int pcx = pc.getX();
            final int pcy = pc.getY();
            if ((Math.abs(pcx - x) >= 3) || (Math.abs(pcy - y) >= 3)) {
                // 142：\f1距离太远不能给东西。
                pc.sendPackets(new S_ServerMessage(142));
                return;
            }

            // 宠物(已经召唤出来)
            for (final Object petObject : pc.getPetList().values()) {
                if (petObject instanceof L1PetInstance) {
                    final L1PetInstance pet = (L1PetInstance) petObject;
                    if (item.getId() == pet.getItemObjId()) {
                        // \f1%0%d是不可转移的…
                        pc.sendPackets(new S_ServerMessage(210, item.getItem()
                                .getNameId()));
                        return;
                    }
                }
            }

            // 取回娃娃
            if (pc.getDoll(item.getId()) != null) {
                // 1,181：这个魔法娃娃目前正在使用中。
                pc.sendPackets(new S_ServerMessage(1181));
                return;
            }

            if (targetInv.checkAddItem(item, count) != L1Inventory.OK) {
                // 对方的负重太重，无法再给予。
                pc.sendPackets(new S_ServerMessage(942));
                return;
            }
            _log.info("人物:" + pc.getName() + "扔给NPC物品" + item.getNumberedName_to_String()
                    + " 物品OBJID:" + item.getId()); //扔物品给NPC也做记录 hjx1000
            // 给予的物件
            final L1ItemInstance getItem = inv
                    .tradeItem(item, count, targetInv);
            target.onGetItem(getItem);
            target.turnOnOffLight();
            pc.turnOnOffLight();

            // 宠物相关判断
            final L1PetType petType = PetTypeTable.getInstance().get(
                    target.getNpcTemplate().get_npcId());
            if ((petType == null) || target.isDead()) {
                return;
            }
            // 抓宠物的判断
            if (getItem.getItemId() == petType.getItemIdForTaming()) {
                this.tamePet(pc, target);
            }
            // 给予的对象 是宠物
            if (target instanceof L1PetInstance) {
                final L1PetInstance tgPet = (L1PetInstance) target;
                // pc.sendPackets(new S_PetInventory(tgPet, true));
                // 进化果实
                if ((getItem.getItemId() == 40070) && petType.canEvolve()) {
                    this.evolvePet(pc, tgPet);
                }
            }

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    private final static String receivableImpls[] = new String[] { "L1Npc", // NPC
            "L1Monster", // 怪物
            "L1Guardian", // 守护神
            "L1Teleporter", // 传送师
            "L1Guard"// 警卫
    };

    /**
     * 是否是可以给予物品的物件
     * 
     * @param npc
     * @return
     */
    private boolean isNpcItemReceivable(final L1Npc npc) {
        for (final String impl : receivableImpls) {
            if (npc.getImpl().equals(impl)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 抓宠物的判断
     * 
     * @param pc
     * @param target
     */
    private void tamePet(final L1PcInstance pc, final L1NpcInstance target) {
        if ((target instanceof L1PetInstance)
                || (target instanceof L1SummonInstance)) {
            return;
        }

        int petcost = 0;
        final Object[] petList = pc.getPetList().values().toArray();
        if (petList.length > 2) {
            // 489：你无法一次控制那么多宠物。
            pc.sendPackets(new S_ServerMessage(489));
            return;
        }
        for (final Object pet : petList) {
            int nowpetcost = ((L1NpcInstance) pet).getPetcost();
            petcost += nowpetcost;
        }

        int charisma = pc.getCha();
        if (pc.isCrown()) { // 君主
            charisma += 6;

        } else if (pc.isElf()) { // エルフ
            charisma += 12;

        } else if (pc.isWizard()) { // WIZ
            charisma += 6;

        } else if (pc.isDarkelf()) { // DE
            charisma += 6;

        } else if (pc.isDragonKnight()) { // ドラゴンナイト
            charisma += 6;

        } else if (pc.isIllusionist()) { // イリュージョニスト
            charisma += 6;
        }
        charisma -= petcost;

        if (charisma <= 0) {
            // 489：你无法一次控制那么多宠物。
            pc.sendPackets(new S_ServerMessage(489));
            return;
        }

        final L1PcInventory inv = pc.getInventory();
        if (inv.getSize() < 180) {
            if (this.isTamePet(target)) {
                final L1ItemInstance petamu = inv.storeItem(40314, 1); // 项圈
                if (petamu != null) {
                    new L1PetInstance(target, pc, petamu.getId());
                    pc.sendPackets(new S_ItemName(petamu));
                }

            } else {
                // 驯养失败。
                pc.sendPackets(new S_ServerMessage(324));
            }
        }
    }

    /**
     * 进化宠物的判断
     * 
     * @param pc
     * @param pet
     */
    private void evolvePet(final L1PcInstance pc, final L1PetInstance pet) {
        final L1PcInventory inv = pc.getInventory();
        final L1ItemInstance petamu = inv.getItem(pet.getItemObjId());
        pet.getInventory().consumeItem(40070, 1);// 进化果实移除

        int level = 30;
        /*
         * if (pet.getNpcId() == 71019) {// 淘气龙 level = 70; } if (pet.getNpcId()
         * == 71020) {// 顽皮龙 level = 60; }
         */
        if ((pet.getLevel() >= level) && // Lv30以上
                (pc == pet.getMaster()) && // 自分のペット
                (petamu != null)) {

            final L1ItemInstance highpetamu = inv.storeItem(40316, 1);// 项圈
            if (highpetamu != null) {
                pet.evolvePet(highpetamu.getId());
                pc.sendPackets(new S_ItemName(highpetamu));
                inv.removeItem(petamu, 1);
            }
        }
    }

    /**
     * 抓取宠物成功的判断
     * 
     * @param npc
     * @return
     */
    private boolean isTamePet(final L1NpcInstance npc) {
        boolean isSuccess = false;
        final int npcId = npc.getNpcTemplate().get_npcId();
        if (npcId == 45313) { // タイガー

            final Random random = new Random();

            // HPが1/3未满で1/16の确率
            if ((npc.getMaxHp() / 3 > npc.getCurrentHp())
                    && (random.nextInt(16) == 15)) {
                isSuccess = true;
            }

        } else {
            if (npc.getMaxHp() / 3 > npc.getCurrentHp()) {
                isSuccess = true;
            }
        }

        // タイガー、ラクーン、纪州犬の子犬
        if ((npcId == 45313) || (npcId == 45044) || (npcId == 45711)) {
            if (npc.isResurrect()) { // RES后はテイム不可
                isSuccess = false;
            }
        }

        return isSuccess;
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
