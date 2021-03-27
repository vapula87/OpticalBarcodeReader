public class DataMatrix implements BarcodeIO {
   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';
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
      this.text = text;
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
       catch(Exception e) {
          return false;
       }
    }
    
   @Override
    public boolean readText(String text) {
        return false;
    }

    @Override
    public boolean generateImageFromText() {
       //TODO
       writeCharToCol(0, 0);
       return false;
    }

    @Override
    public boolean translateImageToText() {
        return false;
    }
    
    @Override
    public void displayTextToConsole() {
       System.out.println(text);
    }
    
    @Override
    public void displayImageToConsole() {

    }
    
    private void cleanImage() {
       // TODO Auto-generated method stub
    }
    
    //Computes and returns width of image counting from bottom left
    private int computeSignalWidth() {
       int width = 0;
       for (int i = 0; i < BarcodeImage.MAX_WIDTH; i++) {
          if(image.getPixel(BarcodeImage.MAX_WIDTH - 1, i))
             width++;
          else
             break;
       }
       return width;
    }
    
    //Computes and returns height of image counting from bottom left
    private int computeSignalHeight() {
       int height = 0;
       for (int i = 0; i < BarcodeImage.MAX_HEIGHT; i++) {
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
}
