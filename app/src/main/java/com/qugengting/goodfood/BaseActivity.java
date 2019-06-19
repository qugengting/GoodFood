package com.qugengting.goodfood;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.qugengting.goodfood.util.FixMemLeak;

/**
 * Created by xuruibin on 2018/10/15.
 * 描述：
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FixMemLeak.fixLeak(this);
    }
}
