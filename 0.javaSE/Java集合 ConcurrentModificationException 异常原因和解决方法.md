# Java集合 ConcurrentModificationException 异常原因和解决方法



## 一. ConcurrentModificationException 异常出现的原因

　　先看下面这段代码：

```java
public class Test {
    public static void main(String[] args)  {
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(2);
        Iterator<Integer> iterator = list.iterator();
        while(iterator.hasNext()){
            Integer integer = iterator.next();
            if(integer==2)
                list.remove(integer);
        }
    }
}
```

 　运行结果：

![img](http://images0.cnblogs.com/blog/288799/201408/251050400793404.jpg)





　　从异常信息可以发现，异常出现在 `checkForComodification()` 方法中。

　　我们不忙看 checkForComodification() 方法的具体实现，我们先根据程序的代码一步一步看 ArrayList 源码的实现：

　　首先看 ArrayList 的 iterator() 方法的具体实现，查看源码发现在 ArrayList 的源码中并没有 iterator() 这个方法，那么很显然这个方法应该是其父类或者实现的接口中的方法，我们在其父类 AbstractList 中找到了 iterator() 方法的具体实现，下面是其实现代码：

```java
public Iterator<E> iterator() {
    return new Itr();
}
```

 　从这段代码可以看出返回的是一个指向 Itr 类型对象的引用，我们接着看 Itr 的具体实现，在 AbstractList 类中找到了 Itr 类的具体实现，它是 AbstractList 的一个成员内部类，下面这段代码是 Itr 类的所有实现：

```java
private class Itr implements Iterator<E> {
    int cursor = 0;
    int lastRet = -1;
    int expectedModCount = modCount;
    public boolean hasNext() {
           return cursor != size();
    }
    public E next() {
           checkForComodification();
        try {
        E next = get(cursor);
        lastRet = cursor++;
        return next;
        } catch (IndexOutOfBoundsException e) {
        checkForComodification();
        throw new NoSuchElementException();
        }
    }
    public void remove() {
        if (lastRet == -1)
        throw new IllegalStateException();
           checkForComodification();
 
        try {
        AbstractList.this.remove(lastRet);
        if (lastRet < cursor)
            cursor--;
        lastRet = -1;
        expectedModCount = modCount;
        } catch (IndexOutOfBoundsException e) {
        throw new ConcurrentModificationException();
        }
    }
 
    final void checkForComodification() {
        if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
    }
}
```

 　首先我们看一下它的几个成员变量：

　　`cursor`：表示下一个要访问的元素的索引，从 next() 方法的具体实现就可看出

　　`lastRet`：表示上一个访问的元素的索引

　　`expectedModCount`：表示对 ArrayList 修改次数的期望值，它的初始值为 modCount。

　　`modCount` 是 AbstractList 类中的一个成员变量

```java
protected transient int modCount = 0;
```

 　该值表示对 List 的修改次数，查看 ArrayList 的 add() 和 remove() 方法就可以发现，每次调用 add() 方法或者 remove() 方法就会对 modCount 进行加 1 操作。

　　好了，到这里我们再看看上面的程序：

　　当调用 list.iterator() 返回一个 Iterator 之后，通过 Iterator 的 hashNext() 方法判断是否还有元素未被访问，我们看一下 hasNext() 方法，hashNext() 方法的实现很简单：

```java
public boolean hasNext() {
    return cursor != size();
}
```

 　如果下一个访问的元素下标不等于 ArrayList 的大小，就表示有元素需要访问，这个很容易理解，如果下一个访问元素的下标等于 ArrayList 的大小，则肯定到达末尾了。

　　然后通过 Iterator 的 next() 方法获取到下标为 0 的元素，我们看一下 next() 方法的具体实现：

```java
public E next() {
    checkForComodification();
 try {
    E next = get(cursor);
    lastRet = cursor++;
    return next;
 } catch (IndexOutOfBoundsException e) {
    checkForComodification();
    throw new NoSuchElementException();
 }
}
```

 　这里是非常关键的地方：首先在 next() 方法中会调用 checkForComodification() 方法，然后根据 cursor 的值获取到元素，接着将 cursor 的值赋给 lastRet，并对 cursor 的值进行加 1 操作。初始时，cursor 为 0，lastRet 为 - 1，那么调用一次之后，cursor 的值为 1，lastRet 的值为 0。注意此时，modCount 为 0，expectedModCount 也为 0。

　　接着往下看，程序中判断当前元素的值是否为 2，若为 2，则调用 list.remove() 方法来删除该元素。

　　我们看一下在 ArrayList 中的 remove() 方法做了什么：



```java
public boolean remove(Object o) {
    if (o == null) {
        for (int index = 0; index < size; index++)
            if (elementData[index] == null) {
                fastRemove(index);
                return true;
            }
    } else {
        for (int index = 0; index < size; index++)
            if (o.equals(elementData[index])) {
                fastRemove(index);
                return true;
            }
    }
    return false;
}
 
 
private void fastRemove(int index) {
    modCount++;
    int numMoved = size - index - 1;
    if (numMoved > 0)
        System.arraycopy(elementData, index+1, elementData, index,
                numMoved);
    elementData[--size] = null; // Let gc do its work
}
```

 　通过 remove 方法删除元素最终是调用的 fastRemove() 方法，在 fastRemove() 方法中，首先对 modCount 进行加 1 操作（因为对集合修改了一次），然后接下来就是删除元素的操作，最后将 size 进行减 1 操作，并将引用置为 null 以方便垃圾收集器进行回收工作。

　　那么注意此时各个变量的值：对于 iterator，其 expectedModCount 为 0，cursor 的值为 1，lastRet 的值为 0。

　　对于 list，其 modCount 为 1，size 为 0。

　　接着看程序代码，执行完删除操作后，继续 while 循环，调用 hasNext 方法 () 判断，由于此时 cursor 为 1，而 size 为 0，那么返回 true，所以继续执行 while 循环，然后继续调用 iterator 的 next()方法：

　　注意，此时要注意 next() 方法中的第一句：checkForComodification()。

　　在 checkForComodification 方法中进行的操作是：



```java
final void checkForComodification() {
    if (modCount != expectedModCount)
    throw new ConcurrentModificationException();
}
```



 　如果 modCount 不等于 expectedModCount，则抛出 ConcurrentModificationException 异常。

　　很显然，此时 modCount 为 1，而 expectedModCount 为 0，因此程序就抛出了 ConcurrentModificationException 异常。

　　到这里，想必大家应该明白为何上述代码会抛出 ConcurrentModificationException 异常了。

　　关键点就在于：调用 list.remove() 方法导致 modCount 和 expectedModCount 的值不一致。

　　注意，像使用 for-each 进行迭代实际上也会出现这种问题。



## 二. 在单线程环境下的解决办法

　　既然知道原因了，那么如何解决呢？

　　其实很简单，细心的朋友可能发现在 Itr 类中也给出了一个 remove() 方法：

```java
public void remove() {
    if (lastRet == -1)
    throw new IllegalStateException();
       checkForComodification();
 
    try {
    AbstractList.this.remove(lastRet);
    if (lastRet < cursor)
        cursor--;
    lastRet = -1;
    expectedModCount = modCount;
    } catch (IndexOutOfBoundsException e) {
    throw new ConcurrentModificationException();
    }
}
```



 　在这个方法中，删除元素实际上调用的就是 list.remove() 方法，但是它多了一个操作：

```java
expectedModCount = modCount;
```

 　因此，在迭代器中如果要删除元素的话，需要调用 Itr 类的 remove 方法。

　　将上述代码改为下面这样就不会报错了：

```java
public class Test {
    public static void main(String[] args)  {
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(2);
        Iterator<Integer> iterator = list.iterator();
        while(iterator.hasNext()){
            Integer integer = iterator.next();
            if(integer==2)
                iterator.remove();   //注意这个地方
        }
    }
}
```

## 三. 在多线程环境下的解决方法

　　上面的解决办法在单线程环境下适用，但是在多线程下适用吗？看下面一个例子：

```java
public class Test {
    static ArrayList<Integer> list = new ArrayList<Integer>();
    public static void main(String[] args)  {
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        Thread thread1 = new Thread(){
            public void run() {
                Iterator<Integer> iterator = list.iterator();
                while(iterator.hasNext()){
                    Integer integer = iterator.next();
                    System.out.println(integer);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        };
        Thread thread2 = new Thread(){
            public void run() {
                Iterator<Integer> iterator = list.iterator();
                while(iterator.hasNext()){
                    Integer integer = iterator.next();
                    if(integer==2)
                        iterator.remove();  
                }
            };
        };
        thread1.start();
        thread2.start();
    }
}
```

 　运行结果：

![img](http://images0.cnblogs.com/blog/288799/201408/251432442989726.jpg)





　　有可能有朋友说 ArrayList 是非线程安全的容器，换成 Vector 就没问题了，实际上换成 Vector 还是会出现这种错误。



　　原因在于，虽然 Vector 的方法采用了 synchronized 进行了同步，但是实际上通过 Iterator 访问的情况下，每个线程里面返回的是不同的 iterator，也即是说 expectedModCount 是每个线程私有。假若此时有 2 个线程，线程 1 在进行遍历，线程 2 在进行修改，那么很有可能导致线程 2 修改后导致 Vector 中的 modCount 自增了，线程 2 的 expectedModCount 也自增了，但是线程 1 的 expectedModCount 没有自增，此时线程 1 遍历时就会出现 expectedModCount 不等于 modCount 的情况了。



　　因此一般有 2 种解决办法：

　　1）在使用 iterator 迭代的时候使用 synchronized 或者 Lock 进行同步；

　　2）使用并发容器 CopyOnWriteArrayList 代替 ArrayList 和 Vector。

