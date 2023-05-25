public abstract class Figure {

    Material material;
    abstract Float intersects(Ray ray);

    abstract VectorF getNormal(VectorF point);
}
