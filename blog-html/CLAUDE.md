# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目简介

这是博客系统的前端静态页面目录，包含完整的用户界面和第三方编辑器组件。该前端配合上级目录的 Spring Cloud 微服务后端使用。

## 项目结构

### 核心页面
- `blog_list.html`: 博客列表主页
- `blog_detail.html`: 博客详情页  
- `blog_edit.html`: 博客编辑/发布页
- `blog_update.html`: 博客更新页
- `blog_login.html`: 用户登录页
- `blog_register.html`: 用户注册页
- `blog_register_success.html`: 注册成功页

### 资源目录
- `css/`: 样式文件
  - `common.css`: 公共样式
  - `list.css`: 列表页样式
  - `detail.css`: 详情页样式
  - `edit.css`: 编辑页样式
  - `login.css`: 登录页样式
  - `register.css`: 注册页样式
- `js/`: JavaScript文件
  - `common.js`: 公共JavaScript功能
  - `jquery.min.js`: jQuery库
- `pic/`: 图片资源
  - `logo2.jpg`: 网站Logo
  - `success.png`: 成功提示图标
  - 其他示例图片

### 第三方组件
- `blog-editormd/`: Markdown编辑器
  - 基于Editor.md的富文本/Markdown编辑器
  - 支持实时预览、语法高亮、流程图、序列图等功能
  - 包含完整的示例和文档

## 技术栈

### 前端技术
- **HTML5**: 页面结构
- **CSS3**: 样式和布局
- **JavaScript/jQuery**: 交互逻辑
- **Editor.md**: Markdown编辑器
- **CodeMirror**: 代码编辑器内核
- **Bootstrap**: 部分样式组件（在Editor.md中使用）

### API通信
- **Ajax/jQuery**: 与后端API通信
- **JSON**: 数据交换格式
- **RESTful API**: 后端接口调用

## API接口说明

### 博客相关接口
```javascript
// 获取博客列表
GET /blog/getList

// 获取博客详情
GET /blog/getBlog?blogId={blogId}

// 发布博客
POST /blog/publish
Content-Type: application/json
{
  "title": "博客标题",
  "content": "博客内容",
  "tags": "标签"
}

// 更新博客
POST /blog/update
Content-Type: application/json
{
  "blogId": 123,
  "title": "更新后标题", 
  "content": "更新后内容",
  "tags": "更新后标签"
}

// 删除博客
DELETE /blog/delete?blogId={blogId}
```

### 用户相关接口
```javascript
// 用户登录
POST /user/login
Content-Type: application/json
{
  "userName": "用户名",
  "password": "密码"
}

// 用户注册
POST /user/register
Content-Type: application/json
{
  "userName": "用户名",
  "password": "密码",
  "email": "邮箱"
}

// 用户注销
POST /user/logout

// 获取用户信息
GET /user/getUserInfo
```

## 开发工作流

### 本地开发
1. **启动后端服务**: 参考上级目录的CLAUDE.md启动Spring Cloud服务
2. **配置代理**: 使用开发服务器代理API请求到后端
3. **实时预览**: 使用Live Server或类似工具预览页面

### 常用开发服务器
```bash
# 使用Python内置服务器（推荐用于开发测试）
python -m http.server 8000

# 使用Node.js serve（需要npm install -g serve）
serve -s . -p 8000

# 使用VSCode Live Server插件
# 右键HTML文件选择"Open with Live Server"
```

### API代理配置示例
如使用webpack-dev-server或vite：
```javascript
// 代理配置示例
proxy: {
  '/blog': {
    target: 'http://localhost:10030', // 网关地址
    changeOrigin: true
  },
  '/user': {
    target: 'http://localhost:10030',
    changeOrigin: true
  }
}
```

## 关键功能实现

### 用户认证流程
1. 用户登录成功后，后端返回JWT token
2. 前端将token存储在localStorage中
3. 后续API请求通过common.js自动添加Authorization头部
4. 登录状态检查在common.js中实现

### 博客编辑器集成
```javascript
// Editor.md初始化示例（在blog_edit.html中）
var testEditor = editormd("test-editormd", {
    width: "100%",
    height: 640,
    syncScrolling: "single",
    path: "blog-editormd/lib/", // Lib目录路径
    saveHTMLToTextarea: true,   // 保存HTML到Textarea
    emoji: true,
    taskList: true,
    tocm: true,         // Table of contents
    tex: true,          // 开启科学公式TeX语言支持
    flowChart: true,    // 开启流程图支持
    sequenceDiagram: true, // 开启时序/序列图支持
    imageUpload: true,
    imageFormats: ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
    imageUploadURL: "/blog/uploadImage" // 图片上传接口
});
```

### AJAX请求封装
```javascript
// common.js中的统一请求处理
function makeRequest(url, method, data, successCallback, errorCallback) {
    $.ajax({
        url: url,
        type: method,
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        },
        data: data,
        dataType: 'json',
        success: function(result) {
            if (result.code === 200) {
                successCallback(result.data);
            } else {
                alert(result.message);
                if (errorCallback) errorCallback(result);
            }
        },
        error: function(xhr, status, error) {
            alert('请求失败: ' + error);
            if (errorCallback) errorCallback(xhr);
        }
    });
}
```

## 样式规范

### CSS命名约定
- 使用kebab-case命名法：`.blog-title`, `.nav-span`
- 组件级样式使用BEM方法论：`.card__header`, `.card__body`
- 页面特定样式加前缀：`.list-container`, `.edit-toolbar`

### 响应式设计
- 主要断点：768px (平板), 480px (手机)
- 使用flexbox进行布局
- 图片使用相对单位和max-width: 100%

### 浏览器兼容性
- 支持Chrome 60+, Firefox 55+, Safari 12+, Edge 79+
- 避免使用实验性CSS特性
- 必要时添加浏览器前缀

## 常见开发任务

### 添加新页面
1. 创建HTML文件，复制现有页面的基本结构
2. 在`css/`目录添加对应的样式文件
3. 更新导航链接（如需要）
4. 添加必要的JavaScript逻辑

### 修改编辑器配置
1. 编辑blog_edit.html中的editormd初始化代码
2. 修改Editor.md配置选项
3. 如需新功能，检查blog-editormd/plugins/目录

### 添加新的API调用
1. 在对应页面的JavaScript部分添加AJAX请求
2. 使用common.js中的统一错误处理
3. 更新用户界面反馈

### 样式调整
1. 修改对应的CSS文件
2. 保持样式的一致性
3. 测试响应式布局

## 调试技巧

### 浏览器开发者工具
- Network面板检查API请求和响应
- Console面板查看JavaScript错误
- Elements面板调试CSS样式
- Application面板检查localStorage中的token

### 常见问题排查
1. **API请求失败**: 检查后端服务是否启动，网关配置是否正确
2. **登录状态丢失**: 检查localStorage中的token是否存在和有效
3. **编辑器不显示**: 检查blog-editormd目录路径配置
4. **样式错乱**: 检查CSS文件引入顺序和路径

### 性能优化建议
- 压缩CSS和JavaScript文件
- 优化图片大小和格式
- 启用浏览器缓存
- 使用CDN加载第三方库（生产环境）

## 部署说明

### 生产环境部署
1. **静态文件服务**: 使用Nginx、Apache或其他Web服务器
2. **API代理**: 配置反向代理指向后端网关
3. **域名配置**: 更新API请求的baseURL
4. **HTTPS配置**: 生产环境建议启用HTTPS

### Nginx配置示例
```nginx
server {
    listen 80;
    server_name yourdomain.com;
    root /path/to/blog-html;
    index blog_list.html;
    
    # 静态文件缓存
    location ~* \.(css|js|png|jpg|jpeg|gif|ico|svg)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
    
    # API代理
    location /blog/ {
        proxy_pass http://localhost:10030;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
    
    location /user/ {
        proxy_pass http://localhost:10030;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## 相关文档

- **后端API文档**: 参考上级目录的CLAUDE.md
- **Editor.md文档**: blog-editormd/docs/index.html
- **jQuery文档**: https://api.jquery.com/
- **Bootstrap文档**: https://getbootstrap.com/docs/

## 注意事项

- 本项目前端直接通过AJAX调用后端API，生产环境需要处理跨域问题
- 用户token存储在localStorage中，注意安全性和过期处理
- Editor.md组件较大，考虑按需加载优化
- 图片上传功能需要后端支持，确保上传路径和权限正确配置