/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.seletores.queries;

import dao.order.${root.model.nome}OrderBy;
import dao.seletores.Seletor${root.model.nome};
import dao.updateCampos.UpdateCampos${root.model.nome};
import restFramework.selecaoPersonalizada.sqlComparisions.BooleanSqlComparision;
import restFramework.selecaoPersonalizada.sqlComparisions.NumberSqlComparision;
import restFramework.selecaoPersonalizada.sqlComparisions.StringSqlComparision;
import restFramework.selecaoPersonalizada.sqlComparisions.CustomClauseSqlComparision;
import restFramework.selecaoPersonalizada.Seletor;
import restFramework.selecaoPersonalizada.TipoSeletor;
import restFramework.MethodContext;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringJoiner;
import model.${root.model.nome};
import restFramework.NamedPreparedStatement;
import restFramework.QueryMethods;
import restFramework.response.ErroRequestException;

/**
 * Classe para realizar buscas na tabela ${root.model.nome}
 *
 * @author ivoaf
 */
@SuppressWarnings("unused")
public class Query${root.model.nome} extends Seletor implements QueryMethods<${root.model.nome}>{
    
    private final String camposString;
    private final ${root.model.nome}.Fields[] campos;
    private ${root.model.nome}OrderBy order;
    
    /**
     * Instancia um novo seletor do tipo especificado para a classe ${root.model.nome}
     * 
     * @param tipo Tipo de agregação dessa query
     * @param campos Campos a selecionar
     */
    public Query${root.model.nome}(TipoSeletor tipo, ${root.model.nome}.Fields... campos) {
        super(tipo);
        this.campos = campos;
        StringJoiner joinerCampos = new StringJoiner(",");
        for (${root.model.nome}.Fields campo : campos) {
            joinerCampos.add(campo.toString());
        }
        camposString = joinerCampos.toString();
    }

    /**
     * Cria um seletor que retornará todos os registros.
     *
     * @return Seletor
     */
    public Query${root.model.nome} all(){
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
    public Query${root.model.nome} ${campo.nome}(${campo.tipoAsString} valor){
        return ${campo.nome}().equals(valor);
    }

    /**
     * Fornece o comparador para o campo ${campo.nome}
     *
     * @return Comparador para o campo
     */
<#switch campo.tipo>
<#case "STRING">
    public StringSqlComparision<Query${root.model.nome}> ${campo.nome}(){
        StringSqlComparision<Query${root.model.nome}> comp = new StringSqlComparision<>("${campo.nome}",this);
        addComparision(comp);
        return comp;
    }<#break>
<#case "DATA">
    public DatetimeSqlComparision<Query${root.model.nome}> ${campo.nome}(){
        DatetimeSqlComparision<Query${root.model.nome}> comp = new DatetimeSqlComparision<>("${campo.nome}",this);
        addComparision(comp);
        return comp;
    }<#break>
<#case "INTEIRO">
    public NumberSqlComparision<Query${root.model.nome}> ${campo.nome}(){
        NumberSqlComparision<Query${root.model.nome}> comp = new NumberSqlComparision<>("${campo.nome}",this);
        addComparision(comp);
        return comp;
    }<#break>
<#case "COM_VIRGULA">
    public NumberSqlComparision<Query${root.model.nome}> ${campo.nome}(){
        NumberSqlComparision<Query${root.model.nome}> comp = new NumberSqlComparision<>("${campo.nome}",this);
        addComparision(comp);
        return comp;
    }<#break>
<#case "BOOLEANO">
    public BooleanSqlComparision<Query${root.model.nome}> ${campo.nome}(){
        BooleanSqlComparision<Query${root.model.nome}> comp = new BooleanSqlComparision<>("${campo.nome}",this);
        addComparision(comp);
        return comp;
    }<#break>
  <#default>
</#switch>
</#list>


    /**
     * Adiciona uma cláusula de comparação personalizada, por exemplo "valor = valorMaximo"
     *
     * @param clause Clausula personalizada
     * @return este próprio seletor de ${root.model.nome}, pronto para comparar mais coisas
     */
    public Query${root.model.nome} customClause(String clause){
        addComparision(new CustomClauseSqlComparision<>(clause,this));
        return this;
    }
	
    public Query${root.model.nome} addSeletor(Seletor${root.model.nome} seletor){
        addComparision(seletor);
        return this;
    }
    
    public ${root.model.nome}OrderBy orderBy(){
        if (this.order == null) this.order = new ${root.model.nome}OrderBy(this);
        return this.order;
    }

    public UpdateCampos${root.model.nome} update(){
        return new UpdateCampos${root.model.nome}(this);
    }

    public int delete(){
        String sql =
            "DELETE FROM ${root.model.nomeMin}" +
            " WHERE "+this.getClause();
        Connection con = MethodContext.getConnection();
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, sql)){
            this.addParameters(stmt);
            MethodContext.setPrecisaCommitar();
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public int count(){
        String sql =
            "SELECT count(" + camposString + ")" +
            " FROM ${root.model.nomeMin} " +
            " WHERE "+this.getClause();
        Connection con = MethodContext.getConnection();
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, sql)){
            this.addParameters(stmt);
            try(ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ${root.model.nome} findOne(){
        String sql =
            "SELECT " + camposString +
            " FROM ${root.model.nomeMin} " +
            " WHERE "+this.getClause() + 
            (order != null ? (order.getClause()) : "") +
            " LIMIT 1" + (order != null && order.useIndex() ? " USING INDEX ": "");
        Connection con = MethodContext.getConnection();
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, sql)){
            this.addParameters(stmt);
            try(ResultSet rs = stmt.executeQuery()) {
                if(!rs.next()) return null;
                return ${root.model.nome}.fromResultSet(rs, campos);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ${root.model.nome} findOneOrThrow(String throwError) {
        ${root.model.nome} ret = this.findOne();
        if(ret == null) throw new ErroRequestException(throwError);
        return ret;
    }

    @Override
    public List<${root.model.nome}> find() {
        return this.find(0, 0);
    }

    @Override
    public List<${root.model.nome}> find(int pageSize, int pageNumber) {
        int limit = pageSize;
        int offset = pageNumber * pageSize;
        String sql =
            "SELECT " + camposString +
            " FROM ${root.model.nomeMin} " +
            " WHERE "+this.getClause() + 
            (order != null ? (order.getClause()) : "") +
            (pageSize > 0 ? (" LIMIT "+ limit + " OFFSET " + offset + (order != null && order.useIndex() ? " USING INDEX ": "")) : "");
        List<${root.model.nome}> ret = new ArrayList<>();
        Connection con = MethodContext.getConnection();
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, sql)){
            this.addParameters(stmt);
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()){
                    ret.add(${root.model.nome}.fromResultSet(rs, campos));
                }
                return ret;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int findLazy(Consumer<${root.model.nome}> c) {
        return this.findLazy((r, index) -> c.accept(r));
    }
    
    @Override
    public int findLazy(BiConsumer<${root.model.nome},Integer> c) {
        String sql =
            "SELECT " + camposString +
            " FROM ${root.model.nomeMin} " +
            " WHERE "+this.getClause() + 
            (order != null ? (order.getClause()) : "");
        Connection con = MethodContext.getConnection();
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, sql)){
            this.addParameters(stmt);
            try(ResultSet rs = stmt.executeQuery()) {
                int count = 0;
                while(rs.next()){
                    c.accept(${root.model.nome}.fromResultSet(rs, campos), count);
                    count++;
                }
                return count;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
