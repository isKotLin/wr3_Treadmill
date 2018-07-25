package com.vigorchip.treadmill.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;
import android.widget.TextView;

import com.vigorchip.treadmill.R;

/**
 * 错误信息
 */
public class DialogError {

    Context mContext;
    public static Dialog dialog;
    TextView dialog_tv_error, dialog_tv_solve;

    public DialogError(Context context) {
        mContext = context;
    }

    public void creatDialog(int type) {
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                for (int i = 0; i < 11; i++) {
//                    SerialComm.setBEEP(1);
//                    try {
//                        Thread.sleep(140);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
        if (dialog == null)
            dialog = new Dialog(mContext, R.style.Transparent);
        dialog.setCancelable(false);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//		Window window = dialog.getWindow();
        dialog.setContentView(R.layout.dialog_error);
        dialog.show();
        dialog_tv_error = (TextView) dialog.findViewById(R.id.dialog_tv_error);
        dialog_tv_solve = (TextView) dialog.findViewById(R.id.dialog_tv_solve);
            dialog_tv_error.setText(mContext.getResources().getStringArray(R.array.error_names)[type - 1]);
            dialog_tv_solve.setText(mContext.getResources().getStringArray(R.array.error_msgs)[type - 1]);
    }


    public static void dimss() {
        if (dialog != null)
            dialog.dismiss();
    }
}