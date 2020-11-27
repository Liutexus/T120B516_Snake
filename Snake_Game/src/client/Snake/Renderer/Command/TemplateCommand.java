package client.Snake.Renderer.Command;

import java.io.OutputStreamWriter;

public abstract class TemplateCommand {

    abstract void undo(String id, OutputStreamWriter out);
    abstract String getString();

    //template method
    public final void command(String id, OutputStreamWriter out){

        undo(id, out);
        getString();
    }
}
