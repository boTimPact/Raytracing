public abstract class Figure {

    Material material;
    abstract float intersects(Ray ray);

    abstract VectorF getNormal(VectorF point);
}
