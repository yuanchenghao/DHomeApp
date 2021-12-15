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