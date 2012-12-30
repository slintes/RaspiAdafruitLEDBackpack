package net.slintes.raspiMatrix;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: slintes
 * Date: 27.12.12
 * Time: 16:46
 *
 * extends the AdafruitLEDBackPack class with some methods specific to the 8*8 bicolor LED matrix
 *
 * code ported from:
 * https://github.com/adafruit/Adafruit-LED-Backpack-Library
 * https://github.com/adafruit/Adafruit-Raspberry-Pi-Python-Code/tree/master/Adafruit_LEDBackpack
 *
 */
public class Adafruit8x8LEDMatrix extends AdafruitLEDBackPack implements LEDMatrix, LEDBackPack {

    /**
     * construct an 8*8 bicolor LED matrix
     *
     * @param busNr the bus nr, 1 on current Pi revision, 0 for older revisions
     * @param address the I2C address of the Adafruit backback (default is 0x0070)
     *
     * @throws IOException
     */
    Adafruit8x8LEDMatrix(int busNr, int address) throws IOException {
        super(busNr, address);
    }

    @Override
    public void setPixel(int row, int column, LedColor color) {

        if(!isBetween0And7(row) || !isBetween0And7(column) || color == null){
            return;
        }

        int[] buffer = getBuffer();
        int oldRow = buffer[row];
        if (color == LedColor.GREEN) {
            setBufferRow(row, oldRow | 1 << column); // lower byte is for green LED
        } else if (color == LedColor.RED) {
            setBufferRow(row, oldRow | 1 << (column + 8)); // higher byte is for red LED
        } else if (color == LedColor.YELLOW) {
            setBufferRow(row, oldRow | (1 << (column + 8) | (1 << column))); // both LEDs = yellow
        } else if (color == LedColor.OFF) {
            setBufferRow(row, oldRow & ~(1 << column) & ~(1 << (column + 8))); // switch off both
        }

    }
}
