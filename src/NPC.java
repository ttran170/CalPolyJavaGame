import processing.core.PImage;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

abstract public class NPC extends Movable implements Animate
{
    private PathingStrategy ps = new AStarPathingStrategy();
    private List<Point> path = new LinkedList<Point>();
    private int aniPeriod;
    private int repeatCount = 0;
    private int dir = 0;

    public NPC(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod);
        aniPeriod = animationPeriod;
    }

    public void setPathingStrat(PathingStrategy pathStrat)
    {
        ps = pathStrat;
    }

    protected void setAnimationPeriod(int animationPeriod)
    {
        aniPeriod = animationPeriod;
    }

    public PathingStrategy getPS()
    {
        return ps;
    }

    @Override
    public int getAnimationPeriod() {
        return aniPeriod;
    }

    abstract void death(WorldModel world, EntityFactory ef, EventScheduler scheduler);

    protected Point nextPosition(WorldModel world, Point destPos, Predicate<Point> canPass)
    {
        if (path.isEmpty() || (path.stream().filter(canPass).count() != path.stream().count()))
        {
            List<Point> potentialPath = ps.computePath(getPosition(), destPos,
                    canPass, null, ps.CARDINAL_NEIGHBORS);
            if (potentialPath.isEmpty())
            {
                potentialPath = ps.computePath(getPosition(), destPos,
                        canPass, null, ps.CARDINAL_NEIGHBORS);
            }
            if (potentialPath.isEmpty())
            {
                return getPosition();
            }
            path = potentialPath;
        }
        Point nextPos = path.get(0);
        path.remove(0);
        return nextPos;
    }

    @Override
    public int getRepeatCount() {
        return this.repeatCount;
    }

    private Optional<Entity> nearestEntity(List<Entity> entities, Point pos)
    {
        if (entities.isEmpty())
        {
            return Optional.empty();
        }
        else
        {
            Entity nearest = entities.get(0);
            int nearestDistance = nearest.getPosition().distanceSquared(pos);

            for (Entity other : entities)
            {
                int otherDistance = other.getPosition().distanceSquared(pos);

                if (otherDistance < nearestDistance)
                {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

    private Optional<Entity> findNearest(WorldModel world, Point pos, Class target)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : world.getEntities())
        {
            if (entity.getClass() == target)
            {
                ofType.add(entity);
            }
        }

        return nearestEntity(ofType, pos);
    }

    private boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler)
    {
        if (super.getPosition().adjacent(target.getPosition()))
        {
            Musty.loseLife();
            return true;
        }
        else
        {
            Predicate<Point> canPass = pt -> world.getOccupant(pt).isEmpty();
            Point nextPos = this.nextPosition(world, target.getPosition(), canPass);
            dir = calcDir(getPosition(), nextPos);

            if (!super.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }
                moveEntity(world, nextPos);
                switch (dir) {
                    case 0:
                        changeToFrontAni();
                        break;
                    case 1:
                        changeToBackAni();
                        break;
                    case 2:
                        changeToLeftAni();
                        break;
                    case 3:
                        changeToRightAni();
                        break;
                }
            }
            return false;
        }
    }
    /*
    dir = direction npc is facing
    0 = forward
    1 = back
    2 = left
    3 = right
    default is forward
     */
    private int calcDir(Point curr, Point next)
    {
        if (curr.getX() - next.getX() > 0)
            return 2;
        if (curr.getX() - next.getX() < 0)
            return 3;
        if (curr.getY() - next.getY() > 0)
            return 1;
        return 0;
    }

    abstract protected void changeToFrontAni();
    abstract protected void changeToBackAni();
    abstract protected void changeToLeftAni();
    abstract protected void changeToRightAni();

    public void executeActivity(WorldModel world, EntityFactory entityFactory, EventScheduler scheduler)
    {
        Optional<Entity> target = findNearest(world, getPosition(), Musty.class);
        long nextPeriod = super.getActionPeriod();

        if (target.isPresent())
        {
            Musty m = (Musty) target.get();
            if (moveTo(world, m, scheduler))
            {
                Musty.loseLife();
            }
        }

        scheduler.scheduleEvent(this, new ActivityAction(this, world, entityFactory),
                nextPeriod);
    }

}
