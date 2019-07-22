package com.example.yf.testgitapplication.input;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yf.testgitapplication.R;
import com.example.yf.testgitapplication.input.keyboard.KeyboardUtil;
import com.superc.yf_lib.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InputActivity extends BaseActivity {

    @BindView(R.id.input_edt)
    EditText mInputEdt;
    @BindView(R.id.keyboard_view)
    KeyboardView mKeyboardView;
    @BindView(R.id.input_tv_content)
    TextView mInputTvContent;
    @BindView(R.id.input_linear)
    LinearLayout mInputLinear;
    private KeyboardUtil keyboardUtil;
    public boolean isShowKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        ButterKnife.bind(this);
        init();
    }

    @Override
    public void init() {
        mInputTvContent.requestFocus();
        configeSimpleTitle("自定义键盘输入", true);
        mInputEdt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                toInitKeyboard();
                return false;
            }
        });

    }

    @OnClick(R.id.input_linear)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.input_linear:
                toHideKeyBoard();
                break;
        }
    }

    private void toInitKeyboard() {
        if (keyboardUtil == null) {
            keyboardUtil = new KeyboardUtil(this, mInputEdt, R.id.keyboard_view, 0);
            keyboardUtil.hideSoftInputMethod(this.getWindow());
            keyboardUtil.setOnYanZClickListener(new KeyboardUtil.OnYanZClickListener() {
                @Override
                public void onYanZClickListener() {//键盘  验证操作
                    toHideKeyBoard();
                    ToastShow("去验证");
                }
            });
        }
        keyboardUtil.showKeyboard();
        isShowKeyboard = true;
        mInputLinear.setVisibility(View.VISIBLE);
    }

    public void toHideKeyBoard() {
        if (isShowKeyboard) {
            mInputLinear.setVisibility(View.GONE);
            isShowKeyboard = false;
            keyboardUtil.hideKeyboard();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isShowKeyboard) {
//            mLinearLayout.setVisibility(View.GONE);
            isShowKeyboard = false;
            keyboardUtil.hideKeyboard();
            return false;
        }
        return super.onKeyDown(keyCode, event);

    }

}
