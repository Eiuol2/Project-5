import java.util.List;

import processing.core.PImage;

public final class Background
{
    private final List<PImage> images;
    private int imageIndex;

    public Background(String id, List<PImage> images) {
        this.images = images;
    }

    public PImage getCurrentImage() {
            return (this.images.get(this.imageIndex));
        }

}
