package com.lineage.echo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 错误讯息
 * 
 * @author daien
 * 
 */
public class EncryptErrorException extends Exception {

    private static final Log _log = LogFactory
            .getLog(EncryptErrorException.class);

    private static final long serialVersionUID = 1L;

    public EncryptErrorException() {
    }

    public EncryptErrorException(final String string) {
        _log.error(string);
    }

    public EncryptErrorException(final String string, final Exception e) {
        _log.error(string, e);
    }
}
