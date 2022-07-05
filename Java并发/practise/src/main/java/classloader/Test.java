package classloader;

import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * 测试
 * <p>Filename: Test.java</p>
 * <p>Date: 2022-07-05 11:11.</p>
 *
 * @author zhichuanzhang
 * @version 0.1.0
 */
public class Test {

    public static void main(String[] args) throws IOException {
        final File workDir = new File(new File("").getCanonicalPath());
        final File moduleDir = new File(workDir, "Java并发/practise");
        Validate.isTrue(Files.exists(moduleDir.toPath()), "module目录不存在，请手工确认");
        Validate.isTrue(Files.isDirectory(moduleDir.toPath()), "module目录不正确，请手工确认");

        final File javaDir = new File(moduleDir, String.join(File.separator, "src", "main", "java"));

        MyClassLoader sysClassLoader = new MyClassLoader(javaDir.getPath());

        // 防止当前目录有同名class文件，保证只加载我们指定位置的class，即把自定义ClassLoader的父加载器设置为Extension ClassLoader，
        // 这样父加载器加载不到HelloWorld.class，就交由子加载器MyClassLoader来加载了
        // 1.直接把需加载的class文件放在当前目录
        MyClassLoader myClassLoader = new MyClassLoader(ClassLoader.getSystemClassLoader().getParent(), javaDir.getPath());
        // 2.自定义指定目录 "C:\test\classloader\HelloWorld.class"
        // MyClassLoader myClassLoader = new MyClassLoader(ClassLoader.getSystemClassLoader().getParent(), "C:\\test\\");

        try {
            // 由Application ClassLoader（AppClassLoader）加载的
            Class<?> sysClazz = sysClassLoader.loadClass("classloader.HelloWorld");
            Object sysObj = sysClazz.newInstance();
            System.out.println(sysClazz.newInstance());
            System.out.println(sysObj.getClass().getClassLoader());

            // 由MyClassLoader加载的
            Class<?> clazz = myClassLoader.loadClass("classloader.HelloWorld");
            Object obj = clazz.newInstance();
            System.out.println(clazz.newInstance());
            System.out.println(obj.getClass().getClassLoader());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
