package com.boyi.shortmessage.widget;

import com.boyi.shortmessage.R;
import com.boyi.shortmessage.activity.LoginActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomDialog extends Dialog implements DialogInterface {
    private static final boolean DEBUG = true; // & Constants.DEBUG;

    private TextView mDefaultBtn;
    private View mLayoutButtons;
    private TextView mOkButton;
    private TextView mCancelButton;
    private TextView mMidButton;
    private TextView mTitleView;
    private ImageView mIcon;
    private Context mContext;

    public CustomDialog(Context context) {
        super(context);
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = context;
        setContentView(R.layout.custom_dialog);
        mLayoutButtons = findViewById(R.id.buttonPanel);
        mOkButton = (TextView) findViewById(R.id.ok);
        mCancelButton = (TextView) findViewById(R.id.cancel);
        mMidButton = (TextView) findViewById(R.id.mid_btn);
        mTitleView = (TextView) findViewById(R.id.title);
        mIcon = (ImageView) findViewById(R.id.icon);

    }

    @Override
    protected void onStart() {
        setDialogSize();
        super.onStart();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,     
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
    }

    public void setEnablePositiveButton(boolean enabled) {
        mOkButton.setEnabled(enabled);
    }

    public void setEnableNegativeButton(boolean enabled) {
        mCancelButton.setEnabled(enabled);
    }

    public void setEnableNeutralButton(boolean enabled) {
        mMidButton.setEnabled(enabled);
    }

    private void setDialogSize() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            WindowManager m = getWindow().getWindowManager();
            Display d = m.getDefaultDisplay();
            DisplayMetrics dm = new DisplayMetrics();
            d.getMetrics(dm);
            WindowManager.LayoutParams p = getWindow().getAttributes();
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            if (height < width) {
                p.width = (int) (d.getHeight() * 0.95);
            } else {
                p.width = (int) (d.getWidth() * 0.95);
            }
            getWindow().setAttributes(p);
        }
    }

    public void setTitle(int resId) {
        findViewById(R.id.topPanel).setVisibility(View.VISIBLE);
        mTitleView.setVisibility(View.VISIBLE);
        mTitleView.setText(resId);
        findViewById(R.id.titleDivider).setVisibility(View.VISIBLE);
    }

    public void setTitle(CharSequence title) {
        findViewById(R.id.topPanel).setVisibility(View.VISIBLE);
        mTitleView.setVisibility(View.VISIBLE);
        mTitleView.setText(title);
        findViewById(R.id.titleDivider).setVisibility(View.VISIBLE);
    }

    /**
     * 
     * @param resId
     *            Resource id (use default app launcher icon if id is 0)
     */
    public void setIcon(int resId) {
        findViewById(R.id.topPanel).setVisibility(View.VISIBLE);
        mIcon.setImageResource(resId);
        mIcon.setVisibility(View.VISIBLE);
    }

    public void setIcon(Drawable icon) {
        findViewById(R.id.topPanel).setVisibility(View.VISIBLE);
        mIcon.setImageDrawable(icon);
        mIcon.setVisibility(View.VISIBLE);
    }

    public void setNoTitleBar() {
        findViewById(R.id.topPanel).setVisibility(View.GONE);
        mTitleView.setVisibility(View.GONE);
    }

    public void setContentView(View view) {
        setView(view);
    }

    public void setMessage(int resId) {
        findViewById(R.id.contentPanel).setVisibility(View.VISIBLE);
        TextView message = (TextView) findViewById(R.id.message);
        message.setText(resId);
    }

    public void setMessage(CharSequence msg) {
        findViewById(R.id.contentPanel).setVisibility(View.VISIBLE);
        TextView message = (TextView) findViewById(R.id.message);
        Spanned text = Html.fromHtml(msg.toString());
        message.setText(text);
    }

    public void setView(View view) {
        if (view != null) {
            FrameLayout custom = (FrameLayout) findViewById(R.id.custom);
            custom.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
        } else {
            findViewById(R.id.customPanel).setVisibility(View.GONE);
        }
    }

    public void setPositiveButton(CharSequence text,
            DialogInterface.OnClickListener clickListener) {
        mLayoutButtons.setVisibility(View.VISIBLE);
        TextView okBtn = mOkButton;
        okBtn.setVisibility(View.VISIBLE);
        setButtonPanelVerticalDivider();

        if (text != null) {
            okBtn.setText(text);
        }

        if (clickListener != null) {
            okBtn.setOnClickListener(new ExternalListener(clickListener, this,
                    DialogInterface.BUTTON_POSITIVE));
        } else {
            okBtn.setOnClickListener(new CloseListener());
        }
    }

    public void setNegativeButton(CharSequence text,
            DialogInterface.OnClickListener clickListener) {
        mLayoutButtons.setVisibility(View.VISIBLE);
        TextView cancelBtn = mCancelButton;
        cancelBtn.setVisibility(View.VISIBLE);
        findViewById(R.id.spacer_cancel).setVisibility(View.VISIBLE);
        setButtonPanelVerticalDivider();

        if (text != null) {
            cancelBtn.setText(text);
        }

        if (clickListener != null) {
            cancelBtn.setOnClickListener(new ExternalListener(clickListener,
                    this, DialogInterface.BUTTON_NEGATIVE));
        } else {
            cancelBtn.setOnClickListener(new CloseListener());
        }
    }

    public void setNeutralButton(CharSequence text,
            DialogInterface.OnClickListener clickListener) {
        mLayoutButtons.setVisibility(View.VISIBLE);
        TextView midBtn = mMidButton;
        midBtn.setVisibility(View.VISIBLE);
        setButtonPanelVerticalDivider();

        if (text != null) {
            midBtn.setText(text);
        }

        if (clickListener != null) {
            midBtn.setOnClickListener(new ExternalListener(clickListener, this,
                    DialogInterface.BUTTON_NEUTRAL));
        } else {
            midBtn.setOnClickListener(new CloseListener());
        }
    }

    public TextView getButton(int whichButton) {
        switch (whichButton) {
        case DialogInterface.BUTTON_POSITIVE:
            return mOkButton;
        case DialogInterface.BUTTON_NEGATIVE:
            return mCancelButton;
        case DialogInterface.BUTTON_NEUTRAL:
            return mMidButton;
        default:
            return null;
        }
    }

    /**
     * Set the default button, which will be focused.
     * 
     * @param defaultBtn
     *            Must be one of {@link #DEFAULT_BTN_LEFT} or
     *            {@link #DEFAULT_BTN_MID} or {@link #DEFAULT_BTN_RIGHT}
     */
    public void setDefaultButton(int whichBtn) {
        if (whichBtn == DialogInterface.BUTTON_POSITIVE) {
            mDefaultBtn = mOkButton;
        } else if (whichBtn == DialogInterface.BUTTON_NEUTRAL) {
            mDefaultBtn = mMidButton;
        } else if (whichBtn == DialogInterface.BUTTON_NEGATIVE) {
            mDefaultBtn = mCancelButton;
        } else {
            mDefaultBtn = null;
        }

        if (mDefaultBtn != null) {
            mDefaultBtn.setSelected(true);
        }
    }

    private void setButtonPanelVerticalDivider() {
        if (mMidButton.getVisibility() == View.VISIBLE) {
            if (mOkButton.getVisibility() == View.VISIBLE) {
                findViewById(R.id.spacer_mid).setVisibility(View.VISIBLE);
            }
            if (mCancelButton.getVisibility() == View.VISIBLE) {
                findViewById(R.id.spacer_cancel).setVisibility(View.VISIBLE);
            }
        } else if (mOkButton.getVisibility() == View.VISIBLE
                && mCancelButton.getVisibility() == View.VISIBLE) {
            findViewById(R.id.spacer_mid).setVisibility(View.VISIBLE);
        }
    }

    public void setWarning(boolean warning) {
        if (warning) {
            mTitleView.setTextColor(0xffc63c3c);
            findViewById(R.id.titleDivider).setBackgroundResource(R.drawable.custom_dialog_title_line_warning);
        } else {
            try {
                mTitleView.setTextColor(mContext.getResources().getColor(
                        R.color.dialog_title_normal_color));
            } catch (NotFoundException e) {
            }
            findViewById(R.id.titleDivider).setBackgroundResource(R.drawable.custom_btn_line);
        }
        mTitleView.setText(mTitleView.getText());

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mDefaultBtn != null) {
            mDefaultBtn.setSelected(false);
            mDefaultBtn = null;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.KEYCODE_SEARCH) {
            return true; // ignore back key & search key
        }
        return super.dispatchKeyEvent(event);
    }

    private class CloseListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            cancel();
        }
    }

    private class ExternalListener implements View.OnClickListener {
        private DialogInterface.OnClickListener mListener;
        private DialogInterface mDialog;
        private int mWhich;

        public ExternalListener(DialogInterface.OnClickListener listener,
                DialogInterface dialog, int which) {
            mListener = listener;
            mDialog = dialog;
            mWhich = which;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(mDialog, mWhich);
            CustomDialog.this.dismiss();
        }

    }

    public static class Builder {
        private final CustomDialogParams P;

        public Builder(Context context) {
            P = new CustomDialogParams(context);
        }

        public Context getContext() {
            return P.mContext;
        }

        public Builder setTitle(int titleId) {
            P.mTitle = P.mContext.getText(titleId);
            return this;
        }

        public Builder setTitle(CharSequence title) {
            P.mTitle = title;
            return this;
        }

        public Builder setMessage(int messageId) {
            P.mMessage = P.mContext.getText(messageId);
            return this;
        }

        public Builder setMessage(CharSequence message) {
            P.mMessage = message;
            return this;
        }

        public Builder setIcon(int iconId) {
            P.mIconId = iconId;
            return this;
        }

        public Builder setIcon(Drawable icon) {
            P.mIcon = icon;
            return this;
        }

        public Builder setIconAttribute(int attrId) {
            TypedValue out = new TypedValue();
            P.mContext.getTheme().resolveAttribute(attrId, out, true);
            P.mIconId = out.resourceId;
            return this;
        }

        public Builder setPositiveButton(int textId,
                final OnClickListener listener) {
            P.mPositiveButtonText = P.mContext.getText(textId);
            P.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text,
                final OnClickListener listener) {
            P.mPositiveButtonText = text;
            P.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(int textId,
                final OnClickListener listener) {
            P.mNegativeButtonText = P.mContext.getText(textId);
            P.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence text,
                final OnClickListener listener) {
            P.mNegativeButtonText = text;
            P.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNeutralButton(int textId,
                final OnClickListener listener) {
            P.mNeutralButtonText = P.mContext.getText(textId);
            P.mNeutralButtonListener = listener;
            return this;
        }

        public Builder setNeutralButton(CharSequence text,
                final OnClickListener listener) {
            P.mNeutralButtonText = text;
            P.mNeutralButtonListener = listener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }

        /**
         * Sets the callback that will be called if a key is dispatched to the
         * dialog.
         * 
         * @return This Builder object to allow for chaining of calls to set
         *         methods
         */
        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            P.mOnKeyListener = onKeyListener;
            return this;
        }

        public Builder setView(View view) {
            P.mView = view;
            return this;
        }

        public Builder setWarning(boolean warning) {
            P.warning = warning;
            return this;
        }

        public CustomDialog create() {
            final CustomDialog dialog = new CustomDialog(P.mContext);
            P.apply(dialog);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setButtonPanelVerticalDivider();
            return dialog;
        }

        public CustomDialog show() {
            CustomDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }

    public static class CustomDialogParams {
        public final Context mContext;
        public final LayoutInflater mInflater;

        public int mIconId = 0;
        public Drawable mIcon;
        public CharSequence mTitle;
        public CharSequence mMessage;
        public CharSequence mPositiveButtonText;
        public DialogInterface.OnClickListener mPositiveButtonListener;
        public CharSequence mNegativeButtonText;
        public DialogInterface.OnClickListener mNegativeButtonListener;
        public CharSequence mNeutralButtonText;
        public DialogInterface.OnClickListener mNeutralButtonListener;
        public boolean mCancelable;
        public DialogInterface.OnCancelListener mOnCancelListener;
        public DialogInterface.OnKeyListener mOnKeyListener;
        public View mView;
        public boolean warning;

        public CustomDialogParams(Context context) {
            mContext = context;
            mCancelable = true;
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void apply(CustomDialog dialog) {
            if (mTitle != null) {
                dialog.setTitle(mTitle);
            }
            if (warning) {
                dialog.setWarning(warning);
            }
            if (mIcon != null) {
                dialog.setIcon(mIcon);
            }
            if (mIconId > 0) {
                dialog.setIcon(mIconId);
            }
            if (mMessage != null) {
                dialog.setMessage(mMessage);
            }
            if (mPositiveButtonText != null) {
                dialog.setPositiveButton(mPositiveButtonText,
                        mPositiveButtonListener);
            }
            if (mNegativeButtonText != null) {
                dialog.setNegativeButton(mNegativeButtonText,
                        mNegativeButtonListener);
            }
            if (mNeutralButtonText != null) {
                dialog.setNeutralButton(mNeutralButtonText,
                        mNeutralButtonListener);
            }
            if (mView != null) {
                dialog.setView(mView);
            }

            dialog.setCancelable(mCancelable);
            dialog.setOnCancelListener(mOnCancelListener);
            if (mOnKeyListener != null) {
                dialog.setOnKeyListener(mOnKeyListener);
            }
        }
    }

    // Utils method
    public static CustomDialog showLoading(Context context, String title,
            final DialogInterface.OnCancelListener cancelListener) {
        CustomDialog dialog = new CustomDialog(context);
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        dialog.setView(inflate.inflate(R.layout.progress_view, null));
        dialog.setTitle(title);
        dialog.setNegativeButton(context.getResources().getString(android.R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (cancelListener != null) {
                            cancelListener.onCancel(dialog);
                        }
                    }
                });
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    // Utils method
    public static CustomDialog showUnCancelableLoading(Context context, String title) {
        CustomDialog dialog = new CustomDialog(context);
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        if (!TextUtils.isEmpty(title)) {
            dialog.setTitle(title);
        }
        dialog.setView(inflate.inflate(R.layout.progress_view, null));
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static CustomDialog showMessageDialog(Context context, String msg) {
        CustomDialog cd = new CustomDialog(context);
        cd.setMessage(msg);
        cd.setPositiveButton(context.getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        cd.setCanceledOnTouchOutside(false);
        cd.show();
        return cd;
    }

    public static CustomDialog showMessageDialog(Context context, int msgID) {
        return showMessageDialog(context, context.getString(msgID));
    }
}
// CHECKSTYLE:ON
