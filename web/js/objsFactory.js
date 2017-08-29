function newModel(){
	return {
		nome:"",
		listaCampos:[],
		config:{
			defaults:{
				
			}
		}
	}
}

function newCampo(){
	return {
		nome:"",
		tipoAsString:"",
		config: {
			conf: {
			}
		}
	}
}

function newFieldConfigWithSubConfigs(nomeConfig){
	return {
		nome:nomeConfig,
		temSubConfs:true,
		subconfs: {
		}
	}
}

function newSimpleFieldConfig(nomeConfig){
	return {
		nome:nomeConfig,
		valor:"",
		temSubConfs:false
	}
}

function newProccessSpecs(){
	return {
		projeto:"",
		modelos:[],
		config:{},
	}
}

function newTemplateSpecs(){
	return {
		projeto:"",
		nome:"",
		nomeAntigo:"",
	}
}

function newProjectSpecs(){
	return {
		nome:"",
		nomeAntigo:"",
		caminho:""
	}
}

function newAssocTipoSpecs(){
	return {
		projeto:"",
		tipoAsString: "",
		tipo: ""
	}
}

function newProject(){
	return {
		nome:"",
		models:[],
		templates:[],
		snippets:[],
		assocTipo:{
		}
	}
}


function newFieldConfigFinal(nome){
	return {
		fimDaArvore:true
	}
}