package engine;

import static org.lwjgl.opengl.GL20.*;

import org.joml.Vector2f;
import org.joml.Vector4f;

import components.Sprite;
import components.SpriteRenderer;
import components.SpriteSheet;
import imgui.ImGui;
import renderer.Shader;
import util.AssetPool;

public class LevelEditorScene extends Scene {

	private GameObject obj1;
	
	@Override
	public void init() {
		loadResources();
		
		this.camera = new Camera(new Vector2f());
		
		SpriteSheet sprites = AssetPool.getSpriteSheet("./src/main/resources/assets/images/wall sprite sheet.png");
		
		GameObject go = new GameObject("TESTOBJECT", new Transform(new Vector2f(100, 100), new Vector2f(100, 100)), 0);
		go.addComponent(new SpriteRenderer(sprites.getSprite(2)));
		addGameObjectToScene(go);
		obj1 = new GameObject("TESTOBJECT1", new Transform(new Vector2f(200, 100), new Vector2f(100, 100)), 0);
		obj1.addComponent(new SpriteRenderer(sprites.getSprite(4)));
		addGameObjectToScene(obj1);
		
		this.activeGameObject = obj1;
		
	}
	
	private void loadResources() {
		AssetPool.getShader("./src/main/resources/assets/shaders/default.glsl");
		AssetPool.addSpriteSheet("./src/main/resources/assets/images/wall sprite sheet.png"
				, new SpriteSheet(
						AssetPool.getTexture("./src/main/resources/assets/images/wall sprite sheet.png"), 32, 32, 6, 0));
	}

	
	private int spriteIndex;
	private float spriteFlipTime = 0.2f;
	private float spriteFlipTimeLeft = 0f;
	@Override
	public void update(float delta) {
		obj1.transfrom.position.x += 10*delta;
		if((spriteFlipTimeLeft -= delta) <= 0) {
			spriteFlipTimeLeft = spriteFlipTime;
			spriteIndex = spriteIndex < 5 ? spriteIndex + 1: 0;
			SpriteRenderer renderer = obj1.getComponent(SpriteRenderer.class);
			renderer.setSprite(AssetPool.getSpriteSheet("./src/main/resources/assets/images/wall sprite sheet.png").getSprite(spriteIndex));
		}
		
		for(GameObject go: this.gameObjects) {
			go.update(delta);
		}
		
		this.renderer.render();
	}
	
	@Override
	public void imgui() {
		ImGui.begin("Test Window");
		ImGui.text("this is the test window");
		ImGui.end();
	}

}
