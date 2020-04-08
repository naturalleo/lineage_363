/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.lineage.server.serverpackets;

import java.util.ArrayList;
import java.util.List;


import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1ClanMatching;
import com.lineage.server.model.L1ClanMatching.ClanMatchingList;
import com.lineage.server.model.L1War;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import com.lineage.server.world.WorldWar;

public class S_ClanMatching extends ServerBasePacket {
	private static final String S_ClanMatching = "[C] S_ClanMatching";

	/**
	 * type
	 * 0: 殿废, 荐沥				' 肯丰 '
	 * 1: 殿废秒家, 焙林俊霸父
	 * 2: 眠玫趋竿, 货肺绊魔		' 肯丰 ' 
	 * 3: 脚没格废, 货肺绊魔
	 * 4: 夸没格废, 货肺绊魔
	 * 5: 脚没窍扁. 8c bb 84 10
	 * 6: 脚没秒家.
	 */
	public S_ClanMatching(L1PcInstance pc, int type, int objid, String text1, int htype) {
		L1Clan clan = null;
		L1ClanMatching cml = L1ClanMatching.getInstance();
		String clanname = null;
		String text = null;

		writeC(S_OPCODE_CLANMATCHING);
		writeC(type);
		if (type == 2) { // 眠玫趋竿
			ArrayList<ClanMatchingList> _list = new ArrayList<ClanMatchingList>();
			String result = null;
			for (int i=0; i<cml.getMatchingList().size(); i++) {
				result = cml.getMatchingList().get(i)._clanname;
				if (!pc.getCMAList().contains(result)){
					_list.add(cml.getMatchingList().get(i));
				}
			}
			int type2 = 0;
			int size = _list.size();
			writeC(0x00);
			writeC(size); // 肮荐.
			for (int i=0; i<size; i++) {
				clanname = _list.get(i)._clanname;
				text = _list.get(i)._text;
				type2 = _list.get(i)._type;
				clan = WorldClan.get().getClan(clanname);
				writeD(clan.getClanId()); // 趋付农
				writeS(clan.getClanName()); // 趋竿 捞抚.
				writeS(clan.getLeaderName()); // 焙林捞抚
				writeD(clan.getOnlineMaxUser()); // 趋竿 痹葛 : 林埃 弥措 立加磊 荐 
				writeC(type2); // 0: 荤成, 1: 傈捧, 2: 模格

				if (clan.getHouseId()!=0) writeC(0x01); // 酒瘤飘 0: X , 1: O
				else writeC(0x00);

				boolean inWar = false;
				List<L1War> warList = WorldWar.get().getWarList(); // 傈里 府胶飘甫 秒垫
				for (L1War war : warList) {
					if (war.checkClanInWar(clanname)) { // 磊农鄂捞 捞固 傈里吝
						inWar = true;
						break;
					}
				}

				if (inWar) writeC(0x01); // 傈里 惑怕	0: X , 1: O
				else writeC(0x00);
				writeC(0x00); // 绊沥蔼.
				writeS(text);// 家俺膏飘.
				writeD(clan.getClanId()); // 趋竿 objid
			}
			_list.clear();
			_list = null;
		} else if (type == 3) { // 脚没格废
			int size = pc.getCMAList().size();
			int type2 = 0;
			writeC(0x00);
			writeC(size); // 肮荐.
			
			for (int i=0; i<size; i++) {
				clanname = pc.getCMAList().get(i);
				text = cml.getClanMatchingList(clanname)._text;
				type2 = cml.getClanMatchingList(clanname)._type;
				clan = WorldClan.get().getClan(clanname);
				writeD(clan.getClanId()); // 昏力 穿甫锭 哆绰 obj蔼
				writeC(0x00);
				writeD(clan.getClanId()); // 趋付农.
				writeS(clan.getClanName()); // 趋竿 捞抚.
				writeS(clan.getLeaderName()); // 焙林捞抚
				writeD(clan.getOnlineMaxUser()); // 趋竿 痹葛 : 林埃 弥措 立加磊 荐 
				writeC(type2); // 0: 荤成, 1: 傈捧, 2: 模格

				if (clan.getHouseId()!=0) writeC(0x01); // 酒瘤飘 0: X , 1: O
				else writeC(0x00);

				boolean inWar = false;
				List<L1War> warList = WorldWar.get().getWarList(); // 傈里 府胶飘甫 秒垫
				for (L1War war : warList) {
					if (war.checkClanInWar(clanname)) { // 磊农鄂捞 捞固 傈里吝
						inWar = true;
						break;
					}
				}

				if (inWar) writeC(0x01); // 傈里 惑怕	0: X , 1: O
				else writeC(0x00);
				writeC(0x00); // 绊沥蔼.
				writeS(text);// 家俺膏飘.
				writeD(clan.getClanId()); // 趋竿 objid
			}
		} else if (type == 4) { // 夸没格废
			
			if (!cml.isClanMatchingList(pc.getClanname())) {
				writeC(0x82); // 夸没 格废捞 绝阑订 捞巴父 朝赴促.
			} else {
				int size = pc.getCMAList().size();
				String username = null;
				writeC(0x00);
				writeC(0x02);
				writeC(0x00);// 绊沥
				writeC(size); // size
				L1PcInstance user = null;
				for (int i=0; i<size; i++) {
					username = pc.getCMAList().get(i);
					user = World.get().getPlayer(username);
					if (user == null) {
						try {
							user = CharacterTable.get().restoreCharacter(username);
							if (user == null) {
								return;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					writeD(user.getId()); // 脚没磊狼 objectid
					writeC(0x00);
					writeC(user.getOnlineStatus()); // 0x01:立加,  0x00:厚立加
					writeS(username); // 脚没磊狼 捞抚.
					writeC(user.getType()); // 某腐磐 努贰胶
					writeH(user.getLawful()); // 扼快钱
					writeC(user.getLevel()); // 饭骇
					writeC(0x01); // 捞抚菊俊 唱坷绰 钱蕾狼 函版
				}
			}
		} else if (type == 5 || type == 6) {
			writeC(0x00);
			writeD(objid);
			writeC(htype);
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_ClanMatching;
	}
}
