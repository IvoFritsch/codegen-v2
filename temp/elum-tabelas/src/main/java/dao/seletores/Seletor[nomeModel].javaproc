/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.seletores;

import restFramework.selecaoPersonalizada.sqlComparisions.BooleanSqlComparision;
import restFramework.selecaoPersonalizada.sqlComparisions.NumberSqlComparision;
import restFramework.selecaoPersonalizada.sqlComparisions.StringSqlComparision;
import restFramework.selecaoPersonalizada.sqlComparisions.CustomClauseSqlComparision;
import restFramework.selecaoPersonalizada.Seletor;
import restFramework.selecaoPersonalizada.TipoSeletor;


/**
 * Seletor personalizado para a classe ${root.model.nome}, permite buscar por essa classe no banco filtrando qualquer campo desejado.<br>
 * Para isso utilizar as funções de "...Seletor" do ${root.model.nome}Dao.
 *
 * @author ivoaf
 */
@SuppressWarnings("unused")
public class Seletor${root.model.nome} extends Seletor{
    
    /**
     * Instancia um novo seletor do tipo AND para a classe ${root.model.nome}
     * 
     */
    public Seletor${root.model.nome}() {
        super(TipoSeletor.AND);
    }
    
    /**
     * Instancia um novo seletor do tipo especificado para a classe ${root.model.nome}
     * 
     */
    public Seletor${root.model.nome}(TipoSeletor tipo) {
        super(tipo);
    }
	
    /**
     * Cria um seletor que retornará todos os registros.
     *
     * @return Seletor
     */
    public Seletor${root.model.nome} all(){
        isAll = true;
        return this;
    }<#list root.model.listaTodosCampos as campo>
	
    /**
     * Atalho para comparar igualdadade do campo ${campo.nome} com o valor especificado.<br>
     * Chamar essa função é equivalente a chamar "${campo.nome}().equals(valor)"
     *
<#if campo.temConfig("valoresPossiveis")>     * <br>Valores possíveis:<br>
<#list campo.getSubconfigsDaConfig("valoresPossiveis") as valor>
     *        ${valor} - ${campo.getValorSubconfigDaConfig("valoresPossiveis", valor)}<br>
</#list></#if>
     * @param valor O valor com o qual comparar igualdade do campo ${campo.nome}
     * @return este próprio seletor de ${root.model.nome}, pronto para comparar outro campo
     */
    public Seletor${root.model.nome} ${campo.nome}(${campo.tipoAsString} valor){
        return ${campo.nome}().equals(valor);
    }
	
    /**
     * Fornece o comparador para o campo ${campo.nome}
     *
     * @return Comparador para o campo
     */
<#switch campo.tipo>
<#case "STRING">
    public StringSqlComparision<Seletor${root.model.nome}> ${campo.nome}(){
        StringSqlComparision<Seletor${root.model.nome}> comp = new StringSqlComparision<>("${campo.nome}",this);
        addComparision(comp);
        return comp;
    }<#break>
<#case "INTEIRO">
    public NumberSqlComparision<Seletor${root.model.nome}> ${campo.nome}(){
        NumberSqlComparision<Seletor${root.model.nome}> comp = new NumberSqlComparision<>("${campo.nome}",this);
        addComparision(comp);
        return comp;
    }<#break>
<#case "COM_VIRGULA">
    public NumberSqlComparision<Seletor${root.model.nome}> ${campo.nome}(){
        NumberSqlComparision<Seletor${root.model.nome}> comp = new NumberSqlComparision<>("${campo.nome}",this);
        addComparision(comp);
        return comp;
    }<#break>
<#case "BOOLEANO">
    public BooleanSqlComparision<Seletor${root.model.nome}> ${campo.nome}(){
        BooleanSqlComparision<Seletor${root.model.nome}> comp = new BooleanSqlComparision<>("${campo.nome}",this);
        addComparision(comp);
        return comp;
    }<#break>
  <#default>
</#switch>
</#list>

    /**
     * Adiciona uma cláusula de comparação personalizada, por exemplo "valor = valorMaximo"
     *
     * @return este próprio seletor de ${root.model.nome}, pronto para comparar mais coisas
     */
    public Seletor${root.model.nome} customClause(String clause){
        addComparision(new CustomClauseSqlComparision<>(clause,this));
        return this;
    }
	
    public Seletor${root.model.nome} addSeletor(Seletor${root.model.nome} seletor){
        addComparision(seletor);
        return this;
    }
    
}
