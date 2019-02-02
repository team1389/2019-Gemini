package com.team1389.robot;

import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.inputs.hardware.PDPHardware;
import com.team1389.hardware.inputs.hardware.SpartanGyro;
import com.team1389.hardware.inputs.hardware.SwitchHardware;
import com.team1389.hardware.outputs.hardware.CANTalonHardware;
import com.team1389.hardware.outputs.hardware.CANSparkMaxHardware;
import com.team1389.hardware.outputs.hardware.DoubleSolenoidHardware;
import com.team1389.hardware.outputs.hardware.VictorHardware;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.CAN;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.team1389.hardware.outputs.hardware.DoubleSolenoidHardware;


/**
 * responsible for initializing and storing hardware objects defined in
 * {@link RobotLayout}
 * 
 * @author amind
 * @see RobotLayout
 * @see RobotMap
 */
public class RobotHardware extends RobotLayout
{

	/**
	 * Initializes robot hardware by subsystem. <br>
	 * note: use this method as an index to show hardware initializations that
	 * occur, and to find the init code for a particular system's hardware
	 */
	protected RobotHardware()
	{
		System.out.println("initializing hardware");
		registry = new Registry();
		pdp = new PDPHardware(new CAN(0), registry);

	}

	private void initDriveTrain() {
		//leftDriveA = new CANSparkMaxHardware()
	}

	private void initArm() {
		armLiftA = new CANTalonHardware(inv_ARM_LIFT_MOTOR_A, sinv_ARM_LIFT_MOTOR_A, 
			FeedbackDevice.CTRE_MagEncoder_Absolute, 1024, can_ARM_LIFT_MOTOR_A, registry);
		//armLiftB = new CANVictorSPXHardware()
		armIntake = new VictorHardware(inv_ARM_INTAKE_MOTOR, pwm_ARM_INTAKE_MOTOR, registry);
		hatchPistonA = new DoubleSolenoidHardware(pcm_HATCH_OUTTAKE_A, pcm_HATCH_OUTTAKE_B, registry);
		hatchPistonB = new DOubleSolenoidHardware(pcm_HATCH_OUTTAKE_A, pcm_HATCH_OUTTAKE_B, registry);
		hatchPistonC = new DoubleSolenoidHardware(pcm_HATCH_OUTTAKE_A, pcm_HATCH_OUTTAKE_B, registry);
		
	}

	private void initCLimber() {

	}

}