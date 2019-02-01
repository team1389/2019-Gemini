package com.team1389.systems;

import com.team1389.auto.command.WaitTimeCommand;
import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.system.Subsystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.BooleanInfo;

public class Climber extends Subsystem
{
    //Output
    private PercentOut wheelVoltage;
    private DigitalOut liftPiston;
    //Sensors
    private DigitalIn bumpSwitch;
    //Controls
    private DigitalIn toggleLift;

    /**
     * @param liftPiston Lifts robot off the ground
     * 
     * @param wheelVoltage Sets voltage of wheel on the climber
     * 
     * @param bumpSwitch Detects if robot is in back and climber should be retracted
     * 
     * @param toggleLift Extends and retracts piston
     */

    public Climber(DigitalOut liftPiston, PercentOut wheelVoltage, DigitalIn bumpSwitch, DigitalIn toggleLift)
    {
        this.wheelVoltage = wheelVoltage;
        this.liftPiston = liftPiston;
        this.bumpSwitch = bumpSwitch;
        this.toggleLift = toggleLift;
    }

    public AddList<Watchable> getSubWatchables(AddList<Watchable> stem)
    {
        return stem.put(scheduler, new BooleanInfo("switch", this::switchBumped));
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
        scheduler.update();

        if(toggleLift.get() == true && liftPiston.getAsBoolean() == false)
        {
            climb();
        }
        else if(toggleLift.get() == true && liftPiston.getAsBoolean() == true)
        {
            retract();
        }
        autoRetract();
    }

    public void climbPiston()
    {
        liftPiston.set(true);
    }
    public Command climbCommand()
    {
        return CommandUtil.createCommand(this::climbPiston);
    }
    public Command climbandGoForwards()
    {
        return CommandUtil.combineSequential(climbCommand(), new WaitTimeCommand(5), goForwardsCommand());
    }
    public void climb()
    {
        scheduler.schedule(climbandGoForwards());
    }

    public void goForwardsWheel()
    {
        wheelVoltage.set(.1);
    }

    public Command goForwardsCommand()
    {
        return CommandUtil.createCommand(this::goForwardsWheel);
    }  

    public boolean switchBumped()
    {
        return bumpSwitch.get();
    }

    public void retract()
    {
        wheelVoltage.set(0);
        liftPiston.set(false);
    }

    public Command retractCommand()
    {
        return CommandUtil.createCommand(this::retract);        
    }

    public void autoRetract()
    {
        if(switchBumped())
        {
            wheelVoltage.set(0);
            liftPiston.set(false);
        }
    }

    public Command autoRetractCommand()
    {
        return CommandUtil.createCommand(this::autoRetract);
    }
}