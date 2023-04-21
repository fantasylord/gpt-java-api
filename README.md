# gpt-java-api
一个实现了chatGpt的远端调用demo 想起来了就加点东西
核心功能 用户鉴权,gpt会话管理,gpt聊天

config:包含了feign的代理配置
constant:系统常量
controller：web访问控制
dao：数据库交互
exception:扩展一些基础异常
filter：OAuth鉴权
model:webapi对外的响应体和请求体
remote：远端访问,对接feign的API请求
service：核心服务逻辑

