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

function selecionaProjetoImportar(){
	var xmlhttp = new XMLHttpRequest();
	var url = "/api/chooseProjectFile";

	xmlhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	    	if(this.responseText !== ""){
	    		document.getElementById("caminhoCgp").value = this.responseText;
	    	}
	    }
	};
	xmlhttp.open("GET", url, true);
	xmlhttp.send();
}

function selecionaDestinoNovoProjeto(){
	var xmlhttp = new XMLHttpRequest();
	var url = "/api/chooseNewProjectFolder";

	xmlhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	    	if(this.responseText !== ""){
	    		document.getElementById("caminhoProjeto").value = this.responseText;
	    	}
	    }
	};
	xmlhttp.open("GET", url, true);
	xmlhttp.send();
}

function confirmaCriacaoProjeto(){
	var xmlhttps = new XMLHttpRequest();
	var url = "/api/createProject";
	xmlhttps.open("POST", url, true);
	xmlhttps.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	var projToSend = newProjectSpecs();
	projToSend.nome = document.getElementById("nome").value;
	projToSend.caminho = document.getElementById("caminhoProjeto").value;
	xmlhttps.send(JSON.stringify(projToSend));
	window.location = "/projects.html";
}

function confirmaImportarProjeto(){
	var xmlhttps = new XMLHttpRequest();
	var url = "/api/importProject";
	xmlhttps.open("POST", url, true);
	xmlhttps.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	var projToSend = newProjectSpecs();
	projToSend.caminho = document.getElementById("caminhoCgp").value;
	xmlhttps.send(JSON.stringify(projToSend));
	window.location = "/projects.html";
}