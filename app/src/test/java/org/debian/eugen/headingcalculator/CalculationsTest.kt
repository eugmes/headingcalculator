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

import org.junit.Assert.*
import org.junit.Test

import java.util.Random
import kotlin.math.*

class CalculationsTest {
    @Test
    fun testNormal() {
        data.forEach { d ->
            val res = Calculations.calcHeadingAndGroundSpeed(d[0], d[1], d[2], d[3])
            assertNotNull(res)
            assertEquals(d[4], res!!.heading)
            assertEquals(d[5], res.groundSpeed)
        }
    }

    @Test
    fun testRandom() {
        val rn = Random()

        for (i in 0..9999) {
            val trueCourse = rn.nextInt(360)
            val trueAirspeed = rn.nextInt(100) + 30
            val windDirection = rn.nextInt(360)
            val windSpeed = rn.nextInt(20)

            val res = Calculations.calcHeadingAndGroundSpeed(trueCourse, trueAirspeed, windDirection, windSpeed)
            assertNotNull(res)

            val trueHeading = res!!.heading
            val groundSpeed = res.groundSpeed

            val airX = trueAirspeed * sin(Math.toRadians(trueHeading.toDouble()))
            val airY = trueAirspeed * cos(Math.toRadians(trueHeading.toDouble()))
            val groundX = groundSpeed * sin(Math.toRadians(trueCourse.toDouble()))
            val groundY = groundSpeed * cos(Math.toRadians(trueCourse.toDouble()))
            val windX = windSpeed * sin(Math.toRadians(windDirection.toDouble()))
            val windY = windSpeed * cos(Math.toRadians(windDirection.toDouble()))

            val rest = hypot(airX - windX - groundX, airY - windY - groundY)

            assertTrue(String.format("%d: %d %d %d %d -> %d %d rest = %f",
                    i, trueCourse, trueAirspeed, windDirection, windSpeed, trueHeading, groundSpeed, rest),
                    rest < 2.0)
        }
    }

    companion object {
        /* Order:          TC, TAS, WD, WS,      TH, GS */
        private val data = arrayOf(
                intArrayOf(10, 100, 0,   0,      10, 100),
                intArrayOf(60, 80,  120, 20,     73, 68),
                intArrayOf(60, 80,  0,   20,     47, 68),
                intArrayOf(90, 120, 45,  40,     76, 88))
    }
}