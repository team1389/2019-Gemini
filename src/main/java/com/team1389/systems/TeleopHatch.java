package com.team1389.systems;

import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.system.Subsystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class TeleopHatch extends Subsystem
{
    private DigitalIn hatchBtn;

    private Hatch hatch;
    private DigitalOut hatchPiston;
    private DigitalOut cargoPiston;
    private boolean extended;

    public TeleopHatch(DigitalOut hatchPiston, DigitalOut cargoPiston, DigitalIn hatchBtn)
    {
        this.hatchPiston = hatchPiston;
        this.cargoPiston = cargoPiston;
        this.hatchBtn = hatchBtn;
    }

    @Override
    public void init()
    {
        hatch = new Hatch(hatchPiston, cargoPiston);
        hatch.init();
        extended = hatchBtn.get();
    }

    @Override
    public void update()
    {
        extended = extended ^ hatchBtn.get();
        if (extended)
        {
            hatch.acquireHatch();
        }
        else if (!extended)
        {
            hatch.scoreHatch();
        }
        hatch.update();
    }

    @Override
    public AddList<Watchable> getSubWatchables(AddList<Watchable> stem)
    {
        return stem.put(hatch);
    }

    @Override
    public String getName()
    {
        return "Hatch System";
    }
}