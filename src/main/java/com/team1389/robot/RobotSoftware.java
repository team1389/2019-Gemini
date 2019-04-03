package com.team1389.robot;

import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.outputs.software.AngleOut;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.system.drive.SixDriveOut;

public class RobotSoftware extends RobotHardware
{
	private static RobotSoftware INSTANCE = new RobotSoftware();
	private final double DRIVETRAIN_SCALE_FACTOR = 0.25;

	public SixDriveOut<Percent> drive;
	public DigitalOut rightShoot;
	public DigitalOut leftShoot;
	public DigitalOut climber;
	public PercentOut climbWheel;
	public DigitalOut hatchOuttakeStream;
	public DigitalOut hatchIntakeStream;
	public DigitalOut hatchExtensionStream;
	public RangeOut<Percent> arm;
	public RangeOut<Percent> cargoIntake;
	public DigitalIn haveBall;
	public AngleIn angle;
	public DigitalIn currentlyAligning;
	public DigitalOut runCompressor;
	public RangeIn leftDistanceStream;
	public RangeIn rightDistanceStream;
	public DigitalOut leftSideDampStream;
	public DigitalOut rightSideDampStream;
	public DigitalOut frontClimbStream;

	public static RobotSoftware getInstance()
	{
		return INSTANCE;
	}

	public RobotSoftware()
	{
		drive = new SixDriveOut<>(leftDriveA.getVoltageController().getScaled(DRIVETRAIN_SCALE_FACTOR),
				rightDriveA.getVoltageController().getScaled(DRIVETRAIN_SCALE_FACTOR),
				leftDriveB.getVoltageController().getScaled(DRIVETRAIN_SCALE_FACTOR),
				rightDriveB.getVoltageController().getScaled(DRIVETRAIN_SCALE_FACTOR),
				leftDriveC.getVoltageController().getScaled(DRIVETRAIN_SCALE_FACTOR),
				rightDriveC.getVoltageController().getScaled(DRIVETRAIN_SCALE_FACTOR));

		rightShoot = rightShooter.getDigitalOut().getInverted();
		leftShoot = leftShooter.getDigitalOut().getInverted();
		climbWheel = climbMotor.getVoltageController();
		leftSideDampStream = leftDamp.getDigitalOut();
		rightSideDampStream = rightDamp.getDigitalOut();
		frontClimbStream = climbFrontPiston.getDigitalOut().getInverted();
		climber = climbPiston.getDigitalOut().getInverted();

		leftDistanceStream = leftDistance.getPositionInInches()
				.getMapped(position -> position * Math.sin(Math.toRadians(80)));
		rightDistanceStream = rightDistance.getPositionInInches()
				.getMapped(position -> position * Math.sin(Math.toRadians(80)));

		runCompressor = compressor.toggleCompressorRunning();

	}

}
