package com.qmul.partymania.dialogs;

import android.app.Dialog;
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

public class BusinessChooseDialog extends Dialog {
    private TextView ok;
    private WheelView business;
    private List<String> mData;
    private String toast;


    public BusinessChooseDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_business);
        setCanceledOnTouchOutside(false);
        initView();
    }


    private void initView() {
        ok = findViewById(R.id.business_OK);
        business = findViewById(R.id.business_picker_from);

        mData = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            mData.add("Tourism");
            mData.add("Internet");
            mData.add("Advertising");
            mData.add("Finance");
            mData.add("Catering");
            mData.add("Sales");
            mData.add("Express");
        }
        business.setWheelAdapter(new ArrayWheelAdapter(getContext()));
        business.setSkin(WheelView.Skin.Holo);
        business.setWheelData(mData);

        business.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                toast = (String)business.getSelectionItem();
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
