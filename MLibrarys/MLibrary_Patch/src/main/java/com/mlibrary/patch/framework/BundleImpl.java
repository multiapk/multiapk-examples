package com.mlibrary.patch.framework;


import com.mlibrary.patch.framework.storage.Archive;
import com.mlibrary.patch.framework.storage.BundleArchive;
import com.mlibrary.patch.log.Logger;
import com.mlibrary.patch.log.LoggerFactory;
import com.mlibrary.patch.util.FileUtil;
import com.mlibrary.patch.util.MLibraryPatchUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yb.wang on 14/12/31.
 * Bundle接口实现类，管理Bundle的生命周期。
 * meta文件存储BundleId，Location等
 */
public final class BundleImpl implements Bundle {
    private static final Logger log = LoggerFactory.getLogcatLogger(MLibraryPatchUtil.TAG + ":BundleImpl");

    private final File bundleDir;
    private final String location;
    private final long bundleID;
    Archive archive;
    //是否dex优化
    private volatile boolean isOptimized;

    BundleImpl(File bundleDir) throws Exception {
        this.bundleDir = bundleDir;
        this.isOptimized = false;
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(new File(bundleDir, "meta")));
        this.bundleID = dataInputStream.readLong();
        this.location = dataInputStream.readUTF();
        dataInputStream.close();
        try {
            this.archive = new BundleArchive(bundleDir);
            Framework.bundles.put(this.location, this);
        } catch (Exception e) {
            throw new BundleException("Could not load bundle " + this.location, e);
        }
    }

    BundleImpl(File bundleDir, String location, long bundleID, InputStream inputStream) throws BundleException {
        this.bundleDir = bundleDir;
        this.isOptimized = false;
        this.bundleID = bundleID;
        this.location = location;
        if (inputStream == null) {
            throw new BundleException("Arg InputStream is null.Bundle:" + location);
        } else {
            try {
                this.archive = new BundleArchive(bundleDir, inputStream);
            } catch (Exception e) {
                FileUtil.deleteDirectory(bundleDir);
                throw new BundleException("Can not install bundle " + location, e);
            }
        }
        this.updateMetadata();
        Framework.bundles.put(location, this);
    }

    @SuppressWarnings("unused")
    public boolean isOptimized() {
        return this.isOptimized;
    }

    public Archive getArchive() {
        return this.archive;
    }

    @Override
    public long getBundleId() {
        return this.bundleID;
    }

    @Override
    public String getLocation() {
        return this.location;
    }

    @Override
    public synchronized void update(InputStream inputStream) throws BundleException {
        try {
            this.archive.newRevision(this.bundleDir, inputStream);
        } catch (Throwable e) {
            throw new BundleException("Could not update bundle " + toString(), e);
        }
    }

    synchronized void optDexFile() throws Exception {
        if (!isOptimized) {
            long startTime = System.currentTimeMillis();
            getArchive().optimizeDexFile();
            isOptimized = true;
            log.log("执行：" + getLocation() + ",时间-----" + String.valueOf(System.currentTimeMillis() - startTime), Logger.LogLevel.ERROR);
        }
    }

    @SuppressWarnings("unused")
    public synchronized void purge() throws BundleException {
        try {
            getArchive().purge();
        } catch (Throwable e) {
            throw new BundleException("Could not purge bundle " + toString(), e);
        }
    }

    void updateMetadata() {
        File file = new File(this.bundleDir, "meta");
        DataOutputStream dataOutputStream;
        try {
            if (!file.getParentFile().exists())
                //noinspection ResultOfMethodCallIgnored
                file.getParentFile().mkdirs();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            dataOutputStream = new DataOutputStream(fileOutputStream);
            dataOutputStream.writeLong(this.bundleID);
            dataOutputStream.writeUTF(this.location);
            dataOutputStream.flush();
            fileOutputStream.getFD().sync();
            try {
                dataOutputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (Throwable e) {
            log.log("Could not save meta data " + file.getAbsolutePath(), Logger.LogLevel.ERROR, e);
        }
    }

    public String toString() {
        return "Bundle [" + this.bundleID + "]: " + this.location;
    }
}
