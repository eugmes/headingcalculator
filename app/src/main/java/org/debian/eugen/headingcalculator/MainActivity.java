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

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import static org.debian.eugen.headingcalculator.CalculatorDisplay.InputType.*;

@SuppressWarnings("WeakerAccess")
public final class MainActivity extends Activity {
    /** Indicates that the current value should be erased on next digital input. */
    private boolean mEraseOnInput = true;

    private CalculatorDisplay mCalculatorDisplay;

    private void processKeypadInput(KeypadButton keypadButton) {
        int val;

        switch (keypadButton) {
            case DELETE:
                val = mCalculatorDisplay.getCurrentValue();
                val /= 10;
                mCalculatorDisplay.setCurrentValue(val);
                mEraseOnInput = false;
                break;
            case CLEAR:
                mCalculatorDisplay.setCurrentValue(0);
                break;
            case TRUE_COURSE:
                mCalculatorDisplay.setInput(TRUE_COURSE);
                mEraseOnInput = true;
                break;
            case TRUE_AIRSPEED:
                mCalculatorDisplay.setInput(TRUE_AIRSPEED);
                mEraseOnInput = true;
                break;
            case WIND_DIRECTION:
                mCalculatorDisplay.setInput(WIND_DIRECTION);
                mEraseOnInput = true;
                break;
            case WIND_SPEED:
                mCalculatorDisplay.setInput(WIND_SPEED);
                mEraseOnInput = true;
                break;
            default:
                if (mEraseOnInput) {
                    mCalculatorDisplay.setCurrentValue(0);
                    mEraseOnInput = false;
                }

                val = mCalculatorDisplay.getCurrentValue();
                if (val < 100) {
                    int digit = keypadButton.getValue();
                    int newVal = val * 10 + digit;
                    mCalculatorDisplay.setCurrentValue(newVal);
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mCalculatorDisplay = findViewById(R.id.display);

        if (savedInstanceState != null) {
            mCalculatorDisplay.onRestoreInstanceState(savedInstanceState);
        }

        CalculatorKeypad calculatorKeypad = findViewById(R.id.keypad);

        calculatorKeypad.setOnKeypadClickListener(new CalculatorKeypad.OnKeypadClickListener() {
            @Override
            public void onKeypadClick(KeypadButton keypadButton) {
                processKeypadInput(keypadButton);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        mCalculatorDisplay.onSaveInstanceState(savedInstanceState);
    }
}
