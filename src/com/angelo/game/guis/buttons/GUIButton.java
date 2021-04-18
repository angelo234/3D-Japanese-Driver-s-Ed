package com.angelo.game.guis.buttons;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import com.angelo.game.guis.GUITexture;
import com.angelo.game.guis.text.FontType;
import com.angelo.game.guis.text.GUIText;
import com.angelo.game.utils.GlobalVariables;
import com.angelo.game.utils.Maths;

public class GUIButton {

	private GUIText text;
	private GUITexture texture;
	
	private boolean visible;
	private boolean isClickable = true;
	private boolean buttonContainsText;
	
	private GUIButton(Vector2f topLeft, Vector2f bottomRight, String text, int size, FontType fontType, int texture){
		GUITexture guiTexture = new GUITexture(texture, topLeft, bottomRight);
		GUIText guiText = new GUIText(text, size, fontType, 1, true);
		guiText.setPosition(new Vector2f((guiTexture.getTopLeft().getX() + guiTexture.getTopRight().getX()) / 2, (guiTexture.getTopLeft().getY() + guiTexture.getBottomLeft().getY()) / 2));		
		
		this.text = guiText;	
		this.texture = guiTexture;
		
		buttonContainsText = true;
	}
	
	private GUIButton(Vector2f topLeft, Vector2f bottomRight, int texture) {
		GUITexture guiTexture = new GUITexture(texture, topLeft, bottomRight);	
		
		this.texture = guiTexture;
		
		buttonContainsText = false;
	}

	public static GUIButton createButton(int id, Vector2f topLeft, Vector2f bottomRight, String text, int size, FontType fontType, int texture){
		GUIButton button = new GUIButton(topLeft, bottomRight, text, size, fontType, texture);
		GlobalVariables.guiButtons.set(id, button);
		return button;
	}
	
	public static GUIButton createButton(int id, Vector2f topLeft, Vector2f bottomRight, int texture){
		GUIButton button = new GUIButton(topLeft, bottomRight, texture);
		GlobalVariables.guiButtons.set(id, button);
		return button;
	}
	
	public static void removeButton(int id){
		GlobalVariables.guiButtons.remove(id);
	}
	
	public static void setText(int id, String text){
		GlobalVariables.guiButtons.get(id).text.setText(text);
	}
	
	//public static void add
	
	public static boolean isVisible(int id){
		return GlobalVariables.guiButtons.get(id).visible;
	}
	
	public boolean isVisible(){
		return visible;
	}
	
	public void setVisible(boolean visible){
		this.visible = visible;
		if(this.containsText()){
			this.getGUIText().setVisible(visible);
		}
		
		this.getGUITexture().setVisible(visible);
	}
	
	public static void setVisible(int id, boolean visible){
		GUIButton button = GlobalVariables.guiButtons.get(id);
		
		button.visible = visible;
		if(button.containsText()){
			button.getGUIText().setVisible(visible);
		}		
		button.getGUITexture().setVisible(visible);
	}
	
	public static void updateButtons(){
		for(GUIButton button : GlobalVariables.guiButtons){
			if (button != null) {
				if(GUIButton.buttonClickable(GlobalVariables.guiButtons.indexOf(button))){
					if(GUIButton.isCursorInsideButton(GlobalVariables.guiButtons.indexOf(button))){
						button.buttonHovered();
					}
					else{
						button.buttonNotHovered();
					}	
					
					ButtonMaster.removeButton(button);
					if (button.isVisible()) {
						ButtonMaster.loadButton(button);
					}
				}			
			}
		}	
	}
	
	public void buttonHovered(){
		this.getGUITexture().setScaleFactor(0.004f);
		this.getGUIText().setFontSize(1.075f);
	}
	
	public void buttonNotHovered(){
		this.getGUITexture().setScaleFactor(0f);
		this.getGUIText().setFontSize(1f);
	}
	
	public static int getGUIButtonClicked(){
		for(int id = 0;id < GlobalVariables.guiButtons.size();id++){
			if(GlobalVariables.guiButtons.get(id) != null){
				if(GUIButton.buttonClickable(id)){
					if (GUIButton.isCursorInsideButton(id)) {
						return id;
					}
				}	
			}			
		}
		return -1;
	}
	
	public static boolean buttonClickable(int id){
		return GlobalVariables.guiButtons.get(id).buttonClickable();
	}
	
	public static void setClickable(int id, boolean clickable){
		GlobalVariables.guiButtons.get(id).setClickable(clickable);
	}
	
	public void setClickable(boolean clickable){
		this.isClickable = clickable;
	}
	
	public boolean buttonClickable(){
		return isClickable;
	}
	
	public static boolean isCursorInsideButton(int id){
		float normMouseX = Maths.normalizeToRange(0, Display.getWidth(), Mouse.getX());
		float normMouseY = 1-Maths.normalizeToRange(0, Display.getHeight(), Mouse.getY());

		GUIButton button = GlobalVariables.guiButtons.get(id);
	
		if(button != null){
			if(button.isVisible()){
				if(normMouseX > button.getGUITexture().getTopLeft().getX() && normMouseX < button.getGUITexture().getTopRight().getX()){
					if(normMouseY > button.getGUITexture().getTopLeft().getY() && normMouseY < button.getGUITexture().getBottomLeft().getY()){
						return true;
					}
				}
			}
		}	
		return false;
	}
	
	public GUIText getGUIText(){
		return text;
	}
	
	public GUITexture getGUITexture(){
		return texture;
	}
	
	public boolean containsText(){
		return buttonContainsText;
	}
}
