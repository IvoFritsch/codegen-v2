CREATE TABLE ${root.model.nomeMin}s (
<#list root.model.listaTodosCampos as campo>
SNIPPET
</#list>
    PRIMARY KEY (${root.model.getCampoComAConfig("isId").nome}),
<#list root.model.getListaCamposComAConfig("chave_estrangeira") as campo>
    CONSTRAINT FK_${campo.nome} FOREIGN KEY (${campo.nome})
		REFERENCES ${campo.config.getValorConfig("chaveEstrangeira")}(${campo.nome}),
</#list>
)