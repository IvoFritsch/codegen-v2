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
		templates:[]
	}
}


function newFieldConfigFinal(nome){
	return {
		fimDaArvore:true
	}
}