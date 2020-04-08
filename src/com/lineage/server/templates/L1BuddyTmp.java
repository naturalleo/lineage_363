package com.lineage.server.templates;

/**
 * 人物好友纪录
 * 
 * @author dexc
 * 
 */
public class L1BuddyTmp {

    private int _char_id;

    private int _buddy_id;

    private String _buddy_name;

    public int get_char_id() {
        return this._char_id;
    }

    public void set_char_id(final int char_id) {
        this._char_id = char_id;
    }

    public int get_buddy_id() {
        return this._buddy_id;
    }

    public void set_buddy_id(final int buddy_id) {
        this._buddy_id = buddy_id;
    }

    public String get_buddy_name() {
        return this._buddy_name;
    }

    public void set_buddy_name(final String buddy_name) {
        this._buddy_name = buddy_name;
    }

}
