package project;

public interface GetPort
{

    static int getMonitorPort()
    {
        int MonitorPort=2018;
        return MonitorPort;
    }


    static int getSendPort()
    {
        int SendPort=2019;
        return SendPort;
    }

    static int getArrayPort()
    {
        int ArrayPort=2020;
        return ArrayPort;
    }
}
