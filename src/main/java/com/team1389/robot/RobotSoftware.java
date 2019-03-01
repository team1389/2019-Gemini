package com.team1389.robot;

import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.value_types.Percent;

public class RobotSoftware extends RobotHardware
{
	private static RobotSoftware INSTANCE = new RobotSoftware();
	public DigitalOut cargoLauncher;
	public RangeOut<Percent> arm;
	public RangeOut<Percent> cargoIntake;
	public DigitalIn haveBall;

	public static RobotSoftware getInstance()
	{
		return INSTANCE;
	}

	public RobotSoftware()
	{

		arm = armLiftA.getVoltageController().getWithAddedFollowers(armLiftB.getVoltageController());
		cargoLauncher = cargoPiston.getDigitalOut();
		cargoIntake = armIntake.getVoltageController().getInverted();
		haveBall = beamBreakA.getSwitchInput().getInverted();

	}

}
