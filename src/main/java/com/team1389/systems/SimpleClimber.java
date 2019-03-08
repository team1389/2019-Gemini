package com.team1389.systems;

import com.team1389.auto.command.WaitTimeCommand;
import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.system.Subsystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.BooleanInfo;

public class SimpleClimber extends Subsystem
{
    // Output
    private PercentOut wheelVoltage;
    private DigitalOut liftPiston;
    // Sensors
    // Controls
    private DigitalIn liftBtn;
    private DigitalIn retractBtn;
    private PercentIn forwardPwr;

    /**
     * @param liftPiston
     *                         Lifts robot off the ground
     * 
     * @param wheelVoltage
     *                         Sets voltage of wheel on the climber
     * 
     * @param bumpSwitch
     *                         Detects if robot is in back and climber should be
     *                         retracted
     * 
     * @param toggleLift
     *                         Extends and retracts piston
     */

    public SimpleClimber(DigitalOut liftPiston, PercentOut wheelVoltage, DigitalIn liftBtn, DigitalIn retractBtn,
            PercentIn forwardPwr)
    {
        this.liftPiston = liftPiston;
        this.wheelVoltage = wheelVoltage;
        this.forwardPwr = forwardPwr;
        this.liftBtn = liftBtn;
        this.retractBtn = retractBtn;
    }

    public AddList<Watchable> getSubWatchables(AddList<Watchable> stem)
    {
        return stem.put(scheduler);
    }

    @Override
    public String getName()
    {
        return "Climber";
    }

    public void init()
    {

    }

    public void update()
    {
        climb();
        scheduler.update();
    }

    public void climb()
    {
        if (liftBtn.get())
        {
            liftPiston.set(true);
        }
        if (forwardPwr.get() != 0)
        {
            wheelVoltage.set(forwardPwr.get());
        }
        if (retractBtn.get())
        {
            liftPiston.set(false);
        }
    }
}