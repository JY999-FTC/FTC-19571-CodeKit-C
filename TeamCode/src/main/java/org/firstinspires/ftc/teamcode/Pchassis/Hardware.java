/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.Pchassis;

import com.arcrobotics.ftclib.drivebase.DifferentialDrive;
import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.hardware.RevIMU;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;



public class Hardware
{
    /* Public OpMode members. */
    public Motor m0 = null, m1 = null, m2 = null, m3 = null;
    public Motor carousel = null, intake = null;
    //DistanceSensor dist = null;
    public MecanumDrive m;
    public RevIMU imu;

    /* Constructor */
    public Hardware(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap h) {

        // Define and Initialize Motors
        m0 = new Motor(h, "m0");
        m1 = new Motor(h, "m2");
        m2 = new Motor(h, "m1");
        m3 = new Motor(h, "m3");
        intake = new Motor(h, "m4");
        carousel = new Motor(h, "m5");
        //dist = h.get(DistanceSensor.class, "distsensor");

        m0.set(0);
        m1.set(0);
        m2.set(0);
        m3.set(0);
        intake.set(0);
        carousel.set(0);

        m0.setInverted(false);
        m1.setInverted(true);
        m2.setInverted(false);
        m3.setInverted(true);
        intake.setInverted(false);
        carousel.setInverted(false);

        // Set motors to run with/without encoders
        m0.setRunMode(Motor.RunMode.PositionControl);
        m1.setRunMode(Motor.RunMode.PositionControl);
        m2.setRunMode(Motor.RunMode.PositionControl);
        m3.setRunMode(Motor.RunMode.PositionControl);
        intake.setRunMode(Motor.RunMode.PositionControl);
        carousel.setRunMode(Motor.RunMode.PositionControl);

        m0.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        m1.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        m2.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        m3.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        carousel.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        //servo.setRange(MIN_ANGLE, MAX_ANGLE);
        //servo.setPosition(0);

        m = new MecanumDrive(m0,m1,m2,m3);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        imu = new RevIMU(h,"imu");
        imu.init(parameters);

    }
}