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
import edu.education.androiddevelopmentcontest.classes.AdminFormula;

public class InflateAdminFormulaAdapter extends RecyclerView.Adapter<InflateAdminFormulaAdapter.AdminFormulaHolder> {

    private Context context;
    private ArrayList<AdminFormula> details;
    private onFormulaClickListener listener;

    public interface onFormulaClickListener {
        public void onDeleteFormula(int position);
    }

    public void setOnClickListener(onFormulaClickListener listener) {
        this.listener = listener;
    }

    public InflateAdminFormulaAdapter(Context context, ArrayList<AdminFormula> details) {
        this.context = context;
        this.details = details;
    }

    @NonNull
    @Override
    public AdminFormulaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adminlecture,parent,false);
        return new AdminFormulaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminFormulaHolder holder, int position) {
        AdminFormula formula = details.get(position);

        holder.title.setText(formula.getTitle());
        holder.formula.setText(formula.getFormula());
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class AdminFormulaHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView formula;
        public LinearLayout deleteFormula;

        public AdminFormulaHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            formula = itemView.findViewById(R.id.formula);
            deleteFormula = itemView.findViewById(R.id.deleteFormula);

            deleteFormula.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteFormula(getAdapterPosition());
                }
            });
        }
    }
}
