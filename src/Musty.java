import processing.core.PImage;

import java.util.List;
import java.util.function.Predicate;

public class Musty extends Entity implements Animate, Active
{
    private static int IndexStart = 0;
    private static int IndexEnd = 1;
    private static final int forwardIndexStart = 0;
    private static final int forwardIndexEnd = 1;
    private static final int backIndexStart = 2;
    private static final int backIndexEnd = 3;
    private static final int leftIndexStart = 4;
    private static final int leftIndexEnd = 6;
    private static final int rightIndexStart = 7;
    private static final int rightIndexEnd = 9;
    private final int STARTINGLIVES = 1;
    private static int lives;
    private static Musty loner;
    private final static int MUSTYACTPERIOD = 900;
    private final static int MUSTYANIPERIOD = 100;
    private final static int repeatCount = 0;
    private static Point position;
    private Musty(String id, Point pos, List<PImage> images)
    {
        super(id, pos, images);
        position = pos;
        lives = STARTINGLIVES;
    }

    public static boolean exists()
    {
        return loner != null;
    }

    public static Musty getMusty(String id, Point pos, List<PImage> images) {
        if (loner == null)
        {
            loner = new Musty(id, pos, images);
        }
        return loner;
    }

    private boolean downed(WorldModel world, EntityFactory entityFactory, EventScheduler scheduler)
    {
        if (lives <= 0)
        {
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);
            loner = null;
            DownedMusty m = entityFactory.createDownedMusty(getId(), position);
            world.addEntity(m);
            scheduler.scheduleActions(m, world, entityFactory);
            return true;
        }
        return false;
    }

    public static void moveUp(WorldModel world)
    {
        if (loner != null)
        {
            Predicate<Point> canEnter = pt -> world.getOccupant(pt).isEmpty() && world.withinBounds(pt);
            Point potPos = new Point(position.getX(), (position.getY() - 1));
            if (canEnter.test(potPos)) {
                changeToBackAni();
                moveEntity(world, potPos);
            }
        }
    }

    public static void moveDown(WorldModel world)
    {
        if (loner != null)
        {
            Predicate<Point> canEnter = pt -> world.getOccupant(pt).isEmpty() && world.withinBounds(pt);
            Point potPos = new Point(position.getX(), (position.getY() + 1));
            if (canEnter.test(potPos)) {
                changeToFrontAni();
                moveEntity(world, potPos);
            }
        }
    }

    public static void moveRight(WorldModel world)
    {
        if (loner != null)
        {
            Predicate<Point> canEnter = pt -> world.getOccupant(pt).isEmpty() && world.withinBounds(pt);
            Point potPos = new Point((position.getX() + 1), position.getY());
            if (canEnter.test(potPos)) {
                changeToRightAni();
                moveEntity(world, potPos);
            }
        }
    }

    @Override
    public Point getPosition()
    {
        return position;
    }

    public static void moveLeft(WorldModel world)
    {
        if (loner != null){
            Predicate<Point> canEnter = pt -> world.getOccupant(pt).isEmpty() && world.withinBounds(pt);
            Point potPos = new Point((position.getX() - 1), position.getY());
          if (canEnter.test(potPos))
            {
                changeToLeftAni();
                moveEntity(world,potPos);
            }
        }
    }

    public static boolean throwUp(WorldModel worldModel, EntityFactory entityFactory, EventScheduler scheduler)
    {
        if (loner != null)
        {
            Point spawnPoint = new Point(position.getX(), (position.getY() - 1));
            if (!worldModel.isOccupied(spawnPoint)) {
                Projectile p = entityFactory.createUpProj("Musty-proj", spawnPoint);
                if (p != null) {
                    worldModel.addEntity(p);
                    scheduler.scheduleActions(p, worldModel, entityFactory);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean throwDown(WorldModel worldModel, EntityFactory entityFactory, EventScheduler scheduler)
    {
        if (loner != null) {
            Point spawnPoint = new Point(position.getX(), (position.getY() + 1));
            if (!worldModel.isOccupied(spawnPoint)) {
                Projectile p = entityFactory.createDownProj("Musty-proj", spawnPoint);
                if (p != null) {
                    worldModel.addEntity(p);
                    scheduler.scheduleActions(p, worldModel, entityFactory);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean throwRight(WorldModel worldModel, EntityFactory entityFactory, EventScheduler scheduler)
    {
        if (loner != null) {
            Point spawnPoint = new Point((position.getX() + 1), position.getY());
            if (!worldModel.isOccupied(spawnPoint)) {
                Projectile p = entityFactory.createRightProj("Musty-proj", spawnPoint);
                if (p != null) {
                    worldModel.addEntity(p);
                    scheduler.scheduleActions(p, worldModel, entityFactory);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean throwLeft(WorldModel worldModel, EntityFactory entityFactory, EventScheduler scheduler)
    {
        if (loner != null) {
            Point spawnPoint = new Point((position.getX() - 1), position.getY());
            if (!worldModel.isOccupied(spawnPoint)) {
                Projectile p = entityFactory.createLeftProj("Musty-proj", spawnPoint);
                if (p != null) {
                    worldModel.addEntity(p);
                    scheduler.scheduleActions(p, worldModel, entityFactory);
                    return true;
                }
            }
        }
        return false;
    }

    public static void loseLife()
    {
        lives--;
    }

    @Override
    public void executeActivity(WorldModel world, EntityFactory entityFactory, EventScheduler scheduler) {
        if (!downed(world, entityFactory, scheduler))
        {
            scheduler.scheduleEvent(this, new ActivityAction(this, world, entityFactory), MUSTYACTPERIOD);
        }
    }

    @Override
    public int getAnimationPeriod()
    {
        return MUSTYANIPERIOD;
    }

    private static void changeToFrontAni() {
        IndexStart = forwardIndexStart;
        IndexEnd = forwardIndexEnd;

    }

    private static void changeToBackAni() {
        IndexStart = backIndexStart;
        IndexEnd = backIndexEnd;

    }

    private static void changeToLeftAni() {
        IndexStart = leftIndexStart;
        IndexEnd = leftIndexEnd;

    }

    private static void changeToRightAni() {
        IndexStart = rightIndexStart;
        IndexEnd = rightIndexEnd;

    }


    @Override
    public void nextImage()
    {
        super.setImageIndex(((super.getImageIndex() + 1) % (IndexEnd - IndexStart)) + IndexStart);
    }

    @Override
    public int getRepeatCount() {
        return this.repeatCount;
    }

    public int getActionPeriod()
    {
        return MUSTYACTPERIOD;
    }

    private static void moveEntity(WorldModel world, Point pos)
    {
        Point oldPos = position;
        if (world.withinBounds(pos) && !pos.equals(oldPos))
        {
            world.setOccupancyCell(oldPos, null);
            world.removeEntityAt(pos);
            world.setOccupancyCell(pos, loner);
            position = (pos);
        }
    }


}
