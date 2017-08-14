var etapaFluxo = 0;
function exibeEtapaAtualFluxo(){
	document.getElementById('espacoEtapasFluxo').innerHTML = "<img src='/static/load.gif'/>"
	$.get('templates/proccess-'+etapaFluxo+'.html', function(template) {
		var html = Mustache.to_html(template, null);
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