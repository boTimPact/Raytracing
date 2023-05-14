public class LightSource {

    VectorF pos;
    public VectorF color;
    public float brightness;
    public float gamma;


    public LightSource(VectorF pos, VectorF color, float brightness, float gamma) {
        this.pos = pos;
        this.color = color;
        this.brightness = brightness;
        this.gamma = gamma;
    }

    public VectorF diffuseLighting(VectorF point, Figure figure){
        VectorF lightDirection = point.add(this.pos.negate());
        VectorF normal = figure.getNormal(point);

        VectorF h = point.negate().normalize().add(lightDirection.normalize()).normalize();

        VectorF f = fresnel(normal.dot(h), figure.material.metalness, figure.material.albedo);
        float d = normalDistribution(normal, h, figure.material.roughness);
        float g = geometry(normal.dot(point.negate()), normal.dot(lightDirection.normalize()), figure.material.roughness);

        VectorF ks = f.multiplyScalar(d * g);
        VectorF kd = (new VectorF(1,1,1).add(ks.negate())).multiplyScalar(1 - figure.material.metalness);



//        float intensity = normal.dot(lightDirection.normalize().negate());
//        VectorF light = figure.material.albedo.multiplyScalar(intensity * (brightness / (lightDirection.magnitude() * lightDirection.magnitude() + 1)));

        //VectorF light = new VectorF(255,255,255).multiplyScalar(d);
        VectorF light = (kd.multiplyLineByLine(figure.material.albedo).add(ks)).multiplyLineByLine(this.color.multiplyScalar(brightness * normal.dot(lightDirection.normalize())));

        light.x = Math.max(Math.min(light.x, 255), 0);
        light.y = Math.max(Math.min(light.y, 255), 0);
        light.z = Math.max(Math.min(light.z, 255), 0);
        return light;
    }

    // N = normalVector,
    // V = Vector vom Schnittpunkt zur Kamera   -> here: point.negate()
    // L = Vector von der Lichtquelle zum Schnittpunkt
    // H = (V+L)/2 (normalisiert)

    float normalDistribution(VectorF n, VectorF w, float roughness){
        return (float) ((roughness * roughness) / (Math.PI * Math.pow(n.dot(w) * n.dot(w) * (roughness * roughness - 1) + 1, 2)));
    }
    float geometry(float ndotv, float ndotl, float roughness){
        return ndotv / (ndotv * (1 - roughness / 2) + roughness / 2) * ndotl / (ndotl * (1 - roughness / 2) + roughness / 2);
    }
    VectorF fresnel(float hdotv, float metalness, VectorF albedo){
        VectorF reflectivity = new VectorF(0.4f,0.4f,0.4f).multiplyScalar(1 - metalness).add(albedo.multiplyScalar(metalness));
        return reflectivity.add(new VectorF(1,1,1).add(reflectivity.negate()).multiplyScalar((float) Math.pow(1- hdotv,5)));
    }



    private float gammaCorrectionDown(float intensity, float gamma){
        return (float) Math.pow(intensity, 1/gamma);
    }
    private VectorF gammaCorrectionUp(VectorF light, float gamma){
        light.x = (float) Math.pow(light.x, gamma);
        light.y = (float) Math.pow(light.y, gamma);
        light.z = (float) Math.pow(light.z, gamma);
        return light;
    }
}
