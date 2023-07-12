import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BoundingBox extends Figure{
    Figure a;
    Figure b;
    float x[];
    float y[];
    float z[];

    public BoundingBox(Figure a, Figure b, float[] x, float[] y, float[] z) {
        this.a = a;
        this.b = b;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    List<IntersectionPoint> intersects(Ray ray) {

//        if(!(ray.origin.x > x[0] && ray.origin.x < x[1] && ray.origin.y > y[0] && ray.origin.y < y[1] && ray.origin.z > z[0] && ray.origin.z < z[1])) {
             int[] sign = new int[]{ray.direction.x < 0 ? 1 : 0, ray.direction.y < 0 ? 1 : 0, ray.direction.z < 0 ? 1 : 0};

            float tmin, tmax, tymin, tymax, tzmin, tzmax;
            VectorF invDirection = ray.direction.componentReciprocal();

            tmin = (x[sign[0]] - ray.origin.x) * invDirection.x;
            tmax = (x[1-sign[0]] - ray.origin.x) * invDirection.x;
            tymin = (y[sign[1]] - ray.origin.y) * invDirection.y;
            tymax = (y[1-sign[1]] - ray.origin.y) * invDirection.y;

            if ((tmin > tymax) || (tymin > tmax))
                return new ArrayList<>();

            if (tymin > tmin)
                tmin = tymin;
            if (tymax < tmax)
                tmax = tymax;

            tzmin = (z[sign[2]] - ray.origin.z) * invDirection.z;
            tzmax = (z[1-sign[2]] - ray.origin.z) * invDirection.z;

            if ((tmin > tzmax) || (tzmin > tmax)) return new ArrayList<>();
//        }

        List<IntersectionPoint> listA = new ArrayList<>();
        if(a != null) listA = a.intersects(ray).stream().sorted((i1, i2) -> Float.compare(i1.intersection, i2.intersection)).collect(Collectors.toList());
        List<IntersectionPoint> listB = new ArrayList<>();
        if(b != null) listB = b.intersects(ray).stream().sorted((i1, i2) -> Float.compare(i1.intersection, i2.intersection)).collect(Collectors.toList());

        if(listA.isEmpty() && listB.isEmpty()) return new LinkedList<>();
        if(listA.isEmpty()) return listB;
        if(listB.isEmpty()) return listA;
//        return listA.get(0).intersection < listB.get(0).intersection ? listA : listB;
        listA.addAll(listB);
        return listA;
    }

    @Override
    VectorF getNormal(VectorF point, Figure figure, Figure intersectionFigure) {
        return null;
    }
}
