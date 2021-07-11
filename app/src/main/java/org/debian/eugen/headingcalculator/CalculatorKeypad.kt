/*
 * Copyright (C) Ievgenii Meshcheriakov
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
import android.support.annotation.IdRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout

internal enum class KeypadButton(@IdRes val id: Int, val value: Int = 0) {
    DIGIT0(R.id.digit0, 0),
    DIGIT1(R.id.digit1, 1),
    DIGIT2(R.id.digit2, 2),
    DIGIT3(R.id.digit3, 3),
    DIGIT4(R.id.digit4, 4),
    DIGIT5(R.id.digit5, 5),
    DIGIT6(R.id.digit6, 6),
    DIGIT7(R.id.digit7, 7),
    DIGIT8(R.id.digit8, 8),
    DIGIT9(R.id.digit9, 9),
    CLEAR(R.id.clear_btn),
    DELETE(R.id.backspace_btn),
    TRUE_COURSE(R.id.true_course_btn),
    TRUE_AIRSPEED(R.id.true_airspeed_btn),
    WIND_DIRECTION(R.id.wind_direction_btn),
    WIND_SPEED(R.id.wind_speed_btn);
}

class CalculatorKeypad(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {
    internal var onKeypadClickListener: ((button: KeypadButton) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.calculator_keypad, this)

        val onClickListener = OnClickListener { view ->
            val keypadButton = view.tag as KeypadButton

            onKeypadClickListener?.invoke(keypadButton)
        }

        KeypadButton.values().forEach { button ->
            findViewById<Button>(button.id).apply {
                tag = button
                setOnClickListener(onClickListener)
            }
        }
    }
}
