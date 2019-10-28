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
        watcher = new Watcher(robot.leftDistanceStream.getWatchable("left dist"),
                robot.rightDistanceStream.getWatchable("right dist"));
        watcher.outputToDashboard();
        robot.runCompressor.set(true);
        teleop = new TeleopMain(robot);
        toggleCompressor = true;
    }

    @Override
    public void autonomousInit() {
        executer = new AutoModeExecuter();
        executer.setAutoMode(new DriveStraightClosedLoop(robot.drive));
        executer.run();
    }

    @Override
    public void autonomousPeriodic() {
        //NOTE: This doesn't run because our executer just stalls the thread.
        //TODO: Is this an issue if our wait time is too long?
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
        toggleCompressor = toggleCompressor ^ robot.compressorToggle.get();
        robot.runCompressor.set(toggleCompressor);
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
