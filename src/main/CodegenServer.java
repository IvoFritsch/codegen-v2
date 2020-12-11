/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import auxiliar.ConsolePrinter;
import auxiliar.FileChooser;
import auxiliar.ServerTemplatesDataSupplier;
import auxiliar.ServerTemplatesProcessor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import database.CodegenDatabaseController;
import database.Project;
import database.ProjectSpecs;
import database.TemplateSpecs;
import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;
import model.ServerModel;
import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.JSONObject;
import proccessor.ProccessSpecs;
import proccessor.ProccessorCore;
import proccessor.TemplatesProcessor;

/**
 *
 * @author Administrador
 */
public class CodegenServer extends AbstractHandler {

    public static int PORTA = 9080;
    private static TrayIcon trayIcon;
    
    static {
        org.eclipse.jetty.util.log.Log.setLog(new NoLogging());
    }
    
    @Override
    public void handle(String target,
            Request baseRequest,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
            ServletException {
        // Declare response encoding and types
        response.setContentType("text/html; charset=utf-8");
        
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3001");
        response.setHeader("Access-Control-Allow-Methods", "GET");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        
        // Declare response status code
        response.setStatus(HttpServletResponse.SC_OK);
        
        Cookie projAtual = retornaCookiePorNome(request.getCookies(), "project");
        // Caso o projeto atual não existe
        if(CodegenDatabaseController.getProjetoViaNome(projAtual.getValue())
                .isPseudo()){
            projAtual.setValue("");
            Cookie cookieProjeto = new Cookie("project", "");
            cookieProjeto.setValue("");            
            cookieProjeto.setMaxAge(0);
            cookieProjeto.setPath("/");
            response.addCookie(cookieProjeto);
        }
        
        try {
            if (target.startsWith("/api/")) {
                supplyApi(target, baseRequest, request, response);
            } else if (target.startsWith("/static/")) {
                OutputStream outStream = response.getOutputStream();
                supplyStaticFile(target, outStream, response);
            } else {
                supplyTemplateFile(target, baseRequest, request, response);
            }
        } catch(VersaoInvalidaException e) {
          newTrayNotification("Erro de versão, atualize o Codegen", e.getMessage(), TrayIcon.MessageType.ERROR);
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
        String projeto = retornaCookiePorNome(request.getCookies(), "project").getValue();
        
//        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
//        response.setHeader("Access-Control-Allow-Methods", "GET");
        
        PrintWriter writer = response.getWriter();
        if (target.equals("/")) {
            target = "/index.html";
        }
        if (projeto.equals("") && !target.equals("/projects.html") && !target.equals("/newProject.html")
                && !target.equals("/importProject.html") && !target.startsWith("/js") && !target.startsWith("/templates")
                && !target.startsWith("/contact.html") && !target.startsWith("/about.html") && !target.startsWith("/help.html")) {
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
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        switch (target) {
            case "getProjects":
                List<Project> listaProjetos = CodegenDatabaseController.getListaProjetos();
                
                writer.println(gson.toJson(listaProjetos));
                break;
            case "getModels":
                List<String> listaModelosProjeto = CodegenDatabaseController.getListaModelosProjeto(baseRequest.getParameter("project"));
                
                writer.println(new Gson().toJson(listaModelosProjeto));
                break;
            case "turnoff":
                ConsolePrinter.printInfo("Desligando o servidor do Codegen...");
                TemplatesProcessor.encerra();
                System.exit(0);
                break;
            case "getGlobalConfig":
                //writer.println(CodegenGlobalConfig.loadConfig().toJson());
                break;
            case "setGlobalConfig":
                //CodegenGlobalConfig.constroiDoJson(leTodasLinhas(request.getReader())).saveConfig();
                break;
            case "novoModel":
                CodegenDatabaseController.addModel(retornaCookiePorNome(request.getCookies(), "project").getValue(),
                        ServerModel.fromJson(leTodasLinhas(request.getReader())));
                break;
            case "deleteModel":
                CodegenDatabaseController.deleteModel(retornaCookiePorNome(request.getCookies(), "project").getValue(),
                                                      baseRequest.getParameter("model"));
            case "getModel":
                String model = baseRequest.getParameter("model");
                writer.println(ServerModel.fromJson(
                        CodegenDatabaseController.getArquivoModelo(
                                retornaCookiePorNome(request.getCookies(), "project").getValue(), model
                        ).json, model).toJson(true));
                break;
            case "setModel":
                String linha = request.getReader().lines().findFirst().get();
                CodegenDatabaseController.gravaArquivoModelo(retornaCookiePorNome(request.getCookies(), "project").getValue(),
                        ServerModel.fromJson(linha));
                break;
            case "getProject":
                String proj = baseRequest.getParameter("project");
                writer.println(gson.toJson(CodegenDatabaseController.getProjetoViaNome(proj)));
                break;
            case "chooseProjectFile":
                String fileEscolha = new FileChooser().getFile("Arquivo de projeto do Codegen (.cgp)",false,"cgp");
                writer.println(fileEscolha);
                break;
            case "renameProject":
                String newName = new JSONObject(leTodasLinhas(request.getReader())).getString("newName");
                CodegenDatabaseController.renameProject(retornaCookiePorNome(request.getCookies(), "project").getValue(), newName);
                Cookie cookieProjeto = new Cookie("project", newName);
                cookieProjeto.setMaxAge(-1);
                cookieProjeto.setPath("/");
                response.addCookie(cookieProjeto);
                break;
            case "mudaCaminhoSaidaGeracaoProjeto":
                String novoCaminho = new JSONObject(leTodasLinhas(request.getReader())).getString("novoCaminho");
                CodegenDatabaseController.mudaCaminhoSaidaGeracaoProjeto(retornaCookiePorNome(request.getCookies(), "project").getValue(), novoCaminho);
                break;
            case "chooseProjectFileJson":
                String fileEscolhaJson = new FileChooser().getFile("Codegen project file (.cgp)",false,"cgp");
                writer.println(new JSONObject().put("path",fileEscolhaJson).toString());
                break;
            case "chooseNewProjectFolderJson":
                String dirEscolhaJson = new FileChooser().getFile("New project path",true,"cgp");
                writer.println(new JSONObject().put("path", dirEscolhaJson).toString());
                break;
            case "chooseNewProjectFolder":
                String dirEscolha = new FileChooser().getFile("Diretorio onde criar o novo projeto",true,"cgp");
                writer.println(dirEscolha);
                break;
            case "importProject":
                CodegenDatabaseController.importaProjetoExistente(ProjectSpecs.fromJson(leTodasLinhas(request.getReader())).
                        getCaminho());
                break;
            case "createProject":
                CodegenDatabaseController.criaNovoProjeto(ProjectSpecs.fromJson(leTodasLinhas(request.getReader())));
                break;
            case "addTemplateProjeto":
                CodegenDatabaseController.newTemplate(TemplateSpecs.fromJson(leTodasLinhas(request.getReader())));
                break;
            case "addSnippetProjeto":
                CodegenDatabaseController.newSnippet(TemplateSpecs.fromJson(leTodasLinhas(request.getReader())));
                break;
            case "editaTemplateProjeto":
                CodegenDatabaseController.openTemplate(TemplateSpecs.fromJson(leTodasLinhas(request.getReader())));
                break;
            case "excluiTemplateProjeto":
                CodegenDatabaseController.excluiTemplate(TemplateSpecs.fromJson(leTodasLinhas(request.getReader())));
                break;
            case "unvincProject":
                String projToUnvinc = baseRequest.getParameter("project");
                CodegenDatabaseController.desvinculaProjeto(projToUnvinc);
                break;
            case "editaSnippetProjeto":
                CodegenDatabaseController.openSnippet(TemplateSpecs.fromJson(leTodasLinhas(request.getReader())));
                break;
            case "excluiSnippetProjeto":
                CodegenDatabaseController.excluiSnippet(TemplateSpecs.fromJson(leTodasLinhas(request.getReader())));
                break;
            case "setProjetoAtual":
                cookieProjeto = new Cookie("project", baseRequest.getParameter("project"));
                cookieProjeto.setMaxAge(-1);
                cookieProjeto.setPath("/");
                response.addCookie(cookieProjeto);
                response.sendRedirect("/index.html");
                writer.println("{}");
                break;
            case "processaTemplate":
                ProccessorCore proccessorCore = new ProccessorCore(ProccessSpecs.fromJson(leTodasLinhas(request.getReader())));
                String log = null;
                try {
                    log = proccessorCore.process().toJson();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                writer.println(log);
                break;
            case "cancelaProcessamento":
                ProccessorCore.cancel();
                break;
            default:
                ConsolePrinter.printError("Chamou a api usando um metodo não reconhecido: " + target);
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
        //System.setOut(new PrintStream(new File("log.txt")));
        //System.setErr(new PrintStream(new File("log.txt")));
        org.eclipse.jetty.util.log.Log.setLog(new NoLogging());
        System.out.println("HW Codegen\n"
                + "  Gerador de fontes da Haftware\n"
                + "  Todos os direitos reservados à Haftware Sistemas ltda.\n");
        ConsolePrinter.printInfo("Inicializando...");
        ConsolePrinter.printInfo("Inicializando o microservidor do Codegen...");
        CliStarter.readConfFile();
        Server server = new Server(PORTA);
        try{
            CodegenDatabaseController.init();
            //FilesSandBox.init(CodegenGlobalConfig.loadConfig().getGenOutput());
            ServerTemplatesProcessor.init();
            criaIconeNaTray();
            server.setHandler(new CodegenServer());
            server.start();
            ConsolePrinter.printInfo("Inicializado OK:\n    Porta " + PORTA);
            server.join();
        } catch (Exception e){
            ConsolePrinter.printError("Não foi possível inicializar o servidor do Codegen\n"
                    + "    Verifique se a porta " + PORTA + " não está ocupada por outro processo.");
            System.exit(0);
        }
    }

    public static void newTrayNotification(String title, String message, TrayIcon.MessageType type){
        if(trayIcon == null) return;
        trayIcon.displayMessage(title, message, type);
    }
    
    private static void criaIconeNaTray(){
        if(!SystemTray.isSupported()) return;
        final PopupMenu popup = new PopupMenu();
        trayIcon =
                new TrayIcon(Toolkit.getDefaultToolkit().getImage("web/haftware-logo.png"));
        final SystemTray tray = SystemTray.getSystemTray();
       
        // Create a pop-up menu components
        MenuItem openItem = new MenuItem("Acessar pagina inicial");
        openItem.addActionListener((ev) -> {
            try{
            Desktop.getDesktop().browse(new URI("http://localhost:"+PORTA+"/index.html"));
            } catch (Exception e){}
        });
        MenuItem aboutItem = new MenuItem("Sobre");
        aboutItem.addActionListener((ev) -> {
            try{
            Desktop.getDesktop().browse(new URI("http://localhost:"+PORTA+"/about.html"));
            } catch (Exception e){}
        });
        MenuItem exitItem = new MenuItem("Desligar servidor");
        exitItem.addActionListener((ev) -> {
            ConsolePrinter.printInfo("Desligando o servidor do Codegen...");
            TemplatesProcessor.encerra();
            System.exit(0);
        });
       
        //Add components to pop-up menu
        popup.add(openItem);
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(exitItem);
       
        trayIcon.setPopupMenu(popup);
        trayIcon.setToolTip("Servidor do Haftware Codegen está em execução");
       
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            ConsolePrinter.printError("Falhou ao adicionar icone na tray.");
        }
        
    }
    
    private static Cookie retornaCookiePorNome(Cookie[] cookies, String nome) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(nome)) {
                    return cookie;
                }
            }
        }
        return new Cookie(nome, "");
    }
}
