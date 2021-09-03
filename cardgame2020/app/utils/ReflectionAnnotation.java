package utils;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
/**Creating Object by reflection.*/
@SuppressWarnings("unchecked")
public class ReflectionAnnotation {
    /**Get the classes which implements the @interface*/
    public static List<Class> scanClasses(Class c){
        List<Class>returnClassList=null;
        if(c.isInterface())
        {
            //get the package Name of annotation
            String packageName=c.getPackage().getName();
            //Get all classes in this Package
            List<Class<?>>allClesses=getClasses(packageName);
            if(allClesses!=null)
            {
                returnClassList=new ArrayList<>();
                for(Class classes:allClesses)
                {
                    //Determine if it is the same interface
                    if(c.isAssignableFrom(classes))
                    {
                        if(!c.equals(classes))
                        {
                            returnClassList.add(classes);
                        }
                    }
                }
            }
        }
        return returnClassList;
    }
    /**Get all classes in a package*/
    public static List<Class<?>>getClasses(String packageName)
    {
        List<Class<?>>classes=new ArrayList<Class<?>>();
        boolean recurisve=true;
        String packageDirName=packageName.replace('.','/');
        Enumeration<URL> dirs;
        try
        {

            //Get the path of the first package
            dirs=Thread.currentThread().getContextClassLoader().getResources(packageDirName);

            //if there is another package
            while(dirs.hasMoreElements())
            {

                URL url=dirs.nextElement();
                String protocol=url.getProtocol();
                //if the package is saved on a server.
                if("file".equals(protocol)){
                    //get the path of the package
                    String filePath= URLDecoder.decode(url.getFile(),"UTF-8");
                    //Scan the files in the file
                    findAndAddClassesInPackageByFile(packageName,filePath,recurisve,classes);
                }else if("jar".equals(protocol)){
                    JarFile jar;
                    try{
                        //get the jar doc.
                        jar=((JarURLConnection)url.openConnection()).getJarFile();
                        Enumeration<JarEntry>entries=jar.entries();
                        while(entries.hasMoreElements())
                        {
                            //get a jar object.
                            JarEntry entry=entries.nextElement();
                            //get the name of a jar.
                            String name=entry.getName();
                            if(name.charAt(0)=='/')
                            {
                                name=name.substring(1);
                            }
                            if(name.startsWith(packageDirName))
                            {
                                int idx=name.lastIndexOf('/');
                                if(idx!=-1)
                                {
                                    packageName=name.substring(0,idx).replace('/','.');
                                }
                                if((idx!=-1)||recurisve)
                                {
                                    if(name.endsWith(".class")&&!entry.isDirectory())
                                    {
                                        String className=name.substring(packageName.length()+1,name.length()-6);
                                        try {
                                            classes.add(Class.forName(packageName+'.'+className));
                                        }catch (ClassNotFoundException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                            }
                        }
                    }catch (Exception e){

                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e)
        {

            e.printStackTrace();
        }
        return classes;
    }
    /**Get all classes in a package by a file form*/
    public static void findAndAddClassesInPackageByFile(String packageName,String packagePath, final boolean recursive,List<Class<?>>classes)
    {
        File dir=new File(packagePath);

        if(!dir.exists()||!dir.isDirectory())
        {
            return;
        }
        File[]dirfiles=dir.listFiles(new FileFilter()
        {
            public boolean accept(File file)
            {
                return (recursive&&file.isDirectory()||(file.getName().endsWith(".class")));
            }
        });
        for(File file:dirfiles)
        {
            if(file.isDirectory())
            {
                findAndAddClassesInPackageByFile(packageName+"."+file.getName(),file.getAbsolutePath(),recursive,classes);
            }else
            {
                // if it is a java doc. delete .class
                String className=file.getName().substring(0,file.getName().length()-6);
                try{
                    classes.add(Class.forName(packageName+"."+className));
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}
