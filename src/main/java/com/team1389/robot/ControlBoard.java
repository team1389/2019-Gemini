package com.team1389.robot;

import com.team1389.hardware.inputs.controllers.LogitechExtreme3D;
import com.team1389.hardware.inputs.controllers.XBoxController;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.outputs.software.DigitalOut;

/**
 * A basic framework for the robot controls. Like the RobotHardware, one
 * instance of the ControlBoard object is created upon startup, then other
 * methods request the singleton ControlBoard instance.
 * 
 */
public class ControlBoard
{
	private static ControlBoard mInstance = new ControlBoard();
	public static final double turnSensitivity = 1.0;
	public static final double spinSensitivity = 1.0;

	public static ControlBoard getInstance()
	{
		return mInstance;
	}

	private ControlBoard()
	{
	}

	private final XBoxController manipController = new XBoxController(1);
	private final XBoxController xDriveController = new XBoxController(2);

	public PercentIn driveLeftY()
	{
		return xDriveController.leftStick.yAxis().getInverted();
	}

	public PercentIn driveLeftX()
	{
		return xDriveController.leftStick.xAxis();
	}

	public PercentIn driveRightX()
	{
		return xDriveController.rightStick.xAxis();
	}

	public PercentIn driveRightY()
	{
		return xDriveController.rightStick.yAxis().getInverted();
	}

	public DigitalIn driveRightBumper()
	{
		return xDriveController.rightBumper();
	}

	public DigitalIn driveLeftBumper()
	{
		return xDriveController.leftBumper();
	}

	public DigitalIn driveStartButton()
	{
		return xDriveController.startButton();
	}

	public DigitalIn driveBackButton()
	{
		return xDriveController.backButton();
	}

	public DigitalIn driveAButton()
	{
		return xDriveController.aButton();
	}

	public DigitalIn driveBButton()
	{
		return xDriveController.bButton().getLatched();
	}

	public DigitalIn driveXButton()
	{
		return xDriveController.xButton().getLatched();
	}

	public DigitalIn driveYButton()
	{
		return xDriveController.yButton().getLatched();
	}

	public DigitalIn aButton()
	{
		return manipController.aButton().getLatched();
	}

	public DigitalIn yButton()
	{
		return manipController.yButton().getLatched();
	}

	public DigitalIn xButton()
	{
		return manipController.xButton().getLatched();
	}

	public DigitalIn bButton()
	{
		return manipController.bButton().getLatched();
	}

	public PercentIn leftTrigger()
	{
		return manipController.leftTrigger();
	}

	public PercentIn leftStickYAxis()
	{
		return manipController.leftStick.yAxis();
	}

	public PercentIn leftStickXAxis()
	{
		return manipController.leftStick.xAxis();
	}

	public PercentIn rightStickXAxis()
	{
		return manipController.rightStick.xAxis();
	}

	public PercentIn rightStickYAxis()
	{
		return manipController.rightStick.yAxis();
	}

	public DigitalIn rightBumper()
	{
		return manipController.rightBumper();
	}

	public DigitalIn leftBumper()
	{
		return manipController.leftBumper();
	}

	public DigitalOut setRumble()
	{
		return new DigitalOut(b -> manipController.setRumble(b ? 1.0 : 0.0));
	}

	public DigitalIn downDPad()
	{
		return manipController.downArrow().getLatched();
	}

	public DigitalIn leftDPad()
	{
		return manipController.leftArrow().getLatched();
	}

	public DigitalIn rightDPad()
	{
		return manipController.rightArrow().getLatched();
	}

	public DigitalIn backButton()
	{
		return manipController.backButton().getLatched();
	}

	public DigitalIn upDPad()
	{
		return manipController.upArrow().getLatched();
	}

	public PercentIn rightTrigger()
	{
		return manipController.rightTrigger();
	}

	public DigitalIn startButton()
	{
		return manipController.startButton().getLatched();
	}

}
