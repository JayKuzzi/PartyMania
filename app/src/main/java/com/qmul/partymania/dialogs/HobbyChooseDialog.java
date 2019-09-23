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

public class HobbyChooseDialog extends Dialog {
    private TextView ok;
    private WheelView hobby;
    private List<String> mData;
    private String toast;


    public HobbyChooseDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_hobby);
        setCanceledOnTouchOutside(false);
        initView();
    }


    private void initView() {
        ok = findViewById(R.id.hobby_OK);
        hobby = findViewById(R.id.hobby_picker_from);

        mData = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            mData.add("Skating");
            mData.add("Swimming");
            mData.add("Picnic");
            mData.add("Games");
            mData.add("Cooking");
        }
        hobby.setWheelAdapter(new ArrayWheelAdapter(getContext()));
        hobby.setSkin(WheelView.Skin.Holo);
        hobby.setWheelData(mData);

        hobby.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                toast = (String)hobby.getSelectionItem();
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
