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

public class FrontRightMotorSubsystem extends SubsystemBase {

  private VictorSPX frontRightDirectionMotor;

  private Encoder frontRightEncoder;

  public static long directionTarget;
  public static long encoderRemainingValue;

  /**
   * Creates a new FrontLeftMotorSubsystem.
   */
  public FrontRightMotorSubsystem() {
    
    frontRightDirectionMotor = new VictorSPX(5);

    frontRightEncoder = new Encoder(4, 5);

    setMinMaxOutput(1, -1);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setMinMaxOutput(double forward, double reverse) {
    frontRightDirectionMotor.configPeakOutputForward(forward);

    frontRightDirectionMotor.configPeakOutputReverse(reverse);
  }

  public int encoderValue() {
    return -frontRightEncoder.get();
  }

  public void moveDirectionMotor(double speed) {
    frontRightDirectionMotor.set(ControlMode.PercentOutput, speed);
  }

  public void swerveDatBoi(double dirX, double dirY) {

    long desiredTarget = directionTargetValue(dirX, dirY);

    if (desiredTarget != 0) {
      if(encoderRemaining(desiredTarget, true) > Constants.swerveRotationError) {
        moveDirectionMotor(Constants.swerveRotationSpeed * (encoderRemaining(desiredTarget, false)/encoderRemaining(desiredTarget, true)));
      } else {
        stopMotors();
      }
    } else {
      if(encoderRemaining(0, true) > Constants.swerveRotationError) {
        moveDirectionMotor(Constants.swerveRotationSpeed * (encoderRemaining(desiredTarget, false)/encoderRemaining(desiredTarget, true)));
      } else {
        stopMotors();
      }
    }
  }
  
  public long encoderRemaining(long targetValue, boolean abs) {
    if (abs) {
      encoderRemainingValue = Math.abs(-targetValue + encoderValue());
    } else {
      encoderRemainingValue = -targetValue + encoderValue();
    }

    return encoderRemainingValue;
  }

  public long directionTargetValue(double x, double y) {
    directionTarget = Math.round(105 * ((((piOverTwo() * (x*y)) / Math.abs(x*y)) ) - Math.atan(y/x)) / piOverTwo());

    return directionTarget;
  }

  public void stopMotors() {
    frontRightDirectionMotor.set(ControlMode.PercentOutput, 0);
  }

  public double piOverTwo() {
    return Math.PI/2;
  }

}
