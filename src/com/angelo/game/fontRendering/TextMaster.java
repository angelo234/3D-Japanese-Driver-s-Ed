package com.angelo.game.fontRendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.angelo.game.guis.text.FontType;
import com.angelo.game.guis.text.GUIText;
import com.angelo.game.guis.text.TextMeshData;
import com.angelo.game.renderEngine.VAO;
import com.angelo.game.utils.Loader;

public class TextMaster {

	private static Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private static FontRenderer renderer;
	
	public static void init(){
		renderer = new FontRenderer();
	}
	
	public static void render(){
		renderer.render(texts);
	}
	
	public static void loadText(GUIText text){
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		
		if(text.getVAO() == null){
			VAO vao = Loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
			text.setMeshInfo(vao, data.getVertexCount());
		}
		else{
			text.setVertexCount(data.getVertexCount());
			Loader.updateVBO(text.getVAO().getVerticesVBOID(), data.getVertexPositions());
			Loader.updateVBO(text.getVAO().getUVSVBOID(), data.getTextureCoords());
		}
			
		List<GUIText> textBatch = texts.get(font);
		if(textBatch == null){
			textBatch = new ArrayList<GUIText>();
			texts.put(font, textBatch);					
		}
		textBatch.add(text);
	}
	
	public static void removeText(GUIText text){	
		List<GUIText> textBatch = texts.get(text.getFont());
		if(textBatch != null){
			textBatch.remove(text);
			if(textBatch.isEmpty()){
				texts.remove(text.getFont());
				texts.remove(text.getVAOID());
				texts.remove(text.getPosition());
			}
		}
		//Loader.deleteVAO(text.getVAOID());
	}
	
	public static void cleanUp(){
		renderer.cleanUp();
	}
	
}
