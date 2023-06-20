package com.cacloud.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * 校验器  key ：校验注解  value ：校验属性
 */
public class ListValueConstraintValidator implements ConstraintValidator<ListValue,Integer> {

    // 根据注解value初始化的值
    private Set<Integer> set = new HashSet<>();

    //初始化方法
    @Override
    public void initialize(ListValue constraintAnnotation) {

        int[] vals = constraintAnnotation.vals();
        for (int val : vals) {
            set.add(val);
        }

    }

    //判断是否校验成功

    /**
     *
     * @param value 需要校验的值
     * @param context
     * @return
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {

        // 传入的值在set集合中
        return set.contains(value);
    }
}
