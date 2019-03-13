package com.team1389.systems;

import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.system.Subsystem;
import com.team1389.system.drive.CurvatureDriveStraightSystem;
import com.team1389.system.drive.CurvatureDriveSystem;
import com.team1389.system.drive.DriveOut;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class ControlCompressorCurvatureDrive extends Subsystem
{
    private final double DRIVING_STRAIGHT_DEADZONE = .3;
    private CurvatureDriveSystem driveSystem;
    private PercentIn wheel;
    private DigitalOut toggleCompressorRunning;
    private DigitalIn noCompressorButton;
    private boolean compressorRunning;

    public ControlCompressorCurvatureDrive(DriveOut<Percent> drive, PercentIn throttle, PercentIn wheel,
            DigitalIn quickTurnButton, DigitalOut toggleCompressorRunning, DigitalIn noCompressorButton)
    {
        this.wheel = wheel;
        driveSystem = new CurvatureDriveSystem(drive, throttle, wheel, quickTurnButton);
        this.toggleCompressorRunning = toggleCompressorRunning;
        this.noCompressorButton = noCompressorButton;
    }

    @Override
    public void init()
    {
        driveSystem.init();
        compressorRunning = !noCompressorButton.get();
    }

    @Override
    public void update()
    {
        compressorRunning = !noCompressorButton.get() ^ compressorRunning;
        toggleCompressorRunning.set(compressorRunning);
        driveSystem.update();

    }

    @Override
    public String getName()
    {
        return "Cancel Curvature Drive";
    }

    @Override
    public AddList<Watchable> getSubWatchables(AddList<Watchable> stem)
    {
        return stem.put(driveSystem);
    }
}