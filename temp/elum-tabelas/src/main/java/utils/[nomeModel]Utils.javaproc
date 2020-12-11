
package utils;

import java.util.Date;
import restFramework.JsonManager;
import com.google.gson.JsonSyntaxException;
import restFramework.Validador;
import restFramework.response.CodigoErroCampo;
import model.${root.model.nome};
import restFramework.response.HwResponse;

/**
 * Utilidades para trabalhar com utilidades na classe ${root.model.nome}.
 * Essa classe e gerada inteiramente pelo Codegen,
 * por isso o codigo presente nela é repetitivo e aparentemente mal estruturado,
 * nao ha a necessidade de altera-la
 *
 * @author Haftware S.I.
 */
@SuppressWarnings("unused")
public class ${root.model.nome}Utils {

    /**
     * Constroi uma nova instância de ${root.model.nome} a partir do JSON.
     * <p>
     * So sao considerados os campos que pertencem aos grupos passados em gruposCampos
     *
     * @param json O JSON a interpretar
     * @param gruposCampos Lista de grupos de campos a considerar(vazio - TODOS CAMPOS)
     * @return Nova instancia de ${root.model.nome}, null caso haja um erro no Json
     */
    public static ${root.model.nome} constroi${root.model.nome}DoJson(String json, ${root.model.nome}.Grupos... gruposCampos) {
        ${root.model.nome} novo;
        try{
            novo = JsonManager.getGson().fromJson(json, ${root.model.nome}.class);
        } catch (JsonSyntaxException e){
            return null;
        }
        limpaCamposNaoPertencemAUmDosGrupos(novo,gruposCampos);
        return novo;
    }

    /**
     * Indica se o campo do ${root.model.nome} pertence à um dos grupos passados.
     *
     * @param campo Nome do campo à verificar
     * @param gruposCampos Lista de grupos à verificar
     * @return true se o campo pertence à um dos grupos, false caso contrário
     */
    public static boolean campoPertenceAUmDosGrupos(String campo, ${root.model.nome}.Grupos... gruposCampos) {
        if(gruposCampos == null || gruposCampos.length == 0) return true;
        for (${root.model.nome}.Grupos grupo : gruposCampos) {
            if(campoPertenceAoGrupo(campo, grupo)) return true;
        }
        return false;
    }
	
    /**
     * Carrega os campos que pertencem aos grupos passados com seu valores iniciais, definidos no Codegen com a configuração "valorInicial".
     *
     * @param ${root.model.nomePriCharMin} Classe à carregar os valores iniciais
     * @param gruposCampos Lista de grupos à inicializar
     */
    public static void poeValoresIniciaisNosGrupos(${root.model.nome} ${root.model.nomePriCharMin}, ${root.model.nome}.Grupos... gruposCampos) {
        if(gruposCampos == null) return;
<#list root.model.getListaCamposComAConfig("valorInicial") as campo>
        if(campoPertenceAUmDosGrupos("${campo.nome}", gruposCampos)) ${root.model.nomePriCharMin}.set${campo.nomePriCharMai}(${campo.getValorConfig("valorInicial")});
</#list>
    }

    /**
     * Mescla um ${root.model.nome} de origem com o destino
     * <p>
     * Caso tanto a origem quanto o destino tenham valores diferentes em um campo, é considerado o valor da origem
     * <p>
     * A origem não é alterada, o destino recebe os valores mesclados
     *
     * @param origem ${root.model.nome} origem
     * @param destino ${root.model.nome} destino
     */
    public static void mesclaOrigemComDestino(${root.model.nome} origem, ${root.model.nome} destino){
<#list root.model.listaTodosCampos as campo>
<#if campo.tipo == 'BOOLEANO' && !campo.temConfig("defaultIfNull")>
        if(origem.get${campo.nomePriCharMai}LiteralBoolean() != null) destino.set${campo.nomePriCharMai}(origem.get${campo.nomePriCharMai}LiteralBoolean());
<#else>
        if(origem.get${campo.nomePriCharMai}() != null) destino.set${campo.nomePriCharMai}(origem.get${campo.nomePriCharMai}());
</#if>
</#list>
    }

    /**
     * Limpa todos os campos que não pertençam à um dos grupos passados
     *
     * @param ${root.model.nomePriCharMin} Classe a limpar
     * @param gruposCampos Lista de grupos à manter
     */
    public static void limpaCamposNaoPertencemAUmDosGrupos(${root.model.nome} ${root.model.nomePriCharMin}, ${root.model.nome}.Grupos... gruposCampos) {
        if(gruposCampos == null || gruposCampos.length == 0 || ${root.model.nomePriCharMin} == null) return;
        <#list root.model.listaTodosCampos as campo>
        if(!campoPertenceAUmDosGrupos("${campo.nome}",gruposCampos)) ${root.model.nomePriCharMin}.set${campo.nomePriCharMai}(null);
        </#list>
    }

    /**
     * Valida a classe ${root.model.nome}
     *
     * @param ${root.model.nomePriCharMin} Classe a validar
     * @param response HwResponse para colocar os erros
     * @param gruposCampos Lista de grupos de campos a considerar(vazio - TODOS CAMPOS)
     * @return true - valido, false - invalido
     */
    public static boolean valida${root.model.nome}(${root.model.nome} ${root.model.nomePriCharMin}, HwResponse response, ${root.model.nome}.Grupos... gruposCampos) {
        return valida${root.model.nome}(${root.model.nomePriCharMin}, null, response, gruposCampos);
    }

    /**
     * Valida a classe ${root.model.nome}
     *
     * @param ${root.model.nomePriCharMin} Classe a validar
     * @param response HwResponse para colocar os erros
     * @param nomeBaseCampos Nome do campo ${root.model.nome} conforme está na input
     * @param gruposCampos Lista de grupos de campos a considerar(vazio - TODOS CAMPOS)
     * @return true - valido, false - invalido
     */
    public static boolean valida${root.model.nome}(${root.model.nome} ${root.model.nomePriCharMin}, String nomeBaseCampos, HwResponse response, ${root.model.nome}.Grupos... gruposCampos) {
        if(nomeBaseCampos == null){
            nomeBaseCampos = "";
        }
        if(${root.model.nomePriCharMin} == null){
            response.addErroValidacaoCampo(nomeBaseCampos.isEmpty() ? "${root.model.nomePriCharMin}":nomeBaseCampos, CodigoErroCampo.NAO_INFORMADO, "Esperava receber um ${root.model.nomePriCharMin}, mas nada foi recebido");
            return false;
        }
        int erros = 0;
<#list root.model.listaTodosCampos as campo>
<#if campo.temConfig("validacao") || campo.tipo == "STRING">
        if(campoPertenceAUmDosGrupos("${campo.nome}",gruposCampos)){
            if(!valida${campo.nomePriCharMai}${root.model.nome}(${root.model.nomePriCharMin}.get${campo.nomePriCharMai}(),response, nomeBaseCampos, true)) erros++;
        }else{
            if(!valida${campo.nomePriCharMai}${root.model.nome}(${root.model.nomePriCharMin}.get${campo.nomePriCharMai}(),response, nomeBaseCampos, false)) erros++;
        }
</#if>
</#list>
        return erros == 0;
    }

<#list root.model.listaTodosCampos as campo>

<#if campo.temConfig("validacao") || campo.tipo == "STRING">
${root.getSnippet("utils/funcaoValidacaoCampo",campo)}
</#if>
</#list>

    /**
     * Indica se o campo pertence ao grupo passado.
     *
     * @param campo Nome do campo
     * @param grupo Nome do grupo à verificar
     * @return true se o campo pertence ao grupo, false caso contrário
     */
    public static boolean campoPertenceAoGrupo(String campo, ${root.model.nome}.Grupos grupo){
        switch(grupo){
<#list root.model.getValoresESubconfigsEncontradosDaConfig("grupoCampos") as grupo>
            case ${grupo}:
${root.getSnippet("utils/switchCamposGrupo",grupo)}
</#list>
        }
        return false;
    }

}
