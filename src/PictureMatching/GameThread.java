package PictureMatching;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

/**
 * Created by effie on 17/1/2.
 */
public class GameThread extends Thread
{
    Gamer g1, g2;
    Game g;

    public GameThread(Gamer gamer1, Gamer gamer2, Game game)
    {
        g1 = gamer1;
        g2 = gamer2;
        g = game;
    }

    public void run()
    {
        String str1 = new String();

        do
        {
            try
            {
                str1 = g1.dataFromClient.readLine();

                if(str1.equals("pos"))
                {
                    int m_i = Integer.parseInt(g1.dataFromClient.readLine());
                    int m_j = Integer.parseInt(g1.dataFromClient.readLine());

                    if(g.pos1.empty())
                    {
                        g.pos1.assign(m_i, m_j);
                        g2.dataToClient.writeBytes("pos" + '\n');
                        g2.dataToClient.writeBytes(Integer.toString(m_i) + '\n');
                        g2.dataToClient.writeBytes(Integer.toString(m_j) + '\n');

                        g1.dataToClient.writeBytes("pos" + '\n');
                        g1.dataToClient.writeBytes(Integer.toString(m_i) + '\n');
                        g1.dataToClient.writeBytes(Integer.toString(m_j) + '\n');
                    }
                    else if(g.pos2.empty())
                    {
                        g.pos2.assign(m_i, m_j);

                        g1.dataToClient.writeBytes("clear" + '\n');
                        g2.dataToClient.writeBytes("clear" + '\n');

                        List<Pos> list = new LinkedList();
                        if(g.logic.Judge(g.pos1.x, g.pos1.y, g.pos2.x, g.pos2.y, list))
                        {
                            g1.dataToClient.writeBytes("check" + '\n');
                            g2.dataToClient.writeBytes("check" + '\n');

                            String temp1 = "pos1" + '\n' + g.pos1.x + '\n' + g.pos1.y + '\n';
                            String temp2 = "pos2" + '\n' + g.pos2.x + '\n' + g.pos2.y + '\n';

                            g1.dataToClient.writeBytes(temp1);
                            g1.dataToClient.writeBytes(temp2);

                            g2.dataToClient.writeBytes(temp1);
                            g2.dataToClient.writeBytes(temp2);

                            String temp3;
                            for(Pos p : list)
                            {
                                temp3 = Integer.toString(p.x) + '\n';
                                g1.dataToClient.writeBytes(temp3);
                                g2.dataToClient.writeBytes(temp3);

                                temp3 = Integer.toString(p.y) + '\n';
                                g1.dataToClient.writeBytes(temp3);
                                g2.dataToClient.writeBytes(temp3);

                            }

                            g1.dataToClient.writeBytes("stop" + '\n');
                            g2.dataToClient.writeBytes("stop" + '\n');

                            g.money += 1;
                            g1.dataToClient.writeBytes("score" + '\n');
                            g1.dataToClient.writeBytes(Integer.toString(g.money) + '\n');

                            g2.dataToClient.writeBytes("score" + '\n');
                            g2.dataToClient.writeBytes(Integer.toString(g.money) + '\n');

                            if(g.logic.finish())
                            {
                                g1.dataToClient.writeBytes("win" + '\n');
                                g1.dataToClient.writeBytes("end" + '\n');

                                g2.dataToClient.writeBytes("win" + '\n');
                                g2.dataToClient.writeBytes("end" + '\n');
                                g.myTimer.cancel();
                            }
                        }

                        g.pos1.clear();
                        g.pos2.clear();
                    }
                }
                else if(str1.equals("pause"))
                {
                    g.myTimer.cancel();
                    g.setTimer();

                    g1.dataToClient.writeBytes("pause" + '\n');
                    g2.dataToClient.writeBytes("pause" + '\n');
                }
                else if(str1.equals("quit"))
                {
                    g.myTimer.cancel();
                    g1.dataToClient.writeBytes("quit" + '\n');
                    g1.dataToClient.writeBytes("end" + '\n');

                    g2.dataToClient.writeBytes("quit" + '\n');
                    g2.dataToClient.writeBytes("end" + '\n');
                }
                else if(str1.equals("continue"))
                {
                    g.myTimer.schedule(g.myTask, 0, 1000);
                    g.remain++;

                    g1.dataToClient.writeBytes("continue" + '\n');
                    g2.dataToClient.writeBytes("continue" + '\n');
                }

            }
            catch(IOException e)
            {
                System.err.println("IOException");
            }
        }while(!str1.equals("end"));

    }
}
