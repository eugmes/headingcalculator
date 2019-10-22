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
    private var mEraseOnInput = true

    private fun processKeypadInput(keypadButton: KeypadButton) {
        var value: Int

        when (keypadButton) {
            KeypadButton.DELETE -> {
                value = display.currentValue
                value /= 10
                display.currentValue = value
                mEraseOnInput = false
            }
            KeypadButton.CLEAR -> display.currentValue = 0
            KeypadButton.TRUE_COURSE -> {
                display.setInput(TRUE_COURSE)
                mEraseOnInput = true
            }
            KeypadButton.TRUE_AIRSPEED -> {
                display.setInput(TRUE_AIRSPEED)
                mEraseOnInput = true
            }
            KeypadButton.WIND_DIRECTION -> {
                display.setInput(WIND_DIRECTION)
                mEraseOnInput = true
            }
            KeypadButton.WIND_SPEED -> {
                display.setInput(WIND_SPEED)
                mEraseOnInput = true
            }
            else -> {
                if (mEraseOnInput) {
                    display.currentValue = 0
                    mEraseOnInput = false
                }

                value = display.currentValue
                if (value < 100) {
                    val digit = keypadButton.value
                    val newVal = value * 10 + digit
                    display.currentValue = newVal
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        if (savedInstanceState != null) {
            display.onRestoreInstanceState(savedInstanceState)
        }

        keypad.setOnKeypadClickListener(object : CalculatorKeypad.OnKeypadClickListener {
            override fun onKeypadClick(keypadButton: KeypadButton) {
                processKeypadInput(keypadButton)
            }
        })
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)

        display.onSaveInstanceState(savedInstanceState)
    }
}
