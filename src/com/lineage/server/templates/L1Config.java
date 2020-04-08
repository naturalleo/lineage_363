package com.lineage.server.templates;

/**
 * 快速键纪录
 * 
 * @author dexc
 * 
 */
public class L1Config {

    private int objid = 0;

    private int length = 0;

    private byte[] data = null;

    /**
     * @return the objid
     */
    public int getObjid() {
        return this.objid;
    }

    /**
     * @param objid
     *            the objid to set
     */
    public void setObjid(final int objid) {
        this.objid = objid;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return this.length;
    }

    /**
     * @param length
     *            the length to set
     */
    public void setLength(final int length) {
        this.length = length;
    }

    /**
     * @return the data
     */
    public byte[] getData() {
        return this.data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(final byte[] data) {
        this.data = data;
    }

}
