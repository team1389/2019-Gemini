package com.team1389.systems;

import com.team1389.auto.command.WaitTimeCommand;
import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.configuration.PIDConstants;
import com.team1389.controllers.SynchronousPIDController;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.system.Subsystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.StringInfo;

/**
 * implements autonomous control of arm subsystem
 */

public class Arm extends Subsystem
{
    // Closed-loop control
    private SynchronousPIDController<Percent, Position> controller;
    private PIDConstants pidConstants;
    private final int TOLERANCE_IN_DEGREES = 3;

    private State currentState;

    // output
    private DigitalOut cargoLauncher;
    private RangeOut<Percent> cargoIntake;
    private RangeOut<Percent> armVoltage;

    // sensors
    private DigitalIn cargoIntakeBeamBreak;
    private RangeIn<Position> armAngle;

    /**
     *
     * @param cargoLauncher
     *                                 controller for piston that hits ball into
     *                                 intake
     * @param cargoIntake
     *                                 controller for flywheel intake
     * @param arm
     *                                 controller for arm motion
     * @param cargoIntakeBeamBreak
     *                                 input from beam break that detects if
     *                                 cargo is in the intake (Must be true when
     *                                 it detects)
     * @param armAngle
     *                                 gives angle of the arm in degrees
     */
    public Arm(DigitalOut cargoLauncher, RangeOut<Percent> cargoIntake, RangeOut<Percent> armVoltage,
            DigitalIn cargoIntakeBeamBreak, RangeIn<Position> armAngle)
    {
        this.cargoLauncher = cargoLauncher;
        this.cargoIntake = cargoIntake;
        this.armVoltage = armVoltage;
        this.cargoIntakeBeamBreak = cargoIntakeBeamBreak;
        this.armAngle = armAngle;

    }

    // TODO: Tune PID constancts
    @Override
    public void init()
    {
        pidConstants = new PIDConstants(0.01, 0, 0);
        controller = new SynchronousPIDController<Percent, Position>(pidConstants, armAngle, armVoltage);
        controller.setInputRange(-15, 115);
        currentState = State.STORE_CARGO;
        enterState(currentState);
    }

    @Override
    public void update()
    {
        scheduler.update();
    }

    public enum State
    {
        INTAKE_CARGO_FROM_GROUND(0, "Intake Cargo"), STORE_CARGO(115, "Store Cargo"), OUTTAKE_CARGO(45,
                "Outtake Cargo"), CLIMBING(100, "Climbing");
        // assumes 0 is when arm is parallel with the robot top
        private double angle;
        private String name;

        private State(double angle, String name)
        {
            this.angle = angle;
            this.name = name;
        }
    }

    // Probably need wait times before outtaking for most of these
    // if we want to be more efficient we can string
    public void enterState(State desiredState)
    {
        reset();
        switch (desiredState)
        {
        case INTAKE_CARGO_FROM_GROUND:
            Command cargoPickUp = CommandUtil.combineSequential(extendCargoPistonsCommand(false),
                    goToAngle(desiredState.angle), intakeCargoCommand()).setName("move and intake cargo");
            scheduler.schedule(cargoPickUp);
            scheduler.schedule(goToAngle(State.STORE_CARGO.angle));
            break;

        case OUTTAKE_CARGO:
            Command outtakeCargo = CommandUtil
                    .combineSequential(controller.getPIDToCommand(TOLERANCE_IN_DEGREES),
                            extendCargoPistonsCommand(true), goToAngle(desiredState.angle), outtakeCargoCommand())
                    .setName("move and outtake cargo");
            scheduler.schedule(outtakeCargo);
            scheduler.schedule(goToAngle(State.STORE_CARGO.angle));

            break;
        case CLIMBING:
            Command goToClimbing = goToAngle(desiredState.angle);
            scheduler.schedule(goToClimbing);
            break;
        case STORE_CARGO:
            Command storeCargo = CommandUtil.combineSequential(goToAngle(desiredState.angle),
                    outtakeCargoCommand().setName("store cargo"));
            scheduler.schedule(storeCargo);
            break;
        }
        currentState = desiredState; // TODO: test that this works
    }

    public String getCurrentStateName()
    {
        return currentState.name;
    }

    public void reset()
    {
        scheduler.cancelAll();
        armVoltage.set(0);
        cargoIntake.set(0);
    }

    @Override
    public String getName()
    {
        return "Arm";
    }

    @Override
    public AddList<Watchable> getSubWatchables(AddList<Watchable> stem)
    {
        return stem.put(new StringInfo("arm state", () -> currentState.name), scheduler);
    }

    private Command goToAngle(double angle)
    {
        return controller.getPIDToCommand(angle, TOLERANCE_IN_DEGREES);
    }

    private Command extendCargoPistonsCommand(boolean extend)
    {
        return CommandUtil.createCommand(() -> cargoLauncher.set(extend)).setName("extend cargo piston");
    }

    private Command intakeCargoCommand()
    {
        return new Command()
        {
            @Override
            protected boolean execute()
            {
                cargoIntake.set(-1);
                return cargoIntakeBeamBreak.get();
            }

            @Override
            protected void done()
            {
                cargoIntake.set(0);
            }

        }.setName("intake cargo");
    }

    private Command outtakeCargoCommand()
    {
        return new Command()
        {
            @Override
            protected boolean execute()
            {
                cargoIntake.set(1);
                return !cargoIntakeBeamBreak.get();
            }

            @Override
            protected void done()
            {
                cargoIntake.set(0);
            }
        }.setName("outtake cargo");
    }
}