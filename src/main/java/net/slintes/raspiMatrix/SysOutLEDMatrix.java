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
}
