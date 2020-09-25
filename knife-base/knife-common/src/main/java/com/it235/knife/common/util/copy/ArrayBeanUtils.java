package com.it235.knife.common.util.copy;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/25 10:16
 */
public class ArrayBeanUtils {

    public static void demo01(){
//        List<teacher> teacherList = ...;
//        List<TeacherVO> teacherVOList = ArrayBeanUtils.copyListProperties(teacherList, TeacherVO::new);
//        return teacherVOList;
    }

    public static void demo02(){
//        List<teacher> teacherList = null;
//        List<TeacherVO> teacherVOList = ArrayBeanUtils.copyListProperties(teacherList , TeacherVO::new, (teacher, teacherVO) -> {
//            // 回调处理
//
//        });
//        return teacherVOList;
    }

    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        return copyListProperties(sources, target, null);
    }

    /**
     * 使用场景：DTO、VO层数据的复制，因为BeanUtils.copyProperties只能给目标对象的属性赋值，却不能在List集合下循环赋值，因此添加该方法
     * 如：List<Teacher> 赋值到 List<TeacherVO> ，List<Teacher>中的 TeacherVO 属性都会被赋予到值
     * S: 数据源类 ，T: 目标类::new(eg: TeacherVO::new)
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target, ArrayBeanUtilsCallBack<S, T> callBack) {
        int size = sources.size();
        if(sources != null &&  size > 0){
            List<T> list = new ArrayList<>(size);
            for (S source : sources) {
                T t = target.get();
                BeanUtils.copyProperties(source, t);
                list.add(t);
                if (callBack != null) {
                    // 回调
                    callBack.callBack(source, t);
                }
            }
            return list;
        }
        return new ArrayList<T>();
    }
}
