import processing.core.PImage;

import java.util.List;

public class MStud extends NPC
{
    private int IndexStart = 0;
    private int IndexEnd = 1;
    private final int forwardIndexStart = 0;
    private final int forwardIndexEnd = 0;
    private final int backIndexStart = 1;
    private final int backIndexEnd = 1;
    private final int leftIndexStart = 2;
    private final int leftIndexEnd = 4;
    private final int rightIndexStart = 5;
    private final int rightIndexEnd = 7;
    private final int actionPeriod = 1100;
    private final int aniPeriod = 100;
    public MStud(String id, Point pos, List<PImage> images)
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
        Downed fallen = ef.createDMStud(getId(), deathPoint);
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
        if (IndexEnd - IndexStart != 0)
        {
            super.setImageIndex(((super.getImageIndex() + 1) % (IndexEnd - IndexStart)) + IndexStart);
        }
        else
        {
            super.setImageIndex(IndexStart);
        }
    }
}
