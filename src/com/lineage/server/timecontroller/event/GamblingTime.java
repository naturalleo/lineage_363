package com.lineage.server.timecontroller.event;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.event.GamblingSet;
import com.lineage.data.event.gambling.Gambling;
import com.lineage.data.event.gambling.GamblingNpc;
import com.lineage.server.datatables.lock.GamblingReading;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.model.Instance.L1GamInstance;
import com.lineage.server.model.Instance.L1GamblingInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_KillMessage;
import com.lineage.server.serverpackets.S_NpcChat;
import com.lineage.server.serverpackets.S_NpcChatShouting;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Gambling;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.timecontroller.server.ServerRestartTimer;
import com.lineage.server.world.World;

/**
 * 奇岩赌场<BR>
 * 70035赛西<BR>
 * 70041波金<BR>
 * 70042波丽
 * 
 * @author dexc
 * 
 */
public class GamblingTime extends TimerTask {

    private static final Log _log = LogFactory.getLog(GamblingTime.class);

    private static Gambling _gambling;

    private static boolean _issystem = true;// 是否可以进行比赛

    private static boolean _isStart = false;// 比赛开始

    private static int _gamblingNo = 100;// 场次编号

    private static Random _random = new Random();

    // private static final Map<Integer, Integer> _xmap = new
    // ConcurrentHashMap<Integer, Integer>();

    private ScheduledFuture<?> _timer;

    public void start() {
        _gamblingNo = GamblingReading.get().maxId();
        final int timeMillis = GamblingSet.GAMADENATIME * 60 * 1000;// 间隔时间
        _timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis,
                timeMillis);
    }

    /**
     * 传回场次编号
     * 
     * @return
     */
    public static int get_gamblingNo() {
        return _gamblingNo;
    }

    /**
     * 传回比赛模组
     * 
     * @return
     */
    public static Gambling get_gambling() {
        return _gambling;
    }

    /**
     * 传回是否已开始比赛
     * 
     * @return
     */
    public static boolean isStart() {
        return _isStart;
    }

    /**
     * 传回是否可以进行比赛
     */
    public static void set_status(boolean b) {
        _issystem = b;
    }

    @Override
    public void run() {
        try {
            if (!_issystem) {
                return;
            }
            if (_gambling != null) {
                return;
            }
            // 重新启动时间不足30分钟
            if (ServerRestartTimer.isRtartTime()) {
                return;
            }

            doorOpen(false);

            long previous = 0;
            /*
             * final L1Gambling previouss =
             * GamblingReading.get().getGambling(_gamblingNo - 1);
             * 
             * if (previouss != null) { if (GamblingSet.NOWODDS > 0D) { //
             * 上次赌金总数的15% previous = (long) Math.max((previouss.get_adena() *
             * GamblingSet.NOWODDS), 0); } } else { previous = 1000000;//
             * 无上次奖金资料 预设 100万 }
             * 
             * if (previous <= 0) { previous = 1000000;// 上次奖金资料为0 预设 100万 }
             */

            // 产生讯息封包 (3033 奇岩竞技场比赛即将开始，本场基本将金达: %0 元。)
            for (final Iterator<L1PcInstance> iter = World.get()
                    .getAllPlayers().iterator(); iter.hasNext();) {
                final L1PcInstance listner = iter.next();
                // 拒绝接收广播频道
                if (!listner.isShowWorldChat()) {
                    continue;
                }
                // 奇岩竞技场比赛即将开始，本场基本将金达: %0 元。
                listner.sendPackets(new S_ServerMessage("奇岩赌狗比赛即将开始"));
            }
            /*
             * for (final L1PcInstance listner : World.get().getAllPlayers()) {
             * // 拒绝接收广播频道 if (!listner.isShowWorldChat()) { continue; } //
             * 奇岩竞技场比赛即将开始，本场基本将金达: %0 元。 listner.sendPackets(new
             * S_ServerMessage("奇岩竞技场比赛即将开始")); }
             */

            // 召唤比赛者
            _gambling = new Gambling();
            _gambling.set_gmaNpc(previous);

            // 广播比赛
            boolean is5m = true;// 5分钟计时

            int timeS = 300;
            while (is5m) {
                Thread.sleep(1000);// 1秒
                // System.out.println(timeM + "分钟");
                switch (timeS) {
                    case 300:// 时间5分钟
                    case 240:// 时间4分钟
                    case 180:// 时间3分钟
                    case 120:// 时间2分钟
                    case 60:// 时间1分钟
                        npcChat((timeS / 60), 0, null);
                        break;

                    case 10:// 时间10秒
                    case 9:// 时间9秒
                    case 8:// 时间8秒
                    case 7:// 时间7秒
                    case 6:// 时间6秒
                    case 5:// 时间5秒
                    case 4:// 时间4秒
                    case 3:// 时间3秒
                    case 2:// 时间2秒
                        npcChat(timeS, 1, null);
                        break;

                    case 1:// 时间1秒
                        _isStart = true;
                        npcChat(0, 2, null);
                        break;

                    case 0:// 时间0秒
                        doorOpen(true);

                        npcChat(0, 3, null);
                        _gambling.startGam();
                        ;// 启动比赛
                        is5m = false;
                        break;
                }
                timeS--;
            }

            // 计算赔率
            _gambling.set_allRate();

            Thread.sleep(2000);

            for (final GamblingNpc gamblingNpc : _gambling.get_allNpc()
                    .values()) {
                // 宣告赔率
                npcChat(0, 4, gamblingNpc);
                Thread.sleep(1000);// 100
            }

            while (_gambling.get_oneNpc() == null) {
                Thread.sleep(100);// 100
            }

            if (_gambling.get_oneNpc() != null) {
                final GamblingNpc one = _gambling.get_oneNpc();
                // 宣告优胜
                npcChat(0, 5, one);

                // $375 第 $366 场比赛的优胜者是
                final String onename = one.get_npc().getNameId();
                for (final L1PcInstance listner : World.get().getAllPlayers()) {
                    // 拒绝接收广播频道
                    if (!listner.isShowWorldChat()) {
                        continue;
                    }
                    listner.sendPackets(new S_ServerMessage(166, "$375 "
                            + _gamblingNo + " $366 " + onename + " "
                            + GamblingSet.GAMADENATIME + ":"
                            + (one.get_adena() * 5)));
                }

                _log.info("奇岩赌场:" + _gamblingNo + " 优胜者:"
                        + one.get_npc().getName() + "/" + one.get_xId() + "-"
                        + _gambling.WIN + " (" + one.get_adena() * 5 + ")");
                /*
                 * int value = 1; if (_xmap.get(one.get_xId()) != null) { value
                 * += _xmap.get(one.get_xId()); } _xmap.put(one.get_xId(),
                 * value); for (Integer key : _xmap.keySet()) { _log.info(key +
                 * " (" + _xmap.get(key) + ")"); }
                 */

                // 取回胜者纪录
                final int npcid = _gambling.get_oneNpc().get_npc().getNpcId();
                // 获胜NPC赔率
                final double rate = 5.0;// _gambling.get_oneNpc().get_rate();
                // 本场总下注金额
                final long adena = _gambling.get_allAdena();
                // 获胜下注数量
                final int outcount = (int) (_gambling.get_oneNpc().get_adena() / GamblingSet.GAMADENA);

                final L1Gambling gambling = new L1Gambling();
                gambling.set_id(_gamblingNo);
                gambling.set_adena(adena);
                gambling.set_rate(rate);
                gambling.set_gamblingno(_gamblingNo + "-" + npcid);
                gambling.set_outcount(outcount);

                GamblingReading.get().add(gambling);
            }
            synchronized (this) {
                _gamblingNo++;
            }
            Thread.sleep(20000);// 100

        } catch (final Exception e) {
            _log.error("奇岩赌场时间轴异常重启", e);
            GeneralThreadPool.get().cancel(_timer, false);
            final GamblingTime timerTask = new GamblingTime();
            timerTask.start();

        } finally {
            if (_gambling == null) {
                return;
            }

            // 删除参赛者
            _gambling.delAllNpc();
            _gambling.clear();

            _gambling = null;

            if (!_isStart) {
                return;
            }
            // 给予奇岩城税收(给予大于0D)
            /*
             * if (GamblingSet.GET_GIRAN_CASTLE > 0D) { // 上次赌金总数的2% long
             * previous = (long) Math.max((gambling.get_adena() *
             * GamblingSet.GET_GIRAN_CASTLE), 0); getCastle(previous); }
             */
            _isStart = false;
        }
    }

    /**
     * 给予奇岩城税金2%
     * 
     * @param previous
     */
    /*
     * private void getCastle(long previous) { final L1Castle l1castle =
     * CastleReading.get().getCastleTable(L1CastleLocation.GIRAN_CASTLE_ID);
     * synchronized (l1castle) { long money = l1castle.getPublicMoney(); money
     * += previous; l1castle.setPublicMoney(money);
     * CastleReading.get().updateCastle(l1castle); } }
     */

    /**
     * 打开/关闭 赌场门
     * 
     * @param isOpen
     *            false:关闭 true:打开
     */
    private void doorOpen(final boolean isOpen) {
        L1DoorInstance.openGam(isOpen);
    }

    private static final String[] _msg = new String[] { "刚刚喝到的是绿水吗?等等跑就知道~^^~",
            "隔壁跑道听说昨天踩到钉子.....", "买我啦!!看我的脸就知道我赢!!", "快点跑完我也想去打一下副本~~",
            "你在看我吗??你可以在靠近一点...", "山歌都不一定跑赢我!看我强壮的鸡腿!", "那个谁谁谁!!等等不要跑超过我黑...",
            "有没骑士在场阿?给瓶勇水喝喝~~", "地球是很危险的...", "谁给我来一下祝福!加持!加持~",
            "咦~~有一个参赛者是传说中的跑道之王...", "没事!没事!!山歌只是个传说~~", "隔壁的~你刚刚喝什么?你是不是作弊??",
            "肚子好饿...没吃饭能赢吗??", "哇靠~~今天感觉精力充沛耶!!", "隔壁的!!你控制一下不要一直放屁!!",
            "嗯......嗯......其他几个是憋三,我会赢....", "我刚刚好像喝多了...头还在晕...",
            "昨晚的妞真正丫，喝绿水算三小。", "肚子饿死了，跑不动了。", "输赢都行啦，娱乐而已。", "小赌怡情，大赌伤身。",
            "我要放点水。经常赢都有点不好意思了。", "【强化勇气的药水】是干嘛的？山歌给了我一罐。", "昨晚被吵死了，现在都觉得好累。",
            "阿干....不要看我啦!!会影响我心情!!", "说什么呢~~你们不想我赢阿!!!",
            "小赌可以养家活口!!大赌可以兴邦建国!!", "赌是不好的....不赌是万万不行的....", };

    /**
     * NPC对话内容
     * 
     * @param i
     * @param mode
     * @param gamblingNpc
     */
    private void npcChat(final int i, final int mode,
            final GamblingNpc gamblingNpc) {
        final Collection<L1Object> allObj = World.get().getObject();
        for (final L1Object obj : allObj) {
            if (!(obj instanceof L1GamInstance)) {
                continue;
            }
            final L1GamInstance npc = (L1GamInstance) obj;
            // final int npcId = npc.getNpcId();
            switch (mode) {
                case 0:
                    if (_random.nextInt(100) < 20) {
                        // NPC对话
                        String msg = _msg[_random.nextInt(_msg.length)];
                        npc.broadcastPacketX10(new S_NpcChat(npc, msg));
                        //屏掉赌狗场的NPC说公聊
                        /*for (final L1PcInstance listner : World.get()
                                .getAllPlayers()) {
                            // 拒绝接收广播频道
                            if (!listner.isShowWorldChat()) {
                                continue;
                            }
                            listner.sendPackets(new S_KillMessage(npc
                                    .getNameId(), msg, 0));
                        }*/
                    }
                    break;
            }
        }

        for (final L1Object obj : allObj) {
            if (!(obj instanceof L1GamblingInstance)) {
                continue;
            }
            final L1GamblingInstance npc = (L1GamblingInstance) obj;
            final int npcId = npc.getNpcId();

            switch (mode) {
                case 0:
                    // $376 剩余时间： $377分钟！
                    npc.broadcastPacketX10(new S_NpcChatShouting(npc, "$376 "
                            + i + "$377"));
                    break;

                case 1:
                    if (npcId != 91172) {
                        // 秒数倒数
                        npc.broadcastPacketX10(new S_NpcChatShouting(npc,
                                String.valueOf(i)));
                    }
                    break;

                case 2:
                    if (npcId != 91172) {
                        // $363 准备
                        npc.broadcastPacketX10(new S_NpcChatShouting(npc,
                                "$363"));
                    }
                    break;

                case 3:
                    if (npcId != 91172) {
                        // $364 GO！
                        npc.broadcastPacketX10(new S_NpcChatShouting(npc,
                                "$364"));
                    }
                    break;

                case 4:
                    if (npcId != 91172) {
                        // $402 的赔率为
                        final String npcname = gamblingNpc.get_npc()
                                .getNameId();
                        /*
                         * final Double rate = gamblingNpc.get_rate(); String
                         * rates = String.valueOf(rate); final int index =
                         * rates.indexOf("."); if (index != -1) { final int
                         * length = rates.length(); if (length >= (index + 3)) {
                         * rates = rates.substring(0, index + 3);
                         * 
                         * } else { rates = rates.substring(0, index + 2); rates
                         * += "0"; }
                         * 
                         * } else { rates += ".00"; }
                         */
                        npc.broadcastPacketX10(new S_NpcChatShouting(npc,
                                npcname + " $402 5.0"/* + rates */));
                    }
                    break;

                case 5:
                    // $375 第 $366 场比赛的优胜者是
                    final String onename = gamblingNpc.get_npc().getNameId();
                    npc.broadcastPacketX10(new S_NpcChatShouting(npc, "$375 "
                            + _gamblingNo + " $366 " + onename));
                    for (final L1PcInstance listner : World.get()
                    .getAllPlayers()) {
                // 拒绝接收广播频道
                    	if (!listner.isShowWorldChat()) {
                    		continue;
                    	}
                    	listner.sendPackets(new S_KillMessage(npc
                        .getNameId(), ("$375 "+ _gamblingNo + " $366 " + onename), 0));
                    }
                    break;
            }
        }
    }
}
