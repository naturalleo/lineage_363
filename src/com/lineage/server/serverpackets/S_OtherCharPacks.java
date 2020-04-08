package com.lineage.server.serverpackets;

import static com.lineage.server.model.skill.L1SkillId.STATUS_BRAVE3;

import java.util.Random;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 物件封包 - 其他人物
 * 
 * @author dexc
 * 
 */
public class S_OtherCharPacks extends ServerBasePacket {

    private static final int STATUS_POISON = 1;
    private static final int STATUS_INVISIBLE = 2;
    private static final int STATUS_PC = 4;
    // private static final int STATUS_FREEZE = 8;
    private static final int STATUS_BRAVE = 16;
    private static final int STATUS_ELFBRAVE = 32;
    private static final int STATUS_FASTMOVABLE = 64;
    // private static final int STATUS_GHOST = 128;
    private static final Random _random = new Random();
    private static String ss = "" + "" + 
	"" + "" + ""
			+ "";

    private byte[] _byte = null;

    /**
     * 物件封包 - 其他人物
     * 
     * @param pc
     */
    public S_OtherCharPacks(final L1PcInstance pc) {
        int status = STATUS_PC;

        if (pc.getPoison() != null) { // 毒状态
            if (pc.getPoison().getEffectId() == 1) {
                status |= STATUS_POISON;
            }
        }
        if (pc.isInvisble()) {
            status |= STATUS_INVISIBLE;
        }
        if (pc.isBrave()) {
            status |= STATUS_BRAVE;
        }
        if (pc.isElfBrave()) {
            // エルヴンワッフルの场合は、STATUS_BRAVEとSTATUS_ELFBRAVEを立てる。
            // STATUS_ELFBRAVEのみでは效果が无い？
            status |= STATUS_BRAVE;
            status |= STATUS_ELFBRAVE;
        }
        if (pc.isFastMovable()) {
            status |= STATUS_FASTMOVABLE;
        }

        this.writeC(S_OPCODE_CHARPACK);
        this.writeH(pc.getX());
        this.writeH(pc.getY());
        this.writeD(pc.getId());

        if (pc.isDead()) {
            this.writeH(pc.getTempCharGfxAtDead());

        } else {
            this.writeH(pc.getTempCharGfx());
        }

        if (pc.isDead()) {
            this.writeC(pc.getStatus());
        } else {
            this.writeC(pc.getCurrentWeapon());
        }

        this.writeC(pc.getHeading());
        // writeC(0); // makes char invis (0x01), cannot move. spells display
		final int ran = _random.nextInt(ss.length());
		final String str = ss.substring(ran, ran+1);
		final String alltitle = pc.getforcetitle();
        this.writeC(pc.getChaLightSize());
        this.writeC(pc.getMoveSpeed());
        this.writeD(0x00000000); // exp
        this.writeH(pc.getLawful());

        final StringBuilder stringBuilder = new StringBuilder();
        if (pc.get_other().get_color() != 0) {
            stringBuilder.append(pc.get_other().color());
        }
        stringBuilder.append(pc.getName());
        stringBuilder.append(str); //防外挂锁定玩家名字 hjx1000

        this.writeS(stringBuilder.toString());
        this.writeS(alltitle);
        this.writeC(status); // 状态
        this.writeD(pc.getClanid());
        this.writeS(pc.getClanname()); // 血盟名称
        this.writeS(null); // 主人名称

        // 0:NPC,道具
        // 1:中毒 ,
        // 2:隐身
        // 4:人物
        // 8:诅咒
        // 16:勇水
        // 32:??
        // 64:??(??)
        // 128:invisible but name
        //this.writeC(pc.getClanRank() << 4);// 血盟阶级
        //修正血盟UI hjx1000
        if (pc.getClanid() == 0)
            writeC(176);
          else {
            switch (pc.getClanRank()) {
            case 3:
              writeC(48);
              break;
            case 2:
            case 7:
              writeC(20);
              break;
            case 5:
            case 8:
              writeC(80);
              break;
            case 6:
            case 9:
              writeC(96);
              break;
            case 4:
            case 10:
              writeC(160);
            }
          }

        this.writeC(0xff); // HP显示
        if (pc.hasSkillEffect(STATUS_BRAVE3)) {
            this.writeC(0x08); // 巧克力蛋糕

        } else {
            this.writeC(0x00); // タルクック距离(通り)
        }
        this.writeC(0x00); // LV
        this.writeC(0x00);
        this.writeC(0xff);
        this.writeC(0xff);
    }

    @Override
    public byte[] getContent() {
        if (this._byte == null) {
            this._byte = this.getBytes();
        }
        return this._byte;
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
