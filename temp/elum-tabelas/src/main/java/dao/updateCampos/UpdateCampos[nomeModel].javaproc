package dao.updateCampos;

import restFramework.tableUpdates.UpdateCampos;
import java.sql.Connection;
import java.sql.SQLException;
import restFramework.MethodContext;
import restFramework.NamedPreparedStatement;
import restFramework.selecaoPersonalizada.ContadorPassavel;
import dao.seletores.queries.Query${root.model.nome};

import restFramework.tableUpdates.updateOperations.BooleanUpdateOperation;
import restFramework.tableUpdates.updateOperations.CustomUpdateOperation;
import restFramework.tableUpdates.updateOperations.NumberUpdateOperation;
import restFramework.tableUpdates.updateOperations.StringUpdateOperation;


/**
 * Classe para realizar UPDATEs na tabela ${root.model.nome}.
 *
 * @author Haftware S.I.
 */
@SuppressWarnings("unused")
public class UpdateCampos${root.model.nome} extends UpdateCampos {
  
    private Query${root.model.nome} query;
    
    public UpdateCampos${root.model.nome}() {
    }

    public UpdateCampos${root.model.nome}(Query${root.model.nome} query){
        this.query = query;
    }
<#list root.model.listaTodosCampos as campo>

    /**
     * Atalho para definir diretamente o valor do campo.
     *
     * @param v Valor a setar no campo.
     * @return Classe para realizar update no campo.
     */
    public UpdateCampos${root.model.nome} ${campo.nome}(${campo.tipoAsString} v){
        return ${campo.nome}().set(v);
    }

    /**
     * Fornece a classe para realizar update no campo ${campo.nome}
     *
     * @return Classe para realizar update no campo
     */
<#switch campo.tipo>
<#case "STRING">
    public StringUpdateOperation<UpdateCampos${root.model.nome}> ${campo.nome}(){
        StringUpdateOperation<UpdateCampos${root.model.nome}> novo = new StringUpdateOperation<>("${campo.nome}", this);
        addUpdate(novo);
        return novo;
    }<#break>
<#case "INTEIRO">
    public NumberUpdateOperation<UpdateCampos${root.model.nome}> ${campo.nome}(){
        NumberUpdateOperation<UpdateCampos${root.model.nome}> novo = new NumberUpdateOperation<>("${campo.nome}", this);
        addUpdate(novo);
        return novo;
    }<#break>
<#case "COM_VIRGULA">
    public NumberUpdateOperation<UpdateCampos${root.model.nome}> ${campo.nome}(){
        NumberUpdateOperation<UpdateCampos${root.model.nome}> novo = new NumberUpdateOperation<>("${campo.nome}", this);
        addUpdate(novo);
        return novo;
    }<#break>
<#case "BOOLEANO">
    public BooleanUpdateOperation<UpdateCampos${root.model.nome}> ${campo.nome}(){
        BooleanUpdateOperation<UpdateCampos${root.model.nome}> novo = new BooleanUpdateOperation<>("${campo.nome}", this);
        addUpdate(novo);
        return novo;
    }<#break>
  <#default>
</#switch>
</#list>


    /**
     * Define uma clausula personalizada para o update, exemplo: valor = valor + incremento
     * @param clause Clausula customizada
     * 
     * @return O UpdateCampos para alterar outros campos
     */
    public UpdateCampos${root.model.nome} customClause(String clause){
        addUpdate(new CustomUpdateOperation<>(clause, this));
        return this;
    }

    public int execute() {
        if(query == null) throw new RuntimeException("Esse UpdateCampos${root.model.nome} não está atrelado a nenhuma query, não é possivel chamar esse método");
        if(updates.isEmpty()) throw new RuntimeException("Nada foi definido para atualizar");
        String sql =
            "UPDATE ${root.model.nomeMin} " +
            "SET " + this.getSetClause(new ContadorPassavel()) + 
            " WHERE " + query.getClause();
        Connection con = MethodContext.getConnection();
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, sql)){
            query.addParameters(stmt);
            this.addParameters(stmt);
            MethodContext.setPrecisaCommitar();
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
