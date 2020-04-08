package com.lineage.server.model.skill;

import static com.lineage.server.model.skill.L1SkillId.*;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigOther;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.datatables.lock.AccountReading;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.skill.skillmode.SkillMode;
import com.lineage.server.serverpackets.S_BlueMessage;
import com.lineage.server.serverpackets.S_Dexup;
import com.lineage.server.serverpackets.S_HPUpdate;
import com.lineage.server.serverpackets.S_Liquor;
import com.lineage.server.serverpackets.S_MPUpdate;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_PacketBoxCooking;
import com.lineage.server.serverpackets.S_PacketBoxWaterLife;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_Poison;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillBrave;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.serverpackets.S_PacketBoxIconAura;
import com.lineage.server.serverpackets.S_SkillIconBlessOfEva;
import com.lineage.server.serverpackets.S_SkillIconBloodstain;
import com.lineage.server.serverpackets.S_SkillIconShield;
import com.lineage.server.serverpackets.S_PacketBoxWisdomPotion;
import com.lineage.server.serverpackets.S_Strup;
import com.lineage.server.templates.L1Skills;

/**
 * 技能停止
 * 
 * @author dexc
 * 
 */
public class L1SkillStop {

    private static final Log _log = LogFactory.getLog(L1SkillStop.class);
    private static final Random _random = new Random();
    public static void stopSkill(final L1Character cha, final int skillId) {
        try {
            // System.out.println("技能停止:"+skillId);
            // TODO SKILL移转
            final SkillMode mode = L1SkillMode.get().getSkill(skillId);
            if (mode != null) {
                mode.stop(cha);

            } else {
                switch (skillId) {
                /*
                 * case IMMUNE_TO_HARM: case BOUNCE_ATTACK: case SHIELD:
                 */
                    case LIGHT: // ライト
                        if (cha instanceof L1PcInstance) {
                            if (!cha.isInvisble()) {
                                final L1PcInstance pc = (L1PcInstance) cha;
                                pc.turnOnOffLight();
                            }
                        }
                        break;

                    case GLOWING_AURA: // グローウィング オーラ
                        cha.addHitup(-5);
                        cha.addBowHitup(-5);
                        cha.addMr(-20);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_SPMR(pc));
                            pc.sendPackets(new S_PacketBoxIconAura(113, 0));
                        }
                        break;

                    case SHINING_AURA: // シャイニング オーラ
                        cha.addAc(8);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_PacketBoxIconAura(114, 0));
                        }
                        break;

                    case BRAVE_AURA: // ブレイブ オーラ
                        cha.addDmgup(-5);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_PacketBoxIconAura(116, 0));
                        }
                        break;

                    case SHIELD: // シールド
                        cha.addAc(2);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_SkillIconShield(5, 0));
                        }
                        break;

                    case BLIND_HIDING: // ブラインドハイディング
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.delBlindHiding();
                        }
                        break;

                    case SHADOW_ARMOR: // シャドウ アーマー
                        cha.addAc(3);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_SkillIconShield(3, 0));
                        }
                        break;

                    case DRESS_DEXTERITY: // ドレス デクスタリティー
                        cha.addDex((byte) -3);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_Dexup(pc, 2, 0));
                        }
                        break;

                    case DRESS_MIGHTY: // ドレス マイティー
                        cha.addStr((byte) -3);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_Strup(pc, 2, 0));
                        }
                        break;

                    case SHADOW_FANG: // シャドウ ファング
                        cha.addDmgup(-5);
                        break;

                    case ENCHANT_WEAPON: // エンチャント ウェポン
                        cha.addDmgup(-2);
                        break;

                    case BLESSED_ARMOR: // ブレスド アーマー
                    	if (cha instanceof L1PcInstance) {
                    		final L1PcInstance pc = (L1PcInstance) cha;
                    		if (pc.getInventory().getTypeEquipped(2, 2) >= 1) {
                    			pc.addAc(3);
                    		}
                    	}
                        //cha.addAc(3);
                        break;

                    case EARTH_BLESS: // アース ブレス
                        cha.addAc(7);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_SkillIconShield(7, 0));
                        }
                        break;

                    case RESIST_MAGIC: // レジスト マジック
                        cha.addMr(-10);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_SPMR(pc));
                        }
                        break;

                    case CLEAR_MIND: // クリアー マインド
                        cha.addWis((byte) -3);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.resetBaseMr();
                        }
                        break;

                    case RESIST_ELEMENTAL: // レジスト エレメント
                        cha.addWind(-10);
                        cha.addWater(-10);
                        cha.addFire(-10);
                        cha.addEarth(-10);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_OwnCharAttrDef(pc));
                        }
                        break;

                    case ELEMENTAL_PROTECTION: // エレメンタルプロテクション
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            final int attr = pc.getElfAttr();
                            if (attr == 1) {
                                cha.addEarth(-50);
                            } else if (attr == 2) {
                                cha.addFire(-50);
                            } else if (attr == 4) {
                                cha.addWater(-50);
                            } else if (attr == 8) {
                                cha.addWind(-50);
                            }
                            pc.sendPackets(new S_OwnCharAttrDef(pc));
                        }
                        break;

                    case WATER_LIFE: // 水之元气
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_PacketBoxWaterLife());
                        }
                        break;

                    /*
                     * case ELEMENTAL_FALL_DOWN: // エレメンタルフォールダウン if (cha
                     * instanceof L1PcInstance) { final L1PcInstance pc =
                     * (L1PcInstance) cha; final int attr = pc.getAddAttrKind();
                     * final int i = 50; switch (attr) { case 1: pc.addEarth(i);
                     * break; case 2: pc.addFire(i); break; case 4:
                     * pc.addWater(i); break; case 8: pc.addWind(i); break;
                     * default: break; } pc.setAddAttrKind(0);
                     * pc.sendPackets(new S_OwnCharAttrDef(pc)); } else if (cha
                     * instanceof L1NpcInstance) { final L1NpcInstance npc =
                     * (L1NpcInstance) cha; final int attr =
                     * npc.getAddAttrKind(); final int i = 50; switch (attr) {
                     * case 1: npc.addEarth(i); break; case 2: npc.addFire(i);
                     * break; case 4: npc.addWater(i); break; case 8:
                     * npc.addWind(i); break; default: break; }
                     * npc.setAddAttrKind(0); } break;
                     */

                    case IRON_SKIN: // アイアン スキン
                        cha.addAc(10);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_SkillIconShield(10, 0));
                        }
                        break;

                    case EARTH_SKIN: // アース スキン
                        cha.addAc(6);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_SkillIconShield(6, 0));
                        }
                        break;

                    case PHYSICAL_ENCHANT_STR: // フィジカル エンチャント：STR
                        cha.addStr((byte) -5);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_Strup(pc, 5, 0));
                        }
                        break;

                    case PHYSICAL_ENCHANT_DEX: // フィジカル エンチャント：DEX
                        cha.addDex((byte) -5);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_Dexup(pc, 5, 0));
                        }
                        break;

                    case FIRE_WEAPON: // ファイアー ウェポン
                        cha.addDmgup(-4);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_PacketBoxIconAura(147, 0));
                        }
                        break;

                    case FIRE_BLESS: // ファイアー ブレス
                        cha.addDmgup(-4);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_PacketBoxIconAura(154, 0));
                        }
                        break;

                    case BURNING_WEAPON: // バーニング ウェポン
                        cha.addDmgup(-6);
                        cha.addHitup(-6);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_PacketBoxIconAura(162, 0));
                        }
                        break;

                    case BLESS_WEAPON: // ブレス ウェポン
                        cha.addDmgup(-2);
                        cha.addHitup(-2);
                        cha.addBowHitup(-2);
                        break;

                    case WIND_SHOT: // ウィンド ショット
                        cha.addBowHitup(-6);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_PacketBoxIconAura(148, 0));
                        }
                        break;
                        
                    case ERASE_MAGIC: // 妖精魔法 (魔法消除)
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_PacketBoxIconAura(152, 0));
                        }
                        break;

                    case STORM_EYE: // ストーム アイ
                        cha.addBowHitup(-2);
                        cha.addBowDmgup(-3);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_PacketBoxIconAura(155, 0));
                        }
                        break;

                    case STORM_SHOT: // ストーム ショット
                        cha.addBowDmgup(-5);
                        cha.addBowHitup(1);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_PacketBoxIconAura(165, 0));
                        }
                        break;

                    case BERSERKERS: // バーサーカー
                        cha.addAc(-10);
                        cha.addDmgup(-5);
                        cha.addHitup(-2);
                        break;

                    case SHAPE_CHANGE: // シェイプ チェンジ
                        L1PolyMorph.undoPoly(cha);
                        break;

                    /*
                     * case ADVANCE_SPIRIT: // アドバンスド スピリッツ if (cha instanceof
                     * L1PcInstance) { final L1PcInstance pc = (L1PcInstance)
                     * cha; pc.addMaxHp(-pc.getAdvenHp());
                     * pc.addMaxMp(-pc.getAdvenMp()); pc.setAdvenHp(0);
                     * pc.setAdvenMp(0); pc.sendPackets(new
                     * S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp())); if
                     * (pc.isInParty()) { // パーティー中
                     * pc.getParty().updateMiniHP(pc); } pc.sendPackets(new
                     * S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp())); } break;
                     */

                    // case HASTE:
                    case GREATER_HASTE: // ヘイスト、グレーターヘイスト
                        cha.setMoveSpeed(0);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));
                        }
                        break;

                    case HOLY_WALK:
                    case MOVING_ACCELERATION:
                    case WIND_WALK:
                        // case BLOODLUST: //
                        // ホーリーウォーク、ムービングアクセレーション、ウィンドウォーク、ブラッドラスト
                        cha.setBraveSpeed(0);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPacketsAll(new S_SkillBrave(pc.getId(), 0, 0));
                        }
                        break;

                    case ILLUSION_OGRE: // イリュージョン：オーガ
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.addDmgup(-4);
                            pc.addHitup(-4);
                        }
                        break;

                    case ILLUSION_LICH: // イリュージョン：リッチ
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.addSp(-2);
                            pc.sendPackets(new S_SPMR(pc));
                        }
                        break;

                    case ILLUSION_DIA_GOLEM: // イリュージョン：ダイアモンドゴーレム
//                        if (cha instanceof L1PcInstance) {
//                            final L1PcInstance pc = (L1PcInstance) cha;
//                            pc.addAc(8);
//                        }
                    	cha.addAc(8);
                        break;

                    /*
                     * case ILLUSION_AVATAR: // イリュージョン：アバター if (cha instanceof
                     * L1PcInstance) { final L1PcInstance pc = (L1PcInstance)
                     * cha; pc.addDmgup(-10); } break;
                     */

                    case INSIGHT: // インサイト
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.addStr((byte) -1);
                            pc.addCon((byte) -1);
                            pc.addDex((byte) -1);
                            pc.addWis((byte) -1);
                            pc.addInt((byte) -1);
                        }
                        break;

                    /*
                     * case CURSE_BLIND: case DARKNESS: if (cha instanceof
                     * L1PcInstance) { final L1PcInstance pc = (L1PcInstance)
                     * cha; pc.sendPackets(new S_CurseBlind(0)); } break;
                     */

                    /*
                     * case CURSE_PARALYZE: // カーズ パラライズ if (cha instanceof
                     * L1PcInstance) { final L1PcInstance pc = (L1PcInstance)
                     * cha; pc.sendPacketsAll(new S_Poison(pc.getId(), 0));
                     * pc.sendPackets(new
                     * S_Paralysis(S_Paralysis.TYPE_PARALYSIS, false)); } break;
                     */

                    case WEAKNESS: // ウィークネス
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.addDmgup(5);
                            pc.addHitup(1);
                        }
                        break;

                    case DISEASE: // ディジーズ
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.addDmgup(6);
                            pc.addAc(-12);
                        }
                        break;

                    case ICE_LANCE: // アイスランス
                    case FREEZING_BLIZZARD: // フリージングブリザード
                    case FREEZING_BREATH: // フリージングブレス
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPacketsAll(new S_Poison(pc.getId(), 0));

                            pc.sendPackets(new S_Paralysis(
                                    S_Paralysis.TYPE_FREEZE, false));
                        } else if ((cha instanceof L1MonsterInstance)
                                || (cha instanceof L1SummonInstance)
                                || (cha instanceof L1PetInstance)) {
                            final L1NpcInstance npc = (L1NpcInstance) cha;
                            npc.broadcastPacketAll(new S_Poison(npc.getId(), 0));
                            npc.setParalyzed(false);
                        }
                        break;

                    case EARTH_BIND: // アースバインド
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPacketsAll(new S_Poison(pc.getId(), 0));

                            pc.sendPackets(new S_Paralysis(
                                    S_Paralysis.TYPE_FREEZE, false));
                        } else if ((cha instanceof L1MonsterInstance)
                                || (cha instanceof L1SummonInstance)
                                || (cha instanceof L1PetInstance)) {
                            final L1NpcInstance npc = (L1NpcInstance) cha;
                            npc.broadcastPacketAll(new S_Poison(npc.getId(), 0));
                            npc.setParalyzed(false);
                        }
                        break;

                    /*
                     * case SHOCK_STUN: // ショック スタン if (cha instanceof
                     * L1PcInstance) { final L1PcInstance pc = (L1PcInstance)
                     * cha; pc.sendPackets(new
                     * S_Paralysis(S_Paralysis.TYPE_STUN, false)); } else if
                     * ((cha instanceof L1MonsterInstance) || (cha instanceof
                     * L1SummonInstance) || (cha instanceof L1PetInstance)) {
                     * final L1NpcInstance npc = (L1NpcInstance) cha;
                     * npc.setParalyzed(false); } break;
                     */

                    case FOG_OF_SLEEPING: // フォグ オブ スリーピング
                        cha.setSleeped(false);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_Paralysis(
                                    S_Paralysis.TYPE_SLEEP, false));
                            pc.sendPackets(new S_OwnCharStatus(pc));
                        }
                        break;

                    case ABSOLUTE_BARRIER: // アブソルート バリア
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.startHpRegeneration();
                            pc.startMpRegeneration();
                        }
                        break;

                    /*
                     * case WIND_SHACKLE: // ウィンド シャックル if (cha instanceof
                     * L1PcInstance) { final L1PcInstance pc = (L1PcInstance)
                     * cha; pc.sendPackets(new
                     * S_PacketBoxWindShackle(pc.getId(), 0)); } break;
                     */

                    case SLOW:
                    case ENTANGLE:
                    case MASS_SLOW: // スロー、エンタングル、マススロー
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));
                        }
                        cha.setMoveSpeed(0);
                        break;

                    /*
                     * case STATUS_FREEZE: // Freeze if (cha instanceof
                     * L1PcInstance) { final L1PcInstance pc = (L1PcInstance)
                     * cha; pc.sendPackets(new
                     * S_Paralysis(S_Paralysis.TYPE_BIND, false));
                     * 
                     * } else if ((cha instanceof L1MonsterInstance) || (cha
                     * instanceof L1SummonInstance) || (cha instanceof
                     * L1PetInstance)) { final L1NpcInstance npc =
                     * (L1NpcInstance) cha; npc.setParalyzed(false); } break;
                     */

                    case GUARD_BRAKE: // ガードブレイク
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.addAc(-15);
                        }
                        break;

                    case HORROR_OF_DEATH: // ホラーオブデス
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.addStr(3);
                            pc.addInt(3);
                        }
                        break;

                    case STATUS_CUBE_IGNITION_TO_ALLY: // キューブ[イグニション]：味方
                        cha.addFire(-30);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_OwnCharAttrDef(pc));
                        }
                        break;

                    case STATUS_CUBE_QUAKE_TO_ALLY: // キューブ[クエイク]：味方
                        cha.addEarth(-30);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_OwnCharAttrDef(pc));
                        }
                        break;

                    case STATUS_CUBE_SHOCK_TO_ALLY: // キューブ[ショック]：味方
                        cha.addWind(-30);
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_OwnCharAttrDef(pc));
                        }
                        break;

                    case STATUS_CUBE_IGNITION_TO_ENEMY: // キューブ[イグニション]：敌
                    case STATUS_CUBE_QUAKE_TO_ENEMY: // キューブ[クエイク]：敌
                    case STATUS_CUBE_SHOCK_TO_ENEMY: // キューブ[ショック]：敌
                    case STATUS_MR_REDUCTION_BY_CUBE_SHOCK: // キューブ[ショック]によるMR减少
                    case STATUS_CUBE_BALANCE: // キューブ[バランス]
                        break;

                    case STATUS_BRAVE: // 勇敢药水效果
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPacketsAll(new S_SkillBrave(pc.getId(), 0, 0));
                        }
                        cha.setBraveSpeed(0);
                        break;

                    case STATUS_BRAVE3: // 巧克力蛋糕
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPacketsAll(new S_Liquor(pc.getId(), 0x00));
                        }
                        break;

                    case EXP13: // 第一段经验加倍效果
                    case EXP15: // 第一段经验加倍效果
                    case EXP17: // 第一段经验加倍效果
                    case EXP20: // 第一段经验加倍效果
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            // 2402 经验直加倍效果消失！
                            pc.sendPackets(new S_ServerMessage("经验加倍效果消失！"));
                            pc.sendPackets(new S_PacketBoxCooking(pc, 32, 0));
                        }
                        break;

                    case SEXP13: // 第二段经验加倍效果
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            // 3077 第二段经验1.3倍效果结束。
                            pc.sendPackets(new S_ServerMessage("第二段经验1.3倍效果结束。"));
                        }
                        break;

                    case SEXP15: // 第二段经验加倍效果
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            // 3079 第二段经验1.5倍效果结束。
                            pc.sendPackets(new S_ServerMessage("第二段经验1.5倍效果结束。"));
                        }
                        break;

                    case SEXP17: // 第二段经验加倍效果
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            // 3081 第二段经验1.7倍效果结束。
                            pc.sendPackets(new S_ServerMessage("第二段经验1.7倍效果结束"));
                        }
                        break;

                    case SEXP20: // 第二段经验加倍效果
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            // 3075 第二段经验双倍效果结束。
                            pc.sendPackets(new S_ServerMessage("第二段经验双倍效果结束。"));
                        }
                        break;

                    case REEXP20: // 第三段经验加倍效果
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            // 3073 第三段经验双倍效果结束。
                            pc.sendPackets(new S_ServerMessage("第三段经验双倍效果结束。"));
                        }
                        break;

                    case STATUS_ELFBRAVE: // 精灵饼干效果
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPacketsAll(new S_SkillBrave(pc.getId(), 0, 0));
                        }
                        cha.setBraveSpeed(0);
                        break;

                    case STATUS_RIBRAVE: // 生命之树果实效果
    					if (cha instanceof L1PcInstance) {
    						final L1PcInstance pc = (L1PcInstance) cha;
    					/*	pc.setBraveSpeed(0);
    						pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));
    						// XXX ユグドラの実のアイコンを消す方法が不明
    						
    					} else {*/
    						pc.setBraveSpeed(0);
    						pc.sendPacketsAll(new S_SkillBrave(pc.getId(), 0, 0));//添加修正果实效果状态 hjx1000
    					}
                        break;

                    case STATUS_HASTE: // 加速药水效果
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));
                        }
                        cha.setMoveSpeed(0);
                        break;

                    case STATUS_BLUE_POTION: // 魔力回复药水效果
                        break;

                    case STATUS_UNDERWATER_BREATH: // 伊娃的祝福药水效果
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_SkillIconBlessOfEva(
                                    pc.getId(), 0));
                        }
                        break;

                    case STATUS_WISDOM_POTION: // 慎重药水效果
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            cha.addSp(-2);
                            pc.sendPackets(new S_PacketBoxWisdomPotion(0));
                        }
                        break;

                    case STATUS_CHAT_PROHIBITED: // 禁言效果
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_ServerMessage(288)); // チャットができるようになりました。
                        }
                        break;

                    case STATUS_POISON: // 毒素效果
                        cha.curePoison();
                        break;

                    case COOKING_1_0_N:
                    case COOKING_1_0_S: // フローティングアイステーキ
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.addWind(-10);
                            pc.addWater(-10);
                            pc.addFire(-10);
                            pc.addEarth(-10);
                            pc.sendPackets(new S_OwnCharAttrDef(pc));
                            pc.sendPackets(new S_PacketBoxCooking(pc, 0, 0));
                            pc.setCookingId(0);
                        }
                        break;

                    case COOKING_1_1_N:
                    case COOKING_1_1_S: // ベアーステーキ
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.addMaxHp(-30);
                            pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc
                                    .getMaxHp()));
                            if (pc.isInParty()) { // パーティー中
                                pc.getParty().updateMiniHP(pc);
                            }
                            pc.sendPackets(new S_PacketBoxCooking(pc, 1, 0));
                            pc.setCookingId(0);
                        }
                        break;

                    case COOKING_1_2_N:
                    case COOKING_1_2_S: // ナッツ饼
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_PacketBoxCooking(pc, 2, 0));
                            pc.setCookingId(0);
                        }
                        break;

                    case COOKING_1_3_N:
                    case COOKING_1_3_S: // 蚁脚のチーズ烧き
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.addAc(1);
                            pc.sendPackets(new S_PacketBoxCooking(pc, 3, 0));
                            pc.setCookingId(0);
                        }
                        break;

                    case COOKING_1_4_N:
                    case COOKING_1_4_S: // フルーツサラダ
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.addMaxMp(-20);
                            pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc
                                    .getMaxMp()));
                            pc.sendPackets(new S_PacketBoxCooking(pc, 4, 0));
                            pc.setCookingId(0);
                        }
                        break;

                    case COOKING_1_5_N:
                    case COOKING_1_5_S: // フルーツ甘酢あんかけ
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_PacketBoxCooking(pc, 5, 0));
                            pc.setCookingId(0);
                        }
                        break;

                    case COOKING_1_6_N:
                    case COOKING_1_6_S: // 猪肉の串烧き
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.addMr(-5);
                            pc.sendPackets(new S_SPMR(pc));
                            pc.sendPackets(new S_PacketBoxCooking(pc, 6, 0));
                            pc.setCookingId(0);
                        }
                        break;

                    case COOKING_1_7_N:
                    case COOKING_1_7_S: // キノコスープ
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_PacketBoxCooking(pc, 7, 0));
                            pc.setDessertId(0);
                        }
                        break;

                    case COOKING_2_0_N:
                    case COOKING_2_0_S: // キャビアカナッペ
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_PacketBoxCooking(pc, 8, 0));
                            pc.setCookingId(0);
                        }
                        break;

                    case COOKING_2_1_N:
                    case COOKING_2_1_S: // アリゲーターステーキ
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.addMaxHp(-30);
                            pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc
                                    .getMaxHp()));
                            if (pc.isInParty()) { // パーティー中
                                pc.getParty().updateMiniHP(pc);
                            }
                            pc.addMaxMp(-30);
                            pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc
                                    .getMaxMp()));
                            pc.sendPackets(new S_PacketBoxCooking(pc, 9, 0));
                            pc.setCookingId(0);
                        }
                        break;

                    case COOKING_2_2_N:
                    case COOKING_2_2_S: // タートルドラゴンの果子
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.addAc(2);
                            pc.sendPackets(new S_PacketBoxCooking(pc, 10, 0));
                            pc.setCookingId(0);
                        }
                        break;

                    case COOKING_2_3_N:
                    case COOKING_2_3_S: // キウィパロット烧き
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_PacketBoxCooking(pc, 11, 0));
                            pc.setCookingId(0);
                        }
                        break;

                    case COOKING_2_4_N:
                    case COOKING_2_4_S: // スコーピオン烧き
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_PacketBoxCooking(pc, 12, 0));
                            pc.setCookingId(0);
                        }
                        break;

                    case COOKING_2_5_N:
                    case COOKING_2_5_S: // イレッカドムシチュー
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.addMr(-10);
                            pc.sendPackets(new S_SPMR(pc));
                            pc.sendPackets(new S_PacketBoxCooking(pc, 13, 0));
                            pc.setCookingId(0);
                        }
                        break;

                    case COOKING_2_6_N:
                    case COOKING_2_6_S: // クモ脚の串烧き
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.addSp(-1);
                            pc.sendPackets(new S_SPMR(pc));
                            pc.sendPackets(new S_PacketBoxCooking(pc, 14, 0));
                            pc.setCookingId(0);
                        }
                        break;

                    case COOKING_2_7_N:
                    case COOKING_2_7_S: // クラブスープ
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_PacketBoxCooking(pc, 15, 0));
                            pc.setDessertId(0);
                        }
                        break;

                    case COOKING_3_0_N:
                    case COOKING_3_0_S: // クラスタシアンのハサミ烧き
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_PacketBoxCooking(pc, 16, 0));
                            pc.setCookingId(0);
                        }
                        break;

                    case COOKING_3_1_N:
                    case COOKING_3_1_S: // グリフォン烧き
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.addMaxHp(-50);
                            pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc
                                    .getMaxHp()));
                            if (pc.isInParty()) { // パーティー中
                                pc.getParty().updateMiniHP(pc);
                            }
                            pc.addMaxMp(-50);
                            pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc
                                    .getMaxMp()));
                            pc.sendPackets(new S_PacketBoxCooking(pc, 17, 0));
                            pc.setCookingId(0);
                        }
                        break;

                    case COOKING_3_2_N:
                    case COOKING_3_2_S: // コカトリスステーキ
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_PacketBoxCooking(pc, 18, 0));
                            pc.setCookingId(0);
                        }
                        break;

                    case COOKING_3_3_N:
                    case COOKING_3_3_S: // タートルドラゴン烧き
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.addAc(3);
                            pc.sendPackets(new S_PacketBoxCooking(pc, 19, 0));
                            pc.setCookingId(0);
                        }
                        break;

                    case COOKING_3_4_N:
                    case COOKING_3_4_S: // レッサードラゴンの手羽先
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.addMr(-15);
                            pc.sendPackets(new S_SPMR(pc));
                            pc.addWind(-10);
                            pc.addWater(-10);
                            pc.addFire(-10);
                            pc.addEarth(-10);
                            pc.sendPackets(new S_OwnCharAttrDef(pc));
                            pc.sendPackets(new S_PacketBoxCooking(pc, 20, 0));
                            pc.setCookingId(0);
                        }
                        break;

                    case COOKING_3_5_N:
                    case COOKING_3_5_S: // ドレイク烧き
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.addSp(-2);
                            pc.sendPackets(new S_SPMR(pc));
                            pc.sendPackets(new S_PacketBoxCooking(pc, 21, 0));
                            pc.setCookingId(0);
                        }
                        break;

                    case COOKING_3_6_N:
                    case COOKING_3_6_S: // 深海鱼のシチュー
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.addMaxHp(-30);
                            pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc
                                    .getMaxHp()));
                            if (pc.isInParty()) { // パーティー中
                                pc.getParty().updateMiniHP(pc);
                            }
                            pc.sendPackets(new S_PacketBoxCooking(pc, 22, 0));
                            pc.setCookingId(0);
                        }
                        break;

                    case COOKING_3_7_N:
                    case COOKING_3_7_S: // バシリスクの卵スープ
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_PacketBoxCooking(pc, 23, 0));
                            pc.setDessertId(0);
                        }
                        break;
                    case EFFECT_BLOODSTAIN_OF_ANTHARAS: // 安塔瑞斯的血痕
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.addAc(2);
                            pc.addWater(-50);
                            pc.sendPackets(new S_SkillIconBloodstain(
                                    S_SkillIconBloodstain.ANTHARAS, 0));
                        }
                        break;
                    case EFFECT_BLOODSTAIN_OF_FAFURION: // 法利昂的血痕
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            pc.addWind(-50);
                            pc.sendPackets(new S_SkillIconBloodstain(
                                    S_SkillIconBloodstain.FAFURION, 0));
                        }
                        break;
                    case 7902: ////检测外挂状态 hjx1000
                    	if ((cha instanceof L1PcInstance)) {
                    		final L1PcInstance pc = (L1PcInstance) cha;
                    		final int rndtime = _random.nextInt(ConfigOther.RND_TIME)+1;
                    		if (pc.hasSkillEffect(attack_ing) /*&& pc.hasSkillEffect(Card_Fee)*/) {
                        		pc.setSkillEffect(7903,16000);
                        		final int rndo = _random.nextInt(2000) + 1;
                        		pc.setCheck_number(rndo);
                        		String R2 = String.valueOf(pc.getCheck_number());
                        		pc.sendPackets(new S_BlueMessage(166,"\\f3挂机检测：\\f2请输入  ["+R2+"]  \\f2并按下回车确认."));
                    		} else {
                        		pc.setSkillEffect(7902,rndtime * 60000);
                        	}
                    	} 
                    	break;
                    case 7903://检测外挂状态 hjx1000
                    	if (cha instanceof L1PcInstance) {
                        	final L1PcInstance pc = (L1PcInstance) cha;
                        	if (!pc.hasSkillEffect(SHOCK_STUN)) {
//                        		if (pc.getCheck_cou() < 5) {
                        			//pc.setCheck_cou(pc.getCheck_cou()+1);
                                	pc.setSkillEffect(SHOCK_STUN, 0);
//                        		}
                                pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, true));
                        	}
                        	pc.setSkillEffect(7903,16000);                       	
                    		final int rndo = _random.nextInt(2000) + 1;
                    		pc.setCheck_number(rndo);
                    		String R2 = String.valueOf(pc.getCheck_number());
                    		pc.sendPackets(new S_BlueMessage(166,"挂机检测：\\f2请输入  ["+R2+"]  \\f2并按下回车确认."));
                    	}
                    	break;
                    case Card_Fee:// 点卡计费状态 hjx1000 
                        if (cha instanceof L1PcInstance) {
                            final L1PcInstance pc = (L1PcInstance) cha;
                            final int card_cou = pc.getNetConnection().getAccount().get_card_fee() - 1;
                            if (card_cou >= 0 && pc.isActived()) {
                                pc.getNetConnection().getAccount().set_card_fee(card_cou);
                                AccountReading.get().updatecard_fee(pc.getAccountName(), card_cou);
                                pc.setSkillEffect(Card_Fee, 60 * 1000);//一分钟
                            }
                        }
                    	break;
                }
            }
            // cha.removeSkillEffect(skillId);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }

        if (cha instanceof L1PcInstance) {
            final L1PcInstance pc = (L1PcInstance) cha;
            sendStopMessage(pc, skillId);
            pc.sendPackets(new S_OwnCharStatus(pc));
        }
    }

    // メッセージの表示（终了するとき）
    private static void sendStopMessage(final L1PcInstance charaPc,
            final int skillid) {
        final L1Skills l1skills = SkillsTable.get().getTemplate(skillid);
        if ((l1skills == null) || (charaPc == null)) {
            return;
        }

        final int msgID = l1skills.getSysmsgIdStop();
        if (msgID > 0) {
            charaPc.sendPackets(new S_ServerMessage(msgID));
        }
    }
}
