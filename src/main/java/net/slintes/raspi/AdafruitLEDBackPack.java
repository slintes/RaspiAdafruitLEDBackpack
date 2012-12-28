package net.slintes.raspi;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: slintes
 * Date: 27.12.12
 * Time: 14:19
 *
 * class for accessing an Adafruit LED Backpack via I2C on a Raspberry Pi
 *
 * for info on the backpack see:
 * http://learn.adafruit.com/adafruit-led-backpack/overview
 * http://learn.adafruit.com/matrix-7-segment-led-backpack-with-the-raspberry-pi/overview
 *
 * code ported from:
 * https://github.com/adafruit/Adafruit-LED-Backpack-Library
 * https://github.com/adafruit/Adafruit-Raspberry-Pi-Python-Code/tree/master/Adafruit_LEDBackpack
 *
 */
public class AdafruitLEDBackPack {

    // Registers
    private static final int HT16K33_REGISTER_DISPLAY_SETUP        = 0x80;
    private static final int HT16K33_REGISTER_SYSTEM_SETUP         = 0x20;
    private static final int HT16K33_REGISTER_DIMMING              = 0xE0;

    // Blink rate
    private static final int HT16K33_BLINKRATE_OFF                 = 0x00;
    private static final int HT16K33_BLINKRATE_2HZ                 = 0x01;
    private static final int HT16K33_BLINKRATE_1HZ                 = 0x02;
    private static final int HT16K33_BLINKRATE_HALFHZ              = 0x03;

    public enum BlinkRate {

        OFF(HT16K33_BLINKRATE_OFF),
        TWO_HZ(HT16K33_BLINKRATE_2HZ),
        ONE_HZ(HT16K33_BLINKRATE_1HZ),
        HALF_HZ(HT16K33_BLINKRATE_HALFHZ);

        int value;
        BlinkRate(int value){
            this.value = value;
        }

    }

    // Display buffer (8x16-bits)
    private static int[] BUFFER = {0, 0, 0, 0, 0, 0, 0, 0};

    private final I2CBus i2CBus;
    private final I2CDevice i2cDevice;

    /**
     * constructs an Adafruit LED Backpack
     *
     * @param busNr the bus nr, 1 on current Pi revision, 0 for older revisions
     * @param address the I2C address of the backback (default is 0x0070)
     *
     * @throws IOException
     */
    public AdafruitLEDBackPack(int busNr, int address) throws IOException {
        i2CBus = I2CFactory.getInstance(busNr);
        i2cDevice = i2CBus.getDevice(address);

        // Turn the oscillator on
        i2cDevice.write(HT16K33_REGISTER_SYSTEM_SETUP | 0x01, (byte)0x00);

        // turn blink rate off
        setBlinkRate(BlinkRate.OFF);

        // set max brightness
        setBrightness(15);

        // clear display
        clear(true);
    }

    public void setBlinkRate(BlinkRate blinkRate) throws IOException {
        i2cDevice.write(HT16K33_REGISTER_DISPLAY_SETUP | 0x01 | (blinkRate.value << 1), (byte)0x00);
    }

    /**
     * sets the brightness
     *
     * @param brightness the brightness, 0..15
     *
     * @throws IOException
     */
    public void setBrightness(int brightness) throws IOException {
        if(brightness < 0) brightness = 0;
        else if (brightness > 15) brightness = 15;
        i2cDevice.write(HT16K33_REGISTER_DIMMING | brightness, (byte)0x00);
    }

    /**
     * clears the buffer
     *
     * @param flush display empty buffer immediately
     *
     * @throws IOException
     */
    public void clear(boolean flush) throws IOException {
        BUFFER = new int[]{0,0,0,0,0,0,0,0};
        if(flush){
            writeDisplay();
        }
    }

    /**
     * write a row to the buffer
     *
     * @param row the row number, 0..7
     * @param value the value
     *
     * @throws IOException
     */
    public void setBufferRow(int row, int value) throws IOException {
        setBufferRow(row, value, false);
    }

    /**
     * write a row to the buffer
     *
     * @param row the row number, 0..7
     * @param value the value
     * @param flush write buffer to display immediately
     *
     * @throws IOException
     */
    public void setBufferRow(int row, int value, boolean flush) throws IOException {

        if(!isBetween0And7(row)) return;
        BUFFER[row] = value;
        if(flush){
            writeDisplay();
        }
    }

    protected boolean isBetween0And7(int x){
        return x >= 0 && x <= 7;
    }

    /**
     * get the current buffer (might not be displayed yet)
     *
     * @return
     */
    public int[] getBuffer(){
        return BUFFER;
    }

    /**
     * writes content of buffer to the LED Backpack
     *
     * @throws IOException
     */
    public void writeDisplay() throws IOException {

        byte[] bytes = new byte[16];
        for (int i = 0; i < 8; i++) {
            bytes[i*2] = (byte)(BUFFER[i] & 0xFF);
            bytes[i*2+1] = (byte)((BUFFER[i] >> 8) & 0xFF);
        }
        i2cDevice.write(0x00, bytes, 0, 16);

    }

}
