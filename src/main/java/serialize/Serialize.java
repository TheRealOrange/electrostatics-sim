package serialize;

import java.io.*;
import java.util.ArrayList;

//functions that save the classes to a database file

public class Serialize<T> {
    public void writeFile(ArrayList<T> objects, File filename) throws IOException {
        FileOutputStream file  = new FileOutputStream(filename, false);
        ObjectOutputStream out = new ObjectOutputStream(file); out.writeInt(objects.size());
        for(T i : objects) out.writeObject(i);
        out.close(); file.close();
    }

    public ArrayList<T> readFile(File filename) throws IOException, ClassNotFoundException {
        ArrayList<T> out = new ArrayList<>();
        FileInputStream file = new FileInputStream(filename);
        ObjectInputStream in = new ObjectInputStream(file); int size = in.readInt();
        while (size > 0) {
            out.add((T)in.readObject());
            --size;
        }
        in.close(); file.close();
        return out;
    }
}
