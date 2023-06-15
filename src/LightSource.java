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
    public VectorF physicallyBasedLighting(VectorF point, Figure figure, Figure intersectionFigure, VectorF camera){

        VectorF lightDirection = point.add(this.pos.negate()).negate().normalize();
        VectorF normal = figure.getNormal(point, figure, intersectionFigure);
        VectorF view = camera.add(point.negate()).normalize();

        VectorF h = view.add(lightDirection).normalize();

        if(normal.dot(lightDirection) < 0) return new VectorF(0,0,0);

        float f = fresnel(normal.dot(view.normalize()), intersectionFigure.material.metalness, intersectionFigure.material.reflectivity);
        float d = normalDistribution(normal.dot(h), intersectionFigure.material.roughness);
        float g = geometry(normal.dot(view.normalize()), normal.dot(lightDirection.normalize()), intersectionFigure.material.roughness);
        //System.out.println("Fresnel: " + f + "\tNormal: " + d + "\tGeometry: " + g);

        float ks = f * d * g;
        float kd = (1 - ks) * (1 - intersectionFigure.material.metalness);

        //VectorF light = new VectorF(1,1,1).multiplyScalar(ks);
        VectorF light = this.color.multiplyScalar(brightness * normal.dot(lightDirection.normalize())).multiplyLineByLine(intersectionFigure.material.albedo.multiplyScalar(kd).add(new VectorF(ks,ks,ks)));

        light.x = Math.max(Math.min(light.x, 1), 0);
        light.y = Math.max(Math.min(light.y, 1), 0);
        light.z = Math.max(Math.min(light.z, 1), 0);
        return light;
    }

    float normalDistribution(float nDotW, float roughness){
        return (float) ((roughness * roughness) / (Math.PI * Math.pow(nDotW * nDotW * (roughness * roughness - 1) + 1, 2)));
    }
    float geometry(float ndotv, float ndotl, float roughness){
        return ndotv / (ndotv * (1 - roughness / 2) + roughness / 2) * ndotl / (ndotl * (1 - roughness / 2) + roughness / 2);
    }
    float fresnel(float ndotv, float metalness, float baseReflectivity){
        float reflectivity = (float) (baseReflectivity * (1 - metalness));
        return reflectivity + (1 - reflectivity) * (float) Math.pow((1 - ndotv), 5);
    }
}
