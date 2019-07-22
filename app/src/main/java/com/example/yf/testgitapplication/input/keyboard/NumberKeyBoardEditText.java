package com.example.yf.testgitapplication.input.keyboard;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ActionMode;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.example.yf.testgitapplication.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/**
 * Created by shikh on 2016/12/8.
 */
public class NumberKeyBoardEditText extends EditText {

    private KeyboardView mKeyboardView;
    private Keyboard mKeyboard;

    private Window mWindow;
    private View mDecorView;
    private View mContentView;

    private PopupWindow mKeyboardWindow;

    private boolean needcustomkeyboard = false; // 是否启用自定义键盘 默认为R.xml.symbols
    private boolean randomkeys = false; //数字按键是否随机

    private int scrolldis = 50; // 输入框在键盘被弹出时，要被推上去的距离

    public static int screenw = -1;// 未知宽高
    public static int screenh = -1;
    public static int screenh_nonavbar = -1; // 不包含导航栏的高度
    public static int real_scontenth = -1; // 实际内容高度， 计算公式:屏幕高度-导航栏高度-电量栏高度

    public static float density = 1.0f;
    public static int densityDpi = 160;

    public KeyboardUtil keyboardUtil;

    public NumberKeyBoardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(context);
        initKeyBoard(context, attrs);
        initPopWindow();
    }

    public NumberKeyBoardEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(context);
        initKeyBoard(context, attrs);
        initPopWindow();
    }

    private void initKeyBoard(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.keyboard);
        if (a.hasValue(R.styleable.keyboard_xml)) {
            needcustomkeyboard = true;
            int xmlid = a.getResourceId(R.styleable.keyboard_xml, 0);
            keyboardUtil = new KeyboardUtil(context, this, xmlid);
        } else {
            keyboardUtil = new KeyboardUtil(context, this, 0);
        }
        if (a.hasValue(R.styleable.keyboard_randomkeys)) {
            randomkeys = a.getBoolean(R.styleable.keyboard_randomkeys, false);
        }

        mKeyboardView = keyboardUtil.getKeyboardView();
        mKeyboard = keyboardUtil.getK();
        if (randomkeys) {//随机键盘
            randomdigkey(mKeyboard);
        }
        keyboardUtil.initKeyBoard();
        a.recycle();
    }

    private void initPopWindow() {
        mKeyboardWindow = new PopupWindow(keyboardUtil.getKeyboardView(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mKeyboardWindow.setAnimationStyle(R.style.AnimationFade);//设置动画效果，文件在资源里面，这里就不贴出来。</span></strong>
        // mKeyboardWindow.setBackgroundDrawable(new BitmapDrawable());
        // mKeyboardWindow.setOutsideTouchable(true);
        keyboardUtil.setmKeyboardWindow(mKeyboardWindow);
        mKeyboardWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                if (scrolldis > 0) {
                    int temp = scrolldis;
                    scrolldis = 0;
                    if (null != mContentView) {
                        //使布局整体向上顶的关键代码，使用布局的scrollBy重新滚动位置
                        mContentView.scrollBy(0, -temp);
                    }
                }
            }
        });

    }

    //显示键盘
    private void showKeyboard() {
        if (null != mKeyboardWindow) {
            if (!mKeyboardWindow.isShowing()) {
                if (randomkeys) {
                    randomdigkey(mKeyboard);
                }
                mKeyboardView.setKeyboard(mKeyboard);
                mKeyboardWindow.showAtLocation(this.mDecorView, Gravity.BOTTOM, 0, 0);
                mKeyboardWindow.update();
                if (null != mDecorView && null != mContentView) {
                    int[] pos = new int[2];
                    // 计算弹出的键盘的尺寸
                    getLocationOnScreen(pos);
                    float height = screenh*47/100;
//                    float height = dpToPx(getContext(), 300);
                    Rect outRect = new Rect();
                    // 然后该View有个getWindowVisibleDisplayFrame()方法可以获取到程序显示的区域，
                    // * 包括标题栏，但不包括状态栏。
                    mDecorView.getWindowVisibleDisplayFrame(outRect);// 获得view空间，也就是除掉标题栏
                    // outRect.top表示状态栏（通知栏)
                    int screen = real_scontenth;
                    scrolldis = (int) ((pos[1] + getMeasuredHeight() - outRect.top) - (screen - height));
                    if (scrolldis > 0) {
                        mContentView.scrollBy(0, scrolldis);
                    }
                }

            }
        }
    }

    //隐藏键盘
    private void hideKeyboard() {
        if (null != mKeyboardWindow) {
            if (mKeyboardWindow.isShowing()) {
                mKeyboardWindow.dismiss();
            }
        }
    }

    //隐藏系统的软键盘
    private void hideSysInput() {
        if (this.mWindow != null && keyboardUtil != null) {
            keyboardUtil.hideSoftInputMethod(this.mWindow);
            return;
        }
        if (this.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        requestFocus();
        requestFocusFromTouch();
//        if (needcustomkeyboard) {
            hideSysInput();
            showKeyboard();
//        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (null != mKeyboardWindow) {
                if (mKeyboardWindow.isShowing()) {
                    mKeyboardWindow.dismiss();
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mWindow = ((Activity) getContext()).getWindow();
        this.mDecorView = this.mWindow.getDecorView();
        this.mContentView = this.mWindow.findViewById(Window.ID_ANDROID_CONTENT);
//        hideSysInput();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        hideKeyboard();
        mKeyboardWindow = null;
        mKeyboardView = null;
        mKeyboard = null;
        mDecorView = null;
        mContentView = null;
        mWindow = null;
    }

    private void initAttributes(Context context) {
        initScreenParams(context);
        this.setLongClickable(false);
        this.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        removeCopyAbility();
//        if (this.getText() != null) {
//            this.setSelection(this.getText().length());
//        }

        this.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (!hasFocus) {
                    hideKeyboard();
                }
            }
        });

    }

    private void removeCopyAbility() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }
            });
        }
    }


    // 暂时未使用到，为了实现随机键盘布局
    private void randomdigkey(Keyboard mKeyboard) {
        if (mKeyboard == null) {
            return;
        }
        List<Keyboard.Key> keyList = mKeyboard.getKeys();
        // 查找出0-9的数字键
        List<Keyboard.Key> newkeyList = new ArrayList<Keyboard.Key>();
        for (int i = 0, size = keyList.size(); i < size; i++) {
            Keyboard.Key key = keyList.get(i);
            CharSequence label = key.label;
            if (label != null && isNumber(label.toString())) {
                newkeyList.add(key);
            }
        }

        int count = newkeyList.size();
        List<KeyModel> resultList = new ArrayList<KeyModel>();
        LinkedList<KeyModel> temp = new LinkedList<KeyModel>();

        for (int i = 0; i < count; i++) {
            temp.add(new KeyModel(48 + i, i + ""));
        }
        Random rand = new SecureRandom();
        rand.setSeed(SystemClock.currentThreadTimeMillis());

        for (int i = 0; i < count; i++) {
            int num = rand.nextInt(count - i);
            KeyModel model = temp.get(num);
            resultList.add(new KeyModel(model.getCode(), model.getLable()));
            temp.remove(num);
        }

        for (int i = 0, size = newkeyList.size(); i < size; i++) {
            Keyboard.Key newKey = newkeyList.get(i);
            KeyModel resultmodle = resultList.get(i);
            newKey.label = resultmodle.getLable();
            newKey.codes[0] = resultmodle.getCode();
        }

    }

    /**
     * 密度转换为像素值
     *
     * @param dp
     * @return
     */
    public static int dpToPx(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private void initScreenParams(Context context) {
        DisplayMetrics dMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dMetrics);

        screenw = dMetrics.widthPixels;
        screenh = dMetrics.heightPixels;
        density = dMetrics.density;
        densityDpi = dMetrics.densityDpi;

        screenh_nonavbar = screenh;

        int ver = Build.VERSION.SDK_INT;

        // 新版本的android 系统有导航栏，造成无法正确获取高度
        if (ver == 13) {
            try {
                Method mt = display.getClass().getMethod("getRealHeight");
                screenh_nonavbar = (Integer) mt.invoke(display);
            } catch (Exception e) {
            }
        } else if (ver > 13) {
            try {
                Method mt = display.getClass().getMethod("getRawHeight");
                screenh_nonavbar = (Integer) mt.invoke(display);
            } catch (Exception e) {
            }
        }

        real_scontenth = screenh_nonavbar - getStatusBarHeight(context);

    }

    /**
     * 电量栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return sbar;
    }

    private boolean isNumber(String str) {
        String wordstr = "0123456789";
        if (wordstr.indexOf(str) > -1) {
            return true;
        }
        return false;
    }
}
