<#list root.model.listaTodosCampos as campo>
Campo: ${campo.nome}
	Lista de Configs:
<#list campo.listaConfigsCampo as config>
${root.getSnippet("exibeConfig",campo.getConfigEndpoint(config))}
</#list>
</#list>