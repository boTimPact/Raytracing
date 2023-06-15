public class Material {
    VectorF albedo;
    float roughness;
    float metalness;
    float reflectivity;

    public Material(VectorF albedo, float roughness, float metalness, float reflectivity) {
        this.albedo = albedo;
        this.roughness = roughness;
        this.metalness = metalness;
        this.reflectivity = reflectivity;
    }
}
