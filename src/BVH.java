import java.util.LinkedList;
import java.util.List;

public class BVH {
    BoundingBox root;

    public BVH(List<Figure> objects) {
        List<BoundingBox> boundingBoxes = new LinkedList<>();
        for (Figure f : objects) {
            Sphere s = f instanceof Sphere ? (Sphere)f : null;
            if(s != null) boundingBoxes.add(s.getBoundingBox());
        }
        root = calcBVH(boundingBoxes);
    }

    private BoundingBox calcBVH(List<BoundingBox> boxes){
        if(boxes.size() == 1) return boxes.get(0);
        BoundingBox[] b = new BoundingBox[2];
        float volume = Float.POSITIVE_INFINITY;
        for (int i = 0; i < boxes.size(); i++) {
            for (int j = i+1; j < boxes.size(); j++) {
                float boundingVolume = getBoundingVolume(boxes.get(i), boxes.get(j));
                if(volume > boundingVolume){
                    b[0] = boxes.get(i);
                    b[1] = boxes.get(j);
                    volume = boundingVolume;
                }
            }
        }
        BoundingBox newBox = new BoundingBox(b[0], b[1], new float[]{Math.min(b[0].x[0], b[1].x[0]), Math.min(b[0].x[1], b[1].x[1])}, new float[]{Math.min(b[0].y[0], b[1].y[0]), Math.min(b[0].y[1], b[1].y[1])}, new float[]{Math.min(b[0].z[0], b[1].z[0]), Math.min(b[0].z[1], b[1].z[1])});
        boxes.add(newBox);
        return calcBVH(boxes);
    }

    private float getBoundingVolume(BoundingBox b1, BoundingBox b2){
        float x = Math.max(b1.x[1], b2.x[1]) - Math.min(b1.x[0], b2.x[0]);
        float y = Math.max(b1.y[1], b2.y[1]) - Math.min(b1.y[0], b2.y[0]);
        float z = Math.max(b1.z[1], b2.z[1]) - Math.min(b1.z[0], b2.z[0]);
        return x * y * z;
    }
}
