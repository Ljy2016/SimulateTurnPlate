package com.example.azadljy.simulateturnplate.TurnPlate;

/**
 * 作者：Ljy on 2017/2/15.
 * 邮箱：enjoy_azad@sina.com
 */

public class TurnPlate {
    private float startAngularSpeed = 10;//初始角速度
    private float m = 3;//质量
    private float g = 9.8f;//重力加速度
    private float a = 0.01f;//阻尼相关常数
    private float b = 0.01f;//阻尼相关常数
    private float I;//转盘转动惯量
    private float r;//半径
    private static float maxSpeed = 80;//最大角速度
    private float currentSpeed;

    public TurnPlate(float r) {
        this.r = r;
        I = (float) (0.5 * m * r * r);//粗略计算转动惯量
    }

    /**
     * 得到当前角速度
     *
     * @param currentTime 当前时间
     * @param isUpSpeed   true:加速状态   false:减速状态
     * @return
     */
    public float getAngularSpeed(float currentTime, boolean isUpSpeed) {
        if (isUpSpeed) {
            currentSpeed = (float) ((startAngularSpeed - (m * g * r - a) / b) * Math.exp(-((b / (I + m * r * r)) * currentTime)) + (m * g * r - a) / b);
            if (currentSpeed < maxSpeed) {
                return currentSpeed;
            } else {
                return maxSpeed;
            }
        } else {
            currentSpeed = (float) ((maxSpeed + a / b) * Math.exp(-(b / I) * currentTime) - a / b);
            if (currentSpeed < 0) {
                return 0;
            } else {
                return currentSpeed;
            }
        }
    }

    public float getStartAngularSpeed() {
        return startAngularSpeed;
    }

    public void setStartAngularSpeed(float startAngularSpeed) {
        this.startAngularSpeed = startAngularSpeed;
    }

    public float getM() {
        return m;
    }

    public void setM(float m) {
        this.m = m;
        I = (float) (0.5 * m * r * r);
    }

    public float getG() {
        return g;
    }

    public void setG(float g) {
        this.g = g;
    }

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }

    public float getI() {
        return I;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
        I = (float) (0.5 * m * r * r);
    }
}
