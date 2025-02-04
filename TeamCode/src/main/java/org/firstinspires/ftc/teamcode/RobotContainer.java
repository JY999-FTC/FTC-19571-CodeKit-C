package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.subsystems.Arm.ArmConstants.eStates.*;
import static org.firstinspires.ftc.teamcode.subsystems.Arm.ArmConstants.pStates.*;
import static org.firstinspires.ftc.teamcode.utils.BT.BTController.Buttons.*;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.RepeatCommand;
import com.arcrobotics.ftclib.command.SelectCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.qualcomm.robotcore.hardware.HardwareMap;


import org.firstinspires.ftc.teamcode.stateMachines.specimenStates.Place;
import org.firstinspires.ftc.teamcode.stateMachines.specimenStates.Supply;
import org.firstinspires.ftc.teamcode.stateMachines.states.Intake;
import org.firstinspires.ftc.teamcode.stateMachines.states.Score;
import org.firstinspires.ftc.teamcode.subsystems.Arm.ExtensionSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.Arm.PivotSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain.ChassisSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.Gripper.GripperSubsystem;
import org.firstinspires.ftc.teamcode.utils.BT.BTController;

import java.util.Map;

public class RobotContainer extends com.arcrobotics.ftclib.command.Robot {

    public GripperSubsystem m_gripper;
    ExtensionSubsystem m_extension;
    PivotSubsystem m_pivot;
    ChassisSubsystem m_chassis;
    BTController m_controller;
    BTController m_controller2;
    Trigger Intake;
    Trigger Score;
    Trigger specimenState;
    Trigger sampleState;
    Intake intakeCommand;
    Score scoreCommand;
    Supply supplyCommand;
    Place placeCommand;
    private Constants.States currentState;

    public RobotContainer(HardwareMap map, BTController gamepad1, BTController gamepad2){
        m_extension = new ExtensionSubsystem(map);
        m_gripper = new GripperSubsystem(map);
        m_chassis = new ChassisSubsystem(map);
        m_pivot = new PivotSubsystem(map, m_extension::getArmLength,m_chassis::getAcc);
        m_gripper.servoClaw.setPosition(0);
        m_gripper.isOpen = false;
        m_gripper.rotServo2.setPosition(score);
        m_controller = gamepad1;
        m_controller2 = gamepad2;
        scoreCommand = new Score(m_extension,m_pivot,m_chassis,m_gripper,m_controller);
        intakeCommand = new Intake(m_extension,m_pivot,m_chassis,m_gripper,m_controller);
        supplyCommand = new Supply(m_extension,m_pivot,m_chassis,m_gripper,m_controller);
        placeCommand = new Place(m_extension,m_pivot,m_chassis,m_gripper,m_controller);
        sampleState = new Trigger(m_controller.m_buttonsSuppliers[DPAD_LEFT.ordinal()]);
        specimenState = new Trigger(m_controller.m_buttonsSuppliers[DPAD_RIGHT.ordinal()]);
        Score = new Trigger(m_controller.m_buttonsSuppliers[BUMPER_LEFT.ordinal()]);
        Intake = new Trigger(m_controller.m_buttonsSuppliers[BUMPER_RIGHT.ordinal()]);
        resetGyro();
        configureBinds();
    }
    public double squareInput(double input){
        return Math.signum(input)*Math.pow(input,2);
    }
    private void configureBinds() {
//        m_controller.assignCommand(new ConditionalCommand(scoreCommand,new WaitUntilCommand(()->intakeCommand.isFinished()).andThen(scoreCommand),()->!intakeCommand.isScheduled()),false,BUMPER_LEFT);
//        m_controller.assignCommand(new ConditionalCommand(intakeCommand,new WaitUntilCommand(()->scoreCommand.isFinished()).andThen(intakeCommand),()->!scoreCommand.isScheduled()),false,BUMPER_RIGHT);
//        m_controller.assignCommand(new RepeatCommand(new StateMachine(m_extension,m_pivot,m_chassis,m_gripper,m_controller)),false,DPAD_UP);
        specimenState.whenActive(()->setSpecimenIntake().alongWith(setSpecimenScore()));
        sampleState.whenActive(()->setSampleIntake().alongWith(setSampleScore()));
        m_controller.assignCommand(m_chassis.fieldRelativeDrive(
                        () -> squareInput(-m_controller.getAxisValue(BTController.Axes.LEFT_Y_axis)),
                        () -> squareInput(m_controller.getAxisValue(BTController.Axes.LEFT_X_axis)),
                        () -> squareInput(m_controller.getAxisValue(BTController.Axes.RIGHT_TRIGGER_axis) - m_controller.getAxisValue(BTController.Axes.LEFT_TRIGGER_axis))),
                true, LEFT_Y, LEFT_X, RIGHT_TRIGGER,LEFT_TRIGGER).whenInactive(m_chassis.stopMotor());

    }

    private Command setSpecimenIntake(){
        new SelectCommand(
                Map.of(
                        Constants.States.SPECIMEN_DELIVER, new SpecimenDeliverIntake(),
                        Constants.States.SPECIMEN_PLACE, new SpecimenPlaceIntake()
                ),
                ()->currentState
        );
        new SelectCommand(
                Map.of(
                        Constants.States.SPECIMEN_DELIVER, new SpecimenDeliverPlace(),
                        Constants.States.SPECIMEN_PLACE, new SpecimenPlacePlace()
                ),
                ()->currentState
        );
        return new InstantCommand(()->currentState = Constants.States.SPECIMEN_DELIVER);
    }

    private Command setSpecimenScore(){
        return new InstantCommand(()->Intake.whenActive(placeCommand));
    }
    private Command setSampleIntake(){
        return new InstantCommand(()->Intake.whenActive(intakeCommand));
    }
    private Command setSampleScore(){
        return new InstantCommand(()->Intake.whenActive(scoreCommand));
    }
    private Command resetGyro() {
        return new InstantCommand(()->m_chassis.gyro.reset());
    }

    private static class Constants{
        public enum States{
            SPECIMEN_DELIVER,
            SPECIMEN_PLACE,
            SAMPLE
        }
    }
}
