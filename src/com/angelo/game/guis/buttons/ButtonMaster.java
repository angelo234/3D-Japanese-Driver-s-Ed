package com.angelo.game.guis.buttons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.angelo.game.guis.GUIRenderer;
import com.angelo.game.guis.GUITexture;
import com.angelo.game.guis.text.FontType;
import com.angelo.game.guis.text.GUIText;
import com.angelo.game.guis.text.TextMeshData;
import com.angelo.game.renderEngine.VAO;
import com.angelo.game.utils.Loader;

public class ButtonMaster {
	
	private static Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private static List<GUITexture> guiTextures = new ArrayList<GUITexture>();
	private static FontRenderer renderer;
	private static GUIRenderer guiRenderer;
	
	public static void init(GUIRenderer guiRenderer){
		ButtonMaster.guiRenderer = guiRenderer;
		renderer = new FontRenderer();
	}
	
	public static void render(){
		guiRenderer.render(guiTextures);
		renderer.render(texts);
	}
	
	public static void loadButton(GUIButton button){
		if(button.containsText()){
			ButtonMaster.loadText(button.getGUIText());
		}
		guiTextures.add(button.getGUITexture());
	}
	
	public static void removeButton(GUIButton button){
		if(button.containsText()){
			ButtonMaster.removeText(button.getGUIText());
		}
		guiTextures.remove(button.getGUITexture());
	}
	
	public static void cleanUp(){
		renderer.cleanUp();
	}
	
	private static void loadText(GUIText text){
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
	
	private static void removeText(GUIText text){	
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
	
}
