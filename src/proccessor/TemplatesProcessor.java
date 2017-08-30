/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessor;

import auxiliar.ConsolePrinter;
import auxiliar.FilesSandBox;
import database.CodegenDatabaseController;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author ivoaf
 */
public class TemplatesProcessor {

    private Configuration cfg;
    private String projeto;
    private FilesSandBox fsb;
    private ProccessLog log = new ProccessLog();
    private Template tmp;
    private Map root;
    private boolean iniciado = false;

    private String templateName;
    private String originalTemplateName;
    private boolean pronto = false;
    private boolean isSnippet = false;
    private String palavraIdentificadora;
    private String palavraIdentificadoraComLink;
    
    
    public TemplatesProcessor(String projeto, String templateName){
        this(projeto,templateName, null);
    }
    
    public TemplatesProcessor(String projeto, String templateName, FilesSandBox fsb) {
        log.startNewTemplate(templateName);
        this.projeto = projeto;
        this.fsb = fsb;
        this.palavraIdentificadora = "template";
        this.palavraIdentificadoraComLink = "template";
        this.originalTemplateName = templateName;
        this.templateName = projeto+"/" + templateName + "proc";
        this.root = new HashMap();
    }

    
    
    public final boolean init() {
        if(iniciado) return true;
        try {
            new File("temp/").mkdir();
            //freemarker.log.Logger.selectLoggerLibrary(freemarker.log.Logger.LIBRARY_NONE);
        } catch (Exception ex) {
        }
        cfg = new Configuration();
        try {
            cfg.setDirectoryForTemplateLoading(new File("temp/"));
        } catch (Exception ex) {
            ex.printStackTrace();
            if (ex.getClass() == FileNotFoundException.class) {
                String mensagem = "Não foi encontrada a pasta de templates.";
                ConsolePrinter.printError(mensagem);
                log.putMessage(mensagem);
                return false;
            }
            Logger.getLogger(TemplatesProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        pronto = true;
        
        try {
            String conteudo = FileUtils.readFileToString(new File(CodegenDatabaseController.getCaminhoTemplates(projeto) + originalTemplateName), "UTF-8");
            //conteudo = conteudo.replace("#{", "${r\"#{\"}");
            FileUtils.write(new File("temp/"+this.templateName), conteudo, "UTF-8", false);
        } catch (Exception ex) {
            String mensagem = "Ocorreu um erro ao tentar ler o "+this.palavraIdentificadora+", erro:\n"
                    + ex.getLocalizedMessage().replace(" in " + this.templateName, "");
            log.putMessage(mensagem);
            ConsolePrinter.printError(mensagem);
            pronto = false;
            return false;
        }
        try {
            tmp = cfg.getTemplate(this.templateName);
        } catch (Exception ex) {
            String mensagem = "Ocorreu um erro ao tentar parsear o "+this.palavraIdentificadoraComLink+", erro:\n"
                    + ex.getLocalizedMessage().replace(" in " + this.templateName, "");
            log.putMessage(mensagem);
            ConsolePrinter.printError(mensagem);
            pronto = false;
        }
        iniciado = true;
        return true;
    }
    
    public static void encerra(){
        try {
            ConsolePrinter.printInfo("Deletando pasta dos templates temporários...");
            FileUtils.deleteDirectory(new File("temp/"));
        } catch (Exception ex) {
        }
    }

    
    public boolean pronto() {
        return pronto;
    }

    
    public void put(String name, Object object) {
        if (root == null) {
            String mensagem = "Verifique a sintaxe dos templates, provavelmente algo está errado\n"
                    + "Erro do Codegen:\n"
                    + "O TemplateProcessor tentou executar um put sendo que o root não está devidamente instanciado";
            ConsolePrinter.printError(mensagem);
            log.putMessage(mensagem);
            pronto = false;
            return;
        }
        root.put(name, object);
    }

    public String proccessAndReturn() {
        init();
        if(!pronto) return "";
        try {
            StringWriter output = new StringWriter();
            tmp.process(root, output);
            FileUtils.deleteQuietly(new File(this.templateName));
            return output.toString();
        } catch (Exception ex) {
            String mensagem = "Ocorreu um erro ao tentar processar o "+this.palavraIdentificadoraComLink+", erro:\n"
                    + ex.getLocalizedMessage().replace(" in " + this.templateName, "");
            ConsolePrinter.printError(mensagem);
            log.putMessage(mensagem);
            FileUtils.deleteQuietly(new File(this.templateName));
            return null;
            
        }
    }

    public void proccessToFile(String caminho) {
        init();
        ConsolePrinter.printInfo("Processando " + caminho.substring(caminho.lastIndexOf("/") + 1) + "...");
        if(!pronto) {
            return;
        }
        fsb.criaDiretorio(caminho.substring(0,caminho.lastIndexOf("/")+1));
        try {
            Writer out = fsb.getFileWriter(caminho);
            tmp.process(root, out);
        } catch (Exception ex) {
            String mensagem = "Ocorreu um erro ao tentar processar o "+this.palavraIdentificadoraComLink+", erro:\n"
                    + ex.getLocalizedMessage().replace(" in " + this.templateName, "");
            ConsolePrinter.printError(mensagem);
            log.putMessage(mensagem);
        }
        FileUtils.deleteQuietly(new File(this.templateName));
    }

    void setLogger(ProccessLog log) {
        this.log = log;
    }

    public void setIsSnippet(boolean isSnippet) {
        this.isSnippet = isSnippet;
        if(isSnippet){
            this.palavraIdentificadora = "snippet "+
                    this.originalTemplateName.substring(0, this.originalTemplateName.lastIndexOf(".")).replace("microSnippets/", "")+"";
            
            this.palavraIdentificadoraComLink = "snippet <a title=\"Editar esse snippet com o editor externo configurado no sistema operacional\" "
                    + "class=\"btn btn-warning btn-xs\" onclick=\"editaSnippetExterno('"+
                    this.originalTemplateName.substring(0, this.originalTemplateName.lastIndexOf(".")).replace("microSnippets/", "")+
                    "','"+this.projeto+"')\">"+this.originalTemplateName.replace("microSnippets/", "")+"</a>";
        }
    }
}
