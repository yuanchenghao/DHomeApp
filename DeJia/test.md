webp和png区别
webp解码速度快
压缩率高 转换后体积小
同样支持无损压缩和有损压缩 可以保证图片质量

约束布局和相对布局
相同层级下 线性布局好于约束布局和相对布局 带有weight权重的线性和relativelayout存在多次渲染
不同层级下 相对布局和约束布局可以有限减少层级 层级相同相对布局优于约束布局

内存泄漏 溢出OOM LeakCanary
指对象生命周期本该结束的时候 还被一系列的引用 导致内存的泄漏 随着泄漏的积累 到达可分配的内存最大值是就会造成OOM
LeakCanary
监听 Activity 的生命周期
在 onDestroy 的时候，创建相应的 Refrence 和 RefrenceQueue，并启动后台进程去检测
一段时间之后，从 RefrenceQueue 读取，若读取不到相应 activity 的 Refrence，有可能发生泄露了，
这个时候，再触发 gc，一段时间之后，再去读取，若在从 RefrenceQueue 还是读取不到相应 activity 的 refrence，可以断定是发生内存泄露了
发生内存泄露之后，dump，分析 hprof 文件，找到泄露路径（使用 haha 库分析）
注意: 如果一个对象要被GC回收了，会把它引用的对象放入到ReferenceQueue中。

    LeakCanary 1.5.1 检测内存泄漏原理：
    在Activity destroy后将Activity的弱引用关联到ReferenceQueue中，这样Activity将要被GC前，会出现在ReferenceQueue中。
    随后，会向主线程的MessageQueue添加一个IdleHandler，用于在idle时触发一个发生在HandlerThread的等待5秒后开始检测内存泄漏的代码。
    这段代码首先会判断是否对象出现在引用队列中，如果有，则说明没有内存泄漏，结束。
    否则，调用Runtime.getRuntime().gc()进行GC，等待100ms后再次判断是否已经出现在引用队列中，若还没有被出现，那么说明有内存泄漏，开始dump hprof。
    解析hprof文件用的是HAHA
    
    LeakCanary 2.0 beta 3 检测内存泄漏原理：
    在Activity destroy后将Activity的弱引用关联到ReferenceQueue中，这样Activity将要被GC前，会出现在ReferenceQueue中。
    随后，会向主线程的抛出一个5秒后执行的Runnable，用于检测内存泄漏。
    这段代码首先会将引用队列中出现的对象从观察对象数组中移除，然后再判断要观察的此对象是否存在。若不存在，则说明没有内存泄漏，结束。
    否则，就说明可能出现了内存泄漏，会调用Runtime.getRuntime().gc()进行GC，等待100ms后再次根据引用队列判断，若仍然出现在引用队列中，那么说明有内存泄漏，此时根据内存泄漏的个数弹出通知或者开始dump hprof。
    解析hprof文件用的是Shark  无需调用初始化方法 AppWatcherInstaller继承ContentProvider app构建过程中会合并到唯一清单文件中 自动执行install方法
    
内存泄漏 oom如何线上线下监控
https://www.jianshu.com/p/aaea40364fd3

Android 开发中 Serializable 和 Parcelable 的使用和区别

Serializable序列化JavaSE本身就支持
Serializable在序列化的时候会产生大量的临时变量，从而引起频繁的GC
Serializable接口非常简单，声明一下就可以了
如果将数据存储到磁盘，尽管Serializable效率低点，但此时还是建议使用Serializable 。

Parcelable序列化Android特有功能
效率比实现Serializable接口高效
实现Parcelable接口稍微复杂一些，但效率更高
Parcelable不能使用在要将数据存储在磁盘上的情况

bundle传输的数据类型 
String,基本数据类型，被序列化的对象（实现Serialiable和Parcelable）

类加载机制
加载
    1）通过类的全限定名来获取定义此类的二进制字节流
    2）将这个字节流所代表的静态存储结构转化为方法区的运行时数据结构
    3）在内存中生成一个代表这个类的java.lang.Class对象，作为这个类的各种数据的方位入口。
连接
    验证：主要是对类中的语法结构是否合法进行验证，确认类型符合Java语言的语义。
    准备：这个阶段是给类中的类变量分配内存，设置默认初始值，比如一个静态的int变量初始值是0，布尔变量初始值是false。
    解析：在类型的常量池中寻找类，接口，字段和方法的符号引用，把这些符号引用替换成直接引用的过程。
初始化  
    初始化阶段才真正到了类中定义的java代码的阶段，
    在这个阶段会对类中的变量和一些代码块进行初始化，比如以类变量进行初始化，
    在准备阶段对类变量进行的默认初始化，到这个阶段就对对变量进行显式的赋值，
    其中静态代码块就是在这个阶段来执行的
    
类加载机制也叫双亲委托模式 具体点儿体现在ClassLoader的LoadClass方法 

    1.调用findLoadedClass()看看该类是否已经加载过了
    2.如果还没有加载，调用parent.loadClass()方法
    3.如果该类仍然没有被加载，调用自身findClass(String)进行加载
    4.如果加载过了return
    
使用双亲委托模式的好处
1.可以避免重复加载
2.考虑安全因素，可以避免恶意程序替代原api。
类加载机制在热修复方面是运用 
如腾讯bugly BaseDexClassLoader的findClass()---pathList.findClass()---DexPathList.findClass()
查找指定类的时候会按顺序遍历dexElements数组 找到立刻返回 由于双亲委托的存在，不会重复加载同一个class
因此 我们只要保证修复bug的class文件比原文件先一步加载就可以实现热更新了

jvm虚拟机模型 gc
gc原理 怎么记？怎么回收？3M内存分配1M为啥oom？
见笔记

view绘制流程（什么时候获取mWidth?什么时候获取width）和事件分发机制（特定场景下分发过程已经如何解决滑动冲突）
viewroot的performTraversals方法会依次调用performMeasure、performLayout、performDraw三个方法。在这三个方法中分别完成顶级view的measure、layout和draw流程。已performMeasure为例，其中会调用measure方法，在measure方法中又调用onMeasure方法，onMeasure方法则会对所有的子view进行measure，这样就完成依次measure过程。接着子元素会重复父容器的measure过程，如此反复完成整个view树的遍历。同理另两个方法传递流程与此类似。

activity-window-decoeView-viewgroup-view dispatchTouchEvent onInterceptTouchEvent(viewgroup) onTouchEvent

handler源码相关
https://github.com/Xiaxiao-1024/

Kotlin data和Javabean 区别
会自动生成get set hashcode equals 带参数的构造方法 

Kotlin协程和线程区别
本质上属于对Java Thread api的封装 协程的suspend挂起本质上就是开启了一个子线程去执行任务 不会阻塞原Thread
协程通过编译时通过插入相关代码  线程需要系统支持 通过调度CPU生效
异步代码同步化。还有就是优化了调度管理

kotlin拓展函数实现
通过反编译的代码可以看出，其实 Kotlin 的扩展函数并没有修改原有类，
而是在自己的类中生成了一个静态的方法,当我们调用扩展函数时,编译器将会调用自动生成的函数并且把当前的对象传入

kotlin in 和 out 
父类泛型对象可以赋值给子类泛型对象，用in；
子类泛型对象可以赋值给父类泛型对象，用out。

kotlin object
对象声明
用于声明一个类，该类自动实现单例模式。不能被赋值。
对象表达式
如果我们只需要“一个对象而已”，并不需要一个类class 

okhttp原理（缓存拦截器？应用拦截器？缓存？那个拦截器建立socket连接）

Glide原理（怎么缓存怎么管理生命周期 子线程能不能用？加载大图）

retrofit原理 用了哪些设计模式

hashMap spareArray 查找时间复杂度

多线程

网络 tcp三次握手？四次挥手？https怎么实现网络安全的？交换秘钥过程

组件化（拦截器原理）

热修复（资源文件如何热修复）

优化

anr 原因和排查
因为主线程被耗时操作阻塞了，主线程无法对下一个操作进行响应才会ANR，没有需要响应的操作自然就不会产生ANR
ANR的类型
KeyDispatch Timeout ：按键或触摸事件在特定时间内无响应。超时时间5秒。超时时间是在ActivityManagerService类中定义的。
Broadcast Timeout ：BroadcastReceiver在特定时间内无法处理完成。前台广播10秒，后台广播60秒。超时时间是在ActivityManagerService类中定义的。
ContentProvider Timeout ：ContentProvider在特定的时间内没有完成发布。超时时间10秒。超时时间是在ActivityManagerService类中定义的。
Service Timeout ：Service在特定的时间内生命周期函数无法处理完成。前台服务20秒，后台服务200秒。超时时间是在ActiveServices类中定义的。
造成ANR的常见原因

应用在主线程上进行长时间的计算。
应用在主线程上执行耗时的I/O的操作。
主线程处于阻塞状态，等待获取锁。
主线程与其他线程之间发生死锁。
主线程在对另一个进程进行同步Binder调用，而后者需要很长时间才能返回。(如果我们知道调用远程方法需要很长时间，我们应该避免在主线程调用)

ANR原因排查
并且会在/data/anr/目录下输出一个traces.tx文件，该文件记录了ANR的更加详细的信息，我们可以导出分析

lanuncher点击APP启动过程  系统启动流程 

线程池原理 线程安全问题（安全问题比较多）

用过哪些设计模式？场景？设计模式六大原则

卡顿分析？原因？方法？线上线下监控？
主线程有耗时操作 到不到1秒60帧 
一、Looper日志检测卡顿
Looper.getMainLooper().setMessageLogging(printer)，即可从回调中拿到Handler处理一个消息的前后时间。
在后台开一个线程，定时获取主线程堆栈，将时间点作为key，堆栈信息作为value，保存到Map中，在发生卡顿的时候，取出卡顿时间段内的堆栈信息即可。
不过此方法会造车内存抖动 频繁调用会创造大量对象 并且在子线程获取主线程堆栈信息也会造成主线程暂停
二、使用Choreographer.FrameCallback
Android系统每隔16ms发出VSYNC信号，来通知界面进行重绘、渲染，每一次同步的周期约为16.6ms，代表一帧的刷新频率。
通过Choreographer类设置它的FrameCallback函数，当每一帧被渲染时会触发回调FrameCallback.doFrame (long frameTimeNanos) 函数。frameTimeNanos是底层VSYNC信号到达的时间戳 。
通过 ChoreographerHelper 可以实时计算帧率和掉帧数，实时监测App页面的帧率数据，发现帧率过低，还可以自动保存现场堆栈信息。


对于线上卡顿监控，可以用字节码插桩技术。通过Gradle Plugin+ASM，编译期在每个方法开始和结束位置分别插入一行代码，统计方法耗时，如微信的Matrix

lrucache原理 近期最少使用
底层LinkedHashMap 该LinkedHashMap是以访问顺序排序的。当调用put()方法时，就会添加元素，并调用trimToSize()判断缓存是否已满，如果满了就用LinkedHashMap的迭代器删除队头元素，即近期最少访问的元素。当调用get()方法访问缓存对象时，就会调用LinkedHashMap的get()方法获得对应元素，同时会更新该元素到队尾。
        

viewModule activity重建时？

页面懒加载怎么实现？
onHiddenChanged()
setUserVisibleHint()结合 同时用标记位标记是否是首次加载

apk打包流程
    通过AAPT工具进行资源文件（包括AndroidManifest.xml、布局文件、各种xml资源等）的打包，生成R.java文件。
    通过AIDL工具处理AIDL文件，生成相应的Java文件。
    通过Javac工具编译项目源码，生成Class文件。
    通过DX工具将所有的Class文件转换成DEX文件，该过程主要完成Java字节码转换成Dalvik字节码，压缩常量池以及清除冗余信息等工作。
    通过ApkBuilder工具将资源文件、DEX文件打包生成APK文件。
    利用KeyStore对生成的APK文件进行签名。
    如果是正式版的APK，还会利用ZipAlign工具进行对齐处理，对齐的过程就是将APK文件中所有的资源文件举例文件的起始距离都偏移4字节的整数倍，这样通过内存映射访问APK文件的速度会更快。
    
避免死锁的条件？怎么避免？
互斥条件：一个资源每次只能被一个进程使用，即在一段时间内某资源仅为一个进程所占有。此时若有其他进程请求该资源，则请求进程只能等待。
请求与保持条件：进程已经保持了至少一个资源，但又提出了新的资源请求时，该资源已被其他进程占有，此时请求进程被阻塞，但对自己已获得的资源保持不放。
不可剥夺条件:进程所获得的资源在未使用完毕之前，不能被其他进程强行夺走，即只能由获得该资源的进程自己来释放（只能是主动释放)。
循环等待条件: 若干进程间形成首尾相接循环等待资源的关系。
这四个条件是死锁的必要条件，只要系统发生死锁，这些条件必然成立，而只要上述条件之一不满足，就不会发生死锁。
避免死锁的方法：
(1)破坏请求和保持条件：在系统中不允许进程在已获得某种资源的情况下，申请其他资源，
   即要想出一个办法，阻止进程在持有资源的同时申请其它资源。
(2)破坏不可抢占条件：允许对资源实行抢夺。
(3)破坏循环等待条件
数据库升级？
继承SQLiteOpenHelper 重写onupgrade 




Binder机制  

AMS PMS WMS

安装apk流程 

listview和recycleview 

eventbus原理

依赖传递
加入我们项目中有A、B、C、D
第一种情况：
A implementation B
B implementation C
A可以直接访问B中资源，但不能直接访问C
第二种情况：
A implementation B
B api C
A 可以直接访问B和C中的资源
第三种情况：
A implementation B
B implementation C
C api D
B可以直接访问C和D中资源
而A只能访问B中资源
原文链接：https://blog.csdn.net/geyuecang/article/details/105270669