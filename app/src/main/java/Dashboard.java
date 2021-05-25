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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.education.androiddevelopmentcontest.adapters.InflateAdminDashboard;
import edu.education.androiddevelopmentcontest.classes.AdminTasks;

public class Dashboard extends AppCompatActivity implements InflateAdminDashboard.OnItemClickListener {

    private final String TASK_URL = "http://192.168.43.89/contest/index.php/admin/loadTasks";
    private final String DELETE_TASK_URL = "http://192.168.43.89/contest/index.php/admin/deleteTask";

    private RecyclerView recyclerView;
    private ImageButton addTask;

    private RequestQueue dashboardQueue;
    private JsonObjectRequest dashboardRequest;
    private JSONObject dashboardObject;
    private JSONArray dashboardArray;

    private RequestQueue deleteQueue;
    private StringRequest deleteRequest;

    private ArrayList<AdminTasks> dashboardList;
    private InflateAdminDashboard adapter;

    @Override
    protected void onStart() {
        super.onStart();
        // Toast.makeText(getApplicationContext(), "New", Toast.LENGTH_LONG).show();
        if (!dashboardList.isEmpty()) {
            dashboardList.clear();
            new FetchTasks().execute();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        addTask = findViewById(R.id.addTask);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this,AddTask.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
        recyclerView.setHasFixedSize(true);

        new FetchTasks().execute();
    }

    @Override
    public void onItemClick(int position) {
        AdminTasks adminTasks = dashboardList.get(position);
        //Toast.makeText(getApplicationContext(),"Layout Click" + adminTasks.getName(),Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Dashboard.this, edu.education.androiddevelopmentcontest.AdminTasks.class);
        Bundle bundle = new Bundle();
        bundle.putString("id",adminTasks.getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onButtonClick(int position) {
        AdminTasks adminTasks = dashboardList.get(position);
        final String TaskId = adminTasks.getId();
        //Toast.makeText(getApplicationContext(),"Button Click" + adminTasks.getTopic(),Toast.LENGTH_LONG).show();
        deleteQueue = Volley.newRequestQueue(Dashboard.this);
        deleteRequest = new StringRequest(Request.Method.POST, DELETE_TASK_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject deleteObject = new JSONObject(response);
                            String status = deleteObject.getString("status");

                            if (status.equals("ok")) {
                                Toast.makeText(getApplicationContext(),"Successfully Deleted",Toast.LENGTH_LONG).show();
                                refreshFrame();
                            } else {
                                Toast.makeText(getApplicationContext(),"Deleting Task failed to Complete" + TaskId,Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error" + error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("taskid",TaskId);
                return params;
            }
        };

        deleteQueue.add(deleteRequest);
    }

    @Override
    public void onEditClick(int adapterPosition) {
        Toast.makeText(getApplicationContext(),"Edit",Toast.LENGTH_LONG).show();
        AdminTasks adminTasks = dashboardList.get(adapterPosition);

        Intent intent = new Intent(Dashboard.this,EditTasks.class);
        Bundle bundle = new Bundle();
        bundle.putString("id",adminTasks.getId());
        bundle.putString("title",adminTasks.getName());
        bundle.putString("topic",adminTasks.getTopic());
        bundle.putString("due",adminTasks.getDue());
        bundle.putString("total",adminTasks.getTotal());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void refreshFrame() {
        if (!dashboardList.isEmpty()) {
            dashboardList.clear();
            new FetchTasks().execute();
        }
    }

    public class FetchTasks extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            dashboardList = new ArrayList<>();

            dashboardQueue = Volley.newRequestQueue(Dashboard.this);
            dashboardRequest = new JsonObjectRequest(Request.Method.GET, TASK_URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                dashboardArray = response.getJSONArray("task");
                                for (int i=0;i<dashboardArray.length();i++) {
                                    dashboardObject = dashboardArray.getJSONObject(i);
                                    dashboardList.add(new AdminTasks(dashboardObject.getString("id"),dashboardObject.getString("name"),dashboardObject.getString("due"),dashboardObject.getString("total"),dashboardObject.getString("topic")));
                                }
                                adapter = new InflateAdminDashboard(Dashboard.this,dashboardList);
                                recyclerView.setAdapter(adapter);
                                adapter.setOnItemClickListener(Dashboard.this);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                }
            });

            dashboardQueue.add(dashboardRequest);
            return null;
        }
    }
}
