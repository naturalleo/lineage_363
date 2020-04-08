package com.lineage.data.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.templates.L1Event;

/**
 * 特殊属性<BR>
 * 除原先地水火风四大属性之外 外加光属性 暗属性 圣属性 邪属性<BR>
 * <BR>
 * <font color=#00800>地属性:束缚效果</font><BR>
 * 地之:1%束缚敌人0.8秒<BR>
 * 崩裂:2%束缚敌人1.0秒<BR>
 * 地灵:3%束缚敌人1.5秒<BR>
 * <BR>
 * <font color=#00800>水属性:攻击加倍效果</font><BR>
 * 火之:1%发动1.2倍伤害<BR>
 * 烈焰:2%发动1.4倍伤害<BR>
 * 火灵:3%发动1.6倍伤害<BR>
 * <BR>
 * <font color=#00800>火属性:吸血吸魔效果</font><BR>
 * 水之:1%吸血吸魔(吸血质伤害*0.2 吸魔质随机1~5)<BR>
 * 海啸:2%吸血吸魔(吸血质伤害*0.4 吸魔质随机2~10)<BR>
 * 水灵:3%吸血吸魔(吸血质伤害*0.6 吸魔质随机3~15)<BR>
 * <BR>
 * <font color=#00800>风属性:范围伤害效果</font><BR>
 * 风之:1%发动1格范围伤害(伤害:50+(0~99随机))<BR>
 * 暴风:2%发动2格范围伤害(伤害:50+(0~99随机))<BR>
 * 风灵:3%发动3格范围伤害(伤害:50+(0~99随机))<BR>
 * <BR>
 * <font color=#00800>光属性:附加究极光裂术</font><BR>
 * 光之:1%召唤光裂(依人物魔功智力产生伤害)<BR>
 * 闪耀:2%召唤光裂(依人物魔功智力产生伤害)<BR>
 * 光灵:3%召唤光裂(依人物魔功智力产生伤害)<BR>
 * <BR>
 * <font color=#00800>暗属性:附加闇盲咒术</font><BR>
 * 暗之:1%施展闇盲<BR>
 * 阴影:2%施展闇盲<BR>
 * 暗灵:3%施展闇盲<BR>
 * <BR>
 * <font color=#00800>圣属性:附加魔法封印</font><BR>
 * 圣之:1%施展魔法封印(封印时间:5秒)<BR>
 * 神圣:2%施展魔法封印(封印时间:8秒)<BR>
 * 圣灵:3%施展魔法封印(封印时间:10秒)<BR>
 * <BR>
 * <font color=#00800>邪属性:附加变形术</font><BR>
 * 邪之:1%施展变形术(目标变形:狼人,妖魔斗士)<BR>
 * 邪恶:2%施展变形术(目标变形:狼人,妖魔斗士,人形僵尸)<BR>
 * 邪灵:3%施展变形术(目标变形:狼人,妖魔斗士,人形僵尸,纸人)<BR>
 * <BR>
 * 
 * DELETE FROM `server_event` WHERE `id`='44'; INSERT INTO `server_event` VALUES
 * ('44', '特殊属性', 'FeatureItemSet', '1', 'true', '说明:启动特殊属性攻击');
 * 
 * DELETE FROM `etcitem` WHERE `item_id`='44117'; DELETE FROM `etcitem` WHERE
 * `item_id`='44118'; DELETE FROM `etcitem` WHERE `item_id`='44119'; DELETE FROM
 * `etcitem` WHERE `item_id`='44120'; INSERT INTO `etcitem` VALUES ('44117',
 * '光之武器强化卷轴', 'reel.ScrollEnchantS1Weapon', '光之武器强化卷轴', 'scroll', 'dai',
 * 'paper', '630', '3702', '3963', '0', '1', '0', '0', '0', '0', '0', '1', '0',
 * '0', '0', '0', '0', '0', '0'); INSERT INTO `etcitem` VALUES ('44118',
 * '暗之武器强化卷轴', 'reel.ScrollEnchantS2Weapon', '暗之武器强化卷轴', 'scroll', 'dai',
 * 'paper', '630', '3703', '3963', '0', '1', '0', '0', '0', '0', '0', '1', '0',
 * '0', '0', '0', '0', '0', '0'); INSERT INTO `etcitem` VALUES ('44119',
 * '圣之武器强化卷轴', 'reel.ScrollEnchantS3Weapon', '圣之武器强化卷轴', 'scroll', 'dai',
 * 'paper', '630', '3700', '3963', '0', '1', '0', '0', '0', '0', '0', '1', '0',
 * '0', '0', '0', '0', '0', '0'); INSERT INTO `etcitem` VALUES ('44120',
 * '邪之武器强化卷轴', 'reel.ScrollEnchantS4Weapon', '邪之武器强化卷轴', 'scroll', 'dai',
 * 'paper', '630', '3701', '3963', '0', '1', '0', '0', '0', '0', '0', '1', '0',
 * '0', '0', '0', '0', '0', '0');
 * 
 * @author dexc
 * 
 */
public class FeatureItemSet extends EventExecutor {

    private static final Log _log = LogFactory.getLog(FeatureItemSet.class);

    // 启用特效攻击
    public static boolean POWER_START = false;

    /**
	 *
	 */
    private FeatureItemSet() {
        // TODO Auto-generated constructor stub
    }

    public static EventExecutor get() {
        return new FeatureItemSet();
    }

    @Override
    public void execute(final L1Event event) {
        try {
            final String[] set = event.get_eventother().split(",");

            POWER_START = Boolean.parseBoolean(set[0]);

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
