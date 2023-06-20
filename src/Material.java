public class Material {
    private static final float GAMMA = 2.2f;
    VectorF albedo;
    float roughness;
    float metallness;
    float reflectivity;
    float transmission;
    SUBSTANCE substance;

    public Material(VectorF albedo, float roughness, float metallness, boolean isReflective, float transmission, SUBSTANCE substance) {
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

    public enum SUBSTANCE {
        GLASS(1.5f),
        WATER(1.3f),
        DIAMOND(1.8f),
        AIR(1),
        SOLID(0);

        private float value;

        SUBSTANCE(float value) {
            this.value = value;
        }

        public float getVal() {
            return value;
        }
    }
}
