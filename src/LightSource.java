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
    public VectorF physicallyBasedLighting(VectorF point, Figure figure, Figure intersectionFigure, VectorF viewRayDirection, VectorF albedo){

        VectorF lightDirection = this.pos.add(point.negate()).normalize();
        VectorF normal = figure.getNormal(point, figure, intersectionFigure);
        VectorF view = viewRayDirection.negate().normalize();

        VectorF h = view.add(lightDirection).normalize();

        if(normal.dot(lightDirection) < 0) return new VectorF(0,0,0);

        VectorF reflectivity = intersectionFigure.material.metallness > 0 ? intersectionFigure.material.albedo : new VectorF(0.04f, 0.04f, 0.04f);

        VectorF f = fresnel(normal.dot(view.normalize()), reflectivity); //* intersectionFigure.material.reflectivity
        float d = normalDistribution(normal.dot(h), intersectionFigure.material.roughness);
        float g = geometry(normal.dot(view.normalize()), normal.dot(lightDirection.normalize()), intersectionFigure.material.roughness);
        //System.out.println("Fresnel: " + f + "\tNormal: " + d + "\tGeometry: " + g);

        VectorF ks = f.multiplyScalar(d * g);
        VectorF kd = new VectorF(1,1,1).add(ks.negate()).multiplyScalar(1 - intersectionFigure.material.metallness);

        //VectorF light = new VectorF(1,1,1).multiplyScalar(ks);
        VectorF light = this.color.multiplyScalar(brightness * normal.dot(lightDirection.normalize())).multiplyLineByLine(albedo.multiplyLineByLine(kd).add(ks));

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
    VectorF fresnel(float ndotv, VectorF baseReflectivity){
        //return baseReflectivity + (1 - baseReflectivity) * (float) Math.pow((1 - ndotv), 5);
        return baseReflectivity.add(new VectorF(1,1,1).add(baseReflectivity.negate())).multiplyScalar((float)Math.pow((1 - ndotv), 5));
    }
}
