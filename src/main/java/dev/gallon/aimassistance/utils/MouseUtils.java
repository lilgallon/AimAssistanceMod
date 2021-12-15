package dev.gallon.aimassistance.utils;

/**
 * Need this class because forge did not implement the mouse move event. They did for the scroll and
 * input events, but not when the mouse moves.
 */
public class MouseUtils {

    private static boolean mouseMoved;

    private static int delay = 2;
    private static double prevX = -1;
    private static double prevY = -1;

    public static void checkForMouseMove() {
        double currX = Wrapper.MC.mouseHandler.xpos();
        double currY = Wrapper.MC.mouseHandler.ypos();

        if (prevX != -1 && prevY != -1) {
            mouseMoved = prevX != currX || prevY != currY;
        }

        if (delay == 0) {
            prevX = currX;
            prevY = currY;
            delay = 2;
        } else {
            delay --;
        }
    }

    public static boolean mouseMoved() {
        return mouseMoved;
    }
}
