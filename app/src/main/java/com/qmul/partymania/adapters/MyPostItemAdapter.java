package com.qmul.partymania.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qmul.partymania.R;
import com.qmul.partymania.beans.PostList_Item;
import com.qmul.partymania.fragments.Me;
import com.qmul.partymania.tools.BitmapTool;
import com.qmul.partymania.views.XCImageView;

import java.util.List;

public class MyPostItemAdapter extends RecyclerView.Adapter<MyPostItemAdapter.MyViewHolder> {
    private List<PostList_Item> mItemInfoList;
    private Context context;
    private Me me;

    public MyPostItemAdapter(List<PostList_Item> mItemInfoList, Context context, Me me) {
        this.mItemInfoList = mItemInfoList;
        this.context = context;
        this.me = me;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        itemView = inflater.inflate(R.layout.list_item_my_post, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        final PostList_Item postList_item = mItemInfoList.get(position);
        viewHolder.my_post_item_postedTo.setText("Posted To: " + postList_item.getPostedTo());
        viewHolder.my_post_item_content.setText(postList_item.getContent());
        viewHolder.my_post_item_time.setText("Time: " + postList_item.getTime());
        viewHolder.my_post_item_photo.setImageBitmap(BitmapTool.String2Bitmap(postList_item.getPhoto()));

        viewHolder.my_post_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("You wanna delete this post?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        me.doDeleteRelation(postList_item.getPostId());
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
        });

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

        TextView my_post_item_postedTo, my_post_item_content, my_post_item_time;
        ImageView my_post_item_photo, my_post_item_delete;

        public MyViewHolder(View itemView) {
            super(itemView);
            item = itemView;//将itemView赋给item拿去做监听
            my_post_item_postedTo = itemView.findViewById(R.id.my_post_item_postedTo);
            my_post_item_content = itemView.findViewById(R.id.my_post_item_content);
            my_post_item_photo = itemView.findViewById(R.id.my_post_item_photo);
            my_post_item_delete = itemView.findViewById(R.id.my_post_item_delete);
            my_post_item_time = itemView.findViewById(R.id.my_post_item_time);
        }
    }


}
