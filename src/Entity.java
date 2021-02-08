import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public interface Entity
{
    void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore);

    void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler);

    int getAnimationPeriod();

    void nextImage();

    Action createAnimationAction(int repeatCount);

    Point getposition();

    void setPosition(Point p);

    PImage getCurrentImage();


}


