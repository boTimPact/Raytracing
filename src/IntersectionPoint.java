public class IntersectionPoint {
    Float intersection;
    VectorF point;
    Figure figure;
    VectorF normal;

    public IntersectionPoint(Float intersection, VectorF point, Figure figure, VectorF normal) {
        this.intersection = intersection;
        this.point = point;
        this.figure = figure;
        this.normal = normal;
    }
}
