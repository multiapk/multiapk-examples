package org.smartrobot.util.bspatch;

public class MBSPatchUtil {
    /**
     * @param basePath      基础文件
     * @param syntheticPath 合成的目标文件
     * @param patchPath     合成所需要的差分文件
     * @return main 返回值
     */
    public static native int bspatch(String basePath, String syntheticPath, String patchPath);

    static {
        System.loadLibrary("bspatch");
    }
}
