public class Camera_ImageLayer {
    //Camera
    public VectorF pos;
    public VectorF cameraUp;   //up
    public VectorF cameraRight;   //right
    public VectorF cameraViewDirection;   //viewDirection

    float aspectRatioX;
    float aspectRatioY;
    int imageWidth;
    int imageHeight;

    //Image Layer
    //private float distanceImage_Camera;
    private VectorF imageBottomLeftCorner;


    public Camera_ImageLayer(int imageWidth, int imageHeight){
        this.pos = new VectorF(0,0,0,1);
        this.cameraUp = new VectorF(1,0,0);
        this.cameraRight = new VectorF(0,1,0);
        this.cameraViewDirection = new VectorF(0,0,-1);


        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.aspectRatioX = imageWidth / (float)imageHeight;
        this.aspectRatioY = (imageHeight * aspectRatioX) / imageWidth;


        this.imageBottomLeftCorner = cameraViewDirection.add(cameraRight.negate().multiplyScalar(aspectRatioX * 0.5f).add(cameraUp.negate().multiplyScalar(aspectRatioY * 0.5f)));
    }

    public Ray rayToImageLayer(float x, float y, int width, int height){
        return new Ray(this.pos, imageBottomLeftCorner.add(cameraRight.multiplyScalar(x * (aspectRatioX / width))).add(cameraUp.multiplyScalar(y * (aspectRatioY / height))));
    }
}