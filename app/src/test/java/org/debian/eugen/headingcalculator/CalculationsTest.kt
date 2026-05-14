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

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.util.Random
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

private const val NUM_RANDOM_TESTS = 10000

class CalculationsTest {
    /**
     * Test calculations with hand-crafted data.
     */
    @ParameterizedTest
    @CsvSource(
        useHeadersInDisplayName = true,
        textBlock =
            """TC, TAS, WD,  WS, TH, GS
               10, 100, 0,   0,  10, 100
               60, 80,  120, 20, 73, 68
               60, 80,  0,   20, 47, 68
               90, 120, 45,  40, 76, 88""")
    fun staticTests(
        trueCourse: Int,
        trueAirspeed: Int,
        windDirection: Int,
        windSpeed: Int,
        heading: Int,
        groundSpeed: Int
    ) {
        val res = Calculations.calcHeadingAndGroundSpeed(
            trueCourse,
            trueAirspeed,
            windDirection,
            windSpeed
        )
        assertNotNull(res)
        assertEquals(heading, res!!.heading)
        assertEquals(groundSpeed, res.groundSpeed)
    }
}

class RandomTests {
    /**
     * Test calculations with randomly generated good data.
     */
    @TestFactory
    fun randomInputs(): Iterator<DynamicTest> {
        val random = Random()
        return generateSequence(0) { it + 1 }
            .take(NUM_RANDOM_TESTS)
            .map { index ->
                val trueCourse = random.nextInt(360)
                val trueAirspeed = random.nextInt(100) + 30
                val windDirection = random.nextInt(360)
                val windSpeed = random.nextInt(20)

                DynamicTest.dynamicTest("random case #$index: TC=$trueCourse, TAS=$trueAirspeed, WD=$windDirection, WS=$windSpeed") {
                    val res = Calculations.calcHeadingAndGroundSpeed(
                        trueCourse,
                        trueAirspeed,
                        windDirection,
                        windSpeed
                    )
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

                    assertTrue(residual < 2.0, "residual: $residual")
                }
            }
            .iterator()
    }
}
