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
        VectorF entry = new VectorF(x[0],y[0],z[0]).add(ray.origin.negate()).divideComponentwise(ray.direction.multiplyScalar(1000));
        VectorF exit = new VectorF(x[1],y[1],z[1]).add(ray.origin.negate()).divideComponentwise(ray.direction.multiplyScalar(1000));

        float tmp;
        if(ray.direction.x < 0){
            tmp = entry.x;
            entry.x = exit.x;
            exit.x = tmp;
        }
        if(ray.direction.y < 0){
            tmp = entry.y;
            entry.y = exit.y;
            exit.y = tmp;
        }
        if(ray.direction.z < 0){
            tmp = entry.z;
            entry.z = exit.z;
            exit.z = tmp;
        }

        float sEntry = entry.maxValue();
        float sExit = exit.minValue();
        if(sExit <= sEntry){
            return new LinkedList<>();
        }

        List<IntersectionPoint> listA = new ArrayList<>();
        if(a != null) listA = a.intersects(ray).stream().sorted((i1, i2) -> Float.compare(i1.intersection, i2.intersection)).toList();
        List<IntersectionPoint> listB = new ArrayList<>();
        if(b != null) listB = b.intersects(ray).stream().sorted((i1, i2) -> Float.compare(i1.intersection, i2.intersection)).toList();

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
