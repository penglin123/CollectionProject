package com.example.yinlian.collectionproject.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.library.base.BaseFragment;
import com.example.yinlian.collectionproject.R;

import java.util.Objects;

/**
 * - @Description:外访任务
 * - @Author:  penglin
 * - @Time:  2018/05/08
 */
public class OutboundTaskFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup radioGroup;
    private RadioButton rbWaitVisitTask;
    private RadioButton rbCompletedSingleTask;
    private WaitVisitTaskFrament waitVisitTaskFrament;
    private CompletedSingleTaskFrament completedSingleTaskFrament;

    public static OutboundTaskFragment newInstance() {
        return new OutboundTaskFragment();
    }


    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        radioGroup = view.findViewById(R.id.radioGroup);
        rbWaitVisitTask = view.findViewById(R.id.rb_wait_visit_task);
        rbCompletedSingleTask = view.findViewById(R.id.rb_completed_single_task);
        radioGroup.setOnCheckedChangeListener(this);

        initFragment(1);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_outbound_task;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_wait_visit_task) {
            rbWaitVisitTask.setTextColor(Color.parseColor("#7990ff"));
            rbCompletedSingleTask.setTextColor(Color.parseColor("#666666"));
            initFragment(1);
        } else {
            rbWaitVisitTask.setTextColor(Color.parseColor("#666666"));
            rbCompletedSingleTask.setTextColor(Color.parseColor("#7990ff"));
            initFragment(2);
        }
    }

    @SuppressLint("CommitTransaction")
    private void initFragment(int tag) {

        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        switch (tag) {
            case 1:
                if (waitVisitTaskFrament == null) {
                    waitVisitTaskFrament = new WaitVisitTaskFrament();
                    transaction.add(R.id.frame, waitVisitTaskFrament);
                }
                if (completedSingleTaskFrament != null) {
                    transaction.hide(completedSingleTaskFrament);
                }

                transaction.show(waitVisitTaskFrament)
                        .commit();
                break;
            case 2:
                if (completedSingleTaskFrament == null) {
                    completedSingleTaskFrament = new CompletedSingleTaskFrament();
                    transaction.add(R.id.frame, completedSingleTaskFrament);
                }
                if (waitVisitTaskFrament != null) {
                    transaction.hide(waitVisitTaskFrament);
                }
                transaction.show(completedSingleTaskFrament)
                        .commit();
                break;
        }
    }
}
