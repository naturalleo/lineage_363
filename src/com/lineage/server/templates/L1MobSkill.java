package com.lineage.server.templates;

public class L1MobSkill implements Cloneable {

    public static final int TYPE_NONE = 0;

    public static final int TYPE_PHYSICAL_ATTACK = 1;// 物理攻击

    public static final int TYPE_MAGIC_ATTACK = 2;// 魔法攻击

    public static final int TYPE_SUMMON = 3;// 召唤属下

    public static final int TYPE_POLY = 4;// 强制变身

    public static final int AHTHARAS_1 = 5;// 群体冲晕

    public static final int AHTHARAS_2 = 6;// 群体相消

    public static final int AHTHARAS_3 = 7;// 群体坏物

    public static final int CHANGE_TARGET_NO = 0;

    public static final int CHANGE_TARGET_COMPANION = 1;

    public static final int CHANGE_TARGET_ME = 2;

    public static final int CHANGE_TARGET_RANDOM = 3;

    private final int skillSize;// 技能数量

    @Override
    public L1MobSkill clone() {
        try {
            return (L1MobSkill) (super.clone());
        } catch (final CloneNotSupportedException e) {
            throw (new InternalError(e.getMessage()));
        }
    }

    /**
     * 技能数量
     * 
     * @return
     */
    public int getSkillSize() {
        return this.skillSize;
    }

    /**
     * 技能数量
     * 
     * @param sSize
     */
    public L1MobSkill(final int sSize) {
        this.skillSize = sSize;

        this.type = new int[this.skillSize];
        this.triRnd = new int[this.skillSize];
        this.triHp = new int[this.skillSize];
        this.triCompanionHp = new int[this.skillSize];
        this.triRange = new int[this.skillSize];
        this.triCount = new int[this.skillSize];
        this.changeTarget = new int[this.skillSize];
        this.range = new int[this.skillSize];
        this.areaWidth = new int[this.skillSize];
        this.areaHeight = new int[this.skillSize];
        this.leverage = new int[this.skillSize];
        this.skillId = new int[this.skillSize];
        this.gfxid = new int[this.skillSize];
        this.actid = new int[this.skillSize];
        this.summon = new int[this.skillSize];
        this.summonMin = new int[this.skillSize];
        this.summonMax = new int[this.skillSize];
        this.polyId = new int[this.skillSize];
    }

    private int mobid;

    /**
     * NPC编号
     * 
     * @return
     */
    public int get_mobid() {
        return this.mobid;
    }

    /**
     * NPC编号
     * 
     * @param i
     */
    public void set_mobid(final int i) {
        this.mobid = i;
    }

    private String mobName;

    /**
     * NPC名称
     * 
     * @return
     */
    public String getMobName() {
        return this.mobName;
    }

    /**
     * NPC名称
     * 
     * @param s
     */
    public void setMobName(final String s) {
        this.mobName = s;
    }

    private int type[];// 技能类型

    /**
     * 技能类型<BR>
     * 1 物理攻击<BR>
     * 2 魔法攻击<BR>
     * 3 召唤属下<BR>
     * 4 强制变身<BR>
     * 5 群体冲晕<BR>
     * 6 群体相消<BR>
     * 7 群体坏物<BR>
     * 
     * @param idx
     * @return
     */
    public int getType(final int idx) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return 0;
        }
        return this.type[idx];
    }

    /**
     * 技能类型<BR>
     * 1 物理攻击<BR>
     * 2 魔法攻击<BR>
     * 3 召唤属下<BR>
     * 4 强制变身<BR>
     * 5 群体冲晕<BR>
     * 6 群体相消<BR>
     * 7 群体坏物<BR>
     * 
     * @param idx
     * @param i
     */
    public void setType(final int idx, final int i) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return;
        }
        this.type[idx] = i;
    }

    private int triRnd[];

    /**
     * 发动机率(%)
     * 
     * @param idx
     * @return
     */
    public int getTriggerRandom(final int idx) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return 0;
        }
        return this.triRnd[idx];
    }

    /**
     * 发动机率(%)
     * 
     * @param idx
     * @param i
     */
    public void setTriggerRandom(final int idx, final int i) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return;
        }
        this.triRnd[idx] = i;
    }

    int triHp[];

    /**
     * HP条件发动(低于设定值)
     * 
     * @param idx
     * @return
     */
    public int getTriggerHp(final int idx) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return 0;
        }
        return this.triHp[idx];
    }

    /**
     * HP条件发动(HP低于设定值)
     * 
     * @param idx
     * @param i
     */
    public void setTriggerHp(final int idx, final int i) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return;
        }
        this.triHp[idx] = i;
    }

    int triCompanionHp[];

    /**
     * 同族HP条件发动(同族HP低于设定值)
     * 
     * @param idx
     * @return
     */
    public int getTriggerCompanionHp(final int idx) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return 0;
        }
        return this.triCompanionHp[idx];
    }

    /**
     * 同族HP条件发动(同族HP低于设定值)
     * 
     * @param idx
     * @param i
     */
    public void setTriggerCompanionHp(final int idx, final int i) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return;
        }
        this.triCompanionHp[idx] = i;
    }

    int triRange[];

    /**
     * 设定值小于0 则小于设定距离(转正整数)发动技能<BR>
     * 设定值大于0 则超出设定距离发动技能
     * 
     * @param idx
     * @return
     */
    public int getTriggerRange(final int idx) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return 0;
        }
        Math.abs(idx);
        return this.triRange[idx];
    }

    /**
     * 设定值小于0 则小于设定距离(转正整数)发动技能<BR>
     * 设定值大于0 则超出设定距离发动技能
     * 
     * @param idx
     * @param i
     */
    public void setTriggerRange(final int idx, final int i) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return;
        }
        this.triRange[idx] = i;
    }

    /**
     * 物件距离是否达成施展技能距离
     * 
     * @param idx
     * @param distance
     * @return
     */
    public boolean isTriggerDistance(final int idx, final int distance) {
        final int triggerRange = this.getTriggerRange(idx);

        if (((triggerRange < 0) && (distance <= Math.abs(triggerRange)))
                || ((triggerRange > 0) && (distance >= triggerRange))) {
            return true;
        }
        return false;
    }

    int triCount[];

    /**
     * 技能发动次数
     * 
     * @param idx
     * @return
     */
    public int getTriggerCount(final int idx) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return 0;
        }
        return this.triCount[idx];
    }

    /**
     * 技能发动次数
     * 
     * @param idx
     * @param i
     */
    public void setTriggerCount(final int idx, final int i) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return;
        }
        this.triCount[idx] = i;
    }

    int changeTarget[];

    /**
     * 技能发动时目标判定<BR>
     * 1:目前攻击者<BR>
     * 2:目前攻击自己的对象<BR>
     * 3:范围目标<BR>
     * 
     * @param idx
     * @return
     */
    public int getChangeTarget(final int idx) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return 0;
        }
        return this.changeTarget[idx];
    }

    /**
     * 技能发动时目标判定<BR>
     * 1:目前攻击者<BR>
     * 2:目前攻击自己的对象<BR>
     * 3:范围目标<BR>
     * 
     * @param idx
     * @param i
     */
    public void setChangeTarget(final int idx, final int i) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return;
        }
        this.changeTarget[idx] = i;
    }

    int range[];

    /**
     * 攻击距离(物理攻击设置)<BR>
     * 物理攻击必须设定1以上
     * 
     * @param idx
     * @return
     */
    public int getRange(final int idx) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return 0;
        }
        return this.range[idx];
    }

    /**
     * 攻击距离(物理攻击设置)<BR>
     * 物理攻击必须设定1以上
     * 
     * @param idx
     * @param i
     */
    public void setRange(final int idx, final int i) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return;
        }
        this.range[idx] = i;
    }

    /*
     * 范围攻击の横幅、单体攻击ならば0を设定、范围攻击するならば0以上を设定
     * WidthとHeightの设定は攻击者からみて横幅をWidth、奥行きをHeightとする。
     * Widthは+-あるので、1を指定すれば、ターゲットを中心として左右1までが对象となる。
     */
    int areaWidth[];

    /**
     * 攻击范围(物理攻击设置)<BR>
     * 单体攻击设置0<BR>
     * 范围攻击必须设定1以上
     * 
     * @param idx
     * @return
     */
    public int getAreaWidth(final int idx) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return 0;
        }
        return this.areaWidth[idx];
    }

    /**
     * 攻击范围(物理攻击设置)<BR>
     * 单体攻击设置0<BR>
     * 范围攻击必须设定1以上
     * 
     * @param idx
     * @param i
     */
    public void setAreaWidth(final int idx, final int i) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return;
        }
        this.areaWidth[idx] = i;
    }

    /*
     * 范围攻击の高さ、单体攻击ならば0を设定、范围攻击するならば1以上を设定
     */
    int areaHeight[];

    /**
     * 攻击范围(物理攻击设置)<BR>
     * 单体攻击设置0<BR>
     * 范围攻击必须设定1以上
     * 
     * @param idx
     * @return
     */
    public int getAreaHeight(final int idx) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return 0;
        }
        return this.areaHeight[idx];
    }

    /**
     * 攻击范围(物理攻击设置)<BR>
     * 单体攻击设置0<BR>
     * 范围攻击必须设定1以上
     * 
     * @param idx
     * @param i
     */
    public void setAreaHeight(final int idx, final int i) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return;
        }
        this.areaHeight[idx] = i;
    }

    int leverage[];

    /**
     * 攻击倍率(1/10)
     * 
     * @param idx
     * @return
     */
    public int getLeverage(final int idx) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return 0;
        }
        return this.leverage[idx];
    }

    /**
     * 攻击倍率(1/10)
     * 
     * @param idx
     * @param i
     */
    public void setLeverage(final int idx, final int i) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return;
        }
        this.leverage[idx] = i;
    }

    int skillId[];

    /**
     * 对应魔法技能编号
     * 
     * @param idx
     * @return
     */
    public int getSkillId(final int idx) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return 0;
        }
        return this.skillId[idx];
    }

    /**
     * 对应魔法技能编号
     * 
     * @param idx
     * @param i
     */
    public void setSkillId(final int idx, final int i) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return;
        }
        this.skillId[idx] = i;
    }

    int gfxid[];

    /**
     * 物理攻击使用的技能动画
     * 
     * @param idx
     * @return
     */
    public int getGfxid(final int idx) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return 0;
        }
        return this.gfxid[idx];
    }

    /**
     * 物理攻击使用的技能动画
     * 
     * @param idx
     * @param i
     */
    public void setGfxid(final int idx, final int i) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return;
        }
        this.gfxid[idx] = i;
    }

    int actid[];

    /**
     * 物理攻击使用的动作编号
     * 
     * @param idx
     * @return
     */
    public int getActid(final int idx) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return 0;
        }
        return this.actid[idx];
    }

    /**
     * 物理攻击使用的动作编号
     * 
     * @param idx
     * @param i
     */
    public void setActid(final int idx, final int i) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return;
        }
        this.actid[idx] = i;
    }

    int summon[];

    /**
     * 召唤技能使用属下编号
     * 
     * @param idx
     * @return
     */
    public int getSummon(final int idx) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return 0;
        }
        return this.summon[idx];
    }

    /**
     * 召唤技能使用属下编号
     * 
     * @param idx
     * @param i
     */
    public void setSummon(final int idx, final int i) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return;
        }
        this.summon[idx] = i;
    }

    int summonMin[];

    /**
     * 召唤最小数量
     * 
     * @param idx
     * @return
     */
    public int getSummonMin(final int idx) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return 0;
        }
        return this.summonMin[idx];
    }

    /**
     * 召唤最小数量
     * 
     * @param idx
     * @param i
     */
    public void setSummonMin(final int idx, final int i) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return;
        }
        this.summonMin[idx] = i;
    }

    int summonMax[];

    /**
     * 召唤最大数量
     * 
     * @param idx
     * @return
     */
    public int getSummonMax(final int idx) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return 0;
        }
        return this.summonMax[idx];
    }

    /**
     * 召唤最大数量
     * 
     * @param idx
     * @param i
     */
    public void setSummonMax(final int idx, final int i) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return;
        }
        this.summonMax[idx] = i;
    }

    int polyId[];

    /**
     * 强制变身代号
     * 
     * @param idx
     * @return
     */
    public int getPolyId(final int idx) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return 0;
        }
        return this.polyId[idx];
    }

    /**
     * 强制变身代号
     * 
     * @param idx
     * @param i
     */
    public void setPolyId(final int idx, final int i) {
        if ((idx < 0) || (idx >= this.getSkillSize())) {
            return;
        }
        this.polyId[idx] = i;
    }
}
