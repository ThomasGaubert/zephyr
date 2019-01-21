package com.texasgamer.zephyr.view;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.texasgamer.zephyr.R;
import com.texasgamer.zephyr.model.ZephyrCard;
import com.texasgamer.zephyr.model.ZephyrCardType;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ZephyrCardView extends LinearLayout implements View.OnClickListener {

    @BindView(R.id.zephyr_card_accent)
    View cardAccent;
    @BindView(R.id.zephyr_card_title)
    TextView cardTitle;
    @BindView(R.id.zephyr_card_body)
    TextView cardBody;
    OnClickListener mOnClickListener;

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
        getTheme().resolveAttribute(R.attr.cardAccentColor, typedValue, true);
        cardAccent.setBackgroundColor(typedValue.data);
    }

    @NonNull
    public String getTitle() {
        return cardTitle.getText().toString();
    }

    public void setTitle(@StringRes int title) {
        cardTitle.setText(title);
    }

    @NonNull
    public String getBody() {
        return cardBody.getText().toString();
    }

    public void setBody(@StringRes int body) {
        cardBody.setText(body);
    }

    public void setZephyrCard(@NonNull ZephyrCard card) {
        setTitle(card.getTitle());
        setBody(card.getBody());
        setType(card.getType());

        if (card.getOnClickListener() != null) {
            mOnClickListener = card.getOnClickListener();
            setOnClickListener(this);
        }
    }

    private void init() {
        inflate(getContext(), R.layout.zephyr_card, this);
        ButterKnife.bind(this);
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
}
