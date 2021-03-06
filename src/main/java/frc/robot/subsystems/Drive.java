
package frc.robot.subsystems;

import static frc.robot.Constants.DriveConstants.kDriveKinematics;
import static frc.robot.Constants.DriveConstants.kGyroReversed;
import static frc.robot.Constants.DriveConstants.kThroughBoreEncoderResolution;
import static frc.robot.Constants.DriveConstants.kWheelRadiusInches;
import static frc.robot.Constants.DriveConstants.knormalModeSpeed;
import static frc.robot.Constants.DriveConstants.kslowModeSpeed;
import static frc.robot.Constants.DriveConstants.kturboModeSpeed;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
// import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

public class Drive extends SubsystemBase {
    // Motors
        private CANSparkMax 
            leftFrontMotor = new CANSparkMax (3, CANSparkMaxLowLevel.MotorType.kBrushless),
            leftRearMotor = new CANSparkMax (2, CANSparkMaxLowLevel.MotorType.kBrushless),
            rightFrontMotor = new CANSparkMax (5, CANSparkMaxLowLevel.MotorType.kBrushless),
            rightRearMotor = new CANSparkMax (4, CANSparkMaxLowLevel.MotorType.kBrushless);
        

    // Encoders
        private final Encoder leftEncoder = new Encoder (        //Left Encoder
            9,
            8,
            false,
            CounterBase.EncodingType.k4X
        );

        private final Encoder rightEncoder = new Encoder(        //Right Encoder
            7,
            6,
            false,
            CounterBase.EncodingType.k4X
    );

    // Gyro
        private AHRS navx = new AHRS(SPI.Port.kMXP);


        private DifferentialDrive drive;

        private DifferentialDriveOdometry externalOdometry;
        private DifferentialDriveOdometry internalOdometry;

        private boolean isControlsFlipped = false;

        private NetworkTable live_dashboard = NetworkTableInstance.getDefault().getTable("Live_Dashboard");

        private MotorControllerGroup leftMotors = new MotorControllerGroup(leftFrontMotor, leftRearMotor);

        private MotorControllerGroup rightMotors = new MotorControllerGroup(rightFrontMotor, rightRearMotor);

        AHRS ahrs;
        Joystick stick;
        PIDController turnController;
        double rotateToAngleRate;


    public Drive() {
        leftFrontMotor.setSmartCurrentLimit(60);
        leftRearMotor.setSmartCurrentLimit(60);
        rightFrontMotor.setSmartCurrentLimit(60);
        rightRearMotor.setSmartCurrentLimit(60);

        rightFrontMotor.setInverted(true);
        rightRearMotor.setInverted(true);
        leftFrontMotor.setInverted(false);
        leftRearMotor.setInverted(false);

        drive = new DifferentialDrive(leftMotors, rightMotors);
        drive.setSafetyEnabled(false);

        leftFrontMotor.setIdleMode(IdleMode.kCoast);
        rightFrontMotor.setIdleMode(IdleMode.kCoast);
        leftRearMotor.setIdleMode(IdleMode.kCoast);
        rightRearMotor.setIdleMode(IdleMode.kCoast);

        // Encoders
            leftEncoder.reset();
            rightEncoder.reset();

            leftEncoder.setDistancePerPulse(2 * Math.PI * kWheelRadiusInches / kThroughBoreEncoderResolution);
            rightEncoder.setDistancePerPulse(2 * Math.PI * kWheelRadiusInches / kThroughBoreEncoderResolution);


            externalOdometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()));
            internalOdometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()));

        // PID Controller
            leftFrontMotor.getPIDController();
            rightFrontMotor.getPIDController();
            leftRearMotor.getPIDController();
            rightRearMotor.getPIDController();

            resetAll();
    }
    public void tankDrive(double leftPower, double rightPower) {
        leftFrontMotor.set(leftPower);
        rightFrontMotor.set(rightPower);
    }


    public void SimpleDriveForward(double power) {
        tankDrive(DriveConstants.kslowModeSpeed, DriveConstants.kslowModeSpeed);
    }
    
    /**
     * Returns the currently-estimated pose of the robot.
     *
     * @return The pose.
     */
    public Pose2d getPose() {
        return externalOdometry.getPoseMeters();
    }

    /**
     * Returns the current wheel speeds of the robot.
     *
     * @return The current wheel speeds.
     */
    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(leftEncoder.getRate(), rightEncoder.getRate());
    }

    /**
     * Resets the odometry to the specified pose.
     *
     * @param pose The pose to which to set the odometry.
     */
    public void resetOdometry(Pose2d pose) {
        resetEncoders();
        navx.zeroYaw();
        internalOdometry.resetPosition(pose, Rotation2d.fromDegrees(getHeading()));
        externalOdometry.resetPosition(pose, Rotation2d.fromDegrees(getHeading()));
    }

    /**
     * Drives the robot using arcade controls.
     *
     * @param fwd the commanded forward movement
     * @param rot the commanded rotation
     */
    public void arcadeDrive(double fwd, double rot) {
        drive.arcadeDrive(fwd, rot);
    }

    public void slowDrive() {
        drive.setMaxOutput(kslowModeSpeed);
    }

    public void normalDrive() {
            drive.setMaxOutput(knormalModeSpeed);
    }

    public void turboDrive() {
        drive.setMaxOutput(kturboModeSpeed);
    }

    public void setRightSideInverted() {
        rightFrontMotor.setInverted(true);
        rightRearMotor.setInverted(true);
    }

    public void setRightSideForward() {
        rightFrontMotor.setInverted(false);
        rightRearMotor.setInverted(false);
    }

    public void curvatureDrive(double fwd, double rot) {
        drive.curvatureDrive(fwd, rot, true);
    }

    /**
     * Controls the left and right sides of the drive directly with voltages.
     *
     * @param leftVolts  the commanded left output
     * @param rightVolts the commanded right output
     */
    public void tankDriveVolts(double leftVolts, double rightVolts) {
        leftFrontMotor.setVoltage(leftVolts);
        rightFrontMotor.setVoltage(rightVolts);
    }

    /**
     * Resets the drive encoders to currently read a position of 0.
     */
    public void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
        // leftNeoEncoder.setPosition(0);
        // rightNeoEncoder.setPosition(0);
    }

    /**
     * Gets the average distance of the two encoders.
     *
     * @return the average of the two encoder readings
     */
    public double getAverageEncoderDistance() {
        return (leftEncoder.getDistance() + rightEncoder.getDistance()) / 2.0;
    }

    /**
     * Gets the left drive encoder.
     *
     * @return the left drive encoder
     */
    public Encoder getLeftEncoder() {
        return leftEncoder;
    }

    /**
     * Gets the right drive encoder.
     *
     * @return the right drive encoder
     */
    public Encoder getRightEncoder() {
        return rightEncoder;
    }

    /**
     * Sets the max output of the drive.  Useful for scaling the drive to drive more slowly.
     *
     * @param maxOutput the maximum output to which the drive will be constrained
     */
    public void setMaxOutput(double maxOutput) {
        drive.setMaxOutput(maxOutput);
    }


    /**
     * Zeroes the heading of the robot.
     */
    public void zeroHeading() {
        navx.zeroYaw();
    }

    /**
     * Returns the heading of the robot.
     *
     * @return the robot's heading in degrees, from 180 to 180
     */
    public double getHeading() {
        return navx.getRotation2d().getDegrees();
    }

    /**
     * @return True if external encoders and internal encoders conflict
     */
   public boolean isEncoderError() {
        return internalOdometry.getPoseMeters().getTranslation().getDistance(externalOdometry.getPoseMeters().getTranslation()) > 0.5;
    }
    

    /**
     * Returns the turn rate of the robot.
     *
     * @return The turn rate of the robot, in degrees per second
     */
    public double getTurnRate() {
        return navx.getRate() * (kGyroReversed ? -1.0 : 1.0);
    }

    public DifferentialDriveKinematics getKinematics() {
        return kDriveKinematics;
    }
    

    public void resetAll() {
        resetOdometry(new Pose2d());
        navx.reset();
    }

    public void setControlsFlipped(boolean controlsFlipped) {
        isControlsFlipped = controlsFlipped;
    }

    public boolean isControlsFlipped() {
        return isControlsFlipped;
    }


    @Override
    public void periodic() {
        double leftDist = leftEncoder.getDistance();
        double rightDist = rightEncoder.getDistance();

        externalOdometry.update(Rotation2d.fromDegrees(getHeading()),
                leftDist,
                rightDist);

        internalOdometry.update(Rotation2d.fromDegrees(getHeading()),
            -leftEncoder.getDistance(),
            rightEncoder.getDistance());

        live_dashboard.getEntry("robotHeading").setDouble(getPose().getRotation().getRadians());
        SmartDashboard.putNumber("robotHeading", getPose().getRotation().getDegrees());
        SmartDashboard.putNumber("robot pitch", navx.getRoll());

        SmartDashboard.putNumber("Left Encoder = ", leftEncoder.getDistance());
        SmartDashboard.putNumber("Right Encoder = ", rightEncoder.getDistance());
    }

    public void antiTipArcadeDrive(double xAxisRate, double zAxisRate) {
        drive.setSafetyEnabled(true);

        // double xAxisRate            = stick.getX();
        // double yAxisRate            = stick.getY();
        double rollAngleDegrees    = navx.getRoll();
        // double rollAngleDegrees     = navx.getRoll();
        final double kOffBalanceAngleThresholdDegrees = 5;
        final double kOonBalanceAngleThresholdDegrees  = 2;
        boolean fixBalance = false;
        
        if ( !fixBalance && 
                (Math.abs(rollAngleDegrees) >= 
                Math.abs(kOffBalanceAngleThresholdDegrees))) {
            fixBalance = true;
        }
        else if ( fixBalance && 
                    (Math.abs(rollAngleDegrees) <= 
                    Math.abs(kOonBalanceAngleThresholdDegrees))) {
            fixBalance = false;
        }
        
        // Control drive system automatically, 
        // driving in reverse direction of pitch/roll angle,
        // with a magnitude based upon the angle
        
        if (fixBalance) {
            double pitchAngleRadians = rollAngleDegrees * (Math.PI / 180.0);
            xAxisRate = Math.sin(pitchAngleRadians) * -1;
        }
        drive.arcadeDrive(xAxisRate, zAxisRate);
    }

    public void encoderDrive(double leftInches, double rightInches, double leftSpeed, double rightSpeed) {
        double leftTarget = leftEncoder.getDistance() + leftInches;
        double rightTarget = rightEncoder.getDistance() + rightInches;

        while (leftEncoder.getDistance() < leftTarget || rightEncoder.getDistance() < rightTarget) {
            if (leftEncoder.getDistance() < leftTarget) {
                leftFrontMotor.set(leftSpeed);
            }
            if (rightEncoder.getDistance() < rightTarget) {
                rightFrontMotor.set(rightSpeed);
            } 
        }
        tankDrive(0, 0);
    }

    public void encoderDrive(double leftInches, double rightInches) {
        encoderDrive(leftInches, rightInches, 0.25, 0.25);
    }


    // /**
    //  * Runs the motors with onnidirectional drive steering.
    //  * 
    //  * Implements Field-centric drive control.
    //  * 
    //  * Also implements "rotate to angle", where the angle
    //  * being rotated to is defined by one of four buttons.
    //  * 
    //  * Note that this "rotate to angle" approach can also 
    //  * be used while driving to implement "straight-line
    //  * driving".
    //  */
    // public void navxTurn() {
    //     drive.setSafetyEnabled(true);
    //     while (navxTurn() && isEnabled()) {
    //         boolean rotateToAngle = false;
    //         if ( stick.getRawButton(1)) {
    //             ahrs.reset();
    //         }
    //         if ( stick.getRawButton(2)) {
    //             turnController.setSetpoint(0.0f);
    //             rotateToAngle = true;
    //         } else if ( stick.getRawButton(3)) {
    //             turnController.setSetpoint(90.0f);
    //             rotateToAngle = true;
    //         } else if ( stick.getRawButton(4)) {
    //             turnController.setSetpoint(179.9f);
    //             rotateToAngle = true;
    //         } else if ( stick.getRawButton(5)) {
    //             turnController.setSetpoint(-90.0f);
    //             rotateToAngle = true;
    //         }
    //         double currentRotationRate;
    //         if ( rotateToAngle ) {
    //             turnController.enable();
    //             currentRotationRate = rotateToAngleRate;
    //         } else {
    //             turnController.disable();
    //             currentRotationRate = stick.getTwist();
    //         }
    //         try {
    //             /* Use the joystick X axis for lateral movement,          */
    //             /* Y axis for forward movement, and the current           */
    //             /* calculate d rotation rate (or joystick Z axis),         */
    //             /* depending upon whether "rotate to angle" is active.    */
    //             drive.driveCartesian(stick.getX(), stick.getY(), 
    //                                    currentRotationRate, ahrs.getAngle());
    //         } catch( RuntimeException ex ) {
    //             DriverStation.reportError("Error communicating with drive system:  " + ex.getMessage(), true);
    //         }
    //         Timer.delay(0.005);		// wait for a motor update time
    //     }
    // }

    // /**
    //  * Runs during test mode
    //  */
    // public void test() {
    // }

    // @Override
    // /* This function is invoked periodically by the PID Controller, */
    // /* based upon navX MXP yaw angle input and PID Coefficients.    */
    // public void pidWrite(double output) {
    //     rotateToAngleRate = output;
    // }
}
