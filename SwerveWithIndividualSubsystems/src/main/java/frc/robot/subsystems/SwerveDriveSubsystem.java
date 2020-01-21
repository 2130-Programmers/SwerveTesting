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
import frc.robot.RobotContainer;

public class SwerveDriveSubsystem extends SubsystemBase {

  private TalonFX frontLeftDriveMotor;
  private TalonFX frontRightDriveMotor;
  private TalonFX rearLeftDriveMotor;
  private TalonFX rearRightDriveMotor;

  /**
   * Creates a new SwerveDriveSubsystem.
   */
  public SwerveDriveSubsystem() {

    
    frontLeftDriveMotor = new TalonFX(6);
    frontRightDriveMotor = new TalonFX(4);
    rearLeftDriveMotor = new TalonFX(0);
    rearRightDriveMotor = new TalonFX(2);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void moveSwerve(double leftX, double leftY, 
                         double rightX, double rightY, 
                         double leftT, double rightT) 
  {


    RobotContainer.frontLeftMotorSubsystem.swerveDatBoi(leftX, leftY);
    RobotContainer.frontRightMotorSubsystem.swerveDatBoi(leftX, leftY);
    RobotContainer.rearLeftMotorSubsystem.swerveDatBoi(rightX, rightY);
    RobotContainer.rearRightMotorSubsystem.swerveDatBoi(rightX, rightY);

    double desiredSpeed = findSpeed(leftT, rightT);
    
    moveDriveMotors(desiredSpeed);
    
  }

  private double findSpeed(double negitive, double positive) {
    //return (y * Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
    return (positive - negitive)/2;
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
}
