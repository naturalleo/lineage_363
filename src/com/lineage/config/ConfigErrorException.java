package com.lineage.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * CONFIG 错误报告
 * 
 * @author daien
 * 
 */
public class ConfigErrorException extends Exception {

    private static final Log _log = LogFactory
            .getLog(ConfigErrorException.class);

    private static final long serialVersionUID = 1L;

    public ConfigErrorException() {
    }

    public ConfigErrorException(final String string) {
        _log.error(string);
    }
}
