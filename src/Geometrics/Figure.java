package Geometrics;

import java.util.List;

import Material.*;
import Math.*;

public abstract class Figure {

    public Material material;
    public abstract List<IntersectionPoint> intersects(Ray ray);
    public abstract VectorF getNormal(VectorF point, Figure figure, Figure intersectionFigure);
}
