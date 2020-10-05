package components;

import org.joml.Vector2f;
import org.joml.Vector4f;

import engine.Component;
import engine.Transform;
import imgui.ImGui;
import renderer.Texture;

public class SpriteRenderer extends Component {
	
	Vector4f color;
	private Sprite sprite;
	
	private Transform lastTransform;
	private boolean isDirty = false;
	
	public SpriteRenderer(Vector4f color) {
		this.color = color;
		this.sprite = new Sprite(null);
		this.isDirty = true;
	}
	
	public SpriteRenderer(Sprite sprite) {
		this.sprite = sprite;
		this.color = new Vector4f(1,1,1,1);
		this.isDirty = true;
	}
	
	@Override
	public void start() {
		super.start();
		this.lastTransform = gameObject.transfrom.copy();
	}

	@Override
	public void update(float delta) {
		if(!this.lastTransform.equals(this.gameObject.transfrom)) {
			this.lastTransform = gameObject.transfrom.copy();
			isDirty = true;
		}
	}
	
	public Vector4f getColor() {
		return this.color;
	}
	
	public Texture getTexture() {
		return sprite.getTexture();
	}
	
	public Vector2f[] getTexCoords() {
		return sprite.getTexCoords();
	}
	
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
		this.isDirty = true;
	}
	
	public void setColor(Vector4f color) {
		if(!this.color.equals(color)) {
			this.color.set(color);
			this.isDirty = true;
		}
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}

	@Override
	public void imgui() {
		float[] imColor = new float[] {color.x, color.y, color.z, color.w};
		if(ImGui.colorPicker4("Color Picker", imColor)) {
			this.color.set(imColor[0], imColor[1], imColor[2], imColor[3]);
			this.isDirty = true;
		}
	}
}
