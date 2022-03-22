package frc.robot.commands.Autos;

import edu.wpi.first.wpilibj2.command.InstantCommand;
// import edu.wpi.first.math.geometry.Pose2d;
// import edu.wpi.first.math.geometry.Rotation2d;
// import edu.wpi.first.math.util.Units;
// import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.Drive.DriveByTime;
import frc.robot.commands.Drive.TurnByTime;
import frc.robot.commands.Shooter.ShootForSeconds;
import frc.robot.commands.Shooter.ShootingSequence;
import frc.robot.subsystems.*;
// import frc.robot.utils.*;

// import java.util.List;

public class IntakeAndShoot extends SequentialCommandGroup {

    public IntakeAndShoot(Drive drive, Shooter shooter, Indexer indexer, Intake intake){
        addCommands(
            new InstantCommand(intake::lift),
            new InstantCommand(intake::intake),
            new DriveByTime(3, 0.4, drive), // moves drivtrain back for 4 seoconds
            new IntakeCommand(intake, indexer),
            new InstantCommand(intake::stopIntake),
            new DriveByTime(5, -0.5, drive),
            new ShootForSeconds(shooter, 4), // shoots for 4 seconds
            new InstantCommand(indexer::outtakeBothIndexer) // starts indexer
        );
    }
}