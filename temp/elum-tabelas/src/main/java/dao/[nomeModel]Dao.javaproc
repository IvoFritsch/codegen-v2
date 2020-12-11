package dao;

import dao.order.${root.model.nome}OrderBy;
import dao.seletores.Seletor${root.model.nome};
import dao.updateCampos.UpdateCampos${root.model.nome};
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import model.${root.model.nome};
import restFramework.Dao;
import restFramework.MethodInput;
import restFramework.NamedPreparedStatement;
import restFramework.selecaoPersonalizada.ContadorPassavel;

/**
 * DAO de acesso ao banco para a classe ${root.model.nome}, essa classe é inteiramente gerada pelo Codegen
 * não há necessidade de alterá-la
 *
 * @author Haftware S.I.
 */
@SuppressWarnings("unused")
public class ${root.model.nome}Dao extends Dao {

    private static final String camposSeparadosPorVirgula = (
<#list root.model.listaTodosCampos as campo>
        "${campo.nome}<#sep>,</#sep>"<#sep> +</#sep>
</#list>    );

    private static final String camposSeparadosPorVirgulaParaBind = (
<#list root.model.listaTodosCampos as campo>
        ":${campo.nome}<#sep>,</#sep>"<#sep> +</#sep>
</#list>    );
    private static final String setCamposParaUpdate = 
<#if (root.model.getListaCamposSemAConfig("isId")?size > 0)>
(
<#list root.model.listaTodosCampos as campo>
<#if !campo.temConfig("isId")>
        "${campo.nome} = :${campo.nome}<#sep>,</#sep>"<#sep> +</#sep>
</#if>
</#list>    );
<#else>
"";
</#if>
    public ${root.model.nome}Dao(){
        super();
    }

    public ${root.model.nome}Dao(Connection transaction, MethodInput instanciador){
        super(transaction, instanciador);
    }

    private List<${root.model.nome}> executaQuerySql(Connection connection, String sql, Seletor${root.model.nome} seletor){
        List<${root.model.nome}> ret = new ArrayList<>();
        Connection con = getConnection();
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, sql)){
            if(seletor != null) seletor.addParameters(stmt);
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()){
                    ret.add(${root.model.nome}.fromResultSet(rs, ${root.model.nome}.Fields.ALL));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return ret;
    }

    /**
     * Conta quantos registros se encaixam no seletor personalizado.
     *
     * @param seletor o seletor a filtrar
     * @return Quantidade de registros
     */
    public int contaRegistrosViaSeletor(Seletor${root.model.nome} seletor) {
        String sql =
            "SELECT count(*)"+
            " FROM ${root.model.nomeMin} " +
            " WHERE "+seletor.getClause();
        Connection con = getConnection();
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, sql)){
            seletor.addParameters(stmt);
            try(ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Verifica se a tabela possui pelo menos um registro que se encaixe no seletor passado
     *
     * @param seletor o seletor a filtrar
     * @return true se possui ao menos 1 registro q se encaixe
     *
    public int possuiRegistroNoSeletor(Seletor${root.model.nome} seletor) {

        String sql =
            "SELECT 1"+
            " FROM ${root.model.nomeMin} " +
            " WHERE "+seletor.getClause()+ " LIMIT 1";
        Connection con = getConnection();
        Query query = con.createQuery(sql);
        seletor.addParameters(query);
        query.executeScalar(Integer.class);
        return cnt;
    }*/

    /**
     * Busca um ${root.model.nome} através da primary key na tabela.
     *
<#list root.model.getListaCamposComAConfig("isId") as campoId>
     * @param ${campoId.nome}
</#list>
     * @return registro lido na tabela, null se não for encontrado
     */
    public ${root.model.nome} procuraViaPrimaryKey(<#list root.model.getListaCamposComAConfig("isId") as campoId>${campoId.tipoAsString} ${campoId.nome}<#sep>, </#sep></#list>) {
        String sql =
            "SELECT " + camposSeparadosPorVirgula +
            " FROM ${root.model.nomeMin} " +
            " WHERE " +
<#list root.model.getListaCamposComAConfig("isId") as campoId>
              "${campoId.nome} = :${campoId.nome}<#sep> AND </#sep>" <#sep>+</#sep></#list>;

        Connection con = getConnection();
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, sql)){
<#list root.model.getListaCamposComAConfig("isId") as campoId>
            stmt.setParameter("${campoId.nome}", ${campoId.nome});
</#list>
            try(ResultSet rs = stmt.executeQuery()) {
                if(!rs.next()) return null;
                return ${root.model.nome}.fromResultSet(rs, ${root.model.nome}.Fields.ALL);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Busca o primeiro registro que se encaixe no seletor personalizado.
     *
     * @param seletor o seletor a buscar
     * @return Primeiro registro encontrado, null se não encontrar nenhum
     */
    public ${root.model.nome} procuraUnicoViaSeletor(Seletor${root.model.nome} seletor) {
        String sql =
            "SELECT " + camposSeparadosPorVirgula +
            " FROM ${root.model.nomeMin} " +
            " WHERE "+seletor.getClause()+" LIMIT 1";
        Connection con = getConnection();
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, sql)){
            seletor.addParameters(stmt);
            try(ResultSet rs = stmt.executeQuery()) {
                if(!rs.next()) return null;
                return ${root.model.nome}.fromResultSet(rs, ${root.model.nome}.Fields.ALL);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Busca uma lista de ${root.model.nome} através de um seletor personalizado.
     *
     * @param seletor o seletor a buscar
     * @return Lista de registros encontrados
     */
    public List<${root.model.nome}> procuraViaSeletor(Seletor${root.model.nome} seletor) {
        String sql =
            "SELECT " + camposSeparadosPorVirgula +
            " FROM ${root.model.nomeMin} " +
            " WHERE "+seletor.getClause();
        Connection con = getConnection();
        return executaQuerySql(con, sql, seletor);
    }   

    /**
     * Busca uma lista de ${root.model.nome} através de um seletor personalizado, ordenando os registros por um especificador de ordem.
     *
     * @param seletor o seletor a buscar
     * @param orderBy especificador de ordem
     * @return Lista de registros encontrados
     */
    public List<${root.model.nome}> procuraViaSeletor(Seletor${root.model.nome} seletor,${root.model.nome}OrderBy orderBy) {
        String sql =
            "SELECT " + camposSeparadosPorVirgula +
            " FROM ${root.model.nomeMin} " +
            " WHERE "+seletor.getClause() + " " +
                orderBy.getClause();
        Connection con = getConnection();
        return executaQuerySql(con, sql, seletor);
    }   
  
    /**
     * Busca uma lista de ${root.model.nome} através de um seletor personalizado, paginando os registros.
     *
     * @param seletor o seletor a buscar
     * @param pageSize o tamanho da página a ser retornada(quantidade de registros)
     * @param pageNumber o número da página a retornar
     * @return Lista de registros encontrados
     */
    public List<${root.model.nome}> procuraViaSeletor(Seletor${root.model.nome} seletor, int pageSize, int pageNumber) {
        if(pageNumber < 0) return new ArrayList<>();
        int limit = pageSize;
        int offset = pageNumber * pageSize;
        String sql =
            "SELECT " + camposSeparadosPorVirgula +
            " FROM ${root.model.nomeMin}" +
            " WHERE "+seletor.getClause() + 
            " LIMIT "+limit+" OFFSET "+offset;
        Connection con = getConnection();
        return executaQuerySql(con, sql, seletor);
    }   
  
    /**
     * Busca uma lista de ${root.model.nome} através de um seletor personalizado, ordenando os registros por um especificador de 
     * ordem e paginando os registros.
     *
     * @param seletor o seletor a buscar
     * @param orderBy especificador de ordem
     * @param pageSize o tamanho da página a ser retornada(quantidade de registros)
     * @param pageNumber o número da página a retornar
     * @return Lista de registros encontrados
     */
    public List<${root.model.nome}> procuraViaSeletor(Seletor${root.model.nome} seletor, ${root.model.nome}OrderBy orderBy, int pageSize, int pageNumber) {
        if(pageNumber < 0) return new ArrayList<>();
        int limit = pageSize;
        int offset = pageNumber * pageSize;
        String sql =
            "SELECT " + camposSeparadosPorVirgula +
            " FROM ${root.model.nomeMin}" +
            " WHERE "+seletor.getClause() + " " +
                orderBy.getClause() +
            " LIMIT "+limit+" OFFSET "+offset + (orderBy.useIndex() ? " USING INDEX ": "");
        Connection con = getConnection();
        return executaQuerySql(con, sql, seletor);
    }   
  
  
<#list root.model.listaTodosCampos as campo><#if campo.temAConfigIgualA("buscavelBanco", "true") || campo.temConfig("chaveEstrangeira")>

    /**
     * Conta quantos registros tem o campo ${campo.nome} igual ao especificado.
     *
     * @param ${campo.nome} o valor para filtrar no campo ${campo.nome}
     * @return Quantidade de registros
     */
    public int contaRegistrosVia${campo.nomePriCharMai}(${campo.tipoAsString} ${campo.nome}) {
        String sql =
            "SELECT count(*)"+
            " FROM ${root.model.nomeMin} " +
            " WHERE ${campo.nome} = :${campo.nome}";
        Connection con = getConnection();
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, sql)){
            stmt.setParameter("${campo.nome}", ${campo.nome});
            try(ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
  
    /**
     * Busca o primeiro registro que tenha o ${campo.nome} igual ao especificado.
     *
     * @param ${campo.nome} o valor para filtrar no campo ${campo.nome}
     * @return Primeiro registro encontrado, null se não encontrar nenhum
     */
    public ${root.model.nome} procuraUnicoVia${campo.nomePriCharMai}(${campo.tipoAsString} ${campo.nome}) {
        String sql =
            "SELECT " + camposSeparadosPorVirgula +
            " FROM ${root.model.nomeMin} " +
            " WHERE ${campo.nome} = :${campo.nome} LIMIT 1";
        Connection con = getConnection();
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, sql)){
            stmt.setParameter("${campo.nome}", ${campo.nome});
            try(ResultSet rs = stmt.executeQuery()) {
                if(!rs.next()) return null;
                return ${root.model.nome}.fromResultSet(rs, ${root.model.nome}.Fields.ALL);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
  
    /**
     * Busca uma lista de ${root.model.nome}, filtrando pelo campo ${campo.nome}.
     *
     * @param ${campo.nome} o valor para filtrar no campo ${campo.nome}
     * @return Lista de registros encontrados
     */
    public List<${root.model.nome}> procuraVia${campo.nomePriCharMai}(${campo.tipoAsString} ${campo.nome}) {
        String sql =
            "SELECT " + camposSeparadosPorVirgula +
            " FROM ${root.model.nomeMin} " +
            " WHERE ${campo.nome} = :${campo.nome}";

        List<${root.model.nome}> ret = new ArrayList<>();
        Connection con = getConnection();
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, sql)){
            stmt.setParameter("${campo.nome}", ${campo.nome});
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()){
                    ret.add(${root.model.nome}.fromResultSet(rs, ${root.model.nome}.Fields.ALL));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return ret;
    }
  
    /**
     * Busca uma lista de ${root.model.nome}, filtrando pelo campo ${campo.nome} e 
     * ordenando os registros através de um especificador de ordem.
     *
     * @param ${campo.nome} o valor para filtrar no campo ${campo.nome}
     * @param orderBy especificador de ordem
     * @return Lista de registros encontrados
     */
    public List<${root.model.nome}> procuraVia${campo.nomePriCharMai}(${campo.tipoAsString} ${campo.nome}, ${root.model.nome}OrderBy orderBy) {
        String sql =
            "SELECT " + camposSeparadosPorVirgula +
            " FROM ${root.model.nomeMin} " +
            " WHERE ${campo.nome} = :${campo.nome} " +
            orderBy.getClause()+ (orderBy.useIndex() ? "": "");

        List<${root.model.nome}> ret = new ArrayList<>();
        Connection con = getConnection();
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, sql)){
            stmt.setParameter("${campo.nome}", ${campo.nome});
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()){
                    ret.add(${root.model.nome}.fromResultSet(rs, ${root.model.nome}.Fields.ALL));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return ret;
    }
  
    /**
     * Busca uma lista de ${root.model.nome}, filtrando pelo campo ${campo.nome}, paginando os registros.
     *
     * @param ${campo.nome} o valor para filtrar no campo ${campo.nome}
     * @param pageSize o tamanho da página a ser retornada(quantidade de registros)
     * @param pageNumber o número da página a retornar
     * @return Lista de registros encontrados
     */
    public List<${root.model.nome}> procuraVia${campo.nomePriCharMai}(${campo.tipoAsString} ${campo.nome}, int pageSize, int pageNumber) {
        if(pageNumber < 0) return new ArrayList<>();
        int limit = pageSize;
        int offset = pageNumber * pageSize;
        String sql =
            "SELECT " + camposSeparadosPorVirgula +
            " FROM ${root.model.nomeMin}" +
            " WHERE ${campo.nome} = :${campo.nome}" + 
            " LIMIT "+limit+" OFFSET "+offset;

        List<${root.model.nome}> ret = new ArrayList<>();
        Connection con = getConnection();
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, sql)){
            stmt.setParameter("${campo.nome}", ${campo.nome});
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()){
                    ret.add(${root.model.nome}.fromResultSet(rs, ${root.model.nome}.Fields.ALL));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return ret;
    }
  
    /**
     * Busca uma lista de ${root.model.nome}, filtrando pelo campo ${campo.nome}, paginando os registros.
     *
     * @param ${campo.nome} o valor para filtrar no campo ${campo.nome}
     * @param orderBy especificador de ordem
     * @param pageSize o tamanho da página a ser retornada(quantidade de registros)
     * @param pageNumber o número da página a retornar
     * @return Lista de registros encontrados
     */
    public List<${root.model.nome}> procuraVia${campo.nomePriCharMai}(${campo.tipoAsString} ${campo.nome}, ${root.model.nome}OrderBy orderBy, int pageSize, int pageNumber) {
        if(pageNumber < 0) return new ArrayList<>();
        int limit = pageSize;
        int offset = pageNumber * pageSize;
        String sql =
            "SELECT " + camposSeparadosPorVirgula +
            " FROM ${root.model.nomeMin}" +
            " WHERE ${campo.nome} = :${campo.nome} " + 
            orderBy.getClause() +
            " LIMIT "+limit+" OFFSET "+offset+ (orderBy.useIndex() ? " USING INDEX ": "");
            
        List<${root.model.nome}> ret = new ArrayList<>();
        Connection con = getConnection();
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, sql)){
            stmt.setParameter("${campo.nome}", ${campo.nome});
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()){
                    ret.add(${root.model.nome}.fromResultSet(rs, ${root.model.nome}.Fields.ALL));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return ret;
    }
</#if></#list>
  
    /**
     * Exclui do banco todos os registros que se encaixem no seletor passado
     *
     * @param seletor o seletor a filtrar
     * @return numero de ocorrências afetadas
     */
    public int excluiRegistrosViaSeletor(Seletor${root.model.nome} seletor) {
        String sql =
            "DELETE  FROM ${root.model.nomeMin}" +
            " WHERE "+seletor.getClause();
        Connection con = getConnection();
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, sql)){
            seletor.addParameters(stmt);
            instanciador.precisaCommitar = true;
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }   

    /**
     * Atualiza o ${root.model.nome} na tabela, regravando todos os campos do registro,
     * sempre dar preferencia para a função atualizaRegistroViaPrimaryKey.
     *
     * @param ${root.model.nomePriCharMin} ${root.model.nome} a regravar
     */
    public void atualizaRegistro(${root.model.nome} ${root.model.nomePriCharMin}) {
        atualizaRegistroInternal(${root.model.nomePriCharMin}, getConnection());
    }

    /**
     * Atualiza os registros que se encaixem no seletor, com base num especificador de Update de ${root.model.nome}
     * (UpdateCampos${root.model.nome})
     *
     * @param seletor o seletor a buscar
     * @param updt especificador de update dos campos do ${root.model.nome}
     * @return numero de ocorrências afetadas
     */
    public int atualizaRegistrosViaSeletor(Seletor${root.model.nome} seletor, UpdateCampos${root.model.nome} updt) {
        
        String sql =
            "UPDATE ${root.model.nomeMin} " +
            "SET " + updt.getSetClause(new ContadorPassavel()) + 
            " WHERE "+seletor.getClause();
        Connection con = getConnection();
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, sql)){
            seletor.addParameters(stmt);
            updt.addParameters(stmt);
            instanciador.precisaCommitar = true;
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
  
    /**
     * Atualiza o registro indicado pela primary key, com base num especificador de Update de ${root.model.nome}
     * (UpdateCampos${root.model.nome})
     *
<#list root.model.getListaCamposComAConfig("isId") as campoId>
     * @param ${campoId.nome}
</#list>
     * @param updt especificador de update dos campos do ${root.model.nome}
     */
    public void atualizaRegistroViaPrimaryKey(<#list root.model.getListaCamposComAConfig("isId") as campoId>${campoId.tipoAsString} ${campoId.nome}<#sep>, </#sep></#list>, UpdateCampos${root.model.nome} updt) {
        
        String sql =
            "UPDATE ${root.model.nomeMin} " +
            "SET " + updt.getSetClause(new ContadorPassavel()) + 
            " WHERE " +
<#list root.model.getListaCamposComAConfig("isId") as campoId>
            "${campoId.nome} = :${campoId.nome}<#sep> AND </#sep>" <#sep>+</#sep></#list>;

        Connection con = getConnection();
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, sql)){
<#list root.model.getListaCamposComAConfig("isId") as campoId>
            stmt.setParameter("${campoId.nome}", ${campoId.nome});
</#list>
            updt.addParameters(stmt);
            instanciador.precisaCommitar = true;
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void atualizaRegistroInternal(${root.model.nome} ${root.model.nomePriCharMin}, Connection con) {
        String updateSql = "UPDATE ${root.model.nomeMin} SET "+setCamposParaUpdate+" WHERE " +
<#list root.model.getListaCamposComAConfig("isId") as campoId>
            "${campoId.nome} = :${campoId.nome}<#sep> AND </#sep>" <#sep>+</#sep></#list>;
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, updateSql)){
            ${root.model.nomePriCharMin}.bindToQuery(stmt);
            instanciador.precisaCommitar = true;
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
  
    /**
     * Exclui o ${root.model.nome} da tabela.
     *
     * @param ${root.model.nomePriCharMin} ${root.model.nome} a excluir
     */
    public void excluiRegistro(${root.model.nome} ${root.model.nomePriCharMin}) {
        excluiRegistroInternal(${root.model.nomePriCharMin}, getConnection());
    }

    private void excluiRegistroInternal(${root.model.nome} ${root.model.nomePriCharMin}, Connection con) {
        String updateSql = "DELETE FROM ${root.model.nomeMin} WHERE " +
<#list root.model.getListaCamposComAConfig("isId") as campoId>
            "${campoId.nome} = :${campoId.nome}<#sep> AND </#sep>" <#sep>+</#sep></#list>;
        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, updateSql)){
<#list root.model.getListaCamposComAConfig("isId") as campoId>
            stmt.setParameter("${campoId.nome}", ${root.model.nomePriCharMin}.get${campoId.nomePriCharMai}());
</#list>
            instanciador.precisaCommitar = true;
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    <#assign temUmUnicoCampoIdInteiro = (root.model.getListaCamposComAConfig("isId")?size == 1 && root.model.getCampoComAConfig("isId").tipo == "INTEIRO")>
    <#assign getCampoIdInteiro = (root.model.getCampoComAConfig("isId"))>
    /**
     * Insere o ${root.model.nome} na tabela, retorna a primary key gerada, quando aplicavel.
     *
     * @param ${root.model.nomePriCharMin} ${root.model.nome} a inserir
<#if temUmUnicoCampoIdInteiro>
     * @return Id inserido
</#if>
     */
  public <#if temUmUnicoCampoIdInteiro>${root.model.getCampoComAConfig("isId").tipoAsString}<#else>void</#if> insereRegistro(${root.model.nome} ${root.model.nomePriCharMin}) {
        <#if temUmUnicoCampoIdInteiro>return </#if>insereRegistroInternal(${root.model.nomePriCharMin}, getConnection());
    }

    private <#if temUmUnicoCampoIdInteiro>${root.model.getCampoComAConfig("isId").tipoAsString}<#else>void</#if> insereRegistroInternal(${root.model.nome} ${root.model.nomePriCharMin}, Connection con) {
        String sql = "INSERT INTO ${root.model.nomePriCharMin} ( "+camposSeparadosPorVirgula+" ) VALUES ( " + camposSeparadosPorVirgulaParaBind + " )";

        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, sql)){
            ${root.model.nomePriCharMin}.bindToQuery(stmt);
            stmt.executeUpdate();
            instanciador.precisaCommitar = true;
<#if temUmUnicoCampoIdInteiro>
            ${root.model.getCampoComAConfig("isId").tipoAsString} insertedId;
            try(ResultSet genKeys = stmt.getGeneratedKeys()){
                genKeys.next();
                insertedId = genKeys.getInt(1);
                ${root.model.nomePriCharMin}.set${getCampoIdInteiro.nomePriCharMai}(insertedId);
            }
            return insertedId;
</#if>
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
  
    /**
     * Insere uma lista de ${root.model.nome} na tabela, retorna as primary keys geradas, quando aplicavel.
     *
     * @param listaInserir List de ${root.model.nome} a inserir
<#if temUmUnicoCampoIdInteiro>
     * @return Lista de ids inseridos
</#if>
     */
    public <#if temUmUnicoCampoIdInteiro>List<${root.model.getCampoComAConfig("isId").tipoAsString}><#else>void</#if> insereListaRegistros(List<${root.model.nome}> listaInserir){
<#if temUmUnicoCampoIdInteiro>
        if(listaInserir.isEmpty()) return new ArrayList<>();
</#if>
        <#if temUmUnicoCampoIdInteiro>return </#if>insereListaRegistrosInternal(listaInserir, getConnection());
    }


    private <#if temUmUnicoCampoIdInteiro>List<${root.model.getCampoComAConfig("isId").tipoAsString}><#else>void</#if> insereListaRegistrosInternal(List<${root.model.nome}> listaInserir, Connection con){
        String sql = "INSERT INTO ${root.model.nomePriCharMin} ( "+camposSeparadosPorVirgula+" ) VALUES ( " + camposSeparadosPorVirgulaParaBind + " )";

        try(NamedPreparedStatement stmt = new NamedPreparedStatement(con, sql)){
            for(${root.model.nome} ${root.model.nomePriCharMin} : listaInserir){
                ${root.model.nomePriCharMin}.bindToQuery(stmt);
                stmt.addBatch();
            }
            stmt.executeBatch();
            instanciador.precisaCommitar = true;
<#if temUmUnicoCampoIdInteiro>
            List<Integer> insertedIds = new ArrayList<>();
            try(ResultSet genKeys = stmt.getGeneratedKeys()){
                int n = 0;
                while(genKeys.next()){
                    ${root.model.getCampoComAConfig("isId").tipoAsString} insertedId = genKeys.getInt(1);
                    insertedIds.add(insertedId);
                    listaInserir.get(n).set${getCampoIdInteiro.nomePriCharMai}(insertedId);
                    n++;
                }
            }
            return insertedIds;
</#if>
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
