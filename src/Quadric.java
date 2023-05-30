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

    public Quadric(Matrix4f mat, Material material){
        this.a = mat.matrix[0][0];
        this.b = mat.matrix[1][1];
        this.c = mat.matrix[2][2];
        this.d = mat.matrix[0][1];
        this.e = mat.matrix[0][2];
        this.f = mat.matrix[1][2];
        this.g = mat.matrix[0][3];
        this.h = mat.matrix[1][3];
        this.i = mat.matrix[2][3];
        this.j = mat.matrix[3][3];
        super.material = material;
    }

    //ax2 +by2 +cz2 +2(dxy+exz+fyz+gx+hy+iz)+j<0
    //x= ((o1) + p * (r1)), y= ((o2) + p * (r2)), z= ((o3) + p * (r3))
    @Override
    public Float intersects(Ray ray) {
        VectorF d = ray.direction;
        VectorF o = ray.origin;

        float a = this.a * (d.x * d.x) + this.b * (d.y * d.y) + this.c * (d.z * d.z) + 2 * this.d * d.x * d.y + 2 * this.e * d.x * d.z + 2 * this.f * d.y * d.z;
        float b = 2 * (this.a * o.x * d.x + this.b * o.y * d.y + this.c * o.z * d.z + this.d * o.x * d.y + this.d * o.y * d.x + this.e * o.x * d.z + this.e * o.z * d.x + this.f * o.y * d.z + this.f * o.z * d.y + this.g * d.x + this.h * d.y + this.i * d.z);
        float c = this.a * (o.x * o.x) + this.b * (o.y * o.y) + this.c * (o.z * o.z) + 2 * (this.d * o.x * o.y + this.e * o.x * o.z + this.f * o.y * o.z + this.g * o.x + this.h * o.y + this.i * o.z) + j;

        float discriminant = b * b - 4 * a * c;

        if(discriminant < 0) return null;

        if(a == 0){
            return -c/b;
        }
        //(-b - [Vorzeichen von b] * √(b2 - 4ac))/2
        float k = (float) (-b - Math.signum(b) * Math.sqrt(discriminant)) / 2;
        return Math.min(c/k, k/a);
    }

    public Quadric translate(VectorF vec){
        Matrix4f out;
        Matrix4f matrixQuadric = new Matrix4f(new float[]{a,d,e,g,d,b,f,h,e,f,c,i,g,h,i,j});
        Matrix4f translateMatrix = new Matrix4f().translate(-vec.x, -vec.y, -vec.z);
        Matrix4f translateTransposeMatrix = new Matrix4f().translate(-vec.x, -vec.y, -vec.z).transpose();

        out = translateTransposeMatrix.multiply(matrixQuadric).multiply(translateMatrix);
        return new Quadric(out, this.material);
    }

    @Override
    VectorF getNormal(VectorF point) {
        return new VectorF(this.a * point.x + this.d * point.y + this.e * point.z + this.g, this.b * point.y + this.d * point.x + this.f * point.z + this.h, this.c * point.z + this.e * point.x + this.f * point.y + this.i).normalize();
    }
}