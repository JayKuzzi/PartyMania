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

public class PartyTypeChooseDialog extends Dialog {
    private TextView ok;
    private WheelView type;
    private List<String> mData;
    private String toast;


    public PartyTypeChooseDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_partytype);
        setCanceledOnTouchOutside(false);
        initView();
    }


    private void initView() {
        ok = findViewById(R.id.partyType_OK);
        type = findViewById(R.id.partyType_picker_from);
        mData = new ArrayList<>();
        mData.add("Party Type");
        mData.add("Company Type");

        type.setWheelAdapter(new ArrayWheelAdapter(getContext()));
        type.setSkin(WheelView.Skin.Holo);
        type.setWheelData(mData);

        type.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                toast = (String) type.getSelectionItem();
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
