package com.lineage.data.event.gambling;

import com.lineage.server.types.Point;

/**
 * 各项数据
 * 
 * @author dexc
 * 
 */
public class GamblingConfig {

    public static boolean ISGFX = true;

    // 可参赛NPC编号
    public static final int[] NPCID = new int[] { 86100, 86101, 86102, 86103,
            86104, 86105, 86106, 86107, 86108, 86109, 86110, 86111, 86112,
            86113, 86114, 86115, 86116, 86117, 86118, 86119 };

    // 食人妖宝宝外型
    public static int[][] GFXD = new int[][] {
            new int[] { 3478, 3479, 3480, 3481 },
            new int[] { 3497, 3501, 3505, 3509 },
            new int[] { 3498, 3502, 3506, 3510 },
            new int[] { 3499, 3503, 3507, 3511 },
            new int[] { 3500, 3504, 3508, 3512 } };

    // 赛狗外型
    public static int[][] GFX = new int[][] {
            new int[] { 1353, 1355, 1357, 1359 },
            new int[] { 1461, 1465, 1469, 1473 },
            new int[] { 1462, 1466, 1470, 1474 },
            new int[] { 1463, 1467, 1471, 1475 },
            new int[] { 1464, 1468, 1472, 1476 } };

    // 座标值
    public static final Point[][] TGLOC = {
            new Point[] {// NO 5
            new Point(33521, 32861), new Point(33478, 32861),
                    new Point(33473, 32858), new Point(33473, 32853),
                    new Point(33472, 32852), new Point(33478, 32846),
                    new Point(33490, 32847), new Point(33524, 32847), },
            new Point[] {// NO 4
            new Point(33519, 32863), new Point(33478, 32863),
                    new Point(33472, 32859), new Point(33472, 32853),
                    new Point(33478, 32846), new Point(33497, 32846),
                    new Point(33513, 32845), new Point(33524, 32845), },
            new Point[] {// NO 3
            new Point(33517, 32865), new Point(33478, 32865),
                    new Point(33471, 32860), new Point(33471, 32853),
                    new Point(33478, 32846), new Point(33497, 32844),
                    new Point(33513, 32843), new Point(33524, 32843), },
            new Point[] {// NO 2
            new Point(33515, 32867), new Point(33478, 32867),
                    new Point(33470, 32859), new Point(33469, 32853),
                    new Point(33477, 32845), new Point(33497, 32843),
                    new Point(33513, 32841), new Point(33524, 32841), },
            new Point[] {// NO 1
            new Point(33513, 32869), new Point(33479, 32869),
                    new Point(33470, 32860), new Point(33469, 32852),
                    new Point(33480, 32841), new Point(33497, 32841),
                    new Point(33513, 32840), new Point(33524, 32839), },

    /*
     * new Point[]{// NO 1 new Point(33524, 32839), new Point(33513, 32840), new
     * Point(33497, 32841), new Point(33480, 32841), new Point(33469, 32852),
     * new Point(33470, 32860), new Point(33479, 32869), new Point(33512, 32869)
     * }, new Point[]{// NO 2 new Point(33524, 32841), new Point(33513, 32841),
     * new Point(33497, 32843), new Point(33477, 32845), new Point(33469,
     * 32853), new Point(33470, 32859), new Point(33478, 32867), new
     * Point(33514, 32867) }, new Point[]{// NO 3 new Point(33524, 32843), new
     * Point(33513, 32843), new Point(33497, 32844), new Point(33478, 32846),
     * new Point(33471, 32853), new Point(33471, 32860), new Point(33478,
     * 32865), new Point(33516, 32865) }, new Point[]{// NO 4 new Point(33524,
     * 32845), new Point(33513, 32845), new Point(33497, 32846), new
     * Point(33478, 32846), new Point(33472, 32853), new Point(33472, 32859),
     * new Point(33478, 32863), new Point(33518, 32863) }, new Point[]{// NO 5
     * new Point(33524, 32847), new Point(33477, 32847), new Point(33472,
     * 32852), new Point(33473, 32853), new Point(33473, 32858), new
     * Point(33478, 32861), new Point(33520, 32861) }
     */
    };
}
