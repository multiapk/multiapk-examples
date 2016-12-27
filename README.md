# MDynamicLib
gradle plugin for ctrip [DynamicAPK](https://github.com/CtripMobile/DynamicAPK)

---

![home page](http://odw6aoxik.bkt.clouddn.com/mdynamic-home.png-320x480)

### local.properties
```
solidMode=false
sdk.dir=/Users/krmao/AndroidBundle/sdk
```
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
    dependencies {
        compile "com.android.support:multidex:1.0.1"
        compile "com.android.support:support-v4:$supportLibraryVersion"
        compile "com.android.support:appcompat-v7:$supportLibraryVersion"
        compile "com.android.support:cardview-v7:$supportLibraryVersion"
        //compile(name: 'MLibrary_Patch-0.0.1', ext: 'aar')
        //compile project(":MLibrarys:MLibrary_Patch")
        compile 'com.mlibrary:mlibrarypatch:0.0.1' //import from jcenter
    }
```
### how to use
* solidMode==false : just normal multidex project, no features about dynamicApk
* solidMode==true  : have all features about dynamicApk,can load custom bundles(so/apk like 'com_mctrip_modules_mine.so') and hotfix
```
    gradle clean
    gradle dynamicRepackAll --info
    adb install -r build-outbut/mdynamic-release-final.apk
```

###hot fix
```
    compile 'com.mlibrary:mbspatchlib:0.0.1'//bsdiff util ,add to common library,for bspatch
```
---

### todo
* hotfix
* upload to Maven or JCenter
