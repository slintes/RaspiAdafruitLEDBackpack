package net.slintes.raspiMatrix;

/**
 * Created with IntelliJ IDEA.
 * User: slintes
 * Date: 30.12.12
 * Time: 14:10
 *
 * interface for a Adafruit LED backpack
 *
 */
public interface LEDBackPack {



    /**
     * enum for blink rate
     */
    public enum BlinkRate {

        DISPLAY_OFF,
        BLINK_OFF,
        TWO_HZ,
        ONE_HZ,
        HALF_HZ

    }

    /**
     * sets the blinkrate
     *
     * @param blinkRate the blinkrate
     */
    void setBlinkRate(BlinkRate blinkRate);

    /**
     * sets the brightness
     *
     * @param brightness the brightness, 0..15
     */
    void setBrightness(int brightness);

    /**
     * clears the buffer
     *
     * @param flush display empty buffer immediately
     */
    void clear(boolean flush);

    /**
     * writes content of buffer to the LED Backpack
     */
    void writeDisplay();
}
