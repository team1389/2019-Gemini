package com.team1389.robot;

import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.outputs.hardware.CANTalonHardware;
import com.team1389.hardware.outputs.hardware.CANSparkMaxHardware;
import com.team1389.hardware.outputs.hardware.CANVictorSPXHardware;
import com.team1389.hardware.outputs.hardware.VictorHardware;
import com.team1389.hardware.inputs.hardware.AnalogDistanceHardware;
import com.team1389.hardware.inputs.hardware.PDPHardware;
import com.team1389.hardware.inputs.hardware.PigeonIMUHardware;
import com.team1389.hardware.inputs.hardware.SwitchHardware;
import com.team1389.hardware.outputs.hardware.DoubleSolenoidHardware;
import com.team1389.hardware.outputs.hardware.SolenoidHardware;

/**
 * contains a list of declared hardware objects for this robot. Separated from
 * {@link RobotHardware} to make it easier to see what hardware is connected to
 * the robot.
 * 
 */
public class RobotLayout extends RobotMap
{
	public Registry registry;

	// DriveTrain
	public CANSparkMaxHardware leftDriveA;
	public CANSparkMaxHardware leftDriveB;
	public CANSparkMaxHardware leftDriveC;
	public CANSparkMaxHardware rightDriveA;
	public CANSparkMaxHardware rightDriveB;
	public CANSparkMaxHardware rightDriveC;
	public PigeonIMUHardware imu;

	// Arm
	public CANTalonHardware armLiftA;
	public CANVictorSPXHardware armLiftB;
	public VictorHardware armIntake;
	public DoubleSolenoidHardware cargoPiston;
	public SwitchHardware beamBreakA;
	public SwitchHardware beamBreakB;

	// Shooter
	public DoubleSolenoidHardware leftShooter;
	public DoubleSolenoidHardware rightShooter;

	// Climber
	public DoubleSolenoidHardware climbPiston;
	public CANVictorSPXHardware climbMotor;

	// Hatch
	public DoubleSolenoidHardware hatchIntake;
	public DoubleSolenoidHardware hatchExtension;
	public DoubleSolenoidHardware hatchOuttake;

	// Miscallenous
	public PDPHardware pdp;
	public AnalogDistanceHardware leftDistance;
	public AnalogDistanceHardware rightDistance;
}
