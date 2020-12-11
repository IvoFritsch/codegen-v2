package dao.order;

import dao.seletores.queries.Query${root.model.nome};
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import model.${root.model.nome};
import restFramework.QueryMethods;
import restFramework.selecaoPersonalizada.ContadorPassavel;

/**
 * Classe para definir ordenação das consultas ao banco na tabela ${root.model.nome}
 * 
 * @author Haftware S.I.
 */
@SuppressWarnings("unused")
public class ${root.model.nome}OrderBy implements QueryMethods<${root.model.nome}>{

    enum OrderType {
        ASC,DESC, ASC_NULLS_LAST, DESC_NULLS_LAST;
    }

    private Query${root.model.nome} query;
    private boolean usingIndex = false;
    private final Map<String, OrderType> fields = new LinkedHashMap<>();

    public ${root.model.nome}OrderBy() {
    }

    public ${root.model.nome}OrderBy(Query${root.model.nome} query){
        this.query = query;
    }
<#list root.model.listaTodosCampos as campo>

    /**
     * Ordena através do campo ${campo.nome} de maneira crescente.
     *
     * @return Esse próprio ${root.model.nome}OrderBy para ordenar mais campos.
     */
    public ${root.model.nome}OrderBy ${campo.nome}(){
        fields.put("${campo.nome}", OrderType.ASC);
        <#if campo.temAConfigIgualA("buscavelBanco", "true") || campo.temConfig("chaveEstrangeira") || campo.temAConfigIgualA("isId", "true")>this.usingIndex = true;</#if>
        return this;
    }

    /**
     * Ordena através do campo ${campo.nome} de maneira crescente.
     *
     * @return Esse próprio ${root.model.nome}OrderBy para ordenar mais campos.
     */
    public ${root.model.nome}OrderBy ${campo.nome}Asc(){
        fields.put("${campo.nome}", OrderType.ASC);
        <#if campo.temAConfigIgualA("buscavelBanco", "true") || campo.temConfig("chaveEstrangeira") || campo.temAConfigIgualA("isId", "true")>this.usingIndex = true;</#if>
        return this;
    }
    
    /**
     * Ordena através do campo ${campo.nome} de maneira crescente colocando os valores nulos no final da ordenação.
     *
     * @return Esse próprio ${root.model.nome}OrderBy para ordenar mais campos.
     */
    public ${root.model.nome}OrderBy ${campo.nome}AscNullsLast(){
        fields.put("${campo.nome}", OrderType.ASC_NULLS_LAST);
        <#if campo.temAConfigIgualA("buscavelBanco", "true") || campo.temConfig("chaveEstrangeira") || campo.temAConfigIgualA("isId", "true")>this.usingIndex = true;</#if>
        return this;
    }

    /**
     * Ordena através do campo ${campo.nome} de maneira decrescente.
     *
     * @return Esse próprio ${root.model.nome}OrderBy para ordenar mais campos.
     */
    public ${root.model.nome}OrderBy ${campo.nome}Desc(){
        fields.put("${campo.nome}", OrderType.DESC);
        <#if campo.temAConfigIgualA("buscavelBanco", "true") || campo.temConfig("chaveEstrangeira") || campo.temAConfigIgualA("isId", "true")>this.usingIndex = true;</#if>
        return this;
    }

    /**
     * Ordena através do campo ${campo.nome} de maneira decrescente colocando os valores nulos no final da ordenação.
     *
     * @return Esse próprio ${root.model.nome}OrderBy para ordenar mais campos.
     */
    public ${root.model.nome}OrderBy ${campo.nome}DescNullsLast(){
        fields.put("${campo.nome}", OrderType.DESC_NULLS_LAST);
        <#if campo.temAConfigIgualA("buscavelBanco", "true") || campo.temConfig("chaveEstrangeira") || campo.temAConfigIgualA("isId", "true")>this.usingIndex = true;</#if>
        return this;
    }

</#list>

    /**
     * Habilita/desabilita o uso de indices para a ordenação, na maioria das vezes o uso é automatico, mas em alguns casos específicos pode ser necessário desabilitar o indice.
     *
     * @param useIndex Se deve ou não adicionar a clausula "using index"
     * @return Esse próprio ${root.model.nome}OrderBy.
     */
    public ${root.model.nome}OrderBy useIndex(boolean useIndex){
        this.usingIndex = useIndex;
        return this;
    }
    
    public boolean useIndex(){
        return this.usingIndex;
    }

    @Override
    public int count() {
        if(query == null) throw new RuntimeException("Esse ${root.model.nome}OrderBy não está atrelado a nenhuma query, não é possivel chamar esse método");
        return query.count();
    }

    @Override
    public ${root.model.nome} findOne() {
        if(query == null) throw new RuntimeException("Esse ${root.model.nome}OrderBy não está atrelado a nenhuma query, não é possivel chamar esse método");
        return query.findOne();
    }

    @Override
    public ${root.model.nome} findOneOrThrow(String throwError) {
        if(query == null) throw new RuntimeException("Esse ${root.model.nome}OrderBy não está atrelado a nenhuma query, não é possivel chamar esse método");
        return query.findOneOrThrow(throwError);
    }

    @Override
    public List<${root.model.nome}> find() {
        if(query == null) throw new RuntimeException("Esse ${root.model.nome}OrderBy não está atrelado a nenhuma query, não é possivel chamar esse método");
        return query.find();
    }

    @Override
    public List<${root.model.nome}> find(int pageSize, int pageNumber) {
        if(query == null) throw new RuntimeException("Esse ${root.model.nome}OrderBy não está atrelado a nenhuma query, não é possivel chamar esse método");
        return query.find(pageSize, pageNumber);
    }
    
    /**
     * Retorna a cláusula ORDER BY gerada.
     *
     * @return Cláusula ORDER BY gerada.
     */
    public String getClause(){
        if(fields.isEmpty()) return "";
        StringBuilder sb = new StringBuilder(" ORDER BY ");
        ContadorPassavel c = new ContadorPassavel();
        c.cont = 1;
        int qtdFields = fields.size();
        fields.forEach((n,ot) -> {
            sb.append(n);
            switch (ot) {
                case DESC:
                    sb.append(" DESC");
                    break;
                case ASC:
                    sb.append(" ASC");
                    break;
                case ASC_NULLS_LAST:
                    sb.append(" ASC NULLS LAST");
                    break;
                case DESC_NULLS_LAST:
                    sb.append(" DESC NULLS LAST");
                    break;
            }
            if(c.cont < qtdFields) sb.append(", ");
            c.cont++;
        });
        sb.append(" ");
        return sb.toString();
    }

    @Override
    public int findLazy(BiConsumer<${root.model.nome}, Integer> c) {
        return query.findLazy(c);
    }

    @Override
    public int findLazy(Consumer<${root.model.nome}> c) {
        return query.findLazy(c);
    }
}
