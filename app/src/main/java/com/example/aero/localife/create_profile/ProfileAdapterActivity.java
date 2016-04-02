package com.example.aero.localife.create_profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.aero.localife.R;

import java.util.List;


public class ProfileAdapterActivity extends ArrayAdapter<ProfileListActivity> {
    private List<ProfileListActivity> profileListActivity;
    private Context context;

    public ProfileAdapterActivity(List<ProfileListActivity> profileListActivity, Context ctx) {
        super(ctx, R.layout.listitem_profiles, profileListActivity);
        this.profileListActivity = profileListActivity;
        this.context = ctx;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ProfileHolder holder = new ProfileHolder();
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.listitem_profiles, null);
            TextView textViewProfileName = (TextView) rowView.findViewById(R.id.textView_profiles);
            holder.profileName = textViewProfileName;
            rowView.setTag(holder);
        } else {
            holder = (ProfileHolder) rowView.getTag();
        }
        ProfileListActivity profile = profileListActivity.get(position);
        holder.profileName.setText(profile.getName());
        return rowView;
    }
    public static class ProfileHolder{
        public TextView profileName;
    }
}
