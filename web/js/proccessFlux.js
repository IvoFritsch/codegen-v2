var etapaFluxo = 0;
var proccessSpecs = newProccessSpecs();

function exibeEtapaAtualFluxo(){
	document.getElementById('espacoEtapasFluxo').innerHTML = "<img src='/static/load.gif'/>"
	$.get('templates/proccess-'+etapaFluxo+'.html', function(template) {
		var html = Mustache.to_html(template, {
			configsList:pegaConfigsGeracao()
		});
		$('#espacoEtapasFluxo').html(html);
	});
}

document.addEventListener('DOMContentLoaded', function() {
	exibeEtapaAtualFluxo();
}, false);

function comandaProcessar(){
	avancaFluxo();
	proccessSpecs.projeto = '${root.projeto.nome}';
	var xmlhttps = new XMLHttpRequest();
	var url = "/api/processaTemplate";
	xmlhttps.open("POST", url, true);
	xmlhttps.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	xmlhttps.onreadystatechange = function(data) {
	    if (this.readyState == 4 && this.status == 200) {
	    	//document.getElementById("espacoResultadoProccess").innerHTML = data.currentTarget.response;
			var result = JSON.parse(data.currentTarget.response);
			exibeResultadoModelos(result);
	    }
	};
	xmlhttps.send(JSON.stringify(proccessSpecs));
}

function novoModelObjResultados(){
	return {
		nome:"",
		ok:true,
		templates:[]
	};
}

function novoTemplateObjResultados(){
	return {
		nome:"",
		ok:true,
		mensagens:[]
	};
}

function montaObjetoResultados(proccessLog){
	
	var saida = {
		results:[],
		ok:true
	};
	var models = pegaModelsLog(proccessLog);
	for(var m in models){
		var modelInserir = novoModelObjResultados();
		modelInserir.nome = models[m];
		
		var templates = pegaTemplatesModelLog(proccessLog.mensagens[modelInserir.nome]);
		
		for(var t in templates){
			var templateInserir = novoTemplateObjResultados();
			templateInserir.nome = templates[t];
			templateInserir.mensagens = proccessLog.mensagens[modelInserir.nome][templateInserir.nome];
			if(templateInserir.mensagens.length > 0) {
				for(var m in templateInserir.mensagens){
					templateInserir.mensagens[m] = templateInserir.mensagens[m].replace(/\n/g, "<br/>");
				}
				templateInserir.ok = false;
				modelInserir.ok = false;
				saida.ok = false;
			}
			modelInserir.templates.push(templateInserir);
		}
		saida.results.push(modelInserir);
	}
	return saida;
	
}


function exibeResultadoModelos(result){
	$.get('templates/proccessResults.html', function(template) {
		var html = Mustache.to_html(template, 
			montaObjetoResultados(result)
		);
		$('#espacoResultadoProccess').html(html);
		$("#tituloProcessando").html("Resultado do processamento:");
	});
}

function avancaFluxo(){
	etapaFluxo++;
	exibeEtapaAtualFluxo();
}

function voltaFluxo(){
	etapaFluxo--;
	exibeEtapaAtualFluxo();
}

function pegaModelsLog(log){
	var listaConfs = [];
	for (var property in log.mensagens) {
		if (log.mensagens.hasOwnProperty(property)) {
			listaConfs.push(property);
		}
	}
	return listaConfs;
}

function pegaTemplatesModelLog(model){
	var listaConfs = [];
	for (var property in model) {
		if (model.hasOwnProperty(property)) {
			listaConfs.push(property);
		}
	}
	return listaConfs;
}

function pegaConfigsGeracao(){
	var listaConfs = [];
	for (var property in proccessSpecs.config) {
		if (proccessSpecs.config.hasOwnProperty(property)) {
			listaConfs.push(property);
		}
	}
	return listaConfs;
}

function fechaCriacaoConfiguracaoGeracao(){
	$("#espacoFormNovaConfiguracaoGeracao").attr('hidden',true);
}

function novaConfiguracaoGeracao(){
	$("#espacoFormNovaConfiguracaoGeracao").removeAttr('hidden');
}

function criaNovaConfiguracaoGeracao(){
	proccessSpecs.config[document.getElementById("nomeNovaConfiguracaoGeracao").value] = "";
	exibeEtapaAtualFluxo();
}

function atualizaConfigGeracao(nome){
	proccessSpecs.config[nome] = document.getElementById("valorConfigGeracao"+nome).value;
}

function removeConfigGeracao(nome){
	delete proccessSpecs.config[nome];
	exibeEtapaAtualFluxo();
}

function coletaModelosSelecionados(){
  	proccessSpecs.modelos = getCheckedBoxes("processaModelo");
	if(proccessSpecs.modelos === null) {
		$("#erroProccess0").html("Selecione ao menos um modelo");
		return;
	}
	avancaFluxo();
}

function deveProcessarModelo(nome){
	if(proccessSpecs.modelos == null) return false;
  	for (var i = proccessSpecs.modelos.length - 1; i >= 0; i--) {
  		if(proccessSpecs.modelos[i] === nome){
  			return true;
  		}
  	}
  	return false;
}

function getCheckedBoxes(chkboxName) {
  var checkboxes = document.getElementsByName(chkboxName);
  var checkboxesChecked = [];
  // loop over them all
  for (var i=0; i<checkboxes.length; i++) {
     // And stick the checked ones onto an array...
     if (checkboxes[i].checked) {
        checkboxesChecked.push(checkboxes[i].value);
     }
  }
  // Return the array if it is non-empty, or null
  return checkboxesChecked.length > 0 ? checkboxesChecked : null;
}