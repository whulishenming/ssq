package com.lsm.ssq.utils;

import java.util.Set;

public class RandomUtil {

    public static <T> T randomFromSet(Set<T> set) {
        Object[] objects = set.toArray();
        int size = set.size();

        int num = (int) (Math.random() * size);

        return (T)objects[num];
    }

}
