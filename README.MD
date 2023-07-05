将普通的 Java Bean 转换为 Retrofit Form 的转换器。

## 使用方法

1. 定义一个请求对象类作为请求参数的接收者，并给其添加上 `AsFormBody` 的注解，如：

   ```java
   @AsFormBody
   public class TestRequest {
     @SerializedName("serialized_key1")
     private String key1;
     private String key2;
     //...
   }
   ```

2. 定义 api service

   ```java
   public interface TestApi {
     
     @POST("test/post")
     Call<ResponseBody> testPost(@Body TestRequest request);
     
   }
   ```

3. 发起请求，所有请求对象上的参数值【`toString()` 方法的值】都会被当做入参提交到服务器

   ```java
   public void test() {
     TestRequest req = new TestRequest();
     req.setKey1("value1");
     req.setKey2("value2");
     // ...
     Call<ResponseBody> call = testAPi.testPost(req);
     // ...
   }
   ```

   提交的数据：

   ```shell
   --> POST https://m.baidu.com/testPost
   Content-Type: application/x-www-form-urlencoded
   Content-Length: 40
   serialized_key1=value1&key2=value2
   --> END POST (168-byte body)
   ```
   
   

## 高级用法

1. 对于请求对象中的成员变量，如果未加 `SerializedName` 的注解，则其变量名将会作为表单的 `key` 被提交到服务器。**针对可能存在的混淆的情况，建议所有成员都加上该注解**，否则可能出现 `a=value1`  的情况。

2. 针对不需要将 Java Bean 中的静态字段作为请求入参的场景，请给 `AsFromBody` 注解添加对应属性：

   ```java
   @AsFormBody(ignoreStaticFields = true) // 该属性默认为 true
   ```

3. 如果请求对象 A 中内嵌了一个成员 B，想把 B 对象中的所有成员也作为表单项提交到服务器，则需要给 B 成员添加 `FlatMembers` 注解：

   ```java
   public class A {
     
     private String keyA1;
     
     @FlatMembers
     private B b;
     
     // ...
   }
   
   public class B {
     
     private String keyB1;
   
     @SerializedName("SerializedName_Key_B2")
     private boolean keyB2;
     
   }
   ```

   最终所有的入参都会被提交到服务器。