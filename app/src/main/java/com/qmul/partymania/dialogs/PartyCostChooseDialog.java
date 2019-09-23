package com.qmul.partymania.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import com.qmul.partymania.R;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 2017/3/14.
 */

public class PartyCostChooseDialog extends Dialog {
    private TextView ok;
    private WheelView cost;
    private List<String> mData;
    private String toast;


    public PartyCostChooseDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_partycost);
        setCanceledOnTouchOutside(false);
        initView();
    }


    private void initView() {
        ok = findViewById(R.id.cost_OK);
        cost = findViewById(R.id.cost_picker_from);

        mData = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            mData.add("￡"+i+" for 1 people");
        }
        mData.add("￡10 + for 1 people");
        cost.setWheelAdapter(new ArrayWheelAdapter(getContext()));
        cost.setSkin(WheelView.Skin.Holo);
        cost.setWheelData(mData);

        cost.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                toast = (String)cost.getSelectionItem();
            }
        });


    }

    public TextView getOk() {
        return ok;
    }

    public String getToast() {
        return toast;
    }
}
