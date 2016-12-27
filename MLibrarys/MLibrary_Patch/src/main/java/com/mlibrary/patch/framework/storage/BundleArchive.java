package com.mlibrary.patch.framework.storage;


import com.mlibrary.patch.util.FileUtil;
import com.mlibrary.patch.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by yb.wang on 14/12/31.
 * Bundle 目录结构：version_1,version_2
 */
public class BundleArchive implements Archive {
    private static final String REVISION_DIRECTORY = "version";
    private static final Long BEGIN_VERSION = 1L;
    private final BundleArchiveRevision currentRevision;
    private final SortedMap<Long, BundleArchiveRevision> revisionSortedMap;
    private File bundleDir;

    public BundleArchive(File bundleDir) throws IOException {
        this.bundleDir = bundleDir;
        this.revisionSortedMap = new TreeMap<>();
        String[] lists = bundleDir.list();
        if (lists != null) {
            for (String str : lists) {
                if (str.startsWith(REVISION_DIRECTORY)) {
                    long parseLong = Long.parseLong(StringUtil.subStringAfter(str, "_"));
                    if (parseLong > 0)
                        this.revisionSortedMap.put(parseLong, null);
                }
            }
        }
        if (revisionSortedMap.isEmpty())
            throw new IOException("No Valid revisions in bundle archive directory");
        long longValue = this.revisionSortedMap.lastKey();
        BundleArchiveRevision bundleArchiveRevision = new BundleArchiveRevision(longValue, new File(bundleDir, REVISION_DIRECTORY + "_" + String.valueOf(longValue)));
        this.revisionSortedMap.put(longValue, bundleArchiveRevision);
        this.currentRevision = bundleArchiveRevision;
    }

    public BundleArchive(File file, InputStream inputStream) throws IOException {
        this.revisionSortedMap = new TreeMap<>();
        this.bundleDir = file;
        BundleArchiveRevision bundleArchiveRevision = new BundleArchiveRevision(BEGIN_VERSION, new File(file, REVISION_DIRECTORY + "_" + String.valueOf(BEGIN_VERSION)), inputStream);
        this.revisionSortedMap.put(BEGIN_VERSION, bundleArchiveRevision);
        this.currentRevision = bundleArchiveRevision;
    }

    @Override
    public BundleArchiveRevision newRevision(File storageFile, InputStream inputStream) throws IOException {
        long version = this.revisionSortedMap.lastKey() + 1;
        BundleArchiveRevision bundleArchiveRevision = new BundleArchiveRevision(version, new File(storageFile, REVISION_DIRECTORY + "_" + String.valueOf(version)), inputStream);
        this.revisionSortedMap.put(version, bundleArchiveRevision);
        return bundleArchiveRevision;
    }

    public BundleArchiveRevision getCurrentRevision() {
        return this.currentRevision;
    }

    public File getBundleDir() {
        return this.bundleDir;
    }

    @Override
    public void close() {
    }

    @Override
    public File getArchiveFile() {
        return this.currentRevision.getRevisionFile();
    }

    @Override
    public boolean isBundleInstalled() {
        return this.currentRevision.isBundleInstalled();
    }

    @Override
    public boolean isDexOptimized() {
        return this.currentRevision.isDexOptimized();
    }

    @Override
    public void optimizeDexFile() throws Exception {
        this.currentRevision.optimizeDexFile();
    }

    @Override
    public void purge() throws Exception {
        FileUtil.deleteDirectory(this.currentRevision.getRevisionDir());
        long lastKey = this.revisionSortedMap.lastKey();
        this.revisionSortedMap.clear();
        if (lastKey < 1)
            this.revisionSortedMap.put(0L, this.currentRevision);
        else
            this.revisionSortedMap.put(lastKey - 1, this.currentRevision);
    }

    @Override
    public InputStream openAssetInputStream(String fileName) throws IOException {
        return this.currentRevision.openAssetInputStream(fileName);
    }

    @Override
    public InputStream openNonAssetInputStream(String fileName) throws IOException {
        return this.currentRevision.openNonAssetInputStream(fileName);
    }
}
