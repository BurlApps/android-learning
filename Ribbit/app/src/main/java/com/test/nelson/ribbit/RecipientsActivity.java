package com.test.nelson.ribbit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipientsActivity extends AppCompatActivity implements RecipientsFragment.OnRecipientSelectedListener{
    public static final String TAG = RecipientsActivity.class.getSimpleName();

    @Bind(R.id.progressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipients);
        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipients, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_select_all) {
            getFragmentManager().beginTransaction().add(R.id.RecipientsFragment, new RecipientsFragment()).commit();
            RecipientsFragment fragment = (RecipientsFragment) getFragmentManager().findFragmentById(R.id.RecipientsFragment);
            fragment.selectAll();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onRecipientSelected(String id) {
        Toast.makeText(this, "Selected: "+id, Toast.LENGTH_SHORT).show();
    }
}
