package com.mlibrary.multiapk.core.hotpatch;

import android.text.TextUtils;

import com.mlibrary.multiapk.MultiApk;
import com.mlibrary.multiapk.base.runtime.RuntimeArgs;
import com.mlibrary.multiapk.base.util.FileUtil;
import com.mlibrary.multiapk.base.util.LogUtil;
import com.mlibrary.multiapk.core.apk.ApkManager;
import com.mlibrary.util.bspatch.MBSPatchUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.zip.ZipFile;

/*
 上传参数: bundleKey: versionCode_versionName

 下发参数: {
              bundleKey: versionCode_versionName,
              patchList: [
                  {
                      patchUrl="https://www.ctrip.com/com.mctrip.modules.device.ios_1.patch",
                      packageName:"com.mctrip.modules.device.ios"
                      patchVersion:1
                      patchMd5:""
                      syntheticMd5:""
                  },
                  {
                      patchUrl="https://www.ctrip.com/com.mctrip.modules.device.android_1.patch",
                      packageName:"com.mctrip.modules.device.android"
                      patchVersion:1
                      patchMd5:""
                      syntheticMd5:""
                  },
              ]
          }
 本地目录: /hotpatch
          ........./app.version.1.1(bundleKey)/
          ......................../com.mctrip.modules.device.ios/
          ......................................./patch.version.1
          ......................................................./com.mctrip.modules.device.ios.patch //下载的差分文件
          ......................................................./com.mctrip.modules.device.ios.zip    //合成的目标文件
          ......................................................./com.mctrip.modules.device.ios.dex   //加载一次后生成的dex文件，如果存在可以直接加载这个(optimize)
          ......................................./patch.version.2
          ......................................................./com.mctrip.modules.device.ios.patch
          ......................................................./com.mctrip.modules.device.ios.zip
          ........./app.version.2.2(bundleKey)/

 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public enum Hotpatch {
    instance;

    private final String TAG = Hotpatch.class.getName();
    private final String baseDirName = "hotpatch";
    private final String suffix_patch = ".patch";
    private final String prefix_patch_version_dir = "patch.version.";
    private File baseDir = null;
    private boolean isInitSuccess = false;

    Hotpatch() {
        baseDir = new File(MultiApk.getBaseDir(RuntimeArgs.androidApplication), baseDirName);
        if (!baseDir.exists())
            isInitSuccess = baseDir.mkdirs();
        //todo check 自检是否存在未合并的差分包，启动合并，下次启动生效
        LogUtil.w(TAG, "isInitSuccess:" + isInitSuccess);
    }

    public String getPatchVersionDirName(int patchVersion) {
        return prefix_patch_version_dir + patchVersion;
    }

    //hotpatch/app.version.1.1/*
    public SortedMap<String, File> getPackageMap(String bundleKey) {
        SortedMap<String, File> packageMap = new TreeMap<>();
        File tmpDir = new File(baseDir, bundleKey);
        File[] packageFiles = tmpDir.listFiles();
        if (tmpDir.exists() && tmpDir.isDirectory() && packageFiles != null && packageFiles.length > 0)
            for (File packageFile : packageFiles)
                packageMap.put(packageFile.getName(), packageFile);
        return packageMap;
    }

    //hotpatch/app.version.1.1/com.mctrip.modules.device.ios/*
    public SortedMap<Integer, File> getPatchVersionMap(String bundleKey, String packageName) {
        SortedMap<Integer, File> patchVersionMap = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;//从小到大排序
            }
        });
        SortedMap<String, File> packageMap = getPackageMap(bundleKey);
        File packageFile = packageMap.get(packageName);

        if (packageFile != null) {
            File[] patchVersionFiles = packageFile.listFiles();
            if (patchVersionFiles != null && patchVersionFiles.length > 0) {
                for (File patchVersionFile : patchVersionFiles) {
                    try {
                        //hotpatch/app.version.1.1/com.mctrip.modules.device.ios/patch.version.1/*
                        String patchVersionFileName = patchVersionFile.getName();
                        String patchVersion = patchVersionFileName.substring(patchVersionFileName.lastIndexOf(".") + 1);
                        int _patchVersion = Integer.parseInt(patchVersion);
                        patchVersionMap.put(_patchVersion, patchVersionFile);
                    } catch (Exception e) {
                        LogUtil.e(TAG, "patch version parse error!", e);
                    }

                }
            }
        }
        return patchVersionMap;
    }

    /**
     * @param packageName 模块包名
     * @return null or 优先返回.dex, 其次返回.zip(最终会被解压生成.dex)
     */
    public File getLatestSyntheticBundle(String packageName) {
        return getLatestSyntheticBundle(ApkManager.getCurrentBundleKey(RuntimeArgs.androidApplication), packageName);
    }

    /**
     * @param bundleKey   bundleKey
     * @param packageName 模块包名
     * @return null or 优先返回.dex, 其次返回.zip(最终会被解压生成.dex)
     */
    public File getLatestSyntheticBundle(String bundleKey, String packageName) {
        File latestSyntheticBundleFile = null;
        SortedMap<Integer, File> patchVersionMap = getPatchVersionMap(bundleKey, packageName);
        try {
            File latestVersionFile = patchVersionMap.get(patchVersionMap.lastKey());
            //hotpatch/app.version.1.1/com.mctrip.modules.device.ios/patch.version.9/com.mctrip.modules.device.ios.zip
            File tmpSyntheticBundleZip = new File(latestVersionFile, packageName + ApkManager.suffix_bundle_in_assets);
            if (!tmpSyntheticBundleZip.exists())
                throw new FileNotFoundException();
            //File tmpSyntheticBundleDex = new File(latestVersionFile, packageName + BundleManager.suffix_dex);
            //latestSyntheticBundleFile = tmpSyntheticBundleDex.exists() ? tmpSyntheticBundleDex : tmpSyntheticBundleZip;//优先给dex,即使给zip最后也会耗费时间解析成dex
            latestSyntheticBundleFile = tmpSyntheticBundleZip;
        } catch (Exception ignore) {
            LogUtil.w(TAG, "find no synthetic bundle file!");
        }
        return latestSyntheticBundleFile;
    }

    //return 0 or patchVersion must >=1
    public int getLatestPatchVersion(String bundleKey, String packageName) {
        int latestPatchVersion = 0;
        SortedMap<Integer, File> patchVersionMap = getPatchVersionMap(bundleKey, packageName);
        try {
            latestPatchVersion = patchVersionMap.lastKey();
        } catch (Exception e) {
            LogUtil.e(TAG, "find no synthetic bundle file!", e);
        }
        return latestPatchVersion;
    }

    //hotpatch/app.version.1.1/com.mctrip.modules.device.ios/patch.version.9/com.mctrip.modules.device.ios.patch
    public void installPatch(String packageName, int patchVersion, File sourceFile) throws IOException {
        if (TextUtils.isEmpty(packageName) || patchVersion <= 0 || sourceFile == null) {
            LogUtil.e(TAG, "arguments is un correct!");
            return;
        }
        String bundleKey = ApkManager.getCurrentBundleKey(RuntimeArgs.androidApplication);
        File patchDir = new File(baseDir, bundleKey + File.separator + packageName + File.separator + getPatchVersionDirName(patchVersion));
        if (!patchDir.exists())
            patchDir.mkdirs();
        File downloadPatchFile = new File(patchDir, packageName + suffix_patch);
        File syntheticBundleFile = new File(patchDir, packageName + ApkManager.suffix_bundle_in_assets);
        if (syntheticBundleFile.exists()) {
            LogUtil.w(TAG, "syntheticBundleFile had exists!");
            return;
        }

        if (downloadPatchFile.exists())
            downloadPatchFile.delete();

        FileUtil.fileChannelCopy(sourceFile, downloadPatchFile);

        File baseBundleFile = ApkManager.instance.getBaseBundleFile(packageName);//从 apk 已经拷贝到本地的 so
        if (!baseBundleFile.exists()) {//使用 apk 里面的 so
            LogUtil.d(TAG, "can not find baseBundleFile in " + baseBundleFile.getPath() + ",\nbegin search in assets");
            ZipFile zipFile = null;
            try {
                zipFile = new ZipFile(RuntimeArgs.androidApplication.getApplicationInfo().sourceDir);
                for (String bundleBasePath : ApkManager.getPathListByFilter(zipFile, ApkManager.bundleLibPath, ApkManager.suffix_bundle_in_assets)) {
                    String bundlePackageName = bundleBasePath.substring(bundleBasePath.indexOf(ApkManager.bundleLibPath) + ApkManager.bundleLibPath.length(), bundleBasePath.indexOf(ApkManager.suffix_bundle_in_assets)).replace("_", ".");
                    LogUtil.d(TAG, "bundleBasePath:" + bundleBasePath + ", packageName:" + packageName);
                    if (bundlePackageName.equals(packageName)) {
                        FileUtil.copyInputStreamToFile(zipFile.getInputStream(zipFile.getEntry(bundleBasePath)), baseBundleFile);
                        break;
                    }
                }
            } catch (Exception e) {
                LogUtil.e(TAG, "zip exception", e);
            } finally {
                try {
                    if (zipFile != null)
                        zipFile.close();
                } catch (Exception e) {
                    LogUtil.e(TAG, "zip close failure", e);
                }
            }
        }

        if (!baseBundleFile.exists())
            throw new IOException("baseBundleFile not exists!");
        else
            LogUtil.d(TAG, "success find baseBundleFile: " + baseBundleFile.getPath());


        LogUtil.w(TAG, "合成差分包 start ****************************************");
        LogUtil.d(TAG, "合成前 baseBundleFile.exists?" + baseBundleFile.exists() + ": " + baseBundleFile.getPath());
        LogUtil.d(TAG, "合成前 patchFile.exists?" + downloadPatchFile.exists() + ": " + downloadPatchFile.getPath());
        LogUtil.d(TAG, "合成前 syntheticBundleFile.exists?" + syntheticBundleFile.exists() + ": " + syntheticBundleFile.getPath());
        try {
            MBSPatchUtil.bspatch(baseBundleFile.getPath(), syntheticBundleFile.getPath(), downloadPatchFile.getPath());
        } catch (Exception e) {
            LogUtil.e(TAG, "合成差分包失败", e);
        }
        LogUtil.d(TAG, "合成后 baseBundleFile.exists?" + baseBundleFile.exists() + ": " + baseBundleFile.getPath());
        LogUtil.d(TAG, "合成后 downloadPatchFile.exists?" + downloadPatchFile.exists() + ": " + downloadPatchFile.getPath());
        LogUtil.d(TAG, "合成后 syntheticBundleFile.exists?" + syntheticBundleFile.exists() + ": " + syntheticBundleFile.getPath());
        LogUtil.w(TAG, "合成差分包 end   ****************************************");
    }
}