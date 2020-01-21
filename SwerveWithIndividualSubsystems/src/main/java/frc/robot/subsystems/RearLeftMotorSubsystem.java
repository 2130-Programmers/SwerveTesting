/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class RearLeftMotorSubsystem extends SubsystemBase {
  private VictorSPX rearLeftDirectionMotor;

  private Encoder rearLeftEncoder;

  public static long directionTarget;
  public static long encoderRemainingValue;

  /**
   * Creates a new FrontLeftMotorSubsystem.
   */
  public RearLeftMotorSubsystem() {
    
    rearLeftDirectionMotor = new VictorSPX(1);

    rearLeftEncoder = new Encoder(0, 1);

    setMinMaxOutput(1, -1);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setMinMaxOutput(double forward, double reverse) {
    rearLeftDirectionMotor.configPeakOutputForward(forward);

    rearLeftDirectionMotor.configPeakOutputReverse(reverse);
  }

  public int encoderValue() {
    return -rearLeftEncoder.get();
  }

  public void moveDirectionMotor(double speed) {
    rearLeftDirectionMotor.set(ControlMode.PercentOutput, speed);
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
    rearLeftDirectionMotor.set(ControlMode.PercentOutput, 0);
  }

  public double piOverTwo() {
    return Math.PI/2;
  }
}
