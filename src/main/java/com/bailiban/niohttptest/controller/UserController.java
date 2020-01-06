package com.bailiban.niohttptest.controller;


import com.bailiban.niohttptest.ServerTest;
import com.bailiban.niohttptest.model.MethodInfo;
import com.bailiban.niohttptest.model.User;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

//@MyRestController
public class UserController {


        private static List<User> userList = new ArrayList<>();

        static {
            userList.add(new User(1, "Jim"));
            userList.add(new User(2, "Lily"));
        }

        @MyRequestMapping("/get")
        public String get(int id) {
            id = id-1;
            return userList.get(id).toString();
        }

        @MyRequestMapping("/getAll")
        public String getAll() {
            return userList.toString();
        }

    public static void main(String[] args) {
        Method[] methods = UserController.class.getDeclaredMethods();
        for (Method method:methods){
            MyRequestMapping annotation = method.getAnnotation(MyRequestMapping.class);
            if (annotation==null){
                continue;
            }
//            System.out.println(annotation.value()+" || method: "+method.getName());

            ServerTest.methodMap.put(annotation.value(),
                    new MethodInfo(method,"userController"));
        }

    }



}
