# Ximalaya FM Android App 开发记录
## 参考资料  
  MagicIndicator https://github.com/hackware1993/MagicIndicator   
  
## 开发日志
  2020:09:01 创建项目，初始化环境，集成XIMALAYA SDK和开源项目MagicIndicator  
  2020:09:02 实现Indicator Adapter 和 ViewPager Adapter,并且绑定ViewPager 和 Indicator,滑动ViewPager Indicator随之改变  
  2020:09:03 实现indicator平分，搜索按钮的显示。使用recyclerView 显示获得的数据，picasso框架显示图片，界面显示优化(添加圆角，item设置间隔，图片填充(fixXY)  
  2020:09:04 添加interface,进行重构-->分离逻辑代码与UI显示代码。使用UILoader加载页面，来处理网络请求的各种结果(成功，数据为空，网络超时等错误) 
  2020:09:07 添加网络错误的UI图标，并实现点击图标重新获取数据; 添加UILoading图标; 实现点击RecyclerView的Item跳转到详情界面的Activity 
  
  
