package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.data.event.BaseResetSet;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.ExpTable;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CharReset;
import com.lineage.server.serverpackets.S_Disconnect;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.utils.CalcInitHpMp;
import com.lineage.server.utils.CalcStat;

/**
 * 要求人物重设
 * 
 * @author daien
 * 
 */
public class C_CharReset extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_CharReset.class);

    /*
     * public C_CharReset() { }
     * 
     * public C_CharReset(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */
    // 各职业初始化属性(王族, 骑士, 精灵, 法师, 黑妖, 龙骑士, 幻术师)
    private static final int[] ORIGINAL_STR = new int[] { 13, 16, 11, 8, 12, 13,
            11 };
    private static final int[] ORIGINAL_DEX = new int[] { 10, 12, 12, 7, 15, 11,
            10 };
    private static final int[] ORIGINAL_CON = new int[] { 10, 14, 12, 12, 8, 14,
            12 };
    private static final int[] ORIGINAL_WIS = new int[] { 11, 9, 12, 12, 10, 12,
            12 };
    private static final int[] ORIGINAL_CHA = new int[] { 13, 12, 9, 8, 9, 8, 8 };
    private static final int[] ORIGINAL_INT = new int[] { 10, 8, 12, 12, 11, 11,
            12 };
    // 各职业初始化可分配点数(王族, 骑士, 精灵, 法师, 黑妖, 龙骑士, 幻术师)
    private static final int[] ORIGINAL_AMOUNT = new int[] { 8, 4, 7, 16, 10, 6,
            10 };
    private int OriginalStr;
    private int OriginalDex;
    private int OriginalCon;
    private int OriginalWis;
    private int OriginalInt;
    private int OriginalCha;
    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final L1PcInstance pc = client.getActiveChar();
            final int stage = this.readC();
            switch (stage) {
                case 0x01: // 0x01:初始化人物数质
                    final int str = this.readC();
                    final int intel = this.readC();
                    final int wis = this.readC();
                    final int dex = this.readC();
                    final int con = this.readC();
                    final int cha = this.readC();
                    //修正蜡烛问题 hjx1000
                	final int statusAmount = str+intel+wis+dex+con+cha; //所有初始属性点总数
                    final int originalStr = ORIGINAL_STR[pc.getType()];
                    final int originalDex = ORIGINAL_DEX[pc.getType()];
                    final int originalCon = ORIGINAL_CON[pc.getType()];
                    final int originalWis = ORIGINAL_WIS[pc.getType()];
                    final int originalCha = ORIGINAL_CHA[pc.getType()];
                    final int originalInt = ORIGINAL_INT[pc.getType()];
                    final int originalAmount = ORIGINAL_AMOUNT[pc.getType()];
                    //System.out.println("originalInt==" + originalInt);
                    //System.out.println("originalAmount==" + originalAmount);
                    if (((str < originalStr)
                            || (dex < originalDex)
                            || (con < originalCon)
                            || (wis < originalWis)
                            || (cha < originalCha) || (intel < originalInt))
                            || ((str > 20)
                                    || (dex > 18)
                                    || (con > 18)
                                    || (wis > 18)
                                    || (cha > 18) 
                                    || (intel > 18)
                                    || statusAmount > 75)) {
                    	pc.sendPackets(new S_Disconnect());
                    	pc.getNetConnection().kick();// 中斷
                    	return;
                    }
                    //修正蜡烛问题 end hjx1000
                    int hp = 0;
                    int mp = 0;
                    if (BaseResetSet.RETAIN != 0) {
                        hp = ((pc.getMaxHp() * BaseResetSet.RETAIN) / 100);
                        mp = ((pc.getMaxMp() * BaseResetSet.RETAIN) / 100);

                    } else {
                        hp = CalcInitHpMp.calcInitHp(pc);
                        mp = CalcInitHpMp.calcInitMp(pc);
                    }

                    pc.sendPackets(new S_CharReset(pc, 1, hp, mp, 10, str,
                            intel, wis, dex, con, cha));
                    this.initCharStatus(pc, hp, mp, str, intel, wis, dex, con,
                            cha);
                    //CharacterTable.get();
                    CharacterTable.saveCharStatus_temp(pc, str, con, dex, cha, intel, wis);
                    pc.refresh();
                    OriginalStr = str;
                    OriginalDex = dex;
                    OriginalCon = con;
                    OriginalWis = wis;
                    OriginalInt = intel;
                    OriginalCha = cha;
                    break;

                case 0x02: // 0x02:等级分配
                    final int type2 = this.readC();
                    switch (type2) {
                        case 0x00: // 提升1级
                            if (pc.getTempLevel() > 50) { //修正回忆蜡烛缺少点数错误 - hjx1000
                            	pc.sendPackets(new S_Disconnect());
                            	pc.getNetConnection().kick();// 中斷
                            	return;
                            }
                            this.setLevelUp(pc, 1);
                            break;

                        case 0x07: // 提升10级
                            if (pc.getTempMaxLevel() - pc.getTempLevel() < 10) {
                                return;
                            }
                            if (pc.getTempLevel() >= 41) { //修正回忆蜡烛缺少点数错误 - hjx1000
                            	pc.sendPackets(new S_Disconnect());
                            	pc.getNetConnection().kick();// 中斷
                            	return;
                            }
                            this.setLevelUp(pc, 10);
                            break;

                        case 0x01:// 提升1级(力量)
                        	if (pc.getTempLevel() >= pc.getTempMaxLevel()||
                        		pc.getBaseStr() >= ConfigAlt.POWER) {
                            	pc.sendPackets(new S_Disconnect());
                            	pc.getNetConnection().kick();// 中斷
                        		return;
                        	}
                            pc.addBaseStr((byte) 1);
                            this.setLevelUp(pc, 1);
                            break;

                        case 0x02:// 提升1级(智力)
                        	if (pc.getTempLevel() >= pc.getTempMaxLevel()||
                        		pc.getBaseInt() >= ConfigAlt.POWER) {
                            	pc.sendPackets(new S_Disconnect());
                            	pc.getNetConnection().kick();// 中斷
                        		return;
                        	}
                            pc.addBaseInt((byte) 1);
                            this.setLevelUp(pc, 1);
                            break;

                        case 0x03:// 提升1级(精神)
                        	if (pc.getTempLevel() >= pc.getTempMaxLevel()||
                    			pc.getBaseWis() >= ConfigAlt.POWER) {
                            	pc.sendPackets(new S_Disconnect());
                            	pc.getNetConnection().kick();// 中斷
                        		return;
                        	}
                            pc.addBaseWis((byte) 1);
                            this.setLevelUp(pc, 1);
                            break;

                        case 0x04:// 提升1级(敏捷)
                        	if (pc.getTempLevel() >= pc.getTempMaxLevel()||
                    			pc.getBaseDex() >= ConfigAlt.POWER) {
                            	pc.sendPackets(new S_Disconnect());
                            	pc.getNetConnection().kick();// 中斷
                        		return;
                        	}
                            pc.addBaseDex((byte) 1);
                            this.setLevelUp(pc, 1);
                            break;

                        case 0x05:// 提升1级(体质)
                        	if (pc.getTempLevel() >= pc.getTempMaxLevel()||
                    			pc.getBaseCon() >= ConfigAlt.POWER) {
                            	pc.sendPackets(new S_Disconnect());
                            	pc.getNetConnection().kick();// 中斷
                        		return;
                        	}
                            pc.addBaseCon((byte) 1);
                            this.setLevelUp(pc, 1);
                            break;

                        case 0x06:// 提升1级(魅力)
                        	if (pc.getTempLevel() >= pc.getTempMaxLevel()||
                    			pc.getBaseCha() >= ConfigAlt.POWER) {
                            	pc.sendPackets(new S_Disconnect());
                            	pc.getNetConnection().kick();// 中斷
                        		return;
                        	}
                            pc.addBaseCha((byte) 1);
                            this.setLevelUp(pc, 1);
                            break;

                        case 0x08:// 完成
                            switch (this.readC()) {
                                case 1:
                                	if (pc.getBaseStr() >= ConfigAlt.POWER) {
                                		return;
                                	}
                                    pc.addBaseStr((byte) 1);
                                    break;
                                case 2:
                                	if (pc.getBaseInt() >= ConfigAlt.POWER) {
                                		return;
                                	}
                                    pc.addBaseInt((byte) 1);
                                    break;
                                case 3:
                                	if (pc.getBaseWis() >= ConfigAlt.POWER) {
                                		return;
                                	}
                                    pc.addBaseWis((byte) 1);
                                    break;
                                case 4:
                                	if (pc.getBaseDex() >= ConfigAlt.POWER) {
                                		return;
                                	}
                                    pc.addBaseDex((byte) 1);
                                    break;
                                case 5:
                                	if (pc.getBaseCon() >= ConfigAlt.POWER) {
                                		return;
                                	}
                                    pc.addBaseCon((byte) 1);
                                    break;
                                case 6:
                                	if (pc.getBaseCha() >= ConfigAlt.POWER) {
                                		return;
                                	}
                                    pc.addBaseCha((byte) 1);
                                    break;
                            }
                            if (pc.getElixirStats() > 0) {
                                pc.sendPackets(new S_CharReset(pc
                                        .getElixirStats()));
                                return;
                            }
                            CharacterTable.saveCharStatus(pc, OriginalStr, OriginalCon, OriginalDex,
                            		OriginalCha, OriginalInt, OriginalWis);
                            this.saveNewCharStatus(pc);
                            break;
                    }
                    break;

                case 0x03:
                    final int read1 = this.readC();
                    final int read2 = this.readC();
                    final int read3 = this.readC();
                    final int read4 = this.readC();
                    final int read5 = this.readC();
                    final int read6 = this.readC();
                    //修正回忆蜡烛BUG hjx1000
                    final int wncou = read1+read2+read3+read4+read5+read6;
                    final int Total_capacity = pc.getBaseStr()+
                    						   pc.getBaseInt()+
                    						   pc.getBaseWis()+
                    						   pc.getBaseDex()+
                    						   pc.getBaseCon()+
                    						   pc.getBaseCha();
                    if (wncou > Total_capacity + pc.getElixirStats()) {
                    	pc.sendPackets(new S_Disconnect());
                    	pc.getNetConnection().kick();// 中斷
                    	return;
                    }
                    if (read1 > ConfigAlt.POWERMEDICINE 
                     || read2 > ConfigAlt.POWERMEDICINE
                     || read3 > ConfigAlt.POWERMEDICINE
                     || read4 > ConfigAlt.POWERMEDICINE
                     || read5 > ConfigAlt.POWERMEDICINE
                     || read6 > ConfigAlt.POWERMEDICINE) {
                    	pc.sendPackets(new S_Disconnect());
                    	pc.getNetConnection().kick();// 中斷
                    	return;
                    }
                    if (read1 < pc.getBaseStr() 
                     || read2 < pc.getBaseInt()
                     || read3 < pc.getBaseWis()
                     || read4 < pc.getBaseDex()
                     || read5 < pc.getBaseCon()
                     || read6 < pc.getBaseCha()) {
                    	pc.sendPackets(new S_Disconnect());
                        pc.getNetConnection().kick();// 中斷
                        return;
                    }
                    pc.addBaseStr((byte) (read1 - pc.getBaseStr()));
                    pc.addBaseInt((byte) (read2 - pc.getBaseInt()));
                    pc.addBaseWis((byte) (read3 - pc.getBaseWis()));
                    pc.addBaseDex((byte) (read4 - pc.getBaseDex()));
                    pc.addBaseCon((byte) (read5 - pc.getBaseCon()));
                    pc.addBaseCha((byte) (read6 - pc.getBaseCha()));
                    CharacterTable.saveCharStatus(pc, OriginalStr, OriginalCon, OriginalDex,
                    		OriginalCha, OriginalInt, OriginalWis);
                    this.saveNewCharStatus(pc);
                    break;
            }

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    private void saveNewCharStatus(final L1PcInstance pc) {
        pc.setInCharReset(false);
        if (pc.getOriginalAc() > 0) {
            pc.addAc(pc.getOriginalAc());
        }

        if (pc.getOriginalMr() > 0) {
            pc.addMr(0 - pc.getOriginalMr());
        }

        pc.refresh();
        pc.setCurrentHp(pc.getMaxHp());
        pc.setCurrentMp(pc.getMaxMp());
        if (pc.getTempMaxLevel() != pc.getLevel()) {
            pc.setLevel(pc.getTempMaxLevel());
            pc.setExp(ExpTable.getExpByLevel(pc.getTempMaxLevel()));
        }

        if (pc.getLevel() > 50) {
            pc.setBonusStats(pc.getLevel() - 50);

        } else {
            pc.setBonusStats(0);
        }
        pc.sendPackets(new S_OwnCharStatus(pc));

        final L1ItemInstance item = pc.getInventory().findItemId(49142); // 回忆蜡烛
        if (item != null) {
            try {
                pc.getInventory().removeItem(item, 1);
                pc.save(); // 资料存档

            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
        L1Teleport.teleport(pc, 32628, 32772, (short) 4, 4, false);
    }

    private void initCharStatus(final L1PcInstance pc, final int hp,
            final int mp, final int str, final int intel, final int wis,
            final int dex, final int con, final int cha) {
        pc.addBaseMaxHp((short) (hp - pc.getBaseMaxHp()));
        pc.addBaseMaxMp((short) (mp - pc.getBaseMaxMp()));
        pc.addBaseStr((byte) (str - pc.getBaseStr()));
        pc.addBaseInt((byte) (intel - pc.getBaseInt()));
        pc.addBaseWis((byte) (wis - pc.getBaseWis()));
        pc.addBaseDex((byte) (dex - pc.getBaseDex()));
        pc.addBaseCon((byte) (con - pc.getBaseCon()));
        pc.addBaseCha((byte) (cha - pc.getBaseCha()));
        //pc.addMr(0 - pc.getMr());//修正重置点数时魔防出错 hjx1000
        //pc.addDmgup(0 - pc.getDmgup());//屏掉这三句，，修复洗点后属性出错
        //pc.addHitup(0 - pc.getHitup());
    }

    private void setLevelUp(final L1PcInstance pc, final int addLv) {
        pc.setTempLevel(pc.getTempLevel() + addLv);
        for (int i = 0; i < addLv; i++) {
            final short randomHp = CalcStat.calcStatHp(pc.getType(),
                    pc.getBaseMaxHp(), pc.getBaseCon(), pc.getOriginalHpup());
            final short randomMp = CalcStat.calcStatMp(pc.getType(),
                    pc.getBaseMaxMp(), pc.getBaseWis(), pc.getOriginalMpup());
            pc.addBaseMaxHp(randomHp);
            pc.addBaseMaxMp(randomMp);
        }
        final int newAc = CalcStat.calcAc(pc.getTempLevel(), pc.getBaseDex());
        pc.sendPackets(new S_CharReset(pc, pc.getTempLevel(),
                pc.getBaseMaxHp(), pc.getBaseMaxMp(), newAc, pc.getBaseStr(),
                pc.getBaseInt(), pc.getBaseWis(), pc.getBaseDex(), pc
                        .getBaseCon(), pc.getBaseCha()));
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
