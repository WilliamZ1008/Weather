# 天气应用 (Weather App)

一个现代化的 Android 天气应用，提供实时天气信息和优雅的用户界面。

## 功能特性

- 🌍 支持全球城市天气查询
- 📅 支持今天、明天、后天天气查看
- 🔄 下拉刷新更新天气数据
- 💾 本地缓存天气数据
- 🎨 现代化磨砂玻璃界面设计
- 🌈 动态渐变背景
- 📱 适配不同屏幕尺寸

## 技术架构

### 开发环境
- Android Studio Hedgehog | 2023.1.1
- Kotlin 2.0.21
- Gradle 8.10.0
- minSdk 26 (Android 8.0)
- targetSdk 35 (Android 15)

### 主要依赖
```gradle
// 核心依赖
implementation 'androidx.core:core-ktx:1.16.0'
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'com.google.android.material:material:1.11.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
implementation 'androidx.swiperefreshlayout:swiperefreshlayout:1.1.0'

// 网络请求
implementation 'com.squareup.okhttp3:okhttp:4.12.0'

// JSON 解析
implementation 'org.json:json:20231013'

// 数据存储
implementation 'androidx.sqlite:sqlite-ktx:2.4.0'

// 图表库
implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
```

### 项目结构
```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/weather/
│   │   │   ├── MainActivity.kt          # 主界面
│   │   │   ├── model/
│   │   │   │   └── WeatherData.kt       # 天气数据模型
│   │   │   ├── network/
│   │   │   │   └── WeatherApi.kt        # 网络请求
│   │   │   └── db/
│   │   │       └── WeatherDbHelper.kt   # 数据库操作
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   └── activity_main.xml    # 主界面布局
│   │   │   ├── drawable/
│   │   │   │   ├── gradient_background.xml  # 渐变背景
│   │   │   │   ├── frosted_glass.xml    # 磨砂玻璃效果
│   │   │   │   └── frosted_card.xml     # 卡片背景
│   │   │   └── values/
│   │   │       ├── colors.xml           # 颜色资源
│   │   │       └── themes.xml           # 主题样式
│   │   └── AndroidManifest.xml
│   └── test/                            # 测试代码
└── build.gradle.kts                     # 应用级构建配置
```

## 安装说明

1. 克隆项目
```bash
git clone https://github.com/WilliamZ1008/Weather.git
```

2. 在 Android Studio 中打开项目

3. 获取 OpenWeatherMap API 密钥
   - 访问 [OpenWeatherMap](https://openweathermap.org/)
   - 注册账号并获取 API 密钥
   - 在 `WeatherApi.kt` 中替换 `apiKey` 变量

4. 构建并运行项目
```bash
./gradlew assembleDebug
```

## 使用说明

1. 启动应用后，默认显示西安市的天气信息
2. 在输入框中输入城市名称（支持中英文）
3. 选择要查看的日期（今天/明天/后天）
4. 点击"查询天气"按钮获取最新天气
5. 下拉可以刷新天气数据

## 界面预览

应用采用现代化的设计风格：
- 渐变背景：从蓝色到绿色的渐变效果
- 磨砂玻璃效果：半透明卡片设计
- 优雅的字体：使用不同字重的字体层次
- 响应式布局：适配各种屏幕尺寸

## 开发计划

- [ ] 添加多语言支持
- [ ] 实现天气预警功能
- [ ] 添加天气趋势图表
- [ ] 支持自定义主题
- [ ] 添加天气分享功能

## 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 联系方式

如有问题或建议，请提交 Issue 或 Pull Request。

## 致谢

- [OpenWeatherMap](https://openweathermap.org/) - 天气数据 API
- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) - 图表库
- [OkHttp](https://square.github.io/okhttp/) - 网络请求库 