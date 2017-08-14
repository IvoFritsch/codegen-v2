function confirmaCriacaoModel(){
	var xmlhttps = new XMLHttpRequest();
	var url = "/api/novoModel";
	xmlhttps.open("POST", url, true);
	xmlhttps.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	var modelToSend = newModel();
	modelToSend.nome = document.getElementById("nome").value;
	xmlhttps.send(JSON.stringify(modelToSend));
	window.location = "/index.html";
}