package frc.robot.subsystems.Shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;


public class TalconFXSetup {
    public static void defaultSetup(TalonFX motor, boolean isInverted, double currentLimit){
        motor.configFactoryDefault();
        motor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
        motor.configVoltageCompSaturation(12);  //default 12v voltage compensation for motors
        motor.enableVoltageCompensation(true);  //enable voltage compensation
        simpleCurrentLimit(motor, currentLimit);
        motor.setInverted(isInverted);
        defaultStatusFrames(motor);
    }

    //Talon FX motor
    //Limit in Amps
    public static void simpleCurrentLimit(TalonFX motor, double limit){
        SupplyCurrentLimitConfiguration supplyCurrentLimit = new SupplyCurrentLimitConfiguration(true, limit, limit, 0.5);
        motor.configSupplyCurrentLimit(supplyCurrentLimit);
    }

    public static void defaultStatusFrames(TalonFX motor){
        //Default Status Rates are listed here: https://docs.ctre-phoenix.com/en/stable/ch18_CommonAPI.html
        int fastTime = 200;
        int slowTime = 250;
        motor.setStatusFramePeriod(StatusFrame.Status_1_General, 10);
        motor.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 50);
        motor.setStatusFramePeriod(StatusFrame.Status_4_AinTempVbat, slowTime);
        motor.setStatusFramePeriod(StatusFrame.Status_10_MotionMagic, fastTime);
        motor.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, slowTime);
        motor.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, fastTime);
        motor.setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, slowTime);
        motor.setStatusFramePeriod(StatusFrame.Status_17_Targets1, slowTime);
    }

    public static void velocityStatusFrames(TalonFX motor){
        int fastTime = 160;
        int slowTime = 200;
        motor.setStatusFramePeriod(StatusFrame.Status_1_General, 10);
        motor.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 20);
        motor.setStatusFramePeriod(StatusFrame.Status_4_AinTempVbat, slowTime);
        motor.setStatusFramePeriod(StatusFrame.Status_10_MotionMagic, fastTime);
        motor.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, slowTime);
        motor.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, fastTime);
        motor.setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, slowTime);
        motor.setStatusFramePeriod(StatusFrame.Status_17_Targets1, slowTime);
    }
}
