package com.lineage.server.datatables.lock;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.MailTable;
import com.lineage.server.datatables.storage.MailStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Mail;

/**
 * 信件资料
 * 
 * @author dexc
 * 
 */
public class MailReading {

    private final Lock _lock;

    private final MailStorage _storage;

    private static MailReading _instance;

    private MailReading() {
        this._lock = new ReentrantLock(true);
        this._storage = new MailTable();
    }

    public static MailReading get() {
        if (_instance == null) {
            _instance = new MailReading();
        }
        return _instance;
    }

    public void load() {
        this._lock.lock();
        try {
            this._storage.load();

        } finally {
            this._lock.unlock();
        }
    }

    public void setReadStatus(final int mailId) {
        this._lock.lock();
        try {
            this._storage.setReadStatus(mailId);

        } finally {
            this._lock.unlock();
        }
    }

    public void setMailType(final int mailId, final int type) {
        this._lock.lock();
        try {
            this._storage.setMailType(mailId, type);

        } finally {
            this._lock.unlock();
        }
    }

    public void deleteMail(final int mailId) {
        this._lock.lock();
        try {
            this._storage.deleteMail(mailId);

        } finally {
            this._lock.unlock();
        }
    }

    public void writeMail(final int type, final String receiver,
            final L1PcInstance writer, final byte[] text) {
        this._lock.lock();
        try {
            this._storage.writeMail(type, receiver, writer, text);

        } finally {
            this._lock.unlock();
        }
    }

    public Map<Integer, L1Mail> getAllMail() {
        this._lock.lock();
        Map<Integer, L1Mail> tmp;
        try {
            tmp = this._storage.getAllMail();

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    public L1Mail getMail(final int mailId) {
        this._lock.lock();
        L1Mail tmp;
        try {
            tmp = this._storage.getMail(mailId);

        } finally {
            this._lock.unlock();
        }
        return tmp;
    }

    public ArrayList<L1Mail> getMails(final String receiverName) {
        final ArrayList<L1Mail> mailList = new ArrayList<L1Mail>();
        for (final L1Mail mail : this.getAllMail().values()) {
            if (mail.getReceiverName().equalsIgnoreCase(receiverName)) {
                mailList.add(mail);
            }
        }
        if (mailList.size() > 0) {
            return mailList;
        }
        return null;
    }

    public int getMailSizeByReceiver(final String receiverName, final int type) {
        final ArrayList<L1Mail> mailList = new ArrayList<L1Mail>();
        for (final L1Mail mail : this.getAllMail().values()) {
            if (mail.getReceiverName().equalsIgnoreCase(receiverName)) {
                if (mail.getType() == type) {
                    mailList.add(mail);
                }
            }
        }
        if (mailList.size() > 0) {
            return mailList.size();
        }
        return 0;
    }
}
