package resourcesManage;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {
    private static ThreadManager instance = new ThreadManager();
    public static ThreadManager getInstance(){ return instance;}

    int currentThreadNumber=3;//
    ExecutorService service;
    public ThreadManager()
    {
    }
    public void CreateThreadPool()
    {
        service= Executors.newFixedThreadPool(currentThreadNumber);

    }
    public void EventProcessorByThread(Runnable clazz)
    {
        service.submit(clazz);
    }

}
