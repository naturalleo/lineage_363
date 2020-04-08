package com.lineage.server.clientpackets;

import static com.lineage.server.model.Instance.L1PcInstance.REGENSTATE_MOVE;
import static com.lineage.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static com.lineage.server.model.skill.L1SkillId.MEDITATION;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.Config;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.DungeonRTable;
import com.lineage.server.datatables.DungeonTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Lock;
import com.lineage.server.serverpackets.S_MoveCharPacket;
import com.lineage.server.utils.CheckUtil;
import com.lineage.server.world.WorldTrap;

/**
 * 要求角色移动 基本封包长度:
 * 
 * @author daien
 * 
 */
public class C_MoveChar extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_MoveChar.class);

    /*
     * public C_MoveChar() { }
     * 
     * public C_MoveChar(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    private static final byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };

    private static final byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

    private static final int CLIENT_LANGUAGE = Config.CLIENT_LANGUAGE;

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final L1PcInstance pc = client.getActiveChar();

            if (pc.isDead()) {// 死亡
                return;
            }

            if (pc.isTeleport()) { // 顺移处理作业
                return;
            }
            
            // 无法攻击/使用道具/技能/回城的状态
            if (pc.isParalyzedX()) {
                //isError = true;
                return;
            }

            int locx = 0;// 目前位置
            int locy = 0;
            int heading = 0;

            try {
                locx = this.readH();
                locy = this.readH();
                heading = this.readC();

                // TODO 伺服器捆绑
                if (!Config.LOGINS_TO_AUTOENTICATION) {
                    if (CLIENT_LANGUAGE == 0x03) { // Taiwan Only
                        heading ^= 0x49;// 换位
                        locx = pc.getX();
                        locy = pc.getY();
                    }
                }

                heading = Math.min(heading, 7);

            } catch (final Exception e) {
                // 座标取回失败
                return;
            }

            pc.killSkillEffectTimer(MEDITATION);// 解除冥想术
            pc.setCallClanId(0); // 人物移动呼唤盟友无效

            if (!pc.hasSkillEffect(ABSOLUTE_BARRIER)) { // 绝对屏障状态
                pc.setRegenState(REGENSTATE_MOVE);
            }

            // 解除旧座标障碍宣告
            pc.getMap().setPassable(pc.getLocation(), true);

            // 移动前位置
            final int oleLocx = pc.getX();
            final int oleLocy = pc.getY();

            // 移动后位置
            final int newlocx = locx + HEADING_TABLE_X[heading];
            final int newlocy = locy + HEADING_TABLE_Y[heading];
            final int Place = pc.getMap().getOriginalTile(newlocx, newlocy); //移动后的地点 hjx1000

            try {
                // 不允许穿过该点
                boolean isError = false;

                // 异位判断(封包数据 与 核心数据 不吻合)
                if ((locx != oleLocx) && (locy != oleLocy)) {
                    isError = true;
                }

                // 商店村模式
                if (pc.isPrivateShop()) {
                    isError = true;
                }


//                if ((Place == 0 || Place == 12 || Place == 16) && pc.getMapId() != 304) { //防止穿墙和无效区域 hjx1000
//                	isError = true;
//                }
                if (pc.getMapId() != 304) {
                    switch (Place) {
                	case 0:
                	case 12:
                	case 16:
                	case 32:
                		isError = true;
                		break;
                
                }
                }


                // 位置具有障碍
                if (!isError) {
                    final boolean isPassable = pc.getMap().isPassable(oleLocx,
                            oleLocy, heading, null);
                    // System.out.println("穿透判断: " + isPassable);
                    // 该点不可通行
                    if (!isPassable) {
                        //System.out.println("该点不可通行");
                        // 该座标点上具有物件
                        if (CheckUtil.checkPassable(pc, newlocx, newlocy,
                                pc.getMapId())) {
                        	
                            //System.out.println("该座标点上具有物件");
                            isError = true;
                        }
                    }
                }

                if (isError) {
                    // 送出座标异常
                    pc.sendPackets(new S_Lock(pc));
                    // System.out.println("座标异常不执行移动送出回溯封包");
                    return;
                }

            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }

            final int result = pc.speed_Attack().checkInterval(
                    AcceleratorChecker.ACT_TYPE.MOVE);
            if (result == AcceleratorChecker.R_DISCONNECTED) {
                _log.error("要求角色移动:速度异常(" + pc.getName() + ")");
            }// */

            // 检查地图使用权
            CheckUtil.isUserMap(pc);

            // 地图切换
            if (DungeonTable.get()
                    .dg(newlocx, newlocy, pc.getMap().getId(), pc)) {
                return;
            }

            // 地图切换(多点)
            if (DungeonRTable.get().dg(newlocx, newlocy, pc.getMap().getId(),
                    pc)) {
                return;
            }

            // 记录移动前座标
            pc.setOleLocX(oleLocx);
            pc.setOleLocY(oleLocy);

            // 设置新作标点
            pc.getLocation().set(newlocx, newlocy);

            // 设置新面向
            pc.setHeading(heading);

            if (!pc.isGmInvis() && !pc.isGhost() && !pc.isInvisble()) {
                // 送出移动封包
                pc.broadcastPacketAll(new S_MoveCharPacket(pc));
            }

            // 设置娃娃移动
            pc.setNpcSpeed();

            // 新增座标障碍宣告
            pc.getMap().setPassable(pc.getLocation(), false);

            // 踩到陷阱的处理
            WorldTrap.get().onPlayerMoved(pc);

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
