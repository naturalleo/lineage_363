package com.lineage.server.serverpackets;

import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;

import com.lineage.config.Config;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Account;
import com.lineage.server.world.World;

/**
 * GM管理选单
 * 
 * @author DaiEn
 * 
 */
public class S_PacketBoxGm extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * writeByte(count) writeInt(time) writeString(name) writeString(info):<br>
     * [CALL] ボタンのついたウィンドウが表示される。これはBOTなどの不正者チェックに
     * 使われる机能らしい。名前をダブルクリックするとC_RequestWhoが飞び、クライアントの
     * フォルダにbot_list.txtが生成される。名前を选択して+キーを押すと新しいウィンドウが开く。
     */
    public static final int CALL_SOMETHING = 0x2d;// 45;

    /**
     * GM管理选单
     * 
     * @param srcpc
     *            执行的GM
     * @param mode
     *            模式
     */
    public S_PacketBoxGm(final L1PcInstance srcpc, final int mode) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(CALL_SOMETHING);
        if (srcpc.isGm()) {
            this.callSomething(srcpc, mode);
        }
    }

    private void callSomething(final L1PcInstance srcpc, final int mode) {
        final Iterator<L1PcInstance> itr = World.get().getAllPlayers()
                .iterator();

        // 显示数量
        this.writeC(World.get().getAllPlayers().size());

        while (itr.hasNext()) {
            final L1PcInstance pc = itr.next();
            // final L1Account acc =
            // AccountReading.get().getAccount(pc.getAccountName());
            final L1Account acc = pc.getNetConnection().getAccount();

            if (acc == null) {
                this.writeD(0);

            } else {
                final Calendar cal = Calendar.getInstance(TimeZone
                        .getTimeZone(Config.TIME_ZONE));
                final long lastactive = acc.get_lastactive().getTime();// 最后登入时间
                cal.setTimeInMillis(lastactive);
                cal.set(Calendar.YEAR, 1970);
                final int time = (int) (cal.getTimeInMillis() / 1000);
                this.writeD(time); // JST 1970 1/1 09:00 が基准
            }

            // 人物资料
            this.writeS(mode + ":" + pc.getName());
            // this.writeS(pc.getClanname());
            this.writeS(String.valueOf(pc.getLevel()));
        }

        String xmode = null;
        switch (mode) {
            case 0:// 删除已存人物保留技能
                xmode = "删除已存人物保留技能";
                break;

            case 1:// 移动座标至指定人物身边
                xmode = "移动座标至指定人物身边";
                break;

            case 2:// 召回指定人物
                xmode = "召回指定人物";
                break;

            case 3:// 召回指定队伍
                xmode = "召回指定队伍";
                break;

            case 4:// 全技能
                xmode = "全技能";
                break;

            case 5:// 踢除下线
                xmode = "踢除下线";
                break;

            case 6:// 封锁IP/MAC
                xmode = "封锁IP/MAC";
                break;

            case 7:// 帐号封锁
                xmode = "帐号封锁";
                break;

            case 8:// 杀死指定人物
                xmode = "杀死指定人物";
                break;
        }

        if (xmode != null) {
            srcpc.setTempID(mode);
            srcpc.sendPackets(new S_ServerMessage(166, "请注意目前模式为: " + xmode));
        }
    }

    @Override
    public byte[] getContent() {
        if (this._byte == null) {
            this._byte = this.getBytes();
        }
        return this._byte;
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
