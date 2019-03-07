package com.team1389.operation;

import com.team1389.hardware.controls.ControlBoard;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.robot.RobotConstants;
import com.team1389.robot.RobotSoftware;
import com.team1389.system.SystemManager;
import com.team1389.system.drive.CurvatureDriveStraightSystem;
import com.team1389.watch.Watcher;
import com.team1389.system.Subsystem;
import com.team1389.systems.TeleopShooter;
import com.team1389.systems.ManualArm;
import com.team1389.systems.Shooter;
import com.team1389.systems.SimpleClimber;
import com.team1389.systems.TeleopHatch;
import com.team1389.systems.ModifiedStraightCurvatureDrive;

public class TeleopMain
{
	SystemManager manager;
	ControlBoard controls;
	RobotSoftware robot;

	public TeleopMain(RobotSoftware robot)
	{
		this.robot = robot;
	}

	public void init()
	{
		controls = ControlBoard.getInstance();
		TeleopShooter shooter = setUpShooter();
		Subsystem drive = setUpDrive(shooter.getAlignmentCommandsRunning());
		Subsystem arm = setUpArm();
		Subsystem climber = setUpClimber();
		Subsystem hatch = setUpHatch();
		manager = new SystemManager(drive, shooter, climber, arm, hatch);
		manager.init();
		Watcher watcher = new Watcher();
		watcher.watch();
		watcher.outputToDashboard();
	}

	private Subsystem setUpDrive(DigitalIn alignmentCommandsRunning)
	{

		// (DriveOut drive, PercentIn throttle, PercentIn wheel,

		return new ModifiedStraightCurvatureDrive(robot.drive.getAsTank(), controls.driveLeftY(),
				controls.driveRightX(), controls.driveLeftBumper(), RobotConstants.TURN_SENSITIVITY,
				RobotConstants.SPIN_SENSITIVITY, robot.angle, RobotConstants.LATERAL_PID_CONSTANTS.p,
				controls.driveRightBumper(), alignmentCommandsRunning);
	}

	private TeleopShooter setUpShooter()
	{
		return new TeleopShooter(robot.rightShoot, robot.leftShoot, controls.driveBButton(), controls.driveYButton(),
				controls.driveAButton(), controls.driveXButton());
	}

	private Subsystem setUpClimber()
	{
		return new SimpleClimber(robot.climber, robot.climbWheel, controls.xButton(), controls.rightStickYAxis());
	}

	private Subsystem setUpArm()
	{
		return new ManualArm(robot.cargoHolder, robot.cargoIntake, robot.arm, robot.haveBall, controls.leftStickYAxis(),
				controls.aButton(), controls.rightBumper(), controls.bButton(), false);
	}

	private Subsystem setUpHatch()
	{
		return new TeleopHatch(robot.hatchOuttake, robot.cargoHolder, controls.yButton());
	}

	public void periodic()
	{
		manager.update();
	}
}
