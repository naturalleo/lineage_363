package com.lineage.server.templates;

public class L1Mail {
    public L1Mail() {
    }

    private int _id;

    public int getId() {
        return this._id;
    }

    public void setId(final int i) {
        this._id = i;
    }

    private int _type;

    public int getType() {
        return this._type;
    }

    public void setType(final int i) {
        this._type = i;
    }

    private String _senderName;

    public String getSenderName() {
        return this._senderName;
    }

    public void setSenderName(final String s) {
        this._senderName = s;
    }

    private String _receiverName;

    public String getReceiverName() {
        return this._receiverName;
    }

    public void setReceiverName(final String s) {
        this._receiverName = s;
    }

    private String _date = null; // yy/mm/dd

    public String getDate() {
        return this._date;
    }

    public void setDate(final String s) {
        this._date = s;
    }

    private int _readStatus = 0;

    public int getReadStatus() {
        return this._readStatus;
    }

    public void setReadStatus(final int i) {
        this._readStatus = i;
    }

    private byte[] _subject = null;

    public byte[] getSubject() {
        return this._subject;
    }

    public void setSubject(final byte[] arg) {
        final byte[] newarg = new byte[arg.length - 2];
        System.arraycopy(arg, 0, newarg, 0, newarg.length);
        this._subject = newarg;
    }

    private byte[] _content = null;

    public byte[] getContent() {
        return this._content;
    }

    public void setContent(final byte[] arg) {
        this._content = arg;
    }

}
