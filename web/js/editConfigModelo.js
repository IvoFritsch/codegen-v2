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
			defaults:pegaConfigsDefaultDoModelo(modeloEmManutencao),
			modelConfigs: pegaConfigsDoModelo(modeloEmManutencao)
		});
		$('#espacoConfiguracoesModelo').html(html);
		var listaConfigs = pegaConfigsDefaultDoModelo(modeloEmManutencao);

		var arrayLength = listaConfigs.length;
		for (var i = 0; i < arrayLength; i++) {
			document.getElementById("valorConfigDefault"+listaConfigs[i]+"Model").value = 
				modeloEmManutencao.config.defaults[listaConfigs[i]];
		}
		listaConfigs = pegaConfigsDoModelo(modeloEmManutencao);

		var arrayLength = listaConfigs.length;
		for (var i = 0; i < arrayLength; i++) {
			document.getElementById("valorConfigConfig"+listaConfigs[i]+"Model").value = 
				modeloEmManutencao.config.modelConfigs[listaConfigs[i]];
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




function removeConfigConfigModel(config){
	delete modeloEmManutencao.config.modelConfigs[config];
	indicaNaoSalvo();
	montaTabelasConfigModelo();
}

function atualizaConfigConfigModel(config){
	modeloEmManutencao.config.modelConfigs[config] = 
		document.getElementById('valorConfigConfig'+config+'Model').value;
	indicaNaoSalvo();
}

function fechaCriacaoConfigModelo(){
	//document.getElementById("nomeNovoCampo").value = "";
	//document.getElementById("tipoNovoCampo").value = "";
	$('#espacoFormNovoConfigModelo').attr('hidden',true);
	//$("#nomeNovoCampoContainer").removeClass("has-error");
	//$("#tipoNovoCampoContainer").removeClass("has-error");
	$('#botaoNovoConfigModelo').removeAttr('hidden');
}

function criaNovoConfigModelo(){
	console.log(modeloEmManutencao.config);
	modeloEmManutencao.config.modelConfigs[document.getElementById('nomeNovoConfigModelo').value] = "";
	fechaCriacaoConfigModelo();
	montaTabelasConfigModelo();
	indicaNaoSalvo();
}

function novoConfigModelo(){
	$('#espacoFormNovoConfigModelo').removeAttr('hidden');
	$('#botaoNovoConfigModelo').attr('hidden',true);
}



function pegaConfigsDoModelo(modelo){
	var listaConfs = [];
	for (var property in modelo.config.modelConfigs) {
		if (modelo.config.modelConfigs.hasOwnProperty(property)) {
			listaConfs.push(property);
		}
	}
	return listaConfs;
}

