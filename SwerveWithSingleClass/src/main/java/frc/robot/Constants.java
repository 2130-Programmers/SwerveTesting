/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    public static int largeSwerveRotationError = 10;
    public static int smallSwerveRotationError = 2;
    public static double slowSwerveRotationSpeed = 0.1;
    public static double fastSwerveRotationSpeed = 0.5;

    public static int leftX = 0;
    public static int leftY = 1;
    public static int rightX = 4;
    public static int rightY = 5;
    public static int leftTrigger = 2;
    public static int rightTrigger = 3;

    public static int strafeEasyModeButton = 5;
    public static int pointTurnButon = 6;

    public static double pointSpeed = 0.2;

}
