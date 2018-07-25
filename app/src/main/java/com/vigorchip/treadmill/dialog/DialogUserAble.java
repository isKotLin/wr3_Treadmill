package com.vigorchip.treadmill.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;

import com.vigorchip.treadmill.R;

/**
 * 后门对话框
 */
public class DialogUserAble {

    Context mContext;
    Dialog dialog;

    public DialogUserAble(Context context) {
        mContext = context;
    }

    public void creatDialog() {
        if (dialog == null)
            dialog = new Dialog(mContext, R.style.Transparent);
        dialog.setCancelable(false);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.setContentView(R.layout.dialog_door);
        dialog.show();
    }

    public void dimss() {
        if (dialog != null)
            dialog.dismiss();
    }
}
