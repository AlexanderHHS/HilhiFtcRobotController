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

package org.firstinspires.ftc.teamcode;

import android.view.inputmethod.CorrectionInfo;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.Range;

import java.lang.Math;
import java.util.Vector;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;



@Autonomous(name="12000", group="Robot")
public class TestAuto12000 extends LinearOpMode {

    public int newTarget = 0;
    private DcMotor LeftFront = null;
    private DcMotor LeftBack = null;
    private DcMotor RightFront = null;
    private DcMotor RightBack = null;
    private DcMotor RightEncoder = null;
    private DcMotor LeftEncoder = null;

    private Vector<Double> InitialPosition = new Vector<Double>(3);
    private Vector<Double> CurrentPosition = new Vector<Double>(3);
    private double FinalDistance = 0;
    private IMU RobotIMU = null;
    private double StartAngle = 0;

    //Robot12000 RobotFunctions = new Robot12000(this);
    //New wheel diameter of 12 cm
    private double COUNTS_PER_INCH  = (((2 * Math.PI * 2) / 8192) * 2.54 * 18) / 70 ; // 2pi * wheel radios / encoder tpr
    @Override
    public void runOpMode() {

        LeftFront = hardwareMap.get(DcMotor.class, "left_front");
        RightFront = hardwareMap.get(DcMotor.class, "right_front");
        LeftBack = hardwareMap.get(DcMotor.class, "left_back");
        RightBack = hardwareMap.get(DcMotor.class, "right_back");
        RightEncoder = hardwareMap.get(DcMotor.class, "right_odom");
        LeftEncoder = hardwareMap.get(DcMotor.class, "left_odom");

        RobotIMU = hardwareMap.get(IMU.class, "imu");

        LeftFront.setDirection(DcMotor.Direction.FORWARD);
        LeftBack.setDirection(DcMotor.Direction.FORWARD);
        RightFront.setDirection(DcMotor.Direction.FORWARD);
        RightBack.setDirection(DcMotor.Direction.FORWARD);
        StartAngle = RobotIMU.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        StartVector(InitialPosition, 0, 0, 10); // might want to be SetVector
        StartVector(CurrentPosition, 0, 0,10);  // ^
        waitForStart();
        //Test Move
        //Stick.setPosition(1);
        MoveTo(18, 18, 0, 5,3,0.4);
        sleep(1000);



    } // :)

    /* public void Move(double speed, double distanceInch, DcMotor encoder, DcMotor encoder2)
    {
        MotorSpeed(0, 0);
        newTarget =  (int)(distanceInch);
        encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        encoder2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        encoder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        encoder2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        encoder.setTargetPosition(newTarget);
        encoder2.setTargetPosition(newTarget);
        encoder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        encoder2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while(encoder.isBusy() || encoder2.isBusy())
        {
            double difference = encoder.getCurrentPosition() - encoder2.getCurrentPosition();
            double change = Range.clip(difference, -1.2, 1.2);;
            if(difference >= 0)
            {
                MotorSpeed(speed * change, speed / change);
            } else if (difference < 0) {
                MotorSpeed(speed / change, speed * change);
            }

        }
        MotorSpeed(0, 0);
        //RightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        encoder2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    } */
    public void MotorSpeed(double P1speed, double P2speed)
    {
        LeftFront.setPower(P1speed);
        RightFront.setPower(P2speed);
        LeftBack.setPower(P2speed);
        RightBack.setPower(P1speed);
        telemetry.addData("MotorSpeed", "Pair1 " + P1speed);
        telemetry.addData("MotorSpeed", "Pair2 " + P2speed);
        telemetry.update();
    }

    public void MoveTo(double TargetX, double TargetY, double TargetAngle, double PositionTolerance, double AngleTolerance, double Speed)
    {
        RightEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightEncoder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LeftEncoder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double Crx = 0;
        double Cry = 0;
        double Cfx = InitialPosition.get(0);
        double Cfy = InitialPosition.get(1);

        SetVector(CurrentPosition, InitialPosition.get(0), InitialPosition.get(1), InitialPosition.get(2));

        //Find distance from target
        double RB = Math.sqrt(Math.pow(CurrentPosition.get(0) - TargetX, 2) + Math.pow(CurrentPosition.get(1) - TargetY, 2));
        //double RB = 5;

        while(RB > PositionTolerance)
        {
            //RB = Math.sqrt(Math.pow(CurrentPosition.get(0) - TargetX, 2) + Math.pow(CurrentPosition.get(1) - TargetY, 2));
            //double AngleDelta = Math.atan((TargetX - CurrentPosition.get(0))/(TargetY - CurrentPosition.get(1)));
            double RobotYaw = StartAngle - RobotIMU.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            //Calculate robot distance and direction on robot frame of reference
            double D1 = COUNTS_PER_INCH*Math.sqrt(Math.pow(LeftEncoder.getCurrentPosition(),2)+ Math.pow(RightEncoder.getCurrentPosition(),2));
            double alpha = LeftEncoder.getCurrentPosition();
            double beta = RightEncoder.getCurrentPosition();

            double Theta1 = Math.atan(beta/alpha)-Math.PI/4.0;

            if(alpha == 0 && beta >= 0)
            {
                Theta1 = Math.PI/4;
            }else if(alpha == 0 && beta < 0)
            {
                Theta1 = 5*Math.PI/4;
            }

            //Account for issue with arctan since it only returns 0-PI
            if(alpha < 0)
            {
                Theta1 = Theta1 + Math.PI;
            }
            //Calculate Robot frame of reference X and Y distance moved
            double Drx = Math.sin(Theta1)*D1;
            double Dry = Math.cos(Theta1)*D1;

            //Convert distance and direction from robot frame of reference to field frame of reference
            //Angle of robot in field reference
            double ThetaF = Theta1 + RobotYaw;
            //Distance robot has moved in x-direction
            double Dfx = Math.sin(ThetaF)*D1;
            //Distance robot has moved in y-direction
            double Dfy = Math.cos(ThetaF)*D1;

            //track current position
            Crx = Crx + Drx;
            Cry = Cry + Dry;
            Cfx = Cfx + Dfx;
            Cfy = Cfy + Dfy;

            double DeltaX = TargetX - Cfx;
            double DeltaY = TargetY - Cfy;

            double R = Math.sqrt(Math.pow(DeltaX, 2) + Math.pow(DeltaY, 2));
            double Ttf = Math.atan(DeltaX/DeltaY);
            if(DeltaY == 0 && DeltaX >= 0)
            {
                Ttf = Math.PI/4;
            }else if(DeltaY == 0 && DeltaX < 0)
            {
                Ttf = 5* Math.PI/4;
            }
            if(DeltaX < 0)
            {
                Ttf = Ttf + Math.PI;
            }

            telemetry.addData("Robot X",Crx);
            telemetry.addData("Robot Y",Cry);
            telemetry.addData("Field X",Cfx);
            telemetry.addData("Field Y",Cfy);
            telemetry.addData("Robot Theta",Theta1);
            telemetry.addData("Field Theta",ThetaF);
            telemetry.addData("target direction",Ttf*360/(2*Math.PI));
            telemetry.addData("target radius", R);
            telemetry.addData("robot imu", RobotYaw*360/(2*Math.PI));
            telemetry.update();

            RB = Math.sqrt(Math.pow(CurrentPosition.get(0) - TargetX, 2) + Math.pow(CurrentPosition.get(1) - TargetY, 2));

            double RobotAngle =  Ttf - RobotYaw + Math.PI;
            //Motor Speed
            double M1 = (Math.sin(RobotAngle) + Math.cos(RobotAngle)); //LF
            double M2 = (Math.sin(RobotAngle) - Math.cos(RobotAngle)); //RF
            double M3 = (-Math.sin(RobotAngle) + Math.cos(RobotAngle)); //LB
            double M4 = (-Math.sin(RobotAngle) - Math.cos(RobotAngle)); //RB
            double Mmax;

            Mmax = Math.max(M1, M2);
            Mmax = Math.max(Mmax, M3);
            Mmax = Math.max(Mmax, M4);

            if(Mmax > 1)
            {
                M1 = M1/Mmax;
                M2 = M2/Mmax;
                M3 = M3/Mmax;
                M4 = M4/Mmax;
            }

            LeftFront.setPower(M1 * Speed);
            RightFront.setPower(M2 * Speed);
            LeftBack.setPower(M3 * Speed);
            RightBack.setPower(M4 * Speed);

            RB = Math.sqrt(Math.pow(CurrentPosition.get(0) - TargetX, 2) + Math.pow(CurrentPosition.get(1) - TargetY, 2));


            //RobotYaw = RobotIMU.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
            //double DeltaX = (RightEncoder.getCurrentPosition() * Math.cos(Math.PI/4 + RobotYaw)) - (LeftEncoder.getCurrentPosition() * Math.cos(Math.PI/4 - RobotYaw));
            //double DeltaY = (RightEncoder.getCurrentPosition() * Math.sin(Math.PI/4 + RobotYaw)) + (LeftEncoder.getCurrentPosition() * Math.sin(Math.PI/4 - RobotYaw));

            //SetVector(CurrentPosition, CurrentPosition.get(0) + (DeltaX * COUNTS_PER_INCH), CurrentPosition.get(1) + (DeltaY * COUNTS_PER_INCH), RobotIMU.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));
            RightEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            LeftEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            RightEncoder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            LeftEncoder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        SetVector(InitialPosition, Cfx, Cfy, CurrentPosition.get(2));
        LeftFront.setPower(0);
        LeftBack.setPower(0);
        RightBack.setPower(0);
        RightFront.setPower(0);
    }

    public void SetVector(Vector vector, double X, double Y, double angle )
    {
        vector.set(0, X);
        vector.set(1, Y);
        vector.set(2, angle);
    }
    public void StartVector(Vector vector, double X, double Y, double angle)
    {
        vector.add(X);
        vector.add(Y);
        vector.add(angle);
    }


}
