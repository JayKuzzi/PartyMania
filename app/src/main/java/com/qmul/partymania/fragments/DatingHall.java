package com.qmul.partymania.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qmul.partymania.R;
import com.qmul.partymania.adapters.DatingItemAdapter;
import com.qmul.partymania.beans.DatingHall_Item;
import com.qmul.partymania.beans.Moment_Item;
import com.qmul.partymania.tools.Constant;
import com.qmul.partymania.tools.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DatingHall extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private DatingItemAdapter mAdapter;
    private List<DatingHall_Item> mData;
    private MyHandler myHandler = new MyHandler(this);
    private String queryHallResult;
    private ProgressDialog queryHallDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_dating_hall, container, false);
        initData();
        return view;
    }

    private void initData() {
        mData = new ArrayList<>();
        queryHallDialog = new ProgressDialog(getContext());
        queryHallDialog.setTitle("Dating Hall");
        queryHallDialog.setMessage("Loading posts, please wait.");
        queryHallDialog.setCancelable(false);
        queryHallDialog.show();
        new Thread(new queryHallThread()).start();
    }

    private void init() {
        recyclerView = view.findViewById(R.id.hall_page_recycler);
        mAdapter = new DatingItemAdapter(mData, getActivity(),this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }


    private static class MyHandler extends Handler {
        private final WeakReference<DatingHall> mTarget;

        public MyHandler(DatingHall target) {
            mTarget = new WeakReference<>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            DatingHall target = mTarget.get();
            if (target != null) {
                switch (msg.what) {
                    case Constant.SERVER_NOT_RETURN:
                        target.queryHallDialog.dismiss();
                        Toast.makeText(target.getContext(), "Server exception", Toast.LENGTH_SHORT).show();
                        break;
                    case Constant.QUERY_POST_NULL:
                        target.queryHallDialog.dismiss();
                        Toast.makeText(target.getContext(), "Server return null", Toast.LENGTH_SHORT).show();
                        break;
                    case Constant.QUERY_POST_SUCCESS:
                        Toast.makeText(target.getContext(), "Query successful", Toast.LENGTH_SHORT).show();
                        JSONArray myJsonArray = null;
                        try {
                            myJsonArray = new JSONArray(target.queryHallResult);
                            for (int i = 0; i < myJsonArray.length(); i++) {
                                JSONObject object = myJsonArray.getJSONObject(i);
                                target.mData.add(new DatingHall_Item(object.getInt("postId"),
                                        object.getString("postManHeadphoto"),
                                        object.getString("postManNickname"),
                                        object.getString("photo"),
                                        object.getInt("likes"),
                                        object.getString("content"),
                                        object.getString("time"),
                                        object.getInt("type")
                                ));
                            }
                            target.queryHallDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        target.init();
                        break;
                }
            }
        }
    }

    private class queryHallThread implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String path = "http://" + Constant.MY_SERVER_IP + "/PartyMania/QueryPost" + "?postManId=0" + "&type=hall";
            Message message = new Message();
            queryHallResult = WebService.executeHttpGet(path);
            System.out.println(queryHallResult);
            if (queryHallResult == null || queryHallResult.equals("")) {
                message.what = Constant.SERVER_NOT_RETURN;
            } else if (queryHallResult.trim().equals("0")) {
                message.what = Constant.QUERY_POST_NULL;
            } else {
                message.what = Constant.QUERY_POST_SUCCESS;
            }
            myHandler.sendMessage(message);
        }
    }


    private class likePlusThread2 implements Runnable {
        private int postId;
        private int newLikes;

        public likePlusThread2(int postId, int newLikes) {
            this.postId = postId;
            this.newLikes = newLikes;
        }

        @Override
        public void run() {
            String path = "http://" + Constant.MY_SERVER_IP + "/PartyMania/ChangePostLikes" + "?postId=" + postId + "&likes=" + newLikes;
            Message message = new Message();
            queryHallResult = WebService.executeHttpGet(path);
            System.out.println(queryHallResult);
            if (queryHallResult == null || queryHallResult.equals("")) {
                message.what = Constant.SERVER_NOT_RETURN;
            } else if (queryHallResult.trim().equals("16")) {
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
        new Thread(new likePlusThread2(postId,newLikes)).start();
        System.out.println(postId+" "+newLikes);
    }
}
