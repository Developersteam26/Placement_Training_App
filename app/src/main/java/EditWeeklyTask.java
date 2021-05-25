package edu.education.androiddevelopmentcontest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditWeeklyTask extends AppCompatActivity {

    private final String EDIT_WEEKLYTASK_URL = "http://192.168.43.89/contest/index.php/admin/editWeeklyTask";

    private Bundle bundle;

    private String TASKId;
    private String TASKTitle;
    private String TASKDue;
    private boolean flag = false;

    private EditText title;
    private EditText due;
    private LinearLayout editTask;
    private LinearLayout warning;
    private TextView warningText;

    private StringRequest stringRequest;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_weekly_task);

        bundle = getIntent().getExtras();

        assert bundle != null;
        TASKId = bundle.getString("id");
        TASKTitle = bundle.getString("title");
        TASKDue = bundle.getString("due");

        title = findViewById(R.id.taskname);
        due = findViewById(R.id.duration);
        editTask = findViewById(R.id.editTask);
        warning = findViewById(R.id.warning);
        warningText = findViewById(R.id.warningText);

        title.setText(TASKTitle);
        due.setText(TASKDue);

        editTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TASKTitle = title.getText().toString();
                TASKDue = due.getText().toString();

                if (TASKTitle.isEmpty()) {
                    flag = true;
                    title.setError("Enter a Valid Task Title");
                }

                if (TASKDue.isEmpty()) {
                    flag = true;
                    due.setError("Enter a Time limit for the Task");
                }

                if (flag) {
                    warningText.setText("Some fields are found Missing");
                    warning.setVisibility(View.VISIBLE);
                } else {
                    //code for editing the data in database
                    requestQueue = Volley.newRequestQueue(EditWeeklyTask.this);
                    stringRequest = new StringRequest(Request.Method.POST, EDIT_WEEKLYTASK_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                                        JSONObject jsonObject = new JSONObject(response);
                                        String res = jsonObject.getString("status");
                                        if (res.equals("ok")) {
                                            onBackPressed();
                                        } else {
                                            warningText.setText("Failed to Edit the task");
                                            warning.setVisibility(View.VISIBLE);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            warningText.setText("Error Occurred on Editing the Task");
                            warning.setVisibility(View.VISIBLE);
                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();
                            params.put("title",TASKTitle);
                            params.put("due",TASKDue);
                            params.put("id",TASKId);
                            return params;
                        }
                    };

                    requestQueue.add(stringRequest);
                }
            }
        });
    }
}
