package com.thinking.machines.servlets;
import java.lang.annotation.*;
import java.lang.reflect.*;
import com.thinking.machines.annotations.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
@Path(value="/student")
public class Student
{
@Secured("com.thinking.machines.servlets.AddStudentSecurity")
@Forward("/add.html")
@Path(value="/add")
public StudentInfo add(StudentInfo studentInfo,HttpServletRequest rq)throws Exception
{
return studentInfo;
}
@Path(value="/edit",responseType="None")
public void edit(StudentInfo studentInfo)throws Exception
{
}
@Path(value="/delete",responseType="text/html")
public String delete(StudentInfo studentInfo)throws Exception
{
return "cool stuff";
}
@Path(value="/error",responseType="text/html")
public String error()throws Exception
{
return "error page";
}
@RequiredFile()
@Path(value="/upload")
public void upload(File[] files)throws Exception
{
String dstFolder="c:/javaeg/testfiles";
for(File file:files)
{
boolean b=file.renameTo(new File(dstFolder+file.getName()));
}
}
}
