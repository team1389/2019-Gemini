package com.team1389.systems;

import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.system.Subsystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;
import com.team1389.systems.Shooter;
import com.team1389.systems.Alignment.Side;

public class TeleopShooter extends Subsystem
{
    // Controls
    private DigitalIn shootRightCloseButton;
    private DigitalIn shootRightFarButton;
    private DigitalIn shootLeftCloseButton;
    private DigitalIn shootLeftFarButton;
    private DigitalIn switchLeftButton;
    private DigitalIn switchRightButton;
    // Output
    private DigitalOut rightShooter;
    private DigitalOut leftShooter;

    private Shooter shooter;

    /**
     * @param shootRightCloseButton
     *                                  Input for shooting the ball to the
     *                                  closer target on the right
     * 
     * @param shootRightFarButton
     *                                  Input for shooting the ball to the
     *                                  farther target on the right
     * 
     * @param shootLeftCloseButton
     *                                  Input for shooting the ball to the
     *                                  closer target on the left
     * 
     * @param shootLeftFarButton
     *                                  Input for shooting the ball to the
     *                                  farther target on the left
     * 
     * @param hasCargo
     *                                  Detects whether there is a ball in the
     *                                  shooter or not
     * 
     * @param rightShooter
     *                                  Controller for shooting ball to the
     *                                  right
     * 
     * @param leftShooter
     *                                  Controller for shooting ball to the left
     */

    public TeleopShooter(DigitalOut rightShooter, DigitalOut leftShooter, DigitalIn shootRightCloseButton,
            DigitalIn shootRightFarButton, DigitalIn shootLeftCloseButton, DigitalIn shootLeftFarButton,
            DigitalIn switchLeftButton, DigitalIn switchRightButton)
    {
        this.rightShooter = rightShooter;
        this.leftShooter = leftShooter;
        this.shootLeftCloseButton = shootLeftCloseButton;
        this.shootLeftFarButton = shootLeftFarButton;
        this.shootRightCloseButton = shootRightCloseButton;
        this.shootRightFarButton = shootRightFarButton;
        this.switchLeftButton = switchLeftButton;
        this.switchRightButton = switchRightButton;
    }

    public AddList<Watchable> getSubWatchables(AddList<Watchable> stem)
    {
        return stem.put(shooter);
    }

    public String getName()
    {
        return "Teleop Shooter";
    }

    public void init()
    {
        shooter = new Shooter(rightShooter, leftShooter);
        shooter.init();
    }

    @Override
    public void update()
    {
        if (switchRightButton.get())
        {
            shooter.aligner.setSide(Side.RIGHT);
        }
        if (switchLeftButton.get())
        {
            shooter.aligner.setSide(Side.LEFT);
        }
        if (shootRightCloseButton.get())
        {
            shooter.shootRightClose();
        }
        if (shootRightFarButton.get())
        {
            shooter.shootRightFar();
        }
        if (shootLeftCloseButton.get())
        {
            shooter.shootLeftClose();
        }
        if (shootLeftFarButton.get())
        {
            shooter.shootLeftFar();
        }

        shooter.update();
    }

    public DigitalIn getAlignmentCommandsRunning()
    {
        return shooter.getAlignmentCommandsRunning();
    }
}