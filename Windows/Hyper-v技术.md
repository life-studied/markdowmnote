---
create: '2025-10-01'
modified: '2025-10-01'
---

# Hyper-v技术

Hypervisor 主要有两种类型：

1. **Type 1 Hypervisor（裸金属虚拟化）**：
   - 直接运行在物理硬件之上，不依赖于宿主操作系统。
   - 例如：VMware ESXi、Microsoft Hyper-V、Citrix XenServer。
   - 优点：性能高，资源利用率高，安全性好。
   - 缺点：需要直接管理硬件，部署和维护相对复杂。
2. **Type 2 Hypervisor（宿主虚拟化）**：
   - 运行在现有的操作系统之上，依赖于宿主操作系统的内核。
   - 例如：VMware Workstation、Oracle VirtualBox。
   - 优点：易于部署和使用，适合桌面环境。
   - 缺点：性能相对较低，资源管理不如 Type 1 Hypervisor 高效。

## 简介

Hyper-V 虚拟化技术是由微软公司开发和提供的。它是一种基于硬件的虚拟化技术，属于 Type 1 Hypervisor（裸金属虚拟化），直接运行在硬件之上，无需额外的宿主操作系统层。这使得虚拟机能够以接近原生性能的方式运行。

## Hyper-V 的工作原理

在Windows操作系统中，Hyper-V被设计为一个内核模式组件。这意味着Hyper-V的代码运行在Windows内核的上下文中，与Windows内核共享相同的地址空间和资源。

当Hyper-V创建虚拟机时，它会为每个虚拟机分配一定数量的虚拟CPU（vCPU）。这些vCPU实际上是物理CPU核心的虚拟表示，它们由Hyper-V的调度程序进行管理。当Hyper-V运行时，它会接管对物理CPU核心的直接控制，根据各种因素（如虚拟机的优先级、CPU使用率等）来决定何时将vCPU分配给物理CPU核心。

## 互斥

在Windows系统上，一般来说硬件提供虚拟化功能，而上层的虚拟化组件如何想要用硬件提供的虚拟化功能，必须是独占的。也就是说，硬件不能同时为两种虚拟化软件提供服务。

这也是为什么使用VMware的时候必须关闭wsl，因为wsl依赖于hyper-v，而VMware则使用自己的虚拟化方式。