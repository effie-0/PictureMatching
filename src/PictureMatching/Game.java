package PictureMatching;

import java.util.*;
import java.io.*;

/**
 * Created by effie on 17/1/1.
 */
public class Game
{
    Gamer g1, g2;
    Logic logic;
    java.util.Timer myTimer;
    TimerTask myTask;
    int remain;
    int money;
    Pos pos1, pos2;

    public Game(Gamer gamer1, Gamer gamer2)
    {
        g1 = gamer1;
        g2 = gamer2;
        logic = new Logic();
        g1.initMap(logic);
        g2.initMap(logic);

        remain = 61;
        setTimer();
        myTimer.schedule(myTask, 0, 1000);

        money = 0;
        pos1 = new Pos();
        pos2 = new Pos();

        GameThread gThread1 = new GameThread(g1, g2, this);
        GameThread gThread2 = new GameThread(g2, g1, this);
        gThread1.start();
        gThread2.start();
    }

    void setTimer()
    {
        myTimer = new java.util.Timer(true);

        myTask = new TimerTask()
        {
            public void run()
            {
                remain--;
                try
                {
                    g1.dataToClient.writeBytes("time" + '\n');
                    g1.dataToClient.writeBytes(Integer.toString(remain) + '\n');
                    g2.dataToClient.writeBytes("time" + '\n');
                    g2.dataToClient.writeBytes(Integer.toString(remain) +'\n');
                }
                catch(IOException e)
                {

                }

                if(remain <= 0)
                {
                    try
                    {
                        g1.dataToClient.writeBytes("lose" + '\n');
                        g1.dataToClient.writeBytes("end" + '\n');
                        g2.dataToClient.writeBytes("lose" + '\n');
                        g2.dataToClient.writeBytes("end" + '\n');
                    }
                    catch(IOException e2)
                    {

                    }
                    myTimer.cancel();
                }
            }
        };
    }

}
