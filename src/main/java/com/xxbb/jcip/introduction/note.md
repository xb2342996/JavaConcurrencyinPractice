# 介绍 Introduction

## 推动多线程发展的因素
1. 资源利用(resource utilization)<br>利用等待时间去执行其他的程序
2. 公平(fairness)<br>多个用户对机器资源享有平等的优先级
3. 方便(convenience)<br>多个程序协调比单独一个程序运行所有任务更简单

## 多线程的优点(Advantages)
正确的使用, 降低开发和维护开销, 优化复杂应用的性能。
- 随着多核处理器的增加，单线程程序会造成资源的浪费, 闲置CPU资源, 良好的设计可以更好的利用闲置的处理器提高系统的吞吐量
- 多类型任务使用不同的线程模型管理, 将复杂的任务分解成简单的同步流程, 并在特定时间点进行交互
- 更流畅的处理异步I/O任务
- 更顺滑的GUI界面响应

## 多线程的风险(Risk)
1. 安全风险(safety hazards)
    - 竞争条件<br>Java中的线程共享相同的内存地址空间且并发运行, 这些线程可以访问和修改其他线程正在使用的变量。允许线程修改其他线程变量造成了数据混乱, 并且难以发现错误原因。
2. 活跃度风险(liveness hazards)
    - 当活动进入某种永远无法执行的状态中，活跃度失败就发生了，比如:
      >死锁，饥饿，活锁
    - 引发活跃度失败的的bug与大多数同步bug一样难以发现，他们主要取决于线程事件的时序，因此在开发和测试中发现他们的机会并不多。
3. 性能风险(performance harzards)
    - 吞吐量，服务时间，响应速度，资源消耗和可拓展性都会因为使用线程而面临问题。
    - 即使良好的多线程程序设计在使用时依旧会产生一定程度上的开销，上下文切换——系统频繁的在多个线程之间来回切切换，会造成巨大的系统开销。
    线程共享数据时，同步机制会限制编译器对代码的优化。
    