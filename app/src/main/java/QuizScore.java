package edu.education.androiddevelopmentcontest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
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

import edu.education.androiddevelopmentcontest.adapters.InflateQuizAnswerAdapter;
import edu.education.androiddevelopmentcontest.classes.GetSolution;

public class QuizScore extends AppCompatActivity {

    private final String GET_SOLUTION_URL = "http://192.168.43.89/contest/index.php/backend/getAnsweredResponse";

    private TextView acquiredMarks;
    private TextView totalMarks;
    private RecyclerView recyclerView;
    private InflateQuizAnswerAdapter adapter;
    private ArrayList<GetSolution> details;

    private Bundle bundle;

    private String ACQUIREDMARKS;
    private String TOTALMARKS;
    private String USERNAME;
    private String QUIZID;

    private JSONObject object;
    private JSONArray jsonArray;
    private RequestQueue requestQueue;
    private StringRequest questionRequest;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_score);

        bundle = getIntent().getExtras();
        ACQUIREDMARKS = bundle.getString("marks");
        TOTALMARKS = bundle.getString("total");
        USERNAME = bundle.getString("username");
        QUIZID = bundle.getString("quizid");

        acquiredMarks = findViewById(R.id.marksAcquired);
        totalMarks = findViewById(R.id.totalMarks);

        acquiredMarks.setText(ACQUIREDMARKS);
        totalMarks.setText(TOTALMARKS);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        details = new ArrayList<>();

        new FetchAnswer().execute();
    }

    public class FetchAnswer extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            requestQueue = Volley.newRequestQueue(QuizScore.this);
            questionRequest = new StringRequest(Request.Method.POST, GET_SOLUTION_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                jsonArray = jsonObject.getJSONArray("answer");

                                for (int i=0;i<jsonArray.length();i++) {
                                    object = jsonArray.getJSONObject(i);

                                    details.add(new GetSolution(object.getString("id"),object.getString("question"),object.getString("answer"),object.getString("response")));
                                }

                                adapter = new InflateQuizAnswerAdapter(QuizScore.this,details);
                                recyclerView.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    System.out.println(error.toString());
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("username",USERNAME);
                    params.put("id",QUIZID);
                    return params;
                }
            };

            requestQueue.add(questionRequest);

            return null;
        }
    }
}
