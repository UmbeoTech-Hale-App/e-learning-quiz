package com.pam.elearning.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.pam.elearning.R;
import com.pam.elearning.view_model.AnswerViewModel;
import com.pam.elearning.view_model.QuestionViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionFragment extends Fragment {


    @BindView(R.id.question)
    TextView question;

    @BindView(R.id.radioButton_1)
    RadioButton radioButton1;

    @BindView(R.id.radioButton_2)
    RadioButton radioButton2;

    @BindView(R.id.radioButton_3)
    RadioButton radioButton3;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    private int lessonNumber;

    private QuestionViewModel questionViewModel;
    private AnswerViewModel answerViewModel;

    public QuestionFragment() {
    }

    public QuestionFragment(int lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        ButterKnife.bind(this, view);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radioButton_1:
                    radioButton1.setSelected(true);
                    radioButton2.setSelected(false);
                    radioButton3.setSelected(false);
                    break;

                case R.id.radioButton_2:
                    radioButton1.setSelected(false);
                    radioButton2.setSelected(true);
                    radioButton3.setSelected(false);
                    break;
                case R.id.radioButton_3:
                    radioButton1.setSelected(false);
                    radioButton2.setSelected(false);
                    radioButton3.setSelected(true);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        questionViewModel = new ViewModelProvider(requireActivity()).get(QuestionViewModel.class);
        answerViewModel = new ViewModelProvider(requireActivity()).get(AnswerViewModel.class);

        questionViewModel.getByLessonId(lessonNumber).observe(getViewLifecycleOwner(), q -> {
            if (q != null) question.setText(q.getContents());
        });
        answerViewModel.getByLessonId(lessonNumber).observe(getViewLifecycleOwner(), a -> {
            if (a != null && a.size() > 2) {
                for (int i = 0; i < radioGroup.getChildCount(); i++)
                    ((RadioButton) radioGroup.getChildAt(i)).setText(a.get(i).getContents());
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();

        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            if (radioGroup.getChildAt(i).isSelected()) {
                answerViewModel.selectByLessonIdAndContents
                        (
                                lessonNumber,
                                ((RadioButton) radioGroup.getChildAt(i)).getText().toString()
                        );
                break;
            }
        }
    }

}
