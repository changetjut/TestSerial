import java.io.*;

public class ReadObjectTest implements Serializable {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        /**
        byte[] serializeData = serialize(new ReadObjectTest());
        deserialize(serializeData);

        Object runtime = Class.forName("java.lang.Runtime")
                .getMethod("getRuntime", new Class[]{})
                .invoke(null);

        Class.forName("java.lang.Runtime")
                .getMethod("exec", String.class)
                .invoke(runtime, "calc.exe");
         **/


    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        System.out.println("read object in ReadObject");
    }

    public static byte[] serialize(final Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(obj);
        return out.toByteArray();
    }

    public static Object deserialize(final byte[] serialized) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(serialized);
        ObjectInputStream objIn = new ObjectInputStream(in);
        return objIn.readObject();
    }
}