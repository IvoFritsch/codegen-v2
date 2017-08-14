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
	if(templateJaExiste(document.getElementById("nomeNovoTemplateProjeto").value)) return;
	projetoEmManutencao.templates.push(
		document.getElementById("nomeNovoTemplateProjeto").value
		);
	criaNovoTemplateProjetoNoServer();
	atualizaQuadroDados();
}

function templateJaExiste(nome){
	var arrayLength = projetoEmManutencao.templates.length;
	for (var i = 0; i < arrayLength; i++) {
		if(projetoEmManutencao.templates[i] === nome){
			return true;
		}
	}
	return false;
}

function excluiTemplateProjeto(nome){
	var xmlhttps = new XMLHttpRequest();
	var url = "/api/excluiTemplateProjeto";
	xmlhttps.open("POST", url, true);
	xmlhttps.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	var template = newTemplateSpecs();
	template.projeto = projetoEmManutencao.nome;
	template.nome = nome;
	xmlhttps.send(JSON.stringify(template));
	loadProjeto();
	atualizaQuadroDados();
}

function editaTemplateExterno(nome){
	var xmlhttps = new XMLHttpRequest();
	var url = "/api/editaTemplateProjeto";
	xmlhttps.open("POST", url, true);
	xmlhttps.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	var template = newTemplateSpecs();
	template.projeto = projetoEmManutencao.nome;
	template.nome = nome;
	xmlhttps.send(JSON.stringify(template));
}


function criaNovoTemplateProjetoNoServer(){
	var xmlhttps = new XMLHttpRequest();
	var url = "/api/addTemplateProjeto";
	xmlhttps.open("POST", url, true);
	xmlhttps.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	var template = newTemplateSpecs();
	template.projeto = projetoEmManutencao.nome;
	template.nome = document.getElementById("nomeNovoTemplateProjeto").value;
	xmlhttps.send(JSON.stringify(template));
}