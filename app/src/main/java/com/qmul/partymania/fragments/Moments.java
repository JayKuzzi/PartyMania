package com.qmul.partymania.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qmul.partymania.R;
import com.qmul.partymania.adapters.MomentItemAdapter;
import com.qmul.partymania.beans.Moment_Item;
import com.qmul.partymania.beans.PostList_Item;
import com.qmul.partymania.tools.Constant;
import com.qmul.partymania.tools.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class Moments extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private MomentItemAdapter mAdapter;
    private List<Moment_Item> mData;
    private String queryPostResult;
    private MyHandler myHandler = new MyHandler(this);
    private ProgressDialog queryMomentsDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_moment, container, false);
        initData();
        return view;
    }

    private void initData() {
        mData = new ArrayList<>();
        queryMomentsDialog = new ProgressDialog(getContext());
        queryMomentsDialog.setTitle("Moments");
        queryMomentsDialog.setMessage("Loading moments, please wait.");
        queryMomentsDialog.setCancelable(false);
        queryMomentsDialog.show();
        new Thread(new queryMomentsThread()).start();
    }

    private void init() {
        recyclerView = view.findViewById(R.id.moment_page_recycler);
        mAdapter = new MomentItemAdapter(mData, getActivity(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }


    private static class MyHandler extends Handler {
        private final WeakReference<Moments> mTarget;

        public MyHandler(Moments target) {
            mTarget = new WeakReference<>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            Moments target = mTarget.get();
            if (target != null) {
                switch (msg.what) {
                    case Constant.SERVER_NOT_RETURN:
                        target.queryMomentsDialog.dismiss();
                        Toast.makeText(target.getContext(), "Server exception", Toast.LENGTH_SHORT).show();
                        break;
                    case Constant.QUERY_POST_NULL:
                        target.queryMomentsDialog.dismiss();
                        Toast.makeText(target.getContext(), "Server return null", Toast.LENGTH_SHORT).show();
                        break;
                    case Constant.QUERY_POST_SUCCESS:
                        Toast.makeText(target.getContext(), "Query successful", Toast.LENGTH_SHORT).show();
                        JSONArray myJsonArray = null;
                        try {
                            myJsonArray = new JSONArray(target.queryPostResult);
                            for (int i = 0; i < myJsonArray.length(); i++) {
                                JSONObject object = myJsonArray.getJSONObject(i);
                                target.mData.add(new Moment_Item(object.getInt("postId"), object.getString("postManHeadphoto"),
                                        object.getString("postManNickname"),
                                        object.getString("photo"),
                                        object.getInt("likes"),
                                        object.getString("content"),
                                        object.getString("time"),
                                        object.getInt("type")
                                ));
                            }
                            target.queryMomentsDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        target.init();
                        break;
                }
            }
        }
    }

    private class queryMomentsThread implements Runnable {
        @Override
        public void run() {
            String path = "http://" + Constant.MY_SERVER_IP + "/PartyMania/QueryPost" + "?postManId=0" + "&type=moments";
            Message message = new Message();
            queryPostResult = WebService.executeHttpGet(path);
            System.out.println(queryPostResult);
            if (queryPostResult == null || queryPostResult.equals("")) {
                message.what = Constant.SERVER_NOT_RETURN;
            } else if (queryPostResult.trim().equals("12")) {
                message.what = Constant.QUERY_POST_NULL;
            } else {
                message.what = Constant.QUERY_POST_SUCCESS;
            }
            myHandler.sendMessage(message);
        }
    }

    private class likePlusThread implements Runnable {
        private int postId;
        private int newLikes;

        public likePlusThread(int postId, int newLikes) {
            this.postId = postId;
            this.newLikes = newLikes;
        }

        @Override
        public void run() {
            String path = "http://" + Constant.MY_SERVER_IP + "/PartyMania/ChangePostLikes" + "?postId=" + postId + "&likes=" + newLikes;
            Message message = new Message();
            queryPostResult = WebService.executeHttpGet(path);
            System.out.println(queryPostResult);
            if (queryPostResult == null || queryPostResult.equals("")) {
                message.what = Constant.SERVER_NOT_RETURN;
            } else if (queryPostResult.trim().equals("16")) {
                message.what = Constant.CHANGE_POST_LIKE_FAIL;
            } else {
                message.what = Constant.CHANGE_POST_LIKE_SUCCESS;
            }
            myHandler.sendMessage(message);
        }
    }


    public MyHandler getMyHandler() {
        return myHandler;
    }

    public void doLikePlus(int postId, int newLikes) {
        new Thread(new likePlusThread(postId,newLikes)).start();
    }
}
