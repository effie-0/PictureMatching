package PictureMatching;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.*;
import javax.swing.*;

/**
 * Created by effie on 16/12/29.
 */
public class Server extends JFrame
{
    JPanel mPanel;
    JLabel Ip;
    JLabel Port;
    SocketAddress addr;
    int port;

    Server()
    {
        mPanel = new JPanel();
        mPanel.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 300);
        this.add(mPanel);
        this.setVisible(true);

        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                super.windowClosing(e);
                System.exit(0);
            }
        });

    }

    void showInfo()
    {
        Port = new JLabel("my port number is " + port);
        Port.setBounds(50, 100, 300, 30);
        mPanel.add(Port);
        mPanel.setVisible(true);
    }

    public static void main(String[] args) throws Exception
    {
        Server server = new Server();
        ServerSocket welcome = new ServerSocket(6729);
        server.addr = welcome.getLocalSocketAddress();
        server.port = welcome.getLocalPort();
        server.showInfo();

        Gamer g1 = new Gamer();
        Gamer g2 = new Gamer();

        while(g2.isEmpty())
        {
            Socket s = welcome.accept();
            if(g1.isEmpty())
            {
                g1 = new Gamer(s);
            }
            else if(g2.isEmpty())
            {
                g2 = new Gamer(s);
                JLabel connect = new JLabel("is connecting");
                connect.setBounds(50, 150, 300, 30);
                server.mPanel.add(connect);
                server.mPanel.repaint();

                Game game = new Game(g1, g2);
            }
        }
    }

}
