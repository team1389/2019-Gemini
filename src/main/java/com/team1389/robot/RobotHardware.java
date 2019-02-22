package com.team1389.robot;

import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.inputs.hardware.PDPHardware;
import com.team1389.hardware.inputs.hardware.PigeonIMUHardware;
import com.team1389.hardware.inputs.hardware.SwitchHardware;
import com.team1389.hardware.outputs.hardware.CANTalonHardware;
import com.team1389.hardware.outputs.hardware.CANSparkMaxHardware;
import com.team1389.hardware.outputs.hardware.CANVictorSPXHardware;
import com.team1389.hardware.outputs.hardware.DoubleSolenoidHardware;
import com.team1389.hardware.outputs.hardware.VictorHardware;
import com.team1389.hardware.registry.port_types.CAN;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

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
		initDriveTrain();
		initArm();
		initShooter();
		initCLimber();
	}

	private void initDriveTrain()
	{
		leftDriveA = new CANSparkMaxHardware(inv_LEFT_DRIVE_MOTOR_A, can_LEFT_DRIVE_MOTOR_A, registry);
		leftDriveB = new CANSparkMaxHardware(inv_LEFT_DRIVE_MOTOR_B, can_LEFT_DRIVE_MOTOR_B, registry);
		leftDriveC = new CANSparkMaxHardware(inv_LEFT_DRIVE_MOTOR_C, can_LEFT_DRIVE_MOTOR_C, registry);
		rightDriveA = new CANSparkMaxHardware(inv_RIGHT_DRIVE_MOTOR_A, can_RIGHT_DRIVE_MOTOR_A, registry);
		rightDriveB = new CANSparkMaxHardware(inv_RIGHT_DRIVE_MOTOR_B, can_RIGHT_DRIVE_MOTOR_B, registry);
		rightDriveC = new CANSparkMaxHardware(inv_RIGHT_DRIVE_MOTOR_C, can_RIGHT_DRIVE_MOTOR_C, registry);
		imu = new PigeonIMUHardware(can_IMU, registry);
	}

	private void initArm()
	{
		armLiftA = new CANTalonHardware(inv_ARM_LIFT_MOTOR_A, sinv_ARM_LIFT_MOTOR_A,
				FeedbackDevice.CTRE_MagEncoder_Absolute, 1024, can_ARM_LIFT_MOTOR_A, registry);
		armLiftB = new CANVictorSPXHardware(inv_ARM_LIFT_MOTOR_B, can_ARM_LIFT_MOTOR_B, registry);
		armIntake = new VictorHardware(inv_ARM_INTAKE_MOTOR, pwm_ARM_INTAKE_MOTOR, registry);
		hatchPiston = new DoubleSolenoidHardware(can_PCM_1, pcm_HATCH_OUTTAKE_A, pcm_HATCH_OUTTAKE_B, registry);
		cargoPiston = new DoubleSolenoidHardware(can_PCM_1, pcm_CARGO_OUTTAKE_A, pcm_CARGO_OUTTAKE_B, registry);
		beamBreak = new SwitchHardware(dio_BEAM_BREAK, registry);
	}

	private void initShooter()
	{
		leftShooter = new DoubleSolenoidHardware(can_PCM_2, pcm_LEFT_SHOOTER_A, pcm_LEFT_SHOOTER_B, registry);
		rightShooter = new DoubleSolenoidHardware(can_PCM_2, pcm_RIGHT_SHOOTER_A, pcm_RIGHT_SHOOTER_B, registry);
	}

	private void initCLimber()
	{
		climbPiston = new DoubleSolenoidHardware(can_PCM_1, pcm_CLIMBER_A, pcm_CLIMBER_B, registry);
		climbMotor = new CANVictorSPXHardware(inv_CLIMB_MOTOR, can_CLIMB_MOTOR, registry);
	}

}