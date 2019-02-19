package com.team1389.systems;

import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.system.Subsystem;
import com.team1389.system.drive.DriveOut;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;
import com.team1389.systems.Shooter;

public class TeleopShooter extends Subsystem
{
    // Controls
    private DigitalIn shootRightCloseButton;
    private DigitalIn shootRightFarButton;
    private DigitalIn shootLeftCloseButton;
    private DigitalIn shootLeftFarButton;
    private DigitalIn turnTo0Button;
    private DigitalIn turnto180Button;
    private DigitalIn cancelCommandsButton;
    // Output
    private DigitalOut rightShooter;
    private DigitalOut leftShooter;
    private DriveOut<Percent> drive;

    // Input
    private AngleIn<Position> robotAngle;

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

    public TeleopShooter(DigitalOut rightShooter, DigitalOut leftShooter, DriveOut<Percent> drive,
            AngleIn<Position> robotAngle, DigitalIn shootRightCloseButton, DigitalIn shootRightFarButton,
            DigitalIn shootLeftCloseButton, DigitalIn shootLeftFarButton, DigitalIn turnTo0Button,
            DigitalIn turnTo180Button, DigitalIn cancelCommandsButton)
    {
        this.rightShooter = rightShooter;
        this.leftShooter = leftShooter;
        this.drive = drive;
        this.robotAngle = robotAngle;
        this.shootLeftCloseButton = shootLeftCloseButton;
        this.shootLeftFarButton = shootLeftFarButton;
        this.shootRightCloseButton = shootRightCloseButton;
        this.shootRightFarButton = shootRightFarButton;
        this.turnTo0Button = turnTo0Button;
        this.turnto180Button = turnTo180Button;
        this.cancelCommandsButton = cancelCommandsButton;

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
        shooter = new Shooter(rightShooter, leftShooter, drive, robotAngle);
    }

    public void updateShooter()
    {
        if (cancelCommandsButton.get())
        {
            shooter.cancelAllCommands();
        }
        if (turnTo0Button.get())
        {
            shooter.turnToAbsolute0();
        }
        if (turnto180Button.get())
        {
            shooter.turnToAbsolute180();
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

    @Override
    public void update()
    {
        updateShooter();
    }

}