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

import android.test.suitebuilder.annotation.SmallTest;
import android.util.Pair;
import junit.framework.TestCase;

import java.util.Random;

public class CalculationsTest extends TestCase {
    public CalculationsTest() {
        super(CalculationsTest.class.getName());
    }

    /* Order: TC, TAS, WD, WS,      TH, GS */
    static private final int[][] data = {
            {10, 100, 0, 0,    10, 100},
            {60, 80, 120, 20,  73, 68},
            {60, 80, 0, 20,    47, 68}
    };

    @SmallTest
    public void testNormal() throws Exception {
        for (int[] d : data) {
            Pair<Integer, Integer> res = Calculations.calcHeadingAndGroundSpeed(d[0], d[1], d[2], d[3]);
            assertEquals(d[4], (int) res.first);
            assertEquals(d[5], (int) res.second);
        }
    }

    @SmallTest
    public void testRandom() throws Exception {
        Random rn = new Random();

        for (int i = 0; i < 10000; i++) {
            int TC = rn.nextInt(360);
            int TAS = rn.nextInt(100) + 30;
            int WD = rn.nextInt(360);
            int WS = rn.nextInt(20);

            Pair<Integer, Integer> res = Calculations.calcHeadingAndGroundSpeed(TC, TAS, WD, WS);
            int TH = res.first;
            int GS = res.second;

            double airX = TAS * Math.sin(Math.toRadians(TH));
            double airY = TAS * Math.cos(Math.toRadians(TH));
            double groundX = GS * Math.sin(Math.toRadians(TC));
            double groundY = GS * Math.cos(Math.toRadians(TC));
            double windX = WS * Math.sin(Math.toRadians(WD));
            double windY = WS * Math.cos(Math.toRadians(WD));

            double rest = Math.hypot(airX - windX - groundX, airY - windY - groundY);

            assertTrue(String.format("%d: %d %d %d %d -> %d %d rest = %f", i, TC, TAS, WD, WS, TH, GS, rest), rest < 2.0);
        }
    }
}