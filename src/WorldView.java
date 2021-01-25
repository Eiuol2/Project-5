import processing.core.PApplet;
import java.util.Optional;
import processing.core.PImage;


public final class WorldView {
    private final PApplet screen;
    private final WorldModel world;
    private final int tileWidth;
    private final int tileHeight;
    private final Viewport viewport;

    public WorldView(
            int numRows,
            int numCols,
            PApplet screen,
            WorldModel world,
            int tileWidth,
            int tileHeight) {
        this.screen = screen;
        this.world = world;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.viewport = new Viewport(numRows, numCols);
    }

    public void shiftView(int colDelta, int rowDelta) {
        int newCol = clamp(this.viewport.getCol() + colDelta, 0,
                this.world.getNumCols() - this.viewport.getnumCols());
        int newRow = clamp(this.viewport.getRow() + rowDelta, 0,
                this.world.getNumRows() - this.viewport.getnumRows());

        this.viewport.shift( newCol, newRow);
    }

    public int clamp(int value, int low, int high) {
        return Math.min(high, Math.max(value, low));
    }

    public void drawEntities() {
        for (Entity entity : this.world.getEntities()) {
            Point pos = entity.getposition();

            if (this.viewport.contains(pos)) {
                Point viewPoint = this.viewport.worldToViewport(pos.x, pos.y);
                this.screen.image(Functions.getCurrentImage(entity),
                        viewPoint.x * this.tileWidth,
                        viewPoint.y * this.tileHeight);
            }
        }
    }

    public void drawBackground() {
        for (int row = 0; row < this.viewport.getnumRows(); row++) {
            for (int col = 0; col < this.viewport.getnumCols(); col++) {
                Point worldPoint = viewport.viewportToWorld(col, row);
                Optional<PImage> image =
                        world.getBackgroundImage(worldPoint);
                if (image.isPresent()) {
                    this.screen.image(image.get(), col * this.tileWidth,
                            row * this.tileHeight);
                }
            }
        }
    }



    public void drawViewport() {
        drawBackground();
        drawEntities();
    }


}