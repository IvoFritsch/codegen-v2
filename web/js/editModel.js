document.addEventListener('DOMContentLoaded', function() {
	loadModel();
}, false);

var modeloEmManutencao;
var campoEmConfig;
var campoEmEdicao;


function loadModel() {
	var xmlhttp = new XMLHttpRequest();
	var url = "/api/getModel?model="+getParameterByName("model");

	xmlhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	        modeloEmManutencao = JSON.parse(this.responseText);
			document.getElementById("nomeModelEmEdicao").innerHTML = modeloEmManutencao.nome;
			montaTabelaCampos();
	    }
	};
	xmlhttp.open("GET", url, true);
	xmlhttp.send();
}

function indicaNaoSalvo(){
	$('#asteriscoNaoSalvo').removeAttr('hidden');
	$('#nomeModelEmEdicao').addClass("text-danger");
}


function indicaSalvo(){
	$('#asteriscoNaoSalvo').attr('hidden',true);
	$('#nomeModelEmEdicao').removeClass("text-danger");
}

function montaTabelaCampos(){
	poeGifLoading("tableCamposContent");
	$.get('templates/tableCamposModel.html', function(template) {
		var html = Mustache.to_html(template, modeloEmManutencao);
		$('#tableCamposContent').html(html);
		
	});
}

function preparaEdicaoCampo(nomeCampo){
	campoEmEdicao = pegaCampoViaNome(nomeCampo);
	document.getElementById("nomeCampoEdicao").value = campoEmEdicao.nome;
	document.getElementById("tipoCampoEdicao").value = campoEmEdicao.tipoAsString;
	apagaConfigsCampoSection();
	fechaCriacaoCampo();
	$('#espacoFormEdicaoCampo').removeAttr('hidden');
	window.location.hash = "espacoFormEdicaoCampo";
}

function fechaEdicaoDadosCampo(){
	document.getElementById("nomeCampoEdicao").value = "";
	document.getElementById("tipoCampoEdicao").value = "";
	$('#espacoFormEdicaoCampo').attr('hidden',true);
	$("#nomeCampoEdicaoContainer").removeClass("has-error");
	$("#tipoCampoEdicaoContainer").removeClass("has-error");
}

function editaDadosCampo(){
	if(!validaEdicaoDadosCampo()) return;

	campoEmEdicao.nome = document.getElementById("nomeCampoEdicao").value;
	campoEmEdicao.tipoAsString = document.getElementById("tipoCampoEdicao").value;
	fechaEdicaoDadosCampo();
	montaTabelaCampos();
	indicaNaoSalvo();
}

function fechaCriacaoCampo(){

	document.getElementById("nomeNovoCampo").value = "";
	document.getElementById("tipoNovoCampo").value = "";
	$('#espacoFormNovoCampo').attr('hidden',true);
	$("#nomeNovoCampoContainer").removeClass("has-error");
	$("#tipoNovoCampoContainer").removeClass("has-error");
	$('#botaoNovoCampoModel').removeAttr('hidden');
}

function validaEdicaoDadosCampo(){
	$("#nomeCampoEdicaoContainer").removeClass("has-error");
	$("#tipoCampoEdicaoContainer").removeClass("has-error");
	var retorno = true;
	if (document.getElementById("nomeCampoEdicao").value == "") {
		$("#nomeCampoEdicaoContainer").addClass("has-error");
		retorno = false;
	}
	if (document.getElementById("tipoCampoEdicao").value == "") {
		$("#tipoCampoEdicaoContainer").addClass("has-error");
		retorno = false;
	}
	return retorno;
}

function validaCriacaoCampo(){
	$("#nomeNovoCampoContainer").removeClass("has-error");
	$("#tipoNovoCampoContainer").removeClass("has-error");
	$("#erroNomeNovoCampo").html("");
	$("#erroTipoNovoCampo").html("");
	var retorno = true;
	if (document.getElementById("nomeNovoCampo").value == "") {
		$("#nomeNovoCampoContainer").addClass("has-error");
		$("#erroNomeNovoCampo").html("O nome do campo é obrigatório");
		retorno = false;
	}
	if(pegaCampoViaNome(document.getElementById("nomeNovoCampo").value)!=null){
		$("#nomeNovoCampoContainer").addClass("has-error");
		$("#erroNomeNovoCampo").html("Já existe um campo com esse nome");
		retorno = false;
	}
	if (document.getElementById("tipoNovoCampo").value == "") {
		$("#tipoNovoCampoContainer").addClass("has-error");
		$("#erroTipoNovoCampo").html("O tipo do campo é obrigatório");
		retorno = false;
	}
	return retorno;
}

function removeCampoModel(nomeCampo){
	apagaConfigsCampoSection();
	fechaEdicaoDadosCampo();
	var arrayLength = modeloEmManutencao.listaCampos.length;
	for (var i = 0; i < arrayLength; i++) {
		if(modeloEmManutencao.listaCampos[i].nome === nomeCampo){
			modeloEmManutencao.listaCampos.splice(i,1);
			break;
		}
	}
	montaTabelaCampos();
	indicaNaoSalvo();
}

function novoCampoModel(){
	apagaConfigsCampoSection();
	fechaEdicaoDadosCampo();
	$('#espacoFormNovoCampo').removeAttr('hidden');
	$('#botaoNovoCampoModel').attr('hidden',true);
}

function criaNovoCampoModel(){
	if(!validaCriacaoCampo()) return;
	var campoCriar = newCampo();
	campoCriar.nome = document.getElementById("nomeNovoCampo").value;
	campoCriar.tipoAsString = document.getElementById("tipoNovoCampo").value;
	modeloEmManutencao.listaCampos.push(campoCriar);
	fechaCriacaoCampo();
	montaTabelaCampos();
	indicaNaoSalvo();
}


function fechaCriacaoConfigCampo(){
	//document.getElementById("nomeNovoCampo").value = "";
	//document.getElementById("tipoNovoCampo").value = "";
	$('#espacoFormNovaConfigCampo').attr('hidden',true);
	//$("#nomeNovoCampoContainer").removeClass("has-error");
	//$("#tipoNovoCampoContainer").removeClass("has-error");
	$('#botaoNovaConfigCampo').removeAttr('hidden');
}

function novaConfigCampo(){
	$('#espacoFormNovaConfigCampo').removeAttr('hidden');
	$('#botaoNovaConfigCampo').attr('hidden',true);
}

function criaNovaConfigCampo(){
	if(!validaCriacaoConfigCampo()) return;
	campoEmConfig.config.conf[document.getElementById("nomeNovaConfigCampo").value] = "";
	indicaNaoSalvo();
	fechaCriacaoConfigCampo();
	exibeConfigsCampoSection();
}

function exibeConfigsCampoSection(){
	poeGifLoading("tableConfigsCampoContent");
	$('#configsCampoSection').removeAttr('hidden');
	$.get('templates/tableConfigsCampo.html', function(template) {
		var html = Mustache.to_html(template, pegaConfigsDoCampo(campoEmConfig));
		$('#tableConfigsCampoContent').html(html);
		var listaConfigs = pegaConfigsDoCampo();

		var arrayLength = listaConfigs.length;
		for (var i = 0; i < arrayLength; i++) {
			document.getElementById("valorConfig"+listaConfigs[i]+"Campo").value = 
				campoEmConfig.config.conf[listaConfigs[i]];
		}
	});
}

function removeConfigCampo(config){
	delete campoEmConfig.config.conf[config];
	indicaNaoSalvo();
	exibeConfigsCampoSection();
}

function salvarModel(){
	var xmlhttps = new XMLHttpRequest();
	var url = "/api/setModel";
	xmlhttps.open("POST", url, true);
	xmlhttps.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	xmlhttps.send(JSON.stringify(modeloEmManutencao));
	indicaSalvo();
}

function apagaConfigsCampoSection(){
	fechaCriacaoConfigCampo();
	$('#configsCampoSection').attr('hidden',true);
}

function atualizaConfigCampo(config){
	campoEmConfig.config.conf[config] = 
		document.getElementById("valorConfig"+config+"Campo").value;
	indicaNaoSalvo();
}

function preparaCampoConfig(nomeCampo){
	campoEmConfig = pegaCampoViaNome(nomeCampo);
	exibeConfigsCampoSection();
	
	
	$("#nomeCampoConfig").html(campoEmConfig.nome);
	
	window.location.hash = "tableConfigsCampoContent";
}

function validaCriacaoConfigCampo(){
	$("#nomeNovaConfigCampo").removeClass("has-error");
	var retorno = true;
	if (document.getElementById("nomeNovaConfigCampo").value == "") {
		$("#nomeNovoCampoContainer").addClass("has-error");
		retorno = false;
	}
	return retorno;
}

function pegaConfigsDoCampo(){
	var listaConfs = [];
	for (var property in campoEmConfig.config.conf) {
		if (campoEmConfig.config.conf.hasOwnProperty(property)) {
			listaConfs.push(property);
		}
	}
	return listaConfs;
}

function pegaCampoViaNome(nomeCampo){
	var arrayLength = modeloEmManutencao.listaCampos.length;
	for (var i = 0; i < arrayLength; i++) {
		if(modeloEmManutencao.listaCampos[i].nome === nomeCampo){
			return modeloEmManutencao.listaCampos[i];
		}
	}
	return null;
}
