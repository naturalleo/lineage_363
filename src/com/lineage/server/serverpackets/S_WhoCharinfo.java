package com.lineage.server.serverpackets;

import java.util.List;

import com.lineage.server.model.Instance.L1ItemInstance;

/**
 * 伺服器讯息(行数/行数,附加字串)
 * 
 * @author dexc
 * 
 */
//public class S_WhoCharinfo extends ServerBasePacket {
//
//    private byte[] _byte = null;
//
//    /**
//     * 伺服器讯息(行数/行数,附加字串)
//     * 
//     * @param cha
//     */
//    public S_WhoCharinfo(final L1Character cha) {
//        String lawfulness = "";
//        final int lawful = cha.getLawful();
//        if (lawful < 0) {
//            lawfulness = "($1503)";// 邪恶者
//
//        } else if ((lawful >= 0) && (lawful < 500)) {
//            lawfulness = "($1502)";// 中立者
//
//        } else if (lawful >= 500) {
//            lawfulness = "($1501)";// 正义者
//        }
//
//        writeC(S_OPCODE_SERVERMSG);
//        writeH(0x00a6);// 166
//        writeC(0x01);
//
//        // this.writeC(S_OPCODE_GLOBALCHAT);
//        // this.writeC(0x08);
//
//        String title = "";
//        String clan = "";
//
//        if (cha.getTitle().equalsIgnoreCase("") == false) {
//            title = cha.getTitle() + " ";
//        }
//
//        String name = "";
//
//        if (cha instanceof L1PcInstance) {
//            L1PcInstance pc = (L1PcInstance) cha;
//            name = pc.getName();
//            if (pc.getClanid() > 0) {
//                clan = "[" + pc.getClanname() + "]";
//            }
//        }
//
//        writeS(title + name + " " + lawfulness + " " + clan);
//        // this.writeS(title + name + " " + lawfulness + " " + clan);
//        // writeD(0x80157FE4);
//        // this.writeD(0);
//    }
//
//    @Override
//    public byte[] getContent() {
//        if (_byte == null) {
//            _byte = this.getBytes();
//        }
//        return _byte;
//    }
//
//    @Override
//    public String getType() {
//        return getClass().getSimpleName();
//    }
//}
public class S_WhoCharinfo extends ServerBasePacket {
	
	private static final String _S_WhoCharinfo = "[S] _S_WhoCharinfo";

    private byte[] _byte = null;

    /**
     * 查看玩家的装备
     * 
     * @param hjx1000
     */
    public S_WhoCharinfo(final List<L1ItemInstance> items) {
//	    String lawfulness = "";
//	    final int lawful = cha.getLawful();
//	    if (lawful < 0) {
//	        lawfulness = "(邪恶者)";// 邪恶者
//	
//	    } else if ((lawful >= 0) && (lawful < 500)) {
//	        lawfulness = "(中立者)";// 中立者
//	
//	    } else if (lawful >= 500) {
//	        lawfulness = "(正义者)";// 正义者
//	    }
//	    
//    	final L1PcInstance pc = (L1PcInstance) cha;
//    	String title = "";
//    	String clan = "";
//    	String name = "";
//    	if (cha.getTitle().equalsIgnoreCase("") == false) {
//    		title = cha.getTitle() + " ";
//    	}
//    	name = pc.getName();
//    	if (pc.getClanid() > 0) {
//    		clan = "[" + pc.getClanname() + "]";
//    	}
//        final String nowDate = new SimpleDateFormat(
//                "yyyy/MM/dd HH:mm:ss").format(new Date());
//        String weapon = null;
//        String helm = null; //头盔
//        String armor = null;//盔甲
//        String T = null;//内衣
//        String cloak = null;//斗篷
//        String glove = null;//手套
//        String boots = null;//靴子
//        String shield = null;//盾牌
//        String amulet  = null;//项链
//        String ring1  = null;//戒指
//        String ring2  = null;//戒指
//        String ring3  = null;//戒指
//        String ring4  = null;//戒指
//        String belt  = null;//腰带
//        String earring = null;//耳环
//        String guarder = null;//臂甲
//        final List<L1ItemInstance> armors = pc.getInventory().getItems();
//        for (final L1ItemInstance item : armors) {
//        	if (item.isEquipped()) {  
//        		if (item.getItem().getType2() == 2) {
//            		switch(item.getItem().getType()) {
//            		case 1:
//            			helm = "暂不开放显示";
//            			break;
//            		case 2:
//            			armor = "暂不开放显示";
//            			break;
//            		case 3:
//            			T = "暂不开放显示";
//            			break;
//            		case 4:
//            			cloak = "暂不开放显示";
//            			break;
//            		case 5:
//            			glove = "暂不开放显示";
//            			break;
//            		case 6:
//            			boots = "暂不开放显示";
//            			break;
//            		case 7:
//            			shield = "暂不开放显示";
//            			break;
//            		case 8:
//            			amulet = "暂不开放显示";
//            			break;
//            		case 9:
//            			ring1 = "暂不开放显示";
//            			break;
//            		case 10:
//            			belt = "暂不开放显示";
//            			break;
//            		case 12:
//            			earring = "暂不开放显示";
//            			break;
//            		case 13:
//            			guarder = "暂不开放显示";
//            			break;
//            		}
//        		} else {
//        			weapon = item.getNumberedName_to_String();
//        		}
//        	}
//        }
//        final String S_WhoCharinfo = 
//        "封号:" + title + "\r\n" + 
//        "玩家名称:" + name + "\r\n" + 
//        "向性:" + lawfulness + "\r\n" + 
//        "血盟:" + clan + "\r\n" +
//        "等级:" + pc.getLevel() + "\r\n" +
//        "武器: " + weapon + "\r\n" +
//        "头盔: " + helm + "\r\n" +
//        "盔甲: " + armor + "\r\n" +
//        "内衣: " + T + "\r\n" +
//        "斗篷: " + cloak + "\r\n" +
//        "手套: " + glove + "\r\n" +
//    	"靴子: " + boots + "\r\n" +
//        "盾牌: " + shield + "\r\n" +
//    	"项链: " + amulet + "\r\n" +
//        "戒指: " + ring1 + "\r\n" +
//    	"腰带: " + belt + "\r\n" +
//        "耳环: " + earring + "\r\n" +
//    	"臂甲: " + guarder + "\r\n" +
//        "当前时间:" + nowDate;
//        this.writeC(S_OPCODE_BOARDREAD);
//        this.writeD(0x00);
//        this.writeS("lineage"); // 作者
//        this.writeS("查看对方资料"); // 标题
//        this.writeS(""); // 讨论编号
//        this.writeS(S_WhoCharinfo); // 显示查询信息
    	
    	
        this.writeC(S_OPCODE_SHOWRETRIEVELIST);
        this.writeD(0);
        this.writeH(items.size());
        this.writeC(0x0c); // 提炼武器/托售 ---封包代码== 12
        for (final L1ItemInstance item : items) {
            final int itemobjid = item.getId();
            this.writeD(itemobjid);
            // System.out.println("itemobjid:" + itemobjid);
            this.writeC(0x00);
            this.writeH(item.get_gfxid());
            this.writeC(item.getBless());
            this.writeD(1);
            this.writeC(item.isIdentified() ? 0x01 : 0x00);
            this.writeS(item.getNumberedName_to_String());
        }
		writeD(0);
		writeD(0x00000000);
		writeH(0x00);
        items.clear();
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
