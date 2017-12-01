package com.example.android.securityapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ramana on 10/8/2017.
 */

public class SecurityCompActivity extends AppCompatActivity {
    TextView Title,Content,Count,Roll;
    Spinner spinner1;
    Button change,upvote;
    String _status,complaintId,Status,userId;
    int count=0;
    String statusurl = "https://sreekana123.000webhostapp.com/api/changestatus";
    String url = "https://sreekana123.000webhostapp.com/api/upvote";
    SharedPreferences login;
    SharedPreferences.Editor edit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.security_complaint_view);
        login = getApplicationContext().getSharedPreferences(Prefer.AUTH_FILE,MODE_PRIVATE);
        edit = login.edit();
        userId = login.getString(Prefer.USER_ID,null);

        Title = (TextView) findViewById(R.id.title_view);
        Content=(TextView) findViewById(R.id.content_view);
        Roll=(TextView) findViewById(R.id.roll_number_view);
        Title.setText(getIntent().getStringExtra("title"));
        Content.setText(getIntent().getStringExtra("content"));
        Roll.setText(getIntent().getStringExtra("roll"));
        Status = getIntent().getStringExtra("status");

        complaintId = getIntent().getStringExtra("id");
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        addListenerOnButton();
    }

    public void addListenerOnButton() {

        change = (Button) findViewById(R.id.change);

        change.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changeStatus();
            }

        });
    }

    private void changeStatus() {
        _status = String.valueOf(spinner1.getSelectedItem());

//        Log.v("tins", login.getString(Prefer.USER_ROLE,null));
        if(login.getString(Prefer.USER_ROLE,"0").equals("1")) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, statusurl,
                    new Response.Listener<String>() {

                        @Override

                        public void onResponse(String response) {
                            try {
                                Log.i("tins", response);

                                JSONObject obj = new JSONObject(response);
                                Boolean success = obj.getBoolean("success");
                                Log.d("success", success + "");
                                if (success) {
                                    Log.d("check", "" + obj);
                                    Toast.makeText(getApplicationContext(), "Status Changed", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    String errorString = "error";
                                    Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.i("eeee", error.toString());
                    change.setEnabled(true);
                }

            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("status", _status);
                    params.put("complaint_id", complaintId);
                    Log.d("code", params.get("status"));
                    return params;
                }
            };
            Volley.newRequestQueue(this).add(stringRequest.setShouldCache(false));
        }else{
            Toast.makeText(getApplicationContext(), "Only admin can change", Toast.LENGTH_LONG).show();

        }

    }

}

