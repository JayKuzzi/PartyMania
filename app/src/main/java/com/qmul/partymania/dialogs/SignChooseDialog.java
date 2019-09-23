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

public class SignChooseDialog extends Dialog {
    private TextView ok;
    private WheelView sign;
    private List<String> mData;
    private String toast;


    public SignChooseDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_sign);
        setCanceledOnTouchOutside(false);
        initView();
    }


    private void initView() {
        ok = findViewById(R.id.sign_OK);
        sign = findViewById(R.id.sign_picker_from);


        mData = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            mData.add("Aries");
            mData.add("Taurus");
            mData.add("Gemini");
            mData.add("Cancer");
            mData.add("Leo");
            mData.add("Virgo");
            mData.add("Libra");
            mData.add("Scorpio");
            mData.add("Sagittarius");
            mData.add("Capricorn");
            mData.add("Aquarius");
            mData.add("Pisces");

        }
        sign.setWheelAdapter(new ArrayWheelAdapter(getContext()));
        sign.setSkin(WheelView.Skin.Holo);
        sign.setWheelData(mData);

        sign.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                toast = (String)sign.getSelectionItem();
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
