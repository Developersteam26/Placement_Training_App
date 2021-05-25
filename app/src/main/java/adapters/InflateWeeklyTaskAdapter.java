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
import edu.education.androiddevelopmentcontest.classes.WeeklyTask;

public class InflateWeeklyTaskAdapter extends RecyclerView.Adapter<InflateWeeklyTaskAdapter.WeeklyViewHolder> {

    private Context context;
    private ArrayList<WeeklyTask> details;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public InflateWeeklyTaskAdapter(Context context, ArrayList<WeeklyTask> details) {
        this.context = context;
        this.details = details;
    }

    @NonNull
    @Override
    public WeeklyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tasks,parent,false);
        return new WeeklyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeeklyViewHolder holder, int position) {
        WeeklyTask weeklyTask = details.get(position);

        String title = weeklyTask.getTitle();
        String type = weeklyTask.getType();
        String stotalTime = weeklyTask.getDuration();
        String scompleted = weeklyTask.getProgress();

        int totalTime = Integer.parseInt(stotalTime);
        int completed = Integer.parseInt(scompleted);
        int dduration = totalTime - completed;

        if (dduration == 0) {
            holder.done.setVisibility(View.VISIBLE);
        }

        switch (type) {
            case "quiz":
                type = "Quiz";
                break;
            case "formula":
                type = "Formula, Lectures";
                break;
        }

        String duration;
        if (dduration < 60) {
            duration = String.valueOf(dduration) + " min";
        } else {
            if (dduration % 60 == 0) {
                int timeCalc = dduration / 60;
                duration = String.valueOf(timeCalc) + " hr";
            } else {
                float timeCalc = dduration / 60;
                duration = String.valueOf(timeCalc) + " hr";
            }
        }

        holder.duration.setText(duration);

        holder.type.setText(type);
        holder.title.setText(title);
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class WeeklyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView type;
        public TextView duration;
        public ImageView done;

        public WeeklyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            type = itemView.findViewById(R.id.type);
            duration = itemView.findViewById(R.id.duration);
            done = itemView.findViewById(R.id.done);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int positon = getAdapterPosition();
                        if (positon != RecyclerView.NO_POSITION) {
                            listener.onItemClick(positon);
                        }
                    }
                }
            });
        }
    }

}
