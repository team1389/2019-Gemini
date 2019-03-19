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
    private DigitalOut liftPistonBack;
    private DigitalOut liftPistonFront;
    // Sensors
    // Controls
    private DigitalIn liftBackBtn;
    private DigitalIn liftFrontBtn;
    private PercentIn forwardPwr;

    private boolean back;
    private boolean front;

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

    public SimpleClimber(DigitalOut liftPistonBack, DigitalOut liftPistonFront, PercentOut wheelVoltage,
            DigitalIn liftBackBtn, DigitalIn liftFrontBtn, PercentIn forwardPwr)
    {
        this.liftPistonBack = liftPistonBack;
        this.liftPistonFront = liftPistonFront;
        this.wheelVoltage = wheelVoltage;
        this.forwardPwr = forwardPwr;
        this.liftFrontBtn = liftFrontBtn;
        this.liftBackBtn = liftBackBtn;
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
        back = false;
        front = false;
    }

    public void update()
    {
        back = liftBackBtn.get() ^ back;
        front = liftFrontBtn.get() ^ front;
        if (back)
        {
            liftPistonBack.set(true);
        }
        if (front)
        {
            liftPistonFront.set(true);
        }
        if (!front && !back)
        {
            liftPistonBack.set(false);
            liftPistonFront.set(false);
        }
        wheelVoltage.set(forwardPwr.get());
        scheduler.update();
    }

}