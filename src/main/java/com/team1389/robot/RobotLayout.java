package com.team1389.robot;

import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.outputs.hardware.CANTalonHardware;
import com.team1389.hardware.outputs.hardware.CANSparkMaxHardware;
import com.team1389.hardware.inputs.hardware.PDPHardware;
import com.team1389.hardware.outputs.hardware.DoubleSolenoidHardware;


/**
 * contains a list of declared hardware objects for this robot. Separated from
 * {@link RobotHardware} to make it easier to see what hardware is connected to
 * the robot.
 * 
 */
public class RobotLayout extends RobotMap
{
	public Registry registry;
	public CANTalonHardware armLiftA;
	public CANSparkMaxHardware leftDriveA;
	public CANSparkMaxHardware leftDriveB;
	public CANSparkMaxHardware leftDriveC;
	public CANSparkMaxHardware rightDriveA;
	public CANSparkMaxHardware rightDriveB;
	public CANSparkMaxHardware rightDriveC;
	public PDPHardware pdp;
	public DoubleSolenoidHardware leftShooter;
	public DoubleSolenoidHardware rightShooter;
	public DoubleSolenoidHardware climbPiston;




}
