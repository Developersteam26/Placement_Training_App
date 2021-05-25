package edu.education.androiddevelopmentcontest.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.education.androiddevelopmentcontest.R;
import edu.education.androiddevelopmentcontest.classes.GetTask;

public class InflateTaskAdapter extends RecyclerView.Adapter<InflateTaskAdapter.TaskViewHolder> {

    private Context context;
    private ArrayList<GetTask> details;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public InflateTaskAdapter(Context context,ArrayList<GetTask> details) {
        this.context = context;
        this.details = details;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_tasks,parent,false);
        return new TaskViewHolder(view);
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        GetTask getTask = details.get(position);

        String id = getTask.getId();
        String title = getTask.getName();
        String due = getTask.getDue();
        String topice = getTask.getTopics();
        String sTotal = getTask.getTotal();
        String sProgressed = getTask.getProgress();
        int total = Integer.parseInt(sTotal);
        int progress = Integer.parseInt(sProgressed);
        float completed;
        if (total > 0) {
            completed = ((progress * 100) / total);
        } else {
            completed = 0;
        }
        int done = Math.round(completed);

        if (done == 100) {
            holder.Done.setVisibility(View.VISIBLE);
            holder.Completed.setProgress(100);
            int colorCodeGreen = Color.parseColor("#4CAF50");
            holder.Completed.setProgressTintList(ColorStateList.valueOf(colorCodeGreen));
        }

        holder.Title.setText(title);
        holder.Due.setText(due);
        holder.Topics.setText(topice);
        holder.Completed.setMax(100);
        holder.Completed.setProgress(done);
        holder.Progressor.setText(sProgressed + "/" + sTotal);
    }

    @Override
    public int getItemCount() {
        return details.size();
    }


    public class TaskViewHolder extends RecyclerView.ViewHolder {

        public TextView Title;
        public TextView Due;
        public TextView Topics;
        public ProgressBar Completed;
        public TextView Progressor;
        public LinearLayout linearLayout;
        public ImageView Done;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            Title = itemView.findViewById(R.id.title);
            Due = itemView.findViewById(R.id.due);
            Topics = itemView.findViewById(R.id.topics);
            Completed = itemView.findViewById(R.id.progress);
            Progressor = itemView.findViewById(R.id.total);
            linearLayout = itemView.findViewById(R.id.task);
            Done = itemView.findViewById(R.id.done);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
