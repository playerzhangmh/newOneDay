# newOneDay
record your life and schedule your daily plan


由于git versioncontrol的问题，我直接在这边新建了个库，但之前的commit信息都还在的，我只是将proguard-rules再次做了点修改，
当然，会增加上一个稍微完善一点，功能都能用的release版本。相关修改修改主要集中于schedule模块的两个service，以保证除用户
forcekill之外的情况都能提醒通知，另外，开机自启动也需要用户去到相关设置，除此之外，对相应的少数数据库接口做了修改。


当然，我们针对forcekill需要的解决方案并非没有，第一种是通过alarmManager来代替我们现有的通知提醒，将我们的每日最早时间存于闹钟，
通过广播来监听这个闹钟，并启动我们的service，若为续航考虑，可以在启动service后完成通知，并将下一次的时间再次存入系统闹钟
，这样service就不会一直运行在后台，我们的软件就摆脱了那么一丁点的流氓特质；第二种是通过Native方法创建子线程去遍历当前系统的所有
service，若没有，则启动，这部分只是一点理论想法，jni暂时还不会啊。
不过作为我们的处女作，偶尔上来更新或fix bug还是有必要的。
