package engine;

import java.util.ArrayList;
import java.util.List;

import imgui.ImGui;
import renderer.Renderer;

public abstract class Scene {

	protected Renderer renderer = new Renderer();
	protected Camera camera;
	private boolean isRunning = false;
	protected List<GameObject> gameObjects = new ArrayList<GameObject>();
	protected GameObject activeGameObject = null;
	
	public Scene() {
		
	}
	
	public void init() {
		
	}
	
	public void start() {
		for(GameObject go: gameObjects) {
			go.start();
			this.renderer.add(go);
		}
		isRunning = true;
	}
	
	public abstract void update(float delta);
	
	public void addGameObjectToScene(GameObject go) {
		if(!isRunning) {
			gameObjects.add(go);
		} else {
			gameObjects.add(go);
			go.start();
			this.renderer.add(go);
		}
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	public void sceneImgui() {
		if(activeGameObject != null) {
			ImGui.begin("Inspector");
			activeGameObject.imgui();
			ImGui.end();
		}
		
		imgui();
	}
	
	public void imgui() {
		
	}
}
