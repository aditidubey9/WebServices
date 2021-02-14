package com.thinking.machines.servlets;
import com.thinking.machines.annotations.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class AddStudentSecurity implements SecureMethodInterface
{
public boolean isMethodSecured(ServletContext sc,HttpSession hs,HttpServletRequest rq)
{
return true;
}
public String ifNotSecured()
{
return "/service/student/error";
}
}