package jacamo.platform;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import cartago.ArtifactId;
import cartago.ArtifactInfo;
import cartago.ArtifactObsProperty;
import cartago.CartagoException;
import cartago.CartagoService;
import jacamo.util.Config;

public class EnvironmentWebInspector implements Platform {
    
    boolean webOn = true;

    @Override
    public void init(String[] args) {
        if (args.length == 1) {
            Config.get().setProperty(Config.START_WEB_EI, args[0]);
            webOn = !"false".equals(args[0]);
        }
    }

    @Override
    public void start() {
        if (webOn) {
            startHttpServer();
            registerWorkspace(CartagoService.MAIN_WSP_NAME);
        }
    }
    
    public static String getArtHtml(String wId, ArtifactInfo info) {
        StringBuilder out = new StringBuilder("<html>");
        //out.append("<head><meta http-equiv=\"refresh\" content=\""+refreshInterval+"\"></head>");
        out.append("<span style=\"color: red; font-family: arial\"><font size=\"+2\">");
        out.append("Inspection of artifact <b>"+info.getId().getName()+"</b> in workspace "+wId+"</font></span>");
        out.append("<table border=0 cellspacing=3 cellpadding=6 style='font-family:verdana'>");
        for (ArtifactObsProperty op: info.getObsProperties()) {
            StringBuilder vls = new StringBuilder();
            String v = "";
            for (Object vl: op.getValues()) {
                vls.append(v+vl);
                v = ",";
            }
            out.append("<tr><td>"+op.getName()+"</td><td>"+vls+"</td></tr>");
        }
        out.append("</table>");
        /*out.append("</br>Operations:<ul>");
        for (OperationInfo op: info.getOngoingOp()) {
            out.append("<li>"+op.toString()+"</li>");
        }
        for (OpDescriptor op: info.getOperations()) {
            out.append("<li>"+op.getOp().getName()+"</li>");
        }
        out.append("</ul>");*/
        out.append("</html>");
        return out.toString();
    }

    /** Http Server for GUI */

    static HttpServer httpServer = null;
    static int        httpServerPort = 3273;
    static String     httpServerURL = "http://localhost:"+httpServerPort;

    static Set<String> wrkps = new HashSet<>();

    static Set<String> hidenArts = new HashSet<>( Arrays.asList(new String[] {
        "node",
        "console",
        "blackboard",
        "workspace",
        "manrepo",
    }));

    public static synchronized String startHttpServer()  {
        if (httpServer == null) {
            try {
                httpServer = HttpServer.create(new InetSocketAddress(httpServerPort), 0);
                httpServer.setExecutor(Executors.newCachedThreadPool());
                registerRootBrowserView();
                registerWksListBrowserView();
                httpServer.start();
                httpServerURL = "http://"+InetAddress.getLocalHost().getHostAddress()+":"+httpServerPort;
                System.out.println("CArtAgO Http Server running on "+httpServerURL);
            } catch (BindException e) {
                httpServerPort++;
                return startHttpServer();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return httpServerURL;
    }

    static void registerRootBrowserView() {
        if (httpServer == null)
            return;
        try {
            httpServer.createContext("/", new HttpHandler() {
                public void handle(HttpExchange exchange) throws IOException {
                    String requestMethod = exchange.getRequestMethod();
                    Headers responseHeaders = exchange.getResponseHeaders();
                    responseHeaders.set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(200, 0);
                    OutputStream responseBody = exchange.getResponseBody();

                    if (requestMethod.equalsIgnoreCase("GET")) {
                        String path = exchange.getRequestURI().getPath();
                        StringWriter so = new StringWriter();

                        if (path.length() < 2) { // it is the root
                            so.append("<html><head><title>CArtAgO Web View</title></head><body>");
                            so.append("<iframe width=\"20%\" height=\"100%\" align=left src=\"/indexarts\" border=5 frameborder=0 ></iframe>");
                            so.append("<iframe width=\"78%\" height=\"100%\" align=left src=\"/arts\" name=\"arts\" border=5 frameborder=0></iframe>");
                            so.append("</body></html>");
                        }
                        responseBody.write(so.toString().getBytes());
                    }
                    responseBody.close();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerWorkspace(String w) {
        wrkps.add(w);
        registerWksBrowserView(w);
    }

    private static void registerWksListBrowserView() {
        if (httpServer == null)
            return;
        try {
            httpServer.createContext("/indexarts", new HttpHandler() {
                public void handle(HttpExchange exchange) throws IOException {
                    String requestMethod = exchange.getRequestMethod();
                    Headers responseHeaders = exchange.getResponseHeaders();
                    responseHeaders.set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(200, 0);
                    OutputStream responseBody = exchange.getResponseBody();

                    if (requestMethod.equalsIgnoreCase("GET")) {
                        //responseBody.write(("<html><head><title>CArtAgO (list of artifacts)</title><meta http-equiv=\"refresh\" content=\""+refreshInterval+"\" ></head><body>").getBytes());
                        responseBody.write(("<html><head><title>CArtAgO (list of artifacts)</title></head><body>").getBytes());
                        responseBody.write(("<font size=\"+2\"><span style='color: red; font-family: arial;'>workspaces</span></font><br/>").getBytes());
                        StringWriter out  = new StringWriter();
                        for (String wname: wrkps) {
                            try {
                                out.append("<br/><scan style='color: red; font-family: arial;'>"+wname+"</scan> <br/>");
                                for (ArtifactId aid: CartagoService.getController(wname).getCurrentArtifacts()) {
                                    if (hidenArts.contains(aid.getName()))
                                        continue;
                                    if (aid.getName().endsWith("-body"))
                                        continue;
                                    String addr = wname+"/"+aid.getName();
                                    out.append(" - <a href=\""+addr+"\" target=\"arts\" style=\"font-family: arial; text-decoration: none\">"+aid.getName()+"</a><br/>");
                                }
                            } catch (CartagoException e) {
                                e.printStackTrace();
                            }
                        }
                        responseBody.write( out.toString().getBytes());
                    }
                    responseBody.write("<hr/>by <a href=\"http://cartago.sourceforge.net\" target=\"_blank\">CArtAgO</a>".getBytes());
                    responseBody.write("</body></html>".getBytes());
                    responseBody.close();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void registerWksBrowserView(final String id) {
        if (httpServer == null)
            return;
        try {
            httpServer.createContext("/"+id, new HttpHandler() {
                public void handle(HttpExchange exchange) throws IOException {
                    String requestMethod = exchange.getRequestMethod();
                    Headers responseHeaders = exchange.getResponseHeaders();
                    exchange.sendResponseHeaders(200, 0);
                    OutputStream responseBody = exchange.getResponseBody();
                    responseHeaders.set("Content-Type", "text/html");
                    if (requestMethod.equalsIgnoreCase("GET")) {
                        try {
                            String path = exchange.getRequestURI().getPath();
                            int p = path.lastIndexOf("/");
                            path = path.substring(p+1);

                            ArtifactInfo info = CartagoService.getController(id).getArtifactInfo(path);
                            responseBody.write(getArtHtml(id, info).getBytes());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    responseBody.close();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
