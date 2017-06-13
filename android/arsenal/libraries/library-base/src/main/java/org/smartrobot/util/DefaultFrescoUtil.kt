package org.smartrobot.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.executors.CallerThreadExecutor
import com.facebook.common.memory.PooledByteBuffer
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.BaseDataSubscriber
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig
import com.facebook.imagepipeline.image.CloseableBitmap
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder
import org.smartrobot.BuildConfig
import org.smartrobot.api.DefaultOkHttpClient
import org.smartrobot.base.DefaultBaseApplication
import java.io.File

//http://www.fresco-cn.org/
object DefaultFrescoUtil {
    val MAX_CACHE_SIZE = (1024 * 1024 * 70).toLong()//70MB
    val MAX_CACHE_SIZE_ON_LOW_DISK_SPACE = (1024 * 1024 * 40).toLong()//40MB
    val MAX_CACHE_SIZE_ON_VERY_LOW_DISK_SPACE = (1024 * 1024 * 10).toLong()//10MB


    private val MAIN_CACHE_DIR_NAME = "main"
    private val SMALL_CACHE_DIR_NAME = "small"
    private val BASE_CACHE_DIR_NAME = "fresco"

    val cacheDir: File
        get() = DefaultCacheManager.getChildCacheDir(if (BuildConfig.DEBUG) BASE_CACHE_DIR_NAME else DefaultMD5Util.getMessageDigest(BASE_CACHE_DIR_NAME.toByteArray()))

    val smallCacheDirName: String
        get() = if (BuildConfig.DEBUG) SMALL_CACHE_DIR_NAME else DefaultMD5Util.getMessageDigest(SMALL_CACHE_DIR_NAME.toByteArray())

    val mainCacheDirName: String
        get() = if (BuildConfig.DEBUG) MAIN_CACHE_DIR_NAME else DefaultMD5Util.getMessageDigest(MAIN_CACHE_DIR_NAME.toByteArray())

    /**
     * http://fresco-cn.org/
     *
     *
     * //图片处理库
     * //API>=9(2.3)
     * compile 'com.squareup.retrofit:retrofit:1.9.0'
     * compile "com.facebook.fresco:fresco:0.9.0"
     * compile 'com.facebook.fresco:imagepipeline-okhttp:0.9.0'
     */
    fun initFresco(context: Context) {
        val config = OkHttpImagePipelineConfigFactory
                .newBuilder(context, DefaultOkHttpClient.INSTANCE.okHttpClient)
                .setMainDiskCacheConfig(DiskCacheConfig.newBuilder(context)//大文件缓存
                        .setBaseDirectoryPath(cacheDir)
                        .setBaseDirectoryName(mainCacheDirName)
                        .setMaxCacheSize(MAX_CACHE_SIZE)
                        .setMaxCacheSizeOnLowDiskSpace(MAX_CACHE_SIZE_ON_LOW_DISK_SPACE)
                        .setMaxCacheSizeOnVeryLowDiskSpace(MAX_CACHE_SIZE_ON_VERY_LOW_DISK_SPACE).build())
                .setSmallImageDiskCacheConfig(DiskCacheConfig.newBuilder(context)//小文件缓存
                        .setBaseDirectoryPath(cacheDir)
                        .setBaseDirectoryName(smallCacheDirName)
                        .setMaxCacheSize(MAX_CACHE_SIZE)
                        .setMaxCacheSizeOnLowDiskSpace(MAX_CACHE_SIZE_ON_LOW_DISK_SPACE)
                        .setMaxCacheSizeOnVeryLowDiskSpace(MAX_CACHE_SIZE_ON_VERY_LOW_DISK_SPACE).build())
                .setProgressiveJpegConfig(SimpleProgressiveJpegConfig())//渐进式JPEG图
                .build()
        Fresco.initialize(context, config)
    }

    /*
        类型	                Scheme	             示例
        远程图片	            http://, https://	HttpURLConnection
        本地文件	            file://	            FileInputStream
        Content provider	content://	        ContentResolver
        asset目录下的资源	    asset://	        AssetManager
        res目录下的资源	    res://	            Resources.openRawResource

        res 示例:
        Uri uri = Uri.parse("res://包名(实际可以是任何字符串甚至留空)/" + R.drawable.ic_launcher);
     */
    fun showResImage(resId: Int, simpleDraweeView: SimpleDraweeView) {
        showProgressiveImage(Uri.parse("res:///" + resId), simpleDraweeView)
    }

    //显示渐进式图片
    fun showProgressiveImage(loadUri: String, simpleDraweeView: SimpleDraweeView) {
        var imageUri: Uri? = null
        if (!TextUtils.isEmpty(loadUri)) {
            try {
                imageUri = Uri.parse(loadUri)//if (uriString == null) { throw new NullPointerException("uriString"); }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        showProgressiveImage(imageUri, simpleDraweeView)
    }

    fun showProgressiveImage(uri: Uri?, simpleDraweeView: SimpleDraweeView) {
        if (uri == null) {
            Log.w("MFrescoUtil", "uri == null , return. 否则会导致空指针")
            return
        }
        val request = ImageRequestBuilder.newBuilderWithSource(uri).setProgressiveRenderingEnabled(true).build()
        simpleDraweeView.controller = Fresco.newDraweeControllerBuilder().setImageRequest(request).setOldController(simpleDraweeView.controller).build()
    }

    fun fetchDecodedImage(imageRequest: ImageRequest, baseDataSubscriber: BaseDataSubscriber<CloseableReference<CloseableImage>>) {
        Fresco.getImagePipeline().fetchDecodedImage(imageRequest, DefaultBaseApplication.INSTANCE).subscribe(baseDataSubscriber, CallerThreadExecutor.getInstance())
    }

    fun fetchImageFromBitmapCache(imageRequest: ImageRequest, baseDataSubscriber: BaseDataSubscriber<CloseableReference<CloseableImage>>) {
        Fresco.getImagePipeline().fetchImageFromBitmapCache(imageRequest, DefaultBaseApplication.INSTANCE).subscribe(baseDataSubscriber, CallerThreadExecutor.getInstance())
    }

    fun fetchEncodedImage(imageRequest: ImageRequest, baseDataSubscriber: BaseDataSubscriber<CloseableReference<PooledByteBuffer>>) {
        Fresco.getImagePipeline().fetchEncodedImage(imageRequest, DefaultBaseApplication.INSTANCE).subscribe(baseDataSubscriber, CallerThreadExecutor.getInstance())
    }

    /*fun isImageDownloaded(loadUri: Uri?): Boolean {
        if (loadUri == null) {
            return false
        }
        val cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(loadUri))
        return ImagePipelineFactory.getInstance().mainFileCache.hasKey(cacheKey) || ImagePipelineFactory.getInstance().smallImageFileCache.hasKey(cacheKey)
    }

    //return file or null
    fun getCachedImageOnDisk(loadUri: Uri?): File {
        var localFile: File? = null
        if (loadUri != null) {
            val cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(loadUri))
            if (ImagePipelineFactory.getInstance().mainFileCache.hasKey(cacheKey)) {
                val resource = ImagePipelineFactory.getInstance().mainFileCache.getResource(cacheKey)
                localFile = (resource as FileBinaryResource).file
            } else if (ImagePipelineFactory.getInstance().smallImageFileCache.hasKey(cacheKey)) {
                val resource = ImagePipelineFactory.getInstance().smallImageFileCache.getResource(cacheKey)
                localFile = (resource as FileBinaryResource).file
            }
        }
        return localFile
    }*/

    fun convertToBitmap(closeableReference: CloseableReference<CloseableImage>?): Bitmap? {
        var bitmap: Bitmap? = null
        if (closeableReference != null) {
            try {
                val closeableBitmap = closeableReference.get() as CloseableBitmap
                // do something with the image
                bitmap = closeableBitmap.underlyingBitmap
            } finally {
                closeableReference.close()
            }
        }
        return bitmap
    }

    /*public static final DataSubscriber dataSubscriberExample = new BaseDataSubscriber<CloseableReference<CloseableImage>>() {
        @Override
        public void onNewResultImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
            CloseableReference<CloseableImage> imageReference = dataSource.getResult();
            if (imageReference != null) {
                try {
                    CloseableImage closeableImage = imageReference.get();
                    // do something with the image
                    CloseableBitmap closeableBitmap = (CloseableBitmap) closeableImage;
                    Bitmap result = closeableBitmap.getUnderlyingBitmap();
                } finally {
                    imageReference.close();
                }
            }
        }

        @Override
        public void onFailureImpl(DataSource dataSource) {
            Throwable throwable = dataSource.getFailureCause();
            // handle failure
        }
    };*/
}
