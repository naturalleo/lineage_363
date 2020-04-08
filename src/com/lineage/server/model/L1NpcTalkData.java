package com.lineage.server.model;

public class L1NpcTalkData {

    int ID;

    int NpcID;

    String normalAction;

    String caoticAction;

    String teleportURL;

    String teleportURLA;

    /**
     * @return Returns the normalAction.
     */
    public String getNormalAction() {
        return this.normalAction;
    }

    /**
     * @param normalAction
     *            The normalAction to set.
     */
    public void setNormalAction(final String normalAction) {
        this.normalAction = normalAction;
    }

    /**
     * @return Returns the caoticAction.
     */
    public String getCaoticAction() {
        return this.caoticAction;
    }

    /**
     * @param caoticAction
     *            The caoticAction to set.
     */
    public void setCaoticAction(final String caoticAction) {
        this.caoticAction = caoticAction;
    }

    /**
     * @return Returns the teleportURL.
     */
    public String getTeleportURL() {
        return this.teleportURL;
    }

    /**
     * @param teleportURL
     *            The teleportURL to set.
     */
    public void setTeleportURL(final String teleportURL) {
        this.teleportURL = teleportURL;
    }

    /**
     * @return Returns the teleportURLA.
     */
    public String getTeleportURLA() {
        return this.teleportURLA;
    }

    /**
     * @param teleportURLA
     *            The teleportURLA to set.
     */
    public void setTeleportURLA(final String teleportURLA) {
        this.teleportURLA = teleportURLA;
    }

    /**
     * @return Returns the iD.
     */
    public int getID() {
        return this.ID;
    }

    /**
     * @param id
     *            The iD to set.
     */
    public void setID(final int id) {
        this.ID = id;
    }

    /**
     * @return Returns the npcID.
     */
    public int getNpcID() {
        return this.NpcID;
    }

    /**
     * @param npcID
     *            The npcID to set.
     */
    public void setNpcID(final int npcID) {
        this.NpcID = npcID;
    }

}
