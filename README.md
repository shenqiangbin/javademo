# java研究室

### 压缩包解压缩

[https://github.com/srikanth-lingala/zip4j](https://github.com/srikanth-lingala/zip4j)

### 图片压缩获取缩略图 [ImgUtil.java](https://github.com/shenqiangbin/javademo/blob/master/src/main/java/MyImage/ImgUtil.java)

```
OutputStream outputStream = new FileOutputStream("d:/th-thumbnail.png");
ImgUtil.thumbnailImg(new File("d:/th.jpg"),400,null,outputStream);
ImgUtil.thumbnailImg(new URL("http://bigdata.cnki.net/img/1.bed4469b.png"),100,null,outputStream);

// 网页输出流使用方法
//HttpServletResponse response
OutputStream outputStream = response.getOutputStream();
ImgUtil.thumbnailImg(new File("d:/th.jpg"),400,null,outputStream);
```

### 图片操作 [ImageTest.java](https://github.com/shenqiangbin/javademo/blob/master/src/main/java/MyImage/ImageTest.java)
* 图片上写字
* 图片保存到磁盘,并打开
* 图片保存成base64模式
* 居中图片上的文字
* 设置图片背景色  
* 图片上画线 多边形
* 旋转角度
* 随机位置放文字
* 随机画多边形
* 随机画线

#### 实践
* 验证码生成
* 为图片添加水印

### 随机数生成 [RandomCode.java](https://github.com/shenqiangbin/javademo/blob/master/src/main/java/MyImage/RandomCode.java)
* 由大小写字母和数字组成的字符串
### 发送请求的HttpClient
* 通过接口下载文件(图片)
* 通过接口上传文件
### 访问 MS Access 数据库 [AccessHelper.java](https://github.com/shenqiangbin/javademo/blob/master/src/main/java/dbmgr/microsoftAccess/AccessHelper.java)
* 支持 jdk1.8
* 支持 *.mdb文件类型 (2007版本的文件格式待测)
### 获取服务器目录结构 [BrowseHelper.java](https://github.com/shenqiangbin/javademo/blob/master/src/main/java/fileDemo/BrowseHelper.java)

