package com.team1389.operation;

import com.team1389.hardware.controls.ControlBoard;
import com.team1389.robot.RobotConstants;
import com.team1389.robot.RobotSoftware;
import com.team1389.system.SystemManager;
import com.team1389.system.drive.CurvatureDriveStraightSystem;
import com.team1389.watch.Watcher;
import com.team1389.system.Subsystem;
import com.team1389.systems.ManualArm;
import com.team1389.systems.TeleopHatch;

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
		Subsystem hatch = setUpHatch();
		Subsystem arm = setUpArm();
		manager = new SystemManager(hatch);
		manager.init();
		Watcher watcher = new Watcher();
		watcher.watch();
		watcher.outputToDashboard();
	}

	private Subsystem setUpHatch()
	{
		return new TeleopHatch(robot.hatchOuttake, robot.cargoLauncher, controls.bButton());
	}

	private Subsystem setUpArm()
	{
		return new ManualArm(robot.cargoLauncher, robot.cargoIntake, robot.arm, robot.haveBall,
				controls.leftStickYAxis(), controls.aButton(), controls.yButton(), controls.xButton(), true);
	}

	public void periodic()
	{
		manager.update();
	}
}
