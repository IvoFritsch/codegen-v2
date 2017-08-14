document.addEventListener('DOMContentLoaded', function() {
	loadProjeto();
}, false);

var projetoEmManutencao;
var salvo = true;


function loadProjeto() {
	var xmlhttp = new XMLHttpRequest();
	var url = "/api/getProject?project="+getParameterByName("project");
	xmlhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	        projetoEmManutencao = JSON.parse(this.responseText);
			document.getElementById("nomeProjetoEmManutencao").innerHTML = projetoEmManutencao.nome;
			atualizaQuadroDados();
	    }
	};
	xmlhttp.open("GET", url, true);
	xmlhttp.send();
}

function atualizaQuadroDados(){
	poeGifLoading("espacoDadosProjeto");
	projetoEmManutencao.templates.sort(function(a, b){
    if(a < b) return -1;
    if(a > b) return 1;
    return 0;
})
	$.get('templates/dadosProjeto.html', function(template) {
		var html = Mustache.to_html(template, projetoEmManutencao);
		$('#espacoDadosProjeto').html(html);
	});
}

function novoTemplateProjeto(){
	$('#espacoFormNovoTemplateProjeto').removeAttr('hidden');
}

function fechaCriacaoTemplateProjeto(){
	$('#espacoFormNovoTemplateProjeto').attr('hidden',true);
}

function criaNovoTemplateProjeto(){
	projetoEmManutencao.templates.push(
		document.getElementById("nomeNovoTemplateProjeto").value
		);
	atualizaQuadroDados();
}