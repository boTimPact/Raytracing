public class Camera_ImageLayer {
    //Camera
    public VectorF pos;
    public VectorF cameraUp;   //up
    public VectorF cameraRight;   //right
    public VectorF cameraViewDirection;   //viewDirection

    //Image Layer
    //private float distanceImage_Camera;
    private VectorF imageBottomLeftCorner;


    public Camera_ImageLayer(int imageWidth, int imageHeight, float distanceImage_Camera){
        this.pos = new VectorF(0,0,0,1);
        this.cameraUp = new VectorF(1,0,0);
        this.cameraRight = new VectorF(0,1,0);
        this.cameraViewDirection = new VectorF(0,0,-1);

        //this.distanceImage_Camera = distanceImage_Camera;
        this.imageBottomLeftCorner = cameraViewDirection.add(cameraRight.negate().multiplyScalar(imageWidth/2).add(cameraUp.negate().multiplyScalar(imageHeight/2)));

//        System.out.println("N:   X: " + n.x + " Y: " + n.y + " Z: " + n.z);
//        System.out.println("V:   X: " + v.x + " Y: " + v.y + " Z: " + v.z);
//        System.out.println("U:   X: " + u.x + " Y: " + u.y + " Z: " + u.z);
    }

    public Ray rayToImageLayer(float x, float y){
        Ray ray = new Ray(this.pos, imageBottomLeftCorner.add(cameraRight.multiplyScalar(x)).add(cameraUp.multiplyScalar(y)));
        return ray;
    }




//
//    public Matrix4f toMatrix(){
//        Matrix4f view = new Matrix4f(pos, u.negate(), v, n);
//        return view;
//    }
}