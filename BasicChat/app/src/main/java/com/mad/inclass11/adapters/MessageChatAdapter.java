package com.mad.inclass11.adapters;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mad.inclass11.CommentActivity;
import com.mad.inclass11.MainActivity;
import com.mad.inclass11.R;
import com.mad.inclass11.model.Chats;
import com.mad.inclass11.model.MessageChat;
import com.mad.inclass11.utility.TimeUtility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sanket on 11/14/2016.
 */

public class MessageChatAdapter extends RecyclerView.Adapter<MessageChatAdapter.ViewHolder>{
    private ArrayList<Chats> chatList;
    private OnItemClickListener mItemClickListener;
    private Context mContext;
    private ArrayList<MessageChat> messageChats;

    public MessageChatAdapter(Context context, ArrayList<Chats> chatList, ArrayList<MessageChat> messageChats) {
        this.chatList = chatList;
        mContext = context;
        this.messageChats = messageChats;
    }

    @Override
    public MessageChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.layout_chat_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MessageChatAdapter.ViewHolder holder, int position) {

        Chats chat = chatList.get(position);
        TextView chatMessageText= holder.textViewChatMessage;
        TextView messageAuthor= holder.textViewChatMessageAuthor;
        TextView timeFromNow= holder.textViewTimeFromNow;
        ImageView chatMessageImage = holder.imageViewChatMessage;
        ImageView chatComment = holder.imageViewChatComment;
        ImageView chatDelete= holder.imageViewChatDelete;

        holder.imageViewChatDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chats chatElement = chatList.get(holder.getAdapterPosition());
                int elementPosnToBeDeleted = -1;

                if (!chatElement.isComment()) {
                    for (int k = 0; k < messageChats.size(); k++) {
                        if (messageChats.get(k).getTime().equals(chatElement.getTime())) {
                            elementPosnToBeDeleted = k;
                            break;
                        }

                        if (messageChats.get(k).getComments() != null) {
                            ArrayList<MessageChat> comments = messageChats.get(k).getComments();

                            for (int l = 0; l < comments.size(); l++) {
                                if (comments.get(l).getTime().equals(chatElement.getTime())) {
                                    elementPosnToBeDeleted = k;
                                    break;
                                }
                            }
                        }
                    }
                }

                if (elementPosnToBeDeleted != -1) {
                    messageChats.remove(elementPosnToBeDeleted);
                    MainActivity.rootRef.child("Messages").setValue(messageChats);
                }
            }
        });

        holder.imageViewChatComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), CommentActivity.class));
            }
        });
        if(!chat.isComment()) {
            chatComment.setVisibility(View.VISIBLE);
            chatDelete.setVisibility(View.VISIBLE);
            if (chat.getMessageType().equals("Text")) {
                chatMessageText.setVisibility(View.VISIBLE);
                chatMessageText.setText(chat.getMessage());
                chatMessageImage.setVisibility(View.INVISIBLE);
                chatMessageImage.setImageBitmap(null);
            } else if (chat.getMessageType().equals("Image")) {
                chatMessageImage.setVisibility(View.VISIBLE);
                chatMessageText.setVisibility(View.INVISIBLE);

                //TODO
                Picasso.with(mContext).load(chat.getImageUrl()).into(chatMessageImage);
            }
        }else
        {
            chatComment.setVisibility(View.INVISIBLE);
            chatDelete.setVisibility(View.INVISIBLE);
            chatMessageImage.setImageBitmap(null);
            if (chat.getMessageType().equals("message")) {

            }
        }

        messageAuthor.setText(chat.getUserName());
        try {
            timeFromNow.setText(TimeUtility.prettyDate(chat.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }


    //Method to initialize ItemCLickListner
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewChatMessage;
        public TextView textViewChatMessageAuthor;
        public ImageView imageViewChatMessage;
        public ImageView imageViewChatComment;
        public ImageView imageViewChatDelete;
        public TextView textViewTimeFromNow;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewChatMessage = (TextView) itemView.findViewById(R.id.textViewChatMessage);
            textViewChatMessageAuthor = (TextView) itemView.findViewById(R.id.textViewChatAuthor);
            imageViewChatMessage = (ImageView) itemView.findViewById(R.id.imageViewChatImage);
            imageViewChatComment = (ImageView) itemView.findViewById(R.id.imageViewChatComment);
            imageViewChatDelete = (ImageView) itemView.findViewById(R.id.imageViewChatDelete);
            textViewTimeFromNow = (TextView) itemView.findViewById(R.id.textViewChatTimeFrom);
            itemView.setOnClickListener(this);  //Bind your setOnClickLister Here on ViewHolder creation
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }


}
