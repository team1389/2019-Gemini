package com.team1389.robot;

import com.team1389.auto.AutoModeExecuter;
import com.team1389.autonomous.DriveStraightClosedLoop;
import com.team1389.hardware.controls.ControlBoard;
import com.team1389.operation.TeleopMain;
import com.team1389.watch.Watcher;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.TimedRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {
    RobotSoftware robot;
    Watcher watcher;
    Compressor compressor;
    TeleopMain teleop;
    boolean toggleCompressor;
    AutoModeExecuter executer;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
        robot = RobotSoftware.getInstance();
        CameraServer.getInstance().startAutomaticCapture(0);
        CameraServer.getInstance().startAutomaticCapture(1);
        watcher = new Watcher(robot.leftDistanceStream.getWatchable("left dist"),
                robot.rightDistanceStream.getWatchable("right dist"));
        watcher.outputToDashboard();
        robot.runCompressor.set(false);
        teleop = new TeleopMain(robot);
        toggleCompressor = true;
        teleop.init();
    }

    @Override
    public void autonomousInit() {

    }

    @Override
    public void autonomousPeriodic() {
        teleop.periodic();

    }

    @Override
    public void teleopInit() {

    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
        teleop.periodic();
        toggleCompressor = toggleCompressor ^ robot.compressorToggle.get();
        robot.runCompressor.set(toggleCompressor);
        Watcher.update();

    }

    @Override
    public void disabledInit() {

    }

    @Override
    public void disabledPeriodic() {
        Watcher.update();
        // teleOperator.disabledPeriodic();
    }
}
