package servlet;


import helper.ServletObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zhongqin on 7/4/17.
 */
public class HeartBeatServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletObject servletObject = new ServletObject();
        servletObject.set(request, response);
        servletObject.setJsonObject("STATUS","OK");
        servletObject.setLogging(false);
        servletObject.respond();
    }
}
