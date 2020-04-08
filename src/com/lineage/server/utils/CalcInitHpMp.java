package com.lineage.server.utils;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 人物素质初始化
 * 
 * @author dexc
 * 
 */
public class CalcInitHpMp {

    private CalcInitHpMp() {
    }

    /**
     * 初始化人物hp
     * 
     * @param pc
     * @return hp
     * 
     */
    public static int calcInitHp(final L1PcInstance pc) {
        int hp = 1;
        if (pc.isCrown()) {
            hp = 14;

        } else if (pc.isKnight()) {
            hp = 16;

        } else if (pc.isElf()) {
            hp = 15;

        } else if (pc.isWizard()) {
            hp = 12;

        } else if (pc.isDarkelf()) {
            hp = 12;

        } else if (pc.isDragonKnight()) {
            hp = 15;

        } else if (pc.isIllusionist()) {
            hp = 15;
        }
        return hp;
    }

    /**
     * 初始化人物mp
     * 
     * @param pc
     * @return mp
     * 
     */
    public static int calcInitMp(final L1PcInstance pc) {
        int mp = 1;
        if (pc.isCrown()) {
            switch (pc.getWis()) {
                case 11:
                    mp = 2;
                    break;
                case 12:
                case 13:
                case 14:
                case 15:
                    mp = 3;
                    break;
                case 16:
                case 17:
                case 18:
                    mp = 4;
                    break;
                default:
                    mp = 2;
                    break;
            }

        } else if (pc.isKnight()) {
            switch (pc.getWis()) {
                case 9:
                case 10:
                case 11:
                    mp = 1;
                    break;
                case 12:
                case 13:
                    mp = 2;
                    break;
                default:
                    mp = 1;
                    break;
            }

        } else if (pc.isElf()) {
            switch (pc.getWis()) {
                case 12:
                case 13:
                case 14:
                case 15:
                    mp = 4;
                    break;
                case 16:
                case 17:
                case 18:
                    mp = 6;
                    break;
                default:
                    mp = 4;
                    break;
            }

        } else if (pc.isWizard()) {
            switch (pc.getWis()) {
                case 12:
                case 13:
                case 14:
                case 15:
                    mp = 6;
                    break;
                case 16:
                case 17:
                case 18:
                    mp = 8;
                    break;
                default:
                    mp = 6;
                    break;
            }

        } else if (pc.isDarkelf()) {
            switch (pc.getWis()) {
                case 10:
                case 11:
                    mp = 3;
                    break;
                case 12:
                case 13:
                case 14:
                case 15:
                    mp = 4;
                    break;
                case 16:
                case 17:
                case 18:
                    mp = 6;
                    break;
                default:
                    mp = 3;
                    break;
            }

        } else if (pc.isDragonKnight()) {
            switch (pc.getWis()) {
                case 12:
                case 13:
                case 14:
                case 15:
                    mp = 4;
                    break;
                case 16:
                case 17:
                case 18:
                    mp = 6;
                    break;
                default:
                    mp = 4;
                    break;
            }

        } else if (pc.isIllusionist()) {
            switch (pc.getWis()) {
                case 12:
                case 13:
                case 14:
                case 15:
                    mp = 4;
                    break;
                case 16:
                case 17:
                case 18:
                    mp = 6;
                    break;
                default:
                    mp = 4;
                    break;
            }
        }
        return mp;
    }

}
