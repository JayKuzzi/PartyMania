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
import android.widget.Toast;

import com.qmul.partymania.R;
import com.qmul.partymania.activities.Friends;
import com.qmul.partymania.beans.FriendsList_Item;
import com.qmul.partymania.beans.PostList_Item;
import com.qmul.partymania.dialogs.UserDetailDialogNoAddFriend;
import com.qmul.partymania.tools.BitmapTool;
import com.qmul.partymania.tools.Square;
import com.qmul.partymania.views.XCImageView;

import java.util.List;

public class  FriendsItemAdapter extends RecyclerView.Adapter<FriendsItemAdapter.MyViewHolder> {
    private List<FriendsList_Item> mItemInfoList;
    private Context context;
    private Friends activity;

    public FriendsItemAdapter(List<FriendsList_Item> mItemInfoList, Context context, Friends friends) {
        this.mItemInfoList = mItemInfoList;
        this.context = context;
        this.activity = friends;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        itemView = inflater.inflate(R.layout.list_item_my_friend, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        final FriendsList_Item friendsList_item = mItemInfoList.get(position);
        viewHolder.friend_item_nickname.setText(friendsList_item.getNickName());
        viewHolder.friend_item_head_photo.setImageBitmap(Square.centerSquareScaleBitmap(BitmapTool.String2Bitmap(friendsList_item.getHeadphoto()), 200));

        viewHolder.friend_item_head_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetailDialogNoAddFriend userDetailDialogNoAddFriend = new UserDetailDialogNoAddFriend(context, friendsList_item.getNickName());
                userDetailDialogNoAddFriend.show();
            }
        });

        viewHolder.friend_item_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetailDialogNoAddFriend userDetailDialogNoAddFriend = new UserDetailDialogNoAddFriend(context, friendsList_item.getNickName());
                userDetailDialogNoAddFriend.show();
            }
        });

        viewHolder.friend_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("You wanna delete this followed one?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        activity.doDeleteRelation(friendsList_item.getFriendsId());
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

        TextView friend_item_nickname;
        ImageView friend_item_delete;
        XCImageView friend_item_head_photo;

        public MyViewHolder(View itemView) {
            super(itemView);
            item = itemView;//将itemView赋给item拿去做监听
            friend_item_nickname = itemView.findViewById(R.id.friend_item_nickname);
            friend_item_delete = itemView.findViewById(R.id.friend_item_delete);
            friend_item_head_photo = itemView.findViewById(R.id.matching_item_head_photo);

        }
    }


}
