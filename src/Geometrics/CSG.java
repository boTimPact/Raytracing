package Geometrics;

import java.util.*;
import Math.*;

public abstract class CSG extends Figure {

    Figure a;
    Figure b;

    public CSG(Figure a, Figure b) {
        this.a = a;
        this.b = b;
    }

    public BoundingBox getBoundingBox(){

        Sphere sA = (Sphere) a;
        Sphere sB = (Sphere) b;

        BoundingBox a = sA.getBoundingBox();
        BoundingBox b = sB.getBoundingBox();

        return new BoundingBox(a,b,  new float[]{Math.min(a.x[0], b.x[0]), Math.max(a.x[1], b.x[1])}, new float[]{Math.min(a.y[0], b.y[0]), Math.max(a.y[1], b.y[1])}, new float[]{Math.min(a.z[0], b.z[0]), Math.max(a.z[1], b.z[1])});
    }

    public static class Union extends CSG{
        public Union(Figure a, Figure b) {
            super(a, b);
        }

        @Override
        public List<IntersectionPoint> intersects(Ray ray) {
            List<IntersectionPoint> out = new LinkedList<>();

            List<IntersectionPoint> intersectionPoints = new LinkedList<>();

            intersectionPoints.addAll(a.intersects(ray));
            intersectionPoints.addAll(b.intersects(ray));

            if(intersectionPoints.size() < 2) return out;

            intersectionPoints.sort(new Comparator<IntersectionPoint>() {
                @Override
                public int compare(IntersectionPoint o1, IntersectionPoint o2) {
                    return Float.compare(o1.intersection, o2.intersection);
                }
            });

            out.add(intersectionPoints.get(0));
            out.add(intersectionPoints.get(intersectionPoints.size()-1));
            return out;
        }

        @Override
        public VectorF getNormal(VectorF point, Figure figure, Figure intersectionFigure) {
            return null;
        }
    }

    public static class Intersection extends CSG{
        public Intersection(Figure a, Figure b) {
            super(a, b);
        }

        @Override
        public List<IntersectionPoint> intersects(Ray ray) {
            List<IntersectionPoint> out = new LinkedList<>();

            List<IntersectionPoint> intersectionPointsA = a.intersects(ray);
            List<IntersectionPoint> intersectionPointsB = b.intersects(ray);

            if(intersectionPointsA.isEmpty() || intersectionPointsB.isEmpty()) return out;

            List<IntersectionPoint> intersectionPoints = new LinkedList<>();
            intersectionPoints.addAll(intersectionPointsA);
            intersectionPoints.addAll(intersectionPointsB);


            //TODO: fix for planes
            intersectionPoints.sort(new Comparator<IntersectionPoint>() {
                @Override
                public int compare(IntersectionPoint o1, IntersectionPoint o2) {
                    return Float.compare(o1.intersection, o2.intersection);
                }
            });

            out.add(intersectionPoints.get(1));
            out.add(intersectionPoints.get(2));
            return out;
        }

        @Override
        public VectorF getNormal(VectorF point, Figure figure, Figure intersectionFigure) {
            return null;
        }
    }

    public static class Differenz extends CSG{
        public Differenz(Figure a, Figure b) {
            super(a, b);
        }

        @Override
        public List<IntersectionPoint> intersects(Ray ray) {
            List<IntersectionPoint> out = new LinkedList<>();

            List<IntersectionPoint> intersectionPointsA = a.intersects(ray);
            List<IntersectionPoint> intersectionPointsB = b.intersects(ray);


            if(intersectionPointsA.isEmpty()) return out;
            if(intersectionPointsB.isEmpty() && intersectionPointsA.size() == 2) {
                out.addAll(intersectionPointsA);
                return out;
            }

            IntersectionPoint[] intersectionPoints = new IntersectionPoint[]{intersectionPointsA.get(0),intersectionPointsA.get(1),intersectionPointsB.get(0),intersectionPointsB.get(1)};

            if(intersectionPoints[0].intersection < intersectionPoints[2].intersection){
                if(intersectionPoints[1].intersection < intersectionPoints[2].intersection) {
                    out.add(intersectionPoints[0]);
                    out.add(intersectionPoints[1]);
                    return out;
                }
                else {
                    out.add(intersectionPoints[0]);
                    intersectionPoints[2].normal = intersectionPoints[2].normal.negate();
                    out.add(intersectionPoints[2]);
                    return out;
                }
            }else {
                if(intersectionPoints[3].intersection < intersectionPoints[0].intersection){
                    out.add(intersectionPoints[0]);
                    out.add(intersectionPoints[1]);
                    return out;
                }
                if(intersectionPoints[3].intersection < intersectionPoints[1].intersection){
                    intersectionPoints[3].normal = intersectionPoints[3].normal.negate();
                    out.add(intersectionPoints[3]);
                    out.add(intersectionPoints[1]);
                    return out;
                }else {
                    return out;
                }
            }
        }

        @Override
        public VectorF getNormal(VectorF point, Figure figure, Figure intersectionFigure) {
            return null;
        }
    }
}