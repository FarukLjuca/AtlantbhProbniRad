package com.atlantbh.atlantchat.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlantbh.atlantchat.R;
import com.atlantbh.atlantchat.model.Session;
import com.atlantbh.atlantchat.model.realm.Message;
import com.atlantbh.atlantchat.model.realm.User;
import com.atlantbh.atlantchat.utils.AppUtil;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_item_message, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Message currentMessage = getItem(position);

        DateTime dateNow = DateTime.now();
        DateTime dateMessage = new DateTime(currentMessage.getTime());

        if (isFirstForDay(dateMessage)) {
            holder.date.setVisibility(View.VISIBLE);

            if (dateNow.getYear() != dateMessage.getYear() ||
                    dateNow.getMonthOfYear() != dateMessage.getMonthOfYear() ||
                    dateNow.getDayOfYear() - dateMessage.getDayOfYear() > 1) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                holder.date.setText(dateFormat.format(currentMessage.getTime()));
            } else if (dateNow.getDayOfYear() - dateMessage.getDayOfYear() == 1 ||
                    (dateNow.getDayOfYear() == 1 && dateMessage.getDayOfYear() == (dateMessage.year().isLeap() ? 366 : 365))) {
                holder.date.setText(context.getResources().getString(R.string.yesterday));
            } else if (dateNow.getDayOfYear() == dateMessage.getDayOfYear()) {
                holder.date.setText(context.getResources().getString(R.string.today));
            } else {
                Log.e(AppUtil.LOG_NAME, "When comparing dates, not all cases were detected!");
            }
        } else {
            holder.date.setVisibility(View.GONE);
        }

        boolean isUser = Session.getUserId() == currentMessage.getUserId();

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        RelativeLayout myMessageContainer = (RelativeLayout) convertView.findViewById(R.id.my_message_container);
        RelativeLayout friendMessageContainer = (RelativeLayout) convertView.findViewById(R.id.friend_message_container);


        if (isUser) {
            myMessageContainer.setVisibility(View.VISIBLE);
            friendMessageContainer.setVisibility(View.GONE);

            holder.messageMe.setText(currentMessage.getMessage());
            holder.timeMe.setText(format.format(currentMessage.getTime()));
        } else {
            myMessageContainer.setVisibility(View.GONE);
            friendMessageContainer.setVisibility(View.VISIBLE);

            //String name = "Loading user";
            String profileUrl = "https://www.buira.net/assets/images/shared/default-profile.png";
            for (User user : Session.getUsers()) {
                if (user.getId() == currentMessage.getUserId()) {
                    //if (user.getName() != null) name = user.getName();
                    if (user.getImage() != null) profileUrl = user.getImage();
                    break;
                }
            }
            if (profileUrl.equals("https://www.buira.net/assets/images/shared/default-profile.png")) {
                Log.e(AppUtil.LOG_NAME, "User should be synchronized");
            }

            Picasso.with(context)
                    .load(profileUrl)
                    .into(holder.profileImage);
            holder.messageOther.setText(currentMessage.getMessage());
            holder.timeOther.setText(format.format(currentMessage.getTime()));
        }

        return convertView;

        /*

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
                convertView = inflater.inflate(R.layout.list_view_item_message_old, parent, false);
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
            SimpleDateFormat simpleDate =  new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Date date = currentMessage.getTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, 2);

            String response = "";
            long difference = (new Date(System.currentTimeMillis()).getTime() - calendar.getTime().getTime()) / 1000;

            if (difference < 10) response = "Just now";
            else if (difference < 30) response = "10 seconds ago";
            else if (difference < 60) response = "30 seconds ago";
            else if (difference < 2 * 60) response = "1 minute ago";
            else if (difference < 60 * 60) response = difference / 60 + " minutes ago";
            else if (difference < 24 * 60 * 60) response = calendar.getTime().getHours() + ":" + calendar.getTime().getMinutes();
            else response = calendar.getTime().getDay() + "." + calendar.getTime().getMonth() + "." + calendar.getTime().getYear() + ". " + calendar.getTime().getHours() + ":" + calendar.getTime().getMinutes();

            holder.time.setText(response);
            Picasso.with(context)
                    .load(profileUrl)
                    .into(holder.profileImage);
        }
        holder.message.setText(currentMessage.getMessage());
        holder.isUsersView = isUser;

        return convertView;
        */
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
        @BindView(R.id.tvDate)
        TextView date;
        @BindView(R.id.tvMessageMe)
        TextView messageMe;
        @BindView(R.id.tvTimeMe)
        TextView timeMe;
        @BindView(R.id.tvMessageOther)
        TextView messageOther;
        @BindView(R.id.tvTimeOther)
        TextView timeOther;
        CircleImageView profileImage;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            profileImage = (CircleImageView) view.findViewById(R.id.ivProfileImage);
        }
    }

    private boolean isFirstForDay(DateTime messageDate) {
        DateTime date;
        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();
        for (Message message : messages) {
            Boolean boolean1 = calendar.compareTo(calendar1) < 0;
            date = new DateTime(message.getTime());
            calendar.setTime(message.getTime());
            calendar1.setTime(messageDate.toDate());
            if (calendar1.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                    calendar1.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                    calendar1.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)) {
                if (boolean1) {
                    return false;
                }
            } else {
                int a = 10;
            }
        }
        return true;
    }
}
