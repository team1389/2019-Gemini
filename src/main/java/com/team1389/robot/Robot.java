package com.team1389.robot;

import com.team1389.operation.TeleopMain;
import com.team1389.watch.Watcher;

import edu.wpi.first.wpilibj.TimedRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot
{
	RobotSoftware robot;
	TeleopMain teleOperator;
	Watcher watcher;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit()
	{
		robot = RobotSoftware.getInstance();
		teleOperator = new TeleopMain(robot);
		//watcher = new Watcher();
		//watcher.outputToDashboard();
	}

	@Override
	public void autonomousInit()
	{

	}

	@Override
	public void autonomousPeriodic()
	{
	}

	@Override
	public void teleopInit()
	{
		teleOperator.init();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic()
	{
		teleOperator.periodic();
		//Watcher.update();
	}

	@Override

	public void disabledInit()
	{
	}

	@Override

	public void disabledPeriodic()
	{

	}
}
