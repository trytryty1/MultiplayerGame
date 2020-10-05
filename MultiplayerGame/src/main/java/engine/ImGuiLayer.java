package engine;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL32.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL32.GL_TRUE;
import static org.lwjgl.opengl.GL32.glClear;
import static org.lwjgl.opengl.GL32.glClearColor;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGui;
import imgui.ImGuiFreeType;
import imgui.ImGuiIO;
import imgui.ImGuiStyle;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.IntBuffer;
import java.util.Objects;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL32.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL32.GL_TRUE;
import static org.lwjgl.opengl.GL32.glClear;
import static org.lwjgl.opengl.GL32.glClearColor;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGui;
import imgui.ImGuiFreeType;
import imgui.ImGuiIO;
import imgui.ImGuiStyle;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.IntBuffer;
import java.util.Objects;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

public class ImGuiLayer {

	private long glfwWindow;

	// LWJGL3 renderer (SHOULD be initialized)
	private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

	// LWJGJ3 window backend
	private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();

	// User UI to render

	public ImGuiLayer(long glfwWindow) {
		this.glfwWindow = glfwWindow;
	}

	public void init() throws Exception {
		setupImGui();
		// Method initializes GLFW backend.
		// This method SHOULD be called after you've setup GLFW.
		// ImGui context should be created as well.
		imGuiGlfw.init(glfwWindow, true);
		// Method initializes LWJGL3 renderer.
		// This method SHOULD be called after you've initialized your ImGui
		// configuration (fonts and so on).
		// ImGui context should be created as well.
		imGuiGl3.init("#version 330 core");

	}

	// Initialize Dear ImGui.
	public void setupImGui() {
		// IMPORTANT!!
		// This line is critical for Dear ImGui to work.
		ImGui.createContext();

		// ------------------------------------------------------------
		// Initialize ImGuiIO config
		final ImGuiIO io = ImGui.getIO();

		io.setIniFilename("imgui.ini"); // We don't want to save .ini file
		io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Enable Keyboard Controls
		io.addConfigFlags(ImGuiConfigFlags.DockingEnable); // Enable Docking
		io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable); // Enable Multi-Viewport / Platform Windows
		io.setConfigViewportsNoTaskBarIcon(true);

		// ------------------------------------------------------------
		// Fonts configuration
		// Read: https://raw.githubusercontent.com/ocornut/imgui/master/docs/FONTS.txt

		final ImFontAtlas fontAtlas = io.getFonts();
		final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

		// Glyphs could be added per-font as well as per config used globally like here
		fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());

		// Add a default font, which is 'ProggyClean.ttf, 13px'
		//fontAtlas.addFontDefault();

		// Fonts merge example
		//fontConfig.setMergeMode(true); // When enabled, all fonts added with this config would be merged with the
										// previously added font
		fontConfig.setPixelSnapH(true);

		fontAtlas.addFontFromFileTTF("./src/main/resources/assets/fonts/AGENCYB.TTF", 24, fontConfig);

		//fontConfig.setMergeMode(false);
		//fontConfig.setPixelSnapH(false);

		fontConfig.destroy(); // After all fonts were added we don't need this config more

		// ------------------------------------------------------------
		// Use freetype instead of stb_truetype to build a fonts texture
		ImGuiFreeType.buildFontAtlas(fontAtlas, ImGuiFreeType.RasterizerFlags.LightHinting);

		// When viewports are enabled we tweak WindowRounding/WindowBg so platform
		// windows can look identical to regular ones.
		if (io.hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
			final ImGuiStyle style = ImGui.getStyle();
			style.setWindowRounding(0.0f);
			style.setColor(ImGuiCol.WindowBg, ImGui.getColorU32(ImGuiCol.WindowBg, 1));
		}
	}

	private void endFrame() {
		// After Dear ImGui prepared a draw data, we use it in the LWJGL3 renderer.
		// At that moment ImGui will be rendered to the current OpenGL context.
		// imGuiGl3.renderDrawData(ImGui.getDrawData());
		imGuiGl3.renderDrawData(ImGui.getDrawData());

		if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
			ImGui.updatePlatformWindows();
			ImGui.renderPlatformWindowsDefault();
		}

	}

	public void update(float delta, Scene currentScene) {

		// Any Dear ImGui code SHOULD go between ImGui.newFrame()/ImGui.render() methods
		// imGuiGlfw.newFrame();

		imGuiGlfw.newFrame();
		ImGui.newFrame();
		currentScene.sceneImgui();
		ImGui.showDemoWindow();
		ImGui.render();

		endFrame();

	}

	public void destoryImGui() {

		// You should clean up after yourself in reverse order.
		imGuiGl3.dispose();
		imGuiGlfw.dispose();

		ImGui.destroyContext();
	}
}
