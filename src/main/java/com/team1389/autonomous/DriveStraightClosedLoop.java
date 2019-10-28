package com.team1389.autonomous;

import com.team1389.auto.AutoModeBase;
import com.team1389.auto.AutoModeEndedException;
import com.team1389.auto.command.WaitTimeCommand;
import com.team1389.hardware.value_types.Percent;
import com.team1389.system.drive.DriveOut;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class DriveStraightClosedLoop extends AutoModeBase {
    private final double DRIVE_TIME_IN_MS = 1000;
    private final double VOLTAGE_PERCENT = 0.5;
    DriveOut<Percent> drive;
    public DriveStraightClosedLoop(DriveOut drive){
        this.drive = drive;
    }
    @Override
    protected void routine() throws AutoModeEndedException {
        drive.set(VOLTAGE_PERCENT, VOLTAGE_PERCENT);
        runCommand(new WaitTimeCommand(DRIVE_TIME_IN_MS));
        drive.set(0,0);
    }

    @Override
    public String getIdentifier() {
        return "DriveStraightClosedLoop";
    }

    @Override
    public AddList<Watchable> getSubWatchables(AddList<Watchable> addList) {
        return addList;
    }
}
