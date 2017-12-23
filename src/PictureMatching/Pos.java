package PictureMatching;

/**
 * Created by effie on 16/12/31.
 */
public class Pos
{
    public int x;
    public int y;
    public Pos()
    {
        x = 0;
        y = 0;
    }

    public Pos(int v1, int v2)
    {
        x = v1;
        y = v2;
    }

    boolean empty()
    {
        if(x == 0 && y == 0)
            return true;
        else
            return false;
    }

    void assign(int v1, int v2)
    {
        x = v1;
        y = v2;
    }

    void clear()
    {
        x = 0;
        y = 0;
    }
}
