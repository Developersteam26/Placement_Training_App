package edu.education.androiddevelopmentcontest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QuizAck extends AppCompatActivity {

    private TextView Title;
    private TextView Hours;
    private TextView Minutes;
    private Button attempQuiz;

    private String USERNAME;
    private String title;
    private String QuizId;
    private String sduration;
    private String sHours;
    private String sMinutes;
    private int duration;
    private int hours;
    private int minutes;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_ack);

        bundle = getIntent().getExtras();
        title = bundle.getString("title");
        sduration = bundle.getString("duration");
        QuizId = bundle.getString("quizid");
        USERNAME = bundle.getString("username");

        Title = findViewById(R.id.title);
        Hours = findViewById(R.id.hour);
        Minutes = findViewById(R.id.minutes);
        attempQuiz = findViewById(R.id.attempt);

        duration = Integer.parseInt(sduration);

        if (duration <= 60) {
            Minutes.setText(String.valueOf(duration));
        } else {
            hours = duration / 60;
            minutes = duration % 60;

            if (hours < 10) {
                sHours = "0" + String.valueOf(hours) + ":";
            } else {
                sHours = String.valueOf(hours) + ":";
            }

            if (minutes < 10) {
                sMinutes = "0" + String.valueOf(minutes);
            } else {
                sMinutes = String.valueOf(minutes);
            }

            Hours.setText(sHours);
            Minutes.setText(sMinutes);
        }

        Title.setText(title);

        attempQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent attemptNow = new Intent(QuizAck.this,AttemptQuiz.class);
                Bundle bundle = new Bundle();
                bundle.putString("quizid",QuizId);
                bundle.putString("duration",sduration);
                bundle.putString("username",USERNAME);
                attemptNow.putExtras(bundle);
                startActivity(attemptNow);
            }
        });
    }
}
