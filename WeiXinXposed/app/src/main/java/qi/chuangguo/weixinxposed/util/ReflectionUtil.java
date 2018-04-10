package qi.chuangguo.weixinxposed.util;

import android.util.Log;

import net.dongliu.apk.parser.bean.DexClass;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.XposedHelpers;

/**
 * Created by chuangguo.qi on 2018/4/10.
 */

public class ReflectionUtil {

    private static String TAG= ReflectionUtil.class.getName();
    private static final Map classesCache = new HashMap();

    public static final String getClassName(DexClass clazz) {
        String str = clazz.getClassType().replace('/', '.');
        return str.substring(1, str.length() - 1);
    }

    public static Classes findClassesFromPackage(ClassLoader loader, List<String> classes, String packageName, int depth) {
        if (classesCache.containsKey(packageName)) {
            return (Classes) classesCache.get(packageName + depth);
        }
        List<String> classNameList = new ArrayList();
        for (int i = 0; i < classes.size(); i++) {
            String clazz = classes.get(i);
            String currentPackage = clazz.substring(0, clazz.lastIndexOf("."));
            for (int j = 0; j < depth; j++) {
                int pos = currentPackage.lastIndexOf(".");
                if (pos < 0)
                    break;
                currentPackage = currentPackage.substring(0, currentPackage.lastIndexOf("."));
            }
            if (currentPackage.equals(packageName)) {
                classNameList.add(clazz);
            }
        }

        List<Class> classList = new ArrayList();
        int classNameListSize=classNameList.size();
        for (int i = 0; i < classNameListSize; i++) {
            String className = classNameList.get(i);
            Class c = findClassIfExists(className, loader);
            if (c != null) {
                classList.add(c);
            }
        }
        Classes cs = new Classes(classList);
        classesCache.put(packageName + depth, cs);
        return cs;
    }

    public static final Class findClassIfExists(String className, ClassLoader classLoader) {
        Class c = null;
        try {
            c = XposedHelpers.findClass(className, classLoader);
            Log.i(TAG, "findClassIfExists: c:"+c);
        } catch (Error | Exception e) {
        }
        return c;
    }

    public static Method findMethodsByExactParameters(Class clazz, Class returnType, Class... parameterTypes){
        if (clazz == null) {
            return null;
        }
        Method[] methodsByExactParameters = XposedHelpers.findMethodsByExactParameters(clazz, returnType, (Class[]) Arrays.copyOf(parameterTypes, parameterTypes.length));

        List<Method> list = Arrays.asList(methodsByExactParameters);
        if (list.isEmpty()){
            return null;
        }else if (list.size() > 1) {
            for (int i = 0; i < list.size(); i++) {
                Log.i(TAG,"methods" + i + ": " + list.get(i));
            }
        }
        return list.get(0);
    }


    public static class Classes{
        public final List<Class> classes;
        public Classes(List<Class> list) {
            this.classes = (List<Class>) list;
        }
        public final Classes filterByNoMethod(Class<?> cls, Class<?>... clsArr) {
            List arrayList = new ArrayList();
            for (Object next : this.classes) {
                Log.i(TAG, "filterByNoMethod: next:"+next.getClass().getName());
                if (ReflectionUtil.findMethodsByExactParameters((Class) next, cls, (Class[]) Arrays.copyOf(clsArr, clsArr.length)) == null) {
                    arrayList.add(next);
                }
            }
            return new Classes(arrayList);
        }

        public final Classes filterByMethod(Class<?> cls, Class<?>... clsArr) {
            Log.i(TAG, "filterByMethod: =======================");
            List arrayList = new ArrayList();
            for (Object next : this.classes) {
                if (ReflectionUtil.findMethodsByExactParameters((Class) next, cls, (Class[]) Arrays.copyOf(clsArr, clsArr.length)) != null) {
                    Log.i(TAG, "filterByMethod: next:"+((Class) next).getName());
                    arrayList.add(next);
                }
            }
            return new Classes(arrayList);
        }

        public final Classes filterByNoField(String fieldType) {
            List arrayList = new ArrayList();
            for (Object next : this.classes) {
                if (ReflectionUtil.findFieldsWithType((Class) next, fieldType).isEmpty()) {
                    arrayList.add(next);
                }
            }

            return new Classes(arrayList);
        }

        public final Classes filterByField(String fieldType) {
            List arrayList = new ArrayList();
            for (Object next : this.classes) {
                if (!ReflectionUtil.findFieldsWithType((Class) next, fieldType).isEmpty()) {

                    arrayList.add(next);
                }
            }
            return new Classes(arrayList);
        }

        public final Classes filterByField(String fieldName, String fieldType) {
            Log.i(TAG, "filterByField: =======================");
            List arrayList = new ArrayList();
            for (Object next : this.classes) {
                Field field = ReflectionUtil.findFieldIfExists((Class) next, fieldName);

                if (field != null && field.getType().getCanonicalName().equals(fieldType)) {
                    Log.i(TAG, "filterByField: next:"+((Class) next).getName());
                    arrayList.add(next);
                }
            }
            return new Classes(arrayList);
        }

        public final Classes filterByMethod(Class returnType, String methodName, Class... parameterTypes) {
            Log.i(TAG, "filterByMethod: ");
            List arrayList = new ArrayList();
            for (Object next : this.classes) {
                Method method = ReflectionUtil.findMethodExactIfExists((Class) next, methodName, (Class[]) Arrays.copyOf(parameterTypes, parameterTypes.length));
                if (method != null && method.getReturnType().getName().equals(returnType.getName())) {
                    Log.i(TAG, "filterByMethod: next:"+((Class) next).getName());
                    arrayList.add(next);
                }
            }

            return new Classes(arrayList);
        }

        public final Class<?> firstOrNull() {
            if (this.classes.isEmpty())
                return null;
            else if (this.classes.size() > 1) {
                Log.i(TAG, "find too many classes");
                for (int i = 0; i < this.classes.size(); i++) {
                    Log.i(TAG,"class" + i + ": " + this.classes.get(i));
                }
            }
            return this.classes.get(0);
        }

    }

    public static final Field findFieldIfExists(Class clazz, String fieldName) {
        if (clazz == null) {
            return null;
        }
        Field field = null;
        try {
            field = clazz.getField(fieldName);
        } catch (Error | Exception e) {
        }
        return field;
    }

    public static final List<Field> findFieldsWithType(Class clazz, String typeName) {
        List<Field> list = new ArrayList<Field>();
        if (clazz == null) {
            return list;
        }
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null) {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                Class fieldType = field.getType();
                if (fieldType.getName().equals(typeName)) {
                    list.add(field);
                }
            }
        }
        return list;
    }

    public static final Method findMethodExactIfExists(Class clazz, String methodName, Class... parameterTypes) {
        Method method = null;
        try {
            method = XposedHelpers.findMethodExact(clazz, methodName, (Class[]) Arrays.copyOf(parameterTypes, parameterTypes.length));
        } catch (Error | Exception e) {
        }
        return method;
    }

}
