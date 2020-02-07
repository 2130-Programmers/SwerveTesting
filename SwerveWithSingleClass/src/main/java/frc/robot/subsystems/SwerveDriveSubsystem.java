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

  public Motor motorFL;
  public Motor motorFR;
  public Motor motorRL;
  public Motor motorRR;


  private double desiredSpeed;

  private double limelightX;

  /**
   * Creates a new SwerveDriveSubsystem   
   */

  public SwerveDriveSubsystem() {

    motorFL = new Motor(7, 16, 17, 3);
    motorFR = new Motor(5, 14, 15, 2);
    motorRL = new Motor(1, 12, 13, 0);
    motorRR = new Motor(3, 10, 11, 1);    
    
    frontLeftDriveMotor = new TalonFX(6);
    frontRightDriveMotor = new TalonFX(4);
    rearLeftDriveMotor = new TalonFX(0);
    rearRightDriveMotor = new TalonFX(2);

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
    motorFL.swerveDatBoi(0.8, 0.6);
    motorFR.swerveDatBoi(-0.8, 0.6);
    motorRL.swerveDatBoi(-0.8, 0.6);
    motorRR.swerveDatBoi(0.8, 0.6);

    double appliedSpeed = findSpeed(leftT, rightT);

    frontLeftDriveMotor.set(ControlMode.PercentOutput, appliedSpeed);
    frontRightDriveMotor.set(ControlMode.PercentOutput, -appliedSpeed);
    rearLeftDriveMotor.set(ControlMode.PercentOutput, appliedSpeed);
    rearRightDriveMotor.set(ControlMode.PercentOutput, -appliedSpeed);
  }

  public void moveSwerveStrafe(double leftT, double rightT) {
    motorFL.swerveDatBoi(1, 0);
    motorFR.swerveDatBoi(1, 0);
    motorRL.swerveDatBoi(1, 0);
    motorRR.swerveDatBoi(1, 0);

    double appliedSpeed = findSpeed(leftT, rightT);

    moveDriveMotors(appliedSpeed);
  }

  private void leftSwerves(double x, double y) {
    motorFL.swerveDatBoi(x, y);
    motorFR.swerveDatBoi(x, y);
  }

  private void rightSwerves(double x, double y) {
    motorRL.swerveDatBoi(x, y);
    motorRR.swerveDatBoi(x, y);
  }

  public void zeroAllEncoders() {
    motorFL.zeroEncoder();
    motorFR.zeroEncoder();
    motorRL.zeroEncoder();
    motorRR.zeroEncoder();
  }

  public void zeroAllEncodersBasedOnProx() {
    motorFL.zeroEncoderBasedOnProx();
    motorFR.zeroEncoderBasedOnProx();
    motorRL.zeroEncoderBasedOnProx();
    motorRR.zeroEncoderBasedOnProx();
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
