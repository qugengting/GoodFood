package com.common.library.widget.popmenu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.common.library.R;

/**
 * @author:xuruibin
 * @date:2019/8/20 Description:
 */
public class ShareDialog extends Dialog {

    private Context mContext;
    private View view;

    public ShareDialog(Context context) {
        super(context, R.style.share_dialog);
        this.mContext = context;
    }

    public View getView() {
        return view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.dialog_share, null, false);
        }
        setContentView(view);
        Window window = getWindow();  //得到弹框
        window.setWindowAnimations(R.style.share_dialog_anim);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
    }
}
