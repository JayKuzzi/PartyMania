package com.qmul.partymania.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qmul.partymania.R;
import com.qmul.partymania.activities.EditProfile;
import com.qmul.partymania.adapters.MyPostItemAdapter;
import com.qmul.partymania.beans.PostList_Item;
import com.qmul.partymania.dialogs.AuthorDialog;
import com.qmul.partymania.dialogs.CommentDialog;
import com.qmul.partymania.dialogs.UserDetailDialog;
import com.qmul.partymania.tools.Constant;
import com.qmul.partymania.tools.Square;
import com.qmul.partymania.tools.WebService;
import com.qmul.partymania.views.XCImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class Me extends Fragment implements View.OnClickListener {

    private View view;
    private Button btnEditProfile, aboutApp;
    private MyPostItemAdapter mAdapter;
    private List<PostList_Item> mData;
    private RecyclerView recyclerView;

    private MyHandler myHandler = new MyHandler(this);
    private ProgressDialog queryMyPostsDialog, deletePostsThread;
    private SharedPreferences sharedPreferences;
    private int currentUserId;
    private String queryMypostsResult;

    private XCImageView head_photo;
    private TextView me_age, me_email;
    private ImageView me_sex;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_me, container, false);
        initData();
        return view;
    }

    private void initData() {
        btnEditProfile = view.findViewById(R.id.editProfile);
        aboutApp = view.findViewById(R.id.aboutApp);
        head_photo = view.findViewById(R.id.login_head);
        me_age = view.findViewById(R.id.me_age);
        me_email = view.findViewById(R.id.me_email);
        me_sex = view.findViewById(R.id.me_sex);
        btnEditProfile.setOnClickListener(this);
        aboutApp.setOnClickListener(this);
        sharedPreferences = getActivity().getSharedPreferences("local_file", getActivity().MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("userId", -1);
        mData = new ArrayList<>();
        queryMyPostsDialog = new ProgressDialog(getContext());
        queryMyPostsDialog.setTitle("Searching");
        queryMyPostsDialog.setMessage("Searching my posts, please wait.");
        queryMyPostsDialog.setCancelable(false);
        queryMyPostsDialog.show();
        new Thread(new queryMyPostsThread()).start();
    }

    private void init() {
        recyclerView = view.findViewById(R.id.manage_posts_recyclerView);
        mAdapter = new MyPostItemAdapter(mData, getActivity(),this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        String currentHead = sharedPreferences.getString("headphoto", "");
        int currentSex = sharedPreferences.getInt("sex", -1);
        String email = sharedPreferences.getString("email", "");
        String age = sharedPreferences.getString("age", "");
        me_email.setText(email);
        me_age.setText("Age : " + age);
        if (currentHead.equals("")) {
            head_photo.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_photo));

        } else {
            byte[] bytes = Base64.decode(currentHead, Base64.DEFAULT);
            head_photo.setImageBitmap(Square.centerSquareScaleBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length), 200));
        }
        if (currentSex == 1)
            me_sex.setImageResource(R.drawable.ic_man_x);
        else
            me_sex.setImageResource(R.drawable.ic_woman_x);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editProfile:
                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
                break;
            case R.id.aboutApp:
                AuthorDialog authorDialog = new AuthorDialog(getContext());
                authorDialog.show();
                break;
        }
    }

    public void doDeleteRelation(int currentPostId) {
        deletePostsThread = new ProgressDialog(getContext());
        deletePostsThread.setTitle("Deleting");
        deletePostsThread.setMessage("Deleting my posts, please wait.");
        deletePostsThread.setCancelable(false);
        deletePostsThread.show();
        new Thread(new deletePostsThread(currentPostId)).start();

    }


    private static class MyHandler extends Handler {
        private final WeakReference<Me> mTarget;

        public MyHandler(Me target) {
            mTarget = new WeakReference<>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            Me target = mTarget.get();
            if (target != null) {
                switch (msg.what) {
                    case Constant.SERVER_NOT_RETURN:
                        target.init();
                        target.queryMyPostsDialog.dismiss();
                        Toast.makeText(target.getContext(), "Server exception", Toast.LENGTH_SHORT).show();
                        break;
                    case Constant.QUERY_POST_NULL:
                        target.init();
                        target.queryMyPostsDialog.dismiss();
                        if (target.mData.isEmpty()) {
                            target.recyclerView.setVisibility(View.INVISIBLE);
                        }
                        Toast.makeText(target.getContext(), "Server return null", Toast.LENGTH_SHORT).show();
                        break;
                    case Constant.QUERY_MY_POST_SUCCESS:
                        Toast.makeText(target.getContext(), "Query successful", Toast.LENGTH_SHORT).show();
                        JSONArray myJsonArray = null;
                        try {
                            myJsonArray = new JSONArray(target.queryMypostsResult);
                            for (int i = 0; i < myJsonArray.length(); i++) {
                                JSONObject object = myJsonArray.getJSONObject(i);
                                String postedTo = "";
                                if (object.getInt("type") == 3)
                                    postedTo = "Moments";
                                else
                                    postedTo = "PartyHall";
                                target.mData.add(new PostList_Item(object.getInt("postId"), object.getString("photo"), postedTo,
                                        object.getString("content"), object.getString("time")
                                ));
                            }
                            target.queryMyPostsDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        target.init();
                        if (target.mData.isEmpty()) {
                            target.recyclerView.setVisibility(View.INVISIBLE);
                        } else
                            target.recyclerView.setVisibility(View.VISIBLE);
                        break;
                    case Constant.DELETE_POST_FAIL:
                        target.initData();
                        target.deletePostsThread.dismiss();
                        Toast.makeText(target.getContext(), "Server exception", Toast.LENGTH_SHORT).show();
                        break;
                    case Constant.DELETE_POST_SUCCESS:
                        target.deletePostsThread.dismiss();
                        Toast.makeText(target.getContext(), "Delete successful", Toast.LENGTH_SHORT).show();
                        target.initData();
                        if (target.mData.isEmpty()) {
                            target.recyclerView.setVisibility(View.INVISIBLE);
                        }
                        break;
                }
            }
        }
    }

    private class queryMyPostsThread implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String path = "http://" + Constant.MY_SERVER_IP + "/PartyMania/QueryPost" + "?postManId=" + currentUserId + "&type=a";
            Message message = new Message();
            queryMypostsResult = WebService.executeHttpGet(path);
            System.out.println(queryMypostsResult);
            if (queryMypostsResult == null || queryMypostsResult.equals("")) {
                message.what = Constant.SERVER_NOT_RETURN;
            } else if (queryMypostsResult.trim().equals("12")) {
                message.what = Constant.QUERY_POST_NULL;
            } else {
                message.what = Constant.QUERY_MY_POST_SUCCESS;
            }
            myHandler.sendMessage(message);
        }
    }

    private class deletePostsThread implements Runnable {
        private int currentPostId;

        public deletePostsThread(int currentPostId) {
            this.currentPostId = currentPostId;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String path = "http://" + Constant.MY_SERVER_IP + "/PartyMania/DeletePost" + "?postId=" + currentPostId;
            Message message = new Message();
            queryMypostsResult = WebService.executeHttpGet(path);
            System.out.println(queryMypostsResult);
            if (queryMypostsResult.trim().equals("14")) {
                message.what = Constant.DELETE_POST_FAIL;
            } else {
                message.what = Constant.DELETE_POST_SUCCESS;
            }
            myHandler.sendMessage(message);
        }
    }
}
