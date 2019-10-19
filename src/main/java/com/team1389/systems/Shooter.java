package com.team1389.systems;

import com.team1389.auto.command.WaitTimeCommand;
import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.system.Subsystem;
import com.team1389.system.drive.DriveOut;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class Shooter extends Subsystem {
    // Constants
    private final double SHORT_SHOT_WAIT_TIME = .5; // TODO: tune these
    private final double LONG_SHOT_WAIT_TIME = 2;
    private final double RETRACT_WAIT_TIME = 0.2;
    // Input
    private AngleIn robotAngle;
    // Output
    private DigitalOut leftShooter;
    private DigitalOut rightShooter;
    private DriveOut<Percent> drive;

    /**
     * @param rightShooter Piston for shooting ball to the right
     * @param leftShooter  Piston for shooting ball to the left
     */

    public Shooter(DigitalOut rightShooter, DigitalOut leftShooter) {
        this.rightShooter = rightShooter;
        this.leftShooter = leftShooter;
    }

    public Shooter(DigitalOut rightShooter, DigitalOut leftShooter, DriveOut<Percent> drive) {
        this.rightShooter = rightShooter;
        this.leftShooter = leftShooter;
        this.drive = drive;
    }

    public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
        return stem.put(scheduler);
    }

    @Override
    public String getName() {
        return "Shooter";
    }

    public void init() {
    }

    public void update() {
        scheduler.update();
    }

    private Command shootRightCloseCommand() {
        return CommandUtil.combineSequential(CommandUtil.createCommand(() ->
        {
            rightShooter.set(true);
        }), new WaitTimeCommand(SHORT_SHOT_WAIT_TIME), CommandUtil.createCommand(() ->
        {
            rightShooter.set(false);
        }));
    }

    private Command shootRightFarCommand() {
        return CommandUtil.combineSequential(CommandUtil.createCommand(() ->
        {
            rightShooter.set(true);
        }), new WaitTimeCommand(LONG_SHOT_WAIT_TIME), CommandUtil.createCommand(() ->
        {
            rightShooter.set(false);
        }));
    }

    private Command shootLeftCloseCommand() {
        return CommandUtil.combineSequential(CommandUtil.createCommand(() ->
        {
            leftShooter.set(true);
        }), new WaitTimeCommand(SHORT_SHOT_WAIT_TIME), CommandUtil.createCommand(() ->
        {
            leftShooter.set(false);
        }));
    }

    private Command shootLeftFarCommand() {
        return CommandUtil.combineSequential(CommandUtil.createCommand(() ->
        {
            leftShooter.set(true);
        }), new WaitTimeCommand(LONG_SHOT_WAIT_TIME), CommandUtil.createCommand(() ->
        {
            leftShooter.set(false);
        }));
    }

    private Command retractAllPistons() {
        return CommandUtil.combineSequential(CommandUtil.createCommand(() -> {
            rightShooter.set(false);
            leftShooter.set(false);
        }), new WaitTimeCommand(RETRACT_WAIT_TIME));


    }

    public void shootRightClose() {
        scheduler.cancelAll();
        scheduler.schedule(retractAllPistons());
        scheduler.schedule(shootRightCloseCommand());
    }

    public void shootRightFar() {
        scheduler.cancelAll();
        scheduler.schedule(retractAllPistons());
        scheduler.schedule(shootRightFarCommand());
    }

    public void shootLeftClose() {
        scheduler.cancelAll();
        scheduler.schedule(retractAllPistons());
        scheduler.schedule(shootLeftCloseCommand());
    }

    public void shootLeftFar() {
        scheduler.cancelAll();
        scheduler.schedule(retractAllPistons());
        scheduler.schedule(shootLeftFarCommand());
    }

}