

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Raytracer {

    public Camera_ImageLayer cam_Image;
    List<Figure> objects = new LinkedList<>();
    BVH bvh;
    ImageReader skyDome;
    List<LightSource> lights;
    JFrame frame;
    JLabel imageLabel;
    MemoryImageSource image;

    private ExecutorService service;
    int[] pixels;

    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1080;
    private static final int RECURSION_DEPTH = 5;
    public static int ADDITIONAL_SHADOW_RAY_COUNT = 0;
    private static final int THREAD_NUMBER = Runtime.getRuntime().availableProcessors();
    private static final int CHUNK_SIZE = HEIGHT / THREAD_NUMBER;


    //TODO:
    // Constuctiv solid geometry Verschachtelung,
    // Lichtkegel,
    // Metallness,
    // Optional:
    // (UI)
    // &&
    // Übung 4 (Nicht alle nötig)
    // Dreicksnetz Figuren
    // Objekte einlesen (siehe Computergrafik)
    // Farbiges Glas (Schattenwurf keine Ablenkung)
    // Nebel
    // Andere Körper
    // Bounding Volume Hierarchy (BVA)


    Quadric test = new Quadric(1,1,1,0,0,0,0,0,0,-1, new Material(new VectorF(1,1,0), 0.9f,0, true, 0, Substance.SOLID)).scale(new VectorF(1.2f,1.2f,1.2f)).translate(new VectorF(-1f,2.3f,0f));

    public void init(){
        pixels = new int[WIDTH * HEIGHT];
        cam_Image = new Camera_ImageLayer(WIDTH, HEIGHT);
        lights = new LinkedList<>();
        lights.add(new LightSource(new VectorF(-15,-5, 0), new VectorF(1,1,1), 1f, 2.2f));

        //Daylight
        lights.add(new LightSource(new VectorF(0,100,-1000), new VectorF(1,1,1), 0.1f,2.2f));
        lights.add(new LightSource(new VectorF(0,100,1000), new VectorF(1,1,1), 0.1f,2.2f));
        lights.add(new LightSource(new VectorF(1000,100,0), new VectorF(1,1,1), 0.1f,2.2f));
        lights.add(new LightSource(new VectorF(-1000,100,0), new VectorF(1,1,1), 0.1f,2.2f));
        lights.add(new LightSource(new VectorF(0,-1000,0), new VectorF(1,1,1), 0.1f,2.2f));

        //region Object Init Region
//        this.objects.add(new Sphere(new Material(new VectorF(0,0,1), 0.3f, 0, false, 0, Substance.SOLID), new VectorF(0,0,-2), 1));
            this.objects.add(new Sphere(new Material(new VectorF(0,0,1), 0.3f, 0, false, 0, Substance.SOLID), new VectorF(-3,0,-5), 2));
            this.objects.add(new Sphere(new Material(new VectorF(1,1,1), 0.04f, 0, true, 0, Substance.SOLID), new VectorF(3,0,-5), 2));
            this.objects.add(new Sphere(new Material(new VectorF(1,1,1), 0, 0, false, 0.99f, Substance.GLASS), new VectorF(-1,-1,0), 1));

            this.objects.add(new Sphere(new Material(new VectorF(1,0.64f,0), 1, 0, false, 0, Substance.SOLID), new VectorF(0,1020,-1000), 980));


//            this.objects.add(new Quadric(0,0,0,0,0,0,0,1,0,-4, new Material(new VectorF(1,0,0),1,0)).translate(new VectorF(0,8,0)));
//
//            this.objects.add(new Quadric(1,1,1,0,0,0,0,0,0,-1, new Material(new VectorF(0.6f,0,1), 0.3f,0, true, 0, Material.SUBSTANCE.GLASS)).scale(new VectorF(2,2,2)).translate(new VectorF(5,0,-10)));

            objects.add(new CSG.Union(
                    new Sphere(new Material(new VectorF(1,0,0), 0.2f,0, false, 0, Substance.SOLID), new VectorF(0,-2,-3-3), 1.5f),
                    new Sphere(new Material(new VectorF(0,0,1), 0.9f,0, true, 0, Substance.SOLID), new VectorF(0.5f,-2,-2.5f-3), 1))
            );

            objects.add(new CSG.Intersection(
                    new Sphere(new Material(new VectorF(1,0.84f,0), 0.2f,0, false, 0, Substance.SOLID), new VectorF(-0.35f,1.5f,-0.3f), 1),
                    new Sphere(new Material(new VectorF(0,1,1), 0.5f,0, false, 0, Substance.SOLID), new VectorF(0.35f,1.5f,0f), 1))
            );

            objects.add(new CSG.Differenz(
                new Sphere(new Material(new VectorF(0.5f,1,0.5f), 0.8f,0, false, 0, Substance.SOLID), new VectorF(7,-2.5f,-6.5f), 1.5f),
                new Sphere(new Material(new VectorF(1,1,0), 0.9f,0, true, 0, Substance.SOLID), new VectorF(7f,-2.7f,-6f), 1.2f))
            );

    //        objects.add(new Quadric(0,1,1,0,0,0,0,0,0,-1, new Material(new VectorF(0,0,1), 0.35f,0, true, 0)).rotate(new VectorF(0,0,1), -75).translate(new VectorF(-5,0,-10)));
    //        objects.add(new CSG.Intersection(
    //                new Quadric(0,0,0,0,0,0,0,-1,0,-8, new Material(new VectorF(1,0,0),1,0)).rotate(new VectorF(1,0,0), 20),
    //                new Quadric(0,1,1,0,0,0,0,0,0,-1, new Material(new VectorF(0,0,1), 0.2f,0)).rotate(new VectorF(0,0,1), -75).rotate(new VectorF(1,0,0), 20).translate(new VectorF(-5,0,-10))
    //        ));

        //endregion R
        OBJFileReader reader = new OBJFileReader();
        List<Figure> triangles = reader.readFile("src/Assets/Models/Pawn_LowPoly.obj", new Material(new VectorF(0.2f,0.1f,0.007f), 0.4f,0, false, 0, Substance.SOLID), new Matrix4f().translate(-17,-3,-150).rotateY(45).rotateX(-20).rotateZ(180));
        BVH mesh = new BVH(triangles);
        this.objects.add(mesh.root);

        bvh = new BVH(objects);
        skyDome = new ImageReader("src/Assets/Skysphere/kloppenheim_06_puresky_1k.png");
        System.out.println("Setup complete!\nObjects: " + this.objects.size());
        frame = new JFrame();
        image = new MemoryImageSource(WIDTH, HEIGHT, new DirectColorModel(24, 0xff0000, 0xff00, 0xff), new int[WIDTH * HEIGHT], 0, WIDTH);
        imageLabel = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(image)));
        frame.add(imageLabel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        service = Executors.newFixedThreadPool(THREAD_NUMBER);


        do{
            long time = System.currentTimeMillis();
            update();
            render();
            System.out.println(System.currentTimeMillis() - time + " milliseconds");
            System.out.printf("%.2f fps\n\n", 1000. / (System.currentTimeMillis() - time));
            //wait(0);
        }while (true);
    }

    float offset = 0;
    float delta = 0.03f;

    public void update(){
        List<Future> tasks = new ArrayList<>();

        offset += delta;
//        if(offset > 10 || offset < -10){
//            delta *= -1;
//        }
//        light.pos.x = -offset;
//        light.pos.y = offset;
        Matrix4f rotMat = new Matrix4f().rotateY(delta);
        lights.get(0).pos = lights.get(0).pos.multiplyMatrix(rotMat);

//        Sphere moveing = (Sphere)objects.get(2);
//        moveing.mid.y = offset;

        for (int i = 0; i < THREAD_NUMBER; i++) {
            int startY = i * CHUNK_SIZE;
            int endY = i == THREAD_NUMBER - 1 ? HEIGHT : startY + CHUNK_SIZE;
            tasks.add(service.submit(new RaytraceTask(pixels, startY, endY)));
        }

        for (Future future : tasks) {
            try {
                future.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        image = new MemoryImageSource(WIDTH, HEIGHT, new DirectColorModel(24, 0xff0000, 0xff00, 0xff), pixels, 0, WIDTH);
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

        for (int i = 0; i < objects.size(); i++) {
            List<IntersectionPoint> tmpPoints = objects.get(i).intersects(ray).stream().sorted((i1, i2) -> Float.compare(i1.intersection, i2.intersection)).toList(); //bvh.root.intersects(ray).stream().sorted((i1, i2) -> Float.compare(i1.intersection, i2.intersection)).toList(); //
            IntersectionPoint tmp = null;
            if (!tmpPoints.isEmpty()) tmp = tmpPoints.get(0);
            if (tmp != null && tmp.intersection != null && tmp.intersection < min && tmp.intersection > 0) {
                min = tmp.intersection;

                intersectionPoint = tmp;
            }
        }

        if(intersectionPoint == null) {
//            return new VectorF(0.01f,0.01f,0.01f);
            return skyDome.getColorAtPoint(ray).multiplyScalar(0.5f);
        }

        VectorF point = intersectionPoint.point;
        VectorF normalVec = intersectionPoint.normal;

        VectorF reflectColor = new VectorF(0,0,0);
        VectorF refractColor = new VectorF(0,0,0);


        VectorF objColor = new VectorF(0,0,0);
        for (LightSource light: lights) {
            VectorF lightColor = light.physicallyBasedLighting(point, normalVec, intersectionPoint.figure, ray.direction, intersectionPoint.figure.material.albedo);
            lightColor = lightColor.multiplyScalar(shadowFactor(point, normalVec, light));
            objColor = objColor.add(lightColor);
        }

        if(intersectionPoint.figure.material.reflectivity > 0){
            reflectColor = getColor(getReflectionRay(ray, point, normalVec), depth - 1);
        }
        if(intersectionPoint.figure.material.transmission > 0){
            refractColor = getColor(getRefractionRay(ray, point, normalVec, intersectionPoint.figure.material.substance), depth - 1);
        }

        color = objColor.multiplyScalar(1 - intersectionPoint.figure.material.reflectivity).add(reflectColor.multiplyScalar(intersectionPoint.figure.material.reflectivity));
        color = color.multiplyScalar(1 - intersectionPoint.figure.material.transmission).add(refractColor.multiplyScalar(intersectionPoint.figure.material.transmission));


        color = color.clamp(1,0);
        return color;
    }

    private Ray getReflectionRay(Ray ray, VectorF newOrigin, VectorF normal){
        float nDotV = normal.dot(ray.direction);
        VectorF newDirection = ray.direction.add(normal.multiplyScalar(-2 * nDotV));
        return  new Ray(newOrigin.add(normal.multiplyScalar(0.002f)), newDirection);
    }

    private Ray getRefractionRay(Ray ray, VectorF newOrigin, VectorF normal, Substance substance){
        float i;
        if(ray.direction.dot(normal) < 0){
            i = Substance.AIR.getVal() / substance.getVal();
        }else {
            normal = normal.negate();
            i = substance.getVal() / Substance.AIR.getVal();
        }
        float a = ray.direction.negate().dot(normal);
        float tmp = 1 - i * i * (1 - a * a);
        if(tmp < 0) return getReflectionRay(ray, newOrigin, normal);
        float b = (float) Math.sqrt(tmp);
        VectorF newDirection = ray.direction.multiplyScalar(i).add(normal.multiplyScalar(i * a - b));
        return new Ray(newOrigin.add(newDirection.multiplyScalar(0.002f)), newDirection);
    }


    private float shadowFactor(VectorF point, VectorF normal, LightSource light){
        int count = 0;

        for (VectorF lightPos: light.lightCheckers) {
            if(isInShadow(point, normal, lightPos)){
                count++;
            }
        }
        return ADDITIONAL_SHADOW_RAY_COUNT == 0 ? 1 - count : 1 - (count / (float) ADDITIONAL_SHADOW_RAY_COUNT);
    }

    private boolean isInShadow(VectorF point, VectorF normal, VectorF lightPos){
        Ray toLight = new Ray(point.add(normal.multiplyScalar(0.002f)), lightPos.add(point.negate()));
        float lightRayMagnitude = toLight.direction.magnitude();
        for (int i = 0; i < objects.size(); i++) {
            List<IntersectionPoint> tmpPoints = objects.get(i).intersects(toLight);
            if(!tmpPoints.isEmpty()){

                for (int j = 0; j < tmpPoints.size(); j++) {
                    if(tmpPoints.get(j).intersection > 0 && lightRayMagnitude < tmpPoints.get(j).intersection && tmpPoints.get(j).figure.material.substance == Substance.SOLID){
                        return true;
                    }
                }
            }
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


    private class RaytraceTask implements Runnable{

        private int pixels[];
        private int startY;
        private int endY;

        public RaytraceTask(int[] pixels, int startY, int endY) {
            this.pixels = pixels;
            this.startY = startY;
            this.endY = endY;
        }

        @Override
        public void run() {

            for (int y = startY; y < endY; ++y){
                for (int x = 0; x < WIDTH; ++x) {
                    Ray ray = cam_Image.rayToImageLayer(x, y, WIDTH, HEIGHT);

                    VectorF color = getColor(ray, RECURSION_DEPTH);
                    color = gammaCorrectionUp(color, 2.2f);
                    color = color.multiplyScalar(255);
                    pixels[y * WIDTH + x] = (0xFF << 24) | ((int)color.x << 16) | ((int)color.y << 8) | (int) color.z;
                }
            }
        }
    }


    public static void main(String[] args) {
        Raytracer raytracer = new Raytracer();
        raytracer.init();
    }
}