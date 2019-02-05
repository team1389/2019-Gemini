package com.team1389.robot;

import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.system.drive.SixDriveOut;
import com.team1389.system.drive.CurvatureDriveSystem;

public class RobotSoftware extends RobotHardware {
	private static RobotSoftware INSTANCE = new RobotSoftware();
	public SixDriveOut<Percent> drive;
	
	public static RobotSoftware getInstance() {
		return INSTANCE;
	}

	public RobotSoftware(){
	drive = new SixDriveOut<>(leftDriveA.getVoltageController(), rightDriveA.getVoltageController(), leftDriveB.getVoltageController(), 
		rightDriveB.getVoltageController(), leftDriveC.getVoltageController(), rightDriveC.getVoltageController());
	}
	

}
