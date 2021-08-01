import processing.core.PImage;

import java.util.List;

public class Biker extends NPC
{
    private int IndexStart = 0;
    private final int forwardIndexStart = 0;
    private final int backIndexStart = 1;
    private final int leftIndexStart = 2;
    private final int rightIndexStart = 3;
    private final int actionPeriod = 900;
    private final int aniPeriod = 100;
    public Biker(String id, Point pos, List<PImage> images)
    {
        super(id, pos, images, 0, 0);
        setActionPeriod(actionPeriod);
        setAnimationPeriod(aniPeriod);

    }

    @Override
    void death(WorldModel world, EntityFactory ef, EventScheduler scheduler) {
        Point deathPoint = getPosition();
        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);
        NPC fallen = ef.createDebiked(getId(), deathPoint);
        world.addEntity(fallen);
        scheduler.scheduleActions(fallen, world, ef);
    }

    @Override
    protected void changeToFrontAni() {
        IndexStart = forwardIndexStart;

    }

    @Override
    protected void changeToBackAni() {
        IndexStart = backIndexStart;

    }

    @Override
    protected void changeToLeftAni() {
        IndexStart = leftIndexStart;

    }

    @Override
    protected void changeToRightAni() {
        IndexStart = rightIndexStart;

    }


    @Override
    public void nextImage()
    {
        super.setImageIndex(IndexStart);
    }
}
