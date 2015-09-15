package com.gmail.nelsonr462.opin;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddClassFragment extends android.support.v4.app.Fragment {

    private OnClassFragmentInteraction mListener;

    @Bind(R.id.classCodeEditText) EditText mClassCode;


    public AddClassFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_class, container, false);
        ButterKnife.bind(this, rootView);
        mClassCode.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnClassFragmentInteraction) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnClassFragmentInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.addClassButton)
    public void addClass() {
        // to lowercase, alphanumeric, query for class code

        String classCode = mClassCode.getText().toString();
        classCode = classCode.replaceAll("[^A-Za-z0-9]", "");
        classCode = classCode.toLowerCase();

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_CLASS);
        query.whereEqualTo(ParseConstants.KEY_CODE, classCode);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> classes, ParseException e) {
                // Change progress bar visibility

                mClassCode.setText("");

                if(e == null && classes.size() > 0) {
                    // success
                    ParseObject classObject = classes.get(0);
                    ParseRelation<ParseInstallation> relation = classObject.getRelation(ParseConstants.KEY_STUDENTS_RELATION);
                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                    relation.add(installation);
                    classObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                alertUser(1);
                            } else {
                                Toast.makeText(getActivity(), R.string.success_toast_add_class, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    // error
                    alertUser(0);
                }
            }
        });
    }

    private void alertUser(int errorType) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        if (errorType == 0) {
            builder.setMessage(R.string.add_class_error_message)
                    .setTitle(R.string.add_class_error_title)
                    .setPositiveButton(android.R.string.ok, null);
            android.app.AlertDialog dialog = builder.create();
            dialog.show();
        } else if (errorType == 1) {
            builder.setMessage(R.string.save_add_class_error_message)
                    .setTitle(R.string.add_class_error_title)
                    .setPositiveButton(android.R.string.ok, null);
            android.app.AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


    public interface OnClassFragmentInteraction {
        // TODO: Update argument type and name
        public void onClassFragmentInteraction(Uri uri);
    }

}
