package Geometrics;

import java.util.*;

import Material.Material;
import Math.*;
import Geometrics.Figure;

public class Triangle extends Figure {
    VectorF[] vertices;
    VectorF normal;

    public Triangle(Material material, VectorF vertex1, VectorF vertex2, VectorF vertex3, Matrix4f transformation) {
        this.material = material;   //new Material.Material(new Math.VectorF((float) Math.random(),(float) Math.random(),(float) Math.random()), 0.4f,0, false, 0, Material.Substance.SOLID);//
        this.vertices = new VectorF[]{vertex1.multiplyMatrix(transformation), vertex2.multiplyMatrix(transformation), vertex3.multiplyMatrix(transformation)};
        calcNormal();
    }


    private static final double EPSILON = 0.0000001;

    public List intersects(Ray ray) {
        List<IntersectionPoint> out = new LinkedList<>();

        VectorF v0 = vertices[0];
        VectorF v1 = vertices[1];
        VectorF v2 = vertices[2];
        VectorF edge1 = v1.add(v0.negate());
        VectorF edge2 = v2.add(v0.negate());
        VectorF h = ray.direction.cross(edge2);

        double a, f, u, v;

        a = edge1.dot(h);

        if (a > -EPSILON && a < EPSILON) {
            return out;    // This ray is parallel to this triangle.
        }

        f = 1.0 / a;
        VectorF s = ray.origin.add(v0.negate());
        u = f * (s.dot(h));

        if (u < 0.0 || u > 1.0) {
            return out;
        }

        VectorF q = s.cross(edge1);
        v = f * ray.direction.dot(q);

        if (v < 0.0 || u + v > 1.0) {
            return out;
        }

        // At this stage we can compute t to find out where the intersection point is on the line.
        double t = f * edge2.dot(q);
        if (t > EPSILON) // ray intersection
        {
            out.add(new IntersectionPoint((float)t, ray.pointOnRay((float) t), this, this.normal));
        }
        return out;
    }

    @Override
    public VectorF getNormal(VectorF point, Figure figure, Figure intersectionFigure) {
        return this.normal;
    }

    public void calcNormal(){
        this.normal = new VectorF(0,0,0);

        VectorF vec1 = vertices[1].add(vertices[0].negate());
        VectorF vec2 = vertices[2].add(vertices[0].negate());
        this.normal = vec1.cross(vec2);

    }

    public BoundingBox getBoundingBox(){
        return new BoundingBox(this, null, new float[]{Math.min(Math.min(vertices[0].x, vertices[1].x), vertices[2].x), Math.max(Math.max(vertices[0].x, vertices[1].x), vertices[2].x)}, new float[]{Math.min(Math.min(vertices[0].y, vertices[1].y), vertices[2].y), Math.max(Math.max(vertices[0].y, vertices[1].y), vertices[2].y)}, new float[]{Math.min(Math.min(vertices[0].z, vertices[1].z), vertices[2].z), Math.max(Math.max(vertices[0].z, vertices[1].z), vertices[2].z)});
    }
}
