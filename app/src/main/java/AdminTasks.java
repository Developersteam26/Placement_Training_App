package edu.education.androiddevelopmentcontest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.education.androiddevelopmentcontest.adapters.InflateAdminTaskAdapter;
import edu.education.androiddevelopmentcontest.classes.ActualTasks;

public class AdminTasks extends AppCompatActivity implements InflateAdminTaskAdapter.OnItemClickListener {

    private final String GET_TASKS_URL = "http://192.168.43.89/contest/index.php/admin/getWeeklyTask";
    private final String DELETE_TASKS_URL = "http://192.168.43.89/contest/index.php/admin/deleteWeeklyTask";

    private ImageButton AdminAddTask;
    private RecyclerView recyclerView;

    private Bundle bundle;

    private String TASKID;
    private ArrayList<ActualTasks> taskList;
    private InflateAdminTaskAdapter adapter;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;
    private JSONArray jsonArray;

    private RequestQueue deleteQueue;
    private StringRequest deleteRequest;

    @Override
    protected void onStart() {
        super.onStart();

        if (!taskList.isEmpty()) {
            taskList.clear();
            new FetchActualTask().execute();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tasks);

        bundle = getIntent().getExtras();

        TASKID = bundle.getString("id");
        //Toast.makeText(getApplicationContext(),TASKID,Toast.LENGTH_LONG).show();

        AdminAddTask = findViewById(R.id.addTask);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminTasks.this));
        recyclerView.setHasFixedSize(true);

        AdminAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminTasks.this,AdminAddTask.class);
                Bundle bundle = new Bundle();
                bundle.putString("id",TASKID);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        new FetchActualTask().execute();
    }

    @Override
    public void onLayoutClick(int position) {
        ActualTasks tasks = taskList.get(position);
        String type = tasks.getType();

        switch (type) {
            case "quiz":
                //Toast.makeText(getApplicationContext(),"Quix" + tasks.getTaskid(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AdminTasks.this,AdminQuiz.class);
                Bundle bundle = new Bundle();
                bundle.putString("id",tasks.getTaskid());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case "formula":
                //Toast.makeText(getApplicationContext(),"Formula",Toast.LENGTH_LONG).show();
                Intent adminFormula = new Intent(AdminTasks.this, AdminReferences.class);
                Bundle formulaBundle = new Bundle();
                formulaBundle.putString("id",tasks.getTaskid());
                adminFormula.putExtras(formulaBundle);
                startActivity(adminFormula);
                break;
        }
    }

    @Override
    public void onEditClick(int position) {
        ActualTasks tasks = taskList.get(position);
        Intent intent = new Intent(AdminTasks.this,EditWeeklyTask.class);
        Bundle bundle = new Bundle();
        bundle.putString("id",tasks.getTaskid());
        bundle.putString("title",tasks.getTitle());
        bundle.putString("due",tasks.getDuration());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {
        final ActualTasks tasks = taskList.get(position);

        deleteQueue = Volley.newRequestQueue(AdminTasks.this);
        deleteRequest = new StringRequest(Request.Method.POST, DELETE_TASKS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            String res = object.getString("status");
                            if (res.equals("ok")) {
                                if (!taskList.isEmpty()) {
                                    /*taskList.clear();
                                    new FetchActualTask().execute();*/
                                    onStart();
                                } else {
                                    Toast.makeText(getApplicationContext(),"Couldn't Delete",Toast.LENGTH_LONG).show();
                                }
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
                params.put("id",tasks.getTaskid());
                return params;
            }
        };

        deleteQueue.add(deleteRequest);
    }

    public class FetchActualTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            taskList = new ArrayList<>();

            requestQueue = Volley.newRequestQueue(AdminTasks.this);
            stringRequest = new StringRequest(Request.Method.POST, GET_TASKS_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                jsonArray = object.getJSONArray("task");
                                for (int i=0;i<jsonArray.length();i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    //adapter = new InflateAdminTaskAdapter(new ActualTasks(object.getString("id"),object.getString("title"),object.getString("due"),object.getString("type")));
                                    taskList.add(new ActualTasks(jsonObject.getString("id"),jsonObject.getString("title"),jsonObject.getString("due"),jsonObject.getString("type")));
                                }

                                adapter = new InflateAdminTaskAdapter(AdminTasks.this,taskList);
                                recyclerView.setAdapter(adapter);
                                adapter.setOnItemClickListener(AdminTasks.this);
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
                    return params;
                }
            };

            requestQueue.add(stringRequest);
            return null;
        }
    }
}
