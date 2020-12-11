/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import auxiliar.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;
import main.VersaoInvalidaException;
import org.json.JSONObject;

/**
 * Modelo usado pelo microservidor para fins de edição e armazenamento
 * @author Ivo Fritsch
 */
public class ServerModel {
    
    public final static int VERSAO_ATUAL = 1;
    
    @Expose
    private String nome;
    
    @Expose
    private List<ServerField> listaCampos = new ArrayList<>();
    
    @Expose
    private CodegenModelConfig config;
    
    @Expose
    private int versao;

    public String getNome() {
        return nome;
    }

    public List<ServerField> getListaCampos() {
        return listaCampos;
    }
    
    public static ServerModel fromJson(String json){ 
      ServerModel retorno = new Gson().fromJson(json, ServerModel.class);
      if(retorno.versao > VERSAO_ATUAL){
        throw new VersaoInvalidaException("modelo "+retorno.nome, VERSAO_ATUAL, retorno.versao);
      }
      retorno.versao = VERSAO_ATUAL;
      return retorno;
    }
    
    public static ServerModel fromJson(String json, String nome){
      ServerModel retorno = fromJson(new JSONObject(json).put("nome", nome).toString());
      return retorno;
    }
    
    public String toJson(){
        this.nome = null;
        return Utils.toJsonOnlyExpose(this);
    }
    public String toJson(boolean keepName){
        if(!keepName) this.nome = null;
        return Utils.toJsonOnlyExpose(this);
    }
    
    
}
