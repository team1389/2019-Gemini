package com.team1389.robot;

import com.team1389.operation.TeleopMain;
import com.team1389.watch.Watcher;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
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
	Compressor compressor;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit()
	{
		CameraServer.getInstance().startAutomaticCapture();
		CameraServer.getInstance().startAutomaticCapture();
		robot = RobotSoftware.getInstance();
		teleOperator = new TeleopMain(robot);
		watcher = new Watcher(robot.leftDistanceStream.getWatchable("left dist"),
				robot.rightDistanceStream.getWatchable("right dist"));
		watcher.outputToDashboard();
		// compressor = new Compressor(robot.CAN_COMPRESSOR_PORT.index());
	}

	@Override
	public void autonomousInit()
	{
		teleOperator.init();

	}

	@Override
	public void autonomousPeriodic()
	{
		teleOperator.periodic();
		Watcher.update();
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
		// System.out.println(robot.compressor.getCurrent().get());
		teleOperator.periodic();
		Watcher.update();

	}

	@Override
	public void disabledInit()
	{

	}

	@Override
	public void disabledPeriodic()
	{
		Watcher.update();
		// teleOperator.disabledPeriodic();
	}
}
