package predicate;

import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.function.Predicate;

/**
 * 测试断言 函数式接口
 * and or ...
 */
public class PredicateTest {
    /**
     * 普通写法
     * test(T t)：传入 predicate 一个参数，做判断
     */
    @Test
    public void Test() {
        Predicate<String> predicate = new StringPredicate();
        System.out.println(predicate.test(""));
        System.out.println(predicate.test("abc"));
    }

    /**
     * 匿名内部类
     */
    @Test
    public void Test2() {
        Predicate<String> predicate = new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s == null || s.isEmpty();
            }
        };
        System.out.println(predicate.test(""));
        System.out.println(predicate.test("abc"));
    }

    /**
     * Lambda 表达式
     */
    @Test
    public void Test3() {
        Predicate<String> predicate = s -> s == null || s.isEmpty();
        System.out.println(predicate.test("")); // true
        System.out.println(predicate.test("abc")); // false
    }

    /**
     * negate 否定
     */
    @Test
    public void Test4() {
        Predicate<String> predicate = s -> s == null || s.isEmpty();
        System.out.println(predicate.negate().test(""));
        System.out.println(predicate.negate().test("abc"));
    }

    /**
     * or 或者
     * 判断字符为 aa 或者 bb
     */
    @Test
    public void Test5() {
        Predicate<String> predicate = s -> s.equals("aa");
        Predicate<String> predicate2 = s -> s.equals("bb");

        System.out.println(predicate.or(predicate2).test(""));
        System.out.println(predicate.or(predicate2).test("aa"));
    }

    /**
     * and 与
     * 判断字符不为空，且由数字组成
     */
    @Test
    public void Test6() {
        Predicate<String> predicate = s -> s != null || !s.isEmpty(); // 字符不为空
        // chars() 将字符串转化为IntSteam字符流
        // isDigit() 判断字符是否为数字（0-9）的方法
        // allMatch(Character::isDigit) 判断所有字符是否为数字
        Predicate<String> predicate2 = s -> s != null && s.chars().allMatch(Character::isDigit);

        System.out.println(predicate.and(predicate2).test("123"));
        System.out.println(predicate.and(predicate2).test("aa"));
    }

    @Test
    public void Test7() {
        System.out.println(ZonedDateTime.now());
    }
}
