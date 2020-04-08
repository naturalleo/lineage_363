package com.lineage.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OutErrorMsg {

    private static final Log _log = LogFactory.getLog(OutErrorMsg.class);

    public static void put(String className, String string, Throwable t) {
        _log.error(string);
        final StringBuilder putInfo = new StringBuilder();
        putInfo.append(string + "###");
        StackTraceElement locations[] = t.getStackTrace();

        for (StackTraceElement stackTraceElement : locations) {
            /*
             * System.out.println("ClassName :"+xxx.getClassName());
             * System.out.println("FileName  :"+xxx.getFileName());
             * System.out.println("LineNumber:"+xxx.getLineNumber());
             * System.out.println("MethodName:"+xxx.getMethodName());
             */
            putInfo.append("   " + stackTraceElement.toString() + "###");
        }
        overOut(className, putInfo);
    }

    public static void put(int oid, String string) {
        _log.error(string);
        final StringBuilder putInfo = new StringBuilder();
        final String nowDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .format(new Date());
        putInfo.append(string + "/" + nowDate);

        overOut(String.valueOf(oid), putInfo);
    }

    private static void overOut(String name, StringBuilder string) {
        try {
            final File file = new File("./" + name + ".txt");
            file.createNewFile();

            final FileOutputStream outStream = new FileOutputStream(file, true);

            final OutputStreamWriter printWriter = new OutputStreamWriter(
                    outStream, "utf-8");

            final String[] clientStrAry = string.toString().split("###");

            for (String txt : clientStrAry) {
                printWriter.write(txt);
                printWriter.write("\r\n");
            }
            printWriter.write("\r\n");
            printWriter.flush();
            printWriter.close();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

}
