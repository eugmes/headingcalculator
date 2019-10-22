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

    private var currentView: TextView? = null

    private var _currentInput = InputType.TRUE_COURSE

    /** Currently active input field ID.  */
    var currentInput : InputType
        get() = _currentInput
        set(newValue) {
            currentView?.isSelected = false

            _currentInput = newValue
            currentView = when (newValue) {
                InputType.TRUE_COURSE -> true_course
                InputType.TRUE_AIRSPEED -> true_airspeed
                InputType.WIND_DIRECTION -> wind_angle
                InputType.WIND_SPEED -> wind_speed
            }

            currentView?.isSelected = true
        }

    private var trueCourse: Int = 0
    private var trueAirspeed: Int = 0
    private var windDirection: Int = 0
    private var windSpeed: Int = 0

    /**
     * Currently edited value.
     */
    var currentValue: Int
        get() = when (currentInput) {
            InputType.TRUE_COURSE -> trueCourse
            InputType.TRUE_AIRSPEED -> trueAirspeed
            InputType.WIND_DIRECTION -> windDirection
            InputType.WIND_SPEED -> windSpeed
        }
        set(newValue) {
            when (currentInput) {
                InputType.TRUE_COURSE -> trueCourse = newValue
                InputType.TRUE_AIRSPEED -> trueAirspeed = newValue
                InputType.WIND_DIRECTION -> windDirection = newValue
                InputType.WIND_SPEED -> windSpeed = newValue
            }

            currentView!!.text = formatNumber(newValue)
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

    init {
        LayoutInflater.from(context).inflate(R.layout.calculator_display, this)

        initializeDisplay()
    }

    private fun formatNumber(number: Int) = String.format(Locale.US, "%d", number)

    /**
     * Calculates output values from inputs and updates the display.
     */
    private fun updateValues() {
        val res = Calculations.calcHeadingAndGroundSpeed(
                trueCourse, trueAirspeed,
                windDirection, windSpeed)

        if (res == null) {
            true_heading.setText(R.string.undefined_field)
            ground_speed.setText(R.string.undefined_field)
        } else {
            true_heading.text = formatNumber(res.heading)
            ground_speed.text = formatNumber(res.groundSpeed)
        }
    }

    fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putInt("TrueCourse", trueCourse)
        savedInstanceState.putInt("TrueAirspeed", trueAirspeed)
        savedInstanceState.putInt("WindDirection", windDirection)
        savedInstanceState.putInt("WindSpeed", windSpeed)
        savedInstanceState.putInt("Input", currentInput.ordinal)
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        trueCourse = savedInstanceState.getInt("TrueCourse")
        trueAirspeed = savedInstanceState.getInt("TrueAirspeed")
        windDirection = savedInstanceState.getInt("WindDirection")
        windSpeed = savedInstanceState.getInt("WindSpeed")

        currentInput = try {
            InputType.values()[savedInstanceState.getInt("Input")]
        } catch (e: ArrayIndexOutOfBoundsException) {
            InputType.TRUE_COURSE
        }

        initializeDisplay()
    }

    private fun initializeDisplay() {
        currentInput = _currentInput

        if (isInEditMode) {
            true_course.text = formatNumber(888)
            true_airspeed.text = formatNumber(888)
            wind_angle.text = formatNumber(888)
            wind_speed.text = formatNumber(888)

            true_heading.text = formatNumber(888)
            /* In case of unrealistic inputs speed result may have 4 digits. */
            ground_speed.text = formatNumber(1888)
        } else {
            true_course.text = formatNumber(trueCourse)
            true_airspeed.text = formatNumber(trueAirspeed)
            wind_angle.text = formatNumber(windDirection)
            wind_speed.text = formatNumber(windSpeed)

            updateValues()
        }
    }
}
