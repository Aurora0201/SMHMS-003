package top.pi1grim.ea.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import top.pi1grim.ea.core.constant.StringConstant;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Objects;

@Slf4j
public class EntityUtils {

    private EntityUtils() {

    }

    public static final String GET = "get";

    public static final String SET = "set";

    /*
    * 检查对象是否有字段为空，或者为空字符串，前提是提供了getter方法
    * */
    public static boolean fieldIsNull(Object obj) {
        if(Objects.isNull(obj))return false;
        Class<?> clazz = obj.getClass();
        for (Field f : clazz.getDeclaredFields()) {
            try {
                String name = f.getName();
                String methodName = GET + name.toUpperCase().charAt(0) + name.substring(1);
                Method method = clazz.getDeclaredMethod(methodName);
                Object o = method.invoke(obj);
                if(Objects.isNull(o) || StringUtils.isEmpty(o.toString()))return true;
            } catch (Exception e) {
                log.error(StringConstant.EXCEPTION_OCCUR, e);
            }
        }
        return false;
    }

    /*
     * 将DTO对象中的同名字段的值赋值到指定的Entity对象中，前提是Entity提供了setter方法，DTO提供了getter方法
     * */
    public static void assign(Object entity, Object dto) {
        if(Objects.isNull(entity) || Objects.isNull(dto)){
            throw new RuntimeException("存在空对象");
        }

        HashMap<String, Method> map = new HashMap<>();

        Class<?> dtoClass = dto.getClass();
        Class<?> entityClass = entity.getClass();

        try {
            for (Field f : dtoClass.getDeclaredFields()) {
                String name = f.getName();
                String methodName = GET + name.toUpperCase().charAt(0) + name.substring(1);
                Method getter = dtoClass.getDeclaredMethod(methodName);
                map.put(name, getter);
            }

            for (Field f : entityClass.getDeclaredFields()) {
                String name = f.getName();
                Method getter = map.get(name);
                if (Objects.nonNull(getter)) {
                    String methodName = SET + name.toUpperCase().charAt(0) + name.substring(1);
                    Method setter = entityClass.getDeclaredMethod(methodName, f.getType());
                    Object o = getter.invoke(dto);
                    setter.invoke(entity, o);
                }
            }
        } catch (Exception e) {
            log.error(StringConstant.EXCEPTION_OCCUR, e);
        }
    }
}
