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

import android.util.Pair;

class Calculations {
    /**
     * Performs true heading and ground speed calculation.
     *
     * @param trueCourse True course in degrees
     * @param trueAirspeed True air speed
     * @param windDirection Wind direction (direction from where the wind is blowing) in degrees
     * @param windSpeed Wind speed
     * @return pair comprising of true heading and ground speed values (int that order) or null
     *         if there is no solution.
     */
    static Pair<Integer, Integer> calcHeadingAndGroundSpeed(int trueCourse, int trueAirspeed,
                                                            int windDirection, int windSpeed) {
        /* Make initial display look better. */
        if ((trueAirspeed == 0) && (windSpeed == 0))
            return new Pair<>(0, 0);

        final double cosTCmW = Math.cos(Math.toRadians(trueCourse - windDirection));
        final double sinTCmW = Math.sin(Math.toRadians(trueCourse - windDirection));

        final double groundSpeed = - windSpeed * cosTCmW +
                Math.sqrt(trueAirspeed * trueAirspeed - windSpeed * windSpeed * sinTCmW * sinTCmW);

        if (Double.isNaN(groundSpeed) || (groundSpeed <= 0))
            return null;

        final double radTC = Math.toRadians(trueCourse);
        final double radW = Math.toRadians(windDirection);

        double radTH = Math.atan2(groundSpeed * Math.sin(radTC) + windSpeed * Math.sin(radW),
                groundSpeed * Math.cos(radTC) + windSpeed * Math.cos(radW));

        final int trueHeading = ((int) Math.round(Math.toDegrees(radTH)) + 360) % 360;

        /* Round the speed down to be safe when calculating fuel consumption. */
        return new Pair<>(trueHeading, (int) Math.floor(groundSpeed));
    }
}
