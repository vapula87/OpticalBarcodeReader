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
      this.scan(image);
   }
   
   public DataMatrix(String text) {
      image = new BarcodeImage();
      this.text = text;
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
}
