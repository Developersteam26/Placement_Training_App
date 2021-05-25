package edu.education.androiddevelopmentcontest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.education.androiddevelopmentcontest.R;
import edu.education.androiddevelopmentcontest.classes.GetSolution;

public class InflateQuizAnswerAdapter extends RecyclerView.Adapter<InflateQuizAnswerAdapter.QuizAnswerHolder> {

    public Context context;
    public ArrayList<GetSolution> details;

    public InflateQuizAnswerAdapter(Context context,ArrayList<GetSolution> details) {
        this.context = context;
        this.details = details;
    }

    @NonNull
    @Override
    public QuizAnswerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.resultanswers,parent,false);
        return new QuizAnswerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizAnswerHolder holder, int position) {
        GetSolution getSolution = details.get(position);

        String question = getSolution.getQuestion();
        String yourAnswer = getSolution.getResponse();
        String answer = getSolution.getAnswer();

        if (yourAnswer.equals(answer)) {
            holder.imageView.setImageResource(R.drawable.ic_action_done);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_action_wrong);
        }

        holder.Question.setText(question);
        holder.YourAnswer.setText(yourAnswer);
        holder.Answer.setText(answer);
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class QuizAnswerHolder extends RecyclerView.ViewHolder {

        public TextView Question;
        public TextView YourAnswer;
        public TextView Answer;
        public ImageView imageView;

        public QuizAnswerHolder(@NonNull View itemView) {
            super(itemView);

            Question = itemView.findViewById(R.id.question);
            YourAnswer = itemView.findViewById(R.id.yourAnswer);
            imageView = itemView.findViewById(R.id.status);
            Answer = itemView.findViewById(R.id.correctAnswer);
        }
    }

}
