package com.lineage.server.model;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.templates.L1HouseLocTmp;
import com.lineage.server.types.Point;

/**
 * 小屋座标相关资料
 * 
 * @author dexc
 * 
 */
public class L1HouseLocation {

    private static final Log _log = LogFactory.getLog(L1HouseLocation.class);

    private static final int[] TELEPORT_LOC_MAPID = { 4, 4, 4, 350, };

    private static final Point[] TELEPORT_LOC_GIRAN = {
            new Point(33419, 32810), new Point(33343, 32723), // 仓库、ペット保管所
            new Point(33553, 32712), new Point(32702, 32842), }; // 赎罪の使者、ギラン市场

    private static final Point[] TELEPORT_LOC_HEINE = {
            new Point(33604, 33236), new Point(33649, 33413), // 仓库、ペット保管所
            new Point(33553, 32712), new Point(32702, 32842), }; // 赎罪の使者、ギラン市场

    private static final Point[] TELEPORT_LOC_ADEN = { new Point(33966, 33253),
            new Point(33921, 33177), // 仓库、ペット保管所
            new Point(33553, 32712), new Point(32702, 32842), }; // 赎罪の使者、ギラン市场

    private static final Point[] TELEPORT_LOC_GLUDIN = {
            new Point(32628, 32807), new Point(32623, 32729), // 仓库、ペット保管所
            new Point(33553, 32712), new Point(32702, 32842), }; // 赎罪の使者、ギラン市场

    // 小屋编号
    // private static final List<Integer> _houseIds = new ArrayList<Integer>();

    // 小屋座标资料(小屋编号/座标资料)
    private static final Map<Integer, L1HouseLocTmp> _houseLoc = new HashMap<Integer, L1HouseLocTmp>();

    /*
     * public static void add(final Integer e) { _houseIds.add(e); }
     */

    public static void put(final Integer e, final L1HouseLocTmp loc) {
        _houseLoc.put(e, loc);
    }

    private L1HouseLocation() {
    }

    /**
     * 地下盟屋座标
     * 
     * @param mapid
     * @return
     */
    public static boolean isInHouse(final short mapid) {
        switch (mapid) {
            case 5001:// 亚丁地下盟屋
            case 5002:// 亚丁地下盟屋
            case 5003:// 亚丁地下盟屋
            case 5004:// 亚丁地下盟屋
            case 5005:// 亚丁地下盟屋
            case 5006:// 亚丁地下盟屋
            case 5007:// 亚丁地下盟屋
            case 5008:// 亚丁地下盟屋
            case 5009:// 亚丁地下盟屋
            case 5010:// 亚丁地下盟屋
            case 5011:// 亚丁地下盟屋
            case 5012:// 亚丁地下盟屋
            case 5013:// 亚丁地下盟屋
            case 5014:// 亚丁地下盟屋
            case 5015:// 亚丁地下盟屋
            case 5016:// 亚丁地下盟屋
            case 5017:// 亚丁地下盟屋
            case 5018:// 亚丁地下盟屋
            case 5019:// 亚丁地下盟屋
            case 5020:// 亚丁地下盟屋
            case 5021:// 亚丁地下盟屋
            case 5022:// 亚丁地下盟屋
            case 5023:// 亚丁地下盟屋
            case 5024:// 亚丁地下盟屋
            case 5025:// 亚丁地下盟屋
            case 5026:// 亚丁地下盟屋
            case 5027:// 亚丁地下盟屋
            case 5028:// 亚丁地下盟屋
            case 5029:// 亚丁地下盟屋
            case 5030:// 亚丁地下盟屋
            case 5031:// 亚丁地下盟屋
            case 5032:// 亚丁地下盟屋
            case 5033:// 亚丁地下盟屋
            case 5034:// 亚丁地下盟屋
            case 5035:// 亚丁地下盟屋
            case 5036:// 亚丁地下盟屋
            case 5037:// 亚丁地下盟屋
            case 5038:// 亚丁地下盟屋
            case 5039:// 亚丁地下盟屋
            case 5040:// 亚丁地下盟屋
            case 5041:// 亚丁地下盟屋
            case 5042:// 亚丁地下盟屋
            case 5043:// 亚丁地下盟屋
            case 5044:// 亚丁地下盟屋
            case 5045:// 亚丁地下盟屋
            case 5046:// 亚丁地下盟屋
            case 5047:// 亚丁地下盟屋
            case 5048:// 亚丁地下盟屋
            case 5049:// 亚丁地下盟屋
            case 5050:// 亚丁地下盟屋
            case 5051:// 亚丁地下盟屋
            case 5052:// 亚丁地下盟屋
            case 5053:// 亚丁地下盟屋
            case 5054:// 亚丁地下盟屋
            case 5055:// 亚丁地下盟屋
            case 5056:// 亚丁地下盟屋
            case 5057:// 亚丁地下盟屋
            case 5058:// 亚丁地下盟屋
            case 5059:// 亚丁地下盟屋
            case 5060:// 亚丁地下盟屋
            case 5061:// 亚丁地下盟屋
            case 5062:// 亚丁地下盟屋
            case 5063:// 亚丁地下盟屋
            case 5064:// 亚丁地下盟屋
            case 5065:// 亚丁地下盟屋
            case 5066:// 亚丁地下盟屋
            case 5067:// 亚丁地下盟屋
            case 5068:// 奇岩地下盟屋
            case 5069:// 奇岩地下盟屋
            case 5070:// 奇岩地下盟屋
            case 5071:// 奇岩地下盟屋
            case 5072:// 奇岩地下盟屋
            case 5073:// 奇岩地下盟屋
            case 5074:// 奇岩地下盟屋
            case 5075:// 奇岩地下盟屋
            case 5076:// 奇岩地下盟屋
            case 5077:// 奇岩地下盟屋
            case 5078:// 奇岩地下盟屋
            case 5079:// 奇岩地下盟屋
            case 5080:// 奇岩地下盟屋
            case 5081:// 奇岩地下盟屋
            case 5082:// 奇岩地下盟屋
            case 5083:// 奇岩地下盟屋
            case 5084:// 奇岩地下盟屋
            case 5085:// 奇岩地下盟屋
            case 5086:// 奇岩地下盟屋
            case 5087:// 奇岩地下盟屋
            case 5088:// 奇岩地下盟屋
            case 5089:// 奇岩地下盟屋
            case 5090:// 奇岩地下盟屋
            case 5091:// 奇岩地下盟屋
            case 5092:// 奇岩地下盟屋
            case 5093:// 奇岩地下盟屋
            case 5094:// 奇岩地下盟屋
            case 5095:// 奇岩地下盟屋
            case 5096:// 奇岩地下盟屋
            case 5097:// 奇岩地下盟屋
            case 5098:// 奇岩地下盟屋
            case 5099:// 奇岩地下盟屋
            case 5100:// 奇岩地下盟屋
            case 5101:// 奇岩地下盟屋
            case 5102:// 奇岩地下盟屋
            case 5103:// 奇岩地下盟屋
            case 5104:// 奇岩地下盟屋
            case 5105:// 奇岩地下盟屋
            case 5106:// 奇岩地下盟屋
            case 5107:// 奇岩地下盟屋
            case 5108:// 奇岩地下盟屋
            case 5109:// 奇岩地下盟屋
            case 5110:// 奇岩地下盟屋
            case 5111:// 奇岩地下盟屋
            case 5112:// 奇岩地下盟屋
            case 5113:// 海音地下盟屋
            case 5114:// 海音地下盟屋
            case 5115:// 海音地下盟屋
            case 5116:// 海音地下盟屋
            case 5117:// 海音地下盟屋
            case 5118:// 海音地下盟屋
            case 5119:// 海音地下盟屋
            case 5120:// 海音地下盟屋
            case 5121:// 海音地下盟屋
            case 5122:// 海音地下盟屋
            case 5123:// 海音地下盟屋
                return true;
        }
        return false;
    }

    /**
     * 指定座标是否属于小屋范围
     * 
     * @param cha
     * @return
     */
    public static boolean isInHouse(final int locx, final int locy,
            final short mapid) {
        for (final Integer houseId : _houseLoc.keySet()) {
            if (isInHouseLoc(houseId, locx, locy, mapid)) {
                return true;
            }
        }

        /*
         * for (final Integer houseId : _houseIds) { if (isInHouseLoc(houseId,
         * locx, locy, mapid)) { return true; } }
         */
        return false;
    }

    /**
     * 指定位置是否在指定小屋范围内
     * 
     * @param houseId
     * @param cha
     * @return
     */
    public static boolean isInHouseLoc(final int houseId, final int locx,
            final int locy, final short mapid) {
        try {
            final L1HouseLocTmp loc = _houseLoc.get(new Integer(houseId));
            if (loc != null) {
                int locx1 = loc.get_locx1();
                int locx2 = loc.get_locx2();
                int locy1 = loc.get_locy1();
                int locy2 = loc.get_locy2();
                int locx3 = loc.get_locx3();
                int locx4 = loc.get_locx4();
                int locy3 = loc.get_locy3();
                int locy4 = loc.get_locy4();

                int locmapid = loc.get_mapid();
                int basement = loc.get_basement();

                if ((locx >= locx1) && (locx <= locx2) && (locy >= locy1)
                        && (locy <= locy2) && (mapid == locmapid)) {
                    return true;
                }

                if (locx3 != 0) {
                    if ((locx >= locx3) && (locx <= locx4) && (locy >= locy3)
                            && (locy <= locy4) && (mapid == locmapid)) {
                        return true;
                    }
                }

                if (basement != 0) {
                    if (mapid == basement) {
                        return true;
                    }
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        }
        return false;
        /*
         * boolean ret = false; switch (houseId) { case 262145: // ギランアジト1 if
         * (((locx >= 33368) && (locx <= 33375) && (locy >= 32651) && (locy <=
         * 32654) && (mapid == 4)) || ((locx >= 33373) && (locx <= 33375) &&
         * (locy >= 32655) && (locy <= 32657) && (mapid == 4)) || (mapid ==
         * 5068)) { ret = true; } break;
         * 
         * case 262146: // ギランアジト2 if (((locx >= 33381) && (locx <= 33387) &&
         * (locy >= 32653) && (locy <= 32656) && (mapid == 4)) || (mapid ==
         * 5069)) { ret = true; } break;
         * 
         * case 262147: // ギランアジト3 if (((locx >= 33392) && (locx <= 33404) &&
         * (locy >= 32650) && (locy <= 32656) && (mapid == 4)) || (mapid ==
         * 5070)) { ret = true; } break;
         * 
         * case 262148: // ギランアジト4 if (((locx >= 33427) && (locx <= 33430) &&
         * (locy >= 32656) && (locy <= 32662) && (mapid == 4)) || (mapid ==
         * 5071)) { ret = true; } break;
         * 
         * case 262149: // ギランアジト5 if (((locx >= 33439) && (locx <= 33445) &&
         * (locy >= 32665) && (locy <= 32667) && (mapid == 4)) || ((locx >=
         * 33442) && (locx <= 33445) && (locy >= 32668) && (locy <= 32672) &&
         * (mapid == 4)) || (mapid == 5072)) { ret = true; } break;
         * 
         * case 262150: // ギランアジト6 if (((locx >= 33454) && (locx <= 33466) &&
         * (locy >= 32648) && (locy <= 32654) && (mapid == 4)) || (mapid ==
         * 5073)) { ret = true; } break;
         * 
         * case 262151: // ギランアジト7 if (((locx >= 33476) && (locx <= 33479) &&
         * (locy >= 32665) && (locy <= 32671) && (mapid == 4)) || (mapid ==
         * 5074)) { ret = true; } break;
         * 
         * case 262152: // ギランアジト8 if (((locx >= 33471) && (locx <= 33477) &&
         * (locy >= 32678) && (locy <= 32680) && (mapid == 4)) || ((locx >=
         * 33474) && (locx <= 33477) && (locy >= 32681) && (locy <= 32685) &&
         * (mapid == 4)) || (mapid == 5075)) { ret = true; } break;
         * 
         * case 262153: // ギランアジト9 if (((locx >= 33453) && (locx <= 33460) &&
         * (locy >= 32694) && (locy <= 32697) && (mapid == 4)) || ((locx >=
         * 33458) && (locx <= 33460) && (locy >= 32698) && (locy <= 32700) &&
         * (mapid == 4)) || (mapid == 5076)) { ret = true; } break;
         * 
         * case 262154: // ギランアジト10 if (((locx >= 33421) && (locx <= 33433) &&
         * (locy >= 32685) && (locy <= 32691) && (mapid == 4)) || (mapid ==
         * 5077)) { ret = true; } break;
         * 
         * case 262155: // ギランアジト11 if (((locx >= 33409) && (locx <= 33415) &&
         * (locy >= 32674) && (locy <= 32676) && (mapid == 4)) || ((locx >=
         * 33412) && (locx <= 33415) && (locy >= 32677) && (locy <= 32681) &&
         * (mapid == 4)) || (mapid == 5078)) { ret = true; } break;
         * 
         * case 262156: // ギランアジト12 if (((locx >= 33414) && (locx <= 33421) &&
         * (locy >= 32703) && (locy <= 32706) && (mapid == 4)) || ((locx >=
         * 33419) && (locx <= 33421) && (locy >= 32707) && (locy <= 32709) &&
         * (mapid == 4)) || (mapid == 5079)) { ret = true; } break;
         * 
         * case 262157: // ギランアジト13 if (((locx >= 33372) && (locx <= 33384) &&
         * (locy >= 32692) && (locy <= 32698) && (mapid == 4)) || (mapid ==
         * 5080)) { ret = true; } break;
         * 
         * case 262158: // ギランアジト14 if (((locx >= 33362) && (locx <= 33365) &&
         * (locy >= 32681) && (locy <= 32687) && (mapid == 4)) || (mapid ==
         * 5081)) { ret = true; } break;
         * 
         * case 262159: // ギランアジト15 if (((locx >= 33360) && (locx <= 33366) &&
         * (locy >= 32669) && (locy <= 32671) && (mapid == 4)) || ((locx >=
         * 33363) && (locx <= 33366) && (locy >= 32672) && (locy <= 32676) &&
         * (mapid == 4)) || (mapid == 5082)) { ret = true; } break;
         * 
         * case 262160: // ギランアジト16 if (((locx >= 33341) && (locx <= 33347) &&
         * (locy >= 32660) && (locy <= 32662) && (mapid == 4)) || ((locx >=
         * 33344) && (locx <= 33347) && (locy >= 32663) && (locy <= 32667) &&
         * (mapid == 4)) || (mapid == 5083)) { ret = true; } break;
         * 
         * case 262161: // ギランアジト17 if (((locx >= 33345) && (locx <= 33348) &&
         * (locy >= 32672) && (locy <= 32678) && (mapid == 4)) || (mapid ==
         * 5084)) { ret = true; } break;
         * 
         * case 262162: // ギランアジト18 if (((locx >= 33338) && (locx <= 33350) &&
         * (locy >= 32704) && (locy <= 32711) && (mapid == 4)) || (mapid ==
         * 5085)) { ret = true; } break;
         * 
         * case 262163: // ギランアジト19 if (((locx >= 33349) && (locx <= 33356) &&
         * (locy >= 32728) && (locy <= 32731) && (mapid == 4)) || ((locx >=
         * 33354) && (locx <= 33356) && (locy >= 32732) && (locy <= 32734) &&
         * (mapid == 4)) || (mapid == 5086)) { ret = true; } break;
         * 
         * case 262164: // ギランアジト20 if (((locx >= 33366) && (locx <= 33372) &&
         * (locy >= 32713) && (locy <= 32715) && (mapid == 4)) || ((locx >=
         * 33369) && (locx <= 33372) && (locy >= 32716) && (locy <= 32720) &&
         * (mapid == 4)) || (mapid == 5087)) { ret = true; } break;
         * 
         * case 262165: // ギランアジト21 if (((locx >= 33380) && (locx <= 33383) &&
         * (locy >= 32712) && (locy <= 32718) && (mapid == 4)) || (mapid ==
         * 5088)) { ret = true; } break;
         * 
         * case 262166: // ギランアジト22 if (((locx >= 33401) && (locx <= 33413) &&
         * (locy >= 32733) && (locy <= 32739) && (mapid == 4)) || (mapid ==
         * 5089)) { ret = true; } break;
         * 
         * case 262167: // ギランアジト23 if (((locx >= 33424) && (locx <= 33430) &&
         * (locy >= 32717) && (locy <= 32719) && (mapid == 4)) || ((locx >=
         * 33427) && (locx <= 33430) && (locy >= 32720) && (locy <= 32724) &&
         * (mapid == 4)) || (mapid == 5090)) { ret = true; } break;
         * 
         * case 262168: // ギランアジト24 if (((locx >= 33448) && (locx <= 33451) &&
         * (locy >= 32729) && (locy <= 32735) && (mapid == 4)) || (mapid ==
         * 5091)) { ret = true; } break;
         * 
         * case 262169: // ギランアジト25 if (((locx >= 33404) && (locx <= 33407) &&
         * (locy >= 32754) && (locy <= 32760) && (mapid == 4)) || (mapid ==
         * 5092)) { ret = true; } break;
         * 
         * case 262170: // ギランアジト26 if (((locx >= 33363) && (locx <= 33375) &&
         * (locy >= 32755) && (locy <= 32761) && (mapid == 4)) || (mapid ==
         * 5093)) { ret = true; } break;
         * 
         * case 262171: // ギランアジト27 if (((locx >= 33351) && (locx <= 33357) &&
         * (locy >= 32774) && (locy <= 32776) && (mapid == 4)) || ((locx >=
         * 33354) && (locx <= 33357) && (locy >= 32777) && (locy <= 32781) &&
         * (mapid == 4)) || (mapid == 5094)) { ret = true; } break;
         * 
         * case 262172: // ギランアジト28 if (((locx >= 33355) && (locx <= 33361) &&
         * (locy >= 32787) && (locy <= 32790) && (mapid == 4)) || (mapid ==
         * 5095)) { ret = true; } break;
         * 
         * case 262173: // ギランアジト29 if (((locx >= 33366) && (locx <= 33373) &&
         * (locy >= 32786) && (locy <= 32789) && (mapid == 4)) || ((locx >=
         * 33371) && (locx <= 33373) && (locy >= 32790) && (locy <= 32792) &&
         * (mapid == 4)) || (mapid == 5096)) { ret = true; } break;
         * 
         * case 262174: // ギランアジト30 if (((locx >= 33383) && (locx <= 33386) &&
         * (locy >= 32773) && (locy <= 32779) && (mapid == 4)) || (mapid ==
         * 5097)) { ret = true; } break;
         * 
         * case 262175: // ギランアジト31 if (((locx >= 33397) && (locx <= 33404) &&
         * (locy >= 32788) && (locy <= 32791) && (mapid == 4)) || ((locx >=
         * 33402) && (locx <= 33404) && (locy >= 32792) && (locy <= 32794) &&
         * (mapid == 4)) || (mapid == 5098)) { ret = true; } break;
         * 
         * case 262176: // ギランアジト32 if (((locx >= 33479) && (locx <= 33486) &&
         * (locy >= 32788) && (locy <= 32791) && (mapid == 4)) || ((locx >=
         * 33484) && (locx <= 33486) && (locy >= 32792) && (locy <= 32794) &&
         * (mapid == 4)) || (mapid == 5099)) { ret = true; } break;
         * 
         * case 262177: // ギランアジト33 if (((locx >= 33498) && (locx <= 33501) &&
         * (locy >= 32801) && (locy <= 32807) && (mapid == 4)) || (mapid ==
         * 5100)) { ret = true; } break;
         * 
         * case 262178: // ギランアジト34 if (((locx >= 33379) && (locx <= 33385) &&
         * (locy >= 32802) && (locy <= 32805) && (mapid == 4)) || (mapid ==
         * 5101)) { ret = true; } break;
         * 
         * case 262179: // ギランアジト35 if (((locx >= 33373) && (locx <= 33385) &&
         * (locy >= 32822) && (locy <= 32829) && (mapid == 4)) || (mapid ==
         * 5102)) { ret = true; } break;
         * 
         * case 262180: // ギランアジト36 if (((locx >= 33398) && (locx <= 33401) &&
         * (locy >= 32810) && (locy <= 32816) && (mapid == 4)) || (mapid ==
         * 5103)) { ret = true; } break;
         * 
         * case 262181: // ギランアジト37 if (((locx >= 33397) && (locx <= 33403) &&
         * (locy >= 32821) && (locy <= 32823) && (mapid == 4)) || ((locx >=
         * 33400) && (locx <= 33403) && (locy >= 32824) && (locy <= 32828) &&
         * (mapid == 4)) || (mapid == 5104)) { ret = true; } break;
         * 
         * case 262182: // ギランアジト38 if (((locx >= 33431) && (locx <= 33438) &&
         * (locy >= 32838) && (locy <= 32841) && (mapid == 4)) || ((locx >=
         * 33436) && (locx <= 33438) && (locy >= 32842) && (locy <= 32844) &&
         * (mapid == 4)) || (mapid == 5105)) { ret = true; } break;
         * 
         * case 262183: // ギランアジト39 if (((locx >= 33456) && (locx <= 33462) &&
         * (locy >= 32838) && (locy <= 32841) && (mapid == 4)) || (mapid ==
         * 5106)) { ret = true; } break;
         * 
         * case 262184: // ギランアジト40 if (((locx >= 33385) && (locx <= 33392) &&
         * (locy >= 32845) && (locy <= 32848) && (mapid == 4)) || ((locx >=
         * 33390) && (locx <= 33392) && (locy >= 32849) && (locy <= 32851) &&
         * (mapid == 4)) || (mapid == 5107)) { ret = true; } break;
         * 
         * case 262185: // ギランアジト41 if (((locx >= 33399) && (locx <= 33405) &&
         * (locy >= 32859) && (locy <= 32861) && (mapid == 4)) || ((locx >=
         * 33402) && (locx <= 33405) && (locy >= 32862) && (locy <= 32866) &&
         * (mapid == 4)) || (mapid == 5108)) { ret = true; } break;
         * 
         * case 262186: // ギランアジト42 if (((locx >= 33414) && (locx <= 33417) &&
         * (locy >= 32850) && (locy <= 32856) && (mapid == 4)) || (mapid ==
         * 5109)) { ret = true; } break;
         * 
         * case 262187: // ギランアジト43 if (((locx >= 33372) && (locx <= 33384) &&
         * (locy >= 32867) && (locy <= 32873) && (mapid == 4)) || (mapid ==
         * 5110)) { ret = true; } break;
         * 
         * case 262188: // ギランアジト44 if (((locx >= 33425) && (locx <= 33437) &&
         * (locy >= 32865) && (locy <= 32871) && (mapid == 4)) || (mapid ==
         * 5111)) { ret = true; } break;
         * 
         * case 262189: // ギランアジト45 if (((locx >= 33443) && (locx <= 33449) &&
         * (locy >= 32869) && (locy <= 32871) && (mapid == 4)) || ((locx >=
         * 33446) && (locx <= 33449) && (locy >= 32872) && (locy <= 32876) &&
         * (mapid == 4)) || (mapid == 5112)) { ret = true; } break;
         * 
         * case 327681: // ハイネアジト1 if (((locx >= 33599) && (locx <= 33601) &&
         * (locy >= 33213) && (locy <= 33214) && (mapid == 4)) || ((locx >=
         * 33602) && (locx <= 33610) && (locy >= 33213) && (locy <= 33218) &&
         * (mapid == 4)) || (mapid == 5113)) { ret = true; } break;
         * 
         * case 327682: // ハイネアジト2 if (((locx >= 33627) && (locx <= 33632) &&
         * (locy >= 33206) && (locy <= 33209) && (mapid == 4)) || (mapid ==
         * 5114)) { ret = true; } break;
         * 
         * case 327683: // ハイネアジト3 if (((locx >= 33626) && (locx <= 33627) &&
         * (locy >= 33225) && (locy <= 33227) && (mapid == 4)) || ((locx >=
         * 33628) && (locx <= 33632) && (locy >= 33221) && (locy <= 33230) &&
         * (mapid == 4)) || (mapid == 5115)) { ret = true; } break;
         * 
         * case 327684: // ハイネアジト4 if (((locx >= 33628) && (locx <= 33636) &&
         * (locy >= 33241) && (locy <= 33244) && (mapid == 4)) || ((locx >=
         * 33632) && (locx <= 33635) && (locy >= 33245) && (locy <= 33250) &&
         * (mapid == 4)) || ((locx >= 33634) && (locx <= 33634) && (locy >=
         * 33251) && (locy <= 33252) && (mapid == 4)) || (mapid == 5116)) { ret
         * = true; } break;
         * 
         * case 327685: // ハイネアジト5 if (((locx >= 33616) && (locx <= 33621) &&
         * (locy >= 33262) && (locy <= 33265) && (mapid == 4)) || (mapid ==
         * 5117)) { ret = true; } break;
         * 
         * case 327686: // ハイネアジト6 if (((locx >= 33570) && (locx <= 33580) &&
         * (locy >= 33228) && (locy <= 33232) && (mapid == 4)) || ((locx >=
         * 33574) && (locx <= 33576) && (locy >= 33233) && (locy <= 33234) &&
         * (mapid == 4)) || (mapid == 5118)) { ret = true; } break;
         * 
         * case 327687: // ハイネアジト7 if (((locx >= 33583) && (locx <= 33588) &&
         * (locy >= 33305) && (locy <= 33314) && (mapid == 4)) || ((locx >=
         * 33587) && (locx <= 33588) && (locy >= 33315) && (locy <= 33316) &&
         * (mapid == 4)) || (mapid == 5119)) { ret = true; } break;
         * 
         * case 327688: // ハイネアジト8 if (((locx >= 33577) && (locx <= 33578) &&
         * (locy >= 33337) && (locy <= 33337) && (mapid == 4)) || ((locx >=
         * 33579) && (locx <= 33588) && (locy >= 33335) && (locy <= 33339) &&
         * (mapid == 4)) || ((locx >= 33585) && (locx <= 33588) && (locy >=
         * 33340) && (locy <= 33343) && (mapid == 4)) || (mapid == 5120)) { ret
         * = true; } break;
         * 
         * case 327689: // ハイネアジト9 if (((locx >= 33615) && (locx <= 33623) &&
         * (locy >= 33374) && (locy <= 33377) && (mapid == 4)) || ((locx >=
         * 33619) && (locx <= 33622) && (locy >= 33378) && (locy <= 33383) &&
         * (mapid == 4)) || ((locx >= 33621) && (locx <= 33621) && (locy >=
         * 33384) && (locy <= 33385) && (mapid == 4)) || (mapid == 5121)) { ret
         * = true; } break;
         * 
         * case 327690: // ハイネアジト10 if (((locx >= 33624) && (locx <= 33625) &&
         * (locy >= 33397) && (locy <= 33399) && (mapid == 4)) || ((locx >=
         * 33626) && (locx <= 33630) && (locy >= 33393) && (locy <= 33403) &&
         * (mapid == 4)) || (mapid == 5122)) { ret = true; } break;
         * 
         * case 327691: // ハイネアジト11 if (((locx >= 33621) && (locx <= 33622) &&
         * (locy >= 33444) && (locy <= 33444) && (mapid == 4)) || ((locx >=
         * 33622) && (locx <= 33632) && (locy >= 33442) && (locy <= 33446) &&
         * (mapid == 4)) || ((locx >= 33629) && (locx <= 33632) && (locy >=
         * 33447) && (locy <= 33450) && (mapid == 4)) || (mapid == 5123)) { ret
         * = true; } break;
         * 
         * case 524289: // グルーディンアジト1 if ((locx >= 32559) && (locx <= 32566) &&
         * (locy >= 32669) && (locy <= 32676) && (mapid == 4)) { ret = true; }
         * break;
         * 
         * case 524290: // グルーディンアジト2 if (((locx >= 32548) && (locx <= 32556) &&
         * (locy >= 32705) && (locy <= 32716) && (mapid == 4)) || ((locx >=
         * 32547) && (locx <= 32547) && (locy >= 32710) && (locy <= 32716) &&
         * (mapid == 4))) { ret = true; } break;
         * 
         * case 524291: // グルーディンアジト3 if ((locx >= 32537) && (locx <= 32544) &&
         * (locy >= 32781) && (locy <= 32791) && (mapid == 4)) { ret = true; }
         * break;
         * 
         * case 524292: // グルーディンアジト4 if ((locx >= 32550) && (locx <= 32560) &&
         * (locy >= 32780) && (locy <= 32787) && (mapid == 4)) { ret = true; }
         * break;
         * 
         * case 524293: // グルーディンアジト5 if (((locx >= 32535) && (locx <= 32543) &&
         * (locy >= 32807) && (locy <= 32818) && (mapid == 4)) || ((locx >=
         * 32534) && (locx <= 32534) && (locy >= 32812) && (locy <= 32818) &&
         * (mapid == 4))) { ret = true; } break;
         * 
         * case 524294: // グルーディンアジト6 if ((locx >= 32553) && (locx <= 32560) &&
         * (locy >= 32814) && (locy <= 32821) && (mapid == 4)) { ret = true; }
         * break; } return ret;
         */
    }

    /**
     * 小屋座标
     * 
     * @param houseId
     * @return
     */
    public static int[] getHouseLoc(final int houseId) { // houseIdからアジトの座标を返す
        final int[] loc = new int[3];
        try {
            L1HouseLocTmp locTmp = _houseLoc.get(new Integer(houseId));
            if (loc != null) {
                loc[0] = locTmp.get_homelocx();
                loc[1] = locTmp.get_homelocy();
                loc[2] = locTmp.get_mapid();
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        }
        return loc;
    }

    /**
     * 小屋所属地下盟屋座标
     * 
     * @param houseId
     * @return
     */
    public static int[] getBasementLoc(final int houseId) { // houseIdからアジトの地下室の座标を返す
        int[] loc = new int[3];
        if ((houseId >= 262145) && (houseId <= 262189)) { // ギランアジト1~45
            loc[0] = 32780;
            loc[1] = 32815;
            loc[2] = houseId - 257077;

        } else if ((houseId >= 327681) && (houseId <= 327691)) { // ハイネアジト1~11
            loc[0] = 32772;
            loc[1] = 32814;
            loc[2] = houseId - 322568;

        } else if ((houseId >= 524289) && (houseId <= 524294)) { // グルーディンアジト1~6
            // 地下室がないため、アジトの入り口の座标を返す
            loc = getHouseLoc(houseId);
        }
        return loc;
    }

    /**
     * 小屋所属领地座标
     * 
     * @param houseId
     * @param number
     * @return
     */
    public static int[] getHouseTeleportLoc(final int houseId, final int number) { // houseIdからテレポート先の座标を返す
        final int[] loc = new int[3];
        if ((houseId >= 262145) && (houseId <= 262189)) { // ギランアジト
            loc[0] = TELEPORT_LOC_GIRAN[number].getX();
            loc[1] = TELEPORT_LOC_GIRAN[number].getY();
            loc[2] = TELEPORT_LOC_MAPID[number];

        } else if ((houseId >= 327681) && (houseId <= 327691)) { // ハイネアジト
            loc[0] = TELEPORT_LOC_HEINE[number].getX();
            loc[1] = TELEPORT_LOC_HEINE[number].getY();
            loc[2] = TELEPORT_LOC_MAPID[number];

        } else if ((houseId >= 458753) && (houseId <= 458819)) { // アデンアジト
            loc[0] = TELEPORT_LOC_ADEN[number].getX();
            loc[1] = TELEPORT_LOC_ADEN[number].getY();
            loc[2] = TELEPORT_LOC_MAPID[number];

        } else if ((houseId >= 524289) && (houseId <= 524294)) { // グルーディンアジト1~6
            loc[0] = TELEPORT_LOC_GLUDIN[number].getX();
            loc[1] = TELEPORT_LOC_GLUDIN[number].getY();
            loc[2] = TELEPORT_LOC_MAPID[number];
        }
        return loc;
    }

}
