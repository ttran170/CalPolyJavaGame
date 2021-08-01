public class EntityFactory
{
    private final int MAXPROJECTILES;
    private static final String OBSTACLE_KEY = "obstacle";
    private static final String MUSTY_KEY = "musty";
    private static final String DOWNED_MUSTY_KEY = "fallenMusty";
    private static final String BIKER_KEY = "biker";
    private static final String DEBIKED_BIKER_KEY = "debiked";
    private static final String MALE_STUDENT_KEY = "mStud";
    private static final String FEMALE_STUDENT_KEY = "fStud";
    private static final String BUILDING_KEY = "building";
    private static final String PROJ_KEY = "projectile";
    private static final String DOWNED_DEBIKED_BIKER_KEY = "fallenDebiked";
    private static final String DOWNED_MALE_STUDENT_KEY = "fallenMStud";
    private static final String DOWNED_FEMALE_STUDENT_KEY = "fallenFStud";
    private static final ProjectilePathingStrategy RPPS = new RightProjectilePathingStrategy();
    private static final ProjectilePathingStrategy UPPS = new UpProjectilePathingStrategy();
    private static final ProjectilePathingStrategy DPPS = new DownProjectilePathingStrategy();
    private static final ProjectilePathingStrategy LPPS = new LeftProjectilePathingStrategy();
    private int projCount;
    private ImageStore imageStore;
    public EntityFactory(int maxProjectiles, ImageStore imageStore)
    {
        this.imageStore = imageStore;
        MAXPROJECTILES = maxProjectiles;
    }

    public void checkProjectiles(WorldModel world)
    {
        int projectileCount = 0;
        for (Entity entity : world.getEntities())
        {
            if (entity.getClass() == Projectile.class)
            {
                projectileCount++;
            }
        }
        projCount = projectileCount;
    }

    public void decrProjCount()
    {
        projCount--;
    }

    public Obstacle createObstacle(String id, Point pos)
    {
        return new  Obstacle(id, pos, imageStore.getImageList(OBSTACLE_KEY));
    }

    public Musty createMusty(String id, Point pos)
    {
        return Musty.getMusty(id, pos, imageStore.getImageList(MUSTY_KEY));
    }

    public DownedMusty createDownedMusty(String id, Point pos)
    {
        return new DownedMusty(id, pos, imageStore.getImageList(DOWNED_MUSTY_KEY));
    }

    public Biker createBiker(String id, Point pos)
    {
        return new Biker(id, pos, imageStore.getImageList(BIKER_KEY));
    }

    public Debiked createDebiked(String id, Point pos)
    {
        return new Debiked(id, pos, imageStore.getImageList(DEBIKED_BIKER_KEY));
    }

    public MStud createMStud(String id, Point pos)
    {
        return new MStud(id, pos, imageStore.getImageList(MALE_STUDENT_KEY));
    }

    public FStud createFStud(String id, Point pos)
    {
        return new FStud(id, pos, imageStore.getImageList(FEMALE_STUDENT_KEY));
    }

    public Building createBuilding(String id, Point pos)
    {
        return new Building(id, pos, imageStore.getImageList(BUILDING_KEY));
    }

    public Projectile createUpProj(String id, Point pos)
    {
        projCount++;
        if (projCount >= MAXPROJECTILES)
        {
            return null;
        }
        return new Projectile(id, pos, imageStore.getImageList(PROJ_KEY), UPPS);
    }

    public Projectile createRightProj(String id, Point pos)
    {
        projCount++;
        if (projCount >= MAXPROJECTILES)
        {
            return null;
        }
        return new Projectile(id, pos, imageStore.getImageList(PROJ_KEY), RPPS);
    }

    public Projectile createLeftProj(String id, Point pos)
    {
        projCount++;
        if (projCount >= MAXPROJECTILES)
        {
            return null;
        }
        return new Projectile(id, pos, imageStore.getImageList(PROJ_KEY), LPPS);
    }

    public Projectile createDownProj(String id, Point pos)
    {
        projCount++;
        if (projCount >= MAXPROJECTILES)
        {
            return null;
        }
        return new Projectile(id, pos, imageStore.getImageList(PROJ_KEY), DPPS);
    }

    public DownedDebiked createDDebiked(String id, Point pos)
    {
        return new DownedDebiked(id, pos, imageStore.getImageList(DOWNED_DEBIKED_BIKER_KEY));
    }

    public  DownedMStud createDMStud(String id, Point pos)
    {
        return new DownedMStud(id, pos, imageStore.getImageList(DOWNED_MALE_STUDENT_KEY));
    }

    public DownedFStud createDFStud(String id, Point pos)
    {
        return new DownedFStud(id, pos, imageStore.getImageList(DOWNED_FEMALE_STUDENT_KEY));
    }
}
