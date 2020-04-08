package com.lineage.server.datatables.storage;

import java.util.Map;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Mail;

/**
 * 信件资料
 * 
 * @author dexc
 * 
 */
public interface MailStorage {

    public void load();

    public void setReadStatus(int mailId);

    public void setMailType(int mailId, int type);

    public void deleteMail(int mailId);

    public void writeMail(int type, String receiver, L1PcInstance writer,
            byte[] text);

    public Map<Integer, L1Mail> getAllMail();

    public L1Mail getMail(int mailId);
}
