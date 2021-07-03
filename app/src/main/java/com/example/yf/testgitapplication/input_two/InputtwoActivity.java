package com.example.yf.testgitapplication.input_two;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yf.testgitapplication.R;
import com.example.yf.testgitapplication.input_two.keyboard.BaseKeyboard;
import com.example.yf.testgitapplication.input_two.keyboard.KeyboardManager;
import com.example.yf.testgitapplication.input_two.keyboard.NumberKeyboard;

public class InputtwoActivity extends AppCompatActivity {
    /*  private KeyboardManager keyboardManagerAbc;
    private ABCKeyboard abcKeyboard;*/
    private EditText editText1;

    private EditText editText2;
    private KeyboardManager keyboardManagerNumber;
    private NumberKeyboard numberKeyboard;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputtwo);
        editText1 = findViewById(R.id.inputtwo_edtone);
        editText2 = findViewById(R.id.inputtwo_edt);
        init();
    }

    private void init(){
/*
        keyboardManagerAbc = new KeyboardManager(this);
        keyboardManagerAbc.bindToEditor(editText1, new ABCKeyboard(this, ABCKeyboard.DEFAULT_ABC_XML_LAYOUT));*/

        keyboardManagerNumber = new KeyboardManager(this);
        initNumberKeyboard();
        keyboardManagerNumber.bindToEditor(editText2, numberKeyboard);
        editText2.requestFocus();


    }


    private void initNumberKeyboard() {
        numberKeyboard = new NumberKeyboard(this,NumberKeyboard.DEFAULT_NUMBER_XML_LAYOUT);
        numberKeyboard.setEnableDotInput(true);
        numberKeyboard.setActionDoneClickListener(new NumberKeyboard.ActionDoneClickListener() {
            @Override
            public void onActionDone(CharSequence charSequence) {
                if(TextUtils.isEmpty(charSequence) || charSequence.toString().equals("0") || charSequence.toString().equals("0.0")) {
                    Toast.makeText(InputtwoActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                }else {
//                    onNumberkeyActionDone();
                }
            }
        });

        numberKeyboard.setKeyStyle(new BaseKeyboard.KeyStyle() {
            @Override
            public Drawable getKeyBackound(Keyboard.Key key) {
                if(key.iconPreview != null) {
                    return key.iconPreview;
                } else {
                    return ContextCompat.getDrawable(InputtwoActivity.this,R.drawable.key_number_bg);
                }
            }

            @Override
            public Float getKeyTextSize(Keyboard.Key key) {
                if(key.codes[0] == InputtwoActivity.this.getResources().getInteger(R.integer.action_done)) {
                    return convertSpToPixels( 20f);
                }
                return convertSpToPixels( 24f);
            }

            @Override
            public Integer getKeyTextColor(Keyboard.Key key) {
                if(key.codes[0] == getResources().getInteger(R.integer.action_done)) {
                    return Color.WHITE;
                }
                return null;
            }

            @Override
            public CharSequence getKeyLabel(Keyboard.Key key) {
                return null;
            }
        });
    }

    public float convertSpToPixels( float sp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,getResources().getDisplayMetrics());
        return px;
    }
    public void onNumberkeyActionDone() {
        editText1.requestFocus();
    }

}
