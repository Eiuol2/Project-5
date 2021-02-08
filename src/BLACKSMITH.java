import processing.core.PImage;

import java.util.List;

public class BLACKSMITH implements Entity  {

    private  Point position;
    private final List<PImage> images;
    private int imageIndex;



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
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod)
    {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }



    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public Action createAnimationAction(int repeatCount) {
        return new Animation( this, null, null,
                repeatCount);
    }


    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

    }

    public int getAnimationPeriod() {
                throw new UnsupportedOperationException(
                        String.format("getAnimationPeriod not supported for %s",
                                this.getClass()));
        }

    public PImage getCurrentImage() {
        return ((this.images.get(this.imageIndex)));
    }


    }

