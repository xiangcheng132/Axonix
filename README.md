# Axonix 说明
轴智辅助软件的设计与实现

关于后端，使用SpringBoot和maven进行控制，

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

