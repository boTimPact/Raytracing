package Material;

import Math.*;

public class Material {
    private static final float GAMMA = 2.2f;
    public VectorF albedo;
    public float roughness;
    public float metallness;
    public float reflectivity;
    public float transmission;
    public Substance substance;

    public Material(VectorF albedo, float roughness, float metallness, boolean isReflective, float transmission, Substance substance) {
        this.albedo = albedo;
        this.albedo = gammaCorrectionDown(albedo, GAMMA);
        this.roughness = roughness;
        this.metallness = metallness;
        this.reflectivity = isReflective ? 1 - roughness : 0;
        this.transmission = transmission;
        this.substance = substance;
    }


    private VectorF gammaCorrectionDown(VectorF light, float gamma){
        light.x = (float) Math.pow(light.x, gamma);
        light.y = (float) Math.pow(light.y, gamma);
        light.z = (float) Math.pow(light.z, gamma);
        return light;
    }
}
