package com.mlibrary.util.bspatch;

public class MBSPatchUtil {
    public native int bspatch(String oldPath, String newPath, String patchPath);

    static {
        System.loadLibrary("bspatch");
    }
}
