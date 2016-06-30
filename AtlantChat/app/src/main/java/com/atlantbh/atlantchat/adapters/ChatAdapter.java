package com.atlantbh.atlantchat.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atlantbh.atlantchat.R;
import com.atlantbh.atlantchat.classes.Session;
import com.atlantbh.atlantchat.classes.realm.Message;
import com.atlantbh.atlantchat.classes.realm.User;
import com.atlantbh.atlantchat.utils.AppUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Faruk on 21/06/16.
 */
public class ChatAdapter extends BaseAdapter {
    private Context context;
    private List<Message> messages;

    public ChatAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Message getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        Message currentMessage = getItem(position);

        boolean isUser = Session.getUserId() == currentMessage.getUserId();
        boolean isUsersView = false;

        if (convertView != null && convertView.getTag() != null) {
            isUsersView = ((ViewHolder) convertView.getTag()).isUsersView;
        }

        // We need to redraw view if View is not users view and message is from user or other way around
        if (convertView == null || (isUser && !isUsersView) || (!isUser && isUsersView)) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (isUser) {
                convertView = inflater.inflate(R.layout.list_view_item_message, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.list_view_item_message_gray, parent, false);
            }
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (!isUser) {
            String name = "Loading user";
            String profileUrl = "https://www.buira.net/assets/images/shared/default-profile.png";
            for (User user : Session.getUsers()) {
                if (user.getId() == currentMessage.getUserId()) {
                    if (user.getName() != null) name = user.getName();
                    if (user.getImage() != null) profileUrl = user.getImage();
                    break;
                }
            }
            if (name.equals("Loading user")) {
                Log.e(AppUtil.LOG_NAME, "User should be synchronized");
            }
            holder.user.setText(name);
            Picasso.with(context)
                    .load(profileUrl)
                    .into(holder.profileImage);
        }
        holder.message.setText(currentMessage.getMessage());
        holder.isUsersView = isUser;

        return convertView;
    }

    // Used in order for ListView items to not be focusable
    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    public void addMessage(Message message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        boolean isUsersView;

        @BindView(R.id.tvUser)
        TextView user;
        @BindView(R.id.tvMessage)
        TextView message;
        CircleImageView profileImage;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            profileImage = (CircleImageView) view.findViewById(R.id.ivProfileImage);
        }
    }
}
