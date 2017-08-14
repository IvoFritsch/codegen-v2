CREATE TABLE ${root.model.nomeMin}s (
<#list root.model.listaTodosCampos as campo>
${root.getSnippet("createCampo",campo)}</#list>
    PRIMARY KEY (${root.model.getCampoComAConfig("isId").nome}),
<#list root.model.getListaCamposComAConfig("chaveEstrangeira") as campo>
    CONSTRAINT FK_${campo.nome} FOREIGN KEY (${campo.nome})
		REFERENCES ${campo.config.getValorConfig("chaveEstrangeira")}(${campo.nome}),
</#list>
)