package com.lineage.server.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1SummonInstance;

/**
 * 对话命令来自召唤兽的执行与判断
 * 
 * @author daien
 * 
 */
public class L1ActionSummon {

    private static final Log _log = LogFactory.getLog(L1ActionSummon.class);

    private final L1PcInstance _pc;

    /**
     * 对话命令来自PC的执行与判断
     * 
     * @param pc
     *            执行者
     */
    public L1ActionSummon(final L1PcInstance pc) {
        this._pc = pc;
    }

    /**
     * 传回执行命令者
     * 
     * @return
     */
    public L1PcInstance get_pc() {
        return this._pc;
    }

    /**
     * 选单命令执行
     * 
     * @param pet
     * @param action
     */
    public void action(final L1SummonInstance npc, final String action) {
        try {
            String status = null;
            if (action.equalsIgnoreCase("aggressive")) { // 攻击态势
                status = "1";

            } else if (action.equalsIgnoreCase("defensive")) { // 防御态势
                status = "2";

            } else if (action.equalsIgnoreCase("stay")) { // 休憩
                status = "3";

            } else if (action.equalsIgnoreCase("extend")) { // 散开
                status = "4";

            } else if (action.equalsIgnoreCase("alert")) { // 警戒
                status = "5";

            } else if (action.equalsIgnoreCase("dismiss")) { // 解散
                status = "6";
            }

            if (status != null) {
                npc.onFinalAction(_pc, status);
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        }
    }
}
