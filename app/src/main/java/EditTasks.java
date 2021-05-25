package edu.education.androiddevelopmentcontest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditTasks extends AppCompatActivity {

    private final String EDIT_TASK_URL = "http://192.168.43.89/contest/index.php/admin/editTask";

    private ImageButton calendarPicker;
    private TextView dateChosen;
    private LinearLayout addTask;
    private TextView WarningText;
    private LinearLayout Warning;
    private EditText taskName;
    private EditText topics;
    private EditText numberOfTask;

    private String TASKName;
    private String TOPICS;
    private String NumberOfTask;
    private String DateChoosen;
    private String TASKID;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    private int year;
    private int month;
    private int day;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tasks);

        calendarPicker = findViewById(R.id.datePicker);
        dateChosen = findViewById(R.id.date);
        addTask = findViewById(R.id.createTask);

        taskName = findViewById(R.id.taskName);
        topics = findViewById(R.id.topics);
        numberOfTask = findViewById(R.id.numberOfTask);
        Warning = findViewById(R.id.warning);
        WarningText = findViewById(R.id.warningText);

        bundle = getIntent().getExtras();
        TASKID = bundle.getString("id");
        taskName.setText(bundle.getString("title"));
        topics.setText(bundle.getString("topic"));
        numberOfTask.setText(bundle.getString("total"));
        dateChosen.setText(bundle.getString("due"));

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Create",Toast.LENGTH_LONG).show();
                TASKName = taskName.getText().toString();
                TOPICS = topics.getText().toString();
                NumberOfTask = numberOfTask.getText().toString();
                DateChoosen = dateChosen.getText().toString();

                boolean flag = false;

                if (TASKName.isEmpty()) {
                    flag = true;
                    taskName.setError("Please enter a Valid Task Name");
                }
                if (TOPICS.isEmpty()) {
                    flag = true;
                    topics.setError("Please enter Valid Topics");
                }
                if (NumberOfTask.isEmpty()) {
                    flag = true;
                    numberOfTask.setError("Please enter a Valid Nummber of Task");
                }
                if (DateChoosen.isEmpty()) {
                    flag = true;
                    dateChosen.setError("Please Choose a valid date");
                }

                if (!flag) {
                    Warning.setVisibility(View.GONE);
                    // Code to Create New Task
                    requestQueue = Volley.newRequestQueue(EditTasks.this);
                    stringRequest = new StringRequest(Request.Method.POST, EDIT_TASK_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                                    try {
                                        JSONObject object = new JSONObject(response);
                                        String status = object.getString("status");
                                        if (status.equals("ok")) {
                                            onBackPressed();
                                        } else {
                                            Warning.setVisibility(View.VISIBLE);
                                            WarningText.setText("Unable to Create a Task, Please Try again");
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
                            params.put("id",TASKID);
                            params.put("taskName",TASKName);
                            params.put("topic",TOPICS);
                            params.put("due",DateChoosen);
                            params.put("total",NumberOfTask);
                            return params;
                        }
                    };

                    requestQueue.add(stringRequest);

                } else {
                    Warning.setVisibility(View.VISIBLE);
                    WarningText.setText("Some fields are found Missing");
                }
            }
        });

        calendarPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditTasks.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateChosen.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
    }
}
