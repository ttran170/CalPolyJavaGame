import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Building extends Entity implements Active
{
    private int actionPeriod;
    private static final int NPC_REACH = 2;
    private static final String NPC_ID_PREFIX = "npc -- ";
    private static final int ACTION_PERIOD = 6000;
    private static double spawnRate = 0.3;
    private static final Random rand = new Random();

    public Building(String id, Point position, List<PImage> images)
    {
        super(id, position, images);
        this.actionPeriod = ACTION_PERIOD;
    }

    public int getActionPeriod()
    {
        return actionPeriod;
    }

    private Optional<Point> findOpenAround(WorldModel world, Point pos)
    {
        for (int dy = -NPC_REACH; dy <= NPC_REACH; dy++)
        {
            for (int dx = -NPC_REACH; dx <= NPC_REACH; dx++)
            {
                Point newPt = new Point(pos.getX() + dx, pos.getY() + dy);
                if (world.withinBounds(newPt) &&
                        !world.isOccupied(newPt))
                {
                    return Optional.of(newPt);
                }
            }
        }

        return Optional.empty();
    }

    public void executeActivity(WorldModel world, EntityFactory entityFactory, EventScheduler scheduler)
    {
        Optional<Point> openPt = findOpenAround(world, super.getPosition());

        float randFloat = rand.nextFloat();
        if (openPt.isPresent() && randFloat < spawnRate)
        {
            NPC enemy;
            int randInt = rand.nextInt(3);
            switch (randInt)
            {
                case 0 : enemy = entityFactory.createBiker((NPC_ID_PREFIX + super.getId()),openPt.get());
                    break;
                case 1 : enemy = entityFactory.createFStud((NPC_ID_PREFIX + super.getId()),openPt.get());
                    break;
                case 2 : enemy = entityFactory.createMStud((NPC_ID_PREFIX + super.getId()),openPt.get());
                    break;
                default: enemy = entityFactory.createFStud((NPC_ID_PREFIX + super.getId()),openPt.get());
                    break;
            }
            world.addEntity(enemy);
            scheduler.scheduleActions(enemy, world, entityFactory);
        }

        scheduler.scheduleEvent(this, new ActivityAction(this, world, entityFactory), actionPeriod);
    }
}
