import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class OBJFileReader {

    public List<Figure> readFile(String path, Material material, Matrix4f transform) {
        List<VectorF> vertices = new LinkedList<>();
        List<Integer> indices = new LinkedList<>();

        File file = new File(path);

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String parts[] = line.split(" ");
                if (parts[0].equals("v")) {                   // v = vertex, vt = Texturkoordinaten, vn = Normale, f = Fläche (f v/vt/vn),
                    vertices.add(new VectorF(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]), 1));
                }
                if (parts[0].equals("f")) {
                    for (int i = 1; i < parts.length; i++) {
                        if (parts[1].contains("/")) {
                            String split[] = parts[i].split("/");
                            indices.add(Integer.parseInt(split[0]) - 1);
                        }
                        //if only vertices are saved in .obj file
                        else {
                            indices.add(Integer.parseInt(parts[i]) - 1);
                        }
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        List<Figure> figure = new LinkedList<>();
        for (int i = 0; i < indices.size() - 2; i = i + 3) {
            figure.add(new Triangle(material, vertices.get(indices.get(i)), vertices.get(indices.get(i+1)), vertices.get(indices.get(i+2)), transform));
        }
        return figure;
    }
}
