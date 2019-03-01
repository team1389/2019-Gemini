package com.team1389.operation;

import com.team1389.hardware.controls.ControlBoard;
import com.team1389.robot.RobotSoftware;
import com.team1389.system.SystemManager;
import com.team1389.watch.Watcher;
import com.team1389.system.Subsystem;
import com.team1389.systems.ManualArm;

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
		Subsystem arm = setUpArm();
		manager = new SystemManager(arm);
		manager.init();
		Watcher watcher = new Watcher();
		watcher.watch();
		watcher.outputToDashboard();
	}

	private Subsystem setUpArm()
	{
		return new ManualArm(robot.cargoLauncher, robot.cargoIntake, robot.arm, robot.haveBall,
				controls.leftStickYAxis(), controls.aButton(), controls.yButton(), controls.xButton(), false);
	}

	public void periodic()
	{
		manager.update();
	}
}
