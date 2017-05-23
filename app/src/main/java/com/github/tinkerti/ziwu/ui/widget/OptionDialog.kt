package com.github.tinkerti.ziwu.ui.widget

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.annotation.StyleRes

/**
 * Created by tiankui on 5/22/17.
 */

class OptionDialog : AlertDialog {
    protected constructor(context: Context) : super(context) {}

    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener) : super(context, cancelable, cancelListener) {}

    protected constructor(context: Context, @StyleRes themeResId: Int) : super(context, themeResId) {}
}
