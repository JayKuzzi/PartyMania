package com.qmul.partymania.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fashare.stack_layout.StackLayout;
import com.fashare.stack_layout.transformer.StackPageTransformer;
import com.qmul.partymania.R;
import com.qmul.partymania.adapters.MatchingItemAdapter;
import com.qmul.partymania.beans.Matching_Item;
import com.qmul.partymania.beans.Moment_Item;
import com.qmul.partymania.dialogs.UserDetailDialog;
import com.qmul.partymania.tools.Constant;
import com.qmul.partymania.tools.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class Matching extends Fragment {
    private View view;
    private StackLayout stackLayout;
    private MatchingItemAdapter mAdapter;
    private List<Matching_Item> mData;
    private MyHandler myHandler = new MyHandler(this);
    private ProgressDialog queryMatchingDialog,addFriendDialog;
    private SharedPreferences sharedPreferences;
    private String freetimeHour;
    private int currentUserId;
    private String queryMatchingResult;
    private int friendsId;
    private TextView matching_retry;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_matching, container, false);
        initData();
        return view;
    }

    private void initData() {
        sharedPreferences = getActivity().getSharedPreferences("local_file", getActivity().MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("userId", -1);
        freetimeHour = sharedPreferences.getString("freeTime", "").substring(0,2);
        mData = new ArrayList<>();
        queryMatchingDialog = new ProgressDialog(getContext());
        queryMatchingDialog.setTitle("Matching");
        queryMatchingDialog.setMessage("Matching friends, please wait.");
        queryMatchingDialog.setCancelable(false);
        queryMatchingDialog.show();
        new Thread(new queryMatchingThread()).start();
    }

    private void init() {
        matching_retry = view.findViewById(R.id.matching_retry);
        stackLayout = view.findViewById(R.id.stack_layout);
        mAdapter = new MatchingItemAdapter(mData, getActivity());
        stackLayout.setAdapter(mAdapter);
        stackLayout.addPageTransformer(
                new StackPageTransformer(0.9f, 1f, 4)// 堆叠
        );
        stackLayout.setOnSwipeListener(new StackLayout.OnSwipeListener() {
            @Override
            public void onSwiped(View swipedView, int swipedItemPos, boolean isSwipeLeft, int itemLeft) {
                if(isSwipeLeft==false){
                    friendsId = mData.get(swipedItemPos).getUserId();
                    addFriendDialog = new ProgressDialog(getContext());
                    addFriendDialog.setTitle("Adding");
                    addFriendDialog.setMessage("Following this one, please wait.");
                    addFriendDialog.setCancelable(false);
                    addFriendDialog.show();
                    new Thread(new addFriendThread()).start();
                }
                if(itemLeft ==0){
                    stackLayout.setVisibility(View.INVISIBLE);
                }
            }
        });
        matching_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }


    private static class MyHandler extends Handler {
        private final WeakReference<Matching> mTarget;

        public MyHandler(Matching target) {
            mTarget = new WeakReference<>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            Matching target = mTarget.get();
            if (target != null) {
                switch (msg.what) {
                    case Constant.SERVER_NOT_RETURN:
                        target.init();
                        target.queryMatchingDialog.dismiss();
                        Toast.makeText(target.getContext(), "Server exception", Toast.LENGTH_SHORT).show();
                        break;
                    case Constant.QUERY_MATCHING_NULL:
                        target.init();
                        if(target.mData.isEmpty()) {
                            target.stackLayout.setVisibility(View.INVISIBLE);
                        }
                        target.queryMatchingDialog.dismiss();
                        Toast.makeText(target.getContext(), "Server return null", Toast.LENGTH_SHORT).show();
                        break;
                    case Constant.QUERY_MATCHING_SUCCESS:
                        Toast.makeText(target.getContext(), "Query successful", Toast.LENGTH_SHORT).show();
                        JSONArray myJsonArray = null;
                        try {
                            myJsonArray = new JSONArray(target.queryMatchingResult);
                            for (int i = 0; i < myJsonArray.length(); i++) {
                                JSONObject object = myJsonArray.getJSONObject(i);
                                target.mData.add(new Matching_Item(object.getInt("userId"),
                                        object.getString("nickName"),
                                        object.getString("freeTime"),
                                        object.getInt("sex"),
                                        object.getString("sign"),
                                        object.getString("business"),
                                        object.getString("hobby"),
                                        object.getInt("age"),
                                        object.getString("headphoto"),
                                        object.getString("email")
                                ));
                            }
                            target.queryMatchingDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        target.init();
                        if(target.mData.isEmpty()) {
                            target.stackLayout.setVisibility(View.INVISIBLE);
                        }else
                            target.stackLayout.setVisibility(View.VISIBLE);
                        break;

                    case Constant.ADD_RELATION_FAIL:
                        target.addFriendDialog.dismiss();
                        Toast.makeText(target.getContext(), "Server exception", Toast.LENGTH_SHORT).show();
                        break;
                    case Constant.ADD_RELATION_SUCCESS:
                        target.addFriendDialog.dismiss();
                        Toast.makeText(target.getContext(), "Add successful", Toast.LENGTH_SHORT).show();
                        break;
                    case Constant.RELATION_EXIST:
                        target.addFriendDialog.dismiss();
                        Toast.makeText(target.getContext(), "You have followed him, do not add again", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }

    private class queryMatchingThread implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String path = "http://" + Constant.MY_SERVER_IP + "/PartyMania/QueryMatchingUser" + "?freetimeHour=" + freetimeHour +
            "&currentUserId=" + currentUserId;
            Message message = new Message();
            queryMatchingResult = WebService.executeHttpGet(path);
            System.out.println(queryMatchingResult);
            if (queryMatchingResult == null || queryMatchingResult.equals("")) {
                message.what = Constant.SERVER_NOT_RETURN;
            } else if (queryMatchingResult.trim().equals("20")) {
                message.what = Constant.QUERY_MATCHING_NULL;
            } else {
                message.what = Constant.QUERY_MATCHING_SUCCESS;
            }
            myHandler.sendMessage(message);
        }
    }

    private class addFriendThread implements Runnable {
        @Override
        public void run() {
            String path = "http://" + Constant.MY_SERVER_IP + "/PartyMania/AddRelation" + "?myId=" + currentUserId +"&friendsId="+ friendsId;
            Message message = new Message();
            queryMatchingResult = WebService.executeHttpGet(path);
            if (queryMatchingResult == null || queryMatchingResult.equals("")) {
                message.what = Constant.ADD_RELATION_FAIL;
            } else if (queryMatchingResult.trim().equals("22")) {
                message.what = Constant.ADD_RELATION_FAIL;
            }else if (queryMatchingResult.trim().equals("35")) {
                message.what = Constant.RELATION_EXIST;
            } else {
                message.what = Constant.ADD_RELATION_SUCCESS;
            }
            myHandler.sendMessage(message);
        }
    }
}
