import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public interface Entity
{


    Point getposition();

    void setPosition(Point p);

    PImage getCurrentImage();


}


