package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.PetTable;
import com.lineage.server.datatables.storage.PetStorage;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.templates.L1Pet;

/**
 * 宠物资料表
 * 
 * @author dexc
 * 
 */
public class PetReading {

    private final Lock _lock;

    private final PetStorage _storage;

    private static PetReading _instance;

    private PetReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new PetTable();
    }

    public static PetReading get() {
        if (_instance == null) {
            _instance = new PetReading();
        }
        return _instance;
    }

    public void load() {
        this._lock.lock();
        try {
            this._storage.load();
        } finally {
            this._lock.unlock();
        }
    }

    public void storeNewPet(final L1NpcInstance pet, final int objid,
            final int itemobjid) {
        this._lock.lock();
        try {
            this._storage.storeNewPet(pet, objid, itemobjid);
        } finally {
            this._lock.unlock();
        }
    }

    public void storePet(final L1Pet pet) {
        this._lock.lock();
        try {
            this._storage.storePet(pet);
        } finally {
            this._lock.unlock();
        }
    }

    public void deletePet(final int itemobjid) {
        this._lock.lock();
        try {
            this._storage.deletePet(itemobjid);
        } finally {
            this._lock.unlock();
        }
    }

    public boolean isNameExists(final String nameCaseInsensitive) {
        this._lock.lock();
        boolean tmp;
        try {
            tmp = this._storage.isNameExists(nameCaseInsensitive);
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    public L1Pet getTemplate(final int itemobjid) {
        this._lock.lock();
        L1Pet tmp;
        try {
            tmp = this._storage.getTemplate(itemobjid);
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 宠物资料
     * 
     * @param npcobjid
     *            宠物OBJID
     * @return
     */
    public L1Pet getTemplateX(final int npcobjid) {
        this._lock.lock();
        L1Pet tmp;
        try {
            tmp = this._storage.getTemplateX(npcobjid);
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    public L1Pet[] getPetTableList() {
        this._lock.lock();
        L1Pet[] tmp;
        try {
            tmp = this._storage.getPetTableList();
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }
}
