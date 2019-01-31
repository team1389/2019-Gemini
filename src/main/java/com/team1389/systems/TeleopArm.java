package com.team1389.systems;

import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.system.Subsystem;
import com.team1389.systems.Arm.State;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class TeleopArm extends Subsystem
{
    // output
    private DigitalOut hatchOuttake;
    private DigitalOut cargoLauncher;
    private RangeOut<Percent> cargoIntake;
    private RangeOut<Percent> arm;

    // sensors
    private DigitalIn cargoIntakeBeamBreak;
    private RangeIn<Position> armAngle;

    // control
    private RangeIn<Percent> armAxis;

    private DigitalIn intakeHatchGroundBtn;
    private DigitalIn intakeHatchFeederBtn;
    private DigitalIn intakeCargoBtn;
    private DigitalIn prepForClimbBtn;
    private DigitalIn outtakeCargoBtn;
    private DigitalIn outtakeHatchBtn;
    private DigitalIn storeCargoBtn;

    private DigitalIn toggleManualModeBtn;

    // config
    private boolean useBeamBreakInManual = true;
    private final boolean USE_MANUAL = false;
    private DigitalIn currentlyInManual;
    private Arm armSystem;
    private ManualArm manualArmSystem;

    /**
     * 
     * @param hatchOuttake
     *                                 controller for hatch detach mechanism
     * @param cargoLauncher
     *                                 controller for piston that hits ball into
     *                                 intake
     * @param cargoIntake
     *                                 controller for flywheel intake
     * @param arm
     *                                 controller for arm motion
     * @param cargoIntakeBeamBreak
     *                                 input from beam break that detects if
     *                                 cargo is in the intake
     * @param armAngle
     *                                 gives angle of the arm in degrees
     * @param armAxis
     *                                 input for controlling arm
     * @param outtakeHatchBtn
     *                                 input for triggering outtaking hatch
     * @param intakeHatchGroundBtn
     *                                 input for triggering intaking hatch from
     *                                 the ground
     * @param intakeHatchFeederBtn
     *                                 input for triggering intaking hatch from
     *                                 the feeder
     * @param outtakeCargoBtn
     *                                 input for triggering cargo outtake
     * @param intakeCargoBtn
     *                                 input for triggering cargo intake
     * @param prepForClimbBtn
     *                                 input for moving arm to its pos for
     *                                 climbing
     * @param storeCargoBtn
     *                                 triggers motion of arm to cargo drop off
     *                                 pos, and then outtake into shooter
     * @param toggleManualModeBtn
     *                                 toggle manual mode
     * @param useBeamBreakInManual
     *                                 toggle for whether or not to use the beam
     *                                 break in manual mode
     */
    public TeleopArm(DigitalOut hatchOuttake, DigitalOut cargoLauncher, RangeOut<Percent> cargoIntake,
            RangeOut<Percent> arm, DigitalIn cargoIntakeBeamBreak, RangeIn<Position> armAngle, RangeIn<Percent> armAxis,
            DigitalIn outtakeHatchBtn, DigitalIn intakeHatchGroundBtn, DigitalIn intakeHatchFeederBtn,
            DigitalIn outtakeCargoBtn, DigitalIn intakeCargoBtn, DigitalIn prepForClimbBtn, DigitalIn storeCargoBtn,
            DigitalIn toggleManualModeBtn, boolean useBeamBreakInManual)
    {
        this.hatchOuttake = hatchOuttake;
        this.cargoLauncher = cargoLauncher;
        this.cargoIntake = cargoIntake;
        this.arm = arm;
        this.cargoIntakeBeamBreak = cargoIntakeBeamBreak;
        this.armAxis = armAxis;
        this.outtakeHatchBtn = outtakeHatchBtn;
        this.intakeHatchGroundBtn = intakeHatchGroundBtn;
        this.intakeHatchFeederBtn = intakeHatchFeederBtn;
        this.outtakeCargoBtn = outtakeCargoBtn;
        this.intakeCargoBtn = intakeCargoBtn;
        this.prepForClimbBtn = prepForClimbBtn;
        this.storeCargoBtn = storeCargoBtn;
        this.toggleManualModeBtn = toggleManualModeBtn;
        this.useBeamBreakInManual = useBeamBreakInManual;

    }

    @Override
    public void init()
    {
        armSystem = new Arm(hatchOuttake, cargoLauncher, cargoIntake, arm, cargoIntakeBeamBreak, armAngle);
        manualArmSystem = new ManualArm(hatchOuttake, cargoLauncher, cargoIntake, arm, cargoIntakeBeamBreak, armAxis,
                outtakeHatchBtn, intakeCargoBtn, outtakeCargoBtn, useBeamBreakInManual);

        // stop all output when switching between modes
        currentlyInManual = new DigitalIn(() -> USE_MANUAL || toggleManualModeBtn.get()).addChangeListener((changed) ->
        {
            armSystem.reset();
            manualArmSystem.reset();
        });
    }

    @Override
    public void update()
    {
        if (currentlyInManual.get())
        {
            manualUpdate();
        }
        else
        {
            advancedUpdate();

        }

    }

    private void advancedUpdate()
    {
        if (intakeHatchGroundBtn.get())
        {
            armSystem.enterState(State.INTAKE_HATCH_FROM_GROUND);
        }
        else if (intakeHatchFeederBtn.get())
        {
            armSystem.enterState(State.INTAKE_HATCH_FROM_FEEDER);
        }
        else if (intakeCargoBtn.get())
        {
            armSystem.enterState(State.INTAKE_CARGO_FROM_GROUND);
        }
        else if (outtakeCargoBtn.get())
        {
            armSystem.enterState(State.OUTTAKE_CARGO);
        }
        else if (outtakeHatchBtn.get())
        {
            armSystem.enterState(State.OUTTAKE_HATCH);
        }
        else if (storeCargoBtn.get())
        {
            armSystem.enterState(State.STORE_CARGO);
        }
        else if (prepForClimbBtn.get())
        {
            armSystem.enterState(State.CLIMBING);
        }
        armSystem.update();
    }

    private void manualUpdate()
    {
        manualArmSystem.update();
    }

    @Override
    public String getName()
    {
        return "Teleop Arm";
    }

    @Override
    public AddList<Watchable> getSubWatchables(AddList<Watchable> arg0)
    {
        return arg0.put(manualArmSystem, armSystem);
    }
}