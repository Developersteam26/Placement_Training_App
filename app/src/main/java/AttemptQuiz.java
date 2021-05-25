package edu.education.androiddevelopmentcontest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
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

import edu.education.androiddevelopmentcontest.adapters.InflateQuizAttempAdapter;
import edu.education.androiddevelopmentcontest.classes.QuizQuestions;
import edu.education.androiddevelopmentcontest.classes.Responser;

public class AttemptQuiz extends AppCompatActivity implements InflateQuizAttempAdapter.OnRadioChange {

    public final String GET_QUIZ_URL = "http://192.168.43.89/contest/index.php/backend/getQuizQuestions";
    public final String UPLOAD_RESPONSE_URL = "http://192.168.43.89/contest/index.php/backend/uploadQuizResponse";
    public final String MARK_PROGRESS_URL = "http://192.168.43.89/contest/index.php/backend/markQuizProgress";

    private String USERNAME;
    private String QUIZId;
    private String DURATION;
    private Bundle bundle;

    private String taskid;
    private String id;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String answer;
    private int duration;
    private int Marks = 0;
    private boolean flag = false;
    private boolean submitted = false;

    private long startTime = SystemClock.uptimeMillis();
    private long timeInMilli = 0L;
    private Handler handler = new Handler();

    private TextView TotalQuestions;
    private TextView AttemptedQuestions;
    private TextView TotalTime;
    private TextView CurrentTimer;
    private TextView Seconds;
    private Button submitQuiz;

    private ArrayList<Integer> counter = new ArrayList<>();
    private ArrayList<Responser> responses = new ArrayList<>();
    private ArrayList<QuizQuestions> quizDetails;
    private InflateQuizAttempAdapter adapter;
    private RecyclerView recyclerView;

    private RequestQueue requestQueue;
    private StringRequest quizRequest;
    private JSONArray quizArray;
    private JSONObject quizObject;

    private RequestQueue submitResponseQueue;
    private StringRequest submitResponseRequest;

    private RequestQueue markProgressQueue;
    private StringRequest markProgressRequest;

    private int totalQuestions;

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"You are not allowed to Go back. Complete the Quiz to Go.",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attempt_quiz);

        bundle = getIntent().getExtras();

        handler.postDelayed(runnable,0);

        USERNAME = bundle.getString("username");
        QUIZId = bundle.getString("quizid");
        DURATION = bundle.getString("duration");

        TotalQuestions = findViewById(R.id.totalQuestion);
        AttemptedQuestions = findViewById(R.id.attempted);
        TotalTime = findViewById(R.id.totalTime);
        CurrentTimer = findViewById(R.id.timer);
        Seconds = findViewById(R.id.seconds);
        submitQuiz = findViewById(R.id.submit);

        quizDetails = new ArrayList<>();

        String Duration;

        duration = Integer.parseInt(DURATION);
        if (duration <= 60) {
            Duration = "00:" + String.valueOf(duration);
        } else {
            int hour;
            int minutes;
            String HOUR;
            String MINUTES;

            hour = duration / 60;
            minutes = duration % 60;

            if (hour < 10) {
                HOUR = "0" + String.valueOf(hour);
            } else {
                HOUR = String.valueOf(hour);
            }

            if (minutes < 10) {
                MINUTES = "0" + String.valueOf(minutes);
            } else {
                MINUTES = String.valueOf(minutes);
            }
            Duration = "/ " + HOUR + ":" + MINUTES;
        }

        TotalTime.setText(Duration);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        submitQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!submitted) {
                    handler.removeCallbacks(runnable);
                    EvaluateQuiz();
                }
                else {
                    //Toast.makeText(getApplicationContext(),"View Result",Toast.LENGTH_SHORT).show();
                    for (int i=0;i<responses.size();i++) {
                        submitResponseQueue = Volley.newRequestQueue(AttemptQuiz.this);
                        final int finalI = i;
                        submitResponseRequest = new StringRequest(Request.Method.POST, UPLOAD_RESPONSE_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
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
                                params.put("username",USERNAME);
                                params.put("id",responses.get(finalI).getId());
                                params.put("response",responses.get(finalI).getResponse());
                                params.put("answer",responses.get(finalI).getAnswer());
                                params.put("quizid",quizDetails.get(finalI).getTaskid());
                                return params;
                            }
                        };
                        submitResponseQueue.add(submitResponseRequest);
                    }

                    markProgress();

                    Intent viewScore = new Intent(AttemptQuiz.this,QuizScore.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username",USERNAME);
                    bundle.putString("quizid",quizDetails.get(0).getTaskid());
                    bundle.putString("marks", String.valueOf(Marks));
                    bundle.putString("total", String.valueOf(responses.size()));
                    Toast.makeText(getApplicationContext(),responses.get(0).getId(),Toast.LENGTH_LONG).show();
                    viewScore.putExtras(bundle);
                    startActivity(viewScore);
                }
            }
        });

        new FetchQuizQuestions().execute();

    }

    private void markProgress() {
        Toast.makeText(getApplicationContext(),"in",Toast.LENGTH_LONG).show();
        markProgressQueue = Volley.newRequestQueue(AttemptQuiz.this);
        markProgressRequest = new StringRequest(Request.Method.POST, MARK_PROGRESS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error in Marking Progress",Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("username",USERNAME);
                params.put("quizid",quizDetails.get(0).getTaskid());
                return params;
            }
        };
        markProgressQueue.add(markProgressRequest);
        return;
    }

    @Override
    public void onRadio(int position, String answers) {
        //Toast.makeText(getApplicationContext(),position + answer,Toast.LENGTH_LONG).show();
        //QuizQuestions quizQuestions = quizDetails.get(position);
        Responser defaultResponse = responses.get(position);
        String actualAnswer = defaultResponse.getAnswer();
        defaultResponse.setResponse(answers);
        //Toast.makeText(getApplicationContext(),defaultResponse.getResponse(),Toast.LENGTH_LONG).show();
        responses.set(position,defaultResponse);
        //Toast.makeText(getApplicationContext(),responses.get(position).getAnswer() + ":" + responses.get(position).getResponse(),Toast.LENGTH_LONG).show();

        if (!counter.contains(position)) {
            counter.add(position);
        }

        AttemptedQuestions.setText(String.valueOf(counter.size()));
    }

    private void EvaluateQuiz() {
        Responser responser;
        for (int i=0;i<responses.size();i++) {
            responser = responses.get(i);
            if (responser.getResponse().equals(responser.getAnswer())) {
                Marks++;
            }
        }

        Toast.makeText(getApplicationContext(),String.valueOf(Marks),Toast.LENGTH_SHORT).show();
        submitted = true;
        submitQuiz.setText("View Result");
        return;
    }

    public class FetchQuizQuestions extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            quizDetails = new ArrayList<>();
            requestQueue = Volley.newRequestQueue(AttemptQuiz.this);
            quizRequest = new StringRequest(Request.Method.POST, GET_QUIZ_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                quizArray = jsonObject.getJSONArray("quiz");

                                for (int i=0;i<quizArray.length();i++) {
                                    quizObject = quizArray.getJSONObject(i);
                                    taskid = quizObject.getString("quizid");
                                    id = quizObject.getString("id");
                                    question = quizObject.getString("question");
                                    optionA = quizObject.getString("optionA");
                                    optionB = quizObject.getString("optionB");
                                    optionC = quizObject.getString("optionC");
                                    optionD = quizObject.getString("optionD");
                                    answer = quizObject.getString("answer");

                                    quizDetails.add(new QuizQuestions(taskid,id,question,optionA,optionB,optionC,optionD,answer));
                                    responses.add(new Responser("null",answer,id));
                                }

                                totalQuestions = responses.size();

                                TotalQuestions.setText(String.valueOf(totalQuestions));

                                adapter = new InflateQuizAttempAdapter(AttemptQuiz.this,quizDetails);
                                recyclerView.setAdapter(adapter);
                                adapter.setOnItemClickListener(AttemptQuiz.this);
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
                    params.put("quizId",QUIZId);
                    return params;
                }
            };

            requestQueue.add(quizRequest);

            return null;
        }
    }

    private Runnable runnable = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void run() {
            timeInMilli = SystemClock.uptimeMillis() - startTime;

            Long updatedTime = timeInMilli;
            int time = (int) (updatedTime / (1000 * 60));
            int secs = (int) (updatedTime / (1000));
            int hour = time / 60;
            int minutes = time % 60;
            String HOUR,MINUTES,SECONDS;
            //Toast.makeText(getApplicationContext(),String.valueOf(duration-time),Toast.LENGTH_SHORT).show();
            if (duration - time < 5) {
                Seconds.setTextColor(getColor(R.color.red));
                CurrentTimer.setTextColor(getColor(R.color.red));
            }

            if (duration - time < 2) {
                if (!flag) {
                    flag = true;
                    blink();
                }
            }

            if (time == duration) {
                handler.removeCallbacks(runnable);
                CurrentTimer.setText("TIME OUT");
                Seconds.setVisibility(View.INVISIBLE);
                EvaluateQuiz();
                return;
            }

            if (hour < 10) {
                HOUR = "0" + String.valueOf(hour);
            } else {
                HOUR = String.valueOf(hour);
            }
            if (minutes < 10) {
                MINUTES = ":0" + String.valueOf(minutes);
            } else {
                MINUTES = ":" + String.valueOf(minutes);
            }
            if (secs < 10) {
                SECONDS = ": 0" + String.valueOf(secs % 60);
            } else {
                SECONDS = ": " + String.valueOf(secs % 60);
            }
            CurrentTimer.setText(HOUR + MINUTES);
            Seconds.setText(SECONDS);
            handler.postDelayed(this,0);
        }
    };

    private void blink() {
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(450);
        animation.setStartOffset(20);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(Animation.INFINITE);
        CurrentTimer.startAnimation(animation);
        Seconds.startAnimation(animation);
    }
}
