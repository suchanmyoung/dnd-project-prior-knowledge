import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MethodReference {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.stream().forEach(System.out::println);
    }
}
