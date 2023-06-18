

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

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

    private static final int width = 1920;
    private static final int height = 1080;


    //TODO:
    // Constuctiv solid geometry Verschachtelung,
    // Übung 3:
    // Schatten, (optional: mehrere Schatten, weiche Schatten),
    // Lichtkegel,
    // Reflexion (Strahl weiterleiten)
    // Refraktion
    // Optional:
    // Multithreaded & (UI) & Weichzeichner (Filter),

    // Übung 4 (Nicht alle nötig)
    // Dreicksnetz Figuren
    // Objekte einlesen (siehe Computergrafik)
    // Farbiges Glas (Schattenwurf keine Ablenkung)
    // Nebel
    // Andere Körper
    // Bounding Volume Hierarchy (BVA)
    //


    Quadric test = new Quadric(1,1,1,0,0,0,0,0,0,-1, new Material(new VectorF(1,1,0), 0.13f,0, 0, 0)).scale(new VectorF(1.2f,1.2f,1.2f)).translate(new VectorF(-1f,2.3f,0f));

    public void init(){
        cam_Image = new Camera_ImageLayer(width, height);
        light = new LightSource(new VectorF(0,0,5), new VectorF(1,1,1), 1.0f, 2.2f);



//        this.objects.add(new Quadric(0,0,0,0,0,0,0,1,0,-4, new Material(new VectorF(1,0,0),1,0)).translate(new VectorF(0,8,0)));

        this.objects.add(new Quadric(1,1,1,0,0,0,0,0,0,-1, new Material(new VectorF(0.6f,0,1), 0.2f,0, 0.8f, 0)).scale(new VectorF(2,2,2)).translate(new VectorF(5,0,-10)));

        objects.add(new CSG.Union(
                new Quadric(1,1,1,0,0,0,0,0,0,-1, new Material(new VectorF(1,0,0), 0.2f,0, 0, 0)).scale(new VectorF(1.5f, 1.5f, 1.5f)).translate(new VectorF(0,-2,-3-3)),
                new Quadric(1,1,1,0,0,0,0,0,0,-1, new Material(new VectorF(0,0,1), 0.4f,0, 0.6f, 0)).translate(new VectorF(0.5f,-2,-2.5f-3)))
        );

        objects.add(new CSG.Intersection(
                new Quadric(1,1,1,0,0,0,0,0,0,-1, new Material(new VectorF(1,1,0), 0.2f,0, 0, 0)).translate(new VectorF(-0.35f,0,-0.3f)),
                new Quadric(1,1,1,0,0,0,0,0,0,-1, new Material(new VectorF(0,1,1), 0.5f,0, 0, 0)).translate(new VectorF(0.35f,0,0f)))
        );


        objects.add(new CSG.Differenz(
            new Quadric(1,1,1,0,0,0,0,0,0,-1, new Material(new VectorF(0.5f,1,0.5f), 0.8f,0, 0, 0)).scale(new VectorF(1.5f,1.5f,1.5f)).translate(new VectorF(-1,2.5f,-0.5f)),
            test)
        );

        objects.add(new Quadric(0,1,1,0,0,0,0,0,0,-1, new Material(new VectorF(0,0,1), 0.1f,0, 0, 0)).rotate(new VectorF(0,0,1), -75).translate(new VectorF(-5,0,-10)));
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

        int resX = cam_Image.imageWidth;
        int resY = cam_Image.imageHeight;
        int[] pixels = new int[resX * resY];

        offset += delta;
        if(offset > 10 || offset < -10){
            delta *= -1;
        }
        light.pos.x = -offset;
        light.pos.y = offset;

        int threadcount = Runtime.getRuntime().availableProcessors();
        ArrayBlockingQueue queue =  new ArrayBlockingQueue<>(16);
        ExecutorService service = new ThreadPoolExecutor(threadcount, threadcount, 100, TimeUnit.MILLISECONDS, queue);

//        Sphere moveing = (Sphere)objects.get(2);
//        moveing.mid.y = offset;

        for (int y = 0; y < resY; ++y){
            for (int x = 0; x < resX; ++x) {
                Ray ray = cam_Image.rayToImageLayer(x, y, resX, resY);

                service.submit(new Raytrace(pixels, ray, x, y));
                while (queue.size() >= 15){
                    //System.out.println("Wait for available Thread!");
                }
            }
        }
        service.shutdown();
        while (!service.isTerminated())
        image = new MemoryImageSource(resX, resY, new DirectColorModel(24, 0xff0000, 0xff00, 0xff), pixels, 0, resX);
    }

    public void render(){
        Image i = Toolkit.getDefaultToolkit().createImage(image);
        imageLabel.setIcon(new ImageIcon(i));
    }


    private VectorF getColor(Ray ray, int depth){
        VectorF color = new VectorF(0,0,0);

        if(depth == 0) return color;

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
        if(intersectionPoint == null) {
            return new VectorF(0.05f,0.05f,0.05f);
        }
        VectorF point = ray.pointOnRay(intersectionPoint.intersection);
        VectorF normalVec = intersectionPoint.figure.getNormal(point, objects.get(index), intersectionPoint.figure);
        //if(!isInShadow(point, normalVec)){
            color = color.add(intersectionPoint.figure.material.albedo).multiplyScalar(1 - intersectionPoint.figure.material.reflectivity);
        //}
        if(intersectionPoint.figure.material.reflectivity > 0){
            color = color.add(getColor(getReflectionRay(ray, intersectionPoint.intersection, normalVec), depth - 1)).multiplyScalar(intersectionPoint.figure.material.reflectivity);
        }
        if(intersectionPoint.figure.material.transmission > 0){
            color = color.add(getColor(ray, 0)); //TODO getColor(refractionRay)
        }

        color = light.physicallyBasedLighting(point, objects.get(index), intersectionPoint.figure, ray.origin, color);

        color.x = Math.max(Math.min(color.x, 1), 0);
        color.y = Math.max(Math.min(color.y, 1), 0);
        color.z = Math.max(Math.min(color.z, 1), 0);
        return color;
    }


    private Ray getReflectionRay(Ray ray, float s, VectorF normal){
        VectorF newOrigin = ray.pointOnRay(s);
        VectorF newDirection = ray.direction.add(normal.multiplyScalar(-2 * normal.dot(ray.direction)));
        return  new Ray(newOrigin.add(normal.multiplyScalar(0.001f)), newDirection.negate());
    }

    private boolean isInShadow(VectorF point, VectorF normal){
        Ray toLight = new Ray(point.add(normal.multiplyScalar(0.001f)), this.light.pos.add(point.negate()));
        for (int i = 0; i < objects.size(); i++) {
            List<IntersectionPoint> tmpPoints = objects.get(i).intersects(toLight);
            if(!tmpPoints.isEmpty()) return true;
        }
        return false;
    }


    private VectorF gammaCorrectionUp(VectorF light, float gamma){
        light.x = (float) Math.pow(light.x, 1/gamma);
        light.y = (float) Math.pow(light.y, 1/gamma);
        light.z = (float) Math.pow(light.z, 1/gamma);
        return light;
    }

    private void wait(int ms){
        try{
            Thread.sleep(ms);
        }catch (Exception e){
            System.err.format("IOException: %s%n", e);
        }
    }


    private class Raytrace implements Runnable{

        private int pixels[];
        private Ray ray;
        private int x;
        private int y;


        public Raytrace(int[] pixels, Ray ray, int x, int y) {
            this.pixels = pixels;
            this.ray = ray;
            this.x = x;
            this.y = y;
        }

        @Override
        public void run() {
//            System.out.println(Thread.currentThread().getName() + " " +this.ray);
//            System.out.println(Thread.currentThread().getName() + " " +this.x);
//            System.out.println(Thread.currentThread().getName() + " " +this.y);

            VectorF color = getColor(ray, 4);
//            if(color.x != 0.05) {
//                System.out.println(color);
//            }
            color = gammaCorrectionUp(color, 2.2f);
            color = color.multiplyScalar(255);
            pixels[y * width + x] = (0xFF << 24) | ((int)color.x << 16) | ((int)color.y << 8) | (int) color.z;
//            pixels[y * width + x] = (0xFF << 24) | (0x22 << 16) | (0x22 << 8) | 0x22;
        }
    }


    public static void main(String[] args) {
        Raytracer raytracer = new Raytracer();
        raytracer.init();
    }
}