package com.team1389.robot;

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

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
        robot = RobotSoftware.getInstance();
        watcher = new Watcher(robot.leftDistanceStream.getWatchable("left dist"),
                robot.rightDistanceStream.getWatchable("right dist"));
        watcher.outputToDashboard();
        robot.runCompressor.set(true);
        teleop = new TeleopMain(robot);
    }

    @Override
    public void autonomousInit() {

    }

    @Override
    public void autonomousPeriodic() {
        Watcher.update();
    }

    @Override
    public void teleopInit() {
        teleop.init();
    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
        teleop.periodic();
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
