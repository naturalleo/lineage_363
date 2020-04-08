package com.lineage.server.model;

public class L1TaxCalculator {

    /**
     * 战争税は15%固定
     */
    private static final int WAR_TAX_RATES = 15;

    /**
     * 国税は10%固定（地域税に对する割合）
     */
    private static final int NATIONAL_TAX_RATES = 10;

    /**
     * ディアド税は10%固定（战争税に对する割合）
     */
    private static final int DIAD_TAX_RATES = 10;

    private final int _taxRatesCastle;
    private final int _taxRatesTown;
    private final int _taxRatesWar = WAR_TAX_RATES;

    /**
     * @param merchantNpcId
     *            计算对象商店のNPCID
     */
    public L1TaxCalculator(final int merchantNpcId) {
        this._taxRatesCastle = L1CastleLocation
                .getCastleTaxRateByNpcId(merchantNpcId);
        this._taxRatesTown = L1TownLocation
                .getTownTaxRateByNpcid(merchantNpcId);
    }

    public int calcTotalTaxPrice(final int price) {
        final int taxCastle = price * this._taxRatesCastle;
        final int taxTown = price * this._taxRatesTown;
        final int taxWar = price * WAR_TAX_RATES;
        return (taxCastle + taxTown + taxWar) / 100;
    }

    // XXX 个别に计算する为、丸め误差が出る。
    public int calcCastleTaxPrice(final int price) {
        return (price * this._taxRatesCastle) / 100
                - this.calcNationalTaxPrice(price);
    }

    public int calcNationalTaxPrice(final int price) {
        return (price * this._taxRatesCastle) / 100
                / (100 / NATIONAL_TAX_RATES);
    }

    public int calcTownTaxPrice(final int price) {
        return (price * this._taxRatesTown) / 100;
    }

    public int calcWarTaxPrice(final int price) {
        return (price * this._taxRatesWar) / 100;
    }

    public int calcDiadTaxPrice(final int price) {
        return (price * this._taxRatesWar) / 100 / (100 / DIAD_TAX_RATES);
    }

    /**
     * 课税后の価格を求める。
     * 
     * @param price
     *            课税前の価格
     * @return 课税后の価格
     */
    public int layTax(final int price) {
        return price + this.calcTotalTaxPrice(price);
    }
}
