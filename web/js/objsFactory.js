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

function newFieldConfigWithSubConfigs(){
	{
		subconfs:{},
		temSubConfs:true
	}
}

function newSimpleFieldConfig(){
	{
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

function newProject(){
	return {
		nome:"",
		models:[],
		templates:[],
		snippets:[]
	}
}


function newFieldConfigFinal(nome){
	return {
		fimDaArvore:true
	}
}