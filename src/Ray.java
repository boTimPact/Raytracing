public class Ray {
    public VectorF origin;
    public VectorF direction;

    public Ray(VectorF origin, VectorF direction){
        this.origin = origin;
        this.direction = direction.normalize();
    }

    public Ray(){}

    public VectorF pointOnRay(float s){
        return origin.add(direction.normalize().multiplyScalar(s));
    }

    @Override
    public String toString() {
        return "Ray{" +
                "origin=" + origin +
                ", direction=" + direction +
                '}';
    }
}
