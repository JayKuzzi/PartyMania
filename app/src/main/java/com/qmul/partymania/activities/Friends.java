package com.qmul.partymania.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.qmul.partymania.R;
import com.qmul.partymania.adapters.FriendsItemAdapter;
import com.qmul.partymania.beans.FriendsList_Item;
import com.qmul.partymania.beans.PostList_Item;
import com.qmul.partymania.tools.Constant;
import com.qmul.partymania.tools.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class Friends extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<FriendsList_Item> mData;
    private FriendsItemAdapter mAdapter;


    private MyHandler myHandler = new MyHandler(this);
    private ProgressDialog queryMyFriendsDialog,deleteDialog;
    private SharedPreferences sharedPreferences;
    private int currentUserId;
    private String queryFriendsResult;
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        initData();
    }

    private void initData() {
        sharedPreferences = getSharedPreferences("local_file", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("userId", -1);
        mData = new ArrayList<>();
        queryMyFriendsDialog = new ProgressDialog(this);
        queryMyFriendsDialog.setTitle("Searching");
        queryMyFriendsDialog.setMessage("Searching my friends, please wait.");
        queryMyFriendsDialog.setCancelable(false);
        queryMyFriendsDialog.show();
        new Thread(new queryMyFriendsThread()).start();
    }

    private void init() {
        back = findViewById(R.id.friends_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.friends_recyclerView);
        mAdapter = new FriendsItemAdapter(mData, this,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }




    private static class MyHandler extends Handler {
        private final WeakReference<Friends> mTarget;

        public MyHandler(Friends target) {
            mTarget = new WeakReference<>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            Friends target = mTarget.get();
            if (target != null) {
                switch (msg.what) {
                    case Constant.SERVER_NOT_RETURN:
                        target.init();
                        target.queryMyFriendsDialog.dismiss();
                        Toast.makeText(target, "Server exception", Toast.LENGTH_SHORT).show();
                        break;
                    case Constant.QUERY_FRIENDS_NULL:
                        target.init();
                        if(target.mData.isEmpty()) {
                            target.recyclerView.setVisibility(View.INVISIBLE);
                        }
                        target.queryMyFriendsDialog.dismiss();
                        Toast.makeText(target, "Server return null", Toast.LENGTH_SHORT).show();
                        break;
                    case Constant.QUERY_FRIENDS_SUCCESS:
                        Toast.makeText(target, "Query successful", Toast.LENGTH_SHORT).show();
                        JSONArray myJsonArray = null;
                        try {
                            myJsonArray = new JSONArray(target.queryFriendsResult);
                            for (int i = 0; i < myJsonArray.length(); i++) {
                                JSONObject object = myJsonArray.getJSONObject(i);
                                target.mData.add(new FriendsList_Item(object.getInt("userId"),object.getString("nickName"), object.getString("headphoto")));
                            }
                            target.queryMyFriendsDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        target.init();
                        break;
                    case Constant.DELETE_FRIEND_FAIL:
                        target.deleteDialog.dismiss();
                        Toast.makeText(target, "Delete exception", Toast.LENGTH_SHORT).show();
                        break;
                    case Constant.DELETE_FRIEND_SUCCESS:
                        target.deleteDialog.dismiss();
                        Toast.makeText(target, "Delete successful", Toast.LENGTH_SHORT).show();
                        target.initData();
                        break;
                }
            }
        }
    }

    private class queryMyFriendsThread implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String path = "http://" + Constant.MY_SERVER_IP + "/PartyMania/QueryFriends" + "?currentId=" + currentUserId;
            Message message = new Message();
            queryFriendsResult = WebService.executeHttpGet(path);
            System.out.println(queryFriendsResult);
            if (queryFriendsResult == null || queryFriendsResult.equals("")) {
                message.what = Constant.SERVER_NOT_RETURN;
            } else if (queryFriendsResult.trim().equals("29")) {
                message.what = Constant.QUERY_FRIENDS_NULL;
            } else {
                message.what = Constant.QUERY_FRIENDS_SUCCESS;
            }
            myHandler.sendMessage(message);
        }
    }

    private class deleteFriendThread implements Runnable {
        private int friendsId;

        public deleteFriendThread(int friendsId) {
            this.friendsId = friendsId;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String path = "http://" + Constant.MY_SERVER_IP + "/PartyMania/DeleteFriends" + "?currnetId=" + currentUserId +"&friendsId=" + friendsId;
            Message message = new Message();
            queryFriendsResult = WebService.executeHttpGet(path);
            System.out.println(queryFriendsResult);
            if (queryFriendsResult == null || queryFriendsResult.equals("")) {
                message.what = Constant.DELETE_FRIEND_FAIL;
            } else if (queryFriendsResult.trim().equals("32")) {
                message.what = Constant.DELETE_FRIEND_FAIL;
            } else {
                message.what = Constant.DELETE_FRIEND_SUCCESS;
            }
            myHandler.sendMessage(message);
        }
    }

    public void doDeleteRelation(int friendsId) {
        deleteDialog = new ProgressDialog(this);
        deleteDialog.setTitle("Deleting");
        deleteDialog.setMessage("Deleting this people, please wait.");
        deleteDialog.setCancelable(false);
        deleteDialog.show();
        new Thread(new deleteFriendThread(friendsId)).start();
    }


}
