import processing.core.PApplet;
import processing.core.PImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/*
VirtualWorld is our main wrapper
It keeps track of data necessary to use Processing for drawing but also keeps track of the necessary
components to make our world run (eventScheduler), the data in our world (WorldModel) and our
current view (think virtual camera) into that world (WorldView)
 */

public final class VirtualWorld
        extends PApplet
{
   private static final int COLOR_MASK = 0xffffff;
   private static final int KEYED_IMAGE_MIN = 5;
   private static final int KEYED_RED_IDX = 2;
   private static final int KEYED_GREEN_IDX = 3;
   private static final int KEYED_BLUE_IDX = 4;

   private static final String MUSTY_KEY = "musty";
   private static final String BIKER_KEY = "biker";
   private static final String MALE_STUDENT_KEY = "mStud";
   private static final String FEMALE_STUDENT_KEY = "fStud";
   private static final String BUILDING_KEY = "building";

   private static final String OBSTACLE_KEY = "obstacle";
   private static final int NUM_PROPERTIES = 4;
   private static final String BGND_KEY = "background";
   private static final int ID = 1;
   private static final int COL = 2;
   private static final int ROW = 3;

   private static final int PROPERTY_KEY = 0;

   private static final int TIMER_ACTION_PERIOD = 100;

   private static final int VIEW_WIDTH = 1280;
   private static final int VIEW_HEIGHT = 960;
   private static final int TILE_WIDTH = 32;
   private static final int TILE_HEIGHT = 32;
   private static final int WORLD_WIDTH_SCALE = 1;
   private static final int WORLD_HEIGHT_SCALE = 1;

   private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
   private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

   private static final String IMAGE_LIST_FILE_NAME = "C:\\Users\\Home\\IdeaProjects\\Project5\\src\\imagelist";
   //private static final String IMAGE_LIST_FILE_NAME = "imagelist";
   private static final String DEFAULT_IMAGE_NAME = "background_default";
   private static final int DEFAULT_IMAGE_COLOR = 0x808080;

   //private static final String LOAD_FILE_NAME = "world.sav";
   private static final String LOAD_FILE_NAME = "C:\\Users\\Home\\IdeaProjects\\Project5\\src\\world.sav";

   private static final String FAST_FLAG = "-fast";
   private static final String FASTER_FLAG = "-faster";
   private static final String FASTEST_FLAG = "-fastest";
   private static final double FAST_SCALE = 0.5;
   private static final double FASTER_SCALE = 0.25;
   private static final double FASTEST_SCALE = 0.10;

   //default is 1.0
   private static double timeScale = 0.25;

   private ImageStore imageStore;
   private WorldModel world;
   private WorldView view;
   private EventScheduler scheduler;
   private EntityFactory entityFactory;

   private long next_time;

   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup()
   {
      this.imageStore = new ImageStore(
              createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
              createDefaultBackground(imageStore));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
              TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
      this.entityFactory = new EntityFactory(10, imageStore);
      loadWorld(world, LOAD_FILE_NAME, imageStore, entityFactory);

      scheduleActions(world, scheduler, entityFactory);

      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
   }

   public void draw()
   {
      long time = System.currentTimeMillis();
      if (time >= next_time)
      {
         scheduler.updateOnTime(time);
         next_time = time + TIMER_ACTION_PERIOD;
      }

      view.drawViewport();
   }

   public void keyPressed()
   {
      if (key == CODED)
      {
         switch (keyCode)
         {
            case UP:
                  if (!Musty.throwUp(world, entityFactory, scheduler))
                  {
                     entityFactory.checkProjectiles(world);
                  }
               break;
            case DOWN:
               if (!Musty.throwDown(world, entityFactory, scheduler))
               {
                  entityFactory.checkProjectiles(world);
               }
               break;
            case LEFT:
                  if (!Musty.throwLeft(world, entityFactory, scheduler))
                  {
                     entityFactory.checkProjectiles(world);
                  }
               break;
            case RIGHT:
                  if (!Musty.throwRight(world, entityFactory, scheduler))
                  {
                     entityFactory.checkProjectiles(world);
                  }
               break;
         }
      }

      else
      {
         switch (key)
         {
            case 'w':
               if (Musty.exists())
               Musty.moveUp(world);
               break;
            case 's':
               if (Musty.exists())
                  Musty.moveDown(world);
               break;
            case 'a':
               if (Musty.exists())
                  Musty.moveLeft(world);
               break;
            case 'd':
               if (Musty.exists())
                  Musty.moveRight(world);
               break;
            case 'n':
               reset();
               break;
         }
      }

   }

   private void reset()
   {
      world = new WorldModel(WORLD_ROWS, WORLD_COLS, createDefaultBackground(imageStore));
      view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
              TILE_WIDTH, TILE_HEIGHT);
      scheduler = new EventScheduler(timeScale);
      loadWorld(world, LOAD_FILE_NAME, imageStore, entityFactory);

      scheduleActions(world, scheduler, entityFactory);

      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
   }

   private Background createDefaultBackground(ImageStore imageStore)
   {
      return new Background(DEFAULT_IMAGE_NAME,
              imageStore.getImageList(DEFAULT_IMAGE_NAME));
   }

   private PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   private void loadImages(String filename, ImageStore imageStore,
                           PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         int lineNumber = 0;
         while (in.hasNextLine())
         {
            try
            {
               processImageLine(imageStore.getImages(), in.nextLine(), screen);
            }
            catch (NumberFormatException e)
            {
               System.out.println(String.format("Image format error on line %d",
                       lineNumber));
            }
            lineNumber++;
         }
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }


   private void loadWorld(WorldModel world, String filename,
                          ImageStore imageStore, EntityFactory entityFactory)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         load(in, world, imageStore, entityFactory);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   private void scheduleActions(WorldModel world,
                                EventScheduler scheduler, EntityFactory entityFactory)
   {
      for (Entity entity : world.getEntities())
      {
         //Only start actions for entities that include action (not those with just animations)
         if (entity instanceof Active)
            scheduler.scheduleActions(entity, world, entityFactory);
      }
   }

   private static void parseCommandLine(String [] args)
   {
      for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }

   private void processImageLine(Map<String, List<PImage>> images,
                                 String line, PApplet screen)
   {
      String[] attrs = line.split("\\s");
      if (attrs.length >= 2)
      {
         String key = attrs[0];
         PImage img = screen.loadImage(attrs[1]);
         if (img != null && img.width != -1)
         {
            List<PImage> imgs = getImages(images, key);
            imgs.add(img);

            if (attrs.length >= KEYED_IMAGE_MIN)
            {
               int r = Integer.parseInt(attrs[KEYED_RED_IDX]);
               int g = Integer.parseInt(attrs[KEYED_GREEN_IDX]);
               int b = Integer.parseInt(attrs[KEYED_BLUE_IDX]);
               setAlpha(img, screen.color(r, g, b), 0);
            }
         }
      }
   }

   private List<PImage> getImages(Map<String, List<PImage>> images,
                                  String key)
   {
      List<PImage> imgs = images.get(key);
      if (imgs == null)
      {
         imgs = new LinkedList<>();
         images.put(key, imgs);
      }
      return imgs;
   }

   /*
     Called with color for which alpha should be set and alpha value.
     setAlpha(img, color(255, 255, 255), 0));
   */
   private void setAlpha(PImage img, int maskColor, int alpha)
   {
      int alphaValue = alpha << 24;
      int nonAlpha = maskColor & COLOR_MASK;
      img.format = PApplet.ARGB;
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         if ((img.pixels[i] & COLOR_MASK) == nonAlpha)
         {
            img.pixels[i] = alphaValue | nonAlpha;
         }
      }
      img.updatePixels();
   }

   private void load(Scanner in, WorldModel world, ImageStore imageStore, EntityFactory entityFactory)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!processLine(in.nextLine(), world, imageStore, entityFactory))
            {
               System.err.println(String.format("invalid entry on line %d",
                       lineNumber));
            }
         }
         catch (NumberFormatException e)
         {
            System.err.println(String.format("invalid entry on line %d",
                    lineNumber));
         }
         catch (IllegalArgumentException e)
         {
            System.err.println(String.format("issue on line %d: %s",
                    lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }

   private boolean processLine(String line, WorldModel world,
                               ImageStore imageStore, EntityFactory entityFactory)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
            case BGND_KEY:
               return parseBackground(properties, world, imageStore);
            case OBSTACLE_KEY:
               return parseObstacle(properties, world, entityFactory);
            case MUSTY_KEY:
               return parseMusty(properties, world, entityFactory);
            case MALE_STUDENT_KEY:
               return parseMStud(properties, world, entityFactory);
            case FEMALE_STUDENT_KEY:
               return parseFStud(properties, world, entityFactory);
            case BIKER_KEY:
               return parseBiker(properties, world, entityFactory);
            case BUILDING_KEY:
               return parseBuilding(properties, world, entityFactory);
         }
      }

      return false;
   }

   private boolean parseBackground(String [] properties,
                                   WorldModel world, ImageStore imageStore)
   {
      if (properties.length == NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[COL]),
                 Integer.parseInt(properties[ROW]));
         String id = properties[ID];
         world.setBackground(pt, new Background(id, imageStore.getImageList(id)));
      }

      return properties.length == NUM_PROPERTIES;
   }

   private boolean parseMusty(String [] properties, WorldModel world,
                             EntityFactory entityFactory)
   {
      if (properties.length == NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[COL]),
                 Integer.parseInt(properties[ROW]));
         Musty pc = entityFactory.createMusty(properties[ID],pt);
         world.tryAddEntity(pc);
      }

      return properties.length == NUM_PROPERTIES;
   }

   private boolean parseObstacle(String [] properties, WorldModel world,
                                 EntityFactory entityFactory)
   {
      if (properties.length == NUM_PROPERTIES)
      {
         Point pt = new Point(
                 Integer.parseInt(properties[COL]),
                 Integer.parseInt(properties[ROW]));
         Obstacle entity = entityFactory.createObstacle(properties[ID], pt);
         world.tryAddEntity(entity);
      }

      return properties.length == NUM_PROPERTIES;
   }

   private boolean parseBuilding(String [] properties, WorldModel world,
                                 EntityFactory entityFactory)
   {
      if (properties.length == NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[COL]),
                 Integer.parseInt(properties[ROW]));
         Building entity = entityFactory.createBuilding(properties[ID], pt);
         world.tryAddEntity(entity);
      }

      return properties.length == NUM_PROPERTIES;
   }

   private boolean parseBiker(String [] properties, WorldModel world,
                                 EntityFactory entityFactory)
   {
      if (properties.length == NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[COL]),
                 Integer.parseInt(properties[ROW]));
         Entity entity = entityFactory.createBiker(properties[ID], pt);
         world.tryAddEntity(entity);
      }

      return properties.length == NUM_PROPERTIES;
   }

   private boolean parseFStud(String [] properties, WorldModel world,
                              EntityFactory entityFactory)
   {
      if (properties.length == NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[COL]),
                 Integer.parseInt(properties[ROW]));
         Entity entity = entityFactory.createFStud(properties[ID], pt);
         world.tryAddEntity(entity);
      }

      return properties.length == NUM_PROPERTIES;
   }

   private boolean parseMStud(String [] properties, WorldModel world,
                              EntityFactory entityFactory)
   {
      if (properties.length == NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[COL]),
                 Integer.parseInt(properties[ROW]));
         Entity entity = entityFactory.createMStud(properties[ID], pt);
         world.tryAddEntity(entity);
      }

      return properties.length == NUM_PROPERTIES;
   }


   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }

}
