package dev.gallon.motorassistance.common.utils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Utils {
    public static <T, R extends Comparable<? super R>> Optional<T> minBy(
            List<T> list,
            java.util.function.Function<? super T, ? extends R> selector
    ) {
        return list.stream().min(Comparator.comparing(selector));
    }
}
