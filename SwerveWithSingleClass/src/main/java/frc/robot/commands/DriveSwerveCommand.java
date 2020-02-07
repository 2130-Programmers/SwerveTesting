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

public class DriveSwerveCommand extends CommandBase {

  private SwerveDriveSubsystem swerveDriveSubsystem;

  /**
   * Creates a new driveSwerveCommand.
   */
  public DriveSwerveCommand(SwerveDriveSubsystem tempSwerveDriveSubsystem) {
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
    swerveDriveSubsystem.moveSwerveAxis(RobotContainer.getDriverAxis(Constants.leftX), 
                                        RobotContainer.getDriverAxis(Constants.leftY), 
                                        RobotContainer.getDriverAxis(Constants.rightX), 
                                        RobotContainer.getDriverAxis(Constants.rightY),
                                        RobotContainer.getDriverAxis(Constants.leftTrigger),
                                        RobotContainer.getDriverAxis(Constants.rightTrigger));
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
