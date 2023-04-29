

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class Raytracer {
    public int resX;
    public int resY;
    public Camera_ImageLayer cam_Image;




    public void init(){
        this.resX = 1280;
        this.resY = 832;
        cam_Image = new Camera_ImageLayer(resX, resY, 1);

        //do{
            update();
//            wait(1000);
//        }while (false);
    }

    public void update(){
        int[] pixels = new int[resX * resY];

        for (int y = 0; y < resY; ++y){
            for (int x = 0; x < resX; ++x) {
                Ray ray = cam_Image.rayToImageLayer(x, y);
                Sphere sphere = new Sphere(new VectorF(0,0,255), new VectorF(0,0,-1.00001f), 1);
                float intersection = sphere.intersects(ray);
                if(intersection > 0) {
                    pixels[y * resX + x] = (0xFF << 24) | (0x00 << 16) | (0x00 << 8) | 0xFF;
                }else {
                    pixels[y * resX + x] = 0xFF888888;
                }
            }
        }
        MemoryImageSource image = new MemoryImageSource(resX, resY, new DirectColorModel(24, 0xff0000, 0xff00, 0xff), pixels, 0, resX);
        render(image);
    }

    public void render(MemoryImageSource mis){
        Image image = Toolkit.getDefaultToolkit().createImage(mis);

        JFrame frame = new JFrame();
        frame.add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void wait(int ms){
        try{
            Thread.sleep(ms);
        }catch (Exception e){
            System.err.format("IOException: %s%n", e);
        }
    }




    public static void main(String[] args) {
        Raytracer raytracer = new Raytracer();
        raytracer.init();
    }
}
