function confirmaCriacaoProjeto(){
	var xmlhttps = new XMLHttpRequest();
	var url = "/api/novoProjeto";
	xmlhttps.open("POST", url, true);
	xmlhttps.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	var projToSend = newProject();
	projToSend.nome = document.getElementById("nome").value;
	xmlhttps.send(JSON.stringify(projToSend));
	window.location = "/projects.html";
}