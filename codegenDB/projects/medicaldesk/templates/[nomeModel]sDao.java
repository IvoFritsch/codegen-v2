Nome do modelo: ${root.model.nome}  ---  Config: ${root.getConfig("teste")}

Campos:
<#list root.model.listaTodosCampos as campo>
<#if campo.config.get("form") == root.getConfig("form")>
	${campo.nome}
</#if>
</#list>