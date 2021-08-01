import processing.core.PImage;
import java.util.List;

public abstract class Movable extends Entity implements Active
{
    private int actionPeriod;
    public Movable(String id, Point position, List<PImage> images, int actionPeriod)
    {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }

    public int getActionPeriod()
    {
        return actionPeriod;
    }

    protected void setActionPeriod(int actionPeriod)
    {
        this.actionPeriod = actionPeriod;
    }

    protected void moveEntity(WorldModel world, Point pos)
    {
        Point oldPos = getPosition();
        if (world.withinBounds(pos) && !pos.equals(oldPos))
        {
            world.setOccupancyCell(oldPos, null);
            world.removeEntityAt(pos);
            world.setOccupancyCell(pos, this);
            setPosition(pos);
        }
    }
}
