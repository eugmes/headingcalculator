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
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout

class CalculatorKeypad(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {
    private var mOnKeypadClickListener: OnKeypadClickListener? = null

    internal interface OnKeypadClickListener {
        fun onKeypadClick(keypadButton: KeypadButton)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.calculator_keypad, this)

        val onClickListener = OnClickListener { view ->
            val keypadButton = view.tag as KeypadButton

            mOnKeypadClickListener?.onKeypadClick(keypadButton)
        }

        mIds.indices.forEach { i ->
            initButton(mIds[i], mKeypadButtons[i], onClickListener)
        }
    }

    internal fun setOnKeypadClickListener(onKeypadClickListener: OnKeypadClickListener) {
        mOnKeypadClickListener = onKeypadClickListener
    }

    private fun initButton(id: Int, keypadButton: KeypadButton, onClickListener: OnClickListener) {
        val btn = findViewById<Button>(id)

        btn.tag = keypadButton
        btn.setOnClickListener(onClickListener)
    }

    companion object {
        private val mIds = intArrayOf(
                R.id.digit0, R.id.digit1, R.id.digit2,
                R.id.digit3, R.id.digit4, R.id.digit5,
                R.id.digit6, R.id.digit7, R.id.digit8,
                R.id.digit9, R.id.clear_btn, R.id.backspace_btn,
                R.id.true_course_btn, R.id.true_airspeed_btn,
                R.id.wind_direction_btn, R.id.wind_speed_btn)

        private val mKeypadButtons = arrayOf(
                KeypadButton.ZERO, KeypadButton.ONE, KeypadButton.TWO,
                KeypadButton.THREE, KeypadButton.FOUR, KeypadButton.FIVE,
                KeypadButton.SIX, KeypadButton.SEVEN, KeypadButton.EIGHT,
                KeypadButton.NINE, KeypadButton.CLEAR, KeypadButton.DELETE,
                KeypadButton.TRUE_COURSE, KeypadButton.TRUE_AIRSPEED,
                KeypadButton.WIND_DIRECTION, KeypadButton.WIND_SPEED)
    }
}
