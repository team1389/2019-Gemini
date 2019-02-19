package com.team1389.robot;

import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.system.drive.SixDriveOut;
import com.team1389.system.drive.CurvatureDriveSystem;
import com.team1389.system.drive.FourDriveOut;
import com.team1389.hardware.outputs.hardware.DoubleSolenoidHardware;

public class RobotSoftware extends RobotHardware
{
	private static RobotSoftware INSTANCE = new RobotSoftware();
	public FourDriveOut<Percent> drive;
	public DigitalOut rightShoot;
	public DigitalOut leftShoot;
	public DigitalOut climber;
	public PercentOut climbWheel;
	public DigitalOut hatchOuttake;
	public DigitalOut cargoLauncher;
	public RangeOut<Percent> arm;
	public RangeOut<Percent> cargoIntake;
	public DigitalIn haveBall;
	public AngleIn<Position> robotAngle;

	public static RobotSoftware getInstance()
	{
		return INSTANCE;
	}

	public RobotSoftware()
	{
		// drive = new SixDriveOut<>(leftDriveA.getVoltageController(),
		// rightDriveA.getVoltageController(),
		// leftDriveB.getVoltageController(),
		// rightDriveB.getVoltageController(),
		// leftDriveC.getVoltageController(),
		// rightDriveC.getVoltageController());
		drive = new FourDriveOut<>(leftDriveC.getVoltageController(), rightDriveA.getVoltageController(),
				leftDriveB.getVoltageController(), rightDriveB.getVoltageController());

		rightShoot = rightShooter.getDigitalOut().getInverted();
		leftShoot = leftShooter.getDigitalOut().getInverted();
		climbWheel = climbMotor.getVoltageController();
		arm = armLiftA.getVoltageController().getWithAddedFollowers(armLiftB.getVoltageController());
		cargoLauncher = cargoPiston.getDigitalOut();
		hatchOuttake = hatchPiston.getDigitalOut();
		climber = climbPiston.getDigitalOut();
		cargoIntake = armIntake.getVoltageController().getInverted();
		haveBall = beamBreak.getSwitchInput();

		robotAngle = imu.getYawInput().getWithSetRange(0, 360).getWrapped();

	}

	/**
	 * Note: This method will not zero the robot angle referenced inside of a
	 * subsystem or other object
	 */
	public void zeroRobotAngleOnStartup()
	{
		double offset = -robotAngle.get();
		double origMin = robotAngle.min();
		double origMax = robotAngle.max();
		double newMin = origMin + offset;
		double newMax = origMax + offset;
		robotAngle = (AngleIn<Position>) robotAngle.getOffset(offset).getWithSetRange(newMin, newMax);
	}
}
