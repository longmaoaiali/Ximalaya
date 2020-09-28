# Ximalaya FM Android App 开发记录
## 参考资料  
  MagicIndicator https://github.com/hackware1993/MagicIndicator   
  Gaussian Blur 毛玻璃效果 https://www.sunofbeach.net/a/1180054455828197376  
  TwinklingRefreshLayout 框架实现下拉刷新 下拉加载更多的样式 https://github.com/lcodecorex/TwinklingRefreshLayout 
  
## 开发日志
  2020:09:01 创建项目，初始化环境，集成XIMALAYA SDK和开源项目MagicIndicator  
  2020:09:02 实现Indicator Adapter 和 ViewPager Adapter,并且绑定ViewPager 和 Indicator,滑动ViewPager Indicator随之改变  
  2020:09:03 实现indicator平分，搜索按钮的显示。使用recyclerView 显示获得的数据，picasso框架显示图片，界面显示优化(添加圆角，item设置间隔，图片填充(fixXY)  
  2020:09:04 添加interface,进行重构-->分离逻辑代码与UI显示代码。使用UILoader加载页面，来处理网络请求的各种结果(成功，数据为空，网络超时等错误) 
  2020:09:07 添加网络错误的UI图标，并实现点击图标重新获取数据; 添加UILoading图标; 实现点击RecyclerView的Item跳转到详情界面的Activity  
  2020:09:08 详情界面加载图片与标题；添加毛玻璃效果(高斯模糊);添加订阅按钮  
  2020:09:09 详情界面UI绘制，使用RecyView显示专辑列表  
  2020:09:10 详情界面设置RecyclerView Item数据;数据加载失败，成功，数据为空的几种情况显示(UILoader加载器);点击Item实现跳转播放Activity;绘制播放界面UI  
  2020:09:11 添加播放器接口,接口实现,UI更新的回调接口；实现播放
  2020:09:14 设置歌曲播放时长；更新设置seekBar歌曲进度;播放上/下一首;显示歌曲title和封面图片  
  2020:09:15 实现图片与歌曲切换的联动; 解决播放器未初始化完成就开始播放导致播放失败的问题;实现点击事件的选择器;实现播放模式的切换
  2020:09:16 使用POPWndow实现弹出列表；添加背景透明度渐变的动画效果  
  2020:09:17 使用RecyclerView实现playList,修复Picasso loadUrl为空导致的应用闪退;修复PlayList点击Item之后不更新playList的问题  
  2020:09:21 详情界面控制播放;使用TwinklingRefreshLayout 框架实现下拉刷新 下拉加载更多的样式 https://github.com/lcodecorex/TwinklingRefreshLayout 
  2020:09:22 实现上拉加载更多 下拉刷新List 的回弹效果; 实现mainActivity的播放控制UI   
  2020:09:24 实现mainActivity的播放控制的逻辑;重构ximalaya数据获取的方式  
  2020:09:25 添加搜索功能的接口与回调接口; 实现搜索界面的输入框UI  
  2020:09:27 添加 FlowTextLayout 自定义控件; 实现UILoader根据状态显示不同页面;实现搜索前显示热词，搜索后显示搜索结果
  2020:09:28 优化UI细节;实现输入框联想词功能;实现RecyclerView Item点击事件
  
  
