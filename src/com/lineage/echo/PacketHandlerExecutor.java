package com.lineage.echo;

/**
 * 客户端封包处理 abstract
 * 
 * @author dexc
 * 
 */
public abstract class PacketHandlerExecutor extends OpcodesClient {

    /**
     * 客户端封包处理
     * 
     * @param decrypt
     * @param object
     * @throws Exception
     */
    public abstract void handlePacket(byte decrypt[]);

}
