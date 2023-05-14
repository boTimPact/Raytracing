public class Quadrik {

    float arr[];

    public Quadrik(float a, float b, float c, float d, float e, float f, float g, float h, float i, float j){
        this.arr = new float[]{a,b,c,d,e,f,g,h,i,j};
    }


    //ax2 +by2 +cz2 +2(dxy+exz+fyz+gx+hy+iz)+j<0
    //x= ((o1) + p * (r1)), y= ((o2) + p * (r2)), z= ((o3) + p * (r3))
//    public float intersects(Ray ray) {
//
//    }
}