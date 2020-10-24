package client.Snake.Renderer.Command;

import java.io.OutputStreamWriter;

public interface ICommand {
    void execute(String id, OutputStreamWriter out);
    void undo(String id, OutputStreamWriter out);
}
