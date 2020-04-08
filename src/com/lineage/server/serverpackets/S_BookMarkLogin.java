/**
 *                            License
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS  
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). 
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW.  
 * ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS LICENSE OR  
 * COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND  
 * AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE  
 * MAY BE CONSIDERED TO BE A CONTRACT,
 * THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED 
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */
package com.lineage.server.serverpackets;

import java.util.ArrayList;

import com.lineage.server.datatables.lock.CharBookReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1BookMark;

/**
 * 登陆时记忆封包.
 * 
 * @author 透明的冰 2012.09.27
 */
public class S_BookMarkLogin extends ServerBasePacket {
    private byte[] _byte = null;

    public S_BookMarkLogin(L1PcInstance pc, boolean flag) {
        final ArrayList<L1BookMark> bookList = CharBookReading.get()
                .getBookMarks(pc);
        writeC(S_OPCODE_CHARRESET);
        writeC(0x2a);
        writeH(0x80);
        writeC(0x02);
        if (flag) {
            // TODO 待完善
            for (int i = 0; i < 127; i++) {
                if (i <= bookList.size()) {
                    writeC(0x00); // 第一个坐标放在快捷栏/依次类推
                } else if (i <= bookList.size() + 1) {
                    writeC(0x01);
                } else if (i <= bookList.size() + 2) {
                    writeC(0x02);
                } else if (i <= bookList.size() + 3) {
                    writeC(0x03);
                } else if (i <= bookList.size() + 4) {
                    writeC(0xff);
                } else {
                    writeC(0xff);
                }
            }
        } else { // 无记忆坐标
            for (int i = 0; i < 127; i++) {
                writeC(0xff);
            }
        }
        writeH(60); // TODO 最大数量
        if (flag) {
            writeH(bookList.size());
            for (final L1BookMark book : bookList) {
                writeD(book.getId());
                writeS(book.getName());
                writeH(book.getMapId());
                writeH(book.getLocX());
                writeH(book.getLocY());
            }
        } else { // 无记忆坐标
            writeH(0); // 当前记忆坐标数量
            writeD(0);
            writeS("");
            writeH(0);
            writeH(0);
            writeH(0);
        }
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
