/*
 * Copyright 2013 Marc Sluiter
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.slintes.raspiMatrix;

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
public class AdafruitLEDBackPack implements LEDBackPack {

    // Registers
    private static final int HT16K33_REGISTER_DISPLAY_SETUP        = 0x80;
    private static final int HT16K33_REGISTER_SYSTEM_SETUP         = 0x20;
    private static final int HT16K33_REGISTER_DIMMING              = 0xE0;

    // Blink rate
    private static final int HT16K33_BLINKRATE_DISPLAY_OFF         = 0x00;
    private static final int HT16K33_BLINKRATE_OFF                 = 0x01;
    private static final int HT16K33_BLINKRATE_2HZ                 = 0x03;
    private static final int HT16K33_BLINKRATE_1HZ                 = 0x05;
    private static final int HT16K33_BLINKRATE_HALFHZ              = 0x07;

    // Display buffer (8x16-bits)
    private int[] BUFFER = {0, 0, 0, 0, 0, 0, 0, 0};

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
        i2cDevice.write((byte) (HT16K33_REGISTER_SYSTEM_SETUP | 0x01));

        // turn display on and blink rate off
        setBlinkRate(BlinkRate.BLINK_OFF);

        // set max brightness
        setBrightness(15);

        // clear display
        clear(true);
    }

    @Override
    public void setBlinkRate(BlinkRate blinkRate) {

        int blinkrateValue;
        switch (blinkRate){
            case DISPLAY_OFF: blinkrateValue = HT16K33_BLINKRATE_DISPLAY_OFF; break;
            case BLINK_OFF: blinkrateValue = HT16K33_BLINKRATE_OFF; break;
            case HALF_HZ: blinkrateValue = HT16K33_BLINKRATE_HALFHZ; break;
            case ONE_HZ: blinkrateValue = HT16K33_BLINKRATE_1HZ; break;
            case TWO_HZ: blinkrateValue = HT16K33_BLINKRATE_2HZ; break;
            default: blinkrateValue = HT16K33_BLINKRATE_OFF;
        }
        try {
            i2cDevice.write((byte) (HT16K33_REGISTER_DISPLAY_SETUP | blinkrateValue));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setBrightness(int brightness) {
        if(brightness < 0) brightness = 0;
        else if (brightness > 15) brightness = 15;
        try {
            i2cDevice.write((byte) (HT16K33_REGISTER_DIMMING | brightness));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear(boolean flush) {
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
     */
    protected void setBufferRow(int row, int value) {
        setBufferRow(row, value, false);
    }

    /**
     * write a row to the buffer
     *
     * @param row the row number, 0..7
     * @param value the value
     * @param flush write buffer to display immediately
     */
    protected void setBufferRow(int row, int value, boolean flush) {

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
     * @return current buffer
     */
    protected int[] getBuffer(){
        return BUFFER;
    }

    @Override
    public void writeDisplay() {

        byte[] bytes = new byte[16]; // we need 2 bytes for each row
        for (int i = 0; i < 8; i++) { // iterate rows
            bytes[i*2] = (byte)(BUFFER[i] & 0xFF); // lower byte for green LED
            bytes[i*2+1] = (byte)((BUFFER[i] >> 8) & 0xFF); // higher byte for red LED
        }
        try {
            i2cDevice.write(0x00, bytes, 0, 16);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void finalize() throws Throwable {
        clear(true);
        super.finalize();
    }

}
