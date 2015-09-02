package com.test.nelson.ribbit;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by nelson on 8/26/15.
 */
public class RibbitApplication extends Application {

    public void onCreate(){
        super.onCreate();

    // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "DTreqlikNNZ0jJKPKi3JD5qWaQv9ZaGNu2E9ApTc", "6nLbIKsVZ59Arw7fx0ksyE5GpdO5qDapFqnj6M8Z");

    }
}
