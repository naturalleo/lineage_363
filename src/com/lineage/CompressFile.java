package com.lineage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩旧文件
 * 
 * @author dexc
 * 
 */
public class CompressFile {

    private static final int BUFFEREDSIZE = 1024;

    public synchronized void zip(final String inputFilename,
            final String zipFilename) throws IOException {
        this.zip(new File(inputFilename), zipFilename);
    }

    public synchronized void zip(final File inputFile, final String zipFilename)
            throws IOException {
        final ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                zipFilename));

        try {
            this.zip(inputFile, out, "");
        } catch (final IOException e) {
            throw e;
        } finally {
            out.close();
        }
    }

    private synchronized void zip(final File inputFile,
            final ZipOutputStream out, String base) throws IOException {
        if (inputFile.isDirectory()) {
            final File[] inputFiles = inputFile.listFiles();
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < inputFiles.length; i++) {
                this.zip(inputFiles[i], out, base + inputFiles[i].getName());
            }

        } else {
            if (base.length() > 0) {
                out.putNextEntry(new ZipEntry(base));
            } else {
                out.putNextEntry(new ZipEntry(inputFile.getName()));
            }

            final FileInputStream in = new FileInputStream(inputFile);
            try {
                int c;
                final byte[] by = new byte[BUFFEREDSIZE];
                while ((c = in.read(by)) != -1) {
                    out.write(by, 0, c);
                }
            } catch (final IOException e) {
                throw e;
            } finally {
                in.close();
            }
        }
    }
}
