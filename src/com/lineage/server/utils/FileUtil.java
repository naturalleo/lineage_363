package com.lineage.server.utils;

import java.io.File;

public class FileUtil {

    public static String getExtension(final File file) {
        final String fileName = file.getName();
        final int index = fileName.lastIndexOf('.');
        if (index != -1) {
            return fileName.substring(index + 1, fileName.length());
        }
        return "";
    }

    public static String getNameWithoutExtension(final File file) {
        final String fileName = file.getName();
        final int index = fileName.lastIndexOf('.');
        if (index != -1) {
            return fileName.substring(0, index);
        }
        return "";
    }
}
