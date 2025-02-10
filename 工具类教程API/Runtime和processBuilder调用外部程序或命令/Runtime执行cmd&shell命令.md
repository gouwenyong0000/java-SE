

# 前言

每个 Java 应用程序都有一个 Runtime 类实例，使应用程序能够与其运行的环境相连接。可以通过 getRuntime 方法获取当前运行时。 应用程序不能创建自己的 Runtime 类实例。我们可以通过 Runtime.exec() 用来执行外部程序或命令

## 一、概述

**Java 中执行外部命令：ProcessBuilder 与 Runtime.exec()**

在 Java 中，`ProcessBuilder` 和 `Runtime.exec()` 都用于创建和管理操作系统进程，从而执行外部命令或程序。它们殊途同归，最终都依赖于 `Process` 类来控制子进程。

**共同点：**

- **进程创建:**  两者都启动一个新的操作系统进程来执行指定命令。
- `Process` 对象:  都返回一个 `Process` 对象，作为 Java 程序与子进程交互的句柄。通过 `Process` 对象，可以：
  - **I/O 流:**  获取子进程的标准输入 (`getOutputStream()`)、标准输出 (`getInputStream()`) 和标准错误流 (`getErrorStream()`)，实现父子进程间的通信。
  - **进程控制:**  等待子进程结束 (`waitFor()`)、获取退出代码 (`exitValue()`)、强制终止进程 (`destroy()`, `destroyForcibly()`)。
- **I/O 重定向:** 子进程没有独立的控制台，其标准 I/O 流会被重定向到父 Java 进程。
- **异步执行:** 子进程的生命周期独立于 `Process` 对象，即使 `Process` 对象被回收，子进程依然会在后台运行。

**不同点：**

- 参数处理:
  - `Runtime.exec()`:  参数形式较为灵活，可以接受：
    - **空格分隔的单个命令字符串:**  例如 `"cmd /c dir C:\\"`。
    - **字符串数组:**  命令和参数分别作为数组元素，例如 `{"cmd", "/c", "dir", "C:\\"}`。
  - **`ProcessBuilder`:**  构造器更结构化，接收 **字符串列表或数组**，首个元素为命令，后续元素为参数。例如 `new ProcessBuilder(List.of("cmd", "/c", "dir", "C:\\"))`。
- **底层实现:**  `Runtime.exec()`  内部最终也是 **委托给 `ProcessBuilder`** 来完成进程创建，`ProcessBuilder` 是更为底层和推荐的方式。

**`Process` 类的局限性：**

- **平台兼容性:**  `Process` 行为可能因操作系统而异，对于特定类型的进程（如窗口程序、守护进程、shell 脚本等）可能无法完美工作。
- **I/O 缓冲限制:**  操作系统对管道缓冲区大小的限制可能导致 I/O 操作阻塞甚至死锁，尤其是在子进程产生大量输出或需要大量输入时。父进程需要及时处理子进程的 I/O 流，避免缓冲区溢出。

**总结:**

`ProcessBuilder` 和 `Runtime.exec()` 都是 Java 执行外部命令的重要工具，`ProcessBuilder` 由于其更清晰的参数处理和底层实现，通常被认为是更现代和推荐的选择。使用 `Process` 类进行进程管理时，需要注意平台差异和潜在的 I/O 阻塞问题。



## **二、Runtime 演示示例**

### 1. 在 windows 下调用 dos 命令：

下面演示了在 windows 下执行 dos 命令 "chdir"，并将执行结果输出的示例。

```java
public class TestDos {  
  
    /** 
     * 在windows下执行dos命令并在console端输出 
     *  
     * @throws Exception  
     */  
    public static void main(String[] args) throws Exception {  
        String strCmd = "chdir";//待执行的dos命令(chdir命令作用是列出当前的工作目录)  
        Process process = Runtime.getRuntime().exec("cmd /k " + strCmd);//通过cmd程序执行cmd命令  
        //process.waitFor();  
        //读取屏幕输出  
        BufferedReader strCon = new BufferedReader(new InputStreamReader(process.getInputStream()));  
        String line;  
        while ((line = strCon.readLine()) != null) {  
            System.out.println(line);  
            }  
    }  
}
```



>如果不需要进行屏幕输出的话可以简写成如下方式：

```java
public class TestDos {  
  
    /** 
     * 在windows下调用dos命令 
     *  
     * @throws Exception  
     */  
    public static void main(String[] args) throws Exception {  
        String strCmd = "dos命令";//待执行的dos命令  
        Runtime.getRuntime().exec("cmd /c " + strCmd).waitFor();//通过cmd程序执行dos命令  
    }  
}
```



注：执行 dos 命令时，需在命令前加上 "cmd /x" 参数，其中 x 可以为 c 或者 k 值，具体说明如下：

cmd /c chdir 是执行完 dir 命令后关闭命令窗口。  
cmd /k chdir 是执行完 dir 命令后不关闭命令窗口。  
cmd /c start chdir 会打开一个新窗口后执行 dir 指令，原窗口会关闭。  
cmd /k start chdir 会打开一个新窗口后执行 dir 指令，原窗口不会关闭。

### 2. 在 windows 下调用外部程序：

下面演示了调用 QQ 程序的过程：

```java
/** 
 * 在windows下调用QQ程序示例 
 * */  
public class CallQQ {  
  
    /** 
     * @param args 
     * @throws Exception  
     */  
    public static void main(String[] args) throws Exception {  
        Runtime.getRuntime().exec("D:\\Program Files (x86)\\Tencent\\QQ\\QQProtect\\Bin\\QQProtect.exe");  
    }  
}
```



### 3. 在 Linux 下执行 shell 命令：

下面演示了在 Linux 中执行 shell 命令 pwd，并显示执行结果：

```java
/** 
 * 执行Linux的shell命令并在console端输出结果 
 */  
public class CallShell {  
  
    /** 
     * @throws Exception  
     */  
    public static void main(String[] args) throws Exception {  
        String strCmd = "pwd";//执行shell命令  
        Process process = Runtime.getRuntime().exec(strCmd);//通过执行cmd命令调用protoc.exe程序  
        BufferedReader strCon = new BufferedReader(new InputStreamReader(process.getInputStream()));  
        String line;  
        while ((line = strCon.readLine()) != null) {  
            System.out.println("java print:"+line);  
            }  
    }  
}
```



### 4. 在 Linux 下调用 shell 脚本并输出结果：



```java
/** 
 * 在linux下调用shell脚本并在console端输出脚本的执行结果 
 * */  
public class CallShell {  
  
    /** 
     * @throws Exception  
     */  
    public static void main(String[] args) throws Exception {  
        String strCmd = "/home/zhu/test/test.sh";//待调用shell脚本  
        Process process = Runtime.getRuntime().exec(strCmd);//通过执行cmd命令调用protoc.exe程序  
        BufferedReader strCon = new BufferedReader(new InputStreamReader(process.getInputStream()));  
        String line;  
        while ((line = strCon.readLine()) != null) {  
            System.out.println("java print:"+line);  
            }  
    }  
}
```



## 三、ProcessBuilder 使用示例

ProcessBuilder 的使用参考如下：

```java
String[] as = new String[]{“待执行命令 1”，"待执行命令 2"，.........};  
ProcessBuilder pb = new ProcessBuilder(as);  
pb.start();  
```

使用 ProcessBuilder 可以依次执行多个命令

### 查看目录树

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

```





### ping命令

```java
public class PingCommand {

    public static void main(String[] args) {
        String ipAddress = "www.google.com"; // 可以替换为您想要 ping 的 IP 地址或域名

        try {
            Process process = new ProcessBuilder("ping", ipAddress).start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("\nExited with error code : " + exitCode);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

### ipconfig正则ip

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetIPConfig {

    public static void main(String[] args) {
        try {
            String ipAddress = getLocalIPAddress();
            if (ipAddress != null) {
                System.out.println("本机 IP 地址: " + ipAddress);
            } else {
                System.out.println("无法获取本机 IP 地址。");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getLocalIPAddress() throws IOException, InterruptedException {
        Process process = new ProcessBuilder("ipconfig").start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK")); // 使用GBK编码，兼容中文环境
        String line;
        StringBuilder output = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            output.append(line).append(System.lineSeparator());
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            System.err.println("ipconfig 命令执行失败，退出代码: " + exitCode);
            return null;
        }

        return parseIPAddress(output.toString());
    }

    private static String parseIPAddress(String ipconfigOutput) {
        // 正则表达式匹配 IPv4 地址，适配不同语言的 "IPv4 地址" 字段
        Pattern pattern = Pattern.compile("IPv4 地址[\\s\\S]*?: ([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})");
        Matcher matcher = pattern.matcher(ipconfigOutput);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
```







Runtime.exec()  详解
====

## Runtime.exec() 

**1.  `Runtime.getRuntime().exec()` 返回 `Process` 对象:**

- `exec()` 方法是 `Runtime` 类的核心，用于创建操作系统进程并执行外部命令。
- 无论使用哪个重载版本的 `exec()`，都会返回一个 `Process` 对象，这是 Java 程序与子进程交互的桥梁。

**2.  `exec()` 方法的多种重载:**

- `Runtime.getRuntime().exec()` 提供了多个重载方法，以适应不同场景的参数传递需求。
- 你可以根据要执行的命令和参数的复杂程度，选择合适的重载版本。

**3.  平台差异：Windows vs. Linux 命令写法:**

- **命令语法不同:**  Windows 和 Linux 操作系统使用不同的命令语法和命令集。例如，Windows 使用 `dir`，Linux 使用 `ls`；Windows 使用 `ipconfig`，Linux 使用 `ifconfig` 或 `ip addr`。

- Shell 解释器:

  - **Linux:**  通常需要使用 Shell 解释器 (`/bin/sh`, `/bin/bash`) 来执行一些复杂的命令，特别是包含管道、重定向等 Shell 特性的命令。例如：

    ```java
    Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "javap -l xxx > output.txt"});
    ```

  - **Windows:**  可以使用 `cmd.exe` 命令解释器，例如：

    ```Java
    Runtime.getRuntime().exec(new String[]{"cmd.exe", "/C", "dir"}); // 使用 /C 执行完命令关闭窗口
    Runtime.getRuntime().exec(new String[]{"cmd.exe", "/K", "dir"}); // 使用 /K 执行完命令保留窗口
    ```

**4.  处理进程的输入/输出流 (I/O Streams) - 避免程序挂起:**

- **重要性:**  **必须**  及时读取子进程的标准输出流 (`getInputStream()`) 和标准错误流 (`getErrorStream()`)。
- **原因:**  操作系统为进程间的管道 (用于 I/O 流) 分配的缓冲区大小有限。如果父进程不及时读取子进程的输出，缓冲区被填满后，子进程可能会被 **阻塞 (挂起)**，导致 Java 程序也失去响应。
- 解决方法:
  - **多线程异步读取:**  最佳实践是使用 **独立的线程**  分别异步读取标准输出流和标准错误流。你提供的 `SerializeTask` 类就是一个很好的示例，它创建线程来专门处理输入流的读取和打印。
  - **及时关闭输出流:**  在不需要向子进程写入数据时，及时关闭父进程的输出流 (`process.getOutputStream().close()`)，避免子进程因等待输入而阻塞。

**5.  等待命令执行完成和获取返回值:**

- **`process.waitFor()`:**  调用 `Process.waitFor()` 方法会让 Java 程序 **同步等待** 子进程执行完成。
- **返回值 (`exitValue()`):**  `waitFor()` 方法返回子进程的 **退出代码 (返回值)**。
  - **`0`:**  通常表示命令执行 **成功**。
  - **非 `0`:**  通常表示命令执行 **失败** 或出现错误。
- **检查返回值:**  务必检查 `exitValue()`，根据返回值判断命令执行是否成功，并进行相应的错误处理。

**6.  资源管理：销毁进程:**

- **`process.destroy()`:**  在不再需要子进程时，应该调用 `process.destroy()` 方法 **显式地销毁 (终止)** 子进程，释放系统资源。
- **`finally` 块中销毁:**  通常在 `try-catch-finally` 结构的 `finally` 块中调用 `process.destroy()`，确保即使发生异常也能清理资源。

**7.  `Runtime.exec()` 的局限性 (需要注意):**

- **不完全等同于命令行:**  `Runtime.exec()` 并非完全等同于直接在命令行中执行命令。对于一些复杂的 Shell 特性 (例如，管道 `|`、重定向 `>`、`>>` 等)，直接将整个命令行字符串传递给 `exec(String command)`  可能会有问题。
- **重定向问题:**  像 `javap -l xxx > output.txt` 这样的重定向命令，直接使用 `exec(String command)` 可能无法正确执行。需要使用 `exec(String[] cmdarray)`  并借助 Shell 解释器 (如 `/bin/sh -c` 或 `cmd /c`) 来处理。举个例子:`javap -l xxx > output.txt`。这时要用到 exec 的第二种重载，即 input 参数为`String[]:Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","javap -l xxx > output.txt"});//通过bin/sh 解释执行该命令`
- **命令解析:**  `exec(String command)`  的参数解析依赖于底层的操作系统 Shell，可能存在平台差异和安全风险。

**8.  推荐的 `Runtime.exec()` 使用方式:**

- **优先使用 `exec(String[] cmdarray)` 重载版本:**  更安全、更可靠，避免 Shell 参数解析问题和命令注入风险.
- **对于复杂命令，借助 Shell 解释器:**  如果命令包含 Shell 特性 (管道、重定向等)，使用 `exec(String[] cmdarray)`  并结合 Shell 解释器 (`/bin/sh -c` 或 `cmd /c`) 来执行。
- **始终处理 I/O 流:**  使用多线程异步读取标准输出和错误流，避免程序挂起。
- **检查退出代码并处理错误:**  根据 `exitValue()` 判断命令执行结果，并处理错误情况。
- **及时销毁进程:**  在 `finally` 块中调用 `process.destroy()` 释放资源。

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RuntimeExecTest {

	@Test
    public  void test() {
	//linux  cmd命令
	Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","javap -l xxx > output.txt"});//通过bin/sh  解释执行该命令

		//Windows  cmd
//        String[] cmd = new String[]{"shutdown" ,"-s" ,"-t" ,"3600"};//定时关机
        String[] cmd = new String[]{"cmd" ,"-s" ,"-t" ,"3600"};//定时关机
//        String[] cmd = new String[]{"cmd.exe", "/C", "wmic process get name"};//

        // 输出aaa到1.txt  然后 使用记事本打开该文件
        String command = "cmd /c echo aaa >> d:\\1.txt && notepad d:\\1.txt";// && 命令之间需连接符连接
        Process process=null;
        try {
            process = Runtime.getRuntime().exec(command，null); 
            new Thread(new SerializeTask(process.getInputStream())).start();//处理
            new Thread(new SerializeTask(process.getErrorStream())).start();
            process.getOutputStream().close();
            int exitValue = process.waitFor();
            if (process.exitValue() != 0) {
                    System.out.println("error!");
             }
            System.out.println("返回值：" + exitValue);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            process.destroy();//关闭进程
        }

    }
}

/**
 * 打印输出线程
 */
class SerializeTask implements Runnable {
    private InputStream in;

    public SerializeTask(InputStream in) {
        this.in = in;
    }

    @Override
    public void run() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```





## CMD - OutputStream连续输入命令模式

```java
import java.io.*;
import java.util.Scanner;

public class Test1 {

    public static void main(String[] args) {

        try {
            Runtime runtime = Runtime.getRuntime();
            String[] cmds = {"cmd", "/k"};//此处必须用/k参数
            Process exec = runtime.exec(cmds);

            BufferedReader ResultErrorBuff = new BufferedReader(new InputStreamReader(exec.getErrorStream(), "gbk"));
            BufferedReader ResultSuccessBuff = new BufferedReader(new InputStreamReader(exec.getInputStream(), "gbk"));

            //处理回显
            new Thread(() -> processingReturnValues(ResultSuccessBuff)).start();
            new Thread(() -> processingReturnValues(ResultErrorBuff)).start();


            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(exec.getOutputStream(), "gbk"));
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("输入需要执行的命令>>>");
                String inCmd = scanner.nextLine();

                if (inCmd.equals("break")) {
                    bufferedWriter.write("exit");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    //等价于
                    exec.destroy();
                    break;
                }
                bufferedWriter.write(inCmd);
                bufferedWriter.newLine();//必须输入换行符  cmd默认按换行符 拆包
                bufferedWriter.flush();
            }
            System.out.println("exec.waitFor() = " + exec.waitFor());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
	//处理回显函数
    private static void processingReturnValues(BufferedReader ResultErrorBuff) {
        try {
            String line = null;
            while ((line = ResultErrorBuff.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (ResultErrorBuff != null) {
                try {
                    ResultErrorBuff.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
```

# ProcessBuilder 详解

## 概述

ProcessBuilder 类是在 J2SE 1.5 版本中引入的 `java.lang` 包中的一个类，它为 Java 程序员提供了一种创建和管理操作系统进程的强大方法。在 J2SE 1.5 之前，进程的管理主要依赖于 `Process` 类。`ProcessBuilder` 的出现，使得进程的创建和管理更加灵活和方便。

**ProcessBuilder 的主要功能和特点：**

- **进程创建与管理:** `ProcessBuilder` 允许 Java 应用程序启动新的操作系统进程，并对这些进程进行管理。这包括设置进程的属性、启动进程以及获取进程的输入、输出和错误流。
- **属性配置:**  每个 `ProcessBuilder` 实例都管理一组进程属性，这些属性在进程启动时被应用。主要的进程属性包括：
  - **命令 (command):**  指定要执行的外部程序及其参数。命令被表示为一个字符串列表，列表的第一个元素通常是要执行的程序文件，后续元素是程序的参数。命令的具体格式和有效性取决于操作系统。
  - **环境 (environment):**  表示进程的环境变量。`ProcessBuilder` 允许你修改子进程的环境变量，初始环境是当前 Java 进程环境的一个副本。你可以通过 `environment()` 方法获取和修改环境变量的映射。
  - **工作目录 (working directory):**  指定子进程的工作目录。默认情况下，子进程的工作目录与当前 Java 进程的工作目录相同，通常根据系统属性 user.dir 来命名。你可以使用 `directory()` 方法来设置子进程的工作目录。
  - **错误流重定向 (redirectErrorStream):**  控制子进程的标准错误输出流的处理方式
    - 当 `redirectErrorStream` 为 `false` (默认值) 时，子进程的标准输出和标准错误输出会被分别发送到独立的流，可以通过 `Process.getInputStream()` 和 `Process.getErrorStream()` 方法访问。
    - 当 `redirectErrorStream` 设置为 `true` 时，标准错误输出会与标准输出合并。合并后的数据可以通过 `Process.getInputStream()` 方法读取，而 `Process.getErrorStream()` 返回的流会直接到达文件末尾。这在需要关联标准输出和标准错误输出时非常有用。
- **start() 方法:**  `ProcessBuilder` 的 `start()` 方法负责利用配置好的属性创建一个新的 `Process` 实例，并启动一个新的操作系统进程。同一个 `ProcessBuilder` 实例可以多次调用 `start()` 方法，以创建多个具有相同或相似属性的子进程。



## `ProcessBuilder` 和 `Process` 两个类对比

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



### **如何创建 Process 对象？**  

一般有两种方法：

*   使用命令名和命令的参数选项构造 ProcessBuilder 对象，它的 start 方法执行命令，启动一个进程，返回一个 Process 对象。
*   Runtime.exec() 方法创建一个本机进程，并返回 Process 子类的一个实例。





## `ProcessBuilder`详细

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

### `ProcessBuilder`运行程序

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



### `ProcessBuilder`命令输出

以下示例执行命令并显示其输出。

```java
public class ProcessBuilderEx {

    public static void main(String[] args) throws IOException {

        var processBuilder = new ProcessBuilder();
        processBuilder.command("cal", "2019", "-m 2");
        var process = processBuilder.start();

        try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))){

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

### `ProcessBuilder`重定向输出

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

### `ProcessBuilder`重定向输入和输出

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

### `ProcessBuilder`继承 IO

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

### `ProcessBuilder`环境

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

### `ProcessBuilder`目录

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

### `ProcessBuilder`非阻塞操作

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



## 实际应用：Windows Java查看文件句柄

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

#  cmd 解释器执行命令详解

### 概述

`cmd.exe` (命令提示符) 是 Windows 操作系统上的命令行解释器。它用于执行用户输入的命令，从而允许用户通过文本命令与操作系统进行交互。以下是 `cmd.exe` 处理和执行命令的详细步骤：

**1. 命令行输入与解析 (Command Line Input and Parsing):**

- **用户输入 (User Input):**  您在命令提示符 (例如 `C:\>`) 下键入命令。此命令行可以包含命令本身、参数、选项（开关）和重定向运算符。
- **解析 (Parsing):** `cmd.exe` 解析命令行以理解其组成部分。它将命令行分解为由空格或分隔符分隔的各个标记（单词）。
  - **命令识别 (Command Identification):**  第一个标记通常被识别为要执行的命令。这可能是内部命令、可执行文件或批处理文件。
  - **参数提取 (Argument Extraction):**  后续的标记被视为命令的参数或形参。
  - **选项识别 (Option Recognition):**  选项或开关通常以 `/` 或 `-` 为前缀，后跟字母或单词（例如 `/s`、`-v`）。`cmd.exe` 识别这些选项并将它们传递给命令。
  - **重定向运算符 (Redirection Operators):**  符号如 `>`、`<`、`>>`、`2>`、`&>` 是重定向运算符。 `cmd.exe` 解释这些符号以重定向标准输入、标准输出和标准错误流。
  - **管道 (Pipes):** 管道符号 `|` 将一个命令的标准输出连接到另一个命令的标准输入，从而创建命令管道。

**2. 命令类型判断 (Command Type Determination):**

解析之后，`cmd.exe` 确定要执行的命令类型：

- **内部命令 (Internal Commands / Built-in Commands):** 这些命令直接在 `cmd.exe` 自身内部实现。示例包括 `dir`、`cd`、`echo`、`copy`、`del`、`type`、`cls`、`set`、`mkdir`、`rmdir`、`pause`、`exit`、`help` 等。
  - 内部命令由 `cmd.exe` 直接执行，无需启动单独的进程。它们通常更快更高效。
- **外部命令 (External Commands / Executable Files):** 这些程序存储为单独的可执行文件（例如 `.exe`、`.com`、`.bat`、`.cmd`）。
  - 当遇到外部命令时，`cmd.exe` 需要定位可执行文件。它按以下顺序搜索可执行文件：
    1. **当前目录 (Current Directory):** `cmd.exe` 首先检查可执行文件是否位于当前工作目录中。
    2. **系统路径 (System Path / PATH Environment Variable):** 如果在当前目录中未找到，`cmd.exe` 将搜索 `PATH` 环境变量中列出的目录。`PATH` 变量是一个系统范围的环境变量，用于指定可执行文件所在的目录列表。
    3. **应用程序路径 (App Paths / Registry):** 在某些情况下，`cmd.exe` 也可能检查 "应用程序路径 (App Paths)" 注册表项，其中可能包含应用程序可执行文件的路径。
  - 一旦找到可执行文件，`cmd.exe` 会创建一个新进程来执行它。
- **批处理文件 (.bat, .cmd / Batch Files):** 这些是包含一系列 `cmd.exe` 命令的文本文件。
  - 当批处理文件被指定为命令时，`cmd.exe` 会逐行读取并执行批处理文件中的命令。

**3. 命令执行 (Command Execution):**

- **内部命令执行 (Internal Command Execution):** 对于内部命令，`cmd.exe` 直接使用其内置功能执行命令。
- **外部命令执行 (External Command Execution):** 对于外部命令，`cmd.exe` 执行以下步骤：
  1. **进程创建 (Process Creation):** `cmd.exe` 为外部命令创建一个新进程。
  2. **环境设置 (Environment Setup):** 新进程继承 `cmd.exe` 进程的环境，包括环境变量（可以使用 `set` 命令修改）。
  3. **命令行传递 (Command Line Passing):** `cmd.exe` 将命令及其参数作为命令行传递给新进程。
  4. **标准输入/输出/错误处理 (Standard Input/Output/Error Handling):** `cmd.exe` 为新进程设置标准输入 (stdin)、标准输出 (stdout) 和标准错误 (stderr) 流。 默认情况下：
     - **stdin:** 连接到 `cmd.exe` 控制台输入（键盘）。
     - **stdout:** 连接到 `cmd.exe` 控制台输出（屏幕）。
     - **stderr:** 连接到 `cmd.exe` 控制台输出（屏幕），或在指定时重定向。
  5. **进程执行 (Process Execution):** 操作系统进程调度程序接管并在新进程中执行外部命令。
  6. **等待完成（可选）(Waiting for Completion - Optional):** 默认情况下，`cmd.exe` 等待外部命令进程完成执行，然后提示输入下一个命令。但是，可以使用 `start` 命令或在命令末尾附加 `&` （在某些上下文中，例如批处理文件）在后台运行命令。
  7. **退出代码检索 (Exit Code Retrieval):** 外部命令完成后，`cmd.exe` 从进程中检索退出代码（返回代码）。退出代码指示命令是否成功执行（通常 0 表示成功，非零表示错误）。可以使用批处理脚本中的 `ERRORLEVEL` 环境变量访问退出代码。
- **批处理文件执行 (Batch File Execution):** 对于批处理文件，`cmd.exe` 顺序读取并执行命令。它将每一行作为单独的命令处理，并对批处理文件中的每个命令执行步骤 1-3。批处理文件还可以包含控制流语句，如 `if`、`for`、`goto` 等，以创建更复杂的脚本。

**4. 输出与错误处理 (Output and Error Handling):**

- **标准输出 (stdout / Standard Output):** 命令生成的通常被认为是“正常”输出。默认情况下，它显示在 `cmd.exe` 控制台上。
- **标准错误 (stderr / Standard Error):** 命令生成的指示错误或警告的输出。默认情况下，它也显示在 `cmd.exe` 控制台上，通常与标准输出的显示方式相同。
- **重定向 (Redirection):** `cmd.exe` 允许您重定向这些流：
  - `>`: 将标准输出重定向到文件，如果文件存在则覆盖文件。 (`command > output.txt`)
  - `>>`: 将标准输出重定向到文件，如果文件存在则追加到文件。 (`command >> output.txt`)
  - `<`: 从文件重定向标准输入。 (`command < input.txt`)
  - `2>`: 将标准错误重定向到文件，覆盖文件。 (`command 2> error.txt`)
  - `2>>`: 将标准错误重定向到文件，追加到文件。 (`command 2>> error.txt`)
  - `&>` 或 `1>` 或 `1>>`: 将标准输出 (1) 和标准错误 (2) 重定向到同一位置。 (`command &> output.txt` 或 `command 1> output.txt 2>&1` - 较旧的语法）。
- **管道 (`|` / Piping):** 将一个命令的标准输出连接到管道中下一个命令的标准输入。这允许您将命令链接在一起，其中一个命令的输出成为下一个命令的输入。 (`command1 | command2 | command3`)

**5. 环境变量 (Environment Variables):**

- `cmd.exe` 使用环境变量来存储配置信息，命令和程序可以访问这些信息。
- **系统环境变量 (System Environment Variables):** 系统范围设置，所有用户和进程都可用。
- **用户环境变量 (User Environment Variables):** 为特定用户帐户设置。
- **命令提示符环境变量 (Command Prompt Environment Variables):** 可以使用 `set` 命令在 `cmd.exe` 会话中设置和修改。这些仅对当前的 `cmd.exe` 会话及其子进程有效。
- `cmd.exe` 中常用的环境变量包括：
  - `PATH`: 指定搜索可执行文件的目录。
  - `TEMP` 或 `TMP`: 指定临时文件的目录。
  - `USERNAME`: 当前用户的用户名。
  - `COMPUTERNAME`: 计算机名称。
  - `OS`: 操作系统名称。
  - `PROCESSOR_ARCHITECTURE`: 处理器架构（例如 x86、AMD64）。
  - `ERRORLEVEL`: 存储上次执行命令的退出代码。

**6. 命令语法和帮助 (Command Syntax and Help):**

- `cmd.exe` 命令具有特定的语法规则。您可以使用 `/`? 或 `-help` 选项（如果命令支持）或使用 `help` 命令本身来获取命令语法的帮助。
  - `command /?` 或 `help command`
- `help` 命令提供内部 `cmd.exe` 命令的列表和简要说明。

**总结 (In Summary):**

`cmd.exe` 是一个强大的工具，用于通过命令与 Windows 操作系统进行交互。它解析命令行，确定命令类型（内部、外部、批处理），执行命令，管理进程，处理输入/输出流，并使用环境变量。理解这些步骤对于有效地使用命令提示符和编写批处理脚本至关重要。

为了获得更详细的信息，您可以在线搜索以下资源：

- **Microsoft 的命令行参考 (Microsoft's Command-Line Reference):** Microsoft 提供了关于 `cmd.exe` 命令的官方文档。在 Microsoft 网站上搜索 "Windows 命令行参考" 或 "CMD 命令"。
- **网站和教程 (Websites and Tutorials):** 许多在线网站和教程详细解释了 `cmd.exe` 命令处理，通常带有示例。搜索 "cmd.exe 命令执行"、"命令提示符工作原理" 或 "批处理脚本教程"。

希望以上解释能够帮助您理解 `cmd.exe` 命令解释器执行命令的详细过程。



### 实操

部分 cmd 命令需要 cmd 解释器来执行【在系统内不存在相应 notepad.exe ping.exe ，winrar【需要提前配置环境变量】等具体可执行文件】，所以 java Runtime 执行 dir 会报错，，需要使用`cmd /c dir`  
`/c` : 打开命令窗口执行完毕自动关闭  
`/k`：打开命令窗口执行完毕不自动关闭

* `cmd /c dir` 是执行完dir命令后关闭命令窗口。
* `cmd /k dir` 是执行完dir命令后不关闭命令窗口。
* `cmd /c start dir` 会打开一个新窗口后执行dir指令，原窗口会关闭。
* `cmd /k start dir` 会打开一个新窗口后执行dir指令，原窗口不会关闭。

> 可以通过 win+R 运行窗口打开测试 notepad 类型的命令是在系统内存在`notepad d:\1.txt`  
> ![](image/Runtime执行cmd&shell命令/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBAamF2YV9tb25rZXlfMTEw,size_20,color_FFFFFF,t_70,g_se,x_16.png)  
> ![](image/Runtime执行cmd&shell命令/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBAamF2YV9tb25rZXlfMTEw,size_20,color_FFFFFF,t_70,g_se,x_16-1673195575008-3.png)  
> ![](image/Runtime执行cmd&shell命令/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBAamF2YV9tb25rZXlfMTEw,size_20,color_FFFFFF,t_70,g_se,x_16-1673195577052-6.png)



# Apache Common-Exec 

强烈建议使用 apache 的第三方库，该库提供了更加详细的设置和监控方法等等。

执行的命令被称为 CommandLine，可使用该类的 addArgument() 方法为其添加参数，parse() 方法将你提供的命令包装好一个可执行的命令。命令是由执行器 Executor 类来执行的，DefaultExecutor 类的 execute() 方法执行命令，exitValue 也可以通过该方法返回接收。设置 ExecuteWatchdog 可指定进程在出错后多长时间结束，这样有效防止了 run-away 的进程。此外 common-exec 还支持异步执行，Executor 通过设置一个 ExecuteResultHandler 类，该类的实例会接收住错误异常和退出代码。

```java
CommandLine cmdLine = new CommandLine("AcroRd32.exe");
cmdLine.addArgument("/p");
    cmdLine.addArgument("/h");
    cmdLine.addArgument("${file}");
    HashMap map = new HashMap();
    map.put("file", new File("invoice.pdf"));
    commandLine.setSubstitutionMap(map);

    DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

    ExecuteWatchdog watchdog = new ExecuteWatchdog(60*1000);
    Executor executor = new DefaultExecutor();
    executor.setExitValue(1);
    executor.setWatchdog(watchdog);
    executor.execute(cmdLine, resultHandler);

    // some time later the result handler callback was invoked so we
    // can safely request the exit value
    int exitValue = resultHandler.waitFor();
```



# java shell命令工具类

```java
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * java shell命令工具类
 * 借助OutputStream  执行多条命令的组合  注意以\n  表示结束
 * 
 * javac JavaShellUtil.java -encoding utf-8
 */
public class JavaShellUtil {

    public static String lineSeparator = System.getProperty("line.separator");
    public static String COMMAND_SH = "sh";
    public static String COMMAND_EXIT = "exit\n";
    public static String COMMAND_LINE_END = "\n";

    static {
        if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
            System.out.println("window");
            COMMAND_SH = "cmd";
        } else {
            System.out.println("unix");
        }

    }

    public static void main(String[] args) {

        //启动一个Process  ，借助outputstream输出多条命令
        System.out.println(JavaShellUtil.execCommand("dir").toString());
        System.out.println(JavaShellUtil.execCommand("ls -l").toString());
        //System.out.println(JavaShellUtil.execCommand("ping www.baidu.com").toString());
        System.out.println(JavaShellUtil.execCommand("aapt v").toString());
        System.out.println(JavaShellUtil.execCommand("aapt.exe").toString());

    }

    public static CommandResult execCommand(String command) {
        return execCommand(new String[]{command}, true);
    }

    public static CommandResult execCommand(String command, boolean isNeedResultMsg) {
        return execCommand(new String[]{command}, isNeedResultMsg);
    }

    public static CommandResult execCommand(List<String> commands, boolean isNeedResultMsg) {
        return execCommand(commands == null ? null : commands.toArray(new String[]{}), isNeedResultMsg);
    }

    /**
     * execute shell commands
     * {@link CommandResult#result} is -1, there maybe some excepiton.
     *
     * @param commands     command array
     * @param needResponse whether need result msg
     */
    public static CommandResult execCommand(String[] commands, final boolean needResponse) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, null, "空命令");
        }

        Process process = null;

        final StringBuilder successMsg = new StringBuilder();
        final StringBuilder errorMsg = new StringBuilder();

        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec(COMMAND_SH);
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) {
                    continue;
                }
                // donnot use os.writeBytes(commmand), avoid chinese charset error
                os.write(command.getBytes());
                os.writeBytes(COMMAND_LINE_END);
                os.flush();
            }
            os.writeBytes(COMMAND_EXIT);
            os.flush();

            
            //启动两个线程,解决process.waitFor()阻塞问题
            final BufferedReader successReadBuff = new BufferedReader(new InputStreamReader(process.getInputStream()));
            final BufferedReader errorReadBuff = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            new Thread(new Runnable() {
                public void run() {
                    try {
                        if (needResponse) {
                            String s;
                            while ((s = successReadBuff.readLine()) != null) {
                                successMsg.append(s);
                                successMsg.append(lineSeparator);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        if (needResponse) {
                            String s;
                            while ((s = errorReadBuff.readLine()) != null) {
                                errorMsg.append(s);
                                errorMsg.append(lineSeparator);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            result = process.waitFor();
            if (errorReadBuff != null) {
                errorReadBuff.close();
            }
            if (successReadBuff != null) {
                successReadBuff.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (process != null) {
                    process.destroy();//关闭进程
                }
            }

        }
        return new CommandResult(result, successMsg == null ? null : successMsg.toString(), errorMsg == null ? null
                : errorMsg.toString());
    }

    public static class CommandResult {

        public int result;
        public String responseMsg;
        public String errorMsg;

        public CommandResult(int result) {
            this.result = result;
        }

        public CommandResult(int result, String responseMsg, String errorMsg) {
            this.result = result;
            this.responseMsg = responseMsg;
            this.errorMsg = errorMsg;
        }

        @Override
        public String toString() {
            return "CommandResult{" +
                    "errorMsg='" + errorMsg + '\'' +
                    ", result=" + result +
                    ", responseMsg='" + responseMsg + '\'' +
                    '}';
        }
    }
}
```

# Java解惑82 

如果你用一行命令行带上参数 slave 去运行这个程序，它会打印什么呢？如果你使用的命令行不带任何参数，它又会打印什么呢

```java
public class BeerBlast {

    static final String COMMAND = "java BeerBlast slave";

    public static void main(String[] args) throws Exception {

        if (args.length == 1 && args[0].equals("slave")) {

            for (int i = 99; i > 0; i--) {
                System.out.println(i + " bottles of beer on the wall");
                System.out.println(i + " bottles of beer");
                System.out.println("You take on down, pass it around,");
                System.out.println((i - 1) + " bottles of beer on the wall");
                System.out.println();
            }
        } else {
            // Master
            Process process = Runtime.getRuntime().exec(COMMAND);
            int exitValue = process.waitFor();
            System.out.println("exit value = " + exitValue);
        }
    }
}
```



解惑：

如果你使用参数 slave 来运行该程序，它就会打印出那首激动人心的名为”99 Bottles of Beer on the Wall”的童谣的歌词，这没有什么神秘的。如果你不使用该参数来运行这个程序，它会启动一个 slave 进程来打印这首歌谣，但是你看不到 slave 进程的输出。主进程会等待 slave 进程结束，然后打印出 slave进程的退出值(exit value)。根据惯例，0 值表示正常结束，所以 0 就是你可能期望该程序打印的东西。如果你运行了程序，你可能会发现该程序只会悬挂在那里，不会打印任何东西，看起来 slave 进程好像永远都在运行着。所以你可能会觉得你应该一直都能听到”99 Bottles of Beer on the Wall”这首童谣，即使是这首歌被唱走调了也是如此，但是这首歌只有 99 句，而且，电脑是很快的，你假设的情况应该是不存在的，那么这个程序出了什么问题呢？

这个秘密的线索可以在 Process 类的文档中找到，它叙述道：“由于某些本地平台只提供有限大小的缓冲，所以如果未能迅速地读取子进程(subprocess)的输出流，就有可能会导致子进程的阻塞，甚至是死锁” [Java-API]。这恰好就是这里所发生的事情：没有足够的缓冲空间来保存这首冗长的歌谣。为了确保 slave进程能够结束，父进程必须排空(drain)它的输出流，而这个输出流从 master线程的角度来看是输入流。下面的这个工具方法会在后台线程中完成这项工作：

```java
static void drainInBackground(final InputStream is) {
    new Thread(new Runnable(){
            public void run(){
            try{
                while( is.read() >= 0 );
            } catch(IOException e){
            // return on IOException
            }
    }
}).start();
```

如果我们修改原有的程序，在等待 slave 进程之前调用这个方法，程序就会打印出 0：

```java
}else{ // Master
Process process = Runtime.getRuntime().exec(COMMAND);
drainInBackground(process.getInputStream());
int exitValue = process.waitFor();
System.out.println("exit value = " + exitValue);
}
```

这里的教训是：**为了确保子进程能够结束，你必须排空它的输出流；对于错误流（error stream）也是一样**，而且它可能会更麻烦，因为你无法预测进程什么时候会倾倒（dump）一些输出到这个流中。在 5.0 版本中，加入了一个名为ProcessBuilder 的类用于排空这些流。它的 redirectErrorStream 方法将各个流合并起来，所以你只需要排空这一个流**。如果你决定不合并输出流和错误流，你必须并行地（concurrently）排空它们**。试图顺序化地（sequentially）排空它们会导致子进程被挂起。多年以来，很多程序员都被这个缺陷所刺痛。这里对于 API 计者们的教训是，Process 类应该避免这个错误，也许应该自动地排空输出流和错误流，除非用户表示要读取它们。更一般的讲，API 应该设计得更容易做出正确的事，而很难或不可能做出错误的事。



# FQA一些注意事项：

**Java中Process和Runtime()使用，以及调用cmd命令阻塞在process.waitfor( )的问题解决**

在java中调用php程序，由于有很多控制台输出，导致一直阻塞在process.waitfor( )，只有强制终止java程序后，结果文件才会输出。根据下面两个博客内容成功解决。

用Java编写应用时，有时需要在程序中调用另一个现成的可执行程序或系统命令，这时可以通过组合使用Java提供的Runtime类和Process类的方法实现。下面是一种比较典型的程序模式：

```java
　　Process process = Runtime.getRuntime().exec("p.exe");
　　process.waitfor( );
```

在上面的程序中，第一行的“p.exe”是要执行的程序名；Runtime.getRuntime()返回当前应用程序的Runtime对象，该对象的exec()方法指示Java虚拟机创建一个子进程执行指定的可执行程序，并返回与该子进程对应的Process对象实例。通过Process可以控制该子进程的执行或获取该子进程的信息。第二条语句的目的等待子进程完成再往下执行。但在windows平台上，如果处理不当，有时并不能得到预期的结果。

下面是笔者在实际编程中总结的几种需要注意的情况：

　　1、执行DOS的内部命令如果要执行一条DOS内部命令，有两种方法。一种方法是把命令解释器包含在exec()的参数中。例如，执行dir命令，在NT上，可写成`exec ("cmd.exe /c dir")，`在windows 95/98下，可写成“`command.exe/c dir`”，**其中参数“/c”表示命令执行后关闭Dos立即关闭窗口**。另一种方法是，把内部命令放在一个批命令my_dir.bat文件中，在Java程序中写成exec("my_dir.bat")。如果仅仅写成exec("dir")，Java虚拟机则会报运行时错误。前一种方法要保证程序的可移植性，需要在程序中读取运行的操作系统平台，以调用不同的命令解释器。后一种方法则不需要做更多的处理。

　　　2、打开一个不可执行的文件打开一个不可执行的文件，但该文件存在关联的应用程序，则可以有两种方式。以打开一个word文档a.doc文件为例，Java中可以有以下两种写法：

```java
exec("start a.doc");

exec(" c:\\Program Files\\MicrosoftOffice\\office winword.exe a.doc");
```

显然，前一种方法更为简捷方便。

　　　3、**执行一个有标准输出的DOS可执行程序在windows 平台上，运行被调用程序的DOS窗口在程序执行完毕后往往并不会自动关闭，从而导致Java应用程序阻塞在waitfor( )。导致该现象的一个可能的原因是，该可执行程序的标准输出比较多，而运行窗口的标准输出缓冲区不够大。解决的办法是，利用Java提供的Process 类提供的方法让Java虚拟机截获被调用程序的DOS运行窗口的标准输出，在waitfor()命令之前读出窗口的标准输出缓冲区中的内容。**

一段典型的程序如下：

```java
String str;
Process process =Runtime.getRuntime().exec("cmd /c dir windows");
BufferedReader bufferedReader = newBufferedReader( new InputStreamReader(process.getInputStream()));
while ( (str=bufferedReader.readLine()) !=null) { System.out.println(str); 　}

process.waitfor(); 
```

示例这里换成

```java
public static boolean  resize(String   pic,String   picTo,int width,int height)  {

    boolean result = true;
    String cmd = "cmd /c  convert -sample " + width + "x" + height + "   "" + pic + """ +"   "" + picTo+""";
    log.debug(cmd);
    try {
        Process process = Runtime.getRuntime().exec(cmd);
        if (process.getErrorStream().read() != -1) {
            result = false;
            process.destroy();
        }
    } catch (IOException e) {
        log.debug("creat icon pic fail!" + e);
        result = false;
    }

    /*BufferedReader bufferedReader = new BufferedReader( newInputStreamReader(process.getInputStream());
        while ( (str=bufferedReader.readLine()) != null)System.out.println(str); 　 */
    return result;

}
```

我使用上面的程序处理不好使。然后通过搜索相关文章看到了如下内容。问题被解决。^-^

```java
Process process = Runtime.getRuntime.exec(cmd); // 执行调用命令

InputStream is = process.getInputStream(); // 获取对应进程的输出流
BufferedReader br = new Buffered(new InputStreamReader(is)); // 缓冲读入
StringBuilder buf = new StringBuilder(); // 保存对应进程的输出结果流
String line = null;
while((line = br.readLine()) != null) buf.append(line); // 循环等待进程结束
System.out.println("ffmpeg输出内容为：" + buf);
……
```

 本来一般都是这样来调用程序并获取进程的输出流的，但是我在windows上执行这样的调用的时候却总是在while那里被堵塞了，结果造成ffmpeg程序在执行了一会后不再执行，这里从官方的参考文档中我们可以看到这是由于缓冲区的问题，由于java进程没有清空ffmpeg程序写到缓冲区的内容，结果导致ffmpeg程序一直在等待。在网上也查找了很多这样的问题，不过说的都是使用单独的线程来进行控制，我也尝试过很多网是所说的方法，可一直没起什么作用。下面就是我的解决方法了，注意到上述代码中的红色部分了么？这里就是关键，我把它改成如下结果就可以正常运行了。

```java
InputStream is = process.getErrorStream(); // 获取ffmpeg进程的输出流
```

 注意到没？我把它改成获取错误流这样进程就不会被堵塞了，而我之前一直想的是同样的命令我手动调用的时候可以完成，而java调用却总是完成不了，一直认为是getInputStream的缓冲区没有被清空，不过问题确实是缓冲区的内容没有被清空，但不是getInputStream的，而是getErrorStream的缓冲区，这样问题就得到解决了。所以我们在遇到java调用外部程序而导致线程阻塞的时候，可以考虑使用两个线程来同时清空process获取的两个输入流，如下这段程序：

```java
……
Process p = Runtime.getRuntime().exec("php.exe test.php");
//Process p = Runtime.getRuntime().exec("cmd.exe /c dir");
final InputStream is1 = p.getInputStream();

new Thread(new Runnable() {
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(is1));
        try {
            while (br.readLine() != null) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}).start(); // 启动单独的线程来清空p.getInputStream()的缓冲区

InputStream is2 = p.getErrorStream();
BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
StringBuilder buf = new StringBuilder(); // 保存输出结果流
String line = null;
while ((line = br2.readLine()) != null) buf.append(line); //
System.out.println("输出结果为：" + buf);
……
```

 通过这样我们使用一个线程来读取process.getInputStream()的输出流，使用另外一个线程来获取process.getErrorStream()的输出流，这样我们就可以保证缓冲区得到及时的清空而不担心线程被阻塞了。当然根据需要你也可以保留process.getInputStream()流中的内容，这个就看调用的程序的处理了。

假如源码内发现用了大量System.err.print，需要使用getErrorStream()捕捉！关于System.err和System.out的区别，可以参考别的日志。这两个流走的是不同的管道。所以需要分别捕捉。



# exec方法envp设置系统环境变量参数

因为没有设置到Python的系统环境变量，所以执行Python脚本时，要设置

```java
public static void main(String[] args) {
	String[] cmdarray = new String[] { "cmd", "/c", "python D:\\python2\\test.py"};
	String[] envp = new String[] {"path=D:\\Anaconda3\\envs\\leantwo"};
	try {
		Process process = Runtime.getRuntime().exec(cmdarray, envp);
		BufferedReader in = new BufferedReader(
		new InputStreamReader(process.getInputStream()));
		String line = null;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
		}
		in.close();
		int re = process.waitFor();
		System.out.println(re);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
```



### 前言

环境变量这个概念不陌生， 就是操作系统的环境变量。

系统变量就是java本身维护的变量。 通过 System.getProperty 的方式获取。

对于不同的操作系统来说， 环境变量的处理可能会有一些不统一的地方， 比如说： 不区分大小写 等等。

### Java 获取环境变量

Java 获取环境变量的方式很简单：

```java
//System.getEnv()  得到所有的环境变量
//System.getEnv(key) 得到某个环境变量的值

Map map = System.getenv();
	Iterator it = map.entrySet().iterator();
	while(it.hasNext())	{
		Entry entry = (Entry)it.next();
		System.out.print(entry.getKey()+"=");
		System.out.println(entry.getValue());
	}
```

如果是windows 系统， 打印出来的值通过从 “我的电脑” 里看到的环境变量是一样的。

### Java 获取和设置系统变量

Java 获取环境变量的方式也很简单：

```java
//System.getProperties()  得到所有的系统变量
//System.getProperty(key)  得到某个系统变量的值	

Properties properties = System.getProperties();
	Iterator it =  properties.entrySet().iterator();
	while(it.hasNext()){
		Entry entry = (Entry)it.next();
		System.out.print(entry.getKey()+"=");
		System.out.println(entry.getValue());
	}
```

 系统变量除了可以获取之外， 还可以通过System.setProperty(key, value)  的方式设置自己需要的系统变量。


默认情况下， java 设置了哪些系统变量：

```
java.version Java 运行时环境版本
java.vendor Java 运行时环境供应商
java.vendor.url Java 供应商的 URL
java.home Java 安装目录
java.vm.specification.version Java 虚拟机规范版本
java.vm.specification.vendor Java 虚拟机规范供应商
java.vm.specification.name Java 虚拟机规范名称
java.vm.version Java 虚拟机实现版本
java.vm.vendor Java 虚拟机实现供应商
java.vm.name Java 虚拟机实现名称
java.specification.version Java 运行时环境规范版本
java.specification.vendor Java 运行时环境规范供应商
java.specification.name Java 运行时环境规范名称
java.class.version Java 类格式版本号
java.class.path Java 类路径
java.library.path 加载库时搜索的路径列表
java.io.tmpdir 默认的临时文件路径
java.compiler 要使用的 JIT 编译器的名称
java.ext.dirs 一个或多个扩展目录的路径
os.name 操作系统的名称
os.arch 操作系统的架构
os.version 操作系统的版本
file.separator 文件分隔符(在 UNIX 系统中是"/")
path.separator 路径分隔符(在 UNIX 系统中是":")
line.separator 行分隔符(在 UNIX 系统中是"/n")
user.name 用户的账户名称
user.home 用户的主目录
user.dir 用户的当前工作目录
```



补充

1. 在.bat ;  .cmd 或  .sh 中会通过 set 的方式设置一些变量，

比如weblogic 的 setDomainEnv.cmd

set SUN_JAVA_HOME=C:\Oracle\Middleware\jdk160_21

这里设置的是环境变量

2. 在log4j 的配置中， 有时会配置log file 的产生路径。 

比如 ${LOG_DIR}/logfile.log， 这里的LOG_DIR 是被系统属性的变量替换的。

3. 看一下java 源码， 通过 System.getProperties() 的方式获取系统变量时， 会有一个安全的检查

```java
public static Properties getProperties() {
SecurityManager sm = getSecurityManager();
    if (sm != null) {
    sm.checkPropertiesAccess();
}

return props;
}
```

在 单个Java application  测试时， System 里的SecurityManager 是空的。
在Applet 运行时， 会结合 .policy 这个文件来检查权限。

如果给一个空的SecurityManager 会发现就会抛出权限异常。

```java
public static void main(String[] args) {
	// TODO Auto-generated method stub
	System.setSecurityManager(new SecurityManager());
	//SecurityManager sm = System.getSecurityManager();
	//System.out.println(sm);
	System.getSecurityManager().checkPropertiesAccess();
}
```



## 环境变量不生效排查

+ 环境变量未生效  通过**检查打印当时环境变量**
+ **检查环境变量是否配置**或者i**dea重启，生效变量**