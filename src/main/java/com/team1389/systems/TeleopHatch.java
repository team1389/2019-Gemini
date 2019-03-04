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
    private boolean haveHatch;

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
        extended = false;
        haveHatch = false;
    }

    @Override
    public void update()
    {

        extended = extended ^ hatchBtn.get();
        System.out.println("extended" + extended);
        if (extended && !haveHatch)
        {
            System.out.println("first condition");
            hatch.acquireHatch();
            haveHatch = true;
        }
        else if (!extended && haveHatch)
        {
            System.out.println("second condition");
            hatch.scoreHatch();
            haveHatch = false;
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