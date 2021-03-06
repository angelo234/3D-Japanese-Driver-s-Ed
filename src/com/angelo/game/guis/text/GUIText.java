package com.angelo.game.guis.text;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.angelo.game.fontRendering.TextMaster;
import com.angelo.game.renderEngine.VAO;
import com.angelo.game.utils.GlobalVariables;

/**
 * Represents a piece of text in the game.
 * 
 * @author Karl
 *
 */
public class GUIText {

	private String textString;
	private float fontSize;

	private VAO vao;
	private int vertexCount;
	private Vector3f color = new Vector3f(0f, 0f, 0f);

	private Vector2f position;
	private float lineMaxSize;
	private int numberOfLines;

	private FontType font;

	private boolean centerText = false;
	private boolean visible = false;

	/**
	 * Creates a new text, loads the text's quads into a VAO, and adds the text
	 * to the screen.
	 * 
	 * @param text
	 *            - the text.
	 * @param fontSize
	 *            - the font size of the text, where a font size of 1 is the
	 *            default size.
	 * @param font
	 *            - the font that this text should use.
	 * @param position
	 *            - the position on the screen where the top left corner of the
	 *            text should be rendered. The top left corner of the screen is
	 *            (0, 0) and the bottom right is (1, 1).
	 * @param maxLineLength
	 *            - basically the width of the virtual page in terms of screen
	 *            width (1 is full screen width, 0.5 is half the width of the
	 *            screen, etc.) Text cannot go off the edge of the page, so if
	 *            the text is longer than this length it will go onto the next
	 *            line. When text is centered it is centered into the middle of
	 *            the line, based on this line length value.
	 * @param centered
	 *            - whether the text should be centered or not.
	 */
	public GUIText(String text, float fontSize, FontType font, Vector2f position, float maxLineLength, boolean centered) {
		this.textString = text;
		this.fontSize = fontSize;
		this.font = font;
		this.position = position;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;
		// load text
		TextMaster.loadText(this);		
	}
	
	/**Only used in creating GUIButton*/
	public GUIText(String text, float fontSize, FontType font, float maxLineLength, boolean centered) {
		this.textString = text;
		this.fontSize = fontSize;
		this.font = font;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;

		// load text
		//ButtonMaster.loadText(this);		
	}
	
	public static GUIText createText(int id, String text, float fontSize, FontType font, Vector2f position, float maxLineLength, boolean centered){
		GlobalVariables.guiTexts.set(id, new GUIText(text,fontSize,font,position,maxLineLength,centered));
		
		return GlobalVariables.guiTexts.get(id);
	}
	
	public static void setText(int id, String text){
		GlobalVariables.guiTexts.get(id).textString = text;
	}
	
	public void setText(String text){
		this.textString = text;
	}

	protected static void removeText(int id){
		TextMaster.removeText(GlobalVariables.guiTexts.get(id));
	}
	
	public static void deleteText(int id){
		TextMaster.removeText(GlobalVariables.guiTexts.get(id));
		GlobalVariables.guiTexts.remove(id);
	}
	
	public static void update(int id){
		if(GlobalVariables.guiTexts.get(id).isVisible()){
			TextMaster.loadText(GlobalVariables.guiTexts.get(id));
		}		
	}

	public static void updateTexts(){
		for(GUIText text : GlobalVariables.guiTexts){
			if(text != null){
				TextMaster.removeText(text);
				if(text.isVisible()){
					TextMaster.loadText(text);					
				}
			}
		}	
	}
	
	public static GUIText getGUIText(int id){
		return GlobalVariables.guiTexts.get(id);
	}
	
	/**
	 * @return The font used by this text.
	 */
	public FontType getFont() {
		return font;
	}

	/**
	 * Set the color of the text.
	 * 
	 * @param r
	 *            - red value, between 0 and 1.
	 * @param g
	 *            - green value, between 0 and 1.
	 * @param b
	 *            - blue value, between 0 and 1.
	 */
	public void setColor(float r, float g, float b) {
		color.set(r, g, b);
	}

	public static void setColor(int id, float r, float g, float b){
		GlobalVariables.guiTexts.get(id).setColor(r, g, b);
	}
	
	/**
	 * @return the color of the text.
	 */
	public Vector3f getColor() {
		return color;
	}

	/**
	 * @return The number of lines of text. This is determined when the text is
	 *         loaded, based on the length of the text and the max line length
	 *         that is set.
	 */
	public int getNumberOfLines() {
		return numberOfLines;
	}

	/**
	 * @return The position of the top-left corner of the text in screen-space.
	 *         (0, 0) is the top left corner of the screen, (1, 1) is the bottom
	 *         right.
	 */
	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position){
		this.position = position;
	}
	
	/**
	 * @return the ID of the text's VAO, which contains all the vertex data for
	 *         the quads on which the text will be rendered.
	 */
	public int getVAOID(){
		return vao.getID();
	}
		
	public VAO getVAO() {
		return vao;
	}

	/**
	 * Set the VAO and vertex count for this text.
	 * 
	 * @param vao
	 *            - the VAO containing all the vertex data for the quads on
	 *            which the text will be rendered.
	 * @param verticesCount
	 *            - the total number of vertices in all of the quads.
	 */
	public void setMeshInfo(VAO vao, int verticesCount) {
		this.vao = vao;
		this.vertexCount = verticesCount;
	}

	/**
	 * @return The total number of vertices of all the text's quads.
	 */
	public int getVertexCount() {
		return this.vertexCount;
	}
	
	public void setVertexCount(int vertexCount) {
		this.vertexCount = vertexCount;
	}

	/**
	 * @return the font size of the text (a font size of 1 is normal).
	 */
	public float getFontSize() {
		return fontSize;
	}
	
	public void setFontSize(float fontSize){
		this.fontSize = fontSize;
	}

	/**
	 * Sets the number of lines that this text covers (method used only in
	 * loading).
	 * 
	 * @param number
	 */
	protected void setNumberOfLines(int number) {
		this.numberOfLines = number;
	}

	/**
	 * @return {@code true} if the text should be centered.
	 */
	protected boolean isCentered() {
		return centerText;
	}

	/**
	 * @return The maximum length of a line of this text.
	 */
	protected float getMaxLineSize() {
		return lineMaxSize;
	}

	/**
	 * @return The string of text.
	 */
	public String getTextString() {
		return textString;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public static void setVisible(int id, boolean visible) {
		GlobalVariables.guiTexts.get(id).setVisible(visible);
	}
	
}
