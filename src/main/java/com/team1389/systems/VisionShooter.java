package com.team1389.systems;

import com.team1389.auto.command.TurnAngleCommand;
import com.team1389.auto.command.WaitTimeCommand;
import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.configuration.PIDConstants;
import com.team1389.controllers.SynchronousPIDController;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.system.Subsystem;
import com.team1389.system.drive.DriveOut;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;

public class VisionShooter extends Subsystem
{

    // Output
    private DigitalOut leftShooter;
    private DigitalOut rightShooter;
    private RangeOut<Percent> drive;
    private SynchronousPIDController<Percent, Position> leftPIDController, rightPIDController;
    private NetworkTableEntry leftTarget, rightTarget;
    NetworkTable table;
    // Constants
    private final double SHORT_SHOT_WAIT_TIME = .5;
    private final double LONG_SHOT_WAIT_TIME = 2;
    private PIDConstants pid;
    private final int center = 320;
    private final double TOLERANCE = 3;

    /**
     * @param rightShooter
     *                         Piston for shooting ball to the right
     * 
     * @param leftShooter
     *                         Piston for shooting ball to the left
     * 
     */

    public VisionShooter(DigitalOut rightShooter, DigitalOut leftShooter, RangeOut<Percent> drive, NetworkTable table)
    {
        this.rightShooter = rightShooter;
        this.leftShooter = leftShooter;
        this.drive = drive;
        this.table = table;
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
        pid = new PIDConstants(.001, 0, 0);
        leftTarget = table.getEntry("LeftSideX");
        rightTarget = table.getEntry("RightSideX");
        RangeIn<Position> leftOffset = new RangeIn<Position>(Position.class,
                () -> leftTarget.getDouble(center) - center, 0, 640);
        RangeIn<Position> rightOffset = new RangeIn<Position>(Position.class,
                () -> rightTarget.getDouble(center) - center, 0, 640);
        leftPIDController = new SynchronousPIDController<Percent, Position>(pid, leftOffset, drive);
        rightPIDController = new SynchronousPIDController<Percent, Position>(pid, rightOffset, drive);
    }

    public void update()
    {
        scheduler.update();
    }

    private Command shootRightCloseCommand()
    {
        return CommandUtil.combineSequential(alignRightCommand(), CommandUtil.createCommand(() ->
        {
            rightShooter.set(true);
        }), new WaitTimeCommand(SHORT_SHOT_WAIT_TIME), CommandUtil.createCommand(() ->
        {
            rightShooter.set(false);
        }));
    }

    private Command shootRightFarCommand()
    {
        return CommandUtil.combineSequential(alignRightCommand(), CommandUtil.createCommand(() ->
        {
            rightShooter.set(true);
        }), new WaitTimeCommand(LONG_SHOT_WAIT_TIME), CommandUtil.createCommand(() ->
        {
            rightShooter.set(false);
        }));
    }

    private Command shootLeftCloseCommand()
    {
        return CommandUtil.combineSequential(alignLeftCommand(), CommandUtil.createCommand(() ->
        {
            leftShooter.set(true);
        }), new WaitTimeCommand(SHORT_SHOT_WAIT_TIME), CommandUtil.createCommand(() ->
        {
            leftShooter.set(false);
        }));
    }

    private Command shootLeftFarCommand()
    {
        return CommandUtil.combineSequential(alignLeftCommand(), CommandUtil.createCommand(() ->
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

    private Command alignLeftCommand()
    {
        return CommandUtil.combineSequential(CommandUtil.createCommand(() ->
        {
            leftPIDController.setEnabled(true);
        }), leftPIDController.getPIDToCommand(TOLERANCE), CommandUtil.createCommand(() ->
        {
            leftPIDController.setEnabled(false);
        }));
    }

    private Command alignRightCommand()
    {
        return CommandUtil.combineSequential(CommandUtil.createCommand(() ->
        {
            rightPIDController.setEnabled(true);
        }), rightPIDController.getPIDToCommand(TOLERANCE), CommandUtil.createCommand(() ->
        {
            rightPIDController.setEnabled(false);
        }));
    }
}