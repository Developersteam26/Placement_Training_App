package edu.education.androiddevelopmentcontest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.education.androiddevelopmentcontest.R;
import edu.education.androiddevelopmentcontest.classes.GetFormula;

public class InflateFormulaAdapter extends RecyclerView.Adapter<InflateFormulaAdapter.FormulaHolder> {

    public Context context;
    public ArrayList<GetFormula> details;

    public InflateFormulaAdapter(Context context,ArrayList<GetFormula> details) {
        this.context = context;
        this.details = details;
    }

    @NonNull
    @Override
    public FormulaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.formulae,parent,false);
        return new FormulaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FormulaHolder holder, int position) {
        GetFormula getFormula = details.get(position);

        holder.Title.setText(getFormula.getTitle());
        holder.formula.setText(getFormula.getFormula());
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class FormulaHolder extends RecyclerView.ViewHolder {

        public TextView Title;
        public TextView formula;

        public FormulaHolder(@NonNull View itemView) {
            super(itemView);

            Title = itemView.findViewById(R.id.title);
            formula = itemView.findViewById(R.id.formula);
        }
    }
}
