import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public interface Entity
{
    void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore);


    Point getposition();

    void setPosition(Point p);

    PImage getCurrentImage();


}


