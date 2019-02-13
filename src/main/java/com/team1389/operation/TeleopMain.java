package com.team1389.operation;

import com.team1389.hardware.controls.ControlBoard;
import com.team1389.robot.RobotSoftware;
import com.team1389.system.SystemManager;
import com.team1389.system.drive.CurvatureDriveSystem;
import com.team1389.watch.Watcher;
import com.team1389.system.Subsystem;
import com.team1389.systems.TeleopShooter;
import com.team1389.systems.Climber;


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
		Subsystem drive = setUpDrive(); 
		Subsystem shooter = setUpShooter();
		manager = new SystemManager(drive, shooter);
		manager.init();
		Watcher watcher = new Watcher();
		watcher.watch(manager.getSystemWatchables());
		watcher.outputToDashboard();
	}

	private Subsystem setUpDrive()
	{
		return new CurvatureDriveSystem(robot.drive.getAsTank(), controls.xLeftDriveY(), controls.xRightDriveX(),
				controls.xRightBumper());
	}

	private Subsystem setUpShooter() {
		return new TeleopShooter(robot.rightShoot, robot.leftShoot, controls.leftBumper(), controls.rightBumper());
	}

	private Subsystem setUpClimber() {
		return new Climber(robot.climber);
	}

	public void periodic()
	{
		manager.update();
	}
}
