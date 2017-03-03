package org.usfirst.frc.team5518.robot.commands;

import org.usfirst.frc.team5518.robot.Robot;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveForwardAuto extends Command {

	public static Ultrasonic ultra;
	public double range; public double total; public double avg;
	public double min; public double max; public double prev;
	public int count;
	
    public DriveForwardAuto() {
    	//requires(Robot.driveTrain);
    	ultra = new Ultrasonic(3, 2);
		ultra.setAutomaticMode(true);
		count = 0; total = 0; avg = 0;
		min = 1000; max = 0; prev = 0;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//The robot must move 4 ft 9.25 in forward to reach the baseline.
    	//The max velocity is about 10.39 ft/s
    	//We will run the robot straight forward for
    	//		3 seconds
    	//at a speed of
    	//		1.59 ft/s
    	
    	range = ultra.getRangeInches();
    	
    	if (range > max) {
    		max = range;
    	}
    	else if (range < min) {
    		min = range;
    	}
    	
		total += range;
		count++;
		
		if (count == 10) {
			total -= (min + max);
			avg = total / (count - 2);
			prev = avg;
			count = 0;
	    	total = 0;
	    	min = 1000;
	    	max = 0;
		}
    	
    	SmartDashboard.putNumber("Raw Range: ", (range / 12));
    	SmartDashboard.putNumber("Average Range: ", (avg / 12));
    	
    	if (avg > 48) {
    		System.out.println("DRIVE FAST count="+count+"  avg="+avg);
    		Robot.driveTrain.drive(0.5, 0, true, false);
    	}
    	else if (avg <= 48 && avg > 2.5) {
    		System.out.println("DRIVE SLOW count="+count+"  avg="+avg);
    		Robot.driveTrain.drive(0.125, 0, true, false);
    	}
    	else if (avg <= 2.5) {
    		System.out.println("DRIVE STOP count="+count+"  avg="+avg);
    		Robot.driveTrain.drive(0, 0, true, false);
    	}
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
