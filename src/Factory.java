import processing.core.PImage;

import java.util.List;


public final class Factory {

    public static final String QUAKE_ID = "quake";
    public static final int QUAKE_ACTION_PERIOD = 1100;
    public static final int QUAKE_ANIMATION_PERIOD = 100;



    public static Entity createBlacksmith(
            String id, Point position, List<PImage> images)
    {

        return new BLACKSMITH(id, position, images);
    }


    public static NonStatic createMinerFull(
            String id,
            int resourceLimit,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {

        return new MINER_FULL(id, position, images, resourceLimit, actionPeriod, animationPeriod);
    }



    public static NonStatic createMinerNotFull(
            String id,
            int resourceLimit,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {

        return new MINER_NOT_FULL(id, position, images, resourceLimit, 0, actionPeriod, animationPeriod);
    }


    public static Entity createObstacle(
            String id, Point position, List<PImage> images)
    {

        return new OBSTACLE(id, position, images);
    }

    public static Entity createOre(
            String id, Point position, int actionPeriod, List<PImage> images)
    {

        return new ORE(id, position, images, actionPeriod, 0);
    }

    public static NonStatic createOreBlob(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {

        return new ORE_BLOB(id, position, images, actionPeriod, animationPeriod);
    }

    public static NonStatic createQuake(
            Point position, List<PImage> images)
    {

        return new QUAKE(position, images, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }

    public static Entity createVein(
            String id, Point position, int actionPeriod, List<PImage> images)
    {

        return new VEIN(id, position, images, actionPeriod);
    }




}
