package com.thinking.machines.servlets;
import java.io.*;
import java.net.*;
import java.lang.reflect.*;
import java.util.*;
public class Service implements java.io.Serializable
{
public String path;
public Class cl;
public Method method;
public Object instance=null;
public List<String> paraList=null;
public String responseType="";
public String rqForward="";
public String secured="";
public boolean requiredFile=false;
}