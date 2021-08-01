import processing.core.PImage;

import java.util.List;

public class Debiked extends NPC
{
    private int IndexStart = 0;
    private int IndexEnd = 1;
    private final int forwardIndexStart = 0;
    private final int forwardIndexEnd = 1;
    private final int backIndexStart = 2;
    private final int backIndexEnd = 3;
    private final int leftIndexStart = 4;
    private final int leftIndexEnd = 6;
    private final int rightIndexStart = 7;
    private final int rightIndexEnd = 9;
    private final int actionPeriod = 1100;
    private final int aniPeriod = 100;
    public Debiked(String id, Point pos, List<PImage> images)
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
        Downed fallen = ef.createDDebiked(getId(), deathPoint);
        world.addEntity(fallen);
        scheduler.scheduleActions(fallen, world, ef);
    }

    @Override
    protected void changeToFrontAni() {
        IndexStart = forwardIndexStart;
        IndexEnd = forwardIndexEnd;

    }

    @Override
    protected void changeToBackAni() {
        IndexStart = backIndexStart;
        IndexEnd = backIndexEnd;

    }

    @Override
    protected void changeToLeftAni() {
        IndexStart = leftIndexStart;
        IndexEnd = leftIndexEnd;

    }

    @Override
    protected void changeToRightAni() {
        IndexStart = rightIndexStart;
        IndexEnd = rightIndexEnd;

    }


    @Override
    public void nextImage()
    {
        super.setImageIndex(((super.getImageIndex() + 1) % (IndexEnd - IndexStart)) + IndexStart);
    }
}
