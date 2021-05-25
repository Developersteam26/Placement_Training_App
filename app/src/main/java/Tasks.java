package edu.education.androiddevelopmentcontest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import edu.education.androiddevelopmentcontest.adapters.InflateWeeklyTaskAdapter;
import edu.education.androiddevelopmentcontest.classes.WeeklyTask;

public class Tasks extends AppCompatActivity implements InflateWeeklyTaskAdapter.OnItemClickListener {

    private final String GET_WEEKLY_TASK_URL = "http://192.168.43.89/contest/index.php/backend/getWeeklyTask";
    private final String IS_USER_ATTENDED_QUIZ = "http://192.168.43.89/contest/index.php/backend/isQuizCompleted";

    private String USERNAME;
    private String WEEKCODE;
    private Bundle bundle;

    private RecyclerView recyclerView;
    private ArrayList<WeeklyTask> weeklyDetails;
    private InflateWeeklyTaskAdapter adapter;

    private RequestQueue requestQueue;
    private StringRequest weeklyRequest;
    private JSONArray weeklyArray;
    private JSONObject weeklyObject;

    private RequestQueue responseQueue;
    private StringRequest responseRequest;
    private JSONArray responseArray;
    private JSONObject responseObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        bundle = getIntent().getExtras();

        USERNAME = bundle.getString("username");
        WEEKCODE = bundle.getString("id");
        Toast.makeText(getApplicationContext(),WEEKCODE,Toast.LENGTH_LONG).show();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new FetchWeeklyTask().execute();
    }

    @Override
    public void onItemClick(int position) {
        final WeeklyTask weeklyTask = weeklyDetails.get(position);

        //Toast.makeText(getApplicationContext(),weeklyTask.getType(),Toast.LENGTH_LONG).show();
        String type = weeklyTask.getType();

        switch (type) {
            case "quiz":
                //code for checking the User has attempted the Test or Not.
                responseQueue = Volley.newRequestQueue(Tasks.this);
                responseRequest = new StringRequest(Request.Method.POST, IS_USER_ATTENDED_QUIZ,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject object = new JSONObject(response);
                                    responseArray = object.getJSONArray("result");
                                    responseObject = responseArray.getJSONObject(0);
                                    //Toast.makeText(getApplicationContext(),responseObject.getString("status"),Toast.LENGTH_LONG).show();
                                    String status = responseObject.getString("status");
                                    Toast.makeText(getApplicationContext(),status,Toast.LENGTH_LONG).show();
                                    if (status.equals("ok")) {
                                        Intent intent = new Intent(Tasks.this,QuizScore.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("username",USERNAME);
                                        bundle.putString("quizid",weeklyTask.getId());
                                        bundle.putString("marks",responseObject.getString("mark"));
                                        bundle.putString("total",responseObject.getString("total"));
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    } else {
                                        Intent nextActivity;
                                        Bundle bundle;
                                        nextActivity = new Intent(Tasks.this,QuizAck.class);
                                        bundle = new Bundle();
                                        bundle.putString("quizid",weeklyTask.getId());
                                        bundle.putString("username",weeklyTask.getUsername());
                                        bundle.putString("progress",weeklyTask.getProgress());
                                        bundle.putString("duration",weeklyTask.getDuration());
                                        bundle.putString("title",weeklyTask.getTitle());
                                        nextActivity.putExtras(bundle);
                                        startActivity(nextActivity);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("quizid",weeklyTask.getId());
                        params.put("username",USERNAME);
                        return params;
                    }
                };
                responseQueue.add(responseRequest);

                break;
            case "formula":
                Intent intent = new Intent(Tasks.this,FormulasLectures.class);
                Bundle bundle = new Bundle();
                bundle.putString("username",USERNAME);
                bundle.putString("taskid",weeklyTask.getId());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            default:
                Toast.makeText(getApplicationContext(),type,Toast.LENGTH_LONG).show();
        }
    }

    public class FetchWeeklyTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            weeklyDetails = new ArrayList<>();

            requestQueue = Volley.newRequestQueue(Tasks.this);
            weeklyRequest = new StringRequest(Request.Method.POST, GET_WEEKLY_TASK_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                weeklyArray = object.getJSONArray("week");

                                for (int i=0;i<weeklyArray.length();i++) {
                                    weeklyObject = weeklyArray.getJSONObject(i);
                                    String id = weeklyObject.getString("id");
                                    String username = weeklyObject.getString("username");
                                    String duration = weeklyObject.getString("duration");
                                    String progress = weeklyObject.getString("progress");
                                    String taskid = weeklyObject.getString("taskid");
                                    String type = weeklyObject.getString("type");
                                    String title = weeklyObject.getString("title");

                                    weeklyDetails.add(new WeeklyTask(id,username,duration,progress,taskid,type,title));
                                }

                                adapter = new InflateWeeklyTaskAdapter(Tasks.this,weeklyDetails);
                                recyclerView.setAdapter(adapter);
                                adapter.setOnItemClickListener(Tasks.this);

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
                    params.put("username",USERNAME);
                    params.put("weekcode",WEEKCODE);
                    return params;
                }
            };

            requestQueue.add(weeklyRequest);

            return null;
        }
    }

    public class GetJSON {
        public String id;
        public String username;
        public String mark;
        public String total;
        public String status;

        public GetJSON(String id, String username, String mark, String total, String status) {
            this.id = id;
            this.username = username;
            this.mark = mark;
            this.total = total;
            this.status = status;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
