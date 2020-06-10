# WebServices

# How to setup
Download the jar file provided in the repository.Put the jar file in your webapps/lib folder.Create servlet mapping in web.xml as follows

    <servlet>
    <servlet-name>TMServiceInitializer</servlet-name>
    <servlet-class>com.thinking.machines.servlets.TMServiceInitializer</servlet-class>
    <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet>
    <servlet-name>TMService</servlet-name>
    <servlet-class>com.thinking.machines.servlets.TMService</servlet-class>
    </servlet>
    <servlet-mapping>
    <servlet-name>TMService</servlet-name>
    <url-pattern>/service/*</url-pattern>
    </servlet-mapping>
 
 # Services
 
 1) @Path:This annotation is compulsary to create a service.It takes two value one url path and second response type.It can be applied     on class and on method.
 example-
      
      import com.thinking.machines.annotations.*;
      @Path(value="/student")
      class Student
      {
      @Path(value="/add",responseType="text/html")
      public String add()
      {
        return "service example";
      }
      }
  
  responseType can be assigned 3 values 1)text/html 2)json 3)none
  
  2)@Forward:This annotation is used to forward request.The method on which @Forward annotation is applied.No response will be sent.It    can only be applied on method.
 
  example-
      
      @Path(value="/student")
      class Student
      {
      @Forward("/forward.html")
      @Path(value='/forward',responseType='none')
      public void forward()
      {
        //some processing
      }
      }
  
  3)@RequiredFile:This annotation is applied on the method in which you required files.It can only applied on method.The parameter of       method should be of type File[].It can only be applied on method.

example-

    @Path("/student")
    class Student
    {
     @Path(value="/file",responseType='none')
    @RequiredFile
     public void fileUpload(File[] file)
    {
    //code to copy files to your preferred directory
    }
    }

  4)@Secured:This annotation is used to provide security to your method.For applying these annotation you have to create a seperate        class which implements SecuredMethodInterface,which is available in package com.thinking.machines.annotations.It can only be applied    on method.
   
   example-
   
    import com.thinking.machines.annotations.*
    class MethodSecurity implements SecuredMethodAnnotation
    {
    public boolean isMethodSecured(HttpServletRequest request,HttpSession session,ServletContext contex)
    {
    //some checks
    return true/fasle;
    } 
    public String ifNotSecured() //you have to return a url and request will forward for that.
    {
    return "/student/error";
    }
    //It is compulsory to implement both methods
    }

    @Path("/student")
    class Student
    {
    @Path("/secure")
    @Secured("com.xyz.MethodSecurity")
    public secureMethod()
    {
    //some processing
    }
    }
# Compilation of Service
Use following command:
		                    
    javac -classpath c:\tomcat9\webapps\name_of_your_web_app\WEB-INF\lib\*;c:\tomcat9\lib\*;. *.java
		
Note:replace c:\tomcat9 with your webserver root folder name

# Creating Service as a jar file

To create your service class as a jar file,you have to create the jar file of your service classes and put that jar file in your webapps's lib folder.You can optimize the framework by creating a file named servletConfiguration.conf in your WEB-INF folder which is a json file.Specify the names of jar which contains services and framework will only scan those jar and leave others.

example

		{
			jarsToScan:["jar1.jar","jar2.jar",...]
		}

# Some rules for method

1)You have to create method with only one user-defined class.

2)You can add HttpServletRequest,HttpSession,ServletContext along with that.

3)All the data should be transferred in json form.

