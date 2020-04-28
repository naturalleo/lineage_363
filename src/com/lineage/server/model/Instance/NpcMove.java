package com.lineage.server.model.Instance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.npcai.CheckPath;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_MoveCharPacket;
import com.lineage.server.types.Point;
import com.lineage.server.world.World;

/**
 * 移動AI
 * @author dexc
 *
 */
public class NpcMove extends NpcMoveExecutor {

	private static final Log _log = LogFactory.getLog(NpcMove.class);

	private static final int _hce = 20;// 遺傳路徑無效搜尋容許次數

	private int[] _cxy = null;// 參考點紀錄

	private int _error = 0;// 路徑搜尋錯誤次數

	private int _aie = 0;// 遺傳路徑無效搜尋次數
	
	private Iterator<int[]> _list = null;
	
	private final L1NpcInstance _npc;

	public NpcMove(final L1NpcInstance npc) {
		_npc = npc;
	}
	
	/**
	 * 往指定面向移動1格
	 * @param dir 方向
	 */
	@Override
	public void setDirectionMove(final int dir) {
		if (dir >= 0) {
			int locx = _npc.getX();
			int locy = _npc.getY();

			locx += HEADING_TABLE_X[dir];
			locy += HEADING_TABLE_Y[dir];
			
			_npc.setHeading(dir);

			if (!(_npc instanceof L1DollInstance)) {
				// 解除舊座標障礙宣告
				this._npc.getMap().setPassable(_npc.getLocation(), true);
			}
			_npc.setX(locx);
			_npc.setY(locy);
			
			if (!(_npc instanceof L1DollInstance)) {
				// 新增座標障礙宣告
				_npc.getMap().setPassable(_npc.getLocation(), false);
			}

			_npc.broadcastPacketAll(new S_MoveCharPacket(_npc));

			// movement_distancet 超過最大移動距離
			if (_npc.getMovementDistance() > 0) {
				if ((_npc instanceof L1GuardInstance)
						|| (_npc instanceof L1MerchantInstance)
						|| (_npc instanceof L1MonsterInstance)) {
					if (_npc.getLocation().getLineDistance(
							_npc.getHomeX(), _npc.getHomeY()) > _npc.getMovementDistance()) {
						_npc.teleport(_npc.getHomeX(), _npc.getHomeY(), _npc.getHeading());
					}
				}
			}
			// 恨みに満ちたソルジャーゴースト、恨みに満ちたゴースト、恨みに満ちたハメル将軍
			if ((_npc.getNpcTemplate().get_npcId() >= 45912)
					&& (_npc.getNpcTemplate().get_npcId() <= 45916)) {
				if ((_npc.getX() >= 32591) && (_npc.getX() <= 32644) && (_npc.getY() >= 32643)
						&& (_npc.getY() <= 32688) && (_npc.getMapId() == 4)) {
					_npc.teleport(_npc.getHomeX(), _npc.getHomeY(), _npc.getHeading());
				}
			}
		}
	}
	
	@Override
	public void clear() {
		if (_list != null) {
			_list = null;
		}
		_aie = 0;
		_error = 0;
		_cxy = null;
	}
	
	/**
	 * 追蹤方向返回<BR>
	 * 一般返回到目標為止最適合的移動方向
	 * @param x 目標点Ｘ
	 * @param y 目標点Ｙ
	 * @param d 距離
	 * @return 移動方向
	 */
	@Override
	public int moveDirection(int x, int y) { //	移动到指定的目标点
		int dir = 0;
		try {
			// 取回與目標點距離
			final double d = _npc.getLocation().getLineDistance(x, y);
			// 被施放黑闇之影 距離超過2 (追蹤停止)
			if ((_npc.hasSkillEffect(L1SkillId.DARKNESS)) && (d >= 2D)) {
				return -1;

			} else if (d > 30D) { // 距離超過30 (追蹤停止)
				return -1;

			} else if (d > L1NpcInstance.DISTANCE) { // 距離超過courceRange (重新取回移動方向)
				dir = targetReverseDirection(x, y);
				dir = _targetDirection(_npc.getHeading(), _npc.getX(), _npc.getY(), x, y);
				dir = checkObject(dir);				
				dir = openDoor(dir);

			} else { // 決定最短距離方向
				//dir = _targetDirection(_npc.getHeading(), _npc.getX(), _npc.getY(), x, y);
				dir = this._serchCource(x, y);
				//System.out.println("111=:" + dir);
				if (glanceCheck(x, y, dir)) {// 與目標之間無障礙
					clear();
				} else {
					if (_list != null) {
						//dir = _check_targe_list(x, y);
						// TODO 等待修正
					} else {
						//dir = _check_targe(x, y, d);
						// TODO 等待修正
					}
				}				
				
				dir = checkObject(dir);
				dir = openDoor(dir);
				
				if (dir == -1) { // 遇到障礙重新取回移動方向
					_error++;// 達到指定次數 重新進入遺傳路徑演算
					if (_error == 10) {
						// TODO 等待修正
						
					} else {
						// 移動方向障礙點檢查(對新的面相取回移動方向)
						dir = targetReverseDirection(x, y);
						dir = _targetDirection(_npc.getHeading(), _npc.getX(), _npc.getY(), x, y);
						if (!_exsistCharacterBetweenTarget(dir)) {
							dir = checkObject(dir);
							dir = openDoor(dir);
							//dir = openDoor(dir);
							if (dir == -1) {
								// 重新定義 遺傳路徑(周邊8點方位判斷)
								dir = _check_targe_re8(x, y, d);
								// System.out.println("重新定義 遺傳路徑(周邊8點方位判斷):"+dir);
							}
						}
					}
				}
			}
			
		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
		return dir;
	}

	/**
	 * 遺傳路徑移動 add hjx1000
	 * @param x
	 * @param y
	 * @return
	 */
	private int _check_targe_list(int x, int y) {
		try {
			if (_list.hasNext()) {
				// 已經建立移動路徑清單
				try {
					final int[] xy = _list.next();
					_list.remove();// 移除該元素
					final int cx = _cxy[0] + xy[0];
					final int cy = _cxy[1] + xy[1];
					return _targetDirection(_npc.getHeading(), _npc.getX(), _npc.getY(), cx, cy);
					
				} catch (final Exception e) {
					clear();
					// 異常重新取回對XY方向
					return _targetDirection(_npc.getHeading(), _npc.getX(), _npc.getY(), x, y);
				}
				
			} else {
				clear();
				return -1;
			}
		
			
		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
		return -1;
	}
	
	/**
	 * 周邊8點方位判斷 add hjx1000
	 * @param x
	 * @param y
	 * @param d
	 * @return
	 */
	private int _check_targe_re8(int x, int y, double d) {
		int dir = _check_targe(x - 1, y, d);
		if (dir != -1) {
			return dir;
		}
		dir = _check_targe(x - 1, y - 1, d);
		if (dir != -1) {
			return dir;
		}
		dir = _check_targe(x + 1, y, d);
		if (dir != -1) {
			return dir;
		}
		dir = _check_targe(x + 1, y + 1, d);
		if (dir != -1) {
			return dir;
		}
		dir = _check_targe(x + 1, y - 1, d);
		if (dir != -1) {
			return dir;
		}
		dir = _check_targe(x - 1, y + 1, d);
		if (dir != -1) {
			return dir;
		}
		dir = _check_targe(x, y - 1, d);
		if (dir != -1) {
			return dir;
		}
		dir = _check_targe(x, y + 1, d);
		if (dir != -1) {
			return dir;
		}
		return -1;
	}
	
	/**
	 * 遺傳路徑判斷 add hjx1000
	 * @param x
	 * @param y
	 * @param d 
	 * @return 傳回第一個移動點
	 */
	private int _check_targe(int x, int y, double d) {
		try {
			// 具有首要目標
			if (_npc.is_now_target() == null) {
				clear();
				return -1;
			}
			if (!_npc.getMap().isPassableDna(x, y, 0)) {
				return -1;// 不可通行
			}
			if (_aie <= _hce) {
				if (d <= L1NpcInstance.DISTANCE) {
					_list = null;
					final CheckPath maze = new CheckPath(x, y, L1NpcInstance.DISTANCE, _npc);
					_cxy = maze.cxy();
					_list = maze.findBestPath().iterator();
					if (!_list.hasNext()) {
						_aie++;
						_list = null;
						_cxy = null;
						return -1;
						
					} else {
						final int[] xy = _list.next();
						_list.remove();// 移除該元素
						final int cx = _cxy[0] + xy[0];
						final int cy = _cxy[1] + xy[1];
						return _targetDirection(_npc.getHeading(), _npc.getX(), _npc.getY(), cx, cy);
					}
				}
				return -1;
			}
			
		} catch (final Exception e) {
			clear();
		}
		return -1;
	}
	
	/**
	 * 前進方向障礙者攻擊判斷
	 * @param dir
	 * @return
	 */
	private boolean _exsistCharacterBetweenTarget(final int dir) {
		try {
			// 執行者非MOB
			if (!(_npc instanceof L1MonsterInstance)) {
				return false;
			}
			// 無首要目標
			if (_npc.is_now_target() == null) {
				return false;
			}

			final int locX = _npc.getX();
			final int locY = _npc.getY();
			
			final int targetX = locX + HEADING_TABLE_X[dir];;
			final int targetY = locY + HEADING_TABLE_Y[dir];
			
			final ArrayList<L1Object> objects = World.get().getVisibleObjects(_npc, 1);
			for (final Iterator<L1Object> iter = objects.iterator(); iter.hasNext();) {
				final L1Object object = iter.next();
				boolean isCheck = false;

				if ((object.getX() == targetX) && 
						(object.getY() == targetY) && 
						(object.getMapId() == _npc.getMapId())) {
					isCheck = true;
				}
				if (isCheck) {
					boolean isHate = false;
					// 判斷障礙
					if (object instanceof L1PcInstance) {// 障礙者是玩家
						final L1PcInstance pc = (L1PcInstance) object;
						if (!pc.isGhost()) { // 鬼魂模式排除
							isHate = true;
						}
					} else if (object instanceof L1PetInstance) {// 障礙者是寵物
						isHate = true;
					} else if (object instanceof L1SummonInstance) {// 障礙者是 召換獸
						isHate = true;
					}
					if (isHate) {
						// 重新設置障礙者為攻擊目標
						final L1Character cha = (L1Character) object;
						_npc._hateList.add(cha, 0);
						_npc._target = cha;
						return true;
					}
				}
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
		return false;
	}

	/**
	 * 傳回目标的反方向
	 * @param tx 目標点Ｘ
	 * @param ty 目標点Ｙ
	 */
	@Override
	public int targetReverseDirection(final int tx, final int ty) {
		int dir = _targetDirection(_npc.getHeading(), _npc.getX(), _npc.getY(), tx, ty);
		return HEADING_RD[dir];
	}

	/**
	 * 目標點方向計算
	 * @param h 目前面向
	 * @param x 目前X
	 * @param y 目前Y
	 * @param tx 目標X
	 * @param ty 目標Y
	 * @return
	 */
	private static int _targetDirection(final int h, final int x, final int y, final int tx, final int ty) {
		try {
			final float dis_x = Math.abs(x - tx); // X點方向距離
			final float dis_y = Math.abs(y - ty); // Y點方向距離
			final float dis = Math.max(dis_x, dis_y); // 取回2者最大質
			if (dis == 0) {
				return h; // 距離為0表示不須改變面向
			}
			final int avg_x = (int) Math.floor((dis_x / dis) + 0.59f); // 上下左右がちょっと優先な丸め
			final int avg_y = (int) Math.floor((dis_y / dis) + 0.59f); // 上下左右がちょっと優先な丸め

			int dir_x = 0;
			int dir_y = 0;
			if (x < tx) {
				dir_x = 1;
			}
			if (x > tx) {
				dir_x = -1;
			}
			if (y < ty) {
				dir_y = 1;
			}
			if (y > ty) {
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

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
		return h;
	}

	/**
	 * 指定座標直線上無障礙物可通行
	 *
	 * @param tx 座標X値
	 * @param ty 座標Y値
	 * @param th 方向
	 * @return true:可以通過
	 *         false:不能通過
	 */
	private boolean glanceCheck(final int tx, final int ty, final int th) {
		final L1Map map = _npc.getMap();
		int chx = _npc.getX();
		int chy = _npc.getY();
		
		for (int i = 0; i < 15; i++) {
			if (((chx == tx) && (chy == ty)) || ((chx + 1 == tx) && (chy - 1 == ty))
					|| ((chx + 1 == tx) && (chy == ty))
					|| ((chx + 1 == tx) && (chy + 1 == ty))
					|| ((chx == tx) && (chy + 1 == ty))
					|| ((chx - 1 == tx) && (chy + 1 == ty))
					|| ((chx - 1 == tx) && (chy == ty))
					|| ((chx - 1 == tx) && (chy - 1 == ty))
					|| ((chx == tx) && (chy - 1 == ty))) {
				break;
			} else {
				if (!map.isPassable(chx, chy, th, _npc)) {
					return false;
					
				} else if (map.isExistDoor(chx, chy) == 0x03) {
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
	 * 對於前進方向是否有障礙的確認
	 * 第一點移動產生障礙
	 * 轉向2,3點判斷
	 * @param d 方向
	 * @return
	 */
	@Override
	public int checkObject(final int h) {
		if ((h >= 0) && (h <= 7)) {
			final int x = _npc.getX(); 
			final int y = _npc.getY(); 
			
			final int h2 = _heading2[h];
			final int h3 = _heading3[h];
			//System.out.println("h=:" + h +"h2=:"+h2+"h3=:"+h3);
			if (_npc.getMap().isPassable(x, y, h, null)) {
				return h;

			} else if (_npc.getMap().isPassable(x, y, h2, null)) {
				return h2;

			} else if (_npc.getMap().isPassable(x, y, h3, null)) {
				return h3;
			}
		}
		return -1;
	}

	@Override
	public int openDoor(final int h) {
		if (h != -1) {
			if (!_npc.getMap().isDoorPassable(_npc.getX(), _npc.getY(), h, _npc)) {
				_aie = _hce + 1;
				return -1;
			}
		}
		return h;
	}
    /**
     * 取得正面
     * 
     * @param ary
     * @param d
     */
    private void _getFront(final int[] ary, final int d) {
        switch (d) {
            case 1:
                ary[4] = 2;
                ary[3] = 0;
                ary[2] = 1;
                ary[1] = 3;
                ary[0] = 7;
                break;

            case 2:
                ary[4] = 2;
                ary[3] = 4;
                ary[2] = 0;
                ary[1] = 1;
                ary[0] = 3;
                break;

            case 3:
                ary[4] = 2;
                ary[3] = 4;
                ary[2] = 1;
                ary[1] = 3;
                ary[0] = 5;
                break;

            case 4:
                ary[4] = 2;
                ary[3] = 4;
                ary[2] = 6;
                ary[1] = 3;
                ary[0] = 5;
                break;

            case 5:
                ary[4] = 4;
                ary[3] = 6;
                ary[2] = 3;
                ary[1] = 5;
                ary[0] = 7;
                break;

            case 6:
                ary[4] = 4;
                ary[3] = 6;
                ary[2] = 0;
                ary[1] = 5;
                ary[0] = 7;
                break;

            case 7:
                ary[4] = 6;
                ary[3] = 0;
                ary[2] = 1;
                ary[1] = 5;
                ary[0] = 7;
                break;

            case 0:
                ary[4] = 2;
                ary[3] = 6;
                ary[2] = 0;
                ary[1] = 1;
                ary[0] = 7;
                break;
        }
    }
    /**
     * 移动位置
     * 
     * @param ary
     * @param d
     */
    private void _moveLocation(final int[] ary, final int d) {
        switch (d) {
            case 1:
                ary[0] = ary[0] + 1;
                ary[1] = ary[1] - 1;
                break;

            case 2:
                ary[0] = ary[0] + 1;
                break;

            case 3:
                ary[0] = ary[0] + 1;
                ary[1] = ary[1] + 1;
                break;

            case 4:
                ary[1] = ary[1] + 1;
                break;

            case 5:
                ary[0] = ary[0] - 1;
                ary[1] = ary[1] + 1;
                break;

            case 6:
                ary[0] = ary[0] - 1;
                break;

            case 7:
                ary[0] = ary[0] - 1;
                ary[1] = ary[1] - 1;
                break;

            case 0:
                ary[1] = ary[1] - 1;
                break;
        }
        ary[2] = d;
    }
    /**
     * 返回目标最短路径的方向
     * 
     * @param x
     *            目标点Ｘ
     * @param y
     *            目标点Ｙ
     */
    private int _serchCource(final int x, final int y) // 目标点Ｘ 目标点Ｙ
    {
        int i;
        final int locCenter = L1NpcInstance.DISTANCE + 1;
        final int diff_x = x - locCenter; // Ｘの实际のロケーションとの差
        final int diff_y = y - locCenter; // Ｙの实际のロケーションとの差
        int[] locBace = { _npc.getX() - diff_x, _npc.getY() - diff_y, 0, 0 }; // Ｘ
                                                                              // Ｙ
                                                                              // 方向
                                                                              // 初期方向
        final int[] locNext = new int[4];
        int[] locCopy;
        final int[] dirFront = new int[5];
        final boolean serchMap[][] = new boolean[locCenter * 2 + 1][locCenter * 2 + 1];
        final LinkedList<int[]> queueSerch = new LinkedList<int[]>();

        // 设置探索地图
        for (int j = L1NpcInstance.DISTANCE * 2 + 1; j > 0; j--) {
            for (i = L1NpcInstance.DISTANCE - Math.abs(locCenter - j); i >= 0; i--) {
                serchMap[j][locCenter + i] = true;
                serchMap[j][locCenter - i] = true;
            }
        }

        // 初期方向の设置
        final int[] firstCource = { 2, 4, 6, 0, 1, 3, 5, 7 };
        for (i = 0; i < 8; i++) {
            System.arraycopy(locBace, 0, locNext, 0, 4);
            this._moveLocation(locNext, firstCource[i]);
            if ((locNext[0] - locCenter == 0) && (locNext[1] - locCenter == 0)) {
                // 最短经路が见つかった场合:邻
                return firstCource[i];
            }
            if (serchMap[locNext[0]][locNext[1]]) {
                final int tmpX = locNext[0] + diff_x;
                final int tmpY = locNext[1] + diff_y;
                boolean found = false;
                switch (i) {
                    case 0:
                        found = _npc.getMap().isPassableDna(tmpX, tmpY + 1, i);
                        break;

                    case 1:
                        found = _npc.getMap().isPassableDna(tmpX - 1, tmpY + 1, i);
                        break;

                    case 2:
                        found = _npc.getMap().isPassableDna(tmpX - 1, tmpY, i);
                        break;

                    case 3:
                        found = _npc.getMap().isPassableDna(tmpX - 1, tmpY - 1, i);
                        break;

                    case 4:
                        found = _npc.getMap().isPassableDna(tmpX, tmpY - 1, i);
                        break;

                    case 5:
                        found = _npc.getMap().isPassableDna(tmpX + 1, tmpY - 1, i);
                        break;

                    case 6:
                        found = _npc.getMap().isPassableDna(tmpX + 1, tmpY, i);
                        break;

                    case 7:
                        found = _npc.getMap().isPassableDna(tmpX + 1, tmpY + 1, i);
                        break;
                }
                if (found)// 移动经路があった场合
                {
                    locCopy = new int[4];
                    System.arraycopy(locNext, 0, locCopy, 0, 4);
                    locCopy[2] = firstCource[i];
                    locCopy[3] = firstCource[i];
                    queueSerch.add(locCopy);
                }
                serchMap[locNext[0]][locNext[1]] = false;
            }
        }
        locBace = null;

        // 最短经路を探索
        while (queueSerch.size() > 0) {
            locBace = queueSerch.removeFirst();
            this._getFront(dirFront, locBace[2]);
            for (i = 4; i >= 0; i--) {
                System.arraycopy(locBace, 0, locNext, 0, 4);
                this._moveLocation(locNext, dirFront[i]);
                if ((locNext[0] - locCenter == 0)
                        && (locNext[1] - locCenter == 0)) {
                    return locNext[3];
                }
                if (serchMap[locNext[0]][locNext[1]]) {
                    final int tmpX = locNext[0] + diff_x;
                    final int tmpY = locNext[1] + diff_y;
                    boolean found = false;
                    switch (i) {
                        case 0:
                            found = _npc.getMap().isPassableDna(tmpX, tmpY + 1, i);
                            break;

                        case 1:
                            found = _npc.getMap().isPassableDna(tmpX - 1,
                                    tmpY + 1, i);
                            break;

                        case 2:
                            found = _npc.getMap().isPassableDna(tmpX - 1, tmpY, i);
                            break;

                        case 3:
                            found = _npc.getMap().isPassableDna(tmpX - 1,
                                    tmpY - 1, i);
                            break;

                        case 4:
                            found = _npc.getMap().isPassableDna(tmpX, tmpY - 1, i);
                            break;
                    }
                    if (found) // 移动经路があった场合
                    {
                        locCopy = new int[4];
                        System.arraycopy(locNext, 0, locCopy, 0, 4);
                        locCopy[2] = dirFront[i];
                        queueSerch.add(locCopy);
                    }
                    serchMap[locNext[0]][locNext[1]] = false;
                }
            }
            locBace = null;
        }
        return -1; // 目标までの经路がない场合
    }
}
