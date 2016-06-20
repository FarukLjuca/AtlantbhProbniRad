package com.example.faruk.learningcontentproviders.Adapeters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.faruk.learningcontentproviders.Classes.MyMessage;
import com.example.faruk.learningcontentproviders.R;

import java.util.List;

/**
 * Created by Faruk on 07/06/16.
 */
public class CustomAdapter extends BaseAdapter {
    private Context context;
    private List<MyMessage> data;

    public CustomAdapter(Context context, List<MyMessage> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.card, parent, false);
        }

        TextView text = (TextView) view.findViewById(R.id.tvText);
        text.setText(data.get(position).getAuthor() + ": " + data.get(position).getText());

        return view;
    }
}
