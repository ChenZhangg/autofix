package fdse.zc;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
public class ClassPath{
  public static void main(String args[]) throws IOException, ClassNotFoundException{
    String jarPath = "/Users/zhangchen/.m2/repository/com/google/guava/guava/20.0/guava-20.0.jar";
    URL[] urls = { new URL("jar:file:" + jarPath + "!/") };
    URLClassLoader cl = URLClassLoader.newInstance(urls);
    JarFile jarFile = new JarFile(jarPath);
    Enumeration<JarEntry> entries = jarFile.entries();
    while (entries.hasMoreElements()) {
      JarEntry jarEntry = entries.nextElement();
      if(jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")){
        continue;
      }
      String className = jarEntry.getName().substring(0, jarEntry.getName().length() - 6);
      className = className.replace('/', '.');
      System.out.println(className);
      Class c = cl.loadClass(className);
      Class p = c.getSuperclass();
      if(p != null){
        String parentName = c.getSuperclass().getName();
        System.out.println(parentName);
      }
      System.out.println("================");
    }
    jarFile.close();
  }
}