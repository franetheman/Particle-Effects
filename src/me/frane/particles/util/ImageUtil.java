package me.frane.particles.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Frane on 01-10-2018.
 */
public class ImageUtil
{
    public static BufferedImage loadImage(File file) throws IOException
    {
        return ImageIO.read(file);
    }

    public static BufferedImage stringToBufferedImage(String text, Font font)
    {
        //create the FontRenderContext object which helps us to measure the text
        FontRenderContext frc = new FontRenderContext(null, true, true);

        //get the height and width of the text
        Rectangle2D bounds = font.getStringBounds(text, frc);
        int w = (int) bounds.getWidth();
        int h = (int) bounds.getHeight();

        //create a BufferedImage object
        BufferedImage image = new BufferedImage(w, h,   BufferedImage.TYPE_INT_RGB);

        //calling createGraphics() to get the Graphics2D
        Graphics2D g = image.createGraphics();

        //set color and other parameters
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.BLACK);
        g.setFont(font);

        g.drawString(text, (float) bounds.getX(), (float) -bounds.getY());

        //releasing resources
        g.dispose();

        return image;
    }
}
