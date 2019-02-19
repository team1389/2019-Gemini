package com.team1389.systems;

import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.system.Subsystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

import edu.wpi.first.networktables.NetworkTable;

import com.team1389.systems.VisionShooter;

public class TeleopShooter extends Subsystem
{
    // Controls
    private DigitalIn shootRightCloseButton;
    private DigitalIn shootRightFarButton;
    private DigitalIn shootLeftCloseButton;
    private DigitalIn shootLeftFarButton;
    // Output
    private DigitalOut rightShooter;
    private DigitalOut leftShooter;
    private RangeOut<Percent> drive;
    private NetworkTable table;

    private VisionShooter shooter;

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

    public TeleopShooter(DigitalOut rightShooter, DigitalOut leftShooter, RangeOut<Percent> drive, NetworkTable table,
            DigitalIn shootRightCloseButton, DigitalIn shootRightFarButton, DigitalIn shootLeftCloseButton,
            DigitalIn shootLeftFarButton)
    {
        this.rightShooter = rightShooter;
        this.leftShooter = leftShooter;
        this.shootLeftCloseButton = shootLeftCloseButton;
        this.shootLeftFarButton = shootLeftFarButton;
        this.shootRightCloseButton = shootRightCloseButton;
        this.shootRightFarButton = shootRightFarButton;
        this.drive = drive;
        this.table = table;
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
        shooter = new VisionShooter(rightShooter, leftShooter, drive, table);
    }

    public void updateShooter()
    {
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

    @Override
    public void update()
    {
        updateShooter();
    }

}