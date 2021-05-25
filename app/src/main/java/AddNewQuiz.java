package edu.education.androiddevelopmentcontest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class AddNewQuiz extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private final String CREATE_QUIZ_URL = "http://192.168.43.89/contest/index.php/admin/createQuiz";

    private Bundle bundle;

    private String TASKId;

    private Button reset;
    private RadioGroup quizGroup;
    private RadioButton optionA;
    private RadioButton optionB;
    private RadioButton optionC;
    private RadioButton optionD;
    private EditText answerOne;
    private EditText answerTwo;
    private EditText answerThree;
    private EditText answerFour;
    private EditText question;
    private TextView answer;
    private LinearLayout addQuiz;
    private LinearLayout Warning;
    private TextView WarningText;
    private boolean flag = false;

    private RequestQueue quizQueue;
    private StringRequest quizRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_quiz);

        reset = findViewById(R.id.reset);
        quizGroup = findViewById(R.id.quizgroup);
        optionA = findViewById(R.id.optionOne);
        optionB = findViewById(R.id.optionTwo);
        optionC = findViewById(R.id.optionThree);
        optionD = findViewById(R.id.optionFour);
        answerOne = findViewById(R.id.answerOne);
        answerTwo = findViewById(R.id.answerTwo);
        answerThree = findViewById(R.id.answerThree);
        answerFour = findViewById(R.id.answerFour);
        question = findViewById(R.id.question);
        answer = findViewById(R.id.answer);
        addQuiz = findViewById(R.id.addQuiz);
        Warning = findViewById(R.id.warning);
        WarningText = findViewById(R.id.warningText);

        bundle = getIntent().getExtras();
        TASKId = bundle.getString("id");

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code to disable all the responses
                optionA.setChecked(false);
                optionB.setChecked(false);
                optionC.setChecked(false);
                optionD.setChecked(false);
                answer.setText("Please select Only one answer from the list");
            }
        });

        addQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;
                Warning.setVisibility(View.GONE);
                boolean selected = false;

                final String Question = question.getText().toString();
                final String AnswerOne = answerOne.getText().toString();
                final String AnswerTwo = answerTwo.getText().toString();
                final String AnswerThree = answerThree.getText().toString();
                final String AnswerFour = answerFour.getText().toString();

                if (Question.isEmpty() || AnswerOne.isEmpty() || AnswerTwo.isEmpty() || AnswerThree.isEmpty() || AnswerFour.isEmpty()) {
                    Warning.setVisibility(View.VISIBLE);
                    WarningText.setText("Found Something Missing here");
                    flag = true;
                }

                if (optionA.isChecked() || optionB.isChecked() || optionC.isChecked() || optionD.isChecked()) {
                    selected = true;
                }

                if (flag && !selected) {
                    Warning.setVisibility(View.VISIBLE);
                    WarningText.setText("Missing out some fields. Please ensure you select an option from the choice before submit");
                }

                if (!flag && selected) {
                    //Toast.makeText(getApplicationContext(),"T",Toast.LENGTH_LONG).show();
                    quizQueue = Volley.newRequestQueue(AddNewQuiz.this);
                    quizRequest = new StringRequest(Request.Method.POST, CREATE_QUIZ_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String res = jsonObject.getString("status");
                                        if (res.equals("ok")) {
                                            onBackPressed();
                                        } else {
                                            Warning.setVisibility(View.VISIBLE);
                                            WarningText.setText("Failed to Create the Quiz Question, Please try again later");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Warning.setVisibility(View.VISIBLE);
                            WarningText.setText("Error Occured while Creating the Quiz");
                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();
                            params.put("id",TASKId);
                            params.put("question",Question);
                            params.put("option1",AnswerOne);
                            params.put("option2",AnswerTwo);
                            params.put("option3",AnswerThree);
                            params.put("option4",AnswerFour);
                            params.put("answer",answer.getText().toString());
                            return params;
                        }
                    };

                    quizQueue.add(quizRequest);
                }

            }
        });

        optionA.setOnCheckedChangeListener(this);
        optionB.setOnCheckedChangeListener(this);
        optionC.setOnCheckedChangeListener(this);
        optionD.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.optionOne && isChecked) {
            if (optionB.isChecked() || optionC.isChecked() || optionD.isChecked()) {
                optionA.setChecked(true);
                optionB.setChecked(false);
                optionC.setChecked(false);
                optionD.setChecked(false);

                String answerResponse = answerOne.getText().toString();
                answer.setText(answerResponse);

                answerOne.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        answer.setText(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        }
        else if (buttonView.getId() == R.id.optionTwo && isChecked) {
            if (optionA.isChecked() || optionC.isChecked() || optionD.isChecked()) {
                optionA.setChecked(false);
                optionB.setChecked(true);
                optionC.setChecked(false);
                optionD.setChecked(false);

                String answerResponse = answerTwo.getText().toString();
                answer.setText(answerResponse);

                answerTwo.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        answer.setText(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        } else if (buttonView.getId() == R.id.optionThree && isChecked) {
            if (optionA.isChecked() || optionB.isChecked() || optionD.isChecked()) {
                optionA.setChecked(false);
                optionB.setChecked(false);
                optionC.setChecked(true);
                optionD.setChecked(false);

                String answerResponse = answerThree.getText().toString();
                answer.setText(answerResponse);

                answerThree.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        answer.setText(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        } else if (buttonView.getId() == R.id.optionFour && isChecked) {
            if (optionA.isChecked() || optionB.isChecked() || optionC.isChecked()) {
                optionA.setChecked(false);
                optionB.setChecked(false);
                optionC.setChecked(false);
                optionD.setChecked(true);

                String answerResponse = answerFour.getText().toString();
                answer.setText(answerResponse);

                answerFour.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        answer.setText(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        }
    }
}
