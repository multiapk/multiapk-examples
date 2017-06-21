package org.smartrobot.util

import android.text.Selection
import android.text.TextUtils
import android.widget.EditText

/**
 * @author krmao
 * *
 * @desc 默认描述
 * *
 * @date 15/5/7
 */
object DefaultEditTextUtil {

    fun setTextWithLastCursor(editText: EditText?, text: String) {
        if (editText == null || TextUtils.isEmpty(text))
            return
        editText.setText(text)
        val charSequence = editText.text
        if (charSequence != null) {
            try {
                Selection.setSelection(charSequence, text.length)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}
