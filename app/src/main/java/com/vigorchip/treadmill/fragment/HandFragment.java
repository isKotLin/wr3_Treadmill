package com.vigorchip.treadmill.fragment;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.vigorchip.treadmill.R;
import com.vigorchip.treadmill.adapter.HistoryAdapter;
import com.vigorchip.treadmill.base.BaseFragment;
import com.vigorchip.treadmill.dialog.Silently;
import com.vigorchip.treadmill.impl.SilentlyToHand;

/**
 * 运动记录
 */
public class HandFragment extends BaseFragment implements SilentlyToHand, AdapterView.OnItemLongClickListener//,AbsListView.OnScrollListener
{
    View view;
    ListView history_lv_list;
    TextView sport_tv_time;
    HistoryAdapter adapter;
    private int mPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hand, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        history_lv_list = view.findViewById(R.id.history_lv_list);
        sport_tv_time = view.findViewById(R.id.sport_tv_time);
        adapter = new HistoryAdapter(getContext(), Silently.getHistoryLists());
        history_lv_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        sport_tv_time.setText(getString(R.string.sport_number) + ": " + Silently.getHistoryLists().size() + " " + getString(R.string.ci));
        Silently.setSilentlyToHand(this);
//        history_lv_list.setOnScrollListener(this);
        history_lv_list.setOnItemLongClickListener(this);
    }

    @Override
    public void setAdap(int index) {
        if (history_lv_list != null) {
            Silently.getHistoryLists().remove(index);//选择行的位置
            adapter.notifyDataSetChanged();
            history_lv_list.invalidate();
            sport_tv_time.setText(getString(R.string.sport_number) + ": " + Silently.getHistoryLists().size() + " " + getString(R.string.ci));
//            adapter = new HistoryAdapter(getContext(), Silently.getHistoryLists());
//            sport_tv_time.setText(getString(R.string.sport_number) + ": " + Silently.getHistoryLists().size() + " " + getString(R.string.ci));
//            history_lv_list.setAdapter(adapter);
//            history_lv_list.setSelection(mPosition);

        }
    }

    //    @Override  public void onScrollStateChanged(AbsListView absListView, int i) {  // 不滚动时保存当前滚动到的位置  if (i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {  mPosition = history_lv_list.getFirstVisiblePosition();   }  }  @Override  public void onScroll(AbsListView absListView, int i, int i1, int i2) { }
//smoothScrollToPosition(int position)
//    Smoothly scroll to the specified adapter position. // 滑动到适配器指定位置
//
//        setSelection(int position)
//    Sets the currently selected item. // 设置当前选中位置
    AlertDialog diaogs;

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int index, long l) {
        diaogs = new AlertDialog.Builder(getContext()).create();
        diaogs.show();
        diaogs.getWindow().setContentView(R.layout.dialog_edit_delete);
        diaogs.findViewById(R.id.dialog_btn_edit).setVisibility(View.GONE);
        diaogs.findViewById(R.id.dialog_btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            Silently.setIsDelete(true);
//            Silently.setCustom("",new int[16],new int[16],"");
                Silently.deleteHistoryList(index);
                diaogs.dismiss();
            }
        });
        return false;
    }
}