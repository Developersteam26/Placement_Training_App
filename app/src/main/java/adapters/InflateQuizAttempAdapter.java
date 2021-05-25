package edu.education.androiddevelopmentcontest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.education.androiddevelopmentcontest.R;
import edu.education.androiddevelopmentcontest.classes.QuizQuestions;

public class InflateQuizAttempAdapter extends RecyclerView.Adapter<InflateQuizAttempAdapter.QuizAttempHolder> {

    public Context context;
    public ArrayList<QuizQuestions> details;
    private OnRadioChange change;

    public interface OnRadioChange {
        void onRadio(int position, String answer);
    }

    public void setOnItemClickListener(OnRadioChange change) {
        this.change = change;
    }

    public InflateQuizAttempAdapter(Context context,ArrayList<QuizQuestions> details) {
        this.context = context;
        this.details = details;
    }

    @NonNull
    @Override
    public QuizAttempHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quizquestions,parent,false);
        return new QuizAttempHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizAttempHolder holder, int position) {
        QuizQuestions  quizQuestions = details.get(position);

        String question = quizQuestions.getQuestion();
        String option1 = quizQuestions.getOptionA();
        String option2 = quizQuestions.getOptionB();
        String option3 = quizQuestions.getOptionC();
        String option4 = quizQuestions.getOptionD();
        int total = details.size();
        String totalQ = String.valueOf(total);
        String current = String.valueOf(position + 1);

        holder.Question.setText(question);
        holder.optionA.setText(option1);
        holder.optionB.setText(option2);
        holder.optionC.setText(option3);
        holder.optionD.setText(option4);
        holder.totalQuestion.setText(totalQ);
        holder.currentQuestion.setText(current);
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class QuizAttempHolder extends RecyclerView.ViewHolder {

        public TextView currentQuestion;
        public TextView totalQuestion;
        public TextView Question;
        public RadioButton optionA;
        public RadioButton optionB;
        public RadioButton optionC;
        public RadioButton optionD;
        public RadioGroup options;

        public QuizAttempHolder(@NonNull final View itemView) {
            super(itemView);

            currentQuestion = itemView.findViewById(R.id.currentQuestion);
            totalQuestion = itemView.findViewById(R.id.totalQuestion);
            Question = itemView.findViewById(R.id.question);
            optionA = itemView.findViewById(R.id.optionOne);
            optionB = itemView.findViewById(R.id.optionTwo);
            optionC = itemView.findViewById(R.id.optionThree);
            optionD = itemView.findViewById(R.id.optionFour);
            options = itemView.findViewById(R.id.quizgroup);

            options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    //Toast.makeText(context,String.valueOf(checkedId),Toast.LENGTH_LONG).show();
                    RadioButton button = itemView.findViewById(checkedId);
                    //Toast.makeText(context,button.getText(),Toast.LENGTH_LONG).show();
                    change.onRadio(getAdapterPosition(), (String) button.getText());
                }
            });
        }
    }
}
