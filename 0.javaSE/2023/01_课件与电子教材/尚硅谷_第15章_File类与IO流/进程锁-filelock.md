> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [developer.aliyun.com](https://developer.aliyun.com/article/378193)

# FileLock 类

Java 提供了文件锁 FileLock 类，利用这个类可以控制不同程序 (JVM) 对同一文件的并发访问，实现进程间文件同步操作。 

FileLock 是 Java 1.4 版本后出现的一个类，它可以通过对一个可写文件 (w) 加锁，保证同时**只有一个进程可以拿到文件的锁**，这个进程从而可以对文件做访问；而其它拿不到锁的进程要么选择被挂起等待，要么选择去做一些其它的事情， 这样的机制保证了众进程可以顺序访问该文件。

也可以看出，能够利用文件锁的这种性质，在一些场景下，虽然我们不需要操作某个文件， 但也可以通过 FileLock 来进行并发控制，保证进程的顺序执行，避免数据错误。  

“Locks are associated with files, not channels. Use locks to coordinate with external processes, not between threads in the same JVM.”  



1. 概念  

  + 共享锁: 共享读操作，但只能一个写（读可以同时，但写不能）。共享锁防止其他正在运行的程序获得重复的独占锁，但是允许他们获得重复的共享锁。  
  + 独占锁: 只有一个读或一个写（读和写都不能同时）。独占锁防止其他程序获得任何类型的锁。

2. `FileLock FileChannel.lock(long position, long size, boolean shared)`  

  shared 的含义: 是否使用共享锁, 一些不支持共享锁的操作系统, 将自动将共享锁改成排它锁。可以通过调用 isShared() 方法来检测获得的是什么类型的锁。

3. lock() 和 tryLock() 的区别： 

  + lock() 阻塞的方法，锁定范围可以随着文件的增大而增加。无参 lock() 默认为独占锁；有参 lock(0L, Long.MAX_VALUE, true) 为共享锁。 
  + tryLock() 非阻塞, 当未获得锁时, 返回 null.

4. FileLock 的生命周期：在调用 FileLock.release(), 或者 Channel.close(), 或者 JVM 关闭

5. FileLock 是线程安全的

6. 注意事项：  

  同一进程内，在文件锁没有被释放之前，不可以再次获取。即在 release() 方法调用前, 只能 lock() 或者 tryLock() 一次。  
  文件锁的效果是与操作系统相关的。一些系统中文件锁是强制性的，就当 Java 的某进程获得文件锁后，操作系统将保证其它进程无法对文件做操作了。而另一些操作系统的文件锁是询问式的 (advisory)，意思是说要想拥有进程互斥的效果，其它的进程也必须也按照 API 所规定的那样来申请或者检测文件锁，不然将起不到进程互斥的功能。所以文档里建议将所有系统都当做是询问式系统来处理，这样程序更加安全也更容易移植。  
  如何避免死锁：在读写关键数据时加锁，操作完成后解锁；一次性申请所有需要的资源，并且在申请不成功的情况下放弃已申请到的资源。



Java 文件锁定一般都通过 FileChannel 来实现。主要涉及如下 2 个方法：  

`tryLock() throws IOException`　　试图获取对此通道的文件的独占锁定。  

`tryLock(long position, long size, boolean shared) throws IOException`　　试图获取对此通道的文件给定区域的锁定。  

tryLock 等同于 tryLock(0L, Long.MAX_VALUE, false) ，它获取的是独占锁，所以一定是在释放锁之后，才能读取到文件内容。

# 写文件的时候加锁:

 Thread_writeFile.java

```java
import java.io.File;
 import java.io.IOException;
 import java.io.RandomAccessFile;
 import java.nio.channels.FileChannel;
 import java.nio.channels.FileLock;
 import java.util.Calendar;
 public class Thread_writeFile extends Thread{
     public void run(){
         Calendar calstart=Calendar.getInstance();
         File file=new File("D:/test.txt");        
         try {
             if(!file.exists()){
                 file.createNewFile();
             }
             //对该文件加锁
             RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
             FileChannel fileChannel=randomAccessFile.getChannel();
             FileLock fileLock=null;
             while(true){  
                 try {
                     fileLock = fileChannel.tryLock();
                     break;
                 } catch (Exception e) {
                      System.out.println("有其他线程正在操作该文件，当前线程"+Thread.currentThread().getName()+"休眠1000毫秒"); 
                      sleep(1000);  
                 }
             }
             for(int i=1;i<=1000;i++){
                 sleep(10);
                 StringBuffer sb=new StringBuffer();
                 sb.append("这是第"+i+"行对应的数据");
                 sb.append("\n");
                 randomAccessFile.write(sb.toString().getBytes("utf-8"));
             }
             fileLock.release();
             fileChannel.close();
             randomAccessFile.close();
             randomAccessFile=null;
         } catch (Exception e) {
             e.printStackTrace();
         }
         Calendar calend=Calendar.getInstance();
         	System.out.println("线程:"+Thread.currentThread().getName()+",写文件共花了"+(calend.getTimeInMillis()-calstart.getTimeInMillis())+"秒");
     }
 }
```

 测试类: Thread_writeTest

```java
public class Thread_writeTest {
     public static void main(String[] args) {
         Thread_writeFile writeFileThread=new Thread_writeFile();
         writeFileThread.setName("writeFileThread");
         Thread_writeFile writeFileThread2=new Thread_writeFile(); 
         writeFileThread2.setName("writeFileThread2");
         writeFileThread.start();  
         writeFileThread2.start();  
     }
 }
```

 运行结果:

```
有其他线程正在操作该文件，当前线程writeFileThread休眠1000毫秒
有其他线程正在操作该文件，当前线程writeFileThread休眠1000毫秒
有其他线程正在操作该文件，当前线程writeFileThread休眠1000毫秒
有其他线程正在操作该文件，当前线程writeFileThread休眠1000毫秒
有其他线程正在操作该文件，当前线程writeFileThread休眠1000毫秒
有其他线程正在操作该文件，当前线程writeFileThread休眠1000毫秒
有其他线程正在操作该文件，当前线程writeFileThread休眠1000毫秒
有其他线程正在操作该文件，当前线程writeFileThread休眠1000毫秒
有其他线程正在操作该文件，当前线程writeFileThread休眠1000毫秒
有其他线程正在操作该文件，当前线程writeFileThread休眠1000毫秒
有其他线程正在操作该文件，当前线程writeFileThread休眠1000毫秒
线程:writeFileThread2,写文件共花了10021秒
线程:writeFileThread,写文件共花了21026秒
```



# 读取文件的时候加锁:

Thread_readFile.java

```java
import java.io.File;
 import java.io.FileNotFoundException;
 import java.io.IOException;
 import java.io.RandomAccessFile;
 import java.nio.channels.FileChannel;
 import java.nio.channels.FileLock;
 import java.util.Calendar;
 public class Thread_readFile extends Thread{  
     public void run(){  
         try {  
             Calendar calstart=Calendar.getInstance();  
             sleep(5000);  
             File file=new File("D:/test.txt");      
             //给该文件加锁  
             RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw"); 
             FileChannel fileChannel=randomAccessFile.getChannel();  
             FileLock fileLock=null;  
             while(true){  
                 try {
                     fileLock = fileChannel.tryLock();
                     break;
                 } catch (Exception e) {
                      System.out.println("有其他线程正在操作该文件，当前线程"+Thread.currentThread().getName()+"休眠1000毫秒"); 
                      sleep(1000);  
                 }
             }  
             byte[] buf = new byte[1024];  
             StringBuffer sb=new StringBuffer();  
             while((randomAccessFile.read(buf))!=-1){                  
                 sb.append(new String(buf,"utf-8"));      
                 buf = new byte[1024];  
             }  
             System.out.println(sb.toString());  
             fileLock.release();  
             fileChannel.close();  
             randomAccessFile.close();  
             randomAccessFile=null;  
             Calendar calend=Calendar.getInstance();  
             System.out.println("当前线程:"+Thread.currentThread().getName()+",读文件共花了"+(calend.getTimeInMillis()-calstart.getTimeInMillis())+"秒");  
        
         } catch (Exception e) {  
             e.printStackTrace();  
         }  
     }  
 }
```



 测试类: Thread_readTest.java

```java
public class Thread_readTest {
     public static void main(String[] args) {
         Thread_readFile readFileThread=new Thread_readFile();
         readFileThread.setName("readFileThread");
         Thread_readFile writeFileThread2=new Thread_readFile(); 
         writeFileThread2.setName("readFileThread2");
         readFileThread.start();  
         writeFileThread2.start();  
     }
 }
```

运行结果:

```
有其他线程正在操作该文件，当前线程readFileThread休眠1000毫秒
这是第1行对应的数据
这是第2行对应的数据
当前线程:readFileThread2,读文件共花了5009秒
这是第1行对应的数据
这是第2行对应的数据
当前线程:readFileThread,读文件共花了6009秒
```



# 另外一个例子

```java
import java.io.FileNotFoundException;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.io.RandomAccessFile;
 import java.nio.ByteBuffer;
 import java.nio.channels.FileChannel;
 import java.nio.channels.FileLock;
 import java.util.Date;
 public class FileLockTest {
     public static void main(String[] args){
         FileChannel channel = null;
         FileLock lock = null;
         try {
             //1. 对于一个只读文件通过任意方式加锁时会报NonWritableChannelException异常
             //2. 无参lock()默认为独占锁，不会报NonReadableChannelException异常，因为独占就是为了写
             //3. 有参lock()为共享锁，所谓的共享也只能读共享，写是独占的，共享锁控制的代码只能是读操作，当有写冲突时会报NonWritableChannelException异常
             channel = new FileOutputStream("test.txt",true).getChannel();
             RandomAccessFile raf = new RandomAccessFile("test.txt","rw");
             //在文件末尾追加内容的处理
             raf.seek(raf.length());
             channel = raf.getChannel();
             //获得锁方法一：lock()，阻塞的方法，当文件锁不可用时，当前进程会被挂起
             lock = channel.lock();//无参lock()为独占锁
             //lock = channel.lock(0L, Long.MAX_VALUE, true);//有参lock()为共享锁，有写操作会报异常
             //获得锁方法二：trylock()，非阻塞的方法，当文件锁不可用时，tryLock()会得到null值
             //do {
             //  lock = channel.tryLock();
             //} while (null == lock);
             //互斥操作
             ByteBuffer sendBuffer=ByteBuffer.wrap((new Date()+" 写入\n").getBytes());
             channel.write(sendBuffer);
             Thread.sleep(5000);
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         } catch (InterruptedException e) {
             e.printStackTrace();
         } finally {
             if (lock != null) {
                 try {
                     lock.release();
                     lock = null;
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
             if (channel != null) {
                 try {
                     channel.close();
                     channel = null;
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         }
     }
 }
```

