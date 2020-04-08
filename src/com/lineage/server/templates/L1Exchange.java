package com.lineage.server.templates;

/**
 * 兑换NPC
 * 
 * @author hjx1000
 * 
 */
public class L1Exchange {

    private int _price_count = 0;
    
    private int _npcid = 0;
    
    private int _upnumber = 0;

    public int get_adena_count() {
        return _price_count;
    }

    public void set_price_count(final int _price_count) {
        this._price_count = _price_count;
    }

	public int get_npcid() {
		return _npcid;
	}

	public void set_npcid(final int _npcid) {
		this._npcid = _npcid;
	}

	public int get_upnumber() {
		return _upnumber;
	}

	public void set_upnumber(final int _upnumber) {
		this._upnumber = _upnumber;
	}
}
