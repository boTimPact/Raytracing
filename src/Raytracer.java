

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.plaf.synth.SynthOptionPaneUI;

public class Raytracer {

    public Camera_ImageLayer cam_Image;
    List<Figure> objects = new LinkedList<>();
    LightSource light;
    JFrame frame;
    JLabel imageLabel;
    MemoryImageSource image;

    private static final int width = 1920;
    private static final int height = 1080;

    //TODO:
    // Quadik Körper,
    // Constuctiv solid geometry,

    // Übung 3:
    // Schatten, (optional: mehrere Schatten, weiche Schatten),
    // Lichtkegel,
    // Reflexion (Strahl weiterleiten)
    Quadric test = new Quadric(1,1,1,0,0,0,0,0,0,-1, new Material(new VectorF(1,1,0), 0.13f,0)).scale(new VectorF(1.2f,1.2f,1.2f)).translate(new VectorF(-0.7f,2.3f,0f));

    public void init(){
        cam_Image = new Camera_ImageLayer(width, height);
        light = new LightSource(new VectorF(0,0,5), new VectorF(1,1,1), 1.0f, 2.2f);



        //this.objects.add(new Quadric(0,0,0,0,0,0,0,1,0,-4, new Material(new VectorF(1,0,0),1,0)).translate(new VectorF(0,8,0)));

//        this.objects.add(
//            new CSG.Intersection(
//                new CSG.Union(
//                    new Quadric(1,1,1,0,0,0,0,0,0,-1, new Material(new VectorF(1,0,0), 0.13f,0)).translate(new VectorF(.5f,0,-2)),
//                    new Quadric(1,1,1,0,0,0,0,0,0,-1, new Material(new VectorF(0,1,0), 0.13f,0)).translate(new VectorF(-.5f,0,-2))
//                ),
//                new Quadric(1,1,1,0,0,0,0,0,0,-1, new Material(new VectorF(0,0,1), 0.13f,0)).translate(new VectorF(0,-1,-1.5f))
//            )
//        );


        this.objects.add(new Quadric(1,1,1,0,0,0,0,0,0,-1, new Material(new VectorF(0.6f,0,1), 0.15f,0)).scale(new VectorF(2,2,2)).translate(new VectorF(5,0,-10)));
        objects.add(new CSG.Union(
                new Quadric(1,1,1,0,0,0,0,0,0,-1, new Material(new VectorF(1,0,0), 0.2f,0)).scale(new VectorF(1.5f, 1.5f, 1.5f)).translate(new VectorF(-0.4f,-3,-0.5f)),
                new Quadric(1,1,1,0,0,0,0,0,0,-1, new Material(new VectorF(0,0,1), 0.4f,0)).translate(new VectorF(0.35f,0-3,0f)))
        );

        objects.add(new CSG.Intersection(
                new Quadric(1,1,1,0,0,0,0,0,0,-1, new Material(new VectorF(1,1,0), 0.2f,0)).translate(new VectorF(-0.35f,0,-0.3f)),
                new Quadric(1,1,1,0,0,0,0,0,0,-1, new Material(new VectorF(0,1,1), 0.5f,0)).translate(new VectorF(0.35f,0,0f)))
        );


        objects.add(new CSG.Differenz(
            new Quadric(1,1,1,0,0,0,0,0,0,-1, new Material(new VectorF(0.5f,1,0.5f), 0.8f,0)).scale(new VectorF(1.5f,1.5f,1.5f)).translate(new VectorF(-1,2.5f,-0.5f)),
            test)
        );


        objects.add(new Quadric(0,1,1,0,0,0,0,0,0,-1, new Material(new VectorF(0,0,1), 0.2f,0)).rotate(new VectorF(0,0,1), -75).translate(new VectorF(-5,0,-10)));
//        objects.add(new CSG.Intersection(
//                new Quadric(0,0,0,0,0,0,0,-1,0,-8, new Material(new VectorF(1,0,0),1,0)).rotate(new VectorF(1,0,0), 20),
//                new Quadric(0,1,1,0,0,0,0,0,0,-1, new Material(new VectorF(0,0,1), 0.2f,0)).rotate(new VectorF(0,0,1), -75).rotate(new VectorF(1,0,0), 20).translate(new VectorF(-5,0,-10))
//        ));



        frame = new JFrame();
        image = new MemoryImageSource(width, height, new DirectColorModel(24, 0xff0000, 0xff00, 0xff), new int[width * height], 0, width);
        imageLabel = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(image)));
        frame.add(imageLabel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        do{
            long time = System.currentTimeMillis();
            update();
            render();
            System.out.println(System.currentTimeMillis() - time + " milliseconds");
            //wait(0);
        }while (false);
    }

    float offset = 0;
    float delta = 0.4f;

    public void update(){
        int[] pixels = new int[width * height];

        offset += delta;
        if(offset > 10 || offset < -10){
            delta *= -1;
        }
        light.pos.x = -offset;
        light.pos.y = offset;

//        Sphere moveing = (Sphere)objects.get(2);
//        moveing.mid.y = offset;


        int threadCount = Thread.activeCount();
        System.out.println(threadCount);

        for (int y = 0; y < height; ++y){
            for (int x = 0; x < width; ++x) {

                Ray ray = cam_Image.rayToImageLayer(x, y, width, height);

                IntersectionPoint intersectionPoint = null;
                float min = Float.POSITIVE_INFINITY;
                int index = -1;
                for (int i = 0; i < objects.size(); i++) {
                    List<IntersectionPoint> tmpPoints = objects.get(i).intersects(ray);
                    IntersectionPoint tmp = null;
                    if(!tmpPoints.isEmpty()) tmp = tmpPoints.get(0);
                    if(tmp != null && tmp.intersection != null && tmp.intersection < min){
                        min = tmp.intersection;
                        index = i;
                        intersectionPoint = tmp;
                    }
                }
                if(intersectionPoint != null) {
                    VectorF lighting = light.physicallyBasedLighting(ray.pointOnRay(intersectionPoint.intersection), objects.get(index), intersectionPoint.figure, ray.origin);
                    System.out.println(lighting);
                    pixels[y * width + x] = (0xFF << 24) | ((int)lighting.x << 16) | ((int)lighting.y << 8) | (int) lighting.z;
                }else {
                    pixels[y * width + x] = 0xFF222222;
                }
            }
        }
        image = new MemoryImageSource(width, height, new DirectColorModel(24, 0xff0000, 0xff00, 0xff), pixels, 0, width);
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