package engine;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {
	private int width;
	private int height;
	private String title;
	private long glfwWindow;
	
	private static Window window = null;
	
	private static Scene currentScene;
	private ImGuiLayer imGuiLayer;
	
	private Window() {
		this.width = 1920;
		this.height = 1080;
		this.title = "Game";
	}
	
	public static void changeScene(int newScene) {
		switch(newScene) {
		case 0:
			currentScene = new LevelEditorScene();
			break;
		case 1:
			currentScene = new LevelScene();
			break;
		default:
			assert false : "Unknown scene [" + newScene + "]";
		}
		currentScene.init();
		currentScene.start();
	}
	
	public static Window get() {
		if(Window.window == null) {
			Window.window = new Window();
		}
		return window;
	}
	
	public static Scene getScene() {
		return get().currentScene;
	}
	
	public void run() {
		System.out.println("Hello LWJGL" + Version.getVersion());
		
		init();
		loop();
		
		// Free the memory
		glfwFreeCallbacks(glfwWindow);
		glfwDestroyWindow(glfwWindow);
		
		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null);
		
	}
	
	public void init() {
		// Setup an error callback
		GLFWErrorCallback.createPrint(System.err).set();
		
		// Initialize GLFW
		if(!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		// Configure GLFW
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
		
		// Create the window
		glfwWindow = glfwCreateWindow(this.width, this.height, title, 0, 0);
		if(glfwWindow == 0) {
			throw new IllegalStateException("Failed to create the GLFW window");
		}
		
		// Setup mouse listener callbacks
		glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
		glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
		glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
		glfwSetWindowSizeCallback(glfwWindow, (w,newWidth,newHeight) -> {
			Window.setWidth(newWidth);
			Window.setHeight(newHeight);
		});
		
		// Setup key listener callback
		glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
		
		// Make the OpenGL context current
		glfwMakeContextCurrent(glfwWindow);
		// Enable v-sync
		glfwSwapInterval(1);
		
		// Make the window visible
		glfwShowWindow(glfwWindow);
		
		GL.createCapabilities();
		
		this.imGuiLayer = new ImGuiLayer(glfwWindow);
		try {
			this.imGuiLayer.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
		
		changeScene(0);
	}
	
	public void loop() {
		float beginTime = (float)glfwGetTime();
		float endTime;
		float dt = -1.0f;
		
		while(!glfwWindowShouldClose(glfwWindow)) {
			// Poll events
			glfwPollEvents();
			
			glClearColor(0.2f, 0.2f, 0.2f, 1);
			glClear(GL_COLOR_BUFFER_BIT);
			
			if(dt >= 0)
				currentScene.update(dt);
			
			this.imGuiLayer.update(dt, currentScene);
			
			glfwSwapBuffers(glfwWindow);
			
			endTime = (float)glfwGetTime();
			dt = endTime - beginTime;
			beginTime = endTime;
		}
		
	}
	
	public static int getWidth() {
		return get().width;
	}
	
	public static int getHeight() {
		return get().height;
	}
	
	public static void setWidth(int width) {
		get().width = width;
	}
	
	public static void setHeight(int height) {
		get().height = height;
	}
}
