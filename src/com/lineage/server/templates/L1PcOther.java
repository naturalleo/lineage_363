package com.lineage.server.templates;

import java.util.HashMap;
import java.util.Map;
import com.lineage.server.command.GmHtml;
import com.lineage.server.model.Instance.L1ItemInstance;

/**
 * 人物其他相关设置表
 * 
 * @author DaiEn
 * 
 */
public class L1PcOther {

    private static final Map<Integer, StringBuilder> _titleList = new HashMap<Integer, StringBuilder>();

    private static boolean _isStart = false;

    public static final int CLEVLE0 = 1;
    public static final int CLEVLE1 = 2;
    public static final int CLEVLE2 = 4;
    public static final int CLEVLE3 = 8;
    public static final int CLEVLE4 = 16;
    public static final int CLEVLE5 = 32;
    public static final int CLEVLE6 = 64;
    public static final int CLEVLE7 = 128;
    public static final int CLEVLE8 = 256;
    public static final int CLEVLE9 = 512;
    public static final int CLEVLE10 = 1024;

    // 测试防天变
    public static final String[] ADDNAME = new String[] { "あ", "ア", "い", "イ",
            "う", "ウ", "え", "エ", "お", "オ", "か", "カ", "き", "キ", "く", "ク", "け",
            "ケ", "こ", "コ", "さ", "サ", "し", "シ", "す", "ス", "せ", "セ", "そ", "ソ",
            "た", "タ", "ち", "チ", "つ", "ツ", "て", "テ", "と", "ト", "な", "ナ", "に",
            "ニ", "ぬ", "ヌ", "ね", "ネ", "の", "ノ", "は", "ハ", "ひ", "ヒ", "ふ", "フ",
            "へ", "ヘ", "ほ", "ホ", "ま", "マ", "み", "ミ", "む", "ム", "め", "メ", "も",
            "モ", "や", "ヤ", "ゆ", "ユ", "よ", "ヨ", "ら", "ラ", "り", "リ", "る", "ル",
            "れ", "レ", "ろ", "ロ", "わ", "ワ", "を", "ヲ", "ん", "ン", };

    public static void load() {
        if (!_isStart) {

            _titleList.put(CLEVLE0, new StringBuilder(""));
            _titleList.put(CLEVLE1, new StringBuilder("\\fD"));// 浅蓝\fD
            _titleList.put(CLEVLE2, new StringBuilder("\\f="));// 黄色\f=
            _titleList.put(CLEVLE3, new StringBuilder("\\fH"));// 米黄\fH
            _titleList.put(CLEVLE4, new StringBuilder("\\f_"));// 粉红\f_
            _titleList.put(CLEVLE5, new StringBuilder("\\f2"));// 亮绿\f2
            _titleList.put(CLEVLE6, new StringBuilder("\\fF"));// 深绿\fF
            _titleList.put(CLEVLE7, new StringBuilder("\\fT"));// 淡绿\fT
            _titleList.put(CLEVLE8, new StringBuilder("\\fE"));// 暗绿\fE
            _titleList.put(CLEVLE9, new StringBuilder("\\f0"));// 黑色\f0
            _titleList.put(CLEVLE10, new StringBuilder("\\f?"));// 浅灰\f?

            /*
             * \\f1 蓝色 \\f2 亮绿 \\f3 红色 \\f4 紫色 \\f5 紫红 \\f6 暗红 \\f7 灰色 \\f8 深灰
             * \\f9 暗灰 \\f0 黑色
             */

            _isStart = true;
        }
    }

    //private L1ItemInstance _item = null;
    private int _item_objid;
    /**
     * 物品暂存
     * 
     * @param item
     */
//    public void set_item(final L1ItemInstance item) {
//        _item = item;
//    }
    
    /**
     * 物品objID暂存
     * @param objid
     */
    public void set_item_objid(final int item_objid) {
    	_item_objid = item_objid;
    }

    /**
     * 物品暂存
     * 
     * @return
     */
//    public L1ItemInstance get_item() {
//        return _item;
//    }
    
    /**
     * 物品ojid 暂存
     * @return
     */
    public int get_item_objid() {
    	return _item_objid;
    }
    

    private boolean _shopSkill = false;

    /**
     * true = 全职技能导师
     * 
     * @param shopSkill
     */
    public void set_shopSkill(final boolean shopSkill) {
        _shopSkill = shopSkill;
    }

    /**
     * true = 全职技能导师
     * 
     * @return
     */
    public boolean get_shopSkill() {
        return _shopSkill;
    }

    // 所有人
    private int _objid = 0;

    public void set_objid(final int objid) {
        _objid = objid;
    }

    public int get_objid() {
        return _objid;
    }

    // 翻页
    private int _page = 0;

    public void set_page(final int page) {
        _page = page;
    }

    public int get_page() {
        return _page;
    }

    // 计时地图编号
    private int _usemap = 0;

    /**
     * 计时地图编号
     * 
     * @param usemap
     */
    public void set_usemap(final int usemap) {
        _usemap = usemap;
    }

    /**
     * 计时地图编号
     * 
     * @return
     */
    public int get_usemap() {
        return _usemap;
    }

    // 计时地图可用时间
    private int _usemapTime = 0;

    /**
     * 计时地图可用时间
     * 
     * @param usemapTime
     */
    public void set_usemapTime(final int usemapTime) {
        _usemapTime = usemapTime;
    }

    /**
     * 计时地图可用时间
     * 
     * @return
     */
    public int get_usemapTime() {
        return _usemapTime;
    }

    // 玩家使用道具已增加的HP值
    private int _addhp = 0;

    /**
     * 玩家使用道具已增加的HP值
     * 
     * @param addhp
     */
    public void set_addhp(int addhp) {
        if (addhp < 0) {
            addhp = 0;
        }
        _addhp = addhp;
    }

    /**
     * 玩家使用道具已增加的HP值
     * 
     * @return
     */
    public int get_addhp() {
        return _addhp;
    }

    // 玩家使用道具已增加的MP值
    private int _addmp = 0;

    /**
     * 玩家使用道具已增加的MP值
     * 
     * @param addmp
     */
    public void set_addmp(int addmp) {
        if (addmp < 0) {
            addmp = 0;
        }
        _addmp = addmp;
    }

    /**
     * 玩家使用道具已增加的MP值
     * 
     * @return
     */
    public int get_addmp() {
        return _addmp;
    }

    // 人物积分
    private int _score = 0;

    /**
     * 设置积分
     * 
     * @param score
     */
    public void set_score(int score) {
        if (score < 0) {
            score = 0;
        }
        _score = score;
    }

    /**
     * 增加积分
     * 
     * @param score
     */
    public void add_score(int score) {
        if (score < 0) {
            score = 0;
        }
        _score += score;
        if (_score < 0) {
            _score = 0;
        }
    }

    /**
     * 传回积分
     * 
     * @return
     */
    public int get_score() {
        return _score;
    }

    // 名称色彩
    private int _color = CLEVLE0;

    /**
     * 名称色彩
     * 
     * @param color
     */
    public void set_color(final int color) {
        _color = color;
    }

    /**
     * 名称色彩编号
     * 
     * @param color
     * @param tg
     * @return
     */
    public boolean set_color(final int color, final int tg) {
        set_score(tg);
        _color = color;
        return true;
    }

    /**
     * 传回名称色彩编号
     * 
     * @return
     */
    public int get_color() {
        return _color;
    }

    /**
     * 名称色彩文字代号
     * 
     * @return
     */
    public String color() {
        StringBuilder stringBuilder = _titleList.get(_color);
        if (stringBuilder != null) {
            return stringBuilder.toString();
        }
        return "";
    }

    // 血盟技能
    private int _clanskill = 0;

    /**
     * 设置血盟技能
     * 
     * @param clanskill
     * <BR>
     *            0:无<BR>
     *            1:狂暴(增加物理攻击力)<BR>
     *            2:寂静(增加物理伤害减免)<BR>
     *            4:魔击(增加魔法攻击力)<BR>
     *            8:消魔(增加魔法伤害减免)<BR>
     */
    public void set_clanskill(int clanskill) {
        _clanskill = clanskill;
    }

    /**
     * 传回血盟技能
     * 
     * @return<BR> 0:无<BR>
     *             1:狂暴(增加物理攻击力)<BR>
     *             2:寂静(增加物理伤害减免)<BR>
     *             4:魔击(增加魔法攻击力)<BR>
     *             8:消魔(增加魔法伤害减免)<BR>
     */
    public int get_clanskill() {
        return _clanskill;
    }

    // 杀人次数
    private int _killCount = 0;

    /**
     * 增加杀人次数
     * 
     * @param i
     */
    public void add_killCount(int i) {
        _killCount += i;
    }

    /**
     * 设置杀人次数
     * 
     * @param i
     */
    public void set_killCount(int i) {
        _killCount = i;
    }

    /**
     * 传回杀人次数
     */
    public int get_killCount() {
        return _killCount;
    }

    // 被杀次数
    private int _deathCount = 0;

    /**
     * 增加被杀次数
     * 
     * @param i
     */
    public void add_deathCount(int i) {
        _deathCount += i;
    }

    /**
     * 设置被杀次数
     * 
     * @param i
     */
    public void set_deathCount(int i) {
        _deathCount = i;
    }

    /**
     * 传回被杀次数
     */
    public int get_deathCount() {
        return _deathCount;
    }

    // 使用GM HTML选单
    private GmHtml _gmHtml = null;

    /**
     * 使用GM HTML选单
     * 
     * @return
     */
    public GmHtml get_gmHtml() {
        return _gmHtml;
    }

    /**
     * 设置使用GM HTML选单
     * 
     * @return
     */
    public void set_gmHtml(GmHtml gmHtml) {
        _gmHtml = gmHtml;
    }
}
