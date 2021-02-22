import processing.core.PImage;

import java.util.List;

public abstract class Miner extends Animated {

    private int resourceCount;
    private int resourceLimit;

    public Miner(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceCount = resourceCount;
        this.resourceLimit = resourceLimit;
    }

    public boolean transform(//try to refactor this
                              NonStatic entity,
                              WorldModel world,
                              EventScheduler scheduler,
                              ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit){
            NonStatic miner = Factory.createMinerFull(this.getId(), this.getResourceLimit(),
                    this.getposition(), this.getactionPeriod(),
                    this.getAnimationPeriod(),
                    this.getImages());

            world.removeEntity(entity);
            scheduler.unscheduleAllEvents(entity);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;}
        else{
            NonStatic miner = Factory.createMinerNotFull(this.getId(), this.getResourceLimit(),
                    this.getposition(), this.getactionPeriod(),
                    this.getAnimationPeriod(),
                    this.getImages());

            world.removeEntity(entity);
            scheduler.unscheduleAllEvents(entity);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);
            
            return false;}
        
    }

    protected abstract boolean _minehelper();


    public Point nextPositionMiner(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.getposition().x);
        Point newPos = new Point(this.getposition().x + horiz, this.getposition().y);

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.y - this.getposition().y);
            newPos = new Point(this.getposition().x, this.getposition().y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = this.getposition();
            }
        }

        return newPos;
    }

    public int getResourceLimit() {return this.resourceLimit;}

    public int getResourceCount() { return this.resourceCount;}

    public void setResourceCount(int nes) {this.resourceCount = nes;}



}
