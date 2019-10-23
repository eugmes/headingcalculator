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

import org.debian.eugen.headingcalculator.Calculations.Result
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

import java.util.Random
import kotlin.math.*

data class Inputs(
        val trueCourse: Int,
        val trueAirspeed: Int,
        val windDirection: Int,
        val windSpeed: Int)

/**
 * Test calculations with hand-crafted data.
 */
@RunWith(Parameterized::class)
class CalculationsTest(private val inputs: Inputs, private val expected: Result) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: {0} -> {1}")
        fun data() = listOf(
                arrayOf(Inputs(10, 100, 0,   0), Result(10, 100)),
                arrayOf(Inputs(60, 80,  120, 20), Result(73, 68)),
                arrayOf(Inputs(60, 80,  0,   20), Result(47, 68)),
                arrayOf(Inputs(90, 120, 45,  40), Result(76, 88)))
    }

    @Test
    fun test() {
        val res = with(inputs) {
            Calculations.calcHeadingAndGroundSpeed(trueCourse, trueAirspeed, windDirection, windSpeed)
        }
        assertNotNull(res)
        assertEquals(expected.heading, res!!.heading)
        assertEquals(expected.groundSpeed, res.groundSpeed)
    }
}

/**
 * Test calculations with randomly generated good data.
 */
@RunWith(Parameterized::class)
class CalculationsRandomTest(private val inputs: Inputs) {
    companion object {
        private const val NUM_RANDOM_TESTS = 10000

        @JvmStatic
        @Parameterized.Parameters
        fun data(): Iterable<Inputs> {
            val rn = Random()

            return (1 .. NUM_RANDOM_TESTS).map {
                val trueCourse = rn.nextInt(360)
                val trueAirspeed = rn.nextInt(100) + 30
                val windDirection = rn.nextInt(360)
                val windSpeed = rn.nextInt(20)
                Inputs(trueCourse, trueAirspeed, windDirection, windSpeed)
            }
        }
    }

    @Test
    fun test() {
        with (inputs) {
            val res = Calculations.calcHeadingAndGroundSpeed(trueCourse, trueAirspeed, windDirection, windSpeed)
            assertNotNull(res)

            val trueHeading = res!!.heading
            val groundSpeed = res.groundSpeed

            val airX = trueAirspeed * sin(trueHeading.toDouble().toRadians())
            val airY = trueAirspeed * cos(trueHeading.toDouble().toRadians())
            val groundX = groundSpeed * sin(trueCourse.toDouble().toRadians())
            val groundY = groundSpeed * cos(trueCourse.toDouble().toRadians())
            val windX = windSpeed * sin(windDirection.toDouble().toRadians())
            val windY = windSpeed * cos(windDirection.toDouble().toRadians())

            val residual = hypot(airX - windX - groundX, airY - windY - groundY)

            assertTrue("$inputs -> $res, rest: $residual", residual < 2.0)
        }
    }
}