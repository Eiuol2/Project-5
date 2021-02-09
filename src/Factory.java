import processing.core.PImage;

import java.util.List;


public final class Factory {

    public static final String QUAKE_ID = "quake";
    public static final int QUAKE_ACTION_PERIOD = 1100;
    public static final int QUAKE_ANIMATION_PERIOD = 100;



    public static Entity createBlacksmith(
            String id, Point position, List<PImage> images)
    {

        BLACKSMITH b = new BLACKSMITH(id, position, images, 0, 0, 0, 0);
        Entity temp = (Entity)b;

        return temp;
    }


    public static Entity createMinerFull(
            String id,
            int resourceLimit,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {

        MINER_FULL miner_full = new MINER_FULL(id, position, images, resourceLimit, resourceLimit, actionPeriod, animationPeriod);
        Entity temp = (Entity)miner_full;

        return temp;
    }



    public static Entity createMinerNotFull(
            String id,
            int resourceLimit,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {

        MINER_NOT_FULL asd = new MINER_NOT_FULL(id, position, images, resourceLimit, 0, actionPeriod, animationPeriod);
        Entity temp = (Entity)asd;
        return temp;
    }


    public static Entity createObstacle(
            String id, Point position, List<PImage> images)
    {
        OBSTACLE temp = new OBSTACLE(id, position, images, 0, 0, 0, 0);
        Entity as = (Entity)temp;

        return as;
    }

    public static Entity createOre(
            String id, Point position, int actionPeriod, List<PImage> images)
    {

        ORE as = new ORE(id, position, images, 0, 0, actionPeriod, 0);
        return as;
    }

    public static Entity createOreBlob(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {

        ORE_BLOB asd = new ORE_BLOB(id, position, images, 0, 0, actionPeriod, animationPeriod);
        Entity temp = (Entity)asd;
        return asd;
    }

    public static Entity createQuake(
            Point position, List<PImage> images)
    {
        QUAKE asd = new QUAKE(QUAKE_ID, position, images, 0, 0, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
        Entity temp = (Entity)asd;

        return temp;
    }

    public static Entity createVein(
            String id, Point position, int actionPeriod, List<PImage> images)
    {
        VEIN asd = new VEIN(id, position, images, 0, 0, actionPeriod, 0);
        Entity temp = (Entity)asd;

        return asd;
    }




}
