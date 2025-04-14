package com.Axonix.index.model;

import android.content.Context;

import androidx.annotation.StringRes;

import com.Axonix.index.R;

public enum Gender {
    UNKNOWN(0, R.string.unknown),
    MALE(1, R.string.male),
    FEMALE(2, R.string.female);

    private final int value;
    private final int displayRes;

    Gender(int value, @StringRes int displayRes) {
        this.value = value;
        this.displayRes = displayRes;
    }

    public int getValue() {
        return value;
    }

    public String getDisplayText(Context context) {
        return context.getString(displayRes);
    }

    public static Gender fromValue(int value) {
        for (Gender gender : values()) {
            if (gender.value == value) {
                return gender;
            }
        }
        return UNKNOWN;
    }

    public static Gender fromPosition(int position) {
        Gender[] values = values();
        if (position >= 0 && position < values.length) {
            return values[position];
        }
        return UNKNOWN;
    }

    public static String[] getDisplayValues(Context context) {
        Gender[] genders = values();
        String[] displayValues = new String[genders.length];
        for (int i = 0; i < genders.length; i++) {
            displayValues[i] = genders[i].getDisplayText(context);
        }
        return displayValues;
    }
}