package edu.education.androiddevelopmentcontest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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

import edu.education.androiddevelopmentcontest.adapters.InflateAdminQuizAdapter;
import edu.education.androiddevelopmentcontest.classes.AdminQuizDetails;

public class AdminQuiz extends AppCompatActivity implements InflateAdminQuizAdapter.OnClickListener {

    private final String GET_QUIZ_URL = "http://192.168.43.89/contest/index.php/admin/getAdminQuiz";
    private final String DELETE_QUIZ_URL = "http://192.168.43.89/contest/index.php/admin/deleteAdminQuiz";

    private Bundle bundle;

    private RecyclerView recyclerView;
    private LinearLayout addQuiz;

    private String TASKId;
    private ArrayList<AdminQuizDetails> details;
    private InflateAdminQuizAdapter adapter;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;
    private JSONArray jsonArray;

    private RequestQueue deleteQueue;
    private StringRequest deleteRequest;

    @Override
    protected void onStart() {
        super.onStart();

        if (! details.isEmpty()) {
            //code to trigger the Executing code
            new FetchQuiz().execute();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_quiz);

        bundle = getIntent().getExtras();

        recyclerView = findViewById(R.id.recyclerView);
        addQuiz = findViewById(R.id.addQuiz);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminQuiz.this));

        TASKId = bundle.getString("id");
        //oast.makeText(getApplicationContext(),TASKId,Toast.LENGTH_LONG).show();

        addQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Toast",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AdminQuiz.this,AddNewQuiz.class);
                Bundle bundle = new Bundle();
                bundle.putString("id",TASKId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        new FetchQuiz().execute();
    }

    @Override
    public void deleteQuiz(int position) {
        final AdminQuizDetails quizDetails = details.get(position);
        //Toast.makeText(getApplicationContext(),"Delete" + quizDetails.getId(),Toast.LENGTH_LONG).show();
        deleteQueue = Volley.newRequestQueue(AdminQuiz.this);
        deleteRequest = new StringRequest(Request.Method.POST, DELETE_QUIZ_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            String res = object.getString("status");
                            if (res.equals("ok")) {
                                onStart();
                            } else {
                                Toast.makeText(getApplicationContext(),"Could't Delete ",Toast.LENGTH_LONG).show();
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
                params.put("id",quizDetails.getId());
                return params;
            }
        };

        deleteQueue.add(deleteRequest);
    }

    @Override
    public void editQuiz(int position) {
        AdminQuizDetails quizDetails = details.get(position);
        Intent intent = new Intent(AdminQuiz.this,EditQuiz.class);
        Bundle bundle = new Bundle();
        bundle.putString("question",quizDetails.getQuestion());
        bundle.putString("answer1",quizDetails.getOptionA());
        bundle.putString("answer2",quizDetails.getOptionB());
        bundle.putString("answer3",quizDetails.getOptionC());
        bundle.putString("answer4",quizDetails.getOptionD());
        bundle.putString("answer",quizDetails.getAnswer());
        bundle.putString("taskid",TASKId);
        bundle.putString("id",quizDetails.getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public class FetchQuiz extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            details = new ArrayList<>();

            requestQueue = Volley.newRequestQueue(AdminQuiz.this);
            stringRequest = new StringRequest(Request.Method.POST, GET_QUIZ_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                                JSONObject object = new JSONObject(response);
                                jsonArray = object.getJSONArray("quiz");
                                for (int i=0;i<jsonArray.length();i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    details.add(new AdminQuizDetails(jsonObject.getString("id"),jsonObject.getString("question"),jsonObject.getString("optionA"),jsonObject.getString("optionB"),jsonObject.getString("optionC"),jsonObject.getString("optionD"),jsonObject.getString("answer")));
                                }
                                adapter = new InflateAdminQuizAdapter(AdminQuiz.this,details);
                                adapter.setOnItemClickListener(AdminQuiz.this);
                                recyclerView.setAdapter(adapter);
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
                    params.put("id",TASKId);
                    return params;
                }
            };

            requestQueue.add(stringRequest);

            return null;
        }
    }
}
