# 构建块 Building Blocks
## 同步集合 Synchronized Collections
### 同步集合中出现的问题
同步集合都是现成安全的，但是对于复杂操作，有时需要使用额外的客户端加锁进行保护。
<br>复杂的操作通常包括：
- 迭代（反复获取元素，直到获取最后一个元素）；
- 导航（根据一定的顺序寻找下一元素）；
- 条件运算。

在同一个同步集合中，这些复杂操作即使没有客户端加锁的保护，技术上来讲也是线程安全的，但是当其他线程能够并发的对集合内容进行修改时，他们可能就不会按照你的期望工作了。
造成迭代不可靠的问题同样可以通过客户端加锁来解决，这要增加一些针对可伸缩行的开销。

### 迭代器与并发修改异常
一些我们不希望在迭代期间加锁的原因，包括当其他线程需要访问集合时，必须等待到迭代结束。如果集合内容很多，可能需要等待较长的时间。同样的，调用其他函数还要持有另一个锁，容易引发死锁。即使没有饥饿或死锁的风险，在相当长的时间把内容锁住，也破坏了程序额可伸缩性，一定程度上影响了程序的性能。
<br>在迭代期间，对容器加锁的一个替代方案是复制集合。因为复制是线程限制的，没有其他线程能够在迭代期间对其进行修改，这样就消除了并发异常修改。

### 隐藏迭代器
标准的集合类调用toString，hashCode，equals方法会间接调用迭代，遍历集合中的每一个元素，所有对迭代间接的调用都会引发并发修改异常。
正如封装一个对象的状态，能够使他更加容易的保持约束不变一样，封装他的同步则可以迫使他符合他的同步策略。

## 并发集合 Concurrent Collections
同步集合通过对容器所有状态的串行化访问，从而实现了线程安全，这样的代价是削弱了并发性，当多个线程共同竞争容器级的锁时，吞吐量会大大降低。并发集合就是为了并发访问而设计的。
> concurrentMap替代了Map<br> 
> copyOnWriteArrayList代替了List<br>
> concurrentSkipListMap和concurrentSkipListSet替代了SortedMap和SortedSet

### ConcurrentHashMap
ConcurrentHashMap与HashMap一样是哈希表，但是使用了不同的锁策略，来提供更好的并发性和可伸缩性。在ConcurrentHashMap以前，程序使用一个公共的锁同步每一个方法，并且严格的限制同时只有一个线程可以访问集合。ConcurrentHashMap使用一种更细粒度话锁机制——分离锁，这个机制允许更深层次的共享访问，任意数量的读线程可以并发的访问Map，读写可以并发访问Map，并且有限制数量的写线程并发修改Map。

### CopyOnWriteArrayList
CopyOnWriteArrayList是同步List的一个并发替代品，通常情况下提供了更好的并发性，同时避免了在迭代期间对容器的加锁和复制。
<br>写时复制集合的线程安全源自于，只要有效的不可变对象被正确的发布，那么访问他就不需要额外的同步。在需要修改时，会重新创建并发布一个修改后的副本，以此来实现可变性。
<br>每次修改时复制数组需要一定的开销，特别是当集合内容比较多时，当对容器迭代操作的频率远高于对容器修改的频率时，使用写时复制集合是个比较合理的选择。


## 阻塞队列与生产者消费者模式 BlockingQueue and Producer-Consumer Pattern
生产消费者模式是围绕阻塞队列设计的，生产者把数据放入队列，并使数据可用，当消费者准备就绪后会从队列中获取数据。生产者并不知道消费者，甚至根本没有消费者，他们只讲数据放入消费队列，同理消费者也是一样。

### 连续的线程限制
在JUC包中实现的阻塞队列，全部都包含了充分的内部同步，从而安全的将对象从生产者发布至消费者。
<br>对于可变对象，生产者-消费者设计与阻塞队列一起，为生产者和消费者之间的移交对象所有权提供连续的线程限制。一个线程约束的对象完全由单一线程所有，但是所有权通过安全的发布被"转移"，这样其他线程中只有唯一一个能访问得到这个对象的权限，并且保证移交之后原线程不能在访问得到。安全发布确保了对象状态对新的所有者完全可见，并且原始的所有者不会再触及他。
<br>对象池拓展了连续的线程限制，把对象"借给"一个请求线程，只要对象池中包含了充足的内部同步，使用对象池安全发布，并且客户端本身不会发布线程池，或者在返回对象池后不再继续使用，所有权可以在线程中安全的传递。

### 双端队列与窃取工作
双端队列使他们与一种叫做窃取工作（work stealing）的模式相关联。一个生产者消费者设计中，所有的消费者共享一个工作队列。在窃取工作设计中，每个消费者都有自己的双端队列。一个消费者完成自己队列中的全部工作后，可以偷取其他双端队列的末尾任务。因为工作者线程不会竞争一个共享的任务队列，所以窃取工作模式比传统的生产者消费者模式具有更好的伸缩性。

## 阻塞与可中断的方法
线程可能因为几种原因被阻塞或暂停：
- 等待I/O操作结束
- 等待获取一个锁
- 等待Thread.sleep唤醒
- 等待另一个线程的计算结果

当一个线程阻塞时，他通常被挂起，并且设置成线程阻塞的某个状态。当外部事件发生后，线程被置回Runnable状态，重新获得调度机会。

Thread提供了interrupt方法，用来中断一个线程，或者查询某个线程是否被中断。每一个线程都有一个bool属性，这个属性代表着线程的中断状态，中断线程时需要这个值。

中断是一种协作机制。一个线程不能迫使其他线程停止正在做的事情，或者去做其他的事情。

当你在代码中调用了一个抛出InterruptedException的方法时，你得方法就成为了一个阻塞方法，要为响应中断做好准备。有两个选择可以做：
1. 传递InterruptedException。如果你能避开异常的话，通常是最好的选择——只需要把Interrupted传递给你得调用者。
2. 恢复中断。当你不抛出InterruptedException时，你必须自己捕获异常，并且在当前线程中调用interrupt从中断中恢复，这样调用栈中更高层的代码可以发现中断已经发生。

## Synchronizer
Synchronizer是一个对象，它根据本身的状态调节线程的控制流。阻塞队列可以作为一个Synchronizer，其他的还包括：信号量（Semaphore），关卡（barrier），闭锁（latch）。
<br>所有synchronizer都享有类似的结构特征：他们封装状态，而这些状态决定着线程执行到在某一点时是通过还是被迫等待，他们有提供操作状态的方法，以及高效的等待Synchronizer进入到期望状态的方法。

### 闭锁 Latch
latch是一个synchronizer，他可以延迟线程的进度直到线程到达终止状态。latch可以确保特定的活动直到其他活动完成后才执行。
- 确保一个计算不会执行，直到他需要的资源被初始化
- 确保一个服务不会开始，直到其他的依赖服务都已经开始
- 等待，直到活动的所有部分都为继续处理做好了准备

CountDownLatch是一个灵活的闭锁，允许一个或多个线程等待一个事件集的发生。countDown方法对计数器做减法，表示一个事件已经发生，而await方法等待计数器到达0，此时所有需要等待的事件都已经发生。如果计数器入口时的值是非0的，await会一直阻塞直到计数器为0，或者等待线程中断以及超时。

### FutureTask
FutureTask同样可以作为闭锁。（FutureTask的实现描述了一个抽象的可携带计算结果的任务）。FutureTask的计算是通过Callable实现的，他等价于一个可以携带任何计算结果的Runnable，并且由3个状态：等待，运行和完成。完成包括所有计算以任意方式结束，包括正常、异常和取消。一旦FutureTask进入这个状态，将永远停留在这个状态。

### 信号量 Semaphore
计数器信号量（Counting Semaphore）用来限制能够同时访问某些特定资源的活动的数量，或同时执行某给定操作的数量。计数器信号量可以用来实现资源池或一个给定边界的容器。

### 关卡 Barrier
关卡类似于闭锁，它能够阻塞一个线程直到某些事件发生。barrier与latch的不同之处在于，所有线程必须**同时**到达关卡点才能继续处理。latch等待的是事件，barrier等待的是线程。
<br> 关卡通常用来模拟这种情况：一个步骤的计算可以并行完成，但是要求必须完成所有与每一个步骤相关的工作后才能进入下一步。