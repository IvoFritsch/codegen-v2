/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import auxiliar.ConsolePrinter;
import auxiliar.ServerTemplatesDataSupplier;
import auxiliar.ServerTemplatesProcessor;
import database.CodegenDatabaseController;
import database.TemplateSpecs;
import database.Project;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ServerModel;
import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 *
 * @author Administrador
 */
public class CodegenServer extends AbstractHandler {

    @Override
    public void handle(String target,
            Request baseRequest,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
            ServletException {
        // Declare response encoding and types
        response.setContentType("text/html; charset=utf-8");
        // Declare response status code
        response.setStatus(HttpServletResponse.SC_OK);
        //System.out.println(target + " -- "+retornaCookiePorNome(request.getCookies(), "project", "nenhum").getValue());
        try {
            if (target.startsWith("/api/")) {
                supplyApi(target, baseRequest, request, response);
            } else if (target.startsWith("/static/")) {
                OutputStream outStream = response.getOutputStream();
                supplyStaticFile(target, outStream, response);
            } else {
                supplyTemplateFile(target, baseRequest, request, response);
            }
        } catch (Exception e) {
        }
        // Inform jetty that this request has now been handled
        baseRequest.setHandled(true);
    }

    private void supplyStaticFile(String target, OutputStream outStream, HttpServletResponse response) {
        target = target.substring(1);
        File file = new File("web/" + target);
        if (!file.exists()) {

            try {
                outStream.write("Arquivo não encontrado, se você acha que deveria haver algo aqui, contate a Haftware".getBytes());
            } catch (IOException ex) {
            }
            return;
        }
        try {
            if (target.endsWith(".css")) {
                response.setContentType("text/css");
            }
            outStream.write(FileUtils.readFileToByteArray(file));
        } catch (IOException ex) {
        }
    }

    private void supplyTemplateFile(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String projeto = retornaCookiePorNome(request.getCookies(), "project", "nenhum").getValue();
        PrintWriter writer = response.getWriter();
        if (target.equals("/")) {
            target = "/index.html";
        }
        if (projeto.equals("nenhum") && !target.equals("/projects.html") && !target.equals("/newProject.html")
                && !target.startsWith("/js") && !target.startsWith("/templates")) {
            response.sendRedirect("/projects.html");
            return;
        }
        target = target.substring(1);
        ServerTemplatesProcessor serverTemplatesProcessor = new ServerTemplatesProcessor(target);
        if (!serverTemplatesProcessor.pronto()) {
            writer.println("Arquivo não encontrado, se você acha que deveria haver algo aqui, contate a Haftware");
            return;
        }
        serverTemplatesProcessor.put("root", new ServerTemplatesDataSupplier(CodegenDatabaseController.getProjetoViaNome(projeto)));
        writer.println(serverTemplatesProcessor.proccessAndReturn());
    }

    private void supplyApi(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        target = target.replace("/api/", "");

        switch (target) {
            case "turnoff":
                ConsolePrinter.printInfo("Desligando o servidor do Codegen...");
                System.exit(0);
                break;
            case "getGlobalConfig":
                //writer.println(CodegenGlobalConfig.loadConfig().toJson());
                break;
            case "setGlobalConfig":
                //CodegenGlobalConfig.constroiDoJson(leTodasLinhas(request.getReader())).saveConfig();
                break;
            case "novoModel":
                 CodegenDatabaseController.addModel(retornaCookiePorNome(request.getCookies(), "project", "").getValue(), 
                        ServerModel.fromJson(leTodasLinhas(request.getReader())));
                break;
            case "getModel":
                String model = baseRequest.getParameter("model");
                String project = baseRequest.getParameter("project");
                writer.println(ServerModel.fromJson(
                        CodegenDatabaseController.getArquivoModelo(
                                retornaCookiePorNome(request.getCookies(), "project", "nenhum").getValue(), model
                        )).toJson());
                break;
            case "setModel":
                CodegenDatabaseController.gravaArquivoModelo(retornaCookiePorNome(request.getCookies(), "project", "nenhum").getValue(), 
                        ServerModel.fromJson(request.getReader().lines().findFirst().get()));
                break;
            case "getProject":
                String proj = baseRequest.getParameter("project");
                writer.println(CodegenDatabaseController.getProjetoViaNome(proj).toJson());
                break;
            case "novoProjeto":
                 CodegenDatabaseController.adicionaProjeto(Project.fromJson(leTodasLinhas(request.getReader())));
                break;
            case "addTemplateProjeto":
                CodegenDatabaseController.newTemplate(TemplateSpecs.fromJson(leTodasLinhas(request.getReader())));
                break;
            case "editaTemplateProjeto":
                CodegenDatabaseController.openTemplate(TemplateSpecs.fromJson(leTodasLinhas(request.getReader())));
                break;
            case "excluiTemplateProjeto":
                CodegenDatabaseController.excluiTemplate(TemplateSpecs.fromJson(leTodasLinhas(request.getReader())));
                break;
            case "setProjetoAtual":
                Cookie cookieProjeto = new Cookie("project", baseRequest.getParameter("project"));
                cookieProjeto.setMaxAge(-1);
                cookieProjeto.setPath("/");
                response.addCookie(cookieProjeto);
                response.sendRedirect("/index.html");
        }
    }

    public String leTodasLinhas(BufferedReader in) {
        StringBuilder saida = new StringBuilder();
        String line;
        try {
            while ((line = in.readLine()) != null) {
                saida.append(line);
            }
        } catch (Exception e) {
        }
        return saida.toString();
    }

    public static void main(String[] args) throws Exception {
        //System.setErr(new PrintStream(new File("log.txt")));
        System.out.println("HW Codegen\n"
                + "  Gerador de fontes da Haftware\n"
                + "  Todos os direitos reservados à Haftware Sistemas ltda.\n");
        ConsolePrinter.printInfo("Inicializando...");
        ConsolePrinter.printInfo("Inicializando o microservidor do Codegen...");
        CodegenDatabaseController.init();
        //FilesSandBox.init(CodegenGlobalConfig.loadConfig().getGenOutput());
        ServerTemplatesProcessor.init();
        Server server = new Server(8080);
        server.setHandler(new CodegenServer());
        server.start();
        ConsolePrinter.printInfo("Inicializado OK...");
        server.join();
    }

    private static Cookie retornaCookiePorNome(Cookie[] cookies, String nome, String valorDefault) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(nome)) {
                    return cookie;
                }
            }
        }
        return new Cookie(nome, valorDefault);
    }
}
