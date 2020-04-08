package com.lineage.server.templates;

public class L1Skills {

    public static final int ATTR_NONE = 0;

    public static final int ATTR_EARTH = 1;

    public static final int ATTR_FIRE = 2;

    public static final int ATTR_WATER = 4;

    public static final int ATTR_WIND = 8;

    public static final int ATTR_RAY = 16;

    public static final int TYPE_PROBABILITY = 1;

    public static final int TYPE_CHANGE = 2;

    public static final int TYPE_CURSE = 4;

    public static final int TYPE_DEATH = 8;

    public static final int TYPE_HEAL = 16;

    public static final int TYPE_RESTORE = 32;

    public static final int TYPE_ATTACK = 64;

    public static final int TYPE_OTHER = 128;

    /** 技能对自己 */
    public static final int TARGET_TO_ME = 0;

    /** 技能对人物 */
    public static final int TARGET_TO_PC = 1;

    /** 技能对NPC */
    public static final int TARGET_TO_NPC = 2;

    /** 技能对血盟 */
    public static final int TARGET_TO_CLAN = 4;

    /** 技能对队伍 */
    public static final int TARGET_TO_PARTY = 8;

    /** 技能对宠物 */
    public static final int TARGET_TO_PET = 16;

    /** 技能对地点 */
    public static final int TARGET_TO_PLACE = 32;

    private int _skillId;

    public int getSkillId() {
        return this._skillId;
    }

    public void setSkillId(final int i) {
        this._skillId = i;
    }

    private String _name;

    public String getName() {
        return this._name;
    }

    public void setName(final String s) {
        this._name = s;
    }

    private int _skillLevel;

    /**
     * 技能分级
     * 
     * @return
     */
    public int getSkillLevel() {
        return this._skillLevel;
    }

    /**
     * 技能分级
     * 
     * @param i
     */
    public void setSkillLevel(final int i) {
        this._skillLevel = i;
    }

    private int _skillNumber;

    public int getSkillNumber() {
        return this._skillNumber;
    }

    public void setSkillNumber(final int i) {
        this._skillNumber = i;
    }

    private int _mpConsume;

    public int getMpConsume() {
        return this._mpConsume;
    }

    public void setMpConsume(final int i) {
        this._mpConsume = i;
    }

    private int _hpConsume;

    public int getHpConsume() {
        return this._hpConsume;
    }

    public void setHpConsume(final int i) {
        this._hpConsume = i;
    }

    private int _itmeConsumeId;

    public int getItemConsumeId() {
        return this._itmeConsumeId;
    }

    public void setItemConsumeId(final int i) {
        this._itmeConsumeId = i;
    }

    private int _itmeConsumeCount;

    public int getItemConsumeCount() {
        return this._itmeConsumeCount;
    }

    public void setItemConsumeCount(final int i) {
        this._itmeConsumeCount = i;
    }

    private int _reuseDelay; // 单位：ミリ秒

    public int getReuseDelay() {
        return this._reuseDelay;
    }

    public void setReuseDelay(final int i) {
        this._reuseDelay = i;
    }

    private int _buffDuration; // 效果时间(单位:秒)

    /**
     * 效果时间(单位:秒)
     * 
     * @return
     */
    public int getBuffDuration() {
        return this._buffDuration;
    }

    /**
     * 效果时间(单位:秒)
     * 
     * @param i
     */
    public void setBuffDuration(final int i) {
        this._buffDuration = i;
    }

    private String _target;

    public String getTarget() {
        return this._target;
    }

    public void setTarget(final String s) {
        this._target = s;
    }

    private int _targetTo; // 0:自己 1:玩家 2:NPC 4:血盟 8:队伍 16:宠物 32:位置

    public int getTargetTo() {
        return this._targetTo;
    }

    /**
     * 施展对象
     * 
     * @param i
     *            0:自己 1:玩家 2:NPC 4:血盟 8:队伍 16:宠物 32:位置
     */
    public void setTargetTo(final int i) {
        this._targetTo = i;
    }

    private int _damageValue;

    /**
     * 魔法基础伤害
     * 
     * @return
     */
    public int getDamageValue() {
        return this._damageValue;
    }

    /**
     * 魔法基础伤害
     * 
     * @param i
     */
    public void setDamageValue(final int i) {
        this._damageValue = i;
    }

    private int _damageDice;

    /**
     * 魔法基础伤害随机附加值
     * 
     * @return
     */
    public int getDamageDice() {
        return this._damageDice;
    }

    /**
     * 魔法基础伤害随机附加值
     * 
     * @param i
     */
    public void setDamageDice(final int i) {
        this._damageDice = i;
    }

    private int _damageDiceCount;

    /**
     * 魔法基础伤害随机附加值 附加次数
     * 
     * @return
     */
    public int getDamageDiceCount() {
        return this._damageDiceCount;
    }

    /**
     * 魔法基础伤害随机附加值 附加次数
     * 
     * @param i
     */
    public void setDamageDiceCount(final int i) {
        this._damageDiceCount = i;
    }

    private int _probabilityValue;

    public int getProbabilityValue() {
        return this._probabilityValue;
    }

    public void setProbabilityValue(final int i) {
        this._probabilityValue = i;
    }

    private int _probabilityDice;

    /**
     * 技能计算机率
     * 
     * @return
     */
    public int getProbabilityDice() {
        return this._probabilityDice;
    }

    /**
     * 技能计算机率
     * 
     * @param i
     */
    public void setProbabilityDice(final int i) {
        this._probabilityDice = i;
    }

    private int _attr;// 魔法属性

    /**
     * 魔法属性<br>
     * 0.无属性魔法,1.地属性魔法,2.火属性魔法,4.水属性魔法,8.风属性魔法,16.光属性魔法
     */
    public int getAttr() {
        return this._attr;
    }

    public void setAttr(final int i) {
        this._attr = i;
    }

    private int _type; // 魔法种类

    /**
     * 魔法种类<br>
     * 1:破坏 2:辅助 4:诅咒 8:死亡 16:治疗 32:复活 64:攻击 128:其他特殊
     */
    public int getType() {
        return this._type;
    }

    public void setType(final int i) {
        this._type = i;
    }

    private int _lawful;

    public int getLawful() {
        return this._lawful;
    }

    public void setLawful(final int i) {
        this._lawful = i;
    }

    private int _ranged;

    /**
     * 施放距离
     * 
     * @return
     */
    public int getRanged() {
        return this._ranged;
    }

    /**
     * 施放距离
     * 
     * @param i
     */
    public void setRanged(final int i) {
        this._ranged = i;
    }

    private int _area;

    /***
     * 技能范围
     * 
     * @return
     */
    public int getArea() {
        return this._area;
    }

    /**
     * 技能范围
     * 
     * @param i
     */
    public void setArea(final int i) {
        this._area = i;
    }

    boolean _isThrough;

    public boolean isThrough() {
        return this._isThrough;
    }

    public void setThrough(final boolean flag) {
        this._isThrough = flag;
    }

    private int _id;

    public int getId() {
        return this._id;
    }

    public void setId(final int i) {
        this._id = i;
    }

    private String _nameId;

    public String getNameId() {
        return this._nameId;
    }

    public void setNameId(final String s) {
        this._nameId = s;
    }

    private int _actionId;

    /**
     * 技能动作代号
     * 
     * @return
     */
    public int getActionId() {
        return this._actionId;
    }

    /**
     * 技能动作代号
     * 
     * @param i
     */
    public void setActionId(final int i) {
        this._actionId = i;
    }

    private int _castGfx;

    public int getCastGfx() {
        return this._castGfx;
    }

    public void setCastGfx(final int i) {
        this._castGfx = i;
    }

    private int _castGfx2;

    public int getCastGfx2() {
        return this._castGfx2;
    }

    public void setCastGfx2(final int i) {
        this._castGfx2 = i;
    }

    private int _sysmsgIdHappen;

    public int getSysmsgIdHappen() {
        return this._sysmsgIdHappen;
    }

    public void setSysmsgIdHappen(final int i) {
        this._sysmsgIdHappen = i;
    }

    private int _sysmsgIdStop;

    public int getSysmsgIdStop() {
        return this._sysmsgIdStop;
    }

    public void setSysmsgIdStop(final int i) {
        this._sysmsgIdStop = i;
    }

    private int _sysmsgIdFail;

    public int getSysmsgIdFail() {
        return this._sysmsgIdFail;
    }

    public void setSysmsgIdFail(final int i) {
        this._sysmsgIdFail = i;
    }

}
