public class Material {
    VectorF albedo;
    float roughness;
    float metalness;

    public Material(VectorF albedo, float roughness, float metalness) {
        this.albedo = albedo;
        this.roughness = roughness;
        this.metalness = metalness;
    }
}
