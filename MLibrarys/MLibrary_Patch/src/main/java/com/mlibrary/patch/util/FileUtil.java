package com.mlibrary.patch.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileUtil {
    public static void deleteDirectory(File file) {
        if (file != null) {
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (File listFile : listFiles) {
                    if (listFile != null) {
                        if (listFile.isDirectory())
                            deleteDirectory(listFile);
                        else
                            listFile.delete();
                    }
                }
                file.delete();
            }
        }
    }

    public static void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {
        FileChannel fileChannel = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileChannel = fileOutputStream.getChannel();
            byte[] buffer = new byte[1024];
            while (true) {
                int read = inputStream.read(buffer);
                if (read <= 0)
                    break;
                fileChannel.write(ByteBuffer.wrap(buffer, 0, read));
            }
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (fileChannel != null)
                    fileChannel.close();
                if (fileOutputStream != null)
                    fileOutputStream.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}