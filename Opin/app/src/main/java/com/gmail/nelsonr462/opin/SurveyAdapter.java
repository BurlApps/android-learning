package com.gmail.nelsonr462.opin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

public class SurveyAdapter extends ArrayAdapter<ParseObject> {

    protected Context mContext;
    protected List<ParseObject> mSurveys;


    public SurveyAdapter(Context context, List<ParseObject> surveys) {
        super(context, R.layout.survey_item, surveys);
        mContext = context;
        mSurveys = surveys;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.survey_item, null);
            holder = new ViewHolder();
            holder.nameLabel = (TextView) convertView.findViewById(R.id.surveyLabel);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ParseObject survey = mSurveys.get(position);

        holder.nameLabel.setText(survey.getString(ParseConstants.KEY_NAME));

        return convertView;
    }

    private static class ViewHolder {
        TextView nameLabel;
    }
}
