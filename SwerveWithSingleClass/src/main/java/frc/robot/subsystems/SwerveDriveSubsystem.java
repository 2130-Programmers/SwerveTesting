/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SwerveDriveSubsystem extends SubsystemBase {

  private TalonFX frontLeftDriveMotor;
  private TalonFX frontRightDriveMotor;
  private TalonFX rearLeftDriveMotor;
  private TalonFX rearRightDriveMotor;

  public Motor motors[];
  public String motorNames[];

  private double desiredSpeed;

  private double limelightX;

  /**
   * Creates a new SwerveDriveSubsystem   
   */

  public SwerveDriveSubsystem() {

    motors[0] = new Motor(7, 16, 17, 3);
    motors[1] = new Motor(5, 14, 15, 2);
    motors[2] = new Motor(1, 12, 13, 0);
    motors[3] = new Motor(3, 10, 11, 1);    
    
    frontLeftDriveMotor = new TalonFX(6);
    frontRightDriveMotor = new TalonFX(4);
    rearLeftDriveMotor = new TalonFX(0);
    rearRightDriveMotor = new TalonFX(2);

    motorNames[0] = "FL";
    motorNames[1] = "FR";
    motorNames[2] = "RL";
    motorNames[3] = "RR";


  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    frontLeftDriveMotor.setInverted(true);
    frontRightDriveMotor.setInverted(false);
    rearLeftDriveMotor.setInverted(true);
    rearRightDriveMotor.setInverted(false);
  }

  public void moveSwerveAxis(double leftX, double leftY, 
                             double rightX, double rightY, 
                             double leftT, double rightT) {
    leftSwerves(leftX, leftY); 
    
    if (Math.abs(leftX - rightX) < 0.15 && Math.abs(leftY = rightY) < 0.15) {
      rightSwerves(leftX, leftY);
    } else {
      rightSwerves(rightX, rightY);
    }

    desiredSpeed = findSpeed(leftT, rightT);
    
    moveDriveMotors(desiredSpeed);
  }

  public void moveSwervePointTurn(double leftT, double rightT) {
    motors[0].swerveDatBoi(0.8, 0.6);
    motors[1].swerveDatBoi(-0.8, 0.6);
    motors[2].swerveDatBoi(-0.8, 0.6);
    motors[3].swerveDatBoi(0.8, 0.6);

    double appliedSpeed = findSpeed(leftT, rightT);

    frontLeftDriveMotor.set(ControlMode.PercentOutput, appliedSpeed);
    frontRightDriveMotor.set(ControlMode.PercentOutput, -appliedSpeed);
    rearLeftDriveMotor.set(ControlMode.PercentOutput, appliedSpeed);
    rearRightDriveMotor.set(ControlMode.PercentOutput, -appliedSpeed);
  }

  public void moveSwerveStrafe(double leftT, double rightT) {
    motors[0].swerveDatBoi(1, 0);
    motors[1].swerveDatBoi(1, 0);
    motors[2].swerveDatBoi(1, 0);
    motors[3].swerveDatBoi(1, 0);

    double appliedSpeed = findSpeed(leftT, rightT);

    moveDriveMotors(appliedSpeed);
  }

  private void leftSwerves(double x, double y) {
    motors[0].swerveDatBoi(x, y);
    motors[1].swerveDatBoi(x, y);
  }

  private void rightSwerves(double x, double y) {
    motors[2].swerveDatBoi(x, y);
    motors[3].swerveDatBoi(x, y);
  }

  public void zeroAllEncoders() {
    for (int i = 0; i < 4; i++) {
      motors[i].zeroEncoder();
    }
  }

  public void zeroAllEncodersBasedOnProx() {
    for (int i = 0; i < 4; i++) {
      motors[i].zeroEncoderBasedOnProx();
    }
  }

  private double findSpeed(double negitive, double positive) {
    return (positive - negitive)/2;
  }

  private void moveDriveMotors(double speed) {

    frontLeftDriveMotor.set(ControlMode.PercentOutput, speed);
    frontRightDriveMotor.set(ControlMode.PercentOutput, speed);
    rearLeftDriveMotor.set(ControlMode.PercentOutput, speed);
    rearRightDriveMotor.set(ControlMode.PercentOutput, speed);

    frontLeftDriveMotor.setInverted(true);
    frontRightDriveMotor.setInverted(false);
    rearLeftDriveMotor.setInverted(true);
    rearRightDriveMotor.setInverted(false);

  }

  public void targetWithSwerve(double xValue) {
    limelightX = xValue;

    if (Math.abs(limelightX) > 3) {
      if (limelightX < 0) {
        moveSwervePointTurn(0.3, 0);
      } else {
        moveSwervePointTurn(0, 0.3);
      }
    } else if (Math.abs(limelightX) > 1.5) {
      if (limelightX < 0) {
        moveSwervePointTurn(.15, 0);
      } else {
        moveSwervePointTurn(0, 0.15);
      }
    } else {
      moveSwerveAxis(0, 0, 0, 0, 0, 0);
    }

  }
}
