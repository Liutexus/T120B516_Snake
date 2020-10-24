package client.Snake.Renderer.Points;

public class PointsLoose implements PointsStrategy{

    @Override
    public int Calculate(int a, int b) {
        return a - b;
    }
}
