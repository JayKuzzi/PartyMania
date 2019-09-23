package com.qmul.partymania.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qmul.partymania.R;
import com.qmul.partymania.adapters.MessItemAdapter;
import com.qmul.partymania.beans.Comment_Item;
import com.qmul.partymania.tools.BitmapTool;
import com.qmul.partymania.tools.Constant;
import com.qmul.partymania.tools.Square;
import com.qmul.partymania.tools.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bb on 2017/3/14.
 */

public class CommentDialog extends Dialog {
    private RecyclerView recyclerView;
    private MessItemAdapter mAdapter;
    private List<Comment_Item> mData;
    private ImageView cancel, addComment;
    private EditText yourComments;
    private int clickedPostId;
    private String queryCommentsResult;
    private ProgressDialog queryCommentsDialog;
    private ProgressDialog addCommentsDialog;
    private MyHandler myHandler;
    private String currentNickName;

    public CommentDialog(Context context, int clickedPostId) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_comment);
        setCanceledOnTouchOutside(false);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("local_file", getContext().MODE_PRIVATE);
        currentNickName = sharedPreferences.getString("nickName", "");
        this.clickedPostId = clickedPostId;
        myHandler = new MyHandler(this);
        initData();
    }


    private void initView() {
        recyclerView = findViewById(R.id.mess_recyclerView);
        mAdapter = new MessItemAdapter(mData, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
        cancel = findViewById(R.id.mess_cancel);
        addComment = findViewById(R.id.mess_ok);
        yourComments = findViewById(R.id.mess_edit);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yourComments.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please type comment", Toast.LENGTH_SHORT).show();
                    return;
                }
                addCommentsDialog = new ProgressDialog(getContext());
                addCommentsDialog.setTitle("Adding");
                addCommentsDialog.setMessage("Adding Comments, please wait.");
                addCommentsDialog.setCancelable(false);
                addCommentsDialog.show();
                new Thread(new addCommentsThread()).start();
            }
        });
    }

    private void initData() {
        mData = new ArrayList<>();
        queryCommentsDialog = new ProgressDialog(getContext());
        queryCommentsDialog.setTitle("Loding");
        queryCommentsDialog.setMessage("Loading Comments, please wait.");
        queryCommentsDialog.setCancelable(false);
        queryCommentsDialog.show();
        new Thread(new queryCommentsThread()).start();
    }


    private static class MyHandler extends Handler {
        private final WeakReference<CommentDialog> mTarget;

        public MyHandler(CommentDialog target) {
            mTarget = new WeakReference<>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            CommentDialog target = mTarget.get();
            if (target != null) {
                switch (msg.what) {
                    case Constant.SERVER_NOT_RETURN:
                        target.initView();
                        target.queryCommentsDialog.dismiss();
                        Toast.makeText(target.getContext(), "Server exception", Toast.LENGTH_SHORT).show();
                        break;
                    case Constant.QUERY_COMMENT_NULL:
                        target.initView();
                        target.queryCommentsDialog.dismiss();
                        Toast.makeText(target.getContext(), "Server return null", Toast.LENGTH_SHORT).show();
                        break;
                    case Constant.QUERY_COMMENT_SUCCESS:
                        Toast.makeText(target.getContext(), "Query successful", Toast.LENGTH_SHORT).show();
                        JSONArray myJsonArray = null;
                        try {
                            myJsonArray = new JSONArray(target.queryCommentsResult);
                            for (int i = 0; i < myJsonArray.length(); i++) {
                                JSONObject object = myJsonArray.getJSONObject(i);
                                target.mData.add(new Comment_Item(object.getString("commentMan"), object.getString("comment")));
                            }
                            target.queryCommentsDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        target.initView();
                        break;

                    case Constant.ADD_COMMENT_FAIL:
                        target.addCommentsDialog.dismiss();
                        target.initView();
                        Toast.makeText(target.getContext(), "Server return null", Toast.LENGTH_SHORT).show();
                        break;
                    case Constant.ADD_COMMENT_SUCCESS:
                        Toast.makeText(target.getContext(), "Add successful", Toast.LENGTH_SHORT).show();
                        target.addCommentsDialog.dismiss();
                        target.initData();
                        break;
                }
            }
        }
    }

    private class queryCommentsThread implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String path = "http://" + Constant.MY_SERVER_IP + "/PartyMania/QueryComment" + "?postId=" + clickedPostId;
            Message message = new Message();
            queryCommentsResult = WebService.executeHttpGet(path);
            if (queryCommentsResult == null || queryCommentsResult.equals("")) {
                message.what = Constant.SERVER_NOT_RETURN;
            } else if (queryCommentsResult.trim().equals("19")) {
                message.what = Constant.QUERY_COMMENT_NULL;
            } else {
                message.what = Constant.QUERY_COMMENT_SUCCESS;
            }
            myHandler.sendMessage(message);
        }
    }

    private class addCommentsThread implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String path = "http://" + Constant.MY_SERVER_IP + "/PartyMania/AddComment" + "?postId=" + clickedPostId + "&currentNickname="
                    + currentNickName + "&comment=" + yourComments.getText().toString();
            Message message = new Message();
            queryCommentsResult = WebService.executeHttpGet(path);
            if (queryCommentsResult == null || queryCommentsResult.equals("")) {
                message.what = Constant.ADD_COMMENT_FAIL;
            } else if (queryCommentsResult.trim().equals("18")) {
                message.what = Constant.ADD_COMMENT_FAIL;
            } else {
                message.what = Constant.ADD_COMMENT_SUCCESS;
            }
            myHandler.sendMessage(message);
        }
    }
}
