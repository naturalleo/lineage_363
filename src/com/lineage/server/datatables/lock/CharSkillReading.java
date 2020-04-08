package com.lineage.server.datatables.lock;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.CharSkillTable;
import com.lineage.server.datatables.storage.CharSkillStorage;
import com.lineage.server.templates.L1UserSkillTmp;

/**
 * 人物技能纪录
 * 
 * @author dexc
 * 
 */
public class CharSkillReading {

    private final Lock _lock;

    private final CharSkillStorage _storage;

    private static CharSkillReading _instance;

    private CharSkillReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new CharSkillTable();
    }

    public static CharSkillReading get() {
        if (_instance == null) {
            _instance = new CharSkillReading();
        }
        return _instance;
    }

    /**
     * 初始化载入
     */
    public void load() {
        this._lock.lock();
        try {
            this._storage.load();
        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 取回该人物技能列表
     * 
     * @param pc
     * @return
     */
    public ArrayList<L1UserSkillTmp> skills(final int playerobjid) {
        this._lock.lock();
        ArrayList<L1UserSkillTmp> tmp;
        try {
            tmp = this._storage.skills(playerobjid);
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 增加技能
     */
    public void spellMastery(final int playerobjid, final int skillid,
            final String skillname, final int active, final int time) {
        this._lock.lock();
        try {
            if (skillname.equals("none")) {
                return;
            }
            this._storage.spellMastery(playerobjid, skillid, skillname, active,
                    time);
        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 删除技能
     */
    public void spellLost(final int playerobjid, final int skillid) {
        this._lock.lock();
        try {
            this._storage.spellLost(playerobjid, skillid);
        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 检查技能是否重复
     */
    public boolean spellCheck(final int playerobjid, final int skillid) {
        this._lock.lock();
        boolean tmp;
        try {
            tmp = this._storage.spellCheck(playerobjid, skillid);
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 设置自动技能状态
     */
    public void setAuto(final int mode, final int objid, final int skillid) {
        this._lock.lock();
        try {
            this._storage.setAuto(mode, objid, skillid);
        } finally {
            this._lock.unlock();
        }
    }
}
