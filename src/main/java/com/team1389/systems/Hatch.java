package com.team1389.systems;

import com.team1389.auto.command.WaitTimeCommand;
import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.system.Subsystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class Hatch extends Subsystem
{
    private DigitalOut hatchPiston;
    private DigitalOut cargoPiston;

    public Hatch(DigitalOut hatchPiston, DigitalOut cargoPiston)
    {
        this.hatchPiston = hatchPiston;
        this.cargoPiston = cargoPiston;
    }

    @Override
    public void init()
    {
    }

    @Override
    public void update()
    {
        scheduler.update();
    }

    @Override
    public AddList<Watchable> getSubWatchables(AddList<Watchable> stem)
    {
        return stem.put(scheduler);
    }

    @Override
    public String getName()
    {
        return "Teleop Hatch System";
    }

    private Command acquireHatchCommand()
    {
        return CommandUtil.combineSequential((CommandUtil.createCommand(() -> hatchPiston.set(true))),
                new WaitTimeCommand(.5), (CommandUtil.createCommand(() -> cargoPiston.set(true))),
                new WaitTimeCommand(.5), (CommandUtil.createCommand(() -> hatchPiston.set(false))));
    }

    private Command scoreHatchCommand()
    {
        return CommandUtil.combineSequential((CommandUtil.createCommand(() -> hatchPiston.set(true))),
                new WaitTimeCommand(.5), (CommandUtil.createCommand(() -> cargoPiston.set(false))),
                new WaitTimeCommand(1), (CommandUtil.createCommand(() -> hatchPiston.set(false))));
    }

    public void acquireHatch()
    {
        scheduler.cancelAll();
        scheduler.schedule(acquireHatchCommand());
    }

    public void scoreHatch()
    {
        scheduler.cancelAll();
        scheduler.schedule(scoreHatchCommand());
    }
}