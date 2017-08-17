function fazRequestGetApi(url){
	$.get(url, function(template) {
		
	});
}

function poeGifLoading(id){
	document.getElementById(id).innerHTML = "<img src='/static/load.gif'/>"
}

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function editaTemplateExterno(nome, projeto){
	var xmlhttps = new XMLHttpRequest();
	var url = "/api/editaTemplateProjeto";
	xmlhttps.open("POST", url, true);
	xmlhttps.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	var template = newTemplateSpecs();
	template.projeto = projeto;
	template.nome = nome;
	xmlhttps.send(JSON.stringify(template));
}