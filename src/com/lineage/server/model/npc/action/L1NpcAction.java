package com.lineage.server.model.npc.action;

import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.npc.L1NpcHtml;

/**
 * 
 * @author dexc
 * 
 */
public interface L1NpcAction {

    public boolean acceptsRequest(String actionName, L1PcInstance pc,
            L1Object obj);

    public L1NpcHtml execute(String actionName, L1PcInstance pc, L1Object obj,
            byte args[]);

    public void execute(final String actionName, final String npcid);

    public L1NpcHtml executeWithAmount(String actionName, L1PcInstance pc,
            L1Object obj, long amount);

}
