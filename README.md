## 简介

线程池（Thread Pool），是一种基于池化思想管理线程的工具，用于降低资源消耗、提高响应速度、提高线程的管理性。池化技术的引入，可以有效的减少线程频繁申请/销毁和调度所带来的额外开销。

## 模块介绍

* **dynamic-thread-pool-admin**

  提供接口和工具，允许用户查询当前线程池的状态、配置参数和运行情况。支持动态调整线程池的核心参数，如核心线程数、最大线程数和队列长度等，以适应不同的负载需求和运行环境。与`docs/front/index.html`配合使用，提供一个直观的前端管理界面，使用户可以方便地查看和修改线程池配置。

* **dynamic-thread-pool-spring-boot-starter**

  线程池的核心场景启动器，自动收集和加载线程池的配置参数，确保线程池能够根据预定义的配置进行初始化和运行。通过Redis注册中心，将线程池的运行数据和状态信息上报，便于集中管理和监控。支持订阅和发布消息机制，允许线程池在运行过程中接收和响应配置变更通知。动态调整线程池的核心参数，如核心线程数、最大线程数和队列长度等，以适应不同的负载需求和运行环境。

* **dynamic-thread-pool-test**

  创建和实例化线程池，需要引入**dynamic-thread-pool-spring-boot-starter**来启动。在`dynamic-thread-pool-test/src/main/java/cn/cactusli/config/ThreadApplicationRunner.java`中，通过实现`ApplicationRunner`接口，在Spring应用启动完成后执行测试任务，以便在线程池管理界面查看线程的执行情况。