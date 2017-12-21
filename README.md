# EpsonPrinterSample


[EPSON Printer 打印机 Sdk For Android](http://www.jianshu.com/p/178dcb046e56)

##### EPOSN SDK 打印的步骤

- 1 先开启发现打印机设备，完后会有个回调监听获取已连接的打印机设备的 TargetId；

 - 2 需要通过初始化 Printer打印类 ，并且设置打印监听；

- 3 创建打印数据添加到已创建到 Printer对象 中，设置小票字体样式都在这里进行设置；

- 4 连接到打印设备并且通过 sendData()  方法发送到命令缓冲区进行打印；

- 5 完成打印，清空命令缓冲区，关闭释放打印对象 。

![image.png](http://upload-images.jianshu.io/upload_images/956862-ebab845196241808.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
