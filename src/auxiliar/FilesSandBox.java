/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auxiliar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import org.apache.commons.io.FileUtils;

/**
 * Caixa de areia de arquivos, intercepta todos os arquivos gerados pelo codegen
 * e os coloca em outro diretório, impedindo que o codegen sobrescreva
 * alterações manuais do programador
 *
 * @author ivoaf
 */
public class FilesSandBox {

    private Map<String, Writer> arquivosCriados;
    private List<String> diretoriosCriar;
    private String saidaBase;
    
    private final String PASTA_SANDBOX = "sandbox_"+hashCode()+"/";

    private int mudancas;

    public FilesSandBox(String saidaBase) {
        init(saidaBase);
    }
    
    private void init(String saidaBase) {
        this.saidaBase = saidaBase;
        ConsolePrinter.printInfo("Inicializando sandbox de arquivos nº "+hashCode()+"...");
        arquivosCriados = new HashMap<>();
        diretoriosCriar = new ArrayList<>();
    }

    public Writer getFileWriter(String caminho) throws Exception {

        File file = new File(caminho.replaceFirst(saidaBase, PASTA_SANDBOX));
        Writer out = new OutputStreamWriter(new FileOutputStream(file));
        arquivosCriados.put(caminho, out);
        return out;
    }

    public void criaDiretorio(String caminho) {
        ConsolePrinter.printInfo("Colocando diretório no sandbox nº "+hashCode()+": " + caminho);
        diretoriosCriar.add(caminho);
        new File(caminho.replaceFirst(saidaBase, PASTA_SANDBOX)).mkdirs();
    }

    public void commitaArquivos() {
        ConsolePrinter.printInfo("Consolidando sandbox de arquivos nº "+hashCode()+"...\n"
                + "Arquivos modificados serão abertos para comparação");
        mudancas = 0;
        ConsolePrinter.printInfo("Fechando os arquivos do Sandbox nº "+hashCode()+"...");
        arquivosCriados.forEach((c, w) -> {
            try {
                w.close();
            } catch (IOException ex) {
                System.err.println("Erro ao fechar arquivo:\n\t\t"
                        + ex.getLocalizedMessage());
            }
        });
        arquivosCriados.forEach((c, w) -> escreveMensagemCopyrightNoFimDoArquivo(c.replaceFirst(saidaBase, PASTA_SANDBOX)));
        arquivosCriados.forEach((c, w) -> escreveChecksumNoFinalDoArquivo(c.replaceFirst(saidaBase, PASTA_SANDBOX)));
        diretoriosCriar.forEach(d -> new File(d).mkdirs());

        arquivosCriados.forEach((c, w) -> {
            File f = new File(c);
            if (precisaFazerWinmerge(f)) {
                runWinMerge(c.replaceFirst(saidaBase, PASTA_SANDBOX), c);
            } else {
                copiaArquivo(c.replaceFirst(saidaBase, PASTA_SANDBOX), c);
            }
        });
        if (mudancas == 0) {
            ConsolePrinter.printInfo("Nada mudou...");
        }
        try {
            ConsolePrinter.printInfo("Deletando o Sandbox nº "+hashCode()+"...");
            FileUtils.deleteDirectory(new File(PASTA_SANDBOX));
        } catch (IOException ex) {
            ConsolePrinter.printError("Erro ao deletar o Sandbox nº "+hashCode()+":\n\t\t"
                    + ex.getLocalizedMessage());
        }
    }

    private boolean precisaFazerWinmerge(File arquivo) {
        try {
            if (arquivo.exists() && !arquivo.isDirectory()) {

                String texto = new String(Files.readAllBytes(arquivo.toPath()));

                String comentarioChecksum = getComentarioChecksum(0, Utils.pegaExtensaoArquivo(
                        arquivo.toString()
                ));

                int inicioChecksum = comentarioChecksum.indexOf("checksum =");

                int ateOndeFazerChecksum = texto.lastIndexOf("checksum =") - inicioChecksum;

                String conteudoComentario = Utils.extraiConteudoComentario(Utils.pegaExtensaoArquivo(arquivo.toString()), texto.substring(ateOndeFazerChecksum));
                String checksumLidoArquivo = conteudoComentario
                        .substring(conteudoComentario.lastIndexOf("checksum =") + "checksum =".length()).trim();
                String checksumCalculadoParaArquivo = String.valueOf(getChecksum(texto, ateOndeFazerChecksum));

                //System.out.println(arquivo+
                //                 "\n     Lido: "+checksumLidoArquivo+
                //                 "\nCalculado: "+checksumCalculadoParaArquivo);
                return !checksumLidoArquivo.equals(checksumCalculadoParaArquivo);
            } else {
                return false;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return true;
        }
    }

    private String getComentarioChecksum(long checksum, String extensao) {
        return Utils.formataComentario(extensao,
                "Não apagar nem modificar esse comentário\n"
                + "Gerenciado pelo Sandbox do Codegen\n"
                + "Passar essa linha pelo Winmerge também >>>\n"
                + " checksum = "
                + String.valueOf(checksum));
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

    private void escreveChecksumNoFinalDoArquivo(String arquivo) {
        try {
            String texto = new String(Files.readAllBytes(Paths.get(arquivo)));

            long checksumValue = getChecksum(texto, texto.length());

            String comentarioChecksum = getComentarioChecksum(checksumValue, Utils.pegaExtensaoArquivo(arquivo));

            Files.write(Paths.get(arquivo), comentarioChecksum.getBytes(), StandardOpenOption.APPEND);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void escreveMensagemCopyrightNoFimDoArquivo(String arquivo) {
        String data;

        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        data = Utils.formataComentario(Utils.pegaExtensaoArquivo(arquivo),
                "    Esse arquivo foi gerado pelo Haftware Codegen\n"
                + "    Todos os direitos reservados à Haftware SI\n");
        try {
            Files.write(Paths.get(arquivo), data.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }

    // Normaliza as quebras de linha e retorna a checksum
    private long getChecksum(String texto, int posicaoFinal) {

        if (texto.length() != posicaoFinal) {
            texto = texto.substring(0, posicaoFinal);
        }

        texto = Utils.formalizaQuebrasDeLinha(texto);
        byte[] bytesTexto = texto.getBytes();

        Checksum checksum = new CRC32();
        checksum.update(bytesTexto, 0, texto.length());

        return checksum.getValue();
    }
}
