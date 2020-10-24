package client.Snake.Renderer.Points;

public class PointsAdd implements PointsStrategy {

    @Override
    public int Calculate(int a, int b) {
        return a + b;
    }
}