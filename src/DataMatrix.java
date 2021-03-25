public class DataMatrix implements BarcodeIO {
    @Override
    public boolean scan(BarcodeImage bc) {
        return false;
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

    }

    @Override
    public void displayImageToConsole() {

    }
}
