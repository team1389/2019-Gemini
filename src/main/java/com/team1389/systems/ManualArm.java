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
 * Allows for manual control of arm without beam break
 */
public class ManualArm extends Subsystem
{

    // output
    private DigitalOut cargoLauncher;
    private RangeOut<Percent> cargoIntake;
    private RangeOut<Percent> arm;

    // sensors
    private DigitalIn cargoIntakeBeamBreak;

    // control
    private RangeIn<Percent> armAxis;
    private DigitalIn intakeCargoBtn;
    private DigitalIn cargoToRocketBtn;
    private DigitalIn cargoToShooterBtn;

    private boolean useBeamBreak;
    private boolean intakingCargo;
    private boolean cargoToRocket;
    private boolean cargoToShooter;

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
     * @param armAxis
     *                                 input for controlling arm
     * @param intakeCargoBtn
     *                                 input for triggering cargo intake
     * @param outtakeCargoBtn
     *                                 input for triggering cargo outtake
     * @param useBeamBreak
     *                                 toggle for whether or not to use the beam
     *                                 break
     */
    public ManualArm(DigitalOut cargoLauncher, RangeOut<Percent> cargoIntake, RangeOut<Percent> arm,
            DigitalIn cargoIntakeBeamBreak, RangeIn<Percent> armAxis, DigitalIn intakeCargoBtn,
            DigitalIn cargoToRocketBtn, DigitalIn cargoToShooterBtn, boolean useBeamBreak)
    {
        this.cargoLauncher = cargoLauncher;
        this.cargoIntake = cargoIntake;
        this.arm = arm;
        this.cargoIntakeBeamBreak = cargoIntakeBeamBreak;
        this.armAxis = armAxis;
        this.intakeCargoBtn = intakeCargoBtn;
        this.cargoToRocketBtn = cargoToRocketBtn;
        this.cargoToShooterBtn = cargoToShooterBtn;
        this.useBeamBreak = useBeamBreak;
    }

    @Override
    public void init()
    {
        intakingCargo = false;
        cargoToRocket = false;
        cargoToShooter = false;
    }

    @Override
    public String getName()
    {
        return "Manual Arm";
    }

    @Override
    public AddList<Watchable> getSubWatchables(AddList<Watchable> stem)
    {
        return stem.put(cargoLauncher.getWatchable("launch piston manual"),
                cargoIntakeBeamBreak.getWatchable("cargo intaken"), cargoIntake.getWatchable("cargo intake wheels"));
    }

    @Override
    public void update()
    {
        System.out.println("beambreak " + cargoIntakeBeamBreak.get());
        arm.set(armAxis.get());
        intakingCargo = intakeCargoBtn.get() ^ intakingCargo;
        cargoToRocket = cargoToRocketBtn.get() ^ cargoToRocket;
        cargoToShooter = cargoToShooterBtn.get() ^ cargoToShooter;
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
     * Precondition: Beam break must be configged so it's true when it detects
     * something
     */
    private void updateCargoWithBeamBreak()
    {
        System.out.println("Beambreak status" + cargoIntakeBeamBreak.get());

        if (!cargoIntakeBeamBreak.get() && intakingCargo)
        {
            cargoLauncher.set(true);
            cargoIntake.set(.5);
        }
        else if (cargoIntakeBeamBreak.get() && cargoToRocket)
        {
            cargoLauncher.set(false);
            cargoIntake.set(-1);
        }
        else if (cargoIntakeBeamBreak.get() && cargoToShooter)
        {
            cargoLauncher.set(false);
            cargoIntake.set(.2);
        }
        else
        {
            cargoIntake.set(0);
        }
    }

    private void updateCargoWithoutBeamBreak()
    {

        if (intakingCargo)
        {
            cargoLauncher.set(true);
            cargoIntake.set(1);
        }
        else if (cargoToRocket)
        {
            cargoIntake.set(-.5);
        }
        else if (cargoToShooter)
        {
            cargoLauncher.set(false);
        }
        else
        {
            cargoIntake.set(0);
        }
    }
}