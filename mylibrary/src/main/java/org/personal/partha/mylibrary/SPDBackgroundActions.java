package org.personal.partha.mylibrary;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Class for Background Running Tasks. It has specific format in the params list
 *
 * 1. Context object
 * 2. String containing the operation name. Currently, it supports
 *      a. INITIALIZE_API(String REST_API_URL, String REST_API_PARAMS, RestApiCallback restApiCallback)
 *      b. INVOKE_APP_REST_API(String REST_API_URL, String REST_API_PARAMS, RestApiCallback restApiCallback)
 * 3...onwards. All the params required by the internal functions as mentioned above
 *
 */
public class SPDBackgroundActions extends AsyncTask<Object, Void, Object> {
    @Override
    protected Object doInBackground(Object... params) {
        Context ctx = (Context) params[0];
        String objectClass = (String) params[1];
        if (objectClass.toUpperCase().equals("INITIALIZE_API")) {
            return SPDUtilities.invokeRESTAPI((String) params[2], (String) params[3], (SPDUtilities.RestApiCallback) params[4]);
        } else if (objectClass.toUpperCase().equals("INVOKE_APP_REST_API")) {
            return SPDUtilities.invokeRESTAPI((String) params[2], (String) params[3]);
        }
        return null;
    }
}
