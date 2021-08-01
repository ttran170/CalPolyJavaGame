public class ActivityAction implements Action
{
    private Active entity;
    private WorldModel world;
    private EntityFactory entityFactory;



    public ActivityAction(Active entity, WorldModel world,
                  EntityFactory entityFactory)
    {
        this.entity = entity;
        this.world = world;
        this.entityFactory = entityFactory;
    }

    public void executeAction(EventScheduler scheduler)
    {
        entity.executeActivity(world, entityFactory, scheduler);
    }
}
