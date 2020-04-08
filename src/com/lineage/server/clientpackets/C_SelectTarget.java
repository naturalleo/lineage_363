package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1CnInstance;
import com.lineage.server.model.Instance.L1GamInstance;
import com.lineage.server.model.Instance.L1GamblingInstance;
import com.lineage.server.model.Instance.L1IllusoryInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldPet;

/**
 * 要求攻击指定物件
 * 
 * @author daien
 * 
 */
public class C_SelectTarget extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_SelectTarget.class);

    /*
     * public C_SelectTarget() { }
     * 
     * public C_SelectTarget(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final int petId = this.readD();
            // System.out.println("type:"+petId);
            final int type = this.readC();
            // System.out.println("type:"+type);
            final int targetId = this.readD();
            // System.out.println("type:"+targetId);

            final L1PetInstance pet = WorldPet.get().get(petId);

            if (pet == null) {
                return;
            }

            final L1Character target = (L1Character) World.get().findObject(
                    targetId);

            if (target == null) {
                return;
            }

            boolean isCheck = false;

            if (target instanceof L1PcInstance) {// PC
                final L1PcInstance tgpc = (L1PcInstance) target;
                if (tgpc.checkNonPvP(tgpc, pet)) {
                    return;
                }
                isCheck = true;

            } else if (target instanceof L1PetInstance) {// 宠物
                isCheck = true;

            } else if (target instanceof L1SummonInstance) {// 召唤兽
                isCheck = true;

            } else if (target instanceof L1CnInstance) {// CN 专属商人
                return;

            } else if (target instanceof L1GamblingInstance) {// 赌场NPC
                return;

            } else if (target instanceof L1GamInstance) {// 赌场NPC(参赛者)
                return;

            } else if (target instanceof L1IllusoryInstance) {// 分身
                return;
            }

            if (isCheck) {
                if (target.isSafetyZone()) {
                    return;
                }
            }
            pet.setMasterSelectTarget(target);

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
