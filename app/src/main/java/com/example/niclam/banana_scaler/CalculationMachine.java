package com.example.niclam.banana_scaler;

/**
 * Created by Roger on 3/22/2016.
 */

public class CalculationMachine {
    final static double USER_HEIGHT = 173.74; // Height in cm. Average height of people.
    final static double CAMERA_HEIGHT = USER_HEIGHT - 30; // If holding like a digital camera, phone height = uHeight - 30 cm.

    public double getObjectHeight(double angleA, double angleB) {
        return CAMERA_HEIGHT * (Math.tan(angleA) + Math.tan(angleB)) / Math.tan(angleB);
    }

    public double getObjectWidth() {
        //TODO
        return 0;
    }
}
