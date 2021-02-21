public interface Animated extends NonStatic{

    int getAnimationPeriod();


    Action createAnimationAction(int repeatCount);


}
