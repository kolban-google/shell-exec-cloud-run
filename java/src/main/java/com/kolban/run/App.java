package com.kolban.run;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class App extends AbstractHandler {
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            response.setContentType("text/html;charset=utf-8");
            baseRequest.setHandled(true);
            String pathInfo = request.getPathInfo();
            String method = request.getMethod();
            if (pathInfo.equals("/exec") && method.equals("POST")) {
                String command = IOUtils.toString(request.getReader());
                Process p = Runtime.getRuntime().exec(command);
                if (p.waitFor(10, TimeUnit.SECONDS)) {
                    String stdoutString = IOUtils.toString(p.getInputStream(), "utf8");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().println(stdoutString);
                    return;
                }
                p.destroy();
                throw new Exception("Timed out running process");
            }
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h1>Hello World</h1>");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println(e.getMessage());
            //e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        int port;
        String portString = System.getenv("PORT");
        if (portString == null) {
            System.out.println("No Port found!!");
            portString = "8080";
            // return;
        }

        port = Integer.parseInt(portString);
        System.out.println("Port is: " + port);
        Server server = new Server(port);
        server.setHandler(new App());

        server.start();
        server.join();
    }
}
