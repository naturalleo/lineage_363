package com.lineage.server.model;

import static com.lineage.server.model.skill.L1SkillId.CANCELLATION;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

/**
 * 鬼屋控制项
 * 
 * @author daien
 * 
 */
public class L1HauntedHouse {
    /*
     * private static final Log _log = LogFactory.getLog(L1HauntedHouse.class
     * .getName());
     */

    public static final int STATUS_NONE = 0;
    public static final int STATUS_READY = 1;
    public static final int STATUS_PLAYING = 2;

    private final ArrayList<L1PcInstance> _members = new ArrayList<L1PcInstance>();
    private int _hauntedHouseStatus = STATUS_NONE;
    private int _winnersCount = 0;
    private int _goalCount = 0;

    private static L1HauntedHouse _instance;

    public static L1HauntedHouse getInstance() {
        if (_instance == null) {
            _instance = new L1HauntedHouse();
        }
        return _instance;
    }

    private void readyHauntedHouse() {
        this.setHauntedHouseStatus(STATUS_READY);
        final L1HauntedHouseReadyTimer hhrTimer = new L1HauntedHouseReadyTimer();
        hhrTimer.begin();
    }

    private void startHauntedHouse() {
    	//System.out.println("鬼屋开始");
        this.setHauntedHouseStatus(STATUS_PLAYING);
        final int membersCount = this.getMembersCount();
        if (membersCount <= 4) {
            this.setWinnersCount(1);
        } else if ((5 >= membersCount) && (membersCount <= 7)) {
            this.setWinnersCount(2);
        } else if ((8 >= membersCount) && (membersCount <= 10)) {
            this.setWinnersCount(3);
        }
        for (final L1PcInstance pc : this.getMembersArray()) {
            final L1SkillUse l1skilluse = new L1SkillUse();
            l1skilluse.handleCommands(pc, CANCELLATION, pc.getId(), pc.getX(),
                    pc.getY(), 0, L1SkillUse.TYPE_LOGIN);
            L1PolyMorph.doPoly(pc, 6284, 300, L1PolyMorph.MORPH_BY_NPC);
        }

        for (final L1Object object : World.get().getObject()) {
            if (object instanceof L1DoorInstance) {
                final L1DoorInstance door = (L1DoorInstance) object;
                if (door.getMapId() == 5140) {
                    door.open();
                }
            }
        }
    }

    public void endHauntedHouse() {
    	//System.out.println("鬼屋结束");
        //this.setHauntedHouseStatus(STATUS_NONE);
        this.setWinnersCount(0);
        this.setGoalCount(0);
        for (final L1PcInstance pc : this.getMembersArray()) {
            if (pc.getMapId() == 5140) {
                final L1SkillUse l1skilluse = new L1SkillUse();
                l1skilluse.handleCommands(pc, CANCELLATION, pc.getId(),
                        pc.getX(), pc.getY(), 0, L1SkillUse.TYPE_LOGIN);
                L1Teleport.teleport(pc, 32624, 32813, (short) 4, 5, true);
            }
        }
        this.clearMembers();
        for (final L1Object object : World.get().getObject()) {
            if (object instanceof L1DoorInstance) {
                final L1DoorInstance door = (L1DoorInstance) object;
                if (door.getMapId() == 5140) {
                    door.close();
                }
            }
        }
    }

    public void removeRetiredMembers() {
        final L1PcInstance[] temp = this.getMembersArray();
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].getMapId() != 5140) {
                this.removeMember(temp[i]);
            }
        }
    }

    public void sendMessage(final int type, final String msg) {
        for (final L1PcInstance pc : this.getMembersArray()) {
            pc.sendPackets(new S_ServerMessage(type, msg));
        }
    }

    public void addMember(final L1PcInstance pc) {
        if (!this._members.contains(pc)) {
            this._members.add(pc);
        }
        if ((this.getMembersCount() == 1)
                && (this.getHauntedHouseStatus() == STATUS_NONE)) {
            this.readyHauntedHouse();
        }
    }

    public void removeMember(final L1PcInstance pc) {
        this._members.remove(pc);
    }

    public void clearMembers() {
        this._members.clear();
    }

    public boolean isMember(final L1PcInstance pc) {
        return this._members.contains(pc);
    }

    public L1PcInstance[] getMembersArray() {
        return this._members.toArray(new L1PcInstance[this._members.size()]);
    }

    public int getMembersCount() {
        return this._members.size();
    }

    private void setHauntedHouseStatus(final int i) {
        this._hauntedHouseStatus = i;
    }

    public int getHauntedHouseStatus() {
        return this._hauntedHouseStatus;
    }

    private void setWinnersCount(final int i) {
        this._winnersCount = i;
    }

    public int getWinnersCount() {
        return this._winnersCount;
    }

    public void setGoalCount(final int i) {
        this._goalCount = i;
    }

    public int getGoalCount() {
        return this._goalCount;
    }

    public class L1HauntedHouseReadyTimer extends TimerTask {

        public L1HauntedHouseReadyTimer() {
        }

        @Override
        public void run() {
            L1HauntedHouse.this.startHauntedHouse();
            final L1HauntedHouseTimer hhTimer = new L1HauntedHouseTimer();
            hhTimer.begin();
        }

        public void begin() {
            final Timer timer = new Timer();
            timer.schedule(this, 90000); // 90秒くらい？
        }

    }

    public class L1HauntedHouseTimer extends TimerTask {

        public L1HauntedHouseTimer() {
        }

        @Override
        public void run() {
            L1HauntedHouse.this.endHauntedHouse();
            L1HauntedHouse.this.setHauntedHouseStatus(STATUS_NONE);
            this.cancel();
        }

        public void begin() {
            final Timer timer = new Timer();
            timer.schedule(this, 300000); // 5分
        }
    }
}
