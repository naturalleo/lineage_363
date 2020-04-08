package com.lineage.server.model;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1Quest;

/**
 * 展示页面用
 * 
 * @author daien
 * 
 */
public class L1ActionShowHtml {

    private static final Log _log = LogFactory.getLog(L1ActionShowHtml.class);

    private final L1PcInstance _pc;

    /**
     * 展示页面用
     */
    public L1ActionShowHtml(final L1PcInstance pc) {
        _pc = pc;
    }

    /**
     * 展示任务选单
     * 
     * @param page
     */
    public void showQuestMap(int page) {
        try {
            final Map<Integer, L1Quest> list = _pc.get_otherList().QUESTMAP;
            if (list == null) {
                return;
            }

            // 全部页面数量
            int allpage = (list.size() / 10);
            if ((page > allpage) || (page < 0)) {
                page = 0;
            }

            // 显示的页面 显示的页面有不足10项的
            if (list.size() % 10 != 0) {
                allpage += 1;// 全部页面数量 +1
            }

            _pc.get_other().set_page(page);// 设置目前页面

            final int showId = page * 10;// 要显示的项目ID

            final StringBuilder stringBuilder = new StringBuilder();
            // 每页显示10笔(showId + 10)资料
            for (int key = showId; key < showId + 10; key++) {
                final L1Quest quest = list.get(key);
                if (quest != null) {
                    stringBuilder.append(quest.get_questname() + ",");

                } else {
                    stringBuilder.append(" ,");// 无该编号 显示为空
                }
            }

            // System.out.println(page + "/" + allpage);
            final String[] clientStrAry = stringBuilder.toString().split(",");

            if (allpage == 1) {
                _pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "y_qp0",
                        clientStrAry));

            } else {
                if (page < 1) {// 无上一页
                    _pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "y_qp1",
                            clientStrAry));

                } else if (page >= (allpage - 1)) {// 无下一页(吻合第一页为0 所以 -1)
                    _pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "y_qp3",
                            clientStrAry));

                } else {// 正常
                    _pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "y_qp2",
                            clientStrAry));
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
