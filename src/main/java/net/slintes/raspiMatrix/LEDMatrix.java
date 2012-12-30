package net.slintes.raspiMatrix;

/**
 * Created with IntelliJ IDEA.
 * User: slintes
 * Date: 30.12.12
 * Time: 13:45
 *
 * interface to a LED Matrix
 * use LEDMatrixFactory for instantiation
 *
 */
public interface LEDMatrix extends LEDBackPack {

    /**
     * enum for possible LED colors
     */
    public enum LedColor {
        OFF, RED, YELLOW, GREEN
    }

    /**
     * sets a pixel in the matrix
     *
     * @param row row number, 0..7
     * @param column column number, 0..7
     * @param color the color
     */
    void setPixel(int row, int column, LedColor color);

}
