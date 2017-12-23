package PictureMatching;
import java.util.List;
import java.util.*;

/**
 * Created by effie on 16/12/27.
 */
public class Logic
{
    public int[][] myMap;
    private int remainNum;

    Logic()
    {
        myMap = new int[8][12];
        int i, j;
        for(i = 0; i < 8; i++)
        {
            for (j = 0; j < 12; j++)
            {
                myMap[i][j] = 0;
            }
        }
        remainNum = 60;
        initMap();
    }

    public void initMap()
    {
        int ran, ranCol, ranRow;
        int num = 0;//the number of the initialized element
        int i, j;
        for(i = 1; i < 7; i++)
        {
            if(num >= 60)
                break;

            for(j = 1; j < 11; j++)
            {
                if(num >= 60)
                    break;

                if(myMap[i][j] == 0)
                {
                    ran = (int)(Math.random() * 12 + 1);
                    myMap[i][j] = ran;
                    num++;

                    do
                    {
                        ranCol = (int) (Math.random() * 6 + 1);
                        ranRow = (int) (Math.random() * 10 + 1);
                    }while(myMap[ranCol][ranRow] != 0);

                    myMap[ranCol][ranRow] = ran;
                    num++;
                }
            }
        }
    }

    void printMap()
    {
        //just a test for the map
        int i, j;
        for(i = 0; i < 8; i++)
        {
            for(j = 0; j < 12; j++)
            {
                System.out.print(myMap[i][j] + " ");
            }
            System.out.println();
        }
    }

    boolean oneRoad(int x1, int y1, int x2, int y2)
    {
        boolean result = false;
        boolean changed = false;
        int i;

        //一条直线相连的状况
        if(x1 == x2)
        {
            if(y1 == y2)
            {
                result = false;
                return result;
            }
            if(y1 < y2)
            {
                for(i = y1 + 1; i < y2; i++)
                {
                    if(myMap[x1][i] != 0)
                    {
                        result = false;
                        changed = true;
                        break;
                    }
                }
                if(!result && !changed && i == y2)
                    result = true;
            }
            else
            {
                for(i = y2 + 1; i < y1; i++)
                {
                    if(myMap[x1][i] != 0)
                    {
                        result = false;
                        changed = true;
                        break;
                    }
                }
                if(!result && !changed && i == y1)
                    result = true;
            }
        }
        if(result)
            return result;

        changed = false;
        if(y1 == y2)
        {
            if(x1 < x2)
            {
                for(i = x1 + 1; i < x2; i++)
                {
                    if(myMap[i][y1] != 0)
                    {
                        result = false;
                        changed = true;
                        break;
                    }
                }

                if(!changed && i == x2)
                    result = true;
            }
            else
            {
                for(i = x2 + 1; i < x1; i++)
                {
                    if(myMap[i][y1] != 0)
                    {
                        result = false;
                        changed = true;
                        break;
                    }
                }

                if(!changed && i == x1)
                    result = true;
            }
        }
        if(result)
            return result;

        return result;
    }

    boolean TwoRoad(int x1, int y1, int x2, int y2, List<Pos> corner)
    {
        boolean result = false;

        if(oneRoad(x1, y1, x2, y2))
            result = true;
        else if(oneRoad(x1, y1, x2, y1) && oneRoad(x2, y1, x2, y2) && myMap[x2][y1] == 0)
        {
            corner.add(new Pos(x2, y1));
            result = true;
        }
        else if(oneRoad(x1, y1, x1, y2) && oneRoad(x1, y2, x2, y2) && myMap[x1][y2] == 0)
        {
            corner.add(new Pos(x1, y2));
            result = true;
        }

        return result;
    }

    boolean Judge(int x1, int y1, int x2, int y2, List<Pos> corner)
    {
        boolean result = false;
        if(myMap[x1][y1] == 0 || myMap[x2][y2] == 0)
            return false;
        if(myMap[x1][y1] != myMap[x2][y2])
            return false;
        if(x1 == x2 && y1 == y2)
            return false;

        if(TwoRoad(x1, y1, x2, y2, corner))
        {
            result = true;
        }

        int i;
        if(!result)
        {
            i = 1;
            if(x1 + i < 8)
            {
                while (!result && myMap[x1 + i][y1] == 0)
                {
                    if (TwoRoad(x1 + i, y1, x2, y2, corner))
                    {
                        corner.add(0, new Pos(x1 + i, y1));
                        result = true;
                        break;
                    }

                    i++;

                    if (x1 + i > 7)
                        break;
                }
            }

            i = 1;
            if(x1 - i >= 0)
            {
                while (!result && myMap[x1 - i][y1] == 0)
                {
                    if (TwoRoad(x1 - i, y1, x2, y2, corner))
                    {
                        corner.add(0, new Pos(x1 - i, y1));
                        result = true;
                        break;
                    }

                    i++;
                    if (x1 - i < 0)
                        break;
                }
            }

            i = 1;
            if(y1 + i < 12)
            {
                while (!result && myMap[x1][y1 + i] == 0)
                {
                    if (TwoRoad(x1, y1 + i, x2, y2, corner))
                    {
                        corner.add(0, new Pos(x1, y1 + i));
                        result = true;
                        break;
                    }

                    i++;
                    if (y1 + i > 11)
                        break;
                }
            }

            i = 1;
            if (y1 - i >= 0)
            {
                while (!result && myMap[x1][y1 - i] == 0)
                {
                    if (TwoRoad(x1, y1 - i, x2, y2, corner))
                    {
                        corner.add(0, new Pos(x1, y1 - i));
                        result = true;
                        break;
                    }

                    i++;
                    if (y1 - i < 0)
                        break;
                }
            }
        }

        if(result)
        {
            myMap[x1][y1] = 0;
            myMap[x2][y2] = 0;
            remainNum -= 2;
        }
        return result;
    }

    boolean finish()
    {
        if (remainNum <= 0)
            return true;
        else
            return false;
    }

    /*public static void main(String[] args)
    {
        Logic g = new Logic();
        g.printMap();

        Scanner console = new Scanner(System.in);
        while (true)
        {

            int x1, y1, x2, y2;
            x1 = console.nextInt();
            y1 = console.nextInt();
            x2 = console.nextInt();
            y2 = console.nextInt();

            List<Pos> corner = new LinkedList();

            if (g.Judge(x1, y1, x2, y2, corner))
            {
                g.myMap[x1][y1] = 0;
                g.myMap[x2][y2] = 0;
            }

            g.printMap();
        }
    }*/
}
