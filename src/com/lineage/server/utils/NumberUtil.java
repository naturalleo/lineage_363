package com.lineage.server.utils;

import java.util.Random;

public class NumberUtil {

    /**
     * 少数を小数点第二位までの确率で上か下に丸めた整数を返す。 例えば1.3は30%の确率で切り舍て、70%の确率で切り上げられる。
     * 
     * @param number
     *            - もとの少数
     * @return 丸められた整数
     */
    public static int randomRound(final double number) {
        final double percentage = (number - Math.floor(number)) * 100;

        if (percentage == 0) {
            return ((int) number);

        } else {
            final int r = new Random().nextInt(100);
            if (r < percentage) {
                return ((int) number + 1);
            } else {
                return ((int) number);
            }
        }
    }
}
