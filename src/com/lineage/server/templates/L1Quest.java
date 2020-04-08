package com.lineage.server.templates;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * Quest(任务)设置 暂存
 * 
 * @author daien
 * 
 */
public class L1Quest {

    private static final Log _log = LogFactory.getLog(L1Quest.class);

    private int _id;// 任务编号

    private String _questname;// 任务NAMEID

    private String _questclass;// 任务CLASS

    private boolean _queststart;// 任务是否启用

    private boolean _del;// 任务是否可以删除

    private int _questlevel;// 任务可执行等级

    private int _difficulty;// 任务难度

    private String _note;// 任务说明

    private boolean _isCrown;// 王族可执行:1

    private boolean _isKnight;// 骑士可执行:2

    private boolean _isElf;// 精灵可执行:4

    private boolean _isWizard;// 法师可执行:8

    private boolean _isDarkelf;// 黑暗精灵可执行:16

    private boolean _isDragonKnight;// 龙骑士可执行:32

    private boolean _isIllusionist;// 幻术师可执行:64

    /**
     * 任务编号
     * 
     * @return
     */
    public int get_id() {
        return _id;
    }

    /**
     * 任务编号
     * 
     * @param _id
     */
    public void set_id(int _id) {
        this._id = _id;
    }

    /**
     * 任务NAMEID
     * 
     * @return
     */
    public String get_questname() {
        return _questname;
    }

    /**
     * 任务NAMEID
     * 
     * @param _questname
     */
    public void set_questname(String _questname) {
        this._questname = _questname;
    }

    /**
     * 任务CLASS
     * 
     * @return
     */
    public String get_questclass() {
        return _questclass;
    }

    /**
     * 任务CLASS
     * 
     * @param _questclass
     */
    public void set_questclass(String _questclass) {
        this._questclass = _questclass;
    }

    /**
     * 任务是否启用
     * 
     * @return
     */
    public boolean is_queststart() {
        return _queststart;
    }

    /**
     * 任务是否启用
     * 
     * @param _queststart
     */
    public void set_queststart(boolean _queststart) {
        this._queststart = _queststart;
    }

    /**
     * 任务可执行等级
     * 
     * @return
     */
    public int get_questlevel() {
        return _questlevel;
    }

    /**
     * 任务可执行等级
     * 
     * @param _questlevel
     */
    public void set_questlevel(int _questlevel) {
        this._questlevel = _questlevel;
    }

    /**
     * 任务步骤
     * 
     * @param _difficulty
     */
    public void set_difficulty(int _difficulty) {
        this._difficulty = _difficulty;
    }

    /**
     * 任务步骤
     * 
     * @return
     */
    public int get_difficulty() {
        return _difficulty;
    }

    /**
     * 任务说明
     * 
     * @param _note
     */
    public void set_note(String _note) {
        this._note = _note;
    }

    /**
     * 任务说明
     * 
     * @return
     */
    public String get_note() {
        return _note;
    }

    private static final int _int7 = 64;// 幻术师可执行:64
    private static final int _int6 = 32;// 龙骑士可执行:32
    private static final int _int5 = 16;// 黑暗精灵可执行:16
    private static final int _int4 = 8;// 法师可执行:8
    private static final int _int3 = 4;// 精灵可执行:4
    private static final int _int2 = 2;// 骑士可执行:2
    private static final int _int1 = 1;// 王族可执行:1

    /**
     * 任务可执行职业设置
     * 
     * @param questuser
     */
    public void set_questuser(int questuser) {
        try {
            if (questuser >= _int7) {
                // 幻术师可执行:64
                questuser -= _int7;
                _isIllusionist = true;
            }
            if (questuser >= _int6) {
                // 龙骑士可执行:32
                questuser -= _int6;
                _isDragonKnight = true;
            }
            if (questuser >= _int5) {
                // 黑暗精灵可执行:16
                questuser -= _int5;
                _isDarkelf = true;
            }
            if (questuser >= _int4) {
                // 法师可执行:8
                questuser -= _int4;
                _isWizard = true;
            }
            if (questuser >= _int3) {
                // 精灵可执行:4
                questuser -= _int3;
                _isElf = true;
            }
            if (questuser >= _int2) {
                // 骑士可执行:2
                questuser -= _int2;
                _isKnight = true;
            }
            if (questuser >= _int1) {
                // 王族可执行:1
                questuser -= _int1;
                _isCrown = true;
            }

            if (questuser > 0) {
                _log.error("任务可执行职业设定错误:余数大于0 编号:" + _id);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 王族可执行:1
     * 
     * @return
     */
    /*
     * public boolean isCrown() { return _isCrown; }
     * 
     * /** 骑士可执行:2
     * 
     * @return
     */
    /*
     * public boolean isKnight() { return _isKnight; }
     * 
     * /** 精灵可执行:4
     * 
     * @return
     */
    /*
     * public boolean isElf() { return _isElf; }
     * 
     * /** 法师可执行:8
     * 
     * @return
     */
    /*
     * public boolean isWizard() { return _isWizard; }
     * 
     * /** 黑暗精灵可执行:16
     * 
     * @return
     */
    /*
     * public boolean isDarkelf() { return _isDarkelf; }
     * 
     * /** 龙骑士可执行:32
     * 
     * @return
     */
    /*
     * public boolean isDragonKnight() { return _isDragonKnight; }
     * 
     * /** 幻术师可执行:64
     * 
     * @return
     */
    /*
     * public boolean isIllusionist() { return _isIllusionist; }
     */

    /**
     * 可执行职业判断
     * 
     * @param pc
     * @return
     */
    public boolean check(final L1PcInstance pc) {
        try {
            if (pc.isCrown() && _isCrown) {
                // 王族可执行:1
                return true;
            }
            if (pc.isKnight() && _isKnight) {
                // 骑士可执行:2
                return true;
            }
            if (pc.isElf() && _isElf) {
                // 精灵可执行:4
                return true;
            }
            if (pc.isWizard() && _isWizard) {
                // 法师可执行:8
                // System.out.println("法师可执行:8 - 任务编号" + _id);
                return true;
            }
            if (pc.isDarkelf() && _isDarkelf) {
                // 黑暗精灵可执行:16
                return true;
            }
            if (pc.isDragonKnight() && _isDragonKnight) {
                // 龙骑士可执行:32
                return true;
            }
            if (pc.isIllusionist() && _isIllusionist) {
                // 幻术师可执行:64
                return true;
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    /**
     * 任务是否可以删除
     * 
     * @param del
     *            true:可以删除 false:不可以删除
     */
    public void set_del(boolean del) {
        this._del = del;
    }

    /**
     * 任务是否可以删除
     * 
     * @return true:可以删除 false:不可以删除
     */
    public boolean is_del() {
        return _del;
    }
}
