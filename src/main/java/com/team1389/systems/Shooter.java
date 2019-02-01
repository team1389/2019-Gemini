package com.team1389.systems;

import com.team1389.auto.command.WaitTimeCommand;
import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.system.Subsystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class Shooter extends Subsystem
{
   
    //Output
    private DigitalOut leftShooter;
    private DigitalOut rightShooter;
    //Sensors
    private DigitalIn hasCargo;
    //Constants
    private final int WAIT_UNTIL_EXTENDED = 1;

    /**
     * @param rightShooter Piston for shooting ball to the right
     * 
     * @param leftShooter Piston for shooting ball to the left
     * 
     * @param hasCargo Beam break checking whether there is a ball in the shooter or not
     */

    
    public Shooter(DigitalOut rightShooter, DigitalOut leftShooter, DigitalIn hasCargo)
    {
        this.rightShooter = rightShooter;
        this.leftShooter = leftShooter;
        this.hasCargo = hasCargo;
    }

    public AddList<Watchable> getSubWatchables(AddList<Watchable> stem)
    {
        return stem.put(scheduler, hasCargo.getWatchable("hasCargo"));
    }

    @Override
    public String getName()
    {
        return "Shooter";
    }

    public void init()
    {

    }

    public void update()
    {
        scheduler.update();
    }

    private void shootRightPiston()
    {
        if(hasCargo())
        {
            rightShooter.set(true);
        }
    }

    private boolean hasCargo() {
        return hasCargo.get();
    }

    private Command shootRightCommand()
    {
        return CommandUtil.createCommand(this::shootRightPiston);
    }

    private void shootLeftPiston()
    {
        if(hasCargo())
        {
            leftShooter.set(true);
        }
    }

    private Command shootLeftCommand()
    {
        return CommandUtil.createCommand(this::shootLeftPiston);
        
    }

    private void resetShooters()
    {
        leftShooter.set(false);
        rightShooter.set(false);
    }

    private Command resetShootersCommand()
    {
        return CommandUtil.createCommand(this::resetShooters);
    }  

    private Command shootRightReset()
    {
        return CommandUtil.combineSequential(shootRightCommand(), new WaitTimeCommand(WAIT_UNTIL_EXTENDED), resetShootersCommand());
        
    }

    public void shootRight()
    {
        scheduler.schedule(shootRightReset());
    }

    private Command shootLeftReset()
    {
        return CommandUtil.combineSequential(shootLeftCommand(), new WaitTimeCommand(WAIT_UNTIL_EXTENDED), resetShootersCommand());
    }

    public void shootLeft()
    {
        scheduler.schedule(shootLeftReset());
    }
}