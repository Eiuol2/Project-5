import processing.core.PImage;

import java.util.List;

public abstract class Animated extends NonStatic {

    private int resourceLimit;
    private int resourceCount;
    private int animationPeriod;

    public Animated(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.animationPeriod = animationPeriod;
    }
    public int getResourceLimit() {return this.resourceLimit;}

    public int getResourceCount() { return this.resourceCount;}

    public void setResourceCount(int nes) {this.resourceCount = nes;}


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
