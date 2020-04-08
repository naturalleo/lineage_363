package com.lineage.server.model;

import com.lineage.config.ConfigRate;

/**
 * 物品升级机率
 * 
 * @author daien
 * 
 */
public class L1ItemUpdata {

    /**
     * 武器现有强化质大于等于9(非YiWei用)
     * 
     * @param enchant_level_tmp
     *            以强化质
     * @return 升级机率 1/100
     */
    public static double enchant_wepon_up9x(double enchant_level_tmp) {
        if (enchant_level_tmp <= 0) {
            enchant_level_tmp = 1;
        }
        // (>> 1: 除) (<< 1: 乘)
        return (100 + (enchant_level_tmp * ConfigRate.ENCHANT_CHANCE_WEAPON))
                / (enchant_level_tmp * (enchant_level_tmp - 7));
    }

    /**
     * 武器现有强化质大于等于9
     * 
     * @param enchant_level_tmp
     *            以强化质
     * @return 升级机率 1/100
     */
    public static double enchant_wepon_up9(double enchant_level_tmp) {
        if (enchant_level_tmp <= 0) {
            enchant_level_tmp = 1;
        }
        // (>> 1: 除) (<< 1: 乘)
        return (100 + (enchant_level_tmp * ConfigRate.ENCHANT_CHANCE_WEAPON))
                / (enchant_level_tmp * 2);
    }

    /**
     * 武器现有强化质小于9
     * 
     * @param enchant_level_tmp
     *            以强化质
     * @return 升级机率 1/100
     */
    public static double enchant_wepon_dn9(double enchant_level_tmp) {
        if (enchant_level_tmp <= 0) {
            enchant_level_tmp = 1;
        }
        // (>> 1: 除) (<< 1: 乘)
        return (100 + (enchant_level_tmp * ConfigRate.ENCHANT_CHANCE_WEAPON))
                / enchant_level_tmp;
    }

    /**
     * 防具现有强化质大于等于9(非YiWei用)
     * 
     * @param enchant_level_tmp
     *            以强化质
     * @return 升级机率 1/100
     */
    public static double enchant_armor_up9x(double enchant_level_tmp) {
        if (enchant_level_tmp <= 0) {
            enchant_level_tmp = 1;
        }
        // (>> 1: 除) (<< 1: 乘)
        return (100 + (enchant_level_tmp * ConfigRate.ENCHANT_CHANCE_ARMOR))
                / (enchant_level_tmp * (enchant_level_tmp - 7));
    }

    /**
     * 防具现有强化质大于等于9
     * 
     * @param enchant_level_tmp
     *            以强化质
     * @return 升级机率 1/100
     */
    public static double enchant_armor_up9(double enchant_level_tmp) {
        if (enchant_level_tmp <= 0) {
            enchant_level_tmp = 1;
        }
        // (>> 1: 除) (<< 1: 乘)
        return (100 + (enchant_level_tmp * ConfigRate.ENCHANT_CHANCE_ARMOR))
                / (enchant_level_tmp * 2);
    }

    /**
     * 防具现有强化质小于9
     * 
     * @param enchant_level_tmp
     *            以强化质
     * @return 升级机率 1/100
     */
    public static double enchant_armor_dn9(double enchant_level_tmp) {
        if (enchant_level_tmp <= 0) {
            enchant_level_tmp = 1;
        }
        // (>> 1: 除) (<< 1: 乘)
        return (100 + (enchant_level_tmp * ConfigRate.ENCHANT_CHANCE_ARMOR))
                / enchant_level_tmp;
    }

    /**
     * 物攻强化卷
     * 
     * @param enchant_level_tmp
     *            已强化质
     * @return 升级机率 1/100
     */
    public static int enchant_Dmg(int enchant_level_tmp) {
        if (enchant_level_tmp <= 0) {
            enchant_level_tmp = 1;
        }
        // (>> 1: 除) (<< 1: 乘)
        return 35 / enchant_level_tmp;
    }

    /**
     * 命中强化卷
     * 
     * @param enchant_level_tmp
     *            已强化质
     * @return 升级机率 1/100
     */
    public static int enchant_Hit(int enchant_level_tmp) {
        if (enchant_level_tmp <= 0) {
            enchant_level_tmp = 1;
        }
        // (>> 1: 除) (<< 1: 乘)
        return 35 / enchant_level_tmp;
    }

    /**
     * 魔攻强化卷
     * 
     * @param enchant_level_tmp
     *            已强化质
     * @return 升级机率 1/100
     */
    public static int enchant_Sp(int enchant_level_tmp) {
        if (enchant_level_tmp <= 0) {
            enchant_level_tmp = 1;
        }
        // (>> 1: 除) (<< 1: 乘)
        return 35 / enchant_level_tmp;
    }

    /**
     * 1级强化石
     * 
     * @return 提高质(1/1000)
     */
    public static int x1() {
        return 1;
    }

    /**
     * 2级强化石
     * 
     * @return 提高质(1/1000)
     */
    public static int x2() {
        return 2;
    }

    /**
     * 3级强化石
     * 
     * @return 提高质(1/1000)
     */
    public static int x3() {
        return 4;
    }

    /**
     * 4级强化石
     * 
     * @return 提高质(1/1000)
     */
    public static int x4() {
        return 8;
    }

    /**
     * 5级强化石
     * 
     * @return 提高质(1/1000)
     */
    public static int x5() {
        return 16;
    }

    /**
     * 第1阶段 幸运符
     * 
     * @param r
     * @return 原始机率 + 原始机率提高0.10
     */
    public static double w1(double r) {
        return (r * 0.10);
    }

    /**
     * 第2阶段 幸运符
     * 
     * @param r
     * @return 原始机率 + 原始机率提高0.20
     */
    public static double w2(double r) {
        return (r * 0.20);
    }

    /**
     * 第3阶段 幸运符
     * 
     * @param r
     * @return 原始机率 + 原始机率提高0.30
     */
    public static double w3(double r) {
        return (r * 0.30);
    }

    /**
     * 能量石基础机率
     * 
     * @param itemid
     * @return 基础机率(1/1000)
     */
    public static int poewr(int itemid) {
        int r = 0;
        switch (itemid) {
            case 46510:// 一级能量石 (力量)
            case 46528:// 一级能量石 (力量)(无法转移)
            case 46513:// 一级能量石 (敏捷)
            case 46531:// 一级能量石 (敏捷)(无法转移)
            case 46516:// 一级能量石 (智力)
            case 46534:// 一级能量石 (智力)(无法转移)
                r = 40;
                break;

            case 46519:// 一级能量石 (精神)
            case 46537:// 一级能量石 (精神)(无法转移)
            case 46522:// 一级能量石 (魅力)
            case 46540:// 一级能量石 (魅力)(无法转移)
            case 46525:// 一级能量石 (体质)
            case 46543:// 一级能量石 (体质)(无法转移)
                r = 50;
                break;

            case 46514:// 二级能量石 (敏捷)
            case 46532:// 二级能量石 (敏捷)(无法转移)
            case 46511:// 二级能量石 (力量)
            case 46529:// 二级能量石 (力量)(无法转移)
            case 46517:// 二级能量石 (智力)
            case 46535:// 二级能量石 (智力)(无法转移)
                r = 20;
                break;

            case 46520:// 二级能量石 (精神)
            case 46538:// 二级能量石 (精神)(无法转移)
            case 46523:// 二级能量石 (魅力)
            case 46541:// 二级能量石 (魅力)(无法转移)
            case 46526:// 二级能量石 (体质)
            case 46544:// 二级能量石 (体质)(无法转移)
                r = 25;
                break;

            case 46515:// 三级能量石 (敏捷)
            case 46533:// 三级能量石 (敏捷)(无法转移)
            case 46512:// 三级能量石 (力量)
            case 46530:// 三级能量石 (力量)(无法转移)
            case 46518:// 三级能量石 (智力)
            case 46536:// 三级能量石 (智力)(无法转移)
                r = 5;
                break;

            case 46521:// 三级能量石 (精神)
            case 46539:// 三级能量石 (精神)(无法转移)
            case 46524:// 三级能量石 (魅力)
            case 46542:// 三级能量石 (魅力)(无法转移)
            case 46527:// 三级能量石 (体质)
            case 46545:// 三级能量石 (体质)(无法转移)
                r = 7;
                break;

            case 46546:// 一级能量石 (体力)
            case 46549:// 一级能量石 (体力)(无法转移)
            case 46552:// 一级能量石 (魔力)
            case 46555:// 一级能量石 (魔力)(无法转移)
                r = 150;
                break;

            case 46547:// 二级能量石 (体力)
            case 46550:// 二级能量石 (体力)(无法转移)
            case 46553:// 二级能量石 (魔力)
            case 46556:// 二级能量石 (魔力)(无法转移)
                r = 75;
                break;

            case 46548:// 三级能量石 (体力)
            case 46551:// 三级能量石 (体力)(无法转移)
            case 46554:// 三级能量石 (魔力)
            case 46557:// 三级能量石 (魔力)(无法转移)
                r = 35;
                break;

            case 46558:// 一级能量石 (魔攻)
            case 46561:// 一级能量石 (魔攻)(无法转移)
                r = 15;
                break;

            case 46559:// 一级能量石 (攻击力)
            case 46562:// 一级能量石 (攻击力)(无法转移)
                r = 15;
                break;

            case 46560:// 一级能量石 (攻击成功)
            case 46563:// 一级能量石 (攻击成功)(无法转移)
                r = 15;
                break;

            case 46564:// 一级能量石 (额外魔法防御)
            case 46567:// 一级能量石 (额外魔法防御)(无法转移)
                r = 15;
                break;

            case 46565:// 一级能量石 (伤害吸收)
            case 46568:// 一级能量石 (伤害吸收)(无法转移)
                r = 15;
                break;

            case 46566:// 二级能量石 (伤害吸收)
            case 46569:// 二级能量石 (伤害吸收)(无法转移)
                r = 7;
                break;

            case 46570:// 一级能量石 (寒冰耐性)
            case 46573:// 一级能量石 (石化耐性)
            case 46576:// 一级能量石 (睡眠耐性)
            case 46579:// 一级能量石 (暗黑耐性)
            case 46582:// 一级能量石 (昏迷耐性)
            case 46585:// 一级能量石 (支撑耐性)
            case 46588:// 一级能量石 (寒冰耐性)(无法转移)
            case 46591:// 一级能量石 (石化耐性)(无法转移)
            case 46594:// 一级能量石 (睡眠耐性)(无法转移)
            case 46597:// 一级能量石 (暗黑耐性)(无法转移)
            case 46600:// 一级能量石 (昏迷耐性)(无法转移)
            case 46603:// 一级能量石 (支撑耐性)(无法转移)
                r = 60;
                break;

            case 46571:// 二级能量石 (寒冰耐性)
            case 46574:// 二级能量石 (石化耐性)
            case 46577:// 二级能量石 (睡眠耐性)
            case 46580:// 二级能量石 (暗黑耐性)
            case 46583:// 二级能量石 (昏迷耐性)
            case 46586:// 二级能量石 (支撑耐性)
            case 46589:// 二级能量石 (寒冰耐性)(无法转移)
            case 46592:// 二级能量石 (石化耐性)(无法转移)
            case 46595:// 二级能量石 (睡眠耐性)(无法转移)
            case 46598:// 二级能量石 (暗黑耐性)(无法转移)
            case 46601:// 二级能量石 (昏迷耐性)(无法转移)
            case 46604:// 二级能量石 (支撑耐性)(无法转移)
                r = 30;
                break;

            case 46572:// 三级能量石 (寒冰耐性)
            case 46575:// 三级能量石 (石化耐性)
            case 46578:// 三级能量石 (睡眠耐性)
            case 46581:// 三级能量石 (暗黑耐性)
            case 46584:// 三级能量石 (昏迷耐性)
            case 46587:// 三级能量石 (支撑耐性)
            case 46590:// 三级能量石 (寒冰耐性)(无法转移)
            case 46593:// 三级能量石 (石化耐性)(无法转移)
            case 46596:// 三级能量石 (睡眠耐性)(无法转移)
            case 46599:// 三级能量石 (暗黑耐性)(无法转移)
            case 46602:// 三级能量石 (昏迷耐性)(无法转移)
            case 46605:// 三级能量石 (支撑耐性)(无法转移)
                r = 15;
                break;

            case 46620:// 一级能量石 (石化)
            case 46623:// 一级能量石 (晕眩)
            case 46626:// 一级能量石 (弱能)
            case 46629:// 一级能量石 (震撼)
            case 46632:// 一级能量石 (落雷)
            case 46635:// 一级能量石 (狂击)
                r = 60;
                break;

            case 46621:// 二级能量石 (石化)
            case 46624:// 二级能量石 (晕眩)
            case 46627:// 二级能量石 (弱能)
            case 46630:// 二级能量石 (震撼)
            case 46633:// 二级能量石 (落雷)
            case 46636:// 二级能量石 (狂击)
                r = 30;
                break;

            case 46622:// 三级能量石 (石化)
            case 46625:// 三级能量石 (晕眩)
            case 46628:// 三级能量石 (弱能)
            case 46631:// 三级能量石 (震撼)
            case 46634:// 三级能量石 (落雷)
            case 46637:// 三级能量石 (狂击)
                r = 15;
                break;
        }
        return r;
    }

    /**
     * 机率提高修正
     * 
     * @param enchant_level_tmp
     * @return
     */
    public static double upRX(int itemid) {
        double r = 0;
        switch (itemid) {
            case 46610:// 一级强化石
            case 46615:// 一级强化石(无法转移)
                r = 0.1;
                break;

            case 46611:// 二级强化石
            case 46616:// 二级强化石(无法转移)
                r = 0.2;
                break;

            case 46612:// 三级强化石
            case 46617:// 三级强化石(无法转移)
                r = 0.3;
                break;

            case 46613:// 四级强化石
            case 46618:// 四级强化石(无法转移)
                r = 0.4;
                break;

            case 46614:// 五级强化石
            case 46619:// 五级强化石(无法转移)
                r = 0.5;
                break;
        }
        return r;
    }

    /**
     * 机率提高修正
     * 
     * @param enchant_level_tmp
     * @return
     */
    public static int upR(int enchant_level_tmp) {
        int r = 0;
        switch (enchant_level_tmp) {
            case 1:
                r += 70;
                break;
            case 2:
                r += 65;
                break;
            case 3:
                r += 60;
                break;
            case 4:
                r += 55;
                break;
            case 5:
                r += 50;
                break;
            case 6:
                r += 45;
                break;
            case 7:
                r += 40;
                break;
            case 8:
                r += 35;
                break;
            case 9:
                r += 30;
                break;
            case 10:
                r += 25;
                break;
            case 11:
                r += 20;
                break;
            case 12:
                r += 15;
                break;
            case 13:
                r += 10;
                break;
            case 14:
                r += 5;
                break;
            case 15:
                r += 0;
                break;
        }
        return r;
    }
}
