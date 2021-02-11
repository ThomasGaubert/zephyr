package com.texasgamer.zephyr.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.core.graphics.ColorUtils;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.model.ZephyrCard;
import com.texasgamer.zephyr.model.ZephyrCardType;

/**
 * View for ZephyrCard.
 */
public class ZephyrCardView extends LinearLayout implements View.OnClickListener {

    private View mCardTop;
    private View mCardBottom;
    private TextView mCardTitle;
    private TextView mCardBody;
    private OnClickListener mOnClickListener;

    @ZephyrCardType
    private int mType;

    public ZephyrCardView(Context context) {
        this(context, null);
    }

    public ZephyrCardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZephyrCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ZephyrCardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public static String generateTag(int index) {
        return "ZephyrCard-" + index;
    }

    @Override
    public void onClick(View v) {
        if (mOnClickListener != null) {
            mOnClickListener.onClick(v);
        }
    }

    @ZephyrCardType
    public int getType() {
        return mType;
    }

    public void setType(@ZephyrCardType int type) {
        mType = type;

        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.cardColor, typedValue, true);
        mCardTop.setBackground(createBackground(typedValue.data, false));
        mCardBottom.setBackground(createBackground(ColorUtils.blendARGB(typedValue.data, Color.BLACK, 0.2f), true));
    }

    @NonNull
    public String getTitle() {
        return mCardTitle.getText().toString();
    }

    public void setTitle(@StringRes int title) {
        mCardTitle.setText(title);
    }

    @NonNull
    public String getBody() {
        return mCardBody.getText().toString();
    }

    public void setBody(@StringRes int body) {
        mCardBody.setText(body);
    }

    public void setZephyrCard(@NonNull ZephyrCard card) {
        setTitle(card.getTitle());
        setBody(card.getBody());
        setType(card.getType());

        if (card.getOnClickListener() != null) {
            mOnClickListener = card.getOnClickListener();
            setOnClickListener(this);
            setClickable(true);
        } else {
            mOnClickListener = null;
            setOnClickListener(null);
            setClickable(false);
        }
    }

    private void init() {
        inflate(getContext(), R.layout.view_zephyr_card, this);
        mCardTop = findViewById(R.id.zephyr_card_top);
        mCardBottom = findViewById(R.id.zephyr_card_bottom);
        mCardTitle = findViewById(R.id.zephyr_card_title);
        mCardBody = findViewById(R.id.zephyr_card_body);
    }

    private Resources.Theme getTheme() {
        Resources.Theme theme = getResources().newTheme();
        switch (mType) {
            case ZephyrCardType.DEFAULT:
                theme.applyStyle(R.style.ZephyrCard_Default, false);
                break;
            case ZephyrCardType.INFO:
                theme.applyStyle(R.style.ZephyrCard_Info, false);
                break;
            case ZephyrCardType.SUCCESS:
                theme.applyStyle(R.style.ZephyrCard_Success, false);
                break;
            case ZephyrCardType.WARNING:
                theme.applyStyle(R.style.ZephyrCard_Warning, false);
                break;
            case ZephyrCardType.ERROR:
                theme.applyStyle(R.style.ZephyrCard_Error, false);
                break;
            default:
                theme.applyStyle(R.style.ZephyrCard_Default, false);
                break;
        }

        return theme;
    }

    private Drawable createBackground(@ColorInt int color, boolean isBottom) {
        float cornerRadius = getResources().getDisplayMetrics().density * 8;

        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);

        if (isBottom) {
            background.setCornerRadii(new float[]{0f, 0f, 0f, 0f, cornerRadius, cornerRadius, cornerRadius, cornerRadius});
        } else {
            background.setCornerRadii(new float[]{cornerRadius, cornerRadius, cornerRadius, cornerRadius, 0f, 0f, 0f, 0f});
        }

        background.setColor(color);
        return background;
    }
}
