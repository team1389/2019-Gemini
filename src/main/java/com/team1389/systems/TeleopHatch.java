package com.team1389.systems;

import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.system.Subsystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class TeleopHatch extends Subsystem
{
    private DigitalIn extendHatchBtn;
    private DigitalIn retractHatchBtn;

    private Hatch hatch;
    private DigitalOut hatchPiston;

    @Override
    public void init()
    {
        hatch = new Hatch(hatchPiston);
    }

    @Override
    public void update()
    {
        if (extendHatchBtn.get())
        {
            hatch.extendHatchPiston();
        }
        else if (retractHatchBtn.get())
        {
            hatch.retractHatchPiston();
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
