package com.thinking.machines.annotations;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
public interface SecureMethodInterface
{
public boolean isMethodSecured(ServletContext sc,HttpSession hs,HttpServletRequest rq);
public String ifNotSecured();
}