package com.team1389.systems;

import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.system.Subsystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

/**
 * Allows for manual control of arm, cargo intake, & hatch intake with or
 * without beam break
 */
public class ManualArm extends Subsystem
{

    // output
    private DigitalOut hatchOuttake;
    private DigitalOut cargoLauncher;
    private RangeOut<Percent> cargoIntake;
    private RangeOut<Percent> arm;

    // sensors
    private DigitalIn cargoIntakeBeamBreak;

    // control
    private RangeIn<Percent> armAxis;
    private DigitalIn outtakeHatchBtn;
    private DigitalIn intakeCargoBtn;
    private DigitalIn outtakeCargoBtn;

    private boolean useBeamBreak = true;

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
     *                                 cargo is in the intake (Must be true when
     *                                 it detects)
     * @param armAxis
     *                                 input for controlling arm
     * @param outtakeHatchBtn
     *                                 input for triggering outtaking hatch
     * @param intakeCargoBtn
     *                                 input for triggering cargo intake
     * @param outtakeCargoBtn
     *                                 input for triggering cargo outtake
     * @param useBeamBreak
     *                                 toggle for whether or not to use the beam
     *                                 break
     */
    public ManualArm(DigitalOut hatchOuttake, DigitalOut cargoLauncher, RangeOut<Percent> cargoIntake,
            RangeOut<Percent> arm, DigitalIn cargoIntakeBeamBreak, RangeIn<Percent> armAxis, DigitalIn outtakeHatchBtn,
            DigitalIn intakeCargoBtn, DigitalIn outtakeCargoBtn, boolean useBeamBreak)
    {
        this.hatchOuttake = hatchOuttake;
        this.cargoLauncher = cargoLauncher;
        this.cargoIntake = cargoIntake;
        this.arm = arm;
        this.cargoIntakeBeamBreak = cargoIntakeBeamBreak;
        this.armAxis = armAxis;
        this.outtakeHatchBtn = outtakeHatchBtn;
        this.intakeCargoBtn = intakeCargoBtn;
        this.outtakeCargoBtn = outtakeCargoBtn;
        this.useBeamBreak = useBeamBreak;
    }

    @Override
    public void init()
    {
        outtakeHatchBtn = outtakeHatchBtn.getToggled();
    }

    @Override
    public String getName()
    {
        return "Manual Arm";
    }

    @Override
    public AddList<Watchable> getSubWatchables(AddList<Watchable> arg0)
    {
        return arg0.put(cargoLauncher.getWatchable("launch piston manual"),
                cargoIntakeBeamBreak.getWatchable("cargo intaken"), hatchOuttake.getWatchable("hatch outtake pistons"),
                cargoIntake.getWatchable("cargo intake wheels"));
    }

    @Override
    public void update()
    {
        arm.set(armAxis.get());
        updateHatch();
        if (useBeamBreak)
        {
            updateCargoWithBeamBreak();
        }
        else
        {
            updateCargoWithoutBeamBreak();
        }
    }

    public void reset()
    {
        cargoIntake.set(0);
        arm.set(0);
    }

    /**
     * assumes outtakeHatchBtn is toggled
     */
    private void updateHatch()
    {
        if (outtakeHatchBtn.get())
        {
            hatchOuttake.set(true);
        }
        else
        {
            hatchOuttake.set(false);
        }
    }

    /**
     * Precondition: Beam break must be configged so it's true when it detects
     * something
     */
    private void updateCargoWithBeamBreak()
    {

        // This might have trouble with piston retracting too slow
        if (!cargoIntakeBeamBreak.get() && intakeCargoBtn.get())
        {
            cargoLauncher.set(false);
            cargoIntake.set(1);
        }
        else if (cargoIntakeBeamBreak.get() && outtakeCargoBtn.get())
        {
            // extend piston
            cargoLauncher.set(true);
            cargoIntake.set(-1);
        }
        cargoIntake.set(0);
    }

    private void updateCargoWithoutBeamBreak()
    {
        // This might have trouble with piston retracting too slow
        if (intakeCargoBtn.get())
        {
            cargoLauncher.set(false);
            cargoIntake.set(1);
        }
        else if (outtakeCargoBtn.get())
        {
            // extend piston
            cargoLauncher.set(true);
            cargoIntake.set(-1);
        }
        cargoIntake.set(0);
    }
}