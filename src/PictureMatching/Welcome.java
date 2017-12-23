package PictureMatching;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;

/**
 * Created by effie on 16/12/28.
 */
public class Welcome
{
    JFrame mFrame;
    JPanel mPanel;

    ImageIcon bg_img;
    JLabel bg;
    JButton single;
    JButton multi;
    JLabel addrLabel;
    JLabel portLabel;
    JTextField addr;
    JTextField port;

    Socket socket;
    int hostPort;
    String hostIP;

    public Welcome()
    {
        mFrame = new JFrame();
        mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mFrame.setSize(810, 610);

        mPanel = new JPanel();
        bg_img = new ImageIcon("resources/bg1.png");
        bg = new JLabel(bg_img);
        bg.setLayout(null);


        addrLabel = new JLabel("Host IP: (suggest: localhost)");
        addrLabel.setForeground(Color.WHITE);
        addrLabel.setBounds(80, 380, 250, 50);
        portLabel = new JLabel("Port: (suggest: 6729)");
        portLabel.setForeground(Color.WHITE);
        portLabel.setBounds(80, 460, 250, 50);
        bg.add(addrLabel);
        bg.add(portLabel);

        addr = new JTextField();
        addr.setBounds(300, 380, 200, 50);
        port = new JTextField();
        port.setBounds(300, 460, 200, 50);
        bg.add(addr);
        bg.add(port);

        single = new JButton("单人游戏");
        single.setBounds(600, 380, 100, 50);
        multi = new JButton("确定选择");
        multi.setBounds(600, 460, 100, 50);
        bg.add(single);
        bg.add(multi);


        mPanel.add(bg);
        mFrame.add(bg);
        mFrame.setVisible(true);

        initAction();
    }


    void initAction()
    {
        single.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                mFrame.dispose();

                GUI gui = new GUI();
                Logic logic = new Logic();
                gui.getLogic(logic);
                gui.initMap(logic.myMap);
                gui.initAction(true);

            }
        });

        multi.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                hostIP = addr.getText();
                try
                {
                    hostPort = Integer.parseInt(port.getText());
                }
                catch(NumberFormatException e1)
                {
                    System.err.println("NumberFormatException");
                }

                try
                {
                    socket = new Socket(hostIP, hostPort);
                    mFrame.dispose();
                    GUI gui = new GUI();
                    gui.getConnected(socket);
                    GUIThread guiThread = new GUIThread(gui);
                    guiThread.start();
                }
                catch(IOException e2)
                {
                    System.err.println("IOException");
                }
            }
        });
    }

    public static void main(String[] argc)
    {
        Welcome w = new Welcome();
    }
}
