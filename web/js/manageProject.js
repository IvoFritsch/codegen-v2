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
	loadProjeto();
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



function novoSnippetProjeto(){
	$('#espacoFormNovoSnippetProjeto').removeAttr('hidden');
}

function fechaCriacaoSnippetProjeto(){
	$('#espacoFormNovoSnippetProjeto').attr('hidden',true);
}


function criaNovoSnippetProjeto(){
	if(snippetJaExiste(document.getElementById("nomeNovoSnippetProjeto").value)) return;
	projetoEmManutencao.snippets.push(
		document.getElementById("nomeNovoSnippetProjeto").value
		);
	criaNovoSnippetProjetoNoServer();
	loadProjeto();
	atualizaQuadroDados();
}

function excluiSnippetProjeto(nome){
	var xmlhttps = new XMLHttpRequest();
	var url = "/api/excluiSnippetProjeto";
	xmlhttps.open("POST", url, true);
	xmlhttps.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	var snippet = newTemplateSpecs();
	snippet.projeto = projetoEmManutencao.nome;
	snippet.nome = nome;
	xmlhttps.send(JSON.stringify(snippet));
	loadProjeto();
	atualizaQuadroDados();
}


function criaNovoSnippetProjetoNoServer(){
	var xmlhttps = new XMLHttpRequest();
	var url = "/api/addSnippetProjeto";
	xmlhttps.open("POST", url, true);
	xmlhttps.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	var snippet = newTemplateSpecs();
	snippet.projeto = projetoEmManutencao.nome;
	snippet.nome = document.getElementById("nomeNovoSnippetProjeto").value;
	xmlhttps.send(JSON.stringify(snippet));
}


function snippetJaExiste(nome){
	var arrayLength = projetoEmManutencao.snippets.length;
	for (var i = 0; i < arrayLength; i++) {
		if(projetoEmManutencao.snippets[i] === nome){
			return true;
		}
	}
	return false;
}
