/*
package com.mlibrary.util.picture

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.mlibrary.util.MCheckerUtil
import com.mlibrary.util.MFileUtil
import com.mlibrary.util.DefaultLogUtil
import com.mlibrary.util.manager.DefaultCacheManager

import org.smartrobot.R
import org.smartrobot.base.DefaultBaseApplication
import org.smartrobot.base.DefaultBaseFragment
import org.smartrobot.base.DefaultTransparentActivity

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

*/
/*
 拍照 or 选择 图片
<activity
            android:name="com.mlibrary.base.DefaultTransparentActivity"
            android:configChanges="locale|fontScale|orientation|keyboardHidden|screenSize"
            android:launchMode="standard"
            android:screenOrientation="portrait"
            android:theme="@style/Default.Theme.App.Translucent.None" />
 *//*

class DefaultPictureFragment : DefaultBaseFragment() {

    //File for capturing camera images
    private var mFileTemp: File? = null
    private var mFileTempCrop: File? = null

    private var mCropWidth = 100
    private var mCropHeight = 100
    private var isCircleCrop = false

    enum class Mode {
        ALL,
        ALL_CROP,
        CAMERA,
        CAMERA_CROP,
        GALLERY,
        GALLERY_CROP
    }

    private var mCurrentMode = Mode.ALL

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //[START]create temp file
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            mFileTemp = File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME)
            mFileTempCrop = File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_CROP_FILE_NAME)
        } else {
            mFileTemp = File(DefaultBaseApplication.INSTANCE.filesDir, TEMP_PHOTO_FILE_NAME)
            mFileTempCrop = File(DefaultBaseApplication.INSTANCE.filesDir, TEMP_PHOTO_CROP_FILE_NAME)
        }
        if (mFileTemp!!.exists()) {
            DefaultLogUtil.e(TAG, "mFileTemp:" + mFileTemp!!.length())
            DefaultLogUtil.e(TAG, "mFileTempCrop:" + mFileTempCrop!!.length())
            mFileTemp!!.delete()
            mFileTempCrop!!.delete()
            DefaultLogUtil.e(TAG, "mFileTemp:" + mFileTemp!!.length())
            DefaultLogUtil.e(TAG, "mFileTempCrop:" + mFileTempCrop!!.length())
            try {
                mFileTemp!!.createNewFile()
                mFileTempCrop!!.createNewFile()
                DefaultLogUtil.e(TAG, "mFileTemp:" + mFileTemp!!.length())
                DefaultLogUtil.e(TAG, "mFileTempCrop:" + mFileTempCrop!!.length())
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        //[END]create temp file
        val bundle = arguments
        if (bundle != null) {
            mCurrentMode = Mode.values()[bundle.getInt(KEY_MODE, 0)]
            var tmp = bundle.getInt(KEY_CROP_WIDTH)
            mCropWidth = if (tmp > 20) tmp else mCropWidth
            tmp = bundle.getInt(KEY_CROP_HEIGHT)
            mCropHeight = if (tmp > 20) tmp else mCropHeight
            isCircleCrop = bundle.getBoolean("isCircleCrop", false)
        }
        return View(activity)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //jump
        //doInternalJump();
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 777)
        } else {
            doInternalJump()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 777) {
            var i = 0
            val len = permissions.size
            while (i < len) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {

                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) || shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        android.support.v7.app.AlertDialog.Builder(activity)
                                .setTitle("权限说明")
                                .setMessage("上传照片功能需要使用到拍照以及从SD卡选择照片两个权限才能使用,否则无法使用该功能,你确定想要拒绝授权使用该功能吗?")
                                .setPositiveButton("重试授权") { dialog, which -> requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 777) }
                                .setNegativeButton("确定拒绝") { dialog, which -> processFinish(null) }
                                .show()
                        return
                    } else {
                        processFinish(null)
                        return
                        //永不再问,不再解释提示,这里采取让用户去自己重新设置授权
                        */
/*new AlertDialog.Builder(activity)
                                .setTitle("权限说明")
                                .setMessage("你以前设置了永不授权,所以无法使用该功能")
                                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                                        intent.setData(uri);
                                        startActivityForResult(intent, 666);
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();*//*

                    }
                }
                i++
            }
            doInternalJump()
        }
    }

    private val modeDialog: Dialog
        get() {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("选择模式")
                    .setItems(arrayOf("拍照", "相册")) { dialog, which ->
                        if (which == 0) {
                            if (mCurrentMode == Mode.ALL) {
                                mCurrentMode = Mode.CAMERA
                            } else if (mCurrentMode == Mode.ALL_CROP) {
                                mCurrentMode = Mode.CAMERA_CROP
                            }
                            doInternalJump()
                        } else if (which == 1) {
                            if (mCurrentMode == Mode.ALL) {
                                mCurrentMode = Mode.GALLERY
                            } else if (mCurrentMode == Mode.ALL_CROP) {
                                mCurrentMode = Mode.GALLERY_CROP
                            }
                            doInternalJump()
                        }
                    }
            builder.setCancelable(true)
            builder.setOnCancelListener { processFinish(null) }
            return builder.create()
        }

    private fun doInternalJump() {
        when (mCurrentMode) {
            DefaultPictureFragment.Mode.ALL, DefaultPictureFragment.Mode.ALL_CROP -> modeDialog.show()
            DefaultPictureFragment.Mode.CAMERA, DefaultPictureFragment.Mode.CAMERA_CROP -> takePicture()
            DefaultPictureFragment.Mode.GALLERY, DefaultPictureFragment.Mode.GALLERY_CROP -> selectPicture()
        }
    }

    private fun takePicture() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            val mImageCaptureUri: Uri
            val state = Environment.getExternalStorageState()
            if (Environment.MEDIA_MOUNTED == state) {
                mImageCaptureUri = Uri.fromFile(mFileTemp)
            } else {
                */
/*
                 The solution is taken from here: http://stackoverflow.com/questions/10042695/how-to-get-camera-result-as-a-uri-in-data-folder
	        	 *//*

                mImageCaptureUri = DefaultInternalStorageContentProvider.CONTENT_URI
            }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri)
            takePictureIntent.putExtra("return-data", true)
            startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PICTURE)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Can't take picture", e)
            Toast.makeText(activity, "不能拍照", Toast.LENGTH_LONG).show()
        }

    }

    fun selectPicture() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).setType("image*/
/*")
        try {
            startActivityForResult(intent, REQUEST_CODE_PICK_GALLERY)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "没有可供选择的图片", Toast.LENGTH_SHORT).show()
        }

    }

    fun cropPictureWithUCrop() {
        var uCrop = UCrop.of(Uri.fromFile(mFileTemp), Uri.fromFile(mFileTempCrop))
                .withAspectRatio(mCropWidth, mCropHeight)
                .withMaxResultSize(mCropWidth, mCropHeight)
        try {
            val _options = DefaultCacheManager.getInstance().get<Any>(TAG, "options") as UCrop.Options
            if (_options != null)
                options = _options
        } catch (ignore: Exception) {
        }

        uCrop = if (options == null) uCrop else uCrop.withOptions(options)
        uCrop.start(activity, this, REQUEST_CODE_CROPPED_PICTURE)
    }

    fun cropPicture() {
        val intent = Intent("com.android.camera.action.CROP")
        intent.type = "image*/
/*"
        intent.setDataAndType(Uri.fromFile(mFileTemp), "image*/
/*")
        intent.putExtra("return-data", false)// 是否将数据保留在Bitmap中返回
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFileTempCrop))// 将URI指向相应的file:///...
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("crop", "true")//发出裁剪信号
        intent.putExtra("scale", true)//是否保留比例 黑边
        intent.putExtra("scaleUpIfNeeded", true)//黑边
        //outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", mCropWidth)//裁剪区的宽
        intent.putExtra("outputY", mCropHeight)//裁剪区的高

        if (mCropWidth > mCropHeight) {
            intent.putExtra("aspectX", mCropWidth / mCropHeight)//X方向上的比例
            intent.putExtra("aspectY", 1)//Y方向上的比例
        } else if (mCropWidth < mCropHeight) {
            intent.putExtra("aspectX", 1)//X方向上的比例
            intent.putExtra("aspectY", mCropHeight / mCropWidth)//Y方向上的比例
        } else {
            if (isCircleCrop) {
                intent.putExtra("aspectX", 1)//X方向上的比例
                intent.putExtra("aspectY", 1)//Y方向上的比例
            } else {
                intent.putExtra("aspectX", Integer.MAX_VALUE)//X方向上的比例
                intent.putExtra("aspectY", Integer.MAX_VALUE - 1)//Y方向上的比例
            }
        }
        if (isCircleCrop) {
            intent.putExtra("circleCrop", "true")//圆形裁剪区域:如果这个属性不为null,则为圆形,否则方形
            intent.putExtra("noFaceDetection", false)
        } else {
            intent.putExtra("circleCrop", "false")
            intent.putExtra("noFaceDetection", true)
        }
        try {
            startActivityForResult(intent, REQUEST_CODE_CROPPED_PICTURE)
        } catch (e: ActivityNotFoundException) {
            //// TODO: 16/6/20  android 4.1
            //http://bugly.qq.com/detail?app=900017903&pid=1&ii=124#stack
            isCropped = true
            processSuccess(mFileTemp!!.path)
            Log.e(TAG, "未找到图片裁剪控件,请升级到 Android4.4")
            //MToastUtil.show("未找到图片裁剪控件,请升级到 Android4.4");
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, result: Intent?) {
        super.onActivityResult(requestCode, resultCode, result)
        val mImagePath: String
        if (requestCode == REQUEST_CODE_TAKE_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                mImagePath = mFileTemp!!.path
                processSuccess(mImagePath)
            } else if (resultCode == Activity.RESULT_CANCELED) {
                processFinish(null)
            } else {
                Log.e(TAG, "Error while opening the image file. Please try again.")
                processFinish(null)
            }

        } else if (requestCode == REQUEST_CODE_PICK_GALLERY) {
            if (resultCode == Activity.RESULT_CANCELED) {
                processFinish(null)
            } else if (resultCode == Activity.RESULT_OK) {
                try {
                    val inputStream = activity.getContentResolver().openInputStream(result!!.data) // Got the bitmap .. Copy it to the temp file for cropping
                    val fileOutputStream = FileOutputStream(mFileTemp!!)
                    copyStream(inputStream, fileOutputStream)
                    fileOutputStream.close()
                    if (inputStream != null) {
                        inputStream!!.close()
                    }
                    mImagePath = mFileTemp!!.path
                    processSuccess(mImagePath)
                } catch (e: Exception) {
                    e.printStackTrace()
                    processFinish(null)
                }

            } else {
                Log.e(TAG, "Error while opening the image file. Please try again.")
                processFinish(null)
            }
        } else if (requestCode == REQUEST_CODE_CROPPED_PICTURE || requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == Activity.RESULT_CANCELED || resultCode == UCrop.RESULT_ERROR) {
                processFinish(null)
            } else if (resultCode == Activity.RESULT_OK) {
                if (mFileTempCrop!!.length() > 0) {
                    isCropped = true
                    if (isCircleCrop) {
                        updateBitmapToFile(mFileTempCrop!!)
                    }
                    processSuccess(mFileTempCrop!!.path)
                } else {
                    processFinish(null)
                }
            }
        }
    }

    fun updateBitmapToFile(file: File): Boolean {
        val maxSize = if (mCropWidth > mCropHeight) mCropHeight else mCropWidth
        return MFileUtil.updateBitmapToFile(getBitmapByFilePath(file.path, maxSize), file)
    }


    private var isCropped = false

    private fun processSuccess(picturePath: String) {
        val intent = Intent()
        intent.putExtra(KEY_RETURN, picturePath)
        when (mCurrentMode) {
            DefaultPictureFragment.Mode.ALL, DefaultPictureFragment.Mode.ALL_CROP -> {
                activity.setResult(Activity.RESULT_CANCELED, intent)
                processFinish(null)
            }
            DefaultPictureFragment.Mode.GALLERY, DefaultPictureFragment.Mode.CAMERA -> {
                activity.setResult(Activity.RESULT_OK, intent)
                processFinish(picturePath)
            }
            DefaultPictureFragment.Mode.CAMERA_CROP, DefaultPictureFragment.Mode.GALLERY_CROP -> if (isCropped) {
                activity.setResult(Activity.RESULT_OK, intent)
                processFinish(picturePath)
            } else {
                //cropPicture();
                cropPictureWithUCrop()
            }
        }
    }

    private fun processFinish(picturePath: String?) {
        if (MCheckerUtil.isContextValid(activity)) {
            val onCacheCallBack = DefaultCacheManager.getInstance().get<Any>(TAG, TAG) as DefaultCacheManager.OnCacheCallBack<String>
            if (onCacheCallBack != null) {
                if (!TextUtils.isEmpty(picturePath))
                    onCacheCallBack.onSuccess(picturePath)
                else
                    onCacheCallBack.onFailure(null)
                DefaultCacheManager.getInstance().clean(TAG)
            }
            activity.finish()
            activity.overridePendingTransition(0, 0)
        }
    }

    companion object {
        val TAG = "MPictureUtil"

        val TEMP_PHOTO_FILE_NAME = "temp_photo.jpg"
        val TEMP_PHOTO_CROP_FILE_NAME = "temp_crop_photo.jpg"
        private val REQUEST_CODE_PICK_GALLERY = 0x1
        private val REQUEST_CODE_TAKE_PICTURE = 0x2
        private val REQUEST_CODE_CROPPED_PICTURE = 0x3

        private val KEY_CROP_WIDTH = "KEY_CROP_WIDTH"
        private val KEY_CROP_HEIGHT = "KEY_CROP_HEIGHT"
        private val KEY_MODE = "key_mode"

        fun goToMPictureFragment(activity: Activity, mode: Mode?, cropWidth: Int, cropHeight: Int, isCircleCrop: Boolean, options: UCrop.Options?, onCacheCallBack: DefaultCacheManager.OnCacheCallBack<String>) {
            var mode = mode
            if (!MCheckerUtil.isContextValid(activity) || MCheckerUtil.isFastDoubleClicked()) {
                return
            }
            DefaultCacheManager.getInstance().put(TAG, TAG, onCacheCallBack)
            if (options != null) {
                DefaultCacheManager.getInstance().put(TAG, "options", options!!)
            }
            val bundle = Bundle()
            if (mode == null)
                mode = Mode.ALL
            bundle.putInt(KEY_MODE, mode.ordinal)
            bundle.putBoolean("isCircleCrop", isCircleCrop)
            bundle.putInt(KEY_CROP_WIDTH, cropWidth)
            bundle.putInt(KEY_CROP_HEIGHT, cropHeight)
            DefaultTransparentActivity.startByCustomAnimation(activity, DefaultPictureFragment::class.java, bundle, 0, 0)
        }

        fun goToMPictureFragment(activity: Activity, mode: Mode, cropWidth: Int, cropHeight: Int, isCircleCrop: Boolean, onCacheCallBack: DefaultCacheManager.OnCacheCallBack<String>) {
            goToMPictureFragment(activity, mode, cropWidth, cropHeight, isCircleCrop, null, onCacheCallBack)
        }

        fun goToMPictureFragment(activity: Activity, mode: Mode, cropWidth: Int, cropHeight: Int, options: UCrop.Options?, onCacheCallBack: DefaultCacheManager.OnCacheCallBack<String>) {
            goToMPictureFragment(activity, mode, cropWidth, cropHeight, false, null, onCacheCallBack)
        }

        fun goToMPictureFragment(activity: Activity, requestCode: Int, mode: Mode?, cropWidth: Int, cropHeight: Int) {
            var mode = mode
            if (!MCheckerUtil.isContextValid(activity) || MCheckerUtil.isFastDoubleClicked()) {
                return
            }
            val bundle = Bundle()
            if (mode == null)
                mode = Mode.ALL
            bundle.putInt(KEY_MODE, mode.ordinal)
            bundle.putInt(KEY_CROP_WIDTH, cropWidth)
            bundle.putInt(KEY_CROP_HEIGHT, cropHeight)
            DefaultTransparentActivity.startForResultByCustomAnimation(activity, R.style.Default_Theme_App_Translucent_None, requestCode, DefaultPictureFragment::class.java, bundle, 0, 0)
        }

        fun goToMPictureFragment(fragment: Fragment, requestCode: Int, mode: Mode?, cropWidth: Int, cropHeight: Int) {
            var mode = mode
            if (!MCheckerUtil.isContextValid(fragment)) {
                return
            }
            val bundle = Bundle()
            if (mode == null)
                mode = Mode.ALL
            bundle.putInt(KEY_MODE, mode.ordinal)
            bundle.putInt(KEY_CROP_WIDTH, cropWidth)
            bundle.putInt(KEY_CROP_HEIGHT, cropHeight)
            DefaultTransparentActivity.startForResultByCustomAnimation(fragment, R.style.Default_Theme_App_Translucent_None, requestCode, DefaultPictureFragment::class.java, bundle, 0, 0)
        }

        var options: UCrop.Options? = null

        var KEY_RETURN = "key_return"

        @JvmOverloads fun getDrawableByFilePath(context: Context, filePath: String, maxSize: Int = 0): Drawable {
            return BitmapDrawable(context.resources, getBitmapByFilePath(filePath, maxSize))
        }

        fun getBitmapByFilePath(filePath: String, maxSize: Int): Bitmap {
            var `in`: InputStream
            var returnedBitmap: Bitmap? = null
            try {
                `in` = FileInputStream(filePath)
                //Decode image size
                val o = BitmapFactory.Options()
                o.inJustDecodeBounds = true
                BitmapFactory.decodeStream(`in`, null, o)
                `in`.close()
                var scale = 1
                if (o.outHeight > maxSize || o.outWidth > maxSize) {
                    scale = Math.pow(2.0, Math.round(Math.log(maxSize / Math.max(o.outHeight, o.outWidth).toDouble()) / Math.log(0.5)).toInt().toDouble()).toInt()
                }

                val o2 = BitmapFactory.Options()
                o2.inSampleSize = scale
                `in` = FileInputStream(filePath)
                returnedBitmap = BitmapFactory.decodeStream(`in`, null, o2)
                `in`.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return returnedBitmap
        }

        @Throws(IOException::class)
        private fun copyStream(input: InputStream, output: OutputStream) {
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead)
            }
        }

        fun rotateImage(source: Bitmap, angle: Float): Bitmap {
            val matrix = Matrix()
            matrix.postRotate(angle)
            return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
        }


        fun getCroppedBitmap(bitmap: Bitmap): Bitmap {
            val output = Bitmap.createBitmap(bitmap.width,
                    bitmap.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)

            val color = 0xff424242.toInt()
            val paint = Paint()
            val rect = Rect(0, 0, bitmap.width, bitmap.height)

            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = color
            // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            canvas.drawCircle((bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(),
                    (bitmap.width / 2).toFloat(), paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, rect, rect, paint)
            //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
            //return _bmp;
            return output
        }
    }
}
*/
