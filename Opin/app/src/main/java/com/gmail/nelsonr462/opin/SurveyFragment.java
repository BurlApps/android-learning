package com.gmail.nelsonr462.opin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ConfigCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseConfig;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.SaveCallback;

import org.json.JSONArray;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SurveyFragment extends android.support.v4.app.ListFragment {
    private String TAG = SurveyFragment.class.getSimpleName();

    private OnSurveyFragmentClick mListener;
    private ParseInstallation mParseInstallation;
    protected List<ParseObject> mSurveys;
    protected ParseRelation<ParseObject> mSurveyRelation;
    protected ParseConfig mParseConfig;

    @Bind(R.id.surveyLoadProgressBar) ProgressBar mProgressBar;
    @Bind(android.R.id.empty) LinearLayout mEmptyLayout;


    public SurveyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseConfig.getInBackground(new ConfigCallback() {
            @Override
            public void done(ParseConfig parseConfig, ParseException e) {
                if(e == null) {
                    mParseConfig = parseConfig;
                    OpinApplication.mParseConfig = mParseConfig;
                } else {
                    mParseConfig = ParseConfig.getCurrentConfig();
                    OpinApplication.mParseConfig = mParseConfig;
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_survey, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mEmptyLayout.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mParseInstallation = ParseInstallation.getCurrentInstallation();
        OpinApplication.mCurrentInstallation = mParseInstallation;

        mParseInstallation.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, e.getMessage());
                    return;
                }



                mSurveyRelation = mParseInstallation.getRelation(ParseConstants.KEY_SURVEYS);
                ParseQuery<ParseObject> query = mSurveyRelation.getQuery();
                query.whereEqualTo(ParseConstants.KEY_SURVEY_STATE, 1);
                query.whereNotEqualTo(ParseConstants.KEY_SHOW_SURVEY, false);
                query.addAscendingOrder(ParseConstants.KEY_CREATED_AT);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> surveyList, ParseException e) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mEmptyLayout.setVisibility(View.VISIBLE);
                        if (e == null) {
                            mSurveys = surveyList;
                            String[] surveys = new String[mSurveys.size()];
                            int i = 0;
                            for (ParseObject survey : mSurveys) {
                                surveys[i] = survey.getString(ParseConstants.KEY_NAME);
                                i++;
                            }

                            SurveyAdapter adapter = new SurveyAdapter(
                                    getListView().getContext(),
                                    mSurveys
                            );

                            setListAdapter(adapter);
                        } else {
                            Log.e(TAG, e.getMessage());
                            AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
                            builder.setMessage(e.getMessage())
                                    .setTitle(R.string.fetch_surveys_error_title)
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSurveyFragmentClick) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSurveyFragmentClick");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String installationId = mParseInstallation.getObjectId();
        ParseObject survey = mSurveys.get(position);
        String surveyId = survey.getObjectId();

        String url = mParseConfig.getString(ParseConstants.KEY_HOST)+"/surveys/"+surveyId+"/"+installationId;

        JSONArray loadingBackground = mParseConfig.getJSONArray(ParseConstants.KEY_LOADER_BACKGROUND);
        JSONArray loaderPrimary = mParseConfig.getJSONArray(ParseConstants.KEY_LOADER_PRIMARY);

        ParseConstants.COLOR_LOADER_BACKGROUND = new int[loadingBackground.length()];
        ParseConstants.COLOR_LOADER_PRIMARY = new int[loaderPrimary.length()];

        for (int i = 0; i < loadingBackground.length(); i++) {
            ParseConstants.COLOR_LOADER_BACKGROUND[i] = loadingBackground.optInt(i);
            ParseConstants.COLOR_LOADER_PRIMARY[i] = loaderPrimary.optInt(i);
        }


        Intent intent = new Intent(getActivity(), SurveyWebViewActivity.class);
        intent.putExtra("surveyUrl", url);
        startActivity(intent);



    }

    public interface OnSurveyFragmentClick {
        public void onSurveyFragmentClick(String id);
    }

}
