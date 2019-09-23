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

public class PartyTimeChooseDialog extends Dialog {
    private TextView ok;
    private WheelView time;
    private List<String> mData;
    private String toast;


    public PartyTimeChooseDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_partytime);
        setCanceledOnTouchOutside(false);
        initView();
    }


    private void initView() {
        ok = findViewById(R.id.partytime_OK);
        time = findViewById(R.id.partytime_picker_from);
        mData = new ArrayList<>();
        mData.add("16:00");
        mData.add("17:00");
        mData.add("18:00");
        mData.add("19:00");
        mData.add("20:00");
        mData.add("21:00");
        mData.add("22:00");
        mData.add("23:00");
        mData.add("00:00");
        mData.add("1:00");
        mData.add("2:00");
        mData.add("3:00");
        mData.add("4:00");
        mData.add("5:00");
        mData.add("6:00");
        mData.add("7:00");
        mData.add("8:00");
        mData.add("9:00");
        mData.add("10:00");
        mData.add("11:00");
        mData.add("12:00");
        mData.add("13:00");
        mData.add("14:00");
        mData.add("15:00");
        time.setWheelAdapter(new ArrayWheelAdapter(getContext()));
        time.setSkin(WheelView.Skin.Holo);
        time.setWheelData(mData);

        time.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                toast = (String) time.getSelectionItem();
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
