package PictureMatching;
import java.net.*;
import java.io.*;

/**
 * Created by effie on 16/12/29.
 */
public class Gamer
{
    public InetAddress addr;
    public int port;
    public BufferedReader dataFromClient;
    public DataOutputStream dataToClient;
    public boolean isPlaying;
    Socket socket;

    public Gamer(Socket s)
    {
        socket = s;
        addr = socket.getInetAddress();
        port = socket.getPort();
        System.out.println(addr.getHostAddress());
        System.out.println(port);

        try
        {
            dataFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataToClient = new DataOutputStream(socket.getOutputStream());
            //dataToClient.writeBytes(addr.getHostAddress() + '\n');
            //dataToClient.writeBytes(Integer.toString(port) + '\n');
        } catch (IOException e)
        {

        }

        isPlaying = false;
    }

    public Gamer()
    {
        port = -1;
    }

    public boolean isEmpty()
    {
        if(port == -1)
            return true;
        else
            return false;
    }

    public void initMap(Logic l)
    {
        int i, j;
        try
        {
            dataToClient.writeBytes("initMap" + '\n');
        }
        catch (IOException e1)
        {
            System.err.println("IOException at initMap");
        }

        for(i = 0; i < 8; i++)
        {
            for(j = 0; j < 12; j++)
            {
                try
                {
                    dataToClient.writeBytes(Integer.toString(l.myMap[i][j]) + '\n');
                }
                catch(IOException e)
                {
                    System.err.println("IOException at initMap");
                }
            }
        }
    }
}
