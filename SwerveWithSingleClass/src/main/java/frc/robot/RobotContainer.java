/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  private static Joystick driverJoy = new Joystick(0);
  private JoystickButton pointTurnButton = new JoystickButton(driverJoy, Constants.pointTurnButon);
  private JoystickButton strafeEasyModeButton = new JoystickButton(driverJoy, Constants.strafeEasyModeButton);

  public static final SwerveDriveSubsystem swerveDriveSubsystem = new SwerveDriveSubsystem();
  public static final LimelightSubsystem limelightSubsystem = new LimelightSubsystem();

  private DriveSwerveCommand driveSwerveCommand = new DriveSwerveCommand(swerveDriveSubsystem);
  private PointTurnCommand pointTurnCommand = new PointTurnCommand(swerveDriveSubsystem);
  private StrafeEasyModeCommand strafeEasyModeCommand = new StrafeEasyModeCommand(swerveDriveSubsystem);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

    swerveDriveSubsystem.setDefaultCommand(driveSwerveCommand);

  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    pointTurnButton.whileHeld(pointTurnCommand, true);
    strafeEasyModeButton.whileHeld(strafeEasyModeCommand, true);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return null; //TODO: Setup auto command
  }

  public static double getDriverAxis(int axis) {
    if (axis == 1 || axis == 5) {
      return -driverJoy.getRawAxis(axis);
    } else {
      return driverJoy.getRawAxis(axis);
    }
  }

}
