# MDynamicHome
gradle plugin **MDynamicLib** examples

![home page](http://odw6aoxik.bkt.clouddn.com/mdynamic-home.png-320x480)

---

### local.properties

```
solidMode=false
sdk.dir=/Users/krmao/AndroidBundle/sdk
```

---

### gradle plugin

https://github.com/mlibrarys/MDynamicLib

---

### root project build.gradle

* MDynamicHome/build.gradle
```
    jcenter()
    dependencies {
        classpath 'com.mlibrary:dynamic:0.0.1'
    }
```
```
    ext {
        solidMode = true
        compileSdkVersion = 25
        buildToolsVersion = "25.0.2"
        supportLibraryVersion = "25.1.0"

        //noinspection OldTargetApi
        targetSdkVersion = 22

        Properties properties = new Properties()
        properties.load(project.rootProject.file('local.properties').newDataInputStream())
        solidMode = false
        solidModeConfigValue = properties.getProperty('solidMode')
        if ('true'.equalsIgnoreCase(solidModeConfigValue))
            solidMode = true
        else if ('false'.equalsIgnoreCase(solidModeConfigValue))
            solidMode = false
    }

```

---

### application(com.android.application)

* MDynamicHome/MApps/MCtripApp/build.gradle
```
    apply plugin: 'com.android.application'
    apply plugin: 'com.mlibrary.dynamic.application'

    dynamicApplication {
        solidMode = project.solidMode
        buildToolsVersion = project.buildToolsVersion
        supportLibraryVersion = project.supportLibraryVersion
        packageName = "com.mctrip"
        moduleConfigFilePath = "$rootDir/gradle/dynamic/apk_module_config.xml"
        keystore = "$rootDir:MApps:MCtripApp/debug.keystore"
        keyAlias = "androiddebugkey"
        keyPassword = "android"
        storePassword = "android"
    }
```
```
    dependencies {
        compile fileTree(dir: 'libs', include: ['*.jar'])
        compile project(":MApps:MCtripLibrarys:MCtripLibrary")

        if (!solidMode) {//普通模式才需要依赖。solid模式编译时不依赖子bu。
            compile project(":MApps:MCtripModules:MCtripHospitalModule")
            compile project(":MApps:MCtripModules:MCtripSettingModule")
            compile project(":MApps:MCtripModules:MCtripMineModule")
            compile project(':MApps:MCtripModules:MCtripDeviceModule:MCtripDeviceMainModule')
            compile project(':MApps:MCtripModules:MCtripDeviceModule:MCtripDeviceAndroidModule')
            compile project(':MApps:MCtripModules:MCtripDeviceModule:MCtripDeviceIosModule')
        }
    }
```

---

### library(com.android.library)

* childModule (like ':MApps:MCtripModules:MCtripMineModule')
```
    apply plugin: 'com.android.library'
    apply plugin: 'com.mlibrary.dynamic.library'

    dynamicLibrary {
        packageName = "com.mctrip.modules.device"
        moduleProguardRulesFilePath = "$rootDir/gradle/dynamic/sub-project-proguard-rules.pro"
    }
```
```
    dependencies {
        compile project(":MApps:MCtripLibrarys:MCtripLibrary")
    }
```
* childChildModule (like ':MApps:MCtripModules:MCtripDeviceModule:MCtripDeviceAndroidModule')
```
    apply plugin: 'com.android.library'
    apply plugin: 'com.mlibrary.dynamic.library'

    dynamicLibrary {
        packageName = "com.mctrip.modules.device.android"
        moduleProguardRulesFilePath = "$rootDir/gradle/dynamic/sub-project-proguard-rules.pro"
        parentModuleName = ":MApps:MCtripModules:MCtripDeviceModule:MCtripDeviceMainModule"
    }
```
```
    dependencies {
        compile project(':MApps:MCtripModules:MCtripDeviceModule:MCtripDeviceMainModule')
    }
```

* common library (like ':MApps:MCtripLibrarys:MCtripLibrary')
    * all projects can share classes and resources


```
    defaultConfig {
        buildConfigField("boolean", "solidMode", "$solidMode")//for custom application
    }
```
```
    dependencies {
        compile "com.android.support:multidex:1.0.1"
        compile "com.android.support:support-v4:$supportLibraryVersion"
        compile "com.android.support:appcompat-v7:$supportLibraryVersion"
        compile "com.android.support:cardview-v7:$supportLibraryVersion"
        compile 'com.mlibrary:mlibrarypatch:0.0.1'
    }
```


* application
```
    public class MApplication extends Application {
        @Override
        public void onCreate() {
            if (!BuildConfig.solidMode)
                MultiDex.install(this);
            super.onCreate();
            if (BuildConfig.solidMode)
                MLibraryPatchUtil.init(this);
        }
    }
```
```
    <application
        android:name=".base.MBaseApplication"
    </application>
```

---
### how to use
* solidMode==false : just normal multidex project, no features about dynamicApk
* solidMode==true  : have all features about dynamicApk,can load custom bundles(so/apk like 'com_mctrip_modules_mine.so') and hotfix
```
    gradle clean
    gradle dynamicRepackAll --info
    adb install -r build-outbut/mdynamic-release-final.apk
```
---

###hot fix

* hotpatch directory
```
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
```

* need add library [MBSPatchLib](https://github.com/krmao/MBSPatchLib)


1. get patchFile from "com_mctrip_modules_device_ios.so" version1 android version2
```
    //examples:
    bsdiff original/com_mctrip_modules_device_ios.so patch/com_mctrip_modules_device_ios.so patch/ios.patch
```
2. and then download patch to sdcard and install by following code
```
    try {
        Hotpatch.instance.installPatch("com.mctrip.modules.device.ios", 1, new File("/storage/emulated/0/ios.patch"));
        Toast.makeText(mFragmentActivity, "合成成功", Toast.LENGTH_SHORT).show();
    } catch (Exception e) {
        e.printStackTrace();
        Toast.makeText(mFragmentActivity, "合成失败", Toast.LENGTH_SHORT).show();
    }
```
3. after application restart, the patch should have be apply

---

### todo

* optimize code

---
---

### react-native
```
npm install --save react-native
gradle installDebug

#确保手机与电脑接入同一个 wifi
#开启服务端
react-native start

#摇晃手机 DEV Settings 设置 Debug server host
192.168.2.31:8081


#5.0以上版本USB调试
adb reverse tcp:8081 tcp:8081


#查看端口占用
lsof -i:8081

#release
react-native bundle --platform android --dev false --entry-file index.android.js --bundle-output android/app/modules/computer/src/main/assets/index.android.bundle --assets-dest android/app/modules/computer/src/main/res/
```
