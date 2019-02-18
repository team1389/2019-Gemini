package com.team1389.robot;

import com.team1389.controllers.SynchronousPIDController;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.operation.TeleopMain;
import com.team1389.watch.Watcher;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
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
	Watcher watcher;
	Registry registry;
	TeleopMain teleOperator;
	private final int center = 320;
	SynchronousPIDController<Percent, Position> pidController;
	NetworkTableEntry xEntry;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit()
	{
		robot = RobotSoftware.getInstance();
		registry = new Registry();
		robot = RobotSoftware.getInstance();

		NetworkTableInstance inst = NetworkTableInstance.getDefault();
		NetworkTable table = inst.getTable("vision");
		xEntry = table.getEntry("X");
		robot = RobotSoftware.getInstance();
		RangeIn<Position> diff = new RangeIn<Position>(Position.class, () -> xEntry.getDouble(center) - center, 0, 640);
		RangeOut<Percent> driveTrain = robot.rightDriveA.getVoltageController()
				.getWithAddedFollowers(robot.rightDriveB.getVoltageController())
				.getWithAddedFollowers(robot.leftDriveB.getVoltageController())
				.getWithAddedFollowers(robot.leftDriveC.getVoltageController());
		pidController = new SynchronousPIDController<>(0.001, 0, 0, 0, diff, driveTrain);
		pidController.enable();

		teleOperator = new TeleopMain(robot);
		watcher = new Watcher();
		watcher.outputToDashboard();
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
		System.out.println(xEntry.getDouble(center));
		pidController.update();
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

	}
}
