package sugar.free.telesto.parser.utils;

import java.util.HashMap;
import java.util.Map;

public class BiMap<A,B> {

    private Map<A,B> ab = new HashMap<>();
    private Map<B,A> ba = new HashMap<>();

    public void put(A a, B b) {
        ab.put(a,b);
        ba.put(b,a);
    }

    public A getA(B b) {
        return ba.get(b);
    }

    public B getB(A a) {
        return ab.get(a);
    }

}
