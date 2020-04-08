package com.lineage.data.item_etcitem;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Party;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.WorldQuest;

/**
 * 藏宝图55125
 */
public class Treasure_Map extends ItemExecutor {
	private static final Log _log = LogFactory.getLog(Treasure_Map.class);
	private static final Random _random = new Random();
    private static final int[] lv80mob = { 96005, 96006, 96007, 96008, 96009, 96010,
    	96011, 96012, 96013, 96014, 96015 };
    private static final int[] lv75mob = { 96016, 96017, 96018, 96019, 96020, 96021, 
    	96022, 96023, 96024, 96025, 96026 };
    private static final int[] lv70mob = { 96027, 96028, 96029, 96030, 96031, 96032, 
    	96033, 96034, 96035, 96036, 96037 };
    private static final int[] lv60mob = { 96038, 96039, 96040, 96041, 96042, 96043, 
    	96044, 96045, 96046, 96047, 96048 };
    private static final int[] lv50mob = { 96049, 96050, 96051, 96052, 96053, 96054, 
    	96055, 96056, 96057, 96058, 96059 };
    /**
	 *
	 */
    private Treasure_Map() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Treasure_Map();
    }

    /**
     * 道具物件执行
     * 
     * @param data
     *            参数
     * @param pc
     *            执行者
     * @param item
     *            物件
     */
    @Override
    public void execute(final int[] data, final L1PcInstance pc,
            final L1ItemInstance item) {
    	int pcslevel = pc.getLevel();//队伍平均等级
    	final L1Party party = pc.getParty();
    	final int pcx = pc.getX();
    	final int pcy = pc.getY();
    	final int mapid = pc.getMapId();
    	final int rndx = item.getRndx();
    	final int rndy = item.getRndy();
        if (pcx == rndx && pcy == rndy && mapid == 4) {
        	final int questid = 145;//进入藏宝图任务编号  145
        	final int questmapid = 5153; //进入藏宝图任务 地图ID
            // 取回新的任务副本编号
            final int showId = WorldQuest.get().nextId();
            L1QuestUser quest = null;
        	final L1Map mapData = L1WorldMap.get().getMap((short) questmapid);
            final int x = mapData.getX();
            final int y = mapData.getY();
            final int height = mapData.getHeight();
            final int width = mapData.getWidth();

            final int newx = x + (height / 2);
            final int newy = y + (width / 2);

            final L1Location loc = new L1Location(newx, newy, (short) questmapid);

            if (party != null) {
                final ConcurrentHashMap<Integer, L1PcInstance> pcs = party
                        .partyUsers();
                if (pcs.isEmpty()) {
                    return;
                }
                if (pcs.size() <= 0) {
                    return;
                }
                int lv = 0;
                for (final L1PcInstance pc1 : pcs.values()) {
                	try {
                		lv += pc1.getLevel();
                	} catch (final Exception e) {
                		_log.error("队伍异常", e);
                    }
                }
                pcslevel = lv / pcs.size();
                final int rndnpc = rndnpc(pcslevel);
                for (int i=0; i<2; i++) {
                    // 召唤主要怪物
                    L1SpawnUtil.spawn(rndnpc, loc, 5, showId);
                    //quest.addNpc(mob);
                }

                for (final L1PcInstance pc2 : pcs.values()) {
                	try {
                        // 加入副本执行成员
                		
                        quest = WorldQuest.get().put(showId, questmapid,
                                questid, pc2);

                        final L1Location newLocations = loc.randomLocation(10, true);
                        final int newsX = newLocations.getX();
                        final int newsY = newLocations.getY();
                        L1Teleport.teleport(pc2, newsX, newsY, (short) questmapid, 5, true);
                	} catch (final Exception e) {
                		_log.error("队伍传送异常", e);
                    }
                }
            } else {
            	final int rndnpc = rndnpc(pcslevel);
                // 加入副本执行成员
                quest = WorldQuest.get().put(showId, questmapid,
                        questid, pc);
                // 召唤主要怪物
                L1SpawnUtil.spawn(rndnpc, loc, 5, showId);
                //quest.addNpc(mob);
                final L1Location newLocation = loc.randomLocation(10, true);
                final int newX = newLocation.getX();
                final int newY = newLocation.getY();

                L1Teleport.teleport(pc, newX, newY, (short) questmapid, 5, true);
            }
            // 取回进入时间限制
            final Integer time = QuestMapTable.get().getTime(questmapid);
            if (time != null) {
                quest.set_time(time.intValue());
            }

            pc.getInventory().removeItem(item, 1);
        } else {
    		if (item.getRndx() == 0) { //宝图随机x坐标
    			final L1Map mapData = L1WorldMap.get().getMap((short) 4);
                final int x = mapData.getX();
                final int y = mapData.getY();
                final int height = mapData.getHeight();
                final int width = mapData.getWidth();

                final int newx = x + (height / 2);
                final int newy = y + (width / 2);
                final L1Location loc = new L1Location(newx, newy, 4);
                final L1Location newLocation = loc.randomLocation(500, true);
                final int newX = newLocation.getX();
                final int newY = newLocation.getY();
                item.setRndx(newX);
                item.setRndy(newY);
    		}
        	pc.sendPackets(new S_SystemMessage("藏宝图上指示的位置是 大陆: " + item.getRndx() + " " + item.getRndy()));
        }
    }
    
    /**
     * 算出应该刷什么怪物
     * @param pcslevel
     * @return
     */
    private int rndnpc(final int pcslevel) {
    	int[] npclv = lv50mob;
    	if (pcslevel >= 80) {
    		npclv = lv80mob;
    	} else if (pcslevel >= 75) {
    		npclv = lv75mob;
    	} else if (pcslevel >= 70) {
    		npclv = lv70mob;
    	} else if (pcslevel >= 60) {
    		npclv = lv60mob;
    	}
    	final int rnd = _random.nextInt(npclv.length);
    	final int targetid = lv50mob[rnd];
    	return targetid;
    }
    
}
