

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
    LightSource light;
    JFrame frame;
    JLabel imageLabel;
    MemoryImageSource image;

    //TODO:
    // Gammkorrektur (vor der Lichtberechnung in Lichtenergie umrechnen und danach wieder zurück),  ?maybe ready?
    // Quadik Körper,
    // Constuctiv solid geometry,

    // Übung 3:
    // Schatten, (optional: mehrere Schatten, weiche Schatten),
    // Lichtkegel,
    // Reflexion (Strahl weiterleiten)

    public void init(){
        cam_Image = new Camera_ImageLayer(1280+200, 800+150);
        light = new LightSource(new VectorF(-10,2,5), new VectorF(1,1,1), 1.0f, 2.2f);

        this.objects.add(new Sphere(new Material(new VectorF(0,1,0), 0.8f,0), new VectorF(0,0,-5f), 1));
        this.objects.add(new Sphere(new Material(new VectorF(1,0,0),0.8f,0), new VectorF(-3,-0,-7f), 1));
        this.objects.add(new Sphere(new Material(new VectorF(0,0,1), 0.8f, 0), new VectorF(3f,1.5f,-8f), 2));

        frame = new JFrame();
        image = new MemoryImageSource(1280, 800, new DirectColorModel(24, 0xff0000, 0xff00, 0xff), new int[1280 * 800], 0, 1280);
        imageLabel = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(image)));
        frame.add(imageLabel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        do{
            update();
            render();
            //wait(0);
        }while (true);
    }

    float offset = 0;
    float delta = 0.15f;

    public void update(){
        int resX = cam_Image.imageWidth;
        int resY = cam_Image.imageHeight;
        int[] pixels = new int[resX * resY];

        offset += delta;
        if(offset > 10 || offset < -10){
            delta *= -1;
        }
        light.pos.x = -offset;
        light.pos.y = offset;

//        Sphere moveing = (Sphere)objects.get(2);
//        moveing.mid.y = offset;

        for (int y = 0; y < resY; ++y){
            for (int x = 0; x < resX; ++x) {
                Ray ray = cam_Image.rayToImageLayer(x, y, resX, resY);
                int index = -1;
                float intersection = Float.POSITIVE_INFINITY;
                for (int i = 0; i < objects.size(); i++) {
                    float tmp = objects.get(i).intersects(ray);
                    if(tmp < intersection){
                        intersection = tmp;
                        index = i;
                    }
                }
                if(index >= 0) {
                    VectorF lighting = light.physicallyBasedLighting(ray.pointOnRay(intersection), objects.get(index));
                    pixels[y * resX + x] = (0xFF << 24) | ((int)lighting.x << 16) | ((int)lighting.y << 8) | (int) lighting.z;
                }else {
                    pixels[y * resX + x] = 0xFF222222;
                }
            }
        }
        image = new MemoryImageSource(resX, resY, new DirectColorModel(24, 0xff0000, 0xff00, 0xff), pixels, 0, resX);
    }

    public void render(){
        Image i = Toolkit.getDefaultToolkit().createImage(image);
        imageLabel.setIcon(new ImageIcon(i));
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