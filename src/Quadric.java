public class Quadric extends Figure{

    float a;
    float b;
    float c;
    float d;
    float e;
    float f;
    float g;
    float h;
    float i;
    float j;


    public Quadric(float a, float b, float c, float d, float e, float f, float g, float h, float i, float j, Material material) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
        this.h = h;
        this.i = i;
        this.j = j;
        super.material = material;
    }

    //ax2 +by2 +cz2 +2(dxy+exz+fyz+gx+hy+iz)+j<0
    //x= ((o1) + p * (r1)), y= ((o2) + p * (r2)), z= ((o3) + p * (r3))
    @Override
    public float intersects(Ray ray) {
        VectorF d = ray.direction;
        VectorF o = ray.origin;
        //(x - cx)^2 + (y + cy)^2 + (z + cz)^2 = r^2
        // ax^2 + bx + c = 0
        float a = this.a * (d.x * d.x) + b * (d.y * d.y) + c * (d.z * d.z) + 2 * this.d * o.x * o.y * d.x * d.y + 2 * e * o.x * o.z * d.x * d.z + 2 * f * o.y * o.z * d.y * d.z;
        float b = 2 * this.a * o.x * d.x + 2 * this.b * o.y * d.y + 2 * this.c * o.z * d.z + 2 * g * o.x * d.x + 2 * h * o.y * d.y + 2 * i * o.z * d.z;
        float c = this.a * (o.x * o.x) + this.b * (o.y * o.y) + this.c * (o.z * o.z) - j;

        float discriminant = b * b - 4 * a * c;

        if(a == 0){
            return -c/b;
        }
        //(-b – [Vorzeichen von b] * √(b2 – 4ac))/2
        float k = (float) (-b - Math.signum(b) * Math.sqrt(b * b - 4 * a * c)) / 2;
        return Math.min(c/k, k/a);
    }

    @Override
    VectorF getNormal(VectorF point) {
        return new VectorF(this.a * point.x + this.d * point.y + this.e * point.z + this.g, this.b * point.y + this.d * point.x + this.f * point.z + this.h, this.c * point.z + this.e * point.x + this.f * point.y + this.i);
    }
}