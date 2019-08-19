<#list root.model.getListaCamposComAConfig("chaveEstrangeira") as campo>
<#-- Testa se o modelo existe, para que não faça referencia a uma tabela ainda não criada -->
<#if root.getOutroModel(campo.config.getValorConfig("chaveEstrangeira"))??>
ALTER TABLE ${root.model.nomeMin}
ADD FOREIGN KEY (${campo.nome}) 
REFERENCES ${root.modifyString(campo.config.getValorConfig("chaveEstrangeira")).toLowerCase()}(
		${root.getOutroModel(campo.config.getValorConfig("chaveEstrangeira")).getCampoComAConfig("isId").nome}) ON DELETE CASCADE;
</#if>
</#list>
<#list root.model.getListaCamposComAConfigIgualA("buscavelBanco", "true") as campo>
CREATE INDEX IF NOT EXISTS IDX_${root.model.nome}_${campo.nome} ON  ${root.model.nomeMin} (${campo.nome});
</#list>