package com.team1389.robot;

import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.outputs.software.AngleOut;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.system.drive.DriveOut;

public class RobotSoftware extends RobotHardware {
    private static RobotSoftware INSTANCE = new RobotSoftware();
    public RangeOut leftA, leftB, leftC;
    public RangeOut rightA, rightB, rightC;
    public RangeOut leftMaster, rightMaster;
    public DriveOut drive;
    public DigitalOut rightShoot;
    public DigitalOut leftShoot;
    public DigitalOut climber;
    public PercentOut climbWheel;
    public DigitalOut hatchOuttakeStream;
    public DigitalOut hatchIntakeStream;
    public DigitalOut hatchExtensionStream;
    public RangeOut<Percent> arm;
    public RangeOut<Percent> cargoIntake;
    public DigitalIn haveBall;
    public AngleIn angle;
    public DigitalIn currentlyAligning;
    public DigitalOut runCompressor;
    public RangeIn leftDistanceStream;
    public RangeIn rightDistanceStream;
    public DigitalOut leftSideDampStream;
    public DigitalOut rightSideDampStream;
    public DigitalOut frontClimbStream;

    public DigitalIn compressorToggle;


    public RobotSoftware() {

        compressorToggle = ControlBoard.getInstance().rightBumper();

        leftA = leftDriveA.getVoltageController();
        leftB = leftDriveB.getVoltageController();
        leftC = leftDriveC.getVoltageController();
        rightA = rightDriveA.getVoltageController();
        rightB = rightDriveB.getVoltageController();
        rightC = rightDriveC.getVoltageController();

        leftMaster = leftA.getWithAddedFollowers(leftB).getWithAddedFollowers(leftC);
        rightMaster = rightA.getWithAddedFollowers(rightB).getWithAddedFollowers(rightC);

        drive = new DriveOut(leftA, rightA);
        rightShoot = rightShooter.getDigitalOut();
        leftShoot = leftShooter.getDigitalOut();
        leftShoot = leftShooter.getDigitalOut();
        climbWheel = climbMotor.getVoltageController();
        leftSideDampStream = leftDamp.getDigitalOut();
        rightSideDampStream = rightDamp.getDigitalOut();
        frontClimbStream = climbFrontPiston.getDigitalOut().getInverted();
        climber = climbPiston.getDigitalOut();

        leftDistanceStream = leftDistance.getPositionInInches()
                .getMapped(position -> position * Math.sin(Math.toRadians(80)));
        rightDistanceStream = rightDistance.getPositionInInches()
                .getMapped(position -> position * Math.sin(Math.toRadians(80)));

        runCompressor = compressor.toggleCompressor();

    }

    public static RobotSoftware getInstance() {
        return INSTANCE;
    }

}
