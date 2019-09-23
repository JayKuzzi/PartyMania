package com.qmul.partymania.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qmul.partymania.R;
import com.qmul.partymania.beans.Comment_Item;
import com.qmul.partymania.beans.FriendsList_Item;
import com.qmul.partymania.tools.BitmapTool;
import com.qmul.partymania.views.XCImageView;

import java.util.List;

public class MessItemAdapter extends RecyclerView.Adapter<MessItemAdapter.MyViewHolder> {
    private List<Comment_Item> mItemInfoList;
    private Context context;

    public MessItemAdapter(List<Comment_Item> mItemInfoList, Context context) {
        this.mItemInfoList = mItemInfoList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        itemView = inflater.inflate(R.layout.list_item_comment, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        Comment_Item comment_item = mItemInfoList.get(position);
        viewHolder.comment_nickname.setText(comment_item.getNickname()+" : ");
        viewHolder.comment_mess.setText(comment_item.getComment());
    }

    @Override
    public int getItemCount() {
        if (mItemInfoList == null) {
            return 0;
        }
        return mItemInfoList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        View item;//最外层子view

        TextView comment_nickname,comment_mess;

        public MyViewHolder(View itemView) {
            super(itemView);
            item = itemView;//将itemView赋给item拿去做监听
            comment_nickname = itemView.findViewById(R.id.comment_nickname);
            comment_mess = itemView.findViewById(R.id.comment_mess);

        }
    }


}
