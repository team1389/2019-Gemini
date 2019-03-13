package com.team1389.operation;

import com.team1389.hardware.controls.ControlBoard;
import com.team1389.robot.RobotSoftware;
import com.team1389.system.SystemManager;
import com.team1389.system.drive.CurvatureDriveSystem;
import com.team1389.watch.Watcher;
import com.team1389.system.Subsystem;
import com.team1389.systems.TeleopShooter;
import com.team1389.systems.ControlCompressorCurvatureDrive;
import com.team1389.systems.ManualArm;
import com.team1389.systems.SimpleClimber;
import com.team1389.systems.TeleopHatch;

public class TeleopMain
{
	SystemManager manager;
	ControlBoard controls;
	RobotSoftware robot;
	TeleopShooter shooter;

	public TeleopMain(RobotSoftware robot)
	{
		this.robot = robot;
	}

	public void init()
	{
		controls = ControlBoard.getInstance();
		Subsystem drive = setUpDrive();
		shooter = setUpShooter();
		Subsystem arm = setUpArm();
		Subsystem climber = setUpClimber();
		Subsystem hatch = setUpHatch();
		manager = new SystemManager(drive, shooter, climber);
		manager.init();
		Watcher watcher = new Watcher();
		watcher.watch();
		watcher.outputToDashboard();
		// robot.runCompressor.set(false);
	}

	private Subsystem setUpDrive()
	{
		// TODO: test drive on full speed; worked well on half speed
		return new CurvatureDriveSystem(robot.drive.getAsTank(), controls.driveLeftY(), controls.driveRightX(),
				controls.driveRightBumper());
	}

	private Subsystem setUpCancelDrive()
	{
		// TODO: thorough testing of this system
		return new ControlCompressorCurvatureDrive(robot.drive.getAsTank(), controls.driveLeftY(),
				controls.driveRightX(), controls.driveRightBumper(), robot.runCompressor, controls.driveLeftBumper());
	}

	private TeleopShooter setUpShooter()
	{
		// r close, r far, l close, l far
		return new TeleopShooter(robot.rightShoot, robot.leftShoot, controls.driveBButton(), controls.driveYButton(),
				controls.driveAButton(), controls.driveXButton());
	}

	private Subsystem setUpClimber()
	{
		return new SimpleClimber(robot.climber, robot.climbWheel, controls.xButton(), controls.leftBumper(),
				controls.rightStickYAxis());
	}

	private Subsystem setUpArm()
	{
		return new ManualArm(robot.cargoIntake, robot.arm, robot.hatchExtension.getDigitalOut(), robot.haveBall,
				controls.leftStickYAxis().getScaled(.5), controls.aButton(), controls.rightBumper(), controls.bButton(),
				true);
	}

	private Subsystem setUpHatch()
	{
		return new TeleopHatch(robot.hatchIntakeStream, robot.hatchExtensionStream, robot.hatchOuttakeStream,
				controls.yButton());
	}

	public void periodic()
	{
		manager.update();
	}
}
