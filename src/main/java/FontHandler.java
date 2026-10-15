import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class FontHandler {
    {
        UIManager.put("Button.focus", new Color(0, 0, 0, 0)); // Transparent focus border
    }
    static Font getGIR(int size){
        Font customFont = null;
        try {
            // Adjust the path as needed
            InputStream is = FontHandler.class.getResourceAsStream("/fonts/GlacialIndifference-Regular.otf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont((float) size);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return customFont;
    }
    static Font getGIB(int size){
        Font customFont = null;
        try {
            // Adjust the path as needed
            InputStream is = FontHandler.class.getResourceAsStream("/fonts/GlacialIndifference-Bold.otf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont((float) size);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return customFont;
    }
    static Font getGII(int size){
        Font customFont = null;
        try {
            // Adjust the path as needed
            InputStream is = FontHandler.class.getResourceAsStream("/fonts/GlacialIndifference-Italic.otf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont((float) size);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return customFont;
    }
}
