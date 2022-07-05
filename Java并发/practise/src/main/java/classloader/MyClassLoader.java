package classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * 自定义类加载器
 * <p>Filename: MyClassLoader.java</p>
 * <p>Date: 2022-07-05 10:43.</p>
 *
 * @author zhichuanzhang
 * @version 0.1.0
 */
public class MyClassLoader extends ClassLoader {
    /**
     * 存放class文件路径
     */
    private final String classStoragePath;

    public MyClassLoader(String classStoragePath) {
        this.classStoragePath = classStoragePath;
    }

    public MyClassLoader(ClassLoader parent, String classStoragePath) {
        super(parent);
        this.classStoragePath = classStoragePath;
    }

    /**
     * 重写父类方法，返回一个Class对象
     * ClassLoader中对于这个方法的注释是:
     * This method should be overridden by class loader implementations
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 方式1
        /*try {
            byte[] bytes = getClassBytes(name);
            return this.defineClass(name, bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClassNotFoundException();
        }*/

        // 方式2
        try {
            byte[] data = loadByte(name);
            //defineClass将一个字节数组转为Class对象，这个字节数组是class文件读取后最终的字节数组。
            return this.defineClass(name, data, 0, data.length);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClassNotFoundException();
        }

        // 方式3
        /*try (InputStream inputStream = new FileInputStream(this.getClassFile(name));
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {
            int temp = 0;
            while ((temp = inputStream.read()) != -1) {
                outputStream.write(temp);
            }

            byte[] data = outputStream.toByteArray();
            return defineClass(name, data,0, data.length);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ClassNotFoundException();
        }*/
    }

    private File getClassFile(String name) {
        // File file = new File("C:/test/HelloWorld.class");
        name = name.replaceAll("\\.", "/");
        return new File(classStoragePath + "/" + name + ".class");
    }

    private byte[] getClassBytes(String name) throws Exception {
        File file = this.getClassFile(name);

        // 这里要读入.class的字节，因此要使用字节流
        FileInputStream       fis  = new FileInputStream(file);
        FileChannel           fc   = fis.getChannel();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        WritableByteChannel   wbc  = Channels.newChannel(baos);
        ByteBuffer            by   = ByteBuffer.allocate(1024);
        while (true) {
            int i = fc.read(by);
            if (i == 0 || i == -1) {break;}
            by.flip();
            wbc.write(by);
            by.clear();
        }
        fis.close();
        return baos.toByteArray();
    }

    private byte[] loadByte(String name) throws Exception {
        // name = name.replaceAll("\\.", "/");
        // FileInputStream fis = new FileInputStream(classStoragePath + "/" + name + ".class");

        FileInputStream fis = new FileInputStream(this.getClassFile(name));

        int len = fis.available();
        byte[] data = new byte[len];
        fis.read(data);
        fis.close();
        return data;
    }
}
