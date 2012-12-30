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
