package com.thinking.machines.servlets;
import com.thinking.machines.annotations.*;
import com.thinking.machines.beans.*;
import com.thinking.machines.servlets.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import java.net.*;
import java.lang.annotation.*;
import javax.servlet.annotation.*;
import java.lang.reflect.*;
import com.google.gson.*;
@MultipartConfig(maxFileSize=(1024*1024*5))
public class TMService extends HttpServlet
{
public void doGet(HttpServletRequest rq,HttpServletResponse rs)
{
doPost(rq,rs);
}
public void doPost(HttpServletRequest rq,HttpServletResponse rs)
{ 
try
{
ServletContext sc=getServletContext();
Map<String,Service> map=(Map)sc.getAttribute("map");
String responseString="";
String jsonString="";
int i=0;
String s=rq.getRequestURI();
int index=rq.getContextPath().length()+"/service".length();
String r=s.substring(index);
Object h=null;
Object response=null;
boolean ifSecured=true;
String value;
Gson gson=new Gson();
List<String> filesList=null;
boolean rqServed=false;
String fileFolderPath="";
File[] uploadFiles=null;
if(map.containsKey(r))
{
Service service=map.get(r);
Class cl=service.cl;
Method method=service.method;
if(service.instance==null)
{
service.instance=cl.newInstance();
}
Class c=null;
Method m=null;
if(service.requiredFile==true)
{
fileFolderPath=getServletContext().getRealPath("");
fileFolderPath=fileFolderPath+"WEB-INF"+File.separator+"filestore"+File.separator;
filesList=new LinkedList<>();
for(Part part:rq.getParts())
{
String cd=part.getHeader("content-disposition");
String pcs[]=cd.split(";");
for(String pc:pcs)
{
if(pc.indexOf("filename")!=-1)
{
String fn=pc.substring(pc.indexOf("=")+2,pc.length()-1);
System.out.println("Considering & saving : "+fn);
filesList.add(fileFolderPath+fn);
File file=new File(fileFolderPath+fn);
if(file.exists()) file.delete();
part.write(fileFolderPath+fn);
System.out.println("File saved");
}
}
}
uploadFiles=new File[filesList.size()];
File f=null;
i=0;
for(String g:filesList)
{
f=new File(g);
System.out.println(g);
uploadFiles[i]=f;
i++;
}
}
else
{
BufferedReader br=rq.getReader();
StringBuilder sb=new StringBuilder();
String line;
while(true)
{
line=br.readLine();
if(line==null)break;
sb.append(line);
}
jsonString=sb.toString();
System.out.println("jsonString is  "+jsonString);
}
if((service.secured).length()!=0)
{
c=Class.forName(service.secured);
m=c.getMethod("isMethodSecured",ServletContext.class,HttpSession.class,HttpServletRequest.class);
h=m.invoke(c.newInstance(),sc,rq.getSession(true),rq);
System.out.println(h);
ifSecured=(boolean)h;
}
if(ifSecured==false)
{
m=c.getMethod("ifNotSecured");
h=m.invoke(c.newInstance());
System.out.println(h);
RequestDispatcher rd;
rd=rq.getRequestDispatcher((String)h);
rd.forward(rq,rs);
}
else
{
Object oo[]=new Object[service.paraList.size()];
i=0;
for(String name:service.paraList)
{
if(name.contains("HttpServletRequest"))
{
oo[i]=rq;
}
else
{
if(name.contains("File"))
{
oo[i]=uploadFiles;
}
else
{
oo[i]=gson.fromJson(jsonString,Class.forName(name));
}
}
i++;
}
h=method.invoke(service.instance,oo);
if((service.rqForward).length()==0)
{
if(service.responseType.length()==0)
{
}
else
{
if(service.responseType.equalsIgnoreCase("json"))
{
jsonString=gson.toJson(h);
System.out.println(jsonString);
rs.setContentType("application/json");
response=jsonString;
AjaxResponse ajaxResponse=new AjaxResponse();
ajaxResponse.response=response;
ajaxResponse.success=true;
ajaxResponse.exception="";
responseString=gson.toJson(ajaxResponse);
rqServed=true;
}
if(service.responseType.equalsIgnoreCase("none"))
{
rs.setContentType("text/html");
responseString="";
rqServed=true;
}
if(service.responseType.equalsIgnoreCase("text/html"))
{
rs.setContentType("text/html");
responseString=(String)h;
rqServed=true;
}

if(rqServed==true)
{
PrintWriter pw=rs.getWriter();
pw.print(responseString);
}
else
{
PrintWriter pw=rs.getWriter();
pw.print("404");
}
}
}
else
{
System.out.println(service.rqForward);
RequestDispatcher rd;
rd=rq.getRequestDispatcher(service.rqForward);
rd.forward(rq,rs);
}
}
}
}catch(Exception e)
{
e.printStackTrace();
}
}
}
