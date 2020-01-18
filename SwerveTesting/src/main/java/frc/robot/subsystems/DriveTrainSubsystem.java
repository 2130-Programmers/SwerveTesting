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

  public static String motors[] = {"FrontLeft", "RearRight", "RearLeft", "FrontRight"};


  /**
   * Creates a new DriveTrainSubsystem.
   */
  public DriveTrainSubsystem() {

    frontLeftDirectionMotor = new VictorSPX(7);
    frontRightDirectionMotor = new VictorSPX(5);
    rearLeftDirectionMotor = new VictorSPX(1);
    rearRightDirectionMotor = new VictorSPX(3);

    frontLeftDriveMotor = new TalonFX(6);
    frontRightDriveMotor = new TalonFX(4);
    rearLeftDriveMotor = new TalonFX(0);
    rearRightDriveMotor = new TalonFX(2);

    frontLeftEncoder = new Encoder(6, 7);
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

  public int encoderValue(String encoder) {
    int returnValue = 0;
    if (encoder == motors[0] || encoder == motors[1] || encoder == motors[2] || encoder == motors[3]) {
      switch (encoder) {
        case "FrontLeft":
          returnValue = frontLeftEncoder.get();
          //break;
        case "RearRight":
          returnValue = rearRightEncoder.get();
          //break;
        case "RearLeft":
          returnValue = rearLeftEncoder.get();
          //break;
        //case "FrontRight":
          //returnValue = frontRightEncoder.get();
          //break;
        //default:
        //  returnValue = 0;
      }
    }

    return -returnValue;
  }

  public void moveDirectionalMotor(String motor, double speed) {
    switch (motor) {
      case "FrontLeft":
        frontLeftDirectionMotor.set(ControlMode.PercentOutput, speed);
        //break;
      case "RearRight":
        rearRightDirectionMotor.set(ControlMode.PercentOutput, speed);
        //break;
      case "RearLeft":
        rearLeftDirectionMotor.set(ControlMode.PercentOutput, speed);
        //break;
      case "FrontRight":
        frontRightDirectionMotor.set(ControlMode.PercentOutput, speed); 
        //break;
    }
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
    

    if (desiredTarget == 0) {
      //TODO: Make this based on if found the prox yet not if target = 0
      for (int i = 0; i < 4; i++) {
        if(encoderRemaining(0, motors[i], true) > Constants.swerveRotationError) {
          moveDirectionalMotor(motors[i], Constants.swerveRotationSpeed * (encoderRemaining(desiredTarget, motors[i], false)/encoderRemaining(desiredTarget, motors[i], true)));
        } else {
          stopMotors();
        }
      }
    } else{
      for (int i = 0; i < 4; i++) {
        if(encoderRemaining(desiredTarget, motors[i], true) > Constants.swerveRotationError) {
          moveDirectionalMotor(motors[i], Constants.swerveRotationSpeed * (encoderRemaining(desiredTarget, motors[i], false)/encoderRemaining(desiredTarget, motors[i], true)));
        } else {
          stopMotors();
        }
      }
    }

    

  }

  public long encoderRemaining(long targetValue, String encoder, boolean abs) {

    if (abs) {
      encoderRemainingValue = Math.abs(-targetValue + encoderValue(encoder));
    } else {
      encoderRemainingValue = -targetValue + encoderValue(encoder);
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
