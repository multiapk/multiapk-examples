package com.mlibrary.patch.util;

import android.text.TextUtils;

public class StringUtil {
    public static String subStringAfter(String source, String prefix) {
        if (TextUtils.isEmpty(source) || prefix == null)
            return source;
        int indexOf = source.indexOf(prefix);
        return indexOf != -1 ? source.substring(indexOf + prefix.length()) : "";
    }
}
