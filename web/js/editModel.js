document.addEventListener('DOMContentLoaded', function() {
	loadModel();
}, false);

var modeloEmManutencao;
var campoEmConfig;
var campoEmEdicao;
var configComSubConfigsEmEdicao = null;


function loadModel() {
	var xmlhttp = new XMLHttpRequest();
	var url = "/api/getModel?model="+getParameterByName("model");

	xmlhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	        modeloEmManutencao = JSON.parse(this.responseText);
			if(!modeloEmManutencao.config.modelConfigs){
				modeloEmManutencao.config.modelConfigs = {};
			}
			document.getElementById("nomeModelEmEdicao").innerHTML = modeloEmManutencao.nome;
			document.getElementById("tituloAba").innerHTML = "Modelo "+modeloEmManutencao.nome+" | Haftware Codegen";
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
                if(modeloEmManutencao.listaCampos.length > 0){
                    $('#botaoSobeCampo'+modeloEmManutencao.listaCampos[0].nome).attr("disabled",true);
                    $('#botaoSobeCampo'+modeloEmManutencao.listaCampos[0].nome).removeClass("btn-info");
                    $('#botaoSobeCampo'+modeloEmManutencao.listaCampos[0].nome).addClass("btn-default");
                    $('#botaoDesceCampo'+modeloEmManutencao.listaCampos[modeloEmManutencao.listaCampos.length-1].nome).attr("disabled",true);
                    $('#botaoDesceCampo'+modeloEmManutencao.listaCampos[modeloEmManutencao.listaCampos.length-1].nome).removeClass("btn-info");
                    $('#botaoDesceCampo'+modeloEmManutencao.listaCampos[modeloEmManutencao.listaCampos.length-1].nome).addClass("btn-default");
                }
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
	fechaEdicaoSubConfigs();
}

function criaNovaConfigCampo(){
	if(!validaCriacaoConfigCampo()) return;
	if(document.getElementById("tipoNovaConfigCampo").value === "comSubConfig"){
		campoEmConfig.config.conf[document.getElementById("nomeNovaConfigCampo").value] = newFieldConfigWithSubConfigs(document.getElementById("nomeNovaConfigCampo").value);
	}else{
		//console.log(newSimpleFieldConfig(document.getElementById("nomeNovaConfigCampo").value));
		campoEmConfig.config.conf[document.getElementById("nomeNovaConfigCampo").value] = newSimpleFieldConfig(document.getElementById("nomeNovaConfigCampo").value);
	}
	indicaNaoSalvo();
	fechaCriacaoConfigCampo();
	exibeConfigsCampoSection();
}

function exibeConfigsCampoSection(){
	poeGifLoading("tableConfigsCampoContent");
	$('#tableSubconfigsConfigContent').html("");
	$('#configsCampoSection').removeAttr('hidden');
	$.get('templates/tableConfigsCampo.html', function(template) {
		var html = Mustache.to_html(template, pegaConfigsDoCampo());
		$('#tableConfigsCampoContent').html(html);
		var listaConfigs = pegaConfigsDoCampo();

		var arrayLength = listaConfigs.length;
		for (var i = 0; i < arrayLength; i++) {
			if(!campoEmConfig.config.conf[listaConfigs[i].nome].temSubConfs){
				document.getElementById("valorConfig"+listaConfigs[i].nome+"Campo").value = 
					campoEmConfig.config.conf[listaConfigs[i].nome].valor;
			}
		}
	});
	if(pegaConfigsDoCampo().length === 0){
		$(".botaoTransferirConfig").css("display","inline");
	} else {
		$(".botaoTransferirConfig").css("display","none");
	}
}

function removeConfigCampo(config){
	delete campoEmConfig.config.conf[config];
	indicaNaoSalvo();
	fechaEdicaoSubConfigs();
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
	$('#tableConfigsCampoContent').html("");
	$('#tableSubconfigsConfigContent').html("");
	$("#configsCampoSection").attr("hidden",true);
	$(".botaoTransferirConfig").css("display","none");

}

function atualizaConfigCampo(config){
	campoEmConfig.config.conf[config].valor = 
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

var subConfigsEmExibicaoPara = "";
function fechaEdicaoSubConfigs(){
	configComSubConfigsEmEdicao = null;
	$('#tableSubconfigsConfigContent').html("");
	$("#btn-editaSubConfigsConfig-"+subConfigsEmExibicaoPara).removeClass("active");
	subConfigsEmExibicaoPara = "";
}

function editaSubconfigsDaConfig(nome){
	fechaCriacaoConfigCampo();
	fechaEdicaoSubConfigs();
	if(nome === subConfigsEmExibicaoPara){
		return;
	}
	$("#btn-editaSubConfigsConfig-"+nome).addClass("active");
	subConfigsEmExibicaoPara = nome;
	configComSubConfigsEmEdicao = campoEmConfig.config.conf[nome];
	
	montaTabelaSubConfigsConfig();
	
}
function montaTabelaSubConfigsConfig(){

	$.get('templates/tableSubconfigsConfig.html', function(template) {
		var html = Mustache.to_html(template, pegaSubconfigsDaConfig(campoEmConfig));
		$('#tableSubconfigsConfigContent').html(html);
		var listaConfigs = pegaSubconfigsDaConfig();
		var arrayLength = listaConfigs.length;
		for (var i = 0; i < arrayLength; i++) {
			document.getElementById("valorSubconfig"+listaConfigs[i]+"Config").value = 
				configComSubConfigsEmEdicao.subconfs[listaConfigs[i]];
		}
	});
}

function atualizaSubconfigConfig(nome){
	configComSubConfigsEmEdicao.subconfs[nome] = document.getElementById("valorSubconfig"+nome+"Config").value;
	indicaNaoSalvo();
}

function removeSubconfigConfig(nome){
	delete configComSubConfigsEmEdicao.subconfs[nome];
	fechaCriacaoSubconfigConfig();
	montaTabelaSubConfigsConfig();
	indicaNaoSalvo();
}

function novaSubconfigConfig(){
	$('#espacoFormNovaSubconfigConfig').removeAttr('hidden');
	$('#botaoNovaSubconfigConfig').attr('hidden',true);
}

function fechaCriacaoSubconfigConfig(){
	$('#botaoNovaSubconfigConfig').removeAttr('hidden');
	$('#espacoFormNovaSubconfigConfig').attr('hidden',true);
}

function criaNovaSubconfigConfig(){
	configComSubConfigsEmEdicao.subconfs[document.getElementById("nomeNovaSubconfigConfig").value] = "";
	fechaCriacaoSubconfigConfig();
	montaTabelaSubConfigsConfig();
	indicaNaoSalvo();
}

function transfereConfigsCampo(nomeCampo){
	campoEmConfig.config = JSON.parse(JSON.stringify(pegaCampoViaNome(nomeCampo).config));
	indicaNaoSalvo();
	fechaCriacaoConfigCampo();
	exibeConfigsCampoSection();
}

function pegaSubconfigsDaConfig(){
	var listaConfs = [];
	for (var property in configComSubConfigsEmEdicao.subconfs) {
		if (configComSubConfigsEmEdicao.subconfs.hasOwnProperty(property)) {
			listaConfs.push(property);
		}
	}
	return listaConfs;
}

function pegaConfigsDoCampo(){
	var listaConfs = [];
	for (var property in campoEmConfig.config.conf) {
		if (campoEmConfig.config.conf.hasOwnProperty(property)) {
			listaConfs.push(campoEmConfig.config.conf[property]);
		}
	}
	return listaConfs;
}

function sobeCampoLista(nomeCampo){
    var index = pegaIndexCampoViaNome(nomeCampo);
    if(index === 0) return;
    swapCampos(index, index-1);
    apagaConfigsCampoSection();
    poeGifLoading("tableCamposContent");
	montaTabelaCampos();
	indicaNaoSalvo();
}

function desceCampoLista(nomeCampo){
    var index = pegaIndexCampoViaNome(nomeCampo);
    if(index >= modeloEmManutencao.listaCampos.length - 1) return;
    swapCampos(index, index+1);
    apagaConfigsCampoSection();
    poeGifLoading("tableCamposContent");
	montaTabelaCampos();
	indicaNaoSalvo();
}

function swapCampos(x,y){
    var b = modeloEmManutencao.listaCampos[y];
    modeloEmManutencao.listaCampos[y] = modeloEmManutencao.listaCampos[x];
    modeloEmManutencao.listaCampos[x] = b;
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

function pegaIndexCampoViaNome(nomeCampo){
	var arrayLength = modeloEmManutencao.listaCampos.length;
	for (var i = 0; i < arrayLength; i++) {
		if(modeloEmManutencao.listaCampos[i].nome === nomeCampo){
			return i;
		}
	}
	return null;
}

