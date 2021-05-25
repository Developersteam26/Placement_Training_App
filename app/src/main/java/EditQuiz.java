package edu.education.androiddevelopmentcontest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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

public class EditQuiz extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, TextWatcher {

    private final String EDIT_QUIZ_URL = "http://192.168.43.89/contest/index.php/admin/editQuiz";

    private RadioButton optionA;
    private RadioButton optionB;
    private RadioButton optionC;
    private RadioButton  optionD;
    private TextView question;
    private EditText answerA;
    private EditText answerB;
    private EditText answerC;
    private EditText answerD;
    private TextView answer;
    private LinearLayout editQuiz;
    private LinearLayout warning;
    private TextView warningText;
    private Button reset;

    private Bundle bundle;

    private String TASKId;
    private String QUIXId;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_quiz);

        optionA = findViewById(R.id.optionOne);
        optionB = findViewById(R.id.optionTwo);
        optionC = findViewById(R.id.optionThree);
        optionD = findViewById(R.id.optionFour);
        question = findViewById(R.id.question);
        answerA = findViewById(R.id.answerOne);
        answerB = findViewById(R.id.answerTwo);
        answerC = findViewById(R.id.answerThree);
        answerD = findViewById(R.id.answerFour);
        answer = findViewById(R.id.answer);
        editQuiz = findViewById(R.id.editQuiz);
        warning = findViewById(R.id.warning);
        warningText = findViewById(R.id.warningText);
        reset = findViewById(R.id.reset);

        bundle = getIntent().getExtras();
        question.setText(bundle.getString("question"));
        answerA.setText(bundle.getString("answer1"));
        answerB.setText(bundle.getString("answer2"));
        answerC.setText(bundle.getString("answer3"));
        answerD.setText(bundle.getString("answer4"));
        answer.setText(bundle.getString("answer"));
        TASKId = bundle.getString("taskid");
        QUIXId = bundle.getString("id");

        String ANSWER = bundle.getString("answer");
        if (ANSWER.equals(bundle.getString("answer1"))) {
            optionA.setChecked(true);
        } else if (ANSWER.equals(bundle.getString("answer2"))) {
            optionB.setChecked(true);
        } else if (ANSWER.equals(bundle.getString("answer3"))) {
            optionC.setChecked(true);
        } else if (ANSWER.equals(bundle.getString("answer4"))) {
            optionD.setChecked(true);
        }

        optionA.setOnCheckedChangeListener(this);
        optionB.setOnCheckedChangeListener(this);
        optionC.setOnCheckedChangeListener(this);
        optionD.setOnCheckedChangeListener(this);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionA.setChecked(false);
                optionB.setChecked(false);
                optionC.setChecked(false);
                optionD.setChecked(false);
                answer.setText("Please select an Option from the above list");
            }
        });

        editQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Question = question.getText().toString();
                String AnswerA = answerA.getText().toString();
                String AnswerB = answerB.getText().toString();
                String AnswerC = answerC.getText().toString();
                String AnswerD = answerD.getText().toString();

                boolean isFilled = false;
                boolean isSelected = false;

                if (Question.isEmpty() || AnswerA.isEmpty() || AnswerB.isEmpty() || AnswerC.isEmpty() || AnswerD.isEmpty()) {
                    isFilled = true;
                    warning.setVisibility(View.VISIBLE);
                    warningText.setText("Some Fields are found missing");
                }

                if (optionA.isChecked() || optionB.isChecked() || optionC.isChecked() || optionD.isChecked()) {
                    isSelected = true;
                } else {
                    warning.setVisibility(View.VISIBLE);
                    warningText.setText("Please select an Option from the choices");
                }

                if (isFilled && !isSelected) {
                    warning.setVisibility(View.VISIBLE);
                    warningText.setText("Some Fields are found missing and Please select an option from the choice");
                } else {
                    //code for editing the code in the database
                    requestQueue = Volley.newRequestQueue(EditQuiz.this);
                    stringRequest = new StringRequest(Request.Method.POST, EDIT_QUIZ_URL,
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
                                            warningText.setText("Editing Quiz failed");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            warning.setVisibility(View.VISIBLE);
                            warningText.setText("Error occured while Editing the Quiz");
                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();
                            params.put("question",question.getText().toString());
                            params.put("optionA",answerA.getText().toString());
                            params.put("optionB",answerB.getText().toString());
                            params.put("optionC",answerC.getText().toString());
                            params.put("optionD",answerD.getText().toString());
                            params.put("answer",answer.getText().toString());
                            params.put("taskid",TASKId);
                            params.put("id",QUIXId);
                            return params;
                        }
                    };

                    requestQueue.add(stringRequest);
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.optionOne && isChecked) {
            optionA.setChecked(true);
            optionB.setChecked(false);
            optionC.setChecked(false);
            optionD.setChecked(false);
            answer.setText(answerA.getText().toString());

            answerA.addTextChangedListener(this);
        } else if (buttonView.getId() == R.id.optionTwo && isChecked) {
            optionA.setChecked(false);
            optionB.setChecked(true);
            optionC.setChecked(false);
            optionD.setChecked(false);
            answer.setText(answerB.getText().toString());

            answerB.addTextChangedListener(this);
        } else if (buttonView.getId() == R.id.optionThree && isChecked) {
            optionA.setChecked(false);
            optionB.setChecked(false);
            optionC.setChecked(true);
            optionD.setChecked(false);
            answer.setText(answerC.getText().toString());

            answerC.addTextChangedListener(this);
        } else if (buttonView.getId() == R.id.optionFour && isChecked) {
            optionA.setChecked(false);
            optionB.setChecked(false);
            optionC.setChecked(false);
            optionD.setChecked(true);
            answer.setText(answerD.getText().toString());

            answerD.addTextChangedListener(this);
        }
    }

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
}
