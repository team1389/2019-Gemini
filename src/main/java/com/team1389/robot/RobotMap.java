package com.team1389.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 * <p>
 * <b>Conventions</b>: <br>
 * For I/O ports, the naming convention is <em>type_ALL_CAPS_IDENTIFIER</em>.
 * for example, a talon port might be named can_RIGHT_MOTOR_A. Possible port
 * types and identifiers are CAN (can), Analog (anlg), PWM (pwm), USB (usb), PCM
 * (pcm), DIO (dio), etc
 * <p>
 * Inputs and Outputs may be inverted. The inversions in this map should only
 * relate to the physical configuration of the robot. A positive value should
 * cause the output to move in the most logical direction (I.e, the drive motors
 * should move forward with positive voltage values) <br>
 * the convention for inversion constants is
 * <em>inv_ASSOCIATED_IO_IDENTIFIER</em> for outputs and
 * <em>sinv_ASSOCIATED_IO_IDENTIFIER</em> for inputs.
 */
public class RobotMap
{
    //Drive Train
	protected final CAN can_LEFT_MOTOR_A = new CAN(3);
    protected final boolean inv_LEFT_MOTOR_A = false;
    protected final boolean sinv_LEFT_MOTOR_A = false;

    protected final CAN can_LEFT_MOTOR_B = new CAN(5);
    protected final boolean inv_LEFT_MOTOR_B = false;
    protected final boolean sinv_LEFT_MOTOR_B = false;

    protected final CAN can_LEFT_MOTOR_C = new CAN(5);
    protected final boolean inv_LEFT_MOTOR_C = false;
    protected final boolean sinv_LEFT_MOTOR_C = false;

	protected final CAN can_RIGHT_MOTOR_A = new CAN(6);
    protected final boolean inv_RIGHT_MOTOR_A = false;
    protected final boolean sinv_RIGHT_MOTOR_A = false;

	protected final CAN can_RIGHT_MOTOR_B = new CAN(4);
    protected final boolean inv_RIGHT_MOTOR_B = false;
    protected final boolean sinv_RIGHT_MOTOR_B = false;

    protected final CAN can_RIGHT_MOTOR_C = new CAN(4);
    protected final boolean inv_RIGHT_MOTOR_C = false;
    protected final boolean sinv_RIGHT_MOTOR_C = false;

    //Arm
	protected final CAN can_ARM_LIFT_MOTOR_A = new CAN(3);
    protected final boolean inv_ARM_LIFT_MOTOR_A = false;
    protected final boolean sinv_ARM_LIFT_MOTOR_A = false;

	protected final CAN can_ARM_LIFT_MOTOR_B = new CAN(1);
    protected final boolean inv_ARM_LIFT_MOTOR_B = false;
    
	protected final PWM pwm_ARM_INTAKE_MOTOR = new PWM(0);
    protected final boolean inv_ARM_INTAKE_MOTOR = false;

    protected final PCM pcm_HATCH_OUTTAKE_A = new PCM(7);
    protected final PCM pcm_HATCH_OUTTAKE_B = new PCM(7);
    protected final PCM pcm_HATCH_OUTTAKE_C = new PCM(7);

    protected final PCM pcm_CARGO_OUTTAKE = new PCM(7);

    //Climber
    protected final CAN can_CLIMB_MOTOR = new CAN(1);
    protected final boolean inv_CLIMB_MOTOR = false;

    protected final PCM pcm_CLIMBER = new PCM(7);

	protected final SPIPort spi_GyroPort = new SPIPort(SPI.Port.kOnboardCS0);
}
