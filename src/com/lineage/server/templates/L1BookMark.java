package com.lineage.server.templates;

public class L1BookMark {

    private int _charId;

    private int _id;

    private String _name;

    private int _locX;

    private int _locY;

    private short _mapId;

    public int getId() {
        return this._id;
    }

    public void setId(final int i) {
        this._id = i;
    }

    public int getCharId() {
        return this._charId;
    }

    public void setCharId(final int i) {
        this._charId = i;
    }

    public String getName() {
        return this._name;
    }

    public void setName(final String s) {
        this._name = s;
    }

    public int getLocX() {
        return this._locX;
    }

    public void setLocX(final int i) {
        this._locX = i;
    }

    public int getLocY() {
        return this._locY;
    }

    public void setLocY(final int i) {
        this._locY = i;
    }

    public short getMapId() {
        return this._mapId;
    }

    public void setMapId(final short i) {
        this._mapId = i;
    }
}
