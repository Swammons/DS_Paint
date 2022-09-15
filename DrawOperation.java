public interface DrawOperation {
    void draw(GraphicsContext gc);
}

public class DrawBoard {
    private final List<DrawOperation> operations = new ArrayList<>();
    private final GraphicsContext gc;
    private int historyIndex = -1;

    public DrawBoard(GraphicsContext gc) {
        this.gc = gc;
    }

    public void redraw() {
        Canvas c = gc.getCanvas();
        gc.clearRect(0, 0, c.getWidth(), c.getHeight());
        for (int i = 0; i <= historyIndex; i++) {
            operations.get(i).draw(gc);
        }
    }

    public void addDrawOperation(DrawOperation op) {
        // clear history after current postion
        operations.subList(historyIndex+1, operations.size()).clear();

        // add new operation
        operations.add(op);
        historyIndex++;
        op.draw(gc);
    }

    public void undo() {
        if (historyIndex >= 0) {
            historyIndex--;
            redraw();
        }
    }

    public void redo() {
        if (historyIndex < operations.size()-1) {
            historyIndex++;
            operations.get(historyIndex).draw(gc);
        }
    }
}
