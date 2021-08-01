final class Point
{
   private final int x;
   private final int y;
   private int f, g, h;
   private Point prev;

   public Point(int x, int y)
   {
      this.x = x;
      this.y = y;
   }

    public void calcF(int g, Point goal)
    {
        setG(g);
        setH(goal);
        f = g + h;
    }

    public void setPrev(Point prev)
    {
        this.prev = prev;
    }

    public int getH()
    {
        return h;
    }

    private void setG(int g)
    {
        this.g = g;
    }

    private void setH(Point goal)
    {
        this.h = Math.abs(x - goal.x) + Math.abs(y - goal.y);
    }

    public int getF()
    {
        return f;
    }

    public Point getPrev()
    {
        return prev;
    }

    public int getG() {
        return g;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }


    public String toString()
   {
      return "(" + x + "," + y + ")";
   }

   public boolean equals(Object other)
   {
      return other instanceof Point &&
         ((Point)other).x == this.x &&
         ((Point)other).y == this.y;
   }

   public int hashCode()
   {
      int result = 17;
      result = result * 31 + x;
      result = result * 31 + y;
      return result;
   }

   public boolean adjacent(Point p1)
   {
      return (p1.x == this.x && Math.abs(p1.y - this.y) == 1) ||
              (p1.y == this.y && Math.abs(p1.x - this.x) == 1);
   }

   public int distanceSquared(Point p1)
   {
      int deltaX = p1.x - this.x;
      int deltaY = p1.y - this.y;

      return deltaX * deltaX + deltaY * deltaY;
   }
}
