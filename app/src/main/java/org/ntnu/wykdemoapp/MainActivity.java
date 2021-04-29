package org.ntnu.wykdemoapp;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.Date;
import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private String TAG = "WYKDEMOAPP";


    private TextView userIDTextView;
    private TextView signupTextView;
    private ListView lView;
    private DataAdapter adapter;

    private int user_id;
    private int user_signup_time;
    private JSONArray auth_log;
    private JSONArray hash_change_log;

    SimpleDateFormat sdf;
    Date date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //class wide param
        sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+1"));

        lView = findViewById(R.id.list_view);

        //tView = findViewById(R.id.current_user);

        Intent broadcastedIntent = getIntent();
        user_id = broadcastedIntent.getIntExtra("UID", 0);

        //tView.setText( "Current User ID: " + String.valueOf(user_id) );
        GetUserData();
    }

    private void GetUserData(){
        //"http://10.24.100.190:9090/wyk/api/v1/user/info?uid="

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constants.WYK_SERVER.IP + Constants.WYK_SERVER.ENDPOINTS[0] + String.valueOf(user_id),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //On Success
                        Log.w(TAG, response.toString());

                        //parse response JSON object
                        parseResponseJSON(response);
                        basicUserDataGUI();
                        try {
                            authLogGUI(response.getJSONArray("auth_log"));
                        } catch (JSONException e) {
                            Log.w(TAG, "GetUserData: Exception parsing JSON object.");
                        }

                        //Update user interface here.
                        //updateGUI(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //On Error
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void updateGUI(JSONObject user_data) {
        //adapter = new DataAdapter(getApplicationContext(), 1);
        //lView.setAdapter(adapter);
    }

    /**
     * Update the basic user GUI
     */
    private void basicUserDataGUI() {
        userIDTextView = findViewById(R.id.current_user);
        signupTextView = findViewById(R.id.signup_time);

        userIDTextView.setText( "USER ID: " + String.valueOf(this.user_id) );
        date = new Date( Long.parseLong(String.valueOf(this.user_signup_time)) * 1000L );
        signupTextView.setText( "SIGNED UP AT: " + sdf.format(date) );
    }

    /**
     * Update authentication log data list view
     * @param auth_log
     */
    private void authLogGUI(JSONArray auth_log) {
        String[] id = new String[auth_log.length()];
        String[] time = new String[auth_log.length()];
        String[] status = new String[auth_log.length()];

        for(int i = 0; i < auth_log.length(); i ++ ){
            try {
                JSONObject jsonObject = auth_log.getJSONObject(i);

                id[i]   = String.valueOf( jsonObject.getInt("auth_id") );

                date  = new Date( Long.parseLong(String.valueOf( jsonObject.getInt("auth_time") )) * 1000L );
                time[i] = sdf.format( date );

                int s = jsonObject.getInt("auth_result");
                if( s == 1 ) {
                    status[i] = "Success";
                } else if ( s == -1 ) {
                    status[i] = "Failed";
                }

            } catch (JSONException e) {
                Log.w(TAG, "authLogGUI: Exception parsing JSON Object");
            }
        }

        adapter = new DataAdapter(getApplicationContext(), id, time, status, Constants.DATA_TYPE.AUTH_LOG);
        lView.setAdapter(adapter);
    }

    private void hashChangeLogGUI(JSONArray hash_change_log) {
        String[] id = new String[hash_change_log.length()];
        String[] time = new String[hash_change_log.length()];
        String[] status = new String[hash_change_log.length()];

        for(int i = 0; i < hash_change_log.length(); i ++ ){
            try {
                JSONObject jsonObject = hash_change_log.getJSONObject(i);

                id[i]   = String.valueOf( jsonObject.getInt("log_id") );
                date  = new Date( Long.parseLong(String.valueOf( jsonObject.getInt("change_time") )) * 1000L );
                time[i] = sdf.format( date );

            } catch (JSONException e) {
                Log.w(TAG, "authLogGUI: Exception parsing JSON Object");
            }
        }

        adapter = new DataAdapter(getApplicationContext(), id, time, status, Constants.DATA_TYPE.HASH_LOG);
        lView.setAdapter(adapter);
    }


    /**
     * parses response JSON object
     * @param input
     */
    private void parseResponseJSON(JSONObject input) {
        try {
            this.user_id = input.getInt("user_id");
            this.user_signup_time = input.getInt("signup_time");
            this.auth_log = input.getJSONArray("auth_log");
            this.hash_change_log = input.getJSONArray("hash_change_log");
        } catch (JSONException e) {
            Log.w(TAG, "Exception parssing response JSON");
        }
    }

    public void onAuthLogClick(View view){
        authLogGUI(this.auth_log);
    }

    public void onHashChangeClick(View view) {
        hashChangeLogGUI(this.hash_change_log);
    }
}
