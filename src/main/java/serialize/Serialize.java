package serialize;

import java.io.*;

//functions that save the classes to a database file

public class Serialize<T> {
    public void writeFile(T obj, File filename) throws IOException {
        FileOutputStream file  = new FileOutputStream(filename, false);
        ObjectOutputStream out = new ObjectOutputStream(file); out.writeObject(obj);
        out.close(); file.close();
    }

    public T readFile(File filename) throws IOException, ClassNotFoundException {
        T out;
        FileInputStream file = new FileInputStream(filename);
        ObjectInputStream in = new ObjectInputStream(file);
        out = (T)in.readObject();
        in.close(); file.close();
        return out;
    }
}
