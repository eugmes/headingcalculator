/*
 * Copyright (C) 2014, 2019 Ievgenii Meshcheriakov
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
package org.debian.eugen.headingcalculator

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.calculator_display.view.*

import java.util.Locale

class CalculatorDisplay(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {

    private var mCurrentView: TextView? = null
    /** Currently active input field ID.  */
    private var mCurrentInput = InputType.TRUE_COURSE

    private var mTrueCourse: Int = 0
    private var mTrueAirspeed: Int = 0
    private var mWindDirection: Int = 0
    private var mWindSpeed: Int = 0

    /**
     * Returns the currently edited value.
     */
    var currentValue: Int
        get() {
            return when (mCurrentInput) {
                InputType.TRUE_COURSE -> mTrueCourse
                InputType.TRUE_AIRSPEED -> mTrueAirspeed
                InputType.WIND_DIRECTION -> mWindDirection
                InputType.WIND_SPEED -> mWindSpeed
            }
        }
        set(newValue) {
            when (mCurrentInput) {
                InputType.TRUE_COURSE -> mTrueCourse = newValue
                InputType.TRUE_AIRSPEED -> mTrueAirspeed = newValue
                InputType.WIND_DIRECTION -> mWindDirection = newValue
                InputType.WIND_SPEED -> mWindSpeed = newValue
            }

            mCurrentView!!.text = formatNumber(newValue)
            updateValues()
        }

    /**
     * Enumeration for various input fields.
     */
    enum class InputType {
        TRUE_COURSE,
        TRUE_AIRSPEED,
        WIND_DIRECTION,
        WIND_SPEED
    }

    /**
     * Activates a new input field.
     *
     * @param what Field to activate
     */
    fun setInput(what: InputType) {
        mCurrentView?.isSelected = false

        mCurrentInput = what
        mCurrentView = when (what) {
            InputType.TRUE_COURSE -> true_course
            InputType.TRUE_AIRSPEED -> true_airspeed
            InputType.WIND_DIRECTION -> wind_angle
            InputType.WIND_SPEED -> wind_speed
        }

        mCurrentView?.isSelected = true
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.calculator_display, this)

        initializeDisplay()
    }

    private fun formatNumber(number: Int): String {
        return String.format(Locale.US, "%d", number)
    }

    /**
     * Calculates output values from inputs and updates the display.
     */
    private fun updateValues() {
        val res = Calculations.calcHeadingAndGroundSpeed(
                mTrueCourse, mTrueAirspeed,
                mWindDirection, mWindSpeed)

        if (res == null) {
            true_heading.setText(R.string.undefined_field)
            ground_speed.setText(R.string.undefined_field)
        } else {
            true_heading.text = formatNumber(res.heading)
            ground_speed.text = formatNumber(res.groundSpeed)
        }
    }

    fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putInt("TrueCourse", mTrueCourse)
        savedInstanceState.putInt("TrueAirspeed", mTrueAirspeed)
        savedInstanceState.putInt("WindDirection", mWindDirection)
        savedInstanceState.putInt("WindSpeed", mWindSpeed)
        savedInstanceState.putInt("Input", mCurrentInput.ordinal)
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        mTrueCourse = savedInstanceState.getInt("TrueCourse")
        mTrueAirspeed = savedInstanceState.getInt("TrueAirspeed")
        mWindDirection = savedInstanceState.getInt("WindDirection")
        mWindSpeed = savedInstanceState.getInt("WindSpeed")

        mCurrentInput = try {
            InputType.values()[savedInstanceState.getInt("Input")]
        } catch (e: ArrayIndexOutOfBoundsException) {
            InputType.TRUE_COURSE
        }

        initializeDisplay()
    }

    private fun initializeDisplay() {
        setInput(mCurrentInput)

        if (isInEditMode) {
            true_course.text = formatNumber(888)
            true_airspeed.text = formatNumber(888)
            wind_angle.text = formatNumber(888)
            wind_speed.text = formatNumber(888)

            true_heading.text = formatNumber(888)
            /* In case of unrealistic inputs speed result may have 4 digits. */
            ground_speed.text = formatNumber(1888)
        } else {
            true_course.text = formatNumber(mTrueCourse)
            true_airspeed.text = formatNumber(mTrueAirspeed)
            wind_angle.text = formatNumber(mWindDirection)
            wind_speed.text = formatNumber(mWindSpeed)

            updateValues()
        }
    }
}
