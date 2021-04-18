package com.angelo.game.lua;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

public class LuaScriptLoader {
	
	private LuaValue luaGlobals;
	
	public void loadScript(){
		luaGlobals = JsePlatform.standardGlobals();
		//luaGlobals.get("dofile").call(LuaValue.valueOf(TrainFileLoader.trainFolderDir+"/LuaScript.lua"));
		
		callAddResponseToData();
	}
	
	private Object callAddResponseToData() {
		Object safetySystemData = new Object();

		try {
			

			LuaValue luaVals = CoerceJavaToLua.coerce(safetySystemData);

			LuaValue luaResponse = luaGlobals.get("addResponseToData");
			if (!luaResponse.isnil()) {
				luaResponse.call(luaVals);
			} else {
				System.err.println("Lua function not found");
			}
		} catch (LuaError e){
			e.printStackTrace();
		}

		return safetySystemData;
	}
}
