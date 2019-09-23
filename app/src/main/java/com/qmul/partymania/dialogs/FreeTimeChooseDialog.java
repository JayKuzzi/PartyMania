package com.qmul.partymania.dialogs;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.qmul.partymania.R;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 2017/3/14.
 */

public class FreeTimeChooseDialog extends Dialog {
    private TextView ok;
    private WheelView from,to;
    private List<String> mData;
    private String toast1;
    private String toast2;


    public FreeTimeChooseDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_free_time);
        setCanceledOnTouchOutside(false);
        initView();
    }


    private void initView() {
        ok = findViewById(R.id.freeTime_OK);
        from = findViewById(R.id.freeTime_picker_from);
        to = findViewById(R.id.freeTime_picker_to);
        mData = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
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
        }
        from.setWheelAdapter(new ArrayWheelAdapter(getContext()));
        from.setSkin(WheelView.Skin.Holo);
        from.setWheelData(mData);

        to.setWheelAdapter(new ArrayWheelAdapter(getContext()));
        to.setSkin(WheelView.Skin.Holo);
        to.setWheelData(mData);

        from.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                toast1 = (String)from.getSelectionItem();
            }
        });

        to.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                toast2 = (String)to.getSelectionItem();
            }
        });
    }

    public TextView getOk() {
        return ok;
    }

    public String getToast1() {
        return toast1;
    }

    public String getToast2() {
        return toast2;
    }
}
