/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;

public class WinchSubsystem extends SubsystemBase {

  WPI_TalonSRX winchMotor = RobotMap.winchMotor;

  public WinchSubsystem() {
    winchMotor.setNeutralMode(NeutralMode.Brake);
  }

  public void setWinchPower(double power) {
    winchMotor.set(power);
  }

  public void stop() {
    winchMotor.set(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
