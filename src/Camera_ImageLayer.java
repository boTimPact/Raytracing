import Math.*;

public class Camera_ImageLayer {
    //Camera
    public VectorF pos;
    public VectorF cameraUp;   //up
    public VectorF cameraRight;   //right
    public VectorF cameraViewDirection;   //viewDirection

    //Image Layer
    float aspectRatioX;
    float aspectRatioY;
    int imageWidth;
    int imageHeight;
    private VectorF imageBottomLeftCorner;


    public Camera_ImageLayer(int imageWidth, int imageHeight){
        this.pos = new VectorF(0,0, 100/*1*/,1);
        this.cameraUp = new VectorF(0,1,0);
        this.cameraRight = new VectorF(1,0,0);
        this.cameraViewDirection = new VectorF(0,0,-1);


        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.aspectRatioX = imageWidth / (float)imageHeight / 10 /* */;
        this.aspectRatioY = (imageHeight * aspectRatioX) / imageWidth;


        this.imageBottomLeftCorner = cameraViewDirection.add(cameraRight.negate().multiplyScalar(aspectRatioX * 0.5f).add(cameraUp.negate().multiplyScalar(aspectRatioY * 0.5f)));
    }

    public Ray rayToImageLayer(float x, float y, int width, int height){
        return new Ray(this.pos, imageBottomLeftCorner.add(cameraRight.multiplyScalar(x * (aspectRatioX / width))).add(cameraUp.multiplyScalar(y * (aspectRatioY / height))));
    }
}