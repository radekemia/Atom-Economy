package committee.nova.atom.eco.utils.math;

import net.minecraft.client.gui.screen.Screen;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/8 9:44
 * Version: 1.0
 */
public class MathUtil {
    /**
     * Restricts an integer between a min & max value
     *
     * @param value
     * @param min
     * @param max
     * @return
     */
    public static int clamp(int value, int min, int max) {
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }
        if (value < min)
            value = min;
        else if (value > max)
            value = max;
        return value;
    }

    public static int scaleInt(int value, int maxValue, int maxScale) {
        float f = value * (float) maxScale / maxValue;
        return (int) f;
    }

    public static int getShiftCtrlInt(int defaultInt, int shiftInt, int ctrlInt, int bothInt) {

        int i = defaultInt;

        boolean s = Screen.hasShiftDown();
        boolean c = Screen.hasControlDown();

        if (s) i = shiftInt;
        if (c) i = ctrlInt;
        if (s && c) i = bothInt;

        return i;
    }
}
