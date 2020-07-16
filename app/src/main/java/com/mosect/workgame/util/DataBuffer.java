package com.mosect.workgame.util;

import java.util.Arrays;

public class DataBuffer {

    private int[] intData;
    private int intIndex = 0;
    private long[] longData;
    private int longIndex;
    private float[] floatData;
    private int floatIndex;
    private double[] doubleData;
    private int doubleIndex;
    private Object[] objectData;
    private int objectIndex;

    public void resetPosition() {
        intIndex = 0;
        longIndex = 0;
        floatIndex = 0;
        doubleIndex = 0;
        objectIndex = 0;
    }

    public void putInt(int value) {
        if (null == intData) {
            intData = new int[4];
        }
        if (intIndex >= intData.length) {
            intData = Arrays.copyOf(intData, intData.length * 2);
        }
        intData[intIndex++] = value;
    }

    public int getInt() {
        return intData[intIndex++];
    }

    public void putLong(long value) {
        if (null == longData) {
            longData = new long[4];
        }
        if (longIndex >= longData.length) {
            longData = Arrays.copyOf(longData, longData.length * 2);
        }
        longData[longIndex++] = value;
    }

    public long getLong() {
        return longData[longIndex++];
    }

    public void putFloat(float value) {
        if (null == floatData) {
            floatData = new float[4];
        }
        if (floatIndex >= floatData.length) {
            floatData = Arrays.copyOf(floatData, floatData.length * 2);
        }
        floatData[floatIndex++] = value;
    }

    public float getFloat() {
        return floatData[floatIndex++];
    }

    public void putDouble(double value) {
        if (null == doubleData) {
            doubleData = new double[4];
        }
        if (doubleIndex >= doubleData.length) {
            doubleData = Arrays.copyOf(doubleData, doubleData.length * 2);
        }
        doubleData[doubleIndex++] = value;
    }

    public double getDouble() {
        return doubleData[doubleIndex++];
    }

    public void putObject(Object value) {
        if (null == objectData) {
            objectData = new Object[4];
        }
        if (objectIndex >= objectData.length) {
            objectData = Arrays.copyOf(objectData, objectData.length * 2);
        }
        objectData[objectIndex++] = value;
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject() {
        return (T) objectData[objectIndex++];
    }
}
