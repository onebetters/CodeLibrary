package classloader;

/**
 * <p>Filename: classloader.HelloWorld.java</p>
 * <p>Date: 2022-07-05 15:57.</p>
 *
 * @author zhichuanzhang
 * @version 0.1.0
 */
public class HelloWorld {

    static {
        System.out.println("orthodox || true");
    }

    public static void sayHello() {
        System.out.println("I am an orthodox class!");
    }
}
