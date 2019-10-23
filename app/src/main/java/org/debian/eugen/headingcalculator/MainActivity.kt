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

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.main.*

import org.debian.eugen.headingcalculator.CalculatorDisplay.InputType.*

class MainActivity : Activity() {
    /** Indicates that the current value should be erased on next digital input.  */
    private var eraseOnInput = true

    private fun processKeypadInput(keypadButton: KeypadButton) {
        when (keypadButton) {
            KeypadButton.DELETE -> {
                display.currentValue = display.currentValue / 10
                eraseOnInput = false
            }
            KeypadButton.CLEAR -> display.currentValue = 0
            KeypadButton.TRUE_COURSE -> {
                display.currentInput = TRUE_COURSE
                eraseOnInput = true
            }
            KeypadButton.TRUE_AIRSPEED -> {
                display.currentInput = TRUE_AIRSPEED
                eraseOnInput = true
            }
            KeypadButton.WIND_DIRECTION -> {
                display.currentInput = WIND_DIRECTION
                eraseOnInput = true
            }
            KeypadButton.WIND_SPEED -> {
                display.currentInput = WIND_SPEED
                eraseOnInput = true
            }
            else -> {
                if (eraseOnInput) {
                    display.currentValue = 0
                    eraseOnInput = false
                }

                val value = display.currentValue
                if (value < 100) {
                    val digit = keypadButton.value
                    display.currentValue = value * 10 + digit
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        savedInstanceState?.let {
            display.onRestoreInstanceState(it)
        }

        keypad.onKeypadClickListener = { processKeypadInput(it) }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)

        display.onSaveInstanceState(savedInstanceState)
    }
}
