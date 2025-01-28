package net.okyildiz.parametre.utils;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.Date;

public abstract class BaseService {

    public static <T extends BaseEntity> void setBaseColumnsForUpdate(T entity, String uid){
        Date now = new Date();
        entity.setUpdated(now);
        entity.setUpdateUserUID(uid);
        if(entity.getStatus() == null){
            entity.setStatus(Boolean.TRUE);
        }
    }

    public static <T extends BaseEntity> void setBaseColumnsForCreate(T entity, String uid){
        Date now = new Date();
        entity.setCreated(now);
        entity.setCreateUserId(uid);
        entity.setUpdated(now);
        entity.setUpdateUserUID(uid);
        if(entity.getStatus() == null){
            entity.setStatus(Boolean.TRUE);
        }
    }

    //bu fonksiyon, değişken isimleri aynı olan nesnelerin değişkenlerinin içeriği dolu ise birbirine aktarımını yapar.
    //this function transfers the contents of the variables of objects with the same variable names to each other if they are full.
    public static <T> T mergeObjects(T target, T source) {
        if (target == null || source == null) {
            throw new IllegalArgumentException("Target ve Source not be null.");
        }

        Class<?> targetClass = target.getClass();
        Class<?> sourceClass = source.getClass();

        while (targetClass != null && sourceClass != null) {
            Field[] targetFields = targetClass.getDeclaredFields();

            for (Field targetField : targetFields) {
                try {
                    targetField.setAccessible(true);
                    Field sourceField = sourceClass.getDeclaredField(targetField.getName());
                    sourceField.setAccessible(true);

                    Object sourceValue = sourceField.get(source);

                    if (sourceValue != null) {
                        targetField.set(target, sourceValue);
                    }
                } catch (NoSuchFieldException e) {
                    // Eğer source'da field bulunamazsa, bu alanı atla
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Field erişim hatası: " + targetField.getName(), e);
                }
            }

            targetClass = targetClass.getSuperclass();
            sourceClass = sourceClass.getSuperclass();
        }

        return target;
    }


}
