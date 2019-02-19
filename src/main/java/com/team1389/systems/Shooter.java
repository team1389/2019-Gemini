package com.team1389.systems;

import com.team1389.auto.command.WaitTimeCommand;
import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.system.Subsystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class Shooter extends Subsystem
{

    // Output
    private DigitalOut leftShooter;
    private DigitalOut rightShooter;
    // Constants
    private final double SHORT_SHOT_WAIT_TIME = .5; // TODO: tune these
    private final double LONG_SHOT_WAIT_TIME = 2;

    /**
     * @param rightShooter
     *                         Piston for shooting ball to the right
     * 
     * @param leftShooter
     *                         Piston for shooting ball to the left
     * 
     */

    public Shooter(DigitalOut rightShooter, DigitalOut leftShooter)
    {
        this.rightShooter = rightShooter;
        this.leftShooter = leftShooter;
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
}