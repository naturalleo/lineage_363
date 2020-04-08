package com.lineage.server.datatables.storage;

import java.util.Map;

import com.lineage.server.templates.L1AuctionBoardTmp;

/**
 * 拍卖公告栏资料
 * 
 * @author dexc
 * 
 */
public interface AuctionBoardStorage {

    /**
     * 初始化载入
     */
    public void load();

    /**
     * 传回公告阵列
     */
    public Map<Integer, L1AuctionBoardTmp> getAuctionBoardTableList();

    /**
     * 传回指定公告
     */
    public L1AuctionBoardTmp getAuctionBoardTable(int houseId);

    /**
     * 增加公告
     */
    public void insertAuctionBoard(L1AuctionBoardTmp board);

    /**
     * 更新公告
     */
    public void updateAuctionBoard(L1AuctionBoardTmp board);

    /**
     * 删除公告
     */
    public void deleteAuctionBoard(int houseId);

}
