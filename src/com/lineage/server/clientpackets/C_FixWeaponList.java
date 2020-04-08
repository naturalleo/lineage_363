package com.lineage.server.clientpackets;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_FixWeaponList;
import com.lineage.server.serverpackets.S_SystemMessage;

/**
 * 要求维修物品清单
 * 
 * @author daien
 * 
 */
public class C_FixWeaponList extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_FixWeaponList.class);

    /*
     * public C_FixWeaponList() { }
     * 
     * public C_FixWeaponList(final byte[] abyte0, final ClientExecutor client)
     * { super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            // this.read(decrypt);

            final L1PcInstance pc = client.getActiveChar();

            // 暂时清单
            final List<L1ItemInstance> weaponList = new ArrayList<L1ItemInstance>();

            // 背包物件
            final List<L1ItemInstance> itemList = pc.getInventory().getItems();
            if (pc.getMapId() == 4 && pc.getX() > 33424 && pc.getX() < 33432
            		&& pc.getY() > 32822 && pc.getY() < 32832) {
                for (final L1ItemInstance item : itemList) {
                	final int use_type = item.getItem().getUseType();

                    // Find Weapon
                    switch (use_type) {
            		case 2:// 盔甲
            		case 18:// T恤
            		case 19:// 斗篷
            		case 20:// 手套
            		case 21:// 靴
            		case 22:// 頭盔
            		case 25:// 盾牌
            			if (item.getEnchantLevel() > 0 && 
                            	item.getItem().get_safeenchant() >= 0) {
            				weaponList.add(item);
            			}
            			break;
                    }
                }

                if (!weaponList.isEmpty()) {
                    pc.sendPackets(new S_FixWeaponList(weaponList));
                } else {
                	pc.sendPackets(new S_SystemMessage("你没有任何可以附魔的装备。"));//hjx1000
                }
            } else if (pc.getMapId() == 4 && pc.getX() > 33417 && pc.getX() < 33425
            		&& pc.getY() > 32822 && pc.getY() < 32832) {
                for (final L1ItemInstance item : itemList) {

                    // Find Weapon
                    switch (item.getItem().getType2()) {
                        case 1:
                            if (item.getEnchantLevel() > 0 && 
                            		item.getItem().get_safeenchant() >= 0) {
                                weaponList.add(item);
                            }
                            break;
                    }
                }

                if (!weaponList.isEmpty()) {
                    pc.sendPackets(new S_FixWeaponList(weaponList));
                } else {
                	pc.sendPackets(new S_SystemMessage("你没有任何可以附魔的武器。"));//hjx1000
                }
            } else {
                for (final L1ItemInstance item : itemList) {

                    // Find Weapon
                    switch (item.getItem().getType2()) {
                        case 1:
                            if (item.get_durability() > 0) {
                                weaponList.add(item);
                            }
                            break;
                    }
                }
                pc.sendPackets(new S_FixWeaponList(weaponList));
            }
            
            weaponList.clear();//清空临时包

            	

            // pc.sendPackets(new S_FixWeaponList(pc));

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
