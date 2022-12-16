package lop.utilities;

import java.io.*;

public interface SerializationUtil extends Serializable {

    /**
     * Serialize the object to a byte array
     *
     * @param fileName
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    static Object deserialize(String fileName) throws IOException,
            ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(fileName)) {
            try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                return ois.readObject();
            }
        }
    }

    /**
     * @param obj
     * @param fileName
     * @throws IOException
     */
    static void serialize(Object obj, String fileName)
            throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(obj);
            }
        }
    }

}