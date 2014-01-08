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

    @Override
    public void writeString(String text, int durationPerChar, boolean doScroll) {
        if(doScroll){
            writeStringScroll(text, durationPerChar);
        }
        else {
            writeStringNoScroll(text, durationPerChar);
        }
    }

    private void writeStringNoScroll(String text, int durationPerChar) {

        clear(true);

        for(char c : text.toCharArray()){

            int i = c;
            i -= 31; // offset in Font8x8

            int[] buffer = Font8x8.FONT8x8[i];

            int row = 0;
            for(int bufferRow : buffer){
                // fit it to our hardware setup (pins of LED matrix to the left
                bufferRow = Integer.reverse(bufferRow);
                bufferRow = Integer.reverseBytes(bufferRow);
                setBufferRow(row++, bufferRow);
            }

            writeDisplay();

            try {
                Thread.sleep(durationPerChar);
            } catch (InterruptedException e) {}


            // make flash the chars, so you can see double chars
            clear(true);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {}

        }

        clear(true);

    }

    private void writeStringScroll(String text, int durationPerChar) {

        clear(true);

        // add a space before and after text
        text = " " + text + " ";

        for(int pos = 0; pos < text.length()-1; pos++){

            // get buffer for current and next char
            int i1 = text.charAt(pos) - 31;
            int[] buffer1 = Font8x8.FONT8x8[i1];

            int i2 = text.charAt(pos+1) -31;
            int[] buffer2 = Font8x8.FONT8x8[i2];

            // we will scroll through the 8 LED columns
            for(int col = 0; col < 8; col++){
                for(int row = 0; row < 8; row++){

                    // fit it to our hardware setup (pins of LED matrix to the left
                    int bufferRow1 = Integer.reverse(buffer1[row]);
                    bufferRow1 = Integer.reverseBytes(bufferRow1);

                    int bufferRow2 = Integer.reverse(buffer2[row]);
                    bufferRow2 = Integer.reverseBytes(bufferRow2);

                    // shift current row by col
                    bufferRow1 = bufferRow1 >>> col;
                    // shift row of next char by 8-col
                    bufferRow2 = bufferRow2 << (8-col);

                    // now combine them, but only use lower 8 bits
                    int bufferRow = 0xFF & (bufferRow1 | bufferRow2);
                    setBufferRow(row, bufferRow);
                    writeDisplay();

                }

                try {
                    Thread.sleep(durationPerChar / 8);
                } catch (InterruptedException e) {}

            }
        }

        clear(true);

    }

}