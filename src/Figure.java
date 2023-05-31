import java.util.List;

public abstract class Figure {

    Material material;
    abstract List<IntersectionPoint> intersects(Ray ray);
    abstract VectorF getNormal(VectorF point, Figure figure);
}
