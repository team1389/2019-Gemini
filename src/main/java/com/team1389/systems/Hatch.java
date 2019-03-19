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
    private DigitalOut outtakePiston;

    public Hatch(DigitalOut hatchPiston, DigitalOut cargoPiston, DigitalOut outtakePiston)
    {
        this.hatchPiston = hatchPiston;
        this.cargoPiston = cargoPiston;
        this.outtakePiston = outtakePiston;
    }

    @Override
    public void init()
    {
        hatchPiston.set(false);
        cargoPiston.set(false);
        outtakePiston.set(false);
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
                new WaitTimeCommand(.125), (CommandUtil.createCommand(() -> outtakePiston.set(true))),
                new WaitTimeCommand(.25), (CommandUtil.createCommand(() -> cargoPiston.set(false))),
                new WaitTimeCommand(.125), (CommandUtil.createCommand(() -> hatchPiston.set(false))),
                new WaitTimeCommand(.125), (CommandUtil.createCommand(() -> outtakePiston.set(false))));
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