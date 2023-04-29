public abstract class Figure {
    VectorF color;

    abstract float intersects(Ray ray);

    abstract VectorF getNormal(VectorF point);
}
