
package model;

import restFramework.JsonManager;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import dao.seletores.queries.Query${root.model.nome};
import restFramework.selecaoPersonalizada.TipoSeletor;
import restFramework.MethodContext;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import restFramework.NamedPreparedStatement;
import restFramework.response.ErroRequestException;
import restFramework.response.HwResponse;
import utils.${root.model.nome}Utils;

/**
 * Classe de modelo do banco de dados para a tabela ${root.model.nomeMin}s
 *
 * @author Haftware S.I.
 */
@SuppressWarnings("unused")
public class ${root.model.nome} extends ${root.model.nome}Impl{

    private boolean isNew = true;
    private boolean isFull = false;
    private boolean changedId = false;
    private Set<String> camposAlterados;

    private static final String fieldsByComma = (
<#list root.model.listaTodosCampos as campo>
        "${campo.nome}<#sep>,</#sep>"<#sep> +</#sep>
</#list>    );

    private static final String updateAllFields = 
<#if (root.model.getListaCamposSemAConfig("isId")?size > 0)>
    (
<#list root.model.listaTodosCampos as campo>
<#if !campo.temConfig("isId")>
        "${campo.nome} = :${campo.nome}<#sep>,</#sep>"<#sep> +</#sep>
</#if>
</#list>
    );
<#else>
"";
</#if>

    private static final String allFieldsBind = (
<#list root.model.listaTodosCampos as campo>
        ":${campo.nome}<#sep>,</#sep>"<#sep> +</#sep>
</#list>    );
<#list root.model.listaTodosCampos as campo>
<#if campo.temConfig("valoresPossiveis")>

    /*
    Valores possíveis:
<#list campo.getSubconfigsDaConfig("valoresPossiveis") as valor>
        ${valor} - ${campo.getValorSubconfigDaConfig("valoresPossiveis", valor)}
</#list>    */</#if>
    // Grupo: <#list campo.getValorConfigIncluiSubconfigs("grupoCampos")as grupo>${grupo}<#sep>, </#sep></#list>
<#if campo.temConfig("serializedName")>
    @SerializedName("${campo.getValorConfig("serializedName")}")
</#if>
<#if campo.temAConfigIgualA("apareceJson","true")>
    @Expose
</#if>
    ${campo.tipoAsString} ${campo.nome};
</#list>

<#assign temConstrutorVazio = true>
    public ${root.model.nome}(<#list root.model.getListaCamposComAConfigIgualA("paramConstrutor","true") as campo><#assign temConstrutorVazio = false>${campo.tipoAsString} ${campo.nome}<#sep>, </#sep></#list>) {
<#list root.model.listaTodosCampos as campo>
<#if campo.temAConfigIgualA("paramConstrutor","true")>
        this.${campo.nome} = ${campo.nome};
<#else>
<#if campo.temConfig("valorInicial")>
        this.${campo.nome} = ${campo.getValorConfig("valorInicial")};
</#if>
</#if>
</#list>
    }

<#if !temConstrutorVazio>
    private ${root.model.nome}() {
    }
</#if>

<#list root.model.listaTodosCampos as campo>
<#if campo.temConfig("valoresPossiveis")>

    /**
    Valores possíveis:<br>
<#list campo.getSubconfigsDaConfig("valoresPossiveis") as valor>
        ${valor} - ${campo.getValorSubconfigDaConfig("valoresPossiveis", valor)}<br>
</#list>    */</#if>
    public ${campo.tipoAsString} get${campo.nomePriCharMai}() {
<#if campo.tipo == 'BOOLEANO' || campo.temConfig("defaultIfNull")>
        if(${campo.nome} == null) return <#if campo.temConfig("defaultIfNull")>${campo.getValorConfig("defaultIfNull")}<#else>false</#if>;
</#if>
        return ${campo.nome};
    }

<#if campo.temConfig("chaveEstrangeira")>
    public ${campo.getValorConfig("chaveEstrangeira")} get${campo.nomePriCharMai}Ref() {
      ${campo.tipoAsString} ref = get${campo.nomePriCharMai}();
      if(ref == null) return null;
      return ${campo.getValorConfig("chaveEstrangeira")}.findById(ref);
    }
</#if>

<#if campo.tipo == 'BOOLEANO' && !campo.temConfig("defaultIfNull")>
    public ${campo.tipoAsString} get${campo.nomePriCharMai}LiteralBoolean() {
        return ${campo.nome};
    }
</#if>
<#if campo.temConfig("valoresPossiveis")>

    /**
    Valores possíveis:<br>
<#list campo.getSubconfigsDaConfig("valoresPossiveis") as valor>
        ${valor} - ${campo.getValorSubconfigDaConfig("valoresPossiveis", valor)}<br>
</#list>    */</#if>
    public ${root.model.nome} set${campo.nomePriCharMai}(${campo.tipoAsString} ${campo.nome}) {
        if (Objects.equals(this.${campo.nome}, ${campo.nome})) return this;
<#if campo.temAConfigIgualA("isId", "true")>
        if(!isNew && this.${campo.nome} != null) changedId = true;
</#if>
        if(camposAlterados == null) camposAlterados = new HashSet<>();
        camposAlterados.add("${campo.nome}");
        this.${campo.nome} = ${campo.nome};
        return this;
    }

<#if campo.temConfig("valorInicial")>
    public ${root.model.nome} reset${campo.nomePriCharMai}() {
<#if campo.temAConfigIgualA("isId", "true")>
        if(!isNew && this.${campo.nome} != null) changedId = true;
</#if>
        if(camposAlterados == null) camposAlterados = new HashSet<>();
        camposAlterados.add("${campo.nome}");
        this.${campo.nome} = ${campo.getValorConfig("valorInicial")};
        return this;
    }
</#if>
<#if campo.temConfig("valoresPossiveis")>

    public String getDescricao${campo.nomePriCharMai}() {
        if(${campo.nome} == null) return null;
        switch(${campo.nome}.toString()){
<#list campo.getSubconfigsDaConfig("valoresPossiveis") as valor>
            case "${valor}": return "${campo.getValorSubconfigDaConfig("valoresPossiveis", valor)}";
</#list>
        }
        return null;
    }</#if>
</#list>
    /**
     * Indica se o ${root.model.nome} está ou não persistido no banco
     * @return true se o ${root.model.nome} ainda não estiver gravado, false caso ja estiver
     */
    public boolean isNew() {
        return this.isNew;
    }

    /**
     * Retorna um objeto para fazer uma busca complexa na tabela ${root.model.nome}, selecionando todos os campos da tabela
     * @return Objeto de Query
     */
    public static Query${root.model.nome} query(){
        return query(TipoSeletor.AND, Fields.ALL);
    }
    
    /**
     * Retorna um objeto para fazer uma busca complexa na tabela ${root.model.nome} com um tipo específico, e selecionando todos os campos da tabela
     * @param tipo tipo de filtragem
     * @return Objeto de Query
     */
    public static Query${root.model.nome} query(TipoSeletor tipo){
        return query(tipo, Fields.ALL);
    }
    
    /**
     * Retorna um objeto para fazer uma busca complexa na tabela ${root.model.nome}, selecionando somente os campos da especificados
     * @param campos campos a selecionar
     * @return Objeto de Query
     */
    public static Query${root.model.nome} query(Fields... campos){
        return query(TipoSeletor.AND, campos);
    }
    
    /**
     * Retorna um objeto para fazer uma busca complexa na tabela ${root.model.nome} com um tipo específico, selecionando somente os campos especificados
     * @param tipo tipo de filtragem
     * @param campos campos a selecionar
     * @return Objeto de Query
     */
    public static Query${root.model.nome} query(TipoSeletor tipo, Fields... campos){
        return new Query${root.model.nome}(tipo, campos);
    }

    /**
     * Procura pelo ${root.model.nome} atráves do id primário ou lança um ErroRequest caso não encontrar
<#list root.model.getListaCamposComAConfig("isId") as campoId>
     * @param ${campoId.nome}
</#list>
     * @param throwError Mensagem de erro para retornar no ErroRequest caso não encontre
     * @return ${root.model.nome} encontrado
     */
    public static ${root.model.nome} findByIdOrThrow(<#list root.model.getListaCamposComAConfig("isId") as campoId>${campoId.tipoAsString} ${campoId.nome}<#sep>, </#sep></#list>, String throwError) {
        ${root.model.nome} ret = ${root.model.nome}.findById(<#list root.model.getListaCamposComAConfig("isId") as campoId>${campoId.nome}<#sep>, </#sep></#list>);
        if(ret == null) throw new ErroRequestException(throwError);
        return ret;
    }
    
    /**
     * Procura pelo ${root.model.nome} atráves do id primário
<#list root.model.getListaCamposComAConfig("isId") as campoId>
     * @param ${campoId.nome}
</#list>
     * @return ${root.model.nome} ou null caso não exista
     */
    public static ${root.model.nome} findById(<#list root.model.getListaCamposComAConfig("isId") as campoId>${campoId.tipoAsString} ${campoId.nome}<#sep>, </#sep></#list>) {
        String sql =
            "SELECT " + Fields.ALL.name +
            " FROM ${root.model.nomeMin} " +
            " WHERE " +
<#list root.model.getListaCamposComAConfig("isId") as campoId>
              "${campoId.nome} = :${campoId.nome}<#sep> AND </#sep>" <#sep>+</#sep></#list>;
        Connection con = MethodContext.getConnection();
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, sql)){
<#list root.model.getListaCamposComAConfig("isId") as campoId>
            stmt.setParameter("${campoId.nome}", ${campoId.nome});
</#list>
            try(ResultSet rs = stmt.executeQuery()) {
                if(!rs.next()) return null;
                return ${root.model.nome}.fromResultSet(rs, Fields.ALL);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    <#assign temUmUnicoCampoIdInteiro = (root.model.getListaCamposComAConfig("isId")?size == 1 && root.model.getCampoComAConfig("isId").tipo == "INTEIRO")>
    <#assign getCampoIdInteiro = (root.model.getCampoComAConfig("isId"))>
    
    /**
     * Grava/Regrava inteiramente o ${root.model.nome} no banco
<#if temUmUnicoCampoIdInteiro>
     * @return Id inserido/Atualizado
</#if>
     */
    public <#if temUmUnicoCampoIdInteiro>${root.model.getCampoComAConfig("isId").tipoAsString}<#else>void</#if> save(){
        String sql;
        boolean isInsert = isNew;
        if(changedId)  throw new RuntimeException("Não é possivel chamar o save() de um model que teve o id alterado");
        if(isNew) {
          sql = "INSERT INTO ${root.model.nomePriCharMin} ( "+ fieldsByComma +" ) VALUES ( " + allFieldsBind + " )";
        } else {
          if(!isFull) throw new RuntimeException("Não é possivel chamar o save() de um model que não foram lidos todos os campos");
          sql = "UPDATE ${root.model.nomeMin} SET "+ updateAllFields +" WHERE " +
<#list root.model.getListaCamposComAConfig("isId") as campoId>
              "${campoId.nome} = :${campoId.nome}<#sep> AND </#sep>" <#sep>+</#sep></#list>;
        }
        Connection con = MethodContext.getConnection();
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, sql)){
            this.bindToQuery(stmt);
            stmt.executeUpdate();
            MethodContext.setPrecisaCommitar();
<#if temUmUnicoCampoIdInteiro>
            if(isInsert){
              ${root.model.getCampoComAConfig("isId").tipoAsString} insertedId;
              try(ResultSet genKeys = stmt.getGeneratedKeys()){
                  genKeys.next();
                  insertedId = genKeys.getInt(1);
                  this.set${getCampoIdInteiro.nomePriCharMai}(insertedId);
              }
              return insertedId;
            }
            return this.get${getCampoIdInteiro.nomePriCharMai}();
</#if>
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    /**
     * Regrava somente os campos alterados do ${root.model.nome} no banco
     * 
     */
    public void update(){
        String sql;
        if(changedId)  throw new RuntimeException("Não é possivel chamar o update() de um model que teve o id alterado");
        if(isNew)  throw new RuntimeException("Não é possivel chamar o update() de um model que não está persistido no banco");
        if(!isFull) throw new RuntimeException("Não é possivel chamar o update() de um model que não foram lidos todos os campos");
        if(camposAlterados == null || camposAlterados.isEmpty()) return;
        StringJoiner camposAlteradosStr = new StringJoiner(",");
        camposAlterados.forEach(nc -> camposAlteradosStr.add(nc + " = :"+ nc));
        sql = "UPDATE ${root.model.nomeMin} SET "+ camposAlteradosStr.toString() +" WHERE " +
<#list root.model.getListaCamposComAConfig("isId") as campoId>
            "${campoId.nome} = :${campoId.nome}<#sep> AND </#sep>" <#sep>+</#sep></#list>;

        Connection con = MethodContext.getConnection();
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, sql)){
            this.bindToQuery(stmt);
            stmt.executeUpdate();
            MethodContext.setPrecisaCommitar();
            this.camposAlterados.clear();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Valida todos campos do modelo
     * 
     * @param gruposCampos Grupos a validar, caso não passado, valida todos
     */
    public boolean validate(HwResponse res, ${root.model.nome}.Grupos... gruposCampos) {
        return ${root.model.nome}Utils.valida${root.model.nome}(this, res, gruposCampos);
    }

    /**
     * Exclui esse ${root.model.nome} do banco
     *
     */
    public void delete(){
        if(changedId) throw new RuntimeException("Não é possivel chamar o delete() de um model que teve o id alterado");
        if(!isNew) throw new RuntimeException("Não é possivel chamar o delete() de um model que não está persistido no banco");
        String updateSql = "DELETE FROM ${root.model.nomeMin} WHERE " +
<#list root.model.getListaCamposComAConfig("isId") as campoId>
            "${campoId.nome} = :${campoId.nome}<#sep> AND </#sep>" <#sep>+</#sep></#list>;
        Connection con = MethodContext.getConnection();
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, updateSql)){
<#list root.model.getListaCamposComAConfig("isId") as campoId>
            stmt.setParameter("${campoId.nome}", this.get${campoId.nomePriCharMai}());
</#list>
            MethodContext.setPrecisaCommitar();
            stmt.executeUpdate();
            this.isNew = true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String toJson() {
      return JsonManager.toJsonOnlyExpose(this);
    }

    public static ${root.model.nome} fromResultSet(ResultSet rs, Fields... pick) throws SQLException{
        ${root.model.nome} ret = new ${root.model.nome}();
        for (Fields f : pick) {
            switch(f){
                case ALL:
                    ret.isFull = true;
<#list root.model.listaTodosCampos as campo>
                case ${campo.nome}:
  <#if campo.tipoAsString == "Integer">
                    ret.${campo.nome} = rs.getInt("${campo.nome}");
  <#else>
                    ret.${campo.nome} = rs.get${campo.tipoAsString}("${campo.nome}");
  </#if>
                    if(rs.wasNull()) ret.${campo.nome} = null;
                    if(f != Fields.ALL) break;
</#list>
            }
        }
        ret.isNew = false;
        return ret;
    }

    public void bindToQuery(NamedPreparedStatement stmt){
        <#list root.model.listaTodosCampos as campo>
        stmt.setParameter("${campo.nome}", this.${campo.nome});
        </#list>
        this.isNew = false;
    }

    @Override
    public String toString() {
        return "${root.model.nome}{" + 
        <#list root.model.listaTodosCampos as campo>
          "${campo.nome}=" + Objects.toString(${campo.nome})<#sep> + ", "</#sep> +
        </#list>
        '}';
    }
  
    public enum Grupos {
        <#list root.model.getValoresESubconfigsEncontradosDaConfig("grupoCampos") as grupo>
        ${grupo}<#sep>,</#sep>
        </#list>
    }
    
    public enum Fields {
<#list root.model.listaTodosCampos as campo>
        ${campo.nome} ("${campo.nome}"),
</#list>

        /**
         * Retorna todos os campos do ${root.model.nome}
         */
        ALL ("*");

        private final String name;       

        private Fields(String s) {
          name = s;
        }

        public boolean equalsName(String otherName) {
          return name.equals(otherName);
        }

        @Override
        public String toString() {
         return this.name;
        }
    }
}
