package com.example.niclam.banana_scaler;

/**
 * CalculationMachine
 * Created by Roger on 3/22/2016.
 *
 * Does backend calculations to calculate the height or the width of the obj.
 */

public class CalculationMachine {

    // User Height
    private double uHeight = 173.74; // Height in cm. Average height of people.
    // Camera height
    private double cHeight = uHeight - 30; // If holding like a digital camera, phone height = uHeight - 30 cm.

    // Getter and Setter for USER_HEIGHT
    public void setUHeight(double height) {
        this.uHeight = height;
    }
    public double getUHeight() {
        return this.uHeight;
    }

    /**
     * getObjectHeight
     *
     * Using geometry to get the height of a distant obj.
     *
     * @param angleA The angle when the camera is pointed at the top of the obj. This value should be in radians.
     * @param angleB The angle when the camera is pointed at the base of the obj. This value should be in radians.
     * @return The obj height in cm.
     */
    public double getObjectHeight(double angleA, double angleB) {
        return cHeight * (Math.tan(angleA) + Math.tan(angleB)) / Math.tan(angleB);
    }

    /**
     * getObjectWidth
     *
     * Using geometry to get the width of a distant obj.
     *
     * @param hDistance The distance from the "base" to second position.
     * @param angleA The angle when the camera is pointed at the "top" of the obj. This value should be in radians.
     * @param angleB The angle when the camera is pointed at the "base" of the obj. This value should be in radians.
     * @return The obj width in cm.
     */
    public double getObjectWidth(double hDistance, double angleA, double angleB) {
        return hDistance * (Math.tan(angleA) + Math.tan(angleB)) / Math.tan(angleB);
    }
}
