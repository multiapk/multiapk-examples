package com.mlibrary.multiapk.base.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileUtil {
    private static final String TAG = FileUtil.class.getName();


    public static void deleteDirectory(File file) {
        try {
            if (file != null) {
                File[] childFiles = file.listFiles();
                if (file.isDirectory() && childFiles != null && childFiles.length > 0) {
                    for (File childFile : childFiles)
                        deleteDirectory(childFile);
                }
                //noinspection ResultOfMethodCallIgnored
                file.delete();
                LogUtil.d(TAG, file.getPath() + " is deleted !");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用文件通道的方式复制文件
     *
     * @param sourceFile 源文件
     * @param destFile   复制到的新文件
     */

    public static void fileChannelCopy(File sourceFile, File destFile) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(sourceFile);
        FileOutputStream fileOutputStream = new FileOutputStream(destFile);
        FileChannel fileChannelIn = fileInputStream.getChannel();// 得到对应的文件通道
        FileChannel fileChannelOut = fileOutputStream.getChannel();// 得到对应的文件通道
        fileChannelIn.transferTo(0, fileChannelIn.size(), fileChannelOut);// 连接两个通道，并且从in通道读取，然后写入out通道
        try {
            fileInputStream.close();
            fileChannelIn.close();
            fileOutputStream.close();
            fileChannelOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {
        FileChannel fileChannel = null;
        FileOutputStream fileOutputStream = null;
        try {
            LogUtil.d(TAG, "copyInputStreamToFile:" + file.getPath());
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