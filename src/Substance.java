public enum Substance {
    GLASS(1.5f),
    WATER(1.3f),
    DIAMOND(1.8f),
    AIR(1),
    SOLID(0);

    private float value;

    Substance(float value) {
        this.value = value;
    }

    public float getVal() {
        return value;
    }
}