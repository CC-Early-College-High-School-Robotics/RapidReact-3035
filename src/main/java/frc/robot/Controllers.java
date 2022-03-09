package frc.robot;

import java.util.ArrayList;
import java.util.HashMap;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.ControllerConstants;
import frc.robot.Controls.XboxButton;
import frc.robot.Controls.XboxDPAD;
import frc.robot.Controls.XboxTrigger;

public class Controllers {
    private XboxController[] xboxControllers;

    private ArrayList<Trigger> joystickButtons = new ArrayList<Trigger>();
    private HashMap<String, Integer> actionToButtonIndex;

    public Controllers (ControllerBuilder builder) {
        this.xboxControllers = builder.xboxControllers;
        this.joystickButtons = builder.actions;
        this.actionToButtonIndex = builder.actionToButtonIndex;
    }

    public Trigger get(String action) {
        return joystickButtons.get(actionToButtonIndex.get(action));
    }

    public XboxController getDriver() {
        return xboxControllers[0];
    }

    public XboxController getOperator() {
        return xboxControllers[1];
    }

    public void resetControls() {

    }

    public static class ControllerBuilder {
        private XboxController[] xboxControllers;

        private ArrayList<Trigger> actions = new ArrayList<Trigger>();
        private HashMap<String, Integer> actionToButtonIndex;
        int selected = 0;

        public ControllerBuilder(int driverPort, int operatorPort) {
                xboxControllers[0] = new XboxController(driverPort);
                xboxControllers[1] = new XboxController(operatorPort);
        }





        public ControllerBuilder addButton(String action, int buttonNumber) {
            actionToButtonIndex.put(action, actions.size());
            actions.add(new JoystickButton(xboxControllers[selected], buttonNumber));
            return this;
        }

        public ControllerBuilder addTrigger(String action, int axis) {
            actionToButtonIndex.put(action, actions.size());
            actions.add(new Trigger(() -> xboxControllers[selected].getRawAxis(axis) < ControllerConstants.kTriggerThreshold));
            return this;
        }

        public ControllerBuilder addDPAD(String action, int degrees) {
            actionToButtonIndex.put(action, actions.size());
            actions.add(new Trigger(() -> xboxControllers[selected].getPOV() == degrees));
            return this;
        }

        public ControllerBuilder add(String action, XboxButton xboxButton) {
            return addButton(action, Controls.buttonToButtonNumber(xboxButton));
        }

        public ControllerBuilder add(String action, XboxTrigger xboxTrigger) {
            return addTrigger(action, Controls.triggerToTriggerNumber(xboxTrigger));
        }

        public ControllerBuilder add(String action, XboxDPAD xboxDPAD) {
            return addDPAD(action, Controls.dpadToHatDegrees(xboxDPAD));
        }






        public ControllerBuilder driver() {
            selected = 0;
            return this;
        }

        public ControllerBuilder operator() {
            selected = 1;
            return this;
        }



        public Controllers build() {
			Controllers user = new Controllers(this);
			return user;
		}
    }
}
