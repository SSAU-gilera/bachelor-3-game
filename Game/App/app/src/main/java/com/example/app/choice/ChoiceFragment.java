package com.example.app.choice;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.db.ChoiceEntity;
import com.example.app.db.DBDao;

public class ChoiceFragment extends DialogFragment {

    private RecyclerView rvChoice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
        return inflater.inflate(R.layout.choice_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvChoice = view.findViewById(R.id.rv_choice);

        int index = getArguments().getInt(INDEX);

        ChoiceEntity choiceEntity = DBDao.getInstance(requireContext()).getChoice().get(index);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rvChoice.setLayoutManager(layoutManager);
        ChoiceAdapter adapter = new ChoiceAdapter(choiceEntity, this);
        rvChoice.setAdapter(adapter);
    }

    //Переопределение кнопки Back
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {

            }
        };
    }

    public static final String TAG = "choice_fragment";
    private static final String INDEX = "index";

    //Передача  индекса записи из PlayActivity
    public static ChoiceFragment init(int id) {
        ChoiceFragment choiceFragment = new ChoiceFragment();
        Bundle args = new Bundle();
        args.putInt(INDEX, id);
        choiceFragment.setArguments(args);
        return choiceFragment;
    }
}
