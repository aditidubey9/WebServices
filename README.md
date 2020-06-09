# WebServices

# How to setup
Download these jar file.Put the jar file in your webapp's lib folder.Create servlet mapping in web.xml as follows

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
 
 put the gson jar file provided in the repository in your lib folder.
 
 # Services
 
 1)@Path:These annotation is compolsoury to create a service.It takes two value one url path second response type.
 eg.
      
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
  
  2)@Forward:These annotation is used to forward request.The method on which @Forward annotation is applied.No response will be sent.
  eg.
      
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
