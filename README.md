# EpsonPrinterSample

###### See Issues if you have questions.

CSDN [EPSON Printer 打印机 Sdk For Android](https://blog.csdn.net/a23006239/article/details/78871913)

##### EPOSN SDK 打印的步骤(Steps for EPOSN SDK printing)

- 1 先开启发现打印机设备，完后会有个回调监听获取已连接的打印机设备的 TargetId；(The discovery of the printer device is turned on first, and a callback listens to obtain the TargetId of the connected printer device afterwards.)

 - 2 需要通过初始化 Printer打印类 ，并且设置打印监听；(Needs to initialize the Printer print class and set up print listening.)

- 3 创建打印数据添加到已创建到 Printer对象 中，设置小票字体样式都在这里进行设置；(Create print data to add to the created to Printer object, where the small ticket font styles are set.)

- 4 连接到打印设备并且通过 sendData()  方法发送到命令缓冲区进行打印；(Connects to the print device and sends it to the command buffer for printing via the sendData() method.)

- 5 完成打印，清空命令缓冲区，关闭释放打印对象 。(Finish printing, clear the command buffer, and close the release print object.)

![image.png](http://upload-images.jianshu.io/upload_images/956862-ebab845196241808.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
