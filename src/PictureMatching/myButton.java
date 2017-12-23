package PictureMatching;
import java.awt.*;
import javax.swing.*;

/**
 * Created by effie on 16/12/31.
 */
public class myButton extends JButton
{
    int m_i;
    int m_j;

    public myButton(ImageIcon img)
    {
        super(img);
        m_i = 0;
        m_j = 0;
    }

    public myButton(ImageIcon img, int x, int y)
    {
        super(img);
        m_i = x + 1;
        m_j = y + 1;
    }
}
