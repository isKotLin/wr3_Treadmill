package com.vigorchip.treadmill.base;

import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vigorchip.treadmill.R;

public class BaseFragment extends Fragment {
    Toast toast;
    View customView;
    public void showToast(String str) {
        if (toast!=null)
            toast.cancel();
        customView = View.inflate(getContext(), R.layout.toast_layout, null);
        toast = new Toast(getContext());
        ((TextView) customView.findViewById(R.id.toast)).setText(str);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(customView);
        toast.show();
    }
}
