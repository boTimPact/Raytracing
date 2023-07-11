import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
        float s1 = new VectorF(x[0],y[0],z[0]).add(ray.origin).divideComponentwise(ray.direction).maxValue();
        float s2 = new VectorF(x[1],y[1],z[1]).add(ray.origin).divideComponentwise(ray.direction).minValue();
        if(s2 < 0 || s2 < s1 || a == null && b == null){
            return new LinkedList<>();
        }

        List<IntersectionPoint> listA = a.intersects(ray).stream().sorted((i1, i2) -> Float.compare(i1.intersection, i2.intersection)).toList();
        List<IntersectionPoint> listB = b.intersects(ray).stream().sorted((i1, i2) -> Float.compare(i1.intersection, i2.intersection)).toList();

        if(listA.isEmpty() && listB.isEmpty()) return new LinkedList<>();
        if(listA.isEmpty()) return listB;
        if(listB.isEmpty()) return listA;
        return listA.get(0).intersection < listB.get(0).intersection ? listA : listB;
    }

    @Override
    VectorF getNormal(VectorF point, Figure figure, Figure intersectionFigure) {
        return null;
    }
}
