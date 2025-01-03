package com.mobprog.artlymobile.utils;

import android.text.InputFilter;
import android.text.Spanned;

public class RangeInputFilter implements InputFilter {
    private final int min, max;

    public RangeInputFilter(int min, int max) {
        boolean rightOrder = min <= max;
        this.min = rightOrder ? min : max;
        this.max = rightOrder ? max : min;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dStart, int dEnd) {
        try {
            String sourceStr = source.toString();
            String destStr = dest.toString();
            String result = destStr.substring(0, dStart) + sourceStr.substring(start, end) + destStr.substring(dEnd);
            int input = Integer.parseInt(result);
            if (min <= input && input <= max) return null;
        } catch (NumberFormatException ignored) { }
        return "";
    }
}
