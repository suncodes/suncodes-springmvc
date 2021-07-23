package converter;

import org.springframework.core.convert.converter.Converter;
import pojo.LifeBO;

public class LifeConverter implements Converter<String, LifeBO> {
    public LifeBO convert(String s) {

        LifeBO lifeBO = new LifeBO();
        // 以“，”分隔
        String stringvalues[] = s.split(",");
        if (stringvalues.length == 3) {
            // 为user实例赋值
            lifeBO.setCity(stringvalues[0]);
            lifeBO.setPerson(stringvalues[1]);
            lifeBO.setTarget(stringvalues[2]);
            return lifeBO;
        } else {
            throw new IllegalArgumentException(String.format("类型转换失败，但格式是[% s ] ", s));
        }
    }
}
