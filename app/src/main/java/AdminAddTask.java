package edu.education.androiddevelopmentcontest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.util.UUID;

public class AdminAddTask extends AppCompatActivity {

    private final String ADD_WEEKLY_TASK_URL = "http://192.168.43.89/contest/index.php/admin/addWeeklyTask";

    private Spinner dropdown;
    private LinearLayout addTask;
    private LinearLayout warning;
    private TextView warningText;
    private EditText taskName;
    private EditText duration;

    private String INFLATER_ELEMENTS[] = {"Quiz","Lectures or Formulas"};
    private String TASKId;
    private String TASKName;
    private String Duration;
    private String FormatType;
    private boolean flag = false;

    private ArrayAdapter<String> adapter;
    private Bundle bundle;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_task);

        bundle = getIntent().getExtras();
        TASKId = bundle.getString("id");

        dropdown = findViewById(R.id.dropdown);
        addTask = findViewById(R.id.addTask);
        taskName = findViewById(R.id.taskname);
        duration = findViewById(R.id.duration);
        warning = findViewById(R.id.warning);
        warningText = findViewById(R.id.warningText);

        adapter = new ArrayAdapter<String>(this,R.layout.dropdownlister,R.id.lister,INFLATER_ELEMENTS);
        adapter.setDropDownViewResource(R.layout.dropdownlister);
        dropdown.setAdapter(adapter);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_LONG).show();
                TASKName = taskName.getText().toString();
                Duration = duration.getText().toString();

                if (TASKName.isEmpty()) {
                    flag = true;
                    taskName.setError("Enter a Valid TaskName");
                }
                if (Duration.isEmpty()) {
                    flag = true;
                    duration.setError("Enter a Valid Duration in Minutes");
                }

                if (!flag) {
                    warning.setVisibility(View.INVISIBLE);
                    switch (String.valueOf(dropdown.getSelectedItem())) {
                        case "Quiz":
                            FormatType = "quiz";
                            break;
                        case "Lectures or Formulas":
                            FormatType = "formula";
                            break;
                    }

                    requestQueue = Volley.newRequestQueue(AdminAddTask.this);
                    stringRequest = new StringRequest(Request.Method.POST, ADD_WEEKLY_TASK_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String res = jsonObject.getString("status");

                                        if (res.equals("ok")) {
                                            onBackPressed();
                                        } else {
                                            warning.setVisibility(View.VISIBLE);
                                            warningText.setText("Unable to Create Task, please retry");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();
                            UUID uuid = UUID.randomUUID();
                            String id = uuid.toString();
                            params.put("id",id);
                            params.put("title",TASKName);
                            params.put("due",Duration);
                            params.put("type",FormatType);
                            params.put("taskid",TASKId);
                            return params;
                        }
                    };

                    requestQueue.add(stringRequest);

                } else {
                    warning.setVisibility(View.VISIBLE);
                    warningText.setText("Missing Some Fields");
                }
            }
        });

    }
}
