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
import edu.education.androiddevelopmentcontest.classes.AdminTasks;

public class InflateAdminDashboard extends RecyclerView.Adapter<InflateAdminDashboard.AdminDashboardHolder> {

    public Context context;
    public ArrayList<AdminTasks> details;
    public OnItemClickListener listener;

    public interface  OnItemClickListener {
        void onItemClick(int position);
        void onButtonClick(int position);
        void onEditClick(int adapterPosition);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public InflateAdminDashboard(Context context,ArrayList<AdminTasks> details) {
        this.context = context;
        this.details = details;
    }

    @NonNull
    @Override
    public AdminDashboardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_dashboard,parent,false);
        return new AdminDashboardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminDashboardHolder holder, int position) {
        AdminTasks tasks = details.get(position);

        holder.title.setText(tasks.getName());
        holder.due.setText(tasks.getDue());
        holder.topic.setText(tasks.getTopic());
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class AdminDashboardHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView due;
        public TextView topic;
        public LinearLayout deleteTask;
        public LinearLayout editTask;

        public AdminDashboardHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            due = itemView.findViewById(R.id.due);
            topic = itemView.findViewById(R.id.topics);
            deleteTask = itemView.findViewById(R.id.deleteTask);
            editTask = itemView.findViewById(R.id.editTask);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int postion = getAdapterPosition();
                        if (postion != RecyclerView.NO_POSITION) {
                            listener.onItemClick(postion);
                        }
                    }
                }
            });

            deleteTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onButtonClick(getAdapterPosition());
                }
            });

            editTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onEditClick(getAdapterPosition());
                }
            });
        }
    }
}
