package com.test.ioc.servlet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.ioc.annotation.Autowired;
import com.test.ioc.annotation.Component;
import com.test.ioc.annotation.Controller;
import com.test.ioc.annotation.RequestMapping;
import com.test.ioc.annotation.Service;
import com.test.ioc.controller.UserController;

/**
 *模拟springmvc框架的ioc流程 
 *
 */
public class DispatcherServlet extends HttpServlet {
	
	private List<String> packageNames = new ArrayList<String>();  
	private Map<String, Object> instanceMap = new HashMap<String, Object>();
	private Map<String, Object> handerMap = new HashMap<String, Object>();  
	
	public DispatcherServlet() {  
        super();  
    } 
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		scanPackage("com.test.ioc");
		try {
			filterAndInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ioc();
		handerMap();
	}

	private void scanPackage(String Package) {
		URL url = this.getClass().getClassLoader().getResource("/" + replaceTo(Package));// 将所有的.转义获取对应的路径  
        String pathFile = url.getFile();  
        File file = new File(pathFile);  
        String fileList[] = file.list();  
        for (String path : fileList) {  
            File eachFile = new File(pathFile + path);  
            if (eachFile.isDirectory()) {  
                scanPackage(Package + "." + eachFile.getName());  
            } else {  
                packageNames.add(Package + "." + eachFile.getName());  
            }  
        } 
	}
	
	private String replaceTo(String path) {  
        return path.replaceAll("\\.", "/");  
    }
	
	private void filterAndInstance() throws Exception{
		if (packageNames.size() <= 0) {  
            return;  
        }  
        for (String className : packageNames) {  
            Class<?> cName = Class.forName(className.replace(".class", "").trim());
            //bean 实例化之前获取增强方法
            if (cName.isAnnotationPresent(Controller.class)) {  
                Object instance = cName.newInstance();  
                //将cName类转为注解类
                Controller controller = (Controller) cName.getAnnotation(Controller.class);  
                String key = controller.value(); 
                //如果@Controller(value="")的话,value默认设置为userController的形式
                if(key.equals("")){
                	key = cutName(cName.getName(), 1);
                }
                instanceMap.put(key, instance);  
            } else if (cName.isAnnotationPresent(Service.class)) {  
                Object instance = cName.newInstance();  
                Service service = (Service) cName.getAnnotation(Service.class);  
                String key = service.value();  
                if(key.equals("")){
                	key = cutName(cName.getName(), 1);
                }
                instanceMap.put(key, instance);  
            } else if (cName.isAnnotationPresent(Component.class)) {  
                Object instance = cName.newInstance();  
                Component Component = (Component) cName.getAnnotation(Component.class);  
                String key = Component.value();  
                if(key.equals("")){
                	key = cutName(cName.getName(), 1);
                }
                instanceMap.put(key, instance);  
            } else {  
                continue;  
            }  
        }  
	}

	private void ioc() {
		if (instanceMap.isEmpty())  
            return;  
        for (Map.Entry<String, Object> entry : instanceMap.entrySet()) {  
            // 拿到里面的所有属性  
            Field fields[] = entry.getValue().getClass().getDeclaredFields();  
            for (Field field : fields) {  
                field.setAccessible(true);// 可访问私有属性  
                if (field.isAnnotationPresent(Autowired.class));  
                Autowired Autowired = field.getAnnotation(Autowired.class);  
                String value = Autowired.value();  
                field.setAccessible(true);  
                try {  
                    field.set(entry.getValue(), instanceMap.get(value));  
                } catch (IllegalArgumentException e) {  
                    e.printStackTrace();  
                } catch (IllegalAccessException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
	}
	
	private static String cutName(String name, int position){
		String[] array= name.split("\\.");
		char[] arrays = array[array.length-position].toCharArray();
		arrays[0]+=32;
		return String.valueOf(arrays);
	}
	
	private static String cutName(String name){
		String[] array= name.split("\\(");
		return cutName(array[0], 2);
	}
	
	 /** 
     * 建立映射关系 
     */  
    private void handerMap() { 
    	//   /say---public void com.test.ioc.controller.UserController.sayHello()
        if (instanceMap.size() <= 0)  
            return;  
        for (Map.Entry<String, Object> entry : instanceMap.entrySet()) {  
            if (entry.getValue().getClass().isAnnotationPresent(Controller.class)) {  
                Controller controller = (Controller) entry.getValue().getClass().getAnnotation(Controller.class);  
                String ctvalue = controller.value();  
                Method[] methods = entry.getValue().getClass().getMethods();  
                for (Method method : methods) {  
                    if (method.isAnnotationPresent(RequestMapping.class)) {  
                        RequestMapping rm = (RequestMapping) method.getAnnotation(RequestMapping.class);  
                        String rmvalue = rm.value();  
//                        handerMap.put("/" + ctvalue + "/" + rmvalue, method);
                        handerMap.put(rmvalue, method);
                    } else {  
                        continue;  
                    }  
                }  
            } else {  
                continue;  
            }  
  
        }  
        Set<String> set = handerMap.keySet();
        for(String string : set){
        	System.out.println(string + "---" + handerMap.get(string));
        }
    }  
    
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp); 
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			String url = req.getRequestURI();  
	        String context = req.getContextPath();  
	        String path = url.replace(context, "");  
	        Method method = (Method) handerMap.get(path);  
	        String name = cutName(method.toString());
	        UserController controller = (UserController) instanceMap.get(name);  
	        try {  
	            method.invoke(controller, new Object[] { req, resp, null });  
	        } catch (IllegalAccessException e) {  
	            e.printStackTrace();  
	        } catch (IllegalArgumentException e) {  
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {  
	            e.printStackTrace();  
	        }  
	    }  
}

