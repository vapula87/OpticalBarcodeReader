public class DataMatrix implements BarcodeIO {
   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';
   public static final int MAX_BARCODE_HEIGHT = 10;
   private BarcodeImage image;
   private String text;
   private int actualWidth, actualHeight;
   
   public DataMatrix() {
      image = new BarcodeImage();
      text = "";
      actualWidth = actualHeight = 0;
   }

   public DataMatrix(BarcodeImage image) {
      text = "";
      scan(image);
   }
   
   public DataMatrix(String text) {
      image = new BarcodeImage();
      readText(text);
   }
   
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

   @Override
   public boolean readText(String text) {
      if (text == null || text.equals("") ||
              text.length() > (BarcodeImage.MAX_WIDTH-2)) return false;
      else {
         this.text = text;
         return true;
      }
   }

   @Override
   public boolean generateImageFromText() {
      //TODO Katie
      clearImage();
      char[] textCharArray = text.toCharArray();
      final int MAX_ACTUAL_HEIGHT = 10;
      for(int i = BarcodeImage.MAX_HEIGHT - 1; i > MAX_ACTUAL_HEIGHT; i--) {
         
      }
      return false;
   }

   @Override
   public boolean translateImageToText() {
       if (image == null || getActualWidth() == 0 || getActualHeight() == 0)
           return false;
       double[] encodings = getEncodings();
       String decodedMessage = decodeMessage(encodings);
       readText(decodedMessage);
       return true;
   }

   private double[] getEncodings() {
       double[] encodings = new double[getActualWidth()-2];
       double base = 2, power = 0;
       int arrayTracker = 0, endRow = BarcodeImage.MAX_HEIGHT-getActualHeight();
       for (int i = 1; i < getActualWidth()-1; i++)
       {
           for (int j = BarcodeImage.MAX_HEIGHT-2; j > endRow; j--)
           {
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

   private String decodeMessage(double[] encodings) {
       String decodedMessage = "";
       for (int i = 0; i < encodings.length; i++)
           decodedMessage+=Character.toString((char) encodings[i]);
       return decodedMessage;
   }

   @Override
   public void displayTextToConsole() {
      System.out.println(text);
   }

   @Override
   public void displayImageToConsole() {
       for (int i = 0; i < getActualWidth()+2; i++)
           System.out.print('-');
       System.out.println();
       int startingRow = BarcodeImage.MAX_HEIGHT - getActualHeight();
       for (int i = startingRow; i < BarcodeImage.MAX_HEIGHT; i++) {
           System.out.print('|');
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

    private void cleanImage() {
        boolean found = false;
        for (int i = BarcodeImage.MAX_HEIGHT-1; i >= 0; i--) {
            for (int j = 0; j < BarcodeImage.MAX_WIDTH-1; j++) {
                if (image.getPixel(i,j)) {
                    moveImageToLowerLeft(i,j);
                    found = true;
                    break;
                }
            }
            if (found) break;
        }
    }

    private void moveImageToLowerLeft(int startingRow,int startingCol) {
       int rowTracker = BarcodeImage.MAX_HEIGHT-1, colTracker = 0;
        for (int i = startingRow; i > (startingRow-MAX_BARCODE_HEIGHT); i--) {
            for (int j = startingCol; j < BarcodeImage.MAX_WIDTH; j++) {
                image.setPixel(rowTracker,colTracker++,image.getPixel(i,j));
                image.setPixel(i,j,false);
            }
            rowTracker--;
            colTracker = 0;
        }
    }

    private void displayRawImage() {
       image.displayToConsole();
    }
    
    //Computes and returns width of image counting from bottom left
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
    
    //Computes and returns height of image counting from bottom left
    private int computeSignalHeight() {
       int height = 0;
       for (int i = BarcodeImage.MAX_HEIGHT-1; i >= 0; i--) {
          if(image.getPixel(i, 0))
             height++;
         else
            break;
      }
      return height;
   }

   /*
    * Helper method for generateImageFromText() which checks
    * if there is a '*' or ' ' and writes the character to
    * the column.
    */
   private boolean writeCharToCol(int col, int code) {
      /*
       * Converts code to binary string, then to character array
       * to allow us to loop through and check each character
       */
      String binaryString = Integer.toBinaryString(code);
      char[] binaryChar = new char[binaryString.length()];
      binaryChar = binaryString.toCharArray();
      //Counter to keep track of place in column, starting from bottom
      int colIndexCounter = binaryChar.length;
      //Set bottom of column to '*' for closed limitation line
      image.setPixel(BarcodeImage.MAX_HEIGHT - 1, col, true);
      //Loops through array, checks character, and writes to column
      for(int i = BarcodeImage.MAX_HEIGHT - 2; i > 0; i--) {
         if(colIndexCounter > -1 || i > BarcodeImage.MAX_HEIGHT - 1) {
               if(binaryChar[colIndexCounter] == '1')
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

   //Helper method for clearing image by setting all elements to false
   private void clearImage() {
      for(int i = 0; i < BarcodeImage.MAX_HEIGHT; i++) {
         for(int j = 0; j < BarcodeImage.MAX_WIDTH; j++)
            image.setPixel(i, j, false);
      }
   }
   //Accessors
   public int getActualWidth() { return actualWidth; }
   public int getActualHeight() { return actualHeight; }

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
