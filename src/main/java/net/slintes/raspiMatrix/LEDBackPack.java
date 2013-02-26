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
