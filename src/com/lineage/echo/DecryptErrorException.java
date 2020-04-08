package com.lineage.echo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DecryptErrorException extends Exception {

    private static final Log _log = LogFactory
            .getLog(DecryptErrorException.class);

    private static final long serialVersionUID = 1L;

    public DecryptErrorException() {
    }

    public DecryptErrorException(final String string) {
        _log.error(string);
    }

    public DecryptErrorException(final String string, final Exception e) {
        _log.error(string, e);
    }
}
