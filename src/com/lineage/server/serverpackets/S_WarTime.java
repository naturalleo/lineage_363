package com.lineage.server.serverpackets;

import java.util.Calendar;

import com.lineage.config.Config;
import com.lineage.server.datatables.lock.CastleReading;
import com.lineage.server.templates.L1Castle;

/**
 * 围城时间设定
 * 
 * @author dexc
 * 
 */
public class S_WarTime extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 围城时间设定
     * 
     * @param cal
     */
    public S_WarTime(final Calendar cal) {
        // 1997/01/01 17:00を基点としている
        final Calendar base_cal = Calendar.getInstance();
        base_cal.set(1997, 0, 1, 17, 0);
        final long base_millis = base_cal.getTimeInMillis();
        final long millis = cal.getTimeInMillis();
        long diff = millis - base_millis;
        diff -= 1200 * 60 * 1000; // 误差修正
        diff = diff / 60000; // 分以下切舍て
        // timeは1加算すると3:02（182分）进む
        final int time = (int) (diff / 182);

        // writeDの直前のwriteCで时间の调节ができる
        // 0.7倍した时间だけ缩まるが
        // 1つ调整するとその次の时间が广がる？
        this.writeC(S_OPCODE_WARTIME);
        this.writeH(0x0006); // リストの数（6以上は无效）
        this.writeS(Config.TIME_ZONE); // 时间の后ろの（）内に表示される文字列
        this.writeC(0x00); // ?
        this.writeC(0x00); // ?
        this.writeC(0x00);
        this.writeD(time);
        this.writeC(0x00);
        this.writeD(time - 1);
        this.writeC(0x00);
        this.writeD(time - 2);
        this.writeC(0x00);
        this.writeD(time - 3);
        this.writeC(0x00);
        this.writeD(time - 4);
        this.writeC(0x00);
        this.writeD(time - 5);
        this.writeC(0x00);
    }

    /**
     * 围城时间设定 - 测试
     * 
     * @param cal
     */
    public S_WarTime(int op) {
        final L1Castle l1castle = CastleReading.get().getCastleTable(5);// 5 海音城
        final Calendar cal = l1castle.getWarTime();
        // 1997/01/01 17:00を基点としている
        final Calendar base_cal = Calendar.getInstance();
        base_cal.set(1997, 0, 1, 17, 0);
        final long base_millis = base_cal.getTimeInMillis();
        final long millis = cal.getTimeInMillis();
        long diff = millis - base_millis;
        diff -= 1200 * 60 * 1000; // 误差修正
        diff = diff / 60000; // 分以下切舍て
        // timeは1加算すると3:02（182分）进む
        final int time = (int) (diff / 182);

        this.writeC(op);
        this.writeH(6); // リストの数（6以上は无效）
        this.writeS(Config.TIME_ZONE); // 时间の后ろの（）内に表示される文字列
        this.writeC(0); // ?
        this.writeC(0); // ?
        this.writeC(0);
        this.writeD(time);
        this.writeC(0);
        this.writeD(time - 1);
        this.writeC(0);
        this.writeD(time - 2);
        this.writeC(0);
        this.writeD(time - 3);
        this.writeC(0);
        this.writeD(time - 4);
        this.writeC(0);
        this.writeD(time - 5);
        this.writeC(0);

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
