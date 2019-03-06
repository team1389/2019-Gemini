package com.team1389.systems;

import com.team1389.auto.command.TurnAngleCommand;
import com.team1389.auto.command.WaitTimeCommand;
import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.configuration.PIDConstants;
import com.team1389.controllers.SynchronousPIDController;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Value;
import com.team1389.robot.RobotConstants;
import com.team1389.system.Subsystem;
import com.team1389.system.drive.DriveOut;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;
import com.team1389.watch.Watcher;
import com.team1389.watch.info.NumberInfo;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Alignment extends Subsystem
{
    // TODO: Figure out a solution to the issue that vision will give us default
    // (320), whenever we switch sides.

    // NetworkTables IDs
    private final String VISION_NETWORK_TABLE_ID = "vision";
    private final String VISION_LEFT_SIDE_X_ID = "LeftSideX";
    private final String VISION_RIGHT_SIDE_X_ID = "RightSideX";
    private final String VISION_TOGGLE_RUNNING_SIDE_ID = "SwitchSides";

    // NetworkTables Entries
    private NetworkTableEntry leftSideXEntry;
    private NetworkTableEntry rightSideXEntry;
    private NetworkTableEntry toggleRunningSideEntry;

    private final int CENTER_X_VAL = 320;

    private DriveOut<Percent> drive;

    private RangeIn<Position> robotAngle;

    private final double VISION_ALIGNMENT_TOLERANCE = 50;
    private final double TURN_TOLERANCE_IN_DEGREES = 5;

    private SynchronousPIDController<Percent, Position> longitudinalControllerLeft;
    private SynchronousPIDController<Percent, Position> longitudinalControllerRight;

    private SynchronousPIDController<Percent, Position> lateralController;

    private RangeIn<Position> targetPositionLeft;
    private RangeIn<Position> targetPositionRight;

    private Side currentState;

    /**
     * 
     * @param drive
     *                       distance reading for right side in inches
     * @param robotAngle
     *                       heading of the robot in degrees, wrapped on the
     *                       range [0,360]
     */
    public Alignment(DriveOut<Percent> drive, RangeIn<Position> robotAngle)
    {
        this.drive = drive;
        this.robotAngle = robotAngle;
    }

    @Override
    public void init()
    {
        NetworkTable table = NetworkTableInstance.getDefault().getTable(VISION_NETWORK_TABLE_ID);
        leftSideXEntry = table.getEntry(VISION_LEFT_SIDE_X_ID);
        rightSideXEntry = table.getEntry(VISION_RIGHT_SIDE_X_ID);
        toggleRunningSideEntry = table.getEntry(VISION_TOGGLE_RUNNING_SIDE_ID);
        currentState = Side.LEFT;
        targetPositionLeft = new RangeIn<Position>(Position.class, () -> leftSideXEntry.getDouble(CENTER_X_VAL), 0,
                720);
        targetPositionRight = new RangeIn<Position>(Position.class, () -> rightSideXEntry.getDouble(CENTER_X_VAL), 0,
                720);
        lateralController = new SynchronousPIDController<>(RobotConstants.LATERAL_PID_CONSTANTS, robotAngle,
                TurnAngleCommand.createTurnController(drive));
        longitudinalControllerLeft = new SynchronousPIDController<Percent, Position>(
                RobotConstants.LONGITUDINAL_PID_CONSTANTS, targetPositionLeft,
                drive.left().getWithAddedFollowers(drive.right()));
        longitudinalControllerRight = new SynchronousPIDController<>(RobotConstants.LONGITUDINAL_PID_CONSTANTS,
                targetPositionRight, drive.left().getWithAddedFollowers(drive.right()));
    }

    @Override
    public void update()
    {
        scheduler.update();
    }

    @Override
    public String getName()
    {
        return "Alignment System";
    }

    @Override
    public AddList<Watchable> getSubWatchables(AddList<Watchable> stem)
    {
        return stem;
    }

    public enum Side
    {
        LEFT, RIGHT
    }

    public void setSide(Side desired)
    {
        if (currentState != desired)
        {
            if (currentState == Side.LEFT)
            {
                currentState = Side.RIGHT;
            }
            else if (currentState == Side.RIGHT)
            {
                currentState = Side.LEFT;
            }
            toggleRunningSideEntry.setBoolean(true);
        }
        scheduler.schedule(CommandUtil.combineSequential(new WaitTimeCommand(.5),
                CommandUtil.createCommand(() -> toggleRunningSideEntry.setBoolean(false))));
    }

    public Command centerOnTarget()
    {
        scheduler.cancelAll();
        if (currentState == Side.LEFT)
        {
            return longitudinalControllerLeft.getPIDToCommand(320, VISION_ALIGNMENT_TOLERANCE);
        }
        return longitudinalControllerRight.getPIDToCommand(320, VISION_ALIGNMENT_TOLERANCE);
    }

    public Command alignAngle()
    {
        scheduler.cancelAll();
        double startingAngle = robotAngle.get();
        double targetAngle;
        if (startingAngle < 90 && startingAngle > -90)
        {
            targetAngle = 0;
        }
        else
        {
            targetAngle = 180;
        }
        return lateralController.getPIDToCommand(targetAngle, TURN_TOLERANCE_IN_DEGREES);
    }

    public void fullAlign()
    {
        scheduler.cancelAll();
        scheduler.schedule(alignAngle());
        scheduler.schedule(centerOnTarget());
    }
}