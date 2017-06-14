package org.smartrobot.multiapk

import org.smartrobot.multiapk.util.TextUtil
import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.bundling.Zip

import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

public class ApplicationPlugin implements Plugin<Project> {

    public static String pluginName = "multiApkApplication"
    private Project project;

    @Override
    public void apply(Project project) {
        this.project = project;
        ApplicationExtension applicationExtension = project.extensions.create(pluginName, ApplicationExtension.class)
        project.afterEvaluate {
            try {
                println("$project.path:apply multiApkApplication:>>>>>>============================================================>>>>>>")
                println("$project.path:apply multiApkApplication:>>>>>>======apply plugin: 'org.smartrobot.dynamic.application'======>>>>>>")
                boolean isConditionOK = true
                if (applicationExtension == null) {
                    println("$project.path:apply multiApkApplication:applicationExtension==null return")
                    isConditionOK = false;
                }
                if (!applicationExtension.solidMode) {
                    println("$project.path:apply multiApkApplication:solidMode==false return")
                    isConditionOK = false;
                }
                if (!project.android) { //只可以在 android application 或者 android lib 项目中使用
                    println("$project.path:apply multiApkApplication: only can be used in 'com.android.application' or 'com.android.library' ! return")
                    isConditionOK = false;
                }
                if (TextUtil.isEmpty(applicationExtension.packageName)) {
                    println("$project.path:apply multiApkApplication:packageName==null return")
                    isConditionOK = false;
                }
                if (isConditionOK) {
                    ApplicationExtension.initApplicationExtensionAfterEvaluate(applicationExtension, project)
                    configureClasspath();
                    configureCleanTask();
                    configureAssembleReleaseTask();
                    configureReloadTask();
                    configureRepackTask()
                    configureResignTask();
                    configureRealignTask();
                    configureConcatMappingsTask();
                    configureRepackAllTask();
                }
                println("$project.path:apply multiApkApplication:<<<<<<======apply plugin: 'org.smartrobot.dynamic.application'======<<<<<<")
                println("$project.path:apply multiApkApplication:<<<<<<============================================================<<<<<<")
            } catch (Exception e) {
                e.printStackTrace()
            }
        }
    }

    private void configureClasspath() {
        project.logger.info("$project.path:configureClasspath:start")
        ConfigurableFileCollection classpath = project.files(
                ApplicationExtension.instance.androidJar,
                "$project.buildDir/intermediates/classes-proguard/release/classes.jar",
                project.fileTree("$project.buildDir/intermediates/exploded-aar/").include('**/**/**/**/*.jar'),
                project.fileTree("$ApplicationExtension.instance.sdkDir/extras/android/m2repository/com/android/support/").include("**/$ApplicationExtension.instance.supportLibraryVersion/*-sources.jar"),
                project.fileTree("$ApplicationExtension.instance.sdkDir/extras/android/support/v7/").include('**/**/*.jar'),//如果上一句path找不到，则在这个里面找，注意先后顺序
                //project.fileTree("$MApplicationExtension.instance.sdkDir/extras/android/support/v7/appcompat/libs/").include('*.jar'),//only need android-support-v4,v7 jar
        )
        if (!TextUtil.isEmpty(ApplicationExtension.instance.apacheJar))
            classpath.files.add(ApplicationExtension.instance.apacheJar)
        project.logger.info("$project.path:------------------------------------------------------------------------------------")
        classpath.each {
            project.logger.info("$project.path:configureCompileReleaseTask:classpath:" + it.path)
        }
        project.logger.info("$project.path:------------------------------------------------------------------------------------")
        ApplicationExtension.instance.classpath = classpath;
        project.logger.info("$project.path:configureClasspath:end")
    }

    private void configureCleanTask() {
        Task clean = project.tasks.getByName("clean")
        if (clean == null) {
            println("$project.path:configureCleanTask:tasks not contains clean, return!")
            return
        }
        clean.doLast {
            project.delete project.buildDir
            project.delete ApplicationExtension.instance.buildOutputPath
        }
    }

    private void configureAssembleReleaseTask() {
        project.logger.info("$project.path:apply multiApkApplication:configureDynamicAssembleRelease:start")
        Copy copy = project.tasks.create("dynamicAssembleRelease", Copy.class);
        copy.inputs.file "$project.buildDir/outputs/mapping/release/mapping.txt"
        copy.inputs.file ApplicationExtension.instance.releaseApkFilePath
        //copy.inputs.file "$project.buildDir/outputs/apk/AndroidManifest.xml"
        //copy.inputs.file "$project.buildDir/intermediates/full/release/AndroidManifest.xml"

        copy.outputs.file "$ApplicationExtension.instance.buildOutputPath/$ApplicationExtension.instance.buildOutputPrefix-base-mapping.txt"
        copy.outputs.file ApplicationExtension.instance.buildOutputBaseApkFilePath
        //copy.outputs.file "$buildOutputPath/AndroidManifest.xml"

        copy.setDescription("复制 $project.buildDir 到 $ApplicationExtension.instance.buildOutputPath")
        copy.from(ApplicationExtension.instance.releaseApkFilePath) {
            rename ApplicationExtension.instance.releaseApkFileName, "$ApplicationExtension.instance.buildOutputPrefix-base-release.apk"
        }
        copy.from("$project.buildDir/outputs/mapping/release/mapping.txt") {
            rename 'mapping.txt', "$ApplicationExtension.instance.buildOutputPrefix-base-mapping.txt"
        }
        //todo make sure
        //copy.from("$project.buildDir/intermediates/manifests/full/release/AndroidManifest.xml")

        copy.into(ApplicationExtension.instance.buildOutputPath)
        copy.dependsOn "assembleRelease"
        project.logger.info("$project.path:apply multiApkApplication:configureDynamicAssembleRelease:end")
    }

    private void configureReloadTask() {
        project.logger.info("$project.path:apply multiApkApplication:configureReloadTask:start")
        Zip zip = project.tasks.create("dynamicReload", Zip.class);

        zip.inputs.file "$ApplicationExtension.instance.buildOutputPath/$ApplicationExtension.instance.buildOutputPrefix-base-release.apk"
        zip.inputs.files project.fileTree(new File(ApplicationExtension.instance.buildOutputPath)).include("*$LibraryExtension.DEFAULT_APK_SUFFIX")
        //增加so文件输入
        zip.inputs.file project.fileTree(new File(ApplicationExtension.instance.buildOutputPath, 'jni'))
        zip.outputs.file "$ApplicationExtension.instance.buildOutputPath/$ApplicationExtension.instance.buildOutputPrefix-release-dynamicReloaded.apk"

        zip.setDescription("dynamicReload task")

        zip.into('assets/baseres/') {
            from project.fileTree(new File(ApplicationExtension.instance.buildOutputPath)).include("*$LibraryExtension.DEFAULT_APK_SUFFIX")
        }
        zip.into('lib') {
            from project.fileTree(new File(ApplicationExtension.instance.buildOutputPath, 'jni'))
        }
        zip.from(project.zipTree("$ApplicationExtension.instance.buildOutputPath/$ApplicationExtension.instance.buildOutputPrefix-base-release.apk")) {
            exclude('**/META-INF/*.SF')
            exclude('**/META-INF/*.RSA')
        }

        zip.destinationDir project.file(ApplicationExtension.instance.buildOutputPath)
        zip.archiveName "$ApplicationExtension.instance.buildOutputPrefix-release-dynamicReloaded.apk"

        Set<Task> dependTasks = project.rootProject.getTasksByName("dynamicBundleRelease", true)
        zip.setDependsOn(dependTasks)
        dependTasks.each {
            println "$project.path:apply multiApkApplication:dynamicReload:findDependency: " + it.toString()
        }
        project.logger.info("$project.path:apply multiApkApplication:configureReloadTask:end")
    }

    private void configureRepackTask() {
        project.logger.info("$project.path:apply multiApkApplication:configureRepackTask:start")
        Task dynamicRepackTask = project.tasks.create("dynamicRepack");
        dynamicRepackTask.inputs.file "$ApplicationExtension.instance.buildOutputPath/$ApplicationExtension.instance.buildOutputPrefix-release-dynamicReloaded.apk"
        dynamicRepackTask.outputs.file "$ApplicationExtension.instance.buildOutputPath/$ApplicationExtension.instance.buildOutputPrefix-release-dynamicRepacked.apk"

        dynamicRepackTask.doLast {
            project.logger.info("$project.path:apply multiApkApplication:configureRepackTask:doLast: start")
            File oldApkFile = project.file("$ApplicationExtension.instance.buildOutputPath/$ApplicationExtension.instance.buildOutputPrefix-release-dynamicReloaded.apk")
            assert oldApkFile != null: "没有找到release包！"
            File newApkFile = new File(oldApkFile.parentFile, "$ApplicationExtension.instance.buildOutputPrefix-release-dynamicRepacked.apk")
            dynamicRepackApk(oldApkFile.absolutePath, newApkFile.absolutePath) //重新打包
            assert newApkFile.exists(): "没有找到重新压缩的release包！"
            project.logger.info("$project.path:apply multiApkApplication:configureRepackTask:doLast: end")
        }
        dynamicRepackTask.dependsOn "dynamicReload"
        project.logger.info("$project.path:apply multiApkApplication:configureRepackTask:end")
    }

    // 打包过程中很多手工zip过程：
    // 1，为了压缩resources.arsc文件而对标准产出包重新压缩
    // 2，以及各子apk的纯手打apk包
    // 但对于音频等文件，压缩会导致资源加载报异常
    // 重新打包方法，使用STORED过滤掉不应该压缩的文件们
    // 后缀名列表来自于android源码
    def dynamicRepackApk(String originApk, String targetApk) {
        project.logger.info("$project.path:apply multiApkApplication:dynamicRepackApk start(重新打包apk: 增加压缩,压缩resources.arsc)")
        def noCompressExt = [".jpg", ".jpeg", ".png", ".gif",
                             ".wav", ".mp2", ".mp3", ".ogg", ".aac",
                             ".mpg", ".mpeg", ".mid", ".midi", ".smf", ".jet",
                             ".rtttl", ".imy", ".xmf", ".mp4", ".m4a",
                             ".m4v", ".3gp", ".3gpp", ".3g2", ".3gpp2",
                             ".amr", ".awb", ".wma", ".wmv"]
        ZipFile zipFile = new ZipFile(originApk)
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(targetApk)))
        zipFile.entries().each { entryIn ->
            if (entryIn.directory) {
                project.logger.info("$project.path:apply multiApkApplication:${entryIn.name} is a directory")
            } else {
                def entryOut = new ZipEntry(entryIn.name)
                def dotPos = entryIn.name.lastIndexOf('.')
                def ext = (dotPos >= 0) ? entryIn.name.substring(dotPos) : ""
                def isRes = entryIn.name.startsWith('res/')
                if (isRes && ext in noCompressExt) {
                    entryOut.method = ZipEntry.STORED
                    entryOut.size = entryIn.size
                    entryOut.compressedSize = entryIn.size
                    entryOut.crc = entryIn.crc
                } else {
                    entryOut.method = ZipEntry.DEFLATED
                }
                zos.putNextEntry(entryOut)
                zos << zipFile.getInputStream(entryIn)
                zos.closeEntry()
            }
        }
        zos.finish()
        zos.close()
        zipFile.close()
        project.logger.info("$project.path:apply multiApkApplication:dynamicRepackApk end")
    }

    private void configureResignTask() {
        project.logger.info("$project.path:apply multiApkApplication:configureResignTask:start")
        Exec dynamicResignExec = project.tasks.create("dynamicResign", Exec.class)
        dynamicResignExec.inputs.file "$ApplicationExtension.instance.buildOutputPath/$ApplicationExtension.instance.buildOutputPrefix-release-dynamicRepacked.apk"
        dynamicResignExec.outputs.file "$ApplicationExtension.instance.buildOutputPath/$ApplicationExtension.instance.buildOutputPrefix-release-dynamicResigned.apk"

        dynamicResignExec.doFirst {
            project.logger.info("$project.path:apply multiApkApplication:dynamicResign:doFirst")

            workingDir ApplicationExtension.instance.buildOutputPath
            //executable "${System.env.'JAVA_HOME'}/bin/jarsigner"
            executable System.getenv('JAVA_HOME') + "/bin/jarsigner"

            def argv = []
            argv << '-verbose'
            argv << '-sigalg'
            argv << 'SHA1withRSA'
            argv << '-digestalg'
            argv << 'SHA1'
            argv << '-keystore'
            argv << ApplicationExtension.instance.keystore
            argv << '-storepass'
            argv << ApplicationExtension.instance.storePassword
            argv << '-keypass'
            argv << ApplicationExtension.instance.keyPassword
            argv << '-signedjar'
            argv << "$ApplicationExtension.instance.buildOutputPrefix-release-dynamicResigned.apk"
            argv << "$ApplicationExtension.instance.buildOutputPrefix-release-dynamicRepacked.apk"
            argv << ApplicationExtension.instance.keyAlias
            args = argv
        }
        dynamicResignExec.dependsOn "dynamicRepack"
        project.logger.info("$project.path:apply multiApkApplication:configureResignTask:end")
    }

    private static String getZipAlignPath() {
        def zipAlignPath = "${ApplicationExtension.instance.sdkDir}/build-tools/${ApplicationExtension.instance.buildToolsVersion}/zipalign"
        if (Os.isFamily(Os.FAMILY_WINDOWS))
            zipAlignPath += '.exe'
        assert (new File(zipAlignPath)).exists(): '没有找到zipalign应用程序！'
        return zipAlignPath
    }

    private void configureRealignTask() {
        project.logger.info("$project.path:apply multiApkApplication:configureRealignTask:start")
        Exec dynamicRealignExec = project.tasks.create("dynamicRealign", Exec.class)
        dynamicRealignExec.inputs.file "$ApplicationExtension.instance.buildOutputPath/$ApplicationExtension.instance.buildOutputPrefix-release-dynamicReloaded.apk"
        dynamicRealignExec.outputs.file "$ApplicationExtension.instance.buildOutputPath/$ApplicationExtension.instance.buildOutputPrefix-release-dynamicRepacked.apk"

        File oldApkFile = project.file("$ApplicationExtension.instance.buildOutputPath/$ApplicationExtension.instance.buildOutputPrefix-release-dynamicResigned.apk")
        assert oldApkFile != null: "没有找到release包！"
        File newApkFile = new File(oldApkFile.parentFile, "$ApplicationExtension.instance.buildOutputPrefix-release-final.apk")

        dynamicRealignExec.doFirst {
            commandLine getZipAlignPath()
            def argv = []
            argv << '-f'    //overwrite existing outfile.zip
            // argv << '-z'    //recompress using Zopfli
            argv << '-v'    //verbose output
            argv << '4'     //alignment in bytes, e.g. '4' provides 32-bit alignment
            argv << oldApkFile.absolutePath
            argv << newApkFile.absolutePath
            args = argv
        }
        dynamicRealignExec.doLast {
            assert newApkFile.exists(): "没有找到重新zipalign的release包！"
        }
        dynamicRealignExec.dependsOn "dynamicResign"
        project.logger.info("$project.path:apply multiApkApplication:configureRealignTask:end")
    }

    private void configureConcatMappingsTask() {
        project.logger.info("$project.path:apply multiApkApplication:configureConcatMappingsTask:start")
        Task dynamicConcatMappingsTask = project.tasks.create("dynamicConcatMappings");
        dynamicConcatMappingsTask.inputs.files project.fileTree(new File(ApplicationExtension.instance.buildOutputPath)).include('*mapping.txt')
        dynamicConcatMappingsTask.outputs.file "$ApplicationExtension.instance.buildOutputPath/$ApplicationExtension.instance.buildOutputPrefix-mapping-final.txt"

        dynamicConcatMappingsTask.doLast {
            project.logger.info("$project.path:apply multiApkApplication:configureConcatMappingsTask:doLast: start")
            FileCollection sources = project.fileTree(new File(ApplicationExtension.instance.buildOutputPath)).include('*mapping.txt')
            File target = new File("$ApplicationExtension.instance.buildOutputPath/$ApplicationExtension.instance.buildOutputPrefix-mapping-final.txt")
            File tmp = File.createTempFile('concat', null, target.getParentFile())
            tmp.withWriter { writer ->
                sources.each { file ->
                    file.withReader { reader ->
                        writer << reader
                    }
                }
            }
            target.delete()
            tmp.renameTo(target)
            project.logger.info("$project.path:apply multiApkApplication:configureConcatMappingsTask:doLast: end")
        }
        dynamicConcatMappingsTask.dependsOn "dynamicRealign"
        project.logger.info("$project.path:apply multiApkApplication:configureConcatMappingsTask:end")
    }

    private void configureRepackAllTask() {
        project.logger.info("$project.path:apply multiApkApplication:configureRepackAllTask:start")
        Task dynamicConcatMappingsTask = project.tasks.create("dynamicRepackAll");
        dynamicConcatMappingsTask.dependsOn "dynamicConcatMappings"
        project.logger.info("$project.path:apply multiApkApplication:configureRepackAllTask:end")
    }

}