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

import kotlin.math.*

internal object Calculations {
    internal data class Result(val heading: Int, val groundSpeed: Int)

    /**
     * Performs true heading and ground speed calculation.
     *
     * @param trueCourse True course in degrees
     * @param trueAirspeed True air speed
     * @param windDirection Wind direction (direction from where the wind is blowing) in degrees
     * @param windSpeed Wind speed
     * @return true heading and ground speed values or null if there is no solution.
     */
    fun calcHeadingAndGroundSpeed(trueCourse: Int, trueAirspeed: Int,
                                  windDirection: Int, windSpeed: Int): Result? {
        /* Make initial display look better. */
        if (trueAirspeed == 0 && windSpeed == 0)
            return Result(0, 0)

        val fTrueCourse = trueCourse.toDouble()
        val fTrueAirspeed = trueAirspeed.toDouble()
        val fWindDirection = windDirection.toDouble()
        val fWindSpeed = windSpeed.toDouble()

        val cosTCmW = cos(Math.toRadians(fTrueCourse - fWindDirection))
        val sinTCmW = sin(Math.toRadians(fTrueCourse - fWindDirection))

        val fGroundSpeed = (-fWindSpeed * cosTCmW + sqrt((fTrueAirspeed * fTrueAirspeed) - (fWindSpeed * fWindSpeed * sinTCmW * sinTCmW)))

        if (fGroundSpeed.isNaN() || fGroundSpeed <= 0.0)
            return null

        val radTC = Math.toRadians(fTrueCourse)
        val radW = Math.toRadians(fWindDirection)

        val radTH = atan2(fGroundSpeed * sin(radTC) + fWindSpeed * sin(radW),
                          fGroundSpeed * cos(radTC) + fWindSpeed * cos(radW))

        val trueHeading = (round(Math.toDegrees(radTH)).toInt() + 360) % 360

        /* Round the speed down to be safe when calculating fuel consumption. */
        return Result(trueHeading, floor(fGroundSpeed).toInt())
    }
}
