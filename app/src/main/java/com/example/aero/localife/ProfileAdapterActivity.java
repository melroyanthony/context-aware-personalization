package com.example.aero.localife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.aero.localife.ProfileListActivity;
import com.example.aero.localife.R;

import java.util.ArrayList;
import java.util.List;


public class ProfileAdapterActivity extends BaseAdapter {

    private Context context;
    ArrayList<ProfileListActivity> profileListActivity;

    public ProfileAdapterActivity(Context ctx, ArrayList<ProfileListActivity> profileListActivity) {
        this.profileListActivity = profileListActivity;
        this.context = ctx;
    }

    @Override
    public int getCount() {
        return profileListActivity.size();
    }

    @Override
    public Object getItem(int position) {
        return profileListActivity.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ProfileListActivity profileListItems = profileListActivity.get(position);

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem_profiles, null);
        }

        TextView textViewProfileName = (TextView) convertView.findViewById(R.id.textView_profiles);
        textViewProfileName.setText(profileListItems.getProfileName());
        return convertView;
    }
}
