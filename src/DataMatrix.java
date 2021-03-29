/**
 * Lab 4: Optical Barcode
 * CST 338: Software Design (Spring B 2021)
 *
 * Takes a barcode image as input (in string format)
 * for analysis and decoding. Can also create a
 * barcode from user input. Uses a pseudo-datamatrix
 * data structure to hold barcode information.
 *
 * @author Katherine Vickstrom, Deen Altawil, Mike Limpus, Michael Hackett
 */
public class DataMatrix implements BarcodeIO {
   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';
   public static final int MAX_BARCODE_HEIGHT = 10;
   private BarcodeImage image;
   private String text;
   private int actualWidth, actualHeight;

   //Constructors
   public DataMatrix() {
      image = new BarcodeImage();
      text = "";
      actualWidth = actualHeight = 0;
   }

   public DataMatrix(BarcodeImage image) {
      text = "";
      actualWidth = actualHeight = 0;
      scan(image);
   }

   public DataMatrix(String text) {
      image = new BarcodeImage();
      actualWidth = actualHeight = 0;
      readText(text);
   }

    //Public methods
    /**
     * Generates a new BarcodeImage from internally
     * stored text.
     *
     * @return false if the text is empty
     */
   @Override
   public boolean generateImageFromText() {
      clearImage();
      char[] textCharArray = text.toCharArray();
      
      if(textCharArray.length > 0) {
         //Write text to image
         for(int i = 0; i < textCharArray.length; i++) {
            int charValue = (int)textCharArray[i];
            writeCharToCol(i + 1, charValue);
         }
         writeLimitationLines(textCharArray);
         //Set image width and height
         actualWidth = computeSignalWidth();
         actualHeight = computeSignalHeight();
         return true;
      }
      else
         return false;
   }

    /**
     * Decodes and stores the message from the internally
     * stored BarcodeImage, if one exists.
     *
     * @return true if the image is successfully stored as text
     */
   @Override
   public boolean translateImageToText() {
       if (image == null || getActualWidth() == 0 || getActualHeight() == 0)
           return false;
       double[] encodings = getEncodings();
       String decodedMessage = decodeMessage(encodings);
       return readText(decodedMessage);
   }

    /**
     * Prints the internally stored BarcodeImage to the console using
     * a dot-matrix of blanks and asterisks. Clips out only the relevant
     * portion of the image (the signal) and surrounds it in a border.
     */
    @Override
    public void displayImageToConsole() {
        for (int i = 0; i < getActualWidth()+2; i++)
            System.out.print('-');
        System.out.println();
        //Determine starting row for iteration
        int startingRow = BarcodeImage.MAX_HEIGHT - getActualHeight();
        for (int i = startingRow; i < BarcodeImage.MAX_HEIGHT; i++) {
            System.out.print('|');
            //Horizontal iteration
            for (int j = 0; j < getActualWidth(); j++) {
                System.out.print((image.getPixel(i, j) ?
                        BLACK_CHAR : WHITE_CHAR));
            }
            System.out.println('|');
        }
        for (int i = 0; i < getActualWidth()+2; i++)
            System.out.print('-');
        System.out.println();
    }

    /**
     * Prints the internally stored text to the console.
     */
    @Override
    public void displayTextToConsole() {
        System.out.println(text);
    }

    //Mutators
    /**
     * Accepts, cleans, and stores a clone
     * of a barcode image.
     *
     * @param bc The barcode image to be scanned
     * @return false if clone() not supported
     */
    @Override
    public boolean scan(BarcodeImage bc) {
        try {
            image = (BarcodeImage) bc.clone();
            cleanImage();
            actualHeight = computeSignalHeight();
            actualWidth = computeSignalWidth();
            return true;
        }
        catch(CloneNotSupportedException e) {
            return false;
        }
    }

    /**
     * A mutator for text.
     *
     * @param text The string to be copied.
     * @return false if the string is empty or too long
     */
    @Override
    public boolean readText(String text) {
        if (text == null || text.equals("") ||
                text.length() > (BarcodeImage.MAX_WIDTH-2)) return false;
        else {
            this.text = text;
            return true;
        }
    }

    //Accessors
    /**
     * Returns the width of the internally-stored BarcodeImage's signal.
     */
    public int getActualWidth() { return actualWidth; }
    /**
     * Returns the height of the internally-stored BarcodeImage's signal.
     */
    public int getActualHeight() { return actualHeight; }

    //Private helper methods
    /**
     * Retrieves the encodings from a BarcodeImage. Iterates vertically
     * through the columns to calculate and store the binary encodings.
     * An array of doubles accommodates the Math.pow() method.
     *
     * @return An array of doubles representing ASCII characters.
     */
   private double[] getEncodings() {
       double[] encodings = new double[getActualWidth()-2];
       double base = 2, power = 0;
       int arrayTracker = 0, endRow = 
             BarcodeImage.MAX_HEIGHT-getActualHeight();
       /*
          Iterate vertically starting from the bottom left of the image.
          Implements a binary to decimal algorithm.
       */
       for (int i = 1; i < getActualWidth()-1; i++) {
           for (int j = BarcodeImage.MAX_HEIGHT-2; j > endRow; j--) {
               if (image.getPixel(j,i)) {
                   encodings[arrayTracker]+=(Math.pow(base,power));
               }
               power++;
           }
           arrayTracker++;
           power = 0;
       }
       return encodings;
   }

    /**
     * Converts the array of encoded doubles to a string.
     * Each double is downcast to a char before being concatenated.
     *
     * @param encodings An array of doubles representing ASCII characters
     * @return The decoded message as a string
     */
   private String decodeMessage(double[] encodings) {
       String decodedMessage = "";
       for (int i = 0; i < encodings.length; i++)
           decodedMessage+=Character.toString((char) encodings[i]);
       return decodedMessage;
   }

    /**
     * Ensures the internal BarcodeImage's signal is lower-left justified.
     * Beginning at the bottom left of the image, searches for the signal's
     * bottom left pixel and submits its coordinates to a helper method
     * for movement.
     */
   private void cleanImage() {
        boolean found = false;
        //Iterate starting at the bottom left
        for (int i = BarcodeImage.MAX_HEIGHT-1; i >= 0; i--) {
            for (int j = 0; j < BarcodeImage.MAX_WIDTH-1; j++) {
                if (image.getPixel(i,j)) {
                    //Located bottom left of signal
                    moveImageToLowerLeft(i,j);
                    found = true;
                    break;
                }
            }
            if (found) break;
        }
    }

    /**
     * A helper method for cleanImage(). Ensures the internally-stored
     * BarcodeImage's signal is lower-left justified. Beginning at the
     * provided coordinates, moves the signal one character at a time.
     * Clears the old signal in the process.
     *
     * @param startingRow The starting row of the signal
     * @param startingCol The starting column of the signal
     */
    private void moveImageToLowerLeft(int startingRow,int startingCol) {
       int rowTracker = BarcodeImage.MAX_HEIGHT-1, colTracker = 0;
       //Iteration begins at the signal's bottom left pixel
        for (int i = startingRow; i > (startingRow-MAX_BARCODE_HEIGHT); i--) {
            for (int j = startingCol; j < BarcodeImage.MAX_WIDTH; j++) {
                image.setPixel(rowTracker,colTracker++,image.getPixel(i,j));
                image.setPixel(i,j,false); //"erase" original position
            }
            rowTracker--;
            colTracker = 0;
        }
    }

    /**
     * Computes and returns width of image counting from bottom left.
     * Follows the "spine" of the image.
     */
    private int computeSignalWidth() {
       int width = 0;
       for (int i = 0; i < BarcodeImage.MAX_WIDTH; i++) {
          if(image.getPixel(BarcodeImage.MAX_HEIGHT - 1, i))
              width++;
          else
             break;
       }
       return width;
    }

    /**
     * Computes and returns height of image counting from bottom left.
     * Follows the "spine" of the image.
     */
    private int computeSignalHeight() {
       int height = 0;
       for (int i = BarcodeImage.MAX_HEIGHT - 1; i >= 0; i--) {
          if(image.getPixel(i, 0))
             height++;
         else
            break;
      }
      return height;
   }

   /**
    * Helper method for generateImageFromText() which checks
    * if there is a '*' or ' ' and writes the character to
    * the column while also writing the bottom limitation line
    * character.
    */
   private boolean writeCharToCol(int col, int code) {
      if(col > 0 && col < BarcodeImage.MAX_WIDTH && code > 0) {
         //Convert code to binary string
         String binaryString = Integer.toBinaryString(code);
         
         //Counter to keep track of place in column, starting from bottom
         int colIndexCounter = binaryString.length() - 1;
         
         //Set bottom of column to '*' for closed limitation line
         image.setPixel(BarcodeImage.MAX_HEIGHT - 1, col, true);
         
         //Loops through string, checking each character, and writes to column
         for(int i = BarcodeImage.MAX_HEIGHT - 2; i > 0; i--) {
            if(colIndexCounter > -1 || i > BarcodeImage.MAX_HEIGHT - 1) {
               if(binaryString.charAt(colIndexCounter) == '1')
                  image.setPixel(i, col, true);
               else
                  image.setPixel(i, col, false);
            }
            else
               image.setPixel(i, col, false);
            colIndexCounter--;
         }
         return true;
      }
      else
         return false;
   }
   
   /**
    * Helper method for generateImageFromText() which writes
    * the left limitation line and the top and right open
    * borderlines.
    */
   private void writeLimitationLines(char[] textCharArray) {
      //Write left limitation line and right open borderline
      for(int i = BarcodeImage.MAX_HEIGHT - 1; 
            i > BarcodeImage.MAX_HEIGHT - MAX_BARCODE_HEIGHT - 1; i--) {
         image.setPixel(i, 0, true);
         if(i % 2 == 1)
            image.setPixel(i, textCharArray.length + 1, true);
         else
            image.setPixel(i, textCharArray.length + 1, false);
      }
      //Write top open borderline
      for(int i = 0; i < textCharArray.length + 2; i++) {
         if(i % 2 == 0)
            image.setPixel(BarcodeImage.MAX_HEIGHT - 
                  MAX_BARCODE_HEIGHT, i, true);
         else
            image.setPixel(BarcodeImage.MAX_HEIGHT - 
                  MAX_BARCODE_HEIGHT, i, false);
      }
   }

   /**
    * Helper method for clearing image by setting all elements to false
    */
   private void clearImage() {
      for(int i = 0; i < BarcodeImage.MAX_HEIGHT; i++) {
         for(int j = 0; j < BarcodeImage.MAX_WIDTH; j++)
            image.setPixel(i, j, false);
      }
   }

   public static void main(String[] args) {
        String[] sImageIn =
                {
                        "                                               ",
                        "                                               ",
                        "                                               ",
                        "     * * * * * * * * * * * * * * * * * * * * * ",
                        "     *                                       * ",
                        "     ****** **** ****** ******* ** *** *****   ",
                        "     *     *    ****************************** ",
                        "     * **    * *        **  *    * * *   *     ",
                        "     *   *    *  *****    *   * *   *  **  *** ",
                        "     *  **     * *** **   **  *    **  ***  *  ",
                        "     ***  * **   **  *   ****    *  *  ** * ** ",
                        "     *****  ***  *  * *   ** ** **  *   * *    ",
                        "     ***************************************** ",
                        "                                               ",
                        "                                               ",
                        "                                               "

                };

        String[] sImageIn_2 =
                {
                        "                                          ",
                        "                                          ",
                        "* * * * * * * * * * * * * * * * * * *     ",
                        "*                                    *    ",
                        "**** *** **   ***** ****   *********      ",
                        "* ************ ************ **********    ",
                        "** *      *    *  * * *         * *       ",
                        "***   *  *           * **    *      **    ",
                        "* ** * *  *   * * * **  *   ***   ***     ",
                        "* *           **    *****  *   **   **    ",
                        "****  *  * *  * **  ** *   ** *  * *      ",
                        "**************************************    ",
                        "                                          ",
                        "                                          ",
                        "                                          ",
                        "                                          "

                };

        BarcodeImage bc = new BarcodeImage(sImageIn);
        DataMatrix dm = new DataMatrix(bc);

        // First secret message
        dm.translateImageToText();
        dm.displayTextToConsole();
        dm.displayImageToConsole();

        // second secret message
        bc = new BarcodeImage(sImageIn_2);
        dm.scan(bc);
        dm.translateImageToText();
        dm.displayTextToConsole();
        dm.displayImageToConsole();

        // create your own message
        dm.readText("What a great resume builder this is!");
        dm.generateImageFromText();
        dm.displayTextToConsole();
        dm.displayImageToConsole();
    }
}
