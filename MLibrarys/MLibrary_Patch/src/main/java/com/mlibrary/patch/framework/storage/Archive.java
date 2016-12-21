package com.mlibrary.patch.framework.storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface Archive {
    //关闭存储
    void close();

    // 获取Bundle下的文件
    File getArchiveFile();

    BundleArchiveRevision getCurrentRevision();

    boolean isBundleInstalled();

    //是否已经被Dex优化过
    boolean isDexOptimized();

    //优化dex文件
    void optimizeDexFile() throws Exception;

    //清理文件
    void purge() throws Exception;

    /**
     * 创建新Bundle存储
     *
     * @param storageFile 存储文件
     * @param inputStream 需要新建的目标文件流
     * @throws IOException
     */
    BundleArchiveRevision newRevision(File storageFile, InputStream inputStream) throws IOException;

    //打开Asset目录文件
    InputStream openAssetInputStream(String fileName) throws IOException;

    /**
     * 打开非Asset目录文件：如 res/drawable-mdpi/icon.png
     *
     * @param fileName 文件相对路径：res/drawable-mdpi/icon.png
     * @return 文件流
     * @throws IOException
     */
    InputStream openNonAssetInputStream(String fileName) throws IOException;
}
