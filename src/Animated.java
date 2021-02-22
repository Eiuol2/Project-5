import processing.core.PImage;

import java.util.List;

public abstract class Animated extends NonStatic {//smaller than quake miner blob bigger than miners check for common methods

    private int animationPeriod;

    public Animated(String id, Point position, List<PImage> images,int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
    }


    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.getactionPeriod());
                scheduler.scheduleEvent(this,
                        this.createAnimationAction(0), //put into animated
                    this.getAnimationPeriod());}



    public void nextImage() {
        super.setImageIndex(((this.getImageIndex() + 1) % this.getImages().size()));
    }

    public Action createAnimationAction(int repeatCount) {
        return new Animation(this, null,
                repeatCount);
    }

    public int getAnimationPeriod() {
        return this.animationPeriod;




    }
}
