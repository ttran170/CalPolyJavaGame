import processing.core.PImage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/*
WorldModel ideally keeps track of the actual size of our grid world and what is in that world
in terms of entities and background elements
 */

final class WorldModel
{
   private int numRows;
   private int numCols;
   private Background background[][];
   private Entity occupancy[][];
   private Set<Entity> entities;



   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

   public int getNumRows()
   {
       return numRows;
   }

    public int getNumCols() {
        return numCols;
    }

    public Set<Entity> getEntities()
    {
        return entities;
    }

    public void tryAddEntity(Entity entity)
   {
      if (isOccupied(entity.getPosition()))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }

      addEntity(entity);
   }

   public boolean withinBounds(Point pos)
   {
      return pos.getY() >= 0 && pos.getY() < this.numRows &&
              pos.getX() >= 0 && pos.getX() < this.numCols;
   }

   public boolean isOccupied(Point pos)
   {
      return withinBounds(pos) &&
              getOccupancyCell(pos) != null;
   }

   /*
   Assumes that there is no entity currently occupying the
   intended destination cell.
   */

   public void addEntity(Entity entity)
   {
      if (withinBounds(entity.getPosition()))
      {
         setOccupancyCell(entity.getPosition(), entity);
         this.entities.add(entity);
      }
   }

    public void removeEntity(Entity entity)
    {
        removeEntityAt(entity.getPosition());
    }

    public void removeEntityAt(Point pos)
    {
        if (this.withinBounds(pos)
                && getOccupancyCell(pos) != null)
        {
            Entity entity = getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
            entity.setPosition(new Point(-1, -1));
            this.entities.remove(entity);
            setOccupancyCell(pos, null);
        }
    }

    public Optional<PImage> getBackgroundImage(Point pos)
    {
        if (withinBounds(pos))
        {
            return Optional.of((getBackgroundCell(pos)).getCurrentImage());
        }
        else
        {
            return Optional.empty();
        }
    }

    public void setBackground(Point pos, Background background)
    {
        if (withinBounds(pos))
        {
            setBackgroundCell(pos, background);
        }
    }

    public Optional<Entity> getOccupant(Point pos)
    {
        if (isOccupied(pos))
        {
            return Optional.of(getOccupancyCell(pos));
        }
        else
        {
            return Optional.empty();
        }
    }

    private Entity getOccupancyCell(Point pos)
    {
        return this.occupancy[pos.getY()][pos.getX()];
    }

    public void setOccupancyCell(Point pos, Entity entity)
    {
        this.occupancy[pos.getY()][pos.getX()] = entity;
    }

    public Background getBackgroundCell(Point pos)
    {
        return this.background[pos.getY()][pos.getX()];
    }

    private void setBackgroundCell(Point pos, Background background)
    {
       this.background[pos.getY()][pos.getX()] = background;
    }


}
