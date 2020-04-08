package com.lineage.server.templates;

/**
 * 管理者命令缓存
 * 
 * @author daien
 * 
 */
public class L1Command {

    private final String _name;

    private final boolean _system;

    private final int _level;

    private final String _executorClassName;

    private final String _note;

    /**
     * 管理者命令缓存
     * 
     * @param name
     * @param system
     * @param level
     * @param executorClassName
     * @param note
     */
    public L1Command(final String name, boolean system, final int level,
            final String executorClassName, final String note) {
        this._name = name;
        this._system = system;
        this._level = level;
        this._executorClassName = executorClassName;
        this._note = note;
    }

    /**
     * 命令
     * 
     * @return
     */
    public String getName() {
        return this._name;
    }

    /**
     * 系统可执行命令
     * 
     * @return
     */
    public boolean isSystem() {
        return this._system;
    }

    /**
     * 执行管理等级
     * 
     * @return
     */
    public int getLevel() {
        return this._level;
    }

    /**
     * 命令执行类
     * 
     * @return
     */
    public String getExecutorClassName() {
        return this._executorClassName;
    }

    /**
     * 备注
     * 
     * @return
     */
    public String get_note() {
        return this._note;
    }
}
