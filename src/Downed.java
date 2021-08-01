import processing.core.PImage;

import java.util.List;

abstract public class Downed extends Entity implements Active, Animate
{
    private static final int DOWNED_ACTION_PERIOD = 1100;
    private static final int DOWNED_ANIMATION_PERIOD = 100;
    private static final int DOWNED_ANIMATION_REPEAT_COUNT = 0;

    public Downed(String id, Point position, List<PImage> images)
    {
        super(id, position, images);
    }

    @Override
    public int getActionPeriod() {
        return DOWNED_ACTION_PERIOD;
    }

    @Override
    public void executeActivity(WorldModel world, EntityFactory entityFactory, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents((Entity) this);
        world.removeEntity((Entity) this);
    }

    @Override
    public int getAnimationPeriod() {
        return DOWNED_ANIMATION_PERIOD;
    }

    @Override
    public void nextImage()
    {
        super.setImageIndex((super.getImageIndex() + 1) % super.getImages().size());
    }

    @Override
    public int getRepeatCount() {
        return DOWNED_ANIMATION_REPEAT_COUNT;
    }
}
