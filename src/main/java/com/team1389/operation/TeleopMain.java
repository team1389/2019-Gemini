package com.team1389.operation;

import com.team1389.hardware.controls.ControlBoard;
import com.team1389.robot.RobotSoftware;
import com.team1389.system.SystemManager;
import com.team1389.system.drive.CurvatureDriveSystem;
import com.team1389.watch.Watcher;
import com.team1389.system.Subsystem;

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
		manager = new SystemManager(drive);
		Watcher watcher = new Watcher();
		watcher.watch(manager.getSystemWatchables());
		watcher.outputToDashboard();
		manager.init();
	}

	private Subsystem setUpDrive()
	{
		return new CurvatureDriveSystem(robot.drive.getAsTank(), controls.xLeftDriveY(), controls.xRightDriveX(),
				controls.xRightBumper());
	}

	public void periodic()
	{
		manager.update();
	}
}
