package edu.education.androiddevelopmentcontest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import edu.education.androiddevelopmentcontest.adapters.InflateTaskAdapter;
import edu.education.androiddevelopmentcontest.classes.GetTask;

public class Home extends AppCompatActivity implements InflateTaskAdapter.OnItemClickListener {

    private final String GET_TASK_URL = "http://192.168.43.89/contest/index.php/backend/getTask";

    private String USERNAME;
    private Bundle bundle;

    private RequestQueue requestQueue;
    private StringRequest getTask;
    private JSONArray task;
    private JSONObject taskObject;

    public String id;
    public String progress;
    public String name;
    public String due;
    public String total;
    private String titles;

    private ArrayList<GetTask> taskDetails;
    private InflateTaskAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bundle = getIntent().getExtras();
        USERNAME = bundle.getString("username");

        warning = findViewById(R.id.warning);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new FetchTask().execute();
    }

    @Override
    public void onItemClick(int position) {
        GetTask details = taskDetails.get(position);
        //Toast.makeText(getApplicationContext(),details.getName() + details.username,Toast.LENGTH_LONG).show();

        Bundle bundle = new Bundle();
        bundle.putString("username",details.getUsername());
        bundle.putString("id",details.getId());

        Intent intent = new Intent(Home.this,Tasks.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public class FetchTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            taskDetails = new ArrayList<>();
            requestQueue = Volley.newRequestQueue(Home.this);
            getTask = new StringRequest(Request.Method.POST, GET_TASK_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject object = new JSONObject(response);
                                task = object.getJSONArray("task");

                                for (int i=0;i<task.length();i++) {
                                    taskObject = task.getJSONObject(i);
                                    id = taskObject.getString("id");
                                    progress = taskObject.getString("progress");
                                    name = taskObject.getString("name");
                                    due = taskObject.getString("due");
                                    total = taskObject.getString("total");
                                    titles = taskObject.getString("topics");

                                    taskDetails.add(new GetTask(id,USERNAME,progress,name,due,total,titles));
                                }
                                adapter = new InflateTaskAdapter(Home.this,taskDetails);
                                recyclerView.setAdapter(adapter);
                                adapter.setOnItemClickListener(Home.this);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.print(error.toString());
                    Toast.makeText(getApplicationContext(),"Error Occured",Toast.LENGTH_LONG).show();
                    warning.setVisibility(View.VISIBLE);
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("username",USERNAME);
                    return params;
                }
            };

            requestQueue.add(getTask);

            return null;
        }
    }
}
