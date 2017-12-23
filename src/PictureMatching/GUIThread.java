package PictureMatching;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by effie on 17/1/2.
 */
public class GUIThread extends Thread
{
    GUI gui;
    public GUIThread(GUI g)
    {
        gui = g;
    }

    public void run()
    {
        String str;
        int[][] myMap = new int[8][12];

        try
        {
            do
            {
                str = gui.dataFromServer.readLine();
                if(str.equals("initMap"))
                {
                    int i, j;
                    for (i = 0; i < 8; i++)
                    {
                        for (j = 0; j < 12; j++)
                        {
                            myMap[i][j] = Integer.parseInt(gui.dataFromServer.readLine());
                        }
                    }
                    gui.initMap(myMap);
                    gui.initAction(false);
                }
                else if(str.equals("pos"))
                {
                    int m_i = Integer.parseInt(gui.dataFromServer.readLine());
                    int m_j = Integer.parseInt(gui.dataFromServer.readLine());

                    Pos p = new Pos(m_i, m_j);
                    gui.setBound(p);
                }
                else if(str.equals("time"))
                {
                    gui.remain = Integer.parseInt(gui.dataFromServer.readLine());
                    gui.setTime();
                }
                else if(str.equals("score"))
                {
                    gui.money = Integer.parseInt(gui.dataFromServer.readLine());
                    gui.setScore();
                }
                else if(str.equals("quit"))
                {
                    gui.setEnd();
                }
                else if(str.equals("lose"))
                {
                    gui.setLose();
                }
                else if(str.equals("pause"))
                {
                    gui.setPause();
                }
                else if(str.equals("win"))
                {
                    gui.setWinning();
                }
                else if(str.equals("clear"))
                {
                    gui.removeBound();
                }
                else if(str.equals("check"))
                {
                    //消去选中的按钮
                    List<Pos> list = new LinkedList<>();

                    int x = -1, y = -1;
                    do
                    {
                        str = gui.dataFromServer.readLine();

                        if(str.equals("pos1"))
                        {
                            x = Integer.parseInt(gui.dataFromServer.readLine());
                            y = Integer.parseInt(gui.dataFromServer.readLine());
                            gui.pos1.assign(x, y);
                            x = -1;
                            y = -1;
                        }
                        else if(str.equals("pos2"))
                        {
                            x = Integer.parseInt(gui.dataFromServer.readLine());
                            y = Integer.parseInt(gui.dataFromServer.readLine());
                            gui.pos2.assign(x, y);
                            x = -1;
                            y = -1;
                        }
                        else if(!str.equals("stop"))
                        {
                            if(x == -1)
                                x = Integer.parseInt(str);
                            else if(y == -1)
                            {
                                y = Integer.parseInt(str);
                                Pos p = new Pos(x, y);
                                list.add(p);

                                x = -1;
                                y = -1;
                            }
                        }

                    }while(!str.equals("stop"));

                    gui.setRoad(list);

                    gui.pos1.clear();
                    gui.pos2.clear();
                }
                else if(str.equals("end"))
                {
                    gui.dataToServer.writeBytes("end" + '\n');
                }
                else if(str.equals("continue"))
                {
                    gui.setContinue();
                }
            }
            while (!str.equals("end"));
        }
        catch(IOException e)
        {
            System.err.println("IOException at running");
        }
    }
}
