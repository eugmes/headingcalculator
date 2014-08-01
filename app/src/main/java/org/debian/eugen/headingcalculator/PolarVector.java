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

/**
 * Class for vectors in polar coordinates.
 */
class PolarVector {
    private final double mLength;
    private final double mAngle;

    public PolarVector(double length, double angle) {
        mLength = length;
        mAngle = angle;
    }

    /** Returns the vector direction. */
    public double getAngle() {
        return mAngle;
    }

    /** Returns the vector length. */
    public double getLength() {
        return mLength;
    }

    /**
     * Adds the vector to another.
     *
     * @param other The vector to add to current vector.
     * @return Sum of this vector and the other one.
     */
    public PolarVector add(PolarVector other) {
        double a1 = mAngle * Math.PI / 180;
        double a2 = other.mAngle * Math.PI / 180;

        double x1 = mLength * Math.cos(a1);
        double y1 = mLength * Math.sin(a1);

        double x2 = other.mLength * Math.cos(a2);
        double y2 = other.mLength * Math.sin(a2);

        double xr = x1 + x2;
        double yr = y1 + y2;

        double r = Math.hypot(xr, yr);
        double a = Math.atan2(yr, xr) * 180 / Math.PI;

        if (r < 0) {
            r = -r;

            if (a > 0)
                a -= 180;
            else
                a += 180;
        }

        if (a > 360)
            a -= 360;
        else if (a < 0)
            a += 360;

        return new PolarVector(r, a);
    }
}
