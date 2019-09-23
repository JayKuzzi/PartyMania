package com.qmul.partymania.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.qmul.partymania.R;
import com.qmul.partymania.dialogs.PartyCostChooseDialog;
import com.qmul.partymania.dialogs.PartyTimeChooseDialog;
import com.qmul.partymania.dialogs.PartyTypeChooseDialog;

public class ShareToHall extends Fragment implements View.OnClickListener {

    private View view;
    private TextView time, cost, nickname, type;
    private EditText desc;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_post_hall, container, false);
        init();
        return view;
    }

    private void init() {
        desc = view.findViewById(R.id.post_to_hall_desc);
        nickname = view.findViewById(R.id.post_to_hall_nickname);
        time = view.findViewById(R.id.post_to_hall_time);
        cost = view.findViewById(R.id.post_to_hall_cost);
        type = view.findViewById(R.id.post_to_hall_type);
        time.setOnClickListener(this);
        cost.setOnClickListener(this);
        type.setOnClickListener(this);
        sharedPreferences = getActivity().getSharedPreferences("local_file", getActivity().MODE_PRIVATE);
        nickname.setText(sharedPreferences.getString("nickName", ""));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_to_hall_time:
                final PartyTimeChooseDialog partyTimeChooseDialog = new PartyTimeChooseDialog(getContext());
                partyTimeChooseDialog.getOk().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        partyTimeChooseDialog.dismiss();
                        time.setText(partyTimeChooseDialog.getToast());
                    }
                });
                partyTimeChooseDialog.show();
                break;
            case R.id.post_to_hall_cost:
                final PartyCostChooseDialog partyCostChooseDialog = new PartyCostChooseDialog(getContext());
                partyCostChooseDialog.getOk().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        partyCostChooseDialog.dismiss();
                        cost.setText(partyCostChooseDialog.getToast());
                    }
                });
                partyCostChooseDialog.show();
                break;

            case R.id.post_to_hall_type:
                final PartyTypeChooseDialog partyTypeChooseDialog = new PartyTypeChooseDialog(getContext());
                partyTypeChooseDialog.getOk().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        partyTypeChooseDialog.dismiss();
                        type.setText(partyTypeChooseDialog.getToast());
                    }
                });
                partyTypeChooseDialog.show();
                break;
        }
    }

    public TextView getTime() {
        return time;
    }

    public TextView getCost() {
        return cost;
    }

    public EditText getDesc() {
        return desc;
    }

    public TextView getType() {
        return type;
    }
}
