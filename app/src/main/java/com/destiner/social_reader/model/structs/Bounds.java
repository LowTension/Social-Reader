package com.destiner.social_reader.model.structs;

public class Bounds {
    private int upper;
    private int lower;

    public Bounds(int upper, int lower) {
        this.upper = upper;
        this.lower = lower;
    }

    public int getUpper() {
        return upper;
    }

    public int getLower() {
        return lower;
    }

    public boolean isValid() {
        return upper >= 0 && lower >= 0;
    }
}
