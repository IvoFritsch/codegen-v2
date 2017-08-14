loadGlobalConfig();

function loadGlobalConfig() {
	var xmlhttp = new XMLHttpRequest();
	var url = "/api/getGlobalConfig";

	xmlhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	        var myArr = JSON.parse(this.responseText);
	        carregaCamposTela(myArr);
	    }
	};
	xmlhttp.open("GET", url, true);
	xmlhttp.send();
}

function carregaCamposTela(arr){
    document.getElementById("templatesLocation").value = arr.templatesLocation;
    document.getElementById("genOutput").value = arr.genOutput;
    document.getElementById("modelsLocation").value = arr.modelsLocation;
}


function enviaGlobalConfig(){
	var xmlhttps = new XMLHttpRequest();
	var url = "/api/setGlobalConfig";
	xmlhttps.open("POST", url, true);
	xmlhttps.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	xmlhttps.send(JSON.stringify(
		{
			templatesLocation:document.getElementById("templatesLocation").value,
			genOutput:document.getElementById("genOutput").value,
			modelsLocation:document.getElementById("modelsLocation").value
		}
	));
	window.location = "/index.html";
}
