package com.lineage.server.model;

import static com.lineage.server.model.skill.L1SkillId.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigOther;
import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.model.Instance.L1FollowerInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.poison.L1Poison;
import com.lineage.server.model.skill.L1SkillTimer;
import com.lineage.server.model.skill.L1SkillTimerCreator;
import com.lineage.server.serverpackets.S_HPMeter;
import com.lineage.server.serverpackets.S_Light;
import com.lineage.server.serverpackets.S_PetCtrlMenu;
import com.lineage.server.serverpackets.S_Poison;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.types.Point;
import com.lineage.server.utils.RangeInt;
import com.lineage.server.world.World;

/**
 * L1Character
 * 
 * @author daien
 * 
 */
public class L1Character extends L1Object {

    private static final Log _log = LogFactory.getLog(L1Character.class);

    private static final long serialVersionUID = 1L;

    private L1Poison _poison = null;

    private boolean _sleeped;

    private final Map<Integer, L1NpcInstance> _petlist = new HashMap<Integer, L1NpcInstance>();

    private final HashMap<Integer, L1SkillTimer> _skillEffect = new HashMap<Integer, L1SkillTimer>();

    private final Map<Integer, L1ItemDelay.ItemDelayTimer> _itemdelay = new HashMap<Integer, L1ItemDelay.ItemDelayTimer>();

    private final Map<Integer, L1FollowerInstance> _followerlist = new HashMap<Integer, L1FollowerInstance>();

    public L1Character() {
        this._level = 1;
    }

    /**
     * 物件复活
     * 
     * @param hp
     *            复活后的HP
     */
    public void resurrect(int hp) {
        try {
            if (!isDead()) {
                return;
            }
            if (hp <= 0) {
                hp = 1;
            }
            // 设置为未死亡
            setDead(false);
            // 设置HP
            setCurrentHp(hp);
            // 设置状态
            setStatus(0);
            // 解除变身
            L1PolyMorph.undoPoly(this);

            // 重新认识物件
            for (final L1PcInstance pc : World.get().getRecognizePlayer(this)) {
                pc.sendPackets(new S_RemoveObject(this));
                pc.removeKnownObject(this);
                pc.updateObject();
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private int _secHp = -1;// 上次HP异动前HP

    /**
     * 发送该物件可见HP
     * 
     * @param pc
     */
    public void broadcastPacketHP(L1PcInstance pc) {
        try {
            // 副本ID相等 必须在这方法之前先判断
            // 记录HP不相等于 目前HP
            if (_secHp != getCurrentHp()) {
                _secHp = getCurrentHp();
                pc.sendPackets(new S_HPMeter(this));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private int _currentHp;

    /**
     * 现在的HP
     * 
     * @return 现在HP
     */
    public int getCurrentHp() {
        return _currentHp;
    }

    /**
     * 设置新HP
     * 
     * @param i
     */
    // 特殊な处理がある场合はこっちをオーバライド（パケット送信等）
    public void setCurrentHp(final int i) {
        _currentHp = i;
        if (_currentHp >= getMaxHp()) {
            _currentHp = getMaxHp();
        }
    }

    /**
     * 设置登场物件HP
     * 
     * @param i
     */
    public void setCurrentHpDirect(final int i) {
        _currentHp = i;
    }

    private int _currentMp;

    /**
     * 现在的MP
     * 
     * @return 现在MP
     */
    public int getCurrentMp() {
        return _currentMp;
    }

    /**
     * 设置新MP
     * 
     * @param i
     */
    public void setCurrentMp(final int i) {
        _currentMp = i;
        if (_currentMp >= getMaxMp()) {
            _currentMp = getMaxMp();
        }
    }

    /**
     * 设置登场物件MP
     * 
     * @param i
     */
    public void setCurrentMpDirect(final int i) {
        _currentMp = i;
    }

    /**
     * 是否为睡眠状态
     * 
     * @return true:麻痹状态 false:无
     */
    public boolean isSleeped() {
        return _sleeped;
    }

    /**
     * 是否为睡眠状态
     * 
     * @param sleeped
     *            true:睡眠状态 false:无
     */
    public void setSleeped(final boolean sleeped) {
        this._sleeped = sleeped;
    }

    /**
     * 无法攻击/使用道具/技能/回城的状态
     * 
     * @return true:状态中 false:无
     */
    public boolean isParalyzedX() {
        // 冰矛围篱
        if (hasSkillEffect(ICE_LANCE)) {
            return true;
        }
        // 冰雪飓风
        if (hasSkillEffect(FREEZING_BLIZZARD)) {
            return true;
        }
        // 寒冰喷吐
        if (hasSkillEffect(FREEZING_BREATH)) {
            return true;
        }
        // 大地屏障
        if (hasSkillEffect(EARTH_BIND)) {
            return true;
        }
        // 冲击之晕
        if (hasSkillEffect(SHOCK_STUN)) {
            return true;
        }
        // 骷髅毁坏
        if (hasSkillEffect(BONE_BREAK)) {
            return true;
        }
        // 木乃伊的诅咒
        if (hasSkillEffect(CURSE_PARALYZE)) {
            return true;
        }
        if (hasSkillEffect(STATUS_POISON_PARALYZED)) { //食尸鬼的麻痹状态 hjx1000
        	return true;
        }

        return false;
    }

    private boolean _paralyzed;// 麻痹状态

    /**
     * 是否为麻痹状态
     * 
     * @return true:麻痹状态 false:无
     */
    public boolean isParalyzed() {
        return this._paralyzed;
    }

    /**
     * 设定麻痹状态
     * 
     * @param paralyzed
     *            true:麻痹状态 false:无
     */
    public void setParalyzed(final boolean paralyzed) {
        this._paralyzed = paralyzed;
    }

    L1Paralysis _paralysis;

    public L1Paralysis getParalysis() {
        return this._paralysis;
    }

    public void setParalaysis(final L1Paralysis p) {
        this._paralysis = p;
    }

    public void cureParalaysis() {
        if (this._paralysis != null) {
            this._paralysis.cure();
        }
    }

    /**
     * 该物件全部可见范围封包发送(不包含自己)
     * 
     * @param packet
     *            封包
     */
    public void broadcastPacketAll(final ServerBasePacket packet) {
        try {
            for (final L1PcInstance pc : World.get().getVisiblePlayer(this)) {
                // 副本ID相等
                if (pc.get_showId() == this.get_showId()) {
                    pc.sendPackets(packet);
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 该物件指定范围封包发送(范围10)
     * 
     * @param packet
     *            封包
     */
    public void broadcastPacketX10(final ServerBasePacket packet) {
        try {
            for (final L1PcInstance pc : World.get().getVisiblePlayer(this, 10)) {
                // 副本ID相等
                if (pc.get_showId() == this.get_showId()) {
                    pc.sendPackets(packet);
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 该物件指定范围封包发送(范围8)
     * 
     * @param packet
     *            封包
     */
    public void broadcastPacketX8(final ServerBasePacket packet) {
        try {
            for (final L1PcInstance pc : World.get().getVisiblePlayer(this, 8)) {
                // 副本ID相等
                if (pc.get_showId() == this.get_showId()) {
                    pc.sendPackets(packet);
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 该物件指定范围封包发送(指定范围)
     * 
     * @param packet
     *            封包
     * @param r
     *            指定范围
     */
    public void broadcastPacketXR(final ServerBasePacket packet, final int r) {
        try {
            for (final L1PcInstance pc : World.get().getVisiblePlayer(this, r)) {
                // 副本ID相等
                if (pc.get_showId() == this.get_showId()) {
                    pc.sendPackets(packet);
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 该物件50格范围封包发送
     * 
     * @param packet
     *            封包
     */
    public void wideBroadcastPacket(final ServerBasePacket packet) {
        for (final L1PcInstance pc : World.get().getVisiblePlayer(this, 50)) {
            // 副本ID相等
            if (pc.get_showId() == this.get_showId()) {
                pc.sendPackets(packet);
            }
        }
    }

    /**
     * 该物件可见范围封包发送, (指定物件)
     * 
     * @param packet
     *            封包
     * @param target
     *            指定物件
     */
    public void broadcastPacketExceptTargetSight(final ServerBasePacket packet,
            final L1Character target) {
        boolean isX8 = false;
        // 检查城堡战争状态
        if (ServerWarExecutor.get().checkCastleWar() > 0) {
            isX8 = true;
            ;
        }
        if (isX8) {
            for (final L1PcInstance tgpc : World.get()
                    .getVisiblePlayerExceptTargetSight(this, target, 8)) {
                tgpc.sendPackets(packet);
            }

        } else {
            for (final L1PcInstance tgpc : World.get()
                    .getVisiblePlayerExceptTargetSight(this, target)) {
                tgpc.sendPackets(packet);
            }
        }
    }

    // 正向
    protected static final byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
    protected static final byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

    /**
     * キャラクターの正面の座标を返す。
     * 
     * @return 正面の座标
     */
    public int[] getFrontLoc() {
        final int[] loc = new int[2];
        int x = this.getX();
        int y = this.getY();
        final int heading = this.getHeading();

        x += HEADING_TABLE_X[heading];
        y += HEADING_TABLE_Y[heading];

        loc[0] = x;
        loc[1] = y;
        return loc;
    }

    /**
     * 指定座标对硬的面向
     * 
     * @param tx
     *            座标 X值
     * @param ty
     *            座标 Y值
     * @return 指定座标对硬的面向
     */
    public int targetDirection(final int tx, final int ty) {

        final float dis_x = Math.abs(this.getX() - tx); // X点方向距离
        final float dis_y = Math.abs(this.getY() - ty); // Y点方向距离
        final float dis = Math.max(dis_x, dis_y); // 取回2者最大质
        if (dis == 0) {
            return this.getHeading(); // 距离为0表示不须改变面向
        }
        final int avg_x = (int) Math.floor((dis_x / dis) + 0.59f); // 上下左右がちょっと优先な丸め
        final int avg_y = (int) Math.floor((dis_y / dis) + 0.59f); // 上下左右がちょっと优先な丸め

        int dir_x = 0;
        int dir_y = 0;
        if (this.getX() < tx) {
            dir_x = 1;
        }
        if (this.getX() > tx) {
            dir_x = -1;
        }
        if (this.getY() < ty) {
            dir_y = 1;
        }
        if (this.getY() > ty) {
            dir_y = -1;
        }

        if (avg_x == 0) {
            dir_x = 0;
        }
        if (avg_y == 0) {
            dir_y = 0;
        }

        switch (dir_x) {
            case -1:
                switch (dir_y) {
                    case -1:
                        return 7; // 左
                    case 0:
                        return 6; // 左下
                    case 1:
                        return 5; // 下
                }
                break;
            case 0:
                switch (dir_y) {
                    case -1:
                        return 0; // 左上
                    case 1:
                        return 4; // 右下
                }
                break;
            case 1:
                switch (dir_y) {
                    case -1:
                        return 1; // 上
                    case 0:
                        return 2; // 右上
                    case 1:
                        return 3; // 右
                }
                break;
        }
        return this.getHeading(); // ここにはこない。はず
    }

    /**
     * 指定された座标までの直线上に、障害物が存在*しないか*を返す。
     * 
     * @param tx
     *            座标のX值
     * @param ty
     *            座标のY值
     * @return 障害物が无ければtrue、あればfalseを返す。
     */
    public boolean glanceCheck(final int tx, final int ty) {
        final L1Map map = getMap();
        int chx = getX();
        int chy = getY();
        // final int arw = 0;

        for (int i = 0; i < 15; i++) {
            if (((chx == tx) && (chy == ty))
                    || ((chx + 1 == tx) && (chy - 1 == ty))
                    || ((chx + 1 == tx) && (chy == ty))
                    || ((chx + 1 == tx) && (chy + 1 == ty))
                    || ((chx == tx) && (chy + 1 == ty))
                    || ((chx - 1 == tx) && (chy + 1 == ty))
                    || ((chx - 1 == tx) && (chy == ty))
                    || ((chx - 1 == tx) && (chy - 1 == ty))
                    || ((chx == tx) && (chy - 1 == ty))) {
                break;

            } else {
                int th = targetDirection(tx, ty);
                if (!map.isArrowPassable(chx, chy, th)) {
                    return false;
                }
                if (chx < tx) {
                    if (chy == ty) {
                        chx++;

                    } else if (chy > ty) {
                        chx++;
                        chy--;

                    } else if (chy < ty) {
                        chx++;
                        chy++;

                    }

                } else if (chx == tx) {
                    if (chy < ty) {
                        chy++;

                    } else if (chy > ty) {
                        chy--;
                    }

                } else if (chx > tx) {
                    if (chy == ty) {
                        chx--;

                    } else if (chy < ty) {
                        chx--;
                        chy++;

                    } else if (chy > ty) {
                        chx--;
                        chy--;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 指定された座标へ攻击可能であるかを返す。
     * 
     * @param x
     *            座标のX值。
     * @param y
     *            座标のY值。
     * @param range
     *            攻击可能な范围(タイル数)
     * @return 攻击可能であればtrue,不可能であればfalse
     */
    public boolean isAttackPosition(final int x, final int y, final int range) {
        if (range >= 7) {// 远隔武器（７以上の场合斜めを考虑すると画面外に出る)
            if (getLocation().getTileDistance(new Point(x, y)) > range) {
                return false;
            }

        } else {// 近接武器
            if (getLocation().getTileLineDistance(new Point(x, y)) > range) {
                return false;
            }
        }
        return this.glanceCheck(x, y);
    }

    /**
     * 传回背包资料
     * 
     * @return L1Inventory
     */
    public L1Inventory getInventory() {
        return null;
    }

    /**
     * キャラクターへ、新たにスキル效果を追加する。
     * 
     * @param skillId
     *            追加する效果のスキルID。
     * @param timeMillis
     *            追加する效果の持续时间。无限の场合は0。
     */
    private void addSkillEffect(final int skillId, final int timeMillis) {
        L1SkillTimer timer = null;
        if (timeMillis > 0) {
            timer = L1SkillTimerCreator.create(this, skillId, timeMillis);
            timer.begin();
        }
        _skillEffect.put(skillId, timer);
    }

    /**
     * キャラクターへ、スキル效果を设定する。<br>
     * 重复するスキルがない场合は、新たにスキル效果を追加する。<br>
     * 重复するスキルがある场合は、残り效果时间とパラメータの效果时间の长い方を优先して设定する。
     * 
     * @param skillId
     *            设定する效果のスキルID。
     * @param timeMillis
     *            效果时间(单位:毫秒)
     */
    public void setSkillEffect(final int skillId, final int timeMillis) {
        // System.out.println(skillId + "/" + timeMillis);
        if (hasSkillEffect(skillId)) {
            final int remainingTimeMills = getSkillEffectTimeSec(skillId) * 1000;

            // 残り时间が有限で、パラメータの效果时间の方が长いか无限の场合は上书きする。
            if ((remainingTimeMills >= 0)
                    && ((remainingTimeMills < timeMillis) || (timeMillis == 0))) {
                killSkillEffectTimer(skillId);
                addSkillEffect(skillId, timeMillis);
            }

        } else {
            addSkillEffect(skillId, timeMillis);
        }
    }

    /**
     * 技能效果结束
     * 
     * @param skillId
     *            技能编号
     */
    public void removeSkillEffect(final int skillId) {
        final L1SkillTimer timer = _skillEffect.remove(skillId);
        if (timer != null) {
            timer.end();
        }
    }

    /**
     * 删除全部技能效果
     */
    public void clearAllSkill() {
        for (final L1SkillTimer timer : _skillEffect.values()) {
            if (timer != null) {
                timer.end();
            }
        }
        _skillEffect.clear();
    }

    /**
     * 指定技能效果消除
     * 
     * @param skillId
     *            技能编号
     */
    public void killSkillEffectTimer(final int skillId) {
        final L1SkillTimer timer = _skillEffect.remove(skillId);
        if (timer != null) {
            timer.kill();
        }
    }

    /**
     * 删除全部技能效果
     */
    public void clearSkillEffectTimer() {
        for (final L1SkillTimer timer : _skillEffect.values()) {
            if (timer != null) {
                timer.kill();
            }
        }
        _skillEffect.clear();
    }

    /**
     * 是否有该技能效果
     * 
     * @param skillId
     * @return 有true 无false。
     */
    public boolean hasSkillEffect(final int skillId) {
        return _skillEffect.containsKey(skillId);
    }

    /**
     * 该物件目前具有的技能编号
     * 
     * @return
     */
    public Set<Integer> getSkillEffect() {
        return _skillEffect.keySet();
    }

    /**
     * 该物件目前具有技能状态
     * 
     * @return true:没有 false:有
     */
    public boolean getSkillisEmpty() {
        return _skillEffect.isEmpty();
    }

    /**
     * 技能效果剩余时间
     * 
     * @param skillId
     * @return 剩余秒数 无时间限制传回-1
     */
    public int getSkillEffectTimeSec(final int skillId) {
        final L1SkillTimer timer = _skillEffect.get(skillId);
        if (timer == null) {
            return -1;
        }
        return timer.getRemainingTime();
    }

    private boolean _isSkillDelay = false;

    /**
     * 设定技能施放延迟中
     * 
     * @param flag
     *            true:是 false:否
     */
    public void setSkillDelay(final boolean flag) {
        _isSkillDelay = flag;
    }

    /**
     * 是否在技能施放延迟中
     * 
     * @return true:是 false:否
     */
    public boolean isSkillDelay() {
        return _isSkillDelay;
    }

    /**
     * 物件使用延迟编号设置
     * 
     * @param delayId
     *            延迟编号
     * @param timer
     *            时间(毫秒)
     */
    public void addItemDelay(final int delayId,
            final L1ItemDelay.ItemDelayTimer timer) {
        _itemdelay.put(delayId, timer);
    }

    /**
     * 物件使用延迟编号移除
     * 
     * @param delayId
     *            延迟编号
     */
    public void removeItemDelay(final int delayId) {
        _itemdelay.remove(delayId);
    }

    /**
     * 是否为延迟使用的物件
     * 
     * @param delayId
     *            延迟编号
     * @return true:是 false:否
     */
    public boolean hasItemDelay(final int delayId) {
        return _itemdelay.containsKey(delayId);
    }

    /**
     * 是否为延迟使用的物件
     * 
     * @param delayId
     *            延迟编号
     * @return 物件延迟设置
     */
    public L1ItemDelay.ItemDelayTimer getItemDelayTimer(final int delayId) {
        return _itemdelay.get(delayId);
    }

    /**
     * 加入宠物清单
     * 
     * @param npc
     */
    public void addPet(final L1NpcInstance npc) {
        _petlist.put(npc.getId(), npc);
        // 加入宠物控制介面
        sendPetCtrlMenu(npc, true);
    }

    /**
     * 移除宠物清单
     * 
     * @param npc
     */
    public void removePet(final L1NpcInstance npc) {
        _petlist.remove(npc.getId());
        // 移除宠物控制介面
        sendPetCtrlMenu(npc, false);
    }

    /**
     * 传回宠物控制清单
     * 
     * @return
     */
    public Map<Integer, L1NpcInstance> getPetList() {
        return _petlist;
    }

    /**
     * 宠物选单控制
     * 
     * @param npc
     * @param type
     *            true:显示 false:关闭
     */
    private final void sendPetCtrlMenu(L1NpcInstance npc, boolean type) {
        if (npc instanceof L1PetInstance) {
            L1PetInstance pet = (L1PetInstance) npc;
            L1Character cha = pet.getMaster();

            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_PetCtrlMenu(pc, pet, type));

                if (type) {
                    pc.sendPackets(new S_HPMeter(pet));
                }
            }

        } else if (npc instanceof L1SummonInstance) {
            L1SummonInstance summon = (L1SummonInstance) npc;
            L1Character cha = summon.getMaster();

            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_PetCtrlMenu(pc, summon, type));

                if (type) {
                    pc.sendPackets(new S_HPMeter(summon));
                }
            }
        }
    }

    private final Map<Integer, L1DollInstance> _dolls = new HashMap<Integer, L1DollInstance>();

    /**
     * 设置娃娃
     * 
     * @param doll
     */
    public void addDoll(final L1DollInstance doll) {
        _dolls.put(doll.getItemObjId(), doll);
    }

    /**
     * 移除娃娃
     */
    public void removeDoll(final L1DollInstance doll) {
        _dolls.remove(doll.getItemObjId());
    }

    /**
     * 移除娃娃
     * 
     * @return
     */
    public L1DollInstance getDoll(final int key) {
        return _dolls.get(key);
    }

    /**
     * 目前携带的娃娃
     * 
     * @return 目前携带的娃娃
     */
    public Map<Integer, L1DollInstance> getDolls() {
        return _dolls;
    }

    /**
     * 加入跟随者
     * 
     * @param follower
     *            跟随者
     */
    public void addFollower(final L1FollowerInstance follower) {
        _followerlist.put(follower.getId(), follower);
    }

    /**
     * 移除跟随者
     * 
     * @param follower
     *            跟随者
     */
    public void removeFollower(final L1FollowerInstance follower) {
        _followerlist.remove(follower.getId());
    }

    /**
     * 传回跟随者
     * 
     * @return 跟随者清单
     */
    public Map<Integer, L1FollowerInstance> getFollowerList() {
        return _followerlist;
    }

    /**
     * キャラクターへ、毒を追加する。
     * 
     * @param poison
     *            毒を表す、L1Poisonオブジェクト。
     */
    public void setPoison(final L1Poison poison) {
        this._poison = poison;
    }

    /**
     * キャラクターの毒を治疗する。
     */
    public void curePoison() {
        if (this._poison == null) {
            return;
        }
        this._poison.cure();
    }

    /**
     * キャラクターの毒状态を返す。
     * 
     * @return キャラクターの毒を表す、L1Poisonオブジェクト。
     */
    public L1Poison getPoison() {
        return this._poison;
    }

    /**
     * キャラクターへ毒のエフェクトを付加する
     * 
     * @param effectId
     * @see S_Poison#S_Poison(int, int)
     */
    public void setPoisonEffect(final int effectId) {
        this.broadcastPacketX8(new S_Poison(this.getId(), effectId));
    }

    /**
     * 所在位置区域属性返回
     * 
     * @return 0一般区域 1安全区域 -1战斗区域
     */
    public int getZoneType() {
        if (this.getMap().isSafetyZone(this.getLocation())) {
            return 1;
        } else if (this.getMap().isCombatZone(this.getLocation())) {
            return -1;
        } else { // ノーマルゾーン
            return 0;
        }
    }

    /**
     * 位于安全区域中
     * 
     * @return
     */
    public boolean isSafetyZone() {
        if (this.getMap().isSafetyZone(this.getLocation())) {
            return true;
        }
        return false;
    }

    /**
     * 位于战斗区域中
     * 
     * @return
     */
    public boolean isCombatZone() {
        if (this.getMap().isCombatZone(this.getLocation())) {
            return true;
        }
        return false;
    }

    /**
     * 位于一般区域中
     * 
     * @return
     */
    public boolean isNoneZone() {
        return getZoneType() == 0;
    }

    private long _exp; // ● 经验值

    /**
     * キャラクターが保持している经验值を返す。
     * 
     * @return 经验值。
     */
    public long getExp() {
        return this._exp;
    }

    /**
     * キャラクターが保持する经验值を设定する。
     * 
     * @param exp
     *            经验值。
     */
    public void setExp(final long exp) {
        this._exp = exp;
    }

    // ■■■■■■■■■■ L1PcInstanceへ移动するプロパティ ■■■■■■■■■■
    private final List<L1Object> _knownObjects = new CopyOnWriteArrayList<L1Object>();
    private final List<L1PcInstance> _knownPlayer = new CopyOnWriteArrayList<L1PcInstance>();

    /**
     * 是否为已认识物件
     * 
     * @param obj
     *            判断对象
     * @return true:是 false:不是
     */
    public boolean knownsObject(final L1Object obj) {
        return this._knownObjects.contains(obj);
    }

    /**
     * 全部认识物件(L1Object)清单
     * 
     * @return 全部认识物件(L1Object)清单
     */
    public List<L1Object> getKnownObjects() {
        return this._knownObjects;
    }

    /**
     * 全部认识物件(PC)清单
     * 
     * @return 全部认识物件(PC)清单
     */
    public List<L1PcInstance> getKnownPlayers() {
        return this._knownPlayer;
    }

    /**
     * 加入已认识物件
     * 
     * @param obj
     *            加入对象
     */
    public void addKnownObject(final L1Object obj) {
        if (!this._knownObjects.contains(obj)) {
            this._knownObjects.add(obj);
            if (obj instanceof L1PcInstance) {
                this._knownPlayer.add((L1PcInstance) obj);
            }
        }
    }

    /**
     * 移出已认识物件
     * 
     * @param obj
     *            移出对象
     */
    public void removeKnownObject(final L1Object obj) {
        this._knownObjects.remove(obj);
        if (obj instanceof L1PcInstance) {
            this._knownPlayer.remove(obj);
        }
    }

    /**
     * 全部认识对象移除
     */
    public void removeAllKnownObjects() {
        this._knownObjects.clear();
        this._knownPlayer.clear();
    }

    // ■■■■■■■■■■ プロパティ ■■■■■■■■■■

    private String _name; // ● 名前

    public String getName() {
        return this._name;
    }

    public void setName(final String s) {
        this._name = s;
    }

    private int _level; // ● レベル

    public synchronized int getLevel() {
        return this._level;
    }

    public synchronized void setLevel(final int level) {
        this._level = level;
    }

    private int _maxHp = 0; // 最大HP量(MOB 1~100000, 其他 1~32767)
    private int _trueMaxHp = 0; // ● 本当のＭＡＸＨＰ

    /**
     * 最大HP量
     * 
     * @return
     */
    public int getMaxHp() {
        return (this._maxHp);
    }

    /**
     * 最大HP
     * 
     * @param hp
     */
    public void setMaxHp(final int hp) {
        this._trueMaxHp = hp;
        this._maxHp = RangeInt.ensure(this._trueMaxHp, 1, 500000);
        this._currentHp = Math.min(this._currentHp, this._maxHp);
    }

    /**
     * 增加(减少)HP上限
     * 
     * @param i
     */
    public void addMaxHp(final int i) {
        this.setMaxHp(this._trueMaxHp + i);
    }

    private short _maxMp = 0; // ● ＭＡＸＭＰ（0～32767）
    private int _trueMaxMp = 0; // ● 本当のＭＡＸＭＰ

    /**
     * 最大MP量
     * 
     * @return
     */
    public short getMaxMp() {
        return (this._maxMp);
    }

    /**
     * 最大MP
     * 
     * @param mp
     */
    public void setMaxMp(final int mp) {
        this._trueMaxMp = mp;
        this._maxMp = (short) RangeInt.ensure(this._trueMaxMp, 0, 32767);
        this._currentMp = Math.min(this._currentMp, this._maxMp);
    }

    /**
     * 增加(减少)MP上限
     * 
     * @param i
     */
    public void addMaxMp(final int i) {
        this.setMaxMp(this._trueMaxMp + i);
    }

    private int _ac = 10; // ● ＡＣ（-211～44）
    private int _trueAc = 0; // ● 本当のＡＣ

    public int getAc() {
        int ac = _ac;
        if (this instanceof L1PcInstance) {
            L1PcInstance pc = (L1PcInstance) this;
            switch (pc.guardianEncounter()) {
                case 0:// 正义的守护 Lv1
                    ac -= 2;
                    break;

                case 1:// 正义的守护 Lv2
                    ac -= 4;
                    break;

                case 2:// 正义的守护 Lv3
                    ac -= 6;
                    break;
            }
        }
        return RangeInt.ensure(ac, -211, 44);
    }

    public void setAc(final int i) {
        this._trueAc = i;
        this._ac = RangeInt.ensure(i, -211, 44);
    }

    /**
     * 增加(减少)防御力
     * 
     * @param i
     */
    public void addAc(final int i) {
        this.setAc(this._trueAc + i);
    }

    private short _str = 0; // ● ＳＴＲ（1～127）
    private short _trueStr = 0; // ● 本当のＳＴＲ

    /**
     * 力量
     * 
     * @return
     */
    public short getStr() {
        return (this._str);
    }

    public void setStr(final int i) {
        this._trueStr = (short) i;
        this._str = (short) RangeInt.ensure(i, 1, 254);
    }

    /**
     * 增加(减少)力量
     * 
     * @param i
     */
    public void addStr(final int i) {
        this.setStr(this._trueStr + i);
    }

    private short _con = 0; // ● ＣＯＮ（1～127）
    private short _trueCon = 0; // ● 本当のＣＯＮ

    /**
     * 体质
     * 
     * @return
     */
    public short getCon() {
        return (this._con);
    }

    public void setCon(final int i) {
        this._trueCon = (short) i;
        this._con = (short) RangeInt.ensure(i, 1, 254);
    }

    /**
     * 增加(减少)体质
     * 
     * @param i
     */
    public void addCon(final int i) {
        this.setCon(this._trueCon + i);
    }

    private short _dex = 0; // ● ＤＥＸ（1～127）
    private short _trueDex = 0; // ● 本当のＤＥＸ

    /**
     * 敏捷
     * 
     * @return
     */
    public short getDex() {
        return (this._dex);
    }

    public void setDex(final int i) {
        this._trueDex = (short) i;
        this._dex = (short) RangeInt.ensure(i, 1, 254);
    }

    /**
     * 增加(减少)敏捷
     * 
     * @param i
     */
    public void addDex(final int i) {
        this.setDex(this._trueDex + i);
    }

    private short _cha = 0; // ● ＣＨＡ（1～127）
    private short _trueCha = 0; // ● 本当のＣＨＡ

    /**
     * 魅力
     * 
     * @return
     */
    public short getCha() {
        return (this._cha);
    }

    public void setCha(final int i) {
        this._trueCha = (short) i;
        this._cha = (short) RangeInt.ensure(i, 1, 254);
    }

    /**
     * 增加(减少)魅力
     * 
     * @param i
     */
    public void addCha(final int i) {
        this.setCha(this._trueCha + i);
    }

    private short _int = 0; // ● ＩＮＴ（1～127）
    private short _trueInt = 0; // ● 本当のＩＮＴ

    /**
     * 智力
     * 
     * @return
     */
    public short getInt() {
        return (this._int);
    }

    public void setInt(final int i) {
        this._trueInt = (short) i;
        this._int = (short) RangeInt.ensure(i, 1, 254);
    }

    /**
     * 增加(减少)智力
     * 
     * @param i
     */
    public void addInt(final int i) {
        this.setInt(this._trueInt + i);
    }

    private short _wis = 0; // ● ＷＩＳ（1～127）
    private short _trueWis = 0; // ● 本当のＷＩＳ

    /**
     * 精神
     * 
     * @return
     */
    public short getWis() {
        return (this._wis);
    }

    public void setWis(final int i) {
        this._trueWis = (short) i;
        this._wis = (short) RangeInt.ensure(i, 1, 254);
    }

    /**
     * 增加(减少)精神
     * 
     * @param i
     */
    public void addWis(final int i) {
        this.setWis(this._trueWis + i);
    }

    private int _wind = 0; // ● 风防御（-128～127）
    private int _trueWind = 0; // ● 本当の风防御

    /**
     * 风属性
     * 
     * @return
     */
    public int getWind() {
        return this._wind;
    } // 使用するとき

    /**
     * 增加(减少)风属性
     * 
     * @param i
     */
    public void addWind(final int i) {
        this._trueWind += i;
        if (this._trueWind >= 127) {
            this._wind = 127;
        } else if (this._trueWind <= -128) {
            this._wind = -128;
        } else {
            this._wind = this._trueWind;
        }
    }

    private int _water = 0; // ● 水防御（-128～127）
    private int _trueWater = 0; // ● 本当の水防御

    /**
     * 水属性
     * 
     * @return
     */
    public int getWater() {
        return this._water;
    } // 使用するとき

    /**
     * 增加(减少)水属性
     * 
     * @param i
     */
    public void addWater(final int i) {
        this._trueWater += i;
        if (this._trueWater >= 127) {
            this._water = 127;
        } else if (this._trueWater <= -128) {
            this._water = -128;
        } else {
            this._water = this._trueWater;
        }
    }

    private int _fire = 0; // ● 火防御（-128～127）
    private int _trueFire = 0; // ● 本当の火防御

    /**
     * 火属性
     * 
     * @return
     */
    public int getFire() {
        return this._fire;
    } // 使用するとき

    /**
     * 增加(减少)火属性
     * 
     * @param i
     */
    public void addFire(final int i) {
        this._trueFire += i;
        if (this._trueFire >= 127) {
            this._fire = 127;
        } else if (this._trueFire <= -128) {
            this._fire = -128;
        } else {
            this._fire = this._trueFire;
        }
    }

    private int _earth = 0; // ● 地防御（-128～127）
    private int _trueEarth = 0; // ● 本当の地防御

    /**
     * 地属性
     * 
     * @return
     */
    public int getEarth() {
        return this._earth;
    } // 使用するとき

    /**
     * 增加(减少)地属性
     * 
     * @param i
     */
    public void addEarth(final int i) {
        this._trueEarth += i;
        if (this._trueEarth >= 127) {
            this._earth = 127;

        } else if (this._trueEarth <= -128) {
            this._earth = -128;

        } else {
            this._earth = this._trueEarth;
        }
    }

    private int _addAttrKind; // エレメンタルフォールダウンで减少した属性の种类

    public int getAddAttrKind() {
        return this._addAttrKind;
    }

    public void setAddAttrKind(final int i) {
        this._addAttrKind = i;
    }

    // 晕眩耐性
    private int _registStun = 0;
    private int _trueRegistStun = 0;

    /**
     * 晕眩耐性
     * 
     * @return
     */
    public int getRegistStun() {
        return this._registStun;
    } // 使用するとき

    /**
     * 晕眩耐性
     * 
     * @param i
     */
    public void addRegistStun(final int i) {
        this._trueRegistStun += i;
        if (this._trueRegistStun > 127) {
            this._registStun = 127;
        } else if (this._trueRegistStun < -128) {
            this._registStun = -128;
        } else {
            this._registStun = this._trueRegistStun;
        }
    }

    // 石化耐性
    private int _registStone = 0;
    private int _trueRegistStone = 0;

    /**
     * 石化耐性
     * 
     * @return
     */
    public int getRegistStone() {
        return this._registStone;
    }

    /**
     * 石化耐性
     * 
     * @param i
     */
    public void addRegistStone(final int i) {
        this._trueRegistStone += i;
        if (this._trueRegistStone > 127) {
            this._registStone = 127;
        } else if (this._trueRegistStone < -128) {
            this._registStone = -128;
        } else {
            this._registStone = this._trueRegistStone;
        }
    }

    // 睡眠耐性
    private int _registSleep = 0;
    private int _trueRegistSleep = 0;

    /**
     * 睡眠耐性
     * 
     * @return
     */
    public int getRegistSleep() {
        return this._registSleep;
    }

    /**
     * 睡眠耐性
     * 
     * @param i
     */
    public void addRegistSleep(final int i) {
        this._trueRegistSleep += i;
        if (this._trueRegistSleep > 127) {
            this._registSleep = 127;
        } else if (this._trueRegistSleep < -128) {
            this._registSleep = -128;
        } else {
            this._registSleep = this._trueRegistSleep;
        }
    }

    // 冻结耐性
    private int _registFreeze = 0;
    private int _trueRegistFreeze = 0;

    /**
     * 寒冰耐性
     * 
     * @return
     */
    public int getRegistFreeze() {
        return this._registFreeze;
    }

    /**
     * 寒冰耐性
     * 
     * @param i
     */
    public void add_regist_freeze(final int i) {
        this._trueRegistFreeze += i;
        if (this._trueRegistFreeze > 127) {
            this._registFreeze = 127;
        } else if (this._trueRegistFreeze < -128) {
            this._registFreeze = -128;
        } else {
            this._registFreeze = this._trueRegistFreeze;
        }
    }

    // 支撑耐性
    private int _registSustain = 0;
    private int _trueRegistSustain = 0;

    /**
     * 支撑耐性
     * 
     * @return
     */
    public int getRegistSustain() {
        return this._registSustain;
    }

    /**
     * 支撑耐性
     * 
     * @param i
     */
    public void addRegistSustain(final int i) {
        this._trueRegistSustain += i;
        if (this._trueRegistSustain > 127) {
            this._registSustain = 127;
        } else if (this._trueRegistSustain < -128) {
            this._registSustain = -128;
        } else {
            this._registSustain = this._trueRegistSustain;
        }
    }

    // 暗黑耐性
    private int _registBlind = 0;
    private int _trueRegistBlind = 0;

    /**
     * 暗黑耐性
     * 
     * @return
     */
    public int getRegistBlind() {
        return this._registBlind;
    }

    /**
     * 暗黑耐性
     * 
     * @param i
     */
    public void addRegistBlind(final int i) {
        this._trueRegistBlind += i;
        if (this._trueRegistBlind > 127) {
            this._registBlind = 127;
        } else if (this._trueRegistBlind < -128) {
            this._registBlind = -128;
        } else {
            this._registBlind = this._trueRegistBlind;
        }
    }

    private int _dmgup = 0; // ● ダメージ补正（-128～127）
    private int _trueDmgup = 0; // ● 本当のダメージ补正

    /**
     * 近战伤害增加
     * 
     * @return
     */
    public int getDmgup() {
        return this._dmgup;
    } // 使用するとき

    /**
     * 伤害增加
     * 
     * @param i
     */
    public void addDmgup(final int i) {
        this._trueDmgup += i;
        if (this._trueDmgup >= 127) {
            this._dmgup = 127;
        } else if (this._trueDmgup <= -128) {
            this._dmgup = -128;
        } else {
            this._dmgup = this._trueDmgup;
        }
    }

    private int _bowDmgup = 0; // ● 弓ダメージ补正（-128～127）
    private int _trueBowDmgup = 0; // ● 本当の弓ダメージ补正

    /**
     * 远距离伤害增加
     * 
     * @return
     */
    public int getBowDmgup() {
        return this._bowDmgup;
    } // 使用するとき

    /**
     * 远距离伤害增加
     * 
     * @param i
     */
    public void addBowDmgup(final int i) {
        this._trueBowDmgup += i;
        if (this._trueBowDmgup >= 127) {
            this._bowDmgup = 127;
        } else if (this._trueBowDmgup <= -128) {
            this._bowDmgup = -128;
        } else {
            this._bowDmgup = this._trueBowDmgup;
        }
    }

    private int _hitup = 0; // ● 命中补正（-128～127）
    private int _trueHitup = 0; // ● 本当の命中补正

    /**
     * 命中增加
     * 
     * @return
     */
    public int getHitup() {
        return this._hitup;
    } // 使用するとき

    /**
     * 命中增加
     * 
     * @param i
     */
    public void addHitup(final int i) {
        this._trueHitup += i;
        if (this._trueHitup >= 127) {
            this._hitup = 127;
        } else if (this._trueHitup <= -128) {
            this._hitup = -128;
        } else {
            this._hitup = this._trueHitup;
        }
    }

    private int _bowHitup = 0; // ● 弓命中补正（-128～127）
    private int _trueBowHitup = 0; // ● 本当の弓命中补正

    /**
     * 远距离命中增加
     * 
     * @return
     */
    public int getBowHitup() {
        return this._bowHitup;
    } // 使用するとき

    /**
     * 远距离命中增加
     * 
     * @param i
     */
    public void addBowHitup(final int i) {
        this._trueBowHitup += i;
        if (this._trueBowHitup >= 127) {
            this._bowHitup = 127;
        } else if (this._trueBowHitup <= -128) {
            this._bowHitup = -128;
        } else {
            this._bowHitup = this._trueBowHitup;
        }
    }

    private int _mr = 0; // ● 魔法防御（0～）
    private int _trueMr = 0; // ● 本当の魔法防御

    /**
     * 魔防
     * 
     * @return
     */
    public int getMr() {
        if (this.hasSkillEffect(ERASE_MAGIC) == true) {// 魔法消除
            return this._mr >> 2;// / 4;
        } else {
            return this._mr;
        }
    } // 使用するとき

    public int getTrueMr() {
        return this._trueMr;
    } // セットするとき

    /**
     * 魔防
     * 
     * @param i
     */
    public void addMr(final int i) {
        this._trueMr += i;
        if (this._trueMr <= 0) {
            this._mr = 0;
        } else {
            this._mr = this._trueMr;
        }
    }

    private int _sp = 0; // ● 增加したＳＰ

    /**
     * 魔功
     * 
     * @return
     */
    public int getSp() {
        return this.getTrueSp() + this._sp;
    }

    /**
     * 魔功
     * 
     * @return
     */
    public int getTrueSp() {
        return this.getMagicLevel() + this.getMagicBonus();
    }

    /**
     * 增加魔功
     * 
     * @param i
     */
    public void addSp(final int i) {
        this._sp += i;
    }

    private boolean _isDead; // 死亡状态

    /**
     * 死亡状态
     * 
     * @return
     */
    public boolean isDead() {
        return this._isDead;
    }

    /**
     * 死亡状态
     * 
     * @param flag
     */
    public void setDead(final boolean flag) {
        this._isDead = flag;
    }

    private int _status; // 初始化状态

    /**
     * 初始化状态
     * 
     * @return
     */
    public int getStatus() {
        return this._status;
    }

    /**
     * 初始化状态
     * 
     * @param i
     */
    public void setStatus(final int i) {
        this._status = i;
    }

    private String _title; // 封号

    /**
     * 封号
     * 
     * @return
     */
    public String getTitle() {
        return this._title;
    }

    /**
     * 封号
     * 
     * @param s
     */
    public void setTitle(final String s) {
        this._title = s;
    }
    
    private String _forcetitle; // 强制封号 hjx1000
    /**
     * 强制封号 hjx1000
     * @return
     */
    public String getforcetitle() {
        return this._forcetitle;
    }
    /**
     * 强制封号 hjx1000
     * @param s
     */
	public void setforcetitle(String s) {// 强制封号 hjx1000
		this._forcetitle = s;
	}

	private int _lawful; // ● アライメント

    /**
     * 传回正义质
     * 
     * @return
     */
    public int getLawful() {
        return this._lawful;
    }

    /**
     * 设定正义质
     * 
     * @param i
     */
    public void setLawful(final int i) {
        this._lawful = i;
    }

    public synchronized void addLawful(final int i) {
        this._lawful += i;
        if (this._lawful > 32767) {
            this._lawful = 32767;

        } else if (this._lawful < -32768) {
            this._lawful = -32768;
        }
    }

    private int _heading; // 面向： 0.左上 1.上 2.右上 3.右 4.右下 5.下 6.左下 7.左

    /**
     * 面向
     * 
     * @return 0:左上 1:上 2:右上 3:右 4:右下 5:下 6:左下 7:左
     */
    public int getHeading() {
        return this._heading;
    }

    /**
     * 面向
     * 
     * @param i
     *            0:左上 1:上 2:右上 3:右 4:右下 5:下 6:左下 7:左
     */
    public void setHeading(final int i) {
        this._heading = i;
    }

    private int _moveSpeed; // 移动加速状态(绿水)

    /**
     * 移动加速状态(绿水)
     * 
     * @return 0:无 1:加速 2:缓速
     */
    public int getMoveSpeed() {
        return this._moveSpeed;
    }

    /**
     * 移动加速状态(绿水)
     * 
     * @param i
     *            0:无 1:加速 2:缓速
     */
    public void setMoveSpeed(final int i) {
        this._moveSpeed = i;
    }

    private int _braveSpeed; // 攻击加速状态(勇水)

    /**
     * 攻击加速状态(勇水)
     * 
     * @return 0:无 1:勇水 5:强化勇水
     */
    public int getBraveSpeed() {
        return this._braveSpeed;
    }

    /**
     * 攻击加速状态(勇水)
     * 
     * @param i
     *            0:无 1:勇水 5:强化勇水
     */
    public void setBraveSpeed(final int i) {
        this._braveSpeed = i;
    }

    private int _tempCharGfx; // 显示外型编号

    /**
     * 传回显示外型编号
     * 
     * @return
     */
    public int getTempCharGfx() {
        return this._tempCharGfx;
    }

    /**
     * 设置显示外型编号
     * 
     * @param i
     */
    public void setTempCharGfx(final int i) {
        this._tempCharGfx = i;
    }

    private int _gfxid; // 原始外型编号

    /**
     * 传回原始外型编号
     * 
     * @return
     */
    public int getGfxId() {
        return this._gfxid;
    }

    /**
     * 设置原始外型编号
     * 
     * @param i
     */
    public void setGfxId(final int i) {
        this._gfxid = i;
    }

    /**
     * 魔法等级
     * 
     * @return
     */
    public int getMagicLevel() {
        return this.getLevel() >> 2;// / 4;
    }

    /**
     * 智力命中魔法追加
     * 
     * @return
     */
    public int getMagicBonus() {
        switch (this.getInt()) {//修正官方魔功算法 hjx1000
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return -2;

            case 6:
            case 7:
            case 8:
                return -1;

            case 9:
            case 10:
            case 11:
                return 0;

            case 12:
            case 13:
            case 14:
                return 1;

            case 15:
            case 16:
            case 17:
                return 2;

            case 18:
                return 3;
            case 19:
                return 4;
            case 20:
                return 5;
            case 21:
                return 6;
            case 22:
                return 7;
            case 23:
                return 8;
            case 24:
                return 9;
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
                return 10;
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
                return 11;
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
                return 12;
            case 50:
                return 13;

            default:
                //return this.getInt() - 25;
            	return 14;// hjx1000 限制魔法命中智力加成最高 14
        }
    }

    /**
     * 是否在隐身状态
     * 
     * @return
     */
    public boolean isInvisble() {
        return (this.hasSkillEffect(INVISIBILITY) || this
                .hasSkillEffect(BLIND_HIDING));
    }

    /**
     * 治疗
     * 
     * @param pt
     *            治疗质
     */
    public void healHp(final int pt) {
        this.setCurrentHp(this.getCurrentHp() + pt);
    }

    private int _karma;

    /**
     * キャラクターが保持しているカルマを返す。
     * 
     * @return カルマ。
     */
    public int getKarma() {
        return this._karma;
    }

    /**
     * キャラクターが保持するカルマを设定する。
     * 
     * @param karma
     *            カルマ。
     */
    public void setKarma(final int karma) {
        this._karma = karma;
    }

    public void setMr(final int i) {
        this._trueMr = i;
        if (this._trueMr <= 0) {
            this._mr = 0;
        } else {
            this._mr = this._trueMr;
        }
    }

    /**
     * 光
     */
    public void turnOnOffLight() {
        int lightSize = 0x00;
        if (this instanceof L1NpcInstance) {
            final L1NpcInstance npc = (L1NpcInstance) this;
            lightSize = npc.getLightSize(); // npc.sqlのライトサイズ
        }

        for (final L1ItemInstance item : this.getInventory().getItems()) {
            if ((item.getItem().getType2() == 0)
                    && (item.getItem().getType() == 2)) { // light系アイテム
                final int itemlightSize = item.getItem().getLightRange();
                if ((itemlightSize != 0) && item.isNowLighting()) {
                    if (itemlightSize > lightSize) {
                        lightSize = itemlightSize;
                    }
                }
            }
        }
        // 照明法术
        if (this.hasSkillEffect(LIGHT)) {
            lightSize = 0x0e;
        }

        // 人物
        if (this instanceof L1PcInstance) {
            if (ConfigOther.LIGHT) {
                lightSize = 0x14;
            }
            final L1PcInstance pc = (L1PcInstance) this;
            pc.sendPackets(new S_Light(pc.getId(), lightSize));
        }

        if (!this.isInvisble()) {
            this.broadcastPacketAll(new S_Light(this.getId(), lightSize));
        }

        this.setOwnLightSize(lightSize); // S_OwnCharPackのライト范围
        this.setChaLightSize(lightSize); // S_OtherCharPack, S_NPCPackなどのライト范围
    }

    private int _chaLightSize; // ● ライトの范围

    /**
     * 物件原始亮度
     * 
     * @return
     */
    public int getChaLightSize() {
        if (this.isInvisble()) {
            return 0;
        }
        if (ConfigOther.LIGHT) {
            return 14;
        }
        return this._chaLightSize;
    }

    /**
     * 设置原始亮度
     * 
     * @param i
     */
    public void setChaLightSize(final int i) {
        this._chaLightSize = i;
    }

    private int _ownLightSize; // ● ライトの范围(S_OwnCharPack用)

    /**
     * 传回附加亮度
     * 
     * @return
     */
    public int getOwnLightSize() {
        if (this.isInvisble()) {
            return 0;
        }
        if (ConfigOther.LIGHT) {
            return 14;
        }
        return this._ownLightSize;
    }

    /**
     * 设置附加亮度
     * 
     * @param i
     */
    public void setOwnLightSize(final int i) {
        this._ownLightSize = i;
    }

    private int _tmp; // 缓存数据

    /**
     * 传出 缓存数据
     * 
     * @return the _tmp
     */
    public int get_tmp() {
        return _tmp;
    }

    /**
     * 设置 缓存数据
     * 
     * @param tmp
     *            对 _tmp 进行设置
     */
    public void set_tmp(int tmp) {
        this._tmp = tmp;
    }

    private int _tmp_mr; // 暂存数据(MR)

    /**
     * 传出 暂存数据(MR)
     * 
     * @return the _tmp_mr
     */
    public int get_tmp_mr() {
        return _tmp_mr;
    }

    /**
     * 设置 暂存数据(MR)
     * 
     * @param tmp
     *            对 _tmp_mr 进行设置
     */
    public void set_tmp_mr(int tmp) {
        this._tmp_mr = tmp;
    }

    // 闪避率 +
    private int _dodge_up = 0;

    /**
     * 闪避增加
     * 
     * @return
     */
    public int get_dodge() {
        return _dodge_up;
    }

    /**
     * 闪避增加
     * 
     * @param i
     */
    public void add_dodge(int i) {
        _dodge_up += i;
        if (_dodge_up > 10) {
            _dodge_up = 10;

        } else if (_dodge_up < 0) {
            _dodge_up = 0;
        }
    }

    // 闪避率 -
    private int _dodge_down = 0;

    /**
     * 闪避减少
     * 
     * @return
     */
    public int get_dodge_down() {
        return _dodge_down;
    }

    /**
     * 闪避减少
     * 
     * @param i
     */
    public void add_dodge_down(int i) {
        _dodge_down += i;
        if (_dodge_down > 10) {
            _dodge_down = 10;

        } else if (_dodge_down < 0) {
            _dodge_down = 0;
        }
    }
    /**
     * 龙之门扉编号
     */
    private int _portalNumber = -1;
    
    /**
     * 取得龙之门扉编号
     */
    public int getPortalNumber() {
        return this._portalNumber;
    }
    
    /**
     * 设定龙之门扉编号
     */
    public void setPortalNumber(final int portalNumber) {
        this._portalNumber = portalNumber;
    }
}
