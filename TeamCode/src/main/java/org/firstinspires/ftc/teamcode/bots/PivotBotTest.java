package org.firstinspires.ftc.teamcode.bots;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.Timer;
import java.util.TimerTask;

@Config
public class PivotBotTest extends FourWheelDriveBot { //change back to odometry bot later
    public static int maximumPivotPos = 1300;
    public static int minumimPivotPos = -100;

    public static int maximumSlidePos = 550;
    public static int minimumSlidePos = 50;

    public boolean pivotOutOfRange = false;
    public int pivotTarget = 200;
    public double pivotPower = 0.7;
    public int slideTarget = minimumSlidePos;

    public DcMotorEx pivotMotor2 = null;
    public DcMotorEx pivotMotor1 = null;
    public DcMotorEx slideMotor1 = null;
    public DcMotorEx slideMotor2 = null;

    @Override
    public void init(HardwareMap ahwMap) {
        super.init(ahwMap);
        pivotMotor1 = hwMap.get(DcMotorEx.class, "pivot1");
        pivotMotor1.setDirection(DcMotorSimple.Direction.REVERSE);
        pivotMotor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pivotMotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pivotMotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        pivotMotor1.setPower(0);

        pivotMotor2 = hwMap.get(DcMotorEx.class, "pivot2");
        pivotMotor2.setDirection(DcMotorSimple.Direction.REVERSE);
        pivotMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pivotMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pivotMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        pivotMotor2.setPower(0);

        slideMotor1 = hwMap.get(DcMotorEx.class, "slide1");
        slideMotor1.setDirection(DcMotorSimple.Direction.REVERSE);
        slideMotor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideMotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideMotor1.setPower(0);

        slideMotor2 = hwMap.get(DcMotorEx.class, "slide2");
        slideMotor2.setDirection(DcMotorSimple.Direction.REVERSE);
        slideMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideMotor2.setPower(0);


        // TODO
    }

    public PivotBotTest(LinearOpMode opMode) {
        super(opMode);
    }

    public int getSlidePosition() {
        return slideMotor1.getCurrentPosition();
    }

    public int getPivotPosition() {
        return pivotMotor1.getCurrentPosition();
    }

    protected void onTick() {

        super.onTick();

        if (pivotTarget > minumimPivotPos - 100 && pivotTarget < maximumPivotPos + 100){

            pivotOutOfRange = false;

            runPivotMotors(pivotTarget, 0.3);

            // TODO : PID control for the pivot motor

        } else {

            pivotOutOfRange = true;
            pivotMotor1.setPower(0);
            pivotMotor2.setPower(0);

        }

        if (slideTarget > 0 && slideTarget < maximumSlidePos + 100){

            slideMotor1.setTargetPosition(slideTarget);
            slideMotor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slideMotor2.setTargetPosition(slideTarget);
            slideMotor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // TODO : PID control for the slide motor
            slideMotor1.setPower(0.5);
            slideMotor2.setPower(0.5);

        } else {

            slideMotor1.setPower(0);
            slideMotor2.setPower(0);

        }
    }
    public void slideByDelta(int delta){
        slideTarget += delta;
    }
    public void slideControl(boolean up, boolean down) {
        if (up) {
            if (slideMotor1.getCurrentPosition() < maximumSlidePos) {
                slideTarget = slideMotor1.getCurrentPosition() + ((maximumSlidePos - slideMotor1.getCurrentPosition()) / 10);
                slideMotor1.setTargetPosition(slideTarget);
                slideMotor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slideMotor2.setTargetPosition(slideTarget);
                slideMotor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            }
        }
        if (down) {
            if (slideMotor1.getCurrentPosition() > minimumSlidePos) {
                slideTarget = slideMotor1.getCurrentPosition() - (slideMotor1.getCurrentPosition() / 10);
                slideMotor1.setTargetPosition(slideTarget);
                slideMotor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slideMotor2.setTargetPosition(slideTarget);
                slideMotor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            }
        }

        //make pivot same
    }

    public void pivotControl(boolean up, boolean down){
        if (up) {
            if (pivotMotor1.getCurrentPosition() < maximumPivotPos) {
                pivotTarget = pivotMotor1.getCurrentPosition() + ((maximumPivotPos - pivotMotor1.getCurrentPosition()) / 10);
            }
        }
        if (down) {
            if (pivotMotor1.getCurrentPosition() > minumimPivotPos) {
                pivotTarget = pivotMotor1.getCurrentPosition() - (pivotMotor1.getCurrentPosition() / 10);
            }
        }
    }
    public void runPivotMotors(int pT, double power){
        pivotMotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pivotMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        if (getPivotPosition() > pT - 20 && getPivotPosition() < pT + 20) {

            pivotMotor1.setPower(-0.2);
            pivotMotor2.setPower(-0.2);

        } else {

            if (getPivotPosition() < pT) {

                pivotMotor1.setPower(-power);
                pivotMotor2.setPower(-power);

            }
        }
    }
}