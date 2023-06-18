public class Material {
    private static final float GAMMA = 2.2f;
    VectorF albedo;
    float roughness;
    float metalness;
    float reflectivity;
    float transmission;

    public Material(VectorF albedo, float roughness, float metalness, boolean isRefective, float transmission) {
        this.albedo = albedo;
        this.albedo = gammaCorrectionDown(albedo, GAMMA);
        this.roughness = roughness;
        this.metalness = metalness;
        this.reflectivity = isRefective ? 1 - roughness : 0;
        this.transmission = transmission;
    }


    private VectorF gammaCorrectionDown(VectorF light, float gamma){
        light.x = (float) Math.pow(light.x, gamma);
        light.y = (float) Math.pow(light.y, gamma);
        light.z = (float) Math.pow(light.z, gamma);
        return light;
    }
}
