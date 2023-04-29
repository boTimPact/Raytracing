

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class Raytracer {
    public Camera_ImageLayer cam_Image;

    List<Figure> objects = new LinkedList<>();


    public void init(){
        cam_Image = new Camera_ImageLayer(1280, 800, 1);

        this.objects.add(new Sphere(0xFF00FFFF, new VectorF(0,0,-5f), 1));
        this.objects.add(new Sphere(0xFFFF0000, new VectorF(2,-2,-7f), 1));
        this.objects.add(new Sphere(0xFF0000FF, new VectorF(1.4f,2.6f,-8f), 2));

        //do{
            update();
//            wait(1000);
//        }while (false);
    }

    public void update(){
        int resX = cam_Image.imageWidth;
        int resY = cam_Image.imageHeight;
        int[] pixels = new int[resX * resY];

        for (int y = 0; y < resY; ++y){
            for (int x = 0; x < resX; ++x) {
                Ray ray = cam_Image.rayToImageLayer(x, y, resX, resY);
                int index = -1;
                float intersection = Float.POSITIVE_INFINITY;
                for (int i = 0; i < objects.size(); i++) {
                    float tmp = objects.get(i).intersects(ray);
                    if(Float.isFinite(tmp) && tmp < intersection){
                        intersection = tmp;
                        index = i;
                    }
                }
                if(index >= 0) {
                    pixels[y * resX + x] = objects.get(index).color;
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
