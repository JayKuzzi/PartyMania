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

public class ShareToMoment extends Fragment  {

    private View view;
    private EditText desc;
    private TextView nickname;
    private SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_post_moment, container, false);
        init();
        return view;
    }

    private void init() {
        desc = view.findViewById(R.id.post_to_moment_description);
        nickname = view.findViewById(R.id.post_to_moment_nickname);
        sharedPreferences = getActivity().getSharedPreferences("local_file", getActivity().MODE_PRIVATE);
        nickname.setText(sharedPreferences.getString("nickName",""));
    }

    public EditText getDesc() {
        return desc;
    }
}
