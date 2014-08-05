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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

@SuppressWarnings("WeakerAccess")
public class CalculatorKeypad extends LinearLayout {
    private OnKeypadClickListener mOnKeypadClickListener;

    public interface OnKeypadClickListener {
        public void onKeypadClick(KeypadButton keypadButton);
    }

    public CalculatorKeypad(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.calculator_keypad, this);

        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                KeypadButton keypadButton = (KeypadButton)view.getTag();

                if (mOnKeypadClickListener != null)
                    mOnKeypadClickListener.onKeypadClick(keypadButton);
            }
        };

        for (int i = 0; i < mIds.length; i++)
            initButton(mIds[i], mKeypadButtons[i], onClickListener);
    }

    public void setOnKeypadClickListener(OnKeypadClickListener onKeypadClickListener) {
        mOnKeypadClickListener = onKeypadClickListener;
    }

    private void initButton(int id, KeypadButton keypadButton, OnClickListener onClickListener) {
        Button btn = (Button)findViewById(id);

        btn.setTag(keypadButton);
        btn.setOnClickListener(onClickListener);
    }

    static private final int mIds[] = {
            R.id.digit0,
            R.id.digit1,
            R.id.digit2,
            R.id.digit3,
            R.id.digit4,
            R.id.digit5,
            R.id.digit6,
            R.id.digit7,
            R.id.digit8,
            R.id.digit9,

            R.id.clear_btn,
            R.id.backspace_btn,

            R.id.true_course_btn,
            R.id.true_airspeed_btn,
            R.id.wind_direction_btn,
            R.id.wind_speed_btn,
    };

    static private final KeypadButton mKeypadButtons[] = {
            KeypadButton.ZERO,
            KeypadButton.ONE,
            KeypadButton.TWO,
            KeypadButton.THREE,
            KeypadButton.FOUR,
            KeypadButton.FIVE,
            KeypadButton.SIX,
            KeypadButton.SEVEN,
            KeypadButton.EIGHT,
            KeypadButton.NINE,

            KeypadButton.CLEAR,
            KeypadButton.DELETE,

            KeypadButton.TRUE_COURSE,
            KeypadButton.TRUE_AIRSPEED,
            KeypadButton.WIND_DIRECTION,
            KeypadButton.WIND_SPEED
    };
}
