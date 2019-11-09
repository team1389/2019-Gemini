package com.team1389.operation;

import com.team1389.hardware.controls.ControlBoard;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.robot.RobotSoftware;
import com.team1389.system.SystemManager;
import com.team1389.system.drive.CurvatureDriveSystem;
import com.team1389.watch.Watcher;
import com.team1389.system.Subsystem;
import com.team1389.systems.TeleopShooter;
import com.team1389.systems.ControlCompressorCurvatureDrive;
import com.team1389.systems.Dampener;
import com.team1389.systems.ManualArm;
import com.team1389.systems.SimpleClimber;
import com.team1389.systems.TeleopHatch;

public class TeleopMain {
    SystemManager manager;
    ControlBoard controls;
    RobotSoftware robot;
    TeleopShooter shooter;

    public TeleopMain(RobotSoftware robot) {
        this.robot = robot;
    }

    public void init() {
        controls = ControlBoard.getInstance();
        Subsystem drive = setUpDrive();
        shooter = setUpShooter();
        Subsystem damp = setUpDampener();
        manager = new SystemManager(drive, shooter, damp);
        manager.init();
        Watcher watcher = new Watcher();
        watcher.watch();
        watcher.outputToDashboard();
    }

    private Subsystem setUpDrive() {
        // TODO: test drive on full speed; worked well on half speed
        return new CurvatureDriveSystem(robot.drive, controls.driveLeftY(),
                controls.driveRightX(), controls.driveRightBumper());
    }

    private Subsystem setUpCancelDrive() {
        // TODO: thorough testing of this system
        return new ControlCompressorCurvatureDrive(robot.drive, controls.driveLeftY(),
                controls.driveRightX(), controls.driveRightBumper(), robot.runCompressor, controls.driveLeftBumper());
    }


    private TeleopShooter setUpShooter() {
        // r close(b),  r far(y), l close(a), l far(x)
        return new TeleopShooter(robot.rightShoot, robot.leftShoot, controls.bButton(), new DigitalIn(() -> false),
                controls.aButton(), new DigitalIn(() -> false));
    }

    private Subsystem setUpClimber() {
        return new SimpleClimber(robot.climber, robot.frontClimbStream, robot.climbWheel, controls.leftBumper(),
                controls.rightBumper(), controls.rightStickYAxis());
    }

    private Subsystem setUpHatch() {
        return new TeleopHatch(robot.hatchIntakeStream, robot.hatchExtensionStream, robot.hatchOuttakeStream,
                controls.yButton());
    }

    public void periodic() {
        manager.update();
    }

    private Subsystem setUpDampener() {
        return new Dampener(robot.leftSideDampStream, robot.rightSideDampStream, controls.xButton());
    }

    // public void disabledPeriodic()
    // {
    // System.out.println("bb" + robot.haveBall.get());
    // }
}
