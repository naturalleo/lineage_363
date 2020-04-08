package com.lineage.data.item_etcitem;

import static com.lineage.server.model.skill.L1SkillId.COOKING_NOW;

import java.util.Random;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBoxCooking;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.world.World;

/**
 * 料理书：1阶段41255 料理书：2阶段41256 料理书：3阶段41257 料理书：4阶段41258 料理书：5阶段41259
 */
public class Cooking_Book extends ItemExecutor {

    /**
	 *
	 */
    private Cooking_Book() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Cooking_Book();
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
        final int cookStatus = data[0];
        final int cookNo = data[1];
        if (cookStatus == 0) {
            pc.sendPackets(new S_PacketBoxCooking(itemId - 41255));

        } else {
            this.makeCooking(pc, cookNo);
        }
    }

    private void makeCooking(final L1PcInstance pc, final int cookNo) {
        final Random random = new Random();
        boolean isNearFire = false;
        for (final L1Object obj : World.get().getVisibleObjects(pc, 3)) {
            if (obj instanceof L1EffectInstance) {
                final L1EffectInstance effect = (L1EffectInstance) obj;
                if (effect.getGfxId() == 5943) {
                    isNearFire = true;
                    break;
                }
            }
        }
        if (!isNearFire) {
            pc.sendPackets(new S_ServerMessage(1160)); // 为了料理所以需要柴火。
            return;
        }
        if (pc.getMaxWeight() <= pc.getInventory().getWeight()) {
            pc.sendPackets(new S_ServerMessage(1103)); // 身上无法再携带烹饪的物品
            return;
        }
        if (pc.hasSkillEffect(COOKING_NOW)) {
            return;
        }
        pc.setSkillEffect(COOKING_NOW, 3 * 1000);

        final int chance = random.nextInt(100) + 1;

        boolean is6392 = false;
        boolean is6390 = false;
        boolean is6394 = false;
        boolean isErr = false;
        int itemid = 0;

        switch (cookNo) {
            case 0:
                if (pc.getInventory().checkItem(40057, 1)) {
                    pc.getInventory().consumeItem(40057, 1);
                    if ((chance >= 1) && (chance <= 90)) {
                        itemid = 41277;
                        is6392 = true;

                    } else if ((chance >= 91) && (chance <= 95)) {
                        itemid = 41285;
                        is6390 = true;

                    } else if ((chance >= 96) && (chance <= 100)) {
                        is6394 = true;
                    }
                } else {
                    isErr = true;
                }
                break;

            case 1:
                if (pc.getInventory().checkItem(41275, 1)) {
                    pc.getInventory().consumeItem(41275, 1);
                    if ((chance >= 1) && (chance <= 90)) {
                        itemid = 41278;
                        is6392 = true;

                    } else if ((chance >= 91) && (chance <= 95)) {
                        itemid = 41286;
                        is6390 = true;

                    } else if ((chance >= 96) && (chance <= 100)) {
                        is6394 = true;
                    }
                } else {
                    isErr = true;
                }
                break;

            case 2:
                if (pc.getInventory().checkItem(41263, 1)
                        && pc.getInventory().checkItem(41265, 1)) {
                    pc.getInventory().consumeItem(41263, 1);
                    pc.getInventory().consumeItem(41265, 1);
                    if ((chance >= 1) && (chance <= 90)) {
                        itemid = 41279;
                        is6392 = true;

                    } else if ((chance >= 91) && (chance <= 95)) {
                        itemid = 41287;
                        is6390 = true;

                    } else if ((chance >= 96) && (chance <= 100)) {
                        is6394 = true;
                    }
                } else {
                    isErr = true;
                }
                break;

            case 3:
                if (pc.getInventory().checkItem(41274, 1)
                        && pc.getInventory().checkItem(41267, 1)) {
                    pc.getInventory().consumeItem(41274, 1);
                    pc.getInventory().consumeItem(41267, 1);
                    if ((chance >= 1) && (chance <= 90)) {
                        itemid = 41280;
                        is6392 = true;

                    } else if ((chance >= 91) && (chance <= 95)) {
                        itemid = 41288;
                        is6390 = true;

                    } else if ((chance >= 96) && (chance <= 100)) {
                        is6394 = true;
                    }
                } else {
                    isErr = true;
                }
                break;

            case 4:
                if (pc.getInventory().checkItem(40062, 1)
                        && pc.getInventory().checkItem(40069, 1)
                        && pc.getInventory().checkItem(40064, 1)) {
                    pc.getInventory().consumeItem(40062, 1);
                    pc.getInventory().consumeItem(40069, 1);
                    pc.getInventory().consumeItem(40064, 1);
                    if ((chance >= 1) && (chance <= 90)) {
                        itemid = 41281;
                        is6392 = true;

                    } else if ((chance >= 91) && (chance <= 95)) {
                        itemid = 41289;
                        is6390 = true;

                    } else if ((chance >= 96) && (chance <= 100)) {
                        is6394 = true;
                    }
                } else {
                    isErr = true;
                }
                break;

            case 5:
                if (pc.getInventory().checkItem(40056, 1)
                        && pc.getInventory().checkItem(40060, 1)
                        && pc.getInventory().checkItem(40061, 1)) {
                    pc.getInventory().consumeItem(40056, 1);
                    pc.getInventory().consumeItem(40060, 1);
                    pc.getInventory().consumeItem(40061, 1);
                    if ((chance >= 1) && (chance <= 90)) {
                        itemid = 41282;
                        is6392 = true;

                    } else if ((chance >= 91) && (chance <= 95)) {
                        itemid = 41290;
                        is6390 = true;

                    } else if ((chance >= 96) && (chance <= 100)) {
                        is6394 = true;
                    }
                } else {
                    isErr = true;
                }
                break;

            case 6:
                if (pc.getInventory().checkItem(41276, 1)) {
                    pc.getInventory().consumeItem(41276, 1);
                    if ((chance >= 1) && (chance <= 90)) {
                        itemid = 41283;
                        is6392 = true;

                    } else if ((chance >= 91) && (chance <= 95)) {
                        itemid = 41291;
                        is6390 = true;

                    } else if ((chance >= 96) && (chance <= 100)) {
                        is6394 = true;
                    }
                } else {
                    isErr = true;
                }
                break;

            case 7:
                if (pc.getInventory().checkItem(40499, 1)
                        && pc.getInventory().checkItem(40060, 1)) {
                    pc.getInventory().consumeItem(40499, 1);
                    pc.getInventory().consumeItem(40060, 1);
                    if ((chance >= 1) && (chance <= 90)) {
                        itemid = 41284;
                        is6392 = true;

                    } else if ((chance >= 91) && (chance <= 95)) {
                        itemid = 41292;
                        is6390 = true;

                    } else if ((chance >= 96) && (chance <= 100)) {
                        is6394 = true;
                    }
                } else {
                    isErr = true;
                }
                break;

            case 8:
                if (pc.getInventory().checkItem(49040, 1)
                        && pc.getInventory().checkItem(49048, 1)) {
                    pc.getInventory().consumeItem(49040, 1);
                    pc.getInventory().consumeItem(49048, 1);
                    if ((chance >= 1) && (chance <= 90)) {
                        itemid = 49049;
                        is6392 = true;

                    } else if ((chance >= 91) && (chance <= 95)) {
                        itemid = 49057;
                        is6390 = true;

                    } else if ((chance >= 96) && (chance <= 100)) {
                        is6394 = true;
                    }
                } else {
                    isErr = true;
                }
                break;

            case 9:
                if (pc.getInventory().checkItem(49041, 1)
                        && pc.getInventory().checkItem(49048, 1)) {
                    pc.getInventory().consumeItem(49041, 1);
                    pc.getInventory().consumeItem(49048, 1);
                    if ((chance >= 1) && (chance <= 90)) {
                        itemid = 49050;
                        is6392 = true;

                    } else if ((chance >= 91) && (chance <= 95)) {
                        itemid = 49058;
                        is6390 = true;

                    } else if ((chance >= 96) && (chance <= 100)) {
                        is6394 = true;
                    }
                } else {
                    isErr = true;
                }
                break;

            case 10:
                if (pc.getInventory().checkItem(49042, 1)
                        && pc.getInventory().checkItem(41265, 1)
                        && pc.getInventory().checkItem(49048, 1)) {
                    pc.getInventory().consumeItem(49042, 1);
                    pc.getInventory().consumeItem(41265, 1);
                    pc.getInventory().consumeItem(49048, 1);
                    if ((chance >= 1) && (chance <= 90)) {
                        itemid = 49051;
                        is6392 = true;

                    } else if ((chance >= 91) && (chance <= 95)) {
                        itemid = 49059;
                        is6390 = true;

                    } else if ((chance >= 96) && (chance <= 100)) {
                        is6394 = true;
                    }
                } else {
                    isErr = true;
                }
                break;

            case 11:
                if (pc.getInventory().checkItem(49043, 1)
                        && pc.getInventory().checkItem(49048, 1)) {
                    pc.getInventory().consumeItem(49043, 1);
                    pc.getInventory().consumeItem(49048, 1);
                    if ((chance >= 1) && (chance <= 90)) {
                        itemid = 49052;
                        is6392 = true;

                    } else if ((chance >= 91) && (chance <= 95)) {
                        itemid = 49060;
                        is6390 = true;

                    } else if ((chance >= 96) && (chance <= 100)) {
                        is6394 = true;
                    }
                } else {
                    isErr = true;
                }
                break;

            case 12:
                if (pc.getInventory().checkItem(49044, 1)
                        && pc.getInventory().checkItem(49048, 1)) {
                    pc.getInventory().consumeItem(49044, 1);
                    pc.getInventory().consumeItem(49048, 1);
                    if ((chance >= 1) && (chance <= 90)) {
                        itemid = 49053;
                        is6392 = true;

                    } else if ((chance >= 91) && (chance <= 95)) {
                        itemid = 49061;
                        is6390 = true;

                    } else if ((chance >= 96) && (chance <= 100)) {
                        is6394 = true;
                    }
                } else {
                    isErr = true;
                }
                break;

            case 13:
                if (pc.getInventory().checkItem(49045, 1)
                        && pc.getInventory().checkItem(49048, 1)) {
                    pc.getInventory().consumeItem(49045, 1);
                    pc.getInventory().consumeItem(49048, 1);
                    if ((chance >= 1) && (chance <= 90)) {
                        itemid = 49054;
                        is6392 = true;

                    } else if ((chance >= 91) && (chance <= 95)) {
                        itemid = 49062;
                        is6390 = true;

                    } else if ((chance >= 96) && (chance <= 100)) {
                        is6394 = true;
                    }
                } else {
                    isErr = true;
                }
                break;

            case 14:
                if (pc.getInventory().checkItem(49046, 1)
                        && pc.getInventory().checkItem(49048, 1)) {
                    pc.getInventory().consumeItem(49046, 1);
                    pc.getInventory().consumeItem(49048, 1);
                    if ((chance >= 1) && (chance <= 30)) {
                        itemid = 49055;
                        is6392 = true;

                    } else if ((chance >= 31) && (chance <= 65)) {
                        itemid = 49063;
                        is6390 = true;

                    } else if ((chance >= 66) && (chance <= 100)) {
                        is6394 = true;
                    }
                } else {
                    isErr = true;
                }
                break;

            case 15:
                if (pc.getInventory().checkItem(49047, 1)
                        && pc.getInventory().checkItem(40499, 1)
                        && pc.getInventory().checkItem(49048, 1)) {
                    pc.getInventory().consumeItem(49047, 1);
                    pc.getInventory().consumeItem(40499, 1);
                    pc.getInventory().consumeItem(49048, 1);
                    if ((chance >= 1) && (chance <= 90)) {
                        itemid = 49056;
                        is6392 = true;

                    } else if ((chance >= 91) && (chance <= 95)) {
                        itemid = 49064;
                        is6390 = true;

                    } else if ((chance >= 96) && (chance <= 100)) {
                        is6394 = true;
                    }
                } else {
                    isErr = true;
                }
                break;

            case 16:
                if (pc.getInventory().checkItem(49048, 1)
                        && pc.getInventory().checkItem(49243, 1)
                        && pc.getInventory().checkItem(49260, 1)) {
                    pc.getInventory().consumeItem(49048, 1);
                    pc.getInventory().consumeItem(49243, 1);
                    pc.getInventory().consumeItem(49260, 1);
                    if ((chance >= 1) && (chance <= 90)) {
                        itemid = 49244;
                        is6392 = true;

                    } else if ((chance >= 91) && (chance <= 95)) {
                        itemid = 49252;
                        is6390 = true;

                    } else if ((chance >= 96) && (chance <= 100)) {
                        is6394 = true;
                    }
                } else {
                    isErr = true;
                }
                break;

            case 17: // グリフォン烧き
                if (pc.getInventory().checkItem(49048, 1)
                        && pc.getInventory().checkItem(49243, 1)
                        && pc.getInventory().checkItem(49261, 1)) {
                    pc.getInventory().consumeItem(49048, 1);
                    pc.getInventory().consumeItem(49243, 1);
                    pc.getInventory().consumeItem(49261, 1);
                    if ((chance >= 1) && (chance <= 90)) {
                        itemid = 49245;
                        is6392 = true;

                    } else if ((chance >= 91) && (chance <= 95)) {
                        itemid = 49253;
                        is6390 = true;

                    } else if ((chance >= 96) && (chance <= 100)) {
                        is6394 = true;
                    }
                } else {
                    isErr = true;
                }
                break;

            case 18:
                if (pc.getInventory().checkItem(49048, 1)
                        && pc.getInventory().checkItem(49243, 1)
                        && pc.getInventory().checkItem(49262, 1)) {
                    pc.getInventory().consumeItem(49048, 1);
                    pc.getInventory().consumeItem(49243, 1);
                    pc.getInventory().consumeItem(49262, 1);
                    if ((chance >= 1) && (chance <= 90)) {
                        itemid = 49246;
                        is6392 = true;

                    } else if ((chance >= 91) && (chance <= 95)) {
                        itemid = 49254;
                        is6390 = true;

                    } else if ((chance >= 96) && (chance <= 100)) {
                        is6394 = true;
                    }
                } else {
                    isErr = true;
                }
                break;

            case 19:
                if (pc.getInventory().checkItem(49048, 1)
                        && pc.getInventory().checkItem(49243, 1)
                        && pc.getInventory().checkItem(49263, 1)) {
                    pc.getInventory().consumeItem(49048, 1);
                    pc.getInventory().consumeItem(49243, 1);
                    pc.getInventory().consumeItem(49263, 1);
                    if ((chance >= 1) && (chance <= 90)) {
                        itemid = 49247;
                        is6392 = true;

                    } else if ((chance >= 91) && (chance <= 95)) {
                        itemid = 49255;
                        is6390 = true;

                    } else if ((chance >= 96) && (chance <= 100)) {
                        is6394 = true;
                    }
                } else {
                    isErr = true;
                }
                break;

            case 20:
                if (pc.getInventory().checkItem(49048, 1)
                        && pc.getInventory().checkItem(49243, 1)
                        && pc.getInventory().checkItem(49264, 1)) {
                    pc.getInventory().consumeItem(49048, 1);
                    pc.getInventory().consumeItem(49243, 1);
                    pc.getInventory().consumeItem(49264, 1);
                    if ((chance >= 1) && (chance <= 90)) {
                        itemid = 49248;
                        is6392 = true;

                    } else if ((chance >= 91) && (chance <= 95)) {
                        itemid = 49256;
                        is6390 = true;

                    } else if ((chance >= 96) && (chance <= 100)) {
                        is6394 = true;
                    }
                } else {
                    isErr = true;
                }
                break;

            case 21:
                if (pc.getInventory().checkItem(49048, 1)
                        && pc.getInventory().checkItem(49243, 1)
                        && pc.getInventory().checkItem(49265, 1)) {
                    pc.getInventory().consumeItem(49048, 1);
                    pc.getInventory().consumeItem(49243, 1);
                    pc.getInventory().consumeItem(49265, 1);
                    if ((chance >= 1) && (chance <= 90)) {
                        itemid = 49249;
                        is6392 = true;

                    } else if ((chance >= 91) && (chance <= 95)) {
                        itemid = 49257;
                        is6390 = true;

                    } else if ((chance >= 96) && (chance <= 100)) {
                        is6394 = true;
                    }
                } else {
                    isErr = true;
                }
                break;

            case 22:
                if (pc.getInventory().checkItem(49048, 1)
                        && pc.getInventory().checkItem(49243, 1)
                        && pc.getInventory().checkItem(49266, 1)) {
                    pc.getInventory().consumeItem(49048, 1);
                    pc.getInventory().consumeItem(49243, 1);
                    pc.getInventory().consumeItem(49266, 1);
                    if ((chance >= 1) && (chance <= 90)) {
                        itemid = 49250;
                        is6392 = true;

                    } else if ((chance >= 91) && (chance <= 95)) {
                        itemid = 49258;
                        is6390 = true;

                    } else if ((chance >= 96) && (chance <= 100)) {
                        is6394 = true;
                    }
                } else {
                    isErr = true;
                }
                break;

            case 23:
                if (pc.getInventory().checkItem(49048, 1)
                        && pc.getInventory().checkItem(49243, 1)
                        && pc.getInventory().checkItem(49267, 1)) {
                    pc.getInventory().consumeItem(49048, 1);
                    pc.getInventory().consumeItem(49243, 1);
                    pc.getInventory().consumeItem(49267, 1);
                    if ((chance >= 1) && (chance <= 90)) {
                        itemid = 49251;
                        is6392 = true;

                    } else if ((chance >= 91) && (chance <= 95)) {
                        itemid = 49259;
                        is6390 = true;

                    } else if ((chance >= 96) && (chance <= 100)) {
                        is6394 = true;
                    }
                } else {
                    isErr = true;
                }
                break;
        }

        if (is6392) {
            if (itemid != 0) {
                CreateNewItem.createNewItem(pc, itemid, 1);
            }
            pc.broadcastPacketX10(new S_SkillSound(pc.getId(), 6392));
        }

        if (is6390) {
            if (itemid != 0) {
                CreateNewItem.createNewItem(pc, itemid, 1);
            }
            pc.sendPacketsAll(new S_SkillSound(pc.getId(), 6390));
        }

        if (is6394) {
            pc.sendPackets(new S_ServerMessage(1101)); // 烹饪失败...。
            pc.broadcastPacketX10(new S_SkillSound(pc.getId(), 6394));
        }

        if (isErr) {
            pc.sendPackets(new S_ServerMessage(1102)); // 不够材料可以烹饪...
        }
    }
}
