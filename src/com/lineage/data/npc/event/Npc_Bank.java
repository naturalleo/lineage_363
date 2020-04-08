package com.lineage.data.npc.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.event.BankSet;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.AccountBankReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_ItemCount;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Bank;
import com.lineage.server.templates.L1Item;
import com.lineage.server.utils.RangeLong;

/**
 * 银行管理员<BR>
 * 50002<BR>
 * 
 * @author loli
 * 
 */
public class Npc_Bank extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Bank.class);

    // 使用者清单
    private static final Map<String, BankType> _bankTypeMap = new ConcurrentHashMap<String, BankType>();

    private Npc_Bank() {
        // TODO Auto-generated constructor stub
    }

    private class BankType {
        String _account_name = null;// 帐户名称
        boolean _isNew = false;// false:旧客户 true:新客户
        String _pass_x1 = "";// 第一次输入的密码
        String _pass = "";// 输入的密码
        boolean _type = false;// false:存款 true:取款
        boolean _repass = false;// false:无 true:更改密码
    }

    public static NpcExecutor get() {
        return new Npc_Bank();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "1y_bank_1",
                    new String[] {
                            RangeLong.scount(BankSet.BANKMAX).toString(),
                            RangeLong.scount(BankSet.BANK_INTEREST_OVER)
                                    .toString() }));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        try {
            BankType bankType = null;
            if (_bankTypeMap.get(pc.getAccountName()) == null) {
                bankType = new BankType();
                bankType._account_name = pc.getAccountName();
                _bankTypeMap.put(bankType._account_name, bankType);

            } else {
                bankType = _bankTypeMap.get(pc.getAccountName());
            }

            L1Bank bank = AccountBankReading.get().get(pc.getAccountName());
            boolean isClose = false;
            String pass = "n";

            if (cmd.equalsIgnoreCase("A")) {// 进入银行作业
                if (bank == null) {
                    bankType._isNew = true;
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                            "1y_bank_4", new String[] { "" }));

                } else {
                    // 恢复预设质
                    bankType._isNew = false;// false:旧客户 true:新客户
                    bankType._pass_x1 = "";// 第一次输入的密码
                    bankType._pass = "";// 输入的密码
                    bankType._type = false;// false:存款 true:取款
                    bankType._repass = false;// false:无 true:更改密码

                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                            "1y_bank_2", new String[] {
                                    RangeLong.scount(bank.get_adena_count())
                                            .toString(),
                                    String.valueOf(BankSet.BANK_TIME),
                                    RangeLong.scount(BankSet.BANKMAX)
                                            .toString(),
                                    String.valueOf(BankSet.BANK_INTEREST) }));
                }

            } else if (cmd.equalsIgnoreCase("B")) {// 我要存款
                bankType._type = false;
                pc.sendPackets(new S_ItemCount(npc.getId(), 100, 2000000000,
                        "1y_bank_3", "I"));

            } else if (cmd.equalsIgnoreCase("I")) {// 我要存款 CMD
                if (!bankType._type && bank != null) {
                    if (bank.get_adena_count() >= BankSet.BANKMAX) {
                        isClose = true;
                        pc.sendPackets(new S_ServerMessage(
                                "\\fR你的存款已经超过可存款最大限额!"));

                    } else {
                        final L1ItemInstance item = pc.getInventory()
                                .checkItemX(BankSet.BANK_ITEMID, amount);
                        if (item != null) {
                            long count = amount + bank.get_adena_count();
                            pc.getInventory().removeItem(item, amount);// 删除道具

                            isClose = true;
                            bank.set_adena_count(count);
                            AccountBankReading.get().updateAdena(
                                    bankType._account_name, count);
                            pc.sendPackets(new S_ServerMessage("\\fU你要求的 "
                                    + item.getName() + "("
                                    + RangeLong.scount(amount) + ") 已经存入银行"));

                        } else {
                            isClose = true;
                            // 找回物品资讯
                            final L1Item itemtmp = ItemTable.get().getTemplate(
                                    BankSet.BANK_ITEMID);
                            pc.sendPackets(new S_ServerMessage("\\fR你身上的 "
                                    + itemtmp.getNameId() + " 数量低于您要求存入的数字"));
                        }
                    }
                }

            } else if (cmd.equalsIgnoreCase("C")) {// 我要取款
                if (bank != null) {
                    if (bank.isEmpty()) {
                        isClose = true;
                        pc.sendPackets(new S_ServerMessage("\\fR你的帐户目前没有余额!"));

                    } else {
                        bankType._type = true;
                        bankType._pass = "";
                        bankType._pass_x1 = "";
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "1y_bank_8", new String[] { "" }));
                    }
                }

            } else if (cmd.equalsIgnoreCase("O")) {// 我要取款 CMD
                if (bankType._type && bank != null) {
                    long count = amount;
                    if (bank.get_adena_count() - amount <= 0) {
                        count = bank.get_adena_count();
                    }
                    long newCount = bank.get_adena_count() - count;
                    bank.set_adena_count(newCount);
                    AccountBankReading.get().updateAdena(
                            bankType._account_name, newCount);
                    // 取得道具
                    CreateNewItem.createNewItem(pc, BankSet.BANK_ITEMID, count);
                }

            } else if (cmd.equalsIgnoreCase("D")) {// 回到银行作业
                _bankTypeMap.remove(pc.getAccountName());
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "1y_bank_1",
                        new String[] {
                                RangeLong.scount(BankSet.BANKMAX).toString(),
                                String.valueOf(BankSet.BANK_INTEREST_OVER) }));

            } else if (cmd.equalsIgnoreCase("E")) {// 重新输入密码
                bankType._pass = "";
                bankType._pass_x1 = "";
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "1y_bank_4",
                        new String[] { "" }));

            } else if (cmd.equalsIgnoreCase("F")) {// 更改取款密码
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "1y_bank_8",
                        new String[] { "" }));
                bankType._repass = true;

            } else if (cmd.equalsIgnoreCase("Z")) {// 取消
                isClose = true;

            } else if (cmd.equalsIgnoreCase("0")) {//
                pass = "0";

            } else if (cmd.equalsIgnoreCase("1")) {//
                pass = "1";

            } else if (cmd.equalsIgnoreCase("2")) {//
                pass = "2";

            } else if (cmd.equalsIgnoreCase("3")) {//
                pass = "3";

            } else if (cmd.equalsIgnoreCase("4")) {//
                pass = "4";

            } else if (cmd.equalsIgnoreCase("5")) {//
                pass = "5";

            } else if (cmd.equalsIgnoreCase("6")) {//
                pass = "6";

            } else if (cmd.equalsIgnoreCase("7")) {//
                pass = "7";

            } else if (cmd.equalsIgnoreCase("8")) {//
                pass = "8";

            } else if (cmd.equalsIgnoreCase("9")) {//
                pass = "9";

            }

            if (bankType._isNew) {
                if (!pass.equals("n")) {
                    String out = "";
                    if (bankType._pass_x1.length() == 6) {
                        switch (bankType._pass.length()) {
                            case 0:
                                bankType._pass = pass;
                                out = "*";
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "1y_bank_5", new String[] { out }));
                                break;

                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                                bankType._pass += pass;
                                for (int i = 0; i < bankType._pass.length(); i++) {
                                    out += "*";
                                }

                                if (bankType._pass.length() == 6) {
                                    if (bankType._pass_x1
                                            .equals(bankType._pass)) {
                                        if (!bankType._repass) {
                                            pc.sendPackets(new S_NPCTalkReturn(
                                                    npc.getId(),
                                                    "1y_bank_6",
                                                    new String[] { bankType._pass }));
                                            bank = new L1Bank();
                                            bank.set_account_name(bankType._account_name);
                                            bank.set_adena_count(0);
                                            bank.set_pass(bankType._pass);
                                            // 建立帐户资料
                                            AccountBankReading.get().create(
                                                    bankType._account_name,
                                                    bank);

                                        } else {
                                            isClose = true;
                                            bank.set_pass(bankType._pass);
                                            // 建立帐户资料
                                            AccountBankReading
                                                    .get()
                                                    .updatePass(
                                                            bankType._account_name,
                                                            bankType._pass);
                                            pc.sendPackets(new S_ServerMessage(
                                                    "\\fR你的密码变更完成!!"));
                                        }

                                    } else {
                                        pc.sendPackets(new S_NPCTalkReturn(npc
                                                .getId(), "1y_bank_7"));
                                    }

                                } else {
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "1y_bank_5",
                                            new String[] { out }));
                                }
                                break;
                        }

                    } else {
                        switch (bankType._pass_x1.length()) {
                            case 0:
                                bankType._pass_x1 = pass;
                                out = "*";
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "1y_bank_4", new String[] { out }));
                                break;

                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                                bankType._pass_x1 += pass;
                                for (int i = 0; i < bankType._pass_x1.length(); i++) {
                                    out += "*";
                                }

                                if (bankType._pass_x1.length() == 6) {
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "1y_bank_5",
                                            new String[] { "" }));

                                } else {
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "1y_bank_4",
                                            new String[] { out }));
                                }
                                break;
                        }
                    }
                }

            } else {
                if (!pass.equals("n") && (bankType._type || bankType._repass)) {
                    String out = "";
                    switch (bankType._pass.length()) {
                        case 0:
                            bankType._pass = pass;
                            out = "*";
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "1y_bank_8", new String[] { out }));
                            break;

                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            bankType._pass += pass;
                            for (int i = 0; i < bankType._pass.length(); i++) {
                                out += "*";
                            }

                            if (bankType._pass.length() == 6) {
                                if (bankType._pass.equals(bank.get_pass())) {
                                    if (bankType._repass) {
                                        bankType._pass = "";
                                        bankType._pass_x1 = "";
                                        bankType._isNew = true;
                                        pc.sendPackets(new S_NPCTalkReturn(npc
                                                .getId(), "1y_bank_4",
                                                new String[] { "" }));

                                    } else {
                                        pc.sendPackets(new S_ItemCount(npc
                                                .getId(), 100, 2000000000,
                                                "1y_bank_3", "O"));
                                    }

                                } else {
                                    isClose = true;
                                    // 835 密码错误。
                                    pc.sendPackets(new S_ServerMessage(835));
                                }

                            } else {
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "1y_bank_8", new String[] { out }));
                            }
                            break;
                    }
                }
            }

            if (isClose) {
                _bankTypeMap.remove(pc.getAccountName());
                pc.sendPackets(new S_CloseList(pc.getId()));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
