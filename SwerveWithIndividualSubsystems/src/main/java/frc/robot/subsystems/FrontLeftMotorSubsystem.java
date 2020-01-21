/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class FrontLeftMotorSubsystem extends SubsystemBase {

  private VictorSPX frontLeftDirectionMotor;

  private Encoder frontLeftEncoder;

  public static long directionTarget;
  public static long encoderRemainingValue;

  /**
   * Creates a new FrontLeftMotorSubsystem.
   */
  public FrontLeftMotorSubsystem() {
    
    frontLeftDirectionMotor = new VictorSPX(7);

    frontLeftEncoder = new Encoder(6, 7);

    setMinMaxOutput(1, -1);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
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

    long desiredTarget = directionTargetValue(dirX, dirY);
    double absY = Math.abs(dirY);

    if (desiredTarget != 0) {
      if (absY < 0.1) {
        desiredTarget = directionTargetValue(dirX, absY);
      } else {
        desiredTarget = directionTargetValue(dirX, dirY);
      }
    } else {
      desiredTarget = 0;
    }

    if(encoderRemaining(desiredTarget, true) > Constants.swerveRotationError) {
      moveFLDirectionMotor(Constants.swerveRotationSpeed * (encoderRemaining(desiredTarget, false)/encoderRemaining(desiredTarget, true)));
    } else {
      stopMotors();
    }

  }

  /**
   * This method is a basic method to ensure the wheel move at a desired max power in both directions.
  */

  public void setMinMaxOutput(double forward, double reverse) {

    frontLeftDirectionMotor.configPeakOutputForward(forward);
    frontLeftDirectionMotor.configPeakOutputReverse(reverse);

  }

  /**
   * This just returns the current count the encoder is producing.
  */

  public int encoderValue() {
    return -frontLeftEncoder.get();
  }

  /**
   * This just sets the speed of the motors to a desired speed. 
   */

  public void moveFLDirectionMotor(double speed) {
    frontLeftDirectionMotor.set(ControlMode.PercentOutput, speed);
  }

  //TODO: why do we do so much inversion?
  /**
   * This asks for 2 things, the target value, and if the user wants the absoulte value of the item.
   * 
   * With the target value, the method just takes the target value and subtracts the current encoder value.
   */
  
  public long encoderRemaining(long targetValue, boolean abs) {
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

  public long directionTargetValue(double x, double y) { 

    return Math.round(105 * ( ( (piOverTwo() * (x*y)) / Math.abs(x*y) ) - Math.atan(y/x)) / piOverTwo());

  }

  /**
   * Just sets the motor values to 0, stoping them.
   * This could be done by setting the speed to 0 with the previous method, but this just gives us a 
   * method to do only one thing, stop everything.
   */

  public void stopMotors() {
    frontLeftDirectionMotor.set(ControlMode.PercentOutput, 0);
  }

  /**
   * Just a simple method to give us pi/2 in a more understandable way.
   * 
   */

  public double piOverTwo() {
    return Math.PI/2;
  }

}
