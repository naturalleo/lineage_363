package com.lineage.echo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.event.OnlineGiftSet;
import com.lineage.server.Shutdown;
import com.lineage.server.datatables.GetbackTable;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1DragonSlayer;
import com.lineage.server.model.L1Trade;
import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.model.Instance.L1FollowerInstance;
import com.lineage.server.model.Instance.L1IllusoryInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.serverpackets.S_NewMaster;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;

public class QuitGame {

    private static final Log _log = LogFactory.getLog(QuitGame.class);

    /**
     * 人物离开游戏的处理
     * 
     * @param pc
     */
    public static void quitGame(final L1PcInstance pc) {
    	
    	try {
    		if (pc.isActived() && !Shutdown.allquit) {
        		pc.setActived(false);
        		pc.setSkillEffect(78, 1000);//给予保护1秒无敌状态
    			Thread.sleep(1000);//延时三秒钟等待自动挂机结束.
    		}
        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    	
        if (pc == null) {
            return;
        }
        if (pc.getOnlineStatus() == 0) {
            return;
        }

        final L1Clan clan = WorldClan.get().getClan(pc.getClanname());
        if (clan != null) {
            if (clan.getWarehouseUsingChar() == pc.getId()) { // 使用血盟仓库中
                clan.setWarehouseUsingChar(0); // 解除使用状态
            }
        }
        if (!pc.getPetList().isEmpty()) {
            final Object[] petList = pc.getPetList().values().toArray();
            // 宠物 召唤兽 消除
            if (petList != null) {
                remove_pet(pc, petList);
            }
        }

        try {
            if (!pc.getDolls().isEmpty()) {
                final Object[] dolls = pc.getDolls().values().toArray();
                for (final Object obj : dolls) {
                    final L1DollInstance doll = (L1DollInstance) obj;
                    if (doll != null) {
                        doll.deleteDoll();
                    }
                }
                pc.getDolls().clear();
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }

        try {
            if (!pc.get_otherList().get_illusoryList().isEmpty()) {
                // 分身消除
                final Object[] illList = pc.get_otherList().get_illusoryList()
                        .values().toArray();
                for (final Object obj : illList) {
                    final L1IllusoryInstance ill = (L1IllusoryInstance) obj;
                    if (ill != null) {
                        ill.deleteMe();
                    }
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }

        try {
            // 清空特殊清单全部资料
            pc.get_otherList().clearAll();
        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }

        try {
            // 死亡状态设置
            if (pc.isDead()) {
                final int[] loc = GetbackTable.GetBack_Location(pc, true);
                pc.setX(loc[0]);
                pc.setY(loc[1]);
                pc.setMap((short) loc[2]);
                pc.setCurrentHp(pc.getLevel());
                if (pc.get_food() > 40) {
                    pc.set_food(40);
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }

        try {
            // 交易终止
            if (pc.getTradeID() != 0) {
                final L1Trade trade = new L1Trade();
                trade.tradeCancel(pc);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }

        try {
            // 决斗终止
            if (pc.getFightId() != 0) {
                pc.setFightId(0);
                final L1PcInstance fightPc = (L1PcInstance) World.get()
                        .findObject(pc.getFightId());
                if (fightPc != null) {
                    fightPc.setFightId(0);
                    fightPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL,
                            0, 0));
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }

        try {
            // 移出队伍
            if (pc.isInParty()) {
                pc.getParty().leaveMember(pc);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }

        try {
            // 移出聊天队伍
            if (pc.isInChatParty()) {
                pc.getChatParty().leaveMember(pc);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        
        try {
            // 删除屠龙副本此玩家纪录
            if (pc.getPortalNumber() != -1) {
                L1DragonSlayer.getInstance().removePlayer(pc, pc.getPortalNumber());
            }
        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }

        try {
            if (!pc.getFollowerList().isEmpty()) {
                // 跟随者 主人离线 重新召唤
                final Object[] followerList = pc.getFollowerList().values()
                        .toArray();
                for (final Object obj : followerList) {
                    final L1FollowerInstance follower = (L1FollowerInstance) obj;
                    follower.setParalyzed(true);
                    follower.spawn(follower.getNpcTemplate().get_npcId(),
                            follower.getX(), follower.getY(),
                            follower.getHeading(), follower.getMapId());
                    follower.deleteMe();
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }

        try {
        	if (pc.isPrivateShop()) {
        		
        		OnlineGiftSet.remove(pc);
        		pc.save();
                pc.saveInventory();
        		return; //离线商店 hjx1000
        	}
            // 移出各种处理清单
            pc.stopEtcMonitor();
            // _log.error("人物离开游戏的处理-移出各种处理清单");
            // 解除登入状态
            pc.setOnlineStatus(0);
            // _log.error("人物离开游戏的处理-解除登入状态");
            // 资料纪录
            pc.save();
            // _log.error("人物离开游戏的处理-资料纪录");
            // 背包纪录
            pc.saveInventory();
            // _log.error("人物离开游戏的处理-背包纪录");
            // 人物登出
            pc.logout();
            // _log.error("人物离开游戏的处理-人物登出");

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private static void remove_pet(final L1PcInstance pc, final Object[] petList) {
        try {
            for (Object obj : petList) {
                L1NpcInstance petObject = (L1NpcInstance) obj;
                if (petObject != null) {
                    if (petObject instanceof L1PetInstance) {
                        final L1PetInstance pet = (L1PetInstance) petObject;
                        pet.dropItem();
                        pet.deleteMe();
        				// 解除舊座標障礙宣告
                        pet.getMap().setPassable(pet.getLocation(), true);
                    }

                    if (petObject instanceof L1SummonInstance) {
                        final L1SummonInstance summon = (L1SummonInstance) petObject;
                        S_NewMaster packet = new S_NewMaster(summon);
                        for (final L1PcInstance visiblePc : World.get()
                                .getVisiblePlayer(summon)) {
                            if (visiblePc.equals(pc)) {
                                continue;
                            }
                            visiblePc.sendPackets(packet);
                        }
                    }
                }
            }
            // 清空 宠物 召唤兽 清单
            pc.getPetList().clear();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
