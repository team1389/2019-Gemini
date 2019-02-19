package com.team1389.systems;

import com.team1389.auto.command.TurnAngleCommand;
import com.team1389.auto.command.WaitTimeCommand;
import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.configuration.PIDConstants;
import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.system.Subsystem;
import com.team1389.system.drive.DriveOut;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class Shooter extends Subsystem
{

    // Output
    private DigitalOut leftShooter;
    private DigitalOut rightShooter;

    // Drive motors (for getting parallel and aligning with vision)
    private DriveOut<Percent> drive;
    private RangeOut<Percent> turnController;
    private AngleIn<Position> robotAngle;

    // Shooter Constants
    private final double SHORT_SHOT_WAIT_TIME = .5;
    private final double LONG_SHOT_WAIT_TIME = 2;

    // Drive constants
    private final double TURN_TOLERANCE_IN_DEGREES = 3;
    private final PIDConstants TURN_ANGLE_PID = new PIDConstants(0.01, 0, 0);

    /**
     * @param rightShooter
     *                         Piston for shooting ball to the right
     * 
     * @param leftShooter
     *                         Piston for shooting ball to the left
     * 
     * @param drive
     *                         reference to controllers for each side of
     *                         drivetrain
     * @param robotAngle
     *                         provides angle of robot in degrees, on a range
     *                         from [0,360]
     * 
     */

    public Shooter(DigitalOut rightShooter, DigitalOut leftShooter, DriveOut<Percent> drive,
            AngleIn<Position> robotAngle)
    {
        this.rightShooter = rightShooter;
        this.leftShooter = leftShooter;

        this.drive = drive;
        this.robotAngle = robotAngle;
    }

    public AddList<Watchable> getSubWatchables(AddList<Watchable> stem)
    {
        return stem.put(scheduler);
    }

    @Override
    public String getName()
    {
        return "Shooter";
    }

    public void init()
    {
        turnController = TurnAngleCommand.createTurnController(drive);
    }

    public void update()
    {
        scheduler.update();
    }

    private Command shootRightCloseCommand()
    {
        return CommandUtil.combineSequential(CommandUtil.createCommand(() ->
        {
            rightShooter.set(true);
        }), new WaitTimeCommand(SHORT_SHOT_WAIT_TIME), CommandUtil.createCommand(() ->
        {
            rightShooter.set(false);
        }));
    }

    private Command shootRightFarCommand()
    {
        return CommandUtil.combineSequential(CommandUtil.createCommand(() ->
        {
            rightShooter.set(true);
        }), new WaitTimeCommand(LONG_SHOT_WAIT_TIME), CommandUtil.createCommand(() ->
        {
            rightShooter.set(false);
        }));
    }

    private Command shootLeftCloseCommand()
    {
        return CommandUtil.combineSequential(CommandUtil.createCommand(() ->
        {
            leftShooter.set(true);
        }), new WaitTimeCommand(SHORT_SHOT_WAIT_TIME), CommandUtil.createCommand(() ->
        {
            leftShooter.set(false);
        }));
    }

    private Command shootLeftFarCommand()
    {
        return CommandUtil.combineSequential(CommandUtil.createCommand(() ->
        {
            leftShooter.set(true);
        }), new WaitTimeCommand(LONG_SHOT_WAIT_TIME), CommandUtil.createCommand(() ->
        {
            leftShooter.set(false);
        }));
    }

    public void shootRightClose()
    {
        scheduler.cancelAll();
        scheduler.schedule(shootRightCloseCommand());
    }

    public void shootRightFar()
    {
        scheduler.cancelAll();
        scheduler.schedule(shootRightFarCommand());
    }

    public void shootLeftClose()
    {
        scheduler.cancelAll();
        scheduler.schedule(shootLeftCloseCommand());
    }

    public void shootLeftFar()
    {
        scheduler.cancelAll();
        scheduler.schedule(shootLeftFarCommand());
    }

    /**
     * rotate the robot to be at 180 degrees relative to startup angle
     */
    public void turnToAbsolute180()
    {

        scheduler.cancelAll();
        TurnAngleCommand<Percent> turnCommand = new TurnAngleCommand<>(180, true, TURN_TOLERANCE_IN_DEGREES, robotAngle,
                turnController, TURN_ANGLE_PID);
        scheduler.schedule(turnCommand);
    }

    /**
     * rotate the robot to be at 0 degrees relative to startup angle
     */
    public void turnToAbsolute0()
    {
        scheduler.cancelAll();
        TurnAngleCommand<Percent> turnCommand = new TurnAngleCommand<>(0, true, TURN_TOLERANCE_IN_DEGREES, robotAngle,
                turnController, TURN_ANGLE_PID);
        scheduler.schedule(turnCommand);
    }

    public void cancelAllCommands()
    {
        scheduler.cancelAll();
    }
}