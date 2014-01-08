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
 * Time: 13:48
 *
 * LED matrix implementation just using System.out
 *
 */
public class SysOutLEDMatrix implements LEDMatrix, LEDBackPack {

    SysOutLEDMatrix(){}

    @Override
    public void setPixel(int row, int column, LedColor color) {
        System.out.println("matrix: row=" + row + ", col=" + column + ", color=" + color.toString());
    }

    @Override
    public void setBlinkRate(BlinkRate blinkRate) {
        System.out.println("matrix: blinkrate " + blinkRate.toString());
    }

    @Override
    public void setBrightness(int brightness) {
        System.out.println("matrix: brightness " + brightness);
    }

    @Override
    public void clear(boolean flush) {
        System.out.println("matrix: clear");
    }

    @Override
    public void writeDisplay() {
        System.out.println("matrix: writeDisplay");
    }

    @Override
    public void writeString(String text, int durationPerChar, boolean doScroll) {
        System.out.println("matrix: writeText: " + text);
    }
}
