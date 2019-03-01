package com.team1389.robot;

import com.team1389.hardware.registry.port_types.CAN;
import com.team1389.hardware.registry.port_types.DIO;
import com.team1389.hardware.registry.port_types.PCM;
import com.team1389.hardware.registry.port_types.PWM;
import com.team1389.hardware.registry.port_types.SPIPort;

import edu.wpi.first.wpilibj.SPI;

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
    // Drive Train
    protected final CAN can_LEFT_DRIVE_MOTOR_A = new CAN(11);
    protected final boolean inv_LEFT_DRIVE_MOTOR_A = false;
    protected final boolean sinv_LEFT_DRIVE_MOTOR_A = false;

    protected final CAN can_LEFT_DRIVE_MOTOR_B = new CAN(7);
    protected final boolean inv_LEFT_DRIVE_MOTOR_B = false;
    protected final boolean sinv_LEFT_DRIVE_MOTOR_B = false;

    protected final CAN can_LEFT_DRIVE_MOTOR_C = new CAN(8);
    protected final boolean inv_LEFT_DRIVE_MOTOR_C = false;
    protected final boolean sinv_LEFT_DRIVE_MOTOR_C = false;

    protected final CAN can_RIGHT_DRIVE_MOTOR_A = new CAN(9);
    protected final boolean inv_RIGHT_DRIVE_MOTOR_A = true;
    protected final boolean sinv_RIGHT_DRIVE_MOTOR_A = true;

    protected final CAN can_RIGHT_DRIVE_MOTOR_B = new CAN(10);
    protected final boolean inv_RIGHT_DRIVE_MOTOR_B = true;
    protected final boolean sinv_RIGHT_DRIVE_MOTOR_B = true;

    protected final CAN can_RIGHT_DRIVE_MOTOR_C = new CAN(6);
    protected final boolean inv_RIGHT_DRIVE_MOTOR_C = true;
    protected final boolean sinv_RIGHT_DRIVE_MOTOR_C = false;

    // Shooter
    protected final PCM pcm_LEFT_SHOOTER_A = new PCM(1);
    protected final PCM pcm_LEFT_SHOOTER_B = new PCM(6);
    protected final PCM pcm_RIGHT_SHOOTER_A = new PCM(1); // TODO: 0
    protected final PCM pcm_RIGHT_SHOOTER_B = new PCM(5); // TODO: 7
    // Arm
    protected final CAN can_ARM_LIFT_MOTOR_A = new CAN(3);
    protected final boolean inv_ARM_LIFT_MOTOR_A = false;
    protected final boolean sinv_ARM_LIFT_MOTOR_A = false;

    protected final CAN can_ARM_LIFT_MOTOR_B = new CAN(1);
    protected final boolean inv_ARM_LIFT_MOTOR_B = false;

    protected final PWM pwm_ARM_INTAKE_MOTOR = new PWM(0); // TODO: change back
                                                           // to 8 for the
                                                           // actual robot
    protected final boolean inv_ARM_INTAKE_MOTOR = false;

    protected final PCM pcm_HATCH = new PCM(3);

    protected final PCM pcm_CARGO_OUTTAKE_A = new PCM(7); // TODO: change to 1,
                                                          // 5 in that order
    protected final PCM pcm_CARGO_OUTTAKE_B = new PCM(0);

    protected final DIO dio_BEAM_BREAK_A = new DIO(0);
    protected final DIO dio_BEAM_BREAK_B = new DIO(1);

    // Climber
    protected final CAN can_CLIMB_MOTOR = new CAN(4);
    protected final boolean inv_CLIMB_MOTOR = false;

    protected final PCM pcm_CLIMBER_A = new PCM(1);
    protected final PCM pcm_CLIMBER_B = new PCM(6);

    // Miscellaneous
    protected final SPIPort spi_GyroPort = new SPIPort(SPI.Port.kOnboardCS0);
    protected final CAN can_PCM_1 = new CAN(1);
    protected final CAN can_PCM_2 = new CAN(2);
    protected final CAN can_IMU = new CAN(12);
}
