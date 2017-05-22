package com.mlibrary.multiapk

import com.mlibrary.multiapk.util.TextUtil
import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.Copy

public class ApplicationExtension {
    public boolean solidMode = false;
    public String packageName;
    public String moduleConfigFilePath;

    public String releaseApkFileName;
    public String releaseApkFilePath;

    public int targetSdkVersion = 22;//23(Android 6.0) 以下 不需要权限申请
    public String buildToolsVersion = "25.0.2";
    public String supportLibraryVersion = "25.1.0";
    public String javaCompileVersion = "1.7";
    public String sdkDir;

    public String buildOutputName = "build-output";
    public String buildOutputPrefix = "multiapk";
    public String buildOutputPath;
    public String buildOutputBaseApkFilePath;
    public String keystore;
    public String keyAlias;
    public String keyPassword;
    public String storePassword;

    public String aapt;
    public String aaptMacPath;
    public String aaptWinPath;
    public String aaptUnixPath;
    public String dex;
    public String androidJar;
    public String apacheJar = null;
    public Project applicationProject = null;

    //inner
    public ConfigurableFileCollection classpath

    @Override
    public String toString() {
        return "    ApplicationExtension{" +
                "\n         ****[must not be null]****" +
                "\n         solidMode='" + solidMode + '\'' +
                "\n         packageName='" + packageName + '\'' +
                "\n         moduleConfigFilePath='" + moduleConfigFilePath + '\'' +
                "\n" +
                "\n         ****[can    be   null]****" +
                "\n         releaseApkFileName='" + releaseApkFileName + '\'' +
                "\n         releaseApkFilePath='" + releaseApkFilePath + '\'' +
                "\n         targetSdkVersion='" + targetSdkVersion + '\'' +
                "\n         buildToolsVersion='" + buildToolsVersion + '\'' +
                "\n         supportLibraryVersion='" + supportLibraryVersion + '\'' +
                "\n         javaCompileVersion='" + javaCompileVersion + '\'' +
                "\n         sdkDir='" + sdkDir + '\'' +
                "\n         buildOutputName='" + buildOutputName + '\'' +
                "\n         buildOutputPath='" + buildOutputPath + '\'' +
                "\n         buildOutputBaseApkFilePath='" + buildOutputBaseApkFilePath + '\'' +
                "\n         keystore='" + keystore + '\'' +
                "\n         keyAlias='" + keyAlias + '\'' +
                "\n         keyPassword='" + keyPassword + '\'' +
                "\n         storePassword='" + storePassword + '\'' +
                "\n" +
                "\n         ****[       sdk      ]****" +
                "\n         aapt='" + aapt + '\'' +
                "\n         dex='" + dex + '\'' +
                "\n         androidJar='" + androidJar + '\'' +
                "\n         apacheJar='" + apacheJar + '\'' +
                "\n" +
                "\n         ****[      extra     ]****" +
                "\n         applicationProject='" + (applicationProject == null ? "null" : applicationProject.path) + '\'' +
                '\n    }';
    }

    private static ApplicationExtension instance = null;

    public static ApplicationExtension getInstance() {
        return instance;
    }

    public
    static void initApplicationExtensionAfterEvaluate(ApplicationExtension applicationExtension, Project applicationProject) {
        applicationExtension.releaseApkFileName = TextUtil.isEmpty(applicationExtension.releaseApkFileName) ? "$applicationProject.name-release.apk" : applicationExtension.releaseApkFileName
        applicationExtension.releaseApkFilePath = TextUtil.isEmpty(applicationExtension.releaseApkFilePath) ? "$applicationProject.buildDir/outputs/apk/$applicationExtension.releaseApkFileName" : applicationExtension.releaseApkFilePath
        applicationExtension.buildOutputName = TextUtil.isEmpty(applicationExtension.buildOutputName) ? "build-output" : applicationExtension.buildOutputName
        applicationExtension.buildOutputPrefix = TextUtil.isEmpty(applicationExtension.buildOutputPrefix) ? "MDynamic" : applicationExtension.buildOutputPrefix
        applicationExtension.buildOutputPath = TextUtil.isEmpty(applicationExtension.buildOutputPath) ? "$applicationProject.rootDir/$applicationExtension.buildOutputName" : applicationExtension.buildOutputPath
        applicationExtension.buildOutputBaseApkFilePath = TextUtil.isEmpty(applicationExtension.buildOutputBaseApkFilePath) ? "$applicationExtension.buildOutputPath/$applicationExtension.buildOutputPrefix-base-release.apk" : applicationExtension.buildOutputBaseApkFilePath
        applicationExtension.sdkDir = TextUtil.isEmpty(applicationExtension.sdkDir) ? getSDKDirFromProject(applicationProject) : applicationExtension.sdkDir

        //config sdk start
        //String aaptDir = MApplicationExtension.class.getResource("/aapt").path
        String outJarFolder = getJarFolder()
        File aaptOutDir = new File("$outJarFolder/aapt");
        if (!aaptOutDir.exists())
            aaptOutDir.mkdirs()
        applicationProject.logger.info("$applicationProject.path:apply dynamicApplication:aaptDir:$aaptOutDir")
        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            applicationExtension.aapt = TextUtil.isEmpty(applicationExtension.aaptWinPath) ? "$aaptOutDir.path/aapt_win.exe" : applicationExtension.aaptWinPath
            applicationExtension.dex = "$applicationExtension.sdkDir/build-tools/$applicationExtension.buildToolsVersion/dx.bat"
        } else if (Os.isFamily(Os.FAMILY_MAC)) {
            applicationExtension.aapt = TextUtil.isEmpty(applicationExtension.aaptMacPath) ? "$aaptOutDir.path/aapt_mac" : applicationExtension.aaptMacPath
            applicationExtension.dex = "$applicationExtension.sdkDir/build-tools/$applicationExtension.buildToolsVersion/dx"
        } else if (Os.isFamily(Os.FAMILY_UNIX)) {
            applicationExtension.aapt = TextUtil.isEmpty(applicationExtension.aaptUnixPath) ? "$aaptOutDir.path/aapt_linux" : applicationExtension.aaptUnixPath
            applicationExtension.dex = "$applicationExtension.sdkDir/build-tools/$applicationExtension.buildToolsVersion/dx"
        }
        applicationExtension.androidJar = "$applicationExtension.sdkDir/platforms/android-$applicationExtension.targetSdkVersion/android.jar"
        if (applicationExtension.targetSdkVersion >= 23)
            applicationExtension.apacheJar = "$applicationExtension.sdkDir/platforms/android-$applicationExtension.targetSdkVersion/optional/org.apache.http.legacy.jar";

        File aaptFile = applicationProject.file(applicationExtension.aapt)
        if (!aaptFile.exists()) {
            File aaptOutZip = new File("$outJarFolder/aapt.zip");
            if (!aaptOutZip.exists())
                exportResource("/aapt.zip")
            applicationProject.logger.info("$applicationProject.path:apply dynamicApplication:aaptOutZip.exists=" + aaptOutZip.exists() + " ,aaptOutZip.path=$aaptOutZip.path")
            FileTree aaptZipFileTree = applicationProject.zipTree(aaptOutZip.path)
            applicationProject.logger.info("$applicationProject.path:apply dynamicApplication:aaptZipFileTree=" + aaptZipFileTree.toList().toListString())
            if (aaptZipFileTree != null) {
                Copy copy = applicationProject.tasks.create("copyAapt", Copy.class);
                copy.setFileMode(0755)
                copy.from(aaptZipFileTree.files)
                copy.into(aaptOutDir)
                copy.execute()
            }
        }
        File dexFile = applicationProject.file(applicationExtension.dex)
        dexFile.setExecutable(true)
        aaptFile.setExecutable(true)
        applicationProject.logger.info("$applicationProject.path:apply dynamicApplication:dexFile.exists=" + dexFile.exists() + " ,dexFile.canExecute=" + dexFile.canExecute() + " ,dexFile.path=$dexFile.path")
        applicationProject.logger.info("$applicationProject.path:apply dynamicApplication:aaptFile.exists=" + aaptFile.exists() + " ,aaptFile.canExecute=" + aaptFile.canExecute() + " ,aaptFile.path=$aaptFile.path")
        //config sdk end
        applicationExtension.applicationProject = applicationProject
        instance = applicationExtension
        instance.applicationProject = applicationProject
        println("$applicationProject.path:apply dynamicApplication:initApplicationExtensionAfterEvaluate:\n" + instance.toString())
    }

    /**
     * Export a resource embedded into a Jar file to the local file path.
     *
     * @param resourceName ie.: "/SmartLibrary.dll"
     * @return The path to the exported resource
     * @throws Exception
     */
    public static String exportResource(String resourceName) throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        String jarFolder = getJarFolder();
        try {
            stream = ApplicationExtension.class.getResourceAsStream(resourceName);
            //note that each / is a directory down in the "jar tree" been the jar the root of the tree
            if (stream == null)
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            int readBytes;
            byte[] buffer = new byte[4096];
            resStreamOut = new FileOutputStream(jarFolder + resourceName);
            while ((readBytes = stream.read(buffer)) > 0)
                resStreamOut.write(buffer, 0, readBytes);
        } catch (Exception ex) {
            throw ex;
        } finally {
            stream.close();
            resStreamOut.close();
        }
        return jarFolder + resourceName;
    }

    public static String getJarFolder() {
        return new File(ApplicationExtension.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
    }

    public static Properties getLocalProperties(Project project) {
        Properties properties = new Properties()
        properties.load(project.rootProject.file('local.properties').newDataInputStream())
        return properties
    }

    public static String getSDKDirFromProject(Project project) {
        String sdkDir = System.getenv("ANDROID_HOME")
        if (sdkDir == null)
            sdkDir = getLocalProperties(project).getProperty('sdk.dir')
        println("$project.path:apply multiApkApplication:sdkDir:$sdkDir")
        return sdkDir
    }
}