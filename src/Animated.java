public abstract class Animated extends NonStatic{

    abstract int getAnimationPeriod();

    abstract void nextImage();

    abstract Action createAnimationAction(int repeatCount);



}
