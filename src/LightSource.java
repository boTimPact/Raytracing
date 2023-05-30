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


    // N = normalVector,
    // V = Vector vom Schnittpunkt zur Kamera   -> here: point.negate()
    // L = Vector von der Lichtquelle zum Schnittpunkt
    // H = (V+L)/2 (normalisiert)
    public VectorF physicallyBasedLighting(VectorF point, Figure figure){
        VectorF lightDirection = point.add(this.pos.negate()).negate();
        VectorF normal = figure.getNormal(point);

        VectorF h = point.negate().normalize().add(lightDirection.normalize()).normalize();

        if(normal.dot(lightDirection) < 0) return new VectorF(0,0,0);

        float f = fresnel(normal.dot(point.negate().normalize()), figure.material.metalness);
        float d = normalDistribution(normal.dot(h), figure.material.roughness);
        float g = geometry(normal.dot(point.negate().normalize()), normal.dot(lightDirection.normalize()), figure.material.roughness);
        //System.out.println("Fresnel: " + f + "\tNormal: " + d + "\tGeometry: " + g);

        float ks = f * d * g;
        float kd = (1 - ks) * (1 - figure.material.metalness);


        //VectorF light = new VectorF(1,1,1).multiplyScalar(f);
        VectorF light = this.color.multiplyScalar(brightness * normal.dot(lightDirection.normalize())).multiplyLineByLine(figure.material.albedo.multiplyScalar(kd).add(new VectorF(ks,ks,ks)));

        light.x = Math.max(Math.min(light.x, 1), 0);
        light.y = Math.max(Math.min(light.y, 1), 0);
        light.z = Math.max(Math.min(light.z, 1), 0);
        return light.multiplyScalar(255);
    }

    float normalDistribution(float nDotW, float roughness){
        return (float) ((roughness * roughness) / (Math.PI * Math.pow(nDotW * nDotW * (roughness * roughness - 1) + 1, 2)));
    }
    float geometry(float ndotv, float ndotl, float roughness){
        return ndotv / (ndotv * (1 - roughness / 2) + roughness / 2) * ndotl / (ndotl * (1 - roughness / 2) + roughness / 2);
    }
    float fresnel(float ndotv, float metalness){
        float reflectivity = (float) (0.4 * (1 - metalness));
        return reflectivity + (1 - reflectivity) * (1 - ndotv);
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
