package com.mlibrary.patch.hotpatch;

import com.mlibrary.patch.loader.BundlePathLoader;
import com.mlibrary.patch.log.Logger;
import com.mlibrary.patch.log.LoggerFactory;
import com.mlibrary.patch.runtime.RuntimeArgs;
import com.mlibrary.patch.util.FileUtil;
import com.mlibrary.patch.util.MLibraryPatchUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

@SuppressWarnings("ResultOfMethodCallIgnored")
class HotPatchItem {
    private static final Logger log = LoggerFactory.getLogcatLogger(MLibraryPatchUtil.TAG + ":HotPatchItem");
    private static final String HOTPATCH_FILE_NAME = "hotfix.zip";

    private File hotFixFile;
    private File storageDir;
    private String hotPatchId;

    HotPatchItem(File storeDir, InputStream inputStream) throws IOException {
        this.storageDir = storeDir;
        if (!storageDir.exists())
            storageDir.mkdirs();
        this.hotPatchId = storeDir.getName();
        this.hotFixFile = new File(storeDir, HOTPATCH_FILE_NAME);
        FileUtil.copyInputStreamToFile(inputStream, this.hotFixFile);
    }

    HotPatchItem(File storeDir) {
        this.storageDir = storeDir;
        if (!storageDir.exists())
            storageDir.mkdirs();
        this.hotPatchId = storeDir.getName();
        this.hotFixFile = new File(storeDir, HOTPATCH_FILE_NAME);
    }

    String getHotPatchId() {
        return this.hotPatchId;
    }

    boolean isPatchInstalled() {
        return hotFixFile.exists() && verifyZipFile(hotFixFile);
    }

    private boolean verifyZipFile(File file) {
        try {
            ZipFile zipFile = new ZipFile(file);
            try {
                zipFile.close();
                return true;
            } catch (IOException e) {
                log.log("Failed to close zip file: " + file.getAbsolutePath(), Logger.LogLevel.ERROR, e);
            }
        } catch (ZipException ex) {
            log.log("File " + file.getAbsolutePath() + " is not a valid zip file.", Logger.LogLevel.ERROR, ex);
        } catch (IOException ex) {
            log.log("Got an IOException trying to open zip file: " + file.getAbsolutePath(), Logger.LogLevel.ERROR, ex);
        }
        return false;
    }

    void optDexFile() throws Exception {
        List<File> files = new ArrayList<>();
        files.add(this.hotFixFile);
        BundlePathLoader.installBundleDexs(RuntimeArgs.androidApplication.getClassLoader(), storageDir, files, false);
    }

    void optHotFixDexFile() throws Exception {
        List<File> files = new ArrayList<>();
        files.add(this.hotFixFile);
        BundlePathLoader.installBundleDexs(RuntimeArgs.androidApplication.getClassLoader(), storageDir, files, true);
    }

    void purge() {
        FileUtil.deleteDirectory(storageDir);
    }
}
