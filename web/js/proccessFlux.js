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

function avancaFluxo(){
	etapaFluxo++;
	exibeEtapaAtualFluxo();
}

function voltaFluxo(){
	etapaFluxo--;
	exibeEtapaAtualFluxo();
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
  	console.log(proccessSpecs.modelos);
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