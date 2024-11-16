package ac.grim.grimac.utils.data;

import java.util.Objects;

public class Triple<A, B, C> {
    private final A first;
    private final B second;
    private final C third;

    public Triple(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public static <T, K> Pair<T, K> of(T a, K b) {
        return new Pair<T, K>(a, b);
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    public C getThird() {
        return third;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Triple)) {
            return false;
        }
        Triple b = (Triple) o;
        return Objects.equals(this.first, b.first) && Objects.equals(this.second, b.second) && Objects.equals(this.third, b.third);
    }
}
