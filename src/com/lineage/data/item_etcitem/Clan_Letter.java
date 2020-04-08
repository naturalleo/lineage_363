package com.lineage.data.item_etcitem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.Config;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.CharItemsReading;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.datatables.sql.LetterTable;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;

/**
 * 血盟的信纸（未使用）40311
 */
public class Clan_Letter extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(Clan_Letter.class);

    /**
	 *
	 */
    private Clan_Letter() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Clan_Letter();
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
        final int letterCode = data[0];// data[0] = readH()
        final String letterReceiver = pc.getText();
        final byte[] letterText = pc.getTextByte();
        if (this.writeClanLetter(itemId, pc, letterCode, letterReceiver,
                letterText)) {
            pc.getInventory().removeItem(item, 1);
        }
    }

    private boolean writeClanLetter(final int itemId, final L1PcInstance pc,
            final int letterCode, final String letterReceiver,
            final byte[] letterText) {
        L1Clan targetClan = null;
        final Collection<L1Clan> allClans = WorldClan.get().getAllClans();
        for (final Iterator<L1Clan> iter = allClans.iterator(); iter.hasNext();) {
            final L1Clan clan = iter.next();
            if (clan.getClanName().toLowerCase()
                    .equals(letterReceiver.toLowerCase())) {
                targetClan = clan;
                break;
            }
        }
        if (targetClan == null) {
            pc.sendPackets(new S_ServerMessage(434)); // 没有收信者。
            return false;
        }

        final String memberName[] = targetClan.getAllMembers();
        for (int i = 0; i < memberName.length; i++) {
            final L1ItemInstance item = ItemTable.get().createItem(49016);
            if (item == null) {
                return false;
            }
            item.setCount(1);
            if (this.sendLetter(pc, memberName[i], item, false)) {
                this.saveLetter(item.getId(), letterCode, pc.getName(),
                        memberName[i], letterText);
            }
        }
        return true;
    }

    private boolean sendLetter(final L1PcInstance pc, final String name,
            final L1ItemInstance item, final boolean isFailureMessage) {
        final L1PcInstance target = World.get().getPlayer(name);
        if (target != null) {
            if (target.getInventory().checkAddItem(item, 1) == L1Inventory.OK) {
                target.getInventory().storeItem(item);
                target.sendPackets(new S_SkillSound(target.getId(), 1091));
                target.sendPackets(new S_ServerMessage(428)); // 您收到鸽子信差给你的信件。
            } else {
                if (isFailureMessage) {
                    // 对方的负重太重，无法再给予。
                    pc.sendPackets(new S_ServerMessage(942));
                }
                return false;
            }
        } else {
            if (CharacterTable.doesCharNameExist(name)) {
                try {
                    final int objid = CharObjidTable.get().charObjid(name);
                    CopyOnWriteArrayList<L1ItemInstance> list = CharItemsReading
                            .get().loadItems(objid);

                    if (list.size() < 180) {
                        CharItemsReading.get().storeItem(objid, item);

                    } else {
                        if (isFailureMessage) {
                            // 对方的负重太重，无法再给予。
                            pc.sendPackets(new S_ServerMessage(942));
                        }
                        return false;
                    }

                } catch (final Exception e) {
                    _log.error(e.getLocalizedMessage(), e);
                }

            } else {
                if (isFailureMessage) {
                    pc.sendPackets(new S_ServerMessage(109, name)); // 没有叫%0的人。
                }
                return false;
            }
        }
        return true;
    }

    private void saveLetter(final int itemObjectId, final int code,
            final String sender, final String receiver, final byte[] text) {
        // 取回日期
        final SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
        final TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
        final String date = sdf.format(Calendar.getInstance(tz).getTime());

        int spacePosition1 = 0;
        int spacePosition2 = 0;
        for (int i = 0; i < text.length; i += 2) {
            if ((text[i] == 0) && (text[i + 1] == 0)) {
                if (spacePosition1 == 0) {
                    spacePosition1 = i;
                } else if ((spacePosition1 != 0) && (spacePosition2 == 0)) {
                    spacePosition2 = i;
                    break;
                }
            }
        }

        final int subjectLength = spacePosition1 + 2;
        int contentLength = spacePosition2 - spacePosition1;
        if (contentLength <= 0) {
            contentLength = 1;
        }
        final byte[] subject = new byte[subjectLength];
        final byte[] content = new byte[contentLength];
        System.arraycopy(text, 0, subject, 0, subjectLength);
        System.arraycopy(text, subjectLength, content, 0, contentLength);
        LetterTable.getInstance().writeLetter(itemObjectId, code, sender,
                receiver, date, 0, subject, content);
    }
}
