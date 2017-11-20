package sanket.com.listviewdemo;

/**
 * Created by sanket on 10/3/16.
 */
public class Color {

    String colorName, colorHex;
    public Color(String colorName, String colorHex) {
        this.colorName = colorName;
        this.colorHex=colorHex;
    }

    public Color(String colorName) {
        this.colorName = colorName;
        this.colorHex=null;
    }

    @Override
    public String toString() {
        return colorName;

    }


}
