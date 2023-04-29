public class LightSource {

    VectorF pos;
    public VectorF color;
    public float brightness;


    public LightSource(VectorF pos, VectorF color, float brightness) {
        this.pos = pos;
        this.color = color;
        this.brightness = brightness;
    }

    public VectorF diffuseLighting(VectorF point, Figure figure){
        VectorF lightDirection = point.add(this.pos.negate());
        VectorF normal = figure.getNormal(point);

        float intensity = normal.dot(lightDirection.normalize().negate());
        VectorF light = figure.color.multiplyScalar(intensity * (brightness / (lightDirection.magnitude() * lightDirection.magnitude() + 1)));
        light.x = Math.max(Math.min(light.x, 255), 0);
        light.y = Math.max(Math.min(light.y, 255), 0);
        light.z = Math.max(Math.min(light.z, 255), 0);
        return light;
    }
}
