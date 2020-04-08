package com.lineage.server.model.Instance;

import static com.lineage.server.model.skill.L1SkillId.FOG_OF_SLEEPING;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.ActionCodes;
import com.lineage.server.IdFactory;
import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.ExpTable;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.PetItemTable;
import com.lineage.server.datatables.PetTypeTable;
import com.lineage.server.datatables.lock.PetReading;
import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.L1Location;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_HPMeter;
import com.lineage.server.serverpackets.S_NpcChat;
import com.lineage.server.serverpackets.S_PetMenuPacket;
import com.lineage.server.serverpackets.S_NPCPack_Pet;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1Pet;
import com.lineage.server.templates.L1PetItem;
import com.lineage.server.templates.L1PetType;
import com.lineage.server.utils.RangeInt;
import com.lineage.server.world.World;

/**
 * 宠物控制项
 * 
 * @author DaiEn
 * 
 */
public class L1PetInstance extends L1NpcInstance {

    private static final long serialVersionUID = 1L;

    private static final Log _log = LogFactory.getLog(L1PetInstance.class);

    private static Random _random = new Random();

    /**
     * 0:无<BR>
     * 1:攻击<BR>
     * 2:防御<BR>
     * 3:休息<BR>
     * 4:配置<BR>
     * 5:警戒<BR>
     * 6:解散<BR>
     * 7:召回
     */
    private int _currentPetStatus;

    private int _checkMove = 0;

    private L1PcInstance _petMaster;

    private int _itemObjId;

    private L1PetType _type;

    private int _expPercent;

    // ターゲットがいない场合の处理
    @Override
    public boolean noTarget() {
        switch (this._currentPetStatus) {
            case 3:// 休息
                return true;

            case 4:// 散开
                if ((_petMaster != null)
                        && (_petMaster.getMapId() == getMapId())
                        && (getLocation().getTileLineDistance(
                                _petMaster.getLocation()) < 5)) {
                    if (_npcMove != null) {
                        int dir = _npcMove.targetReverseDirection(
                                _petMaster.getX(), _petMaster.getY());
                        dir = _npcMove.checkObject(dir);
                        _npcMove.setDirectionMove(dir);
                        setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
                    }

                } else { // 主人を见失うか５マス以上はなれたら休憩状态に
                    _currentPetStatus = 3;
                    return true;
                }
                break;

            case 5:// 警戒
                if ((Math.abs(this.getHomeX() - this.getX()) > 1)
                        || (Math.abs(this.getHomeY() - this.getY()) > 1)) {
                    if (_npcMove != null) {
                        final int dir = _npcMove.moveDirection(getHomeX(),
                                getHomeY());
                        if (dir == -1) { // ホームが离れすぎてたら现在地がホーム
                            setHomeX(getX());
                            setHomeY(getY());
                        } else {
                            _npcMove.setDirectionMove(dir);
                            setSleepTime(calcSleepTime(getPassispeed(),
                                    MOVE_SPEED));
                        }
                    }
                }
                break;

            case 7:// 召回
                if ((this._petMaster != null)
                        && (this._petMaster.getMapId() == this.getMapId())
                        && (this.getLocation().getTileLineDistance(
                                this._petMaster.getLocation()) <= 1)) {
                    this._currentPetStatus = 3;
                    return true;
                }
                if (_npcMove != null) {
                    final int locx = _petMaster.getX() + _random.nextInt(1);
                    final int locy = _petMaster.getY() + _random.nextInt(1);
                    final int dirx = _npcMove.moveDirection(locx, locy);
                    if (dirx == -1) { // 主人を见失うかはなれたらその场で休憩状态に
                        _currentPetStatus = 3;
                        return true;
                    }
                    _npcMove.setDirectionMove(dirx);
                    setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
                }
                break;

            default:
                if ((this._petMaster != null)
                        && (this._petMaster.getMapId() == this.getMapId())) { // ●
                    // 主人を追尾
                    if (this.getLocation().getTileLineDistance(
                            _petMaster.getLocation()) > 2) {
                        if (_npcMove != null) {
                            final int dir = _npcMove.moveDirection(
                                    _petMaster.getX(), _petMaster.getY());
                            if (dir == -1) { // 主人が离れすぎたら休憩状态に
                                _checkMove++;
                                if (_checkMove >= 10) {
                                    // 控制者遗失
                                    // System.out.println("控制者遗失次数: "+_checkMove);
                                    _checkMove = 0;
                                    _currentPetStatus = 3;
                                    return true;
                                }

                            } else {
                                _checkMove = 0;
                                _npcMove.setDirectionMove(dir);
                                setSleepTime(calcSleepTime(getPassispeed(),
                                        MOVE_SPEED));
                            }
                        }
                    }

                } else { // ● 主人を见失ったら休憩状态に
                    _currentPetStatus = 3;
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * GM用宠物
     * 
     * @param npc
     * @param pc
     */
    public L1PetInstance(L1Npc template, L1PcInstance master) {
        super(template);

        this._petMaster = master;
        this._itemObjId = -1;
        this._type = null;

        this.setId(IdFactoryNpc.get().nextId());
        // 副本编号
        this.set_showId(master.get_showId());
        this.setName(template.get_name());
        this.setLevel(template.get_level());
        // HPMP最大质设置
        this.setMaxHp(template.get_hp());
        this.setCurrentHpDirect(template.get_hp());
        this.setMaxMp(template.get_mp());
        this.setCurrentMpDirect(template.get_mp());
        this.setExp(template.get_exp());
        this.setExpPercent(ExpTable.getExpPercentage(template.get_level(),
                template.get_exp()));
        this.setLawful(template.get_lawful());
        this.setTempLawful(template.get_lawful());

        this.setMaster(master);
        this.setX(master.getX() + _random.nextInt(5) - 2);
        this.setY(master.getY() + _random.nextInt(5) - 2);
        this.setMap(master.getMapId());
        this.setHeading(master.getHeading());
        this.setLightSize(template.getLightSize());

        this._currentPetStatus = 3;

        World.get().storeObject(this);
        World.get().addVisibleObject(this);
        for (final L1PcInstance pc : World.get().getRecognizePlayer(this)) {
            this.onPerceive(pc);
        }
        master.addPet(this);
        // 增加物件组人
        addMaster(master);
        // master.sendPackets(new S_NewMaster(master.getName(), this));
    }

    /**
     * 宠物领取
     * 
     * @param template
     *            NPC资料
     * @param master
     *            主人
     * @param pet
     *            宠物资料
     */
    public L1PetInstance(final L1Npc template, final L1PcInstance master,
            final L1Pet pet) {
        super(template);

        this._petMaster = master;
        this._itemObjId = pet.get_itemobjid();
        this._type = PetTypeTable.getInstance().get(template.get_npcId());

        // ステータスを上书き
        this.setId(pet.get_objid());
        // 副本编号
        this.set_showId(master.get_showId());
        this.setName(pet.get_name());
        this.setLevel(pet.get_level());
        // HPMPはMAXとする
        this.setMaxHp(pet.get_hp());
        this.setCurrentHpDirect(pet.get_hp());
        this.setMaxMp(pet.get_mp());
        this.setCurrentMpDirect(pet.get_mp());
        this.setExp(pet.get_exp());
        this.setExpPercent(ExpTable.getExpPercentage(pet.get_level(),
                pet.get_exp()));
        this.setLawful(pet.get_lawful());
        this.setTempLawful(pet.get_lawful());

        this.setMaster(master);
        this.setX(master.getX() + _random.nextInt(5) - 2);
        this.setY(master.getY() + _random.nextInt(5) - 2);
        this.setMap(master.getMapId());
        this.setHeading(5);
        this.setLightSize(template.getLightSize());

        this._currentPetStatus = 3;

        World.get().storeObject(this);
        World.get().addVisibleObject(this);

        for (final L1PcInstance pc : World.get().getRecognizePlayer(this)) {
            this.onPerceive(pc);
        }
        master.addPet(this);
        // 增加物件组人
        addMaster(master);
        // master.sendPackets(new S_NewMaster(master.getName(), this));
    }

    /**
     * 宠物抓取
     * 
     * @param target
     * @param master
     *            主人
     * @param itemid
     *            项圈OBJID
     */
    public L1PetInstance(final L1NpcInstance target, final L1PcInstance master,
            final int itemid) {
        super(null);

        this._petMaster = master;
        this._itemObjId = itemid;
        this._type = PetTypeTable.getInstance().get(
                target.getNpcTemplate().get_npcId());

        // ステータスを上书き
        this.setId(IdFactory.get().nextId());
        // 副本编号
        this.set_showId(master.get_showId());
        this.setting_template(target.getNpcTemplate());
        this.setCurrentHpDirect(target.getCurrentHp());
        this.setCurrentMpDirect(target.getCurrentMp());
        this.setExp(750); // Lv.5のEXP
        this.setExpPercent(0);
        this.setLawful(0);
        this.setTempLawful(0);

        this.setMaster(master);
        this.setX(target.getX());
        this.setY(target.getY());
        this.setMap(target.getMapId());
        this.setHeading(target.getHeading());
        this.setLightSize(target.getLightSize());
        this.setPetcost(6);
        this.setInventory(target.getInventory());
        target.setInventory(null);

        this._currentPetStatus = 3;

        target.deleteMe();
        World.get().storeObject(this);
        World.get().addVisibleObject(this);

        for (final L1PcInstance pc : World.get().getRecognizePlayer(this)) {
            this.onPerceive(pc);
        }

        master.addPet(this);
        PetReading.get().storeNewPet(target, this.getId(), itemid);
        // 增加物件组人
        addMaster(master);
        // master.sendPackets(new S_NewMaster(master.getName(), this));
    }

    /**
     * 宠物取得
     * 
     * @param npcid
     *            宠物npcid
     * @param master
     *            主人
     * @param itemid
     *            项圈OBJID
     */
    public L1PetInstance(final int npcid, final L1PcInstance master,
            final int itemid) {
        super(null);

        this._petMaster = master;
        this._itemObjId = itemid;
        this._type = PetTypeTable.getInstance().get(npcid);
        final L1Npc npc = NpcTable.get().getTemplate(npcid);

        // ステータスを上书き
        this.setId(IdFactory.get().nextId());
        // 副本编号
        this.set_showId(master.get_showId());

        this.setting_template(npc);
        this.setCurrentHpDirect(npc.get_hp());
        this.setCurrentMpDirect(npc.get_mp());

        long exp = 750;// 5
        if (npcid == 71020) {// 顽皮龙
            exp = 55810962;// 50
        }
        if (npcid == 71019) {// 淘气龙
            exp = 560722250;// 64
        }
        this.setExp(exp); // EXP
        this.setLevel(ExpTable.getLevelByExp(exp));
        this.setExpPercent(0);
        this.setLawful(0);
        this.setTempLawful(0);

        this.setMaster(master);

        // 随机周边座标
        final L1Location loc = master.getLocation().randomLocation(5, false);
        this.setX(loc.getX());
        this.setY(loc.getY());
        this.setMap((short) loc.getMapId());
        this.setHeading(5);
        this.setLightSize(npc.getLightSize());
        this.setPetcost(6);

        this._currentPetStatus = 3;

        World.get().storeObject(this);
        World.get().addVisibleObject(this);

        for (final L1PcInstance pc : World.get().getRecognizePlayer(this)) {
            this.onPerceive(pc);
        }

        master.addPet(this);

        // final L1NpcInstance target = NpcTable.get().newNpcInstance(npcid);
        PetReading.get().storeNewPet(this, this.getId(), itemid);
        // 增加物件组人
        addMaster(master);
        // master.sendPackets(new S_NewMaster(master.getName(), this));
    }

    /**
     * 受到攻击HP减少
     */
    @Override
    public void receiveDamage(final L1Character attacker, final int damage) {
        ISASCAPE = false;
        // System.out.println("攻击目标设置:"+attacker.getName() + " h:" + damage);
        if (this.getCurrentHp() > 0) {
            if (damage > 0) { // 回复の场合は攻击しない。
                this.setHate(attacker, 0); // ペットはヘイト无し
                this.removeSkillEffect(FOG_OF_SLEEPING);
            }

            if ((attacker instanceof L1PcInstance) && (damage > 0)) {
                final L1PcInstance player = (L1PcInstance) attacker;
                player.setPetTarget(this);
            }

            final int newHp = this.getCurrentHp() - damage;
            if (newHp <= 0) {
                this.death(attacker);
            } else {
                this.setCurrentHp(newHp);
            }

        } else if (!this.isDead()) { // 念のため
            this.death(attacker);
        }
    }

    public synchronized void death(final L1Character lastAttacker) {
        if (!this.isDead()) {
        	final int oldLevel = this.getLevel();//
            final long needExp = ExpTable.getNeedExpNextLevel(oldLevel);//
            final long death_exp = (long) (needExp * 0.1);//
            this.setExp(this.getExp() - death_exp);//
            this.setLevel(ExpTable.getLevelByExp(this.getExp()));//
            this.setExpPercent(ExpTable.getExpPercentage(this.getLevel(),//
                    this.getExp()));//
            if (this.getLevel() < oldLevel) {//
                final RangeInt hpUpRange = this.getPetType().getHpUpRange();//
                final RangeInt mpUpRange = this.getPetType().getMpUpRange();//
                this.addMaxHp(-hpUpRange.randomValue());//
                this.addMaxMp(-mpUpRange.randomValue());//
            }
            this.setDead(true);
            this.setStatus(ActionCodes.ACTION_Die);
            this.setCurrentHp(0);

            this.getMap().setPassable(this.getLocation(), true);
            this.broadcastPacketAll(new S_DoActionGFX(this.getId(),
                    ActionCodes.ACTION_Die));
        }
    }

    /**
     * 进化宠物
     * 
     * @param new_itemobjid
     */
    public void evolvePet(final int new_itemobjid) {
        final L1Pet pet = PetReading.get().getTemplate(this._itemObjId);
        if (pet == null) {
            return;
        }

        final int newNpcId = this._type.getNpcIdForEvolving();
        // 进化前のmaxHp,maxMpを退避
        final int tmpMaxHp = this.getMaxHp();
        final int tmpMaxMp = this.getMaxMp();

        this.transform(newNpcId);
        this._type = PetTypeTable.getInstance().get(newNpcId);

        this.setLevel(1);
        // HPMPを元の半分にする
        this.setMaxHp(tmpMaxHp / 2);
        this.setMaxMp(tmpMaxMp / 2);
        this.setCurrentHpDirect(this.getMaxHp());
        this.setCurrentMpDirect(this.getMaxMp());
        this.setExp(0);
        this.setExpPercent(0);

        // インベントリを空にする
        this.getInventory().clearItems();

        // 古いペットをDBから消す
        PetReading.get().deletePet(this._itemObjId);

        // 新しいペットをDBに书き迂む
        pet.set_itemobjid(new_itemobjid);
        pet.set_npcid(newNpcId);
        pet.set_name(this.getName());
        pet.set_level(this.getLevel());
        pet.set_hp(this.getMaxHp());
        pet.set_mp(this.getMaxMp());
        pet.set_exp((int) this.getExp());
        PetReading.get().storeNewPet(this, this.getId(), new_itemobjid);

        this._itemObjId = new_itemobjid;
    }

    /**
     * 解散宠物
     */
    public void liberate() {
        final L1MonsterInstance monster = new L1MonsterInstance(
                this.getNpcTemplate());
        monster.setId(IdFactoryNpc.get().nextId());

        monster.setX(this.getX());
        monster.setY(this.getY());
        monster.setMap(this.getMapId());
        monster.setHeading(this.getHeading());
        monster.set_storeDroped(true);
        monster.setInventory(this.getInventory());
        this.setInventory(null);
        monster.setLevel(this.getLevel());
        monster.setMaxHp(this.getMaxHp());
        monster.setCurrentHpDirect(this.getCurrentHp());
        monster.setMaxMp(this.getMaxMp());
        monster.setCurrentMpDirect(this.getCurrentMp());

        this._petMaster.getPetList().remove(this.getId());
        this.deleteMe();

        this._petMaster.getInventory().removeItem(this._itemObjId, 1);
        PetReading.get().deletePet(this._itemObjId);

        World.get().storeObject(monster);
        World.get().addVisibleObject(monster);
        for (final L1PcInstance pc : World.get().getRecognizePlayer(monster)) {
            this.onPerceive(pc);
        }
    }

    /**
     * 收集
     * 
     * @param isDepositnpc
     *            宠物是否进入管理所
     */
    public void collect(final boolean isDepositnpc) {
        L1Inventory masterInv = this._petMaster.getInventory();
        final List<L1ItemInstance> items = this._inventory.getItems();

        for (final L1ItemInstance item : items) {
            // _log.info(item.getItem().getName());
            if (item.isEquipped()) { // 使用中
                if (isDepositnpc) {// 宠物进入管理所
                    final int itemId = item.getItem().getItemId();
                    final L1PetItem petItem = PetItemTable.get().getTemplate(
                            itemId);
                    // 解除使用状态
                    if (petItem != null) {
                        this.setHitByWeapon(0);
                        this.setDamageByWeapon(0);
                        this.addStr(-petItem.getAddStr());
                        this.addCon(-petItem.getAddCon());
                        this.addDex(-petItem.getAddDex());
                        this.addInt(-petItem.getAddInt());
                        this.addWis(-petItem.getAddWis());
                        this.addMaxHp(-petItem.getAddHp());
                        this.addMaxMp(-petItem.getAddMp());
                        this.addSp(-petItem.getAddSp());
                        this.addMr(-petItem.getAddMr());

                        this.setWeapon(null);
                        this.setArmor(null);
                        item.setEquipped(false);
                    }
                    // item.setEquipped(false);

                } else {
                    continue;
                }
            }
            // 容量重量确认
            if (this._petMaster.getInventory().checkAddItem(item,
                    item.getCount()) == L1Inventory.OK) {
                this._inventory.tradeItem(item, item.getCount(), masterInv);
                // 143 \f1%0%s 给你 %1%o 。
                this._petMaster.sendPackets(new S_ServerMessage(143, this
                        .getName(), item.getLogName()));

            } else {
                item.set_showId(this.get_showId());
                // 过重 变更主人背包为地面
                masterInv = World.get().getInventory(this.getX(), this.getY(),
                        this.getMapId());
                this._inventory.tradeItem(item, item.getCount(), masterInv);
            }
        }
        this.savePet();
    }

    /**
     * 背包内物品的掉落
     */
    public void dropItem() {
        final L1Inventory worldInv = World.get().getInventory(this.getX(),
                this.getY(), this.getMapId());
        // 取回背包物件
        final List<L1ItemInstance> items = this._inventory.getItems();

        /*for (final L1ItemInstance item : items) {
            item.set_showId(this.get_showId());
            if (item.isEquipped()) { // 使用中
                final int itemId = item.getItem().getItemId();
                final L1PetItem petItem = PetItemTable.get()
                        .getTemplate(itemId);
                // 解除使用状态
                if (petItem != null) {
                    this.setHitByWeapon(0);
                    this.setDamageByWeapon(0);
                    this.addStr(-petItem.getAddStr());
                    this.addCon(-petItem.getAddCon());
                    this.addDex(-petItem.getAddDex());
                    this.addInt(-petItem.getAddInt());
                    this.addWis(-petItem.getAddWis());
                    this.addMaxHp(-petItem.getAddHp());
                    this.addMaxMp(-petItem.getAddMp());
                    this.addSp(-petItem.getAddSp());
                    this.addMr(-petItem.getAddMr());

                    this.setWeapon(null);
                    this.setArmor(null);
                    item.setEquipped(false);
                }
            }
            // item.setEquipped(false);
            this._inventory.tradeItem(item, item.getCount(), worldInv);
        }*/
        //修改下线后宠物身上的物品回到主人身上,如想恢复请删除下面相于的段恢复便用上面这段 hjx1000
        L1Inventory masterInv = this._petMaster.getInventory();
        for (final L1ItemInstance item : items) {
            item.set_showId(this.get_showId());
            if (item.isEquipped()) { // 使用中
                final int itemId = item.getItem().getItemId();
                final L1PetItem petItem = PetItemTable.get()
                        .getTemplate(itemId);
                // 解除使用状态
                if (petItem != null) {
                    this.setHitByWeapon(0);
                    this.setDamageByWeapon(0);
                    this.addStr(-petItem.getAddStr());
                    this.addCon(-petItem.getAddCon());
                    this.addDex(-petItem.getAddDex());
                    this.addInt(-petItem.getAddInt());
                    this.addWis(-petItem.getAddWis());
                    this.addMaxHp(-petItem.getAddHp());
                    this.addMaxMp(-petItem.getAddMp());
                    this.addSp(-petItem.getAddSp());
                    this.addMr(-petItem.getAddMr());

                    this.setWeapon(null);
                    this.setArmor(null);
                    item.setEquipped(false);
                }
            }
            // 容量重量确认
            if (this._petMaster.getInventory().checkAddItem(item,
                    item.getCount()) == L1Inventory.OK) {
                this._inventory.tradeItem(item, item.getCount(), masterInv);
            } else {
                item.set_showId(this.get_showId());
                // 过重 变更主人背包为地面
                masterInv = World.get().getInventory(this.getX(), this.getY(),
                        this.getMapId());
                this._inventory.tradeItem(item, item.getCount(), masterInv);
            }
        }
        // 存档
        this.savePet();
    }

    /**
     * 宠物资料存档
     */
    private void savePet() {
        try {
            final L1Pet pet = PetReading.get().getTemplate(this._itemObjId);

            if (pet != null) {
                pet.set_exp((int) this.getExp());
                pet.set_level(this.getLevel());
                pet.set_hp(this.getMaxHp());
                pet.set_mp(this.getMaxMp());
                PetReading.get().storePet(pet);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 主人笛子使用
     */
    public void call() {
        if (this._type != null) {
            final int id = this._type.getMessageId(L1PetType
                    .getMessageNumber(this.getLevel()));
            if (id != 0) {
                this.broadcastPacketX8(new S_NpcChat(this, "$" + id));
            }
        }

        // 移动至主人身边休息
        this.setCurrentPetStatus(7);
    }

    /**
     * 设置目标
     * 
     * @param target
     */
    public void setTarget(final L1Character target) {
        if ((target != null) && ((this._currentPetStatus == 1) || // 攻击
                (this._currentPetStatus == 2) || // 防御
                (this._currentPetStatus == 5)// 警戒
                )) {
            this.setHate(target, 0);
            if (!this.isAiRunning()) {
                this.startAI();
            }
        }
    }

    /**
     * 设置主人目标
     * 
     * @param target
     */
    public void setMasterTarget(final L1Character target) {
        // System.out.println("设置主人目标");
        if ((target != null) && ((_currentPetStatus == 1) || // 攻击
                (_currentPetStatus == 5)// 警戒
                )) {
        	if (target instanceof L1PcInstance) { //修正安全区还会跟随主人攻击 hjx1000
        		if (target.isSafetyZone()) {
        			return;
        		}
        	}
            setHate(target, 10);
            if (!isAiRunning()) {
                // System.out.println("设置主人目标 startAI");
                startAI();
            }
        }
    }

    /**
     * 设置主人指定目标
     * 
     * @param target
     */
    public void setMasterSelectTarget(final L1Character target) {
        // System.out.println("设置主人指定目标:" + target);
        // 目标不为空
        if (target != null) {
            this.setHate(target, 0);
            if (!this.isAiRunning()) {
                this.startAI();
            }
        }
    }

    /**
     * TODO 接触资讯
     */
    @Override
    public void onPerceive(final L1PcInstance perceivedFrom) {
        try {
            // 副本ID不相等 不相护显示
            if (perceivedFrom.get_showId() != get_showId()) {
                return;
            }
            perceivedFrom.addKnownObject(this);
            perceivedFrom.sendPackets(new S_NPCPack_Pet(this, perceivedFrom)); // ペット系オブジェクト认识
            if (getMaster() != null) {
                if (perceivedFrom.getId() == getMaster().getId()) {
                    perceivedFrom.sendPackets(new S_HPMeter(getId(), 100
                            * getCurrentHp() / getMaxHp()));
                }
            }
            if (isDead()) {
                perceivedFrom.sendPackets(new S_DoActionGFX(getId(),
                        ActionCodes.ACTION_Die));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 对该物件攻击的调用
     */
    @Override
    public void onAction(final L1PcInstance player) {
        if (player == null) {
            return;
        }
        final L1Character cha = this.getMaster();
        if (cha == null) {
            return;
        }
        final L1PcInstance master = (L1PcInstance) cha;
        if (master.isTeleport()) { // 传送处理中
            return;
        }
        if (master.equals(player)) {// 攻击者是主人
            final L1AttackMode attack_mortion = new L1AttackPc(player, this); // 攻击判断
            attack_mortion.action();
            return;
        }
        if (this.isSafetyZone() || player.isSafetyZone()) {// 安全区域中
            final L1AttackMode attack_mortion = new L1AttackPc(player, this); // 攻击判断
            attack_mortion.action();
            return;
        }

        if (player.checkNonPvP(player, this)) {
            return;
        }

        final L1AttackMode attack = new L1AttackPc(player, this);
        if (attack.calcHit()) {
            attack.calcDamage();
        }
        attack.action();
        attack.commit();
    }

    @Override
    public void onTalkAction(final L1PcInstance player) {
        if (this.isDead()) {
            return;
        }
        if (this._petMaster.equals(player)) {
            player.sendPackets(new S_PetMenuPacket(this, this.getExpPercent()));
            this.savePet();
        }
    }

    @Override
    public void onFinalAction(final L1PcInstance player, final String action) {
        final int status = Integer.parseInt(action);
        // int status = actionType(action);
        switch (status) {
            case 0:
                return;

            case 6:
                this.liberate(); // ペットの解放
                break;

            default:
                // 同じ主人のペットの状态をすべて更新
                final Object[] petList = this._petMaster.getPetList().values()
                        .toArray();
                for (final Object petObject : petList) {
                    if (petObject instanceof L1PetInstance) { // ペット
                        final L1PetInstance pet = (L1PetInstance) petObject;
                        if (this._petMaster != null) {
                            if (this._petMaster.isGm()) {
                                pet.setCurrentPetStatus(status);
                                continue;
                            }

                            // 等级高于宠物
                            if (this._petMaster.getLevel() >= pet.getLevel()) {
                                pet.setCurrentPetStatus(status);

                            } else {
                                // 取回宠物分类
                                // final L1PetType type =
                                // PetTypeTable.getInstance().get(pet.getNpcTemplate().get_npcId());
                                if (this._type != null) {
                                    final int id = this._type
                                            .getDefyMessageId();
                                    if (id != 0) {
                                        this.broadcastPacketX8(new S_NpcChat(
                                                pet, "$" + id));
                                    }
                                }
                            }

                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onItemUse() {
        if (!this.isActived()) {
            this.useItem(USEITEM_HASTE, 100); // １００％の确率でヘイストポーション使用
        }
        if (this.getCurrentHp() * 100 / this.getMaxHp() < 40) { // ＨＰが４０％きったら
            this.useItem(USEITEM_HEAL, 100); // １００％の确率で回复ポーション使用
        }
    }

    @Override
    public void onGetItem(final L1ItemInstance item) {
        if (this.getNpcTemplate().get_digestitem() > 0) {
            this.setDigestItem(item);
        }
        Arrays.sort(healPotions);
        Arrays.sort(haestPotions);
        if (Arrays.binarySearch(healPotions, item.getItem().getItemId()) >= 0) {
            if (this.getCurrentHp() != this.getMaxHp()) {
                this.useItem(USEITEM_HEAL, 100);
            }

        } else if (Arrays
                .binarySearch(haestPotions, item.getItem().getItemId()) >= 0) {
            this.useItem(USEITEM_HASTE, 100);
        }
    }

    @Override
    public void setCurrentHp(final int i) {
        final int currentHp = Math.min(i, this.getMaxHp());

        if (this.getCurrentHp() == currentHp) {
            return;
        }

        this.setCurrentHpDirect(currentHp);

        // 宠物血条更新
        if (this._petMaster != null) {
            final int hpRatio = 100 * currentHp / this.getMaxHp();
            final L1PcInstance master = this._petMaster;
            master.sendPackets(new S_HPMeter(this.getId(), hpRatio));
        }
    }

    @Override
    public void setCurrentMp(final int i) {
        final int currentMp = Math.min(i, this.getMaxMp());

        if (this.getCurrentMp() == currentMp) {
            return;
        }

        this.setCurrentMpDirect(currentMp);
    }

    /**
     * 宠物状态
     * 
     * @param i
     */
    public void setCurrentPetStatus(final int i) {
        // System.out.println("宠物状态:" + i);
        this._currentPetStatus = i;
        set_tempModel();
        switch (this._currentPetStatus) {
            case 5:// 警戒
                this.setHomeX(this.getX());
                this.setHomeY(this.getY());
                break;

            case 3:// 休息
                this.allTargetClear();
                break;

            default:
                if (!this.isAiRunning()) {
                    this.startAI();
                }
                break;
        }
    }

    /**
     * 宠物状态<BR>
     * <BR>
     * 0:无<BR>
     * 1:攻击<BR>
     * 2:防御<BR>
     * 3:休息<BR>
     * 4:配置<BR>
     * 5:警戒<BR>
     * 6:解散<BR>
     * 7:召回
     * 
     * @return
     */
    public int getCurrentPetStatus() {
        return this._currentPetStatus;
    }

    public int getItemObjId() {
        return this._itemObjId;
    }

    public void setExpPercent(final int expPercent) {
        this._expPercent = expPercent;
    }

    public int getExpPercent() {
        return this._expPercent;
    }

    private L1ItemInstance _weapon;

    public void setWeapon(final L1ItemInstance weapon) {
        this._weapon = weapon;
    }

    public L1ItemInstance getWeapon() {
        return this._weapon;
    }

    private L1ItemInstance _armor;

    public void setArmor(final L1ItemInstance armor) {
        this._armor = armor;
    }

    public L1ItemInstance getArmor() {
        return this._armor;
    }

    private int _hitByWeapon;

    public void setHitByWeapon(final int i) {
        this._hitByWeapon = i;
    }

    public int getHitByWeapon() {
        return this._hitByWeapon;
    }

    private int _damageByWeapon;

    public void setDamageByWeapon(final int i) {
        this._damageByWeapon = i;
    }

    public int getDamageByWeapon() {
        return this._damageByWeapon;
    }

    public L1PetType getPetType() {
        return this._type;
    }

    private int _tempModel = 3;

    public void set_tempModel() {
        _tempModel = _currentPetStatus;
    }

    public void get_tempModel() {
        _currentPetStatus = _tempModel;
    }
}
