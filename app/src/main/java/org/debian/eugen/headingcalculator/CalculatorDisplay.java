/*
 * Copyright (C) 2014 Eugeniy Meshcheryakov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.debian.eugen.headingcalculator;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressWarnings("WeakerAccess")
public class CalculatorDisplay extends LinearLayout {
    /**
     * Enumeration for various input fields.
     */
    public enum InputType {
        TRUE_COURSE,
        TRUE_AIRSPEED,
        WIND_DIRECTION,
        WIND_SPEED
    }

    private TextView mCurrentView;
    /** Currently active input field ID. */
    private InputType mCurrentInput = InputType.TRUE_COURSE;

    private int mTrueCourse;
    private int mTrueAirspeed;
    private int mWindDirection;
    private int mWindSpeed;

    /* Editable views */
    private final TextView mTrueCourseView;
    private final TextView mTrueAirspeedView;
    private final TextView mWindAngleView;
    private final TextView mWindSpeedView;

    /* Result views */
    private final TextView mTrueHeadingView;
    private final TextView mGroundSpeedView;

    /**
     * Activates a new input field.
     *
     * @param what Field to activate
     */
    public void setInput(InputType what) {
        if (mCurrentView != null)
            mCurrentView.setSelected(false);

        mCurrentInput = what;
        switch (what) {
            case TRUE_COURSE:
                mCurrentView = mTrueCourseView;
                break;
            case TRUE_AIRSPEED:
                mCurrentView = mTrueAirspeedView;
                break;
            case WIND_DIRECTION:
                mCurrentView = mWindAngleView;
                break;
            case WIND_SPEED:
                mCurrentView = mWindSpeedView;
                break;
        }

        mCurrentView.setSelected(true);
    }

    /**
     * Returns the currently edited value.
     */
    public int getCurrentValue() {
        switch (mCurrentInput) {
            case TRUE_COURSE:
                return mTrueCourse;
            case TRUE_AIRSPEED:
                return mTrueAirspeed;
            case WIND_DIRECTION:
                return mWindDirection;
            case WIND_SPEED:
                return mWindSpeed;
        }

        return 0;
    }

    public void setCurrentValue(int newValue) {
        switch (mCurrentInput) {
            case TRUE_COURSE:
                mTrueCourse = newValue;
                break;
            case TRUE_AIRSPEED:
                mTrueAirspeed = newValue;
                break;
            case WIND_DIRECTION:
                mWindDirection = newValue;
                break;
            case WIND_SPEED:
                mWindSpeed = newValue;
                break;
        }

        mCurrentView.setText(formatNumber(newValue));
        updateValues();
    }

    public CalculatorDisplay(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.calculator_display, this);

        mTrueCourseView = (TextView)findViewById(R.id.true_course);
        mTrueAirspeedView = (TextView)findViewById(R.id.true_airspeed);
        mWindAngleView = (TextView)findViewById(R.id.wind_angle);
        mWindSpeedView = (TextView)findViewById(R.id.wind_speed);

        mTrueHeadingView = (TextView)findViewById(R.id.true_heading);
        mGroundSpeedView = (TextView)findViewById(R.id.ground_speed);

        initializeDisplay();
    }

    private static String formatNumber(int number) {
        return String.format("%d", number);
    }

    /**
     * Calculates output values from inputs and updates the display.
     */
    private void updateValues() {
        Pair<Integer, Integer> res
                = Calculations.calcHeadingAndGroundSpeed(mTrueCourse, mTrueAirspeed,
                                                         mWindDirection, mWindSpeed);

        if (res == null) {
            mTrueHeadingView.setText(R.string.undefined_field);
            mGroundSpeedView.setText(R.string.undefined_field);
        } else {
            mTrueHeadingView.setText(formatNumber(res.first));
            mGroundSpeedView.setText(formatNumber(res.second));
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("TrueCourse", mTrueCourse);
        savedInstanceState.putInt("TrueAirspeed", mTrueAirspeed);
        savedInstanceState.putInt("WindDirection", mWindDirection);
        savedInstanceState.putInt("WindSpeed", mWindSpeed);
        savedInstanceState.putInt("Input", mCurrentInput.ordinal());
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mTrueCourse = savedInstanceState.getInt("TrueCourse");
        mTrueAirspeed = savedInstanceState.getInt("TrueAirspeed");
        mWindDirection = savedInstanceState.getInt("WindDirection");
        mWindSpeed = savedInstanceState.getInt("WindSpeed");

        try {
            mCurrentInput = InputType.values()[savedInstanceState.getInt("Input")];
        } catch (ArrayIndexOutOfBoundsException e) {
            mCurrentInput = InputType.TRUE_COURSE;
        }

        initializeDisplay();
    }

    private void initializeDisplay() {
        setInput(mCurrentInput);

        if (isInEditMode()) {
            mTrueCourseView.setText(formatNumber(888));
            mTrueAirspeedView.setText(formatNumber(888));
            mWindAngleView.setText(formatNumber(888));
            mWindSpeedView.setText(formatNumber(888));

            mTrueHeadingView.setText(formatNumber(888));
            /* In case of unrealistic inputs speed result may have 4 digits. */
            mGroundSpeedView.setText(formatNumber(1888));
        } else {
            mTrueCourseView.setText(formatNumber(mTrueCourse));
            mTrueAirspeedView.setText(formatNumber(mTrueAirspeed));
            mWindAngleView.setText(formatNumber(mWindDirection));
            mWindSpeedView.setText(formatNumber(mWindSpeed));

            updateValues();
        }
    }
}
