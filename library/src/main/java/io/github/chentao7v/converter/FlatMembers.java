package io.github.chentao7v.converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import okhttp3.FormBody;

/**
 * 当对象 A 内部有一个成员变量 B，且 B 上有 {@link FlatMembers} 注解，
 * 则 B 中的所有成员都会和 A 中的成员一起构建成 {@link FormBody} 提交到服务器
 * <br>
 * create by chentao on 2023-07-05.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FlatMembers {

}
