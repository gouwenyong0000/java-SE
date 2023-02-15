> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [segmentfault.com](https://segmentfault.com/a/1190000041913336)

背景
--

在研究规则引擎时，如果规则以文件的形式存储，那么就需要监听指定的目录或文件来感知规则是否变化，进而进行加载。当然，在其他业务场景下，比如想实现配置文件的动态加载、日志文件的监听、FTP 文件变动监听等都会遇到类似的场景。

本文给大家提供三种解决方案，并分析其中的利弊，建议收藏，以备不时之需。

方案一：定时任务 + File#lastModified
----------------------------

这个方案是最简单，最能直接想到的解决方案。通过定时任务，轮训查询文件的最后修改时间，与上一次进行对比。如果发生变化，则说明文件已经修改，进行重新加载或对应的业务逻辑处理。

在上篇文章《[JDK 的一个 Bug，监听文件变更要小心了](https://link.segmentfault.com/?enc=u31O1JoUInpnmruj9OYOiA%3D%3D.ZwLnKnDT7Ta%2BMuhmpruJgQKmJeSedk4xAksozEUFWevd1H%2BXs3ibx1PirT3XbCfKF3PIjhjY%2BOG2FR60ldz2uQ%3D%3D)》中已经编写了具体的实例，并且也提出了其中的不足。

这里再把实例代码贴出来：

```java
public class FileWatchDemo {

 /**
  * 上次更新时间
  */
 public static long LAST_TIME = 0L;

 public static void main(String[] args) throws IOException {

  String fileName = "/Users/zzs/temp/1.txt";
  // 创建文件，仅为实例，实践中由其他程序触发文件的变更
  createFile(fileName);

  // 执行2次
  for (int i = 0; i < 2; i++) {
   long timestamp = readLastModified(fileName);
   if (timestamp != LAST_TIME) {
    System.out.println("文件已被更新：" + timestamp);
    LAST_TIME = timestamp;
    // 重新加载，文件内容
   } else {
    System.out.println("文件未更新");
   }
  }
 }

 public static void createFile(String fileName) throws IOException {
  File file = new File(fileName);
  if (!file.exists()) {
   boolean result = file.createNewFile();
   System.out.println("创建文件：" + result);
  }
 }

 public static long readLastModified(String fileName) {
  File file = new File(fileName);
  return file.lastModified();
 }
}
```

对于文件低频变动的场景，这种方案实现简单，基本上可以满足需求。不过像上篇文章中提到的那样，需要注意 Java 8 和 Java 9 中 File#lastModified 的 Bug 问题。

但该方案如果用在文件目录的变化上，缺点就有些明显了，比如：操作频繁，效率都损耗在遍历、保存状态、对比状态上了，无法充分利用 OS 的功能。

方案二：WatchService
----------------

在 Java 7 中新增了`java.nio.file.WatchService`，通过它可以实现文件变动的监听。WatchService 是基于操作系统的文件系统监控器，可以监控系统所有文件的变化，无需遍历、无需比较，是一种基于信号收发的监控，效率高。

```java
public class WatchServiceDemo {

    public static void main(String[] args) throws IOException {
        // 这里的监听必须是目录
        Path path = Paths.get("/Users/zzs/temp/");
        // 创建WatchService，它是对操作系统的文件监视器的封装，相对之前，不需要遍历文件目录，效率要高很多
        WatchService watcher = FileSystems.getDefault().newWatchService();
        // 注册指定目录使用的监听器，监视目录下文件的变化；
        // PS：Path必须是目录，不能是文件；
        // StandardWatchEventKinds.ENTRY_MODIFY，表示监视文件的修改事件
        path.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);

        // 创建一个线程，等待目录下的文件发生变化
        try {
            while (true) {
                // 获取目录的变化:
                // take()是一个阻塞方法，会等待监视器发出的信号才返回。
                // 还可以使用watcher.poll()方法，非阻塞方法，会立即返回当时监视器中是否有信号。
                // 返回结果WatchKey，是一个单例对象，与前面的register方法返回的实例是同一个；
                WatchKey key = watcher.take();
                // 处理文件变化事件：
                // key.pollEvents()用于获取文件变化事件，只能获取一次，不能重复获取，类似队列的形式。
                for (WatchEvent<?> event : key.pollEvents()) {
                    // event.kind()：事件类型
                    if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
                        //事件可能lost or discarded
                        continue;
                    }
                    // 返回触发事件的文件或目录的路径（相对路径）
                    Path fileName = (Path) event.context();
                    System.out.println("文件更新: " + fileName);
                }
                // 每次调用WatchService的take()或poll()方法时需要通过本方法重置
                if (!key.reset()) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

上述 demo 展示了 WatchService 的基本使用方式，注解部分也说明了每个 API 的具体作用。

通过 WatchService 监听文件的类型也变得更加丰富：

*   ENTRY_CREATE 目标被创建
*   ENTRY_DELETE 目标被删除
*   ENTRY_MODIFY 目标被修改
*   OVERFLOW 一个特殊的 Event，表示 Event 被放弃或者丢失

如果查看 WatchService 实现类（PollingWatchService）的源码，会发现，本质上就是开启了一个独立的线程来监控文件的变化：

```java
PollingWatchService() {
        // TBD: Make the number of threads configurable
        scheduledExecutor = Executors
            .newSingleThreadScheduledExecutor(new ThreadFactory() {
                 @Override
                 public Thread newThread(Runnable r) {
                     Thread t = new Thread(null, r, "FileSystemWatcher", 0, false);
                     t.setDaemon(true);
                     return t;
                 }});
    }
```

也就是说，本来需要我们手动实现的部分，也由 WatchService 内部帮我们完成了。

如果你编写一个 demo，进行验证时，会很明显的感觉到 WatchService 监控文件的变化并不是实时的，有时候要等几秒才监听到文件的变化。以实现类 PollingWatchService 为例，查看源码，可以看到如下代码：

```java
void enable(Set<? extends Kind<?>> var1, long var2) {
            synchronized(this) {
                this.events = var1;
                Runnable var5 = new Runnable() {
                    public void run() {
                        PollingWatchKey.this.poll();
                    }
                };
                this.poller = PollingWatchService.this.scheduledExecutor.scheduleAtFixedRate(var5, var2, var2, TimeUnit.SECONDS);
            }
        }
```

也就是说监听器由按照固定时间间隔的调度器来控制的，而这个时间间隔在 SensitivityWatchEventModifier 类中定义：

```java
public enum SensitivityWatchEventModifier implements Modifier {
    HIGH(2),
    MEDIUM(10),
    LOW(30);
        // ...
}
```

该类提供了 3 个级别的时间间隔，分别为 2 秒、10 秒、30 秒，默认值为 10 秒。这个时间间隔可以在 path#register 时进行传递：

```java
path.register(watcher, new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_MODIFY},
                SensitivityWatchEventModifier.HIGH);
```

相对于方案一，实现起来简单，效率高。不足的地方也很明显，只能监听当前目录下的文件和目录，不能监视子目录，而且我们也看到监听只能算是准实时的，而且监听时间只能取 API 默认提供的三个值。

该 API 在 Stack Overflow 上也有人提出 Java 7 在 Mac OS 下有延迟的问题，甚至涉及到 Windows 和 Linux 系统，笔者没有进行其他操作系统的验证，如果你遇到类似的问题，可参考对应的文章，寻求解决方案：[https://blog.csdn.net/claram/...](https://link.segmentfault.com/?enc=Wnvl9KacrGqYzrqtX2qDrA%3D%3D.tnGrAo4N7QvL98HyLfJXZpcY0Zj30tH%2FjQHbbkiMCb7FIGtVqg5TVMabvbU8GF0hbdOhObnmqTaYL2N%2B5xqZfA%3D%3D) 。

方案三：Apache Commons-IO
---------------------

方案一我们自己来实现，方案二借助于 JDK 的 API 来实现，方案三便是借助于开源的框架来实现，这就是几乎每个项目都会引入的 commons-io 类库。

引入相应依赖：

```xml
<dependency>
    <groupId>commons-io</groupId>
    <artifactId>commons-io</artifactId>
    <version>2.7</version>
</dependency>
```

注意，不同的版本需要不同的 JDK 支持，2.7 需要 Java 8 及以上版本。

commons-io 对实现文件监听的实现位于 org.apache.commons.io.monitor 包下，基本使用流程如下：

*   自定义文件监听类并继承 `FileAlterationListenerAdaptor` 实现对文件与目录的创建、修改、删除事件的处理；
*   自定义文件监控类，通过指定目录创建一个观察者 `FileAlterationObserver`；
*   向监视器添加文件系统观察器，并添加文件监听器；
*   调用并执行。

第一步：创建文件监听器。根据需要在不同的方法内实现对应的业务逻辑处理。

```java
public class FileListener extends FileAlterationListenerAdaptor {

    @Override
    public void onStart(FileAlterationObserver observer) {
        super.onStart(observer);
        System.out.println("onStart");
    }

    @Override
    public void onDirectoryCreate(File directory) {
        System.out.println("新建：" + directory.getAbsolutePath());
    }

    @Override
    public void onDirectoryChange(File directory) {
        System.out.println("修改：" + directory.getAbsolutePath());
    }

    @Override
    public void onDirectoryDelete(File directory) {
        System.out.println("删除：" + directory.getAbsolutePath());
    }

    @Override
    public void onFileCreate(File file) {
        String compressedPath = file.getAbsolutePath();
        System.out.println("新建：" + compressedPath);
        if (file.canRead()) {
            // TODO 读取或重新加载文件内容
            System.out.println("文件变更，进行处理");
        }
    }

    @Override
    public void onFileChange(File file) {
        String compressedPath = file.getAbsolutePath();
        System.out.println("修改：" + compressedPath);
    }

    @Override
    public void onFileDelete(File file) {
        System.out.println("删除：" + file.getAbsolutePath());
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
        super.onStop(observer);
        System.out.println("onStop");
    }
}
```

第二步：封装一个文件监控的工具类，核心就是创建一个观察者 FileAlterationObserver，将文件路径 Path 和监听器 FileAlterationListener 进行封装，然后交给 FileAlterationMonitor。

```java
public class FileMonitor {

    private FileAlterationMonitor monitor;

    public FileMonitor(long interval) {
        monitor = new FileAlterationMonitor(interval);
    }

    /**
     * 给文件添加监听
     *
     * @param path     文件路径
     * @param listener 文件监听器
     */
    public void monitor(String path, FileAlterationListener listener) {
        FileAlterationObserver observer = new FileAlterationObserver(new File(path));
        monitor.addObserver(observer);
        observer.addListener(listener);
    }

    public void stop() throws Exception {
        monitor.stop();
    }

    public void start() throws Exception {
        monitor.start();

    }
}
```

第三步：调用并执行：

```java
public class FileRunner {

    public static void main(String[] args) throws Exception {
        FileMonitor fileMonitor = new FileMonitor(1000);
        fileMonitor.monitor("/Users/zzs/temp/", new FileListener());
        fileMonitor.start();
    }
}
```

执行程序，会发现每隔 1 秒输入一次日志。当文件发生变更时，也会打印出对应的日志：

```java
onStart
修改：/Users/zzs/temp/1.txt
onStop
onStart
onStop
```

当然，对应的监听时间间隔，可以通过在创建 FileMonitor 时进行修改。

该方案中监听器本身会启动一个线程定时处理。在每次运行时，都会先调用事件监听处理类的 onStart 方法，然后检查是否有变动，并调用对应事件的方法；比如，onChange 文件内容改变，检查完后，再调用 onStop 方法，释放当前线程占用的 CPU 资源，等待下次间隔时间到了被再次唤醒运行。

监听器是基于文件目录为根源的，也可以可以设置过滤器，来实现对应文件变动的监听。过滤器的设置可查看 FileAlterationObserver 的构造方法：

```
public FileAlterationObserver(String directoryName, FileFilter fileFilter, IOCase caseSensitivity) {
    this(new File(directoryName), fileFilter, caseSensitivity);
}
```

小结
--

至此，基于 Java 实现监听文件变化的三种方案便介绍完毕。经过上述分析及实例，大家已经看到，并没有完美的解决方案，根据自己的业务情况及系统的容忍度可选择最适合的方案。而且，在此基础上可以新增一些其他的辅助措施，来避免具体方案中的不足之处。
