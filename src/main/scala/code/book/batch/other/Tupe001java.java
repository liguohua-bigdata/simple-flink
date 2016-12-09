package code.book.batch.other;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;

/**
 * Created by liguohua on 09/12/2016.
 */
public class Tupe001java {
    public static void main(String[] args) {
        Tuple2<String, String> person2 = new Tuple2<>("Max", "Mustermann");
        Tuple3<String, String, Integer> person3 = new Tuple3<>("Max", "Mustermann", 42);
        Tuple4<String, String, Integer, Boolean> person4 = new Tuple4<>("Max", "Mustermann", 42, true);
        // zero based index!
        String firstName = person4.f0;
        String secondName = person4.f1;
        Integer age = person4.f2;
        Boolean fired = person4.f3;
    }
}
