package com.lineage.data.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Event;
import com.lineage.server.templates.L1Item;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.world.World;

/**
 * 连线奖励系统<BR>
 * <font color="#0000FF">参数1:给予时间(单位分钟)</font><BR>
 * <font color="#0000FF">参数2:以下设置每3组为一个单位#分隔每项设置(状态#物品编号#给予数量)</font><BR>
 * <font color="#6E8B3D"> 条件一:给予状态 1代表商店状态 2代表钓鱼状态 3代表全部状态<BR>
 * 条件二:物品编号<BR>
 * 条件三:数量<BR>
 * </font> 状态判断说明<BR>
 * 吻合1 将不会发放 2跟3<BR>
 * 吻合2 将不会发放 3<BR>
 * 其余状态均判断为3<BR>
 * <BR>
 * 其余设定类推<BR>
 * <font color="#FF0000"> 警告:<BR>
 * 不能堆叠物品将被排除<BR>
 * 错误的设定参数将导致人物无法取得在线奖励<BR>
 * 相同的状态编号代表给予该排序中的物品<BR>
 * 例如 2个设置状态编号都是1<BR>
 * 人物抵达在线时间并且是商店状态可以取得2种奖励</font><BR>
 * <BR>
 * #SQL增加代码: DELETE FROM `server_event` WHERE `id`='11' ; INSERT INTO
 * `server_event` VALUES ('11', '连线奖励系统', 'OnlineGiftSet', '1', '20, 3#44067#1',
 * '说明:连线奖励系统');
 * 
 * @author loli
 * 
 */
public class OnlineGiftSet extends EventExecutor {

    private static final Log _log = LogFactory.getLog(OnlineGiftSet.class);

    // 时间
    private static int _time = 0;

    // 状态/物品表
    private static final Map<Integer, ArrayList<GetItemData>> _giftList = new HashMap<Integer, ArrayList<GetItemData>>();

    // 线上人物/连线时间(分钟)
    private static final Map<L1PcInstance, Integer> _getMap = new ConcurrentHashMap<L1PcInstance, Integer>();

    /**
	 *
	 */
    private OnlineGiftSet() {
        // TODO Auto-generated constructor stub
    }

    public static EventExecutor get() {
        return new OnlineGiftSet();
    }

    /**
     * 加入在线奖励清单
     * 
     * @param tgpc
     */
    public static void add(final L1PcInstance tgpc) {
        if (_time == 0) {
            return;
        }
        if (_getMap.get(tgpc) == null) {
            _getMap.put(tgpc, _time);
        }
    }

    /**
     * 移出在线奖励清单
     */
    public static void remove(final L1PcInstance tgpc) {
        if (_time == 0) {
            return;
        }
        // 移出在线奖励清单
        _getMap.remove(tgpc);
    }

    private class GetItemData {
        public int _getItemId = 40308;// 给予物品编号
        public int _getAmount = 1;// 给予数量
    }

    /**
     * 给予物品的处理
     * 
     * @param tgpc
     */
    private static void getitem(L1PcInstance tgpc) {
        try {
            if (check(tgpc)) {
                ArrayList<GetItemData> value = null;
                if (tgpc.isPrivateShop()) {// 商店状态
                    value = _giftList.get(new Integer(1));
                } else if (tgpc.isFishing()) {// 钓鱼状态
                    value = _giftList.get(new Integer(2));
                } else {
                    value = _giftList.get(new Integer(3));
                }
                if (value == null) {
                    value = _giftList.get(new Integer(3));
                }
                if (value == null) {
                    return;
                }
                for (GetItemData iteminfo : value) {
                    if (iteminfo == null) {
                        continue;
                    }
                    final L1ItemInstance item = ItemTable.get().createItem(
                            iteminfo._getItemId);
                    item.setCount(iteminfo._getAmount);
                    if (item != null) {
                        // 加入背包成功
                        if (tgpc.getInventory().checkAddItem(item, 1) == 0) {
                            tgpc.getInventory().storeItem(item);
                            // 送出讯息
                            tgpc.sendPackets(new S_ServerMessage("\\aD获得在线奖励: "
                                    + item.getLogName()));
                        }
                    }
                }
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 判断
     * 
     * @param tgpc
     * @return true:执行 false:不执行
     */
    public static boolean check(final L1PcInstance tgpc) {
        try {
            if (tgpc == null) {
                return false;
            }

            if (tgpc.getOnlineStatus() == 0) {
                return false;
            }

            if (tgpc.getNetConnection() == null) {
                return false;
            }
            
//            if (!tgpc.hasSkillEffect(Card_Fee)) { //无点卡
//            	return false;
//            }
//            if (tgpc.isActived()) { //挂机没奖励
//            	return false;
//            }
            
//            if (tgpc.getNetConnection().getAccount().get_card_fee() < 40) { //点卡小于40
//            	return false;
//            }            
            
            if (tgpc.getMapId()!= 4) {
            	return false;
            }
            if (tgpc.getX() < 33395) {
            	return false;
            }
            if (tgpc.getX() > 33497) {
            	return false;
            }
            if (tgpc.getY() < 32783) {
            	return false;
            }
            if (tgpc.getY() > 32846) {
            	return false;
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public void execute(final L1Event event) {
        final PerformanceTimer timer = new PerformanceTimer();
        boolean isError = false;
        try {
            final String[] set = event.get_eventother().split(",");

            try {
                _time = Integer.parseInt(set[0]);
                if (_time <= 0) {
                    _log.error("设定给予奖励的时间(分钟)异常 - 将不启用本项设置");
                    isError = true;
                    return;
                }

            } catch (Exception e) {
                _log.error("设定给予奖励的时间(分钟)异常 - 将不启用本项设置");
                isError = true;
                return;
            }

            try {
                for (int i = 1; i < set.length; i++) {
                    final String[] setItem = set[i].split("#");
                    GetItemData itemData = new GetItemData();

                    int type = Integer.parseInt(setItem[0]);// 取回状态设置
                    switch (type) {
                        case 1:
                        case 2:
                            break;

                        default:
                            type = 3;
                            break;
                    }

                    itemData._getItemId = Integer.parseInt(setItem[1]);

                    L1Item item = ItemTable.get().getTemplate(
                            itemData._getItemId);
                    if (item == null) {
                        _log.error("设定给予奖励物品异常 - 将不启用本项设置 - 找不到这个编号的物品:"
                                + itemData._getItemId);
                        isError = true;
                        break;
                    }
                    if (!item.isStackable()) {
                        _log.error("设定给予奖励物品异常 - 这个编号的物品无发堆叠:"
                                + itemData._getItemId);
                        continue;
                    }
                    itemData._getAmount = Integer.parseInt(setItem[2]);
                    if (itemData._getAmount <= 0) {
                        _log.error("设定给予奖励物品异常 - 将不启用本项设置 - 数量小于等于0:"
                                + itemData._getItemId + " 预设将只给1个");
                        itemData._getAmount = 1;
                    }

                    if (_giftList.get(new Integer(type)) == null) {
                        final ArrayList<GetItemData> value = new ArrayList<GetItemData>();
                        value.add(itemData);
                        _giftList.put(new Integer(type), value);

                    } else {
                        _giftList.get(new Integer(type)).add(itemData);
                    }
                }

            } catch (Exception e) {
                _log.error("设定给予奖励物品异常 - 请检查#内的设置是否吻合3项(状态#物品编号#给予数量)");
                isError = true;
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            if (!isError) {
                final GetItemTimer getItemTimer = new GetItemTimer();
                getItemTimer.start();
                _log.info("载入给予奖励物品: " + _giftList.size() + "(" + timer.get()
                        + "ms)");

            } else {
                _time = 0;
                _giftList.clear();
            }
        }
    }

    private class GetItemTimer extends TimerTask {

        private ScheduledFuture<?> _timer;

        public void start() {
            final int timeMillis = 1000 * 60;// 1分钟
            _timer = GeneralThreadPool.get().scheduleAtFixedRate(this,
                    timeMillis, timeMillis);
        }

        @Override
        public void run() {
            try {
                // 包含元素
                if (!_getMap.isEmpty()) {
                    for (final L1PcInstance tgpc : _getMap.keySet()) {
                        Thread.sleep(1);
                        if (World.get().getPlayer(tgpc.getName()) == null) {
                            // 移出在线奖励清单
                            _getMap.remove(tgpc);
                            continue;
                        }
                        Integer time = _getMap.get(tgpc);
                        if (time != null) {
                            time--;
                            if (time <= 0) {
                                getitem(tgpc);
                                _getMap.put(tgpc, _time);

                            } else {
                                _getMap.put(tgpc, time);
                            }
                        }
                    }
                }

            } catch (final Exception e) {
                _log.error("在线奖励清单时间轴异常重启", e);
                GeneralThreadPool.get().cancel(_timer, false);
                final GetItemTimer getItemTimer = new GetItemTimer();
                getItemTimer.start();
            }
        }
    }
}
