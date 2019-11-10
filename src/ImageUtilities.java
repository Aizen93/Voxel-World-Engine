import java.awt.*;
import java.awt.image.*;

public class ImageUtilities {
 
    /** Create Image from a file, then turn that into a BufferedImage.
     */
    public static BufferedImage getBufferedImage(String imageFile, Component c) {
        Image image = c.getToolkit().getImage(imageFile);
        waitForImage(image, c);

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(c), image.getHeight(c), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(image, 0, 0, c);
        return(bufferedImage);
    }

    /** Take an Image associated with a file, and wait until it is
     * done loading
     */
    public static boolean waitForImage(Image image, Component c) {
        MediaTracker tracker = new MediaTracker(c);
        tracker.addImage(image, 0);
        try {
            tracker.waitForAll();
        }catch(InterruptedException ie) {
        }
        
        return(!tracker.isErrorAny());
    }
 
    /** Take some Images associated with files, and wait until they
     * are done loading
     */
    public static boolean waitForImages(Image[] images, Component c)  {
        MediaTracker tracker = new MediaTracker(c);
        for(int i=0; i<images.length; i++)
            tracker.addImage(images[i], 0);
        try {
            tracker.waitForAll();
        }catch(InterruptedException ie) {
        }
        
        return(!tracker.isErrorAny());
    }
}