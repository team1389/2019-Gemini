package com.team1389.systems;

import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.system.Subsystem;
import com.team1389.system.drive.CurvatureDriveStraightSystem;
import com.team1389.system.drive.DriveOut;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class ModifiedStraightCurvatureDrive extends Subsystem
{
    private CurvatureDriveStraightSystem driveStraight;
    DigitalIn alignmentRunning;

    public ModifiedStraightCurvatureDrive(DriveOut drive, PercentIn throttle, PercentIn wheel,
            DigitalIn quickTurnButton, double turnSensitivity, double spinSensitivity,AngleIn angle, double kP, DigitalIn driveStraightButton,
            DigitalIn alignmentRunning)
    {
        driveStraight = new CurvatureDriveStraightSystem(drive, throttle, wheel, quickTurnButton,turnSensitivity, spinSensitivity, angle, kP,
                driveStraightButton);
        this.alignmentRunning = alignmentRunning;
    }

    @Override
    public void init()
    {
        driveStraight.init();
    }

    @Override
    public String getName()
    {
        return "Modified Straight Curvature Drive";
    }

    @Override
    public AddList<Watchable> getSubWatchables(AddList<Watchable> arg0)
    {
        //add this back
        return arg0;
    }


    @Override

    public void update()
    {
        // don't update if the alignment system is trying to do something
        if (!alignmentRunning.get())
        {
            driveStraight.update();
        }
    }

}