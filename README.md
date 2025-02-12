# Axonix 说明
轴智辅助软件的设计与实现

## 后端

使用SpringBoot和maven进行控制，

**各个目录的作用**

| 目录                 | 作用                                                |
| -------------------- | --------------------------------------------------- |
| **config**           | 存放 Spring Boot 配置类                             |
| **controller**       | 处理 API 请求                                       |
| **dto**              | 数据传输对象，用于封装前端请求数据                  |
| **exception**        | 统一异常处理                                        |
| **interceptor**      | 拦截器                                              |
| **demo.mapper**      | 存放 MyBatis 的 `Mapper` 接口                       |
| **demo.model**       | 存放数据库表对应的 `实体类`                         |
| **service**          | 业务逻辑层，存放 `Service` 接口                     |
| **service.impl**     | 业务逻辑层的 `实现类`                               |
| **util**             | 存放工具类                                          |
| **vo**               | 视图层对象（View Object），定义返回给前端的数据格式 |
| **resources/mapper** | 存放 `MyBatis XML` 配置文件                         |
| **resources/static** | 静态文件                                            |
| **test**             | 存放单元测试代码                                    |

### 数据库配置：

​	需要修改的地方：(具体位置已有注释标明)

​	1.src/main/java/config/MyBatisConfig

​	2.src/main/resources/application.yml

​	3.src/main/resources/generatorConfig.xml （映射文件生成类，可不动，如有新增加数据表时再做修改，修改位置在<!-- 数据库连接配置  （可能需修改）-->下）

### Spring OpenAPI文档：

​	本项目已配置自带的API文档，可暂做参考。使用方法：启动项目后，在浏览器中打开http://localhost:8080/swagger-ui/index.html。若需要JSON格式：http://localhost:8080/v3/api-docs。若需要YAML格式：http://localhost:8080/v3/api-docs.yaml

