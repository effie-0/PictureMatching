package PictureMatching;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import sun.audio.*;

/**
 * Created by effie on 16/12/27.
 */
public class GUI
{
    JFrame mFrame;
    JPanel mPanel;

    JLabel bg;
    myButton[][] map;

    ImageIcon[] img;
    ImageIcon bg_img, bg3, con_img, lose_img, win_img, bound_img;

    JButton pause, quit;
    JLabel myTime, myScore, pauseLabel, boundLabel;

    int remain;
    int money;//得分
    String time;
    String score;

    java.util.Timer myTimer;
    TimerTask myTask;

    private final static int startPosx = 100;
    private final static int startPosy = 120;
    private final static int length = 60;

    Logic logic;
    Pos pos1;
    Pos pos2;

    Socket mySocket;
    DataOutputStream dataToServer;
    BufferedReader dataFromServer;

    AudioStream bgMusic;

    GUI() {
        mFrame = new JFrame();
        mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mFrame.setSize(810, 610);
        mPanel = new JPanel();

        //load the images
        bg_img = new ImageIcon("resources/bg2.png");
        bg3 = new ImageIcon("resources/bg1.png");
        con_img = new ImageIcon("resources/continue.png");
        lose_img = new ImageIcon("resources/lose.png");
        win_img = new ImageIcon("resources/win.png");

        img = new ImageIcon[13];
        int i;
        for (i = 1; i < 13; i++) {
            img[i] = new ImageIcon("resources/" + i + ".png");
            img[i].setImage(img[i].getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
        }

        mPanel.setLayout(null);

        bg = new JLabel(bg_img);
        bg.setBounds(5, 5, bg_img.getIconWidth(), bg_img.getIconHeight());
        bg.setLayout(null);
        mPanel.add(bg);
        //bg.setVisible(true);


        mFrame.add(mPanel);
        mFrame.setVisible(true);

        pause = new JButton("PAUSE");
        pause.setBounds(500, 30, 80, 40);
        quit = new JButton("QUIT");
        quit.setBounds(600, 30, 80, 40);

        time = new String("TIME: ");
        score = new String("SCORE: ");
        money = 0;
        myTime = new JLabel(time);
        myScore = new JLabel(score);
        myTime.setBounds(150, 30, 80, 40);
        myScore.setBounds(250, 30, 80, 40);
        bg.add(myTime);
        bg.add(myScore);

        bg.add(pause);
        bg.add(quit);
        bg.repaint();

        pos1 = new Pos();
        pos2 = new Pos();

        bound_img = new ImageIcon("resources/bounds.png");
        bound_img.setImage(bound_img.getImage().getScaledInstance(length, length, Image.SCALE_DEFAULT));
        boundLabel = new JLabel(bound_img);

    }

    void initMap(int[][] myMap)
    {
        map = new myButton[6][10];

        int i, j;
        for(i = 0; i < 6; i++)
        {
            for(j = 0; j < 10; j++)
            {
                map[i][j] = new myButton(img[myMap[i + 1][j + 1]], i, j);
                map[i][j].setBounds(startPosx + j * length, startPosy + i * length, length, length);
                //map[i][j].setBackground(new Color(117, 117, 180));
                map[i][j].setOpaque(false);
                bg.add(map[i][j]);
                bg.repaint();
            }
        }

        try
        {
            bgMusic = new AudioStream(GUI.class.getResourceAsStream("/Libertango.wav"));
        }
        catch (IOException e)
        {

        }
        AudioPlayer.player.start(bgMusic);
    }

    void initAction(boolean isSingle)
    {
        if(isSingle)
        {
            //单人游戏

            //初始化timer
            myTimer = new java.util.Timer(true);
            remain = 61;
            myTask = new TimerTask()
            {
                public void run()
                {
                    remain--;
                    setTime();

                    if(remain <= 0)
                    {
                        setLose();
                        myTimer.cancel();
                    }
                }
            };

            myTimer.schedule(myTask, 0, 1000);

            int i, j;
            for(i = 0; i < 6; i++)
            {
                for(j = 0; j < 10; j++)
                {
                    map[i][j].addActionListener(new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                            int m_i = ((myButton)e.getSource()).m_i;
                            int m_j = ((myButton)e.getSource()).m_j;

                            if(pos1.empty())
                            {
                                pos1.assign(m_i, m_j);
                                setBound(pos1);
                            }
                            else if(pos2.empty())
                            {
                                pos2.assign(m_i, m_j);
                                removeBound();
                                List<Pos> list = new LinkedList();

                                if(logic.Judge(pos1.x, pos1.y, pos2.x, pos2.y, list))
                                {
                                    money += 1;
                                    setScore();

                                    setRoad(list);
                                }

                                pos1.clear();
                                pos2.clear();

                                if(logic.finish())
                                {
                                    setWinning();
                                    myTimer.cancel();
                                }
                            }
                        }
                    });
                }
            }

            pause.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    myTimer.cancel();
                    myTimer = new java.util.Timer(true);
                    myTask = new TimerTask()
                    {
                        public void run()
                        {
                            remain--;
                            setTime();

                            if(remain <= 0)
                            {
                                setLose();
                                myTimer.cancel();
                            }
                        }
                    };

                    pauseLabel = new JLabel(bg3);
                    JLabel conLabel = new JLabel(con_img);
                    pauseLabel.setBounds(bg.getBounds());
                    conLabel.setBounds(pauseLabel.getBounds());
                    pauseLabel.add(conLabel);
                    bg.setVisible(false);
                    mPanel.add(pauseLabel);
                    mPanel.repaint();

                    pauseLabel.addMouseListener(new MouseAdapter()
                    {
                        @Override
                        public void mousePressed(MouseEvent e)
                        {
                            super.mousePressed(e);
                            mPanel.remove(pauseLabel);
                            bg.setVisible(true);
                            remain++;
                            myTimer.schedule(myTask, 0, 1000);
                        }
                    });
                }
            });

            quit.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    setEnd();
                }
            });
        }
        else
        {
            int i, j;
            for(i = 0; i < 6; i++)
            {
                for(j = 0; j < 10; j++)
                {
                    map[i][j].addActionListener(new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                            int m_i = ((myButton)e.getSource()).m_i;
                            int m_j = ((myButton)e.getSource()).m_j;

                            try
                            {
                                dataToServer.writeBytes("pos" + '\n');
                                dataToServer.writeBytes(Integer.toString(m_i) + '\n');
                                dataToServer.writeBytes(Integer.toString(m_j) + '\n');
                            }
                            catch(IOException e0)
                            {

                            }
                        }
                    });
                }
            }

            pause.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    try
                    {
                        dataToServer.writeBytes("pause" + '\n');
                    }
                    catch(IOException e1)
                    {

                    }
                }
            });

            quit.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    try
                    {
                        dataToServer.writeBytes("quit" + '\n');
                        dataToServer.writeBytes("end" + '\n');
                    }
                    catch(IOException e2)
                    {

                    }

                }
            });
        }

    }

    void getConnected(Socket s)
    {
        mySocket = s;
        try
        {
            dataToServer = new DataOutputStream(mySocket.getOutputStream());
            dataFromServer = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
        }
        catch(IOException e)
        {
            System.err.println("IOException");
        }
    }


    void setLose()
    {
        JLabel endLabel = new JLabel(bg3);
        JLabel loseLabel = new JLabel(lose_img);
        endLabel.setBounds(bg.getBounds());
        loseLabel.setBounds(bg.getBounds());
        endLabel.add(loseLabel);

        bg.setVisible(false);
        bg.remove(myScore);
        myScore.setBounds(350, 400, 100, 60);
        endLabel.add(myScore);
        mPanel.add(endLabel);
        mPanel.repaint();

        loseLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                super.mousePressed(e);
                mFrame.dispose();
                System.exit(0);
            }
        });
    }

    void setTime()
    {
        time += Long.toString(remain);
        myTime.setText(time);
        myTime.repaint();
        time = "TIME: ";
    }

    void setScore()
    {
        score += Integer.toString(money);
        myScore.setText(score);
        score = "SCORE: ";
        bg.repaint();
    }

    void setRoad(List<Pos> list)
    {
        list.add(0, pos1);
        list.add(pos2);

        map[pos1.x - 1][pos1.y - 1].setVisible(false);
        map[pos2.x - 1][pos2.y - 1].setVisible(false);
        bg.repaint();

        ListIterator iter = list.listIterator();
        Pos temp = (Pos) iter.next();
        for (Pos p : list)
        {
            if (p != pos2)
            {
                temp = (Pos) iter.next();
                JLabel myLabel = new JLabel();
                myLabel.setOpaque(true);
                myLabel.setBackground(Color.WHITE);
                int x = 0, y = 0;
                if (p.y < temp.y)
                {
                    y = p.y;
                    x = p.x;
                }
                else if (p.y > temp.y)
                {
                    y = temp.y;
                    x = temp.x;
                }
                else
                {
                    if (p.x < temp.x)
                    {
                        x = p.x;
                        y = p.y;
                    }
                    else if (p.x > temp.x)
                    {
                        x = temp.x;
                        y = temp.y;
                    }
                }

                myLabel.setBounds(startPosx + (y - 1) * length + length / 2 - 4, startPosy + (x - 1) * length + length / 2 - 4,
                        Math.abs(p.y - temp.y) * length + 8, Math.abs(p.x - temp.x) * length + 8);

                java.util.Timer t = new java.util.Timer();
                TimerTask task = new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        myLabel.setVisible(false);
                    }
                };

                t.schedule(task, 300);

                myLabel.setVisible(true);
                bg.add(myLabel);
                bg.repaint();

            }
            else
                break;

        }
    }

    void setWinning()
    {
        JLabel endLabel = new JLabel(bg3);
        JLabel winLabel = new JLabel(win_img);
        endLabel.setBounds(bg.getBounds());
        winLabel.setBounds(bg.getBounds());
        endLabel.add(winLabel);

        bg.setVisible(false);
        mPanel.add(endLabel);
        mPanel.repaint();

        endLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                super.mousePressed(e);
                mFrame.dispose();
                System.exit(0);
            }
        });
    }

    void setPause()
    {
        pauseLabel = new JLabel(bg3);
        JLabel conLabel = new JLabel(con_img);
        pauseLabel.setBounds(bg.getBounds());
        conLabel.setBounds(pauseLabel.getBounds());
        pauseLabel.add(conLabel);
        bg.setVisible(false);
        mPanel.add(pauseLabel);
        mPanel.repaint();

        pauseLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                super.mousePressed(e);

                try
                {
                    dataToServer.writeBytes("continue" + '\n');
                }
                catch(IOException e2)
                {

                }
            }
        });
    }

    void getLogic(Logic l)
    {
        logic = l;
    }

    void setEnd()
    {
        mFrame.dispose();
        System.exit(0);
    }

    void setContinue()
    {
        mPanel.remove(pauseLabel);
        bg.setVisible(true);
        mPanel.repaint();
    }

    void setBound(Pos p)
    {
        boundLabel.setVisible(true);
        boundLabel.setBounds(map[p.x - 1][p.y - 1].getBounds());
        bg.add(boundLabel);
        bg.repaint();
    }

    void removeBound()
    {
        boundLabel.setVisible(false);
        bg.remove(boundLabel);
    }
    /*
    public static void main(String[] args)
    {
        GUI g = new GUI();
    }*/
}
