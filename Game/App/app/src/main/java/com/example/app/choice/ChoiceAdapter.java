package com.example.app.choice;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.PlayActivity;
import com.example.app.R;
import com.example.app.db.ChoiceEntity;

public class ChoiceAdapter extends RecyclerView.Adapter<ChoiceAdapter.ViewHolder> {

    private ChoiceEntity choiceEntities;
    private ChoiceFragment choiceFragment;

    ChoiceAdapter(ChoiceEntity choiceEntities, ChoiceFragment choiceFragment) {
        this.choiceEntities = choiceEntities;
        this.choiceFragment = choiceFragment;
    }

    @NonNull
    @Override
    public ChoiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choice_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChoiceAdapter.ViewHolder holder, int position) {
        holder.textView.setText(choiceEntities.getChoiceSentence().get(position));

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceFragment.dismiss(); //Завершение окна выбора
                //Вызов метода resultChoice из PlayActivity
                ((PlayActivity)choiceFragment.getActivity()).resultChoice(choiceEntities.getTransition().get(holder.getAdapterPosition()) - 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return choiceEntities.getChoiceSentence().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_choice);
        }

    }
}
