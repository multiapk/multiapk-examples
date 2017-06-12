package org.smartrobot.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * @author michael.mao
 * @version V1.0
 * @title SD 卡工具
 * @date 2014年10月8日 下午5:29:07
 */
public class MSdCardUtil {
    /**
     * @return boolean
     * @title 是否存在 SD 卡
     * @author michael.mao
     * @date 2014年10月8日 下午5:29:21
     * @version V1.0
     */
    public static boolean isSdCardExist() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * @return String
     * @title 得到 SD 卡的路径
     * @author michael.mao
     * @date 2014年10月8日 下午5:29:30
     * @version V1.0
     */
    public static String getSdCardPath() {
        if (isSdCardExist())
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        return null;
    }

    @SuppressWarnings("deprecation")
    public static long[] getSDCardMemory() {
        long[] sdCardInfo = new long[2];
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long bSize = sf.getBlockSize();
            long bCount = sf.getBlockCount();
            long availBlocks = sf.getAvailableBlocks();

            sdCardInfo[0] = bSize * bCount;//总大小
            sdCardInfo[1] = bSize * availBlocks;//可用大小
        }
        return sdCardInfo;
    }
}
