

> 概述 ProcessBuilder 类是 J2SE 1.5 在 java.lang 中新添加的一个新类，此类用于创建操作系统进程，它提供一种启动和管理进程（也就是应用程序）的方法。

**概述**
======

ProcessBuilder 类是 J2SE 1.5 在 java.lang 中新添加的一个新类，此类用于创建操作系统进程，它提供一种启动和管理进程（也就是应用程序）的方法。在 J2SE 1.5 之前，都是由 Process 类处来实现进程的控制管理。**每个 ProcessBuilder 实例管理一个进程属性集。它的 start() 方法利用这些属性创建一个新的 Process 实例。start() 方法可以从同一实例重复调用，以利用相同的或相关的属性创建新的子进程。**

每个进程生成器（即 ProcessBuilder 对象）管理这些进程属性：  

+ **命令 command**

  是一个字符串列表，它表示要调用的外部程序文件及其参数（如果有）。在此，表示有效的操作系统命令的字符串列表是依赖于系统的。例如，每一个总体变量，通常都要成为此列表中的元素，但有一些操作系统，希望程序能自己标记命令行字符串——在这种系统中，Java 实现可能需要命令确切地包含这两个元素。

+ **环境 environment**  
  是从变量 到值 的依赖于系统的映射。初始值是当前进程环境的一个副本（请参阅 System.getenv()）。

+ **工作目录 working directory**  
  默认值是当前进程的当前工作目录，通常根据系统属性 user.dir 来命名。

+ **redirectErrorStream 属性**  
  最初，此属性为 false，意思是子进程的标准输出和错误输出被发送给两个独立的流，这些流可以通过 Process.getInputStream() 和 Process.getErrorStream() 方法来访问。如果将值设置为 true，标准错误将与标准输出合并。这使得关联错误消息和相应的输出变得更容易。在此情况下，合并的数据可从 Process.getInputStream() 返回的流读取，而从 Process.getErrorStream() 返回的流读取将直接到达文件尾。



### `ProcessBuilder` 和 `Process` 两个类对比

既然有 Process 类，那为什么还要发明个 ProcessBuilder 类呢？ProcessBuilder 和 Process 两个类有什么区别呢？  
原来，ProcessBuilder 为进程提供了更多的控制，例如，可以设置当前工作目录，还可以改变环境参数。而 Process 的功能相对来说简单的多。  
ProcessBuilder 是一个 final 类，有两个带参数的构造方法，你可以通过构造方法来直接创建 ProcessBuilder 的对象。而 Process 是一个抽象类，一般都通过 `Runtime.exec()` 和 `ProcessBuilder.start()` 来间接创建其实例。（有关 Process 类的详细介绍可以看下一节。）

> 修改进程构造器的属性将影响后续由该对象的 start() 方法启动的进程，但从不会影响以前启动的进程或 Java 自身的进程。  
> ProcessBuilder 类不是同步的。如果多个线程同时访问一个 ProcessBuilder，而其中至少一个线程从结构上修改了其中一个属性，它必须 保持外部同步。

很容易启动一个使用默认工作目录和环境的新进程：（沿用 JDK7 中的例子）

```java
Process p = new ProcessBuilder("myCommand", "myArg").start();
```

下面是一个利用修改过的工作目录和环境启动进程的例子：

```java
ProcessBuilder pb = new ProcessBuilder("myCommand", "myArg1", "myArg2");
Map<String, String> env = pb.environment();
env.put("VAR1", "myValue");
env.remove("OTHERVAR");
env.put("VAR2", env.get("VAR1") + "suffix");
pb.directory("myDir");
Process p = pb.start();
```

要利用一组明确的环境变量启动进程，在添加环境变量之前，首先调用 Map.clear()。

**Process 类**
-------------

Process 类是一个抽象类（所有的方法均是抽象的），封装了一个进程（即一个执行程序）。

Process 类提供了执行从进程输入、执行输出到进程、等待进程完成、检查进程的退出状态以及销毁（杀掉）进程的方法。创建进程的方法可能无法针对某些本机平台上的特定进程很好地工作，比如，本机窗口进程，守护进程，Microsoft Windows 上的 Win16/DOS 进程，或者 shell 脚本。创建的子进程没有自己的终端或控制台。它的所有标准 io（即 stdin、stdout 和 stderr）操作都将通过三个流 (getOutputStream()、getInputStream() 和 getErrorStream()) 重定向到父进程。父进程使用这些流来提供到子进程的输入和获得从子进程的输出。因为有些本机平台仅针对标准输入和输出流提供有限的缓冲区大小，如果读写子 进程的输出流或输入流迅速出现失败，则可能导致子进程阻塞，甚至产生死锁。 当没有 Process 对象的更多引用时，不是删掉子进程，而是继续异步执行子进程。 对于带有 Process 对象的 Java 进程，没有必要异步或并发执行由 Process 对象表示的进程。

Process 抽象类有以下 6 个抽象方法：  
```java
destroy()		//杀掉子进程。  
exitValue()  	//返回子进程的出口值。  
InputStream getErrorStream()  //获得子进程的错误流。  
InputStream getInputStream()  //获得子进程的输入流。  
OutputStream getOutputStream()  //获得子进程的输出流。  
waitFor()  //导致当前线程等待，如果必要，一直要等到由该 Process 对象表示的进程已经终止。
```



## **如何创建 Process 对象？**  

一般有两种方法：

*   使用命令名和命令的参数选项构造 ProcessBuilder 对象，它的 start 方法执行命令，启动一个进程，返回一个 Process 对象。
*   Runtime.exec() 方法创建一个本机进程，并返回 Process 子类的一个实例。

# **Runtime.exec()**

### **`ProcessBuilder` 与 `Runtime.exec()` 的区别?**  

`ProcessBuilder.start()` 和 `Runtime.exec()` 方法都被用来创建一个操作系统进程（执行命令行操作），并返回 Process 子类的一个实例，该实例可用来控制进程状态并获得相关信息。  
ProcessBuilder.start() 和 Runtime.exec() 传递的参数有所不同，Runtime.exec() 可接受一个单独的字符串，这个字符串是通过空格来分隔可执行命令程序和参数的；也可以接受字符串数组参数。而 ProcessBuilder 的构造函数是一个字符串列表或者数组。列表中第一个参数是可执行命令程序，其他的是命令行执行是需要的参数。

> 通过查看 JDK 源码可知，**Runtime.exec 最终是通过调用 ProcessBuilder 来真正执行操作的**。

为了能够详细的说明 ProcessBuilder 和 Runtime.exec 的 “功效”，下面先做一个测试 jar 包 (ProcessJar.jar)，这个 jar 包里就一个类，如下：

```java
public class PrintArgs {
    public static void main(String args[]){
        System.out.println("This is a program test about Process, ProcessBuilder, Runtime.exec etc.");
        System.out.println("Now Print the args:");
        for(int i=0;i<args.length;i++){
            System.out.println("    [args-"+i+"]:"+args[i]);
        }
    }
}
```

然后放在 classpath 下。之后可以调用命令行：`java -jar ProcessJar.jar [args1….n]`

## `Runtime` 的使用 Demo:

```java
package com.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by hidden on 2017/1/17.
 */
public class RuntimeTest {
    public static void main(String[] args) {
        try {
            Process process = Runtime.getRuntime().exec("java -jar ProcessJar.jar args1 agrs2 args3");
            InputStream is = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println(exitCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

输出结果：

```
This is a program test about Process, ProcessBuilder, Runtime.exec etc.
Now Print the args:
    [args-0]:args1
    [args-1]:agrs2
    [args-2]:args3
```

由于调用 Runtime.exec 方法所创建的子进程没有自己的终端或控制台，因此该子进程的标准 IO(如 stdin，stdou，stderr) 都通过 Process.getOutputStream()，Process.getInputStream()，Process.getErrorStream() 方法重定向给它的父进程了。用户需要用这些 stream 来向子进程输入数据或获取子进程的输出。

# **ProcessBuilder **入门

## 与Runtime.exec() 实现对比

这里演示一个 ProcessBuilder 的 demo，和 `Runtime.exec()` 方法差不多，同样是采用 ProcessJar.jar 【j`ava -jar ProcessJar.jar [args1….n]`】 进行测试。

```java
public class ProcessBuilderTest {
    public static void main(String[] args) {
        List<String> params = new ArrayList<String>();
        params.add("java");
        params.add("-jar");
        params.add("ProcessJar.jar");
        params.add("args1");
        params.add("args2");
        params.add("args3");

        ProcessBuilder processBuilder = new ProcessBuilder(params);
//        System.out.println(processBuilder.directory());
//        System.out.println(processBuilder.environment());
        processBuilder.redirectErrorStream(true);
        try {
            Process process = processBuilder.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();
            System.out.println("exitCode = "+exitCode);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

输出结果：

```
This is a program test about Process, ProcessBuilder, Runtime.exec etc.
Now Print the args:
    [args-0]:args1
    [args-1]:args2
    [args-2]:args3
exitCode = 0
```

## 例子：

```java
/**
 * 查看"D:\"目录, Windows系统下查看目录的命令是dir
 */
public static void checkDirectory() throws IOException {
    ProcessBuilder processBuilder = new ProcessBuilder("cmd","/c","dir");
    processBuilder.directory(new File("D:/"));
    Process process = processBuilder.start();
    BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
    String line;
    while ((line = br.readLine()) != null) {
        System.out.println(line);
    }
}

/**
 * 查看ip地址【Windows系统下】
 */
public static void checkPhysicAddress() {
    ProcessBuilder processBuilder = new ProcessBuilder("ipconfig", "/all");
    try {
        Process process = processBuilder.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.indexOf("IPv4") != -1) {
                System.out.println(line);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```







# `ProcessBuilder`详细

ProcessBuilder 用于创建操作系统进程。 其`start()`方法创建具有以下属性的新`Process`实例：

- 命令
- 环境
- 工作目录
- 输入来源
- 标准输出和标准错误输出的目标
- redirectErrorStream

**构造方法**

```java
//利用指定的操作系统程序和参数构造一个进程生成器。 
ProcessBuilder(List<String> command) 
//利用指定的操作系统程序和参数构造一个进程生成器。
ProcessBuilder(String… command) 
```

**方法摘要**

```java
//返回此进程生成器的操作系统程序和参数。 
command() 
//设置此进程生成器的操作系统程序和参数。 
command(List<String> command) 
//设置此进程生成器的操作系统程序和参数。 
command(String… command) 
 
//返回此进程生成器的工作目录。 
directory() 
//设置此进程生成器的工作目录。
directory(File directory) 

//返回此进程生成器环境的字符串映射视图。 environment方法获得运行进程的环境变量,得到一个Map,可以修改环境变量 
environment() 
//返回进程生成器是否合并标准错误和标准输出；true为合并，false为不合并
redirectErrorStream() 
//设置此进程生成器的 redirectErrorStream 属性。默认值为false不合并
redirectErrorStream(boolean redirectErrorStream) 
    
//使用此进程生成器的属性启动一个新进程。
start() 
```

## 

## `ProcessBuilder`运行程序

用`command()`执行程序。 使用`waitFor()`，我们可以等待过程完成。

```java
import java.io.IOException;
//该程序执行 Windows 记事本应用。 它返回其退出代码。
public class ExecuteProgram {

    public static void main(String[] args) throws IOException, InterruptedException {

        var processBuilder = new ProcessBuilder();

        processBuilder.command("notepad.exe");

        var process = processBuilder.start();

        var ret = process.waitFor();

        System.out.printf("Program exited with code: %d", ret);
    }
}
```



## `ProcessBuilder`命令输出

以下示例执行命令并显示其输出。

```java
public class ProcessBuilderEx {

    public static void main(String[] args) throws IOException {

        var processBuilder = new ProcessBuilder();

        processBuilder.command("cal", "2019", "-m 2");

        var process = processBuilder.start();

        try (var reader = new BufferedReader(
            new InputStreamReader(process.getInputStream()))) {

            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        }
    }
}
```



该示例运行 Linux `cal`命令。

```java
processBuilder.command("cal", "2019", "-m 2");
```

`command()`执行`cal`程序。 其他参数是程序的选项。 



为了在 Windows 机器上运行命令，我们可以使用以下命令：`processBuilder.command("cmd.exe", "/c", "ping -n 3 google.com")`。

```java
var process = processBuilder.start();
```

`start()`启动了该过程。

```java
try (var reader = new BufferedReader(  new InputStreamReader(process.getInputStream()))) {
```



使用`getInputStream()`方法，我们从流程的标准输出中获取输入流。

```java
February 2019      
Su Mo Tu We Th Fr Sa  
                1  2  
    3  4  5  6  7  8  9  
10 11 12 13 14 15 16  
17 18 19 20 21 22 23  
24 25 26 27 28 
```

这是输出。

## `ProcessBuilder`重定向输出

使用`redirectOutput()`，我们可以重定向流程构建器的标准输出目的地。

```java
public class RedirectOutputEx {

    public static void main(String[] args) throws IOException {

        var homeDir = System.getProperty("user.home");

        var processBuilder = new ProcessBuilder();

        processBuilder.command("cmd.exe", "/c", "date /t");

        var fileName = new File(String.format("%s/Documents/tmp/output.txt", homeDir));

        processBuilder.redirectOutput(fileName);

        var process = processBuilder.start();

        try (var reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
```



该程序将构建器的输出重定向到文件。 它运行 Windows `date`命令。

```java
processBuilder.redirectOutput(fileName);
```



我们将流程构建器的标准输出重定向到文件。

```java
try (var reader = new BufferedReader(
    new InputStreamReader(process.getInputStream()))) {

    String line;
    while ((line = reader.readLine()) != null) {
        System.out.println(line);
    }
}
```

现在输出到文件。

```java
$ echo %cd%
C:\Users\Jano\Documents\tmp
$ more output.txt
Thu 02/14/2019
```

当前日期已写入`output.txt`文件。

## `ProcessBuilder`重定向输入和输出

下一个示例同时重定向输入和输出。

```
src/resources/input.txt
sky
blue
steel
morning
coffee
earth
forest
```

这是`input.txt`文件的内容。

```java
import java.io.File;
import java.io.IOException;

public class ProcessBuilderRedirectIOEx {

    public static void main(String[] args) throws IOException {

        var processBuilder = new ProcessBuilder();

        processBuilder.command("cat")
                .redirectInput(new File("src/resources", "input.txt"))
                .redirectOutput(new File("src/resources/", "output.txt"))
                .start();
    }
}
```

在程序中，我们将输入从`input.txt`文件重定向到`cat`命令，并将命令的输出重定向到`output.txt`文件。

## `ProcessBuilder`继承 IO

`inheritIO()`将子流程标准 I / O 的源和目的地设置为与当前 Java 流程相同。

```java
import java.io.IOException;

public class ProcessBuilderInheritIOEx {

    public static void main(String[] args) throws IOException, InterruptedException {

        var processBuilder = new ProcessBuilder();

        processBuilder.command("cmd.exe", "/c", "dir");

        var process = processBuilder.inheritIO().start();

        int exitCode = process.waitFor();
        System.out.printf("Program ended with exitCode %d", exitCode);
    }
}
```

通过继承已执行命令的 IO，我们可以跳过读取步骤。 程序输出项目目录的内容和显示退出代码的消息。

```java
02/14/2019  04:55 PM    <DIR>          .
02/14/2019  04:55 PM    <DIR>          ..
02/19/2019  01:11 PM    <DIR>          .idea
02/14/2019  04:55 PM    <DIR>          out
02/14/2019  04:52 PM                     433 ProcessBuilderInheritIOEx.iml
02/14/2019  04:53 PM    <DIR>          src
                1 File(s)            433 bytes
                5 Dir(s)  157,350,264,832 bytes free
Program ended with exitCode 0
```

我们同时获得执行的命令和自己的 Java 程序的输出。

## `ProcessBuilder`环境

`environment()`方法返回流程构建器环境的字符串映射视图。

```java
public class ProcessBuilderEnvEx {

    public static void main(String[] args) {

        var pb = new ProcessBuilder();
        var env = pb.environment();

        env.forEach((s, s2) -> {
            System.out.printf("%s %s %n", s, s2);
        });

        System.out.printf("%s %n", env.get("PATH"));
    }
    
}
```

该程序显示所有环境变量。

```java
configsetroot C:\WINDOWS\ConfigSetRoot 
USERDOMAIN_ROAMINGPROFILE LAPTOP-OBKOFV9J 
LOCALAPPDATA C:\Users\Jano\AppData\Local 
PROCESSOR_LEVEL 6 
USERDOMAIN LAPTOP-OBKOFV9J 
LOGONSERVER \\LAPTOP-OBKOFV9J 
JAVA_HOME C:\Users\Jano\AppData\Local\Programs\Java\openjdk-11\ 
SESSIONNAME Console 
...
```



这是 Windows 上的示例输出。

在下一个程序中，我们定义一个自定义环境变量。

```java
public class ProcessBuilderEnvEx2 {

    public static void main(String[] args) throws IOException {

        var pb = new ProcessBuilder();
        var env = pb.environment();

        env.put("mode", "development");

        pb.command("cmd.exe", "/c", "echo", "%mode%");

        pb.inheritIO().start();
    }
}
```

该程序定义一个`mode`变量并在 Windows 上输出。

```java
pb.command("cmd.exe", "/c", "echo", "%mode%");
```

`%mode%`是 Windows 的环境变量语法； 在 Linux 上，我们使用`$mode`。

## `ProcessBuilder`目录

`directory()`方法设置流程构建器的工作目录。

```Java
public class ProcessBuilderDirectoryEx {

    public static void main(String[] args) throws IOException {

        var homeDir = System.getProperty("user.home");

        var pb = new ProcessBuilder();

        pb.command("cmd.exe", "/c", "dir");
        pb.directory(new File(homeDir));

        var process = pb.start();

        try (var reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
```

该示例将主目录设置为流程生成器的当前目录。 我们显示主目录的内容。

```java
var homeDir = System.getProperty("user.home");
```

我们得到用户的主目录。

```java
pb.command("cmd.exe", "/c", "dir");
```

我们定义了一个在 Windows 上执行`dir`程序的命令。

```java
pb.directory(new File(homeDir));
```

我们设置流程构建器的目录。

```java
Volume in drive C is Windows
Volume Serial Number is 4415-13BB

Directory of C:\Users\Jano

02/14/2019  11:48 AM    <DIR>          .
02/14/2019  11:48 AM    <DIR>          ..
10/13/2018  08:38 AM    <DIR>          .android
01/31/2019  10:58 PM               281 .bash_history
12/17/2018  03:02 PM    <DIR>          .config
...
```

这是一个示例输出。

## `ProcessBuilder`非阻塞操作

在下面的示例中，我们创建一个异步过程。

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class ProcessBuilderNonBlockingEx {

    public static void main(String[] args) throws InterruptedException,
            ExecutionException, TimeoutException, IOException {

        var executor = Executors.newSingleThreadExecutor();

        var processBuilder = new ProcessBuilder();

        processBuilder.command("cmd.exe", "/c", "ping -n 3 google.com");

        try {

            var process = processBuilder.start();

            System.out.println("processing ping command ...");
            var task = new ProcessTask(process.getInputStream());
            Future<List<String>> future = executor.submit(task);

            // non-blocking, doing other tasks
            System.out.println("doing task1 ...");
            System.out.println("doing task2 ...");

            var results = future.get(5, TimeUnit.SECONDS);

            for (String res : results) {
                System.out.println(res);
            }

        } finally {
            executor.shutdown();
        }
    }

    private static class ProcessTask implements Callable<List<String>> {

        private InputStream inputStream;

        public ProcessTask(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public List<String> call() {
            return new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .collect(Collectors.toList());
        }
    }
}
```

该程序创建一个在控制台上运行 ping 命令的进程。 它在`Executors.newSingleThreadExecutor()`方法的帮助下在单独的线程中执行。

```java
processing ping command ...
doing task1 ...
doing task2 ...

Pinging google.com [2a00:1450:4001:825::200e] with 32 bytes of data:
Reply from 2a00:1450:4001:825::200e: time=108ms 
Reply from 2a00:1450:4001:825::200e: time=111ms 
Reply from 2a00:1450:4001:825::200e: time=112ms 

Ping statistics for 2a00:1450:4001:825::200e:
    Packets: Sent = 3, Received = 3, Lost = 0 (0% loss),
Approximate round trip times in milli-seconds:
    Minimum = 108ms, Maximum = 112ms, Average = 110ms
```



这是输出。

## `ProcessBuilder`管道操作

管道是一种用于将信息从一个程序进程传递到另一个程序进程的技术。

```java
import java.io.File;
import java.io.IOException;

public class ProcessBuilderPipeEx {

    public static void main(String[] args) throws IOException {

        var homeDir = System.getProperty("user.home");

        var processBuilder = new ProcessBuilder();

        processBuilder.command("cmd.exe", "/c", "dir | grep [dD]o");

        processBuilder.directory(new File(homeDir));
        processBuilder.inheritIO().start();
    }
}
```

该示例通过管道（|）将信息从`dir`命令发送到`grep`命令。

```java
Volume in drive C is Windows
11/14/2018  06:57 PM    <DIR>          .dotnet
02/18/2019  10:54 PM    <DIR>          Documents
02/17/2019  01:11 AM    <DIR>          Downloads
```

This is the output.

在本教程中，我们使用 Java 的`ProcessBuilder`执行 OS 进程。



# 实际应用：Windows Java查看文件句柄

在Windows操作系统中，文件句柄（File Handle）是对文件或设备的引用，用于在程序中进行文件读写操作。Java作为一种跨平台的编程语言，在Windows上也提供了丰富的API以支持文件句柄的操作。本文将介绍如何使用Java在Windows系统中查看文件句柄，并提供相关的代码示例。

### 什么是文件句柄？

在计算机科学中，文件句柄是操作系统提供给应用程序的一种抽象概念，用于标识一个打开的文件或设备。应用程序可以通过文件句柄进行读写操作，而无需关心底层文件系统的细节。

在Windows操作系统中，每个进程都有一个与其关联的文件句柄表。当进程打开一个文件时，操作系统会为该文件分配一个文件句柄，并将其添加到进程的文件句柄表中。文件句柄通常是一个整数值，可以用于引用文件或设备。

### 为什么需要查看文件句柄？

在开发和调试过程中，我们有时需要查看当前进程打开的文件句柄列表，以了解应用程序对文件的使用情况。例如，我们可能想知道应用程序是否正确地关闭了所有打开的文件，或者某个文件是否被其他进程占用。

Java提供了一些API来访问操作系统的文件句柄信息，从而满足我们的需求。下面我们将介绍如何使用Java在Windows系统中查看文件句柄。

### 使用操作系统自带的工具

大多数操作系统都提供了一些工具来监控系统资源的使用情况，包括文件句柄。例如，

+ 在Linux系统中，我们可以使用`lsof`命令来查看当前打开的文件句柄。

+ 在Windows系统中，我们可以使用[handle命令](https://learn.microsoft.com/en-us/sysinternals/downloads/handle)来查看文件句柄的使用情况。

通过这些工具，我们可以及时发现和处理文件句柄泄露或过度消耗的问题。

### 使用Java获取文件句柄

Java通过`java.lang.ProcessBuilder`类提供了访问操作系统进程的能力。我们可以使用该类来执行Windows系统命令，进而获取文件句柄信息。下面的代码示例演示了如何使用Java获取当前进程的文件句柄信息：

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileHandleViewer {

    public static void main(String[] args) {
        try {
            // 执行命令获取文件句柄信息
            Process process = new ProcessBuilder("handle").start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            
            // 逐行读取输出信息
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            
            // 等待命令执行完成
            process.waitFor();
            reader.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

上述代码使用`ProcessBuilder`类执行了Windows系统命令`handle`，并读取命令的输出信息。`handle`命令是Sysinternals Suite工具集提供的一个命令行工具，用于显示进程的文件句柄信息。该工具可以从Microsoft官方网站下载并安装。

在上述代码中，我们通过`ProcessBuilder.start()`方法启动一个新的进程，并获取其输入流。然后，通过`BufferedReader`逐行读取进程的输出信息，并打印到控制台。最后，通过`process.waitFor()`等待命令执行完成，关闭输入流。

运行上述代码，即可获取当前进程的文件句柄信息。输出结果将包含文件句柄的类型、句柄值、访问权限等信息。

### 文件句柄关系图

下面是一个文件句柄关系图的示例：

```txt
erDiagram
    FILE_HANDLE }|..| FILE
    PROCESS }|..| FILE_HANDLE
    PROCESS }|..| THREAD
    THREAD }|..| FILE_HANDLE
```

在上述关系图中，`FILE_HANDLE`表示文件句柄，`FILE`表示文件，`PROCESS`表示进程，`THREAD`表示线程。文件句柄与文件之间是一对多的关系，进程与文件句柄之间是一对多的关系，进程与线程之间是一对多的关系，线程与文件句柄之间是一对多的关系。

### 总结

通过使用Java的`ProcessBuilder`类，我们可以执行Windows系统命令来获取文件句柄信息。这对于开发和调试