package com.lineage.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GameServerFullException extends Exception {

    private static final Log _log = LogFactory.getLog(GameServer.class);

    private static final long serialVersionUID = 1L;

    public GameServerFullException() {
        super();
    }

    public GameServerFullException(final String string) {
        super(string);
        _log.error(string);
    }
}
