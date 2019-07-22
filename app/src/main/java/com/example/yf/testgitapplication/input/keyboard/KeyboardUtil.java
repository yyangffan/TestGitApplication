package com.example.yf.testgitapplication.input.keyboard;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.example.yf.testgitapplication.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by shikh on 2016/12/7.
 */
public class KeyboardUtil {
    private KeyboardView keyboardView;
    private Keyboard k;// 数字键盘
    private EditText ed;
    private Context mContext;
    private PopupWindow mKeyboardWindow;
    private OnYanZClickListener mOnYanZClickListener;

    public Keyboard getK() {
        return k;
    }

    public KeyboardView getKeyboardView() {
        return keyboardView;
    }

    public void setmKeyboardWindow(PopupWindow mKeyboardWindow) {
        this.mKeyboardWindow = mKeyboardWindow;
    }

    public void setOnYanZClickListener(OnYanZClickListener onYanZClickListener) {
        mOnYanZClickListener = onYanZClickListener;
    }

    /**
     * 键盘置于布局文件中
     *
     * @author shikh
     * @time 2016/12/8 上午10:19
     */
    public KeyboardUtil(Activity atx, EditText edit, int keyId, int xmlId) {
        this.ed = edit;
        this.mContext = atx;
        k = new Keyboard(atx, xmlId == 0 ? R.xml.symbols : xmlId);
        keyboardView = (KeyboardView) atx.findViewById(keyId);
        initKeyBoard();
    }

    /**
     * 键盘用popwindow 形式打开 结合 numberKeyBoardEditText 使用 具有向上顶布局的功能
     *
     * @author shikh
     * @time 2016/12/8 上午10:22
     */
    public KeyboardUtil(Context ctx, EditText edit, int xmlId) {
        this.ed = edit;
        this.mContext = ctx;
        k = new Keyboard(ctx, xmlId == 0 ? R.xml.symbols : xmlId);
        keyboardView = (KeyboardView) View.inflate(ctx, R.layout.layout_keyboard_view, null);
    }


    public void initKeyBoard() {
        keyboardView.setKeyboard(k);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setVisibility(View.VISIBLE);
        keyboardView.setOnKeyboardActionListener(listener);
    }

    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        //一些特殊操作按键的codes是固定的比如完成、回退等
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = ed.getText();
            int start = ed.getSelectionStart();
            if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            } else if (primaryCode == 4896) {//验证
                if (mOnYanZClickListener != null) {
                    mOnYanZClickListener.onYanZClickListener();
                    hideKeyboard();
                }
            } else if (primaryCode == Keyboard.KEYCODE_CANCEL) {//
                hideKeyboard();
            } else if (primaryCode == 46) { // 小数点
                String text = ed.getText().toString();
                if (!text.contains(".") && text.length() > 0) {
                    editable.insert(start, Character.toString((char) primaryCode));
                }
            } else { //将要输入的数字现在编辑框中
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }
    };


    /**
     * 实应于 键盘布局放置在activity 布局文件中
     *
     * @author shikh
     * @time 2016/12/8 上午10:08
     */
    public boolean isShow() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            return true;
        }
        return false;
    }

    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    public void hideKeyboard() {
        if (null != mKeyboardWindow) {
            if (mKeyboardWindow.isShowing()) {
                mKeyboardWindow.dismiss();
            }
        } else {
            int visibility = keyboardView.getVisibility();
            if (visibility == View.VISIBLE) {
                keyboardView.setVisibility(View.GONE);
            }
        }

    }

    public interface OnYanZClickListener {
        void onYanZClickListener();
    }

    /**
     * 隐藏系统键盘
     *
     * @author shikh
     * @time 2016/12/8 下午3:19
     */
    public void hideSoftInputMethod(Window mWindow) {
        mWindow.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        String methodName = null;
        if (currentVersion >= 16) {
            // 4.2
            methodName = "setShowSoftInputOnFocus";
        } else if (currentVersion >= 14) {
            // 4.0
            methodName = "setSoftInputShownOnFocus";
        }
        if (methodName == null) {
            ed.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName,
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(ed, false);
            } catch (NoSuchMethodException e) {
                ed.setInputType(InputType.TYPE_NULL);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
