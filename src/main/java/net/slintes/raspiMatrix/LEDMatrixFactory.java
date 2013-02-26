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

/**
 * Created with IntelliJ IDEA.
 * User: slintes
 * Date: 30.12.12
 * Time: 13:47
 *
 * factory for LED Matrix
 *
 */
public class LEDMatrixFactory {

    /**
     * creates an 8*8 bicolor LED matrix
     *
     * @param busNr the bus nr, 1 on current Pi revision, 0 for older revisions
     * @param address the I2C address of the Adafruit backback (default is 0x0070)
     */
    public static LEDMatrix createLEDMatrix(int busNr, int address){

        LEDMatrix matrix;

        try{
            matrix = new Adafruit8x8LEDMatrix(busNr, address);
        } catch (Throwable t){
            // fails if not on a Raspberry Pi
            matrix = new SysOutLEDMatrix();
        }

        return matrix;
    }
}
