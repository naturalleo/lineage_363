package com.lineage.data.npc.quest;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;

/**
 * 领双NPC<BR>
 * 98004<BR>
 * 说明:每周领双NPC
 * 
 * @author hjx1000
 * 
 */
public class Npc_lingxuang extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_lingxuang.class);

    private Npc_lingxuang() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_lingxuang();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
        	if (pc.getLevel() < 90) {//90级以上不可以领双
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "lingxuang"));
        	}else {
        		pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "lingxuang90"));
        	}
        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;
        if (pc.getLevel() < 90) {
            Calendar now = new GregorianCalendar();
            final int week = now.get(Calendar.DAY_OF_WEEK);
            final int hr = now.get(Calendar.HOUR_OF_DAY);
            if (week == 6 && hr > 17) {
            	if (!pc.hasSkillEffect(6666) && !pc.getInventory().checkItem(44104)) {//检查有无经验药水或状态
            		final L1ItemInstance item = ItemTable.get().createItem(44104);
            		item.setCount(36);//每星期赠送经验药水 36个
            		if (pc.getInventory().checkAddItem(item, 1) == 0) {
            			pc.getInventory().storeItem(item);
            			pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
            		}
            	} else {
            		pc.sendPackets(new S_SystemMessage("您身上存在经验药水或状态，不能领取。"));
            	}
            	isCloseList = true;
            }else {
            	pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "lingxuangoff"));
            }
            //System.out.println("week=="+week+"  "+"hr=="+hr);
        }
        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}
