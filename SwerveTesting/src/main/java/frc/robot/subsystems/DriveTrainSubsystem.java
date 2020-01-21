/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DriveTrainSubsystem extends SubsystemBase {

  private VictorSPX frontLeftDirectionMotor;
  private VictorSPX frontRightDirectionMotor;
  private VictorSPX rearLeftDirectionMotor;
  private VictorSPX rearRightDirectionMotor;

  private TalonFX frontLeftDriveMotor;
  private TalonFX frontRightDriveMotor;
  private TalonFX rearLeftDriveMotor;
  private TalonFX rearRightDriveMotor;

  private Encoder frontLeftEncoder;
  private Encoder frontRightEncoder;
  private Encoder rearLeftEncoder;
  private Encoder rearRightEncoder;

  private long directionTarget;
  private long encoderRemainingValue;

  private DigitalInput prox;

  //public static String motors[] = {"FrontLeft", "RearRight", "RearLeft", "FrontRight"};


  /**
   * Creates a new DriveTrainSubsystem.
   */
  public DriveTrainSubsystem() {

    frontRightDirectionMotor = new VictorSPX(5);
    rearLeftDirectionMotor = new VictorSPX(1);
    rearRightDirectionMotor = new VictorSPX(3);

    frontLeftDriveMotor = new TalonFX(6);
    frontRightDriveMotor = new TalonFX(4);
    rearLeftDriveMotor = new TalonFX(0);
    rearRightDriveMotor = new TalonFX(2);


    frontRightEncoder = new Encoder(4, 5);
    rearLeftEncoder = new Encoder(0, 1);
    rearRightEncoder = new Encoder(2, 3);

    prox = new DigitalInput(8);

    setMinMaxOutput(1, -1);
    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setMinMaxOutput(double forward, double reverse) {
    frontLeftDirectionMotor.configPeakOutputForward(forward);
    frontRightDirectionMotor.configPeakOutputForward(forward);
    rearLeftDirectionMotor.configPeakOutputForward(forward);
    rearRightDirectionMotor.configPeakOutputForward(forward);

    frontLeftDirectionMotor.configPeakOutputReverse(reverse);
    frontRightDirectionMotor.configPeakOutputReverse(reverse);
    rearLeftDirectionMotor.configPeakOutputReverse(reverse);
    rearRightDirectionMotor.configPeakOutputReverse(reverse);
  }

  public void moveMotors(double fLSpeed, double fRSpeed, double rLSpeed, double rRSpeed) {

    setMinMaxOutput(1, -1);

    frontLeftDirectionMotor.set(ControlMode.PercentOutput, fLSpeed);
    frontRightDirectionMotor.set(ControlMode.PercentOutput, fRSpeed);
    rearLeftDirectionMotor.set(ControlMode.PercentOutput, rLSpeed);
    rearRightDirectionMotor.set(ControlMode.PercentOutput, rRSpeed);
  }



  public void moveDriveMotors(double speed) {
    frontLeftDriveMotor.set(ControlMode.PercentOutput, speed);
    frontRightDriveMotor.set(ControlMode.PercentOutput, speed);
    rearLeftDriveMotor.set(ControlMode.PercentOutput, speed);
    rearRightDriveMotor.set(ControlMode.PercentOutput, speed);

    frontLeftDriveMotor.setInverted(true);
    frontRightDriveMotor.setInverted(false);
    rearLeftDriveMotor.setInverted(true);
    rearRightDriveMotor.setInverted(false);
  }

  public void stopMotors() {
    frontLeftDirectionMotor.set(ControlMode.PercentOutput, 0);
    frontRightDirectionMotor.set(ControlMode.PercentOutput, 0);
    rearLeftDirectionMotor.set(ControlMode.PercentOutput, 0);
    rearRightDirectionMotor.set(ControlMode.PercentOutput, 0);
  }

  public int frontLeftEncoderValue() {
    return -frontLeftEncoder.get();
  }

  public int frontRightEncoderValue() {
    return -frontRightEncoder.get();
  }

  public int rearLeftEncoderValue() {
    return -rearLeftEncoder.get();
  }

  public int rearRightEncoderValue() {
    return -rearRightEncoder.get();
  }

  public void moveFLDirectionMotor(double speed) {
    frontLeftDirectionMotor.set(ControlMode.PercentOutput, speed);
  }

  public void moveFRDirectionMotor(double speed) {
    frontRightDirectionMotor.set(ControlMode.PercentOutput, speed);
  }

  public void moveRRDirectionMotor(double speed) {
    rearRightDirectionMotor.set(ControlMode.PercentOutput, speed);
  }

  public void moveRLDirectionMotor(double speed) {
    rearLeftDirectionMotor.set(ControlMode.PercentOutput, speed);
  }

  public void startup() {
    frontRightEncoder.reset();
    frontLeftEncoder.reset();
    rearRightEncoder.reset();
    rearLeftEncoder.reset();
    encoderRemainingValue = 0;
  }

  public void swerveDatBoi(double dirX, double dirY, double speX) {

    moveDriveMotors(speX);

    long desiredTarget = directionTargetValue(dirX, dirY);

    if (desiredTarget != 0) {
      if(encoderRemaining(desiredTarget, "FrontLeft", true) > Constants.swerveRotationError) {
        moveFLDirectionMotor(Constants.swerveRotationSpeed * (encoderRemaining(desiredTarget, "FrontLeft", false)/encoderRemaining(desiredTarget, "FrontLeft", true)));
      } else {
        stopMotors();
      }

      if(encoderRemaining(desiredTarget, "RearRight", true) > Constants.swerveRotationError) {
        moveRRDirectionMotor(Constants.swerveRotationSpeed * (encoderRemaining(desiredTarget, "RearRight", false)/encoderRemaining(desiredTarget, "RearRight", true)));
      } else {
        stopMotors();
      }

      /*if(encoderRemaining(desiredTarget, "RearLeft", true) > Constants.swerveRotationError) {
        moveDirectionalMotor("RearLeft", Constants.swerveRotationSpeed * (encoderRemaining(desiredTarget, "RearLeft", false)/encoderRemaining(desiredTarget, "RearLeft", true)));
      } else {
        stopMotors();
      }

      if(encoderRemaining(desiredTarget, "FrontRight", true) > Constants.swerveRotationError) {
        moveDirectionalMotor("FrontRight", Constants.swerveRotationSpeed * (encoderRemaining(desiredTarget, "FrontRight", false)/encoderRemaining(desiredTarget, "FrontRight", true)));
      } else {
        stopMotors();
      }*/
    } else {
      if(encoderRemaining(0, "FrontLeft", true) > Constants.swerveRotationError) {
        moveFLDirectionMotor(Constants.swerveRotationSpeed * (encoderRemaining(desiredTarget, "FrontLeft", false)/encoderRemaining(desiredTarget, "FrontLeft", true)));
      } else {
        stopMotors();
      }

      if(encoderRemaining(0, "RearRight", true) > Constants.swerveRotationError) {
        moveRRDirectionMotor(Constants.swerveRotationSpeed * (encoderRemaining(desiredTarget, "RearRight", false)/encoderRemaining(desiredTarget, "RearRight", true)));
      } else {
        stopMotors();
      }

      /*if(encoderRemaining(0, "RearLeft", true) > Constants.swerveRotationError) {
        moveDirectionalMotor("RearLeft", Constants.swerveRotationSpeed * (encoderRemaining(desiredTarget, "RearLeft", false)/encoderRemaining(desiredTarget, "RearLeft", true)));
      } else {
        stopMotors();
      }

      if(encoderRemaining(0, "FrontRight", true) > Constants.swerveRotationError) {
        moveDirectionalMotor("FrontRight", Constants.swerveRotationSpeed * (encoderRemaining(desiredTarget, "FrontRight", false)/encoderRemaining(desiredTarget, "FrontRight", true)));
      } else {
        stopMotors();
      }*/
    }
  }

  public long encoderRemaining(long targetValue, String encoder, boolean abs) {

    if (encoder == "FrontLeft") {
      if (abs) {
        encoderRemainingValue = Math.abs(-targetValue + frontLeftEncoderValue());
      } else {
        encoderRemainingValue = -targetValue + frontLeftEncoderValue();
      }
    } else {
      if (abs) {
        encoderRemainingValue = Math.abs(-targetValue + rearRightEncoderValue());
      } else {
        encoderRemainingValue = -targetValue + rearRightEncoderValue();
      }
    }

    
    return encoderRemainingValue;
  }

  public long returnERV() {
    return encoderRemainingValue;
  }

  public long directionTargetValue(double x, double y) {
    directionTarget = Math.round(105 * ((((piOverTwo() * (x*y)) / Math.abs(x*y)) ) - Math.atan(y/x)) / piOverTwo());

    return directionTarget;
  }

  public double piOverTwo() {
    return Math.PI/2;
  }

  public boolean returnProx() {
    return prox.get();
  }

}
