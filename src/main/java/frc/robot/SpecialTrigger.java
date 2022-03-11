// package frc.robot;

// import java.util.function.BooleanSupplier;

// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.CommandScheduler;
// import edu.wpi.first.wpilibj2.command.InstantCommand;
// import edu.wpi.first.wpilibj2.command.Subsystem;
// import edu.wpi.first.wpilibj2.command.button.Trigger;
// import frc.robot.Constants.ControllerConstants;
// import frc.robot.Controls.XboxButton;
// import frc.robot.Controls.XboxDPAD;
// import frc.robot.Controls.XboxTrigger;
// import static edu.wpi.first.wpilibj.util.ErrorMessages.requireNonNullParam;

// import edu.wpi.first.math.filter.Debouncer;
// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.CommandScheduler;
// import edu.wpi.first.wpilibj2.command.InstantCommand;
// import edu.wpi.first.wpilibj2.command.Subsystem;
// import java.util.function.BooleanSupplier;

// import static edu.wpi.first.wpilibj.util.ErrorMessages.requireNonNullParam;

// public class SpecialTrigger implements BooleanSupplier {
//   private final BooleanSupplier m_isActive;
//   private final Controller m_controller;
//   private final int m_port;
//   // private final int m_numberOfControllers;

//   public SpecialTrigger(Controller controller, int port, BooleanSupplier isActive) {
//     m_isActive = isActive;
//     m_controller = controller;
//     m_port = port;
//   }

//   public SpecialTrigger add(String name, XboxButton xboxButton) {
//     return new SpecialTrigger(m_controller, m_port, () -> m_controller.get(m_port).getRawButton(Controls.buttonToButtonNumber(xboxButton)));
//   }

//   public SpecialTrigger add(String name, XboxDPAD xboxDPAD) {
//     return new SpecialTrigger(m_controller, m_port, () -> m_controller.get(m_port).getPOV() == Controls.dpadToHatDegrees(xboxDPAD));
//   }

//   public SpecialTrigger add(String name, XboxTrigger xboxTrigger) {
//     return new SpecialTrigger(m_controller, m_port, () -> m_controller.get(m_port).getRawAxis(Controls.triggerToTriggerNumber(xboxTrigger)) < ControllerConstants.kTriggerThreshold);
//   }

//   public Controller finish() {
//     return m_controller;
//   }




























//   /**
//    * Returns whether or not the trigger is active.
//    *
//    * <p>This method will be called repeatedly a command is linked to the Trigger.
//    *
//    * <p>Functionally identical to {@link Trigger#get()}.
//    *
//    * @return whether or not the trigger condition is active.
//    */
//   @Override
//   public boolean getAsBoolean() {
//     return m_isActive.getAsBoolean();
//   }

//   /**
//    * Starts the given command whenever the trigger just becomes active.
//    *
//    * @param command the command to start
//    * @param interruptible whether the command is interruptible
//    * @return this trigger, so calls can be chained
//    */
//   public Trigger whenActive(final Command command, boolean interruptible) {
//     requireNonNullParam(command, "command", "whenActive");

//     CommandScheduler.getInstance()
//         .addButton(
//             new Runnable() {
//               private boolean m_pressedLast = get();

//               @Override
//               public void run() {
//                 boolean pressed = get();

//                 if (!m_pressedLast && pressed) {
//                   command.schedule(interruptible);
//                 }

//                 m_pressedLast = pressed;
//               }
//             });

//     return this;
//   }

//   /**
//    * Starts the given command whenever the trigger just becomes active. The command is set to be
//    * interruptible.
//    *
//    * @param command the command to start
//    * @return this trigger, so calls can be chained
//    */
//   public Trigger whenActive(final Command command) {
//     return whenActive(command, true);
//   }

//   /**
//    * Runs the given runnable whenever the trigger just becomes active.
//    *
//    * @param toRun the runnable to run
//    * @param requirements the required subsystems
//    * @return this trigger, so calls can be chained
//    */
//   public Trigger whenActive(final Runnable toRun, Subsystem requirements) {
//     return whenActive(new InstantCommand(toRun, requirements));
//   }

//   /**
//    * Constantly starts the given command while the button is held.
//    *
//    * <p>{@link Command#schedule(boolean)} will be called repeatedly while the trigger is active, and
//    * will be canceled when the trigger becomes inactive.
//    *
//    * @param command the command to start
//    * @param interruptible whether the command is interruptible
//    * @return this trigger, so calls can be chained
//    */
//   public Trigger whileActiveContinuous(final Command command, boolean interruptible) {
//     requireNonNullParam(command, "command", "whileActiveContinuous");

//     CommandScheduler.getInstance()
//         .addButton(
//             new Runnable() {
//               private boolean m_pressedLast = get();

//               @Override
//               public void run() {
//                 boolean pressed = get();

//                 if (pressed) {
//                   command.schedule(interruptible);
//                 } else if (m_pressedLast) {
//                   command.cancel();
//                 }

//                 m_pressedLast = pressed;
//               }
//             });
//     return this;
//   }

//   /**
//    * Constantly starts the given command while the button is held.
//    *
//    * <p>{@link Command#schedule(boolean)} will be called repeatedly while the trigger is active, and
//    * will be canceled when the trigger becomes inactive. The command is set to be interruptible.
//    *
//    * @param command the command to start
//    * @return this trigger, so calls can be chained
//    */
//   public Trigger whileActiveContinuous(final Command command) {
//     return whileActiveContinuous(command, true);
//   }

//   /**
//    * Constantly runs the given runnable while the button is held.
//    *
//    * @param toRun the runnable to run
//    * @param requirements the required subsystems
//    * @return this trigger, so calls can be chained
//    */
//   public Trigger whileActiveContinuous(final Runnable toRun, Subsystem... requirements) {
//     return whileActiveContinuous(new InstantCommand(toRun, requirements));
//   }

//   /**
//    * Starts the given command when the trigger initially becomes active, and ends it when it becomes
//    * inactive, but does not re-start it in-between.
//    *
//    * @param command the command to start
//    * @param interruptible whether the command is interruptible
//    * @return this trigger, so calls can be chained
//    */
//   public Trigger whileActiveOnce(final Command command, boolean interruptible) {
//     requireNonNullParam(command, "command", "whileActiveOnce");

//     CommandScheduler.getInstance()
//         .addButton(
//             new Runnable() {
//               private boolean m_pressedLast = get();

//               @Override
//               public void run() {
//                 boolean pressed = get();

//                 if (!m_pressedLast && pressed) {
//                   command.schedule(interruptible);
//                 } else if (m_pressedLast && !pressed) {
//                   command.cancel();
//                 }

//                 m_pressedLast = pressed;
//               }
//             });
//     return this;
//   }

//   /**
//    * Starts the given command when the trigger initially becomes active, and ends it when it becomes
//    * inactive, but does not re-start it in-between. The command is set to be interruptible.
//    *
//    * @param command the command to start
//    * @return this trigger, so calls can be chained
//    */
//   public Trigger whileActiveOnce(final Command command) {
//     return whileActiveOnce(command, true);
//   }

//   /**
//    * Starts the command when the trigger becomes inactive.
//    *
//    * @param command the command to start
//    * @param interruptible whether the command is interruptible
//    * @return this trigger, so calls can be chained
//    */
//   public Trigger whenInactive(final Command command, boolean interruptible) {
//     requireNonNullParam(command, "command", "whenInactive");

//     CommandScheduler.getInstance()
//         .addButton(
//             new Runnable() {
//               private boolean m_pressedLast = get();

//               @Override
//               public void run() {
//                 boolean pressed = get();

//                 if (m_pressedLast && !pressed) {
//                   command.schedule(interruptible);
//                 }

//                 m_pressedLast = pressed;
//               }
//             });
//     return this;
//   }

//   /**
//    * Starts the command when the trigger becomes inactive. The command is set to be interruptible.
//    *
//    * @param command the command to start
//    * @return this trigger, so calls can be chained
//    */
//   public Trigger whenInactive(final Command command) {
//     return whenInactive(command, true);
//   }

//   /**
//    * Runs the given runnable when the trigger becomes inactive.
//    *
//    * @param toRun the runnable to run
//    * @param requirements the required subsystems
//    * @return this trigger, so calls can be chained
//    */
//   public Trigger whenInactive(final Runnable toRun, Subsystem... requirements) {
//     return whenInactive(new InstantCommand(toRun, requirements));
//   }

//   /**
//    * Toggles a command when the trigger becomes active.
//    *
//    * @param command the command to toggle
//    * @param interruptible whether the command is interruptible
//    * @return this trigger, so calls can be chained
//    */
//   public Trigger toggleWhenActive(final Command command, boolean interruptible) {
//     requireNonNullParam(command, "command", "toggleWhenActive");

//     CommandScheduler.getInstance()
//         .addButton(
//             new Runnable() {
//               private boolean m_pressedLast = get();

//               @Override
//               public void run() {
//                 boolean pressed = get();

//                 if (!m_pressedLast && pressed) {
//                   if (command.isScheduled()) {
//                     command.cancel();
//                   } else {
//                     command.schedule(interruptible);
//                   }
//                 }

//                 m_pressedLast = pressed;
//               }
//             });
//     return this;
//   }

//   /**
//    * Toggles a command when the trigger becomes active. The command is set to be interruptible.
//    *
//    * @param command the command to toggle
//    * @return this trigger, so calls can be chained
//    */
//   public Trigger toggleWhenActive(final Command command) {
//     return toggleWhenActive(command, true);
//   }

//   /**
//    * Cancels a command when the trigger becomes active.
//    *
//    * @param command the command to cancel
//    * @return this trigger, so calls can be chained
//    */
//   public Trigger cancelWhenActive(final Command command) {
//     requireNonNullParam(command, "command", "cancelWhenActive");

//     CommandScheduler.getInstance()
//         .addButton(
//             new Runnable() {
//               private boolean m_pressedLast = get();

//               @Override
//               public void run() {
//                 boolean pressed = get();

//                 if (!m_pressedLast && pressed) {
//                   command.cancel();
//                 }

//                 m_pressedLast = pressed;
//               }
//             });
//     return this;
//   }

//   /**
//    * Composes this trigger with another trigger, returning a new trigger that is active when both
//    * triggers are active.
//    *
//    * @param trigger the trigger to compose with
//    * @return the trigger that is active when both triggers are active
//    */
//   public Trigger and(Trigger trigger) {
//     return new Trigger(() -> get() && trigger.get());
//   }

//   /**
//    * Composes this trigger with another trigger, returning a new trigger that is active when either
//    * trigger is active.
//    *
//    * @param trigger the trigger to compose with
//    * @return the trigger that is active when either trigger is active
//    */
//   public Trigger or(Trigger trigger) {
//     return new Trigger(() -> get() || trigger.get());
//   }

//   /**
//    * Creates a new trigger that is active when this trigger is inactive, i.e. that acts as the
//    * negation of this trigger.
//    *
//    * @return the negated trigger
//    */
//   public Trigger negate() {
//     return new Trigger(() -> !get());
//   }

//   /**
//    * Creates a new debounced trigger from this trigger - it will become active when this trigger has
//    * been active for longer than the specified period.
//    *
//    * @param seconds The debounce period.
//    * @return The debounced trigger (rising edges debounced only)
//    */
//   public Trigger debounce(double seconds) {
//     return debounce(seconds, Debouncer.DebounceType.kRising);
//   }

//   /**
//    * Creates a new debounced trigger from this trigger - it will become active when this trigger has
//    * been active for longer than the specified period.
//    *
//    * @param seconds The debounce period.
//    * @param type The debounce type.
//    * @return The debounced trigger.
//    */
//   public Trigger debounce(double seconds, Debouncer.DebounceType type) {
//     return new Trigger(
//         new BooleanSupplier() {
//           Debouncer m_debouncer = new Debouncer(seconds, type);

//           @Override
//           public boolean getAsBoolean() {
//             return m_debouncer.calculate(get());
//           }
//         });
//   }
// }
