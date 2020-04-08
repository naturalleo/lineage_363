package com.lineage.server.datatables.lock;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.AuctionBoardTable;
import com.lineage.server.datatables.storage.AuctionBoardStorage;
import com.lineage.server.templates.L1AuctionBoardTmp;

/**
 * 拍卖公告栏资料
 * 
 * @author dexc
 * 
 */
public class AuctionBoardReading {

    private final Lock _lock;

    private final AuctionBoardStorage _storage;

    private static AuctionBoardReading _instance;

    private AuctionBoardReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new AuctionBoardTable();
    }

    public static AuctionBoardReading get() {
        if (_instance == null) {
            _instance = new AuctionBoardReading();
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
     * 传回公告阵列
     */
    public Map<Integer, L1AuctionBoardTmp> getAuctionBoardTableList() {
        this._lock.lock();
        Map<Integer, L1AuctionBoardTmp> tmp;
        try {
            tmp = this._storage.getAuctionBoardTableList();
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 传回指定公告
     */
    public L1AuctionBoardTmp getAuctionBoardTable(final int houseId) {
        this._lock.lock();
        L1AuctionBoardTmp tmp;
        try {
            tmp = this._storage.getAuctionBoardTable(houseId);
        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    /**
     * 增加公告
     */
    public void insertAuctionBoard(final L1AuctionBoardTmp board) {
        this._lock.lock();
        try {
            this._storage.insertAuctionBoard(board);
        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 更新公告
     */
    public void updateAuctionBoard(final L1AuctionBoardTmp board) {
        this._lock.lock();
        try {
            this._storage.updateAuctionBoard(board);
        } finally {
            this._lock.unlock();
        }
    }

    /**
     * 删除公告
     */
    public void deleteAuctionBoard(final int houseId) {
        this._lock.lock();
        try {
            this._storage.deleteAuctionBoard(houseId);
        } finally {
            this._lock.unlock();
        }
    }

}
