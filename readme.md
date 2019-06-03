## 关于Collections一些Test

#### ReferenceQueueTest
show the feature of ReferenceQueue. 
当一个对象被gc掉的时候通知用户线程，进行额外的处理时，就需要使用引用队列了. (WeakReference & ReferenceQueue) 
