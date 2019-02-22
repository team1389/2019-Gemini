package com.team1389.robot;

import com.team1389.configuration.PIDConstants;

public class RobotConstants
{
    // TODO: Tune PID Constants
    public static final double TURN_SENSITIVITY = 1;
    public static final double SPIN_SENSITIVITY = .5;
    public static final PIDConstants PID = new PIDConstants(0, 0, 0, 0);
}