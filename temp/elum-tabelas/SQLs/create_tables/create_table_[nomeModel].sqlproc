CREATE CACHED TABLE ${root.model.nomeMin} (
<#list root.model.listaTodosCampos as campo>
	${root.getSnippet("SQL/createCampo", campo)},
</#list>
    PRIMARY KEY (<#list root.model.getListaCamposComAConfigIgualA("isId","true") as campo>${campo.nome}<#sep>, </#sep></#list>),
);