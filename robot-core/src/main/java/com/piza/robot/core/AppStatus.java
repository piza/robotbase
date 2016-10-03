package com.piza.robot.core;

/**
 * Created by Peter on 16/9/27.
 */
public class AppStatus {


    public enum RobotStatus{
        RUNNING,EXIT
    }
    public enum ChatStatus{
        ONLINE,OFFLINE
    }

    public enum JMSStatus{
        CONNECTED,DISCONNECTED
    }

    public static volatile ChatStatus chatStatus;

    public static volatile JMSStatus jmsStatus;

    public static volatile RobotStatus robotStatus;
}
