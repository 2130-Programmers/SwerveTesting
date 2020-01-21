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
import frc.robot.commands.DriveSwerveCommand;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.FrontLeftMotorSubsystem;
import frc.robot.subsystems.FrontRightMotorSubsystem;
import frc.robot.subsystems.RearLeftMotorSubsystem;
import frc.robot.subsystems.RearRightMotorSubsystem;
import frc.robot.subsystems.SwerveDriveSubsystem;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  private static Joystick driverJoy = new Joystick(0);

  // The robot's subsystems and commands are defined here...
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();

  public static final FrontLeftMotorSubsystem frontLeftMotorSubsystem = new FrontLeftMotorSubsystem();
  public static final FrontRightMotorSubsystem frontRightMotorSubsystem = new FrontRightMotorSubsystem();
  public static final RearLeftMotorSubsystem rearLeftMotorSubsystem= new RearLeftMotorSubsystem();
  public static final RearRightMotorSubsystem rearRightMotorSubsystem = new RearRightMotorSubsystem();
  public static final SwerveDriveSubsystem swerveDriveSubsystem = new SwerveDriveSubsystem();

  private final ExampleCommand m_autoCommand = new ExampleCommand(m_exampleSubsystem);

  private DriveSwerveCommand driveSwerveCommand = new DriveSwerveCommand(swerveDriveSubsystem);

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
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_autoCommand;
  }

  public static double getDriverAxis(int axis) {
    if (axis == 1 || axis == 5) {
      return -driverJoy.getRawAxis(axis);
    } else {
      return driverJoy.getRawAxis(axis);
    }
  }

}
