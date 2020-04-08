package com.lineage.server.serverpackets;

/**
 * スキルアイコンや遮断リストの表示など复数の用途に使われるパケットのクラス
 */
public class S_PacketBox extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * writeByte(id) writeShort(?): <font color=#00800>(639) %s的攻城战开始。 </font><BR>
     * 1:Kent 2:Orc 3:WW 4:Giran 5:Heine 6:Dwarf 7:Aden 8:Diad 9:城名9 ...
     */
    // public static final int MSG_WAR_BEGIN = 0;

    /**
     * writeByte(id) writeShort(?): <font color=#00800>(640) %s的攻城战结束。 </font>
     */
    // public static final int MSG_WAR_END = 1;

    /**
     * writeByte(id) writeShort(?): <font color=#00800>(641) %s的攻城战正在进行中。
     * </font>
     */
    // public static final int MSG_WAR_GOING = 2;

    /**
     * <font color=#00800>(642) 已掌握了城堡的主导权。 </font>
     */
    // public static final int MSG_WAR_INITIATIVE = 3;

    /**
     * <font color=#00800>(643) 已占领城堡。</font>
     */
    // public static final int MSG_WAR_OCCUPY = 4;

    /**
     * <font color=#00800>(646) 结束决斗。 </font>
     */
    public static final int MSG_DUEL = 5;

    /**
     * writeByte(count): <font color=#00800>(648) 您没有发送出任何简讯。 </font>
     */
    public static final int MSG_SMS_SENT = 6;

    /**
     * <font color=#00800>(790) 俩人的结婚在所有人的祝福下完成 </font>
     */
    public static final int MSG_MARRIED = 9;

    /**
     * writeByte(weight): <font color=#00800>重量(30段阶) </font>
     */
    public static final int WEIGHT = 10;

    /**
     * writeByte(food): <font color=#00800>满腹度(30段阶) </font>
     */
    public static final int FOOD = 11;

    /**
     * <font color=#00800>UB情报HTML </font>
     */
    public static final int HTML_UB = 14;

    /**
     * writeByte(id)<br>
     * <font color=#00800> 1: (978) 感觉到在身上有的精灵力量被空气中融化。<br>
     * 2: (679) 忽然全身充满了%s的灵力。 680 火<br>
     * 3: (679) 忽然全身充满了%s的灵力。 681 水<br>
     * 4: (679) 忽然全身充满了%s的灵力。 682 风<br>
     * 5: (679) 忽然全身充满了%s的灵力。 683 地<br>
     * </font>
     */
    public static final int MSG_ELF = 15;

    /**
     * writeByte(count) S(name)...: <font color=#00800>开启拒绝名单 :</font>
     */
    public static final int ADD_EXCLUDE2 = 17;// 17

    /**
     * writeString(name): <font color=#00800>增加到拒绝名单</font>
     */
    public static final int ADD_EXCLUDE = 18;// 18

    /**
     * writeString(name): <font color=#00800>移除出拒绝名单</font>
     */
    public static final int REM_EXCLUDE = 19;// 19

    /** 技能图示 */
    public static final int ICONS1 = 20;// 0x14

    /** 技能图示 */
    public static final int ICONS2 = 21;// 0x15

    /** 技能图示 */
    public static final int ICON_AURA = 22;// 0x16

    /**
     * writeString(name): <font color=#00800>(764) 新村长由%s选出</font>
     */
    public static final int MSG_TOWN_LEADER = 23;

    /**
     * writeByte(id): <font color=#00800>联盟职位变更</font><br>
     * id - 1:见习 2:一般 3:守护骑士
     */
    public static final int MSG_RANK_CHANGED = 27;

    /**
     * <font color=#00800>血盟线上人数(HTML)</font>
     */
    public static final int MSG_CLANUSER = 29;

    /**
     * writeInt(?) writeString(name) writeString(clanname):<br>
     * <font color=#00800>(782) %s 血盟的 %s打败了反王<br>
     * (783) %s 血盟成为新主人。 </font>
     */
    // public static final int MSG_WIN_LASTAVARD = 30;

    /**
     * <font color=#00800>(77) \f1你觉得舒服多了。</font>
     */
    public static final int MSG_FEEL_GOOD = 31;

    /** 不明。 客户端会传回一个封包 */
    // INFO - Not Set OP ID: 40
    // 0000: 28 58 02 00 00 fe b2 d4 c6 00 00 00 00 00 00 00 (X..............
    public static final int SOMETHING1 = 33;

    /**
     * writeShort(time): <font color=#00800>蓝水图示</font>
     */
    public static final int ICON_BLUEPOTION = 34;// 34

    /**
     * writeShort(time): <font color=#00800>变身图示</font>
     */
    public static final int ICON_POLYMORPH = 35;// 35

    /**
     * writeShort(time): <font color=#00800>禁言图示 </font>
     */
    public static final int ICON_CHATBAN = 36;// 36

    /** 不明。C_7パケットが飞ぶ。C_7はペットのメニューを开いたときにも飞ぶ。 */
    public static final int SOMETHING2 = 37;

    /**
     * <font color=#00800>血盟成员清单(HTML)</font>
     */
    public static final int HTML_CLAN1 = 38;

    /** writeShort(time): 圣结界图示 */
    public static final int ICON_I2H = 40;

    /**
     * <font color=#00800>更新角色使用的快速键</font>
     */
    public static final int CHARACTER_CONFIG = 41;// 41

    /**
     * <font color=#00800>角色选择视窗</font> > 0000 : 39 2a e1 88 08 12 48 fa
     * 9*....H.
     */
    public static final int LOGOUT = 42;// 42

    /**
     * <font color=#00800>(130) \f1战斗中，无法重新开始。</font>
     */
    public static final int MSG_CANT_LOGOUT = 43;

    /**
     * <font color=#00800>风之枷锁</font>
     */
    // public static final int WIND_SHACKLE = 44;

    /**
     * writeByte(count) writeInt(time) writeString(name) writeString(info):<br>
     * [CALL] ボタンのついたウィンドウが表示される。これはBOTなどの不正者チェックに
     * 使われる机能らしい。名前をダブルクリックするとC_RequestWhoが飞び、クライアントの
     * フォルダにbot_list.txtが生成される。名前を选択して+キーを押すと新しいウィンドウが开く。
     */
    // public static final int CALL_SOMETHING = 45;

    /**
     * <font color=#00800>writeByte(id): 大圆形竞技场，混沌的大战<br>
     * id - 1:开始(1045) 2:取消(1047) 3:结束(1046)</font>
     */
    public static final int MSG_COLOSSEUM = 49;

    /**
     * <font color=#00800>血盟情报(HTML)</font>
     */
    public static final int HTML_CLAN2 = 51;

    /**
     * <font color=#00800>料理选单</font>
     */
    // public static final int COOK_WINDOW = 52;

    /** writeByte(type) writeShort(time): 料理アイコンが表示される */
    // public static final int ICON_COOKING = 53;

    /** 鱼上钩的图形表示 */
    public static final int FISHING = 55;

    /** 魔法娃娃图示 */
    public static final int DOLL = 56;

    /** 慎重药水 */
    public static final int WISDOM_POTION = 57;

    /** 同盟目录 */
    public static final int CLAN = 62;

    /** 比赛视窗(倒数开始) */
    // public static final int GAMESTART = 64;

    /** 开始正向计时 */
    // public static final int TIMESTART = 65;

    /** 显示资讯 */
    // public static final int GAMEINFO = 66;

    /** 比赛视窗(倒数结束/停止计时) */
    // public static final int GAMEOVER = 69;

    /** 移除比赛视窗 */
    // public static final int GAMECLEAR = 70;

    /** 开始反向计时 */
    // public static final int STARTTIME = 71;

    /** 移除开始反向计时视窗 */
    // public static final int STARTTIMECLEAR = 72;

    /** 手臂受伤攻击力下降(350秒) */
    public static final int DMG = 74;

    /** 对方开始显示弱点(16秒) */
    public static final int TGDMG = 75;

    /** 攻城战结束讯息 */
    // public static final int MSG_WAR_OVER = 79;

    /** 1,550：受到殷海萨的祝福，增加了些许的狩猎经验值。 */
    public static final int LEAVES = 82;

    /** 不明 绿色叶子 */
    // public static final int LEAVES200 = 87;
    /** 技能图示:龙之血痕. */
    public static final int ICON_BLOOD_STAIN = 100;
    /** 显示龙座标选单 */
    public static final int DRAGON = 101;

	/** VIP显示图片 by:42621391 2014年8月20日09:04:01 */
	public static final int BAPO = 114; // 官器标惯
    /** 记忆坐标对话框相关 提示信息：最大记忆空间扩大10个. */
    public static final int COORDINATES_SPACE_TO_EXPAND = 141;
    /** H(time)：显示限时地图左上角计时器剩余时间[单位:秒]. */
    public static final int MAP_TIMER = 153;
    /** 左上角显示：回合0/6. */
    public static final int ROUND_NUMBER = 156;
    /** 退出游戏界面 限时地图剩余时间. */
    public static final int WINDOW_MAP_TIME = 159;

	/** VIP显示图片 by:42621391 2014年8月20日09:04:58 */
	public S_PacketBox(int subCode, int type, boolean show) {

		writeC(S_OPCODE_PACKETBOX);
		writeC(subCode);

		switch (subCode) {
		case BAPO:
			writeD(type); // 1~7 标惯
			writeD(show ? 0x01 : 0x00);
		default:
			break;
		}
	}
    /**
     * 显示限时地图左上角计时器剩余时间[单位:秒].
     */
    public S_PacketBox(final int subCode, final int value, final String msg) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(MAP_TIMER);
        this.writeH(value);
    }

    /**
     * 殷海萨的祝福
     * 
     * @param subCode
     * @param msg
     * @param value
     */
    public S_PacketBox(final int subCode, final String msg, final int value) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(LEAVES);
        this.writeS(msg);
        this.writeH(value);
    }

    public S_PacketBox(final int subCode) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(subCode);

        switch (subCode) {
        // case MSG_WAR_INITIATIVE:
        // case MSG_WAR_OCCUPY:
            case MSG_MARRIED:
            case MSG_FEEL_GOOD:
            case MSG_CANT_LOGOUT:
            case LOGOUT:
            case FISHING:
                break;

            /*
             * case CALL_SOMETHING: this.callSomething();
             */
            default:
                break;
        }
    }

    public S_PacketBox(final int subCode, final int value) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(subCode);

        switch (subCode) {
            case DMG:
            case TGDMG:
                final int time = value >> 2;
                this.writeC(time); // time >> 2
                break;

            case ICON_BLUEPOTION:
            case ICON_CHATBAN:
            case ICON_I2H:
            case ICON_POLYMORPH:
                this.writeH(value); // time
                break;

            /*
             * case MSG_WAR_BEGIN: case MSG_WAR_END: case MSG_WAR_GOING:
             * this.writeC(value); // castle id this.writeH(0); // ? break;
             */

            case WISDOM_POTION:
                this.writeC(0x2c);
                this.writeH(value); // time
                break;

            case MSG_SMS_SENT:
            case WEIGHT:
            case FOOD:
                this.writeC(value);
                break;

            case MSG_ELF:
            case MSG_RANK_CHANGED:
            case MSG_COLOSSEUM:
                this.writeC(value); // msg id
                break;

            /*
             * case MSG_LEVEL_OVER: writeC(0); // ? writeC(value); //
             * 0-49以外は表示されない break;
             */

            /*
             * case COOK_WINDOW: this.writeC(0xdb); // ? this.writeC(0x31);
             * this.writeC(0xdf); this.writeC(0x02); this.writeC(0x01);
             * this.writeC(value); // level break;
             */

            case DOLL:
                this.writeH(value);
                break;

            default:
                // this.writeH(value); // time
                break;
        }
    }

    public S_PacketBox(final int subCode, final int type, final int time) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(subCode);

        switch (subCode) {
        /*
         * case ICON_COOKING: if (type != 7) { this.writeC(0x0c);
         * this.writeC(0x0c); this.writeC(0x0c); this.writeC(0x12);
         * this.writeC(0x0c); this.writeC(0x09); this.writeC(0x00);
         * this.writeC(0x00); this.writeC(type); this.writeC(0x24);
         * this.writeH(time); this.writeH(0x00);
         * 
         * } else { this.writeC(0x0c); this.writeC(0x0c); this.writeC(0x0c);
         * this.writeC(0x12); this.writeC(0x0c); this.writeC(0x09);
         * this.writeC(0xc8); this.writeC(0x00); this.writeC(type);
         * this.writeC(0x26); this.writeH(time); this.writeC(0x3e);
         * this.writeC(0x87); } break;
         */

            case MSG_DUEL:
                this.writeD(type); // 相手のオブジェクトID
                this.writeD(time); // 自分のオブジェクトID
                break;

            default:
                break;
        }
    }

    public S_PacketBox(final int subCode, final String name) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(subCode);

        switch (subCode) {
            case ADD_EXCLUDE:
            case REM_EXCLUDE:
            case MSG_TOWN_LEADER:
                this.writeS(name);
                break;
            default:
                break;
        }
    }

    public S_PacketBox(final int subCode, final Object[] names) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(subCode);

        switch (subCode) {
            case ADD_EXCLUDE2:
                this.writeC(names.length);
                for (final Object name : names) {
                    this.writeS(name.toString());
                }
                break;

            default:
                break;
        }
    }

    /*
     * private void callSomething() { final Iterator<L1PcInstance> itr =
     * World.get().getAllPlayers().iterator();
     * 
     * this.writeC(World.get().getAllPlayers().size());
     * 
     * while (itr.hasNext()) { final L1PcInstance pc = itr.next(); final
     * L1Account acc = AccountReading.get().getAccount(pc.getAccountName());
     * 
     * // 时间情报 とりあえずログイン时间を入れてみる if (acc == null) { this.writeD(0); } else {
     * final Calendar cal =
     * Calendar.getInstance(TimeZone.getTimeZone(Config.TIME_ZONE)); final long
     * lastactive = acc.get_lastactive().getTime();
     * cal.setTimeInMillis(lastactive); cal.set(Calendar.YEAR, 1970); final int
     * time = (int) (cal.getTimeInMillis() / 1000); this.writeD(time); // JST
     * 1970 1/1 09:00 が基准 }
     * 
     * // キャラ情报 this.writeS(pc.getName()); // 半角12字まで
     * this.writeS(pc.getClanname()); // []内に表示される文字列。半角12字まで } }
     */

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
