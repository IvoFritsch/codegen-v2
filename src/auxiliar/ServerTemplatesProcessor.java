/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auxiliar;

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
public class ServerTemplatesProcessor {

    private static Configuration cfg;

    public static boolean init() {
        try {
            freemarker.log.Logger.selectLoggerLibrary(freemarker.log.Logger.LIBRARY_NONE);
        } catch (ClassNotFoundException ex) {
        }
        cfg = new Configuration();
        try {
            cfg.setDirectoryForTemplateLoading(new File("web"));
        } catch (Exception ex) {
            if (ex.getClass() == FileNotFoundException.class) {
                ConsolePrinter.printError("Não foi encontrada a pasta de web.");
                return false;
            }
            Logger.getLogger(ServerTemplatesProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        return true;
    }

    private Template tmp;
    private Map root;

    private String templateName;
    private boolean pronto = true;

    public ServerTemplatesProcessor(String templateName) {
        this.templateName = templateName + "proc";
        try {
            String conteudo = FileUtils.readFileToString(new File("web/" + templateName), "UTF-8");
            conteudo = conteudo.replace("#{", "${r\"#{\"}");
            FileUtils.write(new File("web/" + this.templateName), conteudo, "UTF-8", false);
        } catch (Exception e) {
            pronto = false;
        }

        try {
            tmp = cfg.getTemplate(this.templateName);
            root = new HashMap();
        } catch (Exception ex) {
            //ConsolePrinter.printError("Ocorreu um erro ao tentar ler o arquivo web '" + this.templateName + "', erro:\n"
            //        + ex.getLocalizedMessage().replace(" in " + this.templateName, ""));
            pronto = false;
        }
    }

    public boolean pronto() {
        return pronto;
    }

    
    public void put(String name, Object object) {
        if (root == null) {
            ConsolePrinter.printError("Ocorreu um erro fatal, a geracão deste model será encerrada\n"
                    + "Verifique a sintaxe dos templates, provavelmente algo está errado\n"
                    + "Erro do Codegen:\n"
                    + "O ServerTemplatesProcessor tentou executar um put sendo que o root não está devidamente instanciado");
            return;
        }
        root.put(name, object);
    }

    public String proccessAndReturn() {
        try {
            StringWriter output = new StringWriter();
            tmp.process(root, output);
            FileUtils.deleteQuietly(new File("web/" + this.templateName));
            return output.toString();
        } catch (Exception ex) {
            ConsolePrinter.printError("Ocorreu um erro ao tentar processar o arquivo web '" + this.templateName + "', erro:\n"
                    + ex.getLocalizedMessage().replace(" in " + this.templateName, ""));
            FileUtils.deleteQuietly(new File("web/" + this.templateName));
            return null;
        }
    }

}
