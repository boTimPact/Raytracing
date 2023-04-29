public class Sphere extends  Figure{

    public VectorF mid;

    public float radius;

    public Sphere(int color, VectorF mid, float radius){
        super.color = color;
        this.mid = mid;
        this.radius = radius;
    }

    @Override
    public float intersects(Ray ray) {
        VectorF d = ray.direction;
        VectorF o = ray.origin;

        // ax^2 + bx + c = 0
        float a = d.x * d.x + d.y * d.y + d.z * d.z;
        float b = 2 * (o.x * d.x - d.x * mid.x + o.y * d.y - d.y * mid.y + o.z * d.z - d.z * mid.z);
        float c = o.x * o.x + o.y * o.y + o.z * o.z + mid.x * mid.x + mid.y * mid.y + mid.z * mid.z - 2 * (o.x * mid.x + o.y * mid.y + o.z * mid.z) - radius * radius;

        float discriminant = b * b - 4 * a * c;

        if(a == 0){
            return -c/b;
        }
        //(-b – [Vorzeichen von b] * √(b2 – 4ac))/2
        float k = (float) (-b - Math.signum(b) * Math.sqrt(b * b - 4 * a * c)) / 2;
        return Math.min(c/k, k/a);
    }
}