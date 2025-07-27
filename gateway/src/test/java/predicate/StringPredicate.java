package predicate;

import java.util.function.Predicate;

/**
 * 判断字符是否为空
 * 为空返回 true
 * 不为空返回 false
 */
public class StringPredicate implements Predicate<String> {
    @Override
    public boolean test(String s) {
        return s == null || s.isEmpty();
    }
}
