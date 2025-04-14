package com.Axonix.index.model;

import android.content.Context;

import androidx.annotation.StringRes;

import com.Axonix.index.R;

public enum DisabilityType {
    NONE(0, R.string.none),
    VISION(1, R.string.vision_impairment),
    HEARING(2, R.string.hearing_impairment),
    OTHER(3, R.string.other_disability);

    private final int value;
    private final int displayRes;

    DisabilityType(int value, @StringRes int displayRes) {
        this.value = value;
        this.displayRes = displayRes;
    }

    public int getValue() {
        return value;
    }

    public String getDisplayText(Context context) {
        return context.getString(displayRes);
    }

    public static DisabilityType fromValue(int value) {
        for (DisabilityType type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        return NONE;
    }

    public static DisabilityType fromPosition(int position) {
        DisabilityType[] values = values();
        if (position >= 0 && position < values.length) {
            return values[position];
        }
        return NONE;
    }

    public static String[] getDisplayValues(Context context) {
        DisabilityType[] types = values();
        String[] displayValues = new String[types.length];
        for (int i = 0; i < types.length; i++) {
            displayValues[i] = types[i].getDisplayText(context);
        }
        return displayValues;
    }
}