/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import frc.robot.Constants;

public class Motor {

  private VictorSPX directionMotor;

  private Encoder encoder;

  private DigitalInput prox;

  public static long directionTarget;
  public static long encoderRemainingValue;

  /**
   * Creates a new FrontLeftMotorSubsystem.
   */
  public Motor(int victorCanId, int encoderPort1, int encoderPort2, int proxPortID) {
    
    directionMotor = new VictorSPX(victorCanId);

    encoder = new Encoder(encoderPort1, encoderPort2);

    prox = new DigitalInput(proxPortID);

    setMinMaxOutput(1, -1);

  }

  /**

   * The main method in this class.
   
   * This takes the inputs "dirX" and "dirY", the direction stick's X and the direction stick's Y
   * values respectivly, and converts them to an angle respective to a released stick, (0,0).
   
   * It then converts those into a desired encoder count (between -105 and 105 ticks, or -90 and
   * 90 degrees).

   * With the target count, it then compares the absolute value of that to an error count, effectively
   * creating a error of + or - the error count away from the target.

   * It then runs the motor the direction required, calculated by dividing the remaining encoder count
   * by it's absolute value, returning either 1 or -1.

   * Then once the motor has either reached the error or is within the error it stops.

   * If the joystick is ever released, if the desired target == 0, it sets the target value to 0,
   * returning to the wheel's forward.

  */

  public void swerveDatBoi(double dirX, double dirY) {

    long desiredTarget;

    if (dirX == 1 && dirY == 0) {
      desiredTarget = 104;
    } else {
      desiredTarget = directionTargetValue(dirX, dirY);
    }

    if (encoderRemaining(desiredTarget, true) < Constants.smallSwerveRotationError) {
      stopMotors();
    } else if (encoderRemaining(desiredTarget, true) < Constants.largeSwerveRotationError) {
      moveMotor(Constants.slowSwerveRotationSpeed * (encoderRemaining(desiredTarget, false)/encoderRemaining(desiredTarget, true)));
    } else {
      moveMotor(Constants.fastSwerveRotationSpeed * (encoderRemaining(desiredTarget, false)/encoderRemaining(desiredTarget, true)));
    }

  }

  /**
   * This method is a basic method to ensure the wheel move at a desired max power in both directions.
  */

  private void setMinMaxOutput(double forward, double reverse) {

    directionMotor.configPeakOutputForward(forward);
    directionMotor.configPeakOutputReverse(reverse);

  }

  public void zeroEncoderBasedOnProx() {
    if (proxValue()) {
      zeroEncoder();
    }
  }

  public void zeroEncoder() {
    encoder.reset();
  }



  public boolean proxValue() {
    return !prox.get();
  }

  /**
   * This just returns the current count the encoder is producing.
  */

  public int encoderValue() {
    return encoder.get();
  }

  /**
   * This just sets the speed of the motors to a desired speed. 
   */

  private void moveMotor(double speed) {
    directionMotor.set(ControlMode.PercentOutput, speed);
  }

  //TODO: why do we do so much inversion?
  /**
   * This asks for 2 things, the target value, and if the user wants the absoulte value of the item.
   * 
   * With the target value, the method just takes the target value and subtracts the current encoder value.
   */
  
  private long encoderRemaining(long targetValue, boolean abs) {

    if (abs) {
      encoderRemainingValue = Math.abs(targetValue - encoderValue());
    } else {
      encoderRemainingValue = targetValue - encoderValue();
    }

    return encoderRemainingValue;
  }

  /**
   * This is where the real math happens. 
   * 
   * What this does in the end is:
   * converts the x and y cordinates into an angle, using inverse tanget,
   * subtracts that from:
   *   pi/2 or -pi/2 
   *   (pi/2 multiplied by either 1 or -1 based on the quadrant we are in: 1 & 3 = 1, 2 & 4 = -1)
   * divides that by pi/2 to get a ratio out of pi/2 radians, 90 degrees
   * multiplies that ratio by 105 to get a desired count out of 105 ticks
   * 
   */

  private long directionTargetValue(double x, double y) { 

    directionTarget = -Math.round(105 * ( ( (piOverTwo() * (x*y)) / Math.abs(x*y) ) - Math.atan(y/x)) / piOverTwo());

    return directionTarget;

  }

  /**
   * Just sets the motor values to 0, stoping them.
   * This could be done by setting the speed to 0 with the previous method, but this just gives us a 
   * method to do only one thing, stop everything.
   */

  private void stopMotors() {
    directionMotor.set(ControlMode.PercentOutput, 0);
  }

  /**
   * Just a simple method to give us pi/2 in a more understandable way.
   */

  private double piOverTwo() {
    return Math.PI/2;
  }

}
