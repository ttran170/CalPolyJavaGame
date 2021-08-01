public interface Active
    //entities with actions
{
    int getActionPeriod();
    void executeActivity(WorldModel world, EntityFactory entityFactory, EventScheduler scheduler);
}
