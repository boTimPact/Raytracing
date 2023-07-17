import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageReader {
    //int[] pixels;
    BufferedImage bufferedImage;

    public ImageReader(String filename) {
        File hdrTexture = new File(filename);
        try {
            bufferedImage = ImageIO.read(hdrTexture);
            //pixels = new int[bufferedImage.getHeight() * bufferedImage.getWidth()];
            //bufferedImage.getRGB(0,0,bufferedImage.getWidth(),bufferedImage.getHeight(), pixels, 0, bufferedImage.getWidth());
        } catch (IOException e) {
            throw new RuntimeException("Unable to read texture from stream", e);
        }
    }


    public VectorF getColorAtPoint(Ray ray){
        int x = (int) (bufferedImage.getWidth() * (ray.direction.x + 1) / 2);
        int y = (int) (bufferedImage.getHeight() * (ray.direction.z + 1) / 2);


        int color = bufferedImage.getRGB(x,y);
        float r = (color >> 16 & 0xFF) / 255f;
        float g = (color >> 8 & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        return new VectorF(r,g,b); //0.01f,0.01f,0.01f
    }
}