package com.team1389.systems;

import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.system.Subsystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class Dampener extends Subsystem
{

    private DigitalOut leftSide;
    private DigitalOut rightSide;
    private DigitalIn btn;

    private boolean actuate;

    public Dampener(DigitalOut leftSide, DigitalOut rightSide, DigitalIn btn)
    {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.btn = btn;
    }

    @Override
    public void init()
    {
        actuate = false;
    }

    @Override
    public void update()
    {
        actuate = btn.get() ^ actuate;

        if (actuate)
        {
            leftSide.set(true);
            rightSide.set(true);
        }
        else
        {
            leftSide.set(false);
            rightSide.set(false);
        }

    }

    @Override
    public AddList<Watchable> getSubWatchables(AddList<Watchable> stem)
    {
        return stem;
    }

    @Override
    public String getName()
    {
        return "Dampener";
    }

}