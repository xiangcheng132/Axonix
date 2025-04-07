// InfoItem.java
package com.Axonix.usermain;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;

public class InfoItem extends LinearLayout {

    private TextView tvTitle, tvContent;
    private EditText etContent;
    private boolean isEditMode = false;
    private EditText editText;

    public InfoItem(Context context) {
        super(context);
        init(context, null);
    }

    public View getContentView() {
        return findViewById(R.id.tv_content);
    }

    public InfoItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.info_item, this, true);
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        etContent = findViewById(R.id.et_content);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.InfoItem);
            String title = ta.getString(R.styleable.InfoItem_title);
            if (title != null) {
                tvTitle.setText(title);
            }
            isEditMode = ta.getBoolean(R.styleable.InfoItem_editable, false);
            float textSize = ta.getDimension(R.styleable.InfoItem_textSize, 14 * getResources().getDisplayMetrics().scaledDensity);
            ta.recycle();
            tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            etContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
        setEditMode(isEditMode);
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
        tvContent.setVisibility(editMode ? View.GONE : View.VISIBLE);
        etContent.setVisibility(editMode ? View.VISIBLE : View.GONE);

        getContentView().setClickable(editMode);
        etContent.setFocusable(editMode);
        etContent.setFocusableInTouchMode(editMode);

    }


    public void setContent(String text) {
        tvContent.setText(text);
        if (!isEditMode) {
            etContent.setText(text);
        }
    }

    public String getEditText() {
        return etContent.getText().toString().trim();
    }
    public void setEditable(boolean editable) {
        setEditMode(editable);
    }

}