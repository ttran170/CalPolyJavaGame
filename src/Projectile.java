import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Projectile extends Movable
{
    private final ProjectilePathingStrategy ps;
    private int actionPeriod = 200;

    Projectile(String id, Point position, List<PImage> images, ProjectilePathingStrategy pps)
    {
        super(id, position, images, 0);
        setActionPeriod(actionPeriod);
        ps = pps;
    }

    private boolean inWay(WorldModel world, Point nextPos)
    {
        Predicate<Point> canDestroy = point -> (world.getOccupant(point).get() instanceof NPC) || (world.getOccupant(point).get() instanceof Projectile) || (world.getOccupant(point).get().getClass() == Obstacle.class);
        return world.isOccupied(nextPos) && canDestroy.test(nextPos);
    }

    public boolean moveTo(WorldModel world, EventScheduler scheduler, EntityFactory entityFactory)
    {
        Predicate<Point> canPass = pt -> world.getOccupant(pt).isEmpty() || inWay(world, pt) || world.withinBounds(pt);
        List<Point> path = ps.computePath(getPosition(),null, canPass, null, null);
        if (path.isEmpty())
        {
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);
            return true;
        }
        Point nextPos = path.get(0);
        if (!world.withinBounds(nextPos) || (world.isOccupied(nextPos) && (world.getOccupant(nextPos).get().getClass() == Building.class|| world.getOccupant(nextPos).get().getClass() == Building.class || world.getOccupant(nextPos).get().getClass() == Musty.class)))
        {
            entityFactory.decrProjCount();
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            return true;
        }
        if (inWay(world, nextPos))
        {
            Entity doomed = world.getOccupant(nextPos).get();
            if (doomed instanceof NPC)
            {
                NPC fallen = (NPC) doomed;
                fallen.death(world, entityFactory, scheduler);
            }
            entityFactory.decrProjCount();
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);
            return true;
        }
        else
        {
            moveEntity(world, nextPos);
            return false;
        }
    }

    @Override
    public void executeActivity(WorldModel world, EntityFactory entityFactory, EventScheduler scheduler)
    {
        if (!moveTo(world, scheduler, entityFactory))
        {
            scheduler.scheduleEvent(this, new ActivityAction(this, world, entityFactory), super.getActionPeriod());
        }

    }
}
