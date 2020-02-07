/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.SwerveDriveSubsystem;

public class StrafeEasyModeCommand extends CommandBase {

  private SwerveDriveSubsystem swerveDriveSubsystem;

  /**
   * Creates a new StrafeEasyModeCommand.
   */
  public StrafeEasyModeCommand(SwerveDriveSubsystem tempSwerveDriveSubsystem) {
    // Use addRequirements() here to declare subsystem dependencies.

    swerveDriveSubsystem = tempSwerveDriveSubsystem;
    addRequirements(tempSwerveDriveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //swerveDriveSubsystem.moveSwerveStrafe(RobotContainer.getDriverAxis(Constants.leftTrigger),
    //                                      RobotContainer.getDriverAxis(Constants.rightTrigger));
    swerveDriveSubsystem.targetWithSwerve(RobotContainer.limelightSubsystem.x);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
