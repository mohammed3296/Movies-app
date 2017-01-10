package com.mohammed_abdullah_bd1996.moviesappv3;
import android.app.Application;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
// this class for controlling the app
//Start class
public class Controller extends Application {
    //************************************************************************************
    public static final String TAG = Controller.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static Controller mInstance;

    //****************************************************************************************
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    //*****************************************************************************************
    public static synchronized Controller getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }
    //*****************************************************************************
    //function to add to request queue
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
}//end class