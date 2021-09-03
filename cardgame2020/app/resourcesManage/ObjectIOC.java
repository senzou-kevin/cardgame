package resourcesManage;

import annotation.IOCResource;
import annotation.IOCService;
import utils.ReflectionAnnotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
@SuppressWarnings("unchecked")
public class ObjectIOC {
    private static ObjectIOC instance = new ObjectIOC("gamelogic");
    public static ObjectIOC getInstance(){
        return instance;}
    private String path;
    // key beanid value Object
    ConcurrentHashMap<String,Object> initBean=null;
    private ObjectIOC(String path)
    {
        this.path=path;
    }
    public void Initialization()
    {
        try
        {
            List<Class> classes=findAnnoationService();
            if(classes==null||classes.isEmpty())
            {
                throw new Exception("no found anything bean is useding inital");
            }
            initBean=initBean(classes);
            if(initBean==null||initBean.isEmpty())
            {
                throw new Exception("initial bean is empty or null");
            }
            Iterator iter=initBean.entrySet().iterator();
            while(iter.hasNext())
            {
                Map.Entry entry=(Map.Entry)iter.next();
                initAttribute(entry.getValue());
            }
        }catch (Exception e)
        {
        }
    }
    /**Get bean by beanid
     * @param beanId
     * @return
     * @throws Exception*/
    public Object getBean(String beanID)
    {
       return initBean.get(beanID);
    }
    /**initialize bean
     *  @param classes
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public ConcurrentHashMap<String,Object>initBean(List<Class>classes)throws IllegalAccessException,InstantiationException
    {
        ConcurrentHashMap<String,Object>map=new ConcurrentHashMap<>();
        String beanId="";
        Object object=null;
        for(Class clazz:classes){
            try{ object=clazz.getDeclaredConstructor().newInstance();}
            catch (Exception e) {
            }
            IOCService annotation=(IOCService) clazz.getDeclaredAnnotation(IOCService.class);
            if(annotation!=null){
                String value=annotation.name();
                if(value!=null&&!value.equals(""))
                {
                    beanId=value;
                }
                else{
                    beanId=toLowerCaseFirstOne(clazz.getSimpleName());
                }
            }
            map.put(beanId,object);
        }
        return map;
    }
    /**Initialize the dependency*/
    public void initAttribute(Object obj)throws Exception
    {
        Class<? extends  Object>classInfo=obj.getClass();
        //get all attributes
        Field[]fields=classInfo.getDeclaredFields();
        for(Field field:fields)
        {
            boolean flag=field.isAnnotationPresent(IOCResource.class);
            if(flag)
            {
                IOCResource iocResource=field.getAnnotation(IOCResource.class);
                if(iocResource!=null)
                {
                    String beanId=field.getName();
                    beanId=toUpperCaseFirstOne(beanId);
                    Object attrObject=getBean(beanId);
                    if(attrObject!=null)
                    {
                        field.setAccessible(true);
                        field.set(obj,attrObject);
                        continue;
                    }
                }
            }
        }

    }
    public static String toLowerCaseFirstOne(String s)
    {
        if(Character.isLowerCase(s.charAt(0)))
        {
            return s;
        }
        else
        {
            return (new StringBuffer()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }
    public static String toUpperCaseFirstOne(String s)
    {
        if(Character.isUpperCase(s.charAt(0)))
        {
            return s;
        }
        else
        {
            return (new StringBuffer()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }
    /**Get all classes which hava annotation in path*/
    public List<Class>findAnnoationService()throws  Exception
    {
        if(path==null||path.equals(""))throw new Exception("scan package is null or empty");
        List<Class<?>>classes= ReflectionAnnotation.getClasses(path);
        if(classes==null||classes.size()==0)
        {
            throw new Exception("not find service is added annoation for @IOCService");
        }
        List<Class> annoationClasses=new ArrayList<Class>();
        for(Class clazz:classes)
        {
            IOCService iosService=(IOCService) clazz.getDeclaredAnnotation(IOCService.class);
            if(iosService!=null)
            {
                annoationClasses.add(clazz);
                continue;
            }
        }
        return annoationClasses;
    }
}
