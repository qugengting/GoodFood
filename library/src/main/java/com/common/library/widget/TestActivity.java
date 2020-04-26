package com.common.library.widget;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.common.library.R;
import com.common.library.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author:xuruibin
 * @date:2020/3/19 Description:
 */
public class TestActivity extends AppCompatActivity {

    @BindView(R2.id.tv_test)
    TextView tvTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        ButterKnife.bind(this);
    }
}
