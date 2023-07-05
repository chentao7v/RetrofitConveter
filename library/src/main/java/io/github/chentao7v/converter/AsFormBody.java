package io.github.chentao7v.converter;

import com.google.gson.annotations.SerializedName;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import okhttp3.FormBody;
import retrofit2.http.Body;

/**
 * 标识是否将{@link Body}转换为{@link FormBody}而不是类型为 application/json 的 body。
 * <p>
 * 被该注解标识的类中，会将有值的成员变量的名称(如果该字段有{@link SerializedName}的标识则优先取注解上的值)作为 key，
 * 该成员变量的值为value，添加到{@link FormBody}中。
 * </p>
 * <br>
 * create by chentao on 2022-08-24.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AsFormBody {

  /**
   * 是否忽略静态变量，默认为true
   */
  boolean ignoreStaticFields() default true;

}
