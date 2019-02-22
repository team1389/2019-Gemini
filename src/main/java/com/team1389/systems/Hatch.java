package com.team1389.systems;

import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.system.Subsystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class Hatch extends Subsystem
{
    private DigitalOut hatchPiston;

    public Hatch(DigitalOut hatchPiston)
    {
        this.hatchPiston = hatchPiston;
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

    private Command extendHatchPistonCommand()
    {
        return CommandUtil.createCommand(() -> hatchPiston.set(true));
    }

    private Command retractHatchPistonCommand()
    {
        return CommandUtil.createCommand(() -> hatchPiston.set(false));
    }

    public void extendHatchPiston()
    {
        scheduler.cancelAll();
        scheduler.schedule(extendHatchPistonCommand());
    }

    public void retractHatchPiston()
    {
        scheduler.cancelAll();
        scheduler.schedule(retractHatchPistonCommand());
    }
}