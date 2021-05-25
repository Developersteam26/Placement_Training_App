package edu.education.androiddevelopmentcontest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.education.androiddevelopmentcontest.R;
import edu.education.androiddevelopmentcontest.classes.AdminQuizDetails;

public class InflateAdminQuizAdapter extends RecyclerView.Adapter<InflateAdminQuizAdapter.AdminQuizHolder> {

    private Context context;
    private ArrayList<AdminQuizDetails> details;
    private OnClickListener listener;

    public interface OnClickListener {
        void deleteQuiz(int position);
        void editQuiz(int position);
    }

    public void setOnItemClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public InflateAdminQuizAdapter(Context context, ArrayList<AdminQuizDetails> details) {
        this.context = context;
        this.details = details;
    }

    @NonNull
    @Override
    public AdminQuizHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adminquiz,parent,false);
        return new AdminQuizHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminQuizHolder holder, int position) {
        AdminQuizDetails quizDetails = details.get(position);

        holder.question.setText(quizDetails.getQuestion());
        holder.optionA.setText(quizDetails.getOptionA());
        holder.optionB.setText(quizDetails.getOptionB());
        holder.optionC.setText(quizDetails.getOptionC());
        holder.optionD.setText(quizDetails.getOptionD());

        String answer = quizDetails.getAnswer();
        String optionA = quizDetails.getOptionA();
        String optionB = quizDetails.getOptionB();
        String optionC = quizDetails.getOptionC();
        String currentQuestion = (position + 1) + " / " + (details.size());

        holder.questionNumber.setText(currentQuestion);

        if (answer.equals(optionA)) {
            holder.optionA.setChecked(true);
        } else if (answer.equals(optionB)) {
            holder.optionB.setChecked(true);
        } else if (answer.equals(optionC)) {
            holder.optionC.setChecked(true);
        } else {
            holder.optionD.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class AdminQuizHolder extends RecyclerView.ViewHolder {

        public TextView question;
        public RadioGroup quizOption;
        public RadioButton optionA;
        public RadioButton optionB;
        public RadioButton optionC;
        public RadioButton optionD;
        public TextView questionNumber;
        public LinearLayout deleteQuiz;
        public LinearLayout editQuiz;

        public AdminQuizHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.question);
            quizOption = itemView.findViewById(R.id.quizgroup);
            optionA = itemView.findViewById(R.id.optionOne);
            optionB = itemView.findViewById(R.id.optionTwo);
            optionC = itemView.findViewById(R.id.optionThree);
            optionD = itemView.findViewById(R.id.optionFour);
            questionNumber = itemView.findViewById(R.id.currentQuestion);
            deleteQuiz = itemView.findViewById(R.id.deleteQuiz);
            editQuiz = itemView.findViewById(R.id.editQuiz);

            deleteQuiz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.deleteQuiz(getAdapterPosition());
                }
            });

            editQuiz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.editQuiz(getAdapterPosition());
                }
            });
        }
    }
}
