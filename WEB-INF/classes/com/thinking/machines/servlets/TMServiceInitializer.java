package com.thinking.machines.servlets;
import com.thinking.machines.annotations.*;
import com.thinking.machines.servlets.*;
import com.google.gson.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import java.net.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.jar.*;
public class TMServiceInitializer extends HttpServlet
{
private List<String> classFilesList=new LinkedList<>();
public Map<String,Service> map=new HashMap<>();
public void traverseFiles(File[] files,int index)
{
String extension= "";
String fileName="";
String c="";
for(File file:files)
{
if(file.isDirectory())
{
traverseFiles(file.listFiles(),index);
}
else
{
fileName=file.getName();
int i = fileName.lastIndexOf('.');
if(i>0)
{
extension = fileName.substring(i+1);
if(extension.equals("class"))
{
c=(file.getAbsolutePath()).substring(index);
c=c.replace(File.separator,".");
c=c.substring(0,c.length()-6);
classFilesList.add(c);
} 
}
}
}
}
private void addJarFileClasses(String jarFilePath)
{
try 
{
JarInputStream jarInputStream = new JarInputStream(new FileInputStream(jarFilePath));
JarEntry jarEntry;
while (true) 
{
jarEntry=jarInputStream.getNextJarEntry();
if(jarEntry == null) 
{
break;
}
if((jarEntry.getName().endsWith(".class"))) 
{
System.out.println(jarEntry.getName());
String className=(jarEntry.getName()).replaceAll("/", ".");
System.out.println(className);
String classWithPackageName = className.substring(0, className.lastIndexOf('.'));
System.out.println(classWithPackageName);
classFilesList.add(classWithPackageName);
}
}
}catch(Exception e) 
{
e.printStackTrace();
}
}
public void init()
{
try
{
ServletContext sc=getServletContext();
String realPath=sc.getRealPath("");
String classFolderPath=realPath+"WEB-INF"+File.separator+"classes"+File.separator;
System.out.println(classFolderPath);
File fi=new File(classFolderPath);
File [] files=fi.listFiles();
int index=classFolderPath.length();
traverseFiles(files,index);

File f=new File(realPath+"WEB-INF"+File.separator+"serviceConfiguration.conf");
String libFolderPath=realPath+"WEB-INF"+File.separator+"lib"+File.separator;
System.out.println(libFolderPath);
if(f.exists())
{
FileReader fr=new FileReader(f);
JsonObject jo=new Gson().fromJson(fr,JsonObject.class);
JsonArray ja=jo.get("scanPackages").getAsJsonArray();
List<String> jarsToScan=new Gson().fromJson(ja.toString(),List.class);
for(String jar:jarsToScan)
{
System.out.println(libFolderPath+jar);
addJarFileClasses(libFolderPath+jar);
}
}

String rqs="";
String h="";
String path="";
String response="";
String type="";
Service service=null;
List<String> paraList=null;
Parameter[] parameters=null;

for(String classFile:classFilesList)
{
System.out.println(classFile);
Class cl=Class.forName(classFile);
Path p=(Path)cl.getAnnotation(Path.class);  
if(p!=null)
{
rqs=p.value();
Method[] m=cl.getMethods();
for(Method method:m)
{
p=(Path)method.getAnnotation(Path.class);  
if(p!=null)
{
service=new Service();
path=rqs+p.value();
response=p.responseType();
parameters=method.getParameters();
paraList=new LinkedList<>();
for(Parameter pa:parameters)
{
paraList.add(pa.getType().getName());
System.out.println(pa.getType().getName());
}
Forward fr=(Forward)method.getAnnotation(Forward.class);
if(fr!=null)
{
service.rqForward=fr.value();
}
Secured s=(Secured)method.getAnnotation(Secured.class);
if(s!=null)
{
service.secured=s.value();
}
RequiredFile rf=(RequiredFile)method.getAnnotation(RequiredFile.class);
if(rf!=null)
{
service.requiredFile=true;
}

service.cl=cl;
service.method=method;
service.path=path;
service.responseType=response;
service.paraList=paraList;
map.put(path,service);
}
}
}
}
sc.setAttribute("map",map);
}catch(Exception e)
{
e.printStackTrace();
}
}
}