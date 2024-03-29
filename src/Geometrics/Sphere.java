package Geometrics;

import java.util.LinkedList;
import java.util.List;

import Material.Material;
import Math.*;

public class Sphere extends Figure {

    public VectorF mid;

    public float radius;

    public Sphere(Material material, VectorF mid, float radius){
        super.material = material;
        this.mid = mid;
        this.radius = radius;
    }

    @Override
    public List<IntersectionPoint> intersects(Ray ray) {
        List<IntersectionPoint> out = new LinkedList<>();

        VectorF d = ray.direction.clone();
        VectorF o = ray.origin.clone();
        //(x - cx)^2 + (y + cy)^2 + (z + cz)^2 = r^2
        // ax^2 + bx + c = 0
        float a = d.x * d.x + d.y * d.y + d.z * d.z;
        float b = 2 * (o.x * d.x - d.x * mid.x + o.y * d.y - d.y * mid.y + o.z * d.z - d.z * mid.z);
        float c = o.x * o.x + o.y * o.y + o.z * o.z + mid.x * mid.x + mid.y * mid.y + mid.z * mid.z - 2 * (o.x * mid.x + o.y * mid.y + o.z * mid.z) - radius * radius;

        float discriminant = b * b - 4 * a * c;

        if(discriminant < 0) return out;

        if(a == 0) {
            float x1 = -c/b;
            VectorF p1 = ray.pointOnRay(x1);
            out.add(new IntersectionPoint(x1, p1,this, this.getNormal(p1, this ,this)));
            return out;
        }

        //(-b - [Vorzeichen von b] * √(b2 - 4ac))/2
        float k = (float) (-b - Math.signum(b) * Math.sqrt(discriminant)) / 2;
        float x1 = Math.min(c/k, k/a);
        float x2 = Math.max(c/k, k/a);
        VectorF p1 = ray.pointOnRay(x1);
        VectorF p2 = ray.pointOnRay(x2);
        out.add(new IntersectionPoint(x1, p1, this, this.getNormal(p1, this, this)));
        out.add(new IntersectionPoint(x2, p2, this, this.getNormal(p2, this, this)));
        return out;
    }

    public VectorF getNormal(VectorF point, Figure figure, Figure intersectionFigure){
        return point.add(this.mid.negate()).normalize();
    }

    public BoundingBox getBoundingBox(){
        return new BoundingBox(this, null, new float[]{this.mid.x - radius, this.mid.x + radius}, new float[]{this.mid.y - radius, this.mid.y + radius}, new float[]{this.mid.z - radius, this.mid.z + radius});
    }
}