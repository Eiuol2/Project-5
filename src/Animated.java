public interface Animated extends NonStatic{

    int getAnimationPeriod();

    void nextImage();

    Action createAnimationAction(int repeatCount);



}
