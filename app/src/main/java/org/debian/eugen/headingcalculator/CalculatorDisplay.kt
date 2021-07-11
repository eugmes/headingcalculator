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
import org.debian.eugen.headingcalculator.databinding.CalculatorDisplayBinding

import java.util.Locale

private const val KEY_TRUE_COURSE = "TrueCourse"
private const val KEY_TRUE_AIRSPEED = "TrueAirspeed"
private const val KEY_WIND_DIRECTION = "WindDirection"
private const val KEY_WIND_SPEED = "WindSpeed"
private const val KEY_INPUT = "Input"

class CalculatorDisplay(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {
    private val binding: CalculatorDisplayBinding =
        CalculatorDisplayBinding.inflate(LayoutInflater.from(context), this, true)

    private var currentView: TextView? = null

    private var _currentInput = InputType.TRUE_COURSE

    /** Currently active input field ID.  */
    var currentInput: InputType
        get() = _currentInput
        set(newValue) {
            currentView?.isSelected = false

            _currentInput = newValue
            currentView = when (newValue) {
                InputType.TRUE_COURSE -> binding.trueCourse
                InputType.TRUE_AIRSPEED -> binding.trueAirspeed
                InputType.WIND_DIRECTION -> binding.windAngle
                InputType.WIND_SPEED -> binding.windSpeed
            }.apply {
                isSelected = true
            }
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
        initializeDisplay()
    }

    private fun formatNumber(number: Int) = String.format(Locale.US, "%d", number)

    /**
     * Calculates output values from inputs and updates the display.
     */
    private fun updateValues() {
        when (val res = Calculations.calcHeadingAndGroundSpeed(
            trueCourse,
            trueAirspeed,
            windDirection,
            windSpeed
        )) {
            null -> {
                binding.trueHeading.setText(R.string.undefined_field)
                binding.groundSpeed.setText(R.string.undefined_field)
            }
            else -> {
                binding.trueHeading.text = formatNumber(res.heading)
                binding.groundSpeed.text = formatNumber(res.groundSpeed)
            }
        }
    }

    fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putInt(KEY_TRUE_COURSE, trueCourse)
        savedInstanceState.putInt(KEY_TRUE_AIRSPEED, trueAirspeed)
        savedInstanceState.putInt(KEY_WIND_DIRECTION, windDirection)
        savedInstanceState.putInt(KEY_WIND_SPEED, windSpeed)
        savedInstanceState.putInt(KEY_INPUT, currentInput.ordinal)
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        trueCourse = savedInstanceState.getInt(KEY_TRUE_COURSE)
        trueAirspeed = savedInstanceState.getInt(KEY_TRUE_AIRSPEED)
        windDirection = savedInstanceState.getInt(KEY_WIND_DIRECTION)
        windSpeed = savedInstanceState.getInt(KEY_WIND_SPEED)

        currentInput = try {
            InputType.values()[savedInstanceState.getInt(KEY_INPUT)]
        } catch (e: ArrayIndexOutOfBoundsException) {
            InputType.TRUE_COURSE
        }

        initializeDisplay()
    }

    private fun initializeDisplay() {
        currentInput = _currentInput

        if (isInEditMode) {
            binding.trueCourse.text = formatNumber(888)
            binding.trueAirspeed.text = formatNumber(888)
            binding.windAngle.text = formatNumber(888)
            binding.windSpeed.text = formatNumber(888)

            binding.trueHeading.text = formatNumber(888)
            /* In case of unrealistic inputs speed result may have 4 digits. */
            binding.groundSpeed.text = formatNumber(1888)
        } else {
            binding.trueCourse.text = formatNumber(trueCourse)
            binding.trueAirspeed.text = formatNumber(trueAirspeed)
            binding.windAngle.text = formatNumber(windDirection)
            binding.windSpeed.text = formatNumber(windSpeed)

            updateValues()
        }
    }
}
