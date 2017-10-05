package com.fanwe.library.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.EditText;

import com.fanwe.library.R;
import com.fanwe.library.drawable.SDDrawable;
import com.fanwe.library.model.SDDelayRunnable;
import com.fanwe.library.utils.SDKeyboardUtil;
import com.fanwe.library.utils.SDViewUtil;

/**
 * 带标题，输入框，确定按钮和取消按钮的窗口
 *
 * @author js02
 */
public class SDDialogInput extends SDDialogCustom
{
    public EditText et_content;

    public SDDialogInput(Activity activity)
    {
        super(activity);
    }

    @Override
    protected void init()
    {
        super.init();

        setCustomView(R.layout.dialog_input);
        et_content = (EditText) findViewById(R.id.dialog_input_et_content);

        Drawable backgroundDrawable = new SDDrawable().color(Color.WHITE).strokeWidthAll(SDViewUtil.dp2px(1)).cornerAll(getLibraryConfig().getCorner());
        et_content.setBackgroundDrawable(backgroundDrawable);
    }

    /**
     * 获得内容
     *
     * @return
     */
    public String getContent()
    {
        return et_content.getText().toString();
    }

    /**
     * 隐藏输入键盘
     */
    public void hideKeyboard()
    {
        SDKeyboardUtil.hideKeyboard(et_content);
    }

    public SDDialogInput setTextContent(String text)
    {
        if (TextUtils.isEmpty(text))
        {
            et_content.setText("");
        } else
        {
            et_content.setText(text);
        }
        return this;
    }

    public SDDialogInput setTextContentHint(String text)
    {
        if (TextUtils.isEmpty(text))
        {
            et_content.setHint("");
        } else
        {
            et_content.setHint(text);
        }
        return this;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mShowKeyboardRunnable.runDelay(200);
    }

    @Override
    protected void onStop()
    {
        mShowKeyboardRunnable.removeDelay();
        hideKeyboard();
        super.onStop();
    }

    private SDDelayRunnable mShowKeyboardRunnable = new SDDelayRunnable()
    {
        @Override
        public void run()
        {
            SDKeyboardUtil.showKeyboard(et_content);
        }
    };

}
