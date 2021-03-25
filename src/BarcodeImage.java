// early "skeleton class"
// Documentation will come later 
public class BarcodeImage implements Cloneable {
    // Data
    public static final int MAX_HEIGHT = 30; 
    public static final int MAX_WIDTH = 65; 

    private boolean[][] imageData;

    // Methods
    // Default Constructor -  instantiates a 2D array (MAX_HEIGHT x MAX_WIDTH) and stuffs it all with blanks (false).
    BarcodeImage() {
        imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
        // TODO Will have to fill this with 'false' values next
    }

    // BarcodeImage(String[] strData) -takes a 1D array of Strings and converts it to the internal 2D array of booleans. 
    BarcodeImage(String[] strData) {
        // TODO
    } 

    /**
     * Accessor and mutator for each bit in the image:  
     * boolean getPixel(int row, int col) and 
     * boolean setPixel(int row, int col, boolean value);   For the getPixel(), 
     * you can use the return value for both the actual data and also the error 
     * condition -- so that we don't "create a scene" if there is an error; 
     * we just return false.
     */
    boolean getPixel(int row, int col) {
        return true; // TODO
    }

    boolean setPixel(int row, int col, boolean value) {
        return true; // TODO
    }

    // A clone() method that overrides the method of that name in Cloneable interface. \
    @Override 
    public BarcodeImage clone() throws CloneNotSupportedException {
        return new BarcodeImage(); // TODO
    }

}