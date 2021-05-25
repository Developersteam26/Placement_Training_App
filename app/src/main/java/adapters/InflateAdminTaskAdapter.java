package edu.education.androiddevelopmentcontest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.education.androiddevelopmentcontest.R;
import edu.education.androiddevelopmentcontest.classes.ActualTasks;
import edu.education.androiddevelopmentcontest.classes.AdminTasks;

public class InflateAdminTaskAdapter extends RecyclerView.Adapter<InflateAdminTaskAdapter.AdminTaskHolder> {

    public Context context;
    public ArrayList<ActualTasks> details;
    public OnItemClickListener listener;

    public interface OnItemClickListener {
        void onLayoutClick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener (OnItemClickListener listener) {
        this.listener = listener;
    }

    public InflateAdminTaskAdapter(Context context, ArrayList<ActualTasks> details) {
        this.context = context;
        this.details = details;
    }

    @NonNull
    @Override
    public AdminTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_dashboard,parent,false);
        return new AdminTaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminTaskHolder holder, int position) {
        ActualTasks tasks = details.get(position);

        holder.title.setText(tasks.getTitle());
        String type = tasks.getType();
        switch (type) {
            case "quiz":
                type = "Quiz";
                break;
            case "formula":
                type = "Formula, Lecture";
                break;
        }
        holder.topic.setText(type);
        int due = Integer.parseInt(tasks.getDuration());
        String timeLimit;
        if (due <= 60) {
            timeLimit = due + " min";
        } else {
            int hrs = due / 60;
            int mins = due % 60;

            timeLimit = hrs + " hr " + mins + " mins";
        }

        holder.duration.setText(timeLimit);
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class AdminTaskHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView duration;
        public TextView topic;
        public LinearLayout editTask;
        public LinearLayout deleteTask;

        public AdminTaskHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            duration = itemView.findViewById(R.id.due);
            topic = itemView.findViewById(R.id.topics);
            editTask = itemView.findViewById(R.id.editTask);
            deleteTask = itemView.findViewById(R.id.deleteTask);

            editTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onEditClick(getAdapterPosition());
                }
            });

            deleteTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteClick(getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onLayoutClick(getAdapterPosition());
                }
            });
        }
    }
}
