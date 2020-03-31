/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auxiliar;

import database.CodegenDatabaseController;
import database.Project;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import org.apache.commons.io.FileUtils;
import proccessor.ProccessorCore;

/**
 * Caixa de areia de arquivos, intercepta todos os arquivos gerados pelo codegen
 * e os coloca em outro diretório, impedindo que o codegen sobrescreva
 * alterações manuais do programador
 *
 * @author ivoaf
 */
public class FilesSandBox {

    private Map<String, ProvidedWriter> arquivosCriados;
    class ProvidedWriter{
        String universalFileName;
        Writer writer;
        boolean closed = false;

        public ProvidedWriter(String universalFileName, Writer writer) {
            this.universalFileName = universalFileName;
            this.writer = writer;
        }
        
        public void close() throws IOException{
            if(closed) return;
            writer.close();
            closed = true;
        }
    }
    
    private List<String> diretoriosCriar;
    private String saidaBase;
    private boolean autoOverwrite;
    private Project projeto;
    private Map<String, String> newChecksums = new HashMap<>();
    
    private final String PASTA_SANDBOX = "sandbox_"+hashCode()+"/";

    private int mudancas;

    public FilesSandBox(String saidaBase, boolean autoOverwrite, String projeto) {
        init(saidaBase);
        this.autoOverwrite = autoOverwrite;
        this.projeto = CodegenDatabaseController.getProjetoViaNome(projeto);
    }
    
    private void init(String saidaBase) {
        this.saidaBase = saidaBase;
        ConsolePrinter.printInfo("Inicializando sandbox de arquivos nº "+hashCode()+"...");
        arquivosCriados = new HashMap<>();
        diretoriosCriar = new ArrayList<>();
    }

    public Writer getFileWriter(String caminho, String universalFileName) throws Exception {
        File file = new File(caminho.replaceFirst(saidaBase, PASTA_SANDBOX));
        Writer out = new OutputStreamWriter(new FileOutputStream(file));
        arquivosCriados.put(caminho, new ProvidedWriter(universalFileName, out));
        return out;
    }

    public void criaDiretorio(String caminho) {
        ConsolePrinter.printInfo("Colocando diretório no sandbox nº "+hashCode()+": " + caminho);
        diretoriosCriar.add(caminho);
        new File(caminho.replaceFirst(saidaBase, PASTA_SANDBOX)).mkdirs();
    }

    public void commitaArquivos() {
        ConsolePrinter.printInfo("Consolidando sandbox de arquivos nº "+hashCode()+"...\n"
                + "Arquivos modificados serão abertos para comparação...");
        mudancas = 0;
        ConsolePrinter.printInfo("Fechando os arquivos do Sandbox nº "+hashCode()+"...");
        fechaArquivos();
        diretoriosCriar.forEach(d -> new File(d).mkdirs());
        arquivosCriados.forEach((c, w) -> {
            colocaChecksumArquivoNaFila(c, w.universalFileName);
        });
        arquivosCriados.forEach((c, w) -> {
            if(ProccessorCore.mustCancel()) return;
            File f = new File(c);
            if (!autoOverwrite && precisaFazerWinmerge(f, w.universalFileName)) {
                runWinMerge(c.replaceFirst(saidaBase, PASTA_SANDBOX), c);
            } else {
                copiaArquivo(c.replaceFirst(saidaBase, PASTA_SANDBOX), c);
            }
        });
        if (mudancas == 0) {
            ConsolePrinter.printInfo("Nada mudou...");
        }
        newChecksums.forEach((a, cs) -> projeto.setFileChecksum(a, cs));
        projeto.save();
    }
    
    public void deleteSandbox(){
        try {
            ConsolePrinter.printInfo("Deletando o Sandbox nº "+hashCode()+"...");
            fechaArquivos();
            FileUtils.deleteDirectory(new File(PASTA_SANDBOX));
        } catch (Exception ex) {
            ConsolePrinter.printError("Erro ao deletar o Sandbox nº "+hashCode()+":\n\t\t"
                    + ex.getLocalizedMessage());
        }
    }
    
    private void fechaArquivos(){
        arquivosCriados.forEach((c, w) -> {
            try {
                w.close();
            } catch (IOException ex) {
                System.err.println("Erro ao fechar arquivo:\n\t\t"
                        + ex.getLocalizedMessage());
            }
        });
    }

    private boolean precisaFazerWinmerge(File arquivo, String nome) {
        try {
            if (arquivo.exists() && !arquivo.isDirectory()) {
                
                String texto = new String(Files.readAllBytes(arquivo.toPath()));

                String checksumLidoArquivo = projeto.getFileChecksum(nome);
                String checksumCalculadoParaArquivo = getChecksum(texto);

//                System.out.println(arquivo+
//                                 "\n     Lido: "+checksumLidoArquivo+
//                                 "\nCalculado: "+checksumCalculadoParaArquivo);
                return !checksumLidoArquivo.equals(checksumCalculadoParaArquivo);
            } else {
                return false;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return true;
        }
    }

    private void copiaArquivo(String origem, String destino) {
        mudancas++;
        File ori = new File(origem);
        File dest = new File(destino);
        try {
            Files.copy(ori.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {

        }
    }

    private void runWinMerge(String origem, String destino) {
        File ori = new File(origem);
        File dest = new File(destino);
        try {
            // Se os arquivos são iguais, nem abre o WinMerge
            if (FileUtils.contentEquals(ori, dest)) {
                return;
            }
            mudancas++;
            Process process = new ProcessBuilder(
                    "WinMerge/WinMerge.exe", "/e", "/xq", "/u", "/maximize",
                    "/dl", Utils.pegaNomeArquivo(origem) + "." + Utils.pegaExtensaoArquivo(origem) + " gerado pelo Codegen",
                    "/dr", Utils.pegaNomeArquivo(destino) + "." + Utils.pegaExtensaoArquivo(destino) + " atual do projeto",
                    origem, destino).start();
            process.waitFor();
        } catch (Exception ex) {
            ConsolePrinter.printError("Ocorreu um erro ao abrir o Winmerge para os arquivos:\n"
                    + origem + "\n"
                    + destino);
        }
    }
    
    private void colocaChecksumArquivoNaFila(String arquivo, String universalFileName) {
        try {
            String texto = new String(Files.readAllBytes(Paths.get(arquivo.replaceFirst(saidaBase, PASTA_SANDBOX))));

            String checksumValue = getChecksum(texto);
            newChecksums.put(universalFileName, checksumValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Normaliza as quebras de linha e retorna a checksum
    private String getChecksum(String texto) {

        texto = Utils.formalizaQuebrasDeLinha(texto);
        byte[] bytesTexto = texto.getBytes();

        Checksum checksum = new CRC32();
        checksum.update(bytesTexto, 0, texto.length());

        return String.valueOf(checksum.getValue());
    }
}
