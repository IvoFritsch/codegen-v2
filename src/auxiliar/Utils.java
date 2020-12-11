/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auxiliar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author ivoaf
 */
public class Utils {
    
    // If obj == null, then return the default value, otherwise, return the same object
    public static Object defaultIfNull(Object obj, Object defaultValue){
        if(obj == null)
            return defaultValue;
        return obj;
    }
    
    // Retorna a string com o primeiro caractere convertido para maiúscula
    public static String priCharMai(String s){
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

// Retorna a string com o primeiro caractere convertido para maiúscula
    public static String priCharMin(String s){
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }
    
    public static String formalizaCaminho(String s){
        s = s.replaceAll("\\\\", "/");
        s = s.replaceAll("//", "/");
        s = s.trim();
        if (s.startsWith("/")){
            s = s.replaceFirst("/", "");
        }
        return s;
    }
    
    public static String pegaNomeArquivo(String s){
        return s.substring(
                s.lastIndexOf("/") + 1,
                s.lastIndexOf("."));
    }
    
    public static String pegaLetraDrive(String s){
        return s.substring(0, s.indexOf(":")+1);
    }
    
    public static String pegaExtensaoArquivo(String s){
        return s.substring(s.lastIndexOf(".") + 1);
    }
    public static String pegaPastaPaiArquivo(String s){
        if(s.endsWith("/")) s = s.substring(0, s.length() - 1);
        return s.substring(0, s.lastIndexOf("/") + 1);
    }
    
    public static void writeFile(File f, String content){
        try {
            FileUtils.write(f, content.replace("\r\n", "\n").replace("\r", "\n").replace("\n", "\r\n"), "UTF-8");
        } catch (IOException ex) {
        }
    }

    public static String formalizaQuebrasDeLinha(String texto){
        return texto.replace("\r\n", "\n").replace("\r", "\n");
    }
    
    public static String getInicioComentario(String linguagem) {
        switch (Utils.pegaExtensaoArquivo(linguagem)) {
            case "java":
            case "js":
            case "c":
            case "cpp":
            case "h":
            case "sql":
                return "\n/*\n";
            case "xhtml":
            case "html":
            case "snip":
            case "xml":
                return "\n<!--\n";
            case "jsp":
                return "\n<%--\n";
            default:
                return "\n/*\n";
        }
    }
    public static String getFinalComentario(String linguagem) {
        switch (Utils.pegaExtensaoArquivo(linguagem)) {
            case "java":
            case "js":
            case "c":
            case "cpp":
            case "h":
            case "sql":
                return "\n*/\n";
            case "xhtml":
            case "html":
            case "snip":
            case "xml":
                return "\n-->\n";
            case "jsp":
                return "\n--%>\n";
            default:
                return "\n*/\n";
        }
    }
    
    public static String formataComentario(String linguagem, String conteudo){
        return getInicioComentario(linguagem) +
                conteudo.replace("\n", "\n\t")+
               getFinalComentario(linguagem);
    }
    
    public static String extraiConteudoComentario(String linguagem, String comentario){
        return comentario.replace(getInicioComentario(linguagem).trim(), "")
                    .replace(getFinalComentario(linguagem).trim(), "");
    }
    
    public static String toJson(Object obj){
        return new Gson().toJson(obj);
    }
    
    public static String toJsonOnlyExpose(Object obj){
        return new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create().toJson(obj);
    }
}
