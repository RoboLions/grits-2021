package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import frc.robot.Constants.ArmConstants;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;
import frc.robot.lib.RoboLionsPID;

public class ArmSubsystem extends SubsystemBase {

    private final WPI_TalonSRX armMotor = RobotMap.armMotor;
    public RoboLionsPID armPID = new RoboLionsPID();
    private final PigeonIMU imu = RobotMap.arm_imu;
    public static double MAX_ARM_POWER = 0.5; //0.3; // hard deadband as to what the maximum possible command is

    public double arm_pitch_readout = 0;

    public ArmSubsystem() {
        armMotor.setNeutralMode(NeutralMode.Brake);
        armPID.initialize2(ArmConstants.kP, // Proportional Gain
                            ArmConstants.kI, // Integral Gain
                            ArmConstants.kD, // Derivative Gain
                            5, // Cage Limit
                            1, // Deadband
                            MAX_ARM_POWER, // MaxOutput
                            false,
                            true
        );
    }

    public void moveArmToPosition(double target_pitch) {
        arm_pitch_readout = -getPitch(); // inverted because of how the Everybot is built
        double arm_cmd = armPID.execute((double)target_pitch, (double)arm_pitch_readout);
        // add hard deadband to arm so we don't break it
        if(arm_cmd > MAX_ARM_POWER) {
            arm_cmd = MAX_ARM_POWER;
        } else if(arm_cmd < -MAX_ARM_POWER) {
            arm_cmd = -MAX_ARM_POWER;
        }
        armMotor.set(arm_cmd); // need to invert command to close the loop
    }

    public void setArmToGround() {
        moveArmToPosition(ArmConstants.GROUND_POSITION);
    }

    public void setArmToScore() {
        moveArmToPosition(ArmConstants.SCORE_POSITION);
    }

    public void setArmPower(double power) {
        // add hard deadband to arm so we don't break it
        if(power > MAX_ARM_POWER) {
            power = MAX_ARM_POWER;
        } else if(power < -MAX_ARM_POWER) {
            power = -MAX_ARM_POWER;
        }
        if((power > 0 && power < 0.25) || (power < 0 && power > -0.25)) {
            power = 0;
        }
        armMotor.set(-power);
    }

    public void stop() {
        armMotor.set(0);
    }

    public double getPitch() {
    	double[] ypr = new double[3];
    	imu.getYawPitchRoll(ypr);
    	return ypr[1];
    }

    public void resetPitch() {
        // TODO Figure out how to set the pitch
        imu.setYaw(0, ArmConstants.TIMEOUT_MS);
    }
}
