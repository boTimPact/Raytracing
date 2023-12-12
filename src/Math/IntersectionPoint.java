package Math;

import Geometrics.Figure;

public class IntersectionPoint {
    public Float intersection;
    public VectorF point;
    public Figure figure;
    public VectorF normal;

    public IntersectionPoint(Float intersection, VectorF point, Figure figure, VectorF normal) {
        this.intersection = intersection;
        this.point = point;
        this.figure = figure;
        this.normal = normal;
    }
}
