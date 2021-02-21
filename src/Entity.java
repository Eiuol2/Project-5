
import processing.core.PImage;

import java.util.List;

public abstract class Entity
{

    private final String id;
    private  Point position;
    private final List<PImage> images;
    private int imageIndex;

    public Entity(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    public void setPosition(Point p)
    {
        this.position = p;
    }

    public Point getposition()
    {
        return this.position;
    }

    public List<PImage> getImages() {return this.images;}

    public String getId() {return this.id;}

    public int getImageIndex() {return this.imageIndex;}

    public void setImageIndex(int aimg) {this.imageIndex = aimg;}

    public PImage getCurrentImage() {
        return ((this.images.get(this.imageIndex)));
    }



}


