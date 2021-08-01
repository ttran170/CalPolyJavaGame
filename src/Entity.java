import processing.core.PImage;

import java.util.List;

public abstract class Entity
{
    private String id;
    private List<PImage> images;
    private int imageIndex;
    private Point position;

    public Entity(String id, Point position, List<PImage> images)
    {
        this.id = id;
        this.images = images;
        this.position = position;
        this.imageIndex = 0;
    }

    public String getId()
    {
        return id;
    }

    public List<PImage> getImages()
    {
        return images;
    }

    public Point getPosition()
    {
        return position;
    }


    public void setPosition(Point p)
    {
        position = p;
    }

    public int getImageIndex()
    {
        return imageIndex;
    }

    public PImage getCurrentImage()
    {
        return (this.images.get(this.imageIndex));
    }

    protected void setImageIndex(int imageIndex)
    {
        this.imageIndex = imageIndex;
    }
}
