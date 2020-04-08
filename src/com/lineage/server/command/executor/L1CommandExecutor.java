package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * コマンド实行处理インターフェース
 * 
 * コマンド处理クラスは、このインターフェースメソッド以外に<br>
 * public static L1CommandExecutor getInstance()<br>
 * を实装しなければならない。
 * 通常、自クラスをインスタンス化して返すが、必要に应じてキャッシュされたインスタンスを返したり、他のクラスをインスタンス化して返すことができる。
 */
public interface L1CommandExecutor {

    /**
     * このコマンドを实行する。
     * 
     * @param pc
     *            实行者
     * @param cmdName
     *            实行されたコマンド名
     * @param arg
     *            引数
     */
    public void execute(L1PcInstance pc, String cmdName, String arg);
}
