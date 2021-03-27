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
      readText(text);
   }
   
    @Override
    public boolean scan(BarcodeImage bc) {
       try {
          image = (BarcodeImage) bc.clone();
          this.cleanImage();
          this.actualHeight = this.computeSignalHeight();
          this.actualWidth = this.computeSignalWidth();
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
        return false;
    }

    @Override
    public boolean translateImageToText() {
        //TODO Michael
        return false;
    }
    
    @Override
    public void displayTextToConsole() {
       System.out.println(text);
    }
    
    @Override
    public void displayImageToConsole() {
        //TODO Michael
    }
    
    private void cleanImage() {
        //TODO Michael
    }
    
    private int computeSignalWidth() {
       int width = 0;
       for (int i = 0; i < BarcodeImage.MAX_WIDTH; i++) {
          if(this.image.getPixel(BarcodeImage.MAX_WIDTH - 1, i))
             width++;
          else
             break;
       }
       return width;
    }
    
    private int computeSignalHeight() {
       int height = 0;
       for (int i = 0; i < BarcodeImage.MAX_HEIGHT; i++) {
          if(this.image.getPixel(i, 0))
             height++;
          else
             break;
       }
       return height;
    }
    public int getActualWidth() { return actualWidth; }
    public int getActualHeight() { return actualHeight; }
    public static void main(String[] args)
    {
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
