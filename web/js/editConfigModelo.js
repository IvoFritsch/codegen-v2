var aberto = false;

function alternaEspacoConfiguracoesModelo(){
	if (!aberto){
		$('#setaBotaoConfigsModelo').removeClass('glyphicon-chevron-down');
		$('#setaBotaoConfigsModelo').addClass('glyphicon-chevron-up');
		montaTabelasConfigModelo();
	} else {
		$('#setaBotaoConfigsModelo').removeClass('glyphicon-chevron-up');
		$('#setaBotaoConfigsModelo').addClass('glyphicon-chevron-down');
		$('#espacoConfiguracoesModelo').html("");
		aberto = false;
	}
}

function montaTabelasConfigModelo(){
	poeGifLoading("espacoConfiguracoesModelo");poeGifLoading("espacoConfiguracoesModelo");
	$.get('templates/configuracoesModelo.html', function(template) {
		var html = Mustache.to_html(template, 
		{
			defaults:pegaConfigsDefaultDoModelo(modeloEmManutencao)
		});
		$('#espacoConfiguracoesModelo').html(html);
		var listaConfigs = pegaConfigsDefaultDoModelo(modeloEmManutencao);

		var arrayLength = listaConfigs.length;
		for (var i = 0; i < arrayLength; i++) {
			document.getElementById("valorConfigDefault"+listaConfigs[i]+"Model").value = 
				modeloEmManutencao.config.defaults[listaConfigs[i]];
		}
		aberto = true;
	});
}

function removeConfigDefaultModel(config){
	delete modeloEmManutencao.config.defaults[config];
	indicaNaoSalvo();
	montaTabelasConfigModelo();
}

function atualizaConfigDefaultModel(config){
	modeloEmManutencao.config.defaults[config] = 
		document.getElementById('valorConfigDefault'+config+'Model').value;
	indicaNaoSalvo();
}

function fechaCriacaoDefaultModelo(){
	//document.getElementById("nomeNovoCampo").value = "";
	//document.getElementById("tipoNovoCampo").value = "";
	$('#espacoFormNovoDefaultModelo').attr('hidden',true);
	//$("#nomeNovoCampoContainer").removeClass("has-error");
	//$("#tipoNovoCampoContainer").removeClass("has-error");
	$('#botaoNovoDefaultModelo').removeAttr('hidden');
}

function criaNovoDefaultModelo(){
	modeloEmManutencao.config.defaults[document.getElementById('nomeNovoDefaultModelo').value] = "";
	fechaCriacaoDefaultModelo();
	montaTabelasConfigModelo();
	indicaNaoSalvo();
}

function novoDefaultModelo(){
	$('#espacoFormNovoDefaultModelo').removeAttr('hidden');
	$('#botaoNovoDefaultModelo').attr('hidden',true);
}

function pegaConfigsDefaultDoModelo(modelo){
	var listaConfs = [];
	for (var property in modelo.config.defaults) {
		if (modelo.config.defaults.hasOwnProperty(property)) {
			listaConfs.push(property);
		}
	}
	return listaConfs;
}

