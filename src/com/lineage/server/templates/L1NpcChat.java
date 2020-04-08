package com.lineage.server.templates;

/**
 * NPC 说话资料
 * 
 * @author daien
 * 
 */
public class L1NpcChat {
    public L1NpcChat() {
    }

    private int _npcId;

    public int getNpcId() {
        return this._npcId;
    }

    public void setNpcId(final int i) {
        this._npcId = i;
    }

    private int _chatTiming;

    public int getChatTiming() {
        return this._chatTiming;
    }

    public void setChatTiming(final int i) {
        this._chatTiming = i;
    }

    private int _startDelayTime;

    public int getStartDelayTime() {
        return this._startDelayTime;
    }

    public void setStartDelayTime(final int i) {
        this._startDelayTime = i;
    }

    private String _chatId1;

    public String getChatId1() {
        return this._chatId1;
    }

    public void setChatId1(final String s) {
        this._chatId1 = s;
    }

    private String _chatId2;

    public String getChatId2() {
        return this._chatId2;
    }

    public void setChatId2(final String s) {
        this._chatId2 = s;
    }

    private String _chatId3;

    public String getChatId3() {
        return this._chatId3;
    }

    public void setChatId3(final String s) {
        this._chatId3 = s;
    }

    private String _chatId4;

    public String getChatId4() {
        return this._chatId4;
    }

    public void setChatId4(final String s) {
        this._chatId4 = s;
    }

    private String _chatId5;

    public String getChatId5() {
        return this._chatId5;
    }

    public void setChatId5(final String s) {
        this._chatId5 = s;
    }

    private int _chatInterval;

    public int getChatInterval() {
        return this._chatInterval;
    }

    public void setChatInterval(final int i) {
        this._chatInterval = i;
    }

    private boolean _isShout;

    public boolean isShout() {
        return this._isShout;
    }

    public void setShout(final boolean flag) {
        this._isShout = flag;
    }

    private boolean _isWorldChat;

    public boolean isWorldChat() {
        return this._isWorldChat;
    }

    public void setWorldChat(final boolean flag) {
        this._isWorldChat = flag;
    }

    private boolean _isRepeat;

    public boolean isRepeat() {
        return this._isRepeat;
    }

    public void setRepeat(final boolean flag) {
        this._isRepeat = flag;
    }

    private int _repeatInterval;

    public int getRepeatInterval() {
        return this._repeatInterval;
    }

    public void setRepeatInterval(final int i) {
        this._repeatInterval = i;
    }

    private int _gameTime;

    public int getGameTime() {
        return this._gameTime;
    }

    public void setGameTime(final int i) {
        this._gameTime = i;
    }

}
