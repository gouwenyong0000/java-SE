
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 扫描类路径下 类  【class + jar都解析】
 * 兼容1.8  及其以上
 */
public class ScanClasspathAllClasses {
    public static void main(String[] args) throws URISyntaxException, IOException {
        List<URL> urls = new ArrayList<>();

        //获取 classPath
        if (ScanClasspathAllClasses.class.getClassLoader() instanceof URLClassLoader) {//JDK1.8以上
            Collections.addAll(urls, ((URLClassLoader) ScanClasspathAllClasses.class.getClassLoader()).getURLs());
        } else {
            for (String s : System.getProperty("java.class.path").split(";")) {
                urls.add(new File(s).toURI().toURL());
            }
        }
//        walkAllClasses(urls).forEach(System.out::println);
    }

    private static Set<String> walkAllClasses(List<URL> urls) throws URISyntaxException, IOException {
        Set<String> classes = new HashSet<>();
        for (URL url : urls) {
            if (url.toURI().getScheme().equals("file")) {
                File file = new File(url.toURI());
                if (!file.exists()) continue;
                if (file.isDirectory()) {
                    Files.walkFileTree(file.toPath(), new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                            if (path.toFile().getName().endsWith(".class")) {
                                String substring = path.toFile().getAbsolutePath().substring(file.getAbsolutePath().length());
                                if (substring.startsWith(File.separator)) {
                                    substring = substring.substring(1);
                                }
                                substring = substring.substring(0, substring.length() - 6);
                                classes.add(substring.replace(File.separator, "."));
                            }
                            return super.visitFile(path, attrs);
                        }
                    });
                } else if (file.getName().endsWith(".jar")) {
                    JarFile jarFile = new JarFile(file);
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry jarEntry = entries.nextElement();
                        if (!jarEntry.getName().endsWith(".class")) continue;
                        String replace = jarEntry.getName().replace("/", ".");
                        replace = replace.substring(0, replace.length() - 6);
                        classes.add(replace);
                    }
                }
            }
        }
        return classes;
    }
}