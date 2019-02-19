package com.team1389.robot;

import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.system.drive.SixDriveOut;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class RobotSoftware extends RobotHardware
{
	private static RobotSoftware INSTANCE = new RobotSoftware();
	public SixDriveOut<Percent> drive;
	public DigitalOut rightShoot;
	public DigitalOut leftShoot;
	public DigitalOut climber;
	public PercentOut climbWheel;
	public DigitalOut hatchOuttake;
	public DigitalOut cargoLauncher;
	public RangeOut<Percent> arm;
	public RangeOut<Percent> cargoIntake;
	public DigitalIn haveBall;
	public NetworkTableInstance inst;
	public NetworkTable visionTable;

	public static RobotSoftware getInstance()
	{
		return INSTANCE;
	}

	public RobotSoftware()
	{
		drive = new SixDriveOut<>(leftDriveA.getVoltageController(), rightDriveA.getVoltageController(),
				leftDriveB.getVoltageController(), rightDriveB.getVoltageController(),
				leftDriveC.getVoltageController(), rightDriveC.getVoltageController());

		rightShoot = rightShooter.getDigitalOut().getInverted();
		leftShoot = leftShooter.getDigitalOut().getInverted();
		climbWheel = climbMotor.getVoltageController();
		arm = armLiftA.getVoltageController().getWithAddedFollowers(armLiftB.getVoltageController());
		cargoLauncher = cargoPiston.getDigitalOut();
		hatchOuttake = hatchPiston.getDigitalOut();
		climber = climbPiston.getDigitalOut();
		cargoIntake = armIntake.getVoltageController().getInverted();
		haveBall = beamBreak.getSwitchInput();
		inst = NetworkTableInstance.getDefault();
		visionTable = inst.getTable("vision");
	}
}
