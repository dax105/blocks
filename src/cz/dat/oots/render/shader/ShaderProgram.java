package cz.dat.oots.render.shader;

import org.lwjgl.opengl.*;
import org.newdawn.slick.util.ResourceLoader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ShaderProgram {

    private int progID = 0, vertID = 0, fragID = 0;
    private Map<String, Integer> uniforms;

    public ShaderProgram(String path) {
        System.out.println("Loading shader \"" + path + "\"");

        String vert = readShader(path + ".vsh");
        String frag = readShader(path + ".fsh");

        this.vertID = compileShader(vert, ARBVertexShader.GL_VERTEX_SHADER_ARB);
        this.fragID = compileShader(frag, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
        this.progID = attachShaders(this.vertID, this.fragID);
        this.uniforms = findUniforms(this.progID);
    }

    private static String readShader(String path) {
        InputStream in = ResourceLoader.getResourceAsStream(path);

        StringBuilder source = new StringBuilder();
        BufferedReader reader;

        try {
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                source.append(line).append("\n");
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return source.toString();
    }

    private static int compileShader(String shader, int type) {
        int id = 0;
        try {
            id = ARBShaderObjects.glCreateShaderObjectARB(type);
            if (id == 0) {
                return 0;
            }

            ARBShaderObjects.glShaderSourceARB(id, shader);
            ARBShaderObjects.glCompileShaderARB(id);

            if (ARBShaderObjects.glGetObjectParameteriARB(id,
                    ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
                throw new RuntimeException("Error creating shader: "
                        + getLogInfo(id));

            return id;
        } catch (Exception e) {
            ARBShaderObjects.glDeleteObjectARB(id);
            e.printStackTrace();
            return 0;
        }
    }

    private static int attachShaders(int vert, int frag) {
        int prog = ARBShaderObjects.glCreateProgramObjectARB();

        try {
            ARBShaderObjects.glAttachObjectARB(prog, vert);
            ARBShaderObjects.glAttachObjectARB(prog, frag);

            ARBShaderObjects.glLinkProgramARB(prog);
            if (ARBShaderObjects.glGetObjectParameteriARB(prog,
                    ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
                System.err.println(getLogInfo(prog));
                throw new RuntimeException("Error attaching shader: "
                        + getLogInfo(prog));
            }

            ARBShaderObjects.glValidateProgramARB(prog);
            if (ARBShaderObjects.glGetObjectParameteriARB(prog,
                    ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
                System.err.println(getLogInfo(prog));
                throw new RuntimeException("Error attaching shader: "
                        + getLogInfo(prog));
            }
        } catch (Exception e) {
            ARBShaderObjects.glDeleteObjectARB(prog);
            e.printStackTrace();
        }

        return prog;
    }

    private static Map<String, Integer> findUniforms(int prog) {
        int activeUniforms = GL20.glGetProgrami(prog, GL20.GL_ACTIVE_UNIFORMS);

        Map<String, Integer> uniforms = new HashMap<String, Integer>();

        for (int i = 0; i < activeUniforms; i++) {
            String s = ARBShaderObjects.glGetActiveUniformARB(prog, i, 256);

            System.out.println(s);

            if (!s.startsWith("gl_") && !s.startsWith("sampler_")) {
                uniforms.put(s, i);
            }
        }

        return uniforms;
    }

    private static String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects
                .glGetObjectParameteriARB(obj,
                        ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }

    public int getProgramID() {
        return this.progID;
    }

    public void setUniform1f(String name, float val1) {
        if (checkUniformPresent(name)) {
            ARBShaderObjects.glUniform1fARB(uniforms.get(name), val1);
        }
    }

    public void setUniform2f(String name, float val1, float val2) {
        if (checkUniformPresent(name)) {
            ARBShaderObjects.glUniform2fARB(uniforms.get(name), val1, val2);
        }
    }

    public void setUniform3f(String name, float val1, float val2, float val3) {
        if (checkUniformPresent(name)) {
            ARBShaderObjects.glUniform3fARB(uniforms.get(name), val1, val2, val3);
        }
    }

    public void setUniform4f(String name, float val1, float val2, float val3, float val4) {
        if (checkUniformPresent(name)) {
            ARBShaderObjects.glUniform4fARB(uniforms.get(name), val1, val2, val3, val4);
        }
    }

    public void setUniform1i(String name, int val1) {
        if (checkUniformPresent(name)) {
            ARBShaderObjects.glUniform1iARB(uniforms.get(name), val1);
        }
    }

    public void setUniform2i(String name, int val1, int val2) {
        if (checkUniformPresent(name)) {
            ARBShaderObjects.glUniform2iARB(uniforms.get(name), val1, val2);
        }
    }

    public void setUniform3i(String name, int val1, int val2, int val3) {
        if (checkUniformPresent(name)) {
            ARBShaderObjects.glUniform3iARB(uniforms.get(name), val1, val2, val3);
        }
    }

    public void setUniform4i(String name, int val1, int val2, int val3, int val4) {
        if (checkUniformPresent(name)) {
            ARBShaderObjects.glUniform4iARB(uniforms.get(name), val1, val2, val3, val4);
        }
    }

    private boolean checkUniformPresent(String name) {
        return this.uniforms.containsKey(name);
    }

}
