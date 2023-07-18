package io.github.chentao7v.converter;

import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 将有使用 {@link AsFormBody} 注解的 Request(普通的JavaBean) 转换为 {@link FormBody} 的 {@link Converter.Factory}。
 * <br>
 * create by chentao on 2022-08-24.
 */
public final class FormBodyConverterFactory extends Converter.Factory {

  private static final String TAG = FormBodyConverterFactory.class.getSimpleName();

  private FormBodyConverterFactory() {}

  public static FormBodyConverterFactory create() {
    return new FormBodyConverterFactory();
  }

  @Override
  public Converter<?, RequestBody> requestBodyConverter(@NonNull Type type, @NonNull Annotation[] parameterAnnotations,
                                                        @NonNull Annotation[] methodAnnotations, @NonNull Retrofit retrofit) {
    if (type instanceof Class<?>) {
      Class<?> clazz = (Class<?>) type;
      AsFormBody annotation = clazz.getAnnotation(AsFormBody.class);
      // 仅处理有 AsFormBody 注解的
      if (annotation != null) {
        boolean ignoreStaticFields = annotation.ignoreStaticFields();
        return new ParamsRequestBodyConverter<>(ignoreStaticFields);
      }
      Log.w(TAG, "If you want convert a Java Bean into an Http Form, please add @AsFormBody on your Class");
    }
    return null;
  }

  public static final class ParamsRequestBodyConverter<T> implements Converter<T, RequestBody> {

    private final boolean ignoreStaticFields;

    public ParamsRequestBodyConverter(boolean ignoreStaticFields) {
      this.ignoreStaticFields = ignoreStaticFields;
    }

    @Override
    public RequestBody convert(T body) throws IOException {
      // 将类的所有有值的成员变量封装到 FormBody 中
      FormBody.Builder builder = new FormBody.Builder();
      convertAllFields(body, body.getClass(), builder);
      return builder.build();
    }

    private void convertAllFields(Object target, Class<?> targetClazz, FormBody.Builder builder) {
      addFieldsToForm(target, targetClazz, builder);
      Class<?> superClass = targetClazz.getSuperclass();
      if (superClass == Object.class) {
        return;
      }
      // 父类上的字段一并添加
      convertAllFields(target, superClass, builder);
    }

    private void addFieldsToForm(Object target, Class<?> targetClazz, FormBody.Builder builder) {
      Field[] fields = targetClazz.getDeclaredFields();
      for (Field field : fields) {
        try {
          field.setAccessible(true);
          // 是静态并且需要忽略
          boolean isStatic = Modifier.isStatic(field.getModifiers());
          if (isStatic && ignoreStaticFields) {
            continue;
          }

          // 将当前成员对象的所有属性添加到表单中
          FlatMembers flatMembers = field.getAnnotation(FlatMembers.class);
          if (flatMembers != null) {
            Object member = field.get(target);
            final Class<?> memberClazz = member.getClass();
            if (!isIgnoreNoNeedFlat(memberClazz)) {
              convertAllFields(member, memberClazz, builder);
              continue;
            }
          }

          SerializedName serializedName = field.getAnnotation(SerializedName.class);
          String key;
          // Key 优先取 SerializedName 注解上的值
          if (serializedName != null) {
            key = serializedName.value();
          } else {
            key = field.getName();
          }

          // Value 转换为字符串
          Object valObj = field.get(target);
          if (valObj != null) {
            String val = valObj.toString();
            if (!TextUtils.isEmpty(val)) {
              builder.add(key, val);
            }
          }
        } catch (IllegalAccessException e) {
          Log.w(TAG, e);
        }
      }
    }

    /**
     * 过滤掉无需平铺的
     */
    private boolean isIgnoreNoNeedFlat(Class<?> memberClazz) {
      return memberClazz == String.class || memberClazz == Number.class;
    }
  }

}
