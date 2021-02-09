import processing.core.PImage;

import java.util.List;

public class BLACKSMITH implements Entity  {

    private  Point position;
    private final List<PImage> images;
    private final int imageIndex;



    public void setPosition(Point p)
    {
        this.position = p;
    }

    public Point getposition()
    {
        return this.position;
    }




    public BLACKSMITH(
            String id,
            Point position,
            List<PImage> images)
    {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }




    public PImage getCurrentImage() {
        return ((this.images.get(this.imageIndex)));
    }


    }

